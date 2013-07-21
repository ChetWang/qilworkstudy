package org.vlg.linghu.mybatis.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SmsReceiveMessageExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    private int pageIndex;

    private int pageSize;

    public SmsReceiveMessageExample() {
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

    public SmsReceiveMessageExample(int pageSize, int pageIndex) {
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

        public Criteria andReceiveIdIsNull() {
            addCriterion("receive_id is null");
            return (Criteria) this;
        }

        public Criteria andReceiveIdIsNotNull() {
            addCriterion("receive_id is not null");
            return (Criteria) this;
        }

        public Criteria andReceiveIdEqualTo(Integer value) {
            addCriterion("receive_id =", value, "receiveId");
            return (Criteria) this;
        }

        public Criteria andReceiveIdNotEqualTo(Integer value) {
            addCriterion("receive_id <>", value, "receiveId");
            return (Criteria) this;
        }

        public Criteria andReceiveIdGreaterThan(Integer value) {
            addCriterion("receive_id >", value, "receiveId");
            return (Criteria) this;
        }

        public Criteria andReceiveIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("receive_id >=", value, "receiveId");
            return (Criteria) this;
        }

        public Criteria andReceiveIdLessThan(Integer value) {
            addCriterion("receive_id <", value, "receiveId");
            return (Criteria) this;
        }

        public Criteria andReceiveIdLessThanOrEqualTo(Integer value) {
            addCriterion("receive_id <=", value, "receiveId");
            return (Criteria) this;
        }

        public Criteria andReceiveIdIn(List<Integer> values) {
            addCriterion("receive_id in", values, "receiveId");
            return (Criteria) this;
        }

        public Criteria andReceiveIdNotIn(List<Integer> values) {
            addCriterion("receive_id not in", values, "receiveId");
            return (Criteria) this;
        }

        public Criteria andReceiveIdBetween(Integer value1, Integer value2) {
            addCriterion("receive_id between", value1, value2, "receiveId");
            return (Criteria) this;
        }

        public Criteria andReceiveIdNotBetween(Integer value1, Integer value2) {
            addCriterion("receive_id not between", value1, value2, "receiveId");
            return (Criteria) this;
        }

        public Criteria andReceiveAddtimeIsNull() {
            addCriterion("receive_addtime is null");
            return (Criteria) this;
        }

        public Criteria andReceiveAddtimeIsNotNull() {
            addCriterion("receive_addtime is not null");
            return (Criteria) this;
        }

        public Criteria andReceiveAddtimeEqualTo(Date value) {
            addCriterion("receive_addtime =", value, "receiveAddtime");
            return (Criteria) this;
        }

        public Criteria andReceiveAddtimeNotEqualTo(Date value) {
            addCriterion("receive_addtime <>", value, "receiveAddtime");
            return (Criteria) this;
        }

        public Criteria andReceiveAddtimeGreaterThan(Date value) {
            addCriterion("receive_addtime >", value, "receiveAddtime");
            return (Criteria) this;
        }

        public Criteria andReceiveAddtimeGreaterThanOrEqualTo(Date value) {
            addCriterion("receive_addtime >=", value, "receiveAddtime");
            return (Criteria) this;
        }

        public Criteria andReceiveAddtimeLessThan(Date value) {
            addCriterion("receive_addtime <", value, "receiveAddtime");
            return (Criteria) this;
        }

        public Criteria andReceiveAddtimeLessThanOrEqualTo(Date value) {
            addCriterion("receive_addtime <=", value, "receiveAddtime");
            return (Criteria) this;
        }

        public Criteria andReceiveAddtimeIn(List<Date> values) {
            addCriterion("receive_addtime in", values, "receiveAddtime");
            return (Criteria) this;
        }

        public Criteria andReceiveAddtimeNotIn(List<Date> values) {
            addCriterion("receive_addtime not in", values, "receiveAddtime");
            return (Criteria) this;
        }

        public Criteria andReceiveAddtimeBetween(Date value1, Date value2) {
            addCriterion("receive_addtime between", value1, value2, "receiveAddtime");
            return (Criteria) this;
        }

        public Criteria andReceiveAddtimeNotBetween(Date value1, Date value2) {
            addCriterion("receive_addtime not between", value1, value2, "receiveAddtime");
            return (Criteria) this;
        }

        public Criteria andReceiveTextIsNull() {
            addCriterion("receive_text is null");
            return (Criteria) this;
        }

        public Criteria andReceiveTextIsNotNull() {
            addCriterion("receive_text is not null");
            return (Criteria) this;
        }

        public Criteria andReceiveTextEqualTo(String value) {
            addCriterion("receive_text =", value, "receiveText");
            return (Criteria) this;
        }

        public Criteria andReceiveTextNotEqualTo(String value) {
            addCriterion("receive_text <>", value, "receiveText");
            return (Criteria) this;
        }

        public Criteria andReceiveTextGreaterThan(String value) {
            addCriterion("receive_text >", value, "receiveText");
            return (Criteria) this;
        }

        public Criteria andReceiveTextGreaterThanOrEqualTo(String value) {
            addCriterion("receive_text >=", value, "receiveText");
            return (Criteria) this;
        }

        public Criteria andReceiveTextLessThan(String value) {
            addCriterion("receive_text <", value, "receiveText");
            return (Criteria) this;
        }

        public Criteria andReceiveTextLessThanOrEqualTo(String value) {
            addCriterion("receive_text <=", value, "receiveText");
            return (Criteria) this;
        }

        public Criteria andReceiveTextLike(String value) {
            addCriterion("receive_text like", value, "receiveText");
            return (Criteria) this;
        }

        public Criteria andReceiveTextNotLike(String value) {
            addCriterion("receive_text not like", value, "receiveText");
            return (Criteria) this;
        }

        public Criteria andReceiveTextIn(List<String> values) {
            addCriterion("receive_text in", values, "receiveText");
            return (Criteria) this;
        }

        public Criteria andReceiveTextNotIn(List<String> values) {
            addCriterion("receive_text not in", values, "receiveText");
            return (Criteria) this;
        }

        public Criteria andReceiveTextBetween(String value1, String value2) {
            addCriterion("receive_text between", value1, value2, "receiveText");
            return (Criteria) this;
        }

        public Criteria andReceiveTextNotBetween(String value1, String value2) {
            addCriterion("receive_text not between", value1, value2, "receiveText");
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

        public Criteria andIsokIsNull() {
            addCriterion("isOK is null");
            return (Criteria) this;
        }

        public Criteria andIsokIsNotNull() {
            addCriterion("isOK is not null");
            return (Criteria) this;
        }

        public Criteria andIsokEqualTo(Boolean value) {
            addCriterion("isOK =", value, "isok");
            return (Criteria) this;
        }

        public Criteria andIsokNotEqualTo(Boolean value) {
            addCriterion("isOK <>", value, "isok");
            return (Criteria) this;
        }

        public Criteria andIsokGreaterThan(Boolean value) {
            addCriterion("isOK >", value, "isok");
            return (Criteria) this;
        }

        public Criteria andIsokGreaterThanOrEqualTo(Boolean value) {
            addCriterion("isOK >=", value, "isok");
            return (Criteria) this;
        }

        public Criteria andIsokLessThan(Boolean value) {
            addCriterion("isOK <", value, "isok");
            return (Criteria) this;
        }

        public Criteria andIsokLessThanOrEqualTo(Boolean value) {
            addCriterion("isOK <=", value, "isok");
            return (Criteria) this;
        }

        public Criteria andIsokIn(List<Boolean> values) {
            addCriterion("isOK in", values, "isok");
            return (Criteria) this;
        }

        public Criteria andIsokNotIn(List<Boolean> values) {
            addCriterion("isOK not in", values, "isok");
            return (Criteria) this;
        }

        public Criteria andIsokBetween(Boolean value1, Boolean value2) {
            addCriterion("isOK between", value1, value2, "isok");
            return (Criteria) this;
        }

        public Criteria andIsokNotBetween(Boolean value1, Boolean value2) {
            addCriterion("isOK not between", value1, value2, "isok");
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

        public Criteria andReceiveTextLikeInsensitive(String value) {
            addCriterion("upper(receive_text) like", value.toUpperCase(), "receiveText");
            return (Criteria) this;
        }

        public Criteria andUserIdLikeInsensitive(String value) {
            addCriterion("upper(user_id) like", value.toUpperCase(), "userId");
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