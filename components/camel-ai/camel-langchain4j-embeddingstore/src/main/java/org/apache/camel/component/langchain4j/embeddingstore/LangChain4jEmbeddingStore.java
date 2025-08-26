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

import org.apache.camel.spi.Metadata;

public class LangChain4jEmbeddingStore {
    public static final String SCHEME = "langchain4j-embeddingstore";
    public static final int DEFAULT_COLLECTION_DIMENSION = 384;
    public static final String DEFAULT_MAX_RESULTS = "5";

    private LangChain4jEmbeddingStore() {
    }

    public static class Headers {
        @Metadata(description = "The action to be performed.", javaType = "String",
                  enums = "ADD,REMOVE,SEARCH")
        public static final String ACTION = "CamelLangchain4jEmbeddingStoreAction";

        @Metadata(description = "MaxResults", javaType = "Integer", defaultValue = DEFAULT_MAX_RESULTS)
        public static final String MAX_RESULTS = "CamelLangchain4jEmbeddingStoreMaxResults";

        @Metadata(description = "MaxResults", javaType = "Integer")
        public static final String MIN_SCORE = "CamelLangchain4jEmbeddingStoreMinScore";

        @Metadata(description = "Filter", javaType = "dev.langchain4j.store.embedding.filter.Filter")
        public static final String FILTER = "CamelLangchain4jEmbeddingStoreFilter";

    }
}
