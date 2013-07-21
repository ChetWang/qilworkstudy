package org.vlg.linghu.mybatis.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class VacReceiveMessageExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    private int pageIndex;

    private int pageSize;

    public VacReceiveMessageExample() {
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

    public VacReceiveMessageExample(int pageSize, int pageIndex) {
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

        public Criteria andVacIdIsNull() {
            addCriterion("vac_id is null");
            return (Criteria) this;
        }

        public Criteria andVacIdIsNotNull() {
            addCriterion("vac_id is not null");
            return (Criteria) this;
        }

        public Criteria andVacIdEqualTo(Integer value) {
            addCriterion("vac_id =", value, "vacId");
            return (Criteria) this;
        }

        public Criteria andVacIdNotEqualTo(Integer value) {
            addCriterion("vac_id <>", value, "vacId");
            return (Criteria) this;
        }

        public Criteria andVacIdGreaterThan(Integer value) {
            addCriterion("vac_id >", value, "vacId");
            return (Criteria) this;
        }

        public Criteria andVacIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("vac_id >=", value, "vacId");
            return (Criteria) this;
        }

        public Criteria andVacIdLessThan(Integer value) {
            addCriterion("vac_id <", value, "vacId");
            return (Criteria) this;
        }

        public Criteria andVacIdLessThanOrEqualTo(Integer value) {
            addCriterion("vac_id <=", value, "vacId");
            return (Criteria) this;
        }

        public Criteria andVacIdIn(List<Integer> values) {
            addCriterion("vac_id in", values, "vacId");
            return (Criteria) this;
        }

        public Criteria andVacIdNotIn(List<Integer> values) {
            addCriterion("vac_id not in", values, "vacId");
            return (Criteria) this;
        }

        public Criteria andVacIdBetween(Integer value1, Integer value2) {
            addCriterion("vac_id between", value1, value2, "vacId");
            return (Criteria) this;
        }

        public Criteria andVacIdNotBetween(Integer value1, Integer value2) {
            addCriterion("vac_id not between", value1, value2, "vacId");
            return (Criteria) this;
        }

        public Criteria andVacAddtimeIsNull() {
            addCriterion("vac_addtime is null");
            return (Criteria) this;
        }

        public Criteria andVacAddtimeIsNotNull() {
            addCriterion("vac_addtime is not null");
            return (Criteria) this;
        }

        public Criteria andVacAddtimeEqualTo(Date value) {
            addCriterion("vac_addtime =", value, "vacAddtime");
            return (Criteria) this;
        }

        public Criteria andVacAddtimeNotEqualTo(Date value) {
            addCriterion("vac_addtime <>", value, "vacAddtime");
            return (Criteria) this;
        }

        public Criteria andVacAddtimeGreaterThan(Date value) {
            addCriterion("vac_addtime >", value, "vacAddtime");
            return (Criteria) this;
        }

        public Criteria andVacAddtimeGreaterThanOrEqualTo(Date value) {
            addCriterion("vac_addtime >=", value, "vacAddtime");
            return (Criteria) this;
        }

        public Criteria andVacAddtimeLessThan(Date value) {
            addCriterion("vac_addtime <", value, "vacAddtime");
            return (Criteria) this;
        }

        public Criteria andVacAddtimeLessThanOrEqualTo(Date value) {
            addCriterion("vac_addtime <=", value, "vacAddtime");
            return (Criteria) this;
        }

        public Criteria andVacAddtimeIn(List<Date> values) {
            addCriterion("vac_addtime in", values, "vacAddtime");
            return (Criteria) this;
        }

        public Criteria andVacAddtimeNotIn(List<Date> values) {
            addCriterion("vac_addtime not in", values, "vacAddtime");
            return (Criteria) this;
        }

        public Criteria andVacAddtimeBetween(Date value1, Date value2) {
            addCriterion("vac_addtime between", value1, value2, "vacAddtime");
            return (Criteria) this;
        }

        public Criteria andVacAddtimeNotBetween(Date value1, Date value2) {
            addCriterion("vac_addtime not between", value1, value2, "vacAddtime");
            return (Criteria) this;
        }

        public Criteria andRecordsequenceidIsNull() {
            addCriterion("RecordSequenceID is null");
            return (Criteria) this;
        }

        public Criteria andRecordsequenceidIsNotNull() {
            addCriterion("RecordSequenceID is not null");
            return (Criteria) this;
        }

        public Criteria andRecordsequenceidEqualTo(String value) {
            addCriterion("RecordSequenceID =", value, "recordsequenceid");
            return (Criteria) this;
        }

        public Criteria andRecordsequenceidNotEqualTo(String value) {
            addCriterion("RecordSequenceID <>", value, "recordsequenceid");
            return (Criteria) this;
        }

        public Criteria andRecordsequenceidGreaterThan(String value) {
            addCriterion("RecordSequenceID >", value, "recordsequenceid");
            return (Criteria) this;
        }

        public Criteria andRecordsequenceidGreaterThanOrEqualTo(String value) {
            addCriterion("RecordSequenceID >=", value, "recordsequenceid");
            return (Criteria) this;
        }

        public Criteria andRecordsequenceidLessThan(String value) {
            addCriterion("RecordSequenceID <", value, "recordsequenceid");
            return (Criteria) this;
        }

        public Criteria andRecordsequenceidLessThanOrEqualTo(String value) {
            addCriterion("RecordSequenceID <=", value, "recordsequenceid");
            return (Criteria) this;
        }

        public Criteria andRecordsequenceidLike(String value) {
            addCriterion("RecordSequenceID like", value, "recordsequenceid");
            return (Criteria) this;
        }

        public Criteria andRecordsequenceidNotLike(String value) {
            addCriterion("RecordSequenceID not like", value, "recordsequenceid");
            return (Criteria) this;
        }

        public Criteria andRecordsequenceidIn(List<String> values) {
            addCriterion("RecordSequenceID in", values, "recordsequenceid");
            return (Criteria) this;
        }

        public Criteria andRecordsequenceidNotIn(List<String> values) {
            addCriterion("RecordSequenceID not in", values, "recordsequenceid");
            return (Criteria) this;
        }

        public Criteria andRecordsequenceidBetween(String value1, String value2) {
            addCriterion("RecordSequenceID between", value1, value2, "recordsequenceid");
            return (Criteria) this;
        }

        public Criteria andRecordsequenceidNotBetween(String value1, String value2) {
            addCriterion("RecordSequenceID not between", value1, value2, "recordsequenceid");
            return (Criteria) this;
        }

        public Criteria andUseridtypeIsNull() {
            addCriterion("UserIdType is null");
            return (Criteria) this;
        }

        public Criteria andUseridtypeIsNotNull() {
            addCriterion("UserIdType is not null");
            return (Criteria) this;
        }

        public Criteria andUseridtypeEqualTo(Integer value) {
            addCriterion("UserIdType =", value, "useridtype");
            return (Criteria) this;
        }

        public Criteria andUseridtypeNotEqualTo(Integer value) {
            addCriterion("UserIdType <>", value, "useridtype");
            return (Criteria) this;
        }

        public Criteria andUseridtypeGreaterThan(Integer value) {
            addCriterion("UserIdType >", value, "useridtype");
            return (Criteria) this;
        }

        public Criteria andUseridtypeGreaterThanOrEqualTo(Integer value) {
            addCriterion("UserIdType >=", value, "useridtype");
            return (Criteria) this;
        }

        public Criteria andUseridtypeLessThan(Integer value) {
            addCriterion("UserIdType <", value, "useridtype");
            return (Criteria) this;
        }

        public Criteria andUseridtypeLessThanOrEqualTo(Integer value) {
            addCriterion("UserIdType <=", value, "useridtype");
            return (Criteria) this;
        }

        public Criteria andUseridtypeIn(List<Integer> values) {
            addCriterion("UserIdType in", values, "useridtype");
            return (Criteria) this;
        }

        public Criteria andUseridtypeNotIn(List<Integer> values) {
            addCriterion("UserIdType not in", values, "useridtype");
            return (Criteria) this;
        }

        public Criteria andUseridtypeBetween(Integer value1, Integer value2) {
            addCriterion("UserIdType between", value1, value2, "useridtype");
            return (Criteria) this;
        }

        public Criteria andUseridtypeNotBetween(Integer value1, Integer value2) {
            addCriterion("UserIdType not between", value1, value2, "useridtype");
            return (Criteria) this;
        }

        public Criteria andUseridIsNull() {
            addCriterion("UserId is null");
            return (Criteria) this;
        }

        public Criteria andUseridIsNotNull() {
            addCriterion("UserId is not null");
            return (Criteria) this;
        }

        public Criteria andUseridEqualTo(String value) {
            addCriterion("UserId =", value, "userid");
            return (Criteria) this;
        }

        public Criteria andUseridNotEqualTo(String value) {
            addCriterion("UserId <>", value, "userid");
            return (Criteria) this;
        }

        public Criteria andUseridGreaterThan(String value) {
            addCriterion("UserId >", value, "userid");
            return (Criteria) this;
        }

        public Criteria andUseridGreaterThanOrEqualTo(String value) {
            addCriterion("UserId >=", value, "userid");
            return (Criteria) this;
        }

        public Criteria andUseridLessThan(String value) {
            addCriterion("UserId <", value, "userid");
            return (Criteria) this;
        }

        public Criteria andUseridLessThanOrEqualTo(String value) {
            addCriterion("UserId <=", value, "userid");
            return (Criteria) this;
        }

        public Criteria andUseridLike(String value) {
            addCriterion("UserId like", value, "userid");
            return (Criteria) this;
        }

        public Criteria andUseridNotLike(String value) {
            addCriterion("UserId not like", value, "userid");
            return (Criteria) this;
        }

        public Criteria andUseridIn(List<String> values) {
            addCriterion("UserId in", values, "userid");
            return (Criteria) this;
        }

        public Criteria andUseridNotIn(List<String> values) {
            addCriterion("UserId not in", values, "userid");
            return (Criteria) this;
        }

        public Criteria andUseridBetween(String value1, String value2) {
            addCriterion("UserId between", value1, value2, "userid");
            return (Criteria) this;
        }

        public Criteria andUseridNotBetween(String value1, String value2) {
            addCriterion("UserId not between", value1, value2, "userid");
            return (Criteria) this;
        }

        public Criteria andServicetypeIsNull() {
            addCriterion("ServiceType is null");
            return (Criteria) this;
        }

        public Criteria andServicetypeIsNotNull() {
            addCriterion("ServiceType is not null");
            return (Criteria) this;
        }

        public Criteria andServicetypeEqualTo(String value) {
            addCriterion("ServiceType =", value, "servicetype");
            return (Criteria) this;
        }

        public Criteria andServicetypeNotEqualTo(String value) {
            addCriterion("ServiceType <>", value, "servicetype");
            return (Criteria) this;
        }

        public Criteria andServicetypeGreaterThan(String value) {
            addCriterion("ServiceType >", value, "servicetype");
            return (Criteria) this;
        }

        public Criteria andServicetypeGreaterThanOrEqualTo(String value) {
            addCriterion("ServiceType >=", value, "servicetype");
            return (Criteria) this;
        }

        public Criteria andServicetypeLessThan(String value) {
            addCriterion("ServiceType <", value, "servicetype");
            return (Criteria) this;
        }

        public Criteria andServicetypeLessThanOrEqualTo(String value) {
            addCriterion("ServiceType <=", value, "servicetype");
            return (Criteria) this;
        }

        public Criteria andServicetypeLike(String value) {
            addCriterion("ServiceType like", value, "servicetype");
            return (Criteria) this;
        }

        public Criteria andServicetypeNotLike(String value) {
            addCriterion("ServiceType not like", value, "servicetype");
            return (Criteria) this;
        }

        public Criteria andServicetypeIn(List<String> values) {
            addCriterion("ServiceType in", values, "servicetype");
            return (Criteria) this;
        }

        public Criteria andServicetypeNotIn(List<String> values) {
            addCriterion("ServiceType not in", values, "servicetype");
            return (Criteria) this;
        }

        public Criteria andServicetypeBetween(String value1, String value2) {
            addCriterion("ServiceType between", value1, value2, "servicetype");
            return (Criteria) this;
        }

        public Criteria andServicetypeNotBetween(String value1, String value2) {
            addCriterion("ServiceType not between", value1, value2, "servicetype");
            return (Criteria) this;
        }

        public Criteria andSpidIsNull() {
            addCriterion("SpId is null");
            return (Criteria) this;
        }

        public Criteria andSpidIsNotNull() {
            addCriterion("SpId is not null");
            return (Criteria) this;
        }

        public Criteria andSpidEqualTo(String value) {
            addCriterion("SpId =", value, "spid");
            return (Criteria) this;
        }

        public Criteria andSpidNotEqualTo(String value) {
            addCriterion("SpId <>", value, "spid");
            return (Criteria) this;
        }

        public Criteria andSpidGreaterThan(String value) {
            addCriterion("SpId >", value, "spid");
            return (Criteria) this;
        }

        public Criteria andSpidGreaterThanOrEqualTo(String value) {
            addCriterion("SpId >=", value, "spid");
            return (Criteria) this;
        }

        public Criteria andSpidLessThan(String value) {
            addCriterion("SpId <", value, "spid");
            return (Criteria) this;
        }

        public Criteria andSpidLessThanOrEqualTo(String value) {
            addCriterion("SpId <=", value, "spid");
            return (Criteria) this;
        }

        public Criteria andSpidLike(String value) {
            addCriterion("SpId like", value, "spid");
            return (Criteria) this;
        }

        public Criteria andSpidNotLike(String value) {
            addCriterion("SpId not like", value, "spid");
            return (Criteria) this;
        }

        public Criteria andSpidIn(List<String> values) {
            addCriterion("SpId in", values, "spid");
            return (Criteria) this;
        }

        public Criteria andSpidNotIn(List<String> values) {
            addCriterion("SpId not in", values, "spid");
            return (Criteria) this;
        }

        public Criteria andSpidBetween(String value1, String value2) {
            addCriterion("SpId between", value1, value2, "spid");
            return (Criteria) this;
        }

        public Criteria andSpidNotBetween(String value1, String value2) {
            addCriterion("SpId not between", value1, value2, "spid");
            return (Criteria) this;
        }

        public Criteria andProductidIsNull() {
            addCriterion("ProductId is null");
            return (Criteria) this;
        }

        public Criteria andProductidIsNotNull() {
            addCriterion("ProductId is not null");
            return (Criteria) this;
        }

        public Criteria andProductidEqualTo(String value) {
            addCriterion("ProductId =", value, "productid");
            return (Criteria) this;
        }

        public Criteria andProductidNotEqualTo(String value) {
            addCriterion("ProductId <>", value, "productid");
            return (Criteria) this;
        }

        public Criteria andProductidGreaterThan(String value) {
            addCriterion("ProductId >", value, "productid");
            return (Criteria) this;
        }

        public Criteria andProductidGreaterThanOrEqualTo(String value) {
            addCriterion("ProductId >=", value, "productid");
            return (Criteria) this;
        }

        public Criteria andProductidLessThan(String value) {
            addCriterion("ProductId <", value, "productid");
            return (Criteria) this;
        }

        public Criteria andProductidLessThanOrEqualTo(String value) {
            addCriterion("ProductId <=", value, "productid");
            return (Criteria) this;
        }

        public Criteria andProductidLike(String value) {
            addCriterion("ProductId like", value, "productid");
            return (Criteria) this;
        }

        public Criteria andProductidNotLike(String value) {
            addCriterion("ProductId not like", value, "productid");
            return (Criteria) this;
        }

        public Criteria andProductidIn(List<String> values) {
            addCriterion("ProductId in", values, "productid");
            return (Criteria) this;
        }

        public Criteria andProductidNotIn(List<String> values) {
            addCriterion("ProductId not in", values, "productid");
            return (Criteria) this;
        }

        public Criteria andProductidBetween(String value1, String value2) {
            addCriterion("ProductId between", value1, value2, "productid");
            return (Criteria) this;
        }

        public Criteria andProductidNotBetween(String value1, String value2) {
            addCriterion("ProductId not between", value1, value2, "productid");
            return (Criteria) this;
        }

        public Criteria andUpdatetypeIsNull() {
            addCriterion("UpdateType is null");
            return (Criteria) this;
        }

        public Criteria andUpdatetypeIsNotNull() {
            addCriterion("UpdateType is not null");
            return (Criteria) this;
        }

        public Criteria andUpdatetypeEqualTo(Integer value) {
            addCriterion("UpdateType =", value, "updatetype");
            return (Criteria) this;
        }

        public Criteria andUpdatetypeNotEqualTo(Integer value) {
            addCriterion("UpdateType <>", value, "updatetype");
            return (Criteria) this;
        }

        public Criteria andUpdatetypeGreaterThan(Integer value) {
            addCriterion("UpdateType >", value, "updatetype");
            return (Criteria) this;
        }

        public Criteria andUpdatetypeGreaterThanOrEqualTo(Integer value) {
            addCriterion("UpdateType >=", value, "updatetype");
            return (Criteria) this;
        }

        public Criteria andUpdatetypeLessThan(Integer value) {
            addCriterion("UpdateType <", value, "updatetype");
            return (Criteria) this;
        }

        public Criteria andUpdatetypeLessThanOrEqualTo(Integer value) {
            addCriterion("UpdateType <=", value, "updatetype");
            return (Criteria) this;
        }

        public Criteria andUpdatetypeIn(List<Integer> values) {
            addCriterion("UpdateType in", values, "updatetype");
            return (Criteria) this;
        }

        public Criteria andUpdatetypeNotIn(List<Integer> values) {
            addCriterion("UpdateType not in", values, "updatetype");
            return (Criteria) this;
        }

        public Criteria andUpdatetypeBetween(Integer value1, Integer value2) {
            addCriterion("UpdateType between", value1, value2, "updatetype");
            return (Criteria) this;
        }

        public Criteria andUpdatetypeNotBetween(Integer value1, Integer value2) {
            addCriterion("UpdateType not between", value1, value2, "updatetype");
            return (Criteria) this;
        }

        public Criteria andUpdatetimeIsNull() {
            addCriterion("UpdateTime is null");
            return (Criteria) this;
        }

        public Criteria andUpdatetimeIsNotNull() {
            addCriterion("UpdateTime is not null");
            return (Criteria) this;
        }

        public Criteria andUpdatetimeEqualTo(String value) {
            addCriterion("UpdateTime =", value, "updatetime");
            return (Criteria) this;
        }

        public Criteria andUpdatetimeNotEqualTo(String value) {
            addCriterion("UpdateTime <>", value, "updatetime");
            return (Criteria) this;
        }

        public Criteria andUpdatetimeGreaterThan(String value) {
            addCriterion("UpdateTime >", value, "updatetime");
            return (Criteria) this;
        }

        public Criteria andUpdatetimeGreaterThanOrEqualTo(String value) {
            addCriterion("UpdateTime >=", value, "updatetime");
            return (Criteria) this;
        }

        public Criteria andUpdatetimeLessThan(String value) {
            addCriterion("UpdateTime <", value, "updatetime");
            return (Criteria) this;
        }

        public Criteria andUpdatetimeLessThanOrEqualTo(String value) {
            addCriterion("UpdateTime <=", value, "updatetime");
            return (Criteria) this;
        }

        public Criteria andUpdatetimeLike(String value) {
            addCriterion("UpdateTime like", value, "updatetime");
            return (Criteria) this;
        }

        public Criteria andUpdatetimeNotLike(String value) {
            addCriterion("UpdateTime not like", value, "updatetime");
            return (Criteria) this;
        }

        public Criteria andUpdatetimeIn(List<String> values) {
            addCriterion("UpdateTime in", values, "updatetime");
            return (Criteria) this;
        }

        public Criteria andUpdatetimeNotIn(List<String> values) {
            addCriterion("UpdateTime not in", values, "updatetime");
            return (Criteria) this;
        }

        public Criteria andUpdatetimeBetween(String value1, String value2) {
            addCriterion("UpdateTime between", value1, value2, "updatetime");
            return (Criteria) this;
        }

        public Criteria andUpdatetimeNotBetween(String value1, String value2) {
            addCriterion("UpdateTime not between", value1, value2, "updatetime");
            return (Criteria) this;
        }

        public Criteria andUpdatedescIsNull() {
            addCriterion("UpdateDesc is null");
            return (Criteria) this;
        }

        public Criteria andUpdatedescIsNotNull() {
            addCriterion("UpdateDesc is not null");
            return (Criteria) this;
        }

        public Criteria andUpdatedescEqualTo(String value) {
            addCriterion("UpdateDesc =", value, "updatedesc");
            return (Criteria) this;
        }

        public Criteria andUpdatedescNotEqualTo(String value) {
            addCriterion("UpdateDesc <>", value, "updatedesc");
            return (Criteria) this;
        }

        public Criteria andUpdatedescGreaterThan(String value) {
            addCriterion("UpdateDesc >", value, "updatedesc");
            return (Criteria) this;
        }

        public Criteria andUpdatedescGreaterThanOrEqualTo(String value) {
            addCriterion("UpdateDesc >=", value, "updatedesc");
            return (Criteria) this;
        }

        public Criteria andUpdatedescLessThan(String value) {
            addCriterion("UpdateDesc <", value, "updatedesc");
            return (Criteria) this;
        }

        public Criteria andUpdatedescLessThanOrEqualTo(String value) {
            addCriterion("UpdateDesc <=", value, "updatedesc");
            return (Criteria) this;
        }

        public Criteria andUpdatedescLike(String value) {
            addCriterion("UpdateDesc like", value, "updatedesc");
            return (Criteria) this;
        }

        public Criteria andUpdatedescNotLike(String value) {
            addCriterion("UpdateDesc not like", value, "updatedesc");
            return (Criteria) this;
        }

        public Criteria andUpdatedescIn(List<String> values) {
            addCriterion("UpdateDesc in", values, "updatedesc");
            return (Criteria) this;
        }

        public Criteria andUpdatedescNotIn(List<String> values) {
            addCriterion("UpdateDesc not in", values, "updatedesc");
            return (Criteria) this;
        }

        public Criteria andUpdatedescBetween(String value1, String value2) {
            addCriterion("UpdateDesc between", value1, value2, "updatedesc");
            return (Criteria) this;
        }

        public Criteria andUpdatedescNotBetween(String value1, String value2) {
            addCriterion("UpdateDesc not between", value1, value2, "updatedesc");
            return (Criteria) this;
        }

        public Criteria andLinkidIsNull() {
            addCriterion("LinkID is null");
            return (Criteria) this;
        }

        public Criteria andLinkidIsNotNull() {
            addCriterion("LinkID is not null");
            return (Criteria) this;
        }

        public Criteria andLinkidEqualTo(String value) {
            addCriterion("LinkID =", value, "linkid");
            return (Criteria) this;
        }

        public Criteria andLinkidNotEqualTo(String value) {
            addCriterion("LinkID <>", value, "linkid");
            return (Criteria) this;
        }

        public Criteria andLinkidGreaterThan(String value) {
            addCriterion("LinkID >", value, "linkid");
            return (Criteria) this;
        }

        public Criteria andLinkidGreaterThanOrEqualTo(String value) {
            addCriterion("LinkID >=", value, "linkid");
            return (Criteria) this;
        }

        public Criteria andLinkidLessThan(String value) {
            addCriterion("LinkID <", value, "linkid");
            return (Criteria) this;
        }

        public Criteria andLinkidLessThanOrEqualTo(String value) {
            addCriterion("LinkID <=", value, "linkid");
            return (Criteria) this;
        }

        public Criteria andLinkidLike(String value) {
            addCriterion("LinkID like", value, "linkid");
            return (Criteria) this;
        }

        public Criteria andLinkidNotLike(String value) {
            addCriterion("LinkID not like", value, "linkid");
            return (Criteria) this;
        }

        public Criteria andLinkidIn(List<String> values) {
            addCriterion("LinkID in", values, "linkid");
            return (Criteria) this;
        }

        public Criteria andLinkidNotIn(List<String> values) {
            addCriterion("LinkID not in", values, "linkid");
            return (Criteria) this;
        }

        public Criteria andLinkidBetween(String value1, String value2) {
            addCriterion("LinkID between", value1, value2, "linkid");
            return (Criteria) this;
        }

        public Criteria andLinkidNotBetween(String value1, String value2) {
            addCriterion("LinkID not between", value1, value2, "linkid");
            return (Criteria) this;
        }

        public Criteria andContentIsNull() {
            addCriterion("Content is null");
            return (Criteria) this;
        }

        public Criteria andContentIsNotNull() {
            addCriterion("Content is not null");
            return (Criteria) this;
        }

        public Criteria andContentEqualTo(String value) {
            addCriterion("Content =", value, "content");
            return (Criteria) this;
        }

        public Criteria andContentNotEqualTo(String value) {
            addCriterion("Content <>", value, "content");
            return (Criteria) this;
        }

        public Criteria andContentGreaterThan(String value) {
            addCriterion("Content >", value, "content");
            return (Criteria) this;
        }

        public Criteria andContentGreaterThanOrEqualTo(String value) {
            addCriterion("Content >=", value, "content");
            return (Criteria) this;
        }

        public Criteria andContentLessThan(String value) {
            addCriterion("Content <", value, "content");
            return (Criteria) this;
        }

        public Criteria andContentLessThanOrEqualTo(String value) {
            addCriterion("Content <=", value, "content");
            return (Criteria) this;
        }

        public Criteria andContentLike(String value) {
            addCriterion("Content like", value, "content");
            return (Criteria) this;
        }

        public Criteria andContentNotLike(String value) {
            addCriterion("Content not like", value, "content");
            return (Criteria) this;
        }

        public Criteria andContentIn(List<String> values) {
            addCriterion("Content in", values, "content");
            return (Criteria) this;
        }

        public Criteria andContentNotIn(List<String> values) {
            addCriterion("Content not in", values, "content");
            return (Criteria) this;
        }

        public Criteria andContentBetween(String value1, String value2) {
            addCriterion("Content between", value1, value2, "content");
            return (Criteria) this;
        }

        public Criteria andContentNotBetween(String value1, String value2) {
            addCriterion("Content not between", value1, value2, "content");
            return (Criteria) this;
        }

        public Criteria andEffectivedateIsNull() {
            addCriterion("EffectiveDate is null");
            return (Criteria) this;
        }

        public Criteria andEffectivedateIsNotNull() {
            addCriterion("EffectiveDate is not null");
            return (Criteria) this;
        }

        public Criteria andEffectivedateEqualTo(String value) {
            addCriterion("EffectiveDate =", value, "effectivedate");
            return (Criteria) this;
        }

        public Criteria andEffectivedateNotEqualTo(String value) {
            addCriterion("EffectiveDate <>", value, "effectivedate");
            return (Criteria) this;
        }

        public Criteria andEffectivedateGreaterThan(String value) {
            addCriterion("EffectiveDate >", value, "effectivedate");
            return (Criteria) this;
        }

        public Criteria andEffectivedateGreaterThanOrEqualTo(String value) {
            addCriterion("EffectiveDate >=", value, "effectivedate");
            return (Criteria) this;
        }

        public Criteria andEffectivedateLessThan(String value) {
            addCriterion("EffectiveDate <", value, "effectivedate");
            return (Criteria) this;
        }

        public Criteria andEffectivedateLessThanOrEqualTo(String value) {
            addCriterion("EffectiveDate <=", value, "effectivedate");
            return (Criteria) this;
        }

        public Criteria andEffectivedateLike(String value) {
            addCriterion("EffectiveDate like", value, "effectivedate");
            return (Criteria) this;
        }

        public Criteria andEffectivedateNotLike(String value) {
            addCriterion("EffectiveDate not like", value, "effectivedate");
            return (Criteria) this;
        }

        public Criteria andEffectivedateIn(List<String> values) {
            addCriterion("EffectiveDate in", values, "effectivedate");
            return (Criteria) this;
        }

        public Criteria andEffectivedateNotIn(List<String> values) {
            addCriterion("EffectiveDate not in", values, "effectivedate");
            return (Criteria) this;
        }

        public Criteria andEffectivedateBetween(String value1, String value2) {
            addCriterion("EffectiveDate between", value1, value2, "effectivedate");
            return (Criteria) this;
        }

        public Criteria andEffectivedateNotBetween(String value1, String value2) {
            addCriterion("EffectiveDate not between", value1, value2, "effectivedate");
            return (Criteria) this;
        }

        public Criteria andExpiredateIsNull() {
            addCriterion("ExpireDate is null");
            return (Criteria) this;
        }

        public Criteria andExpiredateIsNotNull() {
            addCriterion("ExpireDate is not null");
            return (Criteria) this;
        }

        public Criteria andExpiredateEqualTo(String value) {
            addCriterion("ExpireDate =", value, "expiredate");
            return (Criteria) this;
        }

        public Criteria andExpiredateNotEqualTo(String value) {
            addCriterion("ExpireDate <>", value, "expiredate");
            return (Criteria) this;
        }

        public Criteria andExpiredateGreaterThan(String value) {
            addCriterion("ExpireDate >", value, "expiredate");
            return (Criteria) this;
        }

        public Criteria andExpiredateGreaterThanOrEqualTo(String value) {
            addCriterion("ExpireDate >=", value, "expiredate");
            return (Criteria) this;
        }

        public Criteria andExpiredateLessThan(String value) {
            addCriterion("ExpireDate <", value, "expiredate");
            return (Criteria) this;
        }

        public Criteria andExpiredateLessThanOrEqualTo(String value) {
            addCriterion("ExpireDate <=", value, "expiredate");
            return (Criteria) this;
        }

        public Criteria andExpiredateLike(String value) {
            addCriterion("ExpireDate like", value, "expiredate");
            return (Criteria) this;
        }

        public Criteria andExpiredateNotLike(String value) {
            addCriterion("ExpireDate not like", value, "expiredate");
            return (Criteria) this;
        }

        public Criteria andExpiredateIn(List<String> values) {
            addCriterion("ExpireDate in", values, "expiredate");
            return (Criteria) this;
        }

        public Criteria andExpiredateNotIn(List<String> values) {
            addCriterion("ExpireDate not in", values, "expiredate");
            return (Criteria) this;
        }

        public Criteria andExpiredateBetween(String value1, String value2) {
            addCriterion("ExpireDate between", value1, value2, "expiredate");
            return (Criteria) this;
        }

        public Criteria andExpiredateNotBetween(String value1, String value2) {
            addCriterion("ExpireDate not between", value1, value2, "expiredate");
            return (Criteria) this;
        }

        public Criteria andResultcodeIsNull() {
            addCriterion("ResultCode is null");
            return (Criteria) this;
        }

        public Criteria andResultcodeIsNotNull() {
            addCriterion("ResultCode is not null");
            return (Criteria) this;
        }

        public Criteria andResultcodeEqualTo(Integer value) {
            addCriterion("ResultCode =", value, "resultcode");
            return (Criteria) this;
        }

        public Criteria andResultcodeNotEqualTo(Integer value) {
            addCriterion("ResultCode <>", value, "resultcode");
            return (Criteria) this;
        }

        public Criteria andResultcodeGreaterThan(Integer value) {
            addCriterion("ResultCode >", value, "resultcode");
            return (Criteria) this;
        }

        public Criteria andResultcodeGreaterThanOrEqualTo(Integer value) {
            addCriterion("ResultCode >=", value, "resultcode");
            return (Criteria) this;
        }

        public Criteria andResultcodeLessThan(Integer value) {
            addCriterion("ResultCode <", value, "resultcode");
            return (Criteria) this;
        }

        public Criteria andResultcodeLessThanOrEqualTo(Integer value) {
            addCriterion("ResultCode <=", value, "resultcode");
            return (Criteria) this;
        }

        public Criteria andResultcodeIn(List<Integer> values) {
            addCriterion("ResultCode in", values, "resultcode");
            return (Criteria) this;
        }

        public Criteria andResultcodeNotIn(List<Integer> values) {
            addCriterion("ResultCode not in", values, "resultcode");
            return (Criteria) this;
        }

        public Criteria andResultcodeBetween(Integer value1, Integer value2) {
            addCriterion("ResultCode between", value1, value2, "resultcode");
            return (Criteria) this;
        }

        public Criteria andResultcodeNotBetween(Integer value1, Integer value2) {
            addCriterion("ResultCode not between", value1, value2, "resultcode");
            return (Criteria) this;
        }

        public Criteria andTimeStampIsNull() {
            addCriterion("Time_Stamp is null");
            return (Criteria) this;
        }

        public Criteria andTimeStampIsNotNull() {
            addCriterion("Time_Stamp is not null");
            return (Criteria) this;
        }

        public Criteria andTimeStampEqualTo(String value) {
            addCriterion("Time_Stamp =", value, "timeStamp");
            return (Criteria) this;
        }

        public Criteria andTimeStampNotEqualTo(String value) {
            addCriterion("Time_Stamp <>", value, "timeStamp");
            return (Criteria) this;
        }

        public Criteria andTimeStampGreaterThan(String value) {
            addCriterion("Time_Stamp >", value, "timeStamp");
            return (Criteria) this;
        }

        public Criteria andTimeStampGreaterThanOrEqualTo(String value) {
            addCriterion("Time_Stamp >=", value, "timeStamp");
            return (Criteria) this;
        }

        public Criteria andTimeStampLessThan(String value) {
            addCriterion("Time_Stamp <", value, "timeStamp");
            return (Criteria) this;
        }

        public Criteria andTimeStampLessThanOrEqualTo(String value) {
            addCriterion("Time_Stamp <=", value, "timeStamp");
            return (Criteria) this;
        }

        public Criteria andTimeStampLike(String value) {
            addCriterion("Time_Stamp like", value, "timeStamp");
            return (Criteria) this;
        }

        public Criteria andTimeStampNotLike(String value) {
            addCriterion("Time_Stamp not like", value, "timeStamp");
            return (Criteria) this;
        }

        public Criteria andTimeStampIn(List<String> values) {
            addCriterion("Time_Stamp in", values, "timeStamp");
            return (Criteria) this;
        }

        public Criteria andTimeStampNotIn(List<String> values) {
            addCriterion("Time_Stamp not in", values, "timeStamp");
            return (Criteria) this;
        }

        public Criteria andTimeStampBetween(String value1, String value2) {
            addCriterion("Time_Stamp between", value1, value2, "timeStamp");
            return (Criteria) this;
        }

        public Criteria andTimeStampNotBetween(String value1, String value2) {
            addCriterion("Time_Stamp not between", value1, value2, "timeStamp");
            return (Criteria) this;
        }

        public Criteria andEncodestrIsNull() {
            addCriterion("EncodeStr is null");
            return (Criteria) this;
        }

        public Criteria andEncodestrIsNotNull() {
            addCriterion("EncodeStr is not null");
            return (Criteria) this;
        }

        public Criteria andEncodestrEqualTo(String value) {
            addCriterion("EncodeStr =", value, "encodestr");
            return (Criteria) this;
        }

        public Criteria andEncodestrNotEqualTo(String value) {
            addCriterion("EncodeStr <>", value, "encodestr");
            return (Criteria) this;
        }

        public Criteria andEncodestrGreaterThan(String value) {
            addCriterion("EncodeStr >", value, "encodestr");
            return (Criteria) this;
        }

        public Criteria andEncodestrGreaterThanOrEqualTo(String value) {
            addCriterion("EncodeStr >=", value, "encodestr");
            return (Criteria) this;
        }

        public Criteria andEncodestrLessThan(String value) {
            addCriterion("EncodeStr <", value, "encodestr");
            return (Criteria) this;
        }

        public Criteria andEncodestrLessThanOrEqualTo(String value) {
            addCriterion("EncodeStr <=", value, "encodestr");
            return (Criteria) this;
        }

        public Criteria andEncodestrLike(String value) {
            addCriterion("EncodeStr like", value, "encodestr");
            return (Criteria) this;
        }

        public Criteria andEncodestrNotLike(String value) {
            addCriterion("EncodeStr not like", value, "encodestr");
            return (Criteria) this;
        }

        public Criteria andEncodestrIn(List<String> values) {
            addCriterion("EncodeStr in", values, "encodestr");
            return (Criteria) this;
        }

        public Criteria andEncodestrNotIn(List<String> values) {
            addCriterion("EncodeStr not in", values, "encodestr");
            return (Criteria) this;
        }

        public Criteria andEncodestrBetween(String value1, String value2) {
            addCriterion("EncodeStr between", value1, value2, "encodestr");
            return (Criteria) this;
        }

        public Criteria andEncodestrNotBetween(String value1, String value2) {
            addCriterion("EncodeStr not between", value1, value2, "encodestr");
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

        public Criteria andIsputwelcomesmsIsNull() {
            addCriterion("isPutWelcomeSMS is null");
            return (Criteria) this;
        }

        public Criteria andIsputwelcomesmsIsNotNull() {
            addCriterion("isPutWelcomeSMS is not null");
            return (Criteria) this;
        }

        public Criteria andIsputwelcomesmsEqualTo(Boolean value) {
            addCriterion("isPutWelcomeSMS =", value, "isputwelcomesms");
            return (Criteria) this;
        }

        public Criteria andIsputwelcomesmsNotEqualTo(Boolean value) {
            addCriterion("isPutWelcomeSMS <>", value, "isputwelcomesms");
            return (Criteria) this;
        }

        public Criteria andIsputwelcomesmsGreaterThan(Boolean value) {
            addCriterion("isPutWelcomeSMS >", value, "isputwelcomesms");
            return (Criteria) this;
        }

        public Criteria andIsputwelcomesmsGreaterThanOrEqualTo(Boolean value) {
            addCriterion("isPutWelcomeSMS >=", value, "isputwelcomesms");
            return (Criteria) this;
        }

        public Criteria andIsputwelcomesmsLessThan(Boolean value) {
            addCriterion("isPutWelcomeSMS <", value, "isputwelcomesms");
            return (Criteria) this;
        }

        public Criteria andIsputwelcomesmsLessThanOrEqualTo(Boolean value) {
            addCriterion("isPutWelcomeSMS <=", value, "isputwelcomesms");
            return (Criteria) this;
        }

        public Criteria andIsputwelcomesmsIn(List<Boolean> values) {
            addCriterion("isPutWelcomeSMS in", values, "isputwelcomesms");
            return (Criteria) this;
        }

        public Criteria andIsputwelcomesmsNotIn(List<Boolean> values) {
            addCriterion("isPutWelcomeSMS not in", values, "isputwelcomesms");
            return (Criteria) this;
        }

        public Criteria andIsputwelcomesmsBetween(Boolean value1, Boolean value2) {
            addCriterion("isPutWelcomeSMS between", value1, value2, "isputwelcomesms");
            return (Criteria) this;
        }

        public Criteria andIsputwelcomesmsNotBetween(Boolean value1, Boolean value2) {
            addCriterion("isPutWelcomeSMS not between", value1, value2, "isputwelcomesms");
            return (Criteria) this;
        }

        public Criteria andRecordsequenceidLikeInsensitive(String value) {
            addCriterion("upper(RecordSequenceID) like", value.toUpperCase(), "recordsequenceid");
            return (Criteria) this;
        }

        public Criteria andUseridLikeInsensitive(String value) {
            addCriterion("upper(UserId) like", value.toUpperCase(), "userid");
            return (Criteria) this;
        }

        public Criteria andServicetypeLikeInsensitive(String value) {
            addCriterion("upper(ServiceType) like", value.toUpperCase(), "servicetype");
            return (Criteria) this;
        }

        public Criteria andSpidLikeInsensitive(String value) {
            addCriterion("upper(SpId) like", value.toUpperCase(), "spid");
            return (Criteria) this;
        }

        public Criteria andProductidLikeInsensitive(String value) {
            addCriterion("upper(ProductId) like", value.toUpperCase(), "productid");
            return (Criteria) this;
        }

        public Criteria andUpdatetimeLikeInsensitive(String value) {
            addCriterion("upper(UpdateTime) like", value.toUpperCase(), "updatetime");
            return (Criteria) this;
        }

        public Criteria andUpdatedescLikeInsensitive(String value) {
            addCriterion("upper(UpdateDesc) like", value.toUpperCase(), "updatedesc");
            return (Criteria) this;
        }

        public Criteria andLinkidLikeInsensitive(String value) {
            addCriterion("upper(LinkID) like", value.toUpperCase(), "linkid");
            return (Criteria) this;
        }

        public Criteria andContentLikeInsensitive(String value) {
            addCriterion("upper(Content) like", value.toUpperCase(), "content");
            return (Criteria) this;
        }

        public Criteria andEffectivedateLikeInsensitive(String value) {
            addCriterion("upper(EffectiveDate) like", value.toUpperCase(), "effectivedate");
            return (Criteria) this;
        }

        public Criteria andExpiredateLikeInsensitive(String value) {
            addCriterion("upper(ExpireDate) like", value.toUpperCase(), "expiredate");
            return (Criteria) this;
        }

        public Criteria andTimeStampLikeInsensitive(String value) {
            addCriterion("upper(Time_Stamp) like", value.toUpperCase(), "timeStamp");
            return (Criteria) this;
        }

        public Criteria andEncodestrLikeInsensitive(String value) {
            addCriterion("upper(EncodeStr) like", value.toUpperCase(), "encodestr");
            return (Criteria) this;
        }

        public Criteria andUserNameLikeInsensitive(String value) {
            addCriterion("upper(user_name) like", value.toUpperCase(), "userName");
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