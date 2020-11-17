package com.xwkj.ygjces.Mapper.auto;

import com.xwkj.ygjces.model.NoticeModel;
import org.springframework.stereotype.Repository;

@Repository
public interface NoticeModelMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_gb_xm_notice
     *
     * @mbggenerated Tue Aug 27 10:21:37 CST 2019
     */
    int deleteByPrimaryKey(Long pid);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_gb_xm_notice
     *
     * @mbggenerated Tue Aug 27 10:21:37 CST 2019
     */
    int insert(NoticeModel record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_gb_xm_notice
     *
     * @mbggenerated Tue Aug 27 10:21:37 CST 2019
     */
    int insertSelective(NoticeModel record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_gb_xm_notice
     *
     * @mbggenerated Tue Aug 27 10:21:37 CST 2019
     */
    NoticeModel selectByPrimaryKey(Long pid);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_gb_xm_notice
     *
     * @mbggenerated Tue Aug 27 10:21:37 CST 2019
     */
    int updateByPrimaryKeySelective(NoticeModel record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_gb_xm_notice
     *
     * @mbggenerated Tue Aug 27 10:21:37 CST 2019
     */
    int updateByPrimaryKey(NoticeModel record);
}