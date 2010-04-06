--==============================================================
-- Table: UUM_USER
--==============================================================
create table UUM_USER
(
   USER_ID              VARCHAR(64)            not null,
   USER_NAME            VARCHAR(100)           not null,
   USER_SYSTEM_CODE     VARCHAR(100),
   USER_ORG_ID          VARCHAR(64),
   USER_LOGIN_NAME      VARCHAR(50)            not null,
   USER_LOGIN_PASSWD    VARCHAR(50),
   USER_WORK_NO         VARCHAR(64),
   USER_PHONE           VARCHAR(50),
   USER_MOBILE          VARCHAR(50),
   USER_EMAIL           VARCHAR(100),
   USER_SEX             CHAR(2),
   USER_BIRTHDAY        DATE,
   USER_WORKED          VARCHAR(255),
   USER_EDUCATION       CHAR(2),
   USER_ORIGIN          CHAR(6),
   USER_TITLE           VARCHAR(50),
   USER_WORK_LIMIT      VARCHAR(255),
   USER_IDENT_NO        VARCHAR(50),
   USER_IN_DATE         DATE,
   USER_ADDRESS         VARCHAR(255),
   USER_POSTAL          VARCHAR(20),
   USER_STATUS          NUMERIC                not null,
   USER_OUT             NUMERIC,
   USER_AREA            CHAR(6),
   USER_OUT_DATE        DATE,
   USER_LOGIN_DATE      DATE,
   USER_NOTE            VARCHAR(4000),
   USER_PASSWD_CHANGE_DATE DATE,
   USER_UNLOCK_DATE     DATE,
   USER_TYPE            NUMERIC,
   USER_LDAP_URL        VARCHAR(255),
   USER_ORDER           NUMERIC,
   USER_CREATE_ID       VARCHAR(64),
   USER_CREATE_DATE     DATE,
   USER_UPDATE_ID       VARCHAR(64),
   USER_UPDATE_DATE     DATE,
   USER_ANOTHER_JOB1    VARCHAR(64),
   USER_ANOTHER_JOB2    VARCHAR(64),
   USER_ANOTHER_JOB3    VARCHAR(64),
   USER_UNUSED1         VARCHAR(255),
   USER_UNUSED2         VARCHAR(255),
   USER_UNUSED3         NUMERIC,
   USER_UNUSED4         NUMERIC,
   constraint PK_UUM_USER primary key (USER_ID)
);

comment on column UUM_USER.USER_SYSTEM_CODE is
'所在单位或部门的系统编码＋当前用户顺序码。';

comment on column UUM_USER.USER_ORG_ID is
'除超级系统管理员外，其它用户（包括系统管理员和普通）均需指定该值。';

comment on column UUM_USER.USER_LOGIN_NAME is
'具有唯一性约束';

comment on column UUM_USER.USER_LOGIN_PASSWD is
'采用md5加密';

comment on column UUM_USER.USER_SEX is
'1:女性
2:男性';

comment on column UUM_USER.USER_STATUS is
'1:启用 在不考虑分级管理等约束的情况下，启用状态的用户被所有业务操作可见，用户新增时的状态默认为启用状态。
2:暂用 表明用户是临时的，如企业雇佣的临时工，暂用状态的用户只在一段时间内有效，并且不能登录系统。
3:停用 表明需要使用该用户的历史信息，如已退休的用户，在统计时会用到该用户的信息，停用状态的用户不能登录系统。
启用，暂用，停用状态的用户可相互转换。';

comment on column UUM_USER.USER_TYPE is
'1:普通用户 不具备管理用户和行政机构的权利
2:系统管理员 具备分级管理行政机构和用户的权利
9:超级系统管理员 能管理所有的用户和所有的行政机构';






--==============================================================
-- DBMS name:      IBM DB2 UDB 9.5 Common Server
-- Created on:     2010-3-19 11:17:49
--==============================================================


drop table UMS_APPLICATION;

drop table UMS_ATTACHMENTS;

drop table UMS_COMPANY;

drop table UMS_DEFAULT_TACTICS;

drop table UMS_FEE;

drop table UMS_FILTER;

drop table UMS_FORWARD_SERVICE;

drop table UMS_GROUP;

drop table UMS_GROUP_USER;

drop table UMS_MEDIA;

drop table UMS_MESSAGE_SUBSCRIBE;

drop table UMS_PERIOD;

drop table UMS_QFXX;

drop table UMS_QFXX_MSGATTACHMENT;

drop table UMS_QFXX_RECEIVERS;

drop table UMS_RECEIVE_OK;

drop table UMS_RECEIVE_READY;

drop table UMS_REPLY;

drop table UMS_SEND_ERROR;

drop table UMS_SEND_OK;

drop table UMS_SEND_READY;

drop table UMS_SERVICE;

drop table UMS_TACTICS;

drop table UMS_TRANSMIT_ERR;

drop table UMS_TRANSMIT_MES;

--==============================================================
-- Table: UMS_APPLICATION
--==============================================================
create table UMS_APPLICATION
(
   SEQKEY               VARCHAR(36)            not null,
   APPT_ID              VARCHAR(36)            not null,
   APPT_NAME            VARCHAR(50)            not null,
   COMP_KEY             VARCHAR(32)            not null,
   APPT_PASSWORD        VARCHAR(20),
   APPT_MD5PWD          VARCHAR(50),
   APPT_STATUS          VARCHAR(32),
   APPT_IP              VARCHAR(20),
   APPT_TIMEOUT         NUMERIC(11),
   APPT_SPNO            VARCHAR(11),
   APPT_CHANNELTYPE     NUMERIC(4)            not null,
   APPT_EMAIL           VARCHAR(40),
   APPT_LOGINNAME       VARCHAR(20),
   APPT_LOGINPWD        VARCHAR(20),
   FEE_KEY              VARCHAR(36),
   APPT_DESCRIPTIONS    VARCHAR(256),
   APPT_UP_PORT         NUMERIC(4),
   APPT_DOWN_PORT       NUMERIC(4),
   constraint APPLICATION_PRIMARY_1 primary key (SEQKEY)
);

comment on table UMS_APPLICATION is
'应用定义表';

comment on column UMS_APPLICATION.SEQKEY is
'内码';

comment on column UMS_APPLICATION.APPT_ID is
'应用标识';

comment on column UMS_APPLICATION.APPT_NAME is
'应用名称';

comment on column UMS_APPLICATION.COMP_KEY is
'所属单位标识';

comment on column UMS_APPLICATION.APPT_PASSWORD is
'应用注册密码';

comment on column UMS_APPLICATION.APPT_MD5PWD is
'MD5密码';

comment on column UMS_APPLICATION.APPT_STATUS is
'应用状态';

comment on column UMS_APPLICATION.APPT_IP is
'IP地址';

comment on column UMS_APPLICATION.APPT_TIMEOUT is
'通讯超时（秒）';

comment on column UMS_APPLICATION.APPT_SPNO is
'特服号，如9559801';

comment on column UMS_APPLICATION.APPT_CHANNELTYPE is
'接收渠道对应类型';

comment on column UMS_APPLICATION.APPT_EMAIL is
'应用需要发送email';

comment on column UMS_APPLICATION.APPT_LOGINNAME is
'消息用登录名';

comment on column UMS_APPLICATION.APPT_LOGINPWD is
'消息用密码';

comment on column UMS_APPLICATION.FEE_KEY is
'计费标识';

comment on column UMS_APPLICATION.APPT_DESCRIPTIONS is
'应用描述';

comment on column UMS_APPLICATION.APPT_UP_PORT is
'转发上行端口';

comment on column UMS_APPLICATION.APPT_DOWN_PORT is
'转发下行端口';

--==============================================================
-- Table: UMS_ATTACHMENTS
--==============================================================
create table UMS_ATTACHMENTS
(
   BATCHNO              VARCHAR(14)            not null,
   SERIALNO             NUMERIC(8)             not null,
   SEQUENCENO           NUMERIC                not null,
   FILENAME             VARCHAR(200),
   FILECONTENT          BLOB
);

