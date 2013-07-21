package org.vlg.linghu.mybatis.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.vlg.linghu.mybatis.bean.MmsSendMessage;
import org.vlg.linghu.mybatis.bean.MmsSendMessageExample;
import org.vlg.linghu.mybatis.bean.MmsSendMessageWithBLOBs;

public interface MmsSendMessageMapper {
    int countByExample(MmsSendMessageExample example);

    int deleteByExample(MmsSendMessageExample example);

    @Delete({
        "delete from e_mms_send",
        "where send_id = #{sendId,jdbcType=INTEGER}"
    })
    int deleteByPrimaryKey(Integer sendId);

    @Insert({
        "insert into e_mms_send (send_id, send_title, ",
        "send_addtime, send_pic, ",
        "send_music, send_smil_parnum, ",
        "user_id, user_name, ",
        "user_mobile, send_score, ",
        "send_mobile, send_status, ",
        "send_downtime, send_code, ",
        "msgId, linkId, back_msg, ",
        "send_isdel, index_id, ",
        "serviceID, send_text, ",
        "send_smil)",
        "values (#{sendId,jdbcType=INTEGER}, #{sendTitle,jdbcType=VARCHAR}, ",
        "#{sendAddtime,jdbcType=TIMESTAMP}, #{sendPic,jdbcType=VARCHAR}, ",
        "#{sendMusic,jdbcType=VARCHAR}, #{sendSmilParnum,jdbcType=INTEGER}, ",
        "#{userId,jdbcType=INTEGER}, #{userName,jdbcType=VARCHAR}, ",
        "#{userMobile,jdbcType=VARCHAR}, #{sendScore,jdbcType=INTEGER}, ",
        "#{sendMobile,jdbcType=VARCHAR}, #{sendStatus,jdbcType=INTEGER}, ",
        "#{sendDowntime,jdbcType=TIMESTAMP}, #{sendCode,jdbcType=CHAR}, ",
        "#{msgid,jdbcType=VARCHAR}, #{linkid,jdbcType=VARCHAR}, #{backMsg,jdbcType=VARCHAR}, ",
        "#{sendIsdel,jdbcType=INTEGER}, #{indexId,jdbcType=INTEGER}, ",
        "#{serviceid,jdbcType=VARCHAR}, #{sendText,jdbcType=CLOB}, ",
        "#{sendSmil,jdbcType=CLOB})"
    })
    int insert(MmsSendMessageWithBLOBs record);

    int insertSelective(MmsSendMessageWithBLOBs record);

    List<MmsSendMessageWithBLOBs> selectByExampleWithBLOBs(MmsSendMessageExample example);

    List<MmsSendMessage> selectByExample(MmsSendMessageExample example);

    @Select({
        "select",
        "send_id, send_title, send_addtime, send_pic, send_music, send_smil_parnum, user_id, ",
        "user_name, user_mobile, send_score, send_mobile, send_status, send_downtime, ",
        "send_code, msgId, linkId, back_msg, send_isdel, index_id, serviceID, send_text, ",
        "send_smil",
        "from e_mms_send",
        "where send_id = #{sendId,jdbcType=INTEGER}"
    })
    @ResultMap("ResultMapWithBLOBs")
    MmsSendMessageWithBLOBs selectByPrimaryKey(Integer sendId);

    int updateByExampleSelective(@Param("record") MmsSendMessageWithBLOBs record, @Param("example") MmsSendMessageExample example);

    int updateByExampleWithBLOBs(@Param("record") MmsSendMessageWithBLOBs record, @Param("example") MmsSendMessageExample example);

    int updateByExample(@Param("record") MmsSendMessage record, @Param("example") MmsSendMessageExample example);

    int updateByPrimaryKeySelective(MmsSendMessageWithBLOBs record);

    @Update({
        "update e_mms_send",
        "set send_title = #{sendTitle,jdbcType=VARCHAR},",
          "send_addtime = #{sendAddtime,jdbcType=TIMESTAMP},",
          "send_pic = #{sendPic,jdbcType=VARCHAR},",
          "send_music = #{sendMusic,jdbcType=VARCHAR},",
          "send_smil_parnum = #{sendSmilParnum,jdbcType=INTEGER},",
          "user_id = #{userId,jdbcType=INTEGER},",
          "user_name = #{userName,jdbcType=VARCHAR},",
          "user_mobile = #{userMobile,jdbcType=VARCHAR},",
          "send_score = #{sendScore,jdbcType=INTEGER},",
          "send_mobile = #{sendMobile,jdbcType=VARCHAR},",
          "send_status = #{sendStatus,jdbcType=INTEGER},",
          "send_downtime = #{sendDowntime,jdbcType=TIMESTAMP},",
          "send_code = #{sendCode,jdbcType=CHAR},",
          "msgId = #{msgid,jdbcType=VARCHAR},",
          "linkId = #{linkid,jdbcType=VARCHAR},",
          "back_msg = #{backMsg,jdbcType=VARCHAR},",
          "send_isdel = #{sendIsdel,jdbcType=INTEGER},",
          "index_id = #{indexId,jdbcType=INTEGER},",
          "serviceID = #{serviceid,jdbcType=VARCHAR},",
          "send_text = #{sendText,jdbcType=CLOB},",
          "send_smil = #{sendSmil,jdbcType=CLOB}",
        "where send_id = #{sendId,jdbcType=INTEGER}"
    })
    int updateByPrimaryKeyWithBLOBs(MmsSendMessageWithBLOBs record);

    @Update({
        "update e_mms_send",
        "set send_title = #{sendTitle,jdbcType=VARCHAR},",
          "send_addtime = #{sendAddtime,jdbcType=TIMESTAMP},",
          "send_pic = #{sendPic,jdbcType=VARCHAR},",
          "send_music = #{sendMusic,jdbcType=VARCHAR},",
          "send_smil_parnum = #{sendSmilParnum,jdbcType=INTEGER},",
          "user_id = #{userId,jdbcType=INTEGER},",
          "user_name = #{userName,jdbcType=VARCHAR},",
          "user_mobile = #{userMobile,jdbcType=VARCHAR},",
          "send_score = #{sendScore,jdbcType=INTEGER},",
          "send_mobile = #{sendMobile,jdbcType=VARCHAR},",
          "send_status = #{sendStatus,jdbcType=INTEGER},",
          "send_downtime = #{sendDowntime,jdbcType=TIMESTAMP},",
          "send_code = #{sendCode,jdbcType=CHAR},",
          "msgId = #{msgid,jdbcType=VARCHAR},",
          "linkId = #{linkid,jdbcType=VARCHAR},",
          "back_msg = #{backMsg,jdbcType=VARCHAR},",
          "send_isdel = #{sendIsdel,jdbcType=INTEGER},",
          "index_id = #{indexId,jdbcType=INTEGER},",
          "serviceID = #{serviceid,jdbcType=VARCHAR}",
        "where send_id = #{sendId,jdbcType=INTEGER}"
    })
    int updateByPrimaryKey(MmsSendMessage record);

    List<MmsSendMessage> selectPage(MmsSendMessageExample example);
}