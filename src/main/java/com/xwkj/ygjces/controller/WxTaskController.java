package com.xwkj.ygjces.controller;

import com.xwkj.ygjces.common.ResponseResult;
import com.xwkj.ygjces.common.ResponseResultEnum;
import com.xwkj.ygjces.model.*;
import com.xwkj.ygjces.service.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
public class WxTaskController {

    private static Log log = LogFactory.getLog(WxTaskController.class);

    @Autowired
    WxTaskService wxTaskService;
    @Autowired
    WxService myWxService;
    @Autowired
    TaskInfoNoticeService taskInfoNoticeService;
    @Autowired
    OrganizationInfoService organizationInfoService;
    @Autowired
    TaskRemindService taskRemindService;
    @Autowired
    TaskOverTimeService taskOverTimeService;

    @GetMapping("/wx/toInputTask")
    public String toInputTask(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Object user = request.getSession().getAttribute("user");
        if (user == null) {
            String s = myWxService.getOauth2Service().buildAuthorizationUrl(request.getServletPath());
            response.sendRedirect(s);
            return null;
        }
        return "redirect:/wx/inputTask";
    }

    @GetMapping("/wx/toApproval")
    public String toApproval(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Object user = request.getSession().getAttribute("user");
        if (user == null) {
            String s = myWxService.getOauth2Service().buildAuthorizationUrl(request.getServletPath());
            response.sendRedirect(s);
            return null;
        }
        return "redirect:/wx/toApprovalPage";
    }

    @GetMapping("/wx/toCompleteTask")
    public String toCompleteTask(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Object user = request.getSession().getAttribute("user");
        if (user == null) {
            String s = myWxService.getOauth2Service().buildAuthorizationUrl(request.getServletPath());
            response.sendRedirect(s);
            return null;
        }
        return "redirect:/wx/toCompleteTaskPage";
    }

    @GetMapping("/wx/toMyTaskList")
    public String toMyTaskList(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Object user = request.getSession().getAttribute("user");
        if (user == null) {
            String s = myWxService.getOauth2Service().buildAuthorizationUrl(request.getServletPath());
            response.sendRedirect(s);
            return null;
        }
        return "redirect:/wx/toMyTaskListPage";
    }

    @GetMapping("/wx/toTaskRanking")
    public String toTaskRanking(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Object user = request.getSession().getAttribute("user");
        if (user == null) {
            String s = myWxService.getOauth2Service().buildAuthorizationUrl(request.getServletPath());
            response.sendRedirect(s);
            return null;
        }
        return "redirect:/wx/toTaskRankingPage";
    }

    @GetMapping("/wx/toConfirmCompletionList")
    public String toConfirmCompletionList(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Object user = request.getSession().getAttribute("user");
        if (user == null) {
            String s = myWxService.getOauth2Service().buildAuthorizationUrl(request.getServletPath());
            response.sendRedirect(s);
            return null;
        }
        return "redirect:/wx/toConfirmCompletionListPage";
    }

    /**
     * 跳转到新增任务页面
     *
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @author zyh
     */
    @GetMapping("/wx/inputTask")
    @RequiresPermissions("addTask")
    public String toSubmitTask(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Object user = request.getSession().getAttribute("user");
        if (user == null) {
            String s = myWxService.getOauth2Service().buildAuthorizationUrl(request.getServletPath());
            response.sendRedirect(s);
            return null;
        }
        log.info("跳转到新增任务页面");
        return "wx/submitTask";
    }

    /**
     * 跳转到待审批任务列表页面
     *
     * @param request
     * @param response
     * @param map
     * @return
     * @throws IOException
     * @author zyh
     */
    @GetMapping("/wx/toApprovalPage")
    @RequiresPermissions("approveTask")
    public String toApprovalPage(HttpServletRequest request, HttpServletResponse response, Map<String, Object> map) throws IOException {
        Object user = request.getSession().getAttribute("user");
        if (user == null) {
            String s = myWxService.getOauth2Service().buildAuthorizationUrl(request.getServletPath());
            response.sendRedirect(s);
            return null;
        }
        log.info("跳转到待审批任务列表页面");
        List<TaskInfo> list = wxTaskService.getTaskInfoToApproval();
        map.put("list", list);

        return "wx/approval-list";
    }

    /**
     * 跳转到查看待完成的任务和已完成的任务列表的页面
     *
     * @param request
     * @param response
     * @param map
     * @return
     * @author zyh
     */
    @GetMapping("/wx/toCompleteTaskPage")
    @RequiresPermissions("showTask")
    public String toCompleteTaskPage(HttpServletRequest request, HttpServletResponse response, Map<String, Object> map) throws IOException {
        Object user = request.getSession().getAttribute("user");
        if (user == null) {
            String s = myWxService.getOauth2Service().buildAuthorizationUrl(request.getServletPath());
            response.sendRedirect(s);
            return null;
        }

        log.info("跳转到查看待完成的任务和已完成的任务列表的页面");
        Map<String, Object> taskMap = wxTaskService.getTaskInfoByUserDepartments(request);
        map.put("taskList", taskMap);

        return "wx/task-list";
    }