--==============================================================
-- Table: UMS_COMPANY
--==============================================================
create table UMS_COMPANY
(
   SEQKEY               VARCHAR(36)            not null,
   COMP_ID              VARCHAR(4)             not null,
   COMP_NAME            VARCHAR(50)            not null,
   COMP_LINKMAN         VARCHAR(20),
   COMP_EMAIL           VARCHAR(30),
   COMP_TEL             VARCHAR(20),
   COMP_MOBILETEL       VARCHAR(20),
   COMP_ADDRESS         VARCHAR(100),
   COMP_DESCRIPTIONS    VARCHAR(256),
   constraint COMPANY_SEQKEY primary key (SEQKEY)
);

comment on table UMS_COMPANY is
'单位表';

comment on column UMS_COMPANY.SEQKEY is
'内码';

comment on column UMS_COMPANY.COMP_ID is
'单位标识';

comment on column UMS_COMPANY.COMP_NAME is
'单位名称';

comment on column UMS_COMPANY.COMP_LINKMAN is
'联系人';

comment on column UMS_COMPANY.COMP_EMAIL is
'Email';

comment on column UMS_COMPANY.COMP_TEL is
'电话';

comment on column UMS_COMPANY.COMP_MOBILETEL is
'手机号码';

comment on column UMS_COMPANY.COMP_ADDRESS is
'单位地址';

comment on column UMS_COMPANY.COMP_DESCRIPTIONS is
'单位描述';

--==============================================================
-- Table: UMS_DEFAULT_TACTICS
--==============================================================
create table UMS_DEFAULT_TACTICS
(
   SEQKEY               VARCHAR(36)            not null,
   MEDIA_KEY            VARCHAR(36)            not null,
   DEFT_PRIORITY        VARCHAR(3)             not null,
   PERIOD_KEY           VARCHAR(36)            not null,
   constraint PK_UMS_DEFAULT_TACTICS primary key (SEQKEY)
);

comment on table UMS_DEFAULT_TACTICS is
'默认策略表';

comment on column UMS_DEFAULT_TACTICS.SEQKEY is
'内码';

comment on column UMS_DEFAULT_TACTICS.MEDIA_KEY is
'渠道内码';

comment on column UMS_DEFAULT_TACTICS.DEFT_PRIORITY is
'优先级';

comment on column UMS_DEFAULT_TACTICS.PERIOD_KEY is
'有效时间段内码';

--==============================================================
-- Table: UMS_FEE
--==============================================================
create table UMS_FEE
(
   SEQKEY               VARCHAR(36)            not null,
   FEE_SERVICE_NO       VARCHAR(20)            not null,
   FEE_FEE              NUMERIC(12)            not null,
   FEE_TYPE             NUMERIC(1)             not null,
   FEE_TERMINAL         VARCHAR(32),
   FEE_DESCRIPTION      VARCHAR(256),
   FEE_NAME             VARCHAR(200),
   FEE_STYLE            NUMERIC(1)             not null,
   constraint PK_FEE_V3 primary key (SEQKEY)
);

comment on table UMS_FEE is
'服务收费对应表';

comment on column UMS_FEE.SEQKEY is
'内码';

comment on column UMS_FEE.FEE_SERVICE_NO is
'收费服务方';

comment on column UMS_FEE.FEE_FEE is
'费用';

comment on column UMS_FEE.FEE_TYPE is
'计费方式';

comment on column UMS_FEE.FEE_TERMINAL is
'付费对象';

comment on column UMS_FEE.FEE_DESCRIPTION is
'备注';

comment on column UMS_FEE.FEE_NAME is
'费用名称';

comment on column UMS_FEE.FEE_STYLE is
'计费类型（1-条2-大小）';

--==============================================================
-- Table: UMS_FILTER
--==============================================================
create table UMS_FILTER
(
   SEQKEY               VARCHAR(36)            not null,
   FILTER_CONTENT       VARCHAR(200)           not null,
   FILTER_CREATE_USERID VARCHAR(36)            not null,
   FILTER_CREATE_DATE   VARCHAR(36)            not null,
   FILTER_STATUSFLAG    VARCHAR(3)             not null,
   constraint PK_UMS_FILTER primary key (SEQKEY)
);

comment on table UMS_FILTER is
'消息过滤表';

comment on column UMS_FILTER.SEQKEY is
'内码';

comment on column UMS_FILTER.FILTER_CONTENT is
'内码';

comment on column UMS_FILTER.FILTER_CREATE_USERID is
'创建人';

comment on column UMS_FILTER.FILTER_CREATE_DATE is
'创建时间';

comment on column UMS_FILTER.FILTER_STATUSFLAG is
'使用状态';

--==============================================================
-- Table: UMS_FORWARD_SERVICE
--==============================================================
create table UMS_FORWARD_SERVICE
(
   SEQKEY               VARCHAR(36)            not null,
   APP_KEY              VARCHAR(36)            not null,
   COMPANY_KEY          VARCHAR(36)            not null,
   SERVICE_KEY          VARCHAR(36)            not null,
   FORWARD_CONTENT      VARCHAR(200)           not null,
   FORWARD_CREATE_USERID VARCHAR(36)            not null,
   FORWARD_CREATE_DATE  DATE                   not null,
   FORWARD_STATUSFLAG   VARCHAR(3)             not null,
   constraint PK_UMS_FORWARD_SERVICE primary key (SEQKEY)
);

comment on table UMS_FORWARD_SERVICE is
'消息转发表';

comment on column UMS_FORWARD_SERVICE.SEQKEY is
'内码';

comment on column UMS_FORWARD_SERVICE.APP_KEY is
'应用内码';

comment on column UMS_FORWARD_SERVICE.COMPANY_KEY is
'单位内码';

comment on column UMS_FORWARD_SERVICE.SERVICE_KEY is
'服务内码';

comment on column UMS_FORWARD_SERVICE.FORWARD_CONTENT is
'转发对象';

comment on column UMS_FORWARD_SERVICE.FORWARD_CREATE_USERID is
'创建人';

comment on column UMS_FORWARD_SERVICE.FORWARD_CREATE_DATE is
'创建时间';

comment on column UMS_FORWARD_SERVICE.FORWARD_STATUSFLAG is
'使用状态';

--==============================================================
-- Table: UMS_GROUP
--==============================================================
create table UMS_GROUP
(
   SEQKEY               VARCHAR(36)            not null,
   UMS_GROUP_NAME       VARCHAR(50),
   CREATE_USERID        VARCHAR(32),
   CREATE_DATE          DATE,
   UMS_GROUP_SUM        VARCHAR(50),
   UMS_GROUP_SHORT      VARCHAR(50),
   UMS_GROUP_TYPE       VARCHAR(3)
);

comment on table UMS_GROUP is
'组信息表';

comment on column UMS_GROUP.SEQKEY is
'内码';

comment on column UMS_GROUP.UMS_GROUP_NAME is
'组名称';

comment on column UMS_GROUP.CREATE_USERID is
'创建人员编号';

comment on column UMS_GROUP.CREATE_DATE is
'创建日期';

comment on column UMS_GROUP.UMS_GROUP_SUM is
'组员人数';

comment on column UMS_GROUP.UMS_GROUP_SHORT is
'组简称';

comment on column UMS_GROUP.UMS_GROUP_TYPE is
'组类型 1-公共2-私人';

--==============================================================
-- Table: UMS_GROUP_USER
--==============================================================
create table UMS_GROUP_USER
(
   SEQKEY               VARCHAR(36)            not null,
   USERID               VARCHAR(32),
   UMS_GROUP_ID         VARCHAR(50),
   USER_WORK_NO         VARCHAR(50),
   USER_ORG_ID          VARCHAR(50)
);

comment on table UMS_GROUP_USER is
'组人员信息表';

comment on column UMS_GROUP_USER.SEQKEY is
'内码';

comment on column UMS_GROUP_USER.USERID is
'组人员编号';

comment on column UMS_GROUP_USER.UMS_GROUP_ID is
'组号';

