package ${package};

import cn.hutool.core.collection.CollectionUtil;
import org.springframework.beans.BeanUtils;
import ${basePackage}.domain.entity.${tableClass.shortClassName};
import ${basePackage}.model.request.IdRequest;
import ${basePackage}.model.request.${tableClass.shortClassName}AddRequest;
import ${basePackage}.model.request.${tableClass.shortClassName}ListRequest;
import ${basePackage}.model.request.${tableClass.shortClassName}UpdRequest;
import ${basePackage}.model.vo.${tableClass.shortClassName}InfoVo;
import ${basePackage}.model.vo.${tableClass.shortClassName}ListVo;
import ${basePackage}.service.${tableClass.shortClassName}Service;
import org.springframework.stereotype.Component;
import vip.yeee.memo.base.model.exception.BizException;
<#if genType == 'mp'>
import com.baomidou.mybatisplus.core.metadata.IPage;
</#if>
<#if genType == 'tk'>
import com.github.pagehelper.PageInfo;
</#if>
import vip.yeee.memo.base.model.vo.PageVO;

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
        <#if genType == 'mp'>
        IPage<${tableClass.shortClassName}> page = ${tableClass.variableName}Service.${tableClass.variableName}PageList(request);
        PageVO<${tableClass.shortClassName}ListVo> pageVO = new PageVO<>(request.getPageNum(), request.getPageSize());
        if (CollectionUtil.isEmpty(page.getRecords())) {
            return pageVO;
        }
        List<${tableClass.shortClassName}ListVo> voList = page.getRecords()
                .stream()
                .map(po -> {
                    ${tableClass.shortClassName}ListVo vo = new ${tableClass.shortClassName}ListVo();
                    BeanUtils.copyProperties(po, vo);
                    vo.setId(po.getId().toString());
                    return vo;
                })
                .collect(Collectors.toList());
        </#if>
        <#if genType == 'tk'>
        PageInfo<${tableClass.shortClassName}> page = ${tableClass.variableName}Service.${tableClass.variableName}PageList(request);
        PageVO<${tableClass.shortClassName}ListVo> pageVO = new PageVO<>(request.getPageNum(), request.getPageSize());
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
        ${tableClass.shortClassName} saveModel = new ${tableClass.shortClassName}();
        BeanUtils.copyProperties(request, saveModel);
        ${tableClass.variableName}Service.${tableClass.variableName}Add(saveModel);
        return null;
    }

    public Void ${tableClass.variableName}Upd(${tableClass.shortClassName}UpdRequest request) {
        ${tableClass.shortClassName} ${tableClass.variableName} = ${tableClass.variableName}Service.query${tableClass.shortClassName}ById(request.getId());
        if (${tableClass.variableName} == null) {
            throw new BizException("不存在");
        }
        ${tableClass.shortClassName} updModel = new ${tableClass.shortClassName}();
        BeanUtils.copyProperties(request, updModel);
        ${tableClass.variableName}Service.${tableClass.variableName}Upd(updModel);
        return null;
    }

    public ${tableClass.shortClassName}InfoVo ${tableClass.variableName}Info(IdRequest request) {
        if (request.getId() == null) {
            throw new BizException("ID不能为空");
        }
        ${tableClass.shortClassName} ${tableClass.variableName} = ${tableClass.variableName}Service.query${tableClass.shortClassName}ById(request.getId());
        if (${tableClass.variableName} == null) {
            throw new BizException("不存在");
        }
        ${tableClass.shortClassName}InfoVo infoVo = new ${tableClass.shortClassName}InfoVo();
        BeanUtils.copyProperties(${tableClass.variableName}, infoVo);
        infoVo.setId(${tableClass.variableName}.getId().toString());
        return infoVo;
    }

    public Void ${tableClass.variableName}Del(IdRequest request) {
        if (CollectionUtil.isEmpty(request.getIds())) {
            throw new BizException("IDS不能为空");
        }
        // ${tableClass.shortClassName} ${tableClass.variableName} = ${tableClass.variableName}Service.query${tableClass.shortClassName}ById(request.getId());
        // if (${tableClass.variableName} == null) {
        //     throw new BizException("不存在");
        // }
        ${tableClass.variableName}Service.${tableClass.variableName}Del(request.getIds());
        return null;
    }

}