    /**
     * 去往我的任务页面
     *
     * @param taskInfo
     * @param request
     * @param response
     * @param map
     * @return
     * @throws IOException
     * @author zyh
     */
    @GetMapping("/wx/toMyTaskListPage")
    @RequiresPermissions("showTask")
    public String toMyTaskListPage(TaskInfo taskInfo, HttpServletRequest request, HttpServletResponse response, Map<String, Object> map) throws IOException {
        Object user = request.getSession().getAttribute("user");
        if (user == null) {
            String s = myWxService.getOauth2Service().buildAuthorizationUrl(request.getServletPath());
            response.sendRedirect(s);
            return null;
        }

        log.info("去往我的任务页面");
        List<TaskInfo> list = wxTaskService.getTaskInfoListByStatus(taskInfo, request);
        map.put("list", list);
        if (taskInfo.getStatus() != null) {
            map.put("status", taskInfo.getStatus());
        }
        return "wx/mytask-list";
    }

    /**
     * 去往任务排行页面
     *
     * @param request
     * @param response
     * @return
     * @author zyh
     */
    @GetMapping("/wx/toTaskRankingPage")
	@RequiresPermissions("showTask")
    public String toTaskRankingPage(HttpServletRequest request, HttpServletResponse response) throws IOException {
		Object user = request.getSession().getAttribute("user");
		if (user == null){
			String s = myWxService.getOauth2Service().buildAuthorizationUrl(request.getServletPath());
			response.sendRedirect(s);
			return null;
		}

        log.info("去往任务排行页面");
        return "wx/task-ranking";
    }

    /**
     * 新增任务
     *
     * @param taskInfo
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @author zyh
     */
    @PostMapping("/wx/addTaskInfo")
    @RequiresPermissions("addTask")
    @ResponseBody
    public ResponseResult addTaskInfo(TaskInfo taskInfo, HttpServletRequest request, HttpServletResponse response) throws IOException {
        Object user = request.getSession().getAttribute("user");
        if (user == null) {
            String s = myWxService.getOauth2Service().buildAuthorizationUrl(request.getServletPath());
            response.sendRedirect(s);
            return null;
        }

        ResponseResult responseResult = null;
        try {
            wxTaskService.addTaskInfo(taskInfo, request);
            log.info("新增任务成功");
            responseResult = ResponseResult.success(ResponseResultEnum.SUCCESS.getCode(), null, "新增任务成功");
        } catch (Exception e) {
            e.printStackTrace();
            log.error("新增任务失败");
            responseResult = ResponseResult.failure("新增任务失败");
        }
        return responseResult;
    }

    /**
     * 获取所有具有子组织机构的组织机构
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @author zyh
     *//*
	@PostMapping("/wx/getOrganizationInfoThatHasSubOrganization")
	@ResponseBody
	public ResponseResult getOrganizationInfoThatHasSubOrganization(HttpServletRequest request,HttpServletResponse response) throws IOException {
		Object user = request.getSession().getAttribute("user");
		if (user == null){
			String s = myWxService.getOauth2Service().buildAuthorizationUrl(request.getServletPath());
			response.sendRedirect(s);
			return null;
		}
		ResponseResult responseResult = null;
		try {
			List<OrganizationInfo> list = wxTaskService.getOrganizationInfoThatHasSubOrganization();
			log.info("获取所有具有子组织机构的组织机构成功");
			responseResult = ResponseResult.success(ResponseResultEnum.SUCCESS.getCode(),list,"获取所有具有子组织机构的组织机构成功");
		} catch(Exception e){
		    e.printStackTrace();
		    log.error("获取所有具有子组织机构的组织机构失败");
		    responseResult = ResponseResult.failure("获取所有具有子组织机构的组织机构失败");
		}
		return responseResult;
	}

	*//**
     * 根据组织机构id获取子组织机构
     * @param pId
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @author zyh
     *//*
	@PostMapping("/wx/getOrganizationInfoByPid")
	@ResponseBody
	public ResponseResult getOrganizationInfoByPid(Long pId,HttpServletRequest request,HttpServletResponse response) throws IOException {
		Object user = request.getSession().getAttribute("user");
		if (user == null){
			String s = myWxService.getOauth2Service().buildAuthorizationUrl(request.getServletPath());
			response.sendRedirect(s);
			return null;
		}
		ResponseResult responseResult = null;
		try {
			List<OrganizationInfo> list = wxTaskService.getOrganizationInfoByPid(pId);
			log.info("根据组织机构id获取子组织机构成功");
			responseResult = ResponseResult.success(ResponseResultEnum.SUCCESS.getCode(),list,"根据组织机构id获取子组织机构成功");
		} catch(Exception e){
		    e.printStackTrace();
		    log.error("根据组织机构id获取子组织机构失败");
		    responseResult = ResponseResult.failure("根据组织机构id获取子组织机构失败");
		}
		return responseResult;
	}
*/