--==============================================================
-- Table: UMS_MEDIA
--==============================================================
create table UMS_MEDIA
(
   SEQKEY               VARCHAR(36)            not null,
   MEDIA_ID             VARCHAR(36)            not null,
   MEDIA_NAME           VARCHAR(50)            not null,
   MEDIA_CLASS          VARCHAR(200),
   MEDIA_TYPE           NUMERIC(11)            not null,
   MEDIA_STATUSFLAG     NUMERIC(11),
   MEDIA_IP             VARCHAR(20),
   MEDIA_PORT           NUMERIC(11),
   MEDIA_TIMEOUT        NUMERIC(11),
   MEDIA_REPEATTIMES    NUMERIC(11),
   MEDIA_STARTWORKTIME  VARCHAR(6),
   MEDIA_ENDWORKTIME    VARCHAR(6),
   MEDIA_LOGINNAME      VARCHAR(20),
   MEDIA_LOGINPASSWORD  VARCHAR(16),
   MEDIA_SLEEPTIME      NUMERIC(11),
   MEDIA_STYLE          NUMERIC(11),
   MEDIA_DESCRIPTION    VARCHAR(256),
   constraint MEDIA1_PRIMARY_1 primary key (SEQKEY)
);

comment on table UMS_MEDIA is
'渠道定义表';

comment on column UMS_MEDIA.SEQKEY is
'内码';

comment on column UMS_MEDIA.MEDIA_ID is
'渠道标识';

comment on column UMS_MEDIA.MEDIA_NAME is
'渠道名称';

comment on column UMS_MEDIA.MEDIA_CLASS is
'类';

comment on column UMS_MEDIA.MEDIA_TYPE is
'拔入/拔出标志（0：拔入，1：拔出）';

comment on column UMS_MEDIA.MEDIA_STATUSFLAG is
'状态（0：正常，1：停止）';

comment on column UMS_MEDIA.MEDIA_IP is
'IP地址';

comment on column UMS_MEDIA.MEDIA_PORT is
'IP端口';

comment on column UMS_MEDIA.MEDIA_TIMEOUT is
'通讯超时（秒）';

comment on column UMS_MEDIA.MEDIA_REPEATTIMES is
'最大重发次数';

comment on column UMS_MEDIA.MEDIA_STARTWORKTIME is
'开始工作时间';

comment on column UMS_MEDIA.MEDIA_ENDWORKTIME is
'终止工作时间';

comment on column UMS_MEDIA.MEDIA_LOGINNAME is
'用户名';

comment on column UMS_MEDIA.MEDIA_LOGINPASSWORD is
'密码';

comment on column UMS_MEDIA.MEDIA_SLEEPTIME is
'延时';

comment on column UMS_MEDIA.MEDIA_DESCRIPTION is
'备注';

--==============================================================
-- Table: UMS_MESSAGE_SUBSCRIBE
--==============================================================
create table UMS_MESSAGE_SUBSCRIBE
(
   SEQKEY               VARCHAR(36)            not null,
   COMPANY_KEY          VARCHAR(36)            not null,
   APP_KEY              VARCHAR(36)            not null,
   SERVICE_KEY          VARCHAR(36)            not null,
   PERSON_KEY           VARCHAR(36)            not null,
   SUBSTIME             DATE                   not null,
   SUBSTATUS            VARCHAR(3),
   UNSUBSTIME           DATE,
   constraint PK_UMS_MESSAGE_SUBSCRIBE primary key (SEQKEY)
);

comment on table UMS_MESSAGE_SUBSCRIBE is
'消息订阅表';

comment on column UMS_MESSAGE_SUBSCRIBE.SEQKEY is
'内码';

comment on column UMS_MESSAGE_SUBSCRIBE.COMPANY_KEY is
'单位内码';

comment on column UMS_MESSAGE_SUBSCRIBE.APP_KEY is
'应用内码';

comment on column UMS_MESSAGE_SUBSCRIBE.SERVICE_KEY is
'服务内码';

comment on column UMS_MESSAGE_SUBSCRIBE.PERSON_KEY is
'人员内码';

comment on column UMS_MESSAGE_SUBSCRIBE.SUBSTIME is
'订阅时间';

comment on column UMS_MESSAGE_SUBSCRIBE.SUBSTATUS is
'订阅状态';

comment on column UMS_MESSAGE_SUBSCRIBE.UNSUBSTIME is
'退订时间';

--==============================================================
-- Table: UMS_PERIOD
--==============================================================
create table UMS_PERIOD
(
   SEQKEY               VARCHAR(36)            not null,
   PERD_START           VARCHAR(2)             not null,
   PERD_END             VARCHAR(2)             not null,
   PERD_STIME           VARCHAR(5)             not null,
   PERD_ETIME           VARCHAR(5)             not null,
   PERD_NAME            VARCHAR(200),
   constraint PK_UMS_PERIOD primary key (SEQKEY)
);

comment on table UMS_PERIOD is
'有效时间段表';

comment on column UMS_PERIOD.SEQKEY is
'内码';

comment on column UMS_PERIOD.PERD_START is
'开始星期';

comment on column UMS_PERIOD.PERD_END is
'结束星期';

comment on column UMS_PERIOD.PERD_STIME is
'开始时间';

comment on column UMS_PERIOD.PERD_ETIME is
'结束时间';

comment on column UMS_PERIOD.PERD_NAME is
'名称';

--==============================================================
-- Table: UMS_QFXX
--==============================================================
create table UMS_QFXX
(
   MSGCONTENTSUBJECT    VARCHAR(100),
   SERVICEID            VARCHAR(36),
   SENDDIRECTLY         NUMERIC(4),
   MEDIAID              VARCHAR(32),
   SUBMITDATE           VARCHAR(32),
   SUBMITTIME           VARCHAR(32),
   PRIORITY             NUMERIC(4),
   ACK                  NUMERIC(4),
   TIMESETFLAG          NUMERIC(4),
   INVALIDDATE          VARCHAR(32),
   INVALIDTIME          VARCHAR(32),
   REPLYDESTINATION     VARCHAR(32),
   NEEDREPLY            NUMERIC(4),
   FEESERVICENO         VARCHAR(32),
   UMSFLAG              NUMERIC(4),
   COMPANYID            VARCHAR(32),
   SEQKEY               VARCHAR(36)            not null,
   APPSERIALNO          VARCHAR(32),
   DSDATE               DATE,
   DSHH                 VARCHAR(32),
   DSMM                 VARCHAR(32),
   MSGCONTENTCONTENT    VARCHAR(2000),
   constraint QFXX_SEQKEY primary key (SEQKEY)
);

comment on column UMS_QFXX.MSGCONTENTSUBJECT is
'标题';

comment on column UMS_QFXX.SERVICEID is
'服务名称';

comment on column UMS_QFXX.SENDDIRECTLY is
'是否直接发送';

comment on column UMS_QFXX.MEDIAID is
'渠道名称';

comment on column UMS_QFXX.SUBMITDATE is
'提交日期';

comment on column UMS_QFXX.SUBMITTIME is
'提交时间';

comment on column UMS_QFXX.PRIORITY is
'优先级';

comment on column UMS_QFXX.ACK is
'回执标志';

comment on column UMS_QFXX.TIMESETFLAG is
'定时发送标志';

comment on column UMS_QFXX.INVALIDDATE is
'失效日期';

comment on column UMS_QFXX.INVALIDTIME is
'失效时间';

comment on column UMS_QFXX.REPLYDESTINATION is
'回复目的地编号';

comment on column UMS_QFXX.NEEDREPLY is
'消息回复标记';

comment on column UMS_QFXX.FEESERVICENO is
'收费名称';

comment on column UMS_QFXX.UMSFLAG is
'UMS标识';

comment on column UMS_QFXX.COMPANYID is
'单位名称';

comment on column UMS_QFXX.SEQKEY is
'内码';

comment on column UMS_QFXX.APPSERIALNO is
'应用名称';

comment on column UMS_QFXX.DSDATE is
'定时日期';

comment on column UMS_QFXX.DSHH is
'时';

comment on column UMS_QFXX.DSMM is
'分';

