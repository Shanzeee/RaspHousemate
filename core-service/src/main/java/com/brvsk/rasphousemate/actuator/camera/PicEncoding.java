package com.brvsk.rasphousemate.actuator.camera;

public enum PicEncoding {
    PNG("png"),
    JPG("jpg"),
    RGB("rgb"),
    BMP("bmp"),
    YUV420("yuv420");

    private final String encoding;

    PicEncoding(String encoding) {
        this.encoding = encoding;
    }

    public String getEncoding() {
        return encoding;
    }
}
