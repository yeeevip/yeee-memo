package vip.yeee.memo.base.mybatisplus.warpper;

import lombok.Data;

import java.io.Serializable;

/**
 * @author yeee丶一页 (https://www.yeee.vip)
 */
@Data
public class WhereClause implements Serializable {
    private static final long serialVersionUID = 5312268117297358992L;
    
    private String k; //key
    private Object v; //val
    private String m; //model
    
}
