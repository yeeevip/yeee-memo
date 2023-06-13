package ${package};

import cn.hutool.core.collection.CollectionUtil;
import ${basePackage}.domain.entity.${tableClass.shortClassName};
import ${basePackage}.model.request.IdRequest;
import ${basePackage}.model.request.${tableClass.shortClassName}AddRequest;
import ${basePackage}.model.request.${tableClass.shortClassName}ListRequest;
import ${basePackage}.model.request.${tableClass.shortClassName}UpdRequest;
import ${basePackage}.model.vo.${tableClass.shortClassName}InfoVo;
import ${basePackage}.model.vo.${tableClass.shortClassName}ListVo;
import ${basePackage}.service.${tableClass.shortClassName}Service;
import org.springframework.stereotype.Component;
import vip.yeee.memo.integrate.base.model.exception.BizException;
<#if genType == 'mp'>
import com.baomidou.mybatisplus.core.metadata.IPage;
</#if>
<#if genType == 'tk'>
import com.github.pagehelper.PageInfo;
</#if>
import vip.yeee.memo.integrate.base.model.vo.PageVO;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
* create by yeee.一页 ${generateDate}
*/
@Component
public class ${tableClass.shortClassName}Biz {

    @Resource
    private ${tableClass.shortClassName}Service ${tableClass.variableName}Service;

    public PageVO<${tableClass.shortClassName}ListVo> ${tableClass.variableName}PageList(${tableClass.shortClassName}ListRequest request) {
        PageVO<${tableClass.shortClassName}ListVo> pageVO = new PageVO<>(request.getPageNum(), request.getPageSize());
        <#if genType == 'mp'>
        IPage<${tableClass.shortClassName}> page = ${tableClass.variableName}Service.${tableClass.variableName}PageList(request);
        if (CollectionUtil.isEmpty(page.getRecords())) {
            return pageVO;
        }
        List<${tableClass.shortClassName}ListVo> voList = page.getRecords()
                .stream()
                .map(po -> new ${tableClass.shortClassName}ListVo())
                .collect(Collectors.toList());
        </#if>
        <#if genType == 'tk'>
        PageInfo<${tableClass.shortClassName}> page = ${tableClass.variableName}Service.${tableClass.variableName}PageList(request);
        if (CollectionUtil.isEmpty(page.getList())) {
            return pageVO;
        }
        List<${tableClass.shortClassName}ListVo> voList = page.getList()
                .stream()
                .map(po -> new ${tableClass.shortClassName}ListVo())
                .collect(Collectors.toList());
        </#if>
        pageVO.setPages((int) page.getPages());
        pageVO.setTotal(page.getTotal());
        pageVO.setResult(voList);
        return pageVO;
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