    /**
     * 获取紧急程度列表
     *
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @author zyh
     */
    @PostMapping("/wx/getExigencyInfoList")
    @ResponseBody
    public ResponseResult getExigencyInfoList(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Object user = request.getSession().getAttribute("user");
        if (user == null) {
            String s = myWxService.getOauth2Service().buildAuthorizationUrl(request.getServletPath());
            response.sendRedirect(s);
            return null;
        }
        ResponseResult responseResult = null;
        try {
            List<ExigencyInfo> list = wxTaskService.getExigencyInfoList();
            log.info("获取紧急程度列表");
            responseResult = ResponseResult.success(ResponseResultEnum.SUCCESS.getCode(), list, "获取紧急程度列表");
        } catch (Exception e) {
            e.printStackTrace();
            log.error("获取紧急程度列表失败");
            responseResult = ResponseResult.failure("获取紧急程度列表失败");
        }
        return responseResult;
    }

    /**
     * 根据任务编号获取任务信息
     *
     * @param id
     * @param request
     * @param response
     * @param map
     * @return
     * @throws IOException
     * @author zyh
     */
    @GetMapping("/wx/detailsOfPendingTasks")
    @RequiresPermissions("showTask")
    public String getDetailsOfPendingTasks(Long id, HttpServletRequest request, HttpServletResponse response, Map<String, Object> map) throws IOException {
        Object user = request.getSession().getAttribute("user");
        if (user == null) {
            String s = myWxService.getOauth2Service().buildAuthorizationUrl(request.getServletPath());
            response.sendRedirect(s);
            return null;
        }
        TaskInfo taskInfo = wxTaskService.getTaskInfoById(id);
        map.put("taskInfo", taskInfo);
        log.info("根据任务编号获取任务信息成功");
        return "wx/approval";
    }

    /**
     * 审批任务
     *
     * @param taskInfo
     * @param reason
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @author zyh
     */
    @PostMapping("/wx/approvalTasks")
    @RequiresPermissions("approveTask")
    @ResponseBody
    public ResponseResult approvalTasks(TaskInfo taskInfo, String reason, HttpServletRequest request, HttpServletResponse response) throws IOException {
        Object user = request.getSession().getAttribute("user");
        if (user == null) {
            String s = myWxService.getOauth2Service().buildAuthorizationUrl(request.getServletPath());
            response.sendRedirect(s);
            return null;
        }
        ResponseResult responseResult = null;
        try {
            wxTaskService.approvalTasks(taskInfo, reason, request);
            log.info("审批任务成功");
            responseResult = ResponseResult.success(ResponseResultEnum.SUCCESS.getCode(), null, "审批任务成功");
        } catch (Exception e) {
            e.printStackTrace();
            log.error("审批任务失败");
            responseResult = ResponseResult.failure("审批任务失败");
        }
        return responseResult;
    }

    /**
     * 根据主键获取未完成的任务信息
     *
     * @param id
     * @param map
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @author zyh
     */
    @GetMapping("/wx/notCompleteTaskDetails")
    @RequiresPermissions("showTask")
    public String getDetailsOfUnfinishedTasks(Long id, Map<String, Object> map, HttpServletRequest request, HttpServletResponse response) throws IOException {
        Object user = request.getSession().getAttribute("user");
        if (user == null) {
            String s = myWxService.getOauth2Service().buildAuthorizationUrl(request.getServletPath());
            response.sendRedirect(s);
            return null;
        }
        TaskInfo taskInfo = wxTaskService.getDetailsOfUnfinishedTasksById(id, request);
        map.put("taskInfo", taskInfo);
        log.info("根据主键获取未完成的任务信息");

        return "wx/task-info";
    }

    /**
     * 完成任务
     *
     * @param file
     * @param taskInfo
     * @param reason
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @author zyh
     */
    @PostMapping("/wx/completeTask")
    @RequiresPermissions("updateTask")
    @ResponseBody
    public ResponseResult completeTask(MultipartFile file, TaskInfo taskInfo, String reason, HttpServletRequest request, HttpServletResponse response) throws IOException {
        Object user = request.getSession().getAttribute("user");
        if (user == null) {
            String s = myWxService.getOauth2Service().buildAuthorizationUrl(request.getServletPath());
            response.sendRedirect(s);
            return null;
        }
        ResponseResult responseResult = null;
        try {
            wxTaskService.completeTask(file, taskInfo, reason, request);
            log.info("完成任务成功");
            responseResult = ResponseResult.success(ResponseResultEnum.SUCCESS.getCode(), null, "完成任务成功");
        } catch (Exception e) {
            e.printStackTrace();
            log.error("完成任务失败");
            responseResult = ResponseResult.failure("完成任务失败");
        }

        return responseResult;
    }

