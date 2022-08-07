package vip.yeee.memo.integrate.common.mybatisplus.warpper;

import lombok.Data;

import java.io.Serializable;

/**
 * @author yeah丶一页 (quhailong1995@gmail.com)
 */
@Data
public class OrderClause implements Serializable {
    private static final long serialVersionUID = -7864088519623641693L;
    
    private String k; //key
    private String t; //type
    
}
