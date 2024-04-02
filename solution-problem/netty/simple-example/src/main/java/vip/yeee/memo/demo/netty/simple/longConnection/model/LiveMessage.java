package vip.yeee.memo.demo.netty.simple.longConnection.model;

/**
 *
 * @author https://www.yeee.vip
 * @since 2021/12/14 18:24
 */
public class LiveMessage {

    private Integer length;

    private byte type;

    private String content;

    public final static byte TYPE_HEART = 1;
    public final static byte TYPE_MESSAGE = 2;

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public Byte getType() {
        return type;
    }

    public void setType(Byte type) {
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