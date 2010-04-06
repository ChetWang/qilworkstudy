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
'���ڵ�λ���ŵ�ϵͳ���룫��ǰ�û�˳���롣';

comment on column UUM_USER.USER_ORG_ID is
'������ϵͳ����Ա�⣬�����û�������ϵͳ����Ա����ͨ������ָ����ֵ��';

comment on column UUM_USER.USER_LOGIN_NAME is
'����Ψһ��Լ��';

comment on column UUM_USER.USER_LOGIN_PASSWD is
'����md5����';

comment on column UUM_USER.USER_SEX is
'1:Ů��
2:����';

comment on column UUM_USER.USER_STATUS is
'1:���� �ڲ����Ƿּ������Լ��������£�����״̬���û�������ҵ������ɼ����û�����ʱ��״̬Ĭ��Ϊ����״̬��
2:���� �����û�����ʱ�ģ�����ҵ��Ӷ����ʱ��������״̬���û�ֻ��һ��ʱ������Ч�����Ҳ��ܵ�¼ϵͳ��
3:ͣ�� ������Ҫʹ�ø��û�����ʷ��Ϣ���������ݵ��û�����ͳ��ʱ���õ����û�����Ϣ��ͣ��״̬���û����ܵ�¼ϵͳ��
���ã����ã�ͣ��״̬���û����໥ת����';

comment on column UUM_USER.USER_TYPE is
'1:��ͨ�û� ���߱������û�������������Ȩ��
2:ϵͳ����Ա �߱��ּ����������������û���Ȩ��
9:����ϵͳ����Ա �ܹ������е��û������е���������';






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
'Ӧ�ö����';

comment on column UMS_APPLICATION.SEQKEY is
'����';

comment on column UMS_APPLICATION.APPT_ID is
'Ӧ�ñ�ʶ';

comment on column UMS_APPLICATION.APPT_NAME is
'Ӧ������';

comment on column UMS_APPLICATION.COMP_KEY is
'������λ��ʶ';

comment on column UMS_APPLICATION.APPT_PASSWORD is
'Ӧ��ע������';

comment on column UMS_APPLICATION.APPT_MD5PWD is
'MD5����';

comment on column UMS_APPLICATION.APPT_STATUS is
'Ӧ��״̬';

comment on column UMS_APPLICATION.APPT_IP is
'IP��ַ';

comment on column UMS_APPLICATION.APPT_TIMEOUT is
'ͨѶ��ʱ���룩';

comment on column UMS_APPLICATION.APPT_SPNO is
'�ط��ţ���9559801';

comment on column UMS_APPLICATION.APPT_CHANNELTYPE is
'����������Ӧ����';

comment on column UMS_APPLICATION.APPT_EMAIL is
'Ӧ����Ҫ����email';

comment on column UMS_APPLICATION.APPT_LOGINNAME is
'��Ϣ�õ�¼��';

comment on column UMS_APPLICATION.APPT_LOGINPWD is
'��Ϣ������';

comment on column UMS_APPLICATION.FEE_KEY is
'�Ʒѱ�ʶ';

comment on column UMS_APPLICATION.APPT_DESCRIPTIONS is
'Ӧ������';

comment on column UMS_APPLICATION.APPT_UP_PORT is
'ת�����ж˿�';

comment on column UMS_APPLICATION.APPT_DOWN_PORT is
'ת�����ж˿�';

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
'��λ��';

comment on column UMS_COMPANY.SEQKEY is
'����';

comment on column UMS_COMPANY.COMP_ID is
'��λ��ʶ';

comment on column UMS_COMPANY.COMP_NAME is
'��λ����';

comment on column UMS_COMPANY.COMP_LINKMAN is
'��ϵ��';

comment on column UMS_COMPANY.COMP_EMAIL is
'Email';

comment on column UMS_COMPANY.COMP_TEL is
'�绰';

comment on column UMS_COMPANY.COMP_MOBILETEL is
'�ֻ�����';

comment on column UMS_COMPANY.COMP_ADDRESS is
'��λ��ַ';

comment on column UMS_COMPANY.COMP_DESCRIPTIONS is
'��λ����';

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
'Ĭ�ϲ��Ա�';

comment on column UMS_DEFAULT_TACTICS.SEQKEY is
'����';

comment on column UMS_DEFAULT_TACTICS.MEDIA_KEY is
'��������';

comment on column UMS_DEFAULT_TACTICS.DEFT_PRIORITY is
'���ȼ�';

comment on column UMS_DEFAULT_TACTICS.PERIOD_KEY is
'��Чʱ�������';

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
'�����շѶ�Ӧ��';

comment on column UMS_FEE.SEQKEY is
'����';

comment on column UMS_FEE.FEE_SERVICE_NO is
'�շѷ���';

comment on column UMS_FEE.FEE_FEE is
'����';

