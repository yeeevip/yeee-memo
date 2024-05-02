package vip.yeee.memo.common.activiti7.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.IoUtil;
import lombok.extern.slf4j.Slf4j;
import org.activiti.api.process.model.ProcessInstance;
import org.activiti.api.process.model.builders.ProcessPayloadBuilder;
import org.activiti.api.process.model.payloads.DeleteProcessPayload;
import org.activiti.api.process.model.payloads.ResumeProcessPayload;
import org.activiti.api.process.model.payloads.SuspendProcessPayload;
import org.activiti.api.process.runtime.ProcessRuntime;
import org.activiti.api.runtime.shared.query.Order;
import org.activiti.api.runtime.shared.query.Page;
import org.activiti.api.runtime.shared.query.Pageable;
import org.activiti.api.runtime.shared.security.SecurityManager;
import org.activiti.api.task.model.Task;
import org.activiti.api.task.model.builders.TaskPayloadBuilder;
import org.activiti.api.task.model.payloads.CompleteTaskPayload;
import org.activiti.api.task.runtime.TaskRuntime;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.FlowElement;
import org.activiti.bpmn.model.Process;
import org.activiti.bpmn.model.SequenceFlow;
import org.activiti.bpmn.model.UserTask;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.task.TaskInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import vip.yeee.memo.base.model.exception.BizException;
import vip.yeee.memo.base.model.vo.PageReqVO;
import vip.yeee.memo.base.model.vo.PageVO;
import vip.yeee.memo.common.activiti7.mapper.ActivitiMapper;
import vip.yeee.memo.common.activiti7.model.request.DefDeleteReq;
import vip.yeee.memo.common.activiti7.model.request.InsDeleteReq;
import vip.yeee.memo.common.activiti7.model.request.InstCreateReq;
import vip.yeee.memo.common.activiti7.model.request.TaskCompleteReq;
import vip.yeee.memo.common.activiti7.model.vo.DefinitionVo;
import vip.yeee.memo.common.activiti7.model.vo.HistoryInstanceVo;
import vip.yeee.memo.common.activiti7.model.vo.InstanceVo;
import vip.yeee.memo.common.activiti7.model.vo.TaskHighlightVo;
import vip.yeee.memo.common.activiti7.model.vo.TaskVo;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * description......
 *
 * @author https://www.yeee.vip
 * @since 2023/12/15 17:39
 */
@Slf4j
@Component
public class CommonActiviti7Service {

//    @Autowired
//    private ActivitiMapper activitiMapper;
    @Resource
    private RepositoryService repositoryService;
    @Autowired
    private ProcessRuntime processRuntime;
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private TaskRuntime taskRuntime;
    @Autowired
    private TaskService taskService;
    @Autowired
    private HistoryService historyService;
    @Autowired
    private SecurityManager securityManager;

    public PageVO<DefinitionVo> definitionList(PageReqVO<?> reqVO) {
        PageVO<DefinitionVo> pageVO = new PageVO<>(reqVO.getPageNum(), reqVO.getPageSize());

        long totalNum = repositoryService.createProcessDefinitionQuery().count();
        pageVO.setTotal(totalNum);
        if (totalNum == 0) {
            return pageVO;
        }
        List<ProcessDefinition> definitionList = repositoryService.createProcessDefinitionQuery()
                .orderByProcessDefinitionVersion()
                .desc()
                .listPage((reqVO.getPageNum() - 1) * reqVO.getPageSize(), reqVO.getPageSize());
        List<DefinitionVo> voList = definitionList
                .stream()
                .map(po -> {
                    DefinitionVo vo = new DefinitionVo();
                    vo.setProcessDefinitionID(po.getId());
                    vo.setName(po.getName());
                    vo.setKey(po.getKey());
                    vo.setResourceName(po.getResourceName());
                    vo.setDeploymentID(po.getDeploymentId());
                    vo.setVersion(po.getVersion());
                    return vo;
                })
                .collect(Collectors.toList());
        pageVO.setResult(voList);
        return pageVO;
    }

