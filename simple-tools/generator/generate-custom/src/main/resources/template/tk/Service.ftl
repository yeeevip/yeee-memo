package ${package};

import ${tableClass.packageName}.${tableClass.shortClassName};
import ${basePackage}.model.request.IdRequest;
import ${basePackage}.model.request.${tableClass.shortClassName}AddRequest;
import ${basePackage}.model.request.${tableClass.shortClassName}ListRequest;
import ${basePackage}.model.request.${tableClass.shortClassName}UpdRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import vip.yeee.memo.integrate.base.tkmapper.service.BaseService;
import com.github.pagehelper.PageInfo;
import tk.mybatis.mapper.entity.Example;

/**
* create by yeee.一页 ${generateDate}
*/
@Service
public class ${tableClass.shortClassName}Service extends BaseService<${tableClass.shortClassName}> {

    public PageInfo<${tableClass.shortClassName}> ${tableClass.variableName}PageList(${tableClass.shortClassName}ListRequest request) {
        Example example = Example.builder(${tableClass.shortClassName}.class).build();
        return this.queryPageListByField(request.getPageNum(), request.getPageSize(), example);
    }

    @Transactional(rollbackFor = Exception.class, isolation = Isolation.READ_COMMITTED)
    public void ${tableClass.variableName}Add(${tableClass.shortClassName}AddRequest request) {

    }

    @Transactional(rollbackFor = Exception.class, isolation = Isolation.READ_COMMITTED)
    public void ${tableClass.variableName}Upd(${tableClass.shortClassName}UpdRequest request) {

    }

    public void ${tableClass.variableName}Del(IdRequest request) {

    }

    public ${tableClass.shortClassName} query${tableClass.shortClassName}ById(Object id) {
        return this.queryByPrimaryKey(id);
    }
}