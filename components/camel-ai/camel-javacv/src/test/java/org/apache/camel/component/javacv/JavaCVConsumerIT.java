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

import java.io.File;
import java.util.concurrent.TimeUnit;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit5.CamelTestSupport;
import org.bytedeco.javacv.Frame;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIf;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Integration test for JavaCV consumer. Requires a test video file.
 */
class JavaCVConsumerIT extends CamelTestSupport {

    private static final String TEST_VIDEO = "src/test/resources/test-video.mp4";

    static boolean testVideoExists() {
        return new File(TEST_VIDEO).exists();
    }

    @Test
    @EnabledIf("testVideoExists")
    void testConsumeVideoFile() throws Exception {
        MockEndpoint mock = getMockEndpoint("mock:result");
        mock.expectedMinimumMessageCount(5);
        mock.await(30, TimeUnit.SECONDS);

        mock.assertIsSatisfied();

        var exchanges = mock.getExchanges();
        assertNotNull(exchanges);
        assertTrue(exchanges.size() >= 5);

        var firstExchange = exchanges.get(0);
        var frame = firstExchange.getIn().getBody(Frame.class);
        assertNotNull(frame);

        Long frameNumber = firstExchange.getIn().getHeader(JavaCVConstants.FRAME_NUMBER, Long.class);
        assertNotNull(frameNumber);
        assertTrue(frameNumber > 0);
    }

    @Override
    protected RouteBuilder createRouteBuilder() {
        return new RouteBuilder() {
            @Override
            public void configure() {
                from("javacv:" + TEST_VIDEO + "?delay=100")
                        .to("mock:result");
            }
        };
    }
}
