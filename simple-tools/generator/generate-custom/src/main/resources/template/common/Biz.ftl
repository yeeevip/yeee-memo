package ${package};

import cn.hutool.core.collection.CollectionUtil;
import ${modelPackage}.domain.entity.${tableClass.shortClassName};
import ${modelPackage}.model.request.IdRequest;
import ${modelPackage}.model.request.${tableClass.shortClassName}AddRequest;
import ${modelPackage}.model.request.${tableClass.shortClassName}ListRequest;
import ${modelPackage}.model.request.${tableClass.shortClassName}UpdRequest;
import ${modelPackage}.model.vo.${tableClass.shortClassName}InfoVo;
import ${modelPackage}.model.vo.${tableClass.shortClassName}ListVo;
import ${modelPackage}.service.${tableClass.shortClassName}Service;
import org.springframework.stereotype.Component;
import vip.yeee.memo.integrate.base.model.exception.BizException;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
* create by yeee.一页 ${generateDate}
*/
@Component
public class ${tableClass.shortClassName}Biz {

    @Resource
    private ${tableClass.shortClassName}Service ${tableClass.variableName}Service;

    public List<${tableClass.shortClassName}ListVo> ${tableClass.variableName}PageList(${tableClass.shortClassName}ListRequest request) {
        List<${tableClass.shortClassName}> ${tableClass.variableName}List = ${tableClass.variableName}Service.${tableClass.variableName}PageList(request);
        if (CollectionUtil.isEmpty(${tableClass.variableName}List)) {
            return Collections.emptyList();
        }
        List<${tableClass.shortClassName}ListVo> voList = ${tableClass.variableName}List
                .stream()
                .map(po -> new ${tableClass.shortClassName}ListVo())
                .collect(Collectors.toList());
        return voList;
    }

    public Void ${tableClass.variableName}Add(${tableClass.shortClassName}AddRequest request) {
        ${tableClass.variableName}Service.${tableClass.variableName}Add(request);
        return null;
    }

    public Void ${tableClass.variableName}Upd(${tableClass.shortClassName}UpdRequest request) {
        ${tableClass.shortClassName} ${tableClass.variableName} = ${tableClass.variableName}Service.query${tableClass.shortClassName}ById(request.getId());
        if (${tableClass.variableName} == null) {
            throw new BizException("不存在");
        }
        ${tableClass.variableName}Service.${tableClass.variableName}Upd(request);
        return null;
    }

    public ${tableClass.shortClassName}InfoVo ${tableClass.variableName}Info(IdRequest request) {
        ${tableClass.shortClassName} ${tableClass.variableName} = ${tableClass.variableName}Service.query${tableClass.shortClassName}ById(request.getId());
        if (${tableClass.variableName} == null) {
            throw new BizException("不存在");
        }
        ${tableClass.shortClassName}InfoVo infoVo = new ${tableClass.shortClassName}InfoVo();
        return infoVo;
    }

    public Void ${tableClass.variableName}Del(IdRequest request) {
        ${tableClass.shortClassName} ${tableClass.variableName} = ${tableClass.variableName}Service.query${tableClass.shortClassName}ById(request.getId());
        if (${tableClass.variableName} == null) {
            throw new BizException("不存在");
        }
        ${tableClass.variableName}Service.${tableClass.variableName}Del(request);
        return null;
    }

}