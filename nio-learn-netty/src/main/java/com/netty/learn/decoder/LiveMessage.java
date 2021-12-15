package com.netty.learn.decoder;

/**
 * @Description:
 * @Author: yeeeeee
 * @Date: 2021/12/14 18:24
 */
public class LiveMessage {

    private Integer length;

    private Integer type;

    private String content;

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "LiveMessage{" +
                "length=" + length +
                ", type=" + type +
                ", content='" + content + '\'' +
                '}';
    }
}