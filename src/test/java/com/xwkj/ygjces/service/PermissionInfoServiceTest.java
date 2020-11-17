package com.xwkj.ygjces.service;

import com.xwkj.ygjces.Mapper.OrganizationInfoMapperManual;
import com.xwkj.ygjces.Mapper_oracle.JcCoreSampleMapperManual;
import com.xwkj.ygjces.controller.vo.PermissionVo;
import com.xwkj.ygjces.model.OrgItemIntermediate;
import com.xwkj.ygjces.model.SampleInfo;
import com.xwkj.ygjces.model.StageTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PermissionInfoServiceTest {
    /*@Autowired
    private PermissionInfoService permissionInfoService;
    @Test
    public void getPermissionInfoVoShowList() {
        List<PermissionVo> permissionVoList = permissionInfoService.getPermissionInfoVoShowList(null);
        for (PermissionVo vo: permissionVoList
             ) {
            System.out.println(vo.getChildren());
        }
    }*/
    @Autowired
    JcCoreSampleMapperManual jcCoreSampleMapperManual;
    @Autowired
    OrganizationInfoMapperManual organizationInfoMapperManual;
    @Test
    public void test(){
//        List<SampleInfo> firstStageSampleInfoList = jcCoreSampleMapperManual.getFirstStageSampleInfoList();
//        System.out.println(firstStageSampleInfoList);
//        StageTime hun = jcCoreSampleMap
//        List<OrgItemIntermediate> itemsThatNotBoundToTheOrg = jcCoreSampleMapperManual.getItemsThatNotBoundToTheOrg(null);
//        System.out.println(itemsThatNotBoundToTheOrg);
        List<OrgItemIntermediate> itemsByOrgId = organizationInfoMapperManual.getItemsByOrgId(new Long(13));
        System.out.println(itemsByOrgId);
    }
}