comment on column UMS_FEE.FEE_TYPE is
'�Ʒѷ�ʽ';

comment on column UMS_FEE.FEE_TERMINAL is
'���Ѷ���';

comment on column UMS_FEE.FEE_DESCRIPTION is
'��ע';

comment on column UMS_FEE.FEE_NAME is
'��������';

comment on column UMS_FEE.FEE_STYLE is
'�Ʒ����ͣ�1-��2-��С��';

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
'��Ϣ���˱�';

comment on column UMS_FILTER.SEQKEY is
'����';

comment on column UMS_FILTER.FILTER_CONTENT is
'����';

comment on column UMS_FILTER.FILTER_CREATE_USERID is
'������';

comment on column UMS_FILTER.FILTER_CREATE_DATE is
'����ʱ��';

comment on column UMS_FILTER.FILTER_STATUSFLAG is
'ʹ��״̬';

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
'��Ϣת����';

comment on column UMS_FORWARD_SERVICE.SEQKEY is
'����';

comment on column UMS_FORWARD_SERVICE.APP_KEY is
'Ӧ������';

comment on column UMS_FORWARD_SERVICE.COMPANY_KEY is
'��λ����';

comment on column UMS_FORWARD_SERVICE.SERVICE_KEY is
'��������';

comment on column UMS_FORWARD_SERVICE.FORWARD_CONTENT is
'ת������';

comment on column UMS_FORWARD_SERVICE.FORWARD_CREATE_USERID is
'������';

comment on column UMS_FORWARD_SERVICE.FORWARD_CREATE_DATE is
'����ʱ��';

comment on column UMS_FORWARD_SERVICE.FORWARD_STATUSFLAG is
'ʹ��״̬';

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
'����Ϣ��';

comment on column UMS_GROUP.SEQKEY is
'����';

comment on column UMS_GROUP.UMS_GROUP_NAME is
'������';

comment on column UMS_GROUP.CREATE_USERID is
'������Ա���';

comment on column UMS_GROUP.CREATE_DATE is
'��������';

comment on column UMS_GROUP.UMS_GROUP_SUM is
'��Ա����';

comment on column UMS_GROUP.UMS_GROUP_SHORT is
'����';

comment on column UMS_GROUP.UMS_GROUP_TYPE is
'������ 1-����2-˽��';

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
'����Ա��Ϣ��';

comment on column UMS_GROUP_USER.SEQKEY is
'����';

comment on column UMS_GROUP_USER.USERID is
'����Ա���';

comment on column UMS_GROUP_USER.UMS_GROUP_ID is
'���';

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
'���������';

comment on column UMS_MEDIA.SEQKEY is
'����';

comment on column UMS_MEDIA.MEDIA_ID is
'������ʶ';

comment on column UMS_MEDIA.MEDIA_NAME is
'��������';

comment on column UMS_MEDIA.MEDIA_CLASS is
'��';

comment on column UMS_MEDIA.MEDIA_TYPE is
'����/�γ���־��0�����룬1���γ���';

comment on column UMS_MEDIA.MEDIA_STATUSFLAG is
'״̬��0��������1��ֹͣ��';

comment on column UMS_MEDIA.MEDIA_IP is
'IP��ַ';

comment on column UMS_MEDIA.MEDIA_PORT is
'IP�˿�';

comment on column UMS_MEDIA.MEDIA_TIMEOUT is
'ͨѶ��ʱ���룩';

comment on column UMS_MEDIA.MEDIA_REPEATTIMES is
'����ط�����';

comment on column UMS_MEDIA.MEDIA_STARTWORKTIME is
'��ʼ����ʱ��';

comment on column UMS_MEDIA.MEDIA_ENDWORKTIME is
'��ֹ����ʱ��';

comment on column UMS_MEDIA.MEDIA_LOGINNAME is
'�û���';

comment on column UMS_MEDIA.MEDIA_LOGINPASSWORD is
'����';

comment on column UMS_MEDIA.MEDIA_SLEEPTIME is
'��ʱ';

comment on column UMS_MEDIA.MEDIA_DESCRIPTION is
'��ע';

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
'��Ϣ���ı�';

comment on column UMS_MESSAGE_SUBSCRIBE.SEQKEY is
'����';

comment on column UMS_MESSAGE_SUBSCRIBE.COMPANY_KEY is
'��λ����';

comment on column UMS_MESSAGE_SUBSCRIBE.APP_KEY is
'Ӧ������';

comment on column UMS_MESSAGE_SUBSCRIBE.SERVICE_KEY is
'��������';

comment on column UMS_MESSAGE_SUBSCRIBE.PERSON_KEY is
'��Ա����';

comment on column UMS_MESSAGE_SUBSCRIBE.SUBSTIME is
'����ʱ��';

comment on column UMS_MESSAGE_SUBSCRIBE.SUBSTATUS is
'����״̬';

