package com.powernode.crm.workbench.mapper;

import com.powernode.crm.workbench.domain.Transaction;
import com.powernode.crm.workbench.domain.TransactionHistory;
import org.apache.poi.ss.formula.functions.T;

import java.util.List;
import java.util.Map;

public interface TransactionMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_tran
     *
     * @mbggenerated Mon May 16 21:02:47 CST 2022
     */
    int deleteByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_tran
     *
     * @mbggenerated Mon May 16 21:02:47 CST 2022
     */
    int insert(Transaction record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_tran
     *
     * @mbggenerated Mon May 16 21:02:47 CST 2022
     */
    int insertSelective(Transaction record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_tran
     *
     * @mbggenerated Mon May 16 21:02:47 CST 2022
     */
    Transaction selectByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_tran
     *
     * @mbggenerated Mon May 16 21:02:47 CST 2022
     */
    int updateByPrimaryKeySelective(Transaction record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_tran
     *
     * @mbggenerated Mon May 16 21:02:47 CST 2022
     */
    int updateByPrimaryKey(Transaction record);

    /**
     * 保存创建的交易
     * @param transaction
     * @return
     */
    int insertTransaction(Transaction transaction);

    /**
     * 根据条件查询对应的交易
     * @param map
     * @return
     */
    List<Transaction> selectAllTransactionByConditionForPage(Map<String, Object> map);

    /**
     * 查询数目
     * @param map
     * @return
     */
    int selectCountOfTransactionByConditionForPage(Map<String, Object> map);

    /**
     * 根据id查询交易的明细
     * @param id
     * @return
     */

    Transaction selectTranForDetailById(String id);


}