--==============================================================
-- Table: UMS_QFXX_MSGATTACHMENT
--==============================================================
create table UMS_QFXX_MSGATTACHMENT
(
   SEQKEY               VARCHAR(36)            not null,
   FILENAME             VARCHAR(100),
   XXID                 VARCHAR(36),
   constraint QFXX_MSGATTACHMENT_SEQKEY primary key (SEQKEY)
);

comment on table UMS_QFXX_MSGATTACHMENT is
'消息邮件附件表';

--==============================================================
-- Table: UMS_QFXX_RECEIVERS
--==============================================================
create table UMS_QFXX_RECEIVERS
(
   SEQKEY               VARCHAR(36)            not null,
   PARTICIPANTID        VARCHAR(36),
   IDTYPE               VARCHAR(32),
   PARTICIPANTTYPE      VARCHAR(32),
   XXID                 VARCHAR(36),
   constraint QFXX_RECEIVERS_SEQKEY primary key (SEQKEY)
);

--==============================================================
-- Table: UMS_RECEIVE_OK
--==============================================================
create table UMS_RECEIVE_OK
(
   BATCHNO              VARCHAR(42)            not null,
   SERIALNO             NUMERIC                not null,
   SEQUENCENO           NUMERIC                not null,
   RETCODE              VARCHAR(12),
   ERRMSG               VARCHAR(60),
   STATUSFLAG           NUMERIC,
   SERVICEID            VARCHAR(20),
   APPSERIALNO          VARCHAR(35),
   MEDIAID              VARCHAR(10),
   SENDID               VARCHAR(60),
   RECVID               VARCHAR(60),
   SUBMITDATE           VARCHAR(24),
   SUBMITTIME           VARCHAR(18),
   FINISHDATE           VARCHAR(24),
   FINISHTIME           VARCHAR(18),
   CONTENTSUBJECT       VARCHAR(60),
   CONTENT              VARCHAR(1000),
   MSGID                VARCHAR(20),
   ACK                  NUMERIC(1),
   REPLYNO              VARCHAR(30),
   DOCOUNT              NUMERIC,
   MSGTYPE              NUMERIC,
   SEQKEY               VARCHAR(32)            not null,
   SENDERIDTYPE         NUMERIC,
   RECEIVERIDTYPE       NUMERIC,
   CONTENTMODE          NUMERIC,
   BATCHMODE            VARCHAR(1),
   constraint PK_UMS_RECEIVE_OK primary key (SEQKEY)
);

comment on table UMS_RECEIVE_OK is
'拨入已接收表';

comment on column UMS_RECEIVE_OK.BATCHNO is
'批号（8位日期+6位序号）';

comment on column UMS_RECEIVE_OK.SERIALNO is
'交易流水号';

comment on column UMS_RECEIVE_OK.SEQUENCENO is
'消息序号';

comment on column UMS_RECEIVE_OK.RETCODE is
'交易结果';

comment on column UMS_RECEIVE_OK.ERRMSG is
'错误信息';

comment on column UMS_RECEIVE_OK.STATUSFLAG is
'状态（0：正常，1：未就绪）';

comment on column UMS_RECEIVE_OK.SERVICEID is
'应用标识';

comment on column UMS_RECEIVE_OK.APPSERIALNO is
'应用生成消息序号';

comment on column UMS_RECEIVE_OK.MEDIAID is
'接收渠道号';

comment on column UMS_RECEIVE_OK.SENDID is
'发送方地址';

comment on column UMS_RECEIVE_OK.RECVID is
'接收方地址';

comment on column UMS_RECEIVE_OK.SUBMITDATE is
'提交日期';

comment on column UMS_RECEIVE_OK.SUBMITTIME is
'提交时间';

comment on column UMS_RECEIVE_OK.FINISHDATE is
'完成日期';

comment on column UMS_RECEIVE_OK.FINISHTIME is
'完成时间';

comment on column UMS_RECEIVE_OK.CONTENTSUBJECT is
'信息标题';

comment on column UMS_RECEIVE_OK.CONTENT is
'信息内容';

comment on column UMS_RECEIVE_OK.MSGID is
'消息编号';

comment on column UMS_RECEIVE_OK.ACK is
'回执标志';

comment on column UMS_RECEIVE_OK.REPLYNO is
'回复编号';

comment on column UMS_RECEIVE_OK.DOCOUNT is
'发送次数';

comment on column UMS_RECEIVE_OK.MSGTYPE is
'消息类型';

comment on column UMS_RECEIVE_OK.SEQKEY is
'主键';

comment on column UMS_RECEIVE_OK.SENDERIDTYPE is
'发送者ID类型';

comment on column UMS_RECEIVE_OK.RECEIVERIDTYPE is
'接收者ID类型';

comment on column UMS_RECEIVE_OK.CONTENTMODE is
'内容编码';

comment on column UMS_RECEIVE_OK.BATCHMODE is
'批量属性（0：单笔，1批量）';

--==============================================================
-- Table: UMS_RECEIVE_READY
--==============================================================
create table UMS_RECEIVE_READY
(
   BATCHNO              VARCHAR(42)            not null,
   SERIALNO             NUMERIC                not null,
   SEQUENCENO           NUMERIC                not null,
   RETCODE              VARCHAR(12),
   ERRMSG               VARCHAR(60),
   STATUSFLAG           NUMERIC,
   SERVICEID            VARCHAR(20),
   APPSERIALNO          VARCHAR(35),
   MEDIAID              VARCHAR(10),
   SENDID               VARCHAR(60),
   RECVID               VARCHAR(60),
   SUBMITDATE           VARCHAR(24),
   SUBMITTIME           VARCHAR(18),
   FINISHDATE           VARCHAR(24),
   FINISHTIME           VARCHAR(18),
   CONTENTSUBJECT       VARCHAR(60),
   CONTENT              VARCHAR(1000),
   MSGID                VARCHAR(20),
   ACK                  NUMERIC(1),
   REPLYNO              VARCHAR(30),
   DOCOUNT              NUMERIC,
   MSGTYPE              NUMERIC,
   SEQKEY               VARCHAR(36)            not null,
   SENDERIDTYPE         NUMERIC,
   RECEIVERIDTYPE       NUMERIC,
   constraint PK_UMS_RECEIVE_READY primary key (SEQKEY)
);

comment on table UMS_RECEIVE_READY is
'拨入待接收表';

comment on column UMS_RECEIVE_READY.BATCHNO is
'批号（8位日期+6位序号）';

comment on column UMS_RECEIVE_READY.SERIALNO is
'交易流水号';

comment on column UMS_RECEIVE_READY.SEQUENCENO is
'消息序号';

comment on column UMS_RECEIVE_READY.RETCODE is
'交易结果';

comment on column UMS_RECEIVE_READY.ERRMSG is
'错误信息';

comment on column UMS_RECEIVE_READY.STATUSFLAG is
'状态（0：正常，1：未就绪）';

comment on column UMS_RECEIVE_READY.SERVICEID is
'需要接收消息的服务ID';

comment on column UMS_RECEIVE_READY.APPSERIALNO is
'应用系统生成的消息序号';

comment on column UMS_RECEIVE_READY.MEDIAID is
'接收渠道标识';

comment on column UMS_RECEIVE_READY.SENDID is
'发送方地址';

comment on column UMS_RECEIVE_READY.RECVID is
'接收方地址';

comment on column UMS_RECEIVE_READY.SUBMITDATE is
'提交日期';

comment on column UMS_RECEIVE_READY.SUBMITTIME is
'提交时间';

comment on column UMS_RECEIVE_READY.FINISHDATE is
'完成日期';

comment on column UMS_RECEIVE_READY.FINISHTIME is
'完成时间';

comment on column UMS_RECEIVE_READY.CONTENTSUBJECT is
'信息标题';

comment on column UMS_RECEIVE_READY.CONTENT is
'信息内容';

comment on column UMS_RECEIVE_READY.MSGID is
'消息编号';

comment on column UMS_RECEIVE_READY.ACK is
'回执标志';

comment on column UMS_RECEIVE_READY.DOCOUNT is
'发送次数';

comment on column UMS_RECEIVE_READY.MSGTYPE is
'消息类型';

