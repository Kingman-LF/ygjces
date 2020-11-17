package com.xwkj.ygjces.Mapper_oracle;

import com.xwkj.ygjces.model.DateAndCount;
import com.xwkj.ygjces.model.JcHtContract;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface JcHtContractMapperManual {

    public List<JcHtContract> getContractList(JcHtContract jcHtContract);

    /**
     * 根据当月时间段年月日获取合同列表
     * @param startDate 开始时间
     * @param endDate 结束时间
     * @param contractName 合同名称
     * @return
     * @author wanglei
     */
    public List<JcHtContract> getContractListByOneMonth(@Param("startDate") Date startDate , @Param("endDate") Date endDate, @Param("contractName") String contractName);

    /**
     *根据开始时间和结束时间查询合同数量
     *@param jcHtContract startTime 开始时间 endTime 结束时间
     *@author wanglei
     */
    public DateAndCount getContractQuantity(JcHtContract jcHtContract);


    /**
     * 根据当前的年分月份查询合同量
     * @param currYearMonth
     * @return
     */
    public Integer getHtCountByMonth(String currYearMonth);

    /**
     *查询当天的合同数量
     * @return
     * @author wanglei
     */
    public Integer selectContractCountWithOneDay();

    /**
     *查询当月的合同数量
     * @return
     * @author wanglei
     */
    public Integer selectContractCountWithOneMonth();


}