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

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class GrabberTypeTest {

    @Test
    void testFromValue() {
        assertEquals(GrabberType.FFMPEG, GrabberType.fromValue("ffmpeg"));
        assertEquals(GrabberType.OPENCV, GrabberType.fromValue("opencv"));
        assertEquals(GrabberType.IPCAMERA, GrabberType.fromValue("ipcamera"));
        assertEquals(GrabberType.VIDEOINPUT, GrabberType.fromValue("videoinput"));
        assertEquals(GrabberType.DC1394, GrabberType.fromValue("dc1394"));
        assertEquals(GrabberType.FLYCAPTURE2, GrabberType.fromValue("flycapture2"));
        assertEquals(GrabberType.OPENKINECT, GrabberType.fromValue("openkinect"));
        assertEquals(GrabberType.OPENKINECT2, GrabberType.fromValue("openkinect2"));
        assertEquals(GrabberType.REALSENSE, GrabberType.fromValue("realsense"));
        assertEquals(GrabberType.REALSENSE2, GrabberType.fromValue("realsense2"));
        assertEquals(GrabberType.PS3EYE, GrabberType.fromValue("ps3eye"));
    }

    @Test
    void testFromValueCaseInsensitive() {
        assertEquals(GrabberType.FFMPEG, GrabberType.fromValue("FFMPEG"));
        assertEquals(GrabberType.FFMPEG, GrabberType.fromValue("FFmpeg"));
        assertEquals(GrabberType.OPENCV, GrabberType.fromValue("OpenCV"));
    }

    @Test
    void testFromValueInvalid() {
        assertThrows(IllegalArgumentException.class, () -> GrabberType.fromValue("invalid"));
    }

    @Test
    void testGetValue() {
        assertEquals("ffmpeg", GrabberType.FFMPEG.getValue());
        assertEquals("opencv", GrabberType.OPENCV.getValue());
        assertEquals("ipcamera", GrabberType.IPCAMERA.getValue());
    }

    @Test
    void testCreateGrabberFFmpeg() throws Exception {
        var grabber = GrabberType.FFMPEG.createGrabber("/dev/null");
        assertNotNull(grabber);
    }

    @Test
    void testCreateGrabberOpenCVWithDeviceNumber() throws Exception {
        var grabber = GrabberType.OPENCV.createGrabber("0");
        assertNotNull(grabber);
    }

    @Test
    void testCreateGrabberOpenCVWithPath() throws Exception {
        var grabber = GrabberType.OPENCV.createGrabber("/dev/video0");
        assertNotNull(grabber);
    }

    @Test
    void testCreateGrabberIPCamera() throws Exception {
        var grabber = GrabberType.IPCAMERA.createGrabber("http://example.com/stream");
        assertNotNull(grabber);
    }
}