--==============================================================
-- Table: UMS_REPLY
--==============================================================
create table UMS_REPLY
(
   BATCHNO              VARCHAR(14)            not null,
   SERIALNO             NUMERIC(8)             not null,
   SEQUENCENO           NUMERIC                not null,
   BATCHMODE            VARCHAR(1),
   APPSERIALNO          VARCHAR(35),
   RETCODE              VARCHAR(10),
   ERRMSG               VARCHAR(60),
   STATUSFLAG           NUMERIC,
   SERVICEID            VARCHAR(20),
   SENDDIRECTLY         NUMERIC,
   MEDIAID              VARCHAR(3),
   SENDID               VARCHAR(60),
   SENDERTYPE           NUMERIC(1),
   RECVID               VARCHAR(60),
   RECEIVERTYPE         NUMERIC(1),
   SUBMITDATE           VARCHAR(8),
   SUBMITTIME           VARCHAR(6),
   FINISHDATE           VARCHAR(8),
   FINISHTIME           VARCHAR(6),
   REP                  NUMERIC,
   DOCOUNT              NUMERIC,
   PRIORITY             NUMERIC(1),
   CONTENTSUBJECT       VARCHAR(50),
   CONTENT              VARCHAR(1000),
   MSGID                VARCHAR(20),
   TIMESETFLAG          NUMERIC(1),
   SETDATE              VARCHAR(8),
   SETTIME              VARCHAR(6),
   INVALIDDATE          VARCHAR(8),
   INVALIDTIME          VARCHAR(6),
   ACK                  NUMERIC(1),
   REPLYDESTINATION     VARCHAR(60),
   NEEDREPLY            NUMERIC(1),
   FEESERVICENO         VARCHAR(20),
   FEE                  NUMERIC,
   FEETYPE              NUMERIC(1),
   UMSFLAG              NUMERIC,
   COMPANYID            VARCHAR(20),
   SEQKEY               VARCHAR(36)            not null,
   SENDERIDTYPE         VARCHAR(32),
   RECEIVERIDTYPE       VARCHAR(32),
   CONTENTMODE          NUMERIC,
   constraint PK_UMS_REPLY primary key (SEQKEY)
);

comment on table UMS_REPLY is
'回复表';

comment on column UMS_REPLY.BATCHNO is
'批号(8位日期+6位序号)';

comment on column UMS_REPLY.SERIALNO is
'交易流水号';

comment on column UMS_REPLY.SEQUENCENO is
'消息序号';

comment on column UMS_REPLY.BATCHMODE is
'批量属性(0:单笔,1:批量)';

comment on column UMS_REPLY.APPSERIALNO is
'应用生成的消息序号';

comment on column UMS_REPLY.RETCODE is
'交易结果';

comment on column UMS_REPLY.ERRMSG is
'错误信息';

comment on column UMS_REPLY.STATUSFLAG is
'状态(0:正常,1:未就绪)';

comment on column UMS_REPLY.SERVICEID is
'服务号';

comment on column UMS_REPLY.SENDDIRECTLY is
'是否直接从指定渠道发送';

comment on column UMS_REPLY.MEDIAID is
'渠道标识';

comment on column UMS_REPLY.SENDID is
'发送方地址';

comment on column UMS_REPLY.SENDERTYPE is
'发送者角色(FROM：1）';

comment on column UMS_REPLY.RECVID is
'接收方地址';

comment on column UMS_REPLY.RECEIVERTYPE is
'接收者角色(TO：2，CC：3)';

comment on column UMS_REPLY.SUBMITDATE is
'提交日期';

comment on column UMS_REPLY.SUBMITTIME is
'提交时间';

comment on column UMS_REPLY.FINISHDATE is
'完成日期';

comment on column UMS_REPLY.FINISHTIME is
'完成时间';

comment on column UMS_REPLY.REP is
'当前渠道重复发生次数';

comment on column UMS_REPLY.DOCOUNT is
'累计次数';

comment on column UMS_REPLY.PRIORITY is
'优先级别';

comment on column UMS_REPLY.CONTENTSUBJECT is
'标题';

comment on column UMS_REPLY.CONTENT is
'信息内容';

comment on column UMS_REPLY.MSGID is
'消息编号';

comment on column UMS_REPLY.TIMESETFLAG is
'定时发送标志(0:非定时,1:定时)';

comment on column UMS_REPLY.SETDATE is
'定时发送日期';

comment on column UMS_REPLY.SETTIME is
'定时发送时间';

comment on column UMS_REPLY.INVALIDDATE is
'失效日期';

comment on column UMS_REPLY.INVALIDTIME is
'失效时间';

comment on column UMS_REPLY.ACK is
'回执标志';

comment on column UMS_REPLY.REPLYDESTINATION is
'回复目的地编号';

comment on column UMS_REPLY.NEEDREPLY is
'消息回复标志';

comment on column UMS_REPLY.FEESERVICENO is
'计费服务号';

comment on column UMS_REPLY.FEE is
'费用';

comment on column UMS_REPLY.FEETYPE is
'计费方式';

comment on column UMS_REPLY.UMSFLAG is
'消息所要发向UMS标志(1:自身,2:其他UMS)';

comment on column UMS_REPLY.COMPANYID is
'如果UMSFLAG为2,则表示消息将发往哪个company';

--==============================================================
-- Table: UMS_SEND_ERROR
--==============================================================
create table UMS_SEND_ERROR
(
   BATCHNO              VARCHAR(14)            not null,
   SERIALNO             NUMERIC(8)             not null,
   SEQUENCENO           NUMERIC                not null,
   BATCHMODE            VARCHAR(1),
   APPSERIALNO          VARCHAR(35),
   RETCODE              VARCHAR(10),
   ERRMSG               VARCHAR(60),
   STATUSFLAG           NUMERIC,
   SERVICEID            VARCHAR(20),
   SENDDIRECTLY         NUMERIC,
   MEDIAID              VARCHAR(3),
   SENDID               VARCHAR(60),
   SENDERTYPE           NUMERIC(1),
   RECVID               VARCHAR(60),
   RECEIVERTYPE         NUMERIC(1),
   SUBMITDATE           VARCHAR(8),
   SUBMITTIME           VARCHAR(6),
   FINISHDATE           VARCHAR(8),
   FINISHTIME           VARCHAR(6),
   REP                  NUMERIC,
   DOCOUNT              NUMERIC,
   PRIORITY             NUMERIC(1),
   CONTENTSUBJECT       VARCHAR(50),
   CONTENT              VARCHAR(1000),
   MSGID                VARCHAR(20),
   TIMESETFLAG          NUMERIC(1),
   SETDATE              VARCHAR(8),
   SETTIME              VARCHAR(6),
   INVALIDDATE          VARCHAR(8),
   INVALIDTIME          VARCHAR(6),
   ACK                  NUMERIC(1),
   REPLYDESTINATION     VARCHAR(60),
   NEEDREPLY            NUMERIC(1),
   FEESERVICENO         VARCHAR(20),
   FEE                  NUMERIC,
   FEETYPE              NUMERIC(1),
   UMSFLAG              NUMERIC,
   COMPANYID            VARCHAR(20),
   SEQKEY               VARCHAR(36)            not null,
   SENDERIDTYPE         VARCHAR(32),
   RECEIVERIDTYPE       VARCHAR(32),
   CONTENTMODE          NUMERIC,
   constraint PK_UMS_SEND_ERROR primary key (SEQKEY)
);

comment on table UMS_SEND_ERROR is
'错误消息发送表';

comment on column UMS_SEND_ERROR.BATCHNO is
'批号(8位日期+6位序号)';

comment on column UMS_SEND_ERROR.SERIALNO is
'交易流水号';

comment on column UMS_SEND_ERROR.SEQUENCENO is
'消息序号';

comment on column UMS_SEND_ERROR.BATCHMODE is
'批量属性(0:单笔,1:批量)';

comment on column UMS_SEND_ERROR.APPSERIALNO is
'应用生成的消息序号';

comment on column UMS_SEND_ERROR.RETCODE is
'交易结果';

