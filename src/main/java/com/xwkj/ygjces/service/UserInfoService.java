package com.xwkj.ygjces.service;

import com.github.pagehelper.PageInfo;
import com.xwkj.ygjces.model.OrganizationInfo;
import com.xwkj.ygjces.model.RoleInfo;
import com.xwkj.ygjces.model.UserInfo;
import me.chanjar.weixin.common.error.WxErrorException;
import org.apache.ibatis.annotations.Param;

import javax.management.relation.Role;
import java.util.ArrayList;
import java.util.List;

public interface UserInfoService {

    /**
     *
     * @param account
     * @return
     */
    UserInfo getUserInfoByAcount(String account);

    /**
     * 根据用户Id查询角色信息
     * @param userId
     * @return
     * @author wanglei
     */
    public List<RoleInfo> findRolesByUserId(Long userId);

    /**
     *返回用户信息查询是够是审批人
     * @param page
     * @param limit
     */
    public PageInfo<UserInfo> getUserInfoByApprovelId(UserInfo userInfo,Integer page,Integer limit);


    /**
     * 获取用户列表
     * 可以通过账号，用户名，企业openId,
     * 必填是否启用
     * @param userInfo
     * @param pageNum
     * @param pageSize
     * @return
     */
    public PageInfo<UserInfo> getUserInfoList(UserInfo userInfo, Integer pageNum, Integer pageSize);

    /**
     * 获取登录用户信息
     * @param userName
     * @param password
     * @return
     */
    public UserInfo checkUserInfo(String userName, String password);


    /**
     * 通过组织结构ID查询对应的用户列表
     * @param organizationInfo
     * @return
     * @author zyh
     */
    public PageInfo<UserInfo> getUserInfoByOrganizationId(OrganizationInfo organizationInfo, Integer pageNum, Integer pageSize);

    /**
     * 通过人员名称查询对应的用户列表
     * @param trueName
     * @param pageNum
     * @param pageSize
     * @return
     * @author zyh
     */
    public PageInfo<UserInfo> getUserInfoByTrueName(String trueName, Integer pageNum, Integer pageSize);

    /**
     * 根据组织结构id查询不在当前点击的组织机构内的用户
     * @param id
     * @return
     * @author zyh
     */
    public List<UserInfo> getNotInThisOrgUserInfoByOrganizationId(Long id);

    /**
     * 给用户添加组织机构信息
     * @param orgId
     * @param userId
     * @author zyh
     */
    public void addUserToOrg(Long orgId, Long userId) throws WxErrorException, WxErrorException;

    /**
     * 修改用户所属的组织机构
     * @param orgIds
     * @param userId
     * @author zyh
     */
    public void updateUserFromOrg(String orgIds, Long userId) throws WxErrorException;

    /**
     * 获取所有角色的列表
     * @return
     * @author zyh
     */
    public List<RoleInfo> getRoleInfoList();

    /**
     * 添加用户
     * @param userInfo
     * @param roles
     * @param orgIds
     * @author zyh
     */
    public int addAdmin(UserInfo userInfo,String[] roles,String orgIds) throws WxErrorException;

    /**
     * 根据id查询指定的用户信息
     * @param id
     * @return
     * @author zyh
     */
    public ArrayList<Object> getUserInfoById(Long id);

    /**
     * 修改用户
     * @param userInfo
     * @param roles
     * @param orgIds
     * @author zyh
     */
    public int updateUserInfo(UserInfo userInfo,String[] roles,String orgIds) throws WxErrorException;

    /**
     * 修改用户启用状态
     * @param userInfo
     * @author zyh
     */
    public void updateUserEnable(UserInfo userInfo ) throws WxErrorException;

    /**
     * 从微信添加用户
     * @param userInfo
     * @param orgIds
     * @author zyh
     */
    public void addAdminFromWx(UserInfo userInfo,String orgIds);

    /**
     * 从微信修改用户所属的组织机构
     * @param orgIds
     * @param userId
     * @author zyh
     */
    public void updateWxUserFromOrg(String orgIds, Long userId);

    /**
     * 重置用户密码，默认为123456
     * @param id
     * @author zyh
     */
    public void resetPassword(Long id);
}
