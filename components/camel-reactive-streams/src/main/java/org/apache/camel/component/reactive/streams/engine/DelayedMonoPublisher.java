/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.camel.component.reactive.streams.engine;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

/**
 * Publish a single item as soon as it's available.
 */
public class DelayedMonoPublisher<T> implements Publisher<T> {
    private final ExecutorService workerPool;

    private final List<MonoSubscription> subscriptions = new CopyOnWriteArrayList<>();

    private final AtomicBoolean flushing = new AtomicBoolean();

    private volatile T data;

    private volatile Throwable exception;

    public DelayedMonoPublisher(ExecutorService workerPool) {
        this.workerPool = workerPool;
    }

    @Override
    public void subscribe(Subscriber<? super T> subscriber) {
        Objects.requireNonNull(subscriber, "subscriber must not be null");
        MonoSubscription sub = new MonoSubscription(subscriber);
        subscriptions.add(sub);
        subscriber.onSubscribe(sub);
        flushCycle();
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        Objects.requireNonNull(data, "data must be not null");
        if (this.data != null) {
            throw new IllegalStateException("data has already been set");
        } else if (this.exception != null) {
            throw new IllegalStateException("an exception has already been set");
        }

        this.data = data;
        flushCycle();
    }

    public Throwable getException() {
        return exception;
    }

    public void setException(Throwable exception) {
        Objects.requireNonNull(exception, "exception must be not null");
        if (this.data != null) {
            throw new IllegalStateException("data has already been set");
        } else if (this.exception != null) {
            throw new IllegalStateException("an exception has already been set");
        }

        this.exception = exception;
        flushCycle();
    }

    private void flushCycle() {
        boolean notRunning = flushing.compareAndSet(false, true);

        if (notRunning) {
            workerPool.execute(() -> {
                try {
                    List<MonoSubscription> completed = new LinkedList<>();
                    for (MonoSubscription sub : this.subscriptions) {
                        sub.flush();
                        if (sub.isTerminated()) {
                            completed.add(sub);
                        }
                    }
                    this.subscriptions.removeAll(completed);
                } finally {
                    flushing.set(false);
                }

                boolean runAgain = false;
                for (MonoSubscription sub : this.subscriptions) {
                    if (sub.isReady()) {
                        runAgain = true;
                        break;
                    }
                }
                if (runAgain) {
                    flushCycle();
                }

            });
        }
    }

    private final class MonoSubscription implements Subscription {

        private volatile boolean terminated;

        private volatile boolean requested;

        private final Subscriber<? super T> subscriber;
        private final Lock lock = new ReentrantLock();

        private MonoSubscription(Subscriber<? super T> subscriber) {
            this.subscriber = subscriber;
        }

        @Override
        public void request(long l) {
            lock.lock();
            try {
                if (terminated) {
                    // just ignore the request
                    return;
                }
            } finally {
                lock.unlock();
            }

            if (l <= 0) {
                subscriber.onError(new IllegalArgumentException("3.9"));
                lock.lock();
                try {
                    terminated = true;
                } finally {
                    lock.unlock();
                }
            } else {
                lock.lock();
                try {
                    requested = true;
                } finally {
                    lock.unlock();
                }
            }

            flushCycle();
        }

        public void flush() {
            lock.lock();
            try {
                if (!isReady()) {
                    return;
                }

                terminated = true;
            } finally {
                lock.unlock();
            }

            if (data != null) {
                subscriber.onNext(data);
                subscriber.onComplete();
            } else {
                subscriber.onError(exception);
            }
        }

        public boolean isTerminated() {
            return terminated;
        }

        public boolean isReady() {
            return !terminated && requested && (data != null || exception != null);
        }

        @Override
        public void cancel() {
            lock.lock();
            try {
                terminated = true;
            } finally {
                lock.unlock();
            }
        }
    }
}
