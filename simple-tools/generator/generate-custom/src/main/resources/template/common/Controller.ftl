package ${package};

import ${modelPackage}.biz.${tableClass.shortClassName}Biz;
import ${modelPackage}.model.request.IdRequest;
import ${modelPackage}.model.request.${tableClass.shortClassName}AddRequest;
import ${modelPackage}.model.request.${tableClass.shortClassName}ListRequest;
import ${modelPackage}.model.request.${tableClass.shortClassName}UpdRequest;
import ${modelPackage}.model.vo.${tableClass.shortClassName}InfoVo;
import ${modelPackage}.model.vo.${tableClass.shortClassName}ListVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import vip.yeee.memo.integrate.base.model.rest.CommonResult;

import javax.annotation.Resource;
import java.util.List;

/**
* create by yeee.一页 ${generateDate}
*/
@Api(tags = "${tableClass.shortClassName}")
@RequestMapping("/${tableClass.variableName}")
@RestController
public class ${tableClass.shortClassName}Controller {

    @Resource
    private ${tableClass.shortClassName}Biz ${tableClass.variableName}Biz;

    @ApiOperation("列表")
    @PostMapping(value = "/page")
    public CommonResult<List<${tableClass.shortClassName}ListVo>> ${tableClass.variableName}PageList(@RequestBody @Validated ${tableClass.shortClassName}ListRequest request) {
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