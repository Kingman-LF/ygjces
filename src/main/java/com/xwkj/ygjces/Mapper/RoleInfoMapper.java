package com.xwkj.ygjces.Mapper;


import com.xwkj.ygjces.model.RoleInfo;
import lombok.Data;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Mapper
@Component
public interface RoleInfoMapper {

    /**
     * 删除角色信息
     * @param id
     * @author wanglei
     */
    public void deleteRoleInfoById(Long id);

    /**
     * 修改数据库中角色的信息
     * @param roleInfo
     */
    public void updateRoleInfo(RoleInfo roleInfo);
    /**
     * 保存角色信息
     * @param roleInfo
     */
    public void saveRoleInfo(RoleInfo roleInfo);

    /**
     *
     * @param roleInfo
     * @return
     */
    public List<RoleInfo> getRoleInfoList(RoleInfo roleInfo);

    /**
     * 获取所有角色
     * @return
     * @author zyh
     */
    public List<RoleInfo> getAllRoleInfo();

    /**
     * 通过用户id获取对应角色信息
     * @param id
     * @return
     * @author zyh
     */
    public List<RoleInfo> getRoleInfoListByUserId(Long id);

    /**
     * 通过用户id获取对应角色信息
     * @param id
     * @return
     * @author wanglei
     */
    public List<RoleInfo> getRoleInfoByUserId(Long id);


}
