package com.xwkj.ygjces.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.xwkj.ygjces.Mapper.OrganizationInfoMapperManual;
import com.xwkj.ygjces.Mapper.UserInfoMapper;
import com.xwkj.ygjces.Mapper.auto.OrganizationInfoMapper;
import com.xwkj.ygjces.model.OrganizationInfo;
import com.xwkj.ygjces.model.UserInfo;
import com.xwkj.ygjces.service.UserInfoService;
import com.xwkj.ygjces.service.WxService;
import com.xwkj.ygjces.utils.AesException;
import com.xwkj.ygjces.utils.WXBizMsgCrypt;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.cp.api.impl.WxCpServiceImpl;
import me.chanjar.weixin.cp.bean.WxCpUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@PropertySource(value = {"classpath:/config/wxConfig.properties"})
@Service
public class WxServiceImpl extends WxCpServiceImpl implements WxService {

	@Value("${wx.corpId}")
	protected volatile String corpId;
	@Value("${wx.token}")
	protected volatile String token;
	@Value("${wx.aes-key}")
	protected volatile String aesKey;
	@Autowired
	UserInfoService userInfoService;
	@Autowired
	UserInfoMapper userInfoMapper;
	@Autowired
	OrganizationInfoMapper organizationInfoMapper;
	@Autowired
	OrganizationInfoMapperManual organizationInfoMapperManual;

	/**
	 * 处理微信通讯录回调通知
	 * @param request
	 * @param response
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void callbackNotification(HttpServletRequest request, HttpServletResponse response) throws AesException, IOException {
		WXBizMsgCrypt wxBizMsgCrypt = new WXBizMsgCrypt(token, aesKey, corpId);
		String msgSignature = request.getParameter("msg_signature");
		String timestamp = request.getParameter("timestamp");
		String nonce = request.getParameter("nonce");
		String postData = wxBizMsgCrypt.readFromStream(request.getInputStream());
		String s = wxBizMsgCrypt.DecryptMsg(msgSignature, timestamp, nonce, postData);
		System.out.println(s);

		Map<String, String> map = wxBizMsgCrypt.toMap(s);
		String changeType = map.get("ChangeType");
		if("create_user".equals(changeType)){
			//添加用户
			String trueName = map.get("Name");
			String department = map.get("Department");
			String isLeader = map.get("IsLeader");
			String mobile = map.get("Mobile");
			String account = map.get("UserID");
			UserInfo userInfo = new UserInfo();
			userInfo.setAccount(account);
			userInfo.setMobile(mobile);
			userInfo.setTrueName(trueName);
			//默认登录密码为123456
			userInfo.setPassword("123456");
			userInfoService.addAdminFromWx(userInfo,department);
			if (Integer.parseInt(isLeader)==1){
				//给该用户设置领导
				//存放该用户领导的部门id
				List<String> list = new ArrayList<>();
				String isLeaderInDept = map.get("IsLeaderInDept");
				String[] leaders = isLeaderInDept.split(",");
				String[] depts = department.split(",");
				for (int i = 0; i < leaders.length ; i++) {
					if (Integer.parseInt(leaders[i])==1){
						list.add(depts[i]);
					}
				}
				for (String id : list) {
					organizationInfoMapperManual.addLeaderToOrganization(userInfo.getId(),Long.parseLong(id));
				}
			}
			return;
		}else if ("update_user".equals(changeType)){
			//修改用户
			String account = map.get("UserID");
			String trueName = map.get("Name");
			String department = map.get("Department");
			String isLeader = map.get("IsLeader");
			String mobile = map.get("Mobile");
			String status = map.get("Status");
			UserInfo userInfoByAcount = userInfoMapper.getUserInfoByAcount(account);
			UserInfo userInfo = new UserInfo();
			Date date = new Date();
			userInfo.setId(userInfoByAcount.getId());
			if(department!=null){
				userInfoService.updateWxUserFromOrg(department,userInfoByAcount.getId());
				if (Integer.parseInt(isLeader)==1){
					//把该用户所领导的部门都去除，然后再重新加入他所领导的部门
					organizationInfoMapperManual.deleteAllOrgFromLeaderById(userInfo.getId());
					//存放该用户领导的部门id
					List<String> list = new ArrayList<>();
 					String isLeaderInDept = map.get("IsLeaderInDept");
					String[] leaders = isLeaderInDept.split(",");
					String[] depts = department.split(",");
					for (int i = 0; i < leaders.length ; i++) {
						if (Integer.parseInt(leaders[i])==1){
							list.add(depts[i]);
						}
					}
					for (String id : list) {
						organizationInfoMapperManual.addLeaderToOrganization(userInfo.getId(),Long.parseLong(id));
					}
				}
			}
			if(status!=null){
				if("2".equals(status)){
					userInfo.setEnable(0);
				}else{
					userInfo.setEnable(1);
				}
			}
			if(trueName!=null){
				userInfo.setTrueName(trueName);
			}
			if(mobile!=null){
				userInfo.setMobile(mobile);
			}
			userInfo.setLastModifiedTime(date);
			userInfo.setLastModifier(Long.MAX_VALUE);
			userInfoMapper.updateByPrimaryKeySelective(userInfo);
			return;
		}else if ("delete_user".equals(changeType)){
			//删除用户
			String account = map.get("UserID");
			UserInfo userInfoByAcount = userInfoMapper.getUserInfoByAcount(account);
			UserInfo userInfo = new UserInfo();
			userInfo.setId(userInfoByAcount.getId());
			userInfo.setEnable(0);
			Date date = new Date();
			userInfo.setLastModifiedTime(date);
			userInfo.setLastModifier(Long.MAX_VALUE);
			userInfoMapper.updateByPrimaryKeySelective(userInfo);
			return;
		}else if ("create_party".equals(changeType)){
			//添加组织机构
			String parentId = map.get("ParentId");
			String id = map.get("Id");
			String name = map.get("Name");
			OrganizationInfo organizationInfo = new OrganizationInfo();
			organizationInfo.setId(Long.parseLong(id));
			organizationInfo.setName(name);
			organizationInfo.setpId(Long.parseLong(parentId));
			organizationInfo.setIsenablez(1);
			organizationInfoMapper.insertSelective(organizationInfo);
			return;
		}else if ("update_party".equals(changeType)){
			//修改组织机构
			String id = map.get("Id");
			String name = map.get("Name");
			OrganizationInfo organizationInfo = new OrganizationInfo();
			organizationInfo.setId(Long.parseLong(id));
			organizationInfo.setName(name);
			organizationInfoMapper.updateByPrimaryKeySelective(organizationInfo);
			return;
		}else if ("delete_party".equals(changeType)){
			//删除组织机构
			String id = map.get("Id");
			OrganizationInfo organizationInfo = new OrganizationInfo();
			organizationInfo.setId(Long.parseLong(id));
			organizationInfo.setIsenablez(0);
			organizationInfoMapper.updateByPrimaryKeySelective(organizationInfo);
			//为防止因为假删除数据造成后台与微信端的数据冲突进行的修改主键以及子节点父id的操作
			Long maxPrimaryKey = organizationInfoMapperManual.getMaxPrimaryKey();
			organizationInfoMapperManual.updatePrimaryKeyByKey(Long.parseLong(id),maxPrimaryKey+1);
			organizationInfoMapperManual.updatePidByPrimaryKey(Long.parseLong(id),maxPrimaryKey+1);
		}
	}


}
