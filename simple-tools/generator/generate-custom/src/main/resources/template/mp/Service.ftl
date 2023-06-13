package ${package};

import ${tableClass.packageName}.${tableClass.shortClassName};
import ${basePackage}.domain.mapper.${tableClass.shortClassName}Mapper;
import ${basePackage}.model.request.IdRequest;
import ${basePackage}.model.request.${tableClass.shortClassName}AddRequest;
import ${basePackage}.model.request.${tableClass.shortClassName}ListRequest;
import ${basePackage}.model.request.${tableClass.shortClassName}UpdRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.io.Serializable;

/**
* create by yeee.一页 ${generateDate}
*/
@Service
public class ${tableClass.shortClassName}Service extends ServiceImpl<${tableClass.shortClassName}Mapper, ${tableClass.shortClassName}> {

    public IPage<${tableClass.shortClassName}> ${tableClass.variableName}PageList(${tableClass.shortClassName}ListRequest request) {
        LambdaQueryWrapper<${tableClass.shortClassName}> query = Wrappers.lambdaQuery();
        query.orderByDesc(${tableClass.shortClassName}::getId);
        return this.page(new Page<>(request.getPageNum(), request.getPageSize()), query);
    }

    @Transactional(rollbackFor = Exception.class, isolation = Isolation.READ_COMMITTED)
    public void ${tableClass.variableName}Add(${tableClass.shortClassName}AddRequest request) {

    }

    @Transactional(rollbackFor = Exception.class, isolation = Isolation.READ_COMMITTED)
    public void ${tableClass.variableName}Upd(${tableClass.shortClassName}UpdRequest request) {

    }

    public void ${tableClass.variableName}Del(IdRequest request) {

    }

    public ${tableClass.shortClassName} query${tableClass.shortClassName}ById(Serializable id) {
        return this.getById(id);
    }
}