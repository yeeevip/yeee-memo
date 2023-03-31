package vip.yeee.memo.integrate.ai.chatgpt.params.edit.constant;

public enum EditModelEnum {
    // 文本处理模型
    TEXT(0,"text-davinci-edit-001"),
    // 代码处理类型
    CODE(1,"code-davinci-edit-001");

    private int code;
    private String model;

    EditModelEnum(int code, String model) {
        this.code = code;
        this.model = model;
    }

    public int getCode() {
        return code;
    }

    public String getModel() {
        return model;
    }
}
