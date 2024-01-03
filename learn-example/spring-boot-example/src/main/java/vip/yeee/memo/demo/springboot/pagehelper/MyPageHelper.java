package vip.yeee.memo.demo.springboot.pagehelper;

import cn.hutool.core.util.ReflectUtil;
import com.github.pagehelper.*;
import com.github.pagehelper.dialect.AbstractHelperDialect;
import com.github.pagehelper.page.PageBoundSqlInterceptors;
import com.github.pagehelper.page.PageMethod;
import com.github.pagehelper.page.PageParams;
import com.github.pagehelper.parser.CountSqlParser;
import com.github.pagehelper.util.MSUtils;
import com.github.pagehelper.util.StringUtil;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.RowBounds;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;

/**
 * description......
 *
 * @author yeeee
 * @since 2024/1/3 9:53
 */
public class MyPageHelper extends PageMethod implements Dialect, BoundSqlInterceptor.Chain {
    private PageParams pageParams;
    private MyPageAutoDialect autoDialect;
    private PageBoundSqlInterceptors pageBoundSqlInterceptors;

    @Override
    public boolean skip(MappedStatement ms, Object parameterObject, RowBounds rowBounds) {
        if (ms.getId().endsWith(MSUtils.COUNT)) {
            throw new RuntimeException("在系统中发现了多个分页插件，请检查系统配置!");
        }
        Page page = pageParams.getPage(parameterObject, rowBounds);
        if (page == null) {
            return true;
        } else {
            //设置默认的 count 列
            if (StringUtil.isEmpty(page.getCountColumn())) {
                page.setCountColumn(pageParams.getCountColumn());
            }
            autoDialect.initDelegateDialect(ms, page.getDialectClass());
            return false;
        }
    }

    @Override
    public boolean beforeCount(MappedStatement ms, Object parameterObject, RowBounds rowBounds) {
        return autoDialect.getDelegate().beforeCount(ms, parameterObject, rowBounds);
    }

    @Override
    public String getCountSql(MappedStatement ms, BoundSql boundSql, Object parameterObject, RowBounds rowBounds, CacheKey countKey) {
        return autoDialect.getDelegate().getCountSql(ms, boundSql, parameterObject, rowBounds, countKey);
    }

    @Override
    public boolean afterCount(long count, Object parameterObject, RowBounds rowBounds) {
        return autoDialect.getDelegate().afterCount(count, parameterObject, rowBounds);
    }

    @Override
    public Object processParameterObject(MappedStatement ms, Object parameterObject, BoundSql boundSql, CacheKey pageKey) {
        return autoDialect.getDelegate().processParameterObject(ms, parameterObject, boundSql, pageKey);
    }

    @Override
    public boolean beforePage(MappedStatement ms, Object parameterObject, RowBounds rowBounds) {
        return autoDialect.getDelegate().beforePage(ms, parameterObject, rowBounds);
    }

    @Override
    public String getPageSql(MappedStatement ms, BoundSql boundSql, Object parameterObject, RowBounds rowBounds, CacheKey pageKey) {
        return autoDialect.getDelegate().getPageSql(ms, boundSql, parameterObject, rowBounds, pageKey);
    }

    public String getPageSql(String sql, Page page, RowBounds rowBounds, CacheKey pageKey) {
        return autoDialect.getDelegate().getPageSql(sql, page, pageKey);
    }

    @Override
    public Object afterPage(List pageList, Object parameterObject, RowBounds rowBounds) {
        //这个方法即使不分页也会被执行，所以要判断 null
        AbstractHelperDialect delegate = autoDialect.getDelegate();
        if (delegate != null) {
            return delegate.afterPage(pageList, parameterObject, rowBounds);
        }
        return pageList;
    }

    @Override
    public void afterAll() {
        //这个方法即使不分页也会被执行，所以要判断 null
        AbstractHelperDialect delegate = autoDialect.getDelegate();
        if (delegate != null) {
            delegate.afterAll();
            autoDialect.clearDelegate();
        }
        clearPage();
    }

    @Override
    public BoundSql doBoundSql(BoundSqlInterceptor.Type type, BoundSql boundSql, CacheKey cacheKey) {
        Page<Object> localPage = getLocalPage();
        BoundSqlInterceptor.Chain chain = localPage != null ? ReflectUtil.invoke(localPage, "getChain") : null;
        if (chain == null) {
            BoundSqlInterceptor boundSqlInterceptor = localPage != null ? localPage.getBoundSqlInterceptor() : null;
            BoundSqlInterceptor.Chain defaultChain = pageBoundSqlInterceptors != null ? pageBoundSqlInterceptors.getChain() : null;
            if (boundSqlInterceptor != null) {
                chain = new BoundSqlInterceptorChain(defaultChain, Arrays.asList(boundSqlInterceptor));
            } else if (defaultChain != null) {
                chain = defaultChain;
            }
            if (chain == null) {
                chain = DO_NOTHING;
            }
            if (localPage != null) {
                ReflectUtil.invoke(localPage, "setChain", chain);
            }
        }
        return chain.doBoundSql(type, boundSql, cacheKey);
    }

    @Override
    public void setProperties(Properties properties) {
        setStaticProperties(properties);
        pageParams = new PageParams();
        autoDialect = new MyPageAutoDialect();
        pageBoundSqlInterceptors = new PageBoundSqlInterceptors();
        pageParams.setProperties(properties);
        autoDialect.setProperties(properties);
        pageBoundSqlInterceptors.setProperties(properties);
        //20180902新增 aggregateFunctions, 允许手动添加聚合函数（影响行数）
        CountSqlParser.addAggregateFunctions(properties.getProperty("aggregateFunctions"));
    }
}
