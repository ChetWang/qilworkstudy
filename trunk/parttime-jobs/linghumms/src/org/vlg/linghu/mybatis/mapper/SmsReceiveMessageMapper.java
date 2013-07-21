package org.vlg.linghu.mybatis.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.vlg.linghu.mybatis.bean.SmsReceiveMessage;
import org.vlg.linghu.mybatis.bean.SmsReceiveMessageExample;

public interface SmsReceiveMessageMapper {
    int countByExample(SmsReceiveMessageExample example);

    int deleteByExample(SmsReceiveMessageExample example);

    @Delete({
        "delete from e_sms_receive",
        "where receive_id = #{receiveId,jdbcType=INTEGER}"
    })
    int deleteByPrimaryKey(Integer receiveId);

    @Insert({
        "insert into e_sms_receive (receive_id, receive_addtime, ",
        "receive_text, user_id, ",
        "isOK, serviceID)",
        "values (#{receiveId,jdbcType=INTEGER}, #{receiveAddtime,jdbcType=TIMESTAMP}, ",
        "#{receiveText,jdbcType=VARCHAR}, #{userId,jdbcType=VARCHAR}, ",
        "#{isok,jdbcType=BIT}, #{serviceid,jdbcType=VARCHAR})"
    })
    int insert(SmsReceiveMessage record);

    int insertSelective(SmsReceiveMessage record);

    List<SmsReceiveMessage> selectByExample(SmsReceiveMessageExample example);

    @Select({
        "select",
        "receive_id, receive_addtime, receive_text, user_id, isOK, serviceID",
        "from e_sms_receive",
        "where receive_id = #{receiveId,jdbcType=INTEGER}"
    })
    @ResultMap("BaseResultMap")
    SmsReceiveMessage selectByPrimaryKey(Integer receiveId);

    int updateByExampleSelective(@Param("record") SmsReceiveMessage record, @Param("example") SmsReceiveMessageExample example);

    int updateByExample(@Param("record") SmsReceiveMessage record, @Param("example") SmsReceiveMessageExample example);

    int updateByPrimaryKeySelective(SmsReceiveMessage record);

    @Update({
        "update e_sms_receive",
        "set receive_addtime = #{receiveAddtime,jdbcType=TIMESTAMP},",
          "receive_text = #{receiveText,jdbcType=VARCHAR},",
          "user_id = #{userId,jdbcType=VARCHAR},",
          "isOK = #{isok,jdbcType=BIT},",
          "serviceID = #{serviceid,jdbcType=VARCHAR}",
        "where receive_id = #{receiveId,jdbcType=INTEGER}"
    })
    int updateByPrimaryKey(SmsReceiveMessage record);

    List<SmsReceiveMessage> selectPage(SmsReceiveMessageExample example);
}