comment on column UMS_MESSAGE_SUBSCRIBE.UNSUBSTIME is
'�˶�ʱ��';

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
'��Чʱ��α�';

comment on column UMS_PERIOD.SEQKEY is
'����';

comment on column UMS_PERIOD.PERD_START is
'��ʼ����';

comment on column UMS_PERIOD.PERD_END is
'��������';

comment on column UMS_PERIOD.PERD_STIME is
'��ʼʱ��';

comment on column UMS_PERIOD.PERD_ETIME is
'����ʱ��';

comment on column UMS_PERIOD.PERD_NAME is
'����';

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
'����';

comment on column UMS_QFXX.SERVICEID is
'��������';

comment on column UMS_QFXX.SENDDIRECTLY is
'�Ƿ�ֱ�ӷ���';

comment on column UMS_QFXX.MEDIAID is
'��������';

comment on column UMS_QFXX.SUBMITDATE is
'�ύ����';

comment on column UMS_QFXX.SUBMITTIME is
'�ύʱ��';

comment on column UMS_QFXX.PRIORITY is
'���ȼ�';

comment on column UMS_QFXX.ACK is
'��ִ��־';

comment on column UMS_QFXX.TIMESETFLAG is
'��ʱ���ͱ�־';

comment on column UMS_QFXX.INVALIDDATE is
'ʧЧ����';

comment on column UMS_QFXX.INVALIDTIME is
'ʧЧʱ��';

comment on column UMS_QFXX.REPLYDESTINATION is
'�ظ�Ŀ�ĵر��';

comment on column UMS_QFXX.NEEDREPLY is
'��Ϣ�ظ����';

comment on column UMS_QFXX.FEESERVICENO is
'�շ�����';

comment on column UMS_QFXX.UMSFLAG is
'UMS��ʶ';

comment on column UMS_QFXX.COMPANYID is
'��λ����';

comment on column UMS_QFXX.SEQKEY is
'����';

comment on column UMS_QFXX.APPSERIALNO is
'Ӧ������';

comment on column UMS_QFXX.DSDATE is
'��ʱ����';

comment on column UMS_QFXX.DSHH is
'ʱ';

comment on column UMS_QFXX.DSMM is
'��';

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
'��Ϣ�ʼ�������';

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
'�����ѽ��ձ�';

comment on column UMS_RECEIVE_OK.BATCHNO is
'���ţ�8λ����+6λ��ţ�';

comment on column UMS_RECEIVE_OK.SERIALNO is
'������ˮ��';

comment on column UMS_RECEIVE_OK.SEQUENCENO is
'��Ϣ���';

comment on column UMS_RECEIVE_OK.RETCODE is
'���׽��';

comment on column UMS_RECEIVE_OK.ERRMSG is
'������Ϣ';

comment on column UMS_RECEIVE_OK.STATUSFLAG is
'״̬��0��������1��δ������';

comment on column UMS_RECEIVE_OK.SERVICEID is
'Ӧ�ñ�ʶ';

comment on column UMS_RECEIVE_OK.APPSERIALNO is
'Ӧ��������Ϣ���';

comment on column UMS_RECEIVE_OK.MEDIAID is
'����������';

comment on column UMS_RECEIVE_OK.SENDID is
'���ͷ���ַ';

comment on column UMS_RECEIVE_OK.RECVID is
'���շ���ַ';

comment on column UMS_RECEIVE_OK.SUBMITDATE is
'�ύ����';

comment on column UMS_RECEIVE_OK.SUBMITTIME is
'�ύʱ��';

comment on column UMS_RECEIVE_OK.FINISHDATE is
'�������';

comment on column UMS_RECEIVE_OK.FINISHTIME is
'���ʱ��';

comment on column UMS_RECEIVE_OK.CONTENTSUBJECT is
'��Ϣ����';

comment on column UMS_RECEIVE_OK.CONTENT is
'��Ϣ����';

comment on column UMS_RECEIVE_OK.MSGID is
'��Ϣ���';

comment on column UMS_RECEIVE_OK.ACK is
'��ִ��־';

comment on column UMS_RECEIVE_OK.REPLYNO is
'�ظ����';

comment on column UMS_RECEIVE_OK.DOCOUNT is
'���ʹ���';

comment on column UMS_RECEIVE_OK.MSGTYPE is
'��Ϣ����';

comment on column UMS_RECEIVE_OK.SEQKEY is
'����';

comment on column UMS_RECEIVE_OK.SENDERIDTYPE is
'������ID����';

comment on column UMS_RECEIVE_OK.RECEIVERIDTYPE is
'������ID����';

comment on column UMS_RECEIVE_OK.CONTENTMODE is
'���ݱ���';

comment on column UMS_RECEIVE_OK.BATCHMODE is
'�������ԣ�0�����ʣ�1������';

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
'��������ձ�';