comment on column UMS_SEND_ERROR.ERRMSG is
'错误信息';

comment on column UMS_SEND_ERROR.STATUSFLAG is
'状态(0:正常,1:未就绪)';

comment on column UMS_SEND_ERROR.SERVICEID is
'服务号';

comment on column UMS_SEND_ERROR.SENDDIRECTLY is
'是否直接从指定渠道发送';

comment on column UMS_SEND_ERROR.MEDIAID is
'渠道标识';

comment on column UMS_SEND_ERROR.SENDID is
'发送方地址';

comment on column UMS_SEND_ERROR.SENDERTYPE is
'发送者角色(FROM：1）';

comment on column UMS_SEND_ERROR.RECVID is
'接收方地址';

comment on column UMS_SEND_ERROR.RECEIVERTYPE is
'接收者角色(TO：2，CC：3)';

comment on column UMS_SEND_ERROR.SUBMITDATE is
'提交日期';

comment on column UMS_SEND_ERROR.SUBMITTIME is
'提交时间';

comment on column UMS_SEND_ERROR.FINISHDATE is
'完成日期';

comment on column UMS_SEND_ERROR.FINISHTIME is
'完成时间';

comment on column UMS_SEND_ERROR.REP is
'当前渠道重复发生次数';

comment on column UMS_SEND_ERROR.DOCOUNT is
'累计次数';

comment on column UMS_SEND_ERROR.PRIORITY is
'优先级别';

comment on column UMS_SEND_ERROR.CONTENTSUBJECT is
'标题';

comment on column UMS_SEND_ERROR.CONTENT is
'信息内容';

comment on column UMS_SEND_ERROR.MSGID is
'消息编号';

comment on column UMS_SEND_ERROR.TIMESETFLAG is
'定时发送标志(0:非定时,1:定时)';

comment on column UMS_SEND_ERROR.SETDATE is
'定时发送日期';

comment on column UMS_SEND_ERROR.SETTIME is
'定时发送时间';

comment on column UMS_SEND_ERROR.INVALIDDATE is
'失效日期';

comment on column UMS_SEND_ERROR.INVALIDTIME is
'失效时间';

comment on column UMS_SEND_ERROR.ACK is
'回执标志';

comment on column UMS_SEND_ERROR.REPLYDESTINATION is
'回复目的地编号';

comment on column UMS_SEND_ERROR.NEEDREPLY is
'消息回复标志';

comment on column UMS_SEND_ERROR.FEESERVICENO is
'计费服务号';

comment on column UMS_SEND_ERROR.FEE is
'费用';

comment on column UMS_SEND_ERROR.FEETYPE is
'计费方式';

comment on column UMS_SEND_ERROR.UMSFLAG is
'消息所要发向UMS标志(1:自身,2:其他UMS)';

comment on column UMS_SEND_ERROR.COMPANYID is
'如果UMSFLAG为2,则表示消息将发往哪个company';

comment on column UMS_SEND_ERROR.SENDERIDTYPE is
'发送者ID类型';

comment on column UMS_SEND_ERROR.RECEIVERIDTYPE is
'接收者ID类型';

comment on column UMS_SEND_ERROR.CONTENTMODE is
'编码类型';

--==============================================================
-- Table: UMS_SEND_OK
--==============================================================
create table UMS_SEND_OK
(
   BATCHNO              VARCHAR(14)            not null,
   SERIALNO             NUMERIC(8)             not null,
   SEQUENCENO           NUMERIC                not null,
   BATCHMODE            VARCHAR(1),
   APPSERIALNO          VARCHAR(35),
   RETCODE              VARCHAR(10),
   ERRMSG               VARCHAR(60),
   STATUSFLAG           NUMERIC,
   SERVICEID            VARCHAR(20),
   SENDDIRECTLY         NUMERIC,
   MEDIAID              VARCHAR(3),
   SENDID               VARCHAR(60),
   SENDERTYPE           NUMERIC(1),
   RECVID               VARCHAR(60),
   RECEIVERTYPE         NUMERIC(1),
   SUBMITDATE           VARCHAR(8),
   SUBMITTIME           VARCHAR(6),
   FINISHDATE           VARCHAR(8),
   FINISHTIME           VARCHAR(6),
   REP                  NUMERIC,
   DOCOUNT              NUMERIC,
   PRIORITY             NUMERIC(1),
   CONTENTSUBJECT       VARCHAR(50),
   CONTENT              VARCHAR(1000),
   MSGID                VARCHAR(20),
   TIMESETFLAG          NUMERIC(1),
   SETDATE              VARCHAR(8),
   SETTIME              VARCHAR(6),
   INVALIDDATE          VARCHAR(8),
   INVALIDTIME          VARCHAR(6),
   ACK                  NUMERIC(1),
   REPLYDESTINATION     VARCHAR(60),
   NEEDREPLY            NUMERIC(1),
   FEESERVICENO         VARCHAR(20),
   FEE                  NUMERIC,
   FEETYPE              NUMERIC(1),
   UMSFLAG              NUMERIC,
   COMPANYID            VARCHAR(20),
   SEQKEY               VARCHAR(36)            not null,
   SENDERIDTYPE         VARCHAR(32),
   RECEIVERIDTYPE       VARCHAR(32),
   CONTENTMODE          NUMERIC,
   constraint PK_UMS_SEND_OK primary key (SEQKEY)
);

comment on table UMS_SEND_OK is
'外拨已发送表';

comment on column UMS_SEND_OK.BATCHNO is
'批号（8位日期+6位序号）';

comment on column UMS_SEND_OK.SERIALNO is
'交易流水号';

comment on column UMS_SEND_OK.SEQUENCENO is
'消息序号';

comment on column UMS_SEND_OK.BATCHMODE is
'批量属性（0：单笔，1批量）';

comment on column UMS_SEND_OK.APPSERIALNO is
'应用生成消息序号';

comment on column UMS_SEND_OK.RETCODE is
'交易结果';

comment on column UMS_SEND_OK.ERRMSG is
'错误信息';

comment on column UMS_SEND_OK.STATUSFLAG is
'状态（0：正常，1：未就绪）';

comment on column UMS_SEND_OK.SERVICEID is
'服务号';

comment on column UMS_SEND_OK.SENDDIRECTLY is
'是否直接从指定渠道发送';

comment on column UMS_SEND_OK.MEDIAID is
'渠道标识';

comment on column UMS_SEND_OK.SENDID is
'发送方地址';

comment on column UMS_SEND_OK.SENDERTYPE is
'发送者角色(FROM：1）';

comment on column UMS_SEND_OK.RECVID is
'接收方地址';

comment on column UMS_SEND_OK.RECEIVERTYPE is
'接收者角色(TO：2，CC：3)';

comment on column UMS_SEND_OK.SUBMITDATE is
'提交日期';

comment on column UMS_SEND_OK.SUBMITTIME is
'提交时间';

comment on column UMS_SEND_OK.FINISHDATE is
'完成日期';

comment on column UMS_SEND_OK.FINISHTIME is
'完成时间';

comment on column UMS_SEND_OK.REP is
'当前渠道重复发生次数';

comment on column UMS_SEND_OK.DOCOUNT is
'累计次数';

comment on column UMS_SEND_OK.PRIORITY is
'优先级别';

comment on column UMS_SEND_OK.CONTENTSUBJECT is
'标题';

comment on column UMS_SEND_OK.CONTENT is
'信息内容';

comment on column UMS_SEND_OK.MSGID is
'消息编号';

comment on column UMS_SEND_OK.TIMESETFLAG is
'定时发送标志(0:非定时,1:定时)';

comment on column UMS_SEND_OK.SETDATE is
'定时发送日期';

comment on column UMS_SEND_OK.SETTIME is
'定时发送时间';

comment on column UMS_SEND_OK.INVALIDDATE is
'失效日期';

comment on column UMS_SEND_OK.INVALIDTIME is
'失效时间';

comment on column UMS_SEND_OK.ACK is
'回执标志';

comment on column UMS_SEND_OK.REPLYDESTINATION is
'回复目的地编号';

comment on column UMS_SEND_OK.NEEDREPLY is
'消息回复标志';

