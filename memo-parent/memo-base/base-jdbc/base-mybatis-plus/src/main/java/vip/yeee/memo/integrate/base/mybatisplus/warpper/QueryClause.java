package vip.yeee.memo.integrate.base.mybatisplus.warpper;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author yeee丶一页 (quhailong1995@gmail.com)
 */
@Data
public class QueryClause implements Serializable {
    private static final long serialVersionUID = -6014076206210293128L;
    
    private List<WhereClause> w;
    private List<OrderClause> o;
    private PageClause p;
    
}