    /**
     * 获取已完成的任务的详情
     *
     * @param pid
     * @param request
     * @param response
     * @return
     * @author zyh
     */
    @GetMapping("/wx/getDetailsOfFinishedTasks")
    @RequiresPermissions("showTask")
    public String getDetailsOfFinishedTasks(Long pid, Map<String, Object> map, HttpServletRequest request, HttpServletResponse response) throws IOException {
        Object user = request.getSession().getAttribute("user");
        if (user == null) {
            String s = myWxService.getOauth2Service().buildAuthorizationUrl(request.getServletPath());
            response.sendRedirect(s);
            return null;
        }
        Map<String, Object> taskMap = wxTaskService.getDetailsOfFinishedTasksById(pid);
        TaskInfo taskInfo = (TaskInfo) taskMap.get("taskInfo");
        ApproverLog approverLog = (ApproverLog) taskMap.get("approverLog");
        ApproverLog completeLog = (ApproverLog) taskMap.get("completeLog");
        System.out.println(taskInfo);
        System.out.println(approverLog);
        System.out.println(completeLog);
        map.put("taskInfo", taskInfo);
        map.put("approverLog", approverLog);
        map.put("completeLog", completeLog);
        log.info("获取已完成的任务的详情成功");
        return "wx/task-over-info";
    }

    /**
     * 回显图片
     *
     * @param file
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @author zyh
     */
    @PostMapping("/wx/echoPicture")
    @ResponseBody
    public ResponseResult echoPicture(MultipartFile file, HttpServletRequest request, HttpServletResponse response) throws IOException {
        Object user = request.getSession().getAttribute("user");
        if (user == null) {
            String s = myWxService.getOauth2Service().buildAuthorizationUrl(request.getServletPath());
            response.sendRedirect(s);
            return null;
        }
        ResponseResult responseResult = null;
        try {
            String s = wxTaskService.echoPicture(file);
            log.info("回显图片成功");
            responseResult = ResponseResult.success(ResponseResultEnum.SUCCESS.getCode(), s, "回显图片成功");
        } catch (Exception e) {
            e.printStackTrace();
            log.error("回显图片失败");
            responseResult = ResponseResult.failure("回显图片失败");
        }
        return responseResult;
    }

    /**
     * 根据任务编号和该任务所处的状态获取不同的任务详情
     *
     * @param id
     * @param map
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @author zyh
     */
    @GetMapping("/wx/getMyTaskInfo")
    @RequiresPermissions("showTask")
    public String getMyTaskInfo(Long id, Map<String, Object> map, HttpServletRequest request, HttpServletResponse response) throws IOException {
        Object user = request.getSession().getAttribute("user");
        if (user == null) {
            String s = myWxService.getOauth2Service().buildAuthorizationUrl(request.getServletPath());
            response.sendRedirect(s);
            return null;
        }
        Map<String, Object> myTaskInfoMap = wxTaskService.getMyTaskInfo(id);
        map.put("myTaskInfoMap", myTaskInfoMap);
        TaskInfo taskInfo = (TaskInfo) myTaskInfoMap.get("taskInfo");
        log.info("根据任务编号和该任务所处的状态获取不同的任务详情成功");
        if (taskInfo.getStatus() == 1) {
            return "wx/myTaskNotApproval";
        } else if (taskInfo.getStatus() == 2) {
            return "wx/myTaskApproval";
        } else if (taskInfo.getStatus() == 3) {
            return "wx/myTaskComplete";
        } else if (taskInfo.getStatus() == 4) {
            return "wx/myTaskEnd";
        } else if (taskInfo.getStatus() == 5) {
            return "wx/myTaskReject";
        } else {
            return "wx/myTaskDiscard";
        }
    }

    /**
     * 确认完成任务
     *
     * @param taskInfo
     * @param request
     * @param response
     * @return
     * @author zyh
     */
    @PostMapping("/wx/confirmationOfCompletion")
    @RequiresPermissions("updateTask")
    @ResponseBody
    public ResponseResult confirmationOfCompletion(TaskInfo taskInfo, HttpServletRequest request, HttpServletResponse response) throws IOException {
        Object user = request.getSession().getAttribute("user");
        if (user == null) {
            String s = myWxService.getOauth2Service().buildAuthorizationUrl(request.getServletPath());
            response.sendRedirect(s);
            return null;
        }
        ResponseResult responseResult = null;
        try {
            wxTaskService.confirmationOfCompletion(taskInfo, request);
            log.info("确认完成任务成功");
            responseResult = ResponseResult.success(ResponseResultEnum.SUCCESS.getCode(), null, "确认完成任务成功");
        } catch (Exception e) {
            e.printStackTrace();
            log.error("确认完成任务失败");
            responseResult = ResponseResult.failure("确认完成任务失败");
        }
        return responseResult;
    }

