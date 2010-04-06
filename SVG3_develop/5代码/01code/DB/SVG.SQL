------------------------------------------------------
-- Export file for user SVG                         --
-- Created by Administrator on 2009-05-14, 15:55:13 --
------------------------------------------------------

spool SVG.log

prompt
prompt Creating table APP_ACTIVITY_ENTER
prompt =================================
prompt
create table APP_ACTIVITY_ENTER
(
  SEQKEY      VARCHAR2(64) not null,
  ACTIVITYID  VARCHAR2(64),
  ORGID       VARCHAR2(64),
  USERID      VARCHAR2(64),
  CREATEID    VARCHAR2(64),
  CREATEDATE  DATE,
  UPDATEID    VARCHAR2(64),
  UPDATEDATE  DATE,
  USER_UNITID VARCHAR2(64),
  USER_ORGID  VARCHAR2(64),
  REMARK      VARCHAR2(200),
  STATUS      NUMBER(4)
)
tablespace HR30
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
alter table APP_ACTIVITY_ENTER
  add primary key (SEQKEY)
  using index 
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table APP_ACTIVITY_ENTERDETAIL
prompt =======================================
prompt
create table APP_ACTIVITY_ENTERDETAIL
(
  SEQKEY      VARCHAR2(64) not null,
  ACTIVITYID  VARCHAR2(64),
  ORGID       VARCHAR2(64),
  USERID      VARCHAR2(64),
  CREATEID    VARCHAR2(64),
  CREATEDATE  DATE,
  UPDATEID    VARCHAR2(64),
  UPDATEDATE  DATE,
  USER_UNITID VARCHAR2(64),
  USER_ORGID  VARCHAR2(64),
  REMARK      VARCHAR2(200),
  STATUS      NUMBER(4),
  NAME        VARCHAR2(64)
)
tablespace HR30
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
alter table APP_ACTIVITY_ENTERDETAIL
  add primary key (SEQKEY)
  using index 
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table APP_ACTIVITY_INFO
prompt ================================
prompt
create table APP_ACTIVITY_INFO
(
  SEQKEY        VARCHAR2(64) not null,
  BEGINDATE     DATE,
  ENDDATE       DATE,
  ENTER_ENDDATE DATE,
  CONDUCT_ORGID VARCHAR2(64),
  ENTER_ORGID   VARCHAR2(64),
  ACTIVITY_TYPE VARCHAR2(64),
  ADDRESS       VARCHAR2(200),
  STATUS        NUMBER(4),
  ATTACHMENT    VARCHAR2(64),
  CREATEID      VARCHAR2(64),
  CREATEDATE    DATE,
  UPDATEID      VARCHAR2(64),
  UPDATEDATE    DATE,
  USER_ORGID    VARCHAR2(64),
  USER_UNITID   VARCHAR2(64),
  ACTIVITY_NAME VARCHAR2(200),
  REMARK        VARCHAR2(500),
  FEE           NUMBER(12,2),
  PERSON_TIMES  NUMBER(10),
  TOTAL_FEE     NUMBER(12,2)
)
tablespace HR30
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
alter table APP_ACTIVITY_INFO
  add primary key (SEQKEY)
  using index 
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table APP_BBS_BROWSE
prompt =============================
prompt
create table APP_BBS_BROWSE
(
  SEQKEY     VARCHAR2(64) not null,
  CHANNELID  VARCHAR2(64),
  BROWSEDATE DATE,
  TOPIC_NUM  NUMBER(10),
  CLICK_NUM  NUMBER(10),
  REPLY_NUM  NUMBER(10)
)
tablespace HR30
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
alter table APP_BBS_BROWSE
  add primary key (SEQKEY)
  using index 
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table APP_BBS_CHANNEL
prompt ==============================
prompt
create table APP_BBS_CHANNEL
(
  SEQKEY           VARCHAR2(64) not null,
  NAME             VARCHAR2(100),
  DESCRIPTION      VARCHAR2(200),
  PARENTID         VARCHAR2(64),
  STATUS           NUMBER(4),
  CREATEID         VARCHAR2(64),
  CREATEDATE       DATE,
  UPDATEID         VARCHAR2(64),
  UPDATEDATE       DATE,
  USER_UNITID      VARCHAR2(64),
  USER_ORGID       VARCHAR2(64),
  CHANNEL_TYPE     NUMBER(4),
  CHANNEL_ORGID    VARCHAR2(200),
  CHANNEL_UNITID   VARCHAR2(200),
  CHANNEL_COTERIE  VARCHAR2(200),
  TOPIC_NUM        NUMBER(10),
  TOTAL_NUM        NUMBER(10),
  LAST_PUBLISHDATE DATE,
  LAST_PUBLISHER   VARCHAR2(64),
  LAST_TITLE       VARCHAR2(500),
  LAST_TOPICID     VARCHAR2(64),
  DISPLAYORDER     NUMBER(4)
)
tablespace HR30
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
alter table APP_BBS_CHANNEL
  add primary key (SEQKEY)
  using index 
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table APP_BBS_CHANNEL_EXPERT
prompt =====================================
prompt
create table APP_BBS_CHANNEL_EXPERT
(
  SEQKEY      VARCHAR2(64) not null,
  CHANNELID   VARCHAR2(64),
  USERID      VARCHAR2(64),
  NAME        VARCHAR2(64),
  PSNNUM      VARCHAR2(64),
  STATUS      NUMBER(4),
  CREATEID    VARCHAR2(64),
  CREATEDATE  DATE,
  UPDATEID    VARCHAR2(64),
  UPDATEDATE  DATE,
  USER_ORGID  VARCHAR2(64),
  USER_UNITID VARCHAR2(64),
  DESCRIPTION VARCHAR2(500)
)
tablespace HR30
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
alter table APP_BBS_CHANNEL_EXPERT
  add primary key (SEQKEY)
  using index 
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table APP_BBS_CHANNEL_MASTER
prompt =====================================
prompt
create table APP_BBS_CHANNEL_MASTER
(
  SEQKEY      VARCHAR2(64) not null,
  CHANNELID   VARCHAR2(64),
  USERID      VARCHAR2(64),
  NAME        VARCHAR2(64),
  PSNNUM      VARCHAR2(64),
  STATUS      NUMBER(4),
  CREATEID    VARCHAR2(64),
  CREATEDATE  DATE,
  UPDATEID    VARCHAR2(64),
  UPDATEDATE  DATE,
  USER_ORGID  VARCHAR2(64),
  USER_UNITID VARCHAR2(64),
  DESCRIPTION VARCHAR2(200)
)
tablespace HR30
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
alter table APP_BBS_CHANNEL_MASTER
  add primary key (SEQKEY)
  using index 
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table APP_BBS_TOPIC
prompt ============================
prompt
create table APP_BBS_TOPIC
(
  SEQKEY          VARCHAR2(64) not null,
  TITLE           VARCHAR2(400),
  CONTENT         VARCHAR2(100),
  STATUS          NUMBER(4),
  LASTREPLYDATE   DATE,
  REPLY_NUM       NUMBER(10),
  CLICK_NUM       NUMBER(10),
  ASSESS_POINT    NUMBER(10),
  ASSESS_LEVEL    NUMBER(4),
  ASSESSOR        VARCHAR2(64),
  ASSESS_DATE     DATE,
  CREATEID        VARCHAR2(64),
  CREATEDATE      DATE,
  UPDATEID        VARCHAR2(64),
  UPDATEDATE      DATE,
  USER_UNITID     VARCHAR2(64),
  USER_ORGID      VARCHAR2(64),
  CHANNELID       VARCHAR2(64),
  IS_REPLY        NUMBER(4),
  ATTACHMENT      VARCHAR2(200),
  IS_TOP          NUMBER(4),
  IS_HOT          NUMBER(4),
  REPLY_TOPICID   VARCHAR2(64),
  TOPICTYPE       VARCHAR2(64),
  KEY_TAG         VARCHAR2(500),
  MOOD_IMG        VARCHAR2(100),
  RIGHT_MODE      NUMBER(4),
  RIGHT_COTERIEID VARCHAR2(200),
  RIGHT_ORGID     VARCHAR2(200),
  LASTREPLYMAN    VARCHAR2(64),
  MUST_REPLY      NUMBER(4)
)
tablespace HR30
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
alter table APP_BBS_TOPIC
  add primary key (SEQKEY)
  using index 
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table APP_BBS_TOPIC_ASSESS
prompt ===================================
prompt
create table APP_BBS_TOPIC_ASSESS
(
  SEQKEY           VARCHAR2(64) not null,
  TOPICID          VARCHAR2(64),
  ASSESS_POINT     NUMBER(10),
  ASSESS_LEVEL     NUMBER(10),
  ASSESS_USERID    VARCHAR2(64),
  ASSESS_ALIASNAME VARCHAR2(64),
  ASSESS_PSNNUM    VARCHAR2(64),
  CREATEID         VARCHAR2(64),
  CREATEDATE       DATE,
  UPDATEID         VARCHAR2(64),
  UPDATEDATE       DATE,
  DESCRIPTION      VARCHAR2(64),
  USER_UNITID      VARCHAR2(64),
  USER_ORGID       VARCHAR2(64)
)
tablespace HR30
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
alter table APP_BBS_TOPIC_ASSESS
  add primary key (SEQKEY)
  using index 
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table APP_BBS_TOPIC_POINT
prompt ==================================
prompt
create table APP_BBS_TOPIC_POINT
(
  SEQKEY           VARCHAR2(64) not null,
  TOPICID          VARCHAR2(64),
  FROM_USERID      VARCHAR2(64),
  TO_USERID        VARCHAR2(64),
  REASON           VARCHAR2(500),
  POINT            NUMBER(10),
  CREATEID         VARCHAR2(64),
  CREATEDATE       DATE,
  UPDATEID         VARCHAR2(64),
  UPDATEDATE       DATE,
  USER_UNITID      VARCHAR2(64),
  USER_ORGID       VARCHAR2(64),
  LINK_DESCRIPTION VARCHAR2(64),
  LINK_MODULE      VARCHAR2(64),
  LINK_ACTION      VARCHAR2(64),
  LINK_ID          VARCHAR2(64),
  LINK_MODEL       VARCHAR2(64),
  LINK_URL         VARCHAR2(64)
)
tablespace HR30
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
alter table APP_BBS_TOPIC_POINT
  add primary key (SEQKEY)
  using index 
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table APP_BBS_USER
prompt ===========================
prompt
create table APP_BBS_USER
(
  USERID           VARCHAR2(64) not null,
  USERNAME         VARCHAR2(100),
  LOGINNAME        VARCHAR2(100),
  PSNNUM           VARCHAR2(64),
  STATUS           NUMBER(4),
  USER_POINT       NUMBER(10),
  USER_LEVEL       NUMBER(4),
  ALIASNAME        VARCHAR2(64),
  ORGCODE          VARCHAR2(64),
  ORG_ID           VARCHAR2(64),
  USER_GROUP       VARCHAR2(64),
  TOPIC_NUM        NUMBER(10),
  BROWSE_NUM       NUMBER(10),
  REPLY_NUM        NUMBER(10),
  LAST_BROWSEDATE  DATE,
  LAST_REPLYDATE   DATE,
  LAST_PUBLISHDATE DATE,
  USER_CITY        VARCHAR2(64),
  USER_IMG         VARCHAR2(64)
)
tablespace HR30
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
alter table APP_BBS_USER
  add constraint APP_BBS_USER_PK11199932911640 primary key (USERID)
  using index 
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table APP_BBS_USER_BROWSE
prompt ==================================
prompt
create table APP_BBS_USER_BROWSE
(
  SEQKEY      VARCHAR2(64) not null,
  TOPICID     VARCHAR2(64),
  USERID      VARCHAR2(64),
  PSNNUM      VARCHAR2(64),
  ALIASNAME   VARCHAR2(64),
  BROWSE_DATE DATE
)
tablespace HR30
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
alter table APP_BBS_USER_BROWSE
  add primary key (SEQKEY)
  using index 
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table APP_BBS_USER_FELLOW
prompt ==================================
prompt
create table APP_BBS_USER_FELLOW
(
  SEQKEY           VARCHAR2(64) not null,
  USERID           VARCHAR2(64),
  ALIASNAME        VARCHAR2(64),
  PSNNUM           VARCHAR2(64),
  FELLOW_USERID    VARCHAR2(64),
  FELLOW_PSNNUM    VARCHAR2(64),
  FELLOW_ALIASNAME VARCHAR2(64),
  STATUS           NUMBER(4),
  CREATEID         VARCHAR2(64),
  CREATEDATE       DATE,
  UPDATEID         VARCHAR2(64),
  UPDATEDATE       DATE,
  USER_UNITID      VARCHAR2(64),
  USER_ORGID       VARCHAR2(64)
)
tablespace HR30
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
alter table APP_BBS_USER_FELLOW
  add primary key (SEQKEY)
  using index 
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table APP_CALENDAR_WORK
prompt ================================
prompt
create table APP_CALENDAR_WORK
(
  TITLE           VARCHAR2(200) not null,
  CONTENT         VARCHAR2(500),
  BEGIN_DATE      DATE,
  END_DATE        DATE,
  PUBLIC_MODE     NUMBER(4),
  ATTACHMENT      VARCHAR2(200),
  LINK_CUSTOMERID VARCHAR2(64),
  LINK_PROJECTID  VARCHAR2(64),
  IMPORTANTLEVEL  NUMBER(4),
  SEQKEY          VARCHAR2(64) not null,
  CREATEID        VARCHAR2(64),
  CREATEDATE      DATE,
  UPDATEID        VARCHAR2(64),
  UPDATEDATE      DATE,
  USER_ORGID      VARCHAR2(64),
  USER_UNITID     VARCHAR2(64),
  PSNNUM          VARCHAR2(64),
  ORGCODE         VARCHAR2(64),
  USERID          VARCHAR2(64),
  ALLOCATEE       VARCHAR2(64),
  ALLOCATEENAME   VARCHAR2(100),
  BROWSER_USERID  VARCHAR2(1000),
  BROWSER_ORGID   VARCHAR2(1000),
  TASKID          VARCHAR2(64),
  LINK_TASKID     VARCHAR2(64),
  PLAN_HOURS      NUMBER(12,2),
  REAL_HOURS      NUMBER(12,2),
  FINISHSTATUS    NUMBER(4)
)
tablespace HR30
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
alter table APP_CALENDAR_WORK
  add primary key (SEQKEY)
  using index 
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table APP_FORM_ACTIVITY
prompt ================================
prompt
create table APP_FORM_ACTIVITY
(
  SEQKEY       VARCHAR2(64) not null,
  MODELNAME    VARCHAR2(64),
  ACTIVITYID   VARCHAR2(64),
  TEMPLATENAME VARCHAR2(100),
  ACTIVITYTYPE NUMBER(4),
  ACTIVITYNAME VARCHAR2(100)
)
tablespace HR30
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
alter table APP_FORM_ACTIVITY
  add constraint APP_FORM_ACTI_PK11187835970031 primary key (SEQKEY)
  using index 
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table APP_FORM_ACTIVITYFIELD
prompt =====================================
prompt
create table APP_FORM_ACTIVITYFIELD
(
  SEQKEY      VARCHAR2(64) not null,
  MODELNAME   VARCHAR2(100),
  ACTIVITYID  VARCHAR2(64),
  FIELDNAME   VARCHAR2(100),
  EDITFLAG    NUMBER(4),
  DESCRIPTION VARCHAR2(100),
  HTMLCTRL    VARCHAR2(100)
)
tablespace HR30
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
alter table APP_FORM_ACTIVITYFIELD
  add primary key (SEQKEY)
  using index 
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table APP_FORM_DEFINE
prompt ==============================
prompt
create table APP_FORM_DEFINE
(
  MODELNAME         VARCHAR2(100),
  DESCRIPTION       VARCHAR2(200) not null,
  BUSINESSTYPE      NUMBER(4),
  USEATTACHMENT     NUMBER(4),
  USEHTMLEDIT       NUMBER(4),
  PROCESSNAME       VARCHAR2(100),
  CREATEID          VARCHAR2(64),
  CREATEDATE        DATE,
  UPDATEID          VARCHAR2(64),
  UPDATEDATE        DATE,
  USER_ORGID        VARCHAR2(64),
  USER_UNITID       VARCHAR2(64),
  SEQKEY            VARCHAR2(64) not null,
  HTMLCONTENT       VARCHAR2(64),
  FORM_ROWS         NUMBER(10),
  FORM_COLUMNS      NUMBER(10),
  FUNCTION_TEMPLATE VARCHAR2(100)
)
tablespace HR30
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
alter table APP_FORM_DEFINE
  add primary key (SEQKEY)
  using index 
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table APP_FORM_FIELD
prompt =============================
prompt
create table APP_FORM_FIELD
(
  SEQKEY       VARCHAR2(64) not null,
  FIELDNAME    VARCHAR2(100),
  DESCRIPTION  VARCHAR2(200),
  CTRLTYPE     VARCHAR2(100),
  CODETYPE     VARCHAR2(100),
  POSX         NUMBER(10),
  POSY         NUMBER(10),
  ROWSPAN      NUMBER(10),
  COLUMNS      NUMBER(10),
  MODELNAME    VARCHAR2(100),
  OTHERINFO    VARCHAR2(500),
  CANSEARCH    NUMBER(4),
  CANTOTAL     NUMBER(4),
  DEFAULTVALUE VARCHAR2(100),
  ISEMPTY      NUMBER(4),
  FIELDTYPE    VARCHAR2(64)
)
tablespace HR30
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
alter table APP_FORM_FIELD
  add primary key (SEQKEY)
  using index 
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table APP_GENERAL_CODEDEFINE
prompt =====================================
prompt
create table APP_GENERAL_CODEDEFINE
(
  CODETYPE    VARCHAR2(32) not null,
  MANUALFLAG  NUMBER(4),
  DESCRIPTION VARCHAR2(50),
  ORGLEVEL    NUMBER(4),
  SEQUENCELEN NUMBER(4),
  FIXCODE     VARCHAR2(32),
  BUILDMODE   NUMBER(10),
  ORGLENGTH   NUMBER(10),
  CREATEID    VARCHAR2(32),
  CREATEDATE  DATE,
  UPDATEID    VARCHAR2(32),
  UPDATEDATE  DATE,
  USER_ORGID  VARCHAR2(32),
  USER_UNITID VARCHAR2(32)
)
tablespace HR30
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
alter table APP_GENERAL_CODEDEFINE
  add primary key (CODETYPE)
  using index 
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table APP_GENERAL_CODEPOOL
prompt ===================================
prompt
create table APP_GENERAL_CODEPOOL
(
  SEQKEY      VARCHAR2(32) not null,
  CODETYPE    VARCHAR2(32),
  BUILDMODE   NUMBER(32),
  GROUPNAME   VARCHAR2(64),
  DYEAR       VARCHAR2(10),
  NUM         NUMBER(10),
  CREATEID    VARCHAR2(32),
  CREATEDATE  DATE,
  UPDATEID    VARCHAR2(32),
  UPDATEDATE  DATE,
  USER_ORGID  VARCHAR2(32),
  USER_UNITID VARCHAR2(32)
)
tablespace HR30
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
alter table APP_GENERAL_CODEPOOL
  add primary key (SEQKEY)
  using index 
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table APP_GENERAL_COLLECTION
prompt =====================================
prompt
create table APP_GENERAL_COLLECTION
(
  SEQKEY           VARCHAR2(64) not null,
  CREATEID         VARCHAR2(64),
  CREATEDATE       DATE,
  UPDATEID         VARCHAR2(64),
  UPDATEDATE       DATE,
  USER_ORGID       VARCHAR2(64),
  USER_UNITID      VARCHAR2(64),
  LINK_MODEL       VARCHAR2(64),
  LINK_ID          VARCHAR2(64),
  LINK_MODULE      VARCHAR2(100),
  LINK_DESCRIPTION VARCHAR2(200),
  LINK_URL         VARCHAR2(200),
  LINK_ACTION      VARCHAR2(64),
  USERID           VARCHAR2(64)
)
tablespace HR30
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
alter table APP_GENERAL_COLLECTION
  add primary key (SEQKEY)
  using index 
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table APP_GENERAL_COMMUNICATE
prompt ======================================
prompt
create table APP_GENERAL_COMMUNICATE
(
  SEQKEY      VARCHAR2(32) not null,
  LINKTYPE    VARCHAR2(32),
  LINKID      VARCHAR2(32),
  LINKMODEL   VARCHAR2(100),
  RIGHTLEVEL  NUMBER(4),
  CREATEID    VARCHAR2(32),
  CREATEDATE  DATE,
  UPDATEID    VARCHAR2(32),
  UPDATEDATE  DATE,
  USER_ORGID  VARCHAR2(32),
  USER_UNITID VARCHAR2(32),
  CONTENT     CLOB,
  TITLE       VARCHAR2(100)
)
tablespace HR30
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
alter table APP_GENERAL_COMMUNICATE
  add primary key (SEQKEY)
  using index 
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table APP_GENERAL_COTERIE
prompt ==================================
prompt
create table APP_GENERAL_COTERIE
(
  SEQKEY      VARCHAR2(64) not null,
  NAME        VARCHAR2(100),
  DESCRIPTION VARCHAR2(500),
  CREATEID    VARCHAR2(64),
  CREATEDATE  DATE,
  UPDATEID    VARCHAR2(64),
  UPDATEDATE  DATE,
  USER_UNITID VARCHAR2(64),
  USER_ORGID  VARCHAR2(64)
)
tablespace HR30
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
alter table APP_GENERAL_COTERIE
  add primary key (SEQKEY)
  using index 
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table APP_GENERAL_COTERIE_USER
prompt =======================================
prompt
create table APP_GENERAL_COTERIE_USER
(
  SEQKEY      VARCHAR2(64) not null,
  COTERIEID   VARCHAR2(64),
  USERID      VARCHAR2(64),
  PSNNUM      VARCHAR2(64),
  ALIASNAME   VARCHAR2(64),
  STATUS      NUMBER(4),
  DESCRIPTION VARCHAR2(400),
  CREATEID    VARCHAR2(64),
  CREATEDATE  DATE,
  UPDATEID    VARCHAR2(64),
  UPDATEDATE  DATE,
  USER_UNITID VARCHAR2(64),
  USER_ORGID  VARCHAR2(64),
  NAME        VARCHAR2(64)
)
tablespace HR30
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
alter table APP_GENERAL_COTERIE_USER
  add primary key (SEQKEY)
  using index 
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table APP_GENERAL_OPINION
prompt ==================================
prompt
create table APP_GENERAL_OPINION
(
  SEQKEY           VARCHAR2(64) not null,
  OPINIONOR        VARCHAR2(64),
  OPINION_DATE     DATE,
  SCORE            NUMBER(10),
  TITLE            VARCHAR2(100),
  CONTENT          VARCHAR2(1000),
  ISREMIND         NUMBER(4),
  LINK_MODEL       VARCHAR2(100),
  LINK_ID          VARCHAR2(64),
  CREATEID         VARCHAR2(64),
  CREATEDATE       DATE,
  UPDATEID         VARCHAR2(64),
  UPDATEDATE       DATE,
  USER_ORGID       VARCHAR2(64),
  USER_UNITID      VARCHAR2(64),
  LINK_DESCRIPTION VARCHAR2(200),
  LINK_URL         VARCHAR2(200)
)
tablespace HR30
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
alter table APP_GENERAL_OPINION
  add primary key (SEQKEY)
  using index 
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table APP_GENERAL_RECOMMEND
prompt ====================================
prompt
create table APP_GENERAL_RECOMMEND
(
  SEQKEY            VARCHAR2(64) not null,
  RECOMMEND_MODE    NUMBER(4),
  RECOMMEND_LEVEL   NUMBER(4),
  CAN_READER        VARCHAR2(200),
  INFORM_MODE       NUMBER(4),
  RECOMMEND_USER    VARCHAR2(64),
  REASON            VARCHAR2(500),
  CREATEID          VARCHAR2(64),
  CREATEDATE        DATE,
  UPDATEID          VARCHAR2(64),
  UPDATEDATE        DATE,
  USER_ORGID        VARCHAR2(64),
  USER_UNITID       VARCHAR2(64),
  LINK_MODEL        VARCHAR2(64),
  LINK_ID           VARCHAR2(64),
  LINK_MODULE       VARCHAR2(100),
  RECOMMEND_ORG     VARCHAR2(64),
  LINK_DESCRIPTION  VARCHAR2(200),
  LINK_URL          VARCHAR2(200),
  LINK_ACTION       VARCHAR2(64),
  LINK_APPID        VARCHAR2(64),
  RECOMMEND_COTERIE VARCHAR2(64)
)
tablespace HR30
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
alter table APP_GENERAL_RECOMMEND
  add primary key (SEQKEY)
  using index 
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table APP_GENERAL_SEARCH
prompt =================================
prompt
create table APP_GENERAL_SEARCH
(
  SEQKEY        VARCHAR2(64) not null,
  MODULENAME    VARCHAR2(200),
  MODELNAME     VARCHAR2(100),
  DESCRIPTION   VARCHAR2(200),
  SHARED_TYPE   VARCHAR2(64),
  CREATEID      VARCHAR2(64),
  CREATEDATE    DATE,
  UPDATEID      VARCHAR2(64),
  UPDATEDATE    DATE,
  USER_ORGID    VARCHAR2(64),
  USER_UNITID   VARCHAR2(64),
  DISPLAY_ROWS  NUMBER(4),
  DEFAULTORDER  VARCHAR2(64),
  TOTALBY_FIELD VARCHAR2(64),
  CHART_TYPE    VARCHAR2(64),
  CHART_FIELD   VARCHAR2(64),
  GSEARCH_TYPE  NUMBER(4)
)
tablespace HR30
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
alter table APP_GENERAL_SEARCH
  add primary key (SEQKEY)
  using index 
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table APP_GENERAL_S_CONDITION
prompt ======================================
prompt
create table APP_GENERAL_S_CONDITION
(
  SEQKEY      VARCHAR2(64) not null,
  GSEARCH_ID  VARCHAR2(64),
  LEFTSYMBOL  VARCHAR2(64),
  FIELDNAME   VARCHAR2(100),
  MATCHSYMBOL VARCHAR2(64),
  MATCHVALUE  VARCHAR2(100),
  RIGHTSYMBOL VARCHAR2(64),
  LINKSYMBOL  VARCHAR2(64),
  MODELNAME   VARCHAR2(100),
  DISPLAYNAME VARCHAR2(100),
  CREATEID    VARCHAR2(64),
  CREATEDATE  DATE,
  UPDATEID    VARCHAR2(64),
  UPDATEDATE  DATE,
  USER_ORGID  VARCHAR2(64),
  USER_UNITID VARCHAR2(64)
)
tablespace HR30
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
alter table APP_GENERAL_S_CONDITION
  add primary key (SEQKEY)
  using index 
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table APP_GENERAL_S_DISPLAY
prompt ====================================
prompt
create table APP_GENERAL_S_DISPLAY
(
  SEQKEY         VARCHAR2(64) not null,
  GSEARCH_ID     VARCHAR2(64),
  MODELNAME      VARCHAR2(100),
  FIELDNAME      VARCHAR2(100),
  DISPLAYNAME    VARCHAR2(100),
  IS_DISPLAY     NUMBER(4),
  DISPLAY_FORMAT VARCHAR2(20),
  ALIGNMENT      NUMBER(4),
  DISPLAY_ORDER  NUMBER(4),
  EXPRESSION     VARCHAR2(100)
)
tablespace HR30
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
alter table APP_GENERAL_S_DISPLAY
  add primary key (SEQKEY)
  using index 
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table APP_GENERAL_S_ORDER
prompt ==================================
prompt
create table APP_GENERAL_S_ORDER
(
  SEQKEY      VARCHAR2(64) not null,
  GSEARCH_ID  VARCHAR2(64),
  FIELDNAME   VARCHAR2(100),
  DISPLAYNAME VARCHAR2(100),
  ASC_DESC    VARCHAR2(10)
)
tablespace HR30
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
alter table APP_GENERAL_S_ORDER
  add primary key (SEQKEY)
  using index 
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table APP_PROJECT
prompt ==========================
prompt
create table APP_PROJECT
(
  SEQKEY      VARCHAR2(64) not null,
  PROJECTNO   VARCHAR2(64),
  PROJECTNAME VARCHAR2(200),
  MANAGER     VARCHAR2(64),
  DESCRIPTION VARCHAR2(500),
  PARENTID    VARCHAR2(64),
  ORGCODE     VARCHAR2(64),
  CREATEID    VARCHAR2(64),
  CREATEDATE  DATE,
  UPDATEID    VARCHAR2(64),
  UPDATEDATE  DATE,
  USER_ORGID  VARCHAR2(64),
  USER_UNITID VARCHAR2(64)
)
tablespace HR30
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
alter table APP_PROJECT
  add primary key (SEQKEY)
  using index 
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table APP_REPORT_CAT
prompt =============================
prompt
create table APP_REPORT_CAT
(
  SEQKEY      VARCHAR2(64) not null,
  NAME        VARCHAR2(200),
  DESCRIPTION VARCHAR2(500),
  PARENTID    VARCHAR2(64),
  CREATEID    VARCHAR2(64),
  CREATEDATE  DATE,
  UPDATEID    VARCHAR2(64),
  UPDATEDATE  DATE,
  USER_UNITID VARCHAR2(64),
  USER_ORGID  VARCHAR2(64)
)
tablespace HR30
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
alter table APP_REPORT_CAT
  add primary key (SEQKEY)
  using index 
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table APP_REPORT_CHECK
prompt ===============================
prompt
create table APP_REPORT_CHECK
(
  SEQKEY            VARCHAR2(64) not null,
  REPORT_ID         VARCHAR2(64),
  NAME              VARCHAR2(200),
  FORMULA_DESC      VARCHAR2(500),
  FORMULA_SQL       VARCHAR2(500),
  FORMULA           VARCHAR2(500),
  IS_MUST           NUMBER(4),
  REMARK            VARCHAR2(500),
  CREATEID          VARCHAR2(64),
  CREATEDATE        DATE,
  UPDATEID          VARCHAR2(64),
  UPDATEDATE        DATE,
  USER_UNITID       VARCHAR2(64),
  USER_ORGID        VARCHAR2(64),
  CHECK_LEVEL       VARCHAR2(64),
  FORMULA_CONDITION VARCHAR2(200)
)
tablespace HR30
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
alter table APP_REPORT_CHECK
  add primary key (SEQKEY)
  using index 
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table APP_REPORT_COLLECT
prompt =================================
prompt
create table APP_REPORT_COLLECT
(
  SEQKEY        VARCHAR2(64) not null,
  REPORT_ID     VARCHAR2(64),
  ROW_NO        VARCHAR2(64),
  ROW_NAME      VARCHAR2(200),
  CREATEID      VARCHAR2(64),
  CREATEDATE    DATE,
  UPDATEID      VARCHAR2(64),
  UPDATEDATE    DATE,
  USER_ORGID    VARCHAR2(64),
  USER_UNITID   VARCHAR2(64),
  COLLECT_ORDER NUMBER(4)
)
tablespace HR30
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
alter table APP_REPORT_COLLECT
  add primary key (SEQKEY)
  using index 
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table APP_REPORT_DATASOURCE
prompt ====================================
prompt
create table APP_REPORT_DATASOURCE
(
  SEQKEY      VARCHAR2(64) not null,
  REPORT_ID   VARCHAR2(64),
  NAME        VARCHAR2(100),
  DESCRIPTION VARCHAR2(200),
  DATA_SQL    VARCHAR2(500),
  CREATEID    VARCHAR2(64),
  CREATEDATE  DATE,
  UPDATEID    VARCHAR2(64),
  UPDATEDATE  DATE,
  USER_UNITID VARCHAR2(64),
  USER_ORGID  VARCHAR2(64),
  IS_MULTIROW NUMBER(4)
)
tablespace HR30
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
alter table APP_REPORT_DATASOURCE
  add primary key (SEQKEY)
  using index 
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table APP_REPORT_DEF
prompt =============================
prompt
create table APP_REPORT_DEF
(
  SEQKEY               VARCHAR2(64) not null,
  REPORT_NAME          VARCHAR2(500),
  REPORT_LEVEL         NUMBER(4),
  REPORT_MODELNAME     VARCHAR2(100),
  REPORT_NO            VARCHAR2(100),
  ISSENDSUPERUNIT      NUMBER(4),
  ISLOWERUNITSEND      NUMBER(4),
  REPORT_CYCLE         VARCHAR2(64),
  REPORT_TEMPLATE_FILE VARCHAR2(200),
  REPORT_YEAR          VARCHAR2(64),
  REMARK               VARCHAR2(500),
  CREATEID             VARCHAR2(64),
  CREATEDATE           DATE,
  UPDATEID             VARCHAR2(64),
  UPDATEDATE           DATE,
  USER_UNITID          VARCHAR2(64),
  USER_ORGID           VARCHAR2(64),
  VIEW_ACTION          VARCHAR2(200),
  PRINT_ACTION         VARCHAR2(200),
  REPORT_CAT           VARCHAR2(64),
  AUDITOR              VARCHAR2(64),
  RATIFIER             VARCHAR2(64),
  REPORT_TYPE          VARCHAR2(64),
  REPORT_ROWS          NUMBER(4),
  IS_COLLECT           NUMBER(4)
)
tablespace HR30
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
alter table APP_REPORT_DEF
  add primary key (SEQKEY)
  using index 
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table APP_REPORT_FIELD
prompt ===============================
prompt
create table APP_REPORT_FIELD
(
  SEQKEY                 VARCHAR2(64) not null,
  REPORT_ID              VARCHAR2(64),
  ROW_NO                 NUMBER(12),
  FIELD_CODE             VARCHAR2(64),
  FIELD_NAME             VARCHAR2(200),
  FIELD_SHORTNAME        VARCHAR2(64),
  DATA_TYPE              VARCHAR2(64),
  DATA_SCALE             NUMBER(4),
  DATASOURCE_TYPE        VARCHAR2(64),
  DATASOURCE             VARCHAR2(64),
  DATASOURCE_CONDITION   VARCHAR2(200),
  DATA_SQL               VARCHAR2(500),
  SUM_FORMULA            VARCHAR2(200),
  CREATEID               VARCHAR2(64),
  CREATEDATE             DATE,
  UPDATEID               VARCHAR2(64),
  UPDATEDATE             DATE,
  USER_UNITID            VARCHAR2(64),
  USER_ORGID             VARCHAR2(64),
  FIELD_NO               NUMBER(4),
  DATA_OTHER_REPORTID    VARCHAR2(64),
  DATA_OTHER_FIELDID     VARCHAR2(64),
  DATA_OTHER_PERIODMONTH VARCHAR2(64),
  IS_COLLECT             NUMBER(4),
  COLLECT_FORMULA        VARCHAR2(100)
)
tablespace HR30
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
alter table APP_REPORT_FIELD
  add primary key (SEQKEY)
  using index 
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table APP_REPORT_INSTANCE
prompt ==================================
prompt
create table APP_REPORT_INSTANCE
(
  SEQKEY          VARCHAR2(64) not null,
  REPORT_NAME     VARCHAR2(500),
  REPORT_LEVEL    NUMBER(4),
  REPORT_ID       VARCHAR2(100),
  REPORT_NO       VARCHAR2(100),
  REPORT_CYCLE    VARCHAR2(64),
  PERIOD_YEAR     VARCHAR2(64),
  TITLE           VARCHAR2(200),
  CREATEID        VARCHAR2(64),
  CREATEDATE      DATE,
  UPDATEID        VARCHAR2(64),
  UPDATEDATE      DATE,
  USER_UNITID     VARCHAR2(64),
  USER_ORGID      VARCHAR2(64),
  PERIOD_MONTH    VARCHAR2(64),
  PERIOD_QUARTOR  VARCHAR2(64),
  PERIOD_WEEK     VARCHAR2(64),
  UNITID          VARCHAR2(64),
  REPORTER        VARCHAR2(64),
  AUDITOR         VARCHAR2(64),
  RATIFIER        VARCHAR2(64),
  STATUS          NUMBER(4),
  REPORT_CAT      VARCHAR2(64),
  ATTACHMENT      VARCHAR2(64),
  ROLLBACK_REASON VARCHAR2(500),
  BEGINDATE       DATE,
  ENDDATE         DATE,
  ORGCODE         VARCHAR2(64)
)
tablespace HR30
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
alter table APP_REPORT_INSTANCE
  add primary key (SEQKEY)
  using index 
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table APP_REPORT_SECURITY
prompt ==================================
prompt
create table APP_REPORT_SECURITY
(
  SEQKEY        VARCHAR2(64) not null,
  REPORT_CAT    VARCHAR2(64),
  ROLEID        VARCHAR2(64),
  UNITID        VARCHAR2(64),
  RIGHT_LEVEL   VARCHAR2(64),
  CREATEID      VARCHAR2(64),
  CREATEDATE    DATE,
  UPDATEID      VARCHAR2(64),
  UPDATEDATE    DATE,
  USER_ORGID    VARCHAR2(64),
  USER_UNITID   VARCHAR2(64),
  ACCREDIT_TYPE VARCHAR2(64),
  USERID        VARCHAR2(64)
)
tablespace HR30
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
alter table APP_REPORT_SECURITY
  add primary key (SEQKEY)
  using index 
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table APP_SUMMARIZE
prompt ============================
prompt
create table APP_SUMMARIZE
(
  SEQKEY          VARCHAR2(64) not null,
  ORGCODE         VARCHAR2(64),
  PSNNUM          VARCHAR2(64),
  POSCODE         VARCHAR2(64),
  ORGNAME         VARCHAR2(200),
  POSNAME         VARCHAR2(200),
  SUMMARIZETYPE   VARCHAR2(64),
  PERIODTYPE      VARCHAR2(64),
  PERIODYEAR      VARCHAR2(64),
  PERIODMONTH     VARCHAR2(64),
  PERIODQUARTOR   VARCHAR2(200),
  TITLE           VARCHAR2(1000),
  ATTACHMENT      VARCHAR2(100),
  INFORM_MODE     VARCHAR2(64),
  MAIN_RENDER     VARCHAR2(64),
  SUB_RENDER      VARCHAR2(500),
  ISREMIND        NUMBER(4),
  CREATEID        VARCHAR2(64),
  CREATEDATE      DATE,
  UPDATEID        VARCHAR2(64),
  UPDATEDATE      DATE,
  USER_ORGID      VARCHAR2(64),
  USER_UNITID     VARCHAR2(64),
  STATUS          NUMBER(4),
  NAME            VARCHAR2(64),
  LINK_PROJECTID  VARCHAR2(64),
  LINK_CUSTOMERID VARCHAR2(64),
  PERIODWEEK      NUMBER(4),
  LASTSUMMARIZEID VARCHAR2(64),
  NEXTSUMMARIZEID VARCHAR2(64),
  DESCRIPTION     VARCHAR2(500),
  ASSESS_REMARK   VARCHAR2(500)
)
tablespace HR30
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
alter table APP_SUMMARIZE
  add primary key (SEQKEY)
  using index 
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table APP_SUMMARIZE_TEMPLATE
prompt =====================================
prompt
create table APP_SUMMARIZE_TEMPLATE
(
  SEQKEY      VARCHAR2(64) not null,
  NAME        VARCHAR2(200),
  DESCRIPTION VARCHAR2(500),
  ATTACHMENT  VARCHAR2(200),
  CREATEID    VARCHAR2(64),
  CREATEDATE  DATE,
  UPDATEID    VARCHAR2(64),
  UPDATEDATE  DATE,
  USER_ORGID  VARCHAR2(64),
  USER_UNITID VARCHAR2(64)
)
tablespace HR30
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
alter table APP_SUMMARIZE_TEMPLATE
  add primary key (SEQKEY)
  using index 
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table APP_TASK
prompt =======================
prompt
create table APP_TASK
(
  SEQKEY          VARCHAR2(64) not null,
  TITLE           VARCHAR2(500),
  CONTENT         VARCHAR2(1000),
  TASKTYPE        VARCHAR2(64),
  ASSIGNER        VARCHAR2(64),
  PARENTID        VARCHAR2(64),
  IMPORTLEVEL     VARCHAR2(64),
  PRIORITY        VARCHAR2(64),
  STATUS          NUMBER(4),
  BEGINDATE       DATE,
  ENDDATE         DATE,
  PLANHOURS       NUMBER(10,2),
  PRINCIPAL       VARCHAR2(64),
  COADJUTANT      VARCHAR2(1000),
  VISITOR         VARCHAR2(1000),
  INFORMMODE      NUMBER(4),
  ISREMIND        NUMBER(4),
  ATTACHMENT      VARCHAR2(64),
  CANMONITOR      NUMBER(4),
  CREATEID        VARCHAR2(64),
  CREATEDATE      DATE,
  UPDATEID        VARCHAR2(64),
  UPDATEDATE      DATE,
  USER_ORGID      VARCHAR2(64),
  USER_UNITID     VARCHAR2(64),
  PROJECTID       VARCHAR2(64),
  CUSTOMERID      VARCHAR2(64),
  BILLNO          VARCHAR2(64),
  ISPUBLICDOC     NUMBER(4),
  CANCELREASON    VARCHAR2(500),
  REAL_BEGINDATE  DATE,
  REAL_ENDDATE    DATE,
  PROGRESS_RATE   NUMBER(10,2),
  MAIN_QUESTION   VARCHAR2(500),
  SUPER_ADVICE    VARCHAR2(500),
  REAL_HOURS      NUMBER(12,2),
  REAL_FEE        NUMBER(12,2),
  PLANID          VARCHAR2(64),
  PLAN_FORCE      NUMBER(4),
  PRODUCTION      VARCHAR2(200),
  PRODUCTION_NUM  NUMBER(10),
  FINISHSTATUS    NUMBER(4),
  ASSESSRESULT    VARCHAR2(64),
  ASSESSREMARK    VARCHAR2(500),
  WINDAGE         NUMBER(12,2),
  PARENT_TASKID   VARCHAR2(64),
  LAST_REPORTDATE DATE,
  INDEXCODE       VARCHAR2(64)
)
tablespace HR30
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
alter table APP_TASK
  add constraint APP_TASK_PK11191999924171 primary key (SEQKEY)
  using index 
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table APP_TASK_DO
prompt ==========================
prompt
create table APP_TASK_DO
(
  SEQKEY        VARCHAR2(64) not null,
  TASKID        VARCHAR2(64),
  FINISHSTATUS  NUMBER(6),
  PROGRESS_RATE NUMBER(6,2),
  DESCRIPTION   VARCHAR2(500),
  ATTACHMENT    VARCHAR2(200),
  CREATEID      VARCHAR2(64),
  CREATEDATE    DATE,
  UPDATEID      VARCHAR2(64),
  UPDATEDATE    DATE,
  USER_ORGID    VARCHAR2(64),
  USER_UNITID   VARCHAR2(64),
  STATUS        NUMBER(4),
  REPORTDATE    DATE,
  TASKSTATUS    NUMBER(4),
  REALHOURS     NUMBER(12,2)
)
tablespace HR30
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
alter table APP_TASK_DO
  add primary key (SEQKEY)
  using index 
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table APP_TEMPLATE_ATTACHMENT
prompt ======================================
prompt
create table APP_TEMPLATE_ATTACHMENT
(
  SEQKEY      VARCHAR2(64) not null,
  FILENAME    VARCHAR2(200),
  DESCRIPTION VARCHAR2(200),
  FILESIZE    NUMBER(10),
  FILEPATH    VARCHAR2(200),
  FILETYPE    VARCHAR2(64),
  CREATEID    VARCHAR2(64),
  CREATEDATE  DATE,
  USER_ORGID  VARCHAR2(64),
  USER_UNITID VARCHAR2(64),
  LINKMODULE  VARCHAR2(200),
  LINKID      VARCHAR2(64),
  ISPUBLIC    NUMBER(4),
  STATUS      NUMBER(4)
)
tablespace HR30
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
alter table APP_TEMPLATE_ATTACHMENT
  add primary key (SEQKEY)
  using index 
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table APP_TEMPLATE_HISTORY
prompt ===================================
prompt
create table APP_TEMPLATE_HISTORY
(
  SEQKEY      VARCHAR2(64) not null,
  MODELNAME   VARCHAR2(200),
  LINKID      VARCHAR2(64),
  CONTENT     VARCHAR2(2000),
  CREATEID    VARCHAR2(64),
  CREATEDATE  DATE,
  UPDATEID    VARCHAR2(64),
  UPDATEDATE  DATE,
  USER_UNITID VARCHAR2(64),
  USER_ORGID  VARCHAR2(64)
)
tablespace HR30
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
alter table APP_TEMPLATE_HISTORY
  add primary key (SEQKEY)
  using index 
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table APP_UMS_CONFIG
prompt =============================
prompt
create table APP_UMS_CONFIG
(
  USERID   VARCHAR2(64) not null,
  REALTIME NUMBER(4),
  INTERVAL NUMBER(4)
)
tablespace HR30
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
alter table APP_UMS_CONFIG
  add primary key (USERID)
  using index 
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table APP_UMS_MESSAGE
prompt ==============================
prompt
create table APP_UMS_MESSAGE
(
  SEQKEY      VARCHAR2(32) not null,
  MESSAGETYPE NUMBER(10),
  TOPICNAME   VARCHAR2(100),
  SENDDATE    DATE,
  SERIALNO    VARCHAR2(50),
  SRCAPP      VARCHAR2(30),
  SRCMODULE   VARCHAR2(50),
  SENDER      VARCHAR2(30),
  DESTAPP     VARCHAR2(30),
  DESTMODULE  VARCHAR2(50),
  DESTTYPE    NUMBER(10),
  DESTADDR    VARCHAR2(100),
  SCHEMETIME  DATE,
  OVERTIME    DATE,
  TIMES       NUMBER(10),
  ACK         NUMBER(10),
  ACTIONURL   VARCHAR2(100),
  CONTENT     VARCHAR2(1000),
  STATUS      NUMBER(10),
  RECEIVER    VARCHAR2(50),
  CREATEID    VARCHAR2(32),
  CREATEDATE  DATE,
  UPDATEID    VARCHAR2(32),
  UPDATEDATE  DATE,
  USER_ORGID  VARCHAR2(32),
  USER_UNITID VARCHAR2(32),
  TITLE       VARCHAR2(200)
)
tablespace HR30
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
alter table APP_UMS_MESSAGE
  add primary key (SEQKEY)
  using index 
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table APP_WORK_PERSON
prompt ==============================
prompt
create table APP_WORK_PERSON
(
  ORGCODE     VARCHAR2(64),
  PSNNUM      VARCHAR2(64),
  PERIODTYPE  VARCHAR2(64),
  NAME        VARCHAR2(64),
  MAIN_RENDER VARCHAR2(64),
  SUB_RENDER  VARCHAR2(200),
  SEQKEY      VARCHAR2(64) not null,
  CREATEID    VARCHAR2(64),
  CREATEDATE  DATE,
  UPDATEID    VARCHAR2(64),
  UPDATEDATE  DATE,
  USER_ORGID  VARCHAR2(64),
  USER_UNITID VARCHAR2(64)
)
tablespace HR30
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
alter table APP_WORK_PERSON
  add primary key (SEQKEY)
  using index 
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table CD_EDUCATION
prompt ===========================
prompt
create table CD_EDUCATION
(
  CODE_VALUE CHAR(2) not null,
  CODE_NAME  VARCHAR2(100) not null
)
tablespace USERS
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
alter table CD_EDUCATION
  add constraint PK_CD_EDUCATION primary key (CODE_VALUE)
  using index 
  tablespace USERS
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table CD_SEX
prompt =====================
prompt
create table CD_SEX
(
  CODE_VALUE CHAR(2) not null,
  CODE_NAME  VARCHAR2(100) not null
)
tablespace USERS
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
alter table CD_SEX
  add constraint PK_CD_SEX primary key (CODE_VALUE)
  using index 
  tablespace USERS
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table CMS_CONTENT
prompt ==========================
prompt
create table CMS_CONTENT
(
  SEQKEY        VARCHAR2(32) not null,
  NAME          VARCHAR2(50),
  DESCRIPTION   VARCHAR2(100),
  PARENTID      VARCHAR2(32),
  TITLE         VARCHAR2(100),
  CONTENT       CLOB,
  CREATEID      VARCHAR2(50),
  CREATEDATE    DATE,
  UPDATEID      VARCHAR2(50),
  UPDATEDATE    DATE,
  STATE         NUMBER(4),
  CMSTYPE       NUMBER(4),
  AUTHORMODULE  VARCHAR2(50),
  DISPLAYMODULE VARCHAR2(50),
  SITENAME      VARCHAR2(50),
  USER_ORGID    VARCHAR2(32),
  USER_UNITID   VARCHAR2(32)
)
tablespace HR30
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
alter table CMS_CONTENT
  add primary key (SEQKEY)
  using index 
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table CMS_RIGHT
prompt ========================
prompt
create table CMS_RIGHT
(
  SEQKEY       VARCHAR2(32) not null,
  CONTENTID    VARCHAR2(32),
  ACCESSTYPE   NUMBER(4),
  ORGCODE      VARCHAR2(32),
  CHILDREN     VARCHAR2(1000),
  PSNNUMS      VARCHAR2(1000),
  CHILDRENFLAG NUMBER(4),
  BEGINDATE    DATE,
  ENDDATE      DATE,
  RIGHTES      VARCHAR2(32),
  CREATEID     VARCHAR2(32),
  CREATEDATE   DATE,
  UPDATEID     VARCHAR2(32),
  UPDATEDATE   DATE,
  USER_ORGID   VARCHAR2(32),
  USER_UNITID  VARCHAR2(32),
  ISCOPY       NUMBER(4)
)
tablespace HR30
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
alter table CMS_RIGHT
  add primary key (SEQKEY)
  using index 
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table JFW_BASE_CUSTOMCONFIG
prompt ====================================
prompt
create table JFW_BASE_CUSTOMCONFIG
(
  SEQKEY           VARCHAR2(32) not null,
  MODULENAME       VARCHAR2(50),
  MODELNAME        VARCHAR2(50),
  USERID           VARCHAR2(50),
  ORDERBY_FIELD    VARCHAR2(50),
  ORDERBY_MODE     NUMBER(4),
  SUMMARY_STYLE    VARCHAR2(20),
  DISPLAY_ROWS     NUMBER(4),
  CREATEID         VARCHAR2(32),
  CREATEDATE       DATE,
  UPDATEID         VARCHAR2(32),
  UPDATEDATE       DATE,
  USER_ORGID       VARCHAR2(32),
  USER_UNITID      VARCHAR2(32),
  CONDITION_FIELDS VARCHAR2(200)
)
tablespace HR30
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
alter table JFW_BASE_CUSTOMCONFIG
  add primary key (SEQKEY)
  using index 
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table JFW_BASE_CUSTOMFIELD
prompt ===================================
prompt
create table JFW_BASE_CUSTOMFIELD
(
  SEQKEY         VARCHAR2(32) not null,
  CUSTOMDEFINEID VARCHAR2(32),
  FIELDNAME      VARCHAR2(50),
  DISPLAY_ORDER  NUMBER(4),
  SEARCHFLAG     NUMBER(4),
  DISPLAY_FORMAT VARCHAR2(50),
  CREATEID       VARCHAR2(32),
  CREATEDATE     DATE,
  UPDATEID       VARCHAR2(32),
  UPDATEDATE     DATE,
  USER_ORGID     VARCHAR2(32),
  USER_UNITID    VARCHAR2(32),
  ALIGNMENT      NUMBER(4),
  DISPLAY_WIDTH  NUMBER(10),
  MODELNAME      VARCHAR2(200),
  DISPLAYNAME    VARCHAR2(100)
)
tablespace HR30
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
alter table JFW_BASE_CUSTOMFIELD
  add primary key (SEQKEY)
  using index 
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table JFW_BASE_CUSTOMORDER
prompt ===================================
prompt
create table JFW_BASE_CUSTOMORDER
(
  SEQKEY         VARCHAR2(64) not null,
  CUSTOMDEFINEID VARCHAR2(64),
  FIELDNAME      VARCHAR2(100),
  DISPLAYNAME    VARCHAR2(100),
  ASC_DESC       VARCHAR2(64),
  DISPLAY_ORDER  NUMBER(4),
  MODELNAME      VARCHAR2(64)
)
tablespace HR30
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
alter table JFW_BASE_CUSTOMORDER
  add primary key (SEQKEY)
  using index 
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table JFW_BASE_LINKDEFINE
prompt ==================================
prompt
create table JFW_BASE_LINKDEFINE
(
  SEQKEY       VARCHAR2(16) not null,
  MODELNAME    VARCHAR2(50),
  LINKMODULE   VARCHAR2(50),
  LINKURL      VARCHAR2(200),
  GROUPNAME    VARCHAR2(50),
  WINDOW       VARCHAR2(20),
  DISPLAYORDER NUMBER(10),
  DESCRIPTION  VARCHAR2(50),
  TABLENAME    VARCHAR2(50),
  SRCFIELDS    VARCHAR2(100),
  DESTFIELDS   VARCHAR2(100),
  CREATEID     VARCHAR2(32),
  CREATEDATE   DATE,
  UPDATEID     VARCHAR2(32),
  UPDATEDATE   DATE,
  USER_ORGID   VARCHAR2(32),
  USER_UNITID  VARCHAR2(32),
  UUM_RIGHT_ID VARCHAR2(64)
)
tablespace HR30
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table JFW_BASE_NOTION
prompt ==============================
prompt
create table JFW_BASE_NOTION
(
  SEQKEY      VARCHAR2(32) not null,
  INSTANCEID  VARCHAR2(32),
  WORKITEMID  VARCHAR2(32),
  ACTIONNAME  VARCHAR2(100),
  ACTIONDO    VARCHAR2(50),
  REMARK      VARCHAR2(200),
  CREATEID    VARCHAR2(32),
  CREATEDATE  DATE,
  UPDATEID    VARCHAR2(32),
  UPDATEDATE  DATE,
  USER_ORGID  VARCHAR2(32),
  USER_UNITID VARCHAR2(32)
)
tablespace HR30
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
alter table JFW_BASE_NOTION
  add primary key (SEQKEY)
  using index 
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table JFW_BASE_QSEARCH
prompt ===============================
prompt
create table JFW_BASE_QSEARCH
(
  MODULENAME   VARCHAR2(50) not null,
  USERID       VARCHAR2(30),
  QSEARCHNAME  VARCHAR2(100) not null,
  ORDERBYMODE  VARCHAR2(20),
  ORDERBYNAME  VARCHAR2(40),
  CONDITION    VARCHAR2(200),
  ISLOOPSHARED NUMBER(2),
  SEQKEY       VARCHAR2(32) not null,
  CREATEID     VARCHAR2(32),
  CREATEDATE   DATE,
  UPDATEID     VARCHAR2(32),
  UPDATEDATE   DATE,
  USER_ORGID   VARCHAR2(32),
  USER_UNITID  VARCHAR2(32)
)
tablespace HR30
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
alter table JFW_BASE_QSEARCH
  add constraint JFW_BASE_QSEA_PK21169297598532 primary key (SEQKEY)
  using index 
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table JFW_BASE_SPECIALDAY
prompt ==================================
prompt
create table JFW_BASE_SPECIALDAY
(
  SEQKEY      VARCHAR2(32) not null,
  PERIODYEAR  VARCHAR2(10),
  SPECIALDATE DATE,
  DESCRIPTION VARCHAR2(100),
  SPECIALTYPE NUMBER(4),
  CREATEID    VARCHAR2(32),
  CREATEDATE  DATE,
  UPDATEID    VARCHAR2(32),
  UPDATEDATE  DATE,
  USER_ORGID  VARCHAR2(32),
  USER_UNITID VARCHAR2(32)
)
tablespace HR30
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
alter table JFW_BASE_SPECIALDAY
  add primary key (SEQKEY)
  using index 
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table JFW_HELP
prompt =======================
prompt
create table JFW_HELP
(
  SEQKEY     NUMBER,
  MODULENAME VARCHAR2(50),
  TITLE      VARCHAR2(100),
  LINKFILE   VARCHAR2(100),
  CONTENT    BLOB
)
tablespace HR30
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table JFW_OWAY_ASSOCIATION
prompt ===================================
prompt
create table JFW_OWAY_ASSOCIATION
(
  AT_URI        VARCHAR2(1000) not null,
  PKG_URI       VARCHAR2(500),
  NAME          VARCHAR2(100),
  DESCRIPTION   VARCHAR2(100),
  A_ELEMENT     VARCHAR2(500),
  A_NAME        VARCHAR2(100),
  A_MUL         NUMBER(4),
  B_ELEMENT     VARCHAR2(500),
  B_NAME        VARCHAR2(100),
  B_MUL         NUMBER(4),
  DISPLAYORDER  NUMBER(10),
  A_DISPLAYNAME VARCHAR2(500),
  B_DISPLAYNAME VARCHAR2(500)
)
tablespace HR30
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
alter table JFW_OWAY_ASSOCIATION
  add primary key (AT_URI)
  using index 
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table JFW_OWAY_ATDATA
prompt ==============================
prompt
create table JFW_OWAY_ATDATA
(
  AT_URI   VARCHAR2(1000) not null,
  OBJ_A_ID VARCHAR2(64) not null,
  OBJ_B_ID VARCHAR2(64) not null
)
tablespace HR30
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
alter table JFW_OWAY_ATDATA
  add constraint JFW_OWAY_ATDA_PK31184228047028 primary key (AT_URI, OBJ_A_ID, OBJ_B_ID)
  using index 
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table JFW_OWAY_BAK
prompt ===========================
prompt
create table JFW_OWAY_BAK
(
  OBJ_ID  VARCHAR2(64) not null,
  CLS_URI VARCHAR2(500) not null,
  S_01    VARCHAR2(2000),
  S_02    VARCHAR2(2000),
  S_03    VARCHAR2(2000),
  S_04    VARCHAR2(2000),
  S_05    VARCHAR2(2000),
  S_06    VARCHAR2(2000),
  S_07    VARCHAR2(2000),
  S_08    VARCHAR2(2000),
  S_09    VARCHAR2(2000),
  S_10    VARCHAR2(2000),
  S_11    VARCHAR2(2000),
  S_12    VARCHAR2(2000),
  S_13    VARCHAR2(2000),
  S_14    VARCHAR2(2000),
  S_15    VARCHAR2(2000),
  S_16    VARCHAR2(2000),
  S_17    VARCHAR2(2000),
  S_18    VARCHAR2(2000),
  S_19    VARCHAR2(2000),
  S_20    VARCHAR2(2000),
  S_21    VARCHAR2(2000),
  S_22    VARCHAR2(2000),
  S_23    VARCHAR2(2000),
  S_24    VARCHAR2(2000),
  S_25    VARCHAR2(2000),
  S_26    VARCHAR2(2000),
  S_27    VARCHAR2(2000),
  S_28    VARCHAR2(2000),
  S_29    VARCHAR2(2000),
  S_30    VARCHAR2(2000),
  S_31    VARCHAR2(2000),
  S_32    VARCHAR2(2000),
  S_33    VARCHAR2(2000),
  S_34    VARCHAR2(2000),
  S_35    VARCHAR2(2000),
  S_36    VARCHAR2(2000),
  S_37    VARCHAR2(2000),
  S_38    VARCHAR2(2000),
  S_39    VARCHAR2(2000),
  S_40    VARCHAR2(2000),
  S_41    VARCHAR2(2000),
  S_42    VARCHAR2(2000),
  S_43    VARCHAR2(2000),
  S_44    VARCHAR2(2000),
  S_45    VARCHAR2(2000),
  S_46    VARCHAR2(2000),
  S_47    VARCHAR2(2000),
  S_48    VARCHAR2(2000),
  S_49    VARCHAR2(2000),
  S_50    VARCHAR2(2000),
  S_51    VARCHAR2(2000),
  S_52    VARCHAR2(2000),
  S_53    VARCHAR2(2000),
  S_54    VARCHAR2(2000),
  S_55    VARCHAR2(2000),
  S_56    VARCHAR2(2000),
  S_57    VARCHAR2(2000),
  S_58    VARCHAR2(2000),
  S_59    VARCHAR2(2000),
  S_60    VARCHAR2(2000),
  N_01    NUMBER(20,6),
  N_02    NUMBER(20,6),
  N_03    NUMBER(20,6),
  N_04    NUMBER(20,6),
  N_05    NUMBER(20,6),
  N_06    NUMBER(20,6),
  N_07    NUMBER(20,6),
  N_08    NUMBER(20,6),
  N_09    NUMBER(20,6),
  N_10    NUMBER(20,6),
  N_11    NUMBER(20,6),
  N_12    NUMBER(20,6),
  N_13    NUMBER(20,6),
  N_14    NUMBER(20,6),
  N_15    NUMBER(20,6),
  N_16    NUMBER(20,6),
  N_17    NUMBER(20,6),
  N_18    NUMBER(20,6),
  N_19    NUMBER(20,6),
  N_20    NUMBER(20,6),
  N_21    NUMBER(20,6),
  N_22    NUMBER(20,6),
  N_23    NUMBER(20,6),
  N_24    NUMBER(20,6),
  N_25    NUMBER(20,6),
  N_26    NUMBER(20,6),
  N_27    NUMBER(20,6),
  N_28    NUMBER(20,6),
  N_29    NUMBER(20,6),
  N_30    NUMBER(20,6),
  N_31    NUMBER(20,6),
  N_32    NUMBER(20,6),
  N_33    NUMBER(20,6),
  N_34    NUMBER(20,6),
  N_35    NUMBER(20,6),
  N_36    NUMBER(20,6),
  N_37    NUMBER(20,6),
  N_38    NUMBER(20,6),
  N_39    NUMBER(20,6),
  N_40    NUMBER(20,6),
  D_01    DATE,
  D_02    DATE,
  D_03    DATE,
  D_04    DATE,
  D_05    DATE,
  D_06    DATE,
  D_07    DATE,
  D_08    DATE,
  D_09    DATE,
  D_10    DATE,
  D_11    DATE,
  D_12    DATE,
  D_13    DATE,
  D_14    DATE,
  D_15    DATE,
  D_16    DATE,
  D_17    DATE,
  D_18    DATE,
  D_19    DATE,
  D_20    DATE,
  O_01    VARCHAR2(64),
  O_02    VARCHAR2(64),
  O_03    VARCHAR2(64),
  O_04    VARCHAR2(64),
  O_05    VARCHAR2(64),
  O_06    VARCHAR2(64),
  O_07    VARCHAR2(64),
  O_08    VARCHAR2(64),
  O_09    VARCHAR2(64),
  O_10    VARCHAR2(64),
  O_11    VARCHAR2(64),
  O_12    VARCHAR2(64),
  O_13    VARCHAR2(64),
  O_14    VARCHAR2(64),
  O_15    VARCHAR2(64),
  O_16    VARCHAR2(64),
  O_17    VARCHAR2(64),
  O_18    VARCHAR2(64),
  O_19    VARCHAR2(64),
  O_20    VARCHAR2(64),
  S_61    VARCHAR2(2000),
  S_62    VARCHAR2(2000),
  S_63    VARCHAR2(2000),
  S_64    VARCHAR2(2000),
  S_65    VARCHAR2(2000),
  S_66    VARCHAR2(2000),
  S_67    VARCHAR2(2000),
  S_68    VARCHAR2(2000),
  S_69    VARCHAR2(2000),
  S_70    VARCHAR2(2000),
  S_71    VARCHAR2(2000),
  S_72    VARCHAR2(2000),
  S_73    VARCHAR2(2000),
  S_74    VARCHAR2(2000),
  S_75    VARCHAR2(2000),
  S_76    VARCHAR2(2000),
  S_77    VARCHAR2(2000),
  S_78    VARCHAR2(2000),
  S_79    VARCHAR2(2000),
  S_80    VARCHAR2(2000),
  S_81    VARCHAR2(2000),
  S_82    VARCHAR2(2000),
  S_83    VARCHAR2(2000),
  S_84    VARCHAR2(2000),
  S_85    VARCHAR2(2000),
  S_86    VARCHAR2(2000),
  S_87    VARCHAR2(2000),
  S_88    VARCHAR2(2000),
  S_89    VARCHAR2(2000),
  S_90    VARCHAR2(2000)
)
tablespace HR30
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
alter table JFW_OWAY_BAK
  add primary key (OBJ_ID)
  using index 
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table JFW_OWAY_BEHAVIOR
prompt ================================
prompt
create table JFW_OWAY_BEHAVIOR
(
  SEQKEY       VARCHAR2(64) not null,
  CLAZZ        VARCHAR2(64),
  URL          VARCHAR2(500),
  NAME         VARCHAR2(100),
  DESCRIPTION  VARCHAR2(200),
  GROUP_NAME   VARCHAR2(200),
  DISPLAYORDER NUMBER(4),
  CONDITION    VARCHAR2(500),
  GROUP_ID     VARCHAR2(64),
  CREATEID     VARCHAR2(64),
  CREATEDATE   DATE,
  UPDATEID     VARCHAR2(64),
  UPDATEDATE   DATE,
  USER_ORGID   VARCHAR2(64),
  USER_UNITID  VARCHAR2(64),
  URL_OR_JS    NUMBER(4)
)
tablespace HR30
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
alter table JFW_OWAY_BEHAVIOR
  add constraint JFW_OWAY_BEHA_PK11211527237218 primary key (SEQKEY)
  using index 
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table JFW_OWAY_CAT
prompt ===========================
prompt
create table JFW_OWAY_CAT
(
  CAT_ID      VARCHAR2(64) not null,
  DISPLAYNAME VARCHAR2(200),
  CAT_INDEX   NUMBER(10),
  CLS_URI     VARCHAR2(500) not null
)
tablespace HR30
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
alter table JFW_OWAY_CAT
  add constraint JFW_OWAY_CAT_PK61198457356234 primary key (CAT_ID, CLS_URI)
  using index 
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table JFW_OWAY_CLOBBLOB
prompt ================================
prompt
create table JFW_OWAY_CLOBBLOB
(
  SEQKEY   VARCHAR2(64) not null,
  CLOBDATA CLOB,
  BLOBDATA BLOB
)
tablespace HR30
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
alter table JFW_OWAY_CLOBBLOB
  add primary key (SEQKEY)
  using index 
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table JFW_OWAY_DATA
prompt ============================
prompt
create table JFW_OWAY_DATA
(
  OBJ_ID  VARCHAR2(64) not null,
  CLS_URI VARCHAR2(500) not null,
  S_01    VARCHAR2(2000),
  S_02    VARCHAR2(2000),
  S_03    VARCHAR2(2000),
  S_04    VARCHAR2(2000),
  S_05    VARCHAR2(2000),
  S_06    VARCHAR2(2000),
  S_07    VARCHAR2(2000),
  S_08    VARCHAR2(2000),
  S_09    VARCHAR2(2000),
  S_10    VARCHAR2(2000),
  S_11    VARCHAR2(2000),
  S_12    VARCHAR2(2000),
  S_13    VARCHAR2(2000),
  S_14    VARCHAR2(2000),
  S_15    VARCHAR2(2000),
  S_16    VARCHAR2(2000),
  S_17    VARCHAR2(2000),
  S_18    VARCHAR2(2000),
  S_19    VARCHAR2(2000),
  S_20    VARCHAR2(2000),
  S_21    VARCHAR2(2000),
  S_22    VARCHAR2(2000),
  S_23    VARCHAR2(2000),
  S_24    VARCHAR2(2000),
  S_25    VARCHAR2(2000),
  S_26    VARCHAR2(2000),
  S_27    VARCHAR2(2000),
  S_28    VARCHAR2(2000),
  S_29    VARCHAR2(2000),
  S_30    VARCHAR2(2000),
  S_31    VARCHAR2(2000),
  S_32    VARCHAR2(2000),
  S_33    VARCHAR2(2000),
  S_34    VARCHAR2(2000),
  S_35    VARCHAR2(2000),
  S_36    VARCHAR2(2000),
  S_37    VARCHAR2(2000),
  S_38    VARCHAR2(2000),
  S_39    VARCHAR2(2000),
  S_40    VARCHAR2(2000),
  S_41    VARCHAR2(2000),
  S_42    VARCHAR2(2000),
  S_43    VARCHAR2(2000),
  S_44    VARCHAR2(2000),
  S_45    VARCHAR2(2000),
  S_46    VARCHAR2(2000),
  S_47    VARCHAR2(2000),
  S_48    VARCHAR2(2000),
  S_49    VARCHAR2(2000),
  S_50    VARCHAR2(2000),
  S_51    VARCHAR2(2000),
  S_52    VARCHAR2(2000),
  S_53    VARCHAR2(2000),
  S_54    VARCHAR2(2000),
  S_55    VARCHAR2(2000),
  S_56    VARCHAR2(2000),
  S_57    VARCHAR2(2000),
  S_58    VARCHAR2(2000),
  S_59    VARCHAR2(2000),
  S_60    VARCHAR2(2000),
  N_01    NUMBER(20,6),
  N_02    NUMBER(20,6),
  N_03    NUMBER(20,6),
  N_04    NUMBER(20,6),
  N_05    NUMBER(20,6),
  N_06    NUMBER(20,6),
  N_07    NUMBER(20,6),
  N_08    NUMBER(20,6),
  N_09    NUMBER(20,6),
  N_10    NUMBER(20,6),
  N_11    NUMBER(20,6),
  N_12    NUMBER(20,6),
  N_13    NUMBER(20,6),
  N_14    NUMBER(20,6),
  N_15    NUMBER(20,6),
  N_16    NUMBER(20,6),
  N_17    NUMBER(20,6),
  N_18    NUMBER(20,6),
  N_19    NUMBER(20,6),
  N_20    NUMBER(20,6),
  N_21    NUMBER(20,6),
  N_22    NUMBER(20,6),
  N_23    NUMBER(20,6),
  N_24    NUMBER(20,6),
  N_25    NUMBER(20,6),
  N_26    NUMBER(20,6),
  N_27    NUMBER(20,6),
  N_28    NUMBER(20,6),
  N_29    NUMBER(20,6),
  N_30    NUMBER(20,6),
  N_31    NUMBER(20,6),
  N_32    NUMBER(20,6),
  N_33    NUMBER(20,6),
  N_34    NUMBER(20,6),
  N_35    NUMBER(20,6),
  N_36    NUMBER(20,6),
  N_37    NUMBER(20,6),
  N_38    NUMBER(20,6),
  N_39    NUMBER(20,6),
  N_40    NUMBER(20,6),
  D_01    DATE,
  D_02    DATE,
  D_03    DATE,
  D_04    DATE,
  D_05    DATE,
  D_06    DATE,
  D_07    DATE,
  D_08    DATE,
  D_09    DATE,
  D_10    DATE,
  D_11    DATE,
  D_12    DATE,
  D_13    DATE,
  D_14    DATE,
  D_15    DATE,
  D_16    DATE,
  D_17    DATE,
  D_18    DATE,
  D_19    DATE,
  D_20    DATE,
  O_01    VARCHAR2(64),
  O_02    VARCHAR2(64),
  O_03    VARCHAR2(64),
  O_04    VARCHAR2(64),
  O_05    VARCHAR2(64),
  O_06    VARCHAR2(64),
  O_07    VARCHAR2(64),
  O_08    VARCHAR2(64),
  O_09    VARCHAR2(64),
  O_10    VARCHAR2(64),
  O_11    VARCHAR2(64),
  O_12    VARCHAR2(64),
  O_13    VARCHAR2(64),
  O_14    VARCHAR2(64),
  O_15    VARCHAR2(64),
  O_16    VARCHAR2(64),
  O_17    VARCHAR2(64),
  O_18    VARCHAR2(64),
  O_19    VARCHAR2(64),
  O_20    VARCHAR2(64),
  S_61    VARCHAR2(2000),
  S_62    VARCHAR2(2000),
  S_63    VARCHAR2(2000),
  S_64    VARCHAR2(2000),
  S_65    VARCHAR2(2000),
  S_66    VARCHAR2(2000),
  S_67    VARCHAR2(2000),
  S_68    VARCHAR2(2000),
  S_69    VARCHAR2(2000),
  S_70    VARCHAR2(2000),
  S_71    VARCHAR2(2000),
  S_72    VARCHAR2(2000),
  S_73    VARCHAR2(2000),
  S_74    VARCHAR2(2000),
  S_75    VARCHAR2(2000),
  S_76    VARCHAR2(2000),
  S_77    VARCHAR2(2000),
  S_78    VARCHAR2(2000),
  S_79    VARCHAR2(2000),
  S_80    VARCHAR2(2000),
  S_81    VARCHAR2(2000),
  S_82    VARCHAR2(2000),
  S_83    VARCHAR2(2000),
  S_84    VARCHAR2(2000),
  S_85    VARCHAR2(2000),
  S_86    VARCHAR2(2000),
  S_87    VARCHAR2(2000),
  S_88    VARCHAR2(2000),
  S_89    VARCHAR2(2000),
  S_90    VARCHAR2(2000),
  N_41    NUMBER(20,4),
  N_42    NUMBER(20,4),
  N_43    NUMBER(20,4),
  N_44    NUMBER(20,4),
  N_45    NUMBER(20,4),
  N_46    NUMBER(20,4),
  N_47    NUMBER(20,4),
  N_48    NUMBER(20,4),
  N_49    NUMBER(20,4),
  N_50    NUMBER(20,4),
  N_51    NUMBER(20,4),
  N_52    NUMBER(20,4),
  N_53    NUMBER(20,4),
  N_54    NUMBER(20,4),
  N_55    NUMBER(20,4),
  N_56    NUMBER(20,4),
  N_57    NUMBER(20,4),
  N_58    NUMBER(20,4),
  N_59    NUMBER(20,4),
  N_60    NUMBER(20,4),
  N_61    NUMBER(20,4),
  N_62    NUMBER(20,4),
  N_63    NUMBER(20,4),
  N_64    NUMBER(20,4),
  N_65    NUMBER(20,4),
  N_66    NUMBER(20,4),
  N_67    NUMBER(20,4),
  N_68    NUMBER(20,4),
  N_69    NUMBER(20,4),
  N_70    NUMBER(20,4),
  N_71    NUMBER(20,4),
  N_72    NUMBER(20,4),
  N_73    NUMBER(20,4),
  N_74    NUMBER(20,4),
  N_75    NUMBER(20,4),
  N_76    NUMBER(20,4),
  N_77    NUMBER(20,4),
  N_78    NUMBER(20,4),
  N_79    NUMBER(20,4),
  N_80    NUMBER(20,4),
  N_81    NUMBER(20,4),
  N_82    NUMBER(20,4),
  N_83    NUMBER(20,4),
  N_84    NUMBER(20,4),
  N_85    NUMBER(20,4),
  N_86    NUMBER(20,4),
  N_87    NUMBER(20,4),
  N_89    NUMBER(20,4)
)
tablespace HR30
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
alter table JFW_OWAY_DATA
  add primary key (OBJ_ID)
  using index 
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index JFW_OWAY_DATA_CLSURI on JFW_OWAY_DATA (CLS_URI)
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index JFW_OWAY_DATA_D_1 on JFW_OWAY_DATA (D_01)
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index JFW_OWAY_DATA_D_10 on JFW_OWAY_DATA (D_10)
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index JFW_OWAY_DATA_D_11 on JFW_OWAY_DATA (D_11)
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index JFW_OWAY_DATA_D_12 on JFW_OWAY_DATA (D_12)
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index JFW_OWAY_DATA_D_13 on JFW_OWAY_DATA (D_13)
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index JFW_OWAY_DATA_D_14 on JFW_OWAY_DATA (D_14)
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index JFW_OWAY_DATA_D_15 on JFW_OWAY_DATA (D_15)
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index JFW_OWAY_DATA_D_16 on JFW_OWAY_DATA (D_16)
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index JFW_OWAY_DATA_D_17 on JFW_OWAY_DATA (D_17)
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index JFW_OWAY_DATA_D_18 on JFW_OWAY_DATA (D_18)
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index JFW_OWAY_DATA_D_19 on JFW_OWAY_DATA (D_19)
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index JFW_OWAY_DATA_D_2 on JFW_OWAY_DATA (D_02)
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index JFW_OWAY_DATA_D_20 on JFW_OWAY_DATA (D_20)
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index JFW_OWAY_DATA_D_3 on JFW_OWAY_DATA (D_03)
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index JFW_OWAY_DATA_D_4 on JFW_OWAY_DATA (D_04)
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index JFW_OWAY_DATA_D_5 on JFW_OWAY_DATA (D_05)
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index JFW_OWAY_DATA_D_6 on JFW_OWAY_DATA (D_06)
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index JFW_OWAY_DATA_D_7 on JFW_OWAY_DATA (D_07)
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index JFW_OWAY_DATA_D_8 on JFW_OWAY_DATA (D_08)
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index JFW_OWAY_DATA_D_9 on JFW_OWAY_DATA (D_09)
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index JFW_OWAY_DATA_N_1 on JFW_OWAY_DATA (N_01)
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index JFW_OWAY_DATA_N_10 on JFW_OWAY_DATA (N_10)
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index JFW_OWAY_DATA_N_11 on JFW_OWAY_DATA (N_11)
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index JFW_OWAY_DATA_N_12 on JFW_OWAY_DATA (N_12)
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index JFW_OWAY_DATA_N_13 on JFW_OWAY_DATA (N_13)
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index JFW_OWAY_DATA_N_14 on JFW_OWAY_DATA (N_14)
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index JFW_OWAY_DATA_N_15 on JFW_OWAY_DATA (N_15)
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index JFW_OWAY_DATA_N_16 on JFW_OWAY_DATA (N_16)
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index JFW_OWAY_DATA_N_17 on JFW_OWAY_DATA (N_17)
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index JFW_OWAY_DATA_N_18 on JFW_OWAY_DATA (N_18)
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index JFW_OWAY_DATA_N_19 on JFW_OWAY_DATA (N_19)
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index JFW_OWAY_DATA_N_2 on JFW_OWAY_DATA (N_02)
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index JFW_OWAY_DATA_N_20 on JFW_OWAY_DATA (N_20)
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index JFW_OWAY_DATA_N_21 on JFW_OWAY_DATA (N_21)
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index JFW_OWAY_DATA_N_22 on JFW_OWAY_DATA (N_22)
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index JFW_OWAY_DATA_N_23 on JFW_OWAY_DATA (N_23)
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index JFW_OWAY_DATA_N_24 on JFW_OWAY_DATA (N_24)
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index JFW_OWAY_DATA_N_25 on JFW_OWAY_DATA (N_25)
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index JFW_OWAY_DATA_N_26 on JFW_OWAY_DATA (N_26)
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index JFW_OWAY_DATA_N_27 on JFW_OWAY_DATA (N_27)
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index JFW_OWAY_DATA_N_28 on JFW_OWAY_DATA (N_28)
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index JFW_OWAY_DATA_N_29 on JFW_OWAY_DATA (N_29)
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index JFW_OWAY_DATA_N_3 on JFW_OWAY_DATA (N_03)
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index JFW_OWAY_DATA_N_30 on JFW_OWAY_DATA (N_30)
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index JFW_OWAY_DATA_N_31 on JFW_OWAY_DATA (N_31)
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index JFW_OWAY_DATA_N_32 on JFW_OWAY_DATA (N_32)
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index JFW_OWAY_DATA_N_33 on JFW_OWAY_DATA (N_33)
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index JFW_OWAY_DATA_N_34 on JFW_OWAY_DATA (N_34)
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index JFW_OWAY_DATA_N_35 on JFW_OWAY_DATA (N_35)
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index JFW_OWAY_DATA_N_36 on JFW_OWAY_DATA (N_36)
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index JFW_OWAY_DATA_N_37 on JFW_OWAY_DATA (N_37)
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index JFW_OWAY_DATA_N_38 on JFW_OWAY_DATA (N_38)
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index JFW_OWAY_DATA_N_39 on JFW_OWAY_DATA (N_39)
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index JFW_OWAY_DATA_N_4 on JFW_OWAY_DATA (N_04)
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index JFW_OWAY_DATA_N_40 on JFW_OWAY_DATA (N_40)
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index JFW_OWAY_DATA_N_5 on JFW_OWAY_DATA (N_05)
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index JFW_OWAY_DATA_N_6 on JFW_OWAY_DATA (N_06)
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index JFW_OWAY_DATA_N_7 on JFW_OWAY_DATA (N_07)
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index JFW_OWAY_DATA_N_8 on JFW_OWAY_DATA (N_08)
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index JFW_OWAY_DATA_N_9 on JFW_OWAY_DATA (N_09)
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index JFW_OWAY_DATA_O_1 on JFW_OWAY_DATA (O_01)
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index JFW_OWAY_DATA_O_10 on JFW_OWAY_DATA (O_10)
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index JFW_OWAY_DATA_O_11 on JFW_OWAY_DATA (O_11)
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index JFW_OWAY_DATA_O_12 on JFW_OWAY_DATA (O_12)
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index JFW_OWAY_DATA_O_13 on JFW_OWAY_DATA (O_13)
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index JFW_OWAY_DATA_O_14 on JFW_OWAY_DATA (O_14)
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index JFW_OWAY_DATA_O_15 on JFW_OWAY_DATA (O_15)
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index JFW_OWAY_DATA_O_16 on JFW_OWAY_DATA (O_16)
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index JFW_OWAY_DATA_O_17 on JFW_OWAY_DATA (O_17)
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index JFW_OWAY_DATA_O_18 on JFW_OWAY_DATA (O_18)
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index JFW_OWAY_DATA_O_19 on JFW_OWAY_DATA (O_19)
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index JFW_OWAY_DATA_O_2 on JFW_OWAY_DATA (O_02)
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index JFW_OWAY_DATA_O_20 on JFW_OWAY_DATA (O_20)
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index JFW_OWAY_DATA_O_3 on JFW_OWAY_DATA (O_03)
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index JFW_OWAY_DATA_O_4 on JFW_OWAY_DATA (O_04)
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index JFW_OWAY_DATA_O_5 on JFW_OWAY_DATA (O_05)
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index JFW_OWAY_DATA_O_6 on JFW_OWAY_DATA (O_06)
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index JFW_OWAY_DATA_O_7 on JFW_OWAY_DATA (O_07)
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index JFW_OWAY_DATA_O_8 on JFW_OWAY_DATA (O_08)
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index JFW_OWAY_DATA_O_9 on JFW_OWAY_DATA (O_09)
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index JFW_OWAY_DATA_S_1 on JFW_OWAY_DATA (S_01)
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index JFW_OWAY_DATA_S_10 on JFW_OWAY_DATA (S_10)
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index JFW_OWAY_DATA_S_11 on JFW_OWAY_DATA (S_11)
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index JFW_OWAY_DATA_S_12 on JFW_OWAY_DATA (S_12)
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index JFW_OWAY_DATA_S_13 on JFW_OWAY_DATA (S_13)
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index JFW_OWAY_DATA_S_14 on JFW_OWAY_DATA (S_14)
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index JFW_OWAY_DATA_S_15 on JFW_OWAY_DATA (S_15)
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index JFW_OWAY_DATA_S_16 on JFW_OWAY_DATA (S_16)
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index JFW_OWAY_DATA_S_17 on JFW_OWAY_DATA (S_17)
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index JFW_OWAY_DATA_S_18 on JFW_OWAY_DATA (S_18)
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index JFW_OWAY_DATA_S_19 on JFW_OWAY_DATA (S_19)
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index JFW_OWAY_DATA_S_2 on JFW_OWAY_DATA (S_02)
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index JFW_OWAY_DATA_S_20 on JFW_OWAY_DATA (S_20)
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index JFW_OWAY_DATA_S_21 on JFW_OWAY_DATA (S_21)
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index JFW_OWAY_DATA_S_22 on JFW_OWAY_DATA (S_22)
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index JFW_OWAY_DATA_S_23 on JFW_OWAY_DATA (S_23)
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index JFW_OWAY_DATA_S_24 on JFW_OWAY_DATA (S_24)
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index JFW_OWAY_DATA_S_25 on JFW_OWAY_DATA (S_25)
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index JFW_OWAY_DATA_S_26 on JFW_OWAY_DATA (S_26)
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index JFW_OWAY_DATA_S_27 on JFW_OWAY_DATA (S_27)
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index JFW_OWAY_DATA_S_28 on JFW_OWAY_DATA (S_28)
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index JFW_OWAY_DATA_S_29 on JFW_OWAY_DATA (S_29)
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index JFW_OWAY_DATA_S_3 on JFW_OWAY_DATA (S_03)
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index JFW_OWAY_DATA_S_30 on JFW_OWAY_DATA (S_30)
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index JFW_OWAY_DATA_S_31 on JFW_OWAY_DATA (S_31)
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index JFW_OWAY_DATA_S_32 on JFW_OWAY_DATA (S_32)
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index JFW_OWAY_DATA_S_33 on JFW_OWAY_DATA (S_33)
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index JFW_OWAY_DATA_S_34 on JFW_OWAY_DATA (S_34)
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index JFW_OWAY_DATA_S_35 on JFW_OWAY_DATA (S_35)
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index JFW_OWAY_DATA_S_36 on JFW_OWAY_DATA (S_36)
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index JFW_OWAY_DATA_S_37 on JFW_OWAY_DATA (S_37)
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index JFW_OWAY_DATA_S_38 on JFW_OWAY_DATA (S_38)
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index JFW_OWAY_DATA_S_39 on JFW_OWAY_DATA (S_39)
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index JFW_OWAY_DATA_S_4 on JFW_OWAY_DATA (S_04)
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index JFW_OWAY_DATA_S_40 on JFW_OWAY_DATA (S_40)
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index JFW_OWAY_DATA_S_41 on JFW_OWAY_DATA (S_41)
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index JFW_OWAY_DATA_S_42 on JFW_OWAY_DATA (S_42)
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index JFW_OWAY_DATA_S_43 on JFW_OWAY_DATA (S_43)
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index JFW_OWAY_DATA_S_44 on JFW_OWAY_DATA (S_44)
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index JFW_OWAY_DATA_S_45 on JFW_OWAY_DATA (S_45)
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index JFW_OWAY_DATA_S_46 on JFW_OWAY_DATA (S_46)
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index JFW_OWAY_DATA_S_47 on JFW_OWAY_DATA (S_47)
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index JFW_OWAY_DATA_S_48 on JFW_OWAY_DATA (S_48)
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index JFW_OWAY_DATA_S_49 on JFW_OWAY_DATA (S_49)
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index JFW_OWAY_DATA_S_5 on JFW_OWAY_DATA (S_05)
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index JFW_OWAY_DATA_S_50 on JFW_OWAY_DATA (S_50)
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index JFW_OWAY_DATA_S_51 on JFW_OWAY_DATA (S_51)
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index JFW_OWAY_DATA_S_52 on JFW_OWAY_DATA (S_52)
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index JFW_OWAY_DATA_S_53 on JFW_OWAY_DATA (S_53)
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index JFW_OWAY_DATA_S_54 on JFW_OWAY_DATA (S_54)
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index JFW_OWAY_DATA_S_55 on JFW_OWAY_DATA (S_55)
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index JFW_OWAY_DATA_S_56 on JFW_OWAY_DATA (S_56)
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index JFW_OWAY_DATA_S_57 on JFW_OWAY_DATA (S_57)
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index JFW_OWAY_DATA_S_58 on JFW_OWAY_DATA (S_58)
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index JFW_OWAY_DATA_S_59 on JFW_OWAY_DATA (S_59)
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index JFW_OWAY_DATA_S_6 on JFW_OWAY_DATA (S_06)
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index JFW_OWAY_DATA_S_60 on JFW_OWAY_DATA (S_60)
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index JFW_OWAY_DATA_S_61 on JFW_OWAY_DATA (S_61)
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index JFW_OWAY_DATA_S_62 on JFW_OWAY_DATA (S_62)
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index JFW_OWAY_DATA_S_63 on JFW_OWAY_DATA (S_63)
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index JFW_OWAY_DATA_S_64 on JFW_OWAY_DATA (S_64)
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index JFW_OWAY_DATA_S_65 on JFW_OWAY_DATA (S_65)
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index JFW_OWAY_DATA_S_66 on JFW_OWAY_DATA (S_66)
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index JFW_OWAY_DATA_S_67 on JFW_OWAY_DATA (S_67)
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index JFW_OWAY_DATA_S_68 on JFW_OWAY_DATA (S_68)
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index JFW_OWAY_DATA_S_69 on JFW_OWAY_DATA (S_69)
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index JFW_OWAY_DATA_S_7 on JFW_OWAY_DATA (S_07)
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index JFW_OWAY_DATA_S_70 on JFW_OWAY_DATA (S_70)
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index JFW_OWAY_DATA_S_71 on JFW_OWAY_DATA (S_71)
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index JFW_OWAY_DATA_S_72 on JFW_OWAY_DATA (S_72)
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index JFW_OWAY_DATA_S_73 on JFW_OWAY_DATA (S_73)
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index JFW_OWAY_DATA_S_74 on JFW_OWAY_DATA (S_74)
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index JFW_OWAY_DATA_S_75 on JFW_OWAY_DATA (S_75)
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index JFW_OWAY_DATA_S_76 on JFW_OWAY_DATA (S_76)
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index JFW_OWAY_DATA_S_77 on JFW_OWAY_DATA (S_77)
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index JFW_OWAY_DATA_S_78 on JFW_OWAY_DATA (S_78)
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index JFW_OWAY_DATA_S_79 on JFW_OWAY_DATA (S_79)
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index JFW_OWAY_DATA_S_8 on JFW_OWAY_DATA (S_08)
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index JFW_OWAY_DATA_S_80 on JFW_OWAY_DATA (S_80)
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index JFW_OWAY_DATA_S_81 on JFW_OWAY_DATA (S_81)
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index JFW_OWAY_DATA_S_82 on JFW_OWAY_DATA (S_82)
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index JFW_OWAY_DATA_S_83 on JFW_OWAY_DATA (S_83)
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index JFW_OWAY_DATA_S_84 on JFW_OWAY_DATA (S_84)
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index JFW_OWAY_DATA_S_85 on JFW_OWAY_DATA (S_85)
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index JFW_OWAY_DATA_S_86 on JFW_OWAY_DATA (S_86)
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index JFW_OWAY_DATA_S_87 on JFW_OWAY_DATA (S_87)
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index JFW_OWAY_DATA_S_88 on JFW_OWAY_DATA (S_88)
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index JFW_OWAY_DATA_S_89 on JFW_OWAY_DATA (S_89)
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index JFW_OWAY_DATA_S_9 on JFW_OWAY_DATA (S_09)
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index JFW_OWAY_DATA_S_90 on JFW_OWAY_DATA (S_90)
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table JFW_OWAY_LOG
prompt ===========================
prompt
create table JFW_OWAY_LOG
(
  SEQKEY      VARCHAR2(64) not null,
  LOGTIME     DATE,
  OBJ_ID      VARCHAR2(64),
  CLS_URI     VARCHAR2(500),
  FIELDNAME   VARCHAR2(100),
  OLD_VALUE   VARCHAR2(4000),
  NEW_VALUE   VARCHAR2(4000),
  ACTION_TYPE VARCHAR2(64),
  ACTION_USER VARCHAR2(64)
)
tablespace HR30
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
alter table JFW_OWAY_LOG
  add primary key (SEQKEY)
  using index 
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table JFW_OWAY_PACKAGE
prompt ===============================
prompt
create table JFW_OWAY_PACKAGE
(
  PKG_URI      VARCHAR2(500) not null,
  NAME         VARCHAR2(200),
  PARENT_URI   VARCHAR2(500),
  DISPLAYNAME  VARCHAR2(100),
  DISPLAYORDER NUMBER(10),
  DESCRIPTION  VARCHAR2(200)
)
tablespace HR30
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
alter table JFW_OWAY_PACKAGE
  add primary key (PKG_URI)
  using index 
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table JFW_OWAY_PROCESS
prompt ===============================
prompt
create table JFW_OWAY_PROCESS
(
  CLS_URI     VARCHAR2(500) not null,
  PROCESSNAME VARCHAR2(100) not null,
  USER_UNITID VARCHAR2(64),
  SEQKEY      VARCHAR2(64) not null
)
tablespace HR30
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
alter table JFW_OWAY_PROCESS
  add primary key (SEQKEY)
  using index 
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index JFW_OWAY_PROCESS_CLSURI on JFW_OWAY_PROCESS (CLS_URI)
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table JFW_SECURITY_ACTION
prompt ==================================
prompt
create table JFW_SECURITY_ACTION
(
  SEQKEY      VARCHAR2(64) not null,
  MODULENAME  VARCHAR2(64),
  ROLEID      VARCHAR2(64),
  ACTIONS     VARCHAR2(100),
  CREATEID    VARCHAR2(64),
  CREATEDATE  DATE,
  UPDATEID    VARCHAR2(64),
  UPDATEDATE  DATE,
  USER_ORGID  VARCHAR2(64),
  USER_UNITID VARCHAR2(64)
)
tablespace HR30
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
alter table JFW_SECURITY_ACTION
  add primary key (SEQKEY)
  using index 
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table JFW_SECURITY_DATAMODEL
prompt =====================================
prompt
create table JFW_SECURITY_DATAMODEL
(
  SEQKEY      VARCHAR2(32) not null,
  ROLEID      VARCHAR2(32),
  MODELNAME   VARCHAR2(50),
  POLICYID    VARCHAR2(50),
  MODULENAME  VARCHAR2(50),
  CREATEID    VARCHAR2(32),
  CREATEDATE  DATE,
  UPDATEID    VARCHAR2(32),
  UPDATEDATE  DATE,
  USER_ORGID  VARCHAR2(32),
  USER_UNITID VARCHAR2(32)
)
tablespace HR30
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
alter table JFW_SECURITY_DATAMODEL
  add primary key (SEQKEY)
  using index 
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table JFW_SECURITY_FIELD
prompt =================================
prompt
create table JFW_SECURITY_FIELD
(
  SEQKEY      VARCHAR2(32) not null,
  ROLEID      VARCHAR2(32),
  MODELNAME   VARCHAR2(50),
  FIELDNAME   VARCHAR2(2000),
  RIGHTLEVEL  NUMBER(10),
  CREATEID    VARCHAR2(32),
  CREATEDATE  DATE,
  UPDATEID    VARCHAR2(32),
  UPDATEDATE  DATE,
  USER_ORGID  VARCHAR2(32),
  USER_UNITID VARCHAR2(32)
)
tablespace HR30
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
alter table JFW_SECURITY_FIELD
  add primary key (SEQKEY)
  using index 
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table JFW_SECURITY_IP
prompt ==============================
prompt
create table JFW_SECURITY_IP
(
  SEQKEY      VARCHAR2(64) not null,
  POLICYID    VARCHAR2(64),
  IPTYPE      NUMBER(4),
  CREATEID    VARCHAR2(64),
  CREATEDATE  DATE,
  UPDATEID    VARCHAR2(64),
  UPDATEDATE  DATE,
  USER_ORGID  VARCHAR2(64),
  USER_UNITID VARCHAR2(64),
  IP_FROM     VARCHAR2(64),
  IP_TO       VARCHAR2(64),
  DESCRIPTION VARCHAR2(100)
)
tablespace HR30
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
alter table JFW_SECURITY_IP
  add primary key (SEQKEY)
  using index 
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table JFW_SECURITY_IP_POLICY
prompt =====================================
prompt
create table JFW_SECURITY_IP_POLICY
(
  SEQKEY      VARCHAR2(64) not null,
  POLICYNAME  VARCHAR2(64),
  DESCRIPTION VARCHAR2(200),
  CREATEID    VARCHAR2(64),
  CREATEDATE  DATE,
  UPDATEID    VARCHAR2(64),
  UPDATEDATE  DATE,
  USER_ORGID  VARCHAR2(64),
  USER_UNITID VARCHAR2(64),
  ISDEFAULT   NUMBER(4)
)
tablespace HR30
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
alter table JFW_SECURITY_IP_POLICY
  add primary key (SEQKEY)
  using index 
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table JFW_SECURITY_POLICY
prompt ==================================
prompt
create table JFW_SECURITY_POLICY
(
  SEQKEY      VARCHAR2(32) not null,
  POLICYNAME  VARCHAR2(200),
  FILTERSQL   VARCHAR2(500),
  DESCRIPTION VARCHAR2(500),
  POLICYLEVEL NUMBER(4),
  CREATEID    VARCHAR2(32),
  CREATEDATE  DATE,
  UPDATEID    VARCHAR2(32),
  UPDATEDATE  DATE,
  USER_ORGID  VARCHAR2(32),
  USER_UNITID VARCHAR2(32),
  APPID       VARCHAR2(64)
)
tablespace HR30
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
alter table JFW_SECURITY_POLICY
  add primary key (SEQKEY)
  using index 
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table JFW_SECURITY_UNITPOLICY
prompt ======================================
prompt
create table JFW_SECURITY_UNITPOLICY
(
  SEQKEY      VARCHAR2(32) not null,
  UNITID      VARCHAR2(32),
  POLICYID    VARCHAR2(32),
  CREATEID    VARCHAR2(32),
  CREATEDATE  DATE,
  UPDATEID    VARCHAR2(32),
  UPDATEDATE  DATE,
  USER_ORGID  VARCHAR2(32),
  USER_UNITID VARCHAR2(32)
)
tablespace HR30
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
alter table JFW_SECURITY_UNITPOLICY
  add primary key (SEQKEY)
  using index 
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table PDM_BASE
prompt =======================
prompt
create table PDM_BASE
(
  SEQKEY      VARCHAR2(32) not null,
  ROOTDIR     VARCHAR2(100),
  ISIMPORT    NUMBER(4),
  IMPORTDATE  DATE,
  ORGCODE     VARCHAR2(32),
  ADMINID     VARCHAR2(32),
  CREATEID    VARCHAR2(32),
  CREATEDATE  DATE,
  UPDATEID    VARCHAR2(32),
  UPDATEDATE  DATE,
  USER_ORGID  VARCHAR2(32),
  USER_UNITID VARCHAR2(32)
)
tablespace HR30
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
alter table PDM_BASE
  add primary key (SEQKEY)
  using index 
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table PDM_FILE
prompt =======================
prompt
create table PDM_FILE
(
  SEQKEY      VARCHAR2(32) not null,
  FILENAME    VARCHAR2(200),
  DESCRIPTION VARCHAR2(200),
  PARENTID    VARCHAR2(100),
  TOPIC       VARCHAR2(200),
  TITLE       VARCHAR2(200),
  PUBLISHDATE DATE,
  PUBLISHER   VARCHAR2(32),
  INVALIDDATE DATE,
  ISPUBLIC    NUMBER(4),
  DOCNO       VARCHAR2(100),
  FILESIZE    NUMBER(10),
  CREATEDATE  DATE,
  CREATEID    VARCHAR2(32),
  UPDATEID    VARCHAR2(32),
  UPDATEDATE  DATE,
  DIRPATH     VARCHAR2(200),
  FILETYPE    VARCHAR2(32),
  ATTACHMENT  VARCHAR2(64),
  USER_ORGID  VARCHAR2(32),
  USER_UNITID VARCHAR2(32)
)
tablespace HR30
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
alter table PDM_FILE
  add primary key (SEQKEY)
  using index 
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table PDM_RIGHT
prompt ========================
prompt
create table PDM_RIGHT
(
  SEQKEY       VARCHAR2(32) not null,
  FILEID       VARCHAR2(32),
  ACCESSTYPE   NUMBER(4),
  ORGCODE      VARCHAR2(32),
  CHILDREN     VARCHAR2(1000),
  PSNNUMS      VARCHAR2(1000),
  CHILDRENFLAG NUMBER(4),
  BEGINDATE    DATE,
  ENDDATE      DATE,
  RIGHTES      VARCHAR2(32),
  CREATEID     VARCHAR2(32),
  CREATEDATE   DATE,
  UPDATEID     VARCHAR2(32),
  UPDATEDATE   DATE,
  USER_ORGID   VARCHAR2(32),
  USER_UNITID  VARCHAR2(32),
  ISCOPY       NUMBER(4)
)
tablespace HR30
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
alter table PDM_RIGHT
  add primary key (SEQKEY)
  using index 
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table PDM_VIRTUALROOT
prompt ==============================
prompt
create table PDM_VIRTUALROOT
(
  SEQKEY       VARCHAR2(32) not null,
  ORGCODE      VARCHAR2(32),
  CHILDREN     VARCHAR2(1000),
  PSNNUMS      VARCHAR2(1000),
  CHILDRENFLAG NUMBER(4),
  ROOTID       VARCHAR2(32),
  ACCESSTYPE   VARCHAR2(32),
  CREATEID     VARCHAR2(32),
  CREATEDATE   DATE,
  UPDATEID     VARCHAR2(32),
  UPDATEDATE   DATE,
  USER_ORGID   VARCHAR2(32),
  USER_UNITID  VARCHAR2(32)
)
tablespace HR30
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
alter table PDM_VIRTUALROOT
  add primary key (SEQKEY)
  using index 
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table T_JFW_APP
prompt ========================
prompt
create table T_JFW_APP
(
  APPID       VARCHAR2(32) not null,
  APPNAME     VARCHAR2(50),
  DESCRIPTION VARCHAR2(50),
  PARENTID    VARCHAR2(32),
  CREATEID    VARCHAR2(10),
  CREATEDATE  DATE,
  UPDATEID    VARCHAR2(10),
  UPDATEDATE  DATE
)
tablespace HR30
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
alter table T_JFW_APP
  add constraint PK_T_JFW_APP_1_1 primary key (APPID)
  using index 
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table T_JFW_CODEMODEL
prompt ==============================
prompt
create table T_JFW_CODEMODEL
(
  MODELNAME       VARCHAR2(64) not null,
  DESCRIPTION     VARCHAR2(64),
  DATASOURCE      VARCHAR2(64),
  CODESOURCEFLAG  VARCHAR2(16),
  TABLENAME       VARCHAR2(32),
  NAMEFIELD       VARCHAR2(32),
  NAMEFILEDTYPE   VARCHAR2(16),
  VALUEFIELD      VARCHAR2(32),
  VALUEFIELDTYPE  VARCHAR2(32),
  PYFIELD         VARCHAR2(32),
  PYFIELDTYPE     VARCHAR2(32),
  CODETYPE        VARCHAR2(16),
  PARENTFIELD     VARCHAR2(32),
  PARENTFIELDTYPE VARCHAR2(16),
  BASEVALUE       VARCHAR2(64),
  LEAFFIELD       VARCHAR2(32),
  LEAFFIELDTYPE   VARCHAR2(16),
  LEAFVALUE       VARCHAR2(16),
  LEVELFIELD      NUMBER,
  TOTALLENGTH     NUMBER,
  LENGTH1         NUMBER,
  LENGTH2         NUMBER,
  LENGTH3         NUMBER,
  LENGTH4         NUMBER,
  LENGTH5         NUMBER,
  MULTISELECT     NUMBER,
  DATAFILTER      VARCHAR2(8),
  DATAMODEL       VARCHAR2(64),
  FILTERSQL       VARCHAR2(256),
  DEFAULTORDER    VARCHAR2(64),
  INFOFIELD       VARCHAR2(64),
  USECACHE        VARCHAR2(8),
  OPERATIONCLASS  VARCHAR2(256),
  ISVALIDATE      VARCHAR2(8),
  SOURCEFLAG      VARCHAR2(8),
  APPID           VARCHAR2(32),
  FUNCTIONMODEL   VARCHAR2(50),
  CREATEID        VARCHAR2(32),
  CREATEDATE      DATE,
  UPDATEID        VARCHAR2(32),
  UPDATEDATE      DATE,
  MULTIVERSION    NUMBER(4),
  HOTSPOT         VARCHAR2(64)
)
tablespace USERS
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
alter table T_JFW_CODEMODEL
  add constraint JFW_CODEMODEL_1 primary key (MODELNAME)
  using index 
  tablespace USERS
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table T_JFW_CODEMODELVERSION
prompt =====================================
prompt
create table T_JFW_CODEMODELVERSION
(
  SEQKEY        VARCHAR2(64) not null,
  MODELNAME     VARCHAR2(100),
  UNITID        VARCHAR2(64),
  CREATEID      VARCHAR2(64),
  CREATEDATE    DATE,
  UPDATEID      VARCHAR2(64),
  UPDATEDATE    DATE,
  ROLEID        VARCHAR2(64),
  BASEVALUE     VARCHAR2(64),
  FILTERSQL     VARCHAR2(200),
  FUNCTIONMODEL VARCHAR2(64),
  POLICYID      VARCHAR2(64),
  USER_UNITID   VARCHAR2(64),
  USER_ORGID    VARCHAR2(64),
  MODULENAME    VARCHAR2(64)
)
tablespace HR30
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
alter table T_JFW_CODEMODELVERSION
  add primary key (SEQKEY)
  using index 
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table T_JFW_CODE_COMPLEX
prompt =================================
prompt
create table T_JFW_CODE_COMPLEX
(
  MODELNAME    VARCHAR2(50),
  SEQKEY       NUMBER(4) not null,
  DESCRIPTION  VARCHAR2(100),
  PARENTCODE   VARCHAR2(30),
  SUBCODE      VARCHAR2(30),
  PARENTFIELD  VARCHAR2(30),
  TABLENAME    VARCHAR2(30),
  VALUEFIELD   VARCHAR2(50),
  NAMEFIELD    VARCHAR2(50),
  INFOFIELD    VARCHAR2(50),
  DEFAULTORDER VARCHAR2(50),
  FILTERSQL    VARCHAR2(100),
  DATASOURCE   VARCHAR2(50),
  ISLEAF       NUMBER(4),
  IMG          VARCHAR2(50),
  BGCOLOR      VARCHAR2(50),
  ISSELECT     NUMBER(4)
)
tablespace HR30
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
alter table T_JFW_CODE_COMPLEX
  add primary key (SEQKEY)
  using index 
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table T_JFW_CODE_DATA
prompt ==============================
prompt
create table T_JFW_CODE_DATA
(
  SEQKEY      NUMBER not null,
  MODELNAME   VARCHAR2(64),
  CODEVALUE   VARCHAR2(32),
  CODENAME    VARCHAR2(64),
  CODEPY      VARCHAR2(64),
  DESCRIPTION VARCHAR2(64),
  CREATEID    VARCHAR2(32),
  CREATEDATE  DATE,
  UPDATEID    VARCHAR2(32),
  UPDATEDATE  DATE
)
tablespace USERS
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
alter table T_JFW_CODE_DATA
  add constraint JFW_CODE_DATA_1 primary key (SEQKEY)
  using index 
  tablespace USERS
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index T_JFW_CODE_DATA_MODELNAME on T_JFW_CODE_DATA (MODELNAME)
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table T_JFW_DATAMODEL
prompt ==============================
prompt
create table T_JFW_DATAMODEL
(
  MODELNAME       VARCHAR2(100) not null,
  TABLENAME       VARCHAR2(100),
  TABLETYPE       VARCHAR2(8),
  DATASOURCE      VARCHAR2(64),
  DESCRIPTION     VARCHAR2(64),
  DEFAULTORDERBY  VARCHAR2(64),
  EVENTLOG        NUMBER(4),
  PARENT_CLS_URI  VARCHAR2(500),
  PROPVALUETABLE  VARCHAR2(100),
  DISPLAYORDER    VARCHAR2(10),
  APPID           VARCHAR2(50),
  DEFAULTPOLICYID VARCHAR2(32),
  CREATEID        VARCHAR2(32),
  CREATEDATE      DATE,
  UPDATEID        VARCHAR2(32),
  UPDATEDATE      DATE,
  DATARIGHTFLAG   NUMBER(4),
  CLS_ID          VARCHAR2(64),
  CLS_URI         VARCHAR2(500) not null,
  PKG_URI         VARCHAR2(500),
  CLS_DOC         VARCHAR2(200),
  VIEW_SQL        VARCHAR2(4000),
  MAP_CLS         VARCHAR2(200),
  INST            VARCHAR2(20),
  BEHAVIOR        VARCHAR2(200),
  MAP_GIS         VARCHAR2(200)
)
tablespace HR30
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
alter table T_JFW_DATAMODEL
  add constraint T_JFW_DATAMOD_PK21184131685489 primary key (CLS_URI)
  using index 
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index T_JFW_DATAMODEL_APPID on T_JFW_DATAMODEL (APPID)
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index T_JFW_DATAMODEL_MODELNAME on T_JFW_DATAMODEL (MODELNAME)
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table T_JFW_FIELDINFO
prompt ==============================
prompt
create table T_JFW_FIELDINFO
(
  MODELNAME        VARCHAR2(500) not null,
  FIELDNAME        VARCHAR2(64) not null,
  FIELDTYPE        VARCHAR2(16),
  DISPLAYNAME      VARCHAR2(64),
  CODETYPE         VARCHAR2(500),
  LENGTH           NUMBER,
  KEYFLAG          VARCHAR2(8),
  GENERATETYPE     VARCHAR2(16),
  COLUMNS          VARCHAR2(8),
  ISEMPTY          VARCHAR2(8),
  DEFAULTVALUE     VARCHAR2(64),
  DISPLAYFORMAT    VARCHAR2(32),
  MAXVALUE         VARCHAR2(126),
  MINVALUE         VARCHAR2(126),
  STATE            VARCHAR2(8),
  DEFAULTCTRL      VARCHAR2(50),
  DISPLAYORDER     NUMBER(10),
  CHECKVALUE       VARCHAR2(32),
  CREATEID         VARCHAR2(32),
  CREATEDATE       DATE,
  UPDATEID         VARCHAR2(32),
  UPDATEDATE       DATE,
  BOFIELDNAME      VARCHAR2(50),
  CANSEARCH        NUMBER(4),
  CLSDRIVER        VARCHAR2(500),
  CAT_ID           VARCHAR2(64),
  CLS_URI          VARCHAR2(500) not null,
  DESCRIPTION      VARCHAR2(200),
  MAP_PROPNAME     VARCHAR2(64),
  MAP_GIS          VARCHAR2(100),
  IS_MAIN          NUMBER(4),
  IS_LOG           NUMBER(4),
  LINKURL          VARCHAR2(100),
  LINKTYPE         VARCHAR2(100),
  MAP_UPSAP        VARCHAR2(100),
  MAP_PROPNAME_SAP VARCHAR2(100),
  MAP_TYPE         VARCHAR2(5),
  UNIT             VARCHAR2(64)
)
tablespace HR30
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
alter table T_JFW_FIELDINFO
  add constraint T_JFW_FIELDIN_PK41184221048334 primary key (CLS_URI, FIELDNAME)
  using index 
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index T_JFW_FIELDINFO_CLSURI on T_JFW_FIELDINFO (CLS_URI)
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index T_JFW_FIELDINFO_MODELNAME on T_JFW_FIELDINFO (MODELNAME)
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table T_JFW_FUNCTION
prompt =============================
prompt
create table T_JFW_FUNCTION
(
  SEQKEY        VARCHAR2(32) not null,
  MODELNAME     VARCHAR2(50),
  DESCRIPTION   VARCHAR2(50),
  FILEPATH      VARCHAR2(100),
  APPID         VARCHAR2(32),
  DATARIGHTFLAG NUMBER(4),
  CUSTOMFLAG    NUMBER(4),
  MULTIVERSION  NUMBER(4),
  CREATEID      VARCHAR2(32),
  CREATEDATE    DATE,
  UPDATEID      VARCHAR2(32),
  UPDATEDATE    DATE,
  LINKFLAG      NUMBER(4),
  IPCONTROL     NUMBER(4),
  IPPOLICYID    VARCHAR2(64)
)
tablespace USERS
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
alter table T_JFW_FUNCTION
  add primary key (SEQKEY)
  using index 
  tablespace USERS
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index T_JFW_FUNCTION_MODELNAME on T_JFW_FUNCTION (MODELNAME)
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table T_JFW_FUNCTIONVERSION
prompt ====================================
prompt
create table T_JFW_FUNCTIONVERSION
(
  SEQKEY     VARCHAR2(32) not null,
  MODELNAME  VARCHAR2(100),
  UNITID     VARCHAR2(32),
  FILEPATH   VARCHAR2(100),
  CREATEID   VARCHAR2(32),
  CREATEDATE DATE,
  UPDATEID   VARCHAR2(32),
  UPDATEDATE DATE
)
tablespace HR30
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
alter table T_JFW_FUNCTIONVERSION
  add primary key (SEQKEY)
  using index 
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table T_JFW_RULE_PARAM
prompt ===============================
prompt
create table T_JFW_RULE_PARAM
(
  ID      VARCHAR2(50) not null,
  SET_ID  VARCHAR2(50),
  RULE_ID VARCHAR2(50),
  PARAM1  VARCHAR2(50),
  PARAM2  VARCHAR2(50),
  PARAM3  VARCHAR2(50),
  PARAM4  VARCHAR2(50),
  PARAM5  VARCHAR2(50),
  PARAM6  VARCHAR2(50),
  PARAM7  VARCHAR2(50),
  PARAM8  VARCHAR2(50),
  PARAM9  VARCHAR2(50),
  PARAM10 VARCHAR2(50),
  PARAM11 VARCHAR2(50),
  PARAM12 VARCHAR2(50),
  PARAM13 VARCHAR2(50),
  PARAM14 VARCHAR2(50),
  PARAM15 VARCHAR2(50),
  PARAM16 VARCHAR2(50),
  PARAM17 VARCHAR2(50),
  PARAM18 VARCHAR2(50),
  PARAM19 VARCHAR2(50),
  PARAM20 VARCHAR2(50),
  PARAM21 VARCHAR2(50),
  PARAM22 VARCHAR2(50),
  PARAM23 VARCHAR2(50),
  PARAM24 VARCHAR2(50),
  PARAM25 VARCHAR2(50),
  PARAM26 VARCHAR2(50),
  PARAM27 VARCHAR2(50),
  PARAM28 VARCHAR2(50),
  PARAM29 VARCHAR2(50),
  PARAM30 VARCHAR2(50)
)
tablespace HR30
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
alter table T_JFW_RULE_PARAM
  add constraint PK_T_JFW_RULE_PARAM primary key (ID)
  using index 
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table T_JFW_RULE_SET
prompt =============================
prompt
create table T_JFW_RULE_SET
(
  UNIT_ID     VARCHAR2(64),
  SET_CODE    VARCHAR2(64) not null,
  SET_NAME    VARCHAR2(100),
  DESCRIPTION VARCHAR2(200),
  CREATEID    VARCHAR2(64),
  CREATEDATE  DATE,
  BEGINDATE   DATE,
  ENDDATE     DATE
)
tablespace HR30
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
alter table T_JFW_RULE_SET
  add constraint PK_T_JFW_RULE_SET primary key (SET_CODE)
  using index 
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table T_JFW_RULE_SET_PARAM
prompt ===================================
prompt
create table T_JFW_RULE_SET_PARAM
(
  SET_CODE     VARCHAR2(64) not null,
  PARAM_CODE   VARCHAR2(100) not null,
  PARAM_NAME   VARCHAR2(100),
  DESCRIPTION  VARCHAR2(200),
  TYPE         INTEGER,
  WIDTH        INTEGER,
  SCALE_WIDTH  INTEGER,
  FORMAT       VARCHAR2(50),
  DEFAULTVALUE VARCHAR2(100),
  DIRECT       NUMBER(4)
)
tablespace HR30
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
alter table T_JFW_RULE_SET_PARAM
  add constraint PK_T_JFW_RULE_SET_PARAM primary key (SET_CODE, PARAM_CODE)
  using index 
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table T_JFW_RULE_SET_RULE
prompt ==================================
prompt
create table T_JFW_RULE_SET_RULE
(
  SET_CODE            VARCHAR2(64) not null,
  RULE_ID             VARCHAR2(64) not null,
  RULE_ORDER          NUMBER,
  CONDITION           VARCHAR2(1000),
  PASS_EXPRESSION     VARCHAR2(1000),
  NOT_PASS_EXPRESSION VARCHAR2(1000),
  RULE_NAME           VARCHAR2(100),
  DESCRIPTION         VARCHAR2(200)
)
tablespace HR30
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
alter table T_JFW_RULE_SET_RULE
  add constraint PK_T_JFW_RULE_SET_RULE_1 primary key (SET_CODE, RULE_ID)
  using index 
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table T_SVG_BUSINESS_MODEL
prompt ===================================
prompt
create table T_SVG_BUSINESS_MODEL
(
  BUSINESS_ID       VARCHAR2(8),
  MODLE_ID          VARCHAR2(128),
  MODULE_NAME       VARCHAR2(128),
  MODLE_ATTRID      VARCHAR2(128),
  IM_MODLE_ATTRNAME VARCHAR2(256),
  BM_ID             VARCHAR2(36) not null
)
tablespace SVG_PLATROOF
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
comment on table T_SVG_BUSINESS_MODEL
  is '业务模型表（映射SVGSERVER中的数据）';
comment on column T_SVG_BUSINESS_MODEL.BUSINESS_ID
  is '业务系统';
comment on column T_SVG_BUSINESS_MODEL.MODLE_ID
  is '业务模型ID';
comment on column T_SVG_BUSINESS_MODEL.MODULE_NAME
  is '业务模型名';
comment on column T_SVG_BUSINESS_MODEL.MODLE_ATTRID
  is '业务模型属性ID';
comment on column T_SVG_BUSINESS_MODEL.IM_MODLE_ATTRNAME
  is '业务模型属性名';
comment on column T_SVG_BUSINESS_MODEL.BM_ID
  is '主键GUID';
alter table T_SVG_BUSINESS_MODEL
  add constraint PK_T_SVG_BUSINESS_MODEL primary key (BM_ID)
  using index 
  tablespace SVG_PLATROOF
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table T_SVG_CODE_COMMSETS
prompt ==================================
prompt
create table T_SVG_CODE_COMMSETS
(
  CC_SHORTNAME   VARCHAR2(32) not null,
  CC_CODE        VARCHAR2(8) not null,
  CC_NAME        VARCHAR2(128),
  CC_SPELL       VARCHAR2(512),
  CC_DESC        VARCHAR2(1024),
  CC_PARENT_CODE VARCHAR2(8),
  CC_PARAM1      VARCHAR2(1024),
  CC_PARAM2      VARCHAR2(1024),
  CC_PARAM3      VARCHAR2(1024),
  CC_PARAM4      VARCHAR2(1024),
  CC_PARAM5      VARCHAR2(1024),
  CC_ID          VARCHAR2(36) not null
)
tablespace SVG_PLATROOF
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
comment on table T_SVG_CODE_COMMSETS
  is '代码表';
comment on column T_SVG_CODE_COMMSETS.CC_SHORTNAME
  is '代码类型简称';
comment on column T_SVG_CODE_COMMSETS.CC_CODE
  is '代码值';
comment on column T_SVG_CODE_COMMSETS.CC_NAME
  is '代码名称';
comment on column T_SVG_CODE_COMMSETS.CC_SPELL
  is '代码拼音';
comment on column T_SVG_CODE_COMMSETS.CC_DESC
  is '代码描述';
comment on column T_SVG_CODE_COMMSETS.CC_PARENT_CODE
  is '父类型代码值';
comment on column T_SVG_CODE_COMMSETS.CC_PARAM1
  is '预留1';
comment on column T_SVG_CODE_COMMSETS.CC_PARAM2
  is '预留2';
comment on column T_SVG_CODE_COMMSETS.CC_PARAM3
  is '预留3';
comment on column T_SVG_CODE_COMMSETS.CC_PARAM4
  is '预留4';
comment on column T_SVG_CODE_COMMSETS.CC_PARAM5
  is '预留5';
comment on column T_SVG_CODE_COMMSETS.CC_ID
  is '唯一标识';
alter table T_SVG_CODE_COMMSETS
  add constraint PK_T_SVG_CODE_COMMSETS primary key (CC_SHORTNAME, CC_CODE)
  using index 
  tablespace SVG_PLATROOF
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table T_SVG_CODE_MANAGER
prompt =================================
prompt
create table T_SVG_CODE_MANAGER
(
  CM_SHORTNAME       VARCHAR2(32) not null,
  CM_NAME            VARCHAR2(64),
  CM_MODIFY_TIME     VARCHAR2(14),
  CM_PARENTSHORTNAME VARCHAR2(32),
  CM_ISNOT_DEL       VARCHAR2(8)
)
tablespace SVG_PLATROOF
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
comment on table T_SVG_CODE_MANAGER
  is '代码类型表';
comment on column T_SVG_CODE_MANAGER.CM_SHORTNAME
  is '代码类型简称';
comment on column T_SVG_CODE_MANAGER.CM_NAME
  is '代码类型名称';
comment on column T_SVG_CODE_MANAGER.CM_MODIFY_TIME
  is '最后更新时间';
comment on column T_SVG_CODE_MANAGER.CM_PARENTSHORTNAME
  is '父代码类型简称';
comment on column T_SVG_CODE_MANAGER.CM_ISNOT_DEL
  is '不可删除标志';
alter table T_SVG_CODE_MANAGER
  add constraint PK_T_SVG_CODE_MANAGER primary key (CM_SHORTNAME)
  using index 
  tablespace SVG_PLATROOF
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table T_SVG_INDUNORM_DESC
prompt ==================================
prompt
create table T_SVG_INDUNORM_DESC
(
  ID_ID         VARCHAR2(36) not null,
  ID_SHORT_NAME VARCHAR2(32),
  ID_NAME       VARCHAR2(64),
  ID_VARIETY    VARCHAR2(32),
  ID_DESC       VARCHAR2(1024),
  ID_VALIDITY   VARCHAR2(8)
)
tablespace SVG_PLATROOF
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
comment on table T_SVG_INDUNORM_DESC
  is '行业规范文件描述信息表';
comment on column T_SVG_INDUNORM_DESC.ID_ID
  is '编号';
comment on column T_SVG_INDUNORM_DESC.ID_SHORT_NAME
  is '简称';
comment on column T_SVG_INDUNORM_DESC.ID_NAME
  is '名称';
comment on column T_SVG_INDUNORM_DESC.ID_VARIETY
  is '规范种类';
comment on column T_SVG_INDUNORM_DESC.ID_DESC
  is '描述';
comment on column T_SVG_INDUNORM_DESC.ID_VALIDITY
  is '有效标记';
alter table T_SVG_INDUNORM_DESC
  add constraint PK_T_SVG_INDUNORM_DESC primary key (ID_ID)
  using index 
  tablespace SVG_PLATROOF
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table T_SVG_INDUNORM_DESCRELA
prompt ======================================
prompt
create table T_SVG_INDUNORM_DESCRELA
(
  ID_ID                  VARCHAR2(36) not null,
  ID_BUSINESSSYS_ID      VARCHAR2(8),
  ID_GRAPH_BUSINESS_TYPE VARCHAR2(8),
  ID_NORM_DESC_ID        VARCHAR2(36),
  ID_VALIDITY            VARCHAR2(8)
)
tablespace SVG_PLATROOF
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
comment on table T_SVG_INDUNORM_DESCRELA
  is '图类型与规范描述信息关联表';
comment on column T_SVG_INDUNORM_DESCRELA.ID_ID
  is '图类型与规范关联记录编号';
comment on column T_SVG_INDUNORM_DESCRELA.ID_BUSINESSSYS_ID
  is '业务系统编号';
comment on column T_SVG_INDUNORM_DESCRELA.ID_GRAPH_BUSINESS_TYPE
  is '图业务类型';
comment on column T_SVG_INDUNORM_DESCRELA.ID_NORM_DESC_ID
  is '描述信息编号(相当于规范表中的规范简称)';
comment on column T_SVG_INDUNORM_DESCRELA.ID_VALIDITY
  is '有效标记';
alter table T_SVG_INDUNORM_DESCRELA
  add constraint PK_T_SVG_INDUNORM_DESCRELA primary key (ID_ID)
  using index 
  tablespace SVG_PLATROOF
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table T_SVG_INDUNORM_FIELDS
prompt ====================================
prompt
create table T_SVG_INDUNORM_FIELDS
(
  IF_ID         VARCHAR2(36) not null,
  IF_SHORT_NAME VARCHAR2(32),
  IF_NAME       VARCHAR2(64),
  IF_DESC       VARCHAR2(1024),
  IF_VARIETY    VARCHAR2(36),
  IF_VALIDITY   VARCHAR2(8)
)
tablespace SVG_PLATROOF
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
comment on table T_SVG_INDUNORM_FIELDS
  is '规范数据域字段表';
comment on column T_SVG_INDUNORM_FIELDS.IF_ID
  is '编号';
comment on column T_SVG_INDUNORM_FIELDS.IF_SHORT_NAME
  is '字段简称';
comment on column T_SVG_INDUNORM_FIELDS.IF_NAME
  is '字段名称';
comment on column T_SVG_INDUNORM_FIELDS.IF_DESC
  is '字段描述';
comment on column T_SVG_INDUNORM_FIELDS.IF_VARIETY
  is '数据域种类';
comment on column T_SVG_INDUNORM_FIELDS.IF_VALIDITY
  is '有效标记';
alter table T_SVG_INDUNORM_FIELDS
  add constraint PK_T_SVG_INDUNORM_FIELDS primary key (IF_ID)
  using index 
  tablespace SVG_PLATROOF
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table T_SVG_INDUNORM_GRAPHTYPERELA
prompt ===========================================
prompt
create table T_SVG_INDUNORM_GRAPHTYPERELA
(
  IG_ID                  VARCHAR2(36) not null,
  IG_BUSINESSSYS_ID      VARCHAR2(8),
  IG_GRAPH_BUSINESS_TYPE VARCHAR2(8),
  IG_NORM_TYPE           VARCHAR2(32),
  IG_VALIDITY            VARCHAR2(8),
  IG_DEFS                BLOB
)
tablespace SVG_PLATROOF
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
comment on table T_SVG_INDUNORM_GRAPHTYPERELA
  is '图类型与规范关联表';
comment on column T_SVG_INDUNORM_GRAPHTYPERELA.IG_ID
  is '图类型与规范关联记录编号';
comment on column T_SVG_INDUNORM_GRAPHTYPERELA.IG_BUSINESSSYS_ID
  is '业务系统编号';
comment on column T_SVG_INDUNORM_GRAPHTYPERELA.IG_GRAPH_BUSINESS_TYPE
  is '图业务类型';
comment on column T_SVG_INDUNORM_GRAPHTYPERELA.IG_NORM_TYPE
  is '规范种类';
comment on column T_SVG_INDUNORM_GRAPHTYPERELA.IG_VALIDITY
  is '有效标记';
comment on column T_SVG_INDUNORM_GRAPHTYPERELA.IG_DEFS
  is '该业务类型对应的定义包';
alter table T_SVG_INDUNORM_GRAPHTYPERELA
  add constraint PK_T_SVG_INDUNORM_GRAPHTYPER primary key (IG_ID)
  using index 
  tablespace SVG_PLATROOF
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table T_SVG_INDUNORM_METADATAS
prompt =======================================
prompt
create table T_SVG_INDUNORM_METADATAS
(
  IM_ID         VARCHAR2(36) not null,
  IM_SHORT_NAME VARCHAR2(32),
  IM_NAME       VARCHAR2(64),
  IM_VARIETY    VARCHAR2(32),
  IM_DESC       VARCHAR2(1024),
  IM_VALIDITY   VARCHAR2(8)
)
tablespace SVG_PLATROOF
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
comment on table T_SVG_INDUNORM_METADATAS
  is '规范数据域种类表';
comment on column T_SVG_INDUNORM_METADATAS.IM_ID
  is '编号';
comment on column T_SVG_INDUNORM_METADATAS.IM_SHORT_NAME
  is '种类简称';
comment on column T_SVG_INDUNORM_METADATAS.IM_NAME
  is '种类名称';
comment on column T_SVG_INDUNORM_METADATAS.IM_VARIETY
  is '规范种类';
comment on column T_SVG_INDUNORM_METADATAS.IM_DESC
  is '描述';
comment on column T_SVG_INDUNORM_METADATAS.IM_VALIDITY
  is '有效标记';
alter table T_SVG_INDUNORM_METADATAS
  add constraint PK_T_SVG_INDUNORM_METADATAS primary key (IM_ID)
  using index 
  tablespace SVG_PLATROOF
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table T_SVG_INDUNORM_MODELRELA
prompt =======================================
prompt
create table T_SVG_INDUNORM_MODELRELA
(
  IM_ID             VARCHAR2(36) not null,
  IM_MODLE_ID       VARCHAR2(128),
  IM_MODLE_ATTRNAME VARCHAR2(256),
  IM_NORM_TYPE      VARCHAR2(8),
  IM_NORM_ID        VARCHAR2(36),
  IM_VALIDITY       VARCHAR2(8),
  IM_MODULE_NAME    VARCHAR2(128),
  IM_MODLE_ATTRID   VARCHAR2(128),
  IM_BUSS_ID        VARCHAR2(8)
)
tablespace SVG_PLATROOF
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
comment on table T_SVG_INDUNORM_MODELRELA
  is '模型与规范对应表';
comment on column T_SVG_INDUNORM_MODELRELA.IM_ID
  is '对应关系编号';
comment on column T_SVG_INDUNORM_MODELRELA.IM_MODLE_ID
  is '模型编号';
comment on column T_SVG_INDUNORM_MODELRELA.IM_MODLE_ATTRNAME
  is '模型属性名';
comment on column T_SVG_INDUNORM_MODELRELA.IM_NORM_TYPE
  is '规范类型(描述信息、数据域信息)对应t_svg_code_commsets中cc_shortname=SVG_NORM_TYPE';
comment on column T_SVG_INDUNORM_MODELRELA.IM_NORM_ID
  is '规范编号，当IM_NORM_TYPE等于1时，等于T_SVG_INDUNORM_DESC中的ID_ID，当IM_NORM_TYPE等于2时，等于T_SVG_INDUNORM_FIELDS中的IF_ID';
comment on column T_SVG_INDUNORM_MODELRELA.IM_VALIDITY
  is '有效标记';
comment on column T_SVG_INDUNORM_MODELRELA.IM_MODULE_NAME
  is '模型名称';
comment on column T_SVG_INDUNORM_MODELRELA.IM_MODLE_ATTRID
  is '模型属性编号';
comment on column T_SVG_INDUNORM_MODELRELA.IM_BUSS_ID
  is '业务系统编号';
alter table T_SVG_INDUNORM_MODELRELA
  add constraint PK_T_SVG_INDUNORM_MODELRELA primary key (IM_ID)
  using index 
  tablespace SVG_PLATROOF
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table T_SVG_INDUNORM_SYMBOLFIELDRE
prompt ===========================================
prompt
create table T_SVG_INDUNORM_SYMBOLFIELDRE
(
  IS_ID           VARCHAR2(36) not null,
  IS_SYMBOL_TYPE  VARCHAR2(8),
  IS_SYMBOL_ID    VARCHAR2(36),
  IS_DATAFIELD_ID VARCHAR2(36),
  IS_VALIDITY     VARCHAR2(8)
)
tablespace SVG_PLATROOF
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
comment on table T_SVG_INDUNORM_SYMBOLFIELDRE
  is '图元与规范数据域信息关联表';
comment on column T_SVG_INDUNORM_SYMBOLFIELDRE.IS_ID
  is '图元与规范数据域信息关联记录编号';
comment on column T_SVG_INDUNORM_SYMBOLFIELDRE.IS_SYMBOL_TYPE
  is 'SYMBOL类型(图元、模板)';
comment on column T_SVG_INDUNORM_SYMBOLFIELDRE.IS_SYMBOL_ID
  is '图元编号';
comment on column T_SVG_INDUNORM_SYMBOLFIELDRE.IS_DATAFIELD_ID
  is '数据域字段编号';
comment on column T_SVG_INDUNORM_SYMBOLFIELDRE.IS_VALIDITY
  is '有效标记';
alter table T_SVG_INDUNORM_SYMBOLFIELDRE
  add constraint PK_T_SVG_INDUNORM_SYMBOLFIEL primary key (IS_ID)
  using index 
  tablespace SVG_PLATROOF
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table T_SVG_INDUNORM_TYPE
prompt ==================================
prompt
create table T_SVG_INDUNORM_TYPE
(
  IT_SHORTNAME VARCHAR2(32) not null,
  IT_NAME      VARCHAR2(128),
  IT_QUOTE     VARCHAR2(128),
  IT_DESC      VARCHAR2(1024)
)
tablespace SVG_PLATROOF
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
comment on table T_SVG_INDUNORM_TYPE
  is '行业规范种类表';
comment on column T_SVG_INDUNORM_TYPE.IT_SHORTNAME
  is '规范简称(如:PSR)';
comment on column T_SVG_INDUNORM_TYPE.IT_NAME
  is '规范名称';
comment on column T_SVG_INDUNORM_TYPE.IT_QUOTE
  is '规范引用(如:www.cim.com)';
comment on column T_SVG_INDUNORM_TYPE.IT_DESC
  is '规范描述';
alter table T_SVG_INDUNORM_TYPE
  add constraint PK_T_SVG_INDUNORM_TYPE primary key (IT_SHORTNAME)
  using index 
  tablespace SVG_PLATROOF
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table T_SVG_LOG_OPERATION
prompt ==================================
prompt
create table T_SVG_LOG_OPERATION
(
  LO_ID          VARCHAR2(36) not null,
  LO_OPER_PERSON VARCHAR2(64),
  LO_OPER_TIME   VARCHAR2(14),
  LO_OPER_TYPE   VARCHAR2(8),
  LO_OPER_DESC   VARCHAR2(4000)
)
tablespace SVG_PLATROOF
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
comment on table T_SVG_LOG_OPERATION
  is '业务日志';
comment on column T_SVG_LOG_OPERATION.LO_ID
  is '日志记录编号';
comment on column T_SVG_LOG_OPERATION.LO_OPER_PERSON
  is '操作人';
comment on column T_SVG_LOG_OPERATION.LO_OPER_TIME
  is '操作时间';
comment on column T_SVG_LOG_OPERATION.LO_OPER_TYPE
  is '操作类型';
comment on column T_SVG_LOG_OPERATION.LO_OPER_DESC
  is '操作描述(不包含操作结果)';
alter table T_SVG_LOG_OPERATION
  add constraint PK_T_SVG_LOG_OPERATION primary key (LO_ID)
  using index 
  tablespace SVG_PLATROOF
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table T_SVG_MODEL_ACTIONRELASYS
prompt ========================================
prompt
create table T_SVG_MODEL_ACTIONRELASYS
(
  AS_ID         VARCHAR2(36) not null,
  AS_ACTIONID   VARCHAR2(36) not null,
  AS_BUSINESSID VARCHAR2(8) not null,
  AS_CONTENT    VARCHAR2(4000)
)
tablespace SVG_PLATROOF
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
comment on column T_SVG_MODEL_ACTIONRELASYS.AS_ID
  is '编号';
comment on column T_SVG_MODEL_ACTIONRELASYS.AS_ACTIONID
  is '动作编号';
comment on column T_SVG_MODEL_ACTIONRELASYS.AS_BUSINESSID
  is '业务系统编号';
comment on column T_SVG_MODEL_ACTIONRELASYS.AS_CONTENT
  is '动作内容';

prompt
prompt Creating table T_SVG_MODEL_ACTIONS
prompt ==================================
prompt
create table T_SVG_MODEL_ACTIONS
(
  MA_ID         VARCHAR2(36) not null,
  MA_MODELID    VARCHAR2(36) not null,
  MA_TYPE       VARCHAR2(8) not null,
  MA_SHORTNAME  VARCHAR2(64) not null,
  MA_NAME       VARCHAR2(256) not null,
  MA_VALIDITY   VARCHAR2(8) default 1 not null,
  MA_MODIFYTIME VARCHAR2(14),
  MA_CONTENT    VARCHAR2(4000),
  MA_POS        NUMBER(4) not null
)
tablespace SVG_PLATROOF
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
comment on column T_SVG_MODEL_ACTIONS.MA_ID
  is '编号';
comment on column T_SVG_MODEL_ACTIONS.MA_MODELID
  is '模型编号，对应T_SVG_MODEL_SUMMARY.MS_ID';
comment on column T_SVG_MODEL_ACTIONS.MA_TYPE
  is '动作类型';
comment on column T_SVG_MODEL_ACTIONS.MA_SHORTNAME
  is '动作短名';
comment on column T_SVG_MODEL_ACTIONS.MA_NAME
  is '动作名称';
comment on column T_SVG_MODEL_ACTIONS.MA_VALIDITY
  is '有效标记';
comment on column T_SVG_MODEL_ACTIONS.MA_MODIFYTIME
  is '最后更新时间';
comment on column T_SVG_MODEL_ACTIONS.MA_CONTENT
  is '动作内容';
comment on column T_SVG_MODEL_ACTIONS.MA_POS
  is '动作位置';

prompt
prompt Creating table T_SVG_MODEL_BUSINESSRELA
prompt =======================================
prompt
create table T_SVG_MODEL_BUSINESSRELA
(
  MB_ID         VARCHAR2(128) not null,
  MB_MODELID    VARCHAR2(150) not null,
  MB_BUSINESSID VARCHAR2(100) not null,
  MB_VALIDITY   VARCHAR2(100),
  MB_MODIFYTIME VARCHAR2(100)
)
tablespace SVG_PLATROOF
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
comment on column T_SVG_MODEL_BUSINESSRELA.MB_ID
  is '编号';
comment on column T_SVG_MODEL_BUSINESSRELA.MB_MODELID
  is '模型编号，对应T_SVG_MODEL_SUMMARY.MS_ID';
comment on column T_SVG_MODEL_BUSINESSRELA.MB_BUSINESSID
  is '所属业务系统编号';
comment on column T_SVG_MODEL_BUSINESSRELA.MB_VALIDITY
  is '有效标记';
comment on column T_SVG_MODEL_BUSINESSRELA.MB_MODIFYTIME
  is '最后更新时间';
alter table T_SVG_MODEL_BUSINESSRELA
  add constraint PK_T_SVG_MODEL_BUSINESSRELA primary key (MB_ID)
  using index 
  tablespace SVG_PLATROOF
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table T_SVG_MODEL_INDUNORMRELA
prompt =======================================
prompt
create table T_SVG_MODEL_INDUNORMRELA
(
  MI_ID         VARCHAR2(36) not null,
  MI_MODELID    VARCHAR2(36) not null,
  MI_PROPERTYID VARCHAR2(36) not null,
  MI_INDUNORMID VARCHAR2(32) not null,
  MI_DESCID     VARCHAR2(36),
  MI_METADATAID VARCHAR2(36),
  MI_FIELDID    VARCHAR2(36),
  MI_BUSSID     VARCHAR2(8) not null,
  MI_CODE       VARCHAR2(4000)
)
tablespace SVG_PLATROOF
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
comment on column T_SVG_MODEL_INDUNORMRELA.MI_ID
  is '编号';
comment on column T_SVG_MODEL_INDUNORMRELA.MI_MODELID
  is '模型编号';
comment on column T_SVG_MODEL_INDUNORMRELA.MI_PROPERTYID
  is '模型属性编号';
comment on column T_SVG_MODEL_INDUNORMRELA.MI_INDUNORMID
  is '规范编号';
comment on column T_SVG_MODEL_INDUNORMRELA.MI_DESCID
  is '描述编号';
comment on column T_SVG_MODEL_INDUNORMRELA.MI_METADATAID
  is '数据域编号';
comment on column T_SVG_MODEL_INDUNORMRELA.MI_FIELDID
  is '数据字段编号';
comment on column T_SVG_MODEL_INDUNORMRELA.MI_BUSSID
  is '业务系统编号';
comment on column T_SVG_MODEL_INDUNORMRELA.MI_CODE
  is '属性数据源';

prompt
prompt Creating table T_SVG_MODEL_PROPERTYS
prompt ====================================
prompt
create table T_SVG_MODEL_PROPERTYS
(
  MP_ID         VARCHAR2(36) not null,
  MP_MODELID    VARCHAR2(36) not null,
  MP_SHORTNAME  VARCHAR2(64) not null,
  MP_NAME       VARCHAR2(256) not null,
  MP_TYPE       VARCHAR2(8) not null,
  MP_CODE       VARCHAR2(4000),
  MP_VISIBLE    VARCHAR2(8),
  MP_VALIDITY   VARCHAR2(8),
  MP_MODIFYTIME VARCHAR2(14),
  MP_POS        NUMBER(4)
)
tablespace SVG_PLATROOF
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
comment on column T_SVG_MODEL_PROPERTYS.MP_ID
  is '编号';
comment on column T_SVG_MODEL_PROPERTYS.MP_MODELID
  is '模型编号，对应T_SVG_MODEL_SUMMARY.MS_ID';
comment on column T_SVG_MODEL_PROPERTYS.MP_SHORTNAME
  is '属性短名';
comment on column T_SVG_MODEL_PROPERTYS.MP_NAME
  is '属性名称';
comment on column T_SVG_MODEL_PROPERTYS.MP_TYPE
  is '属性种类，（唯一标识，基础属性，扩展属性，状态…）';
comment on column T_SVG_MODEL_PROPERTYS.MP_CODE
  is '如存在选择项，设置代码标识';
comment on column T_SVG_MODEL_PROPERTYS.MP_VISIBLE
  is '显示标记';
comment on column T_SVG_MODEL_PROPERTYS.MP_VALIDITY
  is '有效标记';
comment on column T_SVG_MODEL_PROPERTYS.MP_MODIFYTIME
  is '最后更新时间';
comment on column T_SVG_MODEL_PROPERTYS.MP_POS
  is '属性位置';

prompt
prompt Creating table T_SVG_MODEL_SUMMARY
prompt ==================================
prompt
create table T_SVG_MODEL_SUMMARY
(
  MS_ID         VARCHAR2(36) not null,
  MS_TYPE       VARCHAR2(8) not null,
  MS_SHORTNAME  VARCHAR2(64) not null,
  MS_NAME       VARCHAR2(256) not null,
  MS_VALIDITY   VARCHAR2(8),
  MS_MODIFYTIME VARCHAR2(14),
  MS_PARENTID   VARCHAR2(36),
  MS_SOURCE     VARCHAR2(36)
)
tablespace SVG_PLATROOF
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
comment on column T_SVG_MODEL_SUMMARY.MS_ID
  is '模型编号';
comment on column T_SVG_MODEL_SUMMARY.MS_TYPE
  is '模型类型(自定义模型还是业务系统模型)';
comment on column T_SVG_MODEL_SUMMARY.MS_SHORTNAME
  is '模型短名';
comment on column T_SVG_MODEL_SUMMARY.MS_NAME
  is '模型名称';
comment on column T_SVG_MODEL_SUMMARY.MS_VALIDITY
  is '有效标记';
comment on column T_SVG_MODEL_SUMMARY.MS_MODIFYTIME
  is '最后更新时间';
comment on column T_SVG_MODEL_SUMMARY.MS_PARENTID
  is '父模型编号';
comment on column T_SVG_MODEL_SUMMARY.MS_SOURCE
  is '来源';
alter table T_SVG_MODEL_SUMMARY
  add constraint PK_MODELS_ID primary key (MS_ID)
  using index 
  tablespace SVG_PLATROOF
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table T_SVG_MODULE_GRAPHRELA
prompt =====================================
prompt
create table T_SVG_MODULE_GRAPHRELA
(
  MG_ID                  VARCHAR2(36) not null,
  MG_BUSINESSID          VARCHAR2(8),
  MG_GRAPH_BUSINESS_TYPE VARCHAR2(8),
  MG_CANVASOPER          VARCHAR2(256),
  MG_VALIDITY            VARCHAR2(8)
)
tablespace SVG_PLATROOF
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
alter table T_SVG_MODULE_GRAPHRELA
  add constraint PK_MG_ID primary key (MG_ID)
  using index 
  tablespace SVG_PLATROOF
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table T_SVG_MODULE_HIS
prompt ===============================
prompt
create table T_SVG_MODULE_HIS
(
  MH_ID         VARCHAR2(36) not null,
  MH_SHORT_NAME VARCHAR2(32),
  MH_CONTENT    BLOB,
  MH_EDITION    VARCHAR2(16)
)
tablespace SVG_PLATROOF
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
comment on table T_SVG_MODULE_HIS
  is '组件历史表';
comment on column T_SVG_MODULE_HIS.MH_ID
  is '编号';
comment on column T_SVG_MODULE_HIS.MH_SHORT_NAME
  is '组件简称';
comment on column T_SVG_MODULE_HIS.MH_CONTENT
  is '组件内容';
comment on column T_SVG_MODULE_HIS.MH_EDITION
  is '版本号';
alter table T_SVG_MODULE_HIS
  add constraint PK_T_SVG_MODULE_HIS primary key (MH_ID)
  using index 
  tablespace SVG_PLATROOF
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table T_SVG_MODULE_LIST
prompt ================================
prompt
create table T_SVG_MODULE_LIST
(
  ML_SHORTNAME     VARCHAR2(32) not null,
  ML_NAME          VARCHAR2(64),
  ML_LOCATION      VARCHAR2(8),
  ML_KIND          VARCHAR2(8),
  ML_TYPE          VARCHAR2(8),
  ML_MODIFY_PERSON VARCHAR2(64),
  ML_CONTENT       BLOB,
  ML_EDITION       VARCHAR2(16),
  ML_LOG_LEVEL     VARCHAR2(8),
  ML_LOG_TYPE      VARCHAR2(8),
  ML_ISNOT_DEL     VARCHAR2(8),
  ML_CLASS_NAME    VARCHAR2(256)
)
tablespace SVG_PLATROOF
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
comment on table T_SVG_MODULE_LIST
  is '组件清单表';
comment on column T_SVG_MODULE_LIST.ML_SHORTNAME
  is '组件简称';
comment on column T_SVG_MODULE_LIST.ML_NAME
  is '组件名称';
comment on column T_SVG_MODULE_LIST.ML_LOCATION
  is '部署位置';
comment on column T_SVG_MODULE_LIST.ML_KIND
  is '组件大类';
comment on column T_SVG_MODULE_LIST.ML_TYPE
  is '组件类型';
comment on column T_SVG_MODULE_LIST.ML_MODIFY_PERSON
  is '最新更新人';
comment on column T_SVG_MODULE_LIST.ML_CONTENT
  is '组件内容';
comment on column T_SVG_MODULE_LIST.ML_EDITION
  is '版本号';
comment on column T_SVG_MODULE_LIST.ML_LOG_LEVEL
  is '日志等级';
comment on column T_SVG_MODULE_LIST.ML_LOG_TYPE
  is '日志类型';
comment on column T_SVG_MODULE_LIST.ML_ISNOT_DEL
  is '不可删除标志';
comment on column T_SVG_MODULE_LIST.ML_CLASS_NAME
  is '启动类名';
alter table T_SVG_MODULE_LIST
  add constraint PK_T_SVG_MODULE_LIST primary key (ML_SHORTNAME)
  using index 
  tablespace SVG_PLATROOF
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table T_SVG_MODULE_MODERELA
prompt ====================================
prompt
create table T_SVG_MODULE_MODERELA
(
  MM_ID         VARCHAR2(36) not null,
  MM_SHORT_NAME VARCHAR2(32),
  MM_MODE_NAME  VARCHAR2(8),
  MM_VALIDITY   VARCHAR2(8),
  MM_ISNOT_DEL  VARCHAR2(8)
)
tablespace SVG_PLATROOF
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
comment on column T_SVG_MODULE_MODERELA.MM_SHORT_NAME
  is '业务服务组件短名';
comment on column T_SVG_MODULE_MODERELA.MM_MODE_NAME
  is '运行模式编号';
comment on column T_SVG_MODULE_MODERELA.MM_VALIDITY
  is '有效标记';
comment on column T_SVG_MODULE_MODERELA.MM_ISNOT_DEL
  is '不可删除标志';
alter table T_SVG_MODULE_MODERELA
  add constraint PK_MM_ID primary key (MM_ID)
  using index 
  tablespace SVG_PLATROOF
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table T_SVG_MODULE_SERVICE
prompt ===================================
prompt
create table T_SVG_MODULE_SERVICE
(
  MS_ID             VARCHAR2(36) not null,
  MS_SHORT_NAME     VARCHAR2(32),
  MS_SERVICE_NAME   VARCHAR2(32),
  MS_VALIDITY       VARCHAR2(8),
  MS_ISNOT_DEL      VARCHAR2(8),
  MS_SERVICE_REMARK VARCHAR2(256),
  MS_SERVICE_CLASS  VARCHAR2(256),
  MS_BUSS_TYPE      VARCHAR2(8)
)
tablespace SVG_PLATROOF
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
comment on column T_SVG_MODULE_SERVICE.MS_SHORT_NAME
  is '业务服务组件短名';
comment on column T_SVG_MODULE_SERVICE.MS_SERVICE_NAME
  is '服务名';
comment on column T_SVG_MODULE_SERVICE.MS_VALIDITY
  is '有效标记';
comment on column T_SVG_MODULE_SERVICE.MS_ISNOT_DEL
  is '不可删除标志';
comment on column T_SVG_MODULE_SERVICE.MS_SERVICE_REMARK
  is '服务描述';
comment on column T_SVG_MODULE_SERVICE.MS_SERVICE_CLASS
  is '服务类名';
comment on column T_SVG_MODULE_SERVICE.MS_BUSS_TYPE
  is '业务系统编号';
alter table T_SVG_MODULE_SERVICE
  add constraint PK_MS_ID primary key (MS_ID)
  using index 
  tablespace SVG_PLATROOF
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table T_SVG_OPER_REALINFO
prompt ==================================
prompt
create table T_SVG_OPER_REALINFO
(
  OR_ID             VARCHAR2(36) not null,
  OR_BUSINESSSYS_ID VARCHAR2(8),
  OR_INFO_TYPE      VARCHAR2(8),
  OR_INFO_CONTENT   VARCHAR2(4000),
  OR_INFO_TIME      VARCHAR2(14),
  OR_TRANSACT_STATE VARCHAR2(8)
)
tablespace SVG_PLATROOF
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
comment on table T_SVG_OPER_REALINFO
  is '实时通知表';
comment on column T_SVG_OPER_REALINFO.OR_ID
  is '实时通知的编号';
comment on column T_SVG_OPER_REALINFO.OR_BUSINESSSYS_ID
  is '业务系统编号';
comment on column T_SVG_OPER_REALINFO.OR_INFO_TYPE
  is '通知类型(代码)';
comment on column T_SVG_OPER_REALINFO.OR_INFO_CONTENT
  is '通知内容';
comment on column T_SVG_OPER_REALINFO.OR_INFO_TIME
  is '通知时间';
comment on column T_SVG_OPER_REALINFO.OR_TRANSACT_STATE
  is '处理结果';
alter table T_SVG_OPER_REALINFO
  add constraint PK_T_SVG_OPER_REALINFO primary key (OR_ID)
  using index 
  tablespace SVG_PLATROOF
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table T_SVG_REP_FIELDNOTE
prompt ==================================
prompt
create table T_SVG_REP_FIELDNOTE
(
  RF_ID                   VARCHAR2(36) not null,
  RF_GRAPH_BUSINESS_TYPE  VARCHAR2(8),
  RF_RG_PARAM0_DESC       VARCHAR2(256),
  RF_RG_PARAM1_DESC       VARCHAR2(256),
  RF_RG_PARAM2_DESC       VARCHAR2(256),
  RF_RG_PARAM3_DESC       VARCHAR2(256),
  RF_RG_PARAM4_DESC       VARCHAR2(256),
  RF_RG_PARAM5_DESC       VARCHAR2(256),
  RF_RG_PARAM6_DESC       VARCHAR2(256),
  RF_RG_PARAM7_DESC       VARCHAR2(256),
  RF_RG_PARAM8_DESC       VARCHAR2(256),
  RF_RG_PARAM9_DESC       VARCHAR2(256),
  RF_RG_PARAM0_TYPE       VARCHAR2(256),
  RF_RG_PARAM1_TYPE       VARCHAR2(256),
  RF_RG_PARAM2_TYPE       VARCHAR2(256),
  RF_RG_PARAM3_TYPE       VARCHAR2(256),
  RF_RG_PARAM4_TYPE       VARCHAR2(256),
  RF_RG_PARAM5_TYPE       VARCHAR2(256),
  RF_RG_PARAM6_TYPE       VARCHAR2(256),
  RF_RG_PARAM7_TYPE       VARCHAR2(256),
  RF_RG_PARAM8_TYPE       VARCHAR2(256),
  RF_RG_PARAM9_TYPE       VARCHAR2(256),
  RF_RG_PARAM0_NULLFLAG   VARCHAR2(8),
  RF_RG_PARAM1_NULLFLAG   VARCHAR2(8),
  RF_RG_PARAM2_NULLFLAG   VARCHAR2(8),
  RF_RG_PARAM3_NULLFLAG   VARCHAR2(8),
  RF_RG_PARAM4_NULLFLAG   VARCHAR2(8),
  RF_RG_PARAM5_NULLFLAG   VARCHAR2(8),
  RF_RG_PARAM6_NULLFLAG   VARCHAR2(8),
  RF_RG_PARAM7_NULLFLAG   VARCHAR2(8),
  RF_RG_PARAM8_NULLFLAG   VARCHAR2(8),
  RF_RG_PARAM9_NULLFLAG   VARCHAR2(8),
  RF_RG_PARAM0_QUERYORDER NUMBER(2),
  RF_RG_PARAM1_QUERYORDER NUMBER(2),
  RF_RG_PARAM2_QUERYORDER NUMBER(2),
  RF_RG_PARAM3_QUERYORDER NUMBER(2),
  RF_RG_PARAM4_QUERYORDER NUMBER(2),
  RF_RG_PARAM5_QUERYORDER NUMBER(2),
  RF_RG_PARAM6_QUERYORDER NUMBER(2),
  RF_RG_PARAM7_QUERYORDER NUMBER(2),
  RF_RG_PARAM8_QUERYORDER NUMBER(2),
  RF_RG_PARAM9_QUERYORDER NUMBER(2)
)
tablespace SVG_PLATROOF
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
comment on table T_SVG_REP_FIELDNOTE
  is '图形存储表字段描述表';
comment on column T_SVG_REP_FIELDNOTE.RF_ID
  is '描述字段记录编号';
comment on column T_SVG_REP_FIELDNOTE.RF_GRAPH_BUSINESS_TYPE
  is '图业务类型';
comment on column T_SVG_REP_FIELDNOTE.RF_RG_PARAM0_DESC
  is '图形存储表中参数PARAM0的描述';
comment on column T_SVG_REP_FIELDNOTE.RF_RG_PARAM1_DESC
  is '图形存储表中参数PARAM1的描述';
comment on column T_SVG_REP_FIELDNOTE.RF_RG_PARAM2_DESC
  is '图形存储表中参数PARAM2的描述';
comment on column T_SVG_REP_FIELDNOTE.RF_RG_PARAM3_DESC
  is '图形存储表中参数PARAM3的描述';
comment on column T_SVG_REP_FIELDNOTE.RF_RG_PARAM4_DESC
  is '图形存储表中参数PARAM4的描述';
comment on column T_SVG_REP_FIELDNOTE.RF_RG_PARAM5_DESC
  is '图形存储表中参数PARAM5的描述';
comment on column T_SVG_REP_FIELDNOTE.RF_RG_PARAM6_DESC
  is '图形存储表中参数PARAM6的描述';
comment on column T_SVG_REP_FIELDNOTE.RF_RG_PARAM7_DESC
  is '图形存储表中参数PARAM7的描述';
comment on column T_SVG_REP_FIELDNOTE.RF_RG_PARAM8_DESC
  is '图形存储表中参数PARAM8的描述';
comment on column T_SVG_REP_FIELDNOTE.RF_RG_PARAM9_DESC
  is '图形存储表中参数PARAM9的描述';
comment on column T_SVG_REP_FIELDNOTE.RF_RG_PARAM0_TYPE
  is '图形存储表中参数PARAM0的类型，如为空则表示手工输入，如存在则为代码数据';
comment on column T_SVG_REP_FIELDNOTE.RF_RG_PARAM1_TYPE
  is '图形存储表中参数PARAM1的类型，如为空则表示手工输入，如存在则为代码数据';
comment on column T_SVG_REP_FIELDNOTE.RF_RG_PARAM2_TYPE
  is '图形存储表中参数PARAM2的类型，如为空则表示手工输入，如存在则为代码数据';
comment on column T_SVG_REP_FIELDNOTE.RF_RG_PARAM3_TYPE
  is '图形存储表中参数PARAM3的类型，如为空则表示手工输入，如存在则为代码数据';
comment on column T_SVG_REP_FIELDNOTE.RF_RG_PARAM4_TYPE
  is '图形存储表中参数PARAM4的类型，如为空则表示手工输入，如存在则为代码数据';
comment on column T_SVG_REP_FIELDNOTE.RF_RG_PARAM5_TYPE
  is '图形存储表中参数PARAM5的类型，如为空则表示手工输入，如存在则为代码数据';
comment on column T_SVG_REP_FIELDNOTE.RF_RG_PARAM6_TYPE
  is '图形存储表中参数PARAM6的类型，如为空则表示手工输入，如存在则为代码数据';
comment on column T_SVG_REP_FIELDNOTE.RF_RG_PARAM7_TYPE
  is '图形存储表中参数PARAM7的类型，如为空则表示手工输入，如存在则为代码数据';
comment on column T_SVG_REP_FIELDNOTE.RF_RG_PARAM8_TYPE
  is '图形存储表中参数PARAM8的类型，如为空则表示手工输入，如存在则为代码数据';
comment on column T_SVG_REP_FIELDNOTE.RF_RG_PARAM9_TYPE
  is '图形存储表中参数PARAM9的类型，如为空则表示手工输入，如存在则为代码数据';
comment on column T_SVG_REP_FIELDNOTE.RF_RG_PARAM0_NULLFLAG
  is '图形存储表中参数PARAM0的允许空标记';
comment on column T_SVG_REP_FIELDNOTE.RF_RG_PARAM1_NULLFLAG
  is '图形存储表中参数PARAM1的允许空标记';
comment on column T_SVG_REP_FIELDNOTE.RF_RG_PARAM2_NULLFLAG
  is '图形存储表中参数PARAM2的允许空标记';
comment on column T_SVG_REP_FIELDNOTE.RF_RG_PARAM3_NULLFLAG
  is '图形存储表中参数PARAM3的允许空标记';
comment on column T_SVG_REP_FIELDNOTE.RF_RG_PARAM4_NULLFLAG
  is '图形存储表中参数PARAM4的允许空标记';
comment on column T_SVG_REP_FIELDNOTE.RF_RG_PARAM5_NULLFLAG
  is '图形存储表中参数PARAM5的允许空标记';
comment on column T_SVG_REP_FIELDNOTE.RF_RG_PARAM6_NULLFLAG
  is '图形存储表中参数PARAM6的允许空标记';
comment on column T_SVG_REP_FIELDNOTE.RF_RG_PARAM7_NULLFLAG
  is '图形存储表中参数PARAM7的允许空标记';
comment on column T_SVG_REP_FIELDNOTE.RF_RG_PARAM8_NULLFLAG
  is '图形存储表中参数PARAM8的允许空标记';
comment on column T_SVG_REP_FIELDNOTE.RF_RG_PARAM9_NULLFLAG
  is '图形存储表中参数PARAM9的允许空标记';
comment on column T_SVG_REP_FIELDNOTE.RF_RG_PARAM0_QUERYORDER
  is '图形存储表中参数PARAM0的查询顺序位置';
comment on column T_SVG_REP_FIELDNOTE.RF_RG_PARAM1_QUERYORDER
  is '图形存储表中参数PARAM1的查询顺序位置';
comment on column T_SVG_REP_FIELDNOTE.RF_RG_PARAM2_QUERYORDER
  is '图形存储表中参数PARAM2的查询顺序位置';
comment on column T_SVG_REP_FIELDNOTE.RF_RG_PARAM3_QUERYORDER
  is '图形存储表中参数PARAM3的查询顺序位置';
comment on column T_SVG_REP_FIELDNOTE.RF_RG_PARAM4_QUERYORDER
  is '图形存储表中参数PARAM4的查询顺序位置';
comment on column T_SVG_REP_FIELDNOTE.RF_RG_PARAM5_QUERYORDER
  is '图形存储表中参数PARAM5的查询顺序位置';
comment on column T_SVG_REP_FIELDNOTE.RF_RG_PARAM6_QUERYORDER
  is '图形存储表中参数PARAM6的查询顺序位置';
comment on column T_SVG_REP_FIELDNOTE.RF_RG_PARAM7_QUERYORDER
  is '图形存储表中参数PARAM7的查询顺序位置';
comment on column T_SVG_REP_FIELDNOTE.RF_RG_PARAM8_QUERYORDER
  is '图形存储表中参数PARAM8的查询顺序位置';
comment on column T_SVG_REP_FIELDNOTE.RF_RG_PARAM9_QUERYORDER
  is '图形存储表中参数PARAM9的查询顺序位置';
alter table T_SVG_REP_FIELDNOTE
  add constraint PK_T_SVG_REPOSITORY_FIELDNOTE primary key (RF_ID)
  using index 
  tablespace SVG_PLATROOF
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table T_SVG_REP_FIELDNOTE_ALL
prompt ======================================
prompt
create table T_SVG_REP_FIELDNOTE_ALL
(
  RF_ID                   CHAR(36) not null,
  RF_GRAPH_BUSINESS_TYPE  VARCHAR2(8),
  RF_RG_PARAM0_DESC       VARCHAR2(256),
  RF_RG_PARAM1_DESC       VARCHAR2(256),
  RF_RG_PARAM2_DESC       VARCHAR2(256),
  RF_RG_PARAM3_DESC       VARCHAR2(256),
  RF_RG_PARAM4_DESC       VARCHAR2(256),
  RF_RG_PARAM5_DESC       VARCHAR2(256),
  RF_RG_PARAM6_DESC       VARCHAR2(256),
  RF_RG_PARAM7_DESC       VARCHAR2(256),
  RF_RG_PARAM8_DESC       VARCHAR2(256),
  RF_RG_PARAM9_DESC       VARCHAR2(256),
  RF_RG_PARAM0_TYPE       VARCHAR2(256),
  RF_RG_PARAM1_TYPE       VARCHAR2(256),
  RF_RG_PARAM2_TYPE       VARCHAR2(256),
  RF_RG_PARAM3_TYPE       VARCHAR2(256),
  RF_RG_PARAM4_TYPE       VARCHAR2(256),
  RF_RG_PARAM5_TYPE       VARCHAR2(256),
  RF_RG_PARAM6_TYPE       VARCHAR2(256),
  RF_RG_PARAM7_TYPE       VARCHAR2(256),
  RF_RG_PARAM8_TYPE       VARCHAR2(256),
  RF_RG_PARAM9_TYPE       VARCHAR2(256),
  RF_RG_PARAM0_NULLFLAG   VARCHAR2(8),
  RF_RG_PARAM1_NULLFLAG   VARCHAR2(8),
  RF_RG_PARAM2_NULLFLAG   VARCHAR2(8),
  RF_RG_PARAM3_NULLFLAG   VARCHAR2(8),
  RF_RG_PARAM4_NULLFLAG   VARCHAR2(8),
  RF_RG_PARAM5_NULLFLAG   VARCHAR2(8),
  RF_RG_PARAM6_NULLFLAG   VARCHAR2(8),
  RF_RG_PARAM7_NULLFLAG   VARCHAR2(8),
  RF_RG_PARAM8_NULLFLAG   VARCHAR2(8),
  RF_RG_PARAM9_NULLFLAG   VARCHAR2(8),
  RF_RG_PARAM0_QUERYORDER NUMBER(2),
  RF_RG_PARAM1_QUERYORDER NUMBER(2),
  RF_RG_PARAM2_QUERYORDER NUMBER(2),
  RF_RG_PARAM3_QUERYORDER NUMBER(2),
  RF_RG_PARAM4_QUERYORDER NUMBER(2),
  RF_RG_PARAM5_QUERYORDER NUMBER(2),
  RF_RG_PARAM6_QUERYORDER NUMBER(2),
  RF_RG_PARAM7_QUERYORDER NUMBER(2),
  RF_RG_PARAM8_QUERYORDER NUMBER(2),
  RF_RG_PARAM9_QUERYORDER NUMBER(2),
  FLAG                    VARCHAR2(256)
)
tablespace SVG_PLATROOF
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table T_SVG_REP_FIELDNOTE_NEST
prompt =======================================
prompt
create table T_SVG_REP_FIELDNOTE_NEST
(
  RF_ID                  CHAR(36) not null,
  RF_GRAPH_BUSINESS_TYPE VARCHAR2(8),
  RF_RG_PARAM0_DESC      VARCHAR2(256),
  RF_RG_PARAM1_DESC      VARCHAR2(256),
  RF_RG_PARAM2_DESC      VARCHAR2(256),
  RF_RG_PARAM3_DESC      VARCHAR2(256),
  RF_RG_PARAM4_DESC      VARCHAR2(256),
  RF_RG_PARAM5_DESC      VARCHAR2(256),
  RF_RG_PARAM6_DESC      VARCHAR2(256),
  RF_RG_PARAM7_DESC      VARCHAR2(256),
  RF_RG_PARAM8_DESC      VARCHAR2(256),
  RF_RG_PARAM9_DESC      VARCHAR2(256)
)
tablespace SVG_PLATROOF
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table T_SVG_REP_FIELDNOTE_PLAN
prompt =======================================
prompt
create table T_SVG_REP_FIELDNOTE_PLAN
(
  RF_ID                   CHAR(36) not null,
  RF_GRAPH_BUSINESS_TYPE  VARCHAR2(8),
  RF_RG_PARAM0_DESC       VARCHAR2(256),
  RF_RG_PARAM1_DESC       VARCHAR2(256),
  RF_RG_PARAM2_DESC       VARCHAR2(256),
  RF_RG_PARAM3_DESC       VARCHAR2(256),
  RF_RG_PARAM4_DESC       VARCHAR2(256),
  RF_RG_PARAM5_DESC       VARCHAR2(256),
  RF_RG_PARAM6_DESC       VARCHAR2(256),
  RF_RG_PARAM7_DESC       VARCHAR2(256),
  RF_RG_PARAM8_DESC       VARCHAR2(256),
  RF_RG_PARAM9_DESC       VARCHAR2(256),
  RF_RG_PARAM0_TYPE       VARCHAR2(256),
  RF_RG_PARAM1_TYPE       VARCHAR2(256),
  RF_RG_PARAM2_TYPE       VARCHAR2(256),
  RF_RG_PARAM3_TYPE       VARCHAR2(256),
  RF_RG_PARAM4_TYPE       VARCHAR2(256),
  RF_RG_PARAM5_TYPE       VARCHAR2(256),
  RF_RG_PARAM6_TYPE       VARCHAR2(256),
  RF_RG_PARAM7_TYPE       VARCHAR2(256),
  RF_RG_PARAM8_TYPE       VARCHAR2(256),
  RF_RG_PARAM9_TYPE       VARCHAR2(256),
  RF_RG_PARAM0_NULLFLAG   VARCHAR2(256),
  RF_RG_PARAM1_NULLFLAG   VARCHAR2(256),
  RF_RG_PARAM2_NULLFLAG   VARCHAR2(256),
  RF_RG_PARAM3_NULLFLAG   VARCHAR2(256),
  RF_RG_PARAM4_NULLFLAG   VARCHAR2(256),
  RF_RG_PARAM5_NULLFLAG   VARCHAR2(256),
  RF_RG_PARAM6_NULLFLAG   VARCHAR2(256),
  RF_RG_PARAM7_NULLFLAG   VARCHAR2(256),
  RF_RG_PARAM8_NULLFLAG   VARCHAR2(256),
  RF_RG_PARAM9_NULLFLAG   VARCHAR2(256),
  RF_RG_PARAM0_QUERYORDER NUMBER(2),
  RF_RG_PARAM1_QUERYORDER NUMBER(2),
  RF_RG_PARAM2_QUERYORDER NUMBER(2),
  RF_RG_PARAM3_QUERYORDER NUMBER(2),
  RF_RG_PARAM4_QUERYORDER NUMBER(2),
  RF_RG_PARAM5_QUERYORDER NUMBER(2),
  RF_RG_PARAM6_QUERYORDER NUMBER(2),
  RF_RG_PARAM7_QUERYORDER NUMBER(2),
  RF_RG_PARAM8_QUERYORDER NUMBER(2),
  RF_RG_PARAM9_QUERYORDER NUMBER(2)
)
tablespace SVG_PLATROOF
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table T_SVG_REP_FIELDNOTE_PSMS
prompt =======================================
prompt
create table T_SVG_REP_FIELDNOTE_PSMS
(
  RF_ID                   CHAR(36) not null,
  RF_GRAPH_BUSINESS_TYPE  VARCHAR2(8),
  RF_RG_PARAM0_DESC       VARCHAR2(256),
  RF_RG_PARAM1_DESC       VARCHAR2(256),
  RF_RG_PARAM2_DESC       VARCHAR2(256),
  RF_RG_PARAM3_DESC       VARCHAR2(256),
  RF_RG_PARAM4_DESC       VARCHAR2(256),
  RF_RG_PARAM5_DESC       VARCHAR2(256),
  RF_RG_PARAM6_DESC       VARCHAR2(256),
  RF_RG_PARAM7_DESC       VARCHAR2(256),
  RF_RG_PARAM8_DESC       VARCHAR2(256),
  RF_RG_PARAM9_DESC       VARCHAR2(256),
  RF_RG_PARAM0_TYPE       VARCHAR2(256),
  RF_RG_PARAM1_TYPE       VARCHAR2(256),
  RF_RG_PARAM2_TYPE       VARCHAR2(256),
  RF_RG_PARAM3_TYPE       VARCHAR2(256),
  RF_RG_PARAM4_TYPE       VARCHAR2(256),
  RF_RG_PARAM5_TYPE       VARCHAR2(256),
  RF_RG_PARAM6_TYPE       VARCHAR2(256),
  RF_RG_PARAM7_TYPE       VARCHAR2(256),
  RF_RG_PARAM8_TYPE       VARCHAR2(256),
  RF_RG_PARAM9_TYPE       VARCHAR2(256),
  RF_RG_PARAM0_NULLFLAG   VARCHAR2(8),
  RF_RG_PARAM1_NULLFLAG   VARCHAR2(8),
  RF_RG_PARAM2_NULLFLAG   VARCHAR2(8),
  RF_RG_PARAM3_NULLFLAG   VARCHAR2(8),
  RF_RG_PARAM4_NULLFLAG   VARCHAR2(8),
  RF_RG_PARAM5_NULLFLAG   VARCHAR2(8),
  RF_RG_PARAM6_NULLFLAG   VARCHAR2(8),
  RF_RG_PARAM7_NULLFLAG   VARCHAR2(8),
  RF_RG_PARAM8_NULLFLAG   VARCHAR2(8),
  RF_RG_PARAM9_NULLFLAG   VARCHAR2(8),
  RF_RG_PARAM0_QUERYORDER NUMBER(2),
  RF_RG_PARAM1_QUERYORDER NUMBER(2),
  RF_RG_PARAM2_QUERYORDER NUMBER(2),
  RF_RG_PARAM3_QUERYORDER NUMBER(2),
  RF_RG_PARAM4_QUERYORDER NUMBER(2),
  RF_RG_PARAM5_QUERYORDER NUMBER(2),
  RF_RG_PARAM6_QUERYORDER NUMBER(2),
  RF_RG_PARAM7_QUERYORDER NUMBER(2),
  RF_RG_PARAM8_QUERYORDER NUMBER(2),
  RF_RG_PARAM9_QUERYORDER NUMBER(2)
)
tablespace SVG_PLATROOF
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 16K
    minextents 1
    maxextents unlimited
  );
comment on table T_SVG_REP_FIELDNOTE_PSMS
  is '图形存储表字段描述表';
comment on column T_SVG_REP_FIELDNOTE_PSMS.RF_ID
  is '描述字段记录编号';
comment on column T_SVG_REP_FIELDNOTE_PSMS.RF_GRAPH_BUSINESS_TYPE
  is '图业务类型';
comment on column T_SVG_REP_FIELDNOTE_PSMS.RF_RG_PARAM0_DESC
  is '图形存储表中参数PARAM0的描述';
comment on column T_SVG_REP_FIELDNOTE_PSMS.RF_RG_PARAM1_DESC
  is '图形存储表中参数PARAM1的描述';
comment on column T_SVG_REP_FIELDNOTE_PSMS.RF_RG_PARAM2_DESC
  is '图形存储表中参数PARAM2的描述';
comment on column T_SVG_REP_FIELDNOTE_PSMS.RF_RG_PARAM3_DESC
  is '图形存储表中参数PARAM3的描述';
comment on column T_SVG_REP_FIELDNOTE_PSMS.RF_RG_PARAM4_DESC
  is '图形存储表中参数PARAM4的描述';
comment on column T_SVG_REP_FIELDNOTE_PSMS.RF_RG_PARAM5_DESC
  is '图形存储表中参数PARAM5的描述';
comment on column T_SVG_REP_FIELDNOTE_PSMS.RF_RG_PARAM6_DESC
  is '图形存储表中参数PARAM6的描述';
comment on column T_SVG_REP_FIELDNOTE_PSMS.RF_RG_PARAM7_DESC
  is '图形存储表中参数PARAM7的描述';
comment on column T_SVG_REP_FIELDNOTE_PSMS.RF_RG_PARAM8_DESC
  is '图形存储表中参数PARAM8的描述';
comment on column T_SVG_REP_FIELDNOTE_PSMS.RF_RG_PARAM9_DESC
  is '图形存储表中参数PARAM9的描述';
comment on column T_SVG_REP_FIELDNOTE_PSMS.RF_RG_PARAM0_TYPE
  is '图形存储表中参数PARAM0的类型，如为空则表示手工输入，如存在则为代码数据';
comment on column T_SVG_REP_FIELDNOTE_PSMS.RF_RG_PARAM1_TYPE
  is '图形存储表中参数PARAM1的类型，如为空则表示手工输入，如存在则为代码数据';
comment on column T_SVG_REP_FIELDNOTE_PSMS.RF_RG_PARAM2_TYPE
  is '图形存储表中参数PARAM2的类型，如为空则表示手工输入，如存在则为代码数据';
comment on column T_SVG_REP_FIELDNOTE_PSMS.RF_RG_PARAM3_TYPE
  is '图形存储表中参数PARAM3的类型，如为空则表示手工输入，如存在则为代码数据';
comment on column T_SVG_REP_FIELDNOTE_PSMS.RF_RG_PARAM4_TYPE
  is '图形存储表中参数PARAM4的类型，如为空则表示手工输入，如存在则为代码数据';
comment on column T_SVG_REP_FIELDNOTE_PSMS.RF_RG_PARAM5_TYPE
  is '图形存储表中参数PARAM5的类型，如为空则表示手工输入，如存在则为代码数据';
comment on column T_SVG_REP_FIELDNOTE_PSMS.RF_RG_PARAM6_TYPE
  is '图形存储表中参数PARAM6的类型，如为空则表示手工输入，如存在则为代码数据';
comment on column T_SVG_REP_FIELDNOTE_PSMS.RF_RG_PARAM7_TYPE
  is '图形存储表中参数PARAM7的类型，如为空则表示手工输入，如存在则为代码数据';
comment on column T_SVG_REP_FIELDNOTE_PSMS.RF_RG_PARAM8_TYPE
  is '图形存储表中参数PARAM8的类型，如为空则表示手工输入，如存在则为代码数据';
comment on column T_SVG_REP_FIELDNOTE_PSMS.RF_RG_PARAM9_TYPE
  is '图形存储表中参数PARAM9的类型，如为空则表示手工输入，如存在则为代码数据';
comment on column T_SVG_REP_FIELDNOTE_PSMS.RF_RG_PARAM0_NULLFLAG
  is '图形存储表中参数PARAM0的允许空标记';
comment on column T_SVG_REP_FIELDNOTE_PSMS.RF_RG_PARAM1_NULLFLAG
  is '图形存储表中参数PARAM1的允许空标记';
comment on column T_SVG_REP_FIELDNOTE_PSMS.RF_RG_PARAM2_NULLFLAG
  is '图形存储表中参数PARAM2的允许空标记';
comment on column T_SVG_REP_FIELDNOTE_PSMS.RF_RG_PARAM3_NULLFLAG
  is '图形存储表中参数PARAM3的允许空标记';
comment on column T_SVG_REP_FIELDNOTE_PSMS.RF_RG_PARAM4_NULLFLAG
  is '图形存储表中参数PARAM4的允许空标记';
comment on column T_SVG_REP_FIELDNOTE_PSMS.RF_RG_PARAM5_NULLFLAG
  is '图形存储表中参数PARAM5的允许空标记';
comment on column T_SVG_REP_FIELDNOTE_PSMS.RF_RG_PARAM6_NULLFLAG
  is '图形存储表中参数PARAM6的允许空标记';
comment on column T_SVG_REP_FIELDNOTE_PSMS.RF_RG_PARAM7_NULLFLAG
  is '图形存储表中参数PARAM7的允许空标记';
comment on column T_SVG_REP_FIELDNOTE_PSMS.RF_RG_PARAM8_NULLFLAG
  is '图形存储表中参数PARAM8的允许空标记';
comment on column T_SVG_REP_FIELDNOTE_PSMS.RF_RG_PARAM9_NULLFLAG
  is '图形存储表中参数PARAM9的允许空标记';
comment on column T_SVG_REP_FIELDNOTE_PSMS.RF_RG_PARAM0_QUERYORDER
  is '图形存储表中参数PARAM0的查询顺序位置';
comment on column T_SVG_REP_FIELDNOTE_PSMS.RF_RG_PARAM1_QUERYORDER
  is '图形存储表中参数PARAM1的查询顺序位置';
comment on column T_SVG_REP_FIELDNOTE_PSMS.RF_RG_PARAM2_QUERYORDER
  is '图形存储表中参数PARAM2的查询顺序位置';
comment on column T_SVG_REP_FIELDNOTE_PSMS.RF_RG_PARAM3_QUERYORDER
  is '图形存储表中参数PARAM3的查询顺序位置';
comment on column T_SVG_REP_FIELDNOTE_PSMS.RF_RG_PARAM4_QUERYORDER
  is '图形存储表中参数PARAM4的查询顺序位置';
comment on column T_SVG_REP_FIELDNOTE_PSMS.RF_RG_PARAM5_QUERYORDER
  is '图形存储表中参数PARAM5的查询顺序位置';
comment on column T_SVG_REP_FIELDNOTE_PSMS.RF_RG_PARAM6_QUERYORDER
  is '图形存储表中参数PARAM6的查询顺序位置';
comment on column T_SVG_REP_FIELDNOTE_PSMS.RF_RG_PARAM7_QUERYORDER
  is '图形存储表中参数PARAM7的查询顺序位置';
comment on column T_SVG_REP_FIELDNOTE_PSMS.RF_RG_PARAM8_QUERYORDER
  is '图形存储表中参数PARAM8的查询顺序位置';
comment on column T_SVG_REP_FIELDNOTE_PSMS.RF_RG_PARAM9_QUERYORDER
  is '图形存储表中参数PARAM9的查询顺序位置';

prompt
prompt Creating table T_SVG_REP_GRAPH
prompt ==============================
prompt
create table T_SVG_REP_GRAPH
(
  RG_ID              VARCHAR2(36) not null,
  RG_GRAPH_NAME      VARCHAR2(64),
  RG_GRAPH_TYPE      VARCHAR2(8),
  RG_GRAPH_BUSI_TYPE VARCHAR2(8),
  RG_GRAPH_FILE_TYPE VARCHAR2(8),
  RG_PARAM0          VARCHAR2(1024),
  RG_PARAM1          VARCHAR2(1024),
  RG_PARAM2          VARCHAR2(1024),
  RG_PARAM3          VARCHAR2(1024),
  RG_PARAM4          VARCHAR2(1024),
  RG_PARAM5          VARCHAR2(1024),
  RG_PARAM6          VARCHAR2(1024),
  RG_PARAM7          VARCHAR2(1024),
  RG_PARAM8          VARCHAR2(64),
  RG_PARAM9          VARCHAR2(64),
  RG_MODIFY_PERSON   VARCHAR2(64),
  RG_MODIFY_TIME     VARCHAR2(14),
  RG_CONTENT         BLOB
)
tablespace SVG_PLATROOF
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
comment on table T_SVG_REP_GRAPH
  is '图形存储表';
comment on column T_SVG_REP_GRAPH.RG_ID
  is '图形存储记录编号';
comment on column T_SVG_REP_GRAPH.RG_GRAPH_NAME
  is '图名称';
comment on column T_SVG_REP_GRAPH.RG_GRAPH_TYPE
  is '图类型（个性图、标准图）';
comment on column T_SVG_REP_GRAPH.RG_GRAPH_BUSI_TYPE
  is '图业务类型';
comment on column T_SVG_REP_GRAPH.RG_GRAPH_FILE_TYPE
  is '图格式(SVG,JPG,GIF,CAD,VSD)';
comment on column T_SVG_REP_GRAPH.RG_MODIFY_PERSON
  is '最新更新人';
comment on column T_SVG_REP_GRAPH.RG_MODIFY_TIME
  is '最新更新时间';
comment on column T_SVG_REP_GRAPH.RG_CONTENT
  is '图内容';
alter table T_SVG_REP_GRAPH
  add constraint PK_T_SVG_REPOSITORY_GRAPH primary key (RG_ID)
  using index 
  tablespace SVG_PLATROOF
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table T_SVG_REP_GRAPH_NEST
prompt ===================================
prompt
create table T_SVG_REP_GRAPH_NEST
(
  RG_ID              CHAR(36) not null,
  RG_GRAPH_NAME      VARCHAR2(64),
  RG_GRAPH_TYPE      VARCHAR2(8),
  RG_GRAPH_BUSI_TYPE VARCHAR2(8),
  RG_GRAPH_FILE_TYPE VARCHAR2(8),
  RG_PARAM0          VARCHAR2(1024),
  RG_PARAM1          VARCHAR2(1024),
  RG_PARAM2          VARCHAR2(1024),
  RG_PARAM3          VARCHAR2(1024),
  RG_PARAM4          VARCHAR2(1024),
  RG_PARAM5          VARCHAR2(1024),
  RG_PARAM6          VARCHAR2(1024),
  RG_PARAM7          VARCHAR2(1024),
  RG_PARAM8          VARCHAR2(64),
  RG_PARAM9          VARCHAR2(64),
  RG_MODIFY_PERSON   VARCHAR2(64),
  RG_MODIFY_TIME     VARCHAR2(14),
  RG_CONTENT         BLOB
)
tablespace SVG_PLATROOF
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table T_SVG_REP_GRAPH_PLAN
prompt ===================================
prompt
create table T_SVG_REP_GRAPH_PLAN
(
  RG_ID              CHAR(36) not null,
  RG_GRAPH_NAME      VARCHAR2(64),
  RG_GRAPH_TYPE      VARCHAR2(8),
  RG_GRAPH_BUSI_TYPE VARCHAR2(8),
  RG_GRAPH_FILE_TYPE VARCHAR2(8),
  RG_PARAM0          VARCHAR2(1024),
  RG_PARAM1          VARCHAR2(1024),
  RG_PARAM2          VARCHAR2(1024),
  RG_PARAM3          VARCHAR2(1024),
  RG_PARAM4          VARCHAR2(1024),
  RG_PARAM5          VARCHAR2(1024),
  RG_PARAM6          VARCHAR2(1024),
  RG_PARAM7          VARCHAR2(1024),
  RG_PARAM8          VARCHAR2(64),
  RG_PARAM9          VARCHAR2(64),
  RG_MODIFY_PERSON   VARCHAR2(64),
  RG_MODIFY_TIME     VARCHAR2(14),
  RG_CONTENT         BLOB
)
tablespace SVG_PLATROOF
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table T_SVG_REP_GRAPH_PSMS
prompt ===================================
prompt
create table T_SVG_REP_GRAPH_PSMS
(
  RG_ID              CHAR(36) not null,
  RG_GRAPH_NAME      VARCHAR2(64),
  RG_GRAPH_TYPE      VARCHAR2(8),
  RG_GRAPH_BUSI_TYPE VARCHAR2(8),
  RG_GRAPH_FILE_TYPE VARCHAR2(8),
  RG_PARAM0          VARCHAR2(1024),
  RG_PARAM1          VARCHAR2(1024),
  RG_PARAM2          VARCHAR2(1024),
  RG_PARAM3          VARCHAR2(1024),
  RG_PARAM4          VARCHAR2(1024),
  RG_PARAM5          VARCHAR2(1024),
  RG_PARAM6          VARCHAR2(1024),
  RG_PARAM7          VARCHAR2(1024),
  RG_PARAM8          VARCHAR2(64),
  RG_PARAM9          VARCHAR2(64),
  RG_MODIFY_PERSON   VARCHAR2(64),
  RG_MODIFY_TIME     VARCHAR2(14),
  RG_CONTENT         BLOB
)
tablespace SVG_PLATROOF
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table T_SVG_REP_MANAGER
prompt ================================
prompt
create table T_SVG_REP_MANAGER
(
  RM_ID             CHAR(36) not null,
  RM_BUSINESSSYS_ID VARCHAR2(8) not null,
  RM_TABLE_RELATE   VARCHAR2(64) not null
)
tablespace SVG_PLATROOF
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
comment on table T_SVG_REP_MANAGER
  is '图形存储管理';
comment on column T_SVG_REP_MANAGER.RM_ID
  is '图形存储管理编号';
comment on column T_SVG_REP_MANAGER.RM_BUSINESSSYS_ID
  is '业务系统编号';
comment on column T_SVG_REP_MANAGER.RM_TABLE_RELATE
  is '对应表标识(自行取名，可重复，复用)';
alter table T_SVG_REP_MANAGER
  add constraint PK_T_SVG_REPOSITORY_MANAGER primary key (RM_ID, RM_BUSINESSSYS_ID, RM_TABLE_RELATE)
  using index 
  tablespace SVG_PLATROOF
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table T_SVG_REP_MODEL
prompt ==============================
prompt
create table T_SVG_REP_MODEL
(
  RM_ID                  VARCHAR2(36) not null,
  RM_GRAPH_BUSINESS_TYPE VARCHAR2(8),
  RM_MODEL_ID            VARCHAR2(128)
)
tablespace SVG_PLATROOF
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
comment on table T_SVG_REP_MODEL
  is '图形与模型关系';
comment on column T_SVG_REP_MODEL.RM_ID
  is '关系记录编号';
comment on column T_SVG_REP_MODEL.RM_GRAPH_BUSINESS_TYPE
  is '图业务类型';
comment on column T_SVG_REP_MODEL.RM_MODEL_ID
  is '模型编号';
alter table T_SVG_REP_MODEL
  add constraint PK_T_SVG_REPOSITORY_MODEL primary key (RM_ID)
  using index 
  tablespace SVG_PLATROOF
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table T_SVG_REP_MODEL_NEST
prompt ===================================
prompt
create table T_SVG_REP_MODEL_NEST
(
  RM_ID                  CHAR(36) not null,
  RM_GRAPH_BUSINESS_TYPE VARCHAR2(8),
  RM_MODEL_ID            VARCHAR2(128)
)
tablespace SVG_PLATROOF
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table T_SVG_REP_MODEL_PLAN
prompt ===================================
prompt
create table T_SVG_REP_MODEL_PLAN
(
  RM_ID                  CHAR(36) not null,
  RM_GRAPH_BUSINESS_TYPE VARCHAR2(8),
  RM_MODEL_ID            VARCHAR2(128)
)
tablespace SVG_PLATROOF
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table T_SVG_REP_MODEL_PSMS
prompt ===================================
prompt
create table T_SVG_REP_MODEL_PSMS
(
  RM_ID                  CHAR(36) not null,
  RM_GRAPH_BUSINESS_TYPE VARCHAR2(8),
  RM_MODEL_ID            VARCHAR2(128)
)
tablespace SVG_PLATROOF
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table T_SVG_REP_TOPOLOGY
prompt =================================
prompt
create table T_SVG_REP_TOPOLOGY
(
  RT_ID             VARCHAR2(36) not null,
  RT_GRAPH_ID       VARCHAR2(36),
  RT_LAYER          VARCHAR2(8),
  RT_POINT_FROM     VARCHAR2(256),
  RT_POINT_TO       VARCHAR2(256),
  RT_TOPO_DIRECTION VARCHAR2(8)
)
tablespace SVG_PLATROOF
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
comment on table T_SVG_REP_TOPOLOGY
  is '拓扑关系表';
comment on column T_SVG_REP_TOPOLOGY.RT_ID
  is '拓扑关系记录编号';
comment on column T_SVG_REP_TOPOLOGY.RT_GRAPH_ID
  is '图编号';
comment on column T_SVG_REP_TOPOLOGY.RT_LAYER
  is '层次（代码）';
comment on column T_SVG_REP_TOPOLOGY.RT_POINT_FROM
  is '起点';
comment on column T_SVG_REP_TOPOLOGY.RT_POINT_TO
  is '终点';
comment on column T_SVG_REP_TOPOLOGY.RT_TOPO_DIRECTION
  is '方向（有向，无向）';
alter table T_SVG_REP_TOPOLOGY
  add constraint PK_T_SVG_REPOSITORY_TOPOLOGY primary key (RT_ID)
  using index 
  tablespace SVG_PLATROOF
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table T_SVG_REP_TOPOLOGY_NEST
prompt ======================================
prompt
create table T_SVG_REP_TOPOLOGY_NEST
(
  RT_ID             CHAR(36) not null,
  RT_GRAPH_ID       VARCHAR2(36),
  RT_LAYER          VARCHAR2(8),
  RT_POINT_FROM     VARCHAR2(256),
  RT_POINT_TO       VARCHAR2(256),
  RT_TOPO_DIRECTION VARCHAR2(8)
)
tablespace SVG_PLATROOF
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table T_SVG_REP_TOPOLOGY_PLAN
prompt ======================================
prompt
create table T_SVG_REP_TOPOLOGY_PLAN
(
  RT_ID             CHAR(36) not null,
  RT_GRAPH_ID       VARCHAR2(36),
  RT_LAYER          VARCHAR2(8),
  RT_POINT_FROM     VARCHAR2(256),
  RT_POINT_TO       VARCHAR2(256),
  RT_TOPO_DIRECTION VARCHAR2(8)
)
tablespace SVG_PLATROOF
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table T_SVG_REP_TOPOLOGY_PSMS
prompt ======================================
prompt
create table T_SVG_REP_TOPOLOGY_PSMS
(
  RT_ID             CHAR(36) not null,
  RT_GRAPH_ID       VARCHAR2(36),
  RT_LAYER          VARCHAR2(8),
  RT_POINT_FROM     VARCHAR2(256),
  RT_POINT_TO       VARCHAR2(256),
  RT_TOPO_DIRECTION VARCHAR2(8)
)
tablespace SVG_PLATROOF
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table T_SVG_REP_VERHIS
prompt ===============================
prompt
create table T_SVG_REP_VERHIS
(
  RV_ID            VARCHAR2(36) not null,
  RV_GRAPH_ID      VARCHAR2(36),
  RV_GRAPH_NAME    VARCHAR2(128),
  RV_GRAPH_CONTENT BLOB,
  RV_MODIFY_PERSON VARCHAR2(64),
  RV_MODIFY_TIME   VARCHAR2(14)
)
tablespace SVG_PLATROOF
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
comment on table T_SVG_REP_VERHIS
  is '替换式版本历史表';
comment on column T_SVG_REP_VERHIS.RV_ID
  is '替代式存储记录编号';
comment on column T_SVG_REP_VERHIS.RV_GRAPH_ID
  is '存储的图形的编号';
comment on column T_SVG_REP_VERHIS.RV_GRAPH_NAME
  is '存贮的图形的名称';
comment on column T_SVG_REP_VERHIS.RV_GRAPH_CONTENT
  is '图内容';
comment on column T_SVG_REP_VERHIS.RV_MODIFY_PERSON
  is '最新修改者';
comment on column T_SVG_REP_VERHIS.RV_MODIFY_TIME
  is '最新修改时间';
alter table T_SVG_REP_VERHIS
  add constraint PK_T_SVG_REPOSITORY_VERHIS primary key (RV_ID)
  using index 
  tablespace SVG_PLATROOF
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table T_SVG_REP_VERHIS_NEST
prompt ====================================
prompt
create table T_SVG_REP_VERHIS_NEST
(
  RV_ID            CHAR(36) not null,
  RV_GRAPH_ID      VARCHAR2(36),
  RV_GRAPH_NAME    VARCHAR2(128),
  RV_GRAPH_CONTENT BLOB,
  RV_MODIFY_PERSON VARCHAR2(64),
  RV_MODIFY_TIME   VARCHAR2(14)
)
tablespace SVG_PLATROOF
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table T_SVG_REP_VERHIS_PLAN
prompt ====================================
prompt
create table T_SVG_REP_VERHIS_PLAN
(
  RV_ID            CHAR(36) not null,
  RV_GRAPH_ID      VARCHAR2(36),
  RV_GRAPH_NAME    VARCHAR2(128),
  RV_GRAPH_CONTENT BLOB,
  RV_MODIFY_PERSON VARCHAR2(64),
  RV_MODIFY_TIME   VARCHAR2(14)
)
tablespace SVG_PLATROOF
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table T_SVG_REP_VERHIS_PSMS
prompt ====================================
prompt
create table T_SVG_REP_VERHIS_PSMS
(
  RV_ID            CHAR(36) not null,
  RV_GRAPH_ID      VARCHAR2(36),
  RV_GRAPH_NAME    VARCHAR2(128),
  RV_GRAPH_CONTENT BLOB,
  RV_MODIFY_PERSON VARCHAR2(64),
  RV_MODIFY_TIME   VARCHAR2(14)
)
tablespace SVG_PLATROOF
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
alter table T_SVG_REP_VERHIS_PSMS
  add constraint PK_T_SVG_REP_VERHIS_TEST primary key (RV_ID)
  using index 
  tablespace SVG_PLATROOF
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table T_SVG_REP_VERLOG
prompt ===============================
prompt
create table T_SVG_REP_VERLOG
(
  RV_ID            VARCHAR2(36) not null,
  RV_GRAPH_ID      VARCHAR2(36),
  RV_GRAPH_NAME    VARCHAR2(128),
  RV_CHANGE_TYPE   VARCHAR2(8),
  RV_CHANGE_CONENT BLOB,
  RV_CHANGE_PERSON VARCHAR2(64),
  RV_CHANGE_TIME   VARCHAR2(14)
)
tablespace SVG_PLATROOF
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
comment on table T_SVG_REP_VERLOG
  is '增量式版本历史表';
comment on column T_SVG_REP_VERLOG.RV_ID
  is '增量式记录的编号';
comment on column T_SVG_REP_VERLOG.RV_GRAPH_ID
  is '图编号';
comment on column T_SVG_REP_VERLOG.RV_GRAPH_NAME
  is '图名称';
comment on column T_SVG_REP_VERLOG.RV_CHANGE_TYPE
  is '变更类型(基础图、增加、删除、修改)';
comment on column T_SVG_REP_VERLOG.RV_CHANGE_CONENT
  is '变更内容';
comment on column T_SVG_REP_VERLOG.RV_CHANGE_PERSON
  is '变更人';
comment on column T_SVG_REP_VERLOG.RV_CHANGE_TIME
  is '变更时间';
alter table T_SVG_REP_VERLOG
  add constraint PK_T_SVG_REPOSITORY_VERLOG primary key (RV_ID)
  using index 
  tablespace SVG_PLATROOF
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table T_SVG_REP_VERLOG_NEST
prompt ====================================
prompt
create table T_SVG_REP_VERLOG_NEST
(
  RV_ID            CHAR(36) not null,
  RV_GRAPH_ID      VARCHAR2(36),
  RV_GRAPH_NAME    VARCHAR2(128),
  RV_CHANGE_TYPE   VARCHAR2(8),
  RV_CHANGE_CONENT BLOB,
  RV_CHANGE_PERSON VARCHAR2(64),
  RV_CHANGE_TIME   VARCHAR2(14)
)
tablespace SVG_PLATROOF
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table T_SVG_REP_VERLOG_PLAN
prompt ====================================
prompt
create table T_SVG_REP_VERLOG_PLAN
(
  RV_ID            CHAR(36) not null,
  RV_GRAPH_ID      VARCHAR2(36),
  RV_GRAPH_NAME    VARCHAR2(128),
  RV_CHANGE_TYPE   VARCHAR2(8),
  RV_CHANGE_CONENT BLOB,
  RV_CHANGE_PERSON VARCHAR2(64),
  RV_CHANGE_TIME   VARCHAR2(14)
)
tablespace SVG_PLATROOF
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table T_SVG_REP_VERLOG_PSMS
prompt ====================================
prompt
create table T_SVG_REP_VERLOG_PSMS
(
  RV_ID            CHAR(36) not null,
  RV_GRAPH_ID      VARCHAR2(36),
  RV_GRAPH_NAME    VARCHAR2(128),
  RV_CHANGE_TYPE   VARCHAR2(8),
  RV_CHANGE_CONENT BLOB,
  RV_CHANGE_PERSON VARCHAR2(64),
  RV_CHANGE_TIME   VARCHAR2(14)
)
tablespace SVG_PLATROOF
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table T_SVG_REP_VERSET
prompt ===============================
prompt
create table T_SVG_REP_VERSET
(
  RV_ID                  VARCHAR2(36) not null,
  RV_GRAPH_BUSINESS_TYPE VARCHAR2(8),
  RV_GRAPH_FILE_TYPE     VARCHAR2(8),
  RV_STORE_POLICY        VARCHAR2(8),
  RV_VALIDITY            VARCHAR2(8)
)
tablespace SVG_PLATROOF
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
comment on table T_SVG_REP_VERSET
  is '版本管理设定';
comment on column T_SVG_REP_VERSET.RV_ID
  is '版本管理设定记录编号';
comment on column T_SVG_REP_VERSET.RV_GRAPH_BUSINESS_TYPE
  is '图业务类型';
comment on column T_SVG_REP_VERSET.RV_GRAPH_FILE_TYPE
  is '图格式';
comment on column T_SVG_REP_VERSET.RV_STORE_POLICY
  is '存储策略（增量、替代）';
comment on column T_SVG_REP_VERSET.RV_VALIDITY
  is '有效标记';
alter table T_SVG_REP_VERSET
  add constraint PK_T_SVG_REPOSITORY_VERSET primary key (RV_ID)
  using index 
  tablespace SVG_PLATROOF
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table T_SVG_REP_VERSET_NEST
prompt ====================================
prompt
create table T_SVG_REP_VERSET_NEST
(
  RV_ID                  CHAR(36) not null,
  RV_GRAPH_BUSINESS_TYPE VARCHAR2(8),
  RV_GRAPH_FILE_TYPE     VARCHAR2(8),
  RV_STORE_POLICY        VARCHAR2(8),
  RV_VALIDITY            VARCHAR2(8)
)
tablespace SVG_PLATROOF
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table T_SVG_REP_VERSET_PLAN
prompt ====================================
prompt
create table T_SVG_REP_VERSET_PLAN
(
  RV_ID                  CHAR(36) not null,
  RV_GRAPH_BUSINESS_TYPE VARCHAR2(8),
  RV_GRAPH_FILE_TYPE     VARCHAR2(8),
  RV_STORE_POLICY        VARCHAR2(8),
  RV_VALIDITY            VARCHAR2(8)
)
tablespace SVG_PLATROOF
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table T_SVG_REP_VERSET_PSMS
prompt ====================================
prompt
create table T_SVG_REP_VERSET_PSMS
(
  RV_ID                  CHAR(36) not null,
  RV_GRAPH_BUSINESS_TYPE VARCHAR2(8),
  RV_GRAPH_FILE_TYPE     VARCHAR2(8),
  RV_STORE_POLICY        VARCHAR2(8),
  RV_VALIDITY            VARCHAR2(8)
)
tablespace SVG_PLATROOF
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table T_SVG_SYMBOL_FILEINFO
prompt ====================================
prompt
create table T_SVG_SYMBOL_FILEINFO
(
  SF_ID            VARCHAR2(36) not null,
  SF_NAME          VARCHAR2(256),
  SF_VARIETY       VARCHAR2(8),
  SF_CREATE_TIME   VARCHAR2(14),
  SF_MODIFY_TIME   VARCHAR2(14),
  SF_MODIFY_PERSON VARCHAR2(64),
  SF_CONTENT       BLOB,
  SF_VALIDITY      VARCHAR2(8),
  SF_RELEASEFLAG   VARCHAR2(8) default 0
)
tablespace SVG_PLATROOF
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
comment on table T_SVG_SYMBOL_FILEINFO
  is '图元表';
comment on column T_SVG_SYMBOL_FILEINFO.SF_ID
  is '图元编号';
comment on column T_SVG_SYMBOL_FILEINFO.SF_NAME
  is '图元名称';
comment on column T_SVG_SYMBOL_FILEINFO.SF_VARIETY
  is '图元种类';
comment on column T_SVG_SYMBOL_FILEINFO.SF_CREATE_TIME
  is '图元创建时间';
comment on column T_SVG_SYMBOL_FILEINFO.SF_MODIFY_TIME
  is '图元修改时间';
comment on column T_SVG_SYMBOL_FILEINFO.SF_MODIFY_PERSON
  is '图元修改人';
comment on column T_SVG_SYMBOL_FILEINFO.SF_CONTENT
  is '图元内容';
comment on column T_SVG_SYMBOL_FILEINFO.SF_VALIDITY
  is '有效标记';
comment on column T_SVG_SYMBOL_FILEINFO.SF_RELEASEFLAG
  is '发布标记，0表示未发布，1表示已发布';
alter table T_SVG_SYMBOL_FILEINFO
  add constraint PK_T_SVG_SYMBOL_FILEINFO primary key (SF_ID)
  using index 
  tablespace SVG_PLATROOF
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table T_SVG_SYMBOL_MODELRELA
prompt =====================================
prompt
create table T_SVG_SYMBOL_MODELRELA
(
  SM_SYMBOL_ID   VARCHAR2(36) not null,
  SM_BUSINESS_ID VARCHAR2(8) not null,
  SM_MODULE_ID   VARCHAR2(128) not null
)
tablespace SVG_PLATROOF
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
comment on table T_SVG_SYMBOL_MODELRELA
  is '图元模型关联表';
comment on column T_SVG_SYMBOL_MODELRELA.SM_SYMBOL_ID
  is '图元编号';
comment on column T_SVG_SYMBOL_MODELRELA.SM_BUSINESS_ID
  is '业务系统编号';
comment on column T_SVG_SYMBOL_MODELRELA.SM_MODULE_ID
  is '模型编号';
alter table T_SVG_SYMBOL_MODELRELA
  add constraint PK_T_SVG_SYMBOL_MODELRELA primary key (SM_SYMBOL_ID, SM_BUSINESS_ID, SM_MODULE_ID)
  using index 
  tablespace SVG_PLATROOF
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table T_SVG_SYMBOL_TEMPLATE
prompt ====================================
prompt
create table T_SVG_SYMBOL_TEMPLATE
(
  ST_ID            VARCHAR2(36) not null,
  ST_NAME          VARCHAR2(64),
  ST_VARIETY       VARCHAR2(8),
  ST_CREATE_TIME   VARCHAR2(14),
  ST_MODIFY_TIME   VARCHAR2(14),
  ST_MODIFY_PERSON VARCHAR2(64),
  ST_CONTENT       BLOB,
  ST_VALIDITY      VARCHAR2(8),
  ST_PARAM1        VARCHAR2(1024),
  ST_PARAM2        VARCHAR2(1024),
  ST_PARAM3        VARCHAR2(1024),
  ST_RELEASEFLAG   VARCHAR2(8) default 0
)
tablespace SVG_PLATROOF
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
comment on table T_SVG_SYMBOL_TEMPLATE
  is '模板表';
comment on column T_SVG_SYMBOL_TEMPLATE.ST_ID
  is '模板编号';
comment on column T_SVG_SYMBOL_TEMPLATE.ST_NAME
  is '模板名称';
comment on column T_SVG_SYMBOL_TEMPLATE.ST_VARIETY
  is '模板种类';
comment on column T_SVG_SYMBOL_TEMPLATE.ST_CREATE_TIME
  is '模板创建时间';
comment on column T_SVG_SYMBOL_TEMPLATE.ST_MODIFY_TIME
  is '模板修改时间';
comment on column T_SVG_SYMBOL_TEMPLATE.ST_MODIFY_PERSON
  is '模板修改人';
comment on column T_SVG_SYMBOL_TEMPLATE.ST_CONTENT
  is '模板内容';
comment on column T_SVG_SYMBOL_TEMPLATE.ST_VALIDITY
  is '有效标记';
comment on column T_SVG_SYMBOL_TEMPLATE.ST_PARAM1
  is '预留1';
comment on column T_SVG_SYMBOL_TEMPLATE.ST_PARAM2
  is '预留2';
comment on column T_SVG_SYMBOL_TEMPLATE.ST_PARAM3
  is '预留3';
comment on column T_SVG_SYMBOL_TEMPLATE.ST_RELEASEFLAG
  is '发布标记，0表示未发布，1表示已发布';
alter table T_SVG_SYMBOL_TEMPLATE
  add constraint PK_T_SVG_SYMBOL_TEMPLATE primary key (ST_ID)
  using index 
  tablespace SVG_PLATROOF
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table T_SVG_SYS_SETS
prompt =============================
prompt
create table T_SVG_SYS_SETS
(
  SS_ID         VARCHAR2(36) not null,
  SS_SHORT_NAME VARCHAR2(32),
  SS_NAME       VARCHAR2(128),
  SS_DESC       VARCHAR2(256),
  SS_PARAM1     VARCHAR2(256),
  SS_PARAM2     VARCHAR2(256),
  SS_PARAM3     VARCHAR2(256)
)
tablespace SVG_PLATROOF
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
comment on table T_SVG_SYS_SETS
  is '系统配置表';
comment on column T_SVG_SYS_SETS.SS_ID
  is '编号';
comment on column T_SVG_SYS_SETS.SS_SHORT_NAME
  is '配置项简称';
comment on column T_SVG_SYS_SETS.SS_NAME
  is '配置项名称';
comment on column T_SVG_SYS_SETS.SS_DESC
  is '配置项描述';
comment on column T_SVG_SYS_SETS.SS_PARAM1
  is '配置内容1';
comment on column T_SVG_SYS_SETS.SS_PARAM2
  is '配置内容2';
comment on column T_SVG_SYS_SETS.SS_PARAM3
  is '配置内容3';
alter table T_SVG_SYS_SETS
  add constraint PK_T_SVG_SYS_SETS primary key (SS_ID)
  using index 
  tablespace SVG_PLATROOF
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table T_SVG_TEMPLATE_MODELRELA
prompt =======================================
prompt
create table T_SVG_TEMPLATE_MODELRELA
(
  TM_TEMPLATE_ID VARCHAR2(36) not null,
  TM_BUSINESS_ID VARCHAR2(8) not null,
  TM_MODULE_ID   VARCHAR2(128) not null
)
tablespace SVG_PLATROOF
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
comment on table T_SVG_TEMPLATE_MODELRELA
  is '模板模型关联表';
comment on column T_SVG_TEMPLATE_MODELRELA.TM_TEMPLATE_ID
  is '模板编号';
comment on column T_SVG_TEMPLATE_MODELRELA.TM_BUSINESS_ID
  is '业务系统编号';
comment on column T_SVG_TEMPLATE_MODELRELA.TM_MODULE_ID
  is '模型编号';
alter table T_SVG_TEMPLATE_MODELRELA
  add constraint PK_T_SVG_TEMPLATE_MODELRELA primary key (TM_TEMPLATE_ID, TM_BUSINESS_ID, TM_MODULE_ID)
  using index 
  tablespace SVG_PLATROOF
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table UUM_APPLICATION
prompt ==============================
prompt
create table UUM_APPLICATION
(
  APP_ID         NUMBER not null,
  APP_SHORT_NAME VARCHAR2(20) not null,
  APP_NAME       VARCHAR2(100),
  APP_ORDER      INTEGER,
  APP_HOME_URL   VARCHAR2(255),
  APP_LOGIN_URL  VARCHAR2(255),
  APP_AUTH_URL   VARCHAR2(255),
  APP_AUTH_CODE  VARCHAR2(255),
  APP_SAME_ID    NUMBER,
  APP_NOTE       VARCHAR2(255)
)
tablespace HR30
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
alter table UUM_APPLICATION
  add constraint PK_UUM_APPLICATION primary key (APP_ID)
  using index 
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table UUM_AUTHENTICATION_INFO
prompt ======================================
prompt
create table UUM_AUTHENTICATION_INFO
(
  AI_ID        NUMBER not null,
  AI_USER_ID   VARCHAR2(64) not null,
  AI_KEY       VARCHAR2(30) not null,
  AI_IP        VARCHAR2(15) not null,
  AI_SYS_ID    NUMBER,
  AI_AUTH_DATE DATE not null,
  AI_LAST_DATE DATE not null
)
tablespace HR30
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
alter table UUM_AUTHENTICATION_INFO
  add constraint PK_UUM_AUTHENTICATION_INFO primary key (AI_ID)
  using index 
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table UUM_BUSINESSORG
prompt ==============================
prompt
create table UUM_BUSINESSORG
(
  BUSINESSORG_ID     NUMBER not null,
  BUSINESSORG_CODE   VARCHAR2(64),
  BUSINESSORG_CHAR   CHAR(1),
  BUSINESSORG_NAME   VARCHAR2(100) not null,
  BUSINESSORG_ORG_ID VARCHAR2(64),
  BUSINESSORG_STATUS CHAR(1),
  BUSINESSORG_APP_ID NUMBER,
  BUSINESSORG_NOTE   VARCHAR2(200)
)
tablespace HR30
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
comment on column UUM_BUSINESSORG.BUSINESSORG_CHAR
  is '1-全局，2-局部';
alter table UUM_BUSINESSORG
  add constraint PK_UUM_BUSINESSORG primary key (BUSINESSORG_ID)
  using index 
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table UUM_CONTROL_DOMAIN
prompt =================================
prompt
create table UUM_CONTROL_DOMAIN
(
  UUM_CONTROL_ROLE_ID         VARCHAR2(32) not null,
  UUM_CONTROL_ROLE_NAME       VARCHAR2(32) not null,
  UUM_CONTROL_ROLE_APPID      VARCHAR2(32) not null,
  UUM_CONTROL_ROLE_CREATEROLE VARCHAR2(32) not null,
  UUM_CONTROL_ROLE_CREATEER   VARCHAR2(32) not null,
  CREATEID                    VARCHAR2(32),
  CREATEDATE                  DATE,
  UPDATEID                    VARCHAR2(32),
  UPDATEDATE                  DATE,
  USER_ORGID                  VARCHAR2(32),
  USER_UNITID                 VARCHAR2(32)
)
tablespace HR30
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table UUM_CONTROL_ROLE_USER
prompt ====================================
prompt
create table UUM_CONTROL_ROLE_USER
(
  CONTROL_ROLE_ID     VARCHAR2(32) not null,
  USER_ID             VARCHAR2(32) not null,
  CONTROL_ROLE_SEQKEY VARCHAR2(32) not null,
  CONTROL_ROLE_ORDER  VARCHAR2(32),
  CREATEID            VARCHAR2(32),
  CREATEDATE          DATE,
  UPDATEID            VARCHAR2(32),
  UPDATEDATE          DATE,
  USER_ORGID          VARCHAR2(32),
  USER_UNITID         VARCHAR2(32)
)
tablespace HR30
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table UUM_DOMAIN_ROLE_MAP
prompt ==================================
prompt
create table UUM_DOMAIN_ROLE_MAP
(
  ROLE_DOMAIN_SEQKEY VARCHAR2(64) not null,
  CONTROL_ROLE_ID    VARCHAR2(64) not null,
  ROLE_ID            VARCHAR2(64) not null,
  CREATEID           VARCHAR2(64),
  CREATEDATE         DATE,
  UPDATEID           VARCHAR2(64),
  UPDATEDATE         DATE,
  USER_ORGID         VARCHAR2(64),
  USER_UNITID        VARCHAR2(64)
)
tablespace HR30
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table UUM_GRADE_ROLE
prompt =============================
prompt
create table UUM_GRADE_ROLE
(
  ROLE_ID             VARCHAR2(64) not null,
  ROLE_NAME           VARCHAR2(100) not null,
  ROLE_DESC           VARCHAR2(4000),
  ROLE_CHAR           CHAR(1),
  ROLE_ADMIN_ID       VARCHAR2(64),
  ROLE_CREATE_ID      VARCHAR2(64),
  ROLE_CREATE_DATE    DATE,
  ROLE_UPDATE_ID      VARCHAR2(64),
  ROLE_UPDATE_DATE    DATE,
  ROLE_PARENT_ROLE_ID VARCHAR2(64),
  ROLE_TYPE           VARCHAR2(64)
)
tablespace HR30
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
alter table UUM_GRADE_ROLE
  add constraint UUM_GRADE_ROLE_ID primary key (ROLE_ID)
  using index 
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table UUM_LOG
prompt ======================
prompt
create table UUM_LOG
(
  LOG_ID      NUMBER not null,
  LOG_TYPE    INTEGER,
  LOG_USER_ID VARCHAR2(64),
  LOG_TIME    DATE,
  LOG_NOTE    VARCHAR2(200)
)
tablespace HR30
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
alter table UUM_LOG
  add constraint PK_UUM_LOG primary key (LOG_ID)
  using index 
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table UUM_LOGIN_LOG
prompt ============================
prompt
create table UUM_LOGIN_LOG
(
  LOGIN_LOG_ID    NUMBER not null,
  LOGIN_USER_ID   VARCHAR2(64),
  LOGIN_INTIME    DATE,
  LOGIN_OUTTIME   DATE,
  LOGIN_HOST_NAME VARCHAR2(64),
  LOGIN_HOST_IP   VARCHAR2(64),
  LOGIN_NOTE      VARCHAR2(64)
)
tablespace HR30
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
alter table UUM_LOGIN_LOG
  add constraint PK_UUM_LOGIN_LOG primary key (LOGIN_LOG_ID)
  using index 
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table UUM_LOG_RECORK
prompt =============================
prompt
create table UUM_LOG_RECORK
(
  LOG_SEQKEY      VARCHAR2(64) not null,
  LOG_USER        VARCHAR2(64) not null,
  LOG_IP          VARCHAR2(64) not null,
  LOG_TIME        DATE not null,
  LOG_USER_NAME   VARCHAR2(64) not null,
  LOG_USERNAME    VARCHAR2(64),
  LOG_USER_WORKNO VARCHAR2(64),
  LOGOUT_TIME     DATE
)
tablespace HR30
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table UUM_MENU
prompt =======================
prompt
create table UUM_MENU
(
  MENU_ID          VARCHAR2(255) not null,
  MENU_APP_ID      NUMBER,
  MENU_RGT_ID      VARCHAR2(255),
  MENU_NAME        VARCHAR2(50) not null,
  MENU_URL         VARCHAR2(500),
  MENU_PARENT_ID   VARCHAR2(255),
  MENU_IS_LEAF     INTEGER not null,
  MENU_LAYER       VARCHAR2(255),
  MENU_ORDER       INTEGER,
  MENU_STATUS      INTEGER,
  MENU_NOTE        VARCHAR2(200),
  MENU_CREATE_ID   VARCHAR2(64),
  MENU_CREATE_DATE DATE,
  MENU_UPDATE_ID   VARCHAR2(64),
  MENU_UPDATE_DATE DATE
)
tablespace HR30
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
comment on table UUM_MENU
  is '没有分级管理的概念';

prompt
prompt Creating table UUM_ORG
prompt ======================
prompt
create table UUM_ORG
(
  ORG_ID            VARCHAR2(64) not null,
  ORG_CODE          VARCHAR2(64),
  ORG_NAME          VARCHAR2(100) not null,
  ORG_SYSTEM_CODE   VARCHAR2(64) not null,
  ORG_LEVEL         CHAR(2),
  ORG_CITY          CHAR(6),
  ORG_LAYER         VARCHAR2(255),
  ORG_PARENT_ID     VARCHAR2(64) not null,
  ORG_IS_LEAF       INTEGER,
  ORG_RELATION      CHAR(1),
  ORG_MANAGER       VARCHAR2(50),
  ORG_MANAGER_PHONE VARCHAR2(50),
  ORG_ADDRESS       VARCHAR2(255),
  ORG_BILL_ADDRESS  VARCHAR2(255),
  ORG_POST_CODE     VARCHAR2(10),
  ORG_TELEPHONE     VARCHAR2(50),
  ORG_STATUS        INTEGER,
  ORG_NOTE          VARCHAR2(200),
  ORG_UNITE         VARCHAR2(255),
  ORG_REPORT_ID     VARCHAR2(64),
  ORG_ADMIN_ID      VARCHAR2(64),
  ORG_ORDER         INTEGER,
  ORG_CREATE_ID     VARCHAR2(64),
  ORG_CREATE_DATE   DATE,
  ORG_UPDATE_ID     VARCHAR2(64),
  ORG_UPDATE_DATE   DATE,
  ORG_UNUSED1       VARCHAR2(200),
  ORG_UNUSED2       VARCHAR2(200),
  ORG_UNUSED3       VARCHAR2(64),
  ORG_UNUSED4       VARCHAR2(64)
)
tablespace HR30
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
comment on column UUM_ORG.ORG_SYSTEM_CODE
  is '根据组织机构当前的层次来生成系统编码';
comment on column UUM_ORG.ORG_RELATION
  is '1-直属，2-隶属';
alter table UUM_ORG
  add constraint PK_UUM_ORG primary key (ORG_ID)
  using index 
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table UUM_RIGHT
prompt ========================
prompt
create table UUM_RIGHT
(
  RGT_ID              VARCHAR2(255) not null,
  RGT_CODE            VARCHAR2(255),
  RGT_APP_ID          NUMBER,
  RGT_PARENT_ID       VARCHAR2(255),
  RGT_LAYER           VARCHAR2(255),
  RGT_IS_LEAF         INTEGER,
  RGT_NAME            VARCHAR2(50) not null,
  RGT_TYPE            VARCHAR2(50),
  RGT_URL             VARCHAR2(500),
  RGT_ORDER           INTEGER,
  RGT_STATUS          INTEGER,
  RGT_NOTE            VARCHAR2(200),
  RGT_FUNCTION_NAME   VARCHAR2(100),
  RGT_FUNCTION_ACTION VARCHAR2(100),
  RGT_UNUSED1         VARCHAR2(100),
  RGT_UNUSED2         VARCHAR2(200)
)
tablespace HR30
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
comment on column UUM_RIGHT.RGT_TYPE
  is '1:菜单权限
2:功能权限
3:数据权限（2006-04-22讨论后取消）';
alter table UUM_RIGHT
  add constraint PK_UUM_RIGHT primary key (RGT_ID)
  using index 
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table UUM_ROLE
prompt =======================
prompt
create table UUM_ROLE
(
  ROLE_ID          NUMBER not null,
  ROLE_NAME        VARCHAR2(100) not null,
  ROLE_DESC        VARCHAR2(4000),
  ROLE_CHAR        CHAR(1),
  ROLE_ADMIN_ID    VARCHAR2(64),
  ROLE_CREATE_ID   VARCHAR2(64),
  ROLE_CREATE_DATE DATE,
  ROLE_UPDATE_ID   VARCHAR2(64),
  ROLE_UPDATE_DATE DATE
)
tablespace HR30
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
comment on column UUM_ROLE.ROLE_CHAR
  is '1-全局，2-局部';
alter table UUM_ROLE
  add constraint PK_UUM_ROLE primary key (ROLE_ID)
  using index 
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table UUM_ROLE_DOMAIN_MAP
prompt ==================================
prompt
create table UUM_ROLE_DOMAIN_MAP
(
  ROLE_DOMAIN_SEQKEY VARCHAR2(32) not null,
  CONTROL_ROLE_ID    VARCHAR2(32) not null,
  CONTROL_BASEVALUE  VARCHAR2(32) not null,
  CONTROL_TYPE       VARCHAR2(32),
  CREATEID           VARCHAR2(32),
  CREATEDATE         DATE,
  UPDATEID           VARCHAR2(32),
  UPDATEDATE         DATE,
  USER_ORGID         VARCHAR2(32),
  USER_UNITID        VARCHAR2(32)
)
tablespace HR30
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table UUM_R_BUSINESSORG_ORG
prompt ====================================
prompt
create table UUM_R_BUSINESSORG_ORG
(
  BOO_ID             NUMBER not null,
  BOO_BUSINESSORG_ID NUMBER not null,
  BOO_ORG_ID         VARCHAR2(64) not null
)
tablespace HR30
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
alter table UUM_R_BUSINESSORG_ORG
  add constraint PK_UUM_R_BUSINESSORG_ORG primary key (BOO_ID)
  using index 
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table UUM_R_GRADEROLE_RIGHT
prompt ====================================
prompt
create table UUM_R_GRADEROLE_RIGHT
(
  ROLE_RIGHT_ID   VARCHAR2(64) not null,
  RR_GRADEROLE_ID VARCHAR2(64) not null,
  RR_RGT_ID       VARCHAR2(64) not null,
  RR_IS_FUNC      VARCHAR2(64)
)
tablespace HR30
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table UUM_R_ROLE_APP
prompt =============================
prompt
create table UUM_R_ROLE_APP
(
  ROLE_APP_ID NUMBER(6),
  RA_APP_ID   NUMBER not null,
  RA_ROLE_ID  NUMBER not null,
  RA_IS_FUNC  INTEGER
)
tablespace HR30
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
alter table UUM_R_ROLE_APP
  add constraint PK_UUM_R_ROLE_APP primary key (RA_APP_ID, RA_ROLE_ID)
  using index 
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table UUM_R_ROLE_RIGHT
prompt ===============================
prompt
create table UUM_R_ROLE_RIGHT
(
  ROLE_RIGHT_ID NUMBER(6) not null,
  RR_ROLE_ID    NUMBER not null,
  RR_RGT_ID     VARCHAR2(255) not null,
  RR_IS_FUNC    INTEGER
)
tablespace HR30
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
comment on column UUM_R_ROLE_RIGHT.RR_IS_FUNC
  is '默认是有权限的';
alter table UUM_R_ROLE_RIGHT
  add constraint PK_UUM_R_ROLE_RIGHT primary key (ROLE_RIGHT_ID)
  using index 
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table UUM_R_ROLE_SUB_RIGHT
prompt ===================================
prompt
create table UUM_R_ROLE_SUB_RIGHT
(
  RSR_ID      NUMBER not null,
  RSR_ROLE_ID NUMBER not null,
  RSR_SR_ID   NUMBER not null,
  RSR_IS_FUNC INTEGER
)
tablespace HR30
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
comment on column UUM_R_ROLE_SUB_RIGHT.RSR_IS_FUNC
  is '默认是有权限的';
alter table UUM_R_ROLE_SUB_RIGHT
  add constraint PK_UUM_R_ROLE_SUB_RIGHT primary key (RSR_ID)
  using index 
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table UUM_R_USER_GRADEROLE
prompt ===================================
prompt
create table UUM_R_USER_GRADEROLE
(
  USER_ROLE_ID    VARCHAR2(64),
  UR_USER_ID      VARCHAR2(64) not null,
  UR_GRADEROLE_ID VARCHAR2(64) not null
)
tablespace HR30
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table UUM_R_USER_ROLE
prompt ==============================
prompt
create table UUM_R_USER_ROLE
(
  USER_ROLE_ID NUMBER(6),
  UR_USER_ID   VARCHAR2(64) not null,
  UR_ROLE_ID   NUMBER not null
)
tablespace HR30
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
alter table UUM_R_USER_ROLE
  add constraint PK_UUM_R_USER_ROLE primary key (UR_USER_ID, UR_ROLE_ID)
  using index 
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table UUM_SUB_RIGHT
prompt ============================
prompt
create table UUM_SUB_RIGHT
(
  SR_ID     NUMBER not null,
  SR_RGT_ID NUMBER not null,
  SR_CODE   VARCHAR2(255),
  SR_NAME   VARCHAR2(50) not null,
  SR_NOTE   VARCHAR2(200),
  COLUMN_6  CHAR(10)
)
tablespace HR30
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
alter table UUM_SUB_RIGHT
  add constraint PK_UUM_SUB_RIGHT primary key (SR_ID)
  using index 
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table UUM_USER
prompt =======================
prompt
create table UUM_USER
(
  USER_ID                 VARCHAR2(64) not null,
  USER_NAME               VARCHAR2(100) not null,
  USER_SYSTEM_CODE        VARCHAR2(100),
  USER_ORG_ID             VARCHAR2(64),
  USER_LOGIN_NAME         VARCHAR2(50) not null,
  USER_LOGIN_PASSWD       VARCHAR2(50),
  USER_WORK_NO            VARCHAR2(64),
  USER_PHONE              VARCHAR2(50),
  USER_MOBILE             VARCHAR2(50),
  USER_EMAIL              VARCHAR2(100),
  USER_SEX                CHAR(2),
  USER_BIRTHDAY           DATE,
  USER_WORKED             VARCHAR2(255),
  USER_EDUCATION          CHAR(2),
  USER_ORIGIN             CHAR(6),
  USER_TITLE              VARCHAR2(50),
  USER_WORK_LIMIT         VARCHAR2(255),
  USER_IDENT_NO           VARCHAR2(50),
  USER_IN_DATE            DATE,
  USER_ADDRESS            VARCHAR2(255),
  USER_POSTAL             VARCHAR2(20),
  USER_STATUS             INTEGER not null,
  USER_OUT                INTEGER,
  USER_AREA               CHAR(6),
  USER_OUT_DATE           DATE,
  USER_LOGIN_DATE         DATE,
  USER_NOTE               VARCHAR2(4000),
  USER_PASSWD_CHANGE_DATE DATE,
  USER_UNLOCK_DATE        DATE,
  USER_TYPE               INTEGER,
  USER_LDAP_URL           VARCHAR2(255),
  USER_ORDER              INTEGER,
  USER_CREATE_ID          VARCHAR2(64),
  USER_CREATE_DATE        DATE,
  USER_UPDATE_ID          VARCHAR2(64),
  USER_UPDATE_DATE        DATE,
  USER_ANOTHER_JOB1       VARCHAR2(64),
  USER_ANOTHER_JOB2       VARCHAR2(64),
  USER_ANOTHER_JOB3       VARCHAR2(64),
  USER_UNUSED1            VARCHAR2(255),
  USER_UNUSED2            VARCHAR2(255),
  USER_UNUSED3            NUMBER,
  USER_UNUSED4            NUMBER
)
tablespace HR30
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
comment on column UUM_USER.USER_SYSTEM_CODE
  is '所在单位或部门的系统编码＋当前用户顺序码。';
comment on column UUM_USER.USER_ORG_ID
  is '除超级系统管理员外，其它用户（包括系统管理员和普通）均需指定该值。';
comment on column UUM_USER.USER_LOGIN_NAME
  is '具有唯一性约束';
comment on column UUM_USER.USER_LOGIN_PASSWD
  is '采用md5加密';
comment on column UUM_USER.USER_SEX
  is '1:女性
2:男性';
comment on column UUM_USER.USER_STATUS
  is '1:启用 在不考虑分级管理等约束的情况下，启用状态的用户被所有业务操作可见，用户新增时的状态默认为启用状态。
2:暂用 表明用户是临时的，如企业雇佣的临时工，暂用状态的用户只在一段时间内有效，并且不能登录系统。
3:停用 表明需要使用该用户的历史信息，如已退休的用户，在统计时会用到该用户的信息，停用状态的用户不能登录系统。
启用，暂用，停用状态的用户可相互转换。';
comment on column UUM_USER.USER_TYPE
  is '1:普通用户 不具备管理用户和行政机构的权利
2:系统管理员 具备分级管理行政机构和用户的权利
9:超级系统管理员 能管理所有的用户和所有的行政机构';
alter table UUM_USER
  add constraint PK_UUM_USER primary key (USER_ID)
  using index 
  tablespace HR30
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table WORKFLOW_ACTINS_HISTORY
prompt ======================================
prompt
create table WORKFLOW_ACTINS_HISTORY
(
  ACTIVITYINSTANCEID       NUMBER not null,
  ACTIVITYID               NUMBER not null,
  INSTANCEID               NUMBER,
  PROCESSID                NUMBER not null,
  ACTIVITYTYPE             NUMBER,
  ACTIVITYNAME             VARCHAR2(40),
  DESCRIPTION              VARCHAR2(200),
  PREACTIVITYINSTANCEID    VARCHAR2(200),
  NEXTACTIVITYINSTANCEID   VARCHAR2(200),
  PARENTACTIVITYINSTANCEID VARCHAR2(200),
  CHILDACTIVITYINSTANCEID  VARCHAR2(200),
  ACTIVITYSTATUS           NUMBER,
  TRANSITIONTYPE           NUMBER,
  ROLLBACKFLAG             NUMBER,
  CREATOR                  VARCHAR2(100),
  CREATEDATE               DATE,
  BEGINDATE                DATE,
  ENDUSER                  VARCHAR2(100),
  ENDDATE                  DATE,
  ACTIVITYISTIMEOUT        NUMBER,
  ACTIVITYTIMEOUTNUM       NUMBER,
  ACTIVITYTIMEOUTDATE      DATE,
  ACTIVITYPARTICIPANTTYPE  NUMBER,
  ACTIVITYISBYORDER        NUMBER,
  ACTIVITYJOINMODE         VARCHAR2(10),
  ACTIVITYSPLITMODE        VARCHAR2(10),
  ACTIVITYCANROLLBACK      NUMBER,
  ACTIVITYCANRECLAIM       NUMBER,
  APP                      VARCHAR2(100),
  UNITID                   VARCHAR2(100)
)
tablespace USERS
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
comment on table WORKFLOW_ACTINS_HISTORY
  is '活动实例历史表';
comment on column WORKFLOW_ACTINS_HISTORY.ACTIVITYINSTANCEID
  is '活动实例ID';
comment on column WORKFLOW_ACTINS_HISTORY.ACTIVITYID
  is '活动定义ID';
comment on column WORKFLOW_ACTINS_HISTORY.INSTANCEID
  is '流程实例ID';
comment on column WORKFLOW_ACTINS_HISTORY.PROCESSID
  is '流程定义ID(某个业务流程之某版本的流程定义ID)';
comment on column WORKFLOW_ACTINS_HISTORY.ACTIVITYTYPE
  is '活动类型';
comment on column WORKFLOW_ACTINS_HISTORY.ACTIVITYNAME
  is '活动名称(保留的冗余字段)';
comment on column WORKFLOW_ACTINS_HISTORY.DESCRIPTION
  is '描述';
comment on column WORKFLOW_ACTINS_HISTORY.PREACTIVITYINSTANCEID
  is '前一活动实例ID';
comment on column WORKFLOW_ACTINS_HISTORY.NEXTACTIVITYINSTANCEID
  is '下一环节所有活动实例ID，用半角逗号分割，如"11,23,24"';
comment on column WORKFLOW_ACTINS_HISTORY.PARENTACTIVITYINSTANCEID
  is '所有父活动实例';
comment on column WORKFLOW_ACTINS_HISTORY.CHILDACTIVITYINSTANCEID
  is '所有子活动实例';
comment on column WORKFLOW_ACTINS_HISTORY.ACTIVITYSTATUS
  is '活动实例状态';
comment on column WORKFLOW_ACTINS_HISTORY.TRANSITIONTYPE
  is '跳转类型 1-自动 2-手工';
comment on column WORKFLOW_ACTINS_HISTORY.ROLLBACKFLAG
  is '是否是执行"回退"操作创建的活动实例标记位';
comment on column WORKFLOW_ACTINS_HISTORY.CREATOR
  is '创建人ID';
comment on column WORKFLOW_ACTINS_HISTORY.CREATEDATE
  is '创建日期';
comment on column WORKFLOW_ACTINS_HISTORY.BEGINDATE
  is '开始日期';
comment on column WORKFLOW_ACTINS_HISTORY.ENDUSER
  is '结束人ID';
comment on column WORKFLOW_ACTINS_HISTORY.ENDDATE
  is '结束日期';
comment on column WORKFLOW_ACTINS_HISTORY.ACTIVITYISTIMEOUT
  is '活动是否超时';
comment on column WORKFLOW_ACTINS_HISTORY.ACTIVITYTIMEOUTNUM
  is '活动超时数';
comment on column WORKFLOW_ACTINS_HISTORY.ACTIVITYTIMEOUTDATE
  is '活动超时日期';
comment on column WORKFLOW_ACTINS_HISTORY.ACTIVITYPARTICIPANTTYPE
  is '活动参与类型 1-独占；2－多人';
comment on column WORKFLOW_ACTINS_HISTORY.ACTIVITYISBYORDER
  is '活动是否顺序执行';
comment on column WORKFLOW_ACTINS_HISTORY.ACTIVITYJOINMODE
  is '聚合模式';
comment on column WORKFLOW_ACTINS_HISTORY.ACTIVITYSPLITMODE
  is '分支模式';
comment on column WORKFLOW_ACTINS_HISTORY.ACTIVITYCANROLLBACK
  is '活动可否回退 1-可以；0-不可';
comment on column WORKFLOW_ACTINS_HISTORY.APP
  is '业务模块';
comment on column WORKFLOW_ACTINS_HISTORY.UNITID
  is '机构ID';
alter table WORKFLOW_ACTINS_HISTORY
  add constraint PK_WORKFLOW_AC2 primary key (ACTIVITYINSTANCEID)
  using index 
  tablespace USERS
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table WORKFLOW_ACTIVITY
prompt ================================
prompt
create table WORKFLOW_ACTIVITY
(
  ACTIVITYID              NUMBER not null,
  PROCESSID               NUMBER not null,
  ACTIVITYTYPE            NUMBER,
  ACTIVITYNAME            VARCHAR2(40),
  DESCRIPTION             VARCHAR2(200),
  ACTIVITYSTATUS          NUMBER,
  ACTIVITYPARTICIPANTTYPE NUMBER,
  ACTIVITYISBYORDER       NUMBER,
  ACTIVITYJOINMODE        VARCHAR2(10),
  ACTIVITYSPLITMODE       VARCHAR2(10),
  ACTIVITYCANROLLBACK     NUMBER,
  ACTIVITYCANWITHDRAWAL   NUMBER,
  MODULE_TYPE             VARCHAR2(1),
  APP                     VARCHAR2(100),
  CONDITION               VARCHAR2(500),
  DISPLAYORDER            NUMBER,
  X                       NUMBER(12,4),
  Y                       NUMBER(12,4),
  UNITID                  VARCHAR2(100)
)
tablespace USERS
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
comment on table WORKFLOW_ACTIVITY
  is '活动定义';
comment on column WORKFLOW_ACTIVITY.ACTIVITYID
  is '活动定义ID';
comment on column WORKFLOW_ACTIVITY.PROCESSID
  is '流程定义ID(某个业务流程之某版本的流程定义ID)';
comment on column WORKFLOW_ACTIVITY.ACTIVITYTYPE
  is '活动类型';
comment on column WORKFLOW_ACTIVITY.ACTIVITYNAME
  is '活动名称';
comment on column WORKFLOW_ACTIVITY.DESCRIPTION
  is '描述';
comment on column WORKFLOW_ACTIVITY.ACTIVITYSTATUS
  is '活动状态(虚拟删除标志位)';
comment on column WORKFLOW_ACTIVITY.ACTIVITYPARTICIPANTTYPE
  is '多人参与类型 0-否；1－是(用于活动是否多工作项)';
comment on column WORKFLOW_ACTIVITY.ACTIVITYISBYORDER
  is '活动是否顺序执行 0-否；1－是';
comment on column WORKFLOW_ACTIVITY.ACTIVITYJOINMODE
  is '聚合模式';
comment on column WORKFLOW_ACTIVITY.ACTIVITYSPLITMODE
  is '分支模式 1-可以；0-不可';
comment on column WORKFLOW_ACTIVITY.ACTIVITYCANROLLBACK
  is '活动可否回退 1-可以；0-不可(意为：活动可否被回退)';
comment on column WORKFLOW_ACTIVITY.ACTIVITYCANWITHDRAWAL
  is '活动可否撤回(意为：活动可否被撤回)';
comment on column WORKFLOW_ACTIVITY.MODULE_TYPE
  is '业务模块类型，在 WF_MODULE_TYPES 表中维护';
comment on column WORKFLOW_ACTIVITY.APP
  is '关联的URL，该标签为“业务模块”';
comment on column WORKFLOW_ACTIVITY.CONDITION
  is '完成条件';
comment on column WORKFLOW_ACTIVITY.DISPLAYORDER
  is '显示顺序';
comment on column WORKFLOW_ACTIVITY.X
  is '坐标X';
comment on column WORKFLOW_ACTIVITY.Y
  is '坐标Y';
comment on column WORKFLOW_ACTIVITY.UNITID
  is '机构ID';
alter table WORKFLOW_ACTIVITY
  add constraint PK_WORKFLOW_ACT primary key (ACTIVITYID)
  using index 
  tablespace USERS
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table WORKFLOW_ACTIVITYINSTANCE
prompt ========================================
prompt
create table WORKFLOW_ACTIVITYINSTANCE
(
  ACTIVITYINSTANCEID       NUMBER not null,
  ACTIVITYID               NUMBER not null,
  INSTANCEID               NUMBER not null,
  PROCESSID                NUMBER not null,
  ACTIVITYTYPE             NUMBER,
  ACTIVITYNAME             VARCHAR2(40),
  DESCRIPTION              VARCHAR2(200),
  PREACTIVITYINSTANCEID    VARCHAR2(200),
  NEXTACTIVITYINSTANCEID   VARCHAR2(200),
  PARENTACTIVITYINSTANCEID VARCHAR2(200),
  CHILDACTIVITYINSTANCEID  VARCHAR2(200),
  ACTIVITYSTATUS           NUMBER,
  TRANSITIONTYPE           NUMBER,
  ROLLBACKFLAG             NUMBER,
  CREATOR                  VARCHAR2(100),
  CREATEDATE               DATE,
  BEGINDATE                DATE,
  ENDUSER                  VARCHAR2(100),
  ENDDATE                  DATE,
  ACTIVITYISTIMEOUT        NUMBER,
  ACTIVITYTIMEOUTNUM       NUMBER,
  ACTIVITYTIMEOUTDATE      DATE,
  ACTIVITYPARTICIPANTTYPE  NUMBER,
  ACTIVITYISBYORDER        NUMBER,
  ACTIVITYJOINMODE         VARCHAR2(10),
  ACTIVITYSPLITMODE        VARCHAR2(10),
  ACTIVITYCANROLLBACK      NUMBER,
  ACTIVITYCANRECLAIM       NUMBER,
  APP                      VARCHAR2(100),
  UNITID                   VARCHAR2(100)
)
tablespace USERS
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
comment on table WORKFLOW_ACTIVITYINSTANCE
  is '活动实例';
comment on column WORKFLOW_ACTIVITYINSTANCE.ACTIVITYINSTANCEID
  is '活动实例ID';
comment on column WORKFLOW_ACTIVITYINSTANCE.ACTIVITYID
  is '活动定义ID';
comment on column WORKFLOW_ACTIVITYINSTANCE.INSTANCEID
  is '流程实例ID';
comment on column WORKFLOW_ACTIVITYINSTANCE.PROCESSID
  is '流程定义ID(某个业务流程之某版本的流程定义ID)';
comment on column WORKFLOW_ACTIVITYINSTANCE.ACTIVITYTYPE
  is '活动类型';
comment on column WORKFLOW_ACTIVITYINSTANCE.ACTIVITYNAME
  is '活动名称(保留的冗余字段)';
comment on column WORKFLOW_ACTIVITYINSTANCE.DESCRIPTION
  is '活动实例描述';
comment on column WORKFLOW_ACTIVITYINSTANCE.PREACTIVITYINSTANCEID
  is '前一环节所有活动实例ID，用半角逗号分割，如"11,23,24"';
comment on column WORKFLOW_ACTIVITYINSTANCE.NEXTACTIVITYINSTANCEID
  is '下一环节所有活动实例ID，用半角逗号分割，如"11,23,24"';
comment on column WORKFLOW_ACTIVITYINSTANCE.PARENTACTIVITYINSTANCEID
  is '所有父活动实例';
comment on column WORKFLOW_ACTIVITYINSTANCE.CHILDACTIVITYINSTANCEID
  is '所有子活动实例';
comment on column WORKFLOW_ACTIVITYINSTANCE.ACTIVITYSTATUS
  is '活动实例状态';
comment on column WORKFLOW_ACTIVITYINSTANCE.TRANSITIONTYPE
  is '跳转类型 1-自动 2-手工';
comment on column WORKFLOW_ACTIVITYINSTANCE.ROLLBACKFLAG
  is '是否是执行"回退"操作创建的活动实例标记位';
comment on column WORKFLOW_ACTIVITYINSTANCE.CREATOR
  is '创建人ID';
comment on column WORKFLOW_ACTIVITYINSTANCE.CREATEDATE
  is '创建日期';
comment on column WORKFLOW_ACTIVITYINSTANCE.BEGINDATE
  is '开始日期';
comment on column WORKFLOW_ACTIVITYINSTANCE.ENDUSER
  is '结束人ID';
comment on column WORKFLOW_ACTIVITYINSTANCE.ENDDATE
  is '结束日期';
comment on column WORKFLOW_ACTIVITYINSTANCE.ACTIVITYISTIMEOUT
  is '活动是否超时';
comment on column WORKFLOW_ACTIVITYINSTANCE.ACTIVITYTIMEOUTNUM
  is '活动超时数';
comment on column WORKFLOW_ACTIVITYINSTANCE.ACTIVITYTIMEOUTDATE
  is '活动超时日期';
comment on column WORKFLOW_ACTIVITYINSTANCE.ACTIVITYPARTICIPANTTYPE
  is '活动参与类型 1-独占；2－多人';
comment on column WORKFLOW_ACTIVITYINSTANCE.ACTIVITYISBYORDER
  is '活动是否顺序执行';
comment on column WORKFLOW_ACTIVITYINSTANCE.ACTIVITYJOINMODE
  is '聚合模式';
comment on column WORKFLOW_ACTIVITYINSTANCE.ACTIVITYSPLITMODE
  is '分支模式';
comment on column WORKFLOW_ACTIVITYINSTANCE.ACTIVITYCANROLLBACK
  is '活动可否回退 1-可以；0-不可';
comment on column WORKFLOW_ACTIVITYINSTANCE.ACTIVITYCANRECLAIM
  is '活动可否收回 1-可以；0-不可';
comment on column WORKFLOW_ACTIVITYINSTANCE.APP
  is '业务模块';
comment on column WORKFLOW_ACTIVITYINSTANCE.UNITID
  is '机构ID';

prompt
prompt Creating table WORKFLOW_CONSIGN
prompt ===============================
prompt
create table WORKFLOW_CONSIGN
(
  CONSIGNID        NUMBER not null,
  ACTIVITYID       NUMBER,
  PROCESSID        NUMBER,
  CONSIGNER        VARCHAR2(100),
  CONSIGNEE        VARCHAR2(100),
  CONSIGNBEGINTIME DATE,
  CONSIGNENDTIME   DATE,
  CONSIGNREASON    VARCHAR2(400),
  CONSIGNDATE      DATE,
  CONSIGNSTATUS    NUMBER,
  UNITID           VARCHAR2(100)
)
tablespace USERS
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
comment on table WORKFLOW_CONSIGN
  is '委托';
comment on column WORKFLOW_CONSIGN.CONSIGNID
  is '委托ID';
comment on column WORKFLOW_CONSIGN.ACTIVITYID
  is '活动定义ID';
comment on column WORKFLOW_CONSIGN.PROCESSID
  is '流程定义ID';
comment on column WORKFLOW_CONSIGN.CONSIGNER
  is '委托人';
comment on column WORKFLOW_CONSIGN.CONSIGNEE
  is '被委托人';
comment on column WORKFLOW_CONSIGN.CONSIGNBEGINTIME
  is '委托开始时间';
comment on column WORKFLOW_CONSIGN.CONSIGNENDTIME
  is '委托结束时间';
comment on column WORKFLOW_CONSIGN.CONSIGNREASON
  is '委托原因';
comment on column WORKFLOW_CONSIGN.CONSIGNDATE
  is '委托日期';
comment on column WORKFLOW_CONSIGN.CONSIGNSTATUS
  is '委托状态';
comment on column WORKFLOW_CONSIGN.UNITID
  is '机构ID';
alter table WORKFLOW_CONSIGN
  add constraint PK_WORKFLOW_CON primary key (CONSIGNID)
  using index 
  tablespace USERS
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table WORKFLOW_DEF_ASSOCIATION
prompt =======================================
prompt
create table WORKFLOW_DEF_ASSOCIATION
(
  ID                    NUMBER not null,
  MAIN_BUSINESSID       VARCHAR2(40),
  ACTIVITYID            NUMBER,
  SUB_FUNCTIONMODELNAME VARCHAR2(64),
  RUNTYPE               VARCHAR2(20)
)
tablespace USERS
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
alter table WORKFLOW_DEF_ASSOCIATION
  add constraint PK_WORKFLOW_DEF_ASSOCIATION primary key (ID)
  using index 
  tablespace USERS
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table WORKFLOW_EVENT
prompt =============================
prompt
create table WORKFLOW_EVENT
(
  EVENT_ID      NUMBER not null,
  EVENT_NAME    VARCHAR2(64) not null,
  EVENT_ALIAS   VARCHAR2(100),
  EVENT_TYPE    VARCHAR2(64),
  DESCRIPTION   VARCHAR2(1000),
  CLASS_NAME    VARCHAR2(100),
  METHOD_NAME   VARCHAR2(50),
  SQL           VARCHAR2(2000),
  PROCEDURE     VARCHAR2(50),
  WEBSERVICE    VARCHAR2(100),
  RETURNED_TYPE VARCHAR2(50),
  CREATOR       VARCHAR2(100),
  CREATE_TIME   DATE
)
tablespace USERS
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
comment on table WORKFLOW_EVENT
  is '事件';
comment on column WORKFLOW_EVENT.EVENT_ID
  is '事件ID';
comment on column WORKFLOW_EVENT.EVENT_NAME
  is '事件注册名称(英文逻辑名)';
comment on column WORKFLOW_EVENT.EVENT_ALIAS
  is '事件名称(中文名)';
comment on column WORKFLOW_EVENT.EVENT_TYPE
  is '事件类型，由事件类型表维护 (Java方法，SQL语句，存储过程等)';
comment on column WORKFLOW_EVENT.DESCRIPTION
  is '事件描述';
comment on column WORKFLOW_EVENT.CLASS_NAME
  is '全类名';
comment on column WORKFLOW_EVENT.METHOD_NAME
  is '方法名';
comment on column WORKFLOW_EVENT.SQL
  is 'SQL语句';
comment on column WORKFLOW_EVENT.PROCEDURE
  is '过程名';
comment on column WORKFLOW_EVENT.WEBSERVICE
  is 'WebService';
comment on column WORKFLOW_EVENT.RETURNED_TYPE
  is '返回值类型';
comment on column WORKFLOW_EVENT.CREATOR
  is '创建人ID';
comment on column WORKFLOW_EVENT.CREATE_TIME
  is '创建时间';
alter table WORKFLOW_EVENT
  add constraint PK_WORKFLOW_EVE primary key (EVENT_ID)
  using index 
  tablespace USERS
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table WORKFLOW_EVENT_BIND
prompt ==================================
prompt
create table WORKFLOW_EVENT_BIND
(
  BIND_ID             NUMBER not null,
  EVENT_ID            NUMBER not null,
  ASSOCIATED_OBJECT   VARCHAR2(64) not null,
  ASSOCIATED_OBJ_TYPE VARCHAR2(64) not null,
  EVENT_TRIGER_TYPE   VARCHAR2(64),
  EVENT_TRIGER_MODE   VARCHAR2(64),
  DISPLAY_ORDER       NUMBER
)
tablespace USERS
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
comment on table WORKFLOW_EVENT_BIND
  is '事件绑定';
comment on column WORKFLOW_EVENT_BIND.BIND_ID
  is '绑定ID，关键字';
comment on column WORKFLOW_EVENT_BIND.EVENT_ID
  is '事件ID';
comment on column WORKFLOW_EVENT_BIND.ASSOCIATED_OBJECT
  is '所属对象';
comment on column WORKFLOW_EVENT_BIND.ASSOCIATED_OBJ_TYPE
  is '所属对象类型(流程、活动) 由类型表维护';
comment on column WORKFLOW_EVENT_BIND.EVENT_TRIGER_TYPE
  is '事件触发类型
CREATE-活动创建后触发；
END-活动完成后触发；
CANCEL-活动取消后触发；
TIMEOUT-活动超时时触发';
comment on column WORKFLOW_EVENT_BIND.EVENT_TRIGER_MODE
  is '事件触发方式 1-同步；2-异步';
comment on column WORKFLOW_EVENT_BIND.DISPLAY_ORDER
  is '显示顺序';
alter table WORKFLOW_EVENT_BIND
  add constraint PK_WORKFLOW_EV2 primary key (BIND_ID)
  using index 
  tablespace USERS
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table WORKFLOW_INSRD_HISTORY
prompt =====================================
prompt
create table WORKFLOW_INSRD_HISTORY
(
  VARIABLEID         NUMBER not null,
  INSTANCEID         NUMBER,
  ACTIVITYID         NUMBER,
  ACTIVITYINSTANCEID NUMBER,
  WORKITEMID         NUMBER,
  VARIABLEKIND       NUMBER,
  VARIABLENAME       VARCHAR2(100) not null,
  VARIABLETYPE       NUMBER,
  VARIABLEVALUE      VARCHAR2(400),
  VARIABLEDIRECTION  NUMBER,
  VARIABLESCOPE      NUMBER,
  UNITID             VARCHAR2(100)
)
tablespace USERS
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
alter table WORKFLOW_INSRD_HISTORY
  add constraint PK_WORKFLOW_INSRD primary key (VARIABLEID)
  using index 
  tablespace USERS
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table WORKFLOW_INSTANCERELEVANTDATA
prompt ============================================
prompt
create table WORKFLOW_INSTANCERELEVANTDATA
(
  VARIABLEID         NUMBER not null,
  INSTANCEID         NUMBER,
  ACTIVITYID         NUMBER,
  ACTIVITYINSTANCEID NUMBER,
  WORKITEMID         NUMBER,
  VARIABLEKIND       NUMBER,
  VARIABLENAME       VARCHAR2(100) not null,
  VARIABLETYPE       NUMBER,
  VARIABLEVALUE      VARCHAR2(400),
  VARIABLEDIRECTION  NUMBER,
  VARIABLESCOPE      NUMBER,
  UNITID             VARCHAR2(100)
)
tablespace USERS
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
comment on table WORKFLOW_INSTANCERELEVANTDATA
  is '相关数据实例';
comment on column WORKFLOW_INSTANCERELEVANTDATA.VARIABLEID
  is '参数ID';
comment on column WORKFLOW_INSTANCERELEVANTDATA.INSTANCEID
  is '实例ID(流程实例)';
comment on column WORKFLOW_INSTANCERELEVANTDATA.ACTIVITYID
  is '活动定义ID';
comment on column WORKFLOW_INSTANCERELEVANTDATA.ACTIVITYINSTANCEID
  is '活动实例ID';
comment on column WORKFLOW_INSTANCERELEVANTDATA.WORKITEMID
  is '工作项ID';
comment on column WORKFLOW_INSTANCERELEVANTDATA.VARIABLEKIND
  is '参数类别(与表结构映射关系) 1-字段；2－参数；3－功能项';
comment on column WORKFLOW_INSTANCERELEVANTDATA.VARIABLENAME
  is '参数名称';
comment on column WORKFLOW_INSTANCERELEVANTDATA.VARIABLETYPE
  is '参数类型';
comment on column WORKFLOW_INSTANCERELEVANTDATA.VARIABLEVALUE
  is '参数值';
comment on column WORKFLOW_INSTANCERELEVANTDATA.VARIABLEDIRECTION
  is '输入输出类型 1-输入;2-输出;3-输入输出';
comment on column WORKFLOW_INSTANCERELEVANTDATA.VARIABLESCOPE
  is '参数范围';
comment on column WORKFLOW_INSTANCERELEVANTDATA.UNITID
  is '机构ID';
alter table WORKFLOW_INSTANCERELEVANTDATA
  add constraint PK_WORKFLOW_INS primary key (VARIABLEID)
  using index 
  tablespace USERS
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table WORKFLOW_INS_ASSOCIATION
prompt =======================================
prompt
create table WORKFLOW_INS_ASSOCIATION
(
  ID                 NUMBER not null,
  MAIN_PROCESSID     NUMBER,
  MAIN_INSTANCEID    NUMBER,
  ACTIVITYID         NUMBER,
  ACTIVITYINSTANCEID NUMBER,
  SUB_PROCESSID      NUMBER,
  SUB_INSTANCEID     NUMBER
)
tablespace USERS
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
comment on table WORKFLOW_INS_ASSOCIATION
  is '流程实例关联表';
comment on column WORKFLOW_INS_ASSOCIATION.ID
  is 'ID';
comment on column WORKFLOW_INS_ASSOCIATION.MAIN_PROCESSID
  is '主流程定义ID';
comment on column WORKFLOW_INS_ASSOCIATION.MAIN_INSTANCEID
  is '主流程实例ID';
comment on column WORKFLOW_INS_ASSOCIATION.ACTIVITYID
  is '活动定义ID';
comment on column WORKFLOW_INS_ASSOCIATION.ACTIVITYINSTANCEID
  is '活动实例ID';
comment on column WORKFLOW_INS_ASSOCIATION.SUB_PROCESSID
  is '从流程定义ID';
comment on column WORKFLOW_INS_ASSOCIATION.SUB_INSTANCEID
  is '从流程实例ID';
alter table WORKFLOW_INS_ASSOCIATION
  add constraint PK_WORKFLOW_INS_ASSOCIATION primary key (ID)
  using index 
  tablespace USERS
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table WORKFLOW_PACKAGE
prompt ===============================
prompt
create table WORKFLOW_PACKAGE
(
  PACKAGEID         NUMBER not null,
  PARENT_PACKAGE_ID NUMBER,
  PACKAGENAME       VARCHAR2(40),
  DESCRIPTION       VARCHAR2(50),
  STATUS            NUMBER,
  CREATOR           VARCHAR2(100),
  CREATEDATE        DATE,
  DISPLAY_ORDER     NUMBER,
  UNITID            VARCHAR2(100)
)
tablespace USERS
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
comment on table WORKFLOW_PACKAGE
  is '包';
comment on column WORKFLOW_PACKAGE.PACKAGEID
  is '包ID';
comment on column WORKFLOW_PACKAGE.PARENT_PACKAGE_ID
  is '父包ID';
comment on column WORKFLOW_PACKAGE.PACKAGENAME
  is '包名称';
comment on column WORKFLOW_PACKAGE.DESCRIPTION
  is '描述';
comment on column WORKFLOW_PACKAGE.STATUS
  is '状态';
comment on column WORKFLOW_PACKAGE.CREATOR
  is '创建者ID';
comment on column WORKFLOW_PACKAGE.CREATEDATE
  is '创建日期';
comment on column WORKFLOW_PACKAGE.DISPLAY_ORDER
  is '显示顺序';
comment on column WORKFLOW_PACKAGE.UNITID
  is '机构ID';
alter table WORKFLOW_PACKAGE
  add constraint PK_WORKFLOW_PAC primary key (PACKAGEID)
  using index 
  tablespace USERS
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table WORKFLOW_PACKAGERELEVANTDATA
prompt ===========================================
prompt
create table WORKFLOW_PACKAGERELEVANTDATA
(
  VARIABLEID        NUMBER not null,
  PACKAGEID         CHAR(10),
  VARIABLENAME      VARCHAR2(20) not null,
  VARIABLETYPE      NUMBER,
  VARIABLEVALUE     VARCHAR2(50),
  VARIABLEDIRECTION NUMBER,
  UINTID            VARCHAR2(100)
)
tablespace USERS
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
comment on table WORKFLOW_PACKAGERELEVANTDATA
  is '包相关参数';
comment on column WORKFLOW_PACKAGERELEVANTDATA.VARIABLEID
  is '参数ID';
comment on column WORKFLOW_PACKAGERELEVANTDATA.PACKAGEID
  is '包ID';
comment on column WORKFLOW_PACKAGERELEVANTDATA.VARIABLENAME
  is '参数名';
comment on column WORKFLOW_PACKAGERELEVANTDATA.VARIABLETYPE
  is '参数类型';
comment on column WORKFLOW_PACKAGERELEVANTDATA.VARIABLEVALUE
  is '参数值';
comment on column WORKFLOW_PACKAGERELEVANTDATA.VARIABLEDIRECTION
  is '1-输入;2-输出;3-输入输出';
comment on column WORKFLOW_PACKAGERELEVANTDATA.UINTID
  is '机构ID';
alter table WORKFLOW_PACKAGERELEVANTDATA
  add constraint PK_WORKFLOW_PA2 primary key (VARIABLEID)
  using index 
  tablespace USERS
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table WORKFLOW_PARTICIPANT
prompt ===================================
prompt
create table WORKFLOW_PARTICIPANT
(
  PARTICIPANTID      NUMBER not null,
  ACTIVITYID         NUMBER,
  ACTIVITYINSTANCEID NUMBER,
  PARTICIPANTCODE    VARCHAR2(100),
  PARTICIPANTNAME    VARCHAR2(100),
  PARTICIPANTTYPE    NUMBER,
  CONDITION          VARCHAR2(60),
  PARTICIPANTORDER   NUMBER,
  UNITID             VARCHAR2(100)
)
tablespace USERS
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
comment on table WORKFLOW_PARTICIPANT
  is '参与者';
comment on column WORKFLOW_PARTICIPANT.PARTICIPANTID
  is '参与者ID';
comment on column WORKFLOW_PARTICIPANT.ACTIVITYID
  is '活动定义ID';
comment on column WORKFLOW_PARTICIPANT.ACTIVITYINSTANCEID
  is '活动实例ID(实例参与者？)';
comment on column WORKFLOW_PARTICIPANT.PARTICIPANTCODE
  is '参与者代码';
comment on column WORKFLOW_PARTICIPANT.PARTICIPANTNAME
  is '参与者名称';
comment on column WORKFLOW_PARTICIPANT.PARTICIPANTTYPE
  is '参与者类型 10-角色；20-机构；30-人员；90-流程启动者';
comment on column WORKFLOW_PARTICIPANT.CONDITION
  is '参与条件';
comment on column WORKFLOW_PARTICIPANT.PARTICIPANTORDER
  is '参与顺序';
comment on column WORKFLOW_PARTICIPANT.UNITID
  is '机构ID';
alter table WORKFLOW_PARTICIPANT
  add constraint PK_WORKFLOW_PAR primary key (PARTICIPANTID)
  using index 
  tablespace USERS
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table WORKFLOW_PRCINS_HISTORY
prompt ======================================
prompt
create table WORKFLOW_PRCINS_HISTORY
(
  INSTANCEID        NUMBER not null,
  PROCESSID         NUMBER not null,
  INSTANCEPRIORITY  NUMBER,
  INSTANCETIMELIMIT NUMBER,
  INSTANCESTARTTIME DATE,
  INSTANCEENDTIME   DATE,
  PARENTINSTANCEID  NUMBER,
  PARENTACTIVITYID  NUMBER,
  CREATOR           VARCHAR2(100),
  CREATEDATE        DATE,
  STATE             NUMBER,
  UNITID            VARCHAR2(100)
)
tablespace USERS
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
alter table WORKFLOW_PRCINS_HISTORY
  add constraint PK_WORKFLOW_PRC primary key (INSTANCEID)
  using index 
  tablespace USERS
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table WORKFLOW_PROCESSDEFINITION
prompt =========================================
prompt
create table WORKFLOW_PROCESSDEFINITION
(
  PROCESSID             NUMBER not null,
  PACKAGEID             NUMBER,
  BUSINESS_ID           VARCHAR2(38),
  PROCESSNAME           VARCHAR2(100),
  DESCRIPTION           VARCHAR2(300),
  DISPLAYORDER          NUMBER,
  PROCESSSTATUS         NUMBER,
  PROCESSVERSION        VARCHAR2(20),
  PROCESSVERSIONDESC    VARCHAR2(100),
  PROCESSVERSIONDEFAULT NUMBER,
  PROCESSOWNER          VARCHAR2(100),
  CREATOR               VARCHAR2(100),
  CREATEDATE            DATE,
  UPDATEDATE            DATE,
  MODULE_TYPE           NUMBER,
  APP                   VARCHAR2(100),
  PROCESSFILE           VARCHAR2(200),
  ACTIVE_TIME           DATE,
  PROCESSSCOPE          NUMBER,
  UNITID                VARCHAR2(100)
)
tablespace USERS
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
comment on table WORKFLOW_PROCESSDEFINITION
  is '流程定义';
comment on column WORKFLOW_PROCESSDEFINITION.PROCESSID
  is '流程定义ID';
comment on column WORKFLOW_PROCESSDEFINITION.PACKAGEID
  is '包ID';
comment on column WORKFLOW_PROCESSDEFINITION.BUSINESS_ID
  is '流程业务ID，38字节，只能输入英文';
comment on column WORKFLOW_PROCESSDEFINITION.PROCESSNAME
  is '流程名称';
comment on column WORKFLOW_PROCESSDEFINITION.DESCRIPTION
  is '流程描述';
comment on column WORKFLOW_PROCESSDEFINITION.DISPLAYORDER
  is '显示顺序';
comment on column WORKFLOW_PROCESSDEFINITION.PROCESSSTATUS
  is '流程状态(虚拟删除标志位)';
comment on column WORKFLOW_PROCESSDEFINITION.PROCESSVERSION
  is '版本(版本号)';
comment on column WORKFLOW_PROCESSDEFINITION.PROCESSVERSIONDESC
  is '版本描述';
comment on column WORKFLOW_PROCESSDEFINITION.PROCESSVERSIONDEFAULT
  is '是否默认版本';
comment on column WORKFLOW_PROCESSDEFINITION.PROCESSOWNER
  is '流程所有人';
comment on column WORKFLOW_PROCESSDEFINITION.CREATOR
  is '创建人(指创建人的ID)';
comment on column WORKFLOW_PROCESSDEFINITION.CREATEDATE
  is '创建日期';
comment on column WORKFLOW_PROCESSDEFINITION.UPDATEDATE
  is '更新日期';
comment on column WORKFLOW_PROCESSDEFINITION.MODULE_TYPE
  is '业务模块类型';
comment on column WORKFLOW_PROCESSDEFINITION.APP
  is '关联的URL，该标签为“业务模块”';
comment on column WORKFLOW_PROCESSDEFINITION.PROCESSFILE
  is '临时xml文件存储相对路径';
comment on column WORKFLOW_PROCESSDEFINITION.ACTIVE_TIME
  is '流程生效时间';
comment on column WORKFLOW_PROCESSDEFINITION.PROCESSSCOPE
  is '0：公用流程
1：私有流程';
comment on column WORKFLOW_PROCESSDEFINITION.UNITID
  is '机构ID';
alter table WORKFLOW_PROCESSDEFINITION
  add constraint PK_WORKFLOW_PRO primary key (PROCESSID)
  using index 
  tablespace USERS
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table WORKFLOW_PROCESSINSTANCE
prompt =======================================
prompt
create table WORKFLOW_PROCESSINSTANCE
(
  INSTANCEID        NUMBER not null,
  PROCESSID         NUMBER not null,
  INSTANCEPRIORITY  NUMBER,
  INSTANCETIMELIMIT NUMBER,
  INSTANCESTARTTIME DATE,
  INSTANCEENDTIME   DATE,
  PARENTINSTANCEID  NUMBER,
  PARENTACTIVITYID  NUMBER,
  CREATOR           VARCHAR2(100),
  CREATEDATE        DATE,
  STATE             NUMBER,
  UNITID            VARCHAR2(100)
)
tablespace USERS
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
comment on table WORKFLOW_PROCESSINSTANCE
  is '流程实例';
comment on column WORKFLOW_PROCESSINSTANCE.INSTANCEID
  is '流程实例ID';
comment on column WORKFLOW_PROCESSINSTANCE.PROCESSID
  is '流程定义ID(某个业务流程之某版本的流程定义ID)';
comment on column WORKFLOW_PROCESSINSTANCE.INSTANCEPRIORITY
  is '实例优先级';
comment on column WORKFLOW_PROCESSINSTANCE.INSTANCETIMELIMIT
  is '实例限时策略';
comment on column WORKFLOW_PROCESSINSTANCE.INSTANCESTARTTIME
  is '实例启动时间';
comment on column WORKFLOW_PROCESSINSTANCE.INSTANCEENDTIME
  is '实例结束时间';
comment on column WORKFLOW_PROCESSINSTANCE.PARENTINSTANCEID
  is '父流程实例ID，与子流程有关';
comment on column WORKFLOW_PROCESSINSTANCE.PARENTACTIVITYID
  is '父活动实例ID，与子流程有关';
comment on column WORKFLOW_PROCESSINSTANCE.CREATOR
  is '创建人ID';
comment on column WORKFLOW_PROCESSINSTANCE.CREATEDATE
  is '创建日期';
comment on column WORKFLOW_PROCESSINSTANCE.STATE
  is '状态';
comment on column WORKFLOW_PROCESSINSTANCE.UNITID
  is '机构ID';
alter table WORKFLOW_PROCESSINSTANCE
  add constraint PK_WORKFLOW_PR2 primary key (INSTANCEID)
  using index 
  tablespace USERS
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table WORKFLOW_PROCESSRELEVANTDATA
prompt ===========================================
prompt
create table WORKFLOW_PROCESSRELEVANTDATA
(
  VARIABLEID        NUMBER not null,
  ACTIVITYID        NUMBER,
  PROCESSID         NUMBER,
  VARIABLEKIND      NUMBER,
  VARIABLENAME      VARCHAR2(100) not null,
  VARIABLETYPE      NUMBER,
  DEFAULTVALUE      VARCHAR2(400),
  VARIABLEDIRECTION NUMBER,
  VARIABLESCOPE     NUMBER,
  UNITID            VARCHAR2(100)
)
tablespace USERS
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
comment on table WORKFLOW_PROCESSRELEVANTDATA
  is '相关参数定义';
comment on column WORKFLOW_PROCESSRELEVANTDATA.VARIABLEID
  is '参数ID';
comment on column WORKFLOW_PROCESSRELEVANTDATA.ACTIVITYID
  is '活动定义ID';
comment on column WORKFLOW_PROCESSRELEVANTDATA.PROCESSID
  is '流程定义ID';
comment on column WORKFLOW_PROCESSRELEVANTDATA.VARIABLEKIND
  is '参数类别(与表结构映射关系) 1-字段；2－参数；3－功能项';
comment on column WORKFLOW_PROCESSRELEVANTDATA.VARIABLENAME
  is '参数名称';
comment on column WORKFLOW_PROCESSRELEVANTDATA.VARIABLETYPE
  is '参数类型';
comment on column WORKFLOW_PROCESSRELEVANTDATA.DEFAULTVALUE
  is '参数默认值';
comment on column WORKFLOW_PROCESSRELEVANTDATA.VARIABLEDIRECTION
  is '输入输出类型 1-输入;2-输出;3-输入输出';
comment on column WORKFLOW_PROCESSRELEVANTDATA.VARIABLESCOPE
  is '参数范围';
comment on column WORKFLOW_PROCESSRELEVANTDATA.UNITID
  is '机构ID';
alter table WORKFLOW_PROCESSRELEVANTDATA
  add constraint PK_WORKFLOW_PR3 primary key (VARIABLEID)
  using index 
  tablespace USERS
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table WORKFLOW_TRANSITION
prompt ==================================
prompt
create table WORKFLOW_TRANSITION
(
  TRANSITIONID     NUMBER not null,
  PROCESSID        NUMBER not null,
  TRANSITIONNAME   VARCHAR2(100),
  PREVIOUSACTIVITY NUMBER not null,
  NEXTACTIVITY     NUMBER not null,
  DESCRIPTION      VARCHAR2(50),
  CONDITION        VARCHAR2(200),
  UNITID           VARCHAR2(100)
)
tablespace USERS
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
comment on table WORKFLOW_TRANSITION
  is '迁移';
comment on column WORKFLOW_TRANSITION.TRANSITIONID
  is '迁移ID';
comment on column WORKFLOW_TRANSITION.PROCESSID
  is '流程定义ID(某个业务流程之某版本的流程定义ID)';
comment on column WORKFLOW_TRANSITION.TRANSITIONNAME
  is '迁移名称';
comment on column WORKFLOW_TRANSITION.PREVIOUSACTIVITY
  is '上一活动定义ID';
comment on column WORKFLOW_TRANSITION.NEXTACTIVITY
  is '下一活动定义ID';
comment on column WORKFLOW_TRANSITION.DESCRIPTION
  is '描述';
comment on column WORKFLOW_TRANSITION.CONDITION
  is '迁移条件';
comment on column WORKFLOW_TRANSITION.UNITID
  is '机构ID';
alter table WORKFLOW_TRANSITION
  add constraint PK_WORKFLOW_TRA primary key (TRANSITIONID)
  using index 
  tablespace USERS
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table WORKFLOW_URGE_LOG
prompt ================================
prompt
create table WORKFLOW_URGE_LOG
(
  LOGID              NUMBER not null,
  POLICYID           NUMBER,
  ACTIVITYINSTANCEID NUMBER,
  WORKITEMID         NUMBER,
  URGEKIND           NUMBER,
  LOGTYPE            NUMBER not null,
  LOGCONTENT         VARCHAR2(400),
  LOGTIME            DATE,
  LOGPERSON          VARCHAR2(100),
  URGEDPERSON        VARCHAR2(100),
  LOGSTATUS          NUMBER,
  UNITID             VARCHAR2(100)
)
tablespace USERS
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
alter table WORKFLOW_URGE_LOG
  add constraint PK_WORKFLOW_URG primary key (LOGID)
  using index 
  tablespace USERS
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table WORKFLOW_URGE_POLICY
prompt ===================================
prompt
create table WORKFLOW_URGE_POLICY
(
  POLICYID           NUMBER not null,
  ACTIVITYID         NUMBER,
  ACTIVITYINSTANCEID NUMBER,
  POLICYKIND         NUMBER,
  POLICYTYPE         NUMBER,
  POLICYTIMELIMIT    NUMBER,
  POLICYSCOPE        NUMBER,
  POLICYDAY          NUMBER,
  POLICYCONTENT      VARCHAR2(400),
  POLICYPERSON       VARCHAR2(64),
  POLICYSTATUS       NUMBER,
  UNITID             VARCHAR2(100)
)
tablespace USERS
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
comment on column WORKFLOW_URGE_POLICY.POLICYID
  is '策略ID';
comment on column WORKFLOW_URGE_POLICY.ACTIVITYID
  is '活动定义ID';
comment on column WORKFLOW_URGE_POLICY.ACTIVITYINSTANCEID
  is '活动实例ID';
comment on column WORKFLOW_URGE_POLICY.POLICYKIND
  is '10-email;20-及时消息;30-短消息';
comment on column WORKFLOW_URGE_POLICY.POLICYTYPE
  is '10-在产生待办工作项即督办;20-在时限到达前督办';
comment on column WORKFLOW_URGE_POLICY.POLICYTIMELIMIT
  is '策略时限';
comment on column WORKFLOW_URGE_POLICY.POLICYSCOPE
  is '0:无限制
1:机构级
2:部门级
3以上待定';
comment on column WORKFLOW_URGE_POLICY.POLICYDAY
  is '策略天';
comment on column WORKFLOW_URGE_POLICY.POLICYCONTENT
  is '策略说明';
comment on column WORKFLOW_URGE_POLICY.POLICYPERSON
  is '策略人';
comment on column WORKFLOW_URGE_POLICY.POLICYSTATUS
  is '策略状态';
comment on column WORKFLOW_URGE_POLICY.UNITID
  is '机构ID';
alter table WORKFLOW_URGE_POLICY
  add constraint PK_WORKFLOW_UR2 primary key (POLICYID)
  using index 
  tablespace USERS
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table WORKFLOW_WORKITEM
prompt ================================
prompt
create table WORKFLOW_WORKITEM
(
  WORKITEMID          NUMBER not null,
  INSTANCEID          NUMBER not null,
  ACTIVITYINSTANCEID  NUMBER not null,
  PREVIOUSWORKITEMID  NUMBER,
  TITLE               VARCHAR2(200),
  STATE               NUMBER,
  SENDER              VARCHAR2(100),
  SENDDATE            DATE,
  PARTICIPANTID       VARCHAR2(100),
  PARTICIPANTTYPE     NUMBER,
  PARTICIPANTORDER    NUMBER,
  TEMPPARTICIPANTID   VARCHAR2(100),
  TEMPPARTICIPANTTYPE NUMBER,
  TRANSACTOR          VARCHAR2(100),
  TRANSACTDATE        DATE,
  MODULE_TYPE         NUMBER,
  APP                 VARCHAR2(100),
  UNITID              VARCHAR2(100)
)
tablespace USERS
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
comment on table WORKFLOW_WORKITEM
  is '工作项';
comment on column WORKFLOW_WORKITEM.WORKITEMID
  is '工作项ID';
comment on column WORKFLOW_WORKITEM.INSTANCEID
  is '流程实例ID';
comment on column WORKFLOW_WORKITEM.ACTIVITYINSTANCEID
  is '活动实例ID';
comment on column WORKFLOW_WORKITEM.PREVIOUSWORKITEMID
  is '前一工作项ID';
comment on column WORKFLOW_WORKITEM.TITLE
  is '名称';
comment on column WORKFLOW_WORKITEM.STATE
  is '状态';
comment on column WORKFLOW_WORKITEM.SENDER
  is '发送者';
comment on column WORKFLOW_WORKITEM.SENDDATE
  is '发送时间';
comment on column WORKFLOW_WORKITEM.PARTICIPANTID
  is '参与者ID';
comment on column WORKFLOW_WORKITEM.PARTICIPANTTYPE
  is '参与者类型';
comment on column WORKFLOW_WORKITEM.PARTICIPANTORDER
  is '参与顺序';
comment on column WORKFLOW_WORKITEM.TEMPPARTICIPANTID
  is '临时参与者ID';
comment on column WORKFLOW_WORKITEM.TEMPPARTICIPANTTYPE
  is '临时参与者类型';
comment on column WORKFLOW_WORKITEM.TRANSACTOR
  is '处理人';
comment on column WORKFLOW_WORKITEM.TRANSACTDATE
  is '处理时间';
comment on column WORKFLOW_WORKITEM.MODULE_TYPE
  is '业务模块类型';
comment on column WORKFLOW_WORKITEM.APP
  is '关联的URL，该标签为“业务模块”';
comment on column WORKFLOW_WORKITEM.UNITID
  is '机构ID';
alter table WORKFLOW_WORKITEM
  add constraint PK_WORKFLOW_WOR primary key (WORKITEMID)
  using index 
  tablespace USERS
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table WORKFLOW_WORKITEM_BIZ_DESC
prompt =========================================
prompt
create table WORKFLOW_WORKITEM_BIZ_DESC
(
  WORKITEMID  NUMBER not null,
  FIELD_NAME  VARCHAR2(50) not null,
  FIELD_VALUE VARCHAR2(500)
)
tablespace USERS
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
comment on table WORKFLOW_WORKITEM_BIZ_DESC
  is '工作项业务描述';
comment on column WORKFLOW_WORKITEM_BIZ_DESC.WORKITEMID
  is '工作项ID，关键字';
comment on column WORKFLOW_WORKITEM_BIZ_DESC.FIELD_NAME
  is '字段名，关键字';
comment on column WORKFLOW_WORKITEM_BIZ_DESC.FIELD_VALUE
  is '字段值';
alter table WORKFLOW_WORKITEM_BIZ_DESC
  add constraint PK_WORKFLOW_WO2 primary key (WORKITEMID, FIELD_NAME)
  using index 
  tablespace USERS
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table WORKFLOW_WORKITEM_BIZ_FIELD
prompt ==========================================
prompt
create table WORKFLOW_WORKITEM_BIZ_FIELD
(
  FIELD_NAME  VARCHAR2(50) not null,
  FIELD_LABEL VARCHAR2(50) not null
)
tablespace USERS
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
comment on table WORKFLOW_WORKITEM_BIZ_FIELD
  is '工作项业务字段

维护和工作项有关的业务内容信息';
comment on column WORKFLOW_WORKITEM_BIZ_FIELD.FIELD_NAME
  is '字段名，关键字';
comment on column WORKFLOW_WORKITEM_BIZ_FIELD.FIELD_LABEL
  is '字段标签';
alter table WORKFLOW_WORKITEM_BIZ_FIELD
  add constraint PK_WORKFLOW_WO3 primary key (FIELD_NAME)
  using index 
  tablespace USERS
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table WORKFLOW_WORKITEM_HISTORY
prompt ========================================
prompt
create table WORKFLOW_WORKITEM_HISTORY
(
  WORKITEMID          NUMBER not null,
  INSTANCEID          NUMBER not null,
  ACTIVITYINSTANCEID  NUMBER not null,
  PREVIOUSWORKITEMID  NUMBER,
  TITLE               VARCHAR2(200),
  STATE               NUMBER,
  SENDER              VARCHAR2(100),
  SENDDATE            DATE,
  PARTICIPANTID       VARCHAR2(100),
  PARTICIPANTTYPE     NUMBER,
  PARTICIPANTORDER    NUMBER,
  TEMPPARTICIPANTID   VARCHAR2(100),
  TEMPPARTICIPANTTYPE NUMBER,
  TRANSACTOR          VARCHAR2(100),
  TRANSACTDATE        DATE,
  MODULE_TYPE         NUMBER,
  APP                 VARCHAR2(100),
  UNITID              VARCHAR2(100)
)
tablespace USERS
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
alter table WORKFLOW_WORKITEM_HISTORY
  add constraint PK_WORKFLOW_WO4 primary key (WORKITEMID)
  using index 
  tablespace USERS
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating sequence S_APP_ACTIVITY_ENTER
prompt ======================================
prompt
create sequence S_APP_ACTIVITY_ENTER
minvalue 1
maxvalue 999999999999999999999999999
start with 21
increment by 1
cache 20;

prompt
prompt Creating sequence S_APP_ACTIVITY_ENTERDETAIL
prompt ============================================
prompt
create sequence S_APP_ACTIVITY_ENTERDETAIL
minvalue 1
maxvalue 999999999999999999999999999
start with 21
increment by 1
cache 20;

prompt
prompt Creating sequence S_APP_ACTIVITY_INFO
prompt =====================================
prompt
create sequence S_APP_ACTIVITY_INFO
minvalue 1
maxvalue 999999999999999999999999999
start with 21
increment by 1
cache 20;

prompt
prompt Creating sequence S_APP_BBS_BROWSE
prompt ==================================
prompt
create sequence S_APP_BBS_BROWSE
minvalue 1
maxvalue 999999999999999999999999999
start with 263
increment by 1
cache 20;

prompt
prompt Creating sequence S_APP_BBS_CHANNEL
prompt ===================================
prompt
create sequence S_APP_BBS_CHANNEL
minvalue 1
maxvalue 999999999999999999999999999
start with 63
increment by 1
cache 20;

prompt
prompt Creating sequence S_APP_BBS_CHANNEL_EXPERT
prompt ==========================================
prompt
create sequence S_APP_BBS_CHANNEL_EXPERT
minvalue 1
maxvalue 999999999999999999999999999
start with 3
increment by 1
cache 20;

prompt
prompt Creating sequence S_APP_BBS_CHANNEL_MASTER
prompt ==========================================
prompt
create sequence S_APP_BBS_CHANNEL_MASTER
minvalue 1
maxvalue 999999999999999999999999999
start with 42
increment by 1
cache 20;

prompt
prompt Creating sequence S_APP_BBS_COTERIE
prompt ===================================
prompt
create sequence S_APP_BBS_COTERIE
minvalue 1
maxvalue 999999999999999999999999999
start with 21
increment by 1
cache 20;

prompt
prompt Creating sequence S_APP_BBS_COTERIE_USER
prompt ========================================
prompt
create sequence S_APP_BBS_COTERIE_USER
minvalue 1
maxvalue 999999999999999999999999999
start with 41
increment by 1
cache 20;

prompt
prompt Creating sequence S_APP_BBS_TOPIC
prompt =================================
prompt
create sequence S_APP_BBS_TOPIC
minvalue 1
maxvalue 999999999999999999999999999
start with 102
increment by 1
cache 20;

prompt
prompt Creating sequence S_APP_BBS_TOPIC_POINT
prompt =======================================
prompt
create sequence S_APP_BBS_TOPIC_POINT
minvalue 1
maxvalue 999999999999999999999999999
start with 10
increment by 1
cache 20;

prompt
prompt Creating sequence S_APP_BBS_USER_FELLOW
prompt =======================================
prompt
create sequence S_APP_BBS_USER_FELLOW
minvalue 1
maxvalue 999999999999999999999999999
start with 2
increment by 1
cache 20;

prompt
prompt Creating sequence S_APP_CALENDAR_WORK
prompt =====================================
prompt
create sequence S_APP_CALENDAR_WORK
minvalue 1
maxvalue 999999999999999999999999999
start with 364
increment by 1
cache 20;

prompt
prompt Creating sequence S_APP_FORM_ACTIVITY
prompt =====================================
prompt
create sequence S_APP_FORM_ACTIVITY
minvalue 1
maxvalue 999999999999999999999999999
start with 103
increment by 1
cache 20;

prompt
prompt Creating sequence S_APP_FORM_ACTIVITYFIELD
prompt ==========================================
prompt
create sequence S_APP_FORM_ACTIVITYFIELD
minvalue 1
maxvalue 999999999999999999999999999
start with 125
increment by 1
cache 20;

prompt
prompt Creating sequence S_APP_FORM_DEFINE
prompt ===================================
prompt
create sequence S_APP_FORM_DEFINE
minvalue 1
maxvalue 999999999999999999999999999
start with 102
increment by 1
cache 20;

prompt
prompt Creating sequence S_APP_FORM_FIELD
prompt ==================================
prompt
create sequence S_APP_FORM_FIELD
minvalue 1
maxvalue 999999999999999999999999999
start with 596
increment by 1
cache 20;

prompt
prompt Creating sequence S_APP_GENERAL_CODEPOOL
prompt ========================================
prompt
create sequence S_APP_GENERAL_CODEPOOL
minvalue 1
maxvalue 999999999999999999999999999
start with 21
increment by 1
cache 20;

prompt
prompt Creating sequence S_APP_GENERAL_COLLECTION
prompt ==========================================
prompt
create sequence S_APP_GENERAL_COLLECTION
minvalue 1
maxvalue 999999999999999999999999999
start with 53
increment by 1
cache 20;

prompt
prompt Creating sequence S_APP_GENERAL_COMMUNICATE
prompt ===========================================
prompt
create sequence S_APP_GENERAL_COMMUNICATE
minvalue 1
maxvalue 999999999999999999999999999
start with 22
increment by 1
cache 20;

prompt
prompt Creating sequence S_APP_GENERAL_COTERIE
prompt =======================================
prompt
create sequence S_APP_GENERAL_COTERIE
minvalue 1
maxvalue 999999999999999999999999999
start with 3
increment by 1
cache 20;

prompt
prompt Creating sequence S_APP_GENERAL_COTERIE_USER
prompt ============================================
prompt
create sequence S_APP_GENERAL_COTERIE_USER
minvalue 1
maxvalue 999999999999999999999999999
start with 2
increment by 1
cache 20;

prompt
prompt Creating sequence S_APP_GENERAL_OPINION
prompt =======================================
prompt
create sequence S_APP_GENERAL_OPINION
minvalue 1
maxvalue 999999999999999999999999999
start with 42
increment by 1
cache 20;

prompt
prompt Creating sequence S_APP_GENERAL_RECOMMEND
prompt =========================================
prompt
create sequence S_APP_GENERAL_RECOMMEND
minvalue 1
maxvalue 999999999999999999999999999
start with 43
increment by 1
cache 20;

prompt
prompt Creating sequence S_APP_GENERAL_SEARCH
prompt ======================================
prompt
create sequence S_APP_GENERAL_SEARCH
minvalue 1
maxvalue 999999999999999999999999999
start with 63
increment by 1
cache 20;

prompt
prompt Creating sequence S_APP_GENERAL_S_CONDITION
prompt ===========================================
prompt
create sequence S_APP_GENERAL_S_CONDITION
minvalue 1
maxvalue 999999999999999999999999999
start with 62
increment by 1
cache 20;

prompt
prompt Creating sequence S_APP_GENERAL_S_DISPLAY
prompt =========================================
prompt
create sequence S_APP_GENERAL_S_DISPLAY
minvalue 1
maxvalue 999999999999999999999999999
start with 104
increment by 1
cache 20;

prompt
prompt Creating sequence S_APP_GENERAL_S_ORDER
prompt =======================================
prompt
create sequence S_APP_GENERAL_S_ORDER
minvalue 1
maxvalue 999999999999999999999999999
start with 2
increment by 1
cache 20;

prompt
prompt Creating sequence S_APP_PROJECT
prompt ===============================
prompt
create sequence S_APP_PROJECT
minvalue 1
maxvalue 999999999999999999999999999
start with 3
increment by 1
cache 20;

prompt
prompt Creating sequence S_APP_REPORT_CHECK
prompt ====================================
prompt
create sequence S_APP_REPORT_CHECK
minvalue 1
maxvalue 999999999999999999999999999
start with 2
increment by 1
cache 20;

prompt
prompt Creating sequence S_APP_REPORT_COLLECT
prompt ======================================
prompt
create sequence S_APP_REPORT_COLLECT
minvalue 1
maxvalue 999999999999999999999999999
start with 3
increment by 1
cache 20;

prompt
prompt Creating sequence S_APP_REPORT_SECURITY
prompt =======================================
prompt
create sequence S_APP_REPORT_SECURITY
minvalue 1
maxvalue 999999999999999999999999999
start with 63
increment by 1
cache 20;

prompt
prompt Creating sequence S_APP_SUMMARIZE
prompt =================================
prompt
create sequence S_APP_SUMMARIZE
minvalue 1
maxvalue 999999999999999999999999999
start with 2
increment by 1
cache 20;

prompt
prompt Creating sequence S_APP_SUMMARIZE_TEMPLATE
prompt ==========================================
prompt
create sequence S_APP_SUMMARIZE_TEMPLATE
minvalue 1
maxvalue 999999999999999999999999999
start with 4
increment by 1
cache 20;

prompt
prompt Creating sequence S_APP_TASK
prompt ============================
prompt
create sequence S_APP_TASK
minvalue 1
maxvalue 999999999999999999999999999
start with 166
increment by 1
cache 20;

prompt
prompt Creating sequence S_APP_TASK_ASSESS
prompt ===================================
prompt
create sequence S_APP_TASK_ASSESS
minvalue 1
maxvalue 999999999999999999999999999
start with 2
increment by 1
cache 20;

prompt
prompt Creating sequence S_APP_TASK_DO
prompt ===============================
prompt
create sequence S_APP_TASK_DO
minvalue 1
maxvalue 999999999999999999999999999
start with 87
increment by 1
cache 20;

prompt
prompt Creating sequence S_APP_TASK_TREAT
prompt ==================================
prompt
create sequence S_APP_TASK_TREAT
minvalue 1
maxvalue 999999999999999999999999999
start with 42
increment by 1
cache 20;

prompt
prompt Creating sequence S_APP_TEMPLATE_ATTACHMENT
prompt ===========================================
prompt
create sequence S_APP_TEMPLATE_ATTACHMENT
minvalue 1
maxvalue 999999999999999999999999999
start with 409
increment by 1
cache 20;

prompt
prompt Creating sequence S_APP_TEMPLATE_HISTORY
prompt ========================================
prompt
create sequence S_APP_TEMPLATE_HISTORY
minvalue 1
maxvalue 999999999999999999999999999
start with 103
increment by 1
cache 20;

prompt
prompt Creating sequence S_APP_UMS_MESSAGE
prompt ===================================
prompt
create sequence S_APP_UMS_MESSAGE
minvalue 1
maxvalue 999999999999999999999999999
start with 468
increment by 1
cache 20;

prompt
prompt Creating sequence S_APP_WORK_PERSON
prompt ===================================
prompt
create sequence S_APP_WORK_PERSON
minvalue 1
maxvalue 999999999999999999999999999
start with 22
increment by 1
cache 20;

prompt
prompt Creating sequence S_APP_WORK_TEMPLATE
prompt =====================================
prompt
create sequence S_APP_WORK_TEMPLATE
minvalue 1
maxvalue 999999999999999999999999999
start with 2
increment by 1
cache 20;

prompt
prompt Creating sequence S_CMS_CONTENT
prompt ===============================
prompt
create sequence S_CMS_CONTENT
minvalue 1
maxvalue 999999999999999999999999999
start with 39
increment by 1
cache 20;

prompt
prompt Creating sequence S_EPM_360_INSTANCE
prompt ====================================
prompt
create sequence S_EPM_360_INSTANCE
minvalue 1
maxvalue 999999999999999999999999999
start with 61
increment by 1
cache 20;

prompt
prompt Creating sequence S_EPM_360_INSTANCE_ANSWER
prompt ===========================================
prompt
create sequence S_EPM_360_INSTANCE_ANSWER
minvalue 1
maxvalue 999999999999999999999999999
start with 81
increment by 1
cache 20;

prompt
prompt Creating sequence S_EPM_360_ITEM
prompt ================================
prompt
create sequence S_EPM_360_ITEM
minvalue 1
maxvalue 999999999999999999999999999
start with 21
increment by 1
cache 20;

prompt
prompt Creating sequence S_EPM_360_QUESTIONNAIRE
prompt =========================================
prompt
create sequence S_EPM_360_QUESTIONNAIRE
minvalue 1
maxvalue 999999999999999999999999999
start with 406
increment by 1
cache 20;

prompt
prompt Creating sequence S_EPM_360_QUESTION_DETAIL
prompt ===========================================
prompt
create sequence S_EPM_360_QUESTION_DETAIL
minvalue 1
maxvalue 999999999999999999999999999
start with 517
increment by 1
cache 20;

prompt
prompt Creating sequence S_EPM_360_TEMPLATE
prompt ====================================
prompt
create sequence S_EPM_360_TEMPLATE
minvalue 1
maxvalue 999999999999999999999999999
start with 222
increment by 1
cache 20;

prompt
prompt Creating sequence S_EPM_360_TEMPLATE_ITEM
prompt =========================================
prompt
create sequence S_EPM_360_TEMPLATE_ITEM
minvalue 1
maxvalue 999999999999999999999999999
start with 102
increment by 1
cache 20;

prompt
prompt Creating sequence S_EPM_BASE_GRADE
prompt ==================================
prompt
create sequence S_EPM_BASE_GRADE
minvalue 1
maxvalue 999999999999999999999999999
start with 422
increment by 1
cache 20;

prompt
prompt Creating sequence S_EPM_BASE_GRADELEVEL
prompt =======================================
prompt
create sequence S_EPM_BASE_GRADELEVEL
minvalue 1
maxvalue 999999999999999999999999999
start with 464
increment by 1
cache 20;

prompt
prompt Creating sequence S_EPM_BASE_INDEXTEMPLATE
prompt ==========================================
prompt
create sequence S_EPM_BASE_INDEXTEMPLATE
minvalue 1
maxvalue 999999999999999999999999999
start with 1087
increment by 1
cache 20;

prompt
prompt Creating sequence S_EPM_BASE_INDEXTEMPLATETYPE
prompt ==============================================
prompt
create sequence S_EPM_BASE_INDEXTEMPLATETYPE
minvalue 1
maxvalue 999999999999999999999999999
start with 466
increment by 1
cache 20;

prompt
prompt Creating sequence S_EPM_BASE_INDEXTEMPLATE_ITEM
prompt ===============================================
prompt
create sequence S_EPM_BASE_INDEXTEMPLATE_ITEM
minvalue 1
maxvalue 999999999999999999999999999
start with 1086
increment by 1
cache 20;

prompt
prompt Creating sequence S_EPM_BASE_KPITEMPLATE
prompt ========================================
prompt
create sequence S_EPM_BASE_KPITEMPLATE
minvalue 1
maxvalue 999999999999999999999999999
start with 602
increment by 1
cache 20;

prompt
prompt Creating sequence S_EPM_BASE_KPITEMPLATE_FIELD
prompt ==============================================
prompt
create sequence S_EPM_BASE_KPITEMPLATE_FIELD
minvalue 1
maxvalue 999999999999999999999999999
start with 350
increment by 1
cache 20;

prompt
prompt Creating sequence S_EPM_BASE_KPITEMPLATE_STEP
prompt =============================================
prompt
create sequence S_EPM_BASE_KPITEMPLATE_STEP
minvalue 1
maxvalue 999999999999999999999999999
start with 824
increment by 1
cache 20;

prompt
prompt Creating sequence S_EPM_BASE_PERIOD
prompt ===================================
prompt
create sequence S_EPM_BASE_PERIOD
minvalue 1
maxvalue 999999999999999999999999999
start with 953
increment by 1
cache 20;

prompt
prompt Creating sequence S_EPM_BASE_PERSONTYPESET
prompt ==========================================
prompt
create sequence S_EPM_BASE_PERSONTYPESET
minvalue 1
maxvalue 999999999999999999999999999
start with 69
increment by 1
cache 20;

prompt
prompt Creating sequence S_EPM_BASE_TEMPLATE_ITEM
prompt ==========================================
prompt
create sequence S_EPM_BASE_TEMPLATE_ITEM
minvalue 1
maxvalue 999999999999999999999999999
start with 41
increment by 1
cache 20;

prompt
prompt Creating sequence S_EPM_BASE_VIRTUALPERSON
prompt ==========================================
prompt
create sequence S_EPM_BASE_VIRTUALPERSON
minvalue 1
maxvalue 999999999999999999999999999
start with 21
increment by 1
cache 20;

prompt
prompt Creating sequence S_EPM_CASE_360
prompt ================================
prompt
create sequence S_EPM_CASE_360
minvalue 1
maxvalue 999999999999999999999999999
start with 4
increment by 1
cache 20;

prompt
prompt Creating sequence S_EPM_CASE_360ITEM
prompt ====================================
prompt
create sequence S_EPM_CASE_360ITEM
minvalue 1
maxvalue 999999999999999999999999999
start with 122
increment by 1
cache 20;

prompt
prompt Creating sequence S_EPM_CASE_ACTIVITY
prompt =====================================
prompt
create sequence S_EPM_CASE_ACTIVITY
minvalue 1
maxvalue 999999999999999999999999999
start with 130
increment by 1
cache 20;

prompt
prompt Creating sequence S_EPM_CASE_ASSESS_WORKFLOW
prompt ============================================
prompt
create sequence S_EPM_CASE_ASSESS_WORKFLOW
minvalue 1
maxvalue 999999999999999999999999999
start with 21
increment by 1
cache 20;

prompt
prompt Creating sequence S_EPM_CASE_BASE
prompt =================================
prompt
create sequence S_EPM_CASE_BASE
minvalue 1
maxvalue 999999999999999999999999999
start with 842
increment by 1
cache 20;

prompt
prompt Creating sequence S_EPM_CASE_KPI
prompt ================================
prompt
create sequence S_EPM_CASE_KPI
minvalue 1
maxvalue 999999999999999999999999999
start with 1422
increment by 1
cache 20;

prompt
prompt Creating sequence S_EPM_CASE_PARTICIPANT
prompt ========================================
prompt
create sequence S_EPM_CASE_PARTICIPANT
minvalue 1
maxvalue 999999999999999999999999999
start with 1639
increment by 1
cache 20;

prompt
prompt Creating sequence S_EPM_CASE_PARTICIPATOR
prompt =========================================
prompt
create sequence S_EPM_CASE_PARTICIPATOR
minvalue 1
maxvalue 999999999999999999999999999
start with 1126
increment by 1
cache 20;

prompt
prompt Creating sequence S_EPM_CASE_PERIOD
prompt ===================================
prompt
create sequence S_EPM_CASE_PERIOD
minvalue 1
maxvalue 999999999999999999999999999
start with 182
increment by 1
cache 20;

prompt
prompt Creating sequence S_EPM_CASE_PERSONDETAIL
prompt =========================================
prompt
create sequence S_EPM_CASE_PERSONDETAIL
minvalue 1
maxvalue 999999999999999999999999999
start with 30316
increment by 1
cache 20;

prompt
prompt Creating sequence S_EPM_CASE_PLAN_WORKFLOW
prompt ==========================================
prompt
create sequence S_EPM_CASE_PLAN_WORKFLOW
minvalue 1
maxvalue 999999999999999999999999999
start with 21
increment by 1
cache 20;

prompt
prompt Creating sequence S_EPM_CASE_SCORESET
prompt =====================================
prompt
create sequence S_EPM_CASE_SCORESET
minvalue 1
maxvalue 999999999999999999999999999
start with 1111
increment by 1
cache 20;

prompt
prompt Creating sequence S_EPM_CASE_TEMPLATEDETAIL
prompt ===========================================
prompt
create sequence S_EPM_CASE_TEMPLATEDETAIL
minvalue 1
maxvalue 999999999999999999999999999
start with 1779
increment by 1
cache 20;

prompt
prompt Creating sequence S_EPM_CASE_WEIGHTDETAIL
prompt =========================================
prompt
create sequence S_EPM_CASE_WEIGHTDETAIL
minvalue 1
maxvalue 999999999999999999999999999
start with 1819
increment by 1
cache 20;

prompt
prompt Creating sequence S_EPM_CASE_WEIGHTSET
prompt ======================================
prompt
create sequence S_EPM_CASE_WEIGHTSET
minvalue 1
maxvalue 999999999999999999999999999
start with 629
increment by 1
cache 20;

prompt
prompt Creating sequence S_EPM_CASE_WORKFLOW
prompt =====================================
prompt
create sequence S_EPM_CASE_WORKFLOW
minvalue 1
maxvalue 999999999999999999999999999
start with 5040
increment by 1
cache 20;

prompt
prompt Creating sequence S_EPM_DATA_ACTIVITY_SCORE
prompt ===========================================
prompt
create sequence S_EPM_DATA_ACTIVITY_SCORE
minvalue 1
maxvalue 999999999999999999999999999
start with 1429
increment by 1
cache 20;

prompt
prompt Creating sequence S_EPM_DIRECTOR_KBB
prompt ====================================
prompt
create sequence S_EPM_DIRECTOR_KBB
minvalue 1
maxvalue 999999999999999999999999999
start with 21
increment by 1
cache 20;

prompt
prompt Creating sequence S_EPM_DIRECTOR_KEYINDEX
prompt =========================================
prompt
create sequence S_EPM_DIRECTOR_KEYINDEX
minvalue 1
maxvalue 999999999999999999999999999
start with 305
increment by 1
cache 20;

prompt
prompt Creating sequence S_EPM_DIRECTOR_KPIMONITOR
prompt ===========================================
prompt
create sequence S_EPM_DIRECTOR_KPIMONITOR
minvalue 1
maxvalue 999999999999999999999999999
start with 1053
increment by 1
cache 20;

prompt
prompt Creating sequence S_EPM_DIRECTOR_TASK
prompt =====================================
prompt
create sequence S_EPM_DIRECTOR_TASK
minvalue 1
maxvalue 999999999999999999999999999
start with 182
increment by 1
cache 20;

prompt
prompt Creating sequence S_EPM_DIRECTOR_TASKMONITOR
prompt ============================================
prompt
create sequence S_EPM_DIRECTOR_TASKMONITOR
minvalue 1
maxvalue 999999999999999999999999999
start with 405
increment by 1
cache 20;

prompt
prompt Creating sequence S_EPM_FACTOR
prompt ==============================
prompt
create sequence S_EPM_FACTOR
minvalue 1
maxvalue 999999999999999999999999999
start with 243
increment by 1
cache 20;

prompt
prompt Creating sequence S_EPM_FACTOR_DATA
prompt ===================================
prompt
create sequence S_EPM_FACTOR_DATA
minvalue 1
maxvalue 999999999999999999999999999
start with 42
increment by 1
cache 20;

prompt
prompt Creating sequence S_EPM_FACTOR_PERIOD
prompt =====================================
prompt
create sequence S_EPM_FACTOR_PERIOD
minvalue 1
maxvalue 999999999999999999999999999
start with 21
increment by 1
cache 20;

prompt
prompt Creating sequence S_EPM_FACTOR_TYPE
prompt ===================================
prompt
create sequence S_EPM_FACTOR_TYPE
minvalue 1
maxvalue 999999999999999999999999999
start with 22
increment by 1
cache 20;

prompt
prompt Creating sequence S_EPM_INDEX
prompt =============================
prompt
create sequence S_EPM_INDEX
minvalue 1
maxvalue 999999999999999999999999999
start with 3077
increment by 1
cache 20;

prompt
prompt Creating sequence S_EPM_INDEX_FACTOR
prompt ====================================
prompt
create sequence S_EPM_INDEX_FACTOR
minvalue 1
maxvalue 999999999999999999999999999
start with 4406
increment by 1
cache 20;

prompt
prompt Creating sequence S_EPM_KBB_DETAIL
prompt ==================================
prompt
create sequence S_EPM_KBB_DETAIL
minvalue 1
maxvalue 999999999999999999999999999
start with 26
increment by 1
cache 20;

prompt
prompt Creating sequence S_EPM_LEADER_KEYINDEX
prompt =======================================
prompt
create sequence S_EPM_LEADER_KEYINDEX
minvalue 1
maxvalue 999999999999999999999999999
start with 45
increment by 1
cache 20;

prompt
prompt Creating sequence S_EPM_MONITOR_MONTH
prompt =====================================
prompt
create sequence S_EPM_MONITOR_MONTH
minvalue 1
maxvalue 999999999999999999999999999
start with 4170
increment by 1
cache 20;

prompt
prompt Creating sequence S_EPM_MONITOR_QUARTOR
prompt =======================================
prompt
create sequence S_EPM_MONITOR_QUARTOR
minvalue 1
maxvalue 999999999999999999999999999
start with 425
increment by 1
cache 20;

prompt
prompt Creating sequence S_EPM_MONTH_FACTOR
prompt ====================================
prompt
create sequence S_EPM_MONTH_FACTOR
minvalue 1
maxvalue 999999999999999999999999999
start with 21
increment by 1
cache 20;

prompt
prompt Creating sequence S_EPM_PERSON_CONTRACT
prompt =======================================
prompt
create sequence S_EPM_PERSON_CONTRACT
minvalue 1
maxvalue 999999999999999999999999999
start with 1382
increment by 1
cache 20;

prompt
prompt Creating sequence S_EPM_PERSON_DAILYTASK
prompt ========================================
prompt
create sequence S_EPM_PERSON_DAILYTASK
minvalue 1
maxvalue 999999999999999999999999999
start with 9346
increment by 1
cache 20;

prompt
prompt Creating sequence S_EPM_PERSON_KBB
prompt ==================================
prompt
create sequence S_EPM_PERSON_KBB
minvalue 1
maxvalue 999999999999999999999999999
start with 126
increment by 1
cache 20;

prompt
prompt Creating sequence S_EPM_PERSON_KEYINDEX
prompt =======================================
prompt
create sequence S_EPM_PERSON_KEYINDEX
minvalue 1
maxvalue 999999999999999999999999999
start with 21
increment by 1
cache 20;

prompt
prompt Creating sequence S_EPM_PERSON_SPECTASK
prompt =======================================
prompt
create sequence S_EPM_PERSON_SPECTASK
minvalue 1
maxvalue 999999999999999999999999999
start with 4471
increment by 1
cache 20;

prompt
prompt Creating sequence S_EPM_PERSON_TASK
prompt ===================================
prompt
create sequence S_EPM_PERSON_TASK
minvalue 1
maxvalue 999999999999999999999999999
start with 41
increment by 1
cache 20;

prompt
prompt Creating sequence S_EPM_PERSON_TEMPTASK
prompt =======================================
prompt
create sequence S_EPM_PERSON_TEMPTASK
minvalue 1
maxvalue 999999999999999999999999999
start with 3705
increment by 1
cache 20;

prompt
prompt Creating sequence S_EPM_QUARTOR_FACTOR
prompt ======================================
prompt
create sequence S_EPM_QUARTOR_FACTOR
minvalue 1
maxvalue 999999999999999999999999999
start with 21
increment by 1
cache 20;

prompt
prompt Creating sequence S_EPM_RESULT_APPEAL
prompt =====================================
prompt
create sequence S_EPM_RESULT_APPEAL
minvalue 1
maxvalue 999999999999999999999999999
start with 43
increment by 1
cache 20;

prompt
prompt Creating sequence S_EPM_SECTION_KPIMONITOR
prompt ==========================================
prompt
create sequence S_EPM_SECTION_KPIMONITOR
minvalue 1
maxvalue 999999999999999999999999999
start with 3778
increment by 1
cache 20;

prompt
prompt Creating sequence S_EPM_SECTION_TASKMONITOR
prompt ===========================================
prompt
create sequence S_EPM_SECTION_TASKMONITOR
minvalue 1
maxvalue 999999999999999999999999999
start with 1369
increment by 1
cache 20;

prompt
prompt Creating sequence S_EPM_TEMPLATE_KBB
prompt ====================================
prompt
create sequence S_EPM_TEMPLATE_KBB
minvalue 1
maxvalue 999999999999999999999999999
start with 22
increment by 1
cache 20;

prompt
prompt Creating sequence S_EPM_WORKFLOW_CONSIGN_SET
prompt ============================================
prompt
create sequence S_EPM_WORKFLOW_CONSIGN_SET
minvalue 1
maxvalue 999999999999999999999999999
start with 41
increment by 1
cache 20;

prompt
prompt Creating sequence S_EPM_WORK_EVENT
prompt ==================================
prompt
create sequence S_EPM_WORK_EVENT
minvalue 1
maxvalue 999999999999999999999999999
start with 41
increment by 1
cache 20;

prompt
prompt Creating sequence S_EPM_WORK_EVENT_ORG
prompt ======================================
prompt
create sequence S_EPM_WORK_EVENT_ORG
minvalue 1
maxvalue 999999999999999999999999999
start with 42
increment by 1
cache 20;

prompt
prompt Creating sequence S_EPM_WORK_EVENT_PERSONAL
prompt ===========================================
prompt
create sequence S_EPM_WORK_EVENT_PERSONAL
minvalue 1
maxvalue 999999999999999999999999999
start with 4
increment by 1
cache 20;

prompt
prompt Creating sequence S_EPM_WORK_PLAN
prompt =================================
prompt
create sequence S_EPM_WORK_PLAN
minvalue 1
maxvalue 999999999999999999999999999
start with 22
increment by 1
cache 20;

prompt
prompt Creating sequence S_EPM_WORK_PLANPERIOD
prompt =======================================
prompt
create sequence S_EPM_WORK_PLANPERIOD
minvalue 1
maxvalue 999999999999999999999999999
start with 222
increment by 1
cache 20;

prompt
prompt Creating sequence S_EPM_WORK_PLANTASKS
prompt ======================================
prompt
create sequence S_EPM_WORK_PLANTASKS
minvalue 1
maxvalue 999999999999999999999999999
start with 8
increment by 1
cache 20;

prompt
prompt Creating sequence S_EPM_WORK_SUMMARIZE
prompt ======================================
prompt
create sequence S_EPM_WORK_SUMMARIZE
minvalue 1
maxvalue 999999999999999999999999999
start with 122
increment by 1
cache 20;

prompt
prompt Creating sequence S_ETL_ORG
prompt ===========================
prompt
create sequence S_ETL_ORG
minvalue 1
maxvalue 999999999999999999999999999
start with 178
increment by 1
nocache;

prompt
prompt Creating sequence S_HR_ABS_ABILITY
prompt ==================================
prompt
create sequence S_HR_ABS_ABILITY
minvalue 1
maxvalue 999999999999999999999999999
start with 42
increment by 1
cache 20;

prompt
prompt Creating sequence S_HR_ABS_ABILITYDETAIL
prompt ========================================
prompt
create sequence S_HR_ABS_ABILITYDETAIL
minvalue 1
maxvalue 999999999999999999999999999
start with 63
increment by 1
cache 20;

prompt
prompt Creating sequence S_HR_ABS_CATEGORY
prompt ===================================
prompt
create sequence S_HR_ABS_CATEGORY
minvalue 1
maxvalue 999999999999999999999999999
start with 64
increment by 1
cache 20;

prompt
prompt Creating sequence S_HR_ABS_COMPETENCY
prompt =====================================
prompt
create sequence S_HR_ABS_COMPETENCY
minvalue 1
maxvalue 999999999999999999999999999
start with 55
increment by 1
cache 20;

prompt
prompt Creating sequence S_HR_ABS_COMPETENCYSTD
prompt ========================================
prompt
create sequence S_HR_ABS_COMPETENCYSTD
minvalue 1
maxvalue 999999999999999999999999999
start with 132
increment by 1
cache 20;

prompt
prompt Creating sequence S_HR_AB_ABTYPE
prompt ================================
prompt
create sequence S_HR_AB_ABTYPE
minvalue 1
maxvalue 999999999999999999999999999
start with 45
increment by 1
cache 20;

prompt
prompt Creating sequence S_HR_AB_BASE
prompt ==============================
prompt
create sequence S_HR_AB_BASE
minvalue 1
maxvalue 999999999999999999999999999
start with 8
increment by 1
cache 20;

prompt
prompt Creating sequence S_HR_AB_INTERVIEW
prompt ===================================
prompt
create sequence S_HR_AB_INTERVIEW
minvalue 1
maxvalue 999999999999999999999999999
start with 3
increment by 1
cache 20;

prompt
prompt Creating sequence S_HR_AB_INTERVIEWITEM
prompt =======================================
prompt
create sequence S_HR_AB_INTERVIEWITEM
minvalue 1
maxvalue 999999999999999999999999999
start with 6
increment by 1
cache 20;

prompt
prompt Creating sequence S_HR_AB_IVITEM
prompt ================================
prompt
create sequence S_HR_AB_IVITEM
minvalue 1
maxvalue 999999999999999999999999999
start with 6
increment by 1
cache 20;

prompt
prompt Creating sequence S_HR_AB_SKILL
prompt ===============================
prompt
create sequence S_HR_AB_SKILL
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
cache 20;

prompt
prompt Creating sequence S_HR_AB_TRA
prompt =============================
prompt
create sequence S_HR_AB_TRA
minvalue 1
maxvalue 999999999999999999999999999
start with 2
increment by 1
cache 20;

prompt
prompt Creating sequence S_HR_AB_WORK
prompt ==============================
prompt
create sequence S_HR_AB_WORK
minvalue 1
maxvalue 999999999999999999999999999
start with 4
increment by 1
cache 20;

prompt
prompt Creating sequence S_HR_ALERT_MESSAGE
prompt ====================================
prompt
create sequence S_HR_ALERT_MESSAGE
minvalue 1
maxvalue 999999999999999999999999999
start with 2942
increment by 1
cache 20;

prompt
prompt Creating sequence S_HR_ALERT_RULE
prompt =================================
prompt
create sequence S_HR_ALERT_RULE
minvalue 1
maxvalue 999999999999999999999999999
start with 62
increment by 1
cache 20;

prompt
prompt Creating sequence S_HR_ALERT_SUBSCIBE
prompt =====================================
prompt
create sequence S_HR_ALERT_SUBSCIBE
minvalue 1
maxvalue 999999999999999999999999999
start with 163
increment by 1
cache 20;

prompt
prompt Creating sequence S_HR_CTRT_ARGUE
prompt =================================
prompt
create sequence S_HR_CTRT_ARGUE
minvalue 1
maxvalue 999999999999999999999999999
start with 41
increment by 1
cache 20;

prompt
prompt Creating sequence S_HR_CT_TR_PXKCTXKC
prompt =====================================
prompt
create sequence S_HR_CT_TR_PXKCTXKC
minvalue 1
maxvalue 999999999999999999999999999
start with 4
increment by 1
cache 20;

prompt
prompt Creating sequence S_HR_EVENT_ARCHIVESIN
prompt =======================================
prompt
create sequence S_HR_EVENT_ARCHIVESIN
minvalue 1
maxvalue 999999999999999999999999999
start with 22
increment by 1
cache 20;

prompt
prompt Creating sequence S_HR_EVENT_ARCHIVESOUT
prompt ========================================
prompt
create sequence S_HR_EVENT_ARCHIVESOUT
minvalue 1
maxvalue 999999999999999999999999999
start with 21
increment by 1
cache 20;

prompt
prompt Creating sequence S_HR_EVENT_BASE
prompt =================================
prompt
create sequence S_HR_EVENT_BASE
minvalue 1
maxvalue 999999999999999999999999999
start with 288
increment by 1
cache 20;

prompt
prompt Creating sequence S_HR_EVENT_COMM
prompt =================================
prompt
create sequence S_HR_EVENT_COMM
minvalue 1
maxvalue 999999999999999999999999999
start with 21
increment by 1
cache 20;

prompt
prompt Creating sequence S_HR_EVENT_CT_FIREREASON
prompt ==========================================
prompt
create sequence S_HR_EVENT_CT_FIREREASON
minvalue 1
maxvalue 999999999999999999999999999
start with 21
increment by 1
cache 20;

prompt
prompt Creating sequence S_HR_EVENT_DIMISSION
prompt ======================================
prompt
create sequence S_HR_EVENT_DIMISSION
minvalue 1
maxvalue 999999999999999999999999999
start with 121
increment by 1
cache 20;

prompt
prompt Creating sequence S_HR_EVENT_FIREINFO
prompt =====================================
prompt
create sequence S_HR_EVENT_FIREINFO
minvalue 1
maxvalue 999999999999999999999999999
start with 63
increment by 1
cache 20;

prompt
prompt Creating sequence S_HR_EVENT_INACTIVE
prompt =====================================
prompt
create sequence S_HR_EVENT_INACTIVE
minvalue 1
maxvalue 999999999999999999999999999
start with 21
increment by 1
cache 20;

prompt
prompt Creating sequence S_HR_EVENT_INDUEINFO
prompt ======================================
prompt
create sequence S_HR_EVENT_INDUEINFO
minvalue 1
maxvalue 999999999999999999999999999
start with 21
increment by 1
cache 20;

prompt
prompt Creating sequence S_HR_EVENT_INDUTYINFO
prompt =======================================
prompt
create sequence S_HR_EVENT_INDUTYINFO
minvalue 1
maxvalue 999999999999999999999999999
start with 21
increment by 1
cache 20;

prompt
prompt Creating sequence S_HR_EVENT_INUNACT
prompt ====================================
prompt
create sequence S_HR_EVENT_INUNACT
minvalue 1
maxvalue 999999999999999999999999999
start with 21
increment by 1
cache 20;

prompt
prompt Creating sequence S_HR_EVENT_REDPLOYINFO
prompt ========================================
prompt
create sequence S_HR_EVENT_REDPLOYINFO
minvalue 1
maxvalue 999999999999999999999999999
start with 65
increment by 1
cache 20;

prompt
prompt Creating sequence S_HR_EVENT_REENGAGE
prompt =====================================
prompt
create sequence S_HR_EVENT_REENGAGE
minvalue 1
maxvalue 999999999999999999999999999
start with 21
increment by 1
cache 20;

prompt
prompt Creating sequence S_HR_EVENT_RETIRE
prompt ===================================
prompt
create sequence S_HR_EVENT_RETIRE
minvalue 1
maxvalue 999999999999999999999999999
start with 21
increment by 1
cache 20;

prompt
prompt Creating sequence S_HR_EVENT_TRANSFERIN
prompt =======================================
prompt
create sequence S_HR_EVENT_TRANSFERIN
minvalue 1
maxvalue 999999999999999999999999999
start with 21
increment by 1
cache 20;

prompt
prompt Creating sequence S_HR_EVENT_TRANSFEROUT
prompt ========================================
prompt
create sequence S_HR_EVENT_TRANSFEROUT
minvalue 1
maxvalue 999999999999999999999999999
start with 22
increment by 1
cache 20;

prompt
prompt Creating sequence S_HR_EVENT_WORKHANDOVER
prompt =========================================
prompt
create sequence S_HR_EVENT_WORKHANDOVER
minvalue 1
maxvalue 999999999999999999999999999
start with 21
increment by 1
cache 20;

prompt
prompt Creating sequence S_HR_HI_ABILITY
prompt =================================
prompt
create sequence S_HR_HI_ABILITY
minvalue 1
maxvalue 999999999999999999999999999
start with 43
increment by 1
cache 20;

prompt
prompt Creating sequence S_HR_HI_ABILITYDETAIL
prompt =======================================
prompt
create sequence S_HR_HI_ABILITYDETAIL
minvalue 1
maxvalue 999999999999999999999999999
start with 65
increment by 1
cache 20;

prompt
prompt Creating sequence S_HR_HI_ABROADINFO
prompt ====================================
prompt
create sequence S_HR_HI_ABROADINFO
minvalue 1
maxvalue 999999999999999999999999999
start with 21
increment by 1
cache 20;

prompt
prompt Creating sequence S_HR_HI_ARCHIVES
prompt ==================================
prompt
create sequence S_HR_HI_ARCHIVES
minvalue 1
maxvalue 999999999999999999999999999
start with 2
increment by 1
cache 20;

prompt
prompt Creating sequence S_HR_HI_ASSESS
prompt ================================
prompt
create sequence S_HR_HI_ASSESS
minvalue 1
maxvalue 999999999999999999999999999
start with 21
increment by 1
cache 20;

prompt
prompt Creating sequence S_HR_HI_CERTIFICATE
prompt =====================================
prompt
create sequence S_HR_HI_CERTIFICATE
minvalue 1
maxvalue 999999999999999999999999999
start with 41
increment by 1
cache 20;

prompt
prompt Creating sequence S_HR_HI_CTRT
prompt ==============================
prompt
create sequence S_HR_HI_CTRT
minvalue 1
maxvalue 999999999999999999999999999
start with 104
increment by 1
cache 20;

prompt
prompt Creating sequence S_HR_HI_CT_PERSONSCOPE
prompt ========================================
prompt
create sequence S_HR_HI_CT_PERSONSCOPE
minvalue 1
maxvalue 999999999999999999999999999
start with 14
increment by 1
cache 20;

prompt
prompt Creating sequence S_HR_HI_CT_TERMTYPE
prompt =====================================
prompt
create sequence S_HR_HI_CT_TERMTYPE
minvalue 1
maxvalue 999999999999999999999999999
start with 21
increment by 1
cache 20;

prompt
prompt Creating sequence S_HR_HI_CT_TRACHARA
prompt =====================================
prompt
create sequence S_HR_HI_CT_TRACHARA
minvalue 1
maxvalue 999999999999999999999999999
start with 21
increment by 1
cache 20;

prompt
prompt Creating sequence S_HR_HI_CT_TRATYPE
prompt ====================================
prompt
create sequence S_HR_HI_CT_TRATYPE
minvalue 1
maxvalue 999999999999999999999999999
start with 24
increment by 1
cache 20;

prompt
prompt Creating sequence S_HR_HI_CT_WORKAGE
prompt ====================================
prompt
create sequence S_HR_HI_CT_WORKAGE
minvalue 1
maxvalue 999999999999999999999999999
start with 21
increment by 1
cache 20;

prompt
prompt Creating sequence S_HR_HI_EDU
prompt =============================
prompt
create sequence S_HR_HI_EDU
minvalue 1
maxvalue 999999999999999999999999999
start with 57607
increment by 1
cache 20;

prompt
prompt Creating sequence S_HR_HI_ENGAGE
prompt ================================
prompt
create sequence S_HR_HI_ENGAGE
minvalue 1
maxvalue 999999999999999999999999999
start with 21
increment by 1
cache 20;

prompt
prompt Creating sequence S_HR_HI_ERROR
prompt ===============================
prompt
create sequence S_HR_HI_ERROR
minvalue 1
maxvalue 999999999999999999999999999
start with 106
increment by 1
cache 20;

prompt
prompt Creating sequence S_HR_HI_ERRORPERSON
prompt =====================================
prompt
create sequence S_HR_HI_ERRORPERSON
minvalue 1
maxvalue 999999999999999999999999999
start with 6895
increment by 1
cache 20;

prompt
prompt Creating sequence S_HR_HI_EXAMINATION
prompt =====================================
prompt
create sequence S_HR_HI_EXAMINATION
minvalue 1
maxvalue 999999999999999999999999999
start with 21
increment by 1
cache 20;

prompt
prompt Creating sequence S_HR_HI_EXPERT
prompt ================================
prompt
create sequence S_HR_HI_EXPERT
minvalue 1
maxvalue 999999999999999999999999999
start with 20451
increment by 1
cache 20;

prompt
prompt Creating sequence S_HR_HI_GAME
prompt ==============================
prompt
create sequence S_HR_HI_GAME
minvalue 1
maxvalue 999999999999999999999999999
start with 21
increment by 1
cache 20;

prompt
prompt Creating sequence S_HR_HI_INJURE
prompt ================================
prompt
create sequence S_HR_HI_INJURE
minvalue 1
maxvalue 999999999999999999999999999
start with 3
increment by 1
cache 20;

prompt
prompt Creating sequence S_HR_HI_JOBINFO
prompt =================================
prompt
create sequence S_HR_HI_JOBINFO
minvalue 1
maxvalue 999999999999999999999999999
start with 21
increment by 1
cache 20;

prompt
prompt Creating sequence S_HR_HI_LANGSKILL
prompt ===================================
prompt
create sequence S_HR_HI_LANGSKILL
minvalue 1
maxvalue 999999999999999999999999999
start with 28
increment by 1
cache 20;

prompt
prompt Creating sequence S_HR_HI_MEDICAL
prompt =================================
prompt
create sequence S_HR_HI_MEDICAL
minvalue 1
maxvalue 999999999999999999999999999
start with 42
increment by 1
cache 20;

prompt
prompt Creating sequence S_HR_HI_PARTY
prompt ===============================
prompt
create sequence S_HR_HI_PARTY
minvalue 1
maxvalue 999999999999999999999999999
start with 22
increment by 1
cache 20;

prompt
prompt Creating sequence S_HR_HI_PERSON
prompt ================================
prompt
create sequence S_HR_HI_PERSON
minvalue 1
maxvalue 999999999999999999999999999
start with 27326
increment by 1
cache 20;

prompt
prompt Creating sequence S_HR_HI_PERSONEXTEND
prompt ======================================
prompt
create sequence S_HR_HI_PERSONEXTEND
minvalue 1
maxvalue 999999999999999999999999999
start with 66
increment by 1
cache 20;

prompt
prompt Creating sequence S_HR_HI_PERSONPOS
prompt ===================================
prompt
create sequence S_HR_HI_PERSONPOS
minvalue 1
maxvalue 999999999999999999999999999
start with 490
increment by 1
cache 20;

prompt
prompt Creating sequence S_HR_HI_POSINFO
prompt =================================
prompt
create sequence S_HR_HI_POSINFO
minvalue 1
maxvalue 999999999999999999999999999
start with 7053
increment by 1
cache 20;

prompt
prompt Creating sequence S_HR_HI_PROBATION
prompt ===================================
prompt
create sequence S_HR_HI_PROBATION
minvalue 1
maxvalue 999999999999999999999999999
start with 103
increment by 1
cache 20;

prompt
prompt Creating sequence S_HR_HI_PROFESSIONAL
prompt ======================================
prompt
create sequence S_HR_HI_PROFESSIONAL
minvalue 1
maxvalue 999999999999999999999999999
start with 100
increment by 1
cache 20;

prompt
prompt Creating sequence S_HR_HI_PROFESSIONALTYPE
prompt ==========================================
prompt
create sequence S_HR_HI_PROFESSIONALTYPE
minvalue 1
maxvalue 999999999999999999999999999
start with 41
increment by 1
cache 20;

prompt
prompt Creating sequence S_HR_HI_PROTYPE_REQUIRE
prompt =========================================
prompt
create sequence S_HR_HI_PROTYPE_REQUIRE
minvalue 1
maxvalue 999999999999999999999999999
start with 41
increment by 1
cache 20;

prompt
prompt Creating sequence S_HR_HI_RETIRE
prompt ================================
prompt
create sequence S_HR_HI_RETIRE
minvalue 1
maxvalue 999999999999999999999999999
start with 42
increment by 1
cache 20;

prompt
prompt Creating sequence S_HR_HI_RETIRE_EXTEND
prompt =======================================
prompt
create sequence S_HR_HI_RETIRE_EXTEND
minvalue 1
maxvalue 999999999999999999999999999
start with 21
increment by 1
cache 20;

prompt
prompt Creating sequence S_HR_HI_TECHTITLE
prompt ===================================
prompt
create sequence S_HR_HI_TECHTITLE
minvalue 1
maxvalue 999999999999999999999999999
start with 17353
increment by 1
cache 20;

prompt
prompt Creating sequence S_HR_HI_TECHWORK
prompt ==================================
prompt
create sequence S_HR_HI_TECHWORK
minvalue 1
maxvalue 999999999999999999999999999
start with 24
increment by 1
cache 20;

prompt
prompt Creating sequence S_HR_HI_TRAINING
prompt ==================================
prompt
create sequence S_HR_HI_TRAINING
minvalue 1
maxvalue 999999999999999999999999999
start with 45
increment by 1
cache 20;

prompt
prompt Creating sequence S_HR_HI_WORK
prompt ==============================
prompt
create sequence S_HR_HI_WORK
minvalue 1
maxvalue 999999999999999999999999999
start with 3
increment by 1
cache 20;

prompt
prompt Creating sequence S_HR_HI_WORKQUALIFY
prompt =====================================
prompt
create sequence S_HR_HI_WORKQUALIFY
minvalue 1
maxvalue 999999999999999999999999999
start with 20775
increment by 1
cache 20;

prompt
prompt Creating sequence S_HR_HI_WORKSKILLPERSON
prompt =========================================
prompt
create sequence S_HR_HI_WORKSKILLPERSON
minvalue 1
maxvalue 999999999999999999999999999
start with 21
increment by 1
cache 20;

prompt
prompt Creating sequence S_HR_HI_WORKSKILLTYPE
prompt =======================================
prompt
create sequence S_HR_HI_WORKSKILLTYPE
minvalue 1
maxvalue 999999999999999999999999999
start with 21
increment by 1
cache 20;

prompt
prompt Creating sequence S_HR_KPI_DEPT
prompt ===============================
prompt
create sequence S_HR_KPI_DEPT
minvalue 1
maxvalue 999999999999999999999999999
start with 2
increment by 1
cache 20;

prompt
prompt Creating sequence S_HR_OM_DUTY
prompt ==============================
prompt
create sequence S_HR_OM_DUTY
minvalue 1
maxvalue 999999999999999999999999999
start with 416
increment by 1
cache 20;

prompt
prompt Creating sequence S_HR_OM_PLAN
prompt ==============================
prompt
create sequence S_HR_OM_PLAN
minvalue 1
maxvalue 999999999999999999999999999
start with 62
increment by 1
cache 20;

prompt
prompt Creating sequence S_HR_OM_POS
prompt =============================
prompt
create sequence S_HR_OM_POS
minvalue 1
maxvalue 999999999999999999999999999
start with 20597
increment by 1
cache 20;

prompt
prompt Creating sequence S_HR_OM_POSABILITY
prompt ====================================
prompt
create sequence S_HR_OM_POSABILITY
minvalue 1
maxvalue 999999999999999999999999999
start with 119
increment by 1
cache 20;

prompt
prompt Creating sequence S_HR_OM_POSCTRL
prompt =================================
prompt
create sequence S_HR_OM_POSCTRL
minvalue 1
maxvalue 999999999999999999999999999
start with 141
increment by 1
cache 20;

prompt
prompt Creating sequence S_HR_OM_POSDESC
prompt =================================
prompt
create sequence S_HR_OM_POSDESC
minvalue 1
maxvalue 999999999999999999999999999
start with 45
increment by 1
cache 20;

prompt
prompt Creating sequence S_HR_OM_POSINDEX
prompt ==================================
prompt
create sequence S_HR_OM_POSINDEX
minvalue 1
maxvalue 999999999999999999999999999
start with 5237
increment by 1
cache 20;

prompt
prompt Creating sequence S_HR_OM_POSINDEX1
prompt ===================================
prompt
create sequence S_HR_OM_POSINDEX1
minvalue 1
maxvalue 999999999999999999999999999
start with 101
increment by 1
cache 20;

prompt
prompt Creating sequence S_HR_OM_POSPLAN
prompt =================================
prompt
create sequence S_HR_OM_POSPLAN
minvalue 1
maxvalue 999999999999999999999999999
start with 222
increment by 1
cache 20;

prompt
prompt Creating sequence S_HR_OM_POSSERIAL
prompt ===================================
prompt
create sequence S_HR_OM_POSSERIAL
minvalue 1
maxvalue 999999999999999999999999999
start with 61
increment by 1
cache 20;

prompt
prompt Creating sequence S_HR_OM_POSSUCCESSION
prompt =======================================
prompt
create sequence S_HR_OM_POSSUCCESSION
minvalue 1
maxvalue 999999999999999999999999999
start with 104
increment by 1
cache 20;

prompt
prompt Creating sequence S_HR_OM_POSSUPERV
prompt ===================================
prompt
create sequence S_HR_OM_POSSUPERV
minvalue 1
maxvalue 999999999999999999999999999
start with 31192
increment by 1
cache 20;

prompt
prompt Creating sequence S_HR_OM_POSTRAINING
prompt =====================================
prompt
create sequence S_HR_OM_POSTRAINING
minvalue 1
maxvalue 999999999999999999999999999
start with 49
increment by 1
cache 20;

prompt
prompt Creating sequence S_HR_OM_SERIALABILITY
prompt =======================================
prompt
create sequence S_HR_OM_SERIALABILITY
minvalue 1
maxvalue 999999999999999999999999999
start with 23
increment by 1
cache 20;

prompt
prompt Creating sequence S_HR_OM_SERIALDESC
prompt ====================================
prompt
create sequence S_HR_OM_SERIALDESC
minvalue 1
maxvalue 999999999999999999999999999
start with 42
increment by 1
cache 20;

prompt
prompt Creating sequence S_HR_OM_SERIALINDEX
prompt =====================================
prompt
create sequence S_HR_OM_SERIALINDEX
minvalue 1
maxvalue 999999999999999999999999999
start with 41
increment by 1
cache 20;

prompt
prompt Creating sequence S_HR_OM_SERIALSALARYSTANDARD
prompt ==============================================
prompt
create sequence S_HR_OM_SERIALSALARYSTANDARD
minvalue 1
maxvalue 999999999999999999999999999
start with 21
increment by 1
cache 20;

prompt
prompt Creating sequence S_HR_OM_SERIALTRAIN
prompt =====================================
prompt
create sequence S_HR_OM_SERIALTRAIN
minvalue 1
maxvalue 999999999999999999999999999
start with 24
increment by 1
cache 20;

prompt
prompt Creating sequence S_HR_ORG_B07
prompt ==============================
prompt
create sequence S_HR_ORG_B07
minvalue 1
maxvalue 999999999999999999999999999
start with 164
increment by 1
cache 20;

prompt
prompt Creating sequence S_HR_ORG_B10
prompt ==============================
prompt
create sequence S_HR_ORG_B10
minvalue 1
maxvalue 999999999999999999999999999
start with 2
increment by 1
cache 20;

prompt
prompt Creating sequence S_HR_ORG_B104
prompt ===============================
prompt
create sequence S_HR_ORG_B104
minvalue 1
maxvalue 999999999999999999999999999
start with 43
increment by 1
cache 20;

prompt
prompt Creating sequence S_HR_ORG_B13
prompt ==============================
prompt
create sequence S_HR_ORG_B13
minvalue 1
maxvalue 999999999999999999999999999
start with 2
increment by 1
cache 20;

prompt
prompt Creating sequence S_HR_ORG_B130
prompt ===============================
prompt
create sequence S_HR_ORG_B130
minvalue 1
maxvalue 999999999999999999999999999
start with 44
increment by 1
cache 20;

prompt
prompt Creating sequence S_HR_ORG_B16
prompt ==============================
prompt
create sequence S_HR_ORG_B16
minvalue 1
maxvalue 999999999999999999999999999
start with 2
increment by 1
cache 20;

prompt
prompt Creating sequence S_HR_ORG_B19
prompt ==============================
prompt
create sequence S_HR_ORG_B19
minvalue 1
maxvalue 999999999999999999999999999
start with 21
increment by 1
cache 20;

prompt
prompt Creating sequence S_HR_ORG_BASE
prompt ===============================
prompt
create sequence S_HR_ORG_BASE
minvalue 1
maxvalue 999999999999999999999999999
start with 8915
increment by 1
cache 20;

prompt
prompt Creating sequence S_HR_ORG_INDEX
prompt ================================
prompt
create sequence S_HR_ORG_INDEX
minvalue 1
maxvalue 999999999999999999999999999
start with 3871
increment by 1
cache 20;

prompt
prompt Creating sequence S_HR_ORG_SALARYCONTROL
prompt ========================================
prompt
create sequence S_HR_ORG_SALARYCONTROL
minvalue 1
maxvalue 999999999999999999999999999
start with 12
increment by 1
cache 20;

prompt
prompt Creating sequence S_HR_ORG_SCHEME
prompt =================================
prompt
create sequence S_HR_ORG_SCHEME
minvalue 1
maxvalue 999999999999999999999999999
start with 41
increment by 1
cache 20;

prompt
prompt Creating sequence S_HR_REPORT_LOG
prompt =================================
prompt
create sequence S_HR_REPORT_LOG
minvalue 1
maxvalue 10000
start with 5494
increment by 1
cache 20;

prompt
prompt Creating sequence S_HR_RT_BESPEAK
prompt =================================
prompt
create sequence S_HR_RT_BESPEAK
minvalue 1
maxvalue 999999999999999999999999999
start with 4
increment by 1
cache 20;

prompt
prompt Creating sequence S_HR_RT_HIRENOTICE
prompt ====================================
prompt
create sequence S_HR_RT_HIRENOTICE
minvalue 1
maxvalue 999999999999999999999999999
start with 2
increment by 1
cache 20;

prompt
prompt Creating sequence S_HR_RT_INTERVIEW
prompt ===================================
prompt
create sequence S_HR_RT_INTERVIEW
minvalue 1
maxvalue 999999999999999999999999999
start with 7
increment by 1
cache 20;

prompt
prompt Creating sequence S_HR_RT_INTERVIEWCOMMENT
prompt ==========================================
prompt
create sequence S_HR_RT_INTERVIEWCOMMENT
minvalue 1
maxvalue 999999999999999999999999999
start with 2
increment by 1
cache 20;

prompt
prompt Creating sequence S_HR_RT_JOBCHAN
prompt =================================
prompt
create sequence S_HR_RT_JOBCHAN
minvalue 1
maxvalue 999999999999999999999999999
start with 2
increment by 1
cache 20;

prompt
prompt Creating sequence S_HR_RT_PLAN
prompt ==============================
prompt
create sequence S_HR_RT_PLAN
minvalue 1
maxvalue 999999999999999999999999999
start with 22
increment by 1
cache 20;

prompt
prompt Creating sequence S_HR_RT_PLANFEE
prompt =================================
prompt
create sequence S_HR_RT_PLANFEE
minvalue 1
maxvalue 999999999999999999999999999
start with 3
increment by 1
cache 20;

prompt
prompt Creating sequence S_HR_RT_PLANREQ
prompt =================================
prompt
create sequence S_HR_RT_PLANREQ
minvalue 1
maxvalue 999999999999999999999999999
start with 23
increment by 1
cache 20;

prompt
prompt Creating sequence S_HR_RT_PLOYPLAN
prompt ==================================
prompt
create sequence S_HR_RT_PLOYPLAN
minvalue 1
maxvalue 999999999999999999999999999
start with 21
increment by 1
cache 20;

prompt
prompt Creating sequence S_HR_RT_QUESTION
prompt ==================================
prompt
create sequence S_HR_RT_QUESTION
minvalue 1
maxvalue 999999999999999999999999999
start with 2
increment by 1
cache 20;

prompt
prompt Creating sequence S_HR_RT_QUESTIONTYPE
prompt ======================================
prompt
create sequence S_HR_RT_QUESTIONTYPE
minvalue 1
maxvalue 999999999999999999999999999
start with 9
increment by 1
cache 20;

prompt
prompt Creating sequence S_HR_RT_REQUEST
prompt =================================
prompt
create sequence S_HR_RT_REQUEST
minvalue 1
maxvalue 999999999999999999999999999
start with 42
increment by 1
cache 20;

prompt
prompt Creating sequence S_HR_RT_REQUESTS
prompt ==================================
prompt
create sequence S_HR_RT_REQUESTS
minvalue 1
maxvalue 999999999999999999999999999
start with 42
increment by 1
cache 20;

prompt
prompt Creating sequence S_HR_SURVEY_PAPER
prompt ===================================
prompt
create sequence S_HR_SURVEY_PAPER
minvalue 1
maxvalue 999999999999999999999999999
start with 23
increment by 1
cache 20;

prompt
prompt Creating sequence S_HR_SURVEY_PAPER_ANSWERS
prompt ===========================================
prompt
create sequence S_HR_SURVEY_PAPER_ANSWERS
minvalue 1
maxvalue 999999999999999999999999999
start with 123
increment by 1
cache 20;

prompt
prompt Creating sequence S_HR_SURVEY_PAPER_ANSWERS_R
prompt =============================================
prompt
create sequence S_HR_SURVEY_PAPER_ANSWERS_R
minvalue 1
maxvalue 999999999999999999999999999
start with 273
increment by 1
cache 20;

prompt
prompt Creating sequence S_HR_SURVEY_PAPER_QUESTION
prompt ============================================
prompt
create sequence S_HR_SURVEY_PAPER_QUESTION
minvalue 1
maxvalue 999999999999999999999999999
start with 84
increment by 1
cache 20;

prompt
prompt Creating sequence S_HR_SURVEY_PAPER_QUESTION_R
prompt ==============================================
prompt
create sequence S_HR_SURVEY_PAPER_QUESTION_R
minvalue 1
maxvalue 999999999999999999999999999
start with 114
increment by 1
cache 20;

prompt
prompt Creating sequence S_HR_SURVEY_PAPER_SUBJECT
prompt ===========================================
prompt
create sequence S_HR_SURVEY_PAPER_SUBJECT
minvalue 1
maxvalue 999999999999999999999999999
start with 28
increment by 1
cache 20;

prompt
prompt Creating sequence S_HR_SURVEY_QUESTION
prompt ======================================
prompt
create sequence S_HR_SURVEY_QUESTION
minvalue 1
maxvalue 999999999999999999999999999
start with 28
increment by 1
cache 20;

prompt
prompt Creating sequence S_HR_SURVEY_TOPIC
prompt ===================================
prompt
create sequence S_HR_SURVEY_TOPIC
minvalue 1
maxvalue 999999999999999999999999999
start with 11
increment by 1
cache 20;

prompt
prompt Creating sequence S_HR_SYSTEM_CODEPOOL
prompt ======================================
prompt
create sequence S_HR_SYSTEM_CODEPOOL
minvalue 1
maxvalue 999999999999999999999999999
start with 141
increment by 1
cache 20;

prompt
prompt Creating sequence S_HR_SYSTEM_CUSTOMINFOSET
prompt ===========================================
prompt
create sequence S_HR_SYSTEM_CUSTOMINFOSET
minvalue 1
maxvalue 999999999999999999999999999
start with 181
increment by 1
cache 20;

prompt
prompt Creating sequence S_HR_SYSTEM_DISPLAYITEM
prompt =========================================
prompt
create sequence S_HR_SYSTEM_DISPLAYITEM
minvalue 1
maxvalue 999999999999999999999999999
start with 408
increment by 1
cache 20;

prompt
prompt Creating sequence S_HR_SYSTEM_INFOSET
prompt =====================================
prompt
create sequence S_HR_SYSTEM_INFOSET
minvalue 1
maxvalue 999999999999999999999999999
start with 101
increment by 1
cache 20;

prompt
prompt Creating sequence S_HR_SYSTEM_RULEITEM
prompt ======================================
prompt
create sequence S_HR_SYSTEM_RULEITEM
minvalue 1
maxvalue 999999999999999999999999999
start with 263
increment by 1
cache 20;

prompt
prompt Creating sequence S_HR_SYSTEM_TOPIC
prompt ===================================
prompt
create sequence S_HR_SYSTEM_TOPIC
minvalue 1
maxvalue 999999999999999999999999999
start with 263
increment by 1
cache 20;

prompt
prompt Creating sequence S_HR_TM_ERRAND
prompt ================================
prompt
create sequence S_HR_TM_ERRAND
minvalue 1
maxvalue 999999999999999999999999999
start with 23
increment by 1
cache 20;

prompt
prompt Creating sequence S_HR_TM_ERRANDTYPE
prompt ====================================
prompt
create sequence S_HR_TM_ERRANDTYPE
minvalue 1
maxvalue 999999999999999999999999999
start with 21
increment by 1
cache 20;

prompt
prompt Creating sequence S_HR_TM_LEAVE
prompt ===============================
prompt
create sequence S_HR_TM_LEAVE
minvalue 1
maxvalue 999999999999999999999999999
start with 21
increment by 1
cache 20;

prompt
prompt Creating sequence S_HR_TM_LEAVETYPE
prompt ===================================
prompt
create sequence S_HR_TM_LEAVETYPE
minvalue 1
maxvalue 999999999999999999999999999
start with 21
increment by 1
cache 20;

prompt
prompt Creating sequence S_HR_TM_MONTH
prompt ===============================
prompt
create sequence S_HR_TM_MONTH
minvalue 1
maxvalue 999999999999999999999999999
start with 42
increment by 1
cache 20;

prompt
prompt Creating sequence S_HR_TM_MONTHRESULT
prompt =====================================
prompt
create sequence S_HR_TM_MONTHRESULT
minvalue 1
maxvalue 999999999999999999999999999
start with 150
increment by 1
cache 20;

prompt
prompt Creating sequence S_HR_TM_OVERTIME
prompt ==================================
prompt
create sequence S_HR_TM_OVERTIME
minvalue 1
maxvalue 999999999999999999999999999
start with 103
increment by 1
cache 20;

prompt
prompt Creating sequence S_HR_TM_OVERTIMETYPE
prompt ======================================
prompt
create sequence S_HR_TM_OVERTIMETYPE
minvalue 1
maxvalue 999999999999999999999999999
start with 21
increment by 1
cache 20;

prompt
prompt Creating sequence S_HR_TM_PERIOD
prompt ================================
prompt
create sequence S_HR_TM_PERIOD
minvalue 1
maxvalue 999999999999999999999999999
start with 42
increment by 1
cache 20;

prompt
prompt Creating sequence S_HR_TM_VAC
prompt =============================
prompt
create sequence S_HR_TM_VAC
minvalue 1
maxvalue 999999999999999999999999999
start with 102
increment by 1
cache 20;

prompt
prompt Creating sequence S_HR_TM_VACRATE
prompt =================================
prompt
create sequence S_HR_TM_VACRATE
minvalue 1
maxvalue 999999999999999999999999999
start with 88
increment by 1
cache 20;

prompt
prompt Creating sequence S_HR_TM_VACRULE
prompt =================================
prompt
create sequence S_HR_TM_VACRULE
minvalue 1
maxvalue 999999999999999999999999999
start with 3
increment by 1
cache 20;

prompt
prompt Creating sequence S_HR_TM_VACTYPE
prompt =================================
prompt
create sequence S_HR_TM_VACTYPE
minvalue 1
maxvalue 999999999999999999999999999
start with 61
increment by 1
cache 20;

prompt
prompt Creating sequence S_HR_TR_ACTIVITY
prompt ==================================
prompt
create sequence S_HR_TR_ACTIVITY
minvalue 1
maxvalue 999999999999999999999999999
start with 7343
increment by 1
cache 20;

prompt
prompt Creating sequence S_HR_TR_ACTIVITYCALENDAR
prompt ==========================================
prompt
create sequence S_HR_TR_ACTIVITYCALENDAR
minvalue 1
maxvalue 999999999999999999999999999
start with 707
increment by 1
cache 20;

prompt
prompt Creating sequence S_HR_TR_ACTIVITYPERSON
prompt ========================================
prompt
create sequence S_HR_TR_ACTIVITYPERSON
minvalue 1
maxvalue 999999999999999999999999999
start with 290173
increment by 1
cache 20;

prompt
prompt Creating sequence S_HR_TR_ACTIVITYREADY
prompt =======================================
prompt
create sequence S_HR_TR_ACTIVITYREADY
minvalue 1
maxvalue 999999999999999999999999999
start with 8259
increment by 1
cache 20;

prompt
prompt Creating sequence S_HR_TR_ACTIVITYSTOCK
prompt =======================================
prompt
create sequence S_HR_TR_ACTIVITYSTOCK
minvalue 1
maxvalue 999999999999999999999999999
start with 2
increment by 1
cache 20;

prompt
prompt Creating sequence S_HR_TR_BZGWDY
prompt ================================
prompt
create sequence S_HR_TR_BZGWDY
minvalue 1
maxvalue 999999999999999999999999999
start with 124
increment by 1
cache 20;

prompt
prompt Creating sequence S_HR_TR_COMPARESTANDARD
prompt =========================================
prompt
create sequence S_HR_TR_COMPARESTANDARD
minvalue 1
maxvalue 999999999999999999999999999
start with 1022478
increment by 1
cache 20;

prompt
prompt Creating sequence S_HR_TR_COURSE
prompt ================================
prompt
create sequence S_HR_TR_COURSE
minvalue 1
maxvalue 999999999999999999999999999
start with 22
increment by 1
cache 20;

prompt
prompt Creating sequence S_HR_TR_CT_CERLEVEL
prompt =====================================
prompt
create sequence S_HR_TR_CT_CERLEVEL
minvalue 1
maxvalue 999999999999999999999999999
start with 21
increment by 1
cache 20;

prompt
prompt Creating sequence S_HR_TR_CT_POSTTRAINTYPE
prompt ==========================================
prompt
create sequence S_HR_TR_CT_POSTTRAINTYPE
minvalue 1
maxvalue 999999999999999999999999999
start with 21
increment by 1
cache 20;

prompt
prompt Creating sequence S_HR_TR_CT_TRAINTYPE
prompt ======================================
prompt
create sequence S_HR_TR_CT_TRAINTYPE
minvalue 1
maxvalue 999999999999999999999999999
start with 21
increment by 1
cache 20;

prompt
prompt Creating sequence S_HR_TR_EDUSELFSTUDY
prompt ======================================
prompt
create sequence S_HR_TR_EDUSELFSTUDY
minvalue 1
maxvalue 999999999999999999999999999
start with 3876
increment by 1
cache 20;

prompt
prompt Creating sequence S_HR_TR_EVALUATEACTIVITY
prompt ==========================================
prompt
create sequence S_HR_TR_EVALUATEACTIVITY
minvalue 1
maxvalue 999999999999999999999999999
start with 2416
increment by 1
cache 20;

prompt
prompt Creating sequence S_HR_TR_EVALUATEKNOWLEDGE
prompt ===========================================
prompt
create sequence S_HR_TR_EVALUATEKNOWLEDGE
minvalue 1
maxvalue 999999999999999999999999999
start with 269
increment by 1
cache 20;

prompt
prompt Creating sequence S_HR_TR_EVALUATEKNOWLEDGETHIRD
prompt ================================================
prompt
create sequence S_HR_TR_EVALUATEKNOWLEDGETHIRD
minvalue 1
maxvalue 999999999999999999999999999
start with 124
increment by 1
cache 20;

prompt
prompt Creating sequence S_HR_TR_GAME
prompt ==============================
prompt
create sequence S_HR_TR_GAME
minvalue 1
maxvalue 999999999999999999999999999
start with 934
increment by 1
cache 20;

prompt
prompt Creating sequence S_HR_TR_GWKCGX
prompt ================================
prompt
create sequence S_HR_TR_GWKCGX
minvalue 1
maxvalue 999999999999999999999999999
start with 2
increment by 1
cache 20;

prompt
prompt Creating sequence S_HR_TR_NOTICEPROCESS
prompt =======================================
prompt
create sequence S_HR_TR_NOTICEPROCESS
minvalue 1
maxvalue 999999999999999999999999999
start with 1585
increment by 1
cache 20;

prompt
prompt Creating sequence S_HR_TR_PLANPROCESS
prompt =====================================
prompt
create sequence S_HR_TR_PLANPROCESS
minvalue 1
maxvalue 999999999999999999999999999
start with 164
increment by 1
cache 20;

prompt
prompt Creating sequence S_HR_TR_POSTTRAIN
prompt ===================================
prompt
create sequence S_HR_TR_POSTTRAIN
minvalue 1
maxvalue 999999999999999999999999999
start with 64
increment by 1
cache 20;

prompt
prompt Creating sequence S_HR_TR_POSTTRAINEDU
prompt ======================================
prompt
create sequence S_HR_TR_POSTTRAINEDU
minvalue 1
maxvalue 999999999999999999999999999
start with 67
increment by 1
cache 20;

prompt
prompt Creating sequence S_HR_TR_POSTTRAINITEM
prompt =======================================
prompt
create sequence S_HR_TR_POSTTRAINITEM
minvalue 1
maxvalue 999999999999999999999999999
start with 66
increment by 1
cache 20;

prompt
prompt Creating sequence S_HR_TR_POSTTRAINQUALIFY
prompt ==========================================
prompt
create sequence S_HR_TR_POSTTRAINQUALIFY
minvalue 1
maxvalue 999999999999999999999999999
start with 69
increment by 1
cache 20;

prompt
prompt Creating sequence S_HR_TR_READYTEMPLATE
prompt =======================================
prompt
create sequence S_HR_TR_READYTEMPLATE
minvalue 1
maxvalue 999999999999999999999999999
start with 21
increment by 1
cache 20;

prompt
prompt Creating sequence S_HR_TR_SELFSUBTYPE
prompt =====================================
prompt
create sequence S_HR_TR_SELFSUBTYPE
minvalue 1
maxvalue 999999999999999999999999999
start with 806
increment by 1
cache 20;

prompt
prompt Creating sequence S_HR_TR_TRAINAPPLICATION
prompt ==========================================
prompt
create sequence S_HR_TR_TRAINAPPLICATION
minvalue 1
maxvalue 999999999999999999999999999
start with 43
increment by 1
cache 20;

prompt
prompt Creating sequence S_HR_TR_TRAINBASEFILE
prompt =======================================
prompt
create sequence S_HR_TR_TRAINBASEFILE
minvalue 1
maxvalue 999999999999999999999999999
start with 288
increment by 1
cache 20;

prompt
prompt Creating sequence S_HR_TR_TRAINCHANNEL
prompt ======================================
prompt
create sequence S_HR_TR_TRAINCHANNEL
minvalue 1
maxvalue 999999999999999999999999999
start with 122
increment by 1
cache 20;

prompt
prompt Creating sequence S_HR_TR_TRAINCLASS
prompt ====================================
prompt
create sequence S_HR_TR_TRAINCLASS
minvalue 1
maxvalue 999999999999999999999999999
start with 7
increment by 1
cache 20;

prompt
prompt Creating sequence S_HR_TR_TRAININGOUTCOURSE
prompt ===========================================
prompt
create sequence S_HR_TR_TRAININGOUTCOURSE
minvalue 1
maxvalue 999999999999999999999999999
start with 23
increment by 1
cache 20;

prompt
prompt Creating sequence S_HR_TR_TRAINNOTICE
prompt =====================================
prompt
create sequence S_HR_TR_TRAINNOTICE
minvalue 1
maxvalue 999999999999999999999999999
start with 83
increment by 1
cache 20;

prompt
prompt Creating sequence S_HR_TR_TRAINPLAN
prompt ===================================
prompt
create sequence S_HR_TR_TRAINPLAN
minvalue 1
maxvalue 999999999999999999999999999
start with 1079
increment by 1
cache 20;

prompt
prompt Creating sequence S_HR_TR_TRAINREQUIREMENT
prompt ==========================================
prompt
create sequence S_HR_TR_TRAINREQUIREMENT
minvalue 1
maxvalue 999999999999999999999999999
start with 2807
increment by 1
cache 20;

prompt
prompt Creating sequence S_HR_TR_TRAINTEACHER
prompt ======================================
prompt
create sequence S_HR_TR_TRAINTEACHER
minvalue 1
maxvalue 999999999999999999999999999
start with 1253
increment by 1
cache 20;

prompt
prompt Creating sequence S_HR_TR_TRAPRO
prompt ================================
prompt
create sequence S_HR_TR_TRAPRO
minvalue 1
maxvalue 999999999999999999999999999
start with 414
increment by 1
cache 20;

prompt
prompt Creating sequence S_JFW_BASE_COMMUNICATE
prompt ========================================
prompt
create sequence S_JFW_BASE_COMMUNICATE
minvalue 1
maxvalue 999999999999999999999999999
start with 194
increment by 1
cache 20;

prompt
prompt Creating sequence S_JFW_BASE_CONTENT
prompt ====================================
prompt
create sequence S_JFW_BASE_CONTENT
minvalue 1
maxvalue 999999999999999999999999999
start with 41
increment by 1
cache 20;

prompt
prompt Creating sequence S_JFW_BASE_CUSTOMCONFIG
prompt =========================================
prompt
create sequence S_JFW_BASE_CUSTOMCONFIG
minvalue 1
maxvalue 999999999999999999999999999
start with 169
increment by 1
cache 20;

prompt
prompt Creating sequence S_JFW_BASE_CUSTOMFIELD
prompt ========================================
prompt
create sequence S_JFW_BASE_CUSTOMFIELD
minvalue 1
maxvalue 999999999999999999999999999
start with 329
increment by 1
cache 20;

prompt
prompt Creating sequence S_JFW_BASE_CUSTOMLINK
prompt =======================================
prompt
create sequence S_JFW_BASE_CUSTOMLINK
minvalue 1
maxvalue 999999999999999999999999999
start with 61
increment by 1
cache 20;

prompt
prompt Creating sequence S_JFW_BASE_CUSTOMORDER
prompt ========================================
prompt
create sequence S_JFW_BASE_CUSTOMORDER
minvalue 1
maxvalue 999999999999999999999999999
start with 21
increment by 1
cache 20;

prompt
prompt Creating sequence S_JFW_BASE_LINKDEFINE
prompt =======================================
prompt
create sequence S_JFW_BASE_LINKDEFINE
minvalue 1
maxvalue 999999999999999999999999999
start with 879
increment by 1
cache 20;

prompt
prompt Creating sequence S_JFW_BASE_NOTEPAD
prompt ====================================
prompt
create sequence S_JFW_BASE_NOTEPAD
minvalue 1
maxvalue 999999999999999999999999999
start with 21
increment by 1
cache 20;

prompt
prompt Creating sequence S_JFW_BASE_NOTION
prompt ===================================
prompt
create sequence S_JFW_BASE_NOTION
minvalue 1
maxvalue 999999999999999999999999999
start with 1964
increment by 1
cache 20;

prompt
prompt Creating sequence S_JFW_BASE_QSEARCH
prompt ====================================
prompt
create sequence S_JFW_BASE_QSEARCH
minvalue 1
maxvalue 999999999999999999999999999
start with 207
increment by 1
cache 20;

prompt
prompt Creating sequence S_JFW_BASE_SPECIALDAY
prompt =======================================
prompt
create sequence S_JFW_BASE_SPECIALDAY
minvalue 1
maxvalue 999999999999999999999999999
start with 8
increment by 1
cache 20;

prompt
prompt Creating sequence S_JFW_OWAY_ASSOCIATION
prompt ========================================
prompt
create sequence S_JFW_OWAY_ASSOCIATION
minvalue 1
maxvalue 999999999999999999999999999
start with 21
increment by 1
cache 20;

prompt
prompt Creating sequence S_JFW_OWAY_BEHAVIOR
prompt =====================================
prompt
create sequence S_JFW_OWAY_BEHAVIOR
minvalue 1
maxvalue 999999999999999999999999999
start with 41
increment by 1
cache 20;

prompt
prompt Creating sequence S_JFW_OWAY_CLOBBLOB
prompt =====================================
prompt
create sequence S_JFW_OWAY_CLOBBLOB
minvalue 1
maxvalue 999999999999999999999999999
start with 541
increment by 1
cache 20;

prompt
prompt Creating sequence S_JFW_OWAY_DATA
prompt =================================
prompt
create sequence S_JFW_OWAY_DATA
minvalue 1
maxvalue 999999999999999999999999999
start with 801
increment by 1
cache 20;

prompt
prompt Creating sequence S_JFW_OWAY_LOG
prompt ================================
prompt
create sequence S_JFW_OWAY_LOG
minvalue 1
maxvalue 999999999999999999999999999
start with 105
increment by 1
cache 20;

prompt
prompt Creating sequence S_JFW_OWAY_PACKAGE
prompt ====================================
prompt
create sequence S_JFW_OWAY_PACKAGE
minvalue 1
maxvalue 999999999999999999999999999
start with 41
increment by 1
cache 20;

prompt
prompt Creating sequence S_JFW_OWAY_PROCESS
prompt ====================================
prompt
create sequence S_JFW_OWAY_PROCESS
minvalue 1
maxvalue 999999999999999999999999999
start with 23
increment by 1
cache 20;

prompt
prompt Creating sequence S_JFW_SECURITY_ACTION
prompt =======================================
prompt
create sequence S_JFW_SECURITY_ACTION
minvalue 1
maxvalue 999999999999999999999999999
start with 4
increment by 1
cache 20;

prompt
prompt Creating sequence S_JFW_SECURITY_DATAMODEL
prompt ==========================================
prompt
create sequence S_JFW_SECURITY_DATAMODEL
minvalue 1
maxvalue 999999999999999999999999999
start with 147
increment by 1
cache 20;

prompt
prompt Creating sequence S_JFW_SECURITY_FIELD
prompt ======================================
prompt
create sequence S_JFW_SECURITY_FIELD
minvalue 1
maxvalue 999999999999999999999999999
start with 6
increment by 1
cache 20;

prompt
prompt Creating sequence S_JFW_SECURITY_IP
prompt ===================================
prompt
create sequence S_JFW_SECURITY_IP
minvalue 1
maxvalue 999999999999999999999999999
start with 4
increment by 1
cache 20;

prompt
prompt Creating sequence S_JFW_SECURITY_IP_POLICY
prompt ==========================================
prompt
create sequence S_JFW_SECURITY_IP_POLICY
minvalue 1
maxvalue 999999999999999999999999999
start with 2
increment by 1
cache 20;

prompt
prompt Creating sequence S_JFW_SECURITY_POLICY
prompt =======================================
prompt
create sequence S_JFW_SECURITY_POLICY
minvalue 1
maxvalue 999999999999999999999999999
start with 67
increment by 1
cache 20;

prompt
prompt Creating sequence S_JFW_SECURITY_UNITPOLICY
prompt ===========================================
prompt
create sequence S_JFW_SECURITY_UNITPOLICY
minvalue 1
maxvalue 999999999999999999999999999
start with 4
increment by 1
cache 20;

prompt
prompt Creating sequence S_PDM_BASE
prompt ============================
prompt
create sequence S_PDM_BASE
minvalue 1
maxvalue 999999999999999999999999999
start with 2
increment by 1
cache 20;

prompt
prompt Creating sequence S_PDM_DIR
prompt ===========================
prompt
create sequence S_PDM_DIR
minvalue 1
maxvalue 999999999999999999999999999
start with 81
increment by 1
cache 20;

prompt
prompt Creating sequence S_PDM_FILE
prompt ============================
prompt
create sequence S_PDM_FILE
minvalue 1
maxvalue 999999999999999999999999999
start with 10122
increment by 1
cache 20;

prompt
prompt Creating sequence S_PDM_RIGHT
prompt =============================
prompt
create sequence S_PDM_RIGHT
minvalue 1
maxvalue 999999999999999999999999999
start with 111
increment by 1
cache 20;

prompt
prompt Creating sequence S_PDM_VIRTUALROOT
prompt ===================================
prompt
create sequence S_PDM_VIRTUALROOT
minvalue 1
maxvalue 999999999999999999999999999
start with 2
increment by 1
cache 20;

prompt
prompt Creating sequence S_PROJECT_BASE_MODULE
prompt =======================================
prompt
create sequence S_PROJECT_BASE_MODULE
minvalue 1
maxvalue 999999999999999999999999999
start with 31
increment by 1
cache 20;

prompt
prompt Creating sequence S_PROJECT_BUG
prompt ===============================
prompt
create sequence S_PROJECT_BUG
minvalue 1
maxvalue 999999999999999999999999999
start with 66
increment by 1
cache 20;

prompt
prompt Creating sequence S_PROJECT_BUG_LOG
prompt ===================================
prompt
create sequence S_PROJECT_BUG_LOG
minvalue 1
maxvalue 999999999999999999999999999
start with 49
increment by 1
cache 20;

prompt
prompt Creating sequence S_SALARY_ACCOUNT_CUSTOMFORMULA
prompt ================================================
prompt
create sequence S_SALARY_ACCOUNT_CUSTOMFORMULA
minvalue 1
maxvalue 999999999999999999999999999
start with 127
increment by 1
cache 20;

prompt
prompt Creating sequence S_SALARY_ACCOUNT_FIELD
prompt ========================================
prompt
create sequence S_SALARY_ACCOUNT_FIELD
minvalue 1
maxvalue 999999999999999999999999999
start with 559
increment by 1
cache 20;

prompt
prompt Creating sequence S_SALARY_ACCOUNT_FORMULA
prompt ==========================================
prompt
create sequence S_SALARY_ACCOUNT_FORMULA
minvalue 1
maxvalue 999999999999999999999999999
start with 125
increment by 1
cache 20;

prompt
prompt Creating sequence S_SALARY_ACCOUNT_PERSON
prompt =========================================
prompt
create sequence S_SALARY_ACCOUNT_PERSON
minvalue 1
maxvalue 999999999999999999999999999
start with 10268
increment by 1
cache 20;

prompt
prompt Creating sequence S_SALARY_ACCOUNT_SET
prompt ======================================
prompt
create sequence S_SALARY_ACCOUNT_SET
minvalue 1
maxvalue 999999999999999999999999999
start with 83
increment by 1
cache 20;

prompt
prompt Creating sequence S_SALARY_CHECKRULE
prompt ====================================
prompt
create sequence S_SALARY_CHECKRULE
minvalue 1
maxvalue 999999999999999999999999999
start with 21
increment by 1
cache 20;

prompt
prompt Creating sequence S_SALARY_CONTROL_DEPT
prompt =======================================
prompt
create sequence S_SALARY_CONTROL_DEPT
minvalue 1
maxvalue 999999999999999999999999999
start with 1695
increment by 1
cache 20;

prompt
prompt Creating sequence S_SALARY_CONTROL_UNIT
prompt =======================================
prompt
create sequence S_SALARY_CONTROL_UNIT
minvalue 1
maxvalue 999999999999999999999999999
start with 109
increment by 1
cache 20;

prompt
prompt Creating sequence S_SALARY_DATA_CHECKRESULT
prompt ===========================================
prompt
create sequence S_SALARY_DATA_CHECKRESULT
minvalue 1
maxvalue 999999999999999999999999999
start with 41
increment by 1
cache 20;

prompt
prompt Creating sequence S_SALARY_DATA_PERIOD
prompt ======================================
prompt
create sequence S_SALARY_DATA_PERIOD
minvalue 1
maxvalue 999999999999999999999999999
start with 218
increment by 1
cache 20;

prompt
prompt Creating sequence S_SALARY_DATA_REVISONRECORD
prompt =============================================
prompt
create sequence S_SALARY_DATA_REVISONRECORD
minvalue 1
maxvalue 999999999999999999999999999
start with 489
increment by 1
cache 20;

prompt
prompt Creating sequence S_SALARY_EMPLOYEE_TOTAL
prompt =========================================
prompt
create sequence S_SALARY_EMPLOYEE_TOTAL
minvalue 1
maxvalue 999999999999999999999999999
start with 3622
increment by 1
cache 20;

prompt
prompt Creating sequence S_SALARY_PERSON_CHANGE
prompt ========================================
prompt
create sequence S_SALARY_PERSON_CHANGE
minvalue 1
maxvalue 999999999999999999999999999
start with 146
increment by 1
cache 20;

prompt
prompt Creating sequence S_SALARY_SYS_STANDARD
prompt =======================================
prompt
create sequence S_SALARY_SYS_STANDARD
minvalue 1
maxvalue 999999999999999999999999999
start with 6011
increment by 1
cache 20;

prompt
prompt Creating sequence S_SALARY_SYS_STANDARDTYPE
prompt ===========================================
prompt
create sequence S_SALARY_SYS_STANDARDTYPE
minvalue 1
maxvalue 999999999999999999999999999
start with 25
increment by 1
cache 20;

prompt
prompt Creating sequence S_SALARY_SYS_TAX
prompt ==================================
prompt
create sequence S_SALARY_SYS_TAX
minvalue 1
maxvalue 999999999999999999999999999
start with 22
increment by 1
cache 20;

prompt
prompt Creating sequence S_SALARY_SYS_TAX_TABLE
prompt ========================================
prompt
create sequence S_SALARY_SYS_TAX_TABLE
minvalue 1
maxvalue 999999999999999999999999999
start with 21
increment by 1
cache 20;

prompt
prompt Creating sequence S_SALARY_WELFARE_PERSON
prompt =========================================
prompt
create sequence S_SALARY_WELFARE_PERSON
minvalue 1
maxvalue 999999999999999999999999999
start with 7019
increment by 1
cache 20;

prompt
prompt Creating sequence S_SALARY_WELFARE_WAGE
prompt =======================================
prompt
create sequence S_SALARY_WELFARE_WAGE
minvalue 1
maxvalue 999999999999999999999999999
start with 223
increment by 1
cache 20;

prompt
prompt Creating sequence S_TEMP_HR_HI_POSINFO
prompt ======================================
prompt
create sequence S_TEMP_HR_HI_POSINFO
minvalue 1
maxvalue 999999999999999999999999999
start with 62
increment by 1
cache 20;

prompt
prompt Creating sequence S_TEMP_TASK
prompt =============================
prompt
create sequence S_TEMP_TASK
minvalue 1
maxvalue 999999999999999999999999999
start with 21
increment by 1
cache 20;

prompt
prompt Creating sequence S_TS_PJ_CUSTOMER
prompt ==================================
prompt
create sequence S_TS_PJ_CUSTOMER
minvalue 1
maxvalue 999999999999999999999999999
start with 21
increment by 1
cache 20;

prompt
prompt Creating sequence S_TS_PJ_PHASE
prompt ===============================
prompt
create sequence S_TS_PJ_PHASE
minvalue 1
maxvalue 999999999999999999999999999
start with 41
increment by 1
cache 20;

prompt
prompt Creating sequence S_TS_PJ_PROJECT
prompt =================================
prompt
create sequence S_TS_PJ_PROJECT
minvalue 1
maxvalue 999999999999999999999999999
start with 61
increment by 1
cache 20;

prompt
prompt Creating sequence S_TS_PJ_REPORTING
prompt ===================================
prompt
create sequence S_TS_PJ_REPORTING
minvalue 1
maxvalue 999999999999999999999999999
start with 81
increment by 1
cache 20;

prompt
prompt Creating sequence S_TS_PJ_TASKDETAIL
prompt ====================================
prompt
create sequence S_TS_PJ_TASKDETAIL
minvalue 1
maxvalue 999999999999999999999999999
start with 81
increment by 1
cache 20;

prompt
prompt Creating sequence S_TS_PJ_TASKPKG
prompt =================================
prompt
create sequence S_TS_PJ_TASKPKG
minvalue 1
maxvalue 999999999999999999999999999
start with 41
increment by 1
cache 20;

prompt
prompt Creating sequence S_TS_PJ_TASKRPT
prompt =================================
prompt
create sequence S_TS_PJ_TASKRPT
minvalue 1
maxvalue 999999999999999999999999999
start with 101
increment by 1
cache 20;

prompt
prompt Creating sequence S_TS_PJ_TEAMMEMBER
prompt ====================================
prompt
create sequence S_TS_PJ_TEAMMEMBER
minvalue 1
maxvalue 999999999999999999999999999
start with 41
increment by 1
cache 20;

prompt
prompt Creating sequence S_T_JFW_CODEMODELVERSION
prompt ==========================================
prompt
create sequence S_T_JFW_CODEMODELVERSION
minvalue 1
maxvalue 999999999999999999999999999
start with 42
increment by 1
cache 20;

prompt
prompt Creating sequence S_T_JFW_CODE_COMPLEX
prompt ======================================
prompt
create sequence S_T_JFW_CODE_COMPLEX
minvalue 1
maxvalue 999999999999999999999999999
start with 216
increment by 1
cache 20;

prompt
prompt Creating sequence S_T_JFW_CODE_DATA
prompt ===================================
prompt
create sequence S_T_JFW_CODE_DATA
minvalue 1
maxvalue 999999999999999999999999999
start with 6201
increment by 1
cache 20;

prompt
prompt Creating sequence S_T_JFW_FUNCTION
prompt ==================================
prompt
create sequence S_T_JFW_FUNCTION
minvalue 1
maxvalue 999999999999999999999999999
start with 8806
increment by 1
cache 20;

prompt
prompt Creating sequence S_T_JFW_FUNCTIONVERSION
prompt =========================================
prompt
create sequence S_T_JFW_FUNCTIONVERSION
minvalue 1
maxvalue 999999999999999999999999999
start with 22
increment by 1
cache 20;

prompt
prompt Creating sequence S_T_JFW_RULE_PARAM
prompt ====================================
prompt
create sequence S_T_JFW_RULE_PARAM
minvalue 1
maxvalue 999999999999999999999999999
start with 2861
increment by 1
cache 20;

prompt
prompt Creating sequence S_T_JFW_RULE_SET_RULE
prompt =======================================
prompt
create sequence S_T_JFW_RULE_SET_RULE
minvalue 1
maxvalue 999999999999999999999999999
start with 81
increment by 1
cache 20;

prompt
prompt Creating sequence S_T_SALARY_EMPLOYEE_WELFARE
prompt =============================================
prompt
create sequence S_T_SALARY_EMPLOYEE_WELFARE
minvalue 1
maxvalue 999999999999999999999999999
start with 83101
increment by 1
cache 20;

prompt
prompt Creating sequence S_T_SALARY_WELFARE_FORMULA
prompt ============================================
prompt
create sequence S_T_SALARY_WELFARE_FORMULA
minvalue 1
maxvalue 999999999999999999999999999
start with 263
increment by 1
cache 20;

prompt
prompt Creating sequence S_T_SVG_CODE_MANAGER
prompt ======================================
prompt
create sequence S_T_SVG_CODE_MANAGER
minvalue 1
maxvalue 999999999999999999999999999
start with 181
increment by 1
cache 20;

prompt
prompt Creating sequence S_T_SVG_MODEL_BUSINESSRELA
prompt ============================================
prompt
create sequence S_T_SVG_MODEL_BUSINESSRELA
minvalue 1
maxvalue 999999999999999999999999999
start with 21
increment by 1
cache 20;

prompt
prompt Creating sequence S_T_SVG_MODULE_LIST
prompt =====================================
prompt
create sequence S_T_SVG_MODULE_LIST
minvalue 1
maxvalue 999999999999999999999999999
start with 41
increment by 1
cache 20;

prompt
prompt Creating sequence S_T_TEMP_RULE_HR
prompt ==================================
prompt
create sequence S_T_TEMP_RULE_HR
minvalue 1
maxvalue 999999999999999999999999999
start with 161
increment by 1
cache 20;

prompt
prompt Creating sequence S_T_TEMP_RULE_HR_ABS_ABILITY
prompt ==============================================
prompt
create sequence S_T_TEMP_RULE_HR_ABS_ABILITY
minvalue 1
maxvalue 999999999999999999999999999
start with 21
increment by 1
cache 20;

prompt
prompt Creating sequence S_UUM3_CONTROL_ROLE_USER
prompt ==========================================
prompt
create sequence S_UUM3_CONTROL_ROLE_USER
minvalue 20
maxvalue 999999999999999999999999999
start with 23830
increment by 1
cache 20;

prompt
prompt Creating sequence S_UUM3_LOG
prompt ============================
prompt
create sequence S_UUM3_LOG
minvalue 20
maxvalue 999999999999999999999999999
start with 261
increment by 1
cache 20;

prompt
prompt Creating sequence S_UUM_APPLICATION
prompt ===================================
prompt
create sequence S_UUM_APPLICATION
minvalue 1
maxvalue 999999999999999999999999999
start with 41
increment by 1
cache 20;

prompt
prompt Creating sequence S_UUM_AUTHENTICATION_INFO
prompt ===========================================
prompt
create sequence S_UUM_AUTHENTICATION_INFO
minvalue 1
maxvalue 999999999999999999999999999
start with 21
increment by 1
cache 20;

prompt
prompt Creating sequence S_UUM_BUSINESSORG
prompt ===================================
prompt
create sequence S_UUM_BUSINESSORG
minvalue 1
maxvalue 999999999999999999999999999
start with 21
increment by 1
cache 20;

prompt
prompt Creating sequence S_UUM_CONTROL_DOMAIN
prompt ======================================
prompt
create sequence S_UUM_CONTROL_DOMAIN
minvalue 1
maxvalue 999999999999999999999999999
start with 61
increment by 1
cache 20;

prompt
prompt Creating sequence S_UUM_CONTROL_ROLE_USER
prompt =========================================
prompt
create sequence S_UUM_CONTROL_ROLE_USER
minvalue 20
maxvalue 999999999999999999999999999
start with 23990
increment by 1
cache 20;

prompt
prompt Creating sequence S_UUM_DOMAIN_ROLE_MAP
prompt =======================================
prompt
create sequence S_UUM_DOMAIN_ROLE_MAP
minvalue 1
maxvalue 999999999999999999999999999
start with 41
increment by 1
cache 20;

prompt
prompt Creating sequence S_UUM_GRADE_ROLE
prompt ==================================
prompt
create sequence S_UUM_GRADE_ROLE
minvalue 1
maxvalue 999999999999999999999999999
start with 81
increment by 1
cache 20;

prompt
prompt Creating sequence S_UUM_LOG
prompt ===========================
prompt
create sequence S_UUM_LOG
minvalue 1
maxvalue 999999999999999999999999999
start with 21
increment by 1
cache 20;

prompt
prompt Creating sequence S_UUM_LOGIN_LOG
prompt =================================
prompt
create sequence S_UUM_LOGIN_LOG
minvalue 1
maxvalue 999999999999999999999999999
start with 21
increment by 1
cache 20;

prompt
prompt Creating sequence S_UUM_LOG_RECORK
prompt ==================================
prompt
create sequence S_UUM_LOG_RECORK
minvalue 1
maxvalue 999999999999999999999999999
start with 14580
increment by 1
cache 20;

prompt
prompt Creating sequence S_UUM_MENU
prompt ============================
prompt
create sequence S_UUM_MENU
minvalue 1
maxvalue 999999999999999999999999999
start with 1060
increment by 1
cache 20;

prompt
prompt Creating sequence S_UUM_ORG
prompt ===========================
prompt
create sequence S_UUM_ORG
minvalue 1
maxvalue 999999999999999999999999999
start with 22
increment by 1
nocache;

prompt
prompt Creating sequence S_UUM_ORG_BASE
prompt ================================
prompt
create sequence S_UUM_ORG_BASE
minvalue 1
maxvalue 999999999999999999999999999
start with 81
increment by 1
cache 20;

prompt
prompt Creating sequence S_UUM_RIGHT
prompt =============================
prompt
create sequence S_UUM_RIGHT
minvalue 1
maxvalue 999999999999999999999999999
start with 260
increment by 200
nocache;

prompt
prompt Creating sequence S_UUM_ROLE
prompt ============================
prompt
create sequence S_UUM_ROLE
minvalue 1
maxvalue 999999999999999999999999999
start with 41
increment by 1
cache 20;

prompt
prompt Creating sequence S_UUM_ROLE_DOMAIN_MAP
prompt =======================================
prompt
create sequence S_UUM_ROLE_DOMAIN_MAP
minvalue 1
maxvalue 999999999999999999999999999
start with 81
increment by 1
cache 20;

prompt
prompt Creating sequence S_UUM_R_BUSINESSORG_ORG
prompt =========================================
prompt
create sequence S_UUM_R_BUSINESSORG_ORG
minvalue 1
maxvalue 999999999999999999999999999
start with 21
increment by 1
cache 20;

prompt
prompt Creating sequence S_UUM_R_ROLE_APP
prompt ==================================
prompt
create sequence S_UUM_R_ROLE_APP
minvalue 1
maxvalue 999999999999999999999999999
start with 21
increment by 1
cache 20;

prompt
prompt Creating sequence S_UUM_R_ROLE_RIGHT
prompt ====================================
prompt
create sequence S_UUM_R_ROLE_RIGHT
minvalue 1
maxvalue 999999999999999999999999999
start with 25000
increment by 1
cache 20;

prompt
prompt Creating sequence S_UUM_R_ROLE_SUB_RIGHT
prompt ========================================
prompt
create sequence S_UUM_R_ROLE_SUB_RIGHT
minvalue 1
maxvalue 999999999999999999999999999
start with 21
increment by 1
cache 20;

prompt
prompt Creating sequence S_UUM_R_USER_ROLE
prompt ===================================
prompt
create sequence S_UUM_R_USER_ROLE
minvalue 1
maxvalue 999999999999999999999999999
start with 45280
increment by 1
cache 20;

prompt
prompt Creating sequence S_UUM_SUB_RIGHT
prompt =================================
prompt
create sequence S_UUM_SUB_RIGHT
minvalue 1
maxvalue 999999999999999999999999999
start with 21
increment by 1
cache 20;

prompt
prompt Creating sequence S_UUM_USER
prompt ============================
prompt
create sequence S_UUM_USER
minvalue 1
maxvalue 999999999999999999999999999
start with 41
increment by 1
cache 20;

prompt
prompt Creating sequence S_WORKFLOW_ACTIVITYINSTANCE
prompt =============================================
prompt
create sequence S_WORKFLOW_ACTIVITYINSTANCE
minvalue 1
maxvalue 999999999999999999999999999
start with 14256
increment by 1
cache 20;

prompt
prompt Creating sequence S_WORKFLOW_PROCESSINSTANCE
prompt ============================================
prompt
create sequence S_WORKFLOW_PROCESSINSTANCE
minvalue 1
maxvalue 999999999999999999999999999
start with 9860
increment by 1
cache 20;

prompt
prompt Creating synonym T_JFW_EPMS2_DATAMODEL
prompt ======================================
prompt
create or replace synonym T_JFW_EPMS2_DATAMODEL
  for EPMS2.T_JFW_DATAMODEL;

prompt
prompt Creating synonym T_JFW_FIELDINFO1
prompt =================================
prompt
create or replace synonym T_JFW_FIELDINFO1
  for DOMINO.T_JFW_FIELDINFO;

prompt
prompt Creating view T_SVG_BUSINESS_CODE
prompt =================================
prompt
create or replace view t_svg_business_code as
select b.cc_name,a.rm_table_relate
from T_SVG_REP_MANAGER a , t_svg_code_commsets b
where a.rm_businesssys_id = b.cc_code and b.cc_shortname='SVG_BUSINESS_ID'
/

prompt
prompt Creating view T_SVG_CODE_COMMSETS_KIND
prompt ======================================
prompt
create or replace view t_svg_code_commsets_kind as
select "CC_SHORTNAME","CC_CODE","CC_NAME","CC_SPELL","CC_DESC","CC_PARENT_CODE","CC_PARAM1","CC_PARAM2","CC_PARAM3","CC_PARAM4","CC_PARAM5","CC_ID" from T_SVG_CODE_COMMSETS where CC_SHORTNAME = 'SVG_MODULE_KIND'
/

prompt
prompt Creating view T_SVG_CODE_COMMSETS_LOCATION
prompt ==========================================
prompt
create or replace view t_svg_code_commsets_location as
select "CC_SHORTNAME","CC_CODE","CC_NAME","CC_SPELL","CC_DESC","CC_PARENT_CODE","CC_PARAM1","CC_PARAM2","CC_PARAM3","CC_PARAM4","CC_PARAM5","CC_ID" from T_SVG_CODE_COMMSETS where CC_SHORTNAME = 'SVG_MODULE_LOCATION'
/

prompt
prompt Creating view T_SVG_MODULE_LIST_VIEW
prompt ====================================
prompt
create or replace view t_svg_module_list_view as
select k.cc_shortname,k.cc_code,k.cc_name, '-1' as parentid from T_SVG_CODE_COMMSETS k
where k.cc_shortname='SVG_MODULE_LOCATION'
union all
select l.ml_shortname,l.ml_name,l.ml_name,l.ml_location as parentid
from T_SVG_MODULE_LIST l
/

prompt
prompt Creating view T_SVG_REP_GRAPHALL
prompt ================================
prompt
create or replace view t_svg_rep_graphall as
select 'psms' as flag,T_SVG_REP_GRAPH_psms.* from T_SVG_REP_GRAPH_psms
/

prompt
prompt Creating view T_SVG_REP_GRAPHLIST
prompt =================================
prompt
create or replace view t_svg_rep_graphlist as
select 'nest' as flag,T_SVG_REP_GRAPH_nest.* from T_SVG_REP_GRAPH_nest union all select 'psms' as flag,T_SVG_REP_GRAPH_psms.* from T_SVG_REP_GRAPH_psms union all select 'plan' as flag,T_SVG_REP_GRAPH_plan.* from T_SVG_REP_GRAPH_plan
/

prompt
prompt Creating view T_SVG_REP_GRAPH_ALL
prompt =================================
prompt
create or replace view t_svg_rep_graph_all as
select 'nest' as flag,T_SVG_REP_GRAPH_nest.* from T_SVG_REP_GRAPH_nest union all select 'psms' as flag,T_SVG_REP_GRAPH_psms.* from T_SVG_REP_GRAPH_psms union all select 'plan' as flag,T_SVG_REP_GRAPH_plan.* from T_SVG_REP_GRAPH_plan
/

prompt
prompt Creating view T_SVG_REP_VERSET_ALL
prompt ==================================
prompt
create or replace view t_svg_rep_verset_all as
select 'nest' as flag,T_SVG_REP_VERSET_nest.* from T_SVG_REP_VERSET_nest union all select 'psms' as flag,T_SVG_REP_VERSET_psms.* from T_SVG_REP_VERSET_psms union all select 'plan' as flag,T_SVG_REP_VERSET_plan.* from T_SVG_REP_VERSET_plan
/

prompt
prompt Creating view T_SVG_REP_GRAPH_BB
prompt ================================
prompt
create or replace view t_svg_rep_graph_bb as
select a.* from t_svg_rep_graphlist a ,t_svg_rep_verset_all b
where a.rg_graph_type='1'  and a.flag=b.flag
and a.rg_graph_busi_type=b.rv_graph_business_type and (b.rv_store_policy='2'
or b.rv_store_policy='1')
/

prompt
prompt Creating view T_SVG_REP_GRAPH_GXT
prompt =================================
prompt
create or replace view t_svg_rep_graph_gxt as
select a.* from t_svg_rep_graphlist a where a.rg_graph_type='1'
/

prompt
prompt Creating view T_SVG_REP_GRAPH_PARSON
prompt ====================================
prompt
create or replace view t_svg_rep_graph_parson as
select 'nest' as flag,'-1'as parentid,'nest' as codename,'nest' as codeid from dual union all select 'nest' as flag ,'nest' as parentid,'一次接线图' as codename,'nest70' as codeid from dual union all select 'nest' as flag ,'nest' as parentid,'系统图' as codename,'nest71' as codeid from dual union all select 'nest' as flag ,'nest' as parentid,'单线图' as codename,'nest72' as codeid from dual union all select 'nest' as flag ,'nest' as parentid,'测试图' as codename,'nestCA01F25FADE032EE99C48D197F73B1B7' as codeid from dual union all select 'nest' as flag ,'nest' as parentid,'sss' as codename,'nestCD522CDE65AC397F9142B498B9FCF190' as codeid from dual union all select 'nest' as flag ,'nest' as parentid,'rrr6' as codename,'nest9AD9392E771B62F9BD95199E97471320' as codeid from dual union all select 'nest' as flag ,'nest' as parentid,'台区图' as codename,'nestCB471ADEDE0E572C629A6BC36131B54A' as codeid from dual union all select 'psms' as flag,'-1'as parentid,'psms' as codename,'psms' as codeid from dual union all select 'psms' as flag ,'psms' as parentid,'一次接线图' as codename,'psms70' as codeid from dual union all  select 'psms' as flag,'psms70' as parentid ,'周慧明测试3' as  codename, 'fad09284-b0cf-4661-8f45-28f913b42a08' as codeid from dual union all  select 'psms' as flag,'psms70' as parentid ,'周慧明测试2' as  codename, 'ec121266-bf0e-4e26-9925-2ede81d07af7' as codeid from dual union all select 'psms' as flag ,'psms' as parentid,'系统图' as codename,'psms71' as codeid from dual union all select 'psms' as flag ,'psms' as parentid,'单线图' as codename,'psms72' as codeid from dual union all  select 'psms' as flag,'psms72' as parentid ,'无标题' as  codename, '0ee53837-3c84-4828-88ab-8b847b19ace2' as codeid from dual union all  select 'psms' as flag,'psms72' as parentid ,'单线图1' as  codename, '9c274e68-47e8-4b7d-ab19-a63b116a58b3' as codeid from dual union all  select 'psms' as flag,'psms72' as parentid ,'单线图海宁' as  codename, '9ebe4d34-ddcf-41f1-8b4b-f6e936009d46' as codeid from dual union all  select 'psms' as flag,'psms72' as parentid ,'无标题243' as  codename, 'ae401b0d-12f3-47cb-bd05-ee7288ef6fce' as codeid from dual union all select 'psms' as flag ,'psms' as parentid,'测试图' as codename,'psmsCA01F25FADE032EE99C48D197F73B1B7' as codeid from dual union all select 'psms' as flag ,'psms' as parentid,'sss' as codename,'psmsCD522CDE65AC397F9142B498B9FCF190' as codeid from dual union all select 'psms' as flag ,'psms' as parentid,'rrr6' as codename,'psms9AD9392E771B62F9BD95199E97471320' as codeid from dual union all select 'psms' as flag ,'psms' as parentid,'台区图' as codename,'psmsCB471ADEDE0E572C629A6BC36131B54A' as codeid from dual union all select 'plan' as flag,'-1'as parentid,'plan' as codename,'plan' as codeid from dual union all select 'plan' as flag ,'plan' as parentid,'一次接线图' as codename,'plan70' as codeid from dual union all select 'plan' as flag ,'plan' as parentid,'系统图' as codename,'plan71' as codeid from dual union all select 'plan' as flag ,'plan' as parentid,'单线图' as codename,'plan72' as codeid from dual union all select 'plan' as flag ,'plan' as parentid,'测试图' as codename,'planCA01F25FADE032EE99C48D197F73B1B7' as codeid from dual union all select 'plan' as flag ,'plan' as parentid,'sss' as codename,'planCD522CDE65AC397F9142B498B9FCF190' as codeid from dual union all select 'plan' as flag ,'plan' as parentid,'rrr6' as codename,'plan9AD9392E771B62F9BD95199E97471320' as codeid from dual union all select 'plan' as flag ,'plan' as parentid,'台区图' as codename,'planCB471ADEDE0E572C629A6BC36131B54A' as codeid from dual
/

prompt
prompt Creating view T_SVG_REP_GRAPH_PARSON_ALL
prompt ========================================
prompt
create or replace view t_svg_rep_graph_parson_all as
select 'nest' as flag,'-1'as parentid,'nest' as codename,'nest' as codeid , '0' as code from dual union all select 'nest' as flag ,'nest' as parentid,'一次接线图' as codename,'nest70' as codeid ,'1' as code from dual union all select 'nest' as flag ,'nest' as parentid,'系统图' as codename,'nest71' as codeid ,'2' as code from dual union all select 'nest' as flag ,'nest' as parentid,'单线图' as codename,'nest72' as codeid ,'3' as code from dual union all select 'nest' as flag ,'nest' as parentid,'测试图' as codename,'nestCA01F25FADE032EE99C48D197F73B1B7' as codeid ,'4' as code from dual union all select 'nest' as flag ,'nest' as parentid,'sss' as codename,'nestCD522CDE65AC397F9142B498B9FCF190' as codeid ,'5' as code from dual union all select 'nest' as flag ,'nest' as parentid,'rrr6' as codename,'nest9AD9392E771B62F9BD95199E97471320' as codeid ,'6' as code from dual union all select 'nest' as flag ,'nest' as parentid,'台区图' as codename,'nestCB471ADEDE0E572C629A6BC36131B54A' as codeid ,'district' as code from dual union all select 'psms' as flag,'-1'as parentid,'psms' as codename,'psms' as codeid , '0' as code from dual union all select 'psms' as flag ,'psms' as parentid,'一次接线图' as codename,'psms70' as codeid ,'1' as code from dual union all select 'psms' as flag ,'psms' as parentid,'系统图' as codename,'psms71' as codeid ,'2' as code from dual union all select 'psms' as flag ,'psms' as parentid,'单线图' as codename,'psms72' as codeid ,'3' as code from dual union all select 'psms' as flag ,'psms' as parentid,'测试图' as codename,'psmsCA01F25FADE032EE99C48D197F73B1B7' as codeid ,'4' as code from dual union all select 'psms' as flag ,'psms' as parentid,'sss' as codename,'psmsCD522CDE65AC397F9142B498B9FCF190' as codeid ,'5' as code from dual union all select 'psms' as flag ,'psms' as parentid,'rrr6' as codename,'psms9AD9392E771B62F9BD95199E97471320' as codeid ,'6' as code from dual union all select 'psms' as flag ,'psms' as parentid,'台区图' as codename,'psmsCB471ADEDE0E572C629A6BC36131B54A' as codeid ,'district' as code from dual union all select 'plan' as flag,'-1'as parentid,'plan' as codename,'plan' as codeid , '0' as code from dual union all select 'plan' as flag ,'plan' as parentid,'一次接线图' as codename,'plan70' as codeid ,'1' as code from dual union all select 'plan' as flag ,'plan' as parentid,'系统图' as codename,'plan71' as codeid ,'2' as code from dual union all select 'plan' as flag ,'plan' as parentid,'单线图' as codename,'plan72' as codeid ,'3' as code from dual union all select 'plan' as flag ,'plan' as parentid,'测试图' as codename,'planCA01F25FADE032EE99C48D197F73B1B7' as codeid ,'4' as code from dual union all select 'plan' as flag ,'plan' as parentid,'sss' as codename,'planCD522CDE65AC397F9142B498B9FCF190' as codeid ,'5' as code from dual union all select 'plan' as flag ,'plan' as parentid,'rrr6' as codename,'plan9AD9392E771B62F9BD95199E97471320' as codeid ,'6' as code from dual union all select 'plan' as flag ,'plan' as parentid,'台区图' as codename,'planCB471ADEDE0E572C629A6BC36131B54A' as codeid ,'district' as code from dual
/

prompt
prompt Creating view T_SVG_REP_GRAPH_ZL
prompt ================================
prompt
create or replace view t_svg_rep_graph_zl as
select a.* from t_svg_rep_graphlist a ,t_svg_rep_verset_all b
where a.rg_graph_type='1' and a.rg_graph_file_type='svg' and a.flag=b.flag
and a.rg_graph_busi_type=b.rv_graph_business_type and b.rv_store_policy='2'
/

prompt
prompt Creating view T_SVG_REP_VERLOG_ALL
prompt ==================================
prompt
create or replace view t_svg_rep_verlog_all as
select 'nest' as flag,T_SVG_REP_VERLOG_nest.* from T_SVG_REP_VERLOG_nest union all select 'psms' as flag,T_SVG_REP_VERLOG_psms.* from T_SVG_REP_VERLOG_psms union all select 'plan' as flag,T_SVG_REP_VERLOG_plan.* from T_SVG_REP_VERLOG_plan
/

prompt
prompt Creating view T_VIEW_SVG_BUSINESS_MODEL
prompt =======================================
prompt
create or replace view t_view_svg_business_model as
select a.cc_code as flag, '0' as parentid, a.cc_code as codevalue,a.cc_name as codename
from t_svg_code_commsets a
where a.cc_shortname = 'SVG_BUSINESS_ID'
union all
select b.business_id as flag, b.business_id as parentid,b.modle_id as codevalue,b.module_name as codename
from T_SVG_BUSINESS_MODEL b
union all
select b.business_id as flag, b.modle_id  as parentid, b.BM_ID as codevalue, b.im_modle_attrname as codename
from T_SVG_BUSINESS_MODEL b
/

prompt
prompt Creating view T_VIEW_SVG_CODE_COMMSETS
prompt ======================================
prompt
create or replace view t_view_svg_code_commsets as
select a.cc_shortname||a.cc_code as prikey,a.* from t_svg_code_commsets a
/

prompt
prompt Creating view T_VIEW_SVG_DATAMODEL
prompt ==================================
prompt
CREATE OR REPLACE VIEW T_VIEW_SVG_DATAMODEL AS
(
SELECT a.modelname, a.description, a.parent_cls_uri, a.propvaluetable, a.createdate, a.updatedate, a.cls_uri, a.pkg_uri, a.view_sql, a.behavior FROM t_jfw_datamodel a WHERE a.pkg_uri ='OWPACKAGE://SVG/Consume'
)
/

prompt
prompt Creating view T_VIEW_SVG_DATAMODEL_BEHAVIOR
prompt ===========================================
prompt
CREATE OR REPLACE VIEW T_VIEW_SVG_DATAMODEL_BEHAVIOR AS
(
SELECT a.seqkey, a.url, a.name, a.description, a.group_name, a.clazz as type  FROM jfw_oway_behavior a
)
/

prompt
prompt Creating view T_VIEW_SVG_DATAMODEL_FIELD
prompt ========================================
prompt
CREATE OR REPLACE VIEW T_VIEW_SVG_DATAMODEL_FIELD AS
(
SELECT a.modelname, a.fieldname, ac.cat_id AS cat_id, ac.displayname AS cat_name, a.displayname, a.map_propname, a.createdate, a.updatedate, a.cls_uri
       FROM t_jfw_fieldinfo a, jfw_oway_cat ac
       WHERE a.cls_uri = ac.cls_uri AND a.cat_id = ac.cat_id AND a.modelname in (SELECT modelname FROM t_view_svg_datamodel)
)
/

prompt
prompt Creating view T_VIEW_SVG_DATA_FIELDS
prompt ====================================
prompt
create or replace view t_view_svg_data_fields as
select a.*,b.im_name,b.im_variety,c.it_name
from t_svg_indunorm_fields a,t_svg_indunorm_metadatas b,t_svg_indunorm_type c
where a.if_variety=b.im_id
  and b.im_variety=c.it_shortname
/

prompt
prompt Creating view T_VIEW_SVG_INDUNORM_CODETYPE
prompt ==========================================
prompt
create or replace view t_view_svg_indunorm_codetype as
select '1' as CODEVALUE,'0' as IM_NORM_TYPE, '0' as SOURCE_ID, '描述信息' as CODENAME
from dual
union all
select '2' as CODEVALUE,'0' as IM_NORM_TYPE, '0' as SOURCE_ID, '数据域信息' as CODENAME
from dual
union all
select a.Id_Id as CODEVALUE,'1' as IM_NORM_TYPE, a.id_id as SOURCE_ID, a.id_name as CODENAME
from t_svg_indunorm_desc a
union all
select b.im_id as CODEVALUE,'2' as IM_NORM_TYPE, b.im_id as SOURCE_ID, b.im_name as CODENAME
from t_svg_indunorm_metadatas b
/

prompt
prompt Creating view T_VIEW_SVG_SYMBOLTEMPLATE
prompt =======================================
prompt
create or replace view t_view_svg_symboltemplate as
select 'SVG_GRAPHUNIT_VARIETY' as FLAG,'图元' as OBJECTNAME,
       '0' as PARENTID, 'SVG_GRAPHUNIT_VARIETY' as CODE
       from dual
union all
select 'SVG_TEMPLATE_VARIETY' as flag, '模板' as OBJECTNAME,
       '0' as PARENTID, 'SVG_TEMPLATE_VARIETY' as code
       from dual
union all
select 'SVG_GRAPHUNIT_VARIETY' as FLAG, b.cc_name as OBJECTNAME,
       b.cc_shortname as PARENTID, b.cc_code||b.cc_shortname as code
       from t_svg_code_commsets b
       where cc_shortname = 'SVG_GRAPHUNIT_VARIETY'
union all
select 'SVG_GRAPHUNIT_VARIETY' as FLAG, a.sf_name as OBJECTNAME,
       b.cc_code||'SVG_GRAPHUNIT_VARIETY' as PARENTID,
       a.sf_id as CODE
       from t_svg_symbol_fileinfo a, t_svg_code_commsets b
       where b.cc_shortname='SVG_GRAPHUNIT_VARIETY' and a.sf_variety=b.cc_code
union all
select 'SVG_TEMPLATE_VARIETY' as FLAG, b.cc_name as OBJECTNAME,
       b.cc_shortname as PARENTID, b.cc_code||b.cc_shortname as code
       from t_svg_code_commsets b
       where b.cc_shortname = 'SVG_TEMPLATE_VARIETY'
union all
select 'SVG_TEMPLATE_VARIETY' as FLAG ,c.st_name as OBJECTNAME,
       b.cc_code||'SVG_TEMPLATE_VARIETY' as PARENTID,
       c.st_id as CODE
       from t_svg_symbol_template c, t_svg_code_commsets b
       where b.cc_shortname='SVG_TEMPLATE_VARIETY' and c.st_variety = b.cc_code
/

prompt
prompt Creating view V$_APP_PLAN
prompt =========================
prompt
create or replace view v$_app_plan as
select OBJ_ID "OBJ_ID",CLS_URI "CLS_URI",S_04 "USER_UNITID",S_05 "PROCESS_STATUS",S_06 "PLAN_NO",S_07 "TITLE",S_08 "PLANTYPE",N_01 "STATUS",S_10 "PERIODTYPE",S_11 "ORGCODE",S_12 "PSNNUM",S_13 "NAME",D_03 "BEGINDATE",D_04 "ENDDATE",S_14 "DESCRIPTION",S_15 "ASSESS_RESULT",S_16 "SUMMARY",S_17 "AUDITOR",D_05 "AUDITDATE",S_19 "MAKEACOPYFOR",S_20 "PERIODNAME",N_02 "PERIODYEAR",N_03 "PERIODQUARTOR",N_04 "PERIODMONTH",S_01 "CREATEID",D_01 "CREATEDATE",S_02 "UPDATEID",D_02 "UPDATEDATE",S_03 "USER_ORGID",D_06 "PERIODDAY",N_05 "PERIODWEEK",S_21 "LASTPLANID",S_22 "NEXTPLANID" from JFW_OWAY_DATA where CLS_URI='OWCLASS://obj_base/APP_PLAN' or CLS_URI like 'OWCLASS://obj_base/APP_PLAN/%'
/

prompt
prompt Creating view V$_APP_PLAN_ITEM
prompt ==============================
prompt
create or replace view v$_app_plan_item as
select OBJ_ID "OBJ_ID",CLS_URI "CLS_URI",S_17 "PERIODTYPE",S_18 "PERIODYEAR",S_19 "PERIODMONTH",S_20 "PERIODQUARTOR",S_21 "PERIODWEEK",D_07 "PERIODDAY",N_10 "WINDAGE",S_25 "WORKID",S_01 "CREATEID",D_01 "CREATEDATE",S_02 "UPDATEID",D_02 "UPDATEDATE",S_03 "USER_ORGID",S_04 "USER_UNITID",S_05 "PROCESS_STATUS",S_06 "ITEMTYPE",S_07 "PROJECTID",S_09 "TASKID",S_10 "TASKNAME",N_01 "PLAN_HOURS",D_03 "PLAN_FINISHDATE",S_11 "PRODUCTION",N_02 "PRODUCTION_NUM",N_03 "REAL_HOURS",N_04 "SUM_REAL_HOURS",N_05 "STATUS",N_06 "REMAIN_HOURS",D_04 "REAL_BEGINDATE",D_05 "REAL_ENDDATE",D_06 "INTEND_ENDDATE",S_12 "PLANID",N_07 "ISATTENTION",N_08 "PRI",S_22 "PSNNUM",S_23 "NAME",S_15 "DESCRIPTION",S_24 "ORGCODE",S_14 "PROJECTNAME",S_16 "FINISHSTATUS",N_09 "SYSFLAG" from JFW_OWAY_DATA where CLS_URI='OWCLASS://obj_base/APP_PLAN_ITEM' or CLS_URI like 'OWCLASS://obj_base/APP_PLAN_ITEM/%'
/

prompt
prompt Creating view V$_APP_PLAN_ORG
prompt =============================
prompt
create or replace view v$_app_plan_org as
select OBJ_ID "OBJ_ID",CLS_URI "CLS_URI",S_04 "USER_UNITID",S_05 "PROCESS_STATUS",S_06 "PLAN_NO",S_07 "TITLE",S_08 "PLANTYPE",N_01 "STATUS",S_09 "PERIOD",S_10 "PERIODTYPE",S_11 "ORGCODE",S_12 "PSNNUM",S_13 "NAME",D_03 "BEGINDATE",D_04 "ENDDATE",S_14 "DESCRIPTION",S_15 "ASSESS_RESULT",S_16 "SUMMARY",S_17 "AUDITOR",D_05 "AUDITDATE",S_01 "CREATEID",D_01 "CREATEDATE",S_02 "UPDATEID",D_02 "UPDATEDATE",S_03 "USER_ORGID" from JFW_OWAY_DATA where CLS_URI='OWCLASS://obj_base/APP_PLAN/APP_PLAN_ORG' or CLS_URI like 'OWCLASS://obj_base/APP_PLAN/APP_PLAN_ORG/%'
/

prompt
prompt Creating view V$_APP_PLAN_PERSONAL
prompt ==================================
prompt
create or replace view v$_app_plan_personal as
select OBJ_ID "OBJ_ID",CLS_URI "CLS_URI",S_04 "USER_UNITID",S_05 "PROCESS_STATUS",S_06 "PLAN_NO",S_07 "TITLE",S_08 "PLANTYPE",N_01 "STATUS",S_09 "PERIOD",S_10 "PERIODTYPE",S_11 "ORGCODE",S_12 "PSNNUM",S_13 "NAME",D_03 "BEGINDATE",D_04 "ENDDATE",S_14 "DESCRIPTION",S_15 "ASSESS_RESULT",S_16 "SUMMARY",S_17 "AUDITOR",D_05 "AUDITDATE",S_01 "CREATEID",D_01 "CREATEDATE",S_02 "UPDATEID",D_02 "UPDATEDATE",S_03 "USER_ORGID" from JFW_OWAY_DATA where CLS_URI='OWCLASS://obj_base/APP_PLAN/APP_PLAN_PERSONAL' or CLS_URI like 'OWCLASS://obj_base/APP_PLAN/APP_PLAN_PERSONAL/%'
/

prompt
prompt Creating view V$_APP_PLAN_QUESTION
prompt ==================================
prompt
create or replace view v$_app_plan_question as
select OBJ_ID "OBJ_ID",CLS_URI "CLS_URI",S_01 "CREATEID",D_01 "CREATEDATE",S_02 "UPDATEID",D_02 "UPDATEDATE",S_03 "USER_ORGID",S_04 "USER_UNITID",S_05 "PROCESS_STATUS",S_06 "PLANID",S_07 "PROJECTID",S_08 "PROJECTNAME",S_09 "QUESTION",S_10 "COUNTERMEASURE",N_01 "STATUS",N_02 "NEEDHELP",S_11 "CHANNELID",S_12 "TOPICID",N_04 "TO_KM" from JFW_OWAY_DATA where CLS_URI='OWCLASS://obj_base/APP_PLAN_QUESTION' or CLS_URI like 'OWCLASS://obj_base/APP_PLAN_QUESTION/%'
/

prompt
prompt Creating view V$_APP_REPORT_BASE
prompt ================================
prompt
create or replace view v$_app_report_base as
select OBJ_ID "OBJ_ID",CLS_URI "CLS_URI",S_01 "CREATEID",D_01 "CREATEDATE",S_02 "UPDATEID",D_02 "UPDATEDATE",S_03 "USER_ORGID",S_04 "USER_UNITID",S_05 "PROCESS_STATUS",S_06 "REPORT_ID",N_01 "ROW_NO",S_11 "REPORT_INSTANCEID",S_16 "ROW_NAME",N_14 "COLLECT_ROWNO" from JFW_OWAY_DATA where CLS_URI='OWCLASS://obj_base/APP_REPORT_BASE' or CLS_URI like 'OWCLASS://obj_base/APP_REPORT_BASE/%'
/

prompt
prompt Creating view V$_APP_REPORT_TASK_WEEK_LIST
prompt ==========================================
prompt
create or replace view v$_app_report_task_week_list as
select OBJ_ID "OBJ_ID",CLS_URI "CLS_URI",S_17 "FIELD1",S_18 "FIELD2",S_19 "FIELD3",S_20 "FIELD4",S_21 "FIELD5",S_22 "FIELD6",S_23 "FIELD7",S_24 "FIELD8",S_25 "FIELD9",S_01 "CREATEID",D_01 "CREATEDATE",S_02 "UPDATEID",D_02 "UPDATEDATE",S_03 "USER_ORGID",S_04 "USER_UNITID",S_05 "PROCESS_STATUS",S_06 "REPORT_ID",N_01 "ROW_NO",S_11 "REPORT_INSTANCEID",S_16 "ROW_NAME",N_14 "COLLECT_ROWNO",S_26 "FIELD10" from JFW_OWAY_DATA where CLS_URI='OWCLASS://obj_base/APP_REPORT_BASE/APP_REPORT_TASK_WEEK_LIST' or CLS_URI like 'OWCLASS://obj_base/APP_REPORT_BASE/APP_REPORT_TASK_WEEK_LIST/%'
/

prompt
prompt Creating view V$_APP_REPORT_WAGE_MONTH_BOON
prompt ===========================================
prompt
create or replace view v$_app_report_wage_month_boon as
select OBJ_ID "OBJ_ID",CLS_URI "CLS_URI",S_11 "REPORT_INSTANCEID",S_01 "CREATEID",D_01 "CREATEDATE",S_02 "UPDATEID",D_02 "UPDATEDATE",S_03 "USER_ORGID",S_04 "USER_UNITID",S_05 "PROCESS_STATUS",S_06 "REPORT_ID",N_01 "ROW_NO",N_04 "FIELD5",N_05 "FIELD6",O_01 "FIELD7",N_06 "FIELD8",N_07 "FIELD9",N_08 "FIELD10",N_09 "FIELD11",S_10 "FIELD12",S_14 "FIELD1",S_15 "FIELD2",N_12 "FIELD3",N_13 "FIELD4" from JFW_OWAY_DATA where CLS_URI='OWCLASS://obj_base/APP_REPORT_BASE/APP_REPORT_WAGE_MONTH_BOON' or CLS_URI like 'OWCLASS://obj_base/APP_REPORT_BASE/APP_REPORT_WAGE_MONTH_BOON/%'
/

prompt
prompt Creating view V$_APP_REPORT_WAGE_MONTH_LIST
prompt ===========================================
prompt
create or replace view v$_app_report_wage_month_list as
select OBJ_ID "OBJ_ID",CLS_URI "CLS_URI",S_01 "CREATEID",D_01 "CREATEDATE",S_02 "UPDATEID",D_02 "UPDATEDATE",S_03 "USER_ORGID",S_04 "USER_UNITID",S_05 "PROCESS_STATUS",S_06 "REPORT_ID",N_01 "ROW_NO",S_11 "REPORT_INSTANCEID",S_12 "FIELD1",S_13 "FIELD2",N_02 "FIELD3",N_03 "FIELD4",N_04 "FIELD5",N_05 "FIELD6",O_01 "FIELD7",N_06 "FIELD8",N_07 "FIELD9",N_08 "FIELD10",N_09 "FIELD11",S_14 "FIELD12" from JFW_OWAY_DATA where CLS_URI='OWCLASS://obj_base/APP_REPORT_BASE/APP_REPORT_WAGE_MONTH_LIST' or CLS_URI like 'OWCLASS://obj_base/APP_REPORT_BASE/APP_REPORT_WAGE_MONTH_LIST/%'
/

prompt
prompt Creating view V$_BASE_MESSAGE
prompt =============================
prompt
create or replace view v$_base_message as
select OBJ_ID "OBJ_ID",CLS_URI "CLS_URI",S_01 "TITLE",S_02 "CONTENT",S_03 "TOPICID",S_04 "SENDER",D_01 "SENDDATE",S_05 "RECEIVER",D_02 "SCHEMETIME",D_03 "OVERTIME",N_01 "TIMES",N_02 "ACK",S_06 "STATE",S_07 "ACTIONURL",S_08 "APPID",S_09 "MODELNAME",S_10 "DESCRIPTION" from JFW_OWAY_DATA where CLS_URI='OWCLASS://BASE_MESSAGE' or CLS_URI like 'OWCLASS://BASE_MESSAGE/%'
/

prompt
prompt Creating view V$_BASE_MESSAGE_AMS
prompt =================================
prompt
create or replace view v$_base_message_ams as
select OBJ_ID "OBJ_ID",CLS_URI "CLS_URI",S_01 "TITLE",S_02 "CONTENT",S_03 "TOPICID",S_04 "SENDER",D_01 "SENDDATE",S_05 "RECEIVER",D_02 "SCHEMETIME",D_03 "OVERTIME",N_01 "TIMES",N_02 "ACK",S_06 "STATE",S_07 "ACTIONURL",S_08 "APPID",S_09 "MODELNAME",S_10 "DESCRIPTION" from JFW_OWAY_DATA where CLS_URI='OWCLASS://BASE_MESSAGE/BASE_MESSAGE_AMS' or CLS_URI like 'OWCLASS://BASE_MESSAGE/BASE_MESSAGE_AMS/%'
/

prompt
prompt Creating view V$_BASE_MESSAGE_MAIL
prompt ==================================
prompt
create or replace view v$_base_message_mail as
select OBJ_ID "OBJ_ID",CLS_URI "CLS_URI",S_01 "TITLE",S_02 "CONTENT",S_03 "TOPICID",S_04 "SENDER",D_01 "SENDDATE",S_05 "RECEIVER",D_02 "SCHEMETIME",D_03 "OVERTIME",N_01 "TIMES",N_02 "ACK",S_06 "STATE",S_07 "ACTIONURL",S_08 "APPID",S_09 "MODELNAME",S_10 "DESCRIPTION",S_16 "CC",S_17 "FORMAT",S_18 "RECIEVESERVER",S_19 "SENDSERVER" from JFW_OWAY_DATA where CLS_URI='OWCLASS://BASE_MESSAGE/BASE_MESSAGE_MAIL' or CLS_URI like 'OWCLASS://BASE_MESSAGE/BASE_MESSAGE_MAIL/%'
/

prompt
prompt Creating view V$_BASE_MESSAGE_SMS
prompt =================================
prompt
create or replace view v$_base_message_sms as
select OBJ_ID "OBJ_ID",CLS_URI "CLS_URI",S_01 "TITLE",S_02 "CONTENT",S_03 "TOPICID",S_04 "SENDER",D_01 "SENDDATE",S_05 "RECEIVER",D_02 "SCHEMETIME",D_03 "OVERTIME",N_01 "TIMES",N_02 "ACK",S_06 "STATE",S_07 "ACTIONURL",S_08 "APPID",S_09 "MODELNAME",S_10 "DESCRIPTION" from JFW_OWAY_DATA where CLS_URI='OWCLASS://BASE_MESSAGE/BASE_MESSAGE_SMS' or CLS_URI like 'OWCLASS://BASE_MESSAGE/BASE_MESSAGE_SMS/%'
/

prompt
prompt Creating view V$_CMS_CONTENT_BASE
prompt =================================
prompt
create or replace view v$_cms_content_base as
select OBJ_ID "OBJ_ID",CLS_URI "CLS_URI",S_01 "CREATEID",D_01 "CREATEDATE",S_02 "UPDATEID",D_02 "UPDATEDATE",S_03 "USER_ORGID",S_04 "USER_UNITID",S_10 "NAME",S_12 "PATHNAME",S_13 "DESCRIPTION",S_19 "TITLE",D_08 "PUBLISHDATE",D_09 "INVALIDDATE",S_20 "INSTANCEID",S_05 "PROCESS_STATUS",S_21 "AUDITOR",D_10 "AUDITDATE",S_22 "CONTENT",S_23 "STATUS" from JFW_OWAY_DATA where CLS_URI='OWCLASS://obj_base/OBJ_NAMING/CMS_CONTENT_BASE' or CLS_URI like 'OWCLASS://obj_base/OBJ_NAMING/CMS_CONTENT_BASE/%'
/

prompt
prompt Creating view V$_CMS_CONTENT_NEWS
prompt =================================
prompt
create or replace view v$_cms_content_news as
select OBJ_ID "OBJ_ID",CLS_URI "CLS_URI",S_01 "CREATEID",D_01 "CREATEDATE",S_02 "UPDATEID",D_02 "UPDATEDATE",S_03 "USER_ORGID",S_04 "USER_UNITID",S_10 "NAME",S_12 "PATHNAME",S_13 "DESCRIPTION",S_19 "TITLE",D_08 "PUBLISHDATE",D_09 "INVALIDDATE",S_20 "INSTANCEID",S_21 "AUDITOR",D_10 "AUDITDATE",S_22 "CONTENT",S_23 "STATUS" from JFW_OWAY_DATA where CLS_URI='OWCLASS://obj_base/OBJ_NAMING/CMS_CONTENT_BASE/CMS_CONTENT_NEWS' or CLS_URI like 'OWCLASS://obj_base/OBJ_NAMING/CMS_CONTENT_BASE/CMS_CONTENT_NEWS/%'
/

prompt
prompt Creating view V$_CMS_SITE
prompt =========================
prompt
create or replace view v$_cms_site as
select OBJ_ID "OBJ_ID",CLS_URI "CLS_URI",D_01 "CREATEDATE",S_01 "CREATEID",S_02 "UPDATEID",D_02 "UPDATEDATE",S_03 "USER_ORGID",S_04 "USER_UNITID",S_10 "NAME",S_11 "DECSRIPTION",S_12 "PATHNAME",S_18 "PARENTID" from JFW_OWAY_DATA where CLS_URI='OWCLASS://obj_base/OBJ_NAMING/cms_site' or CLS_URI like 'OWCLASS://obj_base/OBJ_NAMING/cms_site/%'
/

prompt
prompt Creating view V$_CMS_SITE_AUTHOR
prompt ================================
prompt
create or replace view v$_cms_site_author as
select OBJ_ID "OBJ_ID",CLS_URI "CLS_URI",S_01 "SITEID",S_02 "AUTHORID",S_03 "SITENAME" from JFW_OWAY_DATA where CLS_URI='OWCLASS://CMS_SITE_AUTHOR' or CLS_URI like 'OWCLASS://CMS_SITE_AUTHOR/%'
/

prompt
prompt Creating view V$_OBJ_BASE
prompt =========================
prompt
create or replace view v$_obj_base as
select OBJ_ID "OBJ_ID",CLS_URI "CLS_URI",S_01 "CREATEID",D_01 "CREATEDATE",S_02 "UPDATEID",D_02 "UPDATEDATE",S_03 "USER_ORGID",S_04 "USER_UNITID",S_05 "PROCESS_STATUS" from JFW_OWAY_DATA where CLS_URI='OWCLASS://obj_base' or CLS_URI like 'OWCLASS://obj_base/%'
/

prompt
prompt Creating view V$_OBJ_NAMING
prompt ===========================
prompt
create or replace view v$_obj_naming as
select OBJ_ID "OBJ_ID",CLS_URI "CLS_URI",S_01 "CREATEID",D_01 "CREATEDATE",S_02 "UPDATEID",D_02 "UPDATEDATE",S_03 "USER_ORGID",S_04 "USER_UNITID",S_10 "NAME",S_12 "PATHNAME",S_14 "111",S_13 "DESCRIPTION" from JFW_OWAY_DATA where CLS_URI='OWCLASS://obj_base/OBJ_NAMING' or CLS_URI like 'OWCLASS://obj_base/OBJ_NAMING/%'
/

prompt
prompt Creating view V$_SX_NEWS
prompt ========================
prompt
create or replace view v$_sx_news as
select OBJ_ID "OBJ_ID",CLS_URI "CLS_URI",S_01 "CREATEID",D_01 "CREATEDATE",S_02 "UPDATEID",D_02 "UPDATEDATE",S_03 "USER_ORGID",S_04 "USER_UNITID",S_10 "NAME",S_12 "PATHNAME",S_13 "DESCRIPTION",S_19 "TITLE",D_08 "PUBLISHDATE",D_09 "INVALIDDATE",S_20 "INSTANCEID",S_21 "AUDITOR",D_10 "AUDITDATE",S_22 "CONTENT",S_23 "STATUS" from JFW_OWAY_DATA where CLS_URI='OWCLASS://obj_base/OBJ_NAMING/CMS_CONTENT_BASE/CMS_CONTENT_NEWS/SX_NEWS' or CLS_URI like 'OWCLASS://obj_base/OBJ_NAMING/CMS_CONTENT_BASE/CMS_CONTENT_NEWS/SX_NEWS/%'
/

prompt
prompt Creating view V_APP_ACTIVITY_ENTERDETAIL
prompt ========================================
prompt
create or replace view v_app_activity_enterdetail as
select
        APP_ACTIVITY_INFO.Activity_Type as Activity_Type,
        APP_ACTIVITY_INFO.Activity_name as Activity_name,
        APP_ACTIVITY_INFO.fee as fee,
        APP_ACTIVITY_ENTERDETAIL.ACTIVITYID as ACTIVITYID,
        APP_ACTIVITY_ENTERDETAIL.ORGID as ORGID,
        APP_ACTIVITY_ENTERDETAIL.USERID as USERID,
        APP_ACTIVITY_ENTERDETAIL.CREATEID as CREATEID,
        APP_ACTIVITY_ENTERDETAIL.CREATEDate as CREATEDate,
        APP_ACTIVITY_ENTERDETAIL.name as name,
        APP_ACTIVITY_ENTERDETAIL.remark as remark,
        APP_ACTIVITY_ENTERDETAIL.USER_UNITID as USER_UNITID,
        APP_ACTIVITY_ENTERDETAIL.USER_ORGID as USER_ORGID,
        APP_ACTIVITY_ENTERDETAIL.SEQKEY as SEQKEY
FROM APP_ACTIVITY_ENTERDETAIL,APP_ACTIVITY_INFO WHERE APP_ACTIVITY_INFO.SEQKEY=APP_ACTIVITY_ENTERDETAIL.ACTIVITYID
/

prompt
prompt Creating view V_JFW_FIELDINFO
prompt =============================
prompt
CREATE OR REPLACE VIEW V_JFW_FIELDINFO AS
SELECT JFW_OWAY_CAT.CAT_INDEX, 
    JFW_OWAY_CAT.DISPLAYNAME as "CAT_NAME", 
    T_JFW_FIELDINFO.BOFIELDNAME, 
    T_JFW_FIELDINFO.CANSEARCH, T_JFW_FIELDINFO.CAT_ID, 
    T_JFW_FIELDINFO.CHECKVALUE, 
    T_JFW_FIELDINFO.CLSDRIVER, T_JFW_FIELDINFO.CLS_URI,
    T_JFW_FIELDINFO.CODETYPE, T_JFW_FIELDINFO.COLUMNS, 
    T_JFW_FIELDINFO.CREATEDATE, 
    T_JFW_FIELDINFO.CREATEID, 
    T_JFW_FIELDINFO.DEFAULTCTRL, 
    T_JFW_FIELDINFO.DEFAULTVALUE, 
    T_JFW_FIELDINFO.DESCRIPTION, 
    T_JFW_FIELDINFO.DISPLAYFORMAT, 
    T_JFW_FIELDINFO.DISPLAYNAME, 
    T_JFW_FIELDINFO.DISPLAYORDER, 
    T_JFW_FIELDINFO.FIELDNAME, 
    T_JFW_FIELDINFO.FIELDTYPE, 
    T_JFW_FIELDINFO.GENERATETYPE, 
    T_JFW_FIELDINFO.ISEMPTY, T_JFW_FIELDINFO.KEYFLAG, 
    T_JFW_FIELDINFO.LENGTH, T_JFW_FIELDINFO.MAXVALUE, 
    T_JFW_FIELDINFO.MINVALUE, T_JFW_FIELDINFO.MODELNAME,
    T_JFW_FIELDINFO.STATE, T_JFW_FIELDINFO.UPDATEDATE, 
    T_JFW_FIELDINFO.UPDATEID 
    FROM T_JFW_FIELDINFO left outer JOIN
      JFW_OWAY_CAT  ON 
      T_JFW_FIELDINFO.CAT_ID = JFW_OWAY_CAT.CAT_ID WITH READ ONLY
/

prompt
prompt Creating procedure CREATEVIEW
prompt =============================
prompt
create or replace procedure createview is
  cursor currsur is
    select * from T_SVG_REP_MANAGER;
  FLAG      VARCHAR2(108);
  zd        varchar2(108);
  Allsql    VARCHAR2(6000);
  allcount  number;
  countflag number;
begin
  countflag := 0;
  select count(*) into allcount from T_SVG_REP_MANAGER;
  Allsql := 'create or replace view T_SVG_REP_GRAPH_all as ';
  FOR i IN currsur loop
    FLAG := i.RM_TABLE_RELATE;
    zd   := chr(39) || FLAG || chr(39);
    if countflag < allcount - 1 then
      if allcount = 1 then
        Allsql := Allsql || 'select ' || zd || ' as flag,T_SVG_REP_GRAPH_' || FLAG ||
                  '.* from T_SVG_REP_GRAPH_' || FLAG;
      else
        Allsql := Allsql || 'select ' || zd || ' as flag,T_SVG_REP_GRAPH_' || FLAG ||
                  '.* from T_SVG_REP_GRAPH_' || FLAG || ' union all ';
      end if;
    else
      Allsql := Allsql || 'select ' || zd || ' as flag,T_SVG_REP_GRAPH_' || FLAG ||
                '.* from T_SVG_REP_GRAPH_' || FLAG;
    end if;
    countflag := countflag + 1;
  end loop;
  execute immediate Allsql;

end createview;
/

prompt
prompt Creating procedure CREATEVIEWGRAPH
prompt ==================================
prompt
create or replace procedure createviewgraph is
  cursor currsur is
    select * from T_SVG_REP_MANAGER;
  cursor bussiness is select * from T_SVG_CODE_COMMSETS where T_SVG_CODE_COMMSETS.CC_SHORTNAME='SVG_BUSINESS_GRAPH_TYPE';
  inFLAG      VARCHAR2(108);
  zd        varchar2(108);
  Allsql    VARCHAR2(6000);
  allcount  number;
  countflag number;
begin
  countflag := 0;
  select count(*) into allcount from T_SVG_REP_MANAGER;
  Allsql := 'create or replace view T_SVG_REP_GRAPH_parson as ';
  FOR i IN currsur loop
  if countflag> 0 then 
    Allsql:=Allsql|| ' union all';
  end if;
    inFLAG := i.RM_TABLE_RELATE;
    zd   := chr(39) || inFLAG || chr(39);
    Allsql:=Allsql||' select '||zd||' as flag,'||chr(39)||'-1'||chr(39)||'as parentid,'||zd||' as codename,'||zd||' as codeid from dual';
    --eachsql:=' select '||zd||' as flag,'||chr(39)||'-1'||chr(39)||'as parentid,'||zd||' as codename,'||zd||' as codeid from dual';
    --dbms_output.put_line(eachsql);
    for  k in bussiness loop
     Allsql:=Allsql||' union all select '||zd||' as flag ,'||zd||' as parentid,'||chr(39)||k.cc_name||chr(39)||' as codename,'||chr(39)||inFLAG||k.cc_id||chr(39)||' as codeid from dual';
      --eachsql:=' union all select '||zd||' as flag ,'||zd||' as parentid,'||chr(39)||k.cc_name||chr(39)||' as codename,'||chr(39)||k.cc_id||chr(39)||' as codeid from dual';
      --dbms_output.put_line(eachsql);
      for j in (select * from t_svg_rep_graphall where flag = inFLAG and RG_GRAPH_BUSI_TYPE =k.cc_code) loop
        Allsql:=Allsql||' union all  select '||zd||' as flag,'||chr(39)||inFLAG||k.cc_id||chr(39)||' as parentid ,'||chr(39)||j.rg_graph_name||chr(39)||' as  codename, '||chr(39)||j.rg_id||chr(39)||' as codeid from dual';
        --eachsql:=' union all  select '||zd||' as flag,'||chr(39)||k.cc_id||chr(39)||' as parentid ,'||chr(39)||j.rg_graph_name||chr(39)||' as  codename, '||chr(39)||j.rg_id||chr(39)||' as codeid from dual';
        --dbms_output.put_line(eachsql);
      end loop;
    end loop;
  countflag:=countflag+1;
  end loop;
  --dbms_output.put_line(Allsql);
  execute immediate Allsql;

end createviewgraph;
/

prompt
prompt Creating procedure CREATEVIEWGRAPHALL
prompt =====================================
prompt
create or replace procedure createviewgraphall is
  cursor currsur is
    select * from T_SVG_REP_MANAGER;
  cursor bussiness is select * from T_SVG_CODE_COMMSETS where T_SVG_CODE_COMMSETS.CC_SHORTNAME='SVG_BUSINESS_GRAPH_TYPE';
  inFLAG      VARCHAR2(108);
  zd        varchar2(108);
  Allsql    VARCHAR2(6000);
  allcount  number;
  countflag number;
begin
  countflag := 0;
  select count(*) into allcount from T_SVG_REP_MANAGER;
  Allsql := 'create or replace view T_SVG_REP_GRAPH_parson_all as ';
  FOR i IN currsur loop
  if countflag> 0 then 
    Allsql:=Allsql|| ' union all';
  end if;
    inFLAG := i.RM_TABLE_RELATE;
    zd   := chr(39) || inFLAG || chr(39);
    Allsql:=Allsql||' select '||zd||' as flag,'||chr(39)||'-1'||chr(39)||'as parentid,'||zd||' as codename,'||zd||' as codeid , '||chr(39)||0||chr(39)||' as code from dual';
    --eachsql:=' select '||zd||' as flag,'||chr(39)||'-1'||chr(39)||'as parentid,'||zd||' as codename,'||zd||' as codeid from dual';
    --dbms_output.put_line(eachsql);
    for  k in bussiness loop
     Allsql:=Allsql||' union all select '||zd||' as flag ,'||zd||' as parentid,'||chr(39)||k.cc_name||chr(39)||' as codename,'||chr(39)||inFLAG||k.cc_id||chr(39)||' as codeid ,'||chr(39)||k.CC_CODE||chr(39)||' as code from dual';
      --eachsql:=' union all select '||zd||' as flag ,'||zd||' as parentid,'||chr(39)||k.cc_name||chr(39)||' as codename,'||chr(39)||k.cc_id||chr(39)||' as codeid from dual';
      --dbms_output.put_line(eachsql);
      --for j in (select * from t_svg_rep_graphall where flag = inFLAG and RG_GRAPH_BUSI_TYPE =k.cc_code) loop
        --Allsql:=Allsql||' union all  select '||zd||' as flag,'||chr(39)||inFLAG||k.cc_id||chr(39)||' as parentid ,'||chr(39)||j.rg_graph_name||chr(39)||' as  codename, '||chr(39)||j.rg_id||chr(39)||' as codeid from dual';
        --eachsql:=' union all  select '||zd||' as flag,'||chr(39)||k.cc_id||chr(39)||' as parentid ,'||chr(39)||j.rg_graph_name||chr(39)||' as  codename, '||chr(39)||j.rg_id||chr(39)||' as codeid from dual';
        --dbms_output.put_line(eachsql);
      --end loop;
    end loop;
  countflag:=countflag+1;
  end loop;
  --dbms_output.put_line(Allsql);
  execute immediate Allsql;

end createviewgraphall;
/

prompt
prompt Creating procedure CREATEVIEWGRAPHLIST
prompt ======================================
prompt
create or replace procedure createviewGraphlist is
  cursor currsur is
    select * from T_SVG_REP_MANAGER;
  inFLAG    VARCHAR2(108);
  zd        varchar2(108);
  Allsql    VARCHAR2(6000);
  allcount  number;
  countflag number;
begin
  countflag := 0;
  select count(*) into allcount from T_SVG_REP_MANAGER;
  Allsql := 'create or replace view T_SVG_REP_Graphlist as ';
  FOR i IN currsur loop
    inFLAG := i.RM_TABLE_RELATE;
    zd   := chr(39) || inFLAG || chr(39);
    if countflag < allcount - 1 then
      if allcount = 1 then
        Allsql := Allsql || 'select ' || zd || ' as flag,T_SVG_REP_GRAPH_' || inFLAG ||
                  '.* from T_SVG_REP_GRAPH_' || inFLAG;
      else
        Allsql := Allsql || 'select ' || zd || ' as flag,T_SVG_REP_GRAPH_' || inFLAG ||
                  '.* from T_SVG_REP_GRAPH_' || inFLAG || ' union all ';
      end if;
    else
      Allsql := Allsql || 'select ' || zd || ' as flag,T_SVG_REP_GRAPH_' || inFLAG ||
                '.* from T_SVG_REP_GRAPH_' || inFLAG;
    end if;
    countflag := countflag + 1;
  end loop;
  execute immediate Allsql;

end createviewGraphlist;
/

prompt
prompt Creating procedure CREATEVIEWVERLOG
prompt ===================================
prompt
create or replace procedure createviewVerlog is
  cursor currsur is
    select * from T_SVG_REP_MANAGER;
  inFLAG    VARCHAR2(108);
  zd        varchar2(108);
  Allsql    VARCHAR2(6000);
  allcount  number;
  countflag number;
begin
  countflag := 0;
  select count(*) into allcount from T_SVG_REP_MANAGER;
  Allsql := 'create or replace view T_SVG_REP_VERLOG_all as ';
  FOR i IN currsur loop
    inFLAG := i.RM_TABLE_RELATE;
    zd   := chr(39) || inFLAG || chr(39);
    if countflag < allcount - 1 then
      if allcount = 1 then
        Allsql := Allsql || 'select ' || zd || ' as flag,T_SVG_REP_VERLOG_' || inFLAG ||
                  '.* from T_SVG_REP_VERLOG_' || inFLAG;
      else
        Allsql := Allsql || 'select ' || zd || ' as flag,T_SVG_REP_VERLOG_' || inFLAG ||
                  '.* from T_SVG_REP_VERLOG_' || inFLAG || ' union all ';
      end if;
    else
      Allsql := Allsql || 'select ' || zd || ' as flag,T_SVG_REP_VERLOG_' || inFLAG ||
                '.* from T_SVG_REP_VERLOG_' || inFLAG;
    end if;
    countflag := countflag + 1;
  end loop;
  execute immediate Allsql;

end createviewVerlog;
/

prompt
prompt Creating procedure CREATEVIEWVERSET
prompt ===================================
prompt
create or replace procedure createviewVERSET is
  cursor currsur is
    select * from T_SVG_REP_MANAGER;
  inFLAG    VARCHAR2(108);
  zd        varchar2(108);
  Allsql    VARCHAR2(6000);
  allcount  number;
  countflag number;
begin
  countflag := 0;
  select count(*) into allcount from T_SVG_REP_MANAGER;
  Allsql := 'create or replace view T_SVG_REP_VERSET_all as ';
  FOR i IN currsur loop
    inFLAG := i.RM_TABLE_RELATE;
    zd   := chr(39) || inFLAG || chr(39);
    if countflag < allcount - 1 then
      if allcount = 1 then
        Allsql := Allsql || 'select ' || zd || ' as flag,T_SVG_REP_VERSET_' || inFLAG ||
                  '.* from T_SVG_REP_VERSET_' || inFLAG;
      else
        Allsql := Allsql || 'select ' || zd || ' as flag,T_SVG_REP_VERSET_' || inFLAG ||
                  '.* from T_SVG_REP_VERSET_' || inFLAG || ' union all ';
      end if;
    else
      Allsql := Allsql || 'select ' || zd || ' as flag,T_SVG_REP_VERSET_' || inFLAG ||
                '.* from T_SVG_REP_VERSET_' || inFLAG;
    end if;
    countflag := countflag + 1;
  end loop;
  execute immediate Allsql;

end createviewVERSET;
/


spool off
