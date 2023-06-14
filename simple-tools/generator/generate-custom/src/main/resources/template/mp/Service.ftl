package ${package};

import ${tableClass.packageName}.${tableClass.shortClassName};
import ${basePackage}.domain.mapper.${tableClass.shortClassName}Mapper;
import ${basePackage}.model.request.IdRequest;
import ${basePackage}.model.request.${tableClass.shortClassName}ListRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.io.Serializable;
import java.util.List;

/**
* create by yeee.一页 ${generateDate}
*/
@Service
public class ${tableClass.shortClassName}Service extends ServiceImpl<${tableClass.shortClassName}Mapper, ${tableClass.shortClassName}> {

    public IPage<${tableClass.shortClassName}> ${tableClass.variableName}PageList(${tableClass.shortClassName}ListRequest request) {
        // MyPageWrapper<${tableClass.shortClassName}> pageWrapper = new MyPageWrapper<>(query);
        // return this.page(pageWrapper.getPage(), pageWrapper.getQueryWrapper());
        LambdaQueryWrapper<${tableClass.shortClassName}> query = Wrappers.lambdaQuery();
        query.orderByDesc(${tableClass.shortClassName}::getId);
        return this.page(new Page<>(request.getPageNum(), request.getPageSize()), query);
    }

    @Transactional(rollbackFor = Exception.class, isolation = Isolation.READ_COMMITTED)
    public void ${tableClass.variableName}Add(${tableClass.shortClassName} saveModel) {
        this.save(saveModel);
    }

    @Transactional(rollbackFor = Exception.class, isolation = Isolation.READ_COMMITTED)
    public void ${tableClass.variableName}Upd(${tableClass.shortClassName} updModel) {
        this.updateById(updModel);
    }

    public void ${tableClass.variableName}Del(List<? extends Serializable> ids) {
        this.removeByIds(ids);
    }

    public ${tableClass.shortClassName} query${tableClass.shortClassName}ById(Serializable id) {
        return this.getById(id);
    }
}