    /**
     * 任务再次提交
     *
     * @param taskInfo
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @author zyh
     */
    @PostMapping("/wx/taskResubmit")
    @RequiresPermissions("addTask")
    @ResponseBody
    public ResponseResult taskResubmit(TaskInfo taskInfo, HttpServletRequest request, HttpServletResponse response) throws IOException {
        Object user = request.getSession().getAttribute("user");
        if (user == null) {
            String s = myWxService.getOauth2Service().buildAuthorizationUrl(request.getServletPath());
            response.sendRedirect(s);
            return null;
        }
        ResponseResult responseResult = null;
        try {
            wxTaskService.taskResubmit(taskInfo, request);
            log.info("任务再次提交成功");
            responseResult = ResponseResult.success(ResponseResultEnum.SUCCESS.getCode(), null, "任务再次提交成功");
        } catch (Exception e) {
            e.printStackTrace();
            log.error("任务再次提交失败");
            responseResult = ResponseResult.failure("任务再次提交失败");
        }
        return responseResult;
    }

    /**
     * 任务废弃
     *
     * @param taskInfo
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @author zyh
     */
    @PostMapping("/wx/taskDiscard")
    @RequiresPermissions("updateTask")
    @ResponseBody
    public ResponseResult taskDiscard(TaskInfo taskInfo, HttpServletRequest request, HttpServletResponse response) throws IOException {
        Object user = request.getSession().getAttribute("user");
        if (user == null) {
            String s = myWxService.getOauth2Service().buildAuthorizationUrl(request.getServletPath());
            response.sendRedirect(s);
            return null;
        }
        ResponseResult responseResult = null;
        try {
            wxTaskService.taskDiscard(taskInfo, request);
            log.info("任务废弃成功");
            responseResult = ResponseResult.success(ResponseResultEnum.SUCCESS.getCode(), null, "任务废弃成功");
        } catch (Exception e) {
            e.printStackTrace();
            log.error("任务废弃失败");
            responseResult = ResponseResult.failure("任务废弃失败");
        }
        return responseResult;
    }

    /**
     * 获取超时完成和提前完成的任务排行信息
     *
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @author zyh
     */
    @PostMapping("/wx/getOverTimeAndAdvanceTaskRankingList")
	@RequiresPermissions("showTask")
    @ResponseBody
    public ResponseResult getOverTimeAndAdvanceTaskRankingList(HttpServletRequest request, HttpServletResponse response) throws IOException {
		Object user = request.getSession().getAttribute("user");
		if (user == null){
			String s = myWxService.getOauth2Service().buildAuthorizationUrl(request.getServletPath());
			response.sendRedirect(s);
			return null;
		}
        ResponseResult responseResult = null;
        try {
            List<Object> list = wxTaskService.getOverTimeAndAdvanceTaskRankingList();
            log.info("获取超时完成和提前完成的任务排行信息成功");
            responseResult = ResponseResult.success(ResponseResultEnum.SUCCESS.getCode(), list, "获取超时完成和提前完成的任务排行信息成功");
        } catch (Exception e) {
            e.printStackTrace();
            log.error("获取超时完成和提前完成的任务排行信息失败");
            responseResult = ResponseResult.failure("获取超时完成和提前完成的任务排行信息失败");
        }
        return responseResult;
    }


    /**
     * 获取待审批消息中的任务信息
     *
     * @param id
     * @param request
     * @param response
     * @param map
     * @return
     * @throws IOException
     * @author zyh
     */
    @GetMapping("/wx/getApprovalTaskNoticeInfo")
    public String getApprovalTaskNoticeInfo(Long id, HttpServletRequest request, HttpServletResponse response, Map<String, Object> map) throws IOException {
        Object user = request.getSession().getAttribute("user");
        if (user == null) {
            String s = myWxService.getOauth2Service().buildAuthorizationUrl(request.getServletPath() + "?id=" + id);
            response.sendRedirect(s);
            return null;
        }
        TaskInfoNotice taskInfoNotice = taskInfoNoticeService.selectByPrimaryKey(id);
        if (taskInfoNotice.getIsRender() == 0) {
            taskInfoNotice.setIsRender(1);
            taskInfoNotice.setRenderTime(new Date());
            taskInfoNoticeService.updateByPrimaryKeySelective(taskInfoNotice);
        }
        TaskInfo taskInfo = wxTaskService.getTaskInfoById(taskInfoNotice.getTaskId());
        if (taskInfo.getStatus() == 1) {
            log.info("获取待审批消息中的任务信息");
            map.put("taskInfo", taskInfo);
            return "wx/approvalTaskNotice";
        } else {
            map.put("status", 1);
            return "wx/task-error";
        }

    }

