package vip.yeee.memo.integrate.base.mybatisplus.warpper;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 生成查询类
 * </p>
 * @author yeee丶一页 (quhailong1995@gmail.com)
 */
public class MyPageWrapper<T> {

    private final QueryClause clause;
    private final Map<String, Object> wkv;

    public MyPageWrapper(String query) {
        this.clause = JSON.parseObject(query, QueryClause.class);
        wkv = this.clause.getW().stream().collect(Collectors.toMap(WhereClause::getK, WhereClause::getV));
    }

    public Object getQueryValue(String key) {
        return wkv.get(key);
    }

    public IPage<T> getPage() {
        PageClause pageClause = this.clause.getP();
        Page<T> page = new Page<>();
        if (null != pageClause) {
            page.setCurrent(pageClause.getN());
            page.setSize(pageClause.getS());
            page.setSearchCount(pageClause.getC() == 0);
        }
        return page;
    }

    public QueryWrapper<T> getQueryWrapper() {
        QueryWrapper<T> wrapper = new QueryWrapper<>();
        QueryClause queryClause = this.clause;
        whereHandle(wrapper, queryClause.getW());
        orderHandle(wrapper, queryClause.getO());
        return wrapper;
    }

    public LambdaQueryWrapper<T> getLambdaQueryWrapper() {
        return getQueryWrapper().lambda();
    }
    
    private void orderHandle(QueryWrapper<T> wrapper, List<OrderClause> orderList) {
        if (CollectionUtils.isNotEmpty(orderList)) {
            for (OrderClause orderClause : orderList) {
                //String column = getColumn(queryMapper, orderClause.getK());
                String column = StrUtil.toUnderlineCase(orderClause.getK());
                SqlCompareMode mode = SqlCompareMode.get(orderClause.getT(), SqlCompareMode.ASC);
                if (mode == SqlCompareMode.ASC) {
                    wrapper.orderByAsc(column);
                } else {
                    wrapper.orderByDesc(column);
                }
            }
        }
    }
    
    private void whereHandle(QueryWrapper<T> wrapper, List<WhereClause> whereList) {
        if (CollectionUtils.isEmpty(whereList)) {
            return;
        }
        for (WhereClause whereClause : whereList) {
            //String column = getColumn(queryMapper, whereClause.getK());
            String column = StrUtil.toUnderlineCase(whereClause.getK());

            SqlCompareMode mode = SqlCompareMode.get(whereClause.getM(), SqlCompareMode.LK);

            Object val = whereClause.getV();

            // 数值类型不允许用模糊查询(LIKE查询)，SqlCompareMode传过来是LK自动转EQ，如果想模糊查询，传过来的时候用字符串类型(加上引号)
            if (val instanceof Double && mode.name().equals(SqlCompareMode.LK.name())) {
                mode = SqlCompareMode.EQ;
            }

            switch (mode) {
                case EQ:
                    wrapper.eq(column, val);
                    break;
                case NE:
                    wrapper.ne(column, val);
                    break;
                case LT:
                    wrapper.lt(column, val);
                    break;
                case LE:
                    wrapper.le(column, val);
                    break;
                case GT:
                    wrapper.gt(column, val);
                    break;
                case GE:
                    wrapper.ge(column, val);
                    break;
                case LK:
                    wrapper.like(column, val);
                    break;
                case LLK:
                    wrapper.likeLeft(column, val);
                    break;
                case RLK:
                    wrapper.likeRight(column, val);
                    break;
                case NLK:
                    wrapper.notLike(column, val);
                    break;
                case IN:
                    if (val instanceof Collection) {
                        wrapper.in(column,  val);
                    } else {
                        wrapper.in(column, StrUtil.split(String.valueOf(val), ','));
                    }
                    break;
                case NIN:
                    if (val instanceof Collection) {
                        wrapper.notIn(column,  val);
                    } else {
                        wrapper.notIn(column, StrUtil.split(String.valueOf(val), ','));
                    }
                    break;
                case IS:
                    wrapper.isNull(column);
                    break;
                case NIS:
                    wrapper.isNotNull(column);
                    break;
                case BT:
                    if (val instanceof Collection) {
                        List<String> valArr = (List<String>) val;
                        wrapper.between(column, getStartDateTime(valArr.get(0)), getEndDateTime(valArr.get(1)));
                    } else {
                        String[] vals = StrUtil.split(String.valueOf(val), StringPool.COMMA);
                        if (vals.length != 2) {
                            throw new IllegalArgumentException("between mode param value length not equal 2");
                        }
                        wrapper.between(column, getStartDateTime(vals[0]), getEndDateTime(vals[1]));
                    }
                    break;
                default:
                    wrapper.like(column, val);
                    break;
            }
        }
    }
    
/*    private static String getColumn(Map<String, String> queryMapper, String key) {
        String column = null;
        
        if (null != queryMapper) {
            column = queryMapper.get(key);
        }
        
        if (StringUtils.isEmpty(column)) {
            throw new RuntimeException("key:" + key + ", not find column");
        }
        
        return column;
    }*/
    
    private static String getStartDateTime(String dateTime) {
        if (!dateTime.contains(":")) {
            return dateTime + " 00:00:00";
        }
        return dateTime;
    }
    
    private static String getEndDateTime(String dateTime) {
        if (!dateTime.contains(":")) {
            return dateTime + " 23:59:59";
        }
        return dateTime;
    }
    
}
