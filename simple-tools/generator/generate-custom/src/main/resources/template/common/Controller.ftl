package ${package};

import ${basePackage}.biz.${tableClass.shortClassName}Biz;
import ${basePackage}.model.request.IdRequest;
import ${basePackage}.model.request.${tableClass.shortClassName}AddRequest;
import ${basePackage}.model.request.${tableClass.shortClassName}ListRequest;
import ${basePackage}.model.request.${tableClass.shortClassName}UpdRequest;
import ${basePackage}.model.vo.${tableClass.shortClassName}InfoVo;
import ${basePackage}.model.vo.${tableClass.shortClassName}ListVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import vip.yeee.memo.integrate.base.model.rest.CommonResult;
import vip.yeee.memo.integrate.base.model.vo.PageVO;

import javax.annotation.Resource;
import java.util.List;

/**
* create by yeee.一页 ${generateDate}
*/
@Api(tags = "${tableClass.remarks}")
@RequestMapping("/${tableClass.pagesPath}")
@RestController
public class ${tableClass.shortClassName}Controller {

    @Resource
    private ${tableClass.shortClassName}Biz ${tableClass.variableName}Biz;

    @ApiOperation("列表")
    @PostMapping(value = "/page")
    public CommonResult<PageVO<${tableClass.shortClassName}ListVo>> ${tableClass.variableName}PageList(@RequestBody @Validated ${tableClass.shortClassName}ListRequest request) {
        return CommonResult.success(${tableClass.variableName}Biz.${tableClass.variableName}PageList(request));
    }

    @ApiOperation("创建")
    @PostMapping(value = "/add")
    public CommonResult<Void> ${tableClass.variableName}Add(@RequestBody @Validated ${tableClass.shortClassName}AddRequest request) {
        return CommonResult.success(${tableClass.variableName}Biz.${tableClass.variableName}Add(request));
    }

    @ApiOperation("修改")
    @PostMapping(value = "/upd")
    public CommonResult<Void> ${tableClass.variableName}Upd(@RequestBody @Validated ${tableClass.shortClassName}UpdRequest request) {
        return CommonResult.success(${tableClass.variableName}Biz.${tableClass.variableName}Upd(request));
    }

    @ApiOperation("详情")
    @PostMapping(value = "/info")
    public CommonResult<${tableClass.shortClassName}InfoVo> ${tableClass.variableName}Info(@RequestBody @Validated IdRequest request) {
        return CommonResult.success(${tableClass.variableName}Biz.${tableClass.variableName}Info(request));
    }

    @ApiOperation("删除")
    @PostMapping(value = "/del")
    public CommonResult<Void> ${tableClass.variableName}Del(@RequestBody @Validated IdRequest request) {
        return CommonResult.success(${tableClass.variableName}Biz.${tableClass.variableName}Del(request));
    }
}