    /**
     * 获取待完成消息中的任务信息
     *
     * @param id
     * @param request
     * @param response
     * @param map
     * @return
     * @throws IOException
     * @author zyh
     */
    @GetMapping("/wx/getNotCompleteTaskNoticeInfo")
    public String getNotCompleteTaskNoticeInfo(Long id, HttpServletRequest request, HttpServletResponse response, Map<String, Object> map) throws IOException {
        Object user = request.getSession().getAttribute("user");
        if (user == null) {
            String s = myWxService.getOauth2Service().buildAuthorizationUrl(request.getServletPath() + "?id=" + id);
            response.sendRedirect(s);
            return null;
        }
        TaskInfoNotice taskInfoNotice = taskInfoNoticeService.selectByPrimaryKey(id);
        if (taskInfoNotice.getIsRender() == 0) {
            taskInfoNotice.setIsRender(1);
            taskInfoNotice.setRenderTime(new Date());
            taskInfoNoticeService.updateByPrimaryKeySelective(taskInfoNotice);
        }
        TaskInfo taskInfo = wxTaskService.getDetailsOfUnfinishedTasksById(taskInfoNotice.getTaskId(), request);
        if (taskInfo.getStatus() == 2) {
            log.info("获取待完成消息中的任务信息");
            map.put("taskInfo", taskInfo);
            return "wx/completeTaskNotice";
        } else {
            map.put("status", 2);
            return "wx/task-error";
        }

    }


    /**
     * 获取已完成消息中的任务信息
     *
     * @param id
     * @param request
     * @param response
     * @param map
     * @return
     * @throws IOException
     * @author zyh
     */
    @GetMapping("/wx/getCompleteTaskNoticeInfo")
    public String getCompleteTaskNoticeInfo(Long id, HttpServletRequest request, HttpServletResponse response, Map<String, Object> map) throws IOException {
        Object user = request.getSession().getAttribute("user");
        if (user == null) {
            String s = myWxService.getOauth2Service().buildAuthorizationUrl(request.getServletPath() + "?id=" + id);
            response.sendRedirect(s);
            return null;
        }
        TaskInfoNotice taskInfoNotice = taskInfoNoticeService.selectByPrimaryKey(id);
        if (taskInfoNotice.getIsRender() == 0) {
            taskInfoNotice.setIsRender(1);
            taskInfoNotice.setRenderTime(new Date());
            taskInfoNoticeService.updateByPrimaryKeySelective(taskInfoNotice);
        }
        Map<String, Object> myTaskInfoMap = wxTaskService.getMyTaskInfo(taskInfoNotice.getTaskId());
        TaskInfo taskInfo = (TaskInfo) myTaskInfoMap.get("taskInfo");
        if (taskInfo.getStatus() == 3) {
            log.info("获取已完成消息中的任务信息");
            map.put("myTaskInfoMap", myTaskInfoMap);
            return "wx/endTaskNotice";
        } else {
            map.put("status", 3);
            return "wx/task-error";
        }

    }

    /**
     * 获取驳回消息中的任务信息
     *
     * @param id
     * @param request
     * @param response
     * @param map
     * @return
     * @throws IOException
     * @author zyh
     */
    @GetMapping("/wx/getRejectTaskNoticeInfo")
    public String getRejectTaskNoticeInfo(Long id, HttpServletRequest request, HttpServletResponse response, Map<String, Object> map) throws IOException {
        Object user = request.getSession().getAttribute("user");
        if (user == null) {
            String s = myWxService.getOauth2Service().buildAuthorizationUrl(request.getServletPath() + "?id=" + id);
            response.sendRedirect(s);
            return null;
        }
        TaskInfoNotice taskInfoNotice = taskInfoNoticeService.selectByPrimaryKey(id);
        if (taskInfoNotice.getIsRender() == 0) {
            taskInfoNotice.setIsRender(1);
            taskInfoNotice.setRenderTime(new Date());
            taskInfoNoticeService.updateByPrimaryKeySelective(taskInfoNotice);
        }
        Map<String, Object> myTaskInfoMap = wxTaskService.getMyTaskInfo(taskInfoNotice.getTaskId());
        TaskInfo taskInfo = (TaskInfo) myTaskInfoMap.get("taskInfo");
        if (taskInfo.getStatus() == 5) {
            log.info("获取驳回消息中的任务信息");
            map.put("myTaskInfoMap", myTaskInfoMap);
            return "wx/rejectTaskNotice";
        } else {
            map.put("status", 4);
            return "wx/task-error";
        }

    }