comment on column UMS_RECEIVE_READY.BATCHNO is
'���ţ�8λ����+6λ��ţ�';

comment on column UMS_RECEIVE_READY.SERIALNO is
'������ˮ��';

comment on column UMS_RECEIVE_READY.SEQUENCENO is
'��Ϣ���';

comment on column UMS_RECEIVE_READY.RETCODE is
'���׽��';

comment on column UMS_RECEIVE_READY.ERRMSG is
'������Ϣ';

comment on column UMS_RECEIVE_READY.STATUSFLAG is
'״̬��0��������1��δ������';

comment on column UMS_RECEIVE_READY.SERVICEID is
'��Ҫ������Ϣ�ķ���ID';

comment on column UMS_RECEIVE_READY.APPSERIALNO is
'Ӧ��ϵͳ���ɵ���Ϣ���';

comment on column UMS_RECEIVE_READY.MEDIAID is
'����������ʶ';

comment on column UMS_RECEIVE_READY.SENDID is
'���ͷ���ַ';

comment on column UMS_RECEIVE_READY.RECVID is
'���շ���ַ';

comment on column UMS_RECEIVE_READY.SUBMITDATE is
'�ύ����';

comment on column UMS_RECEIVE_READY.SUBMITTIME is
'�ύʱ��';

comment on column UMS_RECEIVE_READY.FINISHDATE is
'�������';

comment on column UMS_RECEIVE_READY.FINISHTIME is
'���ʱ��';

comment on column UMS_RECEIVE_READY.CONTENTSUBJECT is
'��Ϣ����';

comment on column UMS_RECEIVE_READY.CONTENT is
'��Ϣ����';

comment on column UMS_RECEIVE_READY.MSGID is
'��Ϣ���';

comment on column UMS_RECEIVE_READY.ACK is
'��ִ��־';

comment on column UMS_RECEIVE_READY.DOCOUNT is
'���ʹ���';

comment on column UMS_RECEIVE_READY.MSGTYPE is
'��Ϣ����';

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
'�ظ���';

comment on column UMS_REPLY.BATCHNO is
'����(8λ����+6λ���)';

comment on column UMS_REPLY.SERIALNO is
'������ˮ��';

comment on column UMS_REPLY.SEQUENCENO is
'��Ϣ���';

comment on column UMS_REPLY.BATCHMODE is
'��������(0:����,1:����)';

comment on column UMS_REPLY.APPSERIALNO is
'Ӧ�����ɵ���Ϣ���';

comment on column UMS_REPLY.RETCODE is
'���׽��';

comment on column UMS_REPLY.ERRMSG is
'������Ϣ';

comment on column UMS_REPLY.STATUSFLAG is
'״̬(0:����,1:δ����)';

comment on column UMS_REPLY.SERVICEID is
'�����';

comment on column UMS_REPLY.SENDDIRECTLY is
'�Ƿ�ֱ�Ӵ�ָ����������';

comment on column UMS_REPLY.MEDIAID is
'������ʶ';

comment on column UMS_REPLY.SENDID is
'���ͷ���ַ';

comment on column UMS_REPLY.SENDERTYPE is
'�����߽�ɫ(FROM��1��';

comment on column UMS_REPLY.RECVID is
'���շ���ַ';

comment on column UMS_REPLY.RECEIVERTYPE is
'�����߽�ɫ(TO��2��CC��3)';

comment on column UMS_REPLY.SUBMITDATE is
'�ύ����';

comment on column UMS_REPLY.SUBMITTIME is
'�ύʱ��';

comment on column UMS_REPLY.FINISHDATE is
'�������';

comment on column UMS_REPLY.FINISHTIME is
'���ʱ��';

comment on column UMS_REPLY.REP is
'��ǰ�����ظ���������';

comment on column UMS_REPLY.DOCOUNT is
'�ۼƴ���';

comment on column UMS_REPLY.PRIORITY is
'���ȼ���';

comment on column UMS_REPLY.CONTENTSUBJECT is
'����';

comment on column UMS_REPLY.CONTENT is
'��Ϣ����';

comment on column UMS_REPLY.MSGID is
'��Ϣ���';

comment on column UMS_REPLY.TIMESETFLAG is
'��ʱ���ͱ�־(0:�Ƕ�ʱ,1:��ʱ)';

comment on column UMS_REPLY.SETDATE is
'��ʱ��������';

comment on column UMS_REPLY.SETTIME is
'��ʱ����ʱ��';

comment on column UMS_REPLY.INVALIDDATE is
'ʧЧ����';

comment on column UMS_REPLY.INVALIDTIME is
'ʧЧʱ��';

comment on column UMS_REPLY.ACK is
'��ִ��־';

comment on column UMS_REPLY.REPLYDESTINATION is
'�ظ�Ŀ�ĵر��';

