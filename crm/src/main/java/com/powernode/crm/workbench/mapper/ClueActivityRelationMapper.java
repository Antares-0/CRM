package com.powernode.crm.workbench.mapper;

import com.powernode.crm.workbench.domain.ClueActivityRelation;

import java.util.List;

public interface ClueActivityRelationMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_clue_activity_relation
     *
     * @mbggenerated Mon May 16 21:02:47 CST 2022
     */
    int deleteByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_clue_activity_relation
     *
     * @mbggenerated Mon May 16 21:02:47 CST 2022
     */
    int insert(ClueActivityRelation record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_clue_activity_relation
     *
     * @mbggenerated Mon May 16 21:02:47 CST 2022
     */
    int insertSelective(ClueActivityRelation record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_clue_activity_relation
     *
     * @mbggenerated Mon May 16 21:02:47 CST 2022
     */
    ClueActivityRelation selectByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_clue_activity_relation
     *
     * @mbggenerated Mon May 16 21:02:47 CST 2022
     */
    int updateByPrimaryKeySelective(ClueActivityRelation record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_clue_activity_relation
     *
     * @mbggenerated Mon May 16 21:02:47 CST 2022
     */
    int updateByPrimaryKey(ClueActivityRelation record);

    /**
     * 批量保存关联关系
     * @param list
     * @return
     */
    int insertClueActivityRelationByList(List<ClueActivityRelation> list);

    /**
     * 删除关联
     * @param clueActivityRelation
     * @return
     */
    int deleteClueActivityRelationByClueIdActivityId(ClueActivityRelation clueActivityRelation);

    /**
     * 根据clueId查询
     * @param clueId
     * @return
     */
    List<ClueActivityRelation> selectClueActivityRelationByClueId(String clueId);

    /**
     * 根据id删除对应的关系
     * @param clueId
     * @return
     */
    int deleteClueActivityRelationByClueId(String clueId);

}