    /**
     * 获取任务消息信息对应的任务列表
     *
     * @param id
     * @param request
     * @param response
     * @param map
     * @return
     * @throws IOException
     * @author zyh
     */
    @GetMapping("/wx/getTaskMessageList")
    public String getTaskMessageList(Long id, HttpServletRequest request, HttpServletResponse response, Map<String, Object> map) throws IOException {
        Object user = request.getSession().getAttribute("user");
        if (user == null) {
            String s = myWxService.getOauth2Service().buildAuthorizationUrl(request.getServletPath() + "?id=" + id);
            response.sendRedirect(s);
            return null;
        }
        TaskInfoNotice taskInfoNotice = taskInfoNoticeService.selectByPrimaryKey(id);
        if (taskInfoNotice.getIsRender() == 0) {
            taskInfoNotice.setIsRender(1);
            taskInfoNotice.setRenderTime(new Date());
            taskInfoNoticeService.updateByPrimaryKeySelective(taskInfoNotice);
        }

        if (taskInfoNotice.getTaskId() == null) {
            //发给任务负责部门领导的信息
            List<Long> orgIdsByLeaderId = organizationInfoService.getOrgIdsByLeaderId(taskInfoNotice.getUserId());
            List<String> realOrgIdsByLeaderId = new ArrayList<>();
            for (Long oid : orgIdsByLeaderId) {
                realOrgIdsByLeaderId.add(String.valueOf(oid));
            }
            if (taskInfoNotice.getIsOvertime() == 1) {
                //超时
                List<TaskOverTime> list = taskOverTimeService.getTaskOverTimeListByDeptsAndDate(realOrgIdsByLeaderId, taskInfoNotice.getCreatTime());
                map.put("list", list);
            } else {
                //没超时
                List<TaskRemind> list = taskRemindService.getTaskRemindListByByDeptsAndDate(realOrgIdsByLeaderId, taskInfoNotice.getCreatTime());
                map.put("list", list);
            }
        } else {
            //发给任务负责部门的父部门领导的信息
            //获取改领导所领导部门的所有子部门编号
            List<String> subOrgIdsByParentLeaderId = wxTaskService.getSubOrgIdsByParentLeaderId(taskInfoNotice.getUserId());
            if (taskInfoNotice.getIsOvertime() == 1) {
                //超时
                List<TaskOverTime> list = taskOverTimeService.getTaskOverTimeListByDeptsAndDate(subOrgIdsByParentLeaderId, taskInfoNotice.getCreatTime());
                map.put("list", list);
            } else {
                //没超时
                List<TaskRemind> list = taskRemindService.getTaskRemindListByByDeptsAndDate(subOrgIdsByParentLeaderId, taskInfoNotice.getCreatTime());
                map.put("list", list);
            }

        }

        map.put("taskInfoNotice", taskInfoNotice);
        return "wx/task-notice-list";

    }

    /**
     * 获取任务消息列表中的指定任务的详情
     *
     * @param id
     * @param map
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @author zyh
     */
    @GetMapping("/wx/getTaskNoticeListDetails")
    public String getTaskNoticeListDetails(Long id, Long taskNoticeId, Map<String, Object> map, HttpServletRequest request, HttpServletResponse response) throws IOException {
        Object user = request.getSession().getAttribute("user");
        if (user == null) {
            String s = myWxService.getOauth2Service().buildAuthorizationUrl(request.getServletPath());
            response.sendRedirect(s);
            return null;
        }
        TaskInfo taskInfo = wxTaskService.getDetailsOfUnfinishedTasksById(id, request);
        map.put("taskInfo", taskInfo);
        map.put("taskNoticeId", taskNoticeId);
        log.info("根据主键获取未完成的任务信息");
        return "wx/task-notice-info";
    }


    /**
     * 修改提醒或超时任务消息状态
     *
     * @param id           任务主键
     * @param taskNoticeId 任务消息主键
     * @param map
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @author zyh
     */
    @GetMapping("/wx/updateRemindOrOvertimeTaskStatus")
    public String updateRemindOrOvertimeTaskStatus(Long id, Long taskNoticeId, Map<String, Object> map, HttpServletRequest request, HttpServletResponse response) throws IOException {
        Object user = request.getSession().getAttribute("user");
        if (user == null) {
            String s = myWxService.getOauth2Service().buildAuthorizationUrl(request.getServletPath());
            response.sendRedirect(s);
            return null;
        }
        TaskInfoNotice taskInfoNotice = taskInfoNoticeService.selectByPrimaryKey(taskNoticeId);
        if (taskInfoNotice.getIsOvertime() == 1) {
            //超时
            taskOverTimeService.updateTaskOverTimeStatusByTaskIdAndCreatTime(id, taskInfoNotice.getCreatTime());
        } else {
            //没超时
            taskRemindService.updateTaskRemindStatusByTaskIdAndCreatTime(id, taskInfoNotice.getCreatTime());
        }
        return "redirect:/wx/getTaskMessageList?id=" + taskNoticeId;
    }

    /**
     * @param deptName :
     * @param map      :
     * @param request  :
     * @param response :
     * @return : java.lang.String
     * @throws :
     * @Description : 去往超时任务列表页面
     * @methodName : toTaskOverTimeListPage
     * @author : zyh
     */
    @GetMapping("/wx/toTaskOverTimeListPage")
    public String toTaskOverTimeListPage(String deptName, Map<String, Object> map, HttpServletRequest request, HttpServletResponse response) throws IOException {
        /*Object user = request.getSession().getAttribute("user");
        if (user == null) {
            String s = myWxService.getOauth2Service().buildAuthorizationUrl(request.getServletPath());
            response.sendRedirect(s);
            return null;
        }*/
        List<TaskInfo> list = wxTaskService.getOverTimeTaskInfoByDeptName(deptName);
        map.put("list", list);
        return "wx/ranking-overtime-list.html";
    }

