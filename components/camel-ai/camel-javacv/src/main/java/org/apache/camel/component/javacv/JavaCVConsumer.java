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
package org.apache.camel.component.javacv;

import java.util.concurrent.atomic.AtomicLong;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.apache.camel.support.ScheduledPollConsumer;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Consumer that grabs frames from a video source using JavaCV.
 */
public class JavaCVConsumer extends ScheduledPollConsumer {

    private static final Logger LOG = LoggerFactory.getLogger(JavaCVConsumer.class);

    private final JavaCVEndpoint endpoint;
    private FrameGrabber frameGrabber;
    private final AtomicLong frameCounter = new AtomicLong(0);

    public JavaCVConsumer(JavaCVEndpoint endpoint, Processor processor) {
        super(endpoint, processor);
        this.endpoint = endpoint;
    }

    @Override
    protected void doStart() throws Exception {
        super.doStart();

        try {
            frameGrabber = endpoint.getGrabberType().createGrabber(endpoint.getSource());
        } catch (FrameGrabber.Exception e) {
            throw new RuntimeException("Failed to create FrameGrabber", e);
        }

        if (endpoint.getImageWidth() > 0) {
            frameGrabber.setImageWidth(endpoint.getImageWidth());
        }
        if (endpoint.getImageHeight() > 0) {
            frameGrabber.setImageHeight(endpoint.getImageHeight());
        }
        if (endpoint.getFrameRate() > 0) {
            frameGrabber.setFrameRate(endpoint.getFrameRate());
        }
        if (endpoint.getFormat() != null) {
            frameGrabber.setFormat(endpoint.getFormat());
        }

        LOG.info("Starting FrameGrabber for source: {}", endpoint.getSource());
        frameGrabber.start();
    }

    @Override
    protected void doStop() throws Exception {
        if (frameGrabber != null) {
            LOG.info("Stopping FrameGrabber");
            frameGrabber.stop();
            frameGrabber.release();
            frameGrabber = null;
        }
        super.doStop();
    }

    @Override
    protected int poll() throws Exception {
        Frame frame = frameGrabber.grab();
        if (frame == null) {
            return 0;
        }

        Exchange exchange = createExchange(true);
        Message message = exchange.getIn();

        Frame clonedFrame = frame.clone();
        message.setBody(clonedFrame);

        long frameNumber = frameCounter.incrementAndGet();
        message.setHeader(JavaCVConstants.FRAME_TIMESTAMP, frame.timestamp);
        message.setHeader(JavaCVConstants.FRAME_NUMBER, frameNumber);
        message.setHeader(JavaCVConstants.FRAME_KEY_FRAME, frame.keyFrame);
        message.setHeader(JavaCVConstants.FRAME_WIDTH, frame.imageWidth);
        message.setHeader(JavaCVConstants.FRAME_HEIGHT, frame.imageHeight);

        try {
            getProcessor().process(exchange);
        } finally {
            releaseExchange(exchange, false);
        }

        return 1;
    }
}
