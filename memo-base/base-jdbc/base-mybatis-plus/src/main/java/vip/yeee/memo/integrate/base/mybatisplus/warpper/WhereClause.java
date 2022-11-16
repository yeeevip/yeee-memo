package vip.yeee.memo.integrate.base.mybatisplus.warpper;

import lombok.Data;

import java.io.Serializable;

/**
 * @author yeah丶一页 (quhailong1995@gmail.com)
 */
@Data
public class WhereClause implements Serializable {
    private static final long serialVersionUID = 5312268117297358992L;
    
    private String k; //key
    private Object v; //val
    private String m; //model
    
}
