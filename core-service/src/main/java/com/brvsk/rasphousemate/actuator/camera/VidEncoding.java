package com.brvsk.rasphousemate.actuator.camera;

public enum VidEncoding {
    H264("h264"),
    MJPEG("mjpeg"),
    YUV420("yuv420");

    private final String encoding;

    VidEncoding(String encoding) {
        this.encoding = encoding;
    }

    public String getEncoding() {
        return encoding;
    }
}