package com.xwkj.ygjces.Mapper;

import com.xwkj.ygjces.model.OrganizationInfo;
import com.xwkj.ygjces.model.UserInfo;
import org.apache.catalina.User;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface UserInfoMapper {

    public List<UserInfo> getUserInfo(UserInfo userInfo);

    @ResultMap("BaseResultMap")
    @Select("SELECT * FROM tbl_gb_user_info AS u WHERE u.`account`=#{userName} AND u.`password`=#{password} AND isEnable = 1")
    public UserInfo findUserInfoUserNameAndPassword(@Param("userName") String userName, @Param("password") String password);

    /**
     * 通过组织结构ID查询对应的用户列表
     * @param organizationInfo
     * @return
     * @author zyh
     */
    List<UserInfo> getUserInfoByOrganizationId(OrganizationInfo organizationInfo);

    /**
     * 根据主键获取用户信息
     * @param id
     * @return
     * @author zyh
     */
    UserInfo getUserInfoById(Long id);

    /**
     * 根据人员名称获取用户列表
     * @param trueName
     * @return
     * @author zyh
     */
    List<UserInfo> getUserListByTrueName(@Param("trueName") String trueName);

    /**
     * 根据组织结构id查询不在当前点击的组织机构内的用户
     * @param id
     * @return
     * @author zyh
     */
    List<UserInfo> getNotInThisOrgUserInfoByOrganizationId(Long id);

    /**
     * 给用户添加组织机构信息
     * @param orgId
     * @param userId
     * @author zyh
     */
    void addUserToOrg(@Param("orgId") Long orgId, @Param("userId") Long userId);

    /**
     * 根据用户id删除所有该用户所属的组织机构
     * @param id
     * @author zyh
     */
    void deleteAllOrgsFromUserByUid(Long id);

    /**
     * 获取最后一条用户信息
     * @return
     * @author zyh
     */
    UserInfo getTheLastUserInfo();

    /**
     * 添加用户
     * @param userInfo
     * @author zyh
     */
    void addAdmin(UserInfo userInfo);

    /**
     * 向用户与角色关系表中插入用户与角色的关联信息
     * @param uId
     * @param rId
     * @author zyh
     */
    void addAdminAndRoleRelated(@Param("uId") Long uId , @Param("rId") Long rId);

    /**
     * 根据用户id在用户与角色的中间表中删除对应信息
     * @param id
     * @author zyh
     */
    void deleteRolesFromRoleUserRelateByUid(Long id);

    /**
     * 根据主键修改用户信息
     * @param userInfo
     * @return
     * @author zyh
     */
    int updateByPrimaryKeySelective(UserInfo userInfo);

    /**
     * 根据用户的账号查找用户信息
     * @param account
     * @return
     * @author zyh
     */
    UserInfo getUserInfoByAcount(@Param("account") String account);

    /**
     * 根据项目编号获取该项目所在部门的领导编号列表
     * @param itemId
     * @return
     * @author zyh
     */
    List<Long> getUserIdListByItemId(@Param("itemId") String itemId);

    /**
     * 根据用户编号查找用户所管理的检测项目的编号集合
     * @param userId
     * @return
     * @author zyh
     */
    List<String> getItemIdsByUserId(Long userId);


    /**
     * 根据项目编号获取审批该项目的审批人编号列表
     * @param itemId
     * @return
     * @author zyh
     */
    List<Long> getAuditorIdListByItemId(@Param("itemId") String itemId);

    /**
     * 根据审核人编号查找该审核人应审核的检测项目的编号集合
     * @param id
     * @return
     * @author zyh
     */
    List<String> getItemIdsByAuditorId(Long id);


    /**
     * 根据项目编号获取批准该项目的批准人编号列表
     * @param itemId
     * @return
     * @author zyh
     */
    List<Long> getApproverIdListByItemId(@Param("itemId") String itemId);

    /**
     * 根据批准人编号查找该批准人应批准的检测项目的编号集合
     * @param id
     * @return
     * @author zyh
     */
    List<String> getItemIdsByApproverId(Long id);

    /**
     * 根据手机号查找用户
     * @param mobile
     * @return
     * @author zyh
     */
    UserInfo getUserInfoByMobile(@Param("mobile") String mobile);

    /**
     * 判断用户修改的手机号是否重复
     * @param userInfo
     * @return
     * @author zyh
     */
    UserInfo getUserInfoByMobileAndNotByAccount(UserInfo userInfo);

    /**
     * 获取具有审批人角色的用户id集合(必须找准审批人角色所对应的id)
     * @return
     * @author zyh
     */
    List<Long> getUserIdThatHaveAprroverRole();

}
