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

import org.bytedeco.javacv.DC1394FrameGrabber;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.FlyCapture2FrameGrabber;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.IPCameraFrameGrabber;
import org.bytedeco.javacv.OpenCVFrameGrabber;
import org.bytedeco.javacv.OpenKinect2FrameGrabber;
import org.bytedeco.javacv.OpenKinectFrameGrabber;
import org.bytedeco.javacv.PS3EyeFrameGrabber;
import org.bytedeco.javacv.RealSense2FrameGrabber;
import org.bytedeco.javacv.RealSenseFrameGrabber;
import org.bytedeco.javacv.VideoInputFrameGrabber;

/**
 * Supported FrameGrabber types for the JavaCV component.
 */
public enum GrabberType {

    FFMPEG("ffmpeg") {
        @Override
        public FrameGrabber createGrabber(String source) {
            return new FFmpegFrameGrabber(source);
        }
    },

    OPENCV("opencv") {
        @Override
        public FrameGrabber createGrabber(String source) {
            try {
                int deviceNumber = Integer.parseInt(source);
                return new OpenCVFrameGrabber(deviceNumber);
            } catch (NumberFormatException e) {
                return new OpenCVFrameGrabber(source);
            }
        }
    },

    IPCAMERA("ipcamera") {
        @Override
        public FrameGrabber createGrabber(String source) {
            return new IPCameraFrameGrabber(source);
        }
    },

    VIDEOINPUT("videoinput") {
        @Override
        public FrameGrabber createGrabber(String source) {
            int deviceNumber = Integer.parseInt(source);
            return new VideoInputFrameGrabber(deviceNumber);
        }
    },

    DC1394("dc1394") {
        @Override
        public FrameGrabber createGrabber(String source) throws FrameGrabber.Exception {
            int deviceNumber = Integer.parseInt(source);
            return new DC1394FrameGrabber(deviceNumber);
        }
    },

    FLYCAPTURE2("flycapture2") {
        @Override
        public FrameGrabber createGrabber(String source) throws FrameGrabber.Exception {
            int deviceNumber = Integer.parseInt(source);
            return new FlyCapture2FrameGrabber(deviceNumber);
        }
    },

    OPENKINECT("openkinect") {
        @Override
        public FrameGrabber createGrabber(String source) throws FrameGrabber.Exception {
            int deviceNumber = Integer.parseInt(source);
            return new OpenKinectFrameGrabber(deviceNumber);
        }
    },

    OPENKINECT2("openkinect2") {
        @Override
        public FrameGrabber createGrabber(String source) throws FrameGrabber.Exception {
            int deviceNumber = Integer.parseInt(source);
            return new OpenKinect2FrameGrabber(deviceNumber);
        }
    },

    REALSENSE("realsense") {
        @Override
        public FrameGrabber createGrabber(String source) throws FrameGrabber.Exception {
            int deviceNumber = Integer.parseInt(source);
            return new RealSenseFrameGrabber(deviceNumber);
        }
    },

    REALSENSE2("realsense2") {
        @Override
        public FrameGrabber createGrabber(String source) throws FrameGrabber.Exception {
            int deviceNumber = Integer.parseInt(source);
            return new RealSense2FrameGrabber(deviceNumber);
        }
    },

    PS3EYE("ps3eye") {
        @Override
        public FrameGrabber createGrabber(String source) throws FrameGrabber.Exception {
            int deviceNumber = Integer.parseInt(source);
            return new PS3EyeFrameGrabber(deviceNumber);
        }
    };

    private final String value;

    GrabberType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public abstract FrameGrabber createGrabber(String source) throws FrameGrabber.Exception;

    public static GrabberType fromValue(String value) {
        for (GrabberType type : values()) {
            if (type.value.equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown grabber type: " + value);
    }
}
