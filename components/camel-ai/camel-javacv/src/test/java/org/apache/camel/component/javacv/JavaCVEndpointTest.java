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

import org.apache.camel.test.junit5.CamelTestSupport;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class JavaCVEndpointTest extends CamelTestSupport {

    @Test
    void testEndpointConfiguration() throws Exception {
        JavaCVEndpoint endpoint = context.getEndpoint(
                "javacv:0?grabberType=OPENCV&imageWidth=640&imageHeight=480&frameRate=30.0",
                JavaCVEndpoint.class);

        assertNotNull(endpoint);
        assertEquals("0", endpoint.getSource());
        assertEquals(GrabberType.OPENCV, endpoint.getGrabberType());
        assertEquals(640, endpoint.getImageWidth());
        assertEquals(480, endpoint.getImageHeight());
        assertEquals(30.0, endpoint.getFrameRate(), 0.01);
    }

    @Test
    void testEndpointDefaultGrabberType() throws Exception {
        JavaCVEndpoint endpoint = context.getEndpoint("javacv:/path/to/video.mp4", JavaCVEndpoint.class);

        assertNotNull(endpoint);
        assertEquals("/path/to/video.mp4", endpoint.getSource());
        assertEquals(GrabberType.FFMPEG, endpoint.getGrabberType());
    }

    @Test
    void testEndpointWithIPCamera() throws Exception {
        JavaCVEndpoint endpoint = context.getEndpoint(
                "javacv:http://camera/stream?grabberType=IPCAMERA",
                JavaCVEndpoint.class);

        assertNotNull(endpoint);
        assertEquals("http://camera/stream", endpoint.getSource());
        assertEquals(GrabberType.IPCAMERA, endpoint.getGrabberType());
    }

    @Test
    void testEndpointWithFormat() throws Exception {
        JavaCVEndpoint endpoint = context.getEndpoint(
                "javacv:0?format=BGR24",
                JavaCVEndpoint.class);

        assertNotNull(endpoint);
        assertEquals("BGR24", endpoint.getFormat());
    }

    @Test
    void testEndpointProducerNotSupported() throws Exception {
        JavaCVEndpoint endpoint = context.getEndpoint("javacv:0", JavaCVEndpoint.class);

        assertThrows(UnsupportedOperationException.class, endpoint::createProducer);
    }

    @Test
    void testEndpointWithoutSourceThrows() {
        assertThrows(Exception.class, () -> context.getEndpoint("javacv:", JavaCVEndpoint.class));
    }
}