comment on column UMS_REPLY.NEEDREPLY is
'��Ϣ�ظ���־';

comment on column UMS_REPLY.FEESERVICENO is
'�Ʒѷ����';

comment on column UMS_REPLY.FEE is
'����';

comment on column UMS_REPLY.FEETYPE is
'�Ʒѷ�ʽ';

comment on column UMS_REPLY.UMSFLAG is
'��Ϣ��Ҫ����UMS��־(1:����,2:����UMS)';

comment on column UMS_REPLY.COMPANYID is
'���UMSFLAGΪ2,���ʾ��Ϣ�������ĸ�company';

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
'������Ϣ���ͱ�';

comment on column UMS_SEND_ERROR.BATCHNO is
'����(8λ����+6λ���)';

comment on column UMS_SEND_ERROR.SERIALNO is
'������ˮ��';

comment on column UMS_SEND_ERROR.SEQUENCENO is
'��Ϣ���';

comment on column UMS_SEND_ERROR.BATCHMODE is
'��������(0:����,1:����)';

comment on column UMS_SEND_ERROR.APPSERIALNO is
'Ӧ�����ɵ���Ϣ���';

comment on column UMS_SEND_ERROR.RETCODE is
'���׽��';

comment on column UMS_SEND_ERROR.ERRMSG is
'������Ϣ';

comment on column UMS_SEND_ERROR.STATUSFLAG is
'״̬(0:����,1:δ����)';

comment on column UMS_SEND_ERROR.SERVICEID is
'�����';

comment on column UMS_SEND_ERROR.SENDDIRECTLY is
'�Ƿ�ֱ�Ӵ�ָ����������';

comment on column UMS_SEND_ERROR.MEDIAID is
'������ʶ';

comment on column UMS_SEND_ERROR.SENDID is
'���ͷ���ַ';

comment on column UMS_SEND_ERROR.SENDERTYPE is
'�����߽�ɫ(FROM��1��';

comment on column UMS_SEND_ERROR.RECVID is
'���շ���ַ';

comment on column UMS_SEND_ERROR.RECEIVERTYPE is
'�����߽�ɫ(TO��2��CC��3)';

comment on column UMS_SEND_ERROR.SUBMITDATE is
'�ύ����';

comment on column UMS_SEND_ERROR.SUBMITTIME is
'�ύʱ��';

comment on column UMS_SEND_ERROR.FINISHDATE is
'�������';

comment on column UMS_SEND_ERROR.FINISHTIME is
'���ʱ��';

comment on column UMS_SEND_ERROR.REP is
'��ǰ�����ظ���������';

comment on column UMS_SEND_ERROR.DOCOUNT is
'�ۼƴ���';

comment on column UMS_SEND_ERROR.PRIORITY is
'���ȼ���';

comment on column UMS_SEND_ERROR.CONTENTSUBJECT is
'����';

comment on column UMS_SEND_ERROR.CONTENT is
'��Ϣ����';

comment on column UMS_SEND_ERROR.MSGID is
'��Ϣ���';

comment on column UMS_SEND_ERROR.TIMESETFLAG is
'��ʱ���ͱ�־(0:�Ƕ�ʱ,1:��ʱ)';

comment on column UMS_SEND_ERROR.SETDATE is
'��ʱ��������';

comment on column UMS_SEND_ERROR.SETTIME is
'��ʱ����ʱ��';

comment on column UMS_SEND_ERROR.INVALIDDATE is
'ʧЧ����';

comment on column UMS_SEND_ERROR.INVALIDTIME is
'ʧЧʱ��';

comment on column UMS_SEND_ERROR.ACK is
'��ִ��־';

comment on column UMS_SEND_ERROR.REPLYDESTINATION is
'�ظ�Ŀ�ĵر��';

comment on column UMS_SEND_ERROR.NEEDREPLY is
'��Ϣ�ظ���־';

comment on column UMS_SEND_ERROR.FEESERVICENO is
'�Ʒѷ����';

comment on column UMS_SEND_ERROR.FEE is
'����';

comment on column UMS_SEND_ERROR.FEETYPE is
'�Ʒѷ�ʽ';

comment on column UMS_SEND_ERROR.UMSFLAG is
'��Ϣ��Ҫ����UMS��־(1:����,2:����UMS)';

comment on column UMS_SEND_ERROR.COMPANYID is
'���UMSFLAGΪ2,���ʾ��Ϣ�������ĸ�company';

comment on column UMS_SEND_ERROR.SENDERIDTYPE is
'������ID����';

comment on column UMS_SEND_ERROR.RECEIVERIDTYPE is
'������ID����';

comment on column UMS_SEND_ERROR.CONTENTMODE is
'��������';

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
'�Ⲧ�ѷ��ͱ�';

comment on column UMS_SEND_OK.BATCHNO is
'���ţ�8λ����+6λ��ţ�';

