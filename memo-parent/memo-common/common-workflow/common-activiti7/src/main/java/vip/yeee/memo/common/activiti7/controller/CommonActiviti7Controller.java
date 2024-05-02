package vip.yeee.memo.common.activiti7.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import vip.yeee.memo.base.model.rest.CommonResult;
import vip.yeee.memo.base.model.vo.PageReqVO;
import vip.yeee.memo.base.model.vo.PageVO;
import vip.yeee.memo.common.activiti7.model.request.DefDeleteReq;
import vip.yeee.memo.common.activiti7.model.request.InsDeleteReq;
import vip.yeee.memo.common.activiti7.model.request.InstCreateReq;
import vip.yeee.memo.common.activiti7.model.request.TaskCompleteReq;
import vip.yeee.memo.common.activiti7.model.vo.DefinitionVo;
import vip.yeee.memo.common.activiti7.model.vo.HistoryInstanceVo;
import vip.yeee.memo.common.activiti7.model.vo.InstanceVo;
import vip.yeee.memo.common.activiti7.model.vo.TaskHighlightVo;
import vip.yeee.memo.common.activiti7.model.vo.TaskVo;
import vip.yeee.memo.common.activiti7.service.CommonActiviti7Service;

import javax.servlet.http.HttpServletResponse;

/**
 * description......
 *
 * @author https://www.yeee.vip
 * @since 2023/12/15 17:39
 */
@Slf4j
@RestController
@RequestMapping("/activiti7")
public class CommonActiviti7Controller {

    @Autowired
    private CommonActiviti7Service commonActiviti7Service;

    @RequestMapping(value = "/definition/list")
    public CommonResult<PageVO<DefinitionVo>> definitionList(PageReqVO<?> reqVO) {
        return CommonResult.success(commonActiviti7Service.definitionList(reqVO));
    }

    @PostMapping(value = "/definition/delete")
    public CommonResult<Void> definitionDelete(@RequestBody DefDeleteReq req) {
        return CommonResult.success(commonActiviti7Service.definitionDelete(req));
    }

    @RequestMapping(value = "/definition/addDeploymentByString")
    public CommonResult<Void> definitionAddDeploymentByString(String stringBPMN) {
        return CommonResult.success(commonActiviti7Service.definitionAddDeploymentByString(stringBPMN));
    }

    @RequestMapping(value = "/definition/xml")
    public void definitionDetailXml(HttpServletResponse response, String deploymentId, String resourceName) {
        commonActiviti7Service.definitionDetailXml(response, deploymentId, resourceName);
    }

    @RequestMapping(value = "/instance/list")
    public CommonResult<PageVO<InstanceVo>> instanceList(PageReqVO<?> reqVO) {
        return CommonResult.success(commonActiviti7Service.instanceList(reqVO));
    }

    @PostMapping(value = "/instance/create")
    public CommonResult<Void> instanceCreate(@RequestBody InstCreateReq req) {
        return CommonResult.success(commonActiviti7Service.instanceCreate(req));
    }

    @RequestMapping(value = "/instance/suspend")
    public CommonResult<Void> instanceSuspend(String instanceId) {
        return CommonResult.success(commonActiviti7Service.instanceSuspend(instanceId));
    }

    @RequestMapping(value = "/instance/resume")
    public CommonResult<Void> instanceResume(String instanceId) {
        return CommonResult.success(commonActiviti7Service.instanceResume(instanceId));
    }

    @RequestMapping(value = "/instance/taskHighlight")
    public CommonResult<TaskHighlightVo> instanceTaskHighlight(String instanceId) {
        return CommonResult.success(commonActiviti7Service.instanceTaskHighlight(instanceId));
    }

    @PostMapping(value = "/instance/delete")
    public CommonResult<Void> instanceDelete(@RequestBody InsDeleteReq req) {
        return CommonResult.success(commonActiviti7Service.instanceDelete(req));
    }

    @RequestMapping(value = "/instance/history/list")
    public CommonResult<PageVO<HistoryInstanceVo>> instanceHistoryList(PageReqVO<?> reqVO) {
        return CommonResult.success(commonActiviti7Service.instanceHistoryList(reqVO));
    }

    @RequestMapping(value = "/task/list")
    public CommonResult<PageVO<TaskVo>> taskList(PageReqVO<?> reqVO) {
        return CommonResult.success(commonActiviti7Service.taskList(reqVO));
    }

    @RequestMapping(value = "/task/history/list")
    public CommonResult<PageVO<TaskVo>> taskHistoryList(PageReqVO<?> reqVO) {
        return CommonResult.success(commonActiviti7Service.taskHistoryList(reqVO));
    }

    @PostMapping(value = "/task/complete")
    public CommonResult<Void> taskComplete(@RequestBody TaskCompleteReq req) {
        return CommonResult.success(commonActiviti7Service.taskComplete(req));
    }
}