    public Void definitionDelete(DefDeleteReq req) {
        for (String depId : req.getIds()) {
            repositoryService.deleteDeployment(depId, true);
        }
        return null;
    }

    public Void definitionAddDeploymentByString(String stringBPMN) {
        Deployment deployment = repositoryService.createDeployment()
                .addString("CreateWithYeeeSystem.bpmn", stringBPMN)
                .name("未命名的部署名称")
                .deploy();
        return null;
    }

    public void definitionDetailXml(HttpServletResponse response, String deploymentId, String resourceName) {
        InputStream inputStream = repositoryService.getResourceAsStream(deploymentId, resourceName);
        response.setContentType("text/xml");
        try {
            IoUtil.copy(inputStream, response.getOutputStream());
        } catch (IOException e) {
            log.error("获取失败", e);
            throw new BizException("获取失败");
        }
    }

    public PageVO<InstanceVo> instanceList(PageReqVO<?> reqVO) {
        PageVO<InstanceVo> pageVO = new PageVO<>(reqVO.getPageNum(), reqVO.getPageSize());
        Order order = Order.by("startDate", Order.Direction.DESC);
        Pageable pageable = Pageable.of((reqVO.getPageNum() - 1) * reqVO.getPageSize(), reqVO.getPageSize(), order);
        Page<ProcessInstance>  page = processRuntime.processInstances(pageable);
        if (CollectionUtil.isEmpty(page.getContent())) {
            return pageVO;
        }
        Set<String> pdIds = page.getContent().stream().map(ProcessInstance::getProcessDefinitionId).collect(Collectors.toSet());
        List<ProcessDefinition> definitionList = repositoryService.createProcessDefinitionQuery().processDefinitionIds(pdIds).list();
        Map<String, ProcessDefinition> definitionMap = definitionList.stream()
                .collect(Collectors.toMap(ProcessDefinition::getId, Function.identity(), (o, n) -> n));

        List<String> ids = page.getContent().stream().map(ProcessInstance::getId).collect(Collectors.toList());
        List<org.activiti.engine.task.Task> taskList = taskService.createTaskQuery().processInstanceIdIn(ids).list();
        Map<String, List<org.activiti.engine.task.Task>> taskMap = taskList.stream()
                .collect(Collectors.groupingBy(org.activiti.engine.task.Task::getProcessInstanceId));

        List<InstanceVo> voList = page.getContent()
                .stream()
                .map(po -> {
                    InstanceVo vo = new InstanceVo();
                    vo.setId(po.getId());
                    vo.setName(po.getName());
                    vo.setStatus(po.getStatus().name());
                    vo.setProcessDefinitionId(po.getProcessDefinitionId());
                    vo.setProcessDefinitionKey(po.getProcessDefinitionKey());
                    vo.setStartDate(po.getStartDate());
                    vo.setProcessDefinitionVersion(po.getProcessDefinitionVersion());
                    ProcessDefinition processDefinition = definitionMap.get(po.getProcessDefinitionId());
                    vo.setDefinitionName(processDefinition.getName());
                    vo.setDeploymentId(processDefinition.getDeploymentId());
                    vo.setResourceName(processDefinition.getResourceName());
                    List<org.activiti.engine.task.Task> tasks = taskMap.get(po.getId());
                    vo.setCurTask(tasks.stream().map(TaskInfo::getName).collect(Collectors.joining()));
                    return vo;
                })
                .collect(Collectors.toList());
        pageVO.setTotal((long) page.getTotalItems());
        pageVO.setResult(voList);
        return pageVO;
    }

    @Transactional(rollbackFor = Exception.class, isolation = Isolation.READ_COMMITTED)
    public Void instanceCreate(InstCreateReq req) {
        String authenticatedUserId = securityManager.getAuthenticatedUserId();
        Map<String,Object> variables = new HashMap<>();
        variables.put("applicant", authenticatedUserId);
        org.activiti.engine.runtime.ProcessInstance processInstance = runtimeService.startProcessInstanceById(req.getPdId(), variables);
        org.activiti.engine.task.Task task = taskService.createTaskQuery()
                .processInstanceId(processInstance.getProcessInstanceId())
                .taskUnassigned()
                .singleResult();
        taskService.claim(task.getId(), authenticatedUserId);
        taskService.complete(task.getId());
        return null;
    }