comment on column UMS_SEND_OK.SERIALNO is
'������ˮ��';

comment on column UMS_SEND_OK.SEQUENCENO is
'��Ϣ���';

comment on column UMS_SEND_OK.BATCHMODE is
'�������ԣ�0�����ʣ�1������';

comment on column UMS_SEND_OK.APPSERIALNO is
'Ӧ��������Ϣ���';

comment on column UMS_SEND_OK.RETCODE is
'���׽��';

comment on column UMS_SEND_OK.ERRMSG is
'������Ϣ';

comment on column UMS_SEND_OK.STATUSFLAG is
'״̬��0��������1��δ������';

comment on column UMS_SEND_OK.SERVICEID is
'�����';

comment on column UMS_SEND_OK.SENDDIRECTLY is
'�Ƿ�ֱ�Ӵ�ָ����������';

comment on column UMS_SEND_OK.MEDIAID is
'������ʶ';

comment on column UMS_SEND_OK.SENDID is
'���ͷ���ַ';

comment on column UMS_SEND_OK.SENDERTYPE is
'�����߽�ɫ(FROM��1��';

comment on column UMS_SEND_OK.RECVID is
'���շ���ַ';

comment on column UMS_SEND_OK.RECEIVERTYPE is
'�����߽�ɫ(TO��2��CC��3)';

comment on column UMS_SEND_OK.SUBMITDATE is
'�ύ����';

comment on column UMS_SEND_OK.SUBMITTIME is
'�ύʱ��';

comment on column UMS_SEND_OK.FINISHDATE is
'�������';

comment on column UMS_SEND_OK.FINISHTIME is
'���ʱ��';

comment on column UMS_SEND_OK.REP is
'��ǰ�����ظ���������';

comment on column UMS_SEND_OK.DOCOUNT is
'�ۼƴ���';

comment on column UMS_SEND_OK.PRIORITY is
'���ȼ���';

comment on column UMS_SEND_OK.CONTENTSUBJECT is
'����';

comment on column UMS_SEND_OK.CONTENT is
'��Ϣ����';

comment on column UMS_SEND_OK.MSGID is
'��Ϣ���';

comment on column UMS_SEND_OK.TIMESETFLAG is
'��ʱ���ͱ�־(0:�Ƕ�ʱ,1:��ʱ)';

comment on column UMS_SEND_OK.SETDATE is
'��ʱ��������';

comment on column UMS_SEND_OK.SETTIME is
'��ʱ����ʱ��';

comment on column UMS_SEND_OK.INVALIDDATE is
'ʧЧ����';

comment on column UMS_SEND_OK.INVALIDTIME is
'ʧЧʱ��';

comment on column UMS_SEND_OK.ACK is
'��ִ��־';

comment on column UMS_SEND_OK.REPLYDESTINATION is
'�ظ�Ŀ�ĵر��';

comment on column UMS_SEND_OK.NEEDREPLY is
'��Ϣ�ظ���־';

comment on column UMS_SEND_OK.FEESERVICENO is
'�Ʒѷ����';

comment on column UMS_SEND_OK.FEE is
'����';

comment on column UMS_SEND_OK.FEETYPE is
'�Ʒѷ�ʽ';

comment on column UMS_SEND_OK.UMSFLAG is
'��Ϣ��Ҫ����UMS��־(1:����,2:����UMS)';

comment on column UMS_SEND_OK.COMPANYID is
'���UMSFLAGΪ2,���ʾ��Ϣ�������ĸ�company';

comment on column UMS_SEND_OK.SEQKEY is
'����';

comment on column UMS_SEND_OK.SENDERIDTYPE is
'����������';

comment on column UMS_SEND_OK.RECEIVERIDTYPE is
'����������';

comment on column UMS_SEND_OK.CONTENTMODE is
'���ݱ���';

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
'�Ⲧ�����ͱ�';

comment on column UMS_SEND_READY.BATCHNO is
'����(8λ����+6λ���)';

comment on column UMS_SEND_READY.SERIALNO is
'������ˮ��';

comment on column UMS_SEND_READY.SEQUENCENO is
'��Ϣ���';

comment on column UMS_SEND_READY.BATCHMODE is
'��������(0:����,1:����)';

comment on column UMS_SEND_READY.APPSERIALNO is
'Ӧ�����ɵ���Ϣ���';

comment on column UMS_SEND_READY.RETCODE is
'���׽��';

comment on column UMS_SEND_READY.ERRMSG is
'������Ϣ';

comment on column UMS_SEND_READY.STATUSFLAG is
'״̬(0:����,1:δ����)';

comment on column UMS_SEND_READY.SERVICEID is
'�����';

comment on column UMS_SEND_READY.SENDDIRECTLY is
'�Ƿ�ֱ�Ӵ�ָ����������';

comment on column UMS_SEND_READY.MEDIAID is
'������ʶ';

