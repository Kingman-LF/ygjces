package com.xwkj.ygjces.service;

import com.xwkj.ygjces.model.ExigencyInfo;
import com.xwkj.ygjces.model.OrganizationInfo;
import com.xwkj.ygjces.model.TaskInfo;
import me.chanjar.weixin.common.error.WxErrorException;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface WxTaskService {


	/**
	 * 新增任务
	 * @param taskInfo
	 * @param request
	 * @author zyh
	 */
	public void addTaskInfo(TaskInfo taskInfo,HttpServletRequest request) throws WxErrorException;

	/**
	 * 获取所有具有子组织机构的组织机构
	 * @return
	 * @author zyh
	 */
	List<OrganizationInfo> getOrganizationInfoThatHasSubOrganization();

	/**
	 * 根据组织机构id获取子组织机构
	 * @param pId
	 * @return
	 * @author zyh
	 */
	List<OrganizationInfo> getOrganizationInfoByPid(Long pId);

	/**
	 * 获取紧急程度列表
	 * @return
	 * @author zyh
	 */
	List<ExigencyInfo> getExigencyInfoList();

	/**
	 * 获取待审批的任务列表
	 * @return
	 * @author zyh
	 */
	List<TaskInfo> getTaskInfoToApproval();

	/**
	 * 根据任务编号获取任务信息
	 * @param id
	 * @return
	 * @author zyh
	 */
	TaskInfo getTaskInfoById(Long id);

	/**
	 * 审批任务
	 * @param taskInfo
	 * @param reason
	 * @param request
	 * @author zyh
	 */
	void approvalTasks(TaskInfo taskInfo,String reason, HttpServletRequest request) throws WxErrorException;

	/**
	 * 根据当前用户所在部门获取未完成的任务信息和已完成的任务信息
	 * @param request
	 * @return
	 * @author zyh
	 */
	Map<String,Object> getTaskInfoByUserDepartments(HttpServletRequest request);

	/**
	 * 根据主键获取未完成的任务信息
	 * @param id
	 * @param request
	 * @return
	 * @author zyh
	 */
	TaskInfo getDetailsOfUnfinishedTasksById(Long id,HttpServletRequest request);

	/**
	 * 完成任务
	 * @param file
	 * @param taskInfo
	 * @param reason
	 * @param request
	 * @author zyh
	 */
	void completeTask(MultipartFile file, TaskInfo taskInfo , String reason, HttpServletRequest request) throws IOException, WxErrorException;


	/**
	 * 根据主键获取已经完成的任务的相关信息
	 * @param id
	 * @return
	 * @author zyh
	 */
	Map<String,Object> getDetailsOfFinishedTasksById(Long id);

    /**
     * 回显图片
     * @param file
     * @return
     * @author zyh
     */
	String  echoPicture(MultipartFile file) throws IOException;

	/**
	 * 根据任务状态获取任务列表
	 * @param taskInfo
	 * @param request
	 * @return
	 * @author zyh
	 */
	List<TaskInfo> getTaskInfoListByStatus(TaskInfo taskInfo,HttpServletRequest request);

	/**
	 * 根据任务编号和该任务所处的状态获取不同的任务详情
	 * @param id
	 * @return
	 * @author zyh
	 */
	Map<String,Object> getMyTaskInfo(Long id);


	/**
	 * 确认完成任务
	 * @param taskInfo
	 * @param request
	 * @author zyh
	 */
	void confirmationOfCompletion(TaskInfo taskInfo , HttpServletRequest request);

	/**
	 * 任务再次提交
	 * @param taskInfo
	 * @param request
	 * @author zyh
	 */
	void taskResubmit(TaskInfo taskInfo,HttpServletRequest request) throws WxErrorException;

	/**
	 * 任务废弃
	 * @param taskInfo
	 * @param request
	 * @author zyh
	 */
	void taskDiscard(TaskInfo taskInfo,HttpServletRequest request);

	/**
	 * 获取超时完成和提前完成的任务排行信息
	 * @return
	 * @author zyh
	 */
	List<Object> getOverTimeAndAdvanceTaskRankingList();

	/**
	 * 根据父部门领导编号获取获取其领导部门的所有子部门
	 * @param id
	 * @return
	 * @author zyh
	 */
	List<String> getSubOrgIdsByParentLeaderId(Long id);

	/**
	 * @Description : 根据部门名称获取超时完成的任务列表
	 * @methodName : getOverTimeTaskInfoByDeptName
	 * @param deptName : 
	 * @return : java.util.List<com.xwkj.ygjces.model.TaskInfo>
	 * @exception : 
	 * @author : zyh
	 */
	List<TaskInfo> getOverTimeTaskInfoByDeptName(String deptName);
	
	/**
	 * @Description : 根据部门名称获取提前完成的任务列表
	 * @methodName : getAdvanceTaskInfoByDeptName
	 * @param deptName : 
	 * @return : java.util.List<com.xwkj.ygjces.model.TaskInfo>
	 * @exception : 
	 * @author : zyh
	 */
	List<TaskInfo> getAdvanceTaskInfoByDeptName(String deptName);

	/**
	 * @Description : 取消任务
	 * @methodName : cancelTask
	 * @param id :
	 * @return : void
	 * @exception :
	 * @author : zyh
	 */
	void cancelTask(Long id);
}