comment on column UMS_SEND_OK.FEESERVICENO is
'计费服务号';

comment on column UMS_SEND_OK.FEE is
'费用';

comment on column UMS_SEND_OK.FEETYPE is
'计费方式';

comment on column UMS_SEND_OK.UMSFLAG is
'消息所要发向UMS标志(1:自身,2:其他UMS)';

comment on column UMS_SEND_OK.COMPANYID is
'如果UMSFLAG为2,则表示消息将发往哪个company';

comment on column UMS_SEND_OK.SEQKEY is
'内码';

comment on column UMS_SEND_OK.SENDERIDTYPE is
'发送者类型';

comment on column UMS_SEND_OK.RECEIVERIDTYPE is
'接受者类型';

comment on column UMS_SEND_OK.CONTENTMODE is
'内容编码';

--==============================================================
-- Table: UMS_SEND_READY
--==============================================================
create table UMS_SEND_READY
(
   BATCHNO              VARCHAR(14)            not null,
   SERIALNO             NUMERIC(8)             not null,
   SEQUENCENO           NUMERIC                not null,
   BATCHMODE            VARCHAR(1),
   APPSERIALNO          VARCHAR(35),
   RETCODE              VARCHAR(10),
   ERRMSG               VARCHAR(60),
   STATUSFLAG           NUMERIC,
   SERVICEID            VARCHAR(20),
   SENDDIRECTLY         NUMERIC,
   MEDIAID              VARCHAR(3),
   SENDID               VARCHAR(60),
   SENDERTYPE           NUMERIC(1),
   RECVID               VARCHAR(60),
   RECEIVERTYPE         NUMERIC(1),
   SUBMITDATE           VARCHAR(8),
   SUBMITTIME           VARCHAR(6),
   FINISHDATE           VARCHAR(8),
   FINISHTIME           VARCHAR(6),
   REP                  NUMERIC,
   DOCOUNT              NUMERIC,
   PRIORITY             NUMERIC(1),
   CONTENTSUBJECT       VARCHAR(50),
   CONTENT              VARCHAR(1000),
   MSGID                VARCHAR(20),
   TIMESETFLAG          NUMERIC(1),
   SETDATE              VARCHAR(8),
   SETTIME              VARCHAR(6),
   INVALIDDATE          VARCHAR(8),
   INVALIDTIME          VARCHAR(6),
   ACK                  NUMERIC(1),
   REPLYDESTINATION     VARCHAR(60),
   NEEDREPLY            NUMERIC(1),
   FEESERVICENO         VARCHAR(20),
   FEE                  NUMERIC,
   FEETYPE              NUMERIC(1),
   UMSFLAG              NUMERIC(1),
   COMPANYID            VARCHAR(20),
   SEQKEY               VARCHAR(36)            not null,
   SENDERIDTYPE         VARCHAR(32),
   RECEIVERIDTYPE       VARCHAR(32),
   CONTENTMODE          NUMERIC,
   constraint PK_OUT_READY_V3 primary key (SEQKEY)
);

comment on table UMS_SEND_READY is
'外拨待发送表';

comment on column UMS_SEND_READY.BATCHNO is
'批号(8位日期+6位序号)';

comment on column UMS_SEND_READY.SERIALNO is
'交易流水号';

comment on column UMS_SEND_READY.SEQUENCENO is
'消息序号';

comment on column UMS_SEND_READY.BATCHMODE is
'批量属性(0:单笔,1:批量)';

comment on column UMS_SEND_READY.APPSERIALNO is
'应用生成的消息序号';

comment on column UMS_SEND_READY.RETCODE is
'交易结果';

comment on column UMS_SEND_READY.ERRMSG is
'错误信息';

comment on column UMS_SEND_READY.STATUSFLAG is
'状态(0:正常,1:未就绪)';

comment on column UMS_SEND_READY.SERVICEID is
'服务号';

comment on column UMS_SEND_READY.SENDDIRECTLY is
'是否直接从指定渠道发送';

comment on column UMS_SEND_READY.MEDIAID is
'渠道标识';

comment on column UMS_SEND_READY.SENDID is
'发送方地址';

comment on column UMS_SEND_READY.SENDERTYPE is
'发送者角色(FROM：1）';

comment on column UMS_SEND_READY.RECVID is
'接收方地址';

comment on column UMS_SEND_READY.RECEIVERTYPE is
'接收者角色(TO：2，CC：3)';

comment on column UMS_SEND_READY.SUBMITDATE is
'提交日期';

comment on column UMS_SEND_READY.SUBMITTIME is
'提交时间';

comment on column UMS_SEND_READY.FINISHDATE is
'完成日期';

comment on column UMS_SEND_READY.FINISHTIME is
'完成时间';

comment on column UMS_SEND_READY.REP is
'当前渠道重复发生次数';

comment on column UMS_SEND_READY.DOCOUNT is
'累计次数';

comment on column UMS_SEND_READY.PRIORITY is
'优先级别';

comment on column UMS_SEND_READY.CONTENTSUBJECT is
'标题';

comment on column UMS_SEND_READY.CONTENT is
'信息内容';

comment on column UMS_SEND_READY.MSGID is
'消息编号';

comment on column UMS_SEND_READY.TIMESETFLAG is
'定时发送标志(0:非定时,1:定时)';

comment on column UMS_SEND_READY.SETDATE is
'定时发送日期';

comment on column UMS_SEND_READY.SETTIME is
'定时发送时间';

comment on column UMS_SEND_READY.INVALIDDATE is
'失效日期';

comment on column UMS_SEND_READY.INVALIDTIME is
'失效时间';

comment on column UMS_SEND_READY.ACK is
'回执标志';

comment on column UMS_SEND_READY.REPLYDESTINATION is
'回复目的地编号';

comment on column UMS_SEND_READY.NEEDREPLY is
'消息回复标志';

comment on column UMS_SEND_READY.FEESERVICENO is
'计费服务号';

comment on column UMS_SEND_READY.FEE is
'费用';

comment on column UMS_SEND_READY.FEETYPE is
'计费方式';

comment on column UMS_SEND_READY.UMSFLAG is
'消息所要发向UMS标志(1:自身,2:其他UMS)';

comment on column UMS_SEND_READY.COMPANYID is
'如果UMSFLAG为2,则表示消息将发往哪个company';

comment on column UMS_SEND_READY.SEQKEY is
'内码';

comment on column UMS_SEND_READY.SENDERIDTYPE is
'表示发送者id对应的类型';

comment on column UMS_SEND_READY.RECEIVERIDTYPE is
'接收者id对应的类型';

comment on column UMS_SEND_READY.CONTENTMODE is
'编码方式';

--==============================================================
-- Table: UMS_SERVICE
--==============================================================
create table UMS_SERVICE
(
   SEQKEY               VARCHAR(36)            not null,
   SERVICE_ID           VARCHAR(20)            not null,
   SERVICE_NAME         VARCHAR(50)            not null,
   COMP_KEY             VARCHAR(36)            not null,
   APP_KEY              VARCHAR(36)            not null,
   SERVICE_ADDR         VARCHAR(100),
   SERVICE_PORT         VARCHAR(6),
   SERVICE_STATUS       VARCHAR(50),
   SERVICE_TYPE         NUMERIC(1),
   SERVICE_DIRECTIONTYPE NUMERIC(1),
   SERVICE_DESCRIPTION  VARCHAR(1000),
   constraint SERVICE_V3_SEQKEY primary key (SEQKEY)
);

comment on table UMS_SERVICE is
'服务定义表';

comment on column UMS_SERVICE.SEQKEY is
'内码';

comment on column UMS_SERVICE.SERVICE_ID is
'服务编号';

comment on column UMS_SERVICE.SERVICE_NAME is
'服务名称';

comment on column UMS_SERVICE.COMP_KEY is
'所属单位标识';

comment on column UMS_SERVICE.APP_KEY is
'所属应用标识';

comment on column UMS_SERVICE.SERVICE_ADDR is
'服务地址';

comment on column UMS_SERVICE.SERVICE_PORT is
'服务端口';

