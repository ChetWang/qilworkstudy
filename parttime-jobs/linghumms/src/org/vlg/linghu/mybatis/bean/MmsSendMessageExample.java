package org.vlg.linghu.mybatis.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MmsSendMessageExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    private int pageIndex;

    private int pageSize;

    public MmsSendMessageExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    public String getOrderByClause() {
        return orderByClause;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public boolean isDistinct() {
        return distinct;
    }

    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex=pageIndex;
    }

    public int getPageIndex() {
        return this.pageIndex;
    }

    public void setPageSize(int pageSize) {
        this.pageSize=pageSize;
    }

    public int getPageSize() {
        return this.pageSize;
    }

    public int getSkipRecordCount() {
        return (this.pageIndex-1)*this.pageSize;
    }

    public int getEndRecordCount() {
        return this.pageIndex*this.pageSize;
    }

    public MmsSendMessageExample(int pageSize, int pageIndex) {
        this();
        this.pageSize=pageSize;
        this.pageIndex=pageIndex;
    }

    protected abstract static class GeneratedCriteria {
        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<Criterion>();
        }

        public boolean isValid() {
            return criteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            return criteria;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(condition));
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
        }

        public Criteria andSendIdIsNull() {
            addCriterion("send_id is null");
            return (Criteria) this;
        }

        public Criteria andSendIdIsNotNull() {
            addCriterion("send_id is not null");
            return (Criteria) this;
        }

        public Criteria andSendIdEqualTo(Integer value) {
            addCriterion("send_id =", value, "sendId");
            return (Criteria) this;
        }

        public Criteria andSendIdNotEqualTo(Integer value) {
            addCriterion("send_id <>", value, "sendId");
            return (Criteria) this;
        }

        public Criteria andSendIdGreaterThan(Integer value) {
            addCriterion("send_id >", value, "sendId");
            return (Criteria) this;
        }

        public Criteria andSendIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("send_id >=", value, "sendId");
            return (Criteria) this;
        }

        public Criteria andSendIdLessThan(Integer value) {
            addCriterion("send_id <", value, "sendId");
            return (Criteria) this;
        }

        public Criteria andSendIdLessThanOrEqualTo(Integer value) {
            addCriterion("send_id <=", value, "sendId");
            return (Criteria) this;
        }

        public Criteria andSendIdIn(List<Integer> values) {
            addCriterion("send_id in", values, "sendId");
            return (Criteria) this;
        }

        public Criteria andSendIdNotIn(List<Integer> values) {
            addCriterion("send_id not in", values, "sendId");
            return (Criteria) this;
        }

        public Criteria andSendIdBetween(Integer value1, Integer value2) {
            addCriterion("send_id between", value1, value2, "sendId");
            return (Criteria) this;
        }

        public Criteria andSendIdNotBetween(Integer value1, Integer value2) {
            addCriterion("send_id not between", value1, value2, "sendId");
            return (Criteria) this;
        }

        public Criteria andSendTitleIsNull() {
            addCriterion("send_title is null");
            return (Criteria) this;
        }

        public Criteria andSendTitleIsNotNull() {
            addCriterion("send_title is not null");
            return (Criteria) this;
        }

        public Criteria andSendTitleEqualTo(String value) {
            addCriterion("send_title =", value, "sendTitle");
            return (Criteria) this;
        }

        public Criteria andSendTitleNotEqualTo(String value) {
            addCriterion("send_title <>", value, "sendTitle");
            return (Criteria) this;
        }

        public Criteria andSendTitleGreaterThan(String value) {
            addCriterion("send_title >", value, "sendTitle");
            return (Criteria) this;
        }

        public Criteria andSendTitleGreaterThanOrEqualTo(String value) {
            addCriterion("send_title >=", value, "sendTitle");
            return (Criteria) this;
        }

        public Criteria andSendTitleLessThan(String value) {
            addCriterion("send_title <", value, "sendTitle");
            return (Criteria) this;
        }

        public Criteria andSendTitleLessThanOrEqualTo(String value) {
            addCriterion("send_title <=", value, "sendTitle");
            return (Criteria) this;
        }

        public Criteria andSendTitleLike(String value) {
            addCriterion("send_title like", value, "sendTitle");
            return (Criteria) this;
        }

        public Criteria andSendTitleNotLike(String value) {
            addCriterion("send_title not like", value, "sendTitle");
            return (Criteria) this;
        }

        public Criteria andSendTitleIn(List<String> values) {
            addCriterion("send_title in", values, "sendTitle");
            return (Criteria) this;
        }

        public Criteria andSendTitleNotIn(List<String> values) {
            addCriterion("send_title not in", values, "sendTitle");
            return (Criteria) this;
        }

        public Criteria andSendTitleBetween(String value1, String value2) {
            addCriterion("send_title between", value1, value2, "sendTitle");
            return (Criteria) this;
        }

        public Criteria andSendTitleNotBetween(String value1, String value2) {
            addCriterion("send_title not between", value1, value2, "sendTitle");
            return (Criteria) this;
        }

        public Criteria andSendAddtimeIsNull() {
            addCriterion("send_addtime is null");
            return (Criteria) this;
        }

        public Criteria andSendAddtimeIsNotNull() {
            addCriterion("send_addtime is not null");
            return (Criteria) this;
        }

        public Criteria andSendAddtimeEqualTo(Date value) {
            addCriterion("send_addtime =", value, "sendAddtime");
            return (Criteria) this;
        }

        public Criteria andSendAddtimeNotEqualTo(Date value) {
            addCriterion("send_addtime <>", value, "sendAddtime");
            return (Criteria) this;
        }

        public Criteria andSendAddtimeGreaterThan(Date value) {
            addCriterion("send_addtime >", value, "sendAddtime");
            return (Criteria) this;
        }

        public Criteria andSendAddtimeGreaterThanOrEqualTo(Date value) {
            addCriterion("send_addtime >=", value, "sendAddtime");
            return (Criteria) this;
        }

        public Criteria andSendAddtimeLessThan(Date value) {
            addCriterion("send_addtime <", value, "sendAddtime");
            return (Criteria) this;
        }

        public Criteria andSendAddtimeLessThanOrEqualTo(Date value) {
            addCriterion("send_addtime <=", value, "sendAddtime");
            return (Criteria) this;
        }

        public Criteria andSendAddtimeIn(List<Date> values) {
            addCriterion("send_addtime in", values, "sendAddtime");
            return (Criteria) this;
        }

        public Criteria andSendAddtimeNotIn(List<Date> values) {
            addCriterion("send_addtime not in", values, "sendAddtime");
            return (Criteria) this;
        }

        public Criteria andSendAddtimeBetween(Date value1, Date value2) {
            addCriterion("send_addtime between", value1, value2, "sendAddtime");
            return (Criteria) this;
        }

        public Criteria andSendAddtimeNotBetween(Date value1, Date value2) {
            addCriterion("send_addtime not between", value1, value2, "sendAddtime");
            return (Criteria) this;
        }

        public Criteria andSendPicIsNull() {
            addCriterion("send_pic is null");
            return (Criteria) this;
        }

        public Criteria andSendPicIsNotNull() {
            addCriterion("send_pic is not null");
            return (Criteria) this;
        }

        public Criteria andSendPicEqualTo(String value) {
            addCriterion("send_pic =", value, "sendPic");
            return (Criteria) this;
        }

        public Criteria andSendPicNotEqualTo(String value) {
            addCriterion("send_pic <>", value, "sendPic");
            return (Criteria) this;
        }

        public Criteria andSendPicGreaterThan(String value) {
            addCriterion("send_pic >", value, "sendPic");
            return (Criteria) this;
        }

        public Criteria andSendPicGreaterThanOrEqualTo(String value) {
            addCriterion("send_pic >=", value, "sendPic");
            return (Criteria) this;
        }

        public Criteria andSendPicLessThan(String value) {
            addCriterion("send_pic <", value, "sendPic");
            return (Criteria) this;
        }

        public Criteria andSendPicLessThanOrEqualTo(String value) {
            addCriterion("send_pic <=", value, "sendPic");
            return (Criteria) this;
        }

        public Criteria andSendPicLike(String value) {
            addCriterion("send_pic like", value, "sendPic");
            return (Criteria) this;
        }

        public Criteria andSendPicNotLike(String value) {
            addCriterion("send_pic not like", value, "sendPic");
            return (Criteria) this;
        }

        public Criteria andSendPicIn(List<String> values) {
            addCriterion("send_pic in", values, "sendPic");
            return (Criteria) this;
        }

        public Criteria andSendPicNotIn(List<String> values) {
            addCriterion("send_pic not in", values, "sendPic");
            return (Criteria) this;
        }

        public Criteria andSendPicBetween(String value1, String value2) {
            addCriterion("send_pic between", value1, value2, "sendPic");
            return (Criteria) this;
        }

        public Criteria andSendPicNotBetween(String value1, String value2) {
            addCriterion("send_pic not between", value1, value2, "sendPic");
            return (Criteria) this;
        }

        public Criteria andSendMusicIsNull() {
            addCriterion("send_music is null");
            return (Criteria) this;
        }

        public Criteria andSendMusicIsNotNull() {
            addCriterion("send_music is not null");
            return (Criteria) this;
        }

        public Criteria andSendMusicEqualTo(String value) {
            addCriterion("send_music =", value, "sendMusic");
            return (Criteria) this;
        }

        public Criteria andSendMusicNotEqualTo(String value) {
            addCriterion("send_music <>", value, "sendMusic");
            return (Criteria) this;
        }

        public Criteria andSendMusicGreaterThan(String value) {
            addCriterion("send_music >", value, "sendMusic");
            return (Criteria) this;
        }

        public Criteria andSendMusicGreaterThanOrEqualTo(String value) {
            addCriterion("send_music >=", value, "sendMusic");
            return (Criteria) this;
        }

        public Criteria andSendMusicLessThan(String value) {
            addCriterion("send_music <", value, "sendMusic");
            return (Criteria) this;
        }

        public Criteria andSendMusicLessThanOrEqualTo(String value) {
            addCriterion("send_music <=", value, "sendMusic");
            return (Criteria) this;
        }

        public Criteria andSendMusicLike(String value) {
            addCriterion("send_music like", value, "sendMusic");
            return (Criteria) this;
        }

        public Criteria andSendMusicNotLike(String value) {
            addCriterion("send_music not like", value, "sendMusic");
            return (Criteria) this;
        }

        public Criteria andSendMusicIn(List<String> values) {
            addCriterion("send_music in", values, "sendMusic");
            return (Criteria) this;
        }

        public Criteria andSendMusicNotIn(List<String> values) {
            addCriterion("send_music not in", values, "sendMusic");
            return (Criteria) this;
        }

        public Criteria andSendMusicBetween(String value1, String value2) {
            addCriterion("send_music between", value1, value2, "sendMusic");
            return (Criteria) this;
        }

        public Criteria andSendMusicNotBetween(String value1, String value2) {
            addCriterion("send_music not between", value1, value2, "sendMusic");
            return (Criteria) this;
        }

        public Criteria andSendSmilParnumIsNull() {
            addCriterion("send_smil_parnum is null");
            return (Criteria) this;
        }

        public Criteria andSendSmilParnumIsNotNull() {
            addCriterion("send_smil_parnum is not null");
            return (Criteria) this;
        }

        public Criteria andSendSmilParnumEqualTo(Integer value) {
            addCriterion("send_smil_parnum =", value, "sendSmilParnum");
            return (Criteria) this;
        }

        public Criteria andSendSmilParnumNotEqualTo(Integer value) {
            addCriterion("send_smil_parnum <>", value, "sendSmilParnum");
            return (Criteria) this;
        }

        public Criteria andSendSmilParnumGreaterThan(Integer value) {
            addCriterion("send_smil_parnum >", value, "sendSmilParnum");
            return (Criteria) this;
        }

        public Criteria andSendSmilParnumGreaterThanOrEqualTo(Integer value) {
            addCriterion("send_smil_parnum >=", value, "sendSmilParnum");
            return (Criteria) this;
        }

        public Criteria andSendSmilParnumLessThan(Integer value) {
            addCriterion("send_smil_parnum <", value, "sendSmilParnum");
            return (Criteria) this;
        }

        public Criteria andSendSmilParnumLessThanOrEqualTo(Integer value) {
            addCriterion("send_smil_parnum <=", value, "sendSmilParnum");
            return (Criteria) this;
        }

        public Criteria andSendSmilParnumIn(List<Integer> values) {
            addCriterion("send_smil_parnum in", values, "sendSmilParnum");
            return (Criteria) this;
        }

        public Criteria andSendSmilParnumNotIn(List<Integer> values) {
            addCriterion("send_smil_parnum not in", values, "sendSmilParnum");
            return (Criteria) this;
        }

        public Criteria andSendSmilParnumBetween(Integer value1, Integer value2) {
            addCriterion("send_smil_parnum between", value1, value2, "sendSmilParnum");
            return (Criteria) this;
        }

        public Criteria andSendSmilParnumNotBetween(Integer value1, Integer value2) {
            addCriterion("send_smil_parnum not between", value1, value2, "sendSmilParnum");
            return (Criteria) this;
        }

        public Criteria andUserIdIsNull() {
            addCriterion("user_id is null");
            return (Criteria) this;
        }

        public Criteria andUserIdIsNotNull() {
            addCriterion("user_id is not null");
            return (Criteria) this;
        }

        public Criteria andUserIdEqualTo(Integer value) {
            addCriterion("user_id =", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdNotEqualTo(Integer value) {
            addCriterion("user_id <>", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdGreaterThan(Integer value) {
            addCriterion("user_id >", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("user_id >=", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdLessThan(Integer value) {
            addCriterion("user_id <", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdLessThanOrEqualTo(Integer value) {
            addCriterion("user_id <=", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdIn(List<Integer> values) {
            addCriterion("user_id in", values, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdNotIn(List<Integer> values) {
            addCriterion("user_id not in", values, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdBetween(Integer value1, Integer value2) {
            addCriterion("user_id between", value1, value2, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdNotBetween(Integer value1, Integer value2) {
            addCriterion("user_id not between", value1, value2, "userId");
            return (Criteria) this;
        }

        public Criteria andUserNameIsNull() {
            addCriterion("user_name is null");
            return (Criteria) this;
        }

        public Criteria andUserNameIsNotNull() {
            addCriterion("user_name is not null");
            return (Criteria) this;
        }

        public Criteria andUserNameEqualTo(String value) {
            addCriterion("user_name =", value, "userName");
            return (Criteria) this;
        }

        public Criteria andUserNameNotEqualTo(String value) {
            addCriterion("user_name <>", value, "userName");
            return (Criteria) this;
        }

        public Criteria andUserNameGreaterThan(String value) {
            addCriterion("user_name >", value, "userName");
            return (Criteria) this;
        }

        public Criteria andUserNameGreaterThanOrEqualTo(String value) {
            addCriterion("user_name >=", value, "userName");
            return (Criteria) this;
        }

        public Criteria andUserNameLessThan(String value) {
            addCriterion("user_name <", value, "userName");
            return (Criteria) this;
        }

        public Criteria andUserNameLessThanOrEqualTo(String value) {
            addCriterion("user_name <=", value, "userName");
            return (Criteria) this;
        }

        public Criteria andUserNameLike(String value) {
            addCriterion("user_name like", value, "userName");
            return (Criteria) this;
        }

        public Criteria andUserNameNotLike(String value) {
            addCriterion("user_name not like", value, "userName");
            return (Criteria) this;
        }

        public Criteria andUserNameIn(List<String> values) {
            addCriterion("user_name in", values, "userName");
            return (Criteria) this;
        }

        public Criteria andUserNameNotIn(List<String> values) {
            addCriterion("user_name not in", values, "userName");
            return (Criteria) this;
        }

        public Criteria andUserNameBetween(String value1, String value2) {
            addCriterion("user_name between", value1, value2, "userName");
            return (Criteria) this;
        }

        public Criteria andUserNameNotBetween(String value1, String value2) {
            addCriterion("user_name not between", value1, value2, "userName");
            return (Criteria) this;
        }

        public Criteria andUserMobileIsNull() {
            addCriterion("user_mobile is null");
            return (Criteria) this;
        }

        public Criteria andUserMobileIsNotNull() {
            addCriterion("user_mobile is not null");
            return (Criteria) this;
        }

        public Criteria andUserMobileEqualTo(String value) {
            addCriterion("user_mobile =", value, "userMobile");
            return (Criteria) this;
        }

        public Criteria andUserMobileNotEqualTo(String value) {
            addCriterion("user_mobile <>", value, "userMobile");
            return (Criteria) this;
        }

        public Criteria andUserMobileGreaterThan(String value) {
            addCriterion("user_mobile >", value, "userMobile");
            return (Criteria) this;
        }

        public Criteria andUserMobileGreaterThanOrEqualTo(String value) {
            addCriterion("user_mobile >=", value, "userMobile");
            return (Criteria) this;
        }

        public Criteria andUserMobileLessThan(String value) {
            addCriterion("user_mobile <", value, "userMobile");
            return (Criteria) this;
        }

        public Criteria andUserMobileLessThanOrEqualTo(String value) {
            addCriterion("user_mobile <=", value, "userMobile");
            return (Criteria) this;
        }

        public Criteria andUserMobileLike(String value) {
            addCriterion("user_mobile like", value, "userMobile");
            return (Criteria) this;
        }

        public Criteria andUserMobileNotLike(String value) {
            addCriterion("user_mobile not like", value, "userMobile");
            return (Criteria) this;
        }

        public Criteria andUserMobileIn(List<String> values) {
            addCriterion("user_mobile in", values, "userMobile");
            return (Criteria) this;
        }

        public Criteria andUserMobileNotIn(List<String> values) {
            addCriterion("user_mobile not in", values, "userMobile");
            return (Criteria) this;
        }

        public Criteria andUserMobileBetween(String value1, String value2) {
            addCriterion("user_mobile between", value1, value2, "userMobile");
            return (Criteria) this;
        }

        public Criteria andUserMobileNotBetween(String value1, String value2) {
            addCriterion("user_mobile not between", value1, value2, "userMobile");
            return (Criteria) this;
        }

        public Criteria andSendScoreIsNull() {
            addCriterion("send_score is null");
            return (Criteria) this;
        }

        public Criteria andSendScoreIsNotNull() {
            addCriterion("send_score is not null");
            return (Criteria) this;
        }

        public Criteria andSendScoreEqualTo(Integer value) {
            addCriterion("send_score =", value, "sendScore");
            return (Criteria) this;
        }

        public Criteria andSendScoreNotEqualTo(Integer value) {
            addCriterion("send_score <>", value, "sendScore");
            return (Criteria) this;
        }

        public Criteria andSendScoreGreaterThan(Integer value) {
            addCriterion("send_score >", value, "sendScore");
            return (Criteria) this;
        }

        public Criteria andSendScoreGreaterThanOrEqualTo(Integer value) {
            addCriterion("send_score >=", value, "sendScore");
            return (Criteria) this;
        }

        public Criteria andSendScoreLessThan(Integer value) {
            addCriterion("send_score <", value, "sendScore");
            return (Criteria) this;
        }

        public Criteria andSendScoreLessThanOrEqualTo(Integer value) {
            addCriterion("send_score <=", value, "sendScore");
            return (Criteria) this;
        }

        public Criteria andSendScoreIn(List<Integer> values) {
            addCriterion("send_score in", values, "sendScore");
            return (Criteria) this;
        }

        public Criteria andSendScoreNotIn(List<Integer> values) {
            addCriterion("send_score not in", values, "sendScore");
            return (Criteria) this;
        }

        public Criteria andSendScoreBetween(Integer value1, Integer value2) {
            addCriterion("send_score between", value1, value2, "sendScore");
            return (Criteria) this;
        }

        public Criteria andSendScoreNotBetween(Integer value1, Integer value2) {
            addCriterion("send_score not between", value1, value2, "sendScore");
            return (Criteria) this;
        }

        public Criteria andSendMobileIsNull() {
            addCriterion("send_mobile is null");
            return (Criteria) this;
        }

        public Criteria andSendMobileIsNotNull() {
            addCriterion("send_mobile is not null");
            return (Criteria) this;
        }

        public Criteria andSendMobileEqualTo(String value) {
            addCriterion("send_mobile =", value, "sendMobile");
            return (Criteria) this;
        }

        public Criteria andSendMobileNotEqualTo(String value) {
            addCriterion("send_mobile <>", value, "sendMobile");
            return (Criteria) this;
        }

        public Criteria andSendMobileGreaterThan(String value) {
            addCriterion("send_mobile >", value, "sendMobile");
            return (Criteria) this;
        }

        public Criteria andSendMobileGreaterThanOrEqualTo(String value) {
            addCriterion("send_mobile >=", value, "sendMobile");
            return (Criteria) this;
        }

        public Criteria andSendMobileLessThan(String value) {
            addCriterion("send_mobile <", value, "sendMobile");
            return (Criteria) this;
        }

        public Criteria andSendMobileLessThanOrEqualTo(String value) {
            addCriterion("send_mobile <=", value, "sendMobile");
            return (Criteria) this;
        }

        public Criteria andSendMobileLike(String value) {
            addCriterion("send_mobile like", value, "sendMobile");
            return (Criteria) this;
        }

        public Criteria andSendMobileNotLike(String value) {
            addCriterion("send_mobile not like", value, "sendMobile");
            return (Criteria) this;
        }

        public Criteria andSendMobileIn(List<String> values) {
            addCriterion("send_mobile in", values, "sendMobile");
            return (Criteria) this;
        }

        public Criteria andSendMobileNotIn(List<String> values) {
            addCriterion("send_mobile not in", values, "sendMobile");
            return (Criteria) this;
        }

        public Criteria andSendMobileBetween(String value1, String value2) {
            addCriterion("send_mobile between", value1, value2, "sendMobile");
            return (Criteria) this;
        }

        public Criteria andSendMobileNotBetween(String value1, String value2) {
            addCriterion("send_mobile not between", value1, value2, "sendMobile");
            return (Criteria) this;
        }

        public Criteria andSendStatusIsNull() {
            addCriterion("send_status is null");
            return (Criteria) this;
        }

        public Criteria andSendStatusIsNotNull() {
            addCriterion("send_status is not null");
            return (Criteria) this;
        }

        public Criteria andSendStatusEqualTo(Integer value) {
            addCriterion("send_status =", value, "sendStatus");
            return (Criteria) this;
        }

        public Criteria andSendStatusNotEqualTo(Integer value) {
            addCriterion("send_status <>", value, "sendStatus");
            return (Criteria) this;
        }

        public Criteria andSendStatusGreaterThan(Integer value) {
            addCriterion("send_status >", value, "sendStatus");
            return (Criteria) this;
        }

        public Criteria andSendStatusGreaterThanOrEqualTo(Integer value) {
            addCriterion("send_status >=", value, "sendStatus");
            return (Criteria) this;
        }

        public Criteria andSendStatusLessThan(Integer value) {
            addCriterion("send_status <", value, "sendStatus");
            return (Criteria) this;
        }

        public Criteria andSendStatusLessThanOrEqualTo(Integer value) {
            addCriterion("send_status <=", value, "sendStatus");
            return (Criteria) this;
        }

        public Criteria andSendStatusIn(List<Integer> values) {
            addCriterion("send_status in", values, "sendStatus");
            return (Criteria) this;
        }

        public Criteria andSendStatusNotIn(List<Integer> values) {
            addCriterion("send_status not in", values, "sendStatus");
            return (Criteria) this;
        }

        public Criteria andSendStatusBetween(Integer value1, Integer value2) {
            addCriterion("send_status between", value1, value2, "sendStatus");
            return (Criteria) this;
        }

        public Criteria andSendStatusNotBetween(Integer value1, Integer value2) {
            addCriterion("send_status not between", value1, value2, "sendStatus");
            return (Criteria) this;
        }

        public Criteria andSendDowntimeIsNull() {
            addCriterion("send_downtime is null");
            return (Criteria) this;
        }

        public Criteria andSendDowntimeIsNotNull() {
            addCriterion("send_downtime is not null");
            return (Criteria) this;
        }

        public Criteria andSendDowntimeEqualTo(Date value) {
            addCriterion("send_downtime =", value, "sendDowntime");
            return (Criteria) this;
        }

        public Criteria andSendDowntimeNotEqualTo(Date value) {
            addCriterion("send_downtime <>", value, "sendDowntime");
            return (Criteria) this;
        }

        public Criteria andSendDowntimeGreaterThan(Date value) {
            addCriterion("send_downtime >", value, "sendDowntime");
            return (Criteria) this;
        }

        public Criteria andSendDowntimeGreaterThanOrEqualTo(Date value) {
            addCriterion("send_downtime >=", value, "sendDowntime");
            return (Criteria) this;
        }

        public Criteria andSendDowntimeLessThan(Date value) {
            addCriterion("send_downtime <", value, "sendDowntime");
            return (Criteria) this;
        }

        public Criteria andSendDowntimeLessThanOrEqualTo(Date value) {
            addCriterion("send_downtime <=", value, "sendDowntime");
            return (Criteria) this;
        }

        public Criteria andSendDowntimeIn(List<Date> values) {
            addCriterion("send_downtime in", values, "sendDowntime");
            return (Criteria) this;
        }

        public Criteria andSendDowntimeNotIn(List<Date> values) {
            addCriterion("send_downtime not in", values, "sendDowntime");
            return (Criteria) this;
        }

        public Criteria andSendDowntimeBetween(Date value1, Date value2) {
            addCriterion("send_downtime between", value1, value2, "sendDowntime");
            return (Criteria) this;
        }

        public Criteria andSendDowntimeNotBetween(Date value1, Date value2) {
            addCriterion("send_downtime not between", value1, value2, "sendDowntime");
            return (Criteria) this;
        }

        public Criteria andSendCodeIsNull() {
            addCriterion("send_code is null");
            return (Criteria) this;
        }

        public Criteria andSendCodeIsNotNull() {
            addCriterion("send_code is not null");
            return (Criteria) this;
        }

        public Criteria andSendCodeEqualTo(String value) {
            addCriterion("send_code =", value, "sendCode");
            return (Criteria) this;
        }

        public Criteria andSendCodeNotEqualTo(String value) {
            addCriterion("send_code <>", value, "sendCode");
            return (Criteria) this;
        }

        public Criteria andSendCodeGreaterThan(String value) {
            addCriterion("send_code >", value, "sendCode");
            return (Criteria) this;
        }

        public Criteria andSendCodeGreaterThanOrEqualTo(String value) {
            addCriterion("send_code >=", value, "sendCode");
            return (Criteria) this;
        }

        public Criteria andSendCodeLessThan(String value) {
            addCriterion("send_code <", value, "sendCode");
            return (Criteria) this;
        }

        public Criteria andSendCodeLessThanOrEqualTo(String value) {
            addCriterion("send_code <=", value, "sendCode");
            return (Criteria) this;
        }

        public Criteria andSendCodeLike(String value) {
            addCriterion("send_code like", value, "sendCode");
            return (Criteria) this;
        }

        public Criteria andSendCodeNotLike(String value) {
            addCriterion("send_code not like", value, "sendCode");
            return (Criteria) this;
        }

        public Criteria andSendCodeIn(List<String> values) {
            addCriterion("send_code in", values, "sendCode");
            return (Criteria) this;
        }

        public Criteria andSendCodeNotIn(List<String> values) {
            addCriterion("send_code not in", values, "sendCode");
            return (Criteria) this;
        }

        public Criteria andSendCodeBetween(String value1, String value2) {
            addCriterion("send_code between", value1, value2, "sendCode");
            return (Criteria) this;
        }

        public Criteria andSendCodeNotBetween(String value1, String value2) {
            addCriterion("send_code not between", value1, value2, "sendCode");
            return (Criteria) this;
        }

        public Criteria andMsgidIsNull() {
            addCriterion("msgId is null");
            return (Criteria) this;
        }

        public Criteria andMsgidIsNotNull() {
            addCriterion("msgId is not null");
            return (Criteria) this;
        }

        public Criteria andMsgidEqualTo(String value) {
            addCriterion("msgId =", value, "msgid");
            return (Criteria) this;
        }

        public Criteria andMsgidNotEqualTo(String value) {
            addCriterion("msgId <>", value, "msgid");
            return (Criteria) this;
        }

        public Criteria andMsgidGreaterThan(String value) {
            addCriterion("msgId >", value, "msgid");
            return (Criteria) this;
        }

        public Criteria andMsgidGreaterThanOrEqualTo(String value) {
            addCriterion("msgId >=", value, "msgid");
            return (Criteria) this;
        }

        public Criteria andMsgidLessThan(String value) {
            addCriterion("msgId <", value, "msgid");
            return (Criteria) this;
        }

        public Criteria andMsgidLessThanOrEqualTo(String value) {
            addCriterion("msgId <=", value, "msgid");
            return (Criteria) this;
        }

        public Criteria andMsgidLike(String value) {
            addCriterion("msgId like", value, "msgid");
            return (Criteria) this;
        }

        public Criteria andMsgidNotLike(String value) {
            addCriterion("msgId not like", value, "msgid");
            return (Criteria) this;
        }

        public Criteria andMsgidIn(List<String> values) {
            addCriterion("msgId in", values, "msgid");
            return (Criteria) this;
        }

        public Criteria andMsgidNotIn(List<String> values) {
            addCriterion("msgId not in", values, "msgid");
            return (Criteria) this;
        }

        public Criteria andMsgidBetween(String value1, String value2) {
            addCriterion("msgId between", value1, value2, "msgid");
            return (Criteria) this;
        }

        public Criteria andMsgidNotBetween(String value1, String value2) {
            addCriterion("msgId not between", value1, value2, "msgid");
            return (Criteria) this;
        }

        public Criteria andLinkidIsNull() {
            addCriterion("linkId is null");
            return (Criteria) this;
        }

        public Criteria andLinkidIsNotNull() {
            addCriterion("linkId is not null");
            return (Criteria) this;
        }

        public Criteria andLinkidEqualTo(String value) {
            addCriterion("linkId =", value, "linkid");
            return (Criteria) this;
        }

        public Criteria andLinkidNotEqualTo(String value) {
            addCriterion("linkId <>", value, "linkid");
            return (Criteria) this;
        }

        public Criteria andLinkidGreaterThan(String value) {
            addCriterion("linkId >", value, "linkid");
            return (Criteria) this;
        }

        public Criteria andLinkidGreaterThanOrEqualTo(String value) {
            addCriterion("linkId >=", value, "linkid");
            return (Criteria) this;
        }

        public Criteria andLinkidLessThan(String value) {
            addCriterion("linkId <", value, "linkid");
            return (Criteria) this;
        }

        public Criteria andLinkidLessThanOrEqualTo(String value) {
            addCriterion("linkId <=", value, "linkid");
            return (Criteria) this;
        }

        public Criteria andLinkidLike(String value) {
            addCriterion("linkId like", value, "linkid");
            return (Criteria) this;
        }

        public Criteria andLinkidNotLike(String value) {
            addCriterion("linkId not like", value, "linkid");
            return (Criteria) this;
        }

        public Criteria andLinkidIn(List<String> values) {
            addCriterion("linkId in", values, "linkid");
            return (Criteria) this;
        }

        public Criteria andLinkidNotIn(List<String> values) {
            addCriterion("linkId not in", values, "linkid");
            return (Criteria) this;
        }

        public Criteria andLinkidBetween(String value1, String value2) {
            addCriterion("linkId between", value1, value2, "linkid");
            return (Criteria) this;
        }

        public Criteria andLinkidNotBetween(String value1, String value2) {
            addCriterion("linkId not between", value1, value2, "linkid");
            return (Criteria) this;
        }

        public Criteria andBackMsgIsNull() {
            addCriterion("back_msg is null");
            return (Criteria) this;
        }

        public Criteria andBackMsgIsNotNull() {
            addCriterion("back_msg is not null");
            return (Criteria) this;
        }

        public Criteria andBackMsgEqualTo(String value) {
            addCriterion("back_msg =", value, "backMsg");
            return (Criteria) this;
        }

        public Criteria andBackMsgNotEqualTo(String value) {
            addCriterion("back_msg <>", value, "backMsg");
            return (Criteria) this;
        }

        public Criteria andBackMsgGreaterThan(String value) {
            addCriterion("back_msg >", value, "backMsg");
            return (Criteria) this;
        }

        public Criteria andBackMsgGreaterThanOrEqualTo(String value) {
            addCriterion("back_msg >=", value, "backMsg");
            return (Criteria) this;
        }

        public Criteria andBackMsgLessThan(String value) {
            addCriterion("back_msg <", value, "backMsg");
            return (Criteria) this;
        }

        public Criteria andBackMsgLessThanOrEqualTo(String value) {
            addCriterion("back_msg <=", value, "backMsg");
            return (Criteria) this;
        }

        public Criteria andBackMsgLike(String value) {
            addCriterion("back_msg like", value, "backMsg");
            return (Criteria) this;
        }

        public Criteria andBackMsgNotLike(String value) {
            addCriterion("back_msg not like", value, "backMsg");
            return (Criteria) this;
        }

        public Criteria andBackMsgIn(List<String> values) {
            addCriterion("back_msg in", values, "backMsg");
            return (Criteria) this;
        }

        public Criteria andBackMsgNotIn(List<String> values) {
            addCriterion("back_msg not in", values, "backMsg");
            return (Criteria) this;
        }

        public Criteria andBackMsgBetween(String value1, String value2) {
            addCriterion("back_msg between", value1, value2, "backMsg");
            return (Criteria) this;
        }

        public Criteria andBackMsgNotBetween(String value1, String value2) {
            addCriterion("back_msg not between", value1, value2, "backMsg");
            return (Criteria) this;
        }

        public Criteria andSendIsdelIsNull() {
            addCriterion("send_isdel is null");
            return (Criteria) this;
        }

        public Criteria andSendIsdelIsNotNull() {
            addCriterion("send_isdel is not null");
            return (Criteria) this;
        }

        public Criteria andSendIsdelEqualTo(Integer value) {
            addCriterion("send_isdel =", value, "sendIsdel");
            return (Criteria) this;
        }

        public Criteria andSendIsdelNotEqualTo(Integer value) {
            addCriterion("send_isdel <>", value, "sendIsdel");
            return (Criteria) this;
        }

        public Criteria andSendIsdelGreaterThan(Integer value) {
            addCriterion("send_isdel >", value, "sendIsdel");
            return (Criteria) this;
        }

        public Criteria andSendIsdelGreaterThanOrEqualTo(Integer value) {
            addCriterion("send_isdel >=", value, "sendIsdel");
            return (Criteria) this;
        }

        public Criteria andSendIsdelLessThan(Integer value) {
            addCriterion("send_isdel <", value, "sendIsdel");
            return (Criteria) this;
        }

        public Criteria andSendIsdelLessThanOrEqualTo(Integer value) {
            addCriterion("send_isdel <=", value, "sendIsdel");
            return (Criteria) this;
        }

        public Criteria andSendIsdelIn(List<Integer> values) {
            addCriterion("send_isdel in", values, "sendIsdel");
            return (Criteria) this;
        }

        public Criteria andSendIsdelNotIn(List<Integer> values) {
            addCriterion("send_isdel not in", values, "sendIsdel");
            return (Criteria) this;
        }

        public Criteria andSendIsdelBetween(Integer value1, Integer value2) {
            addCriterion("send_isdel between", value1, value2, "sendIsdel");
            return (Criteria) this;
        }

        public Criteria andSendIsdelNotBetween(Integer value1, Integer value2) {
            addCriterion("send_isdel not between", value1, value2, "sendIsdel");
            return (Criteria) this;
        }

        public Criteria andIndexIdIsNull() {
            addCriterion("index_id is null");
            return (Criteria) this;
        }

        public Criteria andIndexIdIsNotNull() {
            addCriterion("index_id is not null");
            return (Criteria) this;
        }

        public Criteria andIndexIdEqualTo(Integer value) {
            addCriterion("index_id =", value, "indexId");
            return (Criteria) this;
        }

        public Criteria andIndexIdNotEqualTo(Integer value) {
            addCriterion("index_id <>", value, "indexId");
            return (Criteria) this;
        }

        public Criteria andIndexIdGreaterThan(Integer value) {
            addCriterion("index_id >", value, "indexId");
            return (Criteria) this;
        }

        public Criteria andIndexIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("index_id >=", value, "indexId");
            return (Criteria) this;
        }

        public Criteria andIndexIdLessThan(Integer value) {
            addCriterion("index_id <", value, "indexId");
            return (Criteria) this;
        }

        public Criteria andIndexIdLessThanOrEqualTo(Integer value) {
            addCriterion("index_id <=", value, "indexId");
            return (Criteria) this;
        }

        public Criteria andIndexIdIn(List<Integer> values) {
            addCriterion("index_id in", values, "indexId");
            return (Criteria) this;
        }

        public Criteria andIndexIdNotIn(List<Integer> values) {
            addCriterion("index_id not in", values, "indexId");
            return (Criteria) this;
        }

        public Criteria andIndexIdBetween(Integer value1, Integer value2) {
            addCriterion("index_id between", value1, value2, "indexId");
            return (Criteria) this;
        }

        public Criteria andIndexIdNotBetween(Integer value1, Integer value2) {
            addCriterion("index_id not between", value1, value2, "indexId");
            return (Criteria) this;
        }

        public Criteria andServiceidIsNull() {
            addCriterion("serviceID is null");
            return (Criteria) this;
        }

        public Criteria andServiceidIsNotNull() {
            addCriterion("serviceID is not null");
            return (Criteria) this;
        }

        public Criteria andServiceidEqualTo(String value) {
            addCriterion("serviceID =", value, "serviceid");
            return (Criteria) this;
        }

        public Criteria andServiceidNotEqualTo(String value) {
            addCriterion("serviceID <>", value, "serviceid");
            return (Criteria) this;
        }

        public Criteria andServiceidGreaterThan(String value) {
            addCriterion("serviceID >", value, "serviceid");
            return (Criteria) this;
        }

        public Criteria andServiceidGreaterThanOrEqualTo(String value) {
            addCriterion("serviceID >=", value, "serviceid");
            return (Criteria) this;
        }

        public Criteria andServiceidLessThan(String value) {
            addCriterion("serviceID <", value, "serviceid");
            return (Criteria) this;
        }

        public Criteria andServiceidLessThanOrEqualTo(String value) {
            addCriterion("serviceID <=", value, "serviceid");
            return (Criteria) this;
        }

        public Criteria andServiceidLike(String value) {
            addCriterion("serviceID like", value, "serviceid");
            return (Criteria) this;
        }

        public Criteria andServiceidNotLike(String value) {
            addCriterion("serviceID not like", value, "serviceid");
            return (Criteria) this;
        }

        public Criteria andServiceidIn(List<String> values) {
            addCriterion("serviceID in", values, "serviceid");
            return (Criteria) this;
        }

        public Criteria andServiceidNotIn(List<String> values) {
            addCriterion("serviceID not in", values, "serviceid");
            return (Criteria) this;
        }

        public Criteria andServiceidBetween(String value1, String value2) {
            addCriterion("serviceID between", value1, value2, "serviceid");
            return (Criteria) this;
        }

        public Criteria andServiceidNotBetween(String value1, String value2) {
            addCriterion("serviceID not between", value1, value2, "serviceid");
            return (Criteria) this;
        }

        public Criteria andSendTitleLikeInsensitive(String value) {
            addCriterion("upper(send_title) like", value.toUpperCase(), "sendTitle");
            return (Criteria) this;
        }

        public Criteria andSendPicLikeInsensitive(String value) {
            addCriterion("upper(send_pic) like", value.toUpperCase(), "sendPic");
            return (Criteria) this;
        }

        public Criteria andSendMusicLikeInsensitive(String value) {
            addCriterion("upper(send_music) like", value.toUpperCase(), "sendMusic");
            return (Criteria) this;
        }

        public Criteria andUserNameLikeInsensitive(String value) {
            addCriterion("upper(user_name) like", value.toUpperCase(), "userName");
            return (Criteria) this;
        }

        public Criteria andUserMobileLikeInsensitive(String value) {
            addCriterion("upper(user_mobile) like", value.toUpperCase(), "userMobile");
            return (Criteria) this;
        }

        public Criteria andSendMobileLikeInsensitive(String value) {
            addCriterion("upper(send_mobile) like", value.toUpperCase(), "sendMobile");
            return (Criteria) this;
        }

        public Criteria andSendCodeLikeInsensitive(String value) {
            addCriterion("upper(send_code) like", value.toUpperCase(), "sendCode");
            return (Criteria) this;
        }

        public Criteria andMsgidLikeInsensitive(String value) {
            addCriterion("upper(msgId) like", value.toUpperCase(), "msgid");
            return (Criteria) this;
        }

        public Criteria andLinkidLikeInsensitive(String value) {
            addCriterion("upper(linkId) like", value.toUpperCase(), "linkid");
            return (Criteria) this;
        }

        public Criteria andBackMsgLikeInsensitive(String value) {
            addCriterion("upper(back_msg) like", value.toUpperCase(), "backMsg");
            return (Criteria) this;
        }

        public Criteria andServiceidLikeInsensitive(String value) {
            addCriterion("upper(serviceID) like", value.toUpperCase(), "serviceid");
            return (Criteria) this;
        }
    }

    public static class Criteria extends GeneratedCriteria {

        protected Criteria() {
            super();
        }
    }

    public static class Criterion {
        private String condition;

        private Object value;

        private Object secondValue;

        private boolean noValue;

        private boolean singleValue;

        private boolean betweenValue;

        private boolean listValue;

        private String typeHandler;

        public String getCondition() {
            return condition;
        }

        public Object getValue() {
            return value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public boolean isNoValue() {
            return noValue;
        }

        public boolean isSingleValue() {
            return singleValue;
        }

        public boolean isBetweenValue() {
            return betweenValue;
        }

        public boolean isListValue() {
            return listValue;
        }

        public String getTypeHandler() {
            return typeHandler;
        }

        protected Criterion(String condition) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }

        protected Criterion(String condition, Object value, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if (value instanceof List<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        protected Criterion(String condition, Object value) {
            this(condition, value, null);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null);
        }
    }
}