package org.vlg.linghu.mybatis.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.vlg.linghu.mybatis.bean.SmsSendMessage;
import org.vlg.linghu.mybatis.bean.SmsSendMessageExample;

public interface SmsSendMessageMapper {
    int countByExample(SmsSendMessageExample example);

    int deleteByExample(SmsSendMessageExample example);

    @Delete({
        "delete from e_sms_send",
        "where send_id = #{sendId,jdbcType=INTEGER}"
    })
    int deleteByPrimaryKey(Integer sendId);

    @Insert({
        "insert into e_sms_send (send_id, user_id, ",
        "send_text, send_addtime, ",
        "send_status, send_downtime, ",
        "msgId, back_msg, ",
        "issend, serviceID)",
        "values (#{sendId,jdbcType=INTEGER}, #{userId,jdbcType=VARCHAR}, ",
        "#{sendText,jdbcType=VARCHAR}, #{sendAddtime,jdbcType=TIMESTAMP}, ",
        "#{sendStatus,jdbcType=INTEGER}, #{sendDowntime,jdbcType=TIMESTAMP}, ",
        "#{msgid,jdbcType=VARCHAR}, #{backMsg,jdbcType=VARCHAR}, ",
        "#{issend,jdbcType=BIT}, #{serviceid,jdbcType=VARCHAR})"
    })
    int insert(SmsSendMessage record);

    int insertSelective(SmsSendMessage record);

    List<SmsSendMessage> selectByExample(SmsSendMessageExample example);

    @Select({
        "select",
        "send_id, user_id, send_text, send_addtime, send_status, send_downtime, msgId, ",
        "back_msg, issend, serviceID",
        "from e_sms_send",
        "where send_id = #{sendId,jdbcType=INTEGER}"
    })
    @ResultMap("BaseResultMap")
    SmsSendMessage selectByPrimaryKey(Integer sendId);

    int updateByExampleSelective(@Param("record") SmsSendMessage record, @Param("example") SmsSendMessageExample example);

    int updateByExample(@Param("record") SmsSendMessage record, @Param("example") SmsSendMessageExample example);

    int updateByPrimaryKeySelective(SmsSendMessage record);

    @Update({
        "update e_sms_send",
        "set user_id = #{userId,jdbcType=VARCHAR},",
          "send_text = #{sendText,jdbcType=VARCHAR},",
          "send_addtime = #{sendAddtime,jdbcType=TIMESTAMP},",
          "send_status = #{sendStatus,jdbcType=INTEGER},",
          "send_downtime = #{sendDowntime,jdbcType=TIMESTAMP},",
          "msgId = #{msgid,jdbcType=VARCHAR},",
          "back_msg = #{backMsg,jdbcType=VARCHAR},",
          "issend = #{issend,jdbcType=BIT},",
          "serviceID = #{serviceid,jdbcType=VARCHAR}",
        "where send_id = #{sendId,jdbcType=INTEGER}"
    })
    int updateByPrimaryKey(SmsSendMessage record);

    List<SmsSendMessage> selectPage(SmsSendMessageExample example);
}