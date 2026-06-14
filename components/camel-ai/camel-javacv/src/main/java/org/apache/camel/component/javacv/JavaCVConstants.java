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

import org.apache.camel.spi.Metadata;

/**
 * Constants for JavaCV component headers.
 */
public final class JavaCVConstants {

    @Metadata(description = "The frame timestamp in microseconds", javaType = "long")
    public static final String FRAME_TIMESTAMP = "CamelJavaCVFrameTimestamp";

    @Metadata(description = "The frame number (sequential counter)", javaType = "long")
    public static final String FRAME_NUMBER = "CamelJavaCVFrameNumber";

    @Metadata(description = "Whether the frame is a key frame", javaType = "boolean")
    public static final String FRAME_KEY_FRAME = "CamelJavaCVKeyFrame";

    @Metadata(description = "The image width in pixels", javaType = "int")
    public static final String FRAME_WIDTH = "CamelJavaCVWidth";

    @Metadata(description = "The image height in pixels", javaType = "int")
    public static final String FRAME_HEIGHT = "CamelJavaCVHeight";

    private JavaCVConstants() {
    }
}