    public Void instanceSuspend(String instanceId) {
        SuspendProcessPayload payload = ProcessPayloadBuilder
                .suspend()
                .withProcessInstanceId(instanceId)
                .build();
        processRuntime.suspend(payload);
        return null;
    }

    public Void instanceResume(String instanceId) {
        ResumeProcessPayload payload = ProcessPayloadBuilder
                .resume()
                .withProcessInstanceId(instanceId)
                .build();
        processRuntime.resume(payload);
        return null;
    }

    public TaskHighlightVo instanceTaskHighlight(String instanceId) {
        HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery()
                .processInstanceId(instanceId).singleResult();
        //获取bpmnModel对象
        BpmnModel bpmnModel = repositoryService.getBpmnModel(historicProcessInstance.getProcessDefinitionId());
        //因为我们这里只定义了一个Process 所以获取集合中的第一个即可
        Process process = bpmnModel.getProcesses().get(0);
        //获取所有的FlowElement信息
        Collection<FlowElement> flowElements = process.getFlowElements();

        Map<String, String> map = new HashMap<>();
        for (FlowElement flowElement : flowElements) {
            //判断是否是连线
            if (flowElement instanceof SequenceFlow) {
                SequenceFlow sequenceFlow = (SequenceFlow) flowElement;
                String ref = sequenceFlow.getSourceRef();
                String targetRef = sequenceFlow.getTargetRef();
                map.put(ref + targetRef, sequenceFlow.getId());
            }
        }

        //获取流程实例 历史节点(全部)
        List<HistoricActivityInstance> list = historyService.createHistoricActivityInstanceQuery()
                .processInstanceId(instanceId)
                .list();
        //各个历史节点   两两组合 key
        Set<String> keyList = new HashSet<>();
        for (HistoricActivityInstance i : list) {
            for (HistoricActivityInstance j : list) {
                if (i != j) {
                    keyList.add(i.getActivityId() + j.getActivityId());
                }
            }
        }
        //高亮连线ID
        Set<String> highLine = new HashSet<>();
        keyList.forEach(s -> highLine.add(map.get(s)));


        //获取流程实例 历史节点（已完成）
        List<HistoricActivityInstance> listFinished = historyService.createHistoricActivityInstanceQuery()
                .processInstanceId(instanceId)
                .finished()
                .list();
        //高亮节点ID
        Set<String> highPoint = new HashSet<>();
        listFinished.forEach(s -> highPoint.add(s.getActivityId()));

        //获取流程实例 历史节点（待办节点）
        List<HistoricActivityInstance> listUnFinished = historyService.createHistoricActivityInstanceQuery()
                .processInstanceId(instanceId)
                .unfinished()
                .list();

        //需要移除的高亮连线
        Set<String> set = new HashSet<>();
        //待办高亮节点
        Set<String> waitingToDo = new HashSet<>();
        listUnFinished.forEach(s -> {
            waitingToDo.add(s.getActivityId());

            for (FlowElement flowElement : flowElements) {
                //判断是否是 用户节点
                if (flowElement instanceof UserTask) {
                    UserTask userTask = (UserTask) flowElement;

                    if (userTask.getId().equals(s.getActivityId())) {
                        List<SequenceFlow> outgoingFlows = userTask.getOutgoingFlows();
                        //因为 高亮连线查询的是所有节点  两两组合 把待办 之后  往外发出的连线 也包含进去了  所以要把高亮待办节点 之后 即出的连线去掉
                        if (CollectionUtil.isNotEmpty(outgoingFlows)) {
                            outgoingFlows.forEach(a -> {
                                if (a.getSourceRef().equals(s.getActivityId())) {
                                    set.add(a.getId());
                                }
                            });
                        }
                    }
                }
            }
        });

        highLine.removeAll(set);

//
//        //获取当前用户
//        Set<String> iDo = new HashSet<>(); //存放 高亮 我的办理节点
//        //当前用户已完成的任务
//
//        List<HistoricTaskInstance> taskInstanceList = historyService.createHistoricTaskInstanceQuery()
//                .taskAssignee(securityManager.getAuthenticatedUserId())
//                .finished()
//                .processInstanceId(instanceId).list();
//
//        taskInstanceList.forEach(a -> iDo.add(a.getTaskDefinitionKey()));

        TaskHighlightVo taskHighlightVo = new TaskHighlightVo();
        taskHighlightVo.setHighPoint(highPoint);
        taskHighlightVo.setHighLine(highLine);
        taskHighlightVo.setWaitingToDo(waitingToDo);
//        taskHighlightVo.setIDo(iDo);
        return taskHighlightVo;
    }

