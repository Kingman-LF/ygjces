package com.xwkj.ygjces.Mapper;

import com.xwkj.ygjces.model.Achievements;
import com.xwkj.ygjces.model.OrgItemIntermediate;
import com.xwkj.ygjces.model.OrganizationInfo;
import com.xwkj.ygjces.model.UserOrgIntermediate;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrganizationInfoMapperManual {
    /**
     * 获取所有组织机构信息
     *
     * @return
     * @author zyh
     */
    List<OrganizationInfo> getOrganizationInfoList();

    /**
     * 通过ID获取组织机构信息
     *
     * @param id
     * @return
     * @author zyh
     */
    OrganizationInfo getOrganizationInfoById(Long id);

    /**
     * 获取最后一条组织结构信息
     *
     * @return
     * @author zyh
     */
    OrganizationInfo getTheLastOneOrganization();

    /**
     * 根据组织机构id获取 组织结构表 中与 中间表 有关联关系的信息列表
     *
     * @return
     * @author zyh
     */
    List<OrganizationInfo> getInfoFromUserAndOrganizationRelatedByID(Long id);

    /**
     * 根据用户id获取组织结构信息
     *
     * @param id
     * @return
     * @author zyh
     */
    List<OrganizationInfo> getOrganizationInfoByUid(Long id);

    /**
     * 获取组织机构表中最大主键的值
     *
     * @return
     * @author zyh
     */
    Long getMaxPrimaryKey();

    /**
     * 根据主键修改对应组织机构信息的主键
     *
     * @param id
     * @author zyh
     */
    void updatePrimaryKeyByKey(@Param("id") Long id, @Param("maxId") Long maxId);

    /**
     * 根据传入的组织机构父节点的主键修改子节点的Pid
     *
     * @param oldId
     * @param newId
     * @author zyh
     */
    void updatePidByPrimaryKey(@Param("oldId") Long oldId, @Param("newId") Long newId);

    /**
     * 查找小于分界线的最大主键值
     *
     * @return
     * @author zyh
     */
    Long selectBigestPrimaryKeyLessThanBoundary();

    /**
     * 给组织机构添加领导
     *
     * @param uId
     * @param oId
     * @author zyh
     */
    void addLeaderToOrganization(@Param("uId") Long uId, @Param("oId") Long oId);

    /**
     * 给组织机构移除领导
     *
     * @param uId
     * @param oId
     * @author zyh
     */
    void removeLeaderFromOrganization(@Param("uId") Long uId, @Param("oId") Long oId);

    /**
     * 根据用户id 获取该用户是哪些组织机构的领导的信息
     *
     * @param id
     * @return
     */
    List<UserOrgIntermediate> getLeaderInfoByUserId(Long id);

    /**
     * 根据用户id和部门id 获取组织机构与领导中间表的信息
     *
     * @param uId
     * @param oId
     * @return
     */
    UserOrgIntermediate getLeaderInfoByUserIdAndOrgId(@Param("uId") Long uId, @Param("oId") Long oId);

    /**
     * 插入组织机构与检测项目的绑定信息
     *
     * @param orgItemIntermediate
     * @author zyh
     */
    void insertOrgAndItemBindingInfo(OrgItemIntermediate orgItemIntermediate);

    /**
     * 通过组织机构id获取与当前组织机构绑定的检测项目列表
     *
     * @param orgId
     * @return
     * @author zyh
     */
    List<OrgItemIntermediate> getItemsThatBoundToTheOrgById(Long orgId);

    /**
     * 解除组织机构与检测项目之间的绑定
     *
     * @param orgItemIntermediate
     * @author zyh
     */
    void unboundOrgAndItems(OrgItemIntermediate orgItemIntermediate);

    /**
     * 根据组织机构名称获取绑定了检测项目的组织机构
     *
     * @param orgName
     * @return
     * @author zyh
     */
    List<Achievements> getOrgThatBoundToItems(@Param("orgName") String orgName);

    /**
     * 根据组织机构id获取在当前组织机构内的用户的真实姓名列表
     *
     * @param orgId
     * @return
     * @author zyh
     */
    List<String> getTrueNameByOrgId(Long orgId);

    /**
     * 根据组织机构id获取在当前组织机构的领导
     *
     * @param orgId
     * @return
     * @author zyh
     */
    List<String> getLeaderByOrgId(Long orgId);

    /**
     * 根据组织机构id获取该组织机构绑定的检测项目列表
     *
     * @param orgId
     * @return
     * @author zyh
     */
    List<OrgItemIntermediate> getItemsByOrgId(Long orgId);

    /**
     * 获取所有具有子组织机构的组织机构
     *
     * @return
     * @author zyh
     */
    List<OrganizationInfo> getOrganizationInfoThatHasSubOrganization();

    /**
     * 根据组织机构id获取子组织机构
     *
     * @param pId
     * @return
     * @author zyh
     */
    List<OrganizationInfo> getOrganizationInfoByPid(Long pId);

    /**
     * 根据领导编号将其领导的所有组织机构都取消
     *
     * @param id
     * @author zyh
     */
    void deleteAllOrgFromLeaderById(Long id);

    /**
     * 根据用户id获取用户所在的部门id列表
     *
     * @param userId
     * @return
     */
    List<Long> getOrgIdsListByUserId(Long userId);

    /**
     * 根据领导id获取其领导的部门id列表
     *
     * @param id
     * @return
     * @author zyh
     */
    List<Long> getOrgIdsListByLeaderId(Long id);

    /**
     * 根据领导编号和组织机构编号获取领导与组织机构信息的主键
     *
     * @param leaderId
     * @param orgId
     * @return
     * @author zyh
     */
    Long getLeaderAndOrgInfoByLeaderIdAndOrgId(@Param("leaderId") Long leaderId, @Param("orgId") Long orgId);

    /**
     * 根据部门编号获取领导编号列表
     *
     * @param orgId
     * @return
     * @author zyh
     */
    List<Long> getLeaderIdByOrgId(Long orgId);

    /**
     * 根据父部门编号获取子部门主键列表
     * @param id
     * @return
     * @author zyh
     */
    List<Long> getSubOrgIdsByPid(Long id);

    /**
     * @Description : 根据部门名称获取部门信息
     * @methodName : getOrganizationInfoByOrgName
     * @param name : 
     * @return : com.xwkj.ygjces.model.OrganizationInfo
     * @exception : 
     * @author : zyh
     */
    OrganizationInfo getOrganizationInfoByOrgName(@Param("name") String name);

}
