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

import org.apache.camel.Category;
import org.apache.camel.Consumer;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.camel.spi.Metadata;
import org.apache.camel.spi.UriEndpoint;
import org.apache.camel.spi.UriParam;
import org.apache.camel.spi.UriPath;
import org.apache.camel.support.ScheduledPollEndpoint;

/**
 * Capture video frames using JavaCV FrameGrabbers.
 */
@UriEndpoint(firstVersion = "4.21.0", scheme = "javacv", title = "JavaCV",
             syntax = "javacv:source", remote = false, category = { Category.AI },
             consumerOnly = true, headersClass = JavaCVConstants.class)
public class JavaCVEndpoint extends ScheduledPollEndpoint {

    @UriPath
    @Metadata(required = true, description = "The source to grab frames from (device number, file path, or URL)")
    private String source;

    @UriParam(defaultValue = "FFMPEG", description = "The type of FrameGrabber to use")
    private GrabberType grabberType = GrabberType.FFMPEG;

    @UriParam(description = "Requested image width in pixels")
    private int imageWidth;

    @UriParam(description = "Requested image height in pixels")
    private int imageHeight;

    @UriParam(description = "Requested frame rate (frames per second)")
    private double frameRate;

    @UriParam(description = "Pixel format (e.g., BGR24, RGB24)")
    private String format;

    public JavaCVEndpoint(String uri, JavaCVComponent component, String source) {
        super(uri, component);
        this.source = source;
    }

    @Override
    public boolean isRemote() {
        return false;
    }

    @Override
    public Producer createProducer() throws Exception {
        throw new UnsupportedOperationException("Producer not supported");
    }

    @Override
    public Consumer createConsumer(Processor processor) throws Exception {
        JavaCVConsumer consumer = new JavaCVConsumer(this, processor);
        configureConsumer(consumer);
        return consumer;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public GrabberType getGrabberType() {
        return grabberType;
    }

    public void setGrabberType(GrabberType grabberType) {
        this.grabberType = grabberType;
    }

    public int getImageWidth() {
        return imageWidth;
    }

    public void setImageWidth(int imageWidth) {
        this.imageWidth = imageWidth;
    }

    public int getImageHeight() {
        return imageHeight;
    }

    public void setImageHeight(int imageHeight) {
        this.imageHeight = imageHeight;
    }

    public double getFrameRate() {
        return frameRate;
    }

    public void setFrameRate(double frameRate) {
        this.frameRate = frameRate;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }
}