comment on column UMS_SEND_READY.SENDID is
'���ͷ���ַ';

comment on column UMS_SEND_READY.SENDERTYPE is
'�����߽�ɫ(FROM��1��';

comment on column UMS_SEND_READY.RECVID is
'���շ���ַ';

comment on column UMS_SEND_READY.RECEIVERTYPE is
'�����߽�ɫ(TO��2��CC��3)';

comment on column UMS_SEND_READY.SUBMITDATE is
'�ύ����';

comment on column UMS_SEND_READY.SUBMITTIME is
'�ύʱ��';

comment on column UMS_SEND_READY.FINISHDATE is
'�������';

comment on column UMS_SEND_READY.FINISHTIME is
'���ʱ��';

comment on column UMS_SEND_READY.REP is
'��ǰ�����ظ���������';

comment on column UMS_SEND_READY.DOCOUNT is
'�ۼƴ���';

comment on column UMS_SEND_READY.PRIORITY is
'���ȼ���';

comment on column UMS_SEND_READY.CONTENTSUBJECT is
'����';

comment on column UMS_SEND_READY.CONTENT is
'��Ϣ����';

comment on column UMS_SEND_READY.MSGID is
'��Ϣ���';

comment on column UMS_SEND_READY.TIMESETFLAG is
'��ʱ���ͱ�־(0:�Ƕ�ʱ,1:��ʱ)';

comment on column UMS_SEND_READY.SETDATE is
'��ʱ��������';

comment on column UMS_SEND_READY.SETTIME is
'��ʱ����ʱ��';

comment on column UMS_SEND_READY.INVALIDDATE is
'ʧЧ����';

comment on column UMS_SEND_READY.INVALIDTIME is
'ʧЧʱ��';

comment on column UMS_SEND_READY.ACK is
'��ִ��־';

comment on column UMS_SEND_READY.REPLYDESTINATION is
'�ظ�Ŀ�ĵر��';

comment on column UMS_SEND_READY.NEEDREPLY is
'��Ϣ�ظ���־';

comment on column UMS_SEND_READY.FEESERVICENO is
'�Ʒѷ����';

comment on column UMS_SEND_READY.FEE is
'����';

comment on column UMS_SEND_READY.FEETYPE is
'�Ʒѷ�ʽ';

comment on column UMS_SEND_READY.UMSFLAG is
'��Ϣ��Ҫ����UMS��־(1:����,2:����UMS)';

comment on column UMS_SEND_READY.COMPANYID is
'���UMSFLAGΪ2,���ʾ��Ϣ�������ĸ�company';

comment on column UMS_SEND_READY.SEQKEY is
'����';

comment on column UMS_SEND_READY.SENDERIDTYPE is
'��ʾ������id��Ӧ������';

comment on column UMS_SEND_READY.RECEIVERIDTYPE is
'������id��Ӧ������';

comment on column UMS_SEND_READY.CONTENTMODE is
'���뷽ʽ';

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
'�������';

comment on column UMS_SERVICE.SEQKEY is
'����';

comment on column UMS_SERVICE.SERVICE_ID is
'������';

comment on column UMS_SERVICE.SERVICE_NAME is
'��������';

comment on column UMS_SERVICE.COMP_KEY is
'������λ��ʶ';

comment on column UMS_SERVICE.APP_KEY is
'����Ӧ�ñ�ʶ';

comment on column UMS_SERVICE.SERVICE_ADDR is
'�����ַ';

comment on column UMS_SERVICE.SERVICE_PORT is
'����˿�';

comment on column UMS_SERVICE.SERVICE_STATUS is
'����״̬';

comment on column UMS_SERVICE.SERVICE_TYPE is
'�����ṩ�����ͣ�socket��jms��webservice��';

comment on column UMS_SERVICE.SERVICE_DIRECTIONTYPE is
'�����ͷ����������ͣ��������գ�';

comment on column UMS_SERVICE.SERVICE_DESCRIPTION is
'��������';

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
'���Ա�';

comment on column UMS_TACTICS.SEQKEY is
'����';

comment on column UMS_TACTICS.MEDIA_KEY is
'��������';

comment on column UMS_TACTICS.TACT_MEDIA_PRIORITY is
'�������ȼ�';

comment on column UMS_TACTICS.PERIOD_KEY is
'��Чʱ�������';

comment on column UMS_TACTICS.SERVICE_KEY is
'��������';

comment on column UMS_TACTICS.APP_KEY is
'Ӧ������';

comment on column UMS_TACTICS.COMPANY_KEY is
'��λ����';

comment on column UMS_TACTICS.TACT_TYPE is
'��������(02:Ӧ��01:����)';

