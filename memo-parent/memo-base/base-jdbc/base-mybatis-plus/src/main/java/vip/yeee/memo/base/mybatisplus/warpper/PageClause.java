package vip.yeee.memo.base.mybatisplus.warpper;

import lombok.Data;

import java.io.Serializable;

/**
 * @author yeee丶一页 (quhailong1995@gmail.com)
 */
@Data
public class PageClause implements Serializable {
    private static final long serialVersionUID = -3083543848592552639L;
    
    /**
     * 页码
     */
    private int n;
    /**
     * 每页条数
     */
    private int s;
    
    /**
     * 是否查询条数 0：是  1：否
     */
    private int c = 0;
    
    public PageClause() {
        this.n = 1;
        this.s = 10;
    }
    
    public PageClause(int pageNum, int pageSize) {
        this.n = pageNum;
        this.s = pageSize;
    }

}
