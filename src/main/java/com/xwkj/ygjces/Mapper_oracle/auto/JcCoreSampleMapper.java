package com.xwkj.ygjces.Mapper_oracle.auto;

import com.xwkj.ygjces.model.JcCoreSample;
import org.springframework.stereotype.Repository;

@Repository
public interface JcCoreSampleMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table JC_CORE_SAMPLE
     *
     * @mbggenerated Fri Aug 16 16:03:38 CST 2019
     */
    int deleteByPrimaryKey(String syNum);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table JC_CORE_SAMPLE
     *
     * @mbggenerated Fri Aug 16 16:03:38 CST 2019
     */
    int insert(JcCoreSample record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table JC_CORE_SAMPLE
     *
     * @mbggenerated Fri Aug 16 16:03:38 CST 2019
     */
    int insertSelective(JcCoreSample record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table JC_CORE_SAMPLE
     *
     * @mbggenerated Fri Aug 16 16:03:38 CST 2019
     */
    JcCoreSample selectByPrimaryKey(String syNum);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table JC_CORE_SAMPLE
     *
     * @mbggenerated Fri Aug 16 16:03:38 CST 2019
     */
    int updateByPrimaryKeySelective(JcCoreSample record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table JC_CORE_SAMPLE
     *
     * @mbggenerated Fri Aug 16 16:03:38 CST 2019
     */
    int updateByPrimaryKey(JcCoreSample record);
}