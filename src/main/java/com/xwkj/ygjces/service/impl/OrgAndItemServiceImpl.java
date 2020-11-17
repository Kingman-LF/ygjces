package com.xwkj.ygjces.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xwkj.ygjces.Mapper.OrganizationInfoMapperManual;
import com.xwkj.ygjces.Mapper_oracle.JcCoreSampleMapperManual;
import com.xwkj.ygjces.model.Achievements;
import com.xwkj.ygjces.model.OrgItemIntermediate;
import com.xwkj.ygjces.model.SampleInfo;
import com.xwkj.ygjces.service.OrgAndItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.MethodWrapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DecimalFormat;
import java.util.*;

@Service
public class OrgAndItemServiceImpl implements OrgAndItemService {

    @Autowired
    JcCoreSampleMapperManual jcCoreSampleMapperManual;
    @Autowired
    OrganizationInfoMapperManual organizationInfoMapperManual;

    /**
     * 给检测项目绑定组织机构
     * @param orgItemIntermediate
     * @author zyh
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void bindOrgAndItems(OrgItemIntermediate orgItemIntermediate) {
        organizationInfoMapperManual.insertOrgAndItemBindingInfo(orgItemIntermediate);
    }

    /**
     * 解除组织机构与检测项目之间的绑定
     * @param orgItemIntermediate
     * @author zyh
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void unboundOrgAndItems(OrgItemIntermediate orgItemIntermediate) {
        organizationInfoMapperManual.unboundOrgAndItems(orgItemIntermediate);
    }

    /**
     * 根据组织机构id获取该组织机构绑定的项目列表和该组织机构没有绑定的项目列表
     * @param orgId
     * @return
     * @author zyh
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<Object> getItemsByOrgId(Long orgId) {

        List<Object> list = new ArrayList<>();
        //获取该组织机构绑定的项目列表
        List<OrgItemIntermediate> itemsThatBoundToTheOrgById = organizationInfoMapperManual.getItemsThatBoundToTheOrgById(orgId);
        list.add(itemsThatBoundToTheOrgById);

        if(itemsThatBoundToTheOrgById.size() != 0){
            //拼装该组织机构绑定的项目的id集合
            List<String> itemIds = new ArrayList<>();
            for (OrgItemIntermediate o:itemsThatBoundToTheOrgById) {
                itemIds.add(o.getItemId());
            }
            //获取该组织机构没有绑定的项目列表
            List<OrgItemIntermediate>  itemsThatNotBoundToTheOrg = jcCoreSampleMapperManual.getItemsThatNotBoundToTheOrg(itemIds);
            list.add(itemsThatNotBoundToTheOrg);
        }else {
            List<OrgItemIntermediate>  itemsThatNotBoundToTheOrg = jcCoreSampleMapperManual.getItemsThatNotBoundToTheOrg(null);
            list.add(itemsThatNotBoundToTheOrg);
        }
        return list;
    }

    /**
     * 根据组织机构名称获取绑定了检测项目的组织机构的绩效信息
     * @param startTime
     * @param endTime
     * @param orgName
     * @param pageNum
     * @param pageSize
     * @return
     * @author zyh
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public PageInfo<Achievements> getAchievementsInfoList(Date startTime,Date endTime, String orgName, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum , pageSize);
        List<Achievements> orgList = organizationInfoMapperManual.getOrgThatBoundToItems(orgName);
        for (int i = 0; i < orgList.size() ; i++) {
            Achievements achievements = orgList.get(i);
            //获取当前组织机构内的所有用户信息
            List<String> trueNameByOrgId = organizationInfoMapperManual.getTrueNameByOrgId(achievements.getId());
            //获取当前组织机构的领导
            List<String> leaderByOrgId = organizationInfoMapperManual.getLeaderByOrgId(achievements.getId());
            //将所有用户中的领导去除，余下的就是所有下属
            trueNameByOrgId.removeAll(leaderByOrgId);
            //拼接下属字符串
            for (int j = 0; j < trueNameByOrgId.size(); j++){
                if(achievements.getSubordinateName()==null){
                    achievements.setSubordinateName("");
                }
                if(j==trueNameByOrgId.size()-1){
                    achievements.setSubordinateName(achievements.getSubordinateName()+trueNameByOrgId.get(j));
                }else {
                    achievements.setSubordinateName(achievements.getSubordinateName()+trueNameByOrgId.get(j)+",");
                }
            }
            //拼接领导字符串
            for (int k = 0; k < leaderByOrgId.size(); k++){
                if(achievements.getLeaderName()==null){
                    achievements.setLeaderName("");
                }
                if(k==leaderByOrgId.size()-1){
                    achievements.setLeaderName(achievements.getLeaderName()+leaderByOrgId.get(k));
                }else {
                    achievements.setLeaderName(achievements.getLeaderName()+leaderByOrgId.get(k)+",");
                }
            }
            //计算按时完成率
            //设置按时完成数量初始值为0
            long onTimeNum = 0;
            List<OrgItemIntermediate> itemsByOrgId = organizationInfoMapperManual.getItemsByOrgId(achievements.getId());
            //拼装检测项目id集合
            List<String> itemIds = new ArrayList<>();
            for (int j = 0; j < itemsByOrgId.size() ; j++) {
                itemIds.add(itemsByOrgId.get(j).getItemId());
            }
            List<SampleInfo> sampleInfoByItemIds = jcCoreSampleMapperManual.getSampleInfoByItemIds(startTime, endTime, itemIds);
            for (int k = 0; k < sampleInfoByItemIds.size() ; k++) {
                SampleInfo sampleInfo = sampleInfoByItemIds.get(k);
                String day = jcCoreSampleMapperManual.getProvisionalTimeByItemId(sampleInfo.getItemId());
                if (sampleInfo.getUseTime()<=Integer.parseInt(day)){
                    onTimeNum++;
                }
            }
            if (sampleInfoByItemIds.size()==0){
                achievements.setCompletionRate("100%");
            }else {
                double percent = (double)onTimeNum/sampleInfoByItemIds.size()*100;
                DecimalFormat decimalFormat = new DecimalFormat("0.00");
                achievements.setCompletionRate(decimalFormat.format(percent)+"%");
            }




        }

        return new PageInfo<Achievements>(orgList);
    }


    /**
     * 根据项目id列表获取检测样品信息
     * @param pageNum
     * @param pageSize
     * @param startTime
     * @param endTime
     * @param orgId
     * @return
     * @author zyh
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public PageInfo<SampleInfo> getSampleInfoByOrgId(Integer pageNum, Integer pageSize,Date startTime, Date endTime, Long orgId) {

        List<OrgItemIntermediate> itemsByOrgId = organizationInfoMapperManual.getItemsByOrgId(orgId);
        //拼装检测项目id集合
        List<String> itemIds = new ArrayList<>();
        for (int j = 0; j < itemsByOrgId.size() ; j++) {
            itemIds.add(itemsByOrgId.get(j).getItemId());
        }

        PageHelper.startPage(pageNum , pageSize);
        List<SampleInfo> sampleInfoByItemIds = jcCoreSampleMapperManual.getSampleInfoByItemIds(startTime, endTime, itemIds);
        for (int i = 0; i < sampleInfoByItemIds.size() ; i++) {
            SampleInfo sampleInfo = sampleInfoByItemIds.get(i);
            String day = jcCoreSampleMapperManual.getProvisionalTimeByItemId(sampleInfo.getItemId());
            if (sampleInfo.getUseTime()<=Integer.parseInt(day)){
                //没超时
                sampleInfo.setOvertime(1);
            }else {
                //超时
                sampleInfo.setOvertime(0);
            }
        }

        return new PageInfo<SampleInfo>(sampleInfoByItemIds);
    }

    /**
     * 根据部门id获取及时的检测样品和不及时的检测样品的数量
     * @param startTime
     * @param endTime
     * @param orgId
     * @return
     * @author zyh
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<Object> getOnTimeAndOvertimeSampleCountByOrgId(Date startTime, Date endTime,Long orgId) {
        List<OrgItemIntermediate> itemsByOrgId = organizationInfoMapperManual.getItemsByOrgId(orgId);
        //拼装检测项目id集合
        List<String> itemIds = new ArrayList<>();
        for (int j = 0; j < itemsByOrgId.size() ; j++) {
            itemIds.add(itemsByOrgId.get(j).getItemId());
        }
        List<SampleInfo> sampleInfoByItemIds = jcCoreSampleMapperManual.getSampleInfoByItemIds(startTime, endTime, itemIds);
        long onTimeNum = 0;
        long overtimeNum = 0;
        for (int i = 0; i < sampleInfoByItemIds.size() ; i++) {
            SampleInfo sampleInfo = sampleInfoByItemIds.get(i);
            String day = jcCoreSampleMapperManual.getProvisionalTimeByItemId(sampleInfo.getItemId());
            if (sampleInfo.getUseTime()<=Integer.parseInt(day)){
                //没超时
                onTimeNum++;
            }else {
                //超时
                overtimeNum++;
            }
        }
        List<Object> count = new ArrayList<>();
        Map<String , Object> onTimeMap = new HashMap<>();
        Map<String , Object> overtimeMap = new HashMap<>();
        onTimeMap.put("value",onTimeNum);
        onTimeMap.put("name","准时");
        overtimeMap.put("value",overtimeNum);
        overtimeMap.put("name","超时");
        count.add(onTimeMap);
        count.add(overtimeMap);

        return count;
    }
}