    /**
     * @param deptName :
     * @param map      :
     * @param request  :
     * @param response :
     * @return : java.lang.String
     * @throws :
     * @Description : 去往提前任务列表页面
     * @methodName : toTaskAdvanceListPage
     * @author : zyh
     */
    @GetMapping("/wx/toTaskAdvanceListPage")
    public String toTaskAdvanceListPage(String deptName, Map<String, Object> map, HttpServletRequest request, HttpServletResponse response) throws IOException {
        Object user = request.getSession().getAttribute("user");
        if (user == null) {
            String s = myWxService.getOauth2Service().buildAuthorizationUrl(request.getServletPath());
            response.sendRedirect(s);
            return null;
        }
        List<TaskInfo> list = wxTaskService.getAdvanceTaskInfoByDeptName(deptName);
        map.put("list", list);
        return "wx/ranking-advance-list";
    }

    /**
     * @Description : 去往排行任务详情页面
     * @methodName : toRankingOvertimeTaskInfoPage
     * @param id :
     * @param map :
     * @param request :
     * @param response :
     * @return : java.lang.String
     * @exception :
     * @author : zyh
     */
    @GetMapping("/wx/toRankingOvertimeTaskInfoPage")
    public String toRankingOvertimeTaskInfoPage(Long id,Map<String, Object> map,HttpServletRequest request, HttpServletResponse response) throws IOException {
        Object user = request.getSession().getAttribute("user");
        if (user == null) {
            String s = myWxService.getOauth2Service().buildAuthorizationUrl(request.getServletPath());
            response.sendRedirect(s);
            return null;
        }
        Map<String, Object> myTaskInfoMap = wxTaskService.getMyTaskInfo(id);
        map.put("myTaskInfoMap", myTaskInfoMap);
        return "wx/ranking-overtime-task-info";
    }

    @GetMapping("/wx/toRankingAdvanceTaskInfoPage")
    public String toRankingAdvanceTaskInfoPage(Long id,Map<String, Object> map,HttpServletRequest request, HttpServletResponse response) throws IOException {
        Object user = request.getSession().getAttribute("user");
        if (user == null) {
            String s = myWxService.getOauth2Service().buildAuthorizationUrl(request.getServletPath());
            response.sendRedirect(s);
            return null;
        }
        Map<String, Object> myTaskInfoMap = wxTaskService.getMyTaskInfo(id);
        map.put("myTaskInfoMap", myTaskInfoMap);
        return "wx/ranking-advance-task-info";
    }

    /**
     * @Description : 去往需要确认完成的任务列表页面
     * @methodName : toConfirmCompletionListPage
     * @param request :
     * @param response :
     * @return : java.lang.String
     * @exception :
     * @author : zyh
     */
    @GetMapping("/wx/toConfirmCompletionListPage")
    public String toConfirmCompletionListPage(Map<String,Object> map,HttpServletRequest request, HttpServletResponse response) throws IOException {
        Object user = request.getSession().getAttribute("user");
        if (user == null) {
            String s = myWxService.getOauth2Service().buildAuthorizationUrl(request.getServletPath());
            response.sendRedirect(s);
            return null;
        }
        TaskInfo taskInfo = new TaskInfo();
        taskInfo.setStatus(3);
        List<TaskInfo> list = wxTaskService.getTaskInfoListByStatus(taskInfo, request);
        map.put("list",list);
        return "wx/confirm-completion-list";
    }

    @GetMapping("/wx/getConfirmCompletionTaskInfo")
    public String getConfirmCompletionTaskInfo(Long id,Map<String,Object> map,HttpServletRequest request, HttpServletResponse response) throws IOException {
        Object user = request.getSession().getAttribute("user");
        if (user == null) {
            String s = myWxService.getOauth2Service().buildAuthorizationUrl(request.getServletPath());
            response.sendRedirect(s);
            return null;
        }
        Map<String, Object> myTaskInfoMap = wxTaskService.getMyTaskInfo(id);
        map.put("myTaskInfoMap", myTaskInfoMap);
        return "wx/confirm-completion-info";
    }

    /**
     * @Description : 取消任务
     * @methodName : cancelTask
     * @param id :
     * @param request :
     * @param response :
     * @return : com.xwkj.ygjces.common.ResponseResult
     * @exception :
     * @author : zyh
     */
    @PostMapping("/wx/cancelTask")
    @ResponseBody
    public ResponseResult cancelTask(Long id,HttpServletRequest request, HttpServletResponse response) throws IOException {
        Object user = request.getSession().getAttribute("user");
        if (user == null) {
            String s = myWxService.getOauth2Service().buildAuthorizationUrl(request.getServletPath());
            response.sendRedirect(s);
            return null;
        }
        ResponseResult responseResult = null;
        try {
            wxTaskService.cancelTask(id);
            responseResult = ResponseResult.success(ResponseResultEnum.SUCCESS.getCode(),null,"取消任务成功");
        } catch(Exception e){
            e.printStackTrace();
            responseResult = ResponseResult.failure("取消任务失败");
        }
        return responseResult;
    }
}