    public Void instanceDelete(InsDeleteReq req) {
        for (String instanceId : req.getIds()) {
            DeleteProcessPayload payload = ProcessPayloadBuilder
                    .delete()
                    .withProcessInstanceId(instanceId)
                    .build();
            processRuntime.delete(payload);
        }
        return null;
    }

    public PageVO<HistoryInstanceVo> instanceHistoryList(PageReqVO<?> reqVO) {
        PageVO<HistoryInstanceVo> pageVO = new PageVO<>(reqVO.getPageNum(), reqVO.getPageSize());
        long totalNum = historyService.createHistoricProcessInstanceQuery().notDeleted().count();
        pageVO.setTotal(totalNum);
        if (totalNum == 0) {
            return pageVO;
        }
        List<HistoricProcessInstance> historicProcessInstanceList = historyService.createHistoricProcessInstanceQuery()
                .notDeleted()
                .finished()
                .listPage((reqVO.getPageNum() - 1) * reqVO.getPageSize(), reqVO.getPageSize());

        Set<String> pdIds = historicProcessInstanceList.stream().map(HistoricProcessInstance::getProcessDefinitionId).collect(Collectors.toSet());
        List<ProcessDefinition> definitionList = repositoryService.createProcessDefinitionQuery().processDefinitionIds(pdIds).list();
        Map<String, ProcessDefinition> definitionMap = definitionList.stream()
                .collect(Collectors.toMap(ProcessDefinition::getId, Function.identity(), (o, n) -> n));

        List<HistoryInstanceVo> voList = historicProcessInstanceList
                .stream()
                .map(po -> {
                    HistoryInstanceVo vo = new HistoryInstanceVo();
                    vo.setId(po.getId());
                    vo.setProcessDefinitionId(po.getProcessDefinitionId());
                    vo.setProcessDefinitionKey(po.getProcessDefinitionKey());
                    vo.setStartTime(po.getStartTime());
                    vo.setEndTime(po.getEndTime());
                    ProcessDefinition processDefinition = definitionMap.get(po.getProcessDefinitionId());
                    vo.setDefinitionName(processDefinition.getName());
                    vo.setDeploymentId(processDefinition.getDeploymentId());
                    vo.setResourceName(processDefinition.getResourceName());
                    if (po.getDurationInMillis() != null) {
                        vo.setDuration(DateUtil.formatBetween(po.getDurationInMillis()));
                    }
                    return vo;
                })
                .collect(Collectors.toList());
        pageVO.setResult(voList);
        return pageVO;
    }