comment on column UMS_TACTICS.PERSON_KEY is
'���˱�ʶ����½�˱�ţ�';

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
Insert into UMS_MEDIA (SEQKEY,MEDIA_ID,MEDIA_NAME,MEDIA_CLASS,MEDIA_TYPE,MEDIA_STATUSFLAG,MEDIA_IP,MEDIA_PORT,MEDIA_TIMEOUT,MEDIA_REPEATTIMES,MEDIA_STARTWORKTIME,MEDIA_ENDWORKTIME,MEDIA_LOGINNAME,MEDIA_LOGINPASSWORD,MEDIA_SLEEPTIME,MEDIA_STYLE,MEDIA_DESCRIPTION) values ('7','025','LCS�Ⲧ','com.nci.ums.channel.outchannel.LCSOutChannel_V3',1,1,null,0,0,0,null,null,'240000',null,100,3,null);
Insert into UMS_MEDIA (SEQKEY,MEDIA_ID,MEDIA_NAME,MEDIA_CLASS,MEDIA_TYPE,MEDIA_STATUSFLAG,MEDIA_IP,MEDIA_PORT,MEDIA_TIMEOUT,MEDIA_REPEATTIMES,MEDIA_STARTWORKTIME,MEDIA_ENDWORKTIME,MEDIA_LOGINNAME,MEDIA_LOGINPASSWORD,MEDIA_SLEEPTIME,MEDIA_STYLE,MEDIA_DESCRIPTION) values ('9','015','�ƶ�SMSV3�Ⲧ','com.nci.ums.channel.outchannel.NCIOutChannel_V3',1,1,null,0,0,0,null,null,null,null,0,1,null);
Insert into UMS_MEDIA (SEQKEY,MEDIA_ID,MEDIA_NAME,MEDIA_CLASS,MEDIA_TYPE,MEDIA_STATUSFLAG,MEDIA_IP,MEDIA_PORT,MEDIA_TIMEOUT,MEDIA_REPEATTIMES,MEDIA_STARTWORKTIME,MEDIA_ENDWORKTIME,MEDIA_LOGINNAME,MEDIA_LOGINPASSWORD,MEDIA_SLEEPTIME,MEDIA_STYLE,MEDIA_DESCRIPTION) values ('10','075','NCI Email�Ⲧ','com.nci.ums.channel.outchannel.EmailOutChannel_V3',1,0,null,0,0,0,null,null,null,null,0,2,null);
Insert into UMS_MEDIA (SEQKEY,MEDIA_ID,MEDIA_NAME,MEDIA_CLASS,MEDIA_TYPE,MEDIA_STATUSFLAG,MEDIA_IP,MEDIA_PORT,MEDIA_TIMEOUT,MEDIA_REPEATTIMES,MEDIA_STARTWORKTIME,MEDIA_ENDWORKTIME,MEDIA_LOGINNAME,MEDIA_LOGINPASSWORD,MEDIA_SLEEPTIME,MEDIA_STYLE,MEDIA_DESCRIPTION) values ('11','076','NCI Email�ڲ�','com.nci.ums.channel.inchannel.email.EmailInChannel_V3',0,1,null,0,0,0,null,null,null,null,0,2,null);

Insert into UMS_COMPANY (SEQKEY,COMP_ID,COMP_NAME,COMP_LINKMAN,COMP_EMAIL,COMP_TEL,COMP_MOBILETEL,COMP_ADDRESS,COMP_DESCRIPTIONS) values ('47','1','�㽭ʡ����','test',null,null,null,'11222','1');
Insert into UMS_APPLICATION (SEQKEY,APPT_ID,APPT_NAME,COMP_KEY,APPT_PASSWORD,APPT_MD5PWD,APPT_STATUS,APPT_IP,APPT_TIMEOUT,APPT_SPNO,APPT_CHANNELTYPE,APPT_EMAIL,APPT_LOGINNAME,APPT_LOGINPWD,FEE_KEY,APPT_DESCRIPTIONS,APPT_UP_PORT,APPT_DOWN_PORT) values ('7','web','WEBƽ̨����','47',null,null,'0','127.0.0.1',100,null,1,null,null,null,null,null,null,null);
Insert into UMS_SERVICE (SEQKEY,SERVICE_ID,SERVICE_NAME,COMP_KEY,APP_KEY,SERVICE_ADDR,SERVICE_PORT,SERVICE_STATUS,SERVICE_TYPE,SERVICE_DIRECTIONTYPE,SERVICE_DESCRIPTION) values ('21','2332','3232','47','7','32',null,'0',1,0,null);
Insert into UMS_SERVICE (SEQKEY,SERVICE_ID,SERVICE_NAME,COMP_KEY,APP_KEY,SERVICE_ADDR,SERVICE_PORT,SERVICE_STATUS,SERVICE_TYPE,SERVICE_DIRECTIONTYPE,SERVICE_DESCRIPTION) values ('4','3005','���Է���','47','7','127.0.0.1','8080','0',1,1,'WEBƽ̨����');
