package com.xwkj.ygjces.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xwkj.ygjces.Mapper.OrganizationInfoMapperManual;
import com.xwkj.ygjces.Mapper.RoleInfoMapper;
import com.xwkj.ygjces.Mapper.UserInfoMapper;
import com.xwkj.ygjces.common.LoginUserInfoManager;
import com.xwkj.ygjces.model.OrganizationInfo;
import com.xwkj.ygjces.model.RoleInfo;
import com.xwkj.ygjces.model.UserInfo;
import com.xwkj.ygjces.model.UserOrgIntermediate;
import com.xwkj.ygjces.service.ApproverInfoService;
import com.xwkj.ygjces.service.UserInfoService;
import com.xwkj.ygjces.service.WxService;
import com.xwkj.ygjces.utils.MD5Util;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.cp.bean.WxCpUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 用户管理层
 * @author zyh
 * @Date
 */
@Service
public class UserInfoServiceImpl implements UserInfoService {
    @Autowired
    private UserInfoMapper userInfoMapper;
    @Autowired
    private OrganizationInfoMapperManual organizationInfoMapperManual;
    @Autowired
    private RoleInfoMapper roleInfoMapper;
    @Autowired
    private WxService wxService;

    @Autowired
    private ApproverInfoService approverInfoService;

    /**
     * 根据用户的账号查找用户信息
     * @param account
     * @return
     * @author zyh
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserInfo getUserInfoByAcount(String account){
        return userInfoMapper.getUserInfoByAcount(account);
    }

    /**
     * 根据用户Id查询角色信息
     * @param userId
     * @return
     * @author wanglei
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<RoleInfo> findRolesByUserId(Long userId){
        return roleInfoMapper.getRoleInfoByUserId(userId);
    }

    /**
     * 返回用户信息
     * @param page
     * @param limit
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public PageInfo<UserInfo> getUserInfoByApprovelId(UserInfo userInfo,Integer page,Integer limit){
        //查询所有用户信息
        PageHelper.startPage(page,limit);
        List<UserInfo> userInfoList =  userInfoMapper.getUserInfo(userInfo);
        for (int i = 0; i < userInfoList.size(); i++) {
            Integer count = approverInfoService.selectCountApproverInfoByUserId(userInfoList.get(i).getId());
            if(count == 0){
                userInfoList.get(i).setIsApprovelPerson(1);
                continue;
            }else{
                userInfoList.get(i).setIsApprovelPerson(2);
            }
        }
        return  new PageInfo<UserInfo>(userInfoList);
    }

    /**
     * 返回用户列表
     * @param userInfo
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public PageInfo<UserInfo> getUserInfoList(UserInfo userInfo, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum , pageSize);
        List<UserInfo> list = userInfoMapper.getUserInfo(userInfo);
        return new PageInfo<UserInfo>(list);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserInfo checkUserInfo(String userName, String password){
        return userInfoMapper.findUserInfoUserNameAndPassword(userName, password);
    }


    /**
     * 通过组织结构ID查询对应的用户列表
     * @param organizationInfo
     * @return
     * @author zyh
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public PageInfo<UserInfo> getUserInfoByOrganizationId(OrganizationInfo organizationInfo, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<UserInfo> list = userInfoMapper.getUserInfoByOrganizationId(organizationInfo);
        //将每个用户的所属部门的信息封装到orgNames字段中
        for (UserInfo user:list) {
            List<OrganizationInfo> orgList = organizationInfoMapperManual.getOrganizationInfoByUid(user.getId());
            for (int i=0;i<orgList.size();i++) {
                OrganizationInfo org = orgList.get(i);
                //将组织机构字符串中的null去掉
                if (user.getOrgNames()==null){
                    user.setOrgNames("");
                }
                if(i==orgList.size()-1){
                   user.setOrgNames(user.getOrgNames()+org.getName());
                }else {
                   user.setOrgNames(user.getOrgNames()+org.getName()+" | ");
                }

            }
        }
        //把每个用户是否为部门领导的信息封装到用户对象中
        for (UserInfo user:list) {
            List<UserOrgIntermediate> leaderInfo = organizationInfoMapperManual.getLeaderInfoByUserId(user.getId());
            UserOrgIntermediate leaderInfoByUserIdAndOrgId = organizationInfoMapperManual.getLeaderInfoByUserIdAndOrgId(user.getId(), organizationInfo.getId());
            //判断当前用户是否为当前部门的领导，是：设置1，不是：设置0
            if (leaderInfoByUserIdAndOrgId != null){
                user.setIsLeader(1);
            }else {
                user.setIsLeader(0);
            }
            //当前用户是若干个部门的领导
            if (leaderInfo!=null){
                for (int i = 0; i < leaderInfo.size() ; i++) {
                    UserOrgIntermediate userOrgIntermediate = leaderInfo.get(i);
                    if (user.getLeaders()==null){
                        user.setLeaders("");
                    }
                    if (i==leaderInfo.size()-1){
                        user.setLeaders(user.getLeaders()+userOrgIntermediate.getName());
                    }else {
                        user.setLeaders(user.getLeaders()+userOrgIntermediate.getName()+" | ");
                    }
                }
            }else {
                //当前用户不是任何一个部门的领导
                user.setLeaders("");
            }

        }
        PageInfo<UserInfo> pageInfo =new PageInfo<UserInfo>(list);
        return pageInfo;
    }

    /**
     * 通过人员名称查询对应的用户列表
     * @param trueName
     * @param pageNum
     * @param pageSize
     * @return
     * @author zyh
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public PageInfo<UserInfo> getUserInfoByTrueName(String trueName, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<UserInfo> list = userInfoMapper.getUserListByTrueName(trueName);
        //将每个用户的所属部门的信息封装到orgNames字段中
        for (UserInfo user:list) {
            List<OrganizationInfo> orgList = organizationInfoMapperManual.getOrganizationInfoByUid(user.getId());
            for (int i=0;i<orgList.size();i++) {
                OrganizationInfo org = orgList.get(i);
                if (user.getOrgNames()==null){
                    user.setOrgNames("");
                }
                if(i==orgList.size()-1){
                    user.setOrgNames(user.getOrgNames()+org.getName());
                }else {
                    user.setOrgNames(user.getOrgNames()+org.getName()+" | ");
                }

            }
        }
        //把每个用户是否为部门领导的信息封装到用户对象中
        for (UserInfo user:list) {
            List<UserOrgIntermediate> leaderInfo = organizationInfoMapperManual.getLeaderInfoByUserId(user.getId());
            //当前用户是若干个部门的领导
            if (leaderInfo!=null){
                for (int i = 0; i < leaderInfo.size() ; i++) {
                    UserOrgIntermediate userOrgIntermediate = leaderInfo.get(i);
                    if (user.getLeaders()==null){
                        user.setLeaders("");
                    }
                    if (i==leaderInfo.size()-1){
                        user.setLeaders(user.getLeaders()+userOrgIntermediate.getName());
                    }else {
                        user.setLeaders(user.getLeaders()+userOrgIntermediate.getName()+" | ");
                    }
                }
            }else {
                //当前用户不是任何一个部门的领导
                user.setLeaders("");
            }
        }
        PageInfo<UserInfo> pageInfo =new PageInfo<UserInfo>(list);
        return pageInfo;
    }

    /**
     * 根据组织结构id查询不在当前点击的组织机构内的用户
     * @param id
     * @return
     * @author zyh
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<UserInfo> getNotInThisOrgUserInfoByOrganizationId(Long id) {
        return userInfoMapper.getNotInThisOrgUserInfoByOrganizationId(id);
    }

    /**
     * 给用户添加组织机构信息
     * @param orgId
     * @param userId
     * @author zyh
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addUserToOrg(Long orgId, Long userId) throws WxErrorException {
        userInfoMapper.addUserToOrg(orgId,userId);

        UserInfo userInfoById = userInfoMapper.getUserInfoById(userId);
        WxCpUser wxCpUser = wxService.getUserService().getById(userInfoById.getAccount());
        Long[] oldDeptIds = wxCpUser.getDepartIds();
        Long[] newDeptIds = new Long[oldDeptIds.length+1];
        for (int i = 0; i < oldDeptIds.length ; i++) {
            newDeptIds[i] = oldDeptIds[i];
        }
        newDeptIds[newDeptIds.length-1] = orgId;

        WxCpUser finalWxCpUser = new WxCpUser();
        finalWxCpUser.setUserId(wxCpUser.getUserId());
        finalWxCpUser.setDepartIds(newDeptIds);
        wxService.getUserService().update(finalWxCpUser);
    }

    /**
     * 从后台修改用户所属的组织机构
     * @param orgIds
     * @param userId
     * @author zyh
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateUserFromOrg(String orgIds, Long userId) throws WxErrorException {
        String[] ids = orgIds.split(",");
        //获取该用户原来所在的所有组织机构
        List<OrganizationInfo> organizationInfoByUid = organizationInfoMapperManual.getOrganizationInfoByUid(userId);
        //将原来的组织机构id封装成集合
        List<Long> oldIds = new ArrayList<>();
        for (OrganizationInfo o : organizationInfoByUid){
            oldIds.add(o.getId());
        }
        //将现在的组织机构id封装成集合
        List<Long> newIds = new ArrayList<>();
        for (int i = 0; i < ids.length; i++) {
            newIds.add(Long.parseLong(ids[i]));
        }
        //移除原来的组织机构与现在的组织机构之间重合的部分
        oldIds.removeAll(newIds);
        //将原来的组织机构与现在的组织机构之间不重合部分的领导信息删除
        if (oldIds.size() > 0){
            for (Long id : oldIds){
                organizationInfoMapperManual.removeLeaderFromOrganization(userId,id);
            }
        }
        userInfoMapper.deleteAllOrgsFromUserByUid(userId);
        for (int i = 0; i < ids.length ; i++) {
            userInfoMapper.addUserToOrg(Long.parseLong(ids[i]),userId);
        }

        UserInfo userInfoById = userInfoMapper.getUserInfoById(userId);

        //将用户同步到企业微信中
        WxCpUser wxCpUser = new WxCpUser();
        wxCpUser.setUserId(userInfoById.getAccount());
        //将String类型的数组转换成Integer类型的数组
        Long[] deptIds = new Long[ids.length];
        for (int i = 0; i < ids.length ; i++) {
            deptIds[i] = Long.parseLong(ids[i]);
        }
        wxCpUser.setDepartIds(deptIds);
        wxService.getUserService().update(wxCpUser);

    }

    /**
     * 获取所有角色的列表
     * @return
     * @author zyh
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<RoleInfo> getRoleInfoList() {
        return roleInfoMapper.getAllRoleInfo();
    }

    /**
     * 从后台添加用户
     * @param userInfo
     * @param roles
     * @param orgIds
     * @author zyh
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int addAdmin(UserInfo userInfo, String[] roles,String orgIds) throws WxErrorException {

        UserInfo userInfoByAcount = userInfoMapper.getUserInfoByAcount(userInfo.getAccount());
        UserInfo userInfoByMobile = userInfoMapper.getUserInfoByMobile(userInfo.getMobile());
        if(userInfoByAcount==null&&userInfoByMobile==null){
            UserInfo info = LoginUserInfoManager.getUserInfo();
            Date date = new Date();
            userInfo.setCreateTime(date);
            userInfo.setCreateUserId(info.getId());
            userInfo.setLastModifiedTime(date);
            userInfo.setLastModifier(info.getId());
            userInfo.setPassword(MD5Util.getMD5(userInfo.getPassword()));
            //将用户插入表中
            userInfoMapper.addAdmin(userInfo);
            //获取刚才插入的用户信息，此信息中有刚才插入信息的主键
            UserInfo theLastUserInfo = userInfoMapper.getTheLastUserInfo();
            //向用户与角色关系表中插入信息
            for (int i = 1; i < roles.length ; i++) {
                userInfoMapper.addAdminAndRoleRelated(theLastUserInfo.getId(),Long.parseLong(roles[i]));
            }
            //通过“，”将组织机构的字符串分隔开
            String[] oids = orgIds.split(",");
            //将用户加入所有组织机构中
            for (int i = 0; i < oids.length ; i++) {
                userInfoMapper.addUserToOrg(Long.parseLong(oids[i]),theLastUserInfo.getId());
            }

            //将用户同步到企业微信中
            WxCpUser wxCpUser = new WxCpUser();
            wxCpUser.setUserId(userInfo.getAccount());
            wxCpUser.setName(userInfo.getTrueName());
            wxCpUser.setMobile(userInfo.getMobile());
            //将String类型的数组转换成Integer类型的数组
            Long[] deptIds = new Long[oids.length];
            for (int i = 0; i < oids.length ; i++) {
                deptIds[i] = Long.parseLong(oids[i]);
            }
            wxCpUser.setDepartIds(deptIds);
            wxService.getUserService().create(wxCpUser);
            return 1;
        }else {
            return 0;
        }


    }


    /**
     * 根据id查询指定的用户信息
     * @param id
     * @return
     * @author zyh
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ArrayList<Object> getUserInfoById(Long id) {
        ArrayList<Object> list = new ArrayList<>();
        UserInfo userInfo = userInfoMapper.getUserInfoById(id);
        List<RoleInfo> roleInfoListByUserId = roleInfoMapper.getRoleInfoListByUserId(id);
        list.add(userInfo);
        list.add(roleInfoListByUserId);
        return list;
    }

    /**
     * 从后台修改用户
     * @param userInfo
     * @param roles
     * @param orgIds
     * @author zyh
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateUserInfo(UserInfo userInfo, String[] roles,String orgIds) throws WxErrorException {
        UserInfo userInfoByMobileAndNotByAccount = userInfoMapper.getUserInfoByMobileAndNotByAccount(userInfo);
        if (userInfoByMobileAndNotByAccount==null){
            UserInfo info = LoginUserInfoManager.getUserInfo();
            Date date = new Date();
            userInfo.setLastModifiedTime(date);
            userInfo.setLastModifier(info.getId());
            //修改用户信息
            userInfoMapper.updateByPrimaryKeySelective(userInfo);
            //删除用户与角色中间表中对应的信息
            userInfoMapper.deleteRolesFromRoleUserRelateByUid(userInfo.getId());
            //向用户与角色中间表中重新添加信息
            for (int i = 1; i < roles.length ; i++) {
                userInfoMapper.addAdminAndRoleRelated(userInfo.getId(),Long.parseLong(roles[i]));
            }
            //通过“，”将组织机构的字符串分隔开
            String[] ids = orgIds.split(",");
            //获取该用户原来所在的所有组织机构
            List<OrganizationInfo> organizationInfoByUid = organizationInfoMapperManual.getOrganizationInfoByUid(userInfo.getId());
            //将原来的组织机构id封装成集合
            List<Long> oldIds = new ArrayList<>();
            for (OrganizationInfo o : organizationInfoByUid){
                oldIds.add(o.getId());
            }
            //将现在的组织机构id封装成集合
            List<Long> newIds = new ArrayList<>();
            for (int i = 0; i < ids.length; i++) {
                newIds.add(Long.parseLong(ids[i]));
            }
            //移除原来的组织机构与现在的组织机构之间重合的部分
            oldIds.removeAll(newIds);
            //将原来的组织机构与现在的组织机构之间不重合部分的领导信息删除
            if (oldIds.size() > 0){
                for (Long id : oldIds){
                    organizationInfoMapperManual.removeLeaderFromOrganization(userInfo.getId(),id);
                }
            }
            //删除用户原来所属的所有组织机构
            userInfoMapper.deleteAllOrgsFromUserByUid(userInfo.getId());
            //将用户加入新的组织机构
            for (int i = 0; i < ids.length ; i++) {
                userInfoMapper.addUserToOrg(Long.parseLong(ids[i]),userInfo.getId());
            }

            //将用户同步到企业微信中
            WxCpUser wxCpUser = new WxCpUser();
            wxCpUser.setUserId(userInfo.getAccount());
            wxCpUser.setName(userInfo.getTrueName());
            wxCpUser.setMobile(userInfo.getMobile());
            //将String类型的数组转换成Integer类型的数组
            Long[] deptIds = new Long[ids.length];
            for (int i = 0; i < ids.length ; i++) {
                deptIds[i] = Long.parseLong(ids[i]);
            }
            wxCpUser.setDepartIds(deptIds);
            wxService.getUserService().update(wxCpUser);
            return 1;
        }else {
            return 0;
        }

    }

    /**
     * 修改用户启用状态
     * @param userInfo
     * @author zyh
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateUserEnable(UserInfo userInfo) throws WxErrorException {
        UserInfo info = LoginUserInfoManager.getUserInfo();
        Date date = new Date();
        userInfo.setLastModifiedTime(date);
        userInfo.setLastModifier(info.getId());
        userInfoMapper.updateByPrimaryKeySelective(userInfo);

        //根据用户主键获取用户信息
        UserInfo userInfoById = userInfoMapper.getUserInfoById(userInfo.getId());

        WxCpUser wxuser = null;
        try {
            wxuser = wxService.getUserService().getById(userInfoById.getAccount());
        } catch(Exception e){
            System.out.println("微信中无此用户");
        }finally {
            WxCpUser wxCpUser = new WxCpUser();
            if (wxuser==null){
                //如果企业微信中不存在对应的用户，那么就给企业微信添加用户
                //获取对应用户所属的组织机构集合和领导的组织机构集合
                List<Long> orgIdsListByUserId = organizationInfoMapperManual.getOrgIdsListByUserId(userInfo.getId());
                List<Long> orgIdsListByLeaderId = organizationInfoMapperManual.getOrgIdsListByLeaderId(userInfo.getId());
                Integer[] isLeaderInDept = new Integer[orgIdsListByUserId.size()];
                for (int i = 0; i < orgIdsListByUserId.size() ; i++) {
                    if (orgIdsListByLeaderId.contains(orgIdsListByUserId.get(i))){
                        isLeaderInDept[i] = 1;
                    }else {
                        isLeaderInDept[i] = 0;
                    }
                }
                Long[] depts = new Long[orgIdsListByUserId.size()];
                for (int j = 0; j < orgIdsListByUserId.size() ; j++) {
                    depts[j] = orgIdsListByUserId.get(j);
                }

                wxCpUser.setUserId(userInfoById.getAccount());
                wxCpUser.setName(userInfoById.getTrueName());
                wxCpUser.setMobile(userInfoById.getMobile());
                wxCpUser.setDepartIds(depts);
                wxCpUser.setIsLeaderInDept(isLeaderInDept);
                wxService.getUserService().create(wxCpUser);
            }else {
                //如果其微信中存在对应的用户，那么就将用户状态同步到企业微信中
                wxCpUser.setUserId(userInfoById.getAccount());
                wxCpUser.setEnable(userInfoById.getEnable());
                wxService.getUserService().update(wxCpUser);
            }
        }

    }

    /**
     * 从微信添加用户同步到后台
     * @param userInfo
     * @param orgIds
     * @author zyh
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addAdminFromWx(UserInfo userInfo, String orgIds) {
        Date date = new Date();
        userInfo.setCreateTime(date);
        userInfo.setCreateUserId(Long.MAX_VALUE);
        userInfo.setLastModifiedTime(date);
        userInfo.setLastModifier(Long.MAX_VALUE);
        userInfo.setPassword(MD5Util.getMD5(userInfo.getPassword()));
        //将用户插入表中
        userInfoMapper.addAdmin(userInfo);
        //获取刚才插入的用户信息，此信息中有刚才插入信息的主键
        UserInfo theLastUserInfo = userInfoMapper.getTheLastUserInfo();
        //通过“，”将组织机构的字符串分隔开
        String[] oids = orgIds.split(",");
        //将用户加入所有组织机构中
        for (int i = 0; i < oids.length ; i++) {
            userInfoMapper.addUserToOrg(Long.parseLong(oids[i]),theLastUserInfo.getId());
        }
    }

    /**
     * 从微信修改用户所属的组织机构
     * @param orgIds
     * @param userId
     * @author zyh
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateWxUserFromOrg(String orgIds, Long userId) {
        String[] ids = orgIds.split(",");
        userInfoMapper.deleteAllOrgsFromUserByUid(userId);
        for (int i = 0; i < ids.length ; i++) {
            userInfoMapper.addUserToOrg(Long.parseLong(ids[i]),userId);
        }
    }

    /**
     * 重置用户密码，默认为123456
     * @param id
     * @author zyh
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void resetPassword(Long id) {
        String password = MD5Util.getMD5("123456");
        UserInfo info = LoginUserInfoManager.getUserInfo();
        Date date = new Date();
        UserInfo userInfo = new UserInfo();
        userInfo.setId(id);
        userInfo.setPassword(password);
        userInfo.setLastModifiedTime(date);
        userInfo.setLastModifier(info.getId());
        userInfoMapper.updateByPrimaryKeySelective(userInfo);
    }


    public UserInfoMapper getUserInfoMapper() {
        return userInfoMapper;
    }

    public void setUserInfoMapper(UserInfoMapper userInfoMapper) {
        this.userInfoMapper = userInfoMapper;
    }

}