comment on column UMS_SERVICE.SERVICE_STATUS is
'服务状态';

comment on column UMS_SERVICE.SERVICE_TYPE is
'服务提供的类型（socket，jms，webservice）';

comment on column UMS_SERVICE.SERVICE_DIRECTIONTYPE is
'服务发送方向（主动发送，被动接收）';

comment on column UMS_SERVICE.SERVICE_DESCRIPTION is
'服务描述';

--==============================================================
-- Table: UMS_TACTICS
--==============================================================
create table UMS_TACTICS
(
   SEQKEY               VARCHAR(36)            not null,
   MEDIA_KEY            VARCHAR(36)            not null,
   TACT_MEDIA_PRIORITY  VARCHAR(3)             not null,
   PERIOD_KEY           VARCHAR(36)            not null,
   SERVICE_KEY          VARCHAR(32),
   APP_KEY              VARCHAR(36),
   COMPANY_KEY          VARCHAR(36),
   TACT_TYPE            VARCHAR(3)             not null,
   PERSON_KEY           VARCHAR(36),
   constraint PK_UMS_TACTICS primary key (SEQKEY)
);

comment on table UMS_TACTICS is
'策略表';

comment on column UMS_TACTICS.SEQKEY is
'内码';

comment on column UMS_TACTICS.MEDIA_KEY is
'渠道内码';

comment on column UMS_TACTICS.TACT_MEDIA_PRIORITY is
'渠道优先级';

comment on column UMS_TACTICS.PERIOD_KEY is
'有效时间段内码';

comment on column UMS_TACTICS.SERVICE_KEY is
'服务内码';

comment on column UMS_TACTICS.APP_KEY is
'应用内码';

comment on column UMS_TACTICS.COMPANY_KEY is
'单位内码';

comment on column UMS_TACTICS.TACT_TYPE is
'策略类型(02:应用01:个人)';

comment on column UMS_TACTICS.PERSON_KEY is
'个人标识（登陆人编号）';

--==============================================================
-- Table: UMS_TRANSMIT_ERR
--==============================================================
create table UMS_TRANSMIT_ERR
(
   SEQKEY               VARCHAR(36)            not null,
   TRANS_SOURCE         VARCHAR(36),
   TRANS_TARGET         VARCHAR(36),
   TRANS_RECEIVE_TIME   VARCHAR(19),
   TRANS_SENT_TIME      VARCHAR(19),
   TRANS_CONTENT        BLOB,
   TRANS_ERR_TYPE       VARCHAR(16),
   TRANS_LENGTH         NUMERIC,
   constraint PK_UMS_TRANSMIT_ERR primary key (SEQKEY)
);

--==============================================================
-- Table: UMS_TRANSMIT_MES
--==============================================================
create table UMS_TRANSMIT_MES
(
   SEQKEY               VARCHAR(36)            not null,
   TRANS_SOURCE         VARCHAR(36),
   TRANS_TARGET         VARCHAR(36),
   TRANS_RECEIVE_TIME   VARCHAR(19),
   TRANS_SENT_TIME      VARCHAR(19),
   TRANS_CONTENT        BLOB,
   TRANS_LENGTH         NUMERIC,
   constraint PK_UMS_TRANSMIT_MES primary key (SEQKEY)
);





--==============================================================
-- DATA: UMS_MEDIA 
--==============================================================
Insert into UMS_MEDIA (SEQKEY,MEDIA_ID,MEDIA_NAME,MEDIA_CLASS,MEDIA_TYPE,MEDIA_STATUSFLAG,MEDIA_IP,MEDIA_PORT,MEDIA_TIMEOUT,MEDIA_REPEATTIMES,MEDIA_STARTWORKTIME,MEDIA_ENDWORKTIME,MEDIA_LOGINNAME,MEDIA_LOGINPASSWORD,MEDIA_SLEEPTIME,MEDIA_STYLE,MEDIA_DESCRIPTION) values ('7','025','LCS外拨','com.nci.ums.channel.outchannel.LCSOutChannel_V3',1,1,null,0,0,0,null,null,'240000',null,100,3,null);
Insert into UMS_MEDIA (SEQKEY,MEDIA_ID,MEDIA_NAME,MEDIA_CLASS,MEDIA_TYPE,MEDIA_STATUSFLAG,MEDIA_IP,MEDIA_PORT,MEDIA_TIMEOUT,MEDIA_REPEATTIMES,MEDIA_STARTWORKTIME,MEDIA_ENDWORKTIME,MEDIA_LOGINNAME,MEDIA_LOGINPASSWORD,MEDIA_SLEEPTIME,MEDIA_STYLE,MEDIA_DESCRIPTION) values ('9','015','移动SMSV3外拨','com.nci.ums.channel.outchannel.NCIOutChannel_V3',1,1,null,0,0,0,null,null,null,null,0,1,null);
Insert into UMS_MEDIA (SEQKEY,MEDIA_ID,MEDIA_NAME,MEDIA_CLASS,MEDIA_TYPE,MEDIA_STATUSFLAG,MEDIA_IP,MEDIA_PORT,MEDIA_TIMEOUT,MEDIA_REPEATTIMES,MEDIA_STARTWORKTIME,MEDIA_ENDWORKTIME,MEDIA_LOGINNAME,MEDIA_LOGINPASSWORD,MEDIA_SLEEPTIME,MEDIA_STYLE,MEDIA_DESCRIPTION) values ('10','075','NCI Email外拨','com.nci.ums.channel.outchannel.EmailOutChannel_V3',1,0,null,0,0,0,null,null,null,null,0,2,null);
Insert into UMS_MEDIA (SEQKEY,MEDIA_ID,MEDIA_NAME,MEDIA_CLASS,MEDIA_TYPE,MEDIA_STATUSFLAG,MEDIA_IP,MEDIA_PORT,MEDIA_TIMEOUT,MEDIA_REPEATTIMES,MEDIA_STARTWORKTIME,MEDIA_ENDWORKTIME,MEDIA_LOGINNAME,MEDIA_LOGINPASSWORD,MEDIA_SLEEPTIME,MEDIA_STYLE,MEDIA_DESCRIPTION) values ('11','076','NCI Email内拨','com.nci.ums.channel.inchannel.email.EmailInChannel_V3',0,1,null,0,0,0,null,null,null,null,0,2,null);

Insert into UMS_COMPANY (SEQKEY,COMP_ID,COMP_NAME,COMP_LINKMAN,COMP_EMAIL,COMP_TEL,COMP_MOBILETEL,COMP_ADDRESS,COMP_DESCRIPTIONS) values ('47','1','浙江省电力','test',null,null,null,'11222','1');
Insert into UMS_APPLICATION (SEQKEY,APPT_ID,APPT_NAME,COMP_KEY,APPT_PASSWORD,APPT_MD5PWD,APPT_STATUS,APPT_IP,APPT_TIMEOUT,APPT_SPNO,APPT_CHANNELTYPE,APPT_EMAIL,APPT_LOGINNAME,APPT_LOGINPWD,FEE_KEY,APPT_DESCRIPTIONS,APPT_UP_PORT,APPT_DOWN_PORT) values ('7','web','WEB平台管理','47',null,null,'0','127.0.0.1',100,null,1,null,null,null,null,null,null,null);
Insert into UMS_SERVICE (SEQKEY,SERVICE_ID,SERVICE_NAME,COMP_KEY,APP_KEY,SERVICE_ADDR,SERVICE_PORT,SERVICE_STATUS,SERVICE_TYPE,SERVICE_DIRECTIONTYPE,SERVICE_DESCRIPTION) values ('21','2332','3232','47','7','32',null,'0',1,0,null);
Insert into UMS_SERVICE (SEQKEY,SERVICE_ID,SERVICE_NAME,COMP_KEY,APP_KEY,SERVICE_ADDR,SERVICE_PORT,SERVICE_STATUS,SERVICE_TYPE,SERVICE_DIRECTIONTYPE,SERVICE_DESCRIPTION) values ('4','3005','测试服务','47','7','127.0.0.1','8080','0',1,1,'WEB平台管理');
