package com.xwkj.ygjces.Mapper_oracle.auto;

import com.xwkj.ygjces.model.JcCoreWtInfo;
import org.springframework.stereotype.Repository;

@Repository
public interface JcCoreWtInfoMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table JC_CORE_WT_INFO
     *
     * @mbggenerated Mon Jul 29 14:16:39 CST 2019
     */
    int deleteByPrimaryKey(String wtId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table JC_CORE_WT_INFO
     *
     * @mbggenerated Mon Jul 29 14:16:39 CST 2019
     */
    int insert(JcCoreWtInfo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table JC_CORE_WT_INFO
     *
     * @mbggenerated Mon Jul 29 14:16:39 CST 2019
     */
    int insertSelective(JcCoreWtInfo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table JC_CORE_WT_INFO
     *
     * @mbggenerated Mon Jul 29 14:16:39 CST 2019
     */
    JcCoreWtInfo selectByPrimaryKey(String wtId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table JC_CORE_WT_INFO
     *
     * @mbggenerated Mon Jul 29 14:16:39 CST 2019
     */
    int updateByPrimaryKeySelective(JcCoreWtInfo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table JC_CORE_WT_INFO
     *
     * @mbggenerated Mon Jul 29 14:16:39 CST 2019
     */
    int updateByPrimaryKey(JcCoreWtInfo record);
}