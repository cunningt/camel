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
package org.apache.camel.component.langchain4j.embeddingstore;

import java.util.concurrent.ExecutorService;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.NoSuchHeaderException;
import org.apache.camel.support.DefaultProducer;

public class LangChain4jEmbeddingStoreProducer extends DefaultProducer {
    private ExecutorService executor;

    public LangChain4jEmbeddingStoreProducer(LangChain4jEmbeddingStoreEndpoint endpoint) {
        super(endpoint);
    }

    @Override
    public LangChain4jEmbeddingStoreEndpoint getEndpoint() {
        return (LangChain4jEmbeddingStoreEndpoint) super.getEndpoint();
    }

    @Override
    public void doStart() throws Exception {
        super.doStart();

    }

    @Override
    public void process(Exchange exchange) {
        final Message in = exchange.getMessage();
        final LangChain4jEmbeddingStoreAction action
                = in.getHeader(LangChain4jEmbeddingStore.Headers.ACTION, LangChain4jEmbeddingStoreAction.class);

        try {
            if (action == null) {
                throw new NoSuchHeaderException(
                        "The action is a required header", exchange, LangChain4jEmbeddingStore.Headers.ACTION);
            }

            switch (action) {
                case ADD:
                    add(exchange);
                    break;
                case REMOVE:
                    remove(exchange);
                    break;
                case SEARCH:
                    search(exchange);
                    break;
                default:
                    throw new UnsupportedOperationException("Unsupported action: " + action.name());
            }
        } catch (Exception e) {
            exchange.setException(e);
        }
    }

    // ***************************************
    //
    // Actions
    //
    // ***************************************

    private void add(Exchange exchange) throws Exception {
        final Message in = exchange.getMessage();
    }

    private void remove(Exchange exchange) throws Exception {
        final Message in = exchange.getMessage();
    }

    private void search(Exchange exchange) throws Exception {
        final Message in = exchange.getMessage();
    }

    // ***************************************
    //
    // Helpers
    //
    // ***************************************

    private CamelContext getCamelContext() {
        return getEndpoint().getCamelContext();
    }
}