    public PageVO<TaskVo> taskList(PageReqVO<?> reqVO) {
        PageVO<TaskVo> pageVO = new PageVO<>(reqVO.getPageNum(), reqVO.getPageSize());
        Order order = Order.by("createdDate", Order.Direction.DESC);
        Pageable pageable = Pageable.of((reqVO.getPageNum() - 1) * reqVO.getPageSize(), reqVO.getPageSize(), order);

        Page<Task> page = taskRuntime.tasks(pageable);
        if (CollectionUtil.isEmpty(page.getContent())) {
            return pageVO;
        }

        Set<String> piIds = page.getContent().stream().map(Task::getProcessInstanceId).collect(Collectors.toSet());
        List<org.activiti.engine.runtime.ProcessInstance> instanceList = runtimeService.createProcessInstanceQuery().processInstanceIds(piIds).list();
        Map<String, org.activiti.engine.runtime.ProcessInstance> definitionMap = instanceList.stream()
                .collect(Collectors.toMap(org.activiti.engine.runtime.ProcessInstance::getId, Function.identity(), (o, n) -> n));
        List<TaskVo> voList = page.getContent()
                .stream()
                .map(po -> {
                    TaskVo vo = new TaskVo();
                    vo.setId(po.getId());
                    vo.setName(po.getName());
                    vo.setStatus(po.getStatus().name());
                    vo.setCreatedDate(po.getCreatedDate());
                    vo.setAssignee(po.getAssignee());
                    org.activiti.engine.runtime.ProcessInstance instance = definitionMap.get(po.getProcessInstanceId());
                    vo.setInstanceName(instance.getProcessDefinitionName());
                    return vo;
                })
                .collect(Collectors.toList());
        pageVO.setTotal((long) page.getTotalItems());
        pageVO.setResult(voList);
        return pageVO;
    }

    public PageVO<TaskVo> taskHistoryList(PageReqVO<?> reqVO) {
        PageVO<TaskVo> pageVO = new PageVO<>(reqVO.getPageNum(), reqVO.getPageSize());

        long totalNum = historyService.createHistoricTaskInstanceQuery().finished()
                .taskAssignee(securityManager.getAuthenticatedUserId()).count();
        pageVO.setTotal(totalNum);
        if (totalNum == 0) {
            return pageVO;
        }
        List<HistoricTaskInstance> historicTaskInstanceList = historyService.createHistoricTaskInstanceQuery()
                .finished()
                .orderByHistoricTaskInstanceEndTime().desc()
                .taskAssignee(securityManager.getAuthenticatedUserId())
                .listPage((reqVO.getPageNum() - 1) * reqVO.getPageSize(), reqVO.getPageSize());
        if (CollectionUtil.isEmpty(historicTaskInstanceList)) {
            return pageVO;
        }

        Set<String> piIds = historicTaskInstanceList.stream().map(HistoricTaskInstance::getProcessInstanceId).collect(Collectors.toSet());
        List<org.activiti.engine.runtime.ProcessInstance> instanceList = runtimeService.createProcessInstanceQuery().processInstanceIds(piIds).list();
        Map<String, org.activiti.engine.runtime.ProcessInstance> definitionMap = instanceList.stream()
                .collect(Collectors.toMap(org.activiti.engine.runtime.ProcessInstance::getId, Function.identity(), (o, n) -> n));
        List<TaskVo> voList = historicTaskInstanceList
                .stream()
                .map(po -> {
                    TaskVo vo = new TaskVo();
                    vo.setId(po.getId());
                    vo.setName(po.getName());
                    vo.setEndDate(po.getEndTime());
                    vo.setAssignee(po.getAssignee());
                    org.activiti.engine.runtime.ProcessInstance instance = definitionMap.get(po.getProcessInstanceId());
                    if (instance != null) {
                        vo.setInstanceName(instance.getProcessDefinitionName());
                    }
                    return vo;
                })
                .collect(Collectors.toList());
        pageVO.setResult(voList);
        return pageVO;
    }

    public Void taskComplete(TaskCompleteReq req) {
        Task task = taskRuntime.task(req.getTaskId());
        if (task == null) {
            throw new BizException("代办任务不存在");
        }
        if (task.getAssignee() == null) {
            taskRuntime.claim(TaskPayloadBuilder.claim().withTaskId(task.getId()).build());
        }
        CompleteTaskPayload payload = TaskPayloadBuilder.complete().withTaskId(task.getId())
                .withVariable("isPass", req.getIsPass())
                .build();
        taskRuntime.complete(payload);
        return null;
    }
}
