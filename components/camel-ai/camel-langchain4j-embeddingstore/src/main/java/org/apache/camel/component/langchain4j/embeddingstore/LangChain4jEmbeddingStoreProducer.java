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

import java.util.List;
import java.util.concurrent.ExecutorService;

import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.store.embedding.filter.Filter;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.store.embedding.EmbeddingMatch;
import dev.langchain4j.store.embedding.EmbeddingSearchRequest;
import dev.langchain4j.store.embedding.EmbeddingSearchRequest.EmbeddingSearchRequestBuilder;

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
        Embedding embedding = in.getMandatoryBody(Embedding.class);

        String id = getEndpoint().getConfiguration().getEmbeddingStore().add(embedding);
        Message out = exchange.getMessage();
        out.setBody(id);
    }

    private void remove(Exchange exchange) throws Exception {
        final Message in = exchange.getMessage();
        String id = in.getMandatoryBody(String.class);

        getEndpoint().getConfiguration().getEmbeddingStore().remove(id);

        Message out = exchange.getMessage();
        out.setBody(null);

    }

    private void search(Exchange exchange) throws Exception {
        final Message in = exchange.getMessage();
        Embedding embedding = in.getMandatoryBody(Embedding.class);

        int maxResults = Integer.parseInt(LangChain4jEmbeddingStore.DEFAULT_MAX_RESULTS);
        if (in.getHeader(LangChain4jEmbeddingStore.Headers.MAX_RESULTS, Integer.class) != null) {
            maxResults = in.getHeader(LangChain4jEmbeddingStore.Headers.MAX_RESULTS, Integer.class);
        }

        EmbeddingSearchRequestBuilder esrb = EmbeddingSearchRequest.builder()
                .queryEmbedding(embedding)
                .maxResults(maxResults);
        
        if (in.getHeader(LangChain4jEmbeddingStore.Headers.MAX_RESULTS, Integer.class) != null) {
            Double minScore = in.getHeader(LangChain4jEmbeddingStore.Headers.MAX_RESULTS, Double.class);
            esrb = esrb.minScore(minScore);
        }    

        if (in.getHeader(LangChain4jEmbeddingStore.Headers.MAX_RESULTS, Filter.class) != null) {
            Filter filter = in.getHeader(LangChain4jEmbeddingStore.Headers.FILTER, Filter.class);
            esrb = esrb.filter(filter);
        }
        EmbeddingSearchRequest embeddingSearchRequest = esrb.build();

        List<EmbeddingMatch<TextSegment>> result
                = getEndpoint().getConfiguration().getEmbeddingStore().search(embeddingSearchRequest).matches();
        Message out = exchange.getMessage();
        out.setBody(result);
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
