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
package org.apache.camel.reifier;

import org.apache.camel.Expression;
import org.apache.camel.Predicate;
import org.apache.camel.Processor;
import org.apache.camel.Route;
import org.apache.camel.model.LoopDefinition;
import org.apache.camel.model.ProcessorDefinition;
import org.apache.camel.processor.LoopProcessor;

public class LoopReifier extends ExpressionReifier<LoopDefinition> {

    public LoopReifier(Route route, ProcessorDefinition<?> definition) {
        super(route, (LoopDefinition) definition);
    }

    @Override
    public Processor createProcessor() throws Exception {
        Processor output = this.createChildProcessor(true);
        boolean isCopy = parseBoolean(definition.getCopy(), false);
        boolean isWhile = parseBoolean(definition.getDoWhile(), false);
        boolean isBreakOnShutdown = parseBoolean(definition.getBreakOnShutdown(), false);
        Processor prepare = definition.getOnPrepareProcessor();
        if (prepare == null && definition.getOnPrepare() != null) {
            prepare = mandatoryLookup(definition.getOnPrepare(), Processor.class);
        }

        Predicate predicate = null;
        Expression expression = null;
        if (isWhile) {
            predicate = createPredicate(definition.getExpression());
        } else {
            expression = createExpression(definition.getExpression());
        }
        return new LoopProcessor(camelContext, output, expression, predicate, prepare, isCopy, isBreakOnShutdown);
    }

}
