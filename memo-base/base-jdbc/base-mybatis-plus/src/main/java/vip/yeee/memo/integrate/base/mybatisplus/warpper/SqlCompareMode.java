package vip.yeee.memo.integrate.base.mybatisplus.warpper;

/**
 * @author yeah丶一页 (quhailong1995@gmail.com)
 */
public enum SqlCompareMode {

    EQ(" = ", "等于"), 
    NE(" <> ", "不等于"), 
    LK(" like ", "包含"), 
    LLK(" like ", "左包含"), 
    RLK(" like ", "右包含"), 
    NLK(" not like ", "不包含"), 
    IN(" in ", "在..中"), 
    NIN(" not in ", "不在..中"),
    LT(" < ", "小于"), 
    LE(" <= ", "小于等于"), 
    GT(" > ", "大于"), 
    GE(" >= ", "大于等于"), 
    BT(" between ", "位于..和..之间"),
    IS(" is ", "是"),
    NIS(" is not ", "不是"),
    OR(" or ", "或者"),
    ASC(" asc ", "升序"),
    DESC(" desc ", "降序"),
    ;

    /**
     * 运算符号
     */
    private final String symbol;
    /**
     * 描述
     */
    private final String label;

    SqlCompareMode(String symbol, String label) {
        this.symbol = symbol;
        this.label = label;
    }

    public static SqlCompareMode get(String symbol, SqlCompareMode defaultModel) {
        try {
            return SqlCompareMode.valueOf(symbol.trim().toUpperCase());
        } catch (Exception e) {
            return defaultModel;
        }
    }
    
    public String symbol() {
        return symbol;
    }

    /**
     * 返回描述
     * 
     * @return
     */
    public String getLabel() {
        return label;
    }
    
}
