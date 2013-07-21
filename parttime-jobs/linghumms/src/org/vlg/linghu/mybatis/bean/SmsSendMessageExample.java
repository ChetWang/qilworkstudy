package org.vlg.linghu.mybatis.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SmsSendMessageExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    private int pageIndex;

    private int pageSize;

    public SmsSendMessageExample() {
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

    public SmsSendMessageExample(int pageSize, int pageIndex) {
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

        public Criteria andUserIdIsNull() {
            addCriterion("user_id is null");
            return (Criteria) this;
        }

        public Criteria andUserIdIsNotNull() {
            addCriterion("user_id is not null");
            return (Criteria) this;
        }

        public Criteria andUserIdEqualTo(String value) {
            addCriterion("user_id =", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdNotEqualTo(String value) {
            addCriterion("user_id <>", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdGreaterThan(String value) {
            addCriterion("user_id >", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdGreaterThanOrEqualTo(String value) {
            addCriterion("user_id >=", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdLessThan(String value) {
            addCriterion("user_id <", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdLessThanOrEqualTo(String value) {
            addCriterion("user_id <=", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdLike(String value) {
            addCriterion("user_id like", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdNotLike(String value) {
            addCriterion("user_id not like", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdIn(List<String> values) {
            addCriterion("user_id in", values, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdNotIn(List<String> values) {
            addCriterion("user_id not in", values, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdBetween(String value1, String value2) {
            addCriterion("user_id between", value1, value2, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdNotBetween(String value1, String value2) {
            addCriterion("user_id not between", value1, value2, "userId");
            return (Criteria) this;
        }

        public Criteria andSendTextIsNull() {
            addCriterion("send_text is null");
            return (Criteria) this;
        }

        public Criteria andSendTextIsNotNull() {
            addCriterion("send_text is not null");
            return (Criteria) this;
        }

        public Criteria andSendTextEqualTo(String value) {
            addCriterion("send_text =", value, "sendText");
            return (Criteria) this;
        }

        public Criteria andSendTextNotEqualTo(String value) {
            addCriterion("send_text <>", value, "sendText");
            return (Criteria) this;
        }

        public Criteria andSendTextGreaterThan(String value) {
            addCriterion("send_text >", value, "sendText");
            return (Criteria) this;
        }

        public Criteria andSendTextGreaterThanOrEqualTo(String value) {
            addCriterion("send_text >=", value, "sendText");
            return (Criteria) this;
        }

        public Criteria andSendTextLessThan(String value) {
            addCriterion("send_text <", value, "sendText");
            return (Criteria) this;
        }

        public Criteria andSendTextLessThanOrEqualTo(String value) {
            addCriterion("send_text <=", value, "sendText");
            return (Criteria) this;
        }

        public Criteria andSendTextLike(String value) {
            addCriterion("send_text like", value, "sendText");
            return (Criteria) this;
        }

        public Criteria andSendTextNotLike(String value) {
            addCriterion("send_text not like", value, "sendText");
            return (Criteria) this;
        }

        public Criteria andSendTextIn(List<String> values) {
            addCriterion("send_text in", values, "sendText");
            return (Criteria) this;
        }

        public Criteria andSendTextNotIn(List<String> values) {
            addCriterion("send_text not in", values, "sendText");
            return (Criteria) this;
        }

        public Criteria andSendTextBetween(String value1, String value2) {
            addCriterion("send_text between", value1, value2, "sendText");
            return (Criteria) this;
        }

        public Criteria andSendTextNotBetween(String value1, String value2) {
            addCriterion("send_text not between", value1, value2, "sendText");
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

        public Criteria andIssendIsNull() {
            addCriterion("issend is null");
            return (Criteria) this;
        }

        public Criteria andIssendIsNotNull() {
            addCriterion("issend is not null");
            return (Criteria) this;
        }

        public Criteria andIssendEqualTo(Boolean value) {
            addCriterion("issend =", value, "issend");
            return (Criteria) this;
        }

        public Criteria andIssendNotEqualTo(Boolean value) {
            addCriterion("issend <>", value, "issend");
            return (Criteria) this;
        }

        public Criteria andIssendGreaterThan(Boolean value) {
            addCriterion("issend >", value, "issend");
            return (Criteria) this;
        }

        public Criteria andIssendGreaterThanOrEqualTo(Boolean value) {
            addCriterion("issend >=", value, "issend");
            return (Criteria) this;
        }

        public Criteria andIssendLessThan(Boolean value) {
            addCriterion("issend <", value, "issend");
            return (Criteria) this;
        }

        public Criteria andIssendLessThanOrEqualTo(Boolean value) {
            addCriterion("issend <=", value, "issend");
            return (Criteria) this;
        }

        public Criteria andIssendIn(List<Boolean> values) {
            addCriterion("issend in", values, "issend");
            return (Criteria) this;
        }

        public Criteria andIssendNotIn(List<Boolean> values) {
            addCriterion("issend not in", values, "issend");
            return (Criteria) this;
        }

        public Criteria andIssendBetween(Boolean value1, Boolean value2) {
            addCriterion("issend between", value1, value2, "issend");
            return (Criteria) this;
        }

        public Criteria andIssendNotBetween(Boolean value1, Boolean value2) {
            addCriterion("issend not between", value1, value2, "issend");
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

        public Criteria andUserIdLikeInsensitive(String value) {
            addCriterion("upper(user_id) like", value.toUpperCase(), "userId");
            return (Criteria) this;
        }

        public Criteria andSendTextLikeInsensitive(String value) {
            addCriterion("upper(send_text) like", value.toUpperCase(), "sendText");
            return (Criteria) this;
        }

        public Criteria andMsgidLikeInsensitive(String value) {
            addCriterion("upper(msgId) like", value.toUpperCase(), "msgid");
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