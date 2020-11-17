package com.xwkj.ygjces.Mapper;

import com.xwkj.ygjces.model.ApproverItemRelated;
import com.xwkj.ygjces.model.AuditorItemRelated;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 *批准人与项目中间表Dao
 */
@Repository
public interface AuditorItemRelatedMapper {

	/**
	 * 根据用户Id查询categoryId
	 * @param userId
	 * @return
	 */
	public List<String> selectCategoryIdByUserId(String userId);

	/**
	 * 根据用户ID查询
	 * @param userId
	 * @return
	 */
	public Integer selectByUserId(String userId);

	/**
	 * 根据用户ID删除
	 * @param userId
	 */
	public void deleteByUserId(String userId);

	/**
	 * 插入数据
	 */
	public void insertAuditorItemRelated(AuditorItemRelated auditorItemRelated);
}
