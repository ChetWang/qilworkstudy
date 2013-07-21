package org.vlg.linghu.mybatis.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.vlg.linghu.mybatis.bean.VacReceiveMessage;
import org.vlg.linghu.mybatis.bean.VacReceiveMessageExample;

public interface VacReceiveMessageMapper {
    int countByExample(VacReceiveMessageExample example);

    int deleteByExample(VacReceiveMessageExample example);

    @Delete({
        "delete from e_vac_receive",
        "where vac_id = #{vacId,jdbcType=INTEGER}"
    })
    int deleteByPrimaryKey(Integer vacId);

    @Insert({
        "insert into e_vac_receive (vac_id, vac_addtime, ",
        "RecordSequenceID, UserIdType, ",
        "UserId, ServiceType, ",
        "SpId, ProductId, ",
        "UpdateType, UpdateTime, ",
        "UpdateDesc, LinkID, ",
        "Content, EffectiveDate, ",
        "ExpireDate, ResultCode, ",
        "Time_Stamp, EncodeStr, ",
        "user_id, user_name, ",
        "isPutWelcomeSMS)",
        "values (#{vacId,jdbcType=INTEGER}, #{vacAddtime,jdbcType=TIMESTAMP}, ",
        "#{recordsequenceid,jdbcType=VARCHAR}, #{useridtype,jdbcType=INTEGER}, ",
        "#{userid,jdbcType=VARCHAR}, #{servicetype,jdbcType=VARCHAR}, ",
        "#{spid,jdbcType=VARCHAR}, #{productid,jdbcType=VARCHAR}, ",
        "#{updatetype,jdbcType=INTEGER}, #{updatetime,jdbcType=VARCHAR}, ",
        "#{updatedesc,jdbcType=VARCHAR}, #{linkid,jdbcType=VARCHAR}, ",
        "#{content,jdbcType=VARCHAR}, #{effectivedate,jdbcType=VARCHAR}, ",
        "#{expiredate,jdbcType=VARCHAR}, #{resultcode,jdbcType=INTEGER}, ",
        "#{timeStamp,jdbcType=VARCHAR}, #{encodestr,jdbcType=VARCHAR}, ",
        "#{userId,jdbcType=INTEGER}, #{userName,jdbcType=VARCHAR}, ",
        "#{isputwelcomesms,jdbcType=BIT})"
    })
    int insert(VacReceiveMessage record);

    int insertSelective(VacReceiveMessage record);

    List<VacReceiveMessage> selectByExample(VacReceiveMessageExample example);

    @Select({
        "select",
        "vac_id, vac_addtime, RecordSequenceID, UserIdType, UserId, ServiceType, SpId, ",
        "ProductId, UpdateType, UpdateTime, UpdateDesc, LinkID, Content, EffectiveDate, ",
        "ExpireDate, ResultCode, Time_Stamp, EncodeStr, user_id, user_name, isPutWelcomeSMS",
        "from e_vac_receive",
        "where vac_id = #{vacId,jdbcType=INTEGER}"
    })
    @ResultMap("BaseResultMap")
    VacReceiveMessage selectByPrimaryKey(Integer vacId);

    int updateByExampleSelective(@Param("record") VacReceiveMessage record, @Param("example") VacReceiveMessageExample example);

    int updateByExample(@Param("record") VacReceiveMessage record, @Param("example") VacReceiveMessageExample example);

    int updateByPrimaryKeySelective(VacReceiveMessage record);

    @Update({
        "update e_vac_receive",
        "set vac_addtime = #{vacAddtime,jdbcType=TIMESTAMP},",
          "RecordSequenceID = #{recordsequenceid,jdbcType=VARCHAR},",
          "UserIdType = #{useridtype,jdbcType=INTEGER},",
          "UserId = #{userid,jdbcType=VARCHAR},",
          "ServiceType = #{servicetype,jdbcType=VARCHAR},",
          "SpId = #{spid,jdbcType=VARCHAR},",
          "ProductId = #{productid,jdbcType=VARCHAR},",
          "UpdateType = #{updatetype,jdbcType=INTEGER},",
          "UpdateTime = #{updatetime,jdbcType=VARCHAR},",
          "UpdateDesc = #{updatedesc,jdbcType=VARCHAR},",
          "LinkID = #{linkid,jdbcType=VARCHAR},",
          "Content = #{content,jdbcType=VARCHAR},",
          "EffectiveDate = #{effectivedate,jdbcType=VARCHAR},",
          "ExpireDate = #{expiredate,jdbcType=VARCHAR},",
          "ResultCode = #{resultcode,jdbcType=INTEGER},",
          "Time_Stamp = #{timeStamp,jdbcType=VARCHAR},",
          "EncodeStr = #{encodestr,jdbcType=VARCHAR},",
          "user_id = #{userId,jdbcType=INTEGER},",
          "user_name = #{userName,jdbcType=VARCHAR},",
          "isPutWelcomeSMS = #{isputwelcomesms,jdbcType=BIT}",
        "where vac_id = #{vacId,jdbcType=INTEGER}"
    })
    int updateByPrimaryKey(VacReceiveMessage record);

    List<VacReceiveMessage> selectPage(VacReceiveMessageExample example);
}