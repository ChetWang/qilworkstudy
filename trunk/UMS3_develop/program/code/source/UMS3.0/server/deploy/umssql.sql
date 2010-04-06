--------------------------------------------------
-- Export file for user UMS3                    --
-- Created by wu00000bing on 2008-1-16, 8:40:46 --
--------------------------------------------------

spool umsbasesq.log

prompt
prompt Creating table APPCHANNELPOLICY_V3
prompt ==================================
prompt
create table APPCHANNELPOLICY_V3
(
  APPID    VARCHAR2(10),
  MEDIAID  VARCHAR2(3),
  PRIORITY NUMBER(1),
  PROP1    VARCHAR2(50),
  PROP2    VARCHAR2(50),
  PROP3    VARCHAR2(50),
  PROP4    VARCHAR2(50),
  SEQKEY   VARCHAR2(32)
)
;

prompt
prompt Creating table APPLICATION
prompt ==========================
prompt
create table APPLICATION
(
  COMPANYID   VARCHAR2(32) not null,
  APPID       VARCHAR2(32) default '' not null,
  APPNAME     VARCHAR2(40),
  PASSWORD    VARCHAR2(20),
  MD5PWD      VARCHAR2(50),
  STATUS      VARCHAR2(32),
  IP          VARCHAR2(20),
  PORT        NUMBER(11),
  TIMEOUT     NUMBER(11),
  SPNO        VARCHAR2(11),
  CHANNELTYPE NUMBER(32) default 0 not null,
  EMAIL       VARCHAR2(40),
  OBJECT      VARCHAR2(30),
  LOGINNAME   VARCHAR2(20),
  LOGINPWD    VARCHAR2(20),
  FEE         NUMBER(11) default 0,
  FEETYPE     NUMBER(10) default 0,
  SEQKEY      VARCHAR2(32) not null
)
;
alter table APPLICATION
  add constraint APPLICATION_PRIMARY_1 primary key (COMPANYID, APPID, SEQKEY);

prompt
prompt Creating table CD_EDUCATION
prompt ===========================
prompt
create table CD_EDUCATION
(
  CODE_VALUE CHAR(2) not null,
  CODE_NAME  VARCHAR2(100) not null
)
;
alter table CD_EDUCATION
  add constraint PK_CD_EDUCATION_1 primary key (CODE_VALUE);

prompt
prompt Creating table COMPANY
prompt ======================
prompt
create table COMPANY
(
  COMPANYID   VARCHAR2(4) default '' not null,
  COMPANYNAME VARCHAR2(20),
  LINKMAN     VARCHAR2(20),
  EMAIL       VARCHAR2(30),
  TEL         VARCHAR2(20),
  MOBILETEL   VARCHAR2(20),
  ADDRESS     VARCHAR2(50),
  SEQKEY      VARCHAR2(32) not null
)
;
alter table COMPANY
  add constraint COMPANY_SEQKEY primary key (SEQKEY);

prompt
prompt Creating table FEE_V3
prompt =====================
prompt
create table FEE_V3
(
  FEESERVICENO VARCHAR2(20) not null,
  FEE          NUMBER,
  FEETYPE      NUMBER(1) not null,
  SEQKEY       VARCHAR2(32) not null,
  FEEDESC      VARCHAR2(200) not null,
  FEETERMINAL  VARCHAR2(32)
)
;
alter table FEE_V3
  add constraint PK_FEE_V3 primary key (FEESERVICENO);

prompt
prompt Creating table FILTER
prompt =====================
prompt
create table FILTER
(
  ID         NUMBER(7) not null,
  CONTENT    VARCHAR2(20),
  STATUSFLAG NUMBER(11) default 1 not null,
  CREATOR    VARCHAR2(20),
  CREATETIME DATE not null,
  SEQKEY     VARCHAR2(32)
)
;
alter table FILTER
  add constraint FILTER_PRIMARY_1 primary key (ID);

prompt
prompt Creating table FILTERMESSAGE
prompt ============================
prompt
create table FILTERMESSAGE
(
  ID      NUMBER(10) not null,
  APPID   VARCHAR2(20),
  CONTENT VARCHAR2(160)
)
;

prompt
prompt Creating table FORWARD_SERVICELIST
prompt ==================================
prompt
create table FORWARD_SERVICELIST
(
  SERVICEID      VARCHAR2(20),
  FORWARDCONTENT VARCHAR2(50),
  SEQKEY         VARCHAR2(32) not null,
  PROP1          VARCHAR2(50),
  PROP2          VARCHAR2(50),
  PROP3          VARCHAR2(50),
  PROP4          VARCHAR2(50)
)
;
alter table FORWARD_SERVICELIST
  add constraint FORWARD_SEQKEY unique (SEQKEY);

prompt
prompt Creating table HR_HI_CT_EXPERTTYPE
prompt ==================================
prompt
create table HR_HI_CT_EXPERTTYPE
(
  SEQKEY       VARCHAR2(16) not null,
  CODENAME     VARCHAR2(50),
  CODEVALUE    VARCHAR2(50),
  CODELEVEL    NUMBER(4),
  PARENTID     VARCHAR2(16),
  ISLEAF       NUMBER(4),
  PY           VARCHAR2(50),
  DISPLAYORDER NUMBER(4)
)
;
comment on table HR_HI_CT_EXPERTTYPE
  is 'ר�����-ר�����';
comment on column HR_HI_CT_EXPERTTYPE.SEQKEY
  is '����';
comment on column HR_HI_CT_EXPERTTYPE.CODENAME
  is '��������';
comment on column HR_HI_CT_EXPERTTYPE.CODEVALUE
  is '����ֵ';
comment on column HR_HI_CT_EXPERTTYPE.CODELEVEL
  is '����ȼ�';
comment on column HR_HI_CT_EXPERTTYPE.PARENTID
  is '��ID';
comment on column HR_HI_CT_EXPERTTYPE.ISLEAF
  is '�Ƿ�Ҷ��';
comment on column HR_HI_CT_EXPERTTYPE.PY
  is 'ƴ��';
comment on column HR_HI_CT_EXPERTTYPE.DISPLAYORDER
  is '�����ֶ�';
alter table HR_HI_CT_EXPERTTYPE
  add constraint PK_HR_HI_CT_EXPERTTYPE primary key (SEQKEY);

prompt
prompt Creating table HR_HI_CT_PERSONSCOPE
prompt ===================================
prompt
create table HR_HI_CT_PERSONSCOPE
(
  SEQKEY       VARCHAR2(16) not null,
  CODENAME     VARCHAR2(50),
  CODEVALUE    VARCHAR2(50),
  CODELEVEL    NUMBER(4),
  PARENTID     VARCHAR2(16),
  ISLEAF       NUMBER(4),
  PY           VARCHAR2(50),
  DISPLAYORDER NUMBER(4),
  DESCRIPTION  VARCHAR2(500)
)
;
alter table HR_HI_CT_PERSONSCOPE
  add primary key (SEQKEY);

prompt
prompt Creating table HR_HI_CT_PERSONTYPE
prompt ==================================
prompt
create table HR_HI_CT_PERSONTYPE
(
  SEQKEY       VARCHAR2(16) not null,
  CODENAME     VARCHAR2(50),
  CODEVALUE    VARCHAR2(50),
  CODELEVEL    NUMBER(4),
  PARENTID     VARCHAR2(16),
  ISLEAF       NUMBER(4),
  PY           VARCHAR2(50),
  DISPLAYORDER NUMBER(4)
)
;
alter table HR_HI_CT_PERSONTYPE
  add constraint HR_OM_CT_POSRANK1_1 primary key (SEQKEY);

prompt
prompt Creating table HR_HI_CT_QUALIFYLEV
prompt ==================================
prompt
create table HR_HI_CT_QUALIFYLEV
(
  SEQKEY       VARCHAR2(16) not null,
  CODENAME     VARCHAR2(50),
  CODEVALUE    VARCHAR2(50),
  CODELEVEL    NUMBER(4),
  PARENTID     VARCHAR2(16),
  ISLEAF       NUMBER(4),
  PY           VARCHAR2(50),
  DISPLAYORDER NUMBER(4)
)
;
alter table HR_HI_CT_QUALIFYLEV
  add primary key (SEQKEY);

prompt
prompt Creating table HR_HI_CT_SEX
prompt ===========================
prompt
create table HR_HI_CT_SEX
(
  SEQKEY       VARCHAR2(16) not null,
  CODENAME     VARCHAR2(50),
  CODEVALUE    VARCHAR2(50),
  CODELEVEL    NUMBER(4),
  PARENTID     VARCHAR2(16),
  ISLEAF       NUMBER(4),
  PY           VARCHAR2(50),
  DISPLAYORDER NUMBER(4)
)
;
alter table HR_HI_CT_SEX
  add constraint HR_OM_CT_POSRANK1_1_1 primary key (SEQKEY);

prompt
prompt Creating table HR_HI_CT_TECHPOSTLEV
prompt ===================================
prompt
create table HR_HI_CT_TECHPOSTLEV
(
  SEQKEY       VARCHAR2(16) not null,
  CODENAME     VARCHAR2(50),
  CODEVALUE    VARCHAR2(50),
  CODELEVEL    NUMBER(4),
  PARENTID     VARCHAR2(16),
  ISLEAF       NUMBER(4),
  PY           VARCHAR2(50),
  DISPLAYORDER NUMBER(4)
)
;
comment on table HR_HI_CT_TECHPOSTLEV
  is 'רҵ����ְ��-רҵ�����ʸ�ȼ�';
alter table HR_HI_CT_TECHPOSTLEV
  add constraint PK_HR_HI_CT_TECHPOSTLEV primary key (SEQKEY);

prompt
prompt Creating table HR_HI_CT_UNITLEV
prompt ===============================
prompt
create table HR_HI_CT_UNITLEV
(
  SEQKEY       VARCHAR2(16) not null,
  CODENAME     VARCHAR2(50),
  CODEVALUE    VARCHAR2(50),
  CODELEVEL    NUMBER(4),
  PARENTID     VARCHAR2(16),
  ISLEAF       NUMBER(4),
  PY           VARCHAR2(50),
  DISPLAYORDER NUMBER(4)
)
;
comment on table HR_HI_CT_UNITLEV
  is 'ר�����-��׼��λ����';
comment on column HR_HI_CT_UNITLEV.SEQKEY
  is '����';
comment on column HR_HI_CT_UNITLEV.CODENAME
  is '��������';
comment on column HR_HI_CT_UNITLEV.CODEVALUE
  is '����ֵ';
comment on column HR_HI_CT_UNITLEV.CODELEVEL
  is '����ȼ�';
comment on column HR_HI_CT_UNITLEV.PARENTID
  is '��ID';
comment on column HR_HI_CT_UNITLEV.ISLEAF
  is '�Ƿ�Ҷ��';
comment on column HR_HI_CT_UNITLEV.PY
  is 'ƴ��';
comment on column HR_HI_CT_UNITLEV.DISPLAYORDER
  is '�����ֶ�';
alter table HR_HI_CT_UNITLEV
  add constraint PK_HR_HI_CT_UNITLEV primary key (SEQKEY);

prompt
prompt Creating table HR_HI_EDU
prompt ========================
prompt
create table HR_HI_EDU
(
  PSNNUM       VARCHAR2(20) not null,
  SEQKEY       NUMBER(16) not null,
  LASTFLAG     NUMBER(4),
  ENTRANCEDATE DATE,
  GRDUATEDATE  DATE,
  SCHOOL       VARCHAR2(200),
  MAJOR        VARCHAR2(30),
  EDUSYSTEM    VARCHAR2(30),
  STUDYMODE    VARCHAR2(30),
  EDUCATION    VARCHAR2(30),
  DEGREE       VARCHAR2(30),
  DEGREEDATE   DATE,
  DEGREEUNIT   VARCHAR2(60),
  CERTIFCODE   VARCHAR2(28),
  AMEMO        VARCHAR2(500),
  ABBILLNO     VARCHAR2(32),
  STATUS       NUMBER(4),
  DEPTLEADER   VARCHAR2(30),
  ORGID        VARCHAR2(32),
  NAME         VARCHAR2(32)
)
;
comment on table HR_HI_EDU
  is 'ѧ��ѧλ���';
comment on column HR_HI_EDU.PSNNUM
  is '��Ա����';
comment on column HR_HI_EDU.SEQKEY
  is '��¼���';
comment on column HR_HI_EDU.LASTFLAG
  is '��ǰ��¼��ʶ';
comment on column HR_HI_EDU.ENTRANCEDATE
  is '��ѧ����';
comment on column HR_HI_EDU.GRDUATEDATE
  is '��ҵ����';
comment on column HR_HI_EDU.SCHOOL
  is 'ѧУ';
comment on column HR_HI_EDU.MAJOR
  is 'רҵ';
comment on column HR_HI_EDU.EDUSYSTEM
  is 'ѧ��';
comment on column HR_HI_EDU.STUDYMODE
  is 'ѧϰ��ʽ';
comment on column HR_HI_EDU.EDUCATION
  is 'ѧ��';
comment on column HR_HI_EDU.DEGREE
  is 'ѧλ';
comment on column HR_HI_EDU.DEGREEDATE
  is 'ѧλ��������';
comment on column HR_HI_EDU.DEGREEUNIT
  is 'ѧλ���赥λ';
comment on column HR_HI_EDU.CERTIFCODE
  is '֤����';
comment on column HR_HI_EDU.AMEMO
  is '��ע';
alter table HR_HI_EDU
  add constraint PK_HR_HI_EDU primary key (SEQKEY);

prompt
prompt Creating table HR_HI_EXPERT
prompt ===========================
prompt
create table HR_HI_EXPERT
(
  PSNNUM     VARCHAR2(64) not null,
  SEQKEY     NUMBER(16) not null,
  LASTFLAG   NUMBER(4),
  EXPERTTYPE VARCHAR2(30),
  AUTHUNIT   VARCHAR2(60),
  UNITLEV    VARCHAR2(30),
  AUTHDATE   DATE,
  TREAT      VARCHAR2(100)
)
;
comment on table HR_HI_EXPERT
  is 'ר�����';
comment on column HR_HI_EXPERT.PSNNUM
  is '��Ա����';
comment on column HR_HI_EXPERT.SEQKEY
  is '��¼���';
comment on column HR_HI_EXPERT.LASTFLAG
  is '��ǰ��¼��ʶ';
comment on column HR_HI_EXPERT.EXPERTTYPE
  is 'ר�����';
comment on column HR_HI_EXPERT.AUTHUNIT
  is '��׼��λ����';
comment on column HR_HI_EXPERT.UNITLEV
  is '��׼��λ����';
comment on column HR_HI_EXPERT.AUTHDATE
  is '��׼ʱ��';
comment on column HR_HI_EXPERT.TREAT
  is 'ר�Ҵ���';
alter table HR_HI_EXPERT
  add constraint PK_HR_HI_EXPERT primary key (SEQKEY);

prompt
prompt Creating table HR_HI_PERSON
prompt ===========================
prompt
create table HR_HI_PERSON
(
  SEQKEY        VARCHAR2(32) not null,
  ORGCODE       VARCHAR2(32),
  PSNNUM        VARCHAR2(30),
  NAME          VARCHAR2(30),
  PERSONTYPE    VARCHAR2(30),
  SEX           NUMBER(4),
  BIRTHDATE     DATE,
  NATIVEPLACE   VARCHAR2(32),
  NATIONAL      VARCHAR2(32),
  HEALTHSTATUS  VARCHAR2(32),
  MARRISTATUS   VARCHAR2(32),
  IDNO          VARCHAR2(30),
  PERRESIDENCE  VARCHAR2(32),
  ALIANAME      VARCHAR2(30),
  JOINWORKDATE  DATE,
  ENTERDATE     DATE,
  REGULARDATE   DATE,
  BUSINESSSORT  VARCHAR2(32),
  WORKMAJOR     VARCHAR2(32),
  EMIGFLAG      NUMBER(4),
  SSNO          VARCHAR2(30),
  PERSTATUS     NUMBER(4),
  CHECKINFLAG   VARCHAR2(10),
  SYNWAGEFLAG   VARCHAR2(10),
  ENTERUNITDATE VARCHAR2(20),
  PSNPROPERTY   VARCHAR2(10),
  PSNBANKCODE   VARCHAR2(10),
  PSNACCOUNT    VARCHAR2(10),
  INDUTYSTATUS  NUMBER(4),
  DUTYCLASS     VARCHAR2(32),
  LOGINNAME     VARCHAR2(32),
  PERSONSCOPE   VARCHAR2(32),
  LASTDATE      DATE,
  WORKAGEDIFF   NUMBER(4),
  AGE           NUMBER(4),
  WORKAGE       NUMBER(4),
  ENTERUNITAGE  NUMBER(4),
  PHOTOFILE     VARCHAR2(200),
  POSCODE       VARCHAR2(32),
  JOBID         VARCHAR2(32),
  JOBTITLEID    VARCHAR2(64),
  JOBTITLELEVEL VARCHAR2(64),
  EDUCATION     VARCHAR2(32),
  DEGREE        VARCHAR2(32),
  LEAVEDATE     DATE,
  WORKTYPE      VARCHAR2(32),
  JOBLEVELID    VARCHAR2(32),
  QUALIFYNAME   VARCHAR2(64),
  QUALIFYLEV    VARCHAR2(64),
  CREATEID      VARCHAR2(32),
  CREATEDATE    DATE,
  UPDATEID      VARCHAR2(32),
  UPDATEDATE    DATE,
  ORGID         VARCHAR2(32),
  COMPANYID     VARCHAR2(32)
)
;
comment on column HR_HI_PERSON.SEQKEY
  is '���';
comment on column HR_HI_PERSON.ORGCODE
  is '����';
comment on column HR_HI_PERSON.NAME
  is '��Ա����';
comment on column HR_HI_PERSON.PERSONTYPE
  is '��Ա���';
comment on column HR_HI_PERSON.SEX
  is '�Ա�';
comment on column HR_HI_PERSON.BIRTHDATE
  is '��������';
comment on column HR_HI_PERSON.NATIVEPLACE
  is '����';
comment on column HR_HI_PERSON.NATIONAL
  is '����';
comment on column HR_HI_PERSON.HEALTHSTATUS
  is '����״��';
comment on column HR_HI_PERSON.MARRISTATUS
  is '����״��';
comment on column HR_HI_PERSON.IDNO
  is '���֤����';
comment on column HR_HI_PERSON.ALIANAME
  is '������';
comment on column HR_HI_PERSON.JOINWORKDATE
  is '�μӹ���ʱ��';
comment on column HR_HI_PERSON.ENTERDATE
  is '���뱾��ҵ����ʱ��';
comment on column HR_HI_PERSON.REGULARDATE
  is 'ת������ʱ��';
comment on column HR_HI_PERSON.BUSINESSSORT
  is 'ְҵ���';
comment on column HR_HI_PERSON.WORKMAJOR
  is '�ִ���רҵ';
comment on column HR_HI_PERSON.EMIGFLAG
  is '�۰�̨�Ȱ���ʶ';
comment on column HR_HI_PERSON.SSNO
  is '��ᱣ�Ϻ�';
comment on column HR_HI_PERSON.PERSTATUS
  is '�������';
comment on column HR_HI_PERSON.CHECKINFLAG
  is '��˱�־';
comment on column HR_HI_PERSON.SYNWAGEFLAG
  is '�빤��ͬ��״̬��־';
comment on column HR_HI_PERSON.ENTERUNITDATE
  is '���뱾��λʱ��';
comment on column HR_HI_PERSON.PSNBANKCODE
  is '����';
comment on column HR_HI_PERSON.PSNACCOUNT
  is '�˺�';
comment on column HR_HI_PERSON.INDUTYSTATUS
  is '��ְ״̬';
comment on column HR_HI_PERSON.DUTYCLASS
  is '����';
comment on column HR_HI_PERSON.PERSONSCOPE
  is '��Ա��Χ/����';
comment on column HR_HI_PERSON.LASTDATE
  is '����޸�ʱ��';
comment on column HR_HI_PERSON.POSCODE
  is '��λ';
comment on column HR_HI_PERSON.JOBID
  is 'ְ��';
comment on column HR_HI_PERSON.JOBTITLEID
  is 'ְ��';
comment on column HR_HI_PERSON.JOBTITLELEVEL
  is 'ְ�Ƶȼ�';
comment on column HR_HI_PERSON.EDUCATION
  is 'ѧ��';
comment on column HR_HI_PERSON.DEGREE
  is 'ѧλ';
comment on column HR_HI_PERSON.QUALIFYNAME
  is '�ʸ�����';
comment on column HR_HI_PERSON.QUALIFYLEV
  is '�ʸ�ȼ�';
alter table HR_HI_PERSON
  add primary key (SEQKEY);

prompt
prompt Creating table HR_HI_POSINFO
prompt ============================
prompt
create table HR_HI_POSINFO
(
  PSNNUM       VARCHAR2(20) not null,
  SEQKEY       NUMBER(16) not null,
  LASTFLAG     NUMBER(4),
  DCHGTYPE     VARCHAR2(30),
  ORGCODE      VARCHAR2(30),
  POSCODE      VARCHAR2(30),
  POSNAME      VARCHAR2(100),
  ODDJDATE     DATE,
  OFFJREASON   VARCHAR2(30),
  DUTYCODE     VARCHAR2(30),
  DUTYLEV      VARCHAR2(30),
  HPSTATUS     VARCHAR2(30),
  HOLDPOSTWAY  VARCHAR2(30),
  HPREASON     VARCHAR2(30),
  HPAUTHUNIT   VARCHAR2(60),
  AUTHORIZEDOC VARCHAR2(60),
  BEGINDATE    DATE,
  ENDDATE      DATE,
  REMOVDATE    DATE,
  REMOVMODE    VARCHAR2(30),
  REMOVREASON  VARCHAR2(30),
  RMAUTHUNIT   VARCHAR2(60),
  RMAUTHDOC    VARCHAR2(60),
  SUPPERSON    VARCHAR2(20),
  PARTJOB      NUMBER(4)
)
;
alter table HR_HI_POSINFO
  add constraint PK_HR_HI_JOBINFO_1 primary key (SEQKEY);

prompt
prompt Creating table HR_HI_TECHTITLE
prompt ==============================
prompt
create table HR_HI_TECHTITLE
(
  PSNNUM        VARCHAR2(20) not null,
  SEQKEY        NUMBER(16) not null,
  LASTFLAG      NUMBER(4),
  TECHPOSTTITLE VARCHAR2(200),
  TECHPOSTLEV   VARCHAR2(60),
  GETWAY        VARCHAR2(200),
  AUTHORG       VARCHAR2(60),
  GETDATE       DATE,
  CERTIFCODE    VARCHAR2(60),
  SUMM          VARCHAR2(100),
  APPOINTTITLE  VARCHAR2(200),
  APPONITBDATE  DATE,
  APPOINYEDATE  DATE,
  APPONITORG    VARCHAR2(60),
  APPOITYPE     VARCHAR2(200),
  STRONGSUIT    VARCHAR2(100)
)
;
comment on table HR_HI_TECHTITLE
  is 'רҵ����ְ��';
comment on column HR_HI_TECHTITLE.PSNNUM
  is '��Ա����';
comment on column HR_HI_TECHTITLE.SEQKEY
  is '��¼���';
comment on column HR_HI_TECHTITLE.LASTFLAG
  is '��ǰ��¼��ʶ';
comment on column HR_HI_TECHTITLE.TECHPOSTTITLE
  is 'רҵ�����ʸ�����';
comment on column HR_HI_TECHTITLE.TECHPOSTLEV
  is 'רҵ�����ʸ�ȼ�';
comment on column HR_HI_TECHTITLE.GETWAY
  is 'ȡ���ʸ�;��';
comment on column HR_HI_TECHTITLE.AUTHORG
  is '�ʸ�������λ';
comment on column HR_HI_TECHTITLE.GETDATE
  is 'ȡ���ʸ�ʱ��';
comment on column HR_HI_TECHTITLE.CERTIFCODE
  is '֤����';
comment on column HR_HI_TECHTITLE.SUMM
  is '��Ҫ˵��';
comment on column HR_HI_TECHTITLE.APPOINTTITLE
  is 'Ƹ��רҵ����ְ������';
comment on column HR_HI_TECHTITLE.APPONITBDATE
  is 'Ƹ����ʼʱ��';
comment on column HR_HI_TECHTITLE.APPOINYEDATE
  is 'Ƹ����ֹʱ��';
comment on column HR_HI_TECHTITLE.APPONITORG
  is 'Ƹ�ε�λ';
comment on column HR_HI_TECHTITLE.APPOITYPE
  is 'Ƹ�����';
comment on column HR_HI_TECHTITLE.STRONGSUIT
  is 'ר��';
alter table HR_HI_TECHTITLE
  add constraint PK_HR_HI_TECHTITLE primary key (SEQKEY);

prompt
prompt Creating table HR_HI_WORKQUALIFY
prompt ================================
prompt
create table HR_HI_WORKQUALIFY
(
  PSNNUM         VARCHAR2(20),
  SEQKEY         NUMBER(16) not null,
  LASTFLAG       NUMBER(4),
  QUALIFYNAME    VARCHAR2(200),
  QUALIFYLEV     VARCHAR2(60),
  GETDATE        DATE,
  AUTHUNIT       VARCHAR2(200),
  VALIDBEGINDATE DATE,
  VALIDENDDATE   DATE,
  CHECKPERIOD    NUMBER(16),
  LASTCHECKDATE  DATE,
  NEXTCHECKDATE  DATE,
  REMARK         VARCHAR2(500),
  ORGCODE        VARCHAR2(32),
  NAME           VARCHAR2(200),
  QUALIFYNO      VARCHAR2(50)
)
;
comment on table HR_HI_WORKQUALIFY
  is '��������ְҵ�ʸ�';
comment on column HR_HI_WORKQUALIFY.PSNNUM
  is '��Ա����';
comment on column HR_HI_WORKQUALIFY.SEQKEY
  is '��¼���';
comment on column HR_HI_WORKQUALIFY.LASTFLAG
  is '��ǰ��¼��ʶ';
comment on column HR_HI_WORKQUALIFY.QUALIFYNAME
  is '�ʸ�����';
comment on column HR_HI_WORKQUALIFY.QUALIFYLEV
  is '�ʸ�ȼ�';
comment on column HR_HI_WORKQUALIFY.GETDATE
  is '���ʱ��';
comment on column HR_HI_WORKQUALIFY.AUTHUNIT
  is '��׼��λ';
alter table HR_HI_WORKQUALIFY
  add constraint PK_HR_HI_WORKQUALIFY primary key (SEQKEY);

prompt
prompt Creating table HR_OM_CT_ORGLEVEL
prompt ================================
prompt
create table HR_OM_CT_ORGLEVEL
(
  SEQKEY       VARCHAR2(16) not null,
  CODENAME     VARCHAR2(200),
  CODEVALUE    VARCHAR2(50),
  CODELEVEL    NUMBER(4),
  PARENTID     VARCHAR2(16),
  ISLEAF       NUMBER(4),
  PY           VARCHAR2(50),
  DISPLAYORDER NUMBER(4)
)
;
alter table HR_OM_CT_ORGLEVEL
  add constraint HR_OM_CT_POSRANK1_1_1_1_1 primary key (SEQKEY);

prompt
prompt Creating table HR_OM_CT_ORGTYPE
prompt ===============================
prompt
create table HR_OM_CT_ORGTYPE
(
  SEQKEY       VARCHAR2(16) not null,
  CODENAME     VARCHAR2(200),
  CODEVALUE    VARCHAR2(50),
  CODELEVEL    NUMBER(4),
  PARENTID     VARCHAR2(16),
  ISLEAF       NUMBER(4),
  PY           VARCHAR2(50),
  DISPLAYORDER NUMBER(4)
)
;
comment on column HR_OM_CT_ORGTYPE.SEQKEY
  is '����';
comment on column HR_OM_CT_ORGTYPE.CODENAME
  is '��������';
comment on column HR_OM_CT_ORGTYPE.CODEVALUE
  is '����ֵ';
comment on column HR_OM_CT_ORGTYPE.CODELEVEL
  is '����ȼ�';
comment on column HR_OM_CT_ORGTYPE.PARENTID
  is '��ID';
comment on column HR_OM_CT_ORGTYPE.ISLEAF
  is '�Ƿ�Ҷ��';
comment on column HR_OM_CT_ORGTYPE.PY
  is 'ƴ��';
comment on column HR_OM_CT_ORGTYPE.DISPLAYORDER
  is '�����ֶ�';
alter table HR_OM_CT_ORGTYPE
  add constraint HR_OM_CT_POSRANK1_4 primary key (SEQKEY);

prompt
prompt Creating table HR_OM_DUTY
prompt =========================
prompt
create table HR_OM_DUTY
(
  SEQKEY    VARCHAR2(64) not null,
  DUTYCODE  VARCHAR2(64),
  DUTYNAME  VARCHAR2(200),
  SERIES    VARCHAR2(64),
  DUTYSUMM  VARCHAR2(500),
  DUTYLEV   VARCHAR2(64),
  BEGINDATE DATE,
  ENDDATE   DATE,
  COMPANYID VARCHAR2(32)
)
;
alter table HR_OM_DUTY
  add primary key (SEQKEY);

prompt
prompt Creating table HR_OM_POS
prompt ========================
prompt
create table HR_OM_POS
(
  POSCODE    VARCHAR2(32),
  POSNAME    VARCHAR2(100),
  ORGCODE    VARCHAR2(32),
  DUTYCODE   VARCHAR2(32),
  SUPORIOR   VARCHAR2(32),
  BUILDDATE  DATE,
  ISABORT    NUMBER(4),
  ABORTDATE  DATE,
  POSSERIES  VARCHAR2(32),
  POSRANK    VARCHAR2(32),
  WORKSUMM   VARCHAR2(500),
  SEQKEY     VARCHAR2(32) not null,
  LEADERFLAG NUMBER(4),
  COMPANYID  VARCHAR2(50),
  CREATEID   VARCHAR2(32),
  CREATEDATE DATE,
  UPDATEID   VARCHAR2(32),
  UPDATEDATE DATE,
  ORGID      VARCHAR2(32)
)
;
comment on column HR_OM_POS.POSCODE
  is '��λ����';
comment on column HR_OM_POS.POSNAME
  is '��λ����';
comment on column HR_OM_POS.ORGCODE
  is '������֯';
comment on column HR_OM_POS.DUTYCODE
  is '��Ӧְ��';
comment on column HR_OM_POS.SUPORIOR
  is '�Ƿ����λ';
comment on column HR_OM_POS.BUILDDATE
  is '��������';
comment on column HR_OM_POS.ISABORT
  is '�Ƿ��Ѿ�����';
comment on column HR_OM_POS.ABORTDATE
  is '��������';
comment on column HR_OM_POS.POSSERIES
  is '��λ����';
comment on column HR_OM_POS.POSRANK
  is '��λ�ȼ�';
comment on column HR_OM_POS.WORKSUMM
  is '������Ҫ';
comment on column HR_OM_POS.SEQKEY
  is '����';
alter table HR_OM_POS
  add primary key (SEQKEY);

prompt
prompt Creating table HR_OM_POSSUPERV
prompt ==============================
prompt
create table HR_OM_POSSUPERV
(
  SEQKEY       VARCHAR2(32) not null,
  POSCODE      VARCHAR2(32),
  SUPERVTYPE   VARCHAR2(32),
  WORKDEGREE   VARCHAR2(200),
  SUPERPOSCODE VARCHAR2(32),
  VMEMO        VARCHAR2(200)
)
;
alter table HR_OM_POSSUPERV
  add primary key (SEQKEY);

prompt
prompt Creating table HR_ORG_BASE
prompt ==========================
prompt
create table HR_ORG_BASE
(
  SEQKEY            VARCHAR2(16) not null,
  ORGCODE           VARCHAR2(50) not null,
  ORGNAME           VARCHAR2(100) not null,
  ORGLEVEL          VARCHAR2(16),
  GOVERORG          VARCHAR2(16),
  ADDRESS           VARCHAR2(100),
  STATE             NUMBER(4),
  COUNTRY           VARCHAR2(50),
  AUTHORDATE        DATE,
  AUTHORDOC         VARCHAR2(50),
  REMOVORG          VARCHAR2(16),
  REMOVDOC          VARCHAR2(50),
  REMOVDATE         DATE,
  INNERNAME         VARCHAR2(50),
  INNERLEVEL        NUMBER(4),
  INNERORGNUM       NUMBER(4),
  PARENTID          VARCHAR2(16),
  ORGTYPE           NUMBER(4),
  ISACTUALORG       NUMBER(4),
  VALIDBEGINDATE    DATE,
  VALIDENDDATE      DATE,
  DISPLAYORDER      NUMBER(8),
  ECONOMYCATEGORYID VARCHAR2(16),
  TRADEID           VARCHAR2(16),
  COMPANYTYPE       VARCHAR2(16),
  ISREFERRENCE      NUMBER(4),
  CREATEID          VARCHAR2(32),
  CREATEDATE        DATE,
  UPDATEID          VARCHAR2(32),
  UPDATEDATE        DATE,
  ORGID             VARCHAR2(32),
  COMPANYID         VARCHAR2(32)
)
;
comment on column HR_ORG_BASE.SEQKEY
  is '����';
comment on column HR_ORG_BASE.ORGCODE
  is '��λ����';
comment on column HR_ORG_BASE.ORGNAME
  is '��λ����';
comment on column HR_ORG_BASE.ORGLEVEL
  is '��λ����';
comment on column HR_ORG_BASE.GOVERORG
  is '���ܵ�λ����';
comment on column HR_ORG_BASE.ADDRESS
  is '��λ���ڵ�';
comment on column HR_ORG_BASE.STATE
  is '��λ״̬';
comment on column HR_ORG_BASE.COUNTRY
  is '���ڹ��ң�������';
comment on column HR_ORG_BASE.AUTHORDATE
  is '��׼����ʱ��';
comment on column HR_ORG_BASE.AUTHORDOC
  is '��׼�����ĺ�';
comment on column HR_ORG_BASE.REMOVORG
  is '������׼��λ';
comment on column HR_ORG_BASE.REMOVDOC
  is '��׼�����ĺ�';
comment on column HR_ORG_BASE.REMOVDATE
  is '����ʱ��';
comment on column HR_ORG_BASE.INNERNAME
  is '�����������';
comment on column HR_ORG_BASE.INNERLEVEL
  is '�����������';
comment on column HR_ORG_BASE.INNERORGNUM
  is '���������';
comment on column HR_ORG_BASE.PARENTID
  is '�ϼ���λID';
comment on column HR_ORG_BASE.ORGTYPE
  is '��������';
comment on column HR_ORG_BASE.ISACTUALORG
  is '�Ƿ�ʵ����';
comment on column HR_ORG_BASE.VALIDBEGINDATE
  is '��Ч��ʼ����';
comment on column HR_ORG_BASE.VALIDENDDATE
  is '��Ч��������';
comment on column HR_ORG_BASE.DISPLAYORDER
  is '��ʾ˳��';
comment on column HR_ORG_BASE.ECONOMYCATEGORYID
  is '��˾�������';
comment on column HR_ORG_BASE.TRADEID
  is '������ҵ';
comment on column HR_ORG_BASE.COMPANYTYPE
  is '��ҵ����';
comment on column HR_ORG_BASE.ISREFERRENCE
  is '�Ƿ����ñ��ְ����ϵ';
alter table HR_ORG_BASE
  add primary key (SEQKEY);

prompt
prompt Creating table HR_ORG_EXTENSION
prompt ===============================
prompt
create table HR_ORG_EXTENSION
(
  ORGCODE        VARCHAR2(16) not null,
  EMAIL          VARCHAR2(50),
  CORPORATOR     VARCHAR2(50),
  DESCRIPTION    VARCHAR2(200),
  POSTCODE       VARCHAR2(50),
  HOMEPAGE       VARCHAR2(50),
  TELEPHONE      VARCHAR2(50),
  FAX            VARCHAR2(50),
  RESPONSIBILITY VARCHAR2(200)
)
;

prompt
prompt Creating table IN_OK
prompt ====================
prompt
create table IN_OK
(
  BATCHNO     VARCHAR2(42) default '' not null,
  SERIALNO    NUMBER(11) default 1 not null,
  SEQUENCENO  NUMBER(11) default 0,
  RETCODE     VARCHAR2(12),
  ERRMSG      VARCHAR2(60),
  STATUSFLAG  NUMBER(11) default 0,
  APPID       VARCHAR2(12),
  APPSERIALNO VARCHAR2(35),
  MEDIAID     VARCHAR2(10),
  SENDID      VARCHAR2(60),
  RECVID      VARCHAR2(60),
  SUBMITDATE  VARCHAR2(24),
  SUBMITTIME  VARCHAR2(18),
  FINISHDATE  VARCHAR2(24),
  FINISHTIME  VARCHAR2(18),
  CONTENT     VARCHAR2(160),
  ACK         NUMBER(11) default 0,
  REPLY       VARCHAR2(30),
  MSGTYPE     NUMBER(11) default 0,
  SUBAPP      VARCHAR2(10)
)
;
alter table IN_OK
  add constraint IN_OK_PRIMARY_1 primary key (BATCHNO, SERIALNO);

prompt
prompt Creating table IN_OK_V3
prompt =======================
prompt
create table IN_OK_V3
(
  BATCHNO        VARCHAR2(42) not null,
  SERIALNO       NUMBER not null,
  SEQUENCENO     NUMBER not null,
  RETCODE        VARCHAR2(12),
  ERRMSG         VARCHAR2(60),
  STATUSFLAG     NUMBER,
  SERVICEID      VARCHAR2(20),
  APPSERIALNO    VARCHAR2(35),
  MEDIAID        VARCHAR2(10),
  SENDID         VARCHAR2(60),
  RECVID         VARCHAR2(60),
  SUBMITDATE     VARCHAR2(24),
  SUBMITTIME     VARCHAR2(18),
  FINISHDATE     VARCHAR2(24),
  FINISHTIME     VARCHAR2(18),
  CONTENTSUBJECT VARCHAR2(60),
  CONTENT        VARCHAR2(1000),
  MSGID          VARCHAR2(20),
  ACK            NUMBER(1),
  REPLYNO        VARCHAR2(30),
  DOCOUNT        NUMBER,
  MSGTYPE        NUMBER,
  PROP1          VARCHAR2(50),
  PROP2          VARCHAR2(50),
  PROP3          VARCHAR2(50),
  PROP4          VARCHAR2(50),
  SEQKEY         VARCHAR2(32),
  SENDERIDTYPE   NUMBER,
  RECEIVERIDTYPE NUMBER
)
;

prompt
prompt Creating table IN_OUT_ATTACHMENTS
prompt =================================
prompt
create table IN_OUT_ATTACHMENTS
(
  BATCHNO        VARCHAR2(14),
  SERIALNO       NUMBER(8),
  CHANNELTYPE    VARCHAR2(1),
  SOURCEFILENAME VARCHAR2(64),
  TARGETFILENAME VARCHAR2(128),
  FILESTYLE      VARCHAR2(2)
)
;

prompt
prompt Creating table IN_OUT_ATTACHMENTS_V3
prompt ====================================
prompt
create table IN_OUT_ATTACHMENTS_V3
(
  BATCHNO     VARCHAR2(14) not null,
  SERIALNO    NUMBER(8) not null,
  SEQUENCENO  NUMBER not null,
  FILENAME    VARCHAR2(200),
  FILECONTENT BLOB
)
;
comment on table IN_OUT_ATTACHMENTS_V3
  is '��Ϣ��������UMS3.0��ʼ';
create index UMS_BATCHNO_3D2915A8 on IN_OUT_ATTACHMENTS_V3 (BATCHNO);
create index UMS_SEQUENCENO_3D2915A8 on IN_OUT_ATTACHMENTS_V3 (SEQUENCENO);
create index UMS_SERIALNO_3D2915A8 on IN_OUT_ATTACHMENTS_V3 (SERIALNO);

prompt
prompt Creating table IN_READY
prompt =======================
prompt
create table IN_READY
(
  BATCHNO     VARCHAR2(42) default '' not null,
  SERIALNO    NUMBER(11) default 1 not null,
  SEQUENCENO  NUMBER(11) default 1,
  RETCODE     VARCHAR2(12),
  ERRMSG      VARCHAR2(60),
  STATUSFLAG  NUMBER(11) default 1,
  APPID       VARCHAR2(12),
  APPSERIALNO VARCHAR2(35),
  MEDIAID     VARCHAR2(10),
  SENDID      VARCHAR2(60),
  RECVID      VARCHAR2(60),
  SUBMITDATE  VARCHAR2(24),
  SUBMITTIME  VARCHAR2(18),
  FINISHDATE  VARCHAR2(24),
  FINISHTIME  VARCHAR2(18),
  CONTENT     VARCHAR2(160),
  ACK         NUMBER(11) default 0,
  REPLY       VARCHAR2(30),
  DOCOUNT     NUMBER(11) default 0,
  MSGTYPE     NUMBER(11) default 0,
  SUBAPP      VARCHAR2(10)
)
;
alter table IN_READY
  add constraint IN_READY_PRIMARY_1 primary key (BATCHNO, SERIALNO);

prompt
prompt Creating table IN_READY_V3
prompt ==========================
prompt
create table IN_READY_V3
(
  BATCHNO        VARCHAR2(42) not null,
  SERIALNO       NUMBER not null,
  SEQUENCENO     NUMBER not null,
  RETCODE        VARCHAR2(12),
  ERRMSG         VARCHAR2(60),
  STATUSFLAG     NUMBER,
  SERVICEID      VARCHAR2(20),
  APPSERIALNO    VARCHAR2(35),
  MEDIAID        VARCHAR2(10),
  SENDID         VARCHAR2(60),
  RECVID         VARCHAR2(60),
  SUBMITDATE     VARCHAR2(24),
  SUBMITTIME     VARCHAR2(18),
  FINISHDATE     VARCHAR2(24),
  FINISHTIME     VARCHAR2(18),
  CONTENTSUBJECT VARCHAR2(60),
  CONTENT        VARCHAR2(1000),
  MSGID          VARCHAR2(20),
  ACK            NUMBER(1),
  REPLYNO        VARCHAR2(30),
  DOCOUNT        NUMBER,
  MSGTYPE        NUMBER,
  PROP1          VARCHAR2(50),
  PROP2          VARCHAR2(50),
  PROP3          VARCHAR2(50),
  PROP4          VARCHAR2(50),
  SEQKEY         VARCHAR2(32),
  SENDERIDTYPE   NUMBER,
  RECEIVERIDTYPE NUMBER
)
;

prompt
prompt Creating table JFW_BASE_ATTACHMENT
prompt ==================================
prompt
create table JFW_BASE_ATTACHMENT
(
  SEQKEY        VARCHAR2(32) not null,
  LINKTABLE     VARCHAR2(50),
  LINKID        VARCHAR2(50),
  LINKFIELDNAME VARCHAR2(50),
  FILETYPE      VARCHAR2(20),
  FILENAME      VARCHAR2(100),
  CONTENT       BLOB,
  CREATEID      VARCHAR2(50),
  CREATEDATE    DATE,
  UPDATEID      VARCHAR2(50),
  UPDATEDATE    DATE,
  FILESIZE      NUMBER(10)
)
;

prompt
prompt Creating table JFW_BASE_COMMUNICATE
prompt ===================================
prompt
create table JFW_BASE_COMMUNICATE
(
  SEQKEY     VARCHAR2(32) not null,
  TASKTYPE   VARCHAR2(32),
  TASKID     VARCHAR2(32),
  TASKMODEL  VARCHAR2(100),
  RIGHTLEVEL NUMBER(4),
  CREATEID   VARCHAR2(32),
  CREATEDATE DATE,
  UPDATEID   VARCHAR2(32),
  UPDATEDATE DATE,
  ORGID      VARCHAR2(32),
  COMPANYID  VARCHAR2(32),
  CONTENT    CLOB,
  TITLE      VARCHAR2(100)
)
;

prompt
prompt Creating table JFW_BASE_CONTENT
prompt ===============================
prompt
create table JFW_BASE_CONTENT
(
  LINKMODEL  VARCHAR2(50) not null,
  LINKID     VARCHAR2(50),
  SEQKEY     VARCHAR2(32) not null,
  CONTENT    CLOB,
  CREATEID   VARCHAR2(32),
  CREATEDATE DATE,
  UPDATEID   VARCHAR2(32),
  UPDATEDATE DATE,
  COMPANYID  VARCHAR2(32),
  TITLE      VARCHAR2(100)
)
;

prompt
prompt Creating table JFW_BASE_CT_YEAR
prompt ===============================
prompt
create table JFW_BASE_CT_YEAR
(
  SEQKEY       VARCHAR2(16) not null,
  CODENAME     VARCHAR2(50),
  CODEVALUE    VARCHAR2(50),
  CODELEVEL    NUMBER(4),
  PARENTID     VARCHAR2(16),
  ISLEAF       NUMBER(4),
  PY           VARCHAR2(50),
  DISPLAYORDER NUMBER(4)
)
;

prompt
prompt Creating table JFW_BASE_CUSTOMCONFIG
prompt ====================================
prompt
create table JFW_BASE_CUSTOMCONFIG
(
  SEQKEY       VARCHAR2(32) not null,
  MODULENAME   VARCHAR2(50),
  DATAMODEL    VARCHAR2(50),
  USERID       VARCHAR2(50),
  ORDERBYFIELD VARCHAR2(50),
  ORDERBYMODE  NUMBER(4),
  LISTSTYLE    VARCHAR2(20),
  DISPLAYROWS  NUMBER(4)
)
;

prompt
prompt Creating table JFW_BASE_CUSTOMFIELD
prompt ===================================
prompt
create table JFW_BASE_CUSTOMFIELD
(
  SEQKEY         VARCHAR2(32) not null,
  CUSTOMDEFINEID VARCHAR2(32),
  FIELDNAME      VARCHAR2(50),
  DISPLAYORDER   NUMBER(4),
  SEARCHFLAG     NUMBER(4),
  LISTFLAG       NUMBER(4),
  DISPLAYWIDTH   NUMBER(4),
  DISPLAYFORMAT  VARCHAR2(50)
)
;

prompt
prompt Creating table JFW_BASE_LINKDEFINE
prompt ==================================
prompt
create table JFW_BASE_LINKDEFINE
(
  SEQKEY       VARCHAR2(16) not null,
  MODULENAME   VARCHAR2(50) not null,
  LINKMODULE   VARCHAR2(50),
  LINKURL      VARCHAR2(200),
  GROUPNAME    VARCHAR2(50),
  WINDOW       VARCHAR2(20),
  DISPLAYORDER NUMBER(10),
  DESCRIPTION  VARCHAR2(50)
)
;

prompt
prompt Creating table JFW_BASE_MESSAGE
prompt ===============================
prompt
create table JFW_BASE_MESSAGE
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
  CONTENT     VARCHAR2(200),
  STATE       NUMBER(10),
  RECEIVER    VARCHAR2(50)
)
;

prompt
prompt Creating table JFW_BASE_NOTEPAD
prompt ===============================
prompt
create table JFW_BASE_NOTEPAD
(
  LINKMODULE VARCHAR2(50) not null,
  LINKID     VARCHAR2(50),
  SEQKEY     VARCHAR2(32) not null,
  CONTENT    CLOB,
  CREATEID   VARCHAR2(32),
  CREATEDATE DATE,
  UPDATEID   VARCHAR2(32),
  UPDATEDATE DATE,
  UNITID     VARCHAR2(32),
  COMPANYID  VARCHAR2(32),
  TITLE      VARCHAR2(100)
)
;

prompt
prompt Creating table JFW_BASE_NOTION
prompt ==============================
prompt
create table JFW_BASE_NOTION
(
  SEQKEY     VARCHAR2(32) not null,
  INSTANCEID VARCHAR2(32),
  WORKITEMID VARCHAR2(32),
  ACTIONNAME VARCHAR2(100),
  ACTIONDO   VARCHAR2(50),
  REMARK     VARCHAR2(200),
  CREATEID   VARCHAR2(32),
  CREATEDATE DATE,
  UPDATEID   VARCHAR2(32),
  UPDATEDATE DATE,
  ORGID      VARCHAR2(32)
)
;

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
  SEQKEY       VARCHAR2(32) not null
)
;

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
  SPECIALTYPE NUMBER(4)
)
;

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
;

prompt
prompt Creating table JFW_SECURITY_DATAMODEL
prompt =====================================
prompt
create table JFW_SECURITY_DATAMODEL
(
  SEQKEY     VARCHAR2(32) not null,
  ROLEID     VARCHAR2(32),
  MODELNAME  VARCHAR2(50),
  POLICYID   VARCHAR2(50),
  MODULENAME VARCHAR2(50),
  BASEVALUE  VARCHAR2(50)
)
;

prompt
prompt Creating table JFW_SECURITY_FIELD
prompt =================================
prompt
create table JFW_SECURITY_FIELD
(
  SEQKEY     VARCHAR2(32) not null,
  ROLEID     VARCHAR2(32),
  MODELNAME  VARCHAR2(50),
  FIELDNAME  VARCHAR2(50),
  RIGHTLEVEL NUMBER(10)
)
;

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
  POLICYLEVEL NUMBER(4)
)
;

prompt
prompt Creating table JFW_SECURITY_UNITPOLICY
prompt ======================================
prompt
create table JFW_SECURITY_UNITPOLICY
(
  SEQKEY   VARCHAR2(32) not null,
  UNITID   VARCHAR2(32),
  POLICYID VARCHAR2(32)
)
;

prompt
prompt Creating table JMS_V3
prompt =====================
prompt
create table JMS_V3
(
  COMPANYID   VARCHAR2(20) not null,
  URL         VARCHAR2(100) not null,
  TYPE        NUMBER(1),
  JMSUSER     VARCHAR2(50),
  JMSPASSWORD VARCHAR2(50),
  PROP1       VARCHAR2(50),
  PROP2       VARCHAR2(50),
  PROP3       VARCHAR2(50),
  PROP4       VARCHAR2(50),
  SEQKEY      VARCHAR2(32)
)
;
alter table JMS_V3
  add constraint PK_JMS_V3 primary key (URL);

prompt
prompt Creating table MEDIA
prompt ====================
prompt
create table MEDIA
(
  MEDIA_ID      VARCHAR2(3) default '' not null,
  MEDIANAME     VARCHAR2(40) default '' not null,
  CLASS         VARCHAR2(200),
  TYPE          NUMBER(11) default 1 not null,
  STATUSFLAG    NUMBER(11) default 0,
  IP            VARCHAR2(20),
  PORT          NUMBER(11) default 0,
  TIMEOUT       NUMBER(11) default 0,
  REPEATTIMES   NUMBER(11) default 0,
  STARTWORKTIME VARCHAR2(6),
  ENDWORKTIME   VARCHAR2(6),
  LOGINNAME     VARCHAR2(20),
  LOGINPASSWORD VARCHAR2(16),
  SLEEPTIME     NUMBER(11) default 0,
  PROP1         VARCHAR2(20),
  PROP2         VARCHAR2(20),
  PROP3         VARCHAR2(20),
  PROP4         VARCHAR2(20),
  PROP5         VARCHAR2(20),
  SEQKEY        VARCHAR2(32),
  MEDIASTYLE    NUMBER(11)
)
;
alter table MEDIA
  add constraint MEDIA1_PRIMARY_1 primary key (MEDIA_ID, MEDIANAME);

prompt
prompt Creating table OUT_ERROR
prompt ========================
prompt
create table OUT_ERROR
(
  BATCHNO     VARCHAR2(14) default '' not null,
  SERIALNO    NUMBER(11) default 1 not null,
  SEQUENCENO  NUMBER(11) default 1 not null,
  RETCODE     VARCHAR2(4),
  ERRMSG      VARCHAR2(60),
  STATUSFLAG  NUMBER(11),
  APPID       VARCHAR2(12),
  APPSERIALNO VARCHAR2(35),
  MEDIAID     VARCHAR2(3),
  SENDID      VARCHAR2(60),
  RECVID      VARCHAR2(60),
  SUBMITDATE  VARCHAR2(8),
  SUBMITTIME  VARCHAR2(6),
  FINISHDATE  VARCHAR2(8),
  FINISHTIME  VARCHAR2(6),
  REP         NUMBER(11) default 0,
  DOCOUNT     NUMBER(11) default 0,
  PRIORITY    NUMBER(11) default 0,
  BATCHMODE   VARCHAR2(1),
  CONTENTMODE VARCHAR2(1),
  CONTENT     VARCHAR2(160),
  TIMESETFLAG VARCHAR2(1),
  SETDATE     VARCHAR2(8),
  SETTIME     VARCHAR2(6),
  INVALIDDATE VARCHAR2(8),
  INVALIDTIME VARCHAR2(6),
  ACK         NUMBER(11) default 0,
  REPLYDES    VARCHAR2(64),
  REPLY       VARCHAR2(30),
  SUBAPP      VARCHAR2(10) not null
)
;
alter table OUT_ERROR
  add constraint OUT_ERROR_PRIMARY_1 primary key (BATCHNO, SERIALNO);

prompt
prompt Creating table OUT_ERROR_V3
prompt ===========================
prompt
create table OUT_ERROR_V3
(
  BATCHNO          VARCHAR2(14) not null,
  SERIALNO         NUMBER(8) not null,
  SEQUENCENO       NUMBER not null,
  BATCHMODE        VARCHAR2(1),
  APPSERIALNO      VARCHAR2(35),
  RETCODE          VARCHAR2(10),
  ERRMSG           VARCHAR2(60),
  STATUSFLAG       NUMBER,
  SERVICEID        VARCHAR2(20),
  SENDDIRECTLY     NUMBER,
  MEDIAID          VARCHAR2(3),
  SENDID           VARCHAR2(60),
  SENDERTYPE       NUMBER(1),
  RECVID           VARCHAR2(60),
  RECEIVERTYPE     NUMBER(1),
  SUBMITDATE       VARCHAR2(8),
  SUBMITTIME       VARCHAR2(6),
  FINISHDATE       VARCHAR2(8),
  FINISHTIME       VARCHAR2(6),
  REP              NUMBER,
  DOCOUNT          NUMBER,
  PRIORITY         NUMBER(1),
  CONTENTSUBJECT   VARCHAR2(50),
  CONTENT          VARCHAR2(1000),
  MSGID            VARCHAR2(20),
  TIMESETFLAG      NUMBER(1),
  SETDATE          VARCHAR2(8),
  SETTIME          VARCHAR2(6),
  INVALIDDATE      VARCHAR2(8),
  INVALIDTIME      VARCHAR2(6),
  ACK              NUMBER(1),
  REPLYDESTINATION VARCHAR2(60),
  NEEDREPLY        NUMBER(1),
  FEESERVICENO     VARCHAR2(20),
  FEE              NUMBER,
  FEETYPE          NUMBER(1),
  UMSFLAG          NUMBER,
  COMPANYID        VARCHAR2(20),
  PROP1            VARCHAR2(20),
  PROP2            VARCHAR2(20),
  PROP3            VARCHAR2(20),
  PROP4            VARCHAR2(20),
  PROP5            VARCHAR2(20),
  SEQKEY           VARCHAR2(32),
  SENDERIDTYPE     VARCHAR2(32),
  RECEIVERIDTYPE   VARCHAR2(32),
  CONTENTMODE      NUMBER
)
;

prompt
prompt Creating table OUT_OK
prompt =====================
prompt
create table OUT_OK
(
  BATCHNO     VARCHAR2(42) default '' not null,
  SERIALNO    NUMBER(11) default 1 not null,
  SEQUENCENO  NUMBER(11) default 1 not null,
  RETCODE     VARCHAR2(12),
  ERRMSG      VARCHAR2(60),
  STATUSFLAG  NUMBER(11) default 0 not null,
  APPID       VARCHAR2(33),
  APPSERIALNO VARCHAR2(100),
  MEDIAID     VARCHAR2(9),
  SENDID      VARCHAR2(36),
  RECVID      VARCHAR2(225),
  SUBMITDATE  VARCHAR2(24),
  SUBMITTIME  VARCHAR2(18),
  FINISHDATE  VARCHAR2(24),
  FINISHTIME  VARCHAR2(18),
  REP         NUMBER(11) default 0,
  DOCOUNT     NUMBER(11) default 0,
  PRIORITY    NUMBER(11) default 0,
  BATCHMODE   VARCHAR2(3),
  CONTENTMODE VARCHAR2(3),
  CONTENT     VARCHAR2(160),
  TIMESETFLAG VARCHAR2(1),
  SETDATE     VARCHAR2(24),
  SETTIME     VARCHAR2(18),
  INVALIDDATE VARCHAR2(24),
  INVALIDTIME VARCHAR2(18),
  ACK         NUMBER(11) default 0,
  REPLYDES    VARCHAR2(64),
  REPLY       VARCHAR2(30),
  FEE         NUMBER(11) default 0,
  FEETYPE     NUMBER(11) default 0,
  SUBAPP      VARCHAR2(10),
  MSGID       VARCHAR2(30)
)
;
alter table OUT_OK
  add constraint OUT_OK_PRIMARY_1 primary key (BATCHNO, SERIALNO);

prompt
prompt Creating table OUT_OK_V3
prompt ========================
prompt
create table OUT_OK_V3
(
  BATCHNO          VARCHAR2(14) not null,
  SERIALNO         NUMBER(8) not null,
  SEQUENCENO       NUMBER not null,
  BATCHMODE        VARCHAR2(1),
  APPSERIALNO      VARCHAR2(35),
  RETCODE          VARCHAR2(10),
  ERRMSG           VARCHAR2(60),
  STATUSFLAG       NUMBER,
  SERVICEID        VARCHAR2(20),
  SENDDIRECTLY     NUMBER,
  MEDIAID          VARCHAR2(3),
  SENDID           VARCHAR2(60),
  SENDERTYPE       NUMBER(1),
  RECVID           VARCHAR2(60),
  RECEIVERTYPE     NUMBER(1),
  SUBMITDATE       VARCHAR2(8),
  SUBMITTIME       VARCHAR2(6),
  FINISHDATE       VARCHAR2(8),
  FINISHTIME       VARCHAR2(6),
  REP              NUMBER,
  DOCOUNT          NUMBER,
  PRIORITY         NUMBER(1),
  CONTENTSUBJECT   VARCHAR2(50),
  CONTENT          VARCHAR2(1000),
  MSGID            VARCHAR2(20),
  TIMESETFLAG      NUMBER(1),
  SETDATE          VARCHAR2(8),
  SETTIME          VARCHAR2(6),
  INVALIDDATE      VARCHAR2(8),
  INVALIDTIME      VARCHAR2(6),
  ACK              NUMBER(1),
  REPLYDESTINATION VARCHAR2(60),
  NEEDREPLY        NUMBER(1),
  FEESERVICENO     VARCHAR2(20),
  FEE              NUMBER,
  FEETYPE          NUMBER(1),
  UMSFLAG          NUMBER,
  COMPANYID        VARCHAR2(20),
  PROP1            VARCHAR2(20),
  PROP2            VARCHAR2(20),
  PROP3            VARCHAR2(20),
  PROP4            VARCHAR2(20),
  PROP5            VARCHAR2(20),
  SEQKEY           VARCHAR2(32),
  SENDERIDTYPE     VARCHAR2(32),
  RECEIVERIDTYPE   VARCHAR2(32),
  CONTENTMODE      NUMBER
)
;

prompt
prompt Creating table OUT_READY
prompt ========================
prompt
create table OUT_READY
(
  BATCHNO     VARCHAR2(42) default '' not null,
  SERIALNO    NUMBER(11) default 1 not null,
  SEQUENCENO  NUMBER(11) default 1 not null,
  RETCODE     VARCHAR2(12),
  ERRMSG      VARCHAR2(60),
  STATUSFLAG  NUMBER(11) default 0,
  APPID       VARCHAR2(12),
  APPSERIALNO VARCHAR2(35),
  MEDIAID     VARCHAR2(9),
  SENDID      VARCHAR2(60),
  RECVID      VARCHAR2(255),
  SUBMITDATE  VARCHAR2(24),
  SUBMITTIME  VARCHAR2(24),
  FINISHDATE  VARCHAR2(24),
  FINISHTIME  VARCHAR2(24),
  REP         NUMBER(11) default 0,
  DOCOUNT     NUMBER(11) default 0,
  PRIORITY    NUMBER(11) default 0,
  BATCHMODE   VARCHAR2(3),
  CONTENTMODE VARCHAR2(3),
  CONTENT     VARCHAR2(160),
  TIMESETFLAG VARCHAR2(3),
  SETDATE     VARCHAR2(24),
  SETTIME     VARCHAR2(24),
  INVALIDDATE VARCHAR2(24),
  ACK         NUMBER(11) default 0,
  REPLYDES    VARCHAR2(64),
  REPLY       VARCHAR2(30),
  FEE         NUMBER(11) default 0,
  FEETYPE     NUMBER(11) default 0,
  INVALIDTIME VARCHAR2(20),
  SUBAPP      VARCHAR2(20),
  MSGID       VARCHAR2(20)
)
;
alter table OUT_READY
  add constraint OUT_READY_PRIMARY_1 primary key (BATCHNO, SERIALNO);

prompt
prompt Creating table OUT_READY_V3
prompt ===========================
prompt
create table OUT_READY_V3
(
  BATCHNO          VARCHAR2(14) not null,
  SERIALNO         NUMBER(8) not null,
  SEQUENCENO       NUMBER not null,
  BATCHMODE        VARCHAR2(1),
  APPSERIALNO      VARCHAR2(35),
  RETCODE          VARCHAR2(10),
  ERRMSG           VARCHAR2(60),
  STATUSFLAG       NUMBER,
  SERVICEID        VARCHAR2(20),
  SENDDIRECTLY     NUMBER,
  MEDIAID          VARCHAR2(3),
  SENDID           VARCHAR2(60),
  SENDERTYPE       NUMBER(1),
  RECVID           VARCHAR2(60),
  RECEIVERTYPE     NUMBER(1),
  SUBMITDATE       VARCHAR2(8),
  SUBMITTIME       VARCHAR2(6),
  FINISHDATE       VARCHAR2(8),
  FINISHTIME       VARCHAR2(6),
  REP              NUMBER,
  DOCOUNT          NUMBER,
  PRIORITY         NUMBER(1),
  CONTENTSUBJECT   VARCHAR2(50),
  CONTENT          VARCHAR2(1000),
  MSGID            VARCHAR2(20),
  TIMESETFLAG      NUMBER(1),
  SETDATE          VARCHAR2(8),
  SETTIME          VARCHAR2(6),
  INVALIDDATE      VARCHAR2(8),
  INVALIDTIME      VARCHAR2(6),
  ACK              NUMBER(1),
  REPLYDESTINATION VARCHAR2(60),
  NEEDREPLY        NUMBER(1),
  FEESERVICENO     VARCHAR2(20),
  FEE              NUMBER,
  FEETYPE          NUMBER(1),
  UMSFLAG          NUMBER(1),
  COMPANYID        VARCHAR2(20),
  PROP1            VARCHAR2(20),
  PROP2            VARCHAR2(20),
  PROP3            VARCHAR2(20),
  PROP4            VARCHAR2(20),
  PROP5            VARCHAR2(20),
  SEQKEY           VARCHAR2(32),
  SENDERIDTYPE     VARCHAR2(32),
  RECEIVERIDTYPE   VARCHAR2(32),
  CONTENTMODE      NUMBER
)
;
comment on column OUT_READY_V3.SENDERIDTYPE
  is '��ʾ������id��Ӧ������';
comment on column OUT_READY_V3.RECEIVERIDTYPE
  is '������id��Ӧ������';
comment on column OUT_READY_V3.CONTENTMODE
  is '���뷽ʽ';
alter table OUT_READY_V3
  add constraint PK_OUT_READY_V3 primary key (BATCHNO, SERIALNO, SEQUENCENO);
create index UMS_ACK_3E1D39E1 on OUT_READY_V3 (ACK);
create index UMS_APPSERIALNO_3E1D39E1 on OUT_READY_V3 (APPSERIALNO);
create index UMS_BATCHMODE_3E1D39E1 on OUT_READY_V3 (BATCHMODE);
create index UMS_COMPANYID_3E1D39E1 on OUT_READY_V3 (COMPANYID);
create index UMS_CONTENTSUBJECT_3E1D39E on OUT_READY_V3 (CONTENTSUBJECT);
create index UMS_DOCOUNT_3E1D39E1 on OUT_READY_V3 (DOCOUNT);
create index UMS_ERRMSG_3E1D39E1 on OUT_READY_V3 (ERRMSG);
create index UMS_FEESERVICENO_3E1D39E1 on OUT_READY_V3 (FEESERVICENO);
create index UMS_FEETYPE_3E1D39E1 on OUT_READY_V3 (FEETYPE);
create index UMS_FEE_3E1D39E1 on OUT_READY_V3 (FEE);
create index UMS_FINISHDATE_3E1D39E1 on OUT_READY_V3 (FINISHDATE);
create index UMS_FINISHTIME_3E1D39E1 on OUT_READY_V3 (FINISHTIME);
create index UMS_INVALIDDATE_3E1D39E1 on OUT_READY_V3 (INVALIDDATE);
create index UMS_INVALIDTIME_3E1D39E1 on OUT_READY_V3 (INVALIDTIME);
create index UMS_MEDIAID_3E1D39E1 on OUT_READY_V3 (MEDIAID);
create index UMS_MSGID_3E1D39E1 on OUT_READY_V3 (MSGID);
create index UMS_NEEDREPLY_3E1D39E1 on OUT_READY_V3 (NEEDREPLY);
create index UMS_PRIORITY_3E1D39E1 on OUT_READY_V3 (PRIORITY);
create index UMS_PROP1_3E1D39E1 on OUT_READY_V3 (PROP1);
create index UMS_PROP2_3E1D39E1 on OUT_READY_V3 (PROP2);
create index UMS_PROP3_3E1D39E1 on OUT_READY_V3 (PROP3);
create index UMS_PROP4_3E1D39E1 on OUT_READY_V3 (PROP4);
create index UMS_PROP5_3E1D39E1 on OUT_READY_V3 (PROP5);
create index UMS_RECEIVERTYPE_3E1D39E1 on OUT_READY_V3 (RECEIVERTYPE);
create index UMS_RECVID_3E1D39E1 on OUT_READY_V3 (RECVID);
create index UMS_REPLYDESTINATION_3E1D3 on OUT_READY_V3 (REPLYDESTINATION);
create index UMS_REP_3E1D39E1 on OUT_READY_V3 (REP);
create index UMS_RETCODE_3E1D39E1 on OUT_READY_V3 (RETCODE);
create index UMS_SENDDIRECTLY_3E1D39E1 on OUT_READY_V3 (SENDDIRECTLY);
create index UMS_SENDERTYPE_3E1D39E1 on OUT_READY_V3 (SENDERTYPE);
create index UMS_SENDID_3E1D39E1 on OUT_READY_V3 (SENDID);
create index UMS_SEQUENCENO_3E1D39E1 on OUT_READY_V3 (SEQUENCENO);
create index UMS_SERIALNO_3E1D39E1 on OUT_READY_V3 (SERIALNO);
create index UMS_SERVICEID_3E1D39E1 on OUT_READY_V3 (SERVICEID);
create index UMS_SETDATE_3E1D39E1 on OUT_READY_V3 (SETDATE);
create index UMS_SETTIME_3E1D39E1 on OUT_READY_V3 (SETTIME);
create index UMS_STATUSFLAG_3E1D39E1 on OUT_READY_V3 (STATUSFLAG);
create index UMS_SUBMITDATE_3E1D39E1 on OUT_READY_V3 (SUBMITDATE);
create index UMS_SUBMITTIME_3E1D39E1 on OUT_READY_V3 (SUBMITTIME);
create index UMS_TIMESETFLAG_3E1D39E1 on OUT_READY_V3 (TIMESETFLAG);
create index UMS_UMSFLAG_3E1D39E1 on OUT_READY_V3 (UMSFLAG);

prompt
prompt Creating table OUT_REPLY
prompt ========================
prompt
create table OUT_REPLY
(
  BATCHNO     VARCHAR2(14) default '' not null,
  SERIALNO    NUMBER(11) default 1 not null,
  SEQUENCENO  NUMBER(11) default 1 not null,
  RETCODE     VARCHAR2(4),
  ERRMSG      VARCHAR2(60),
  STATUSFLAG  NUMBER(11),
  APPID       VARCHAR2(12),
  APPSERIALNO VARCHAR2(35),
  MEDIAID     VARCHAR2(3),
  SENDID      VARCHAR2(12),
  RECVID      VARCHAR2(255),
  SUBMITDATE  VARCHAR2(8),
  SUBMITTIME  VARCHAR2(6),
  FINISHDATE  VARCHAR2(8),
  FINISHTIME  VARCHAR2(6),
  REP         NUMBER(11) default 0,
  DOCOUNT     NUMBER(11) default 0,
  PRIORITY    NUMBER(11) default 0,
  BATCHMODE   VARCHAR2(1),
  CONTENTMODE VARCHAR2(1),
  CONTENT     VARCHAR2(160),
  TIMESETFLAG VARCHAR2(1),
  SETDATE     VARCHAR2(8),
  SETTIME     VARCHAR2(6),
  INVALIDDATE VARCHAR2(8),
  INVALIDTIME VARCHAR2(6),
  ACK         NUMBER(11) default 0,
  REPLYDES    VARCHAR2(64),
  REPLY       VARCHAR2(30),
  SUBAPP      VARCHAR2(10) not null
)
;
alter table OUT_REPLY
  add constraint OUT_REPLY_PRIMARY_1 primary key (BATCHNO, SERIALNO);

prompt
prompt Creating table OUT_REPLY_V3
prompt ===========================
prompt
create table OUT_REPLY_V3
(
  BATCHNO          VARCHAR2(14) not null,
  SERIALNO         NUMBER(8) not null,
  SEQUENCENO       NUMBER not null,
  BATCHMODE        VARCHAR2(1),
  APPSERIALNO      VARCHAR2(35),
  RETCODE          VARCHAR2(10),
  ERRMSG           VARCHAR2(60),
  STATUSFLAG       NUMBER,
  SERVICEID        VARCHAR2(20),
  SENDDIRECTLY     NUMBER,
  MEDIAID          VARCHAR2(3),
  SENDID           VARCHAR2(60),
  SENDERTYPE       NUMBER(1),
  RECVID           VARCHAR2(60),
  RECEIVERTYPE     NUMBER(1),
  SUBMITDATE       VARCHAR2(8),
  SUBMITTIME       VARCHAR2(6),
  FINISHDATE       VARCHAR2(8),
  FINISHTIME       VARCHAR2(6),
  REP              NUMBER,
  DOCOUNT          NUMBER,
  PRIORITY         NUMBER(1),
  CONTENTSUBJECT   VARCHAR2(50),
  CONTENT          VARCHAR2(1000),
  MSGID            VARCHAR2(20),
  TIMESETFLAG      NUMBER(1),
  SETDATE          VARCHAR2(8),
  SETTIME          VARCHAR2(6),
  INVALIDDATE      VARCHAR2(8),
  INVALIDTIME      VARCHAR2(6),
  ACK              NUMBER(1),
  REPLYDESTINATION VARCHAR2(60),
  NEEDREPLY        NUMBER(1),
  FEESERVICENO     VARCHAR2(20),
  FEE              NUMBER,
  FEETYPE          NUMBER(1),
  UMSFLAG          NUMBER,
  COMPANYID        VARCHAR2(20),
  PROP1            VARCHAR2(20),
  PROP2            VARCHAR2(20),
  PROP3            VARCHAR2(20),
  PROP4            VARCHAR2(20),
  PROP5            VARCHAR2(20),
  SEQKEY           VARCHAR2(32),
  SENDERIDTYPE     VARCHAR2(32),
  RECEIVERIDTYPE   VARCHAR2(32),
  CONTENTMODE      NUMBER
)
;

prompt
prompt Creating table PLAN_TABLE
prompt =========================
prompt
create table PLAN_TABLE
(
  STATEMENT_ID    VARCHAR2(30),
  TIMESTAMP       DATE,
  REMARKS         VARCHAR2(80),
  OPERATION       VARCHAR2(30),
  OPTIONS         VARCHAR2(30),
  OBJECT_NODE     VARCHAR2(128),
  OBJECT_OWNER    VARCHAR2(30),
  OBJECT_NAME     VARCHAR2(30),
  OBJECT_INSTANCE INTEGER,
  OBJECT_TYPE     VARCHAR2(30),
  OPTIMIZER       VARCHAR2(255),
  SEARCH_COLUMNS  NUMBER,
  ID              INTEGER,
  PARENT_ID       INTEGER,
  POSITION        INTEGER,
  COST            INTEGER,
  CARDINALITY     INTEGER,
  BYTES           INTEGER,
  OTHER_TAG       VARCHAR2(255),
  PARTITION_START VARCHAR2(255),
  PARTITION_STOP  VARCHAR2(255),
  PARTITION_ID    INTEGER,
  OTHER           LONG,
  DISTRIBUTION    VARCHAR2(30),
  CPU_COST        INTEGER,
  IO_COST         INTEGER,
  TEMP_SPACE      INTEGER
)
;

prompt
prompt Creating table QFXX
prompt ===================
prompt
create table QFXX
(
  MSGCONTENTSUBJECT VARCHAR2(100),
  MSGCONTENTCONTENT VARCHAR2(1000),
  SERVICEID         VARCHAR2(32),
  SENDDIRECTLY      NUMBER(4),
  MEDIAID           VARCHAR2(32),
  SUBMITDATE        VARCHAR2(32),
  SUBMITTIME        VARCHAR2(32),
  PRIORITY          NUMBER(4) default -1,
  ACK               NUMBER(4) default -1,
  TIMESETFLAG       NUMBER(4) default -1,
  INVALIDDATE       VARCHAR2(32),
  INVALIDTIME       VARCHAR2(32),
  REPLYDESTINATION  VARCHAR2(32),
  NEEDREPLY         NUMBER(4) default -1,
  FEESERVICENO      VARCHAR2(32),
  UMSFLAG           NUMBER(4) default -1,
  COMPANYID         VARCHAR2(32),
  SEQKEY            VARCHAR2(32) not null,
  APPSERIALNO       VARCHAR2(32),
  DSDATE            DATE,
  DSHH              VARCHAR2(32),
  DSMM              VARCHAR2(32)
)
;
alter table QFXX
  add constraint QFXX_SEQKEY primary key (SEQKEY);

prompt
prompt Creating table QFXX_EAMIL
prompt =========================
prompt
create table QFXX_EAMIL
(
  MSGCONTENTSUBJECT VARCHAR2(100),
  MSGCONTENTCONTENT VARCHAR2(1000),
  SERVICEID         VARCHAR2(32),
  SENDDIRECTLY      NUMBER(4),
  MEDIAID           VARCHAR2(32),
  SUBMITDATE        VARCHAR2(32),
  SUBMITTIME        VARCHAR2(32),
  PRIORITY          NUMBER(4) default -1,
  ACK               NUMBER(4) default -1,
  TIMESETFLAG       NUMBER(4) default -1,
  INVALIDDATE       VARCHAR2(32),
  INVALIDTIME       VARCHAR2(32),
  REPLYDESTINATION  VARCHAR2(32),
  NEEDREPLY         NUMBER(4) default -1,
  FEESERVICENO      VARCHAR2(32),
  UMSFLAG           NUMBER(4) default -1,
  COMPANYID         VARCHAR2(32),
  SEQKEY            VARCHAR2(32) not null,
  APPSERIALNO       VARCHAR2(32)
)
;

prompt
prompt Creating table QFXX_MSGATTACHMENT
prompt =================================
prompt
create table QFXX_MSGATTACHMENT
(
  SEQKEY   VARCHAR2(32) not null,
  FILENAME VARCHAR2(100),
  XXID     VARCHAR2(32)
)
;
alter table QFXX_MSGATTACHMENT
  add constraint QFXX_MSGATTACHMENT_SEQKEY primary key (SEQKEY);

prompt
prompt Creating table QFXX_RECEIVERS
prompt =============================
prompt
create table QFXX_RECEIVERS
(
  SEQKEY          VARCHAR2(32) not null,
  PARTICIPANTID   VARCHAR2(32),
  IDTYPE          VARCHAR2(32),
  PARTICIPANTTYPE VARCHAR2(32),
  XXID            VARCHAR2(32)
)
;
alter table QFXX_RECEIVERS
  add constraint QFXX_RECEIVERS_SEQKEY primary key (SEQKEY);

prompt
prompt Creating table QFXX_SENDER
prompt ==========================
prompt
create table QFXX_SENDER
(
  SEQKEY          VARCHAR2(32) not null,
  PARTICIPANTID   VARCHAR2(32),
  IDTYPE          VARCHAR2(32),
  PARTICIPANTTYPE VARCHAR2(32),
  XXID            VARCHAR2(32)
)
;
alter table QFXX_SENDER
  add constraint QFXX_SENDER_SEQKEY primary key (SEQKEY);

prompt
prompt Creating table SERIAL
prompt =====================
prompt
create table SERIAL
(
  SERIALNO   NUMBER(11) default 1 not null,
  SERIALTYPE VARCHAR2(12) default '' not null
)
;
alter table SERIAL
  add constraint SERIAL_PRIMARY_1 primary key (SERIALTYPE);

prompt
prompt Creating table SERVICE_V3
prompt =========================
prompt
create table SERVICE_V3
(
  SERVICEID     VARCHAR2(20) not null,
  SERVICENAME   VARCHAR2(50),
  APPID         VARCHAR2(12),
  SERVICEDESC   VARCHAR2(1000),
  SERVICEADDR   VARCHAR2(100),
  PORT          VARCHAR2(6),
  STATUS        VARCHAR2(50),
  SERVICETYPE   NUMBER(1),
  DIRECTIONTYPE NUMBER(1),
  PROP1         VARCHAR2(50),
  PROP2         VARCHAR2(50),
  PROP3         VARCHAR2(50),
  PROP4         VARCHAR2(50),
  PROP5         VARCHAR2(50),
  SEQKEY        VARCHAR2(32) not null
)
;
alter table SERVICE_V3
  add constraint SERVICE_V3_SEQKEY primary key (SEQKEY);

prompt
prompt Creating table SYSTEM_SERVER
prompt ============================
prompt
create table SYSTEM_SERVER
(
  SYSTEMNAME VARCHAR2(50),
  OWNERNAME  VARCHAR2(50),
  SERVERIP   VARCHAR2(50),
  SERVERPORT NUMBER,
  CONSOLEIP  VARCHAR2(50)
)
;

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
;
alter table T_JFW_APP
  add constraint PK_T_JFW_APP_1_1 primary key (APPID);

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
  BASEVALUE       VARCHAR2(16),
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
  APPID           VARCHAR2(100),
  FUNCTIONMODEL   VARCHAR2(50),
  CREATEID        VARCHAR2(32),
  CREATEDATE      DATE,
  UPDATEID        VARCHAR2(32),
  UPDATEDATE      DATE
)
;
alter table T_JFW_CODEMODEL
  add constraint JFW_CODEMODEL_1 primary key (MODELNAME);

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
;
alter table T_JFW_CODE_COMPLEX
  add primary key (SEQKEY);

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
  CODEPY      VARCHAR2(16),
  DESCRIPTION VARCHAR2(64),
  CREATEID    VARCHAR2(32),
  CREATEDATE  DATE,
  UPDATEID    VARCHAR2(32),
  UPDATEDATE  DATE
)
;
alter table T_JFW_CODE_DATA
  add constraint JFW_CODE_DATA_1 primary key (SEQKEY);

prompt
prompt Creating table T_JFW_DATAMODEL
prompt ==============================
prompt
create table T_JFW_DATAMODEL
(
  MODELNAME       VARCHAR2(64) not null,
  TABLENAME       VARCHAR2(32),
  TABLETYPE       VARCHAR2(8),
  DATASOURCE      VARCHAR2(64),
  DESCRIPTION     VARCHAR2(64),
  DEFAULTORDERBY  VARCHAR2(64),
  EVENTLOG        VARCHAR2(8),
  ABASTRACTMODEL  VARCHAR2(64),
  PROPVALUETABLE  VARCHAR2(32),
  SOURCEFLAG      VARCHAR2(8),
  APPID           VARCHAR2(50),
  DEFAULTPOLICYID VARCHAR2(32),
  CREATEID        VARCHAR2(32),
  CREATEDATE      DATE,
  UPDATEID        VARCHAR2(32),
  UPDATEDATE      DATE
)
;
alter table T_JFW_DATAMODEL
  add constraint PK_T_JFW_DATAMODEL_1_1 primary key (MODELNAME);

prompt
prompt Creating table T_JFW_FIELDINFO
prompt ==============================
prompt
create table T_JFW_FIELDINFO
(
  MODELNAME     VARCHAR2(64) not null,
  FIELDNAME     VARCHAR2(64) not null,
  FIELDTYPE     VARCHAR2(16),
  DISPLAYNAME   VARCHAR2(128),
  CODETYPE      VARCHAR2(64),
  LENGTH        NUMBER,
  KEYFLAG       VARCHAR2(8),
  GENERATETYPE  VARCHAR2(16),
  COLUMNS       VARCHAR2(8),
  ISEMPTY       VARCHAR2(8),
  DEFAULTVALUE  VARCHAR2(64),
  DISPLAYFORMAT VARCHAR2(32),
  MAXVALUE      VARCHAR2(126),
  MINVALUE      VARCHAR2(126),
  STATE         VARCHAR2(8),
  DEFAULTCTRL   VARCHAR2(10),
  DISPLAYORDER  NUMBER(10),
  CHECKVALUE    VARCHAR2(32),
  CREATEID      VARCHAR2(32),
  CREATEDATE    DATE,
  UPDATEID      VARCHAR2(32),
  UPDATEDATE    DATE
)
;
alter table T_JFW_FIELDINFO
  add constraint T_JFW_FIELDIN_PK111659_1 primary key (MODELNAME, FIELDNAME);

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
  UPDATEDATE    DATE
)
;
alter table T_JFW_FUNCTION
  add primary key (SEQKEY);

prompt
prompt Creating table T_JFW_FUNCTIONVERSION
prompt ====================================
prompt
create table T_JFW_FUNCTIONVERSION
(
  SEQKEY     VARCHAR2(32) not null,
  MODELNAME  VARCHAR2(100),
  COMPANYID  VARCHAR2(32),
  FILEPATH   VARCHAR2(100),
  CREATEID   VARCHAR2(32),
  CREATEDATE DATE,
  UPDATEID   VARCHAR2(32),
  UPDATEDATE DATE
)
;
alter table T_JFW_FUNCTIONVERSION
  add primary key (SEQKEY);

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
;
alter table T_JFW_RULE_PARAM
  add constraint PK_T_JFW_RULE_PARAM primary key (ID);

prompt
prompt Creating table T_JFW_RULE_SET
prompt =============================
prompt
create table T_JFW_RULE_SET
(
  UNIT_ID     VARCHAR2(10),
  SET_CODE    VARCHAR2(10) not null,
  SET_NAME    VARCHAR2(50),
  DESCRIPTION VARCHAR2(50),
  CREATE_ID   VARCHAR2(10),
  CREATE_DATE DATE,
  BEGIN_DATE  DATE,
  END_DATE    DATE
)
;
alter table T_JFW_RULE_SET
  add constraint PK_T_JFW_RULE_SET primary key (SET_CODE);

prompt
prompt Creating table T_JFW_RULE_SET_PARAM
prompt ===================================
prompt
create table T_JFW_RULE_SET_PARAM
(
  SET_CODE    VARCHAR2(10) not null,
  PARAM_CODE  VARCHAR2(30) not null,
  PARAM_NAME  VARCHAR2(50),
  DESCRIPTION VARCHAR2(50),
  TYPE        NUMBER,
  WIDTH       NUMBER,
  SCALE_WIDTH NUMBER,
  FORMAT      VARCHAR2(50)
)
;
alter table T_JFW_RULE_SET_PARAM
  add constraint PK_T_JFW_RULE_SET_PARAM primary key (SET_CODE, PARAM_CODE);

prompt
prompt Creating table T_JFW_RULE_SET_RULE2
prompt ===================================
prompt
create table T_JFW_RULE_SET_RULE2
(
  SET_CODE            VARCHAR2(10) not null,
  RULE_ID             VARCHAR2(10) not null,
  RULE_ORDER          NUMBER,
  CONDITION           VARCHAR2(200),
  PASS_EXPRESSION     VARCHAR2(200),
  NOT_PASS_EXPRESSION VARCHAR2(200),
  RULE_NAME           VARCHAR2(50),
  DESCRIPTION         VARCHAR2(50)
)
;
alter table T_JFW_RULE_SET_RULE2
  add constraint PK_T_JFW_RULE_SET_RULE primary key (SET_CODE, RULE_ID);

prompt
prompt Creating table T_JFW_TOOLBARMENU
prompt ================================
prompt
create table T_JFW_TOOLBARMENU
(
  MODULENAME   VARCHAR2(50),
  DESCRIPTION  VARCHAR2(100),
  EVENT        VARCHAR2(20),
  SRC          VARCHAR2(100),
  PARENTMENU   VARCHAR2(20),
  GROUPNAME    VARCHAR2(50),
  SEQKEY       VARCHAR2(16) not null,
  URL          VARCHAR2(300),
  DISPLAYORDER NUMBER(10),
  WINDOW       VARCHAR2(40),
  VALUENAME    VARCHAR2(100),
  PARAMS       VARCHAR2(100),
  LINKMODULE   VARCHAR2(100),
  STATE        NUMBER(4)
)
;
alter table T_JFW_TOOLBARMENU
  add primary key (SEQKEY);

prompt
prompt Creating table UMS_CT_BRZT
prompt ==========================
prompt
create table UMS_CT_BRZT
(
  SEQKEY       VARCHAR2(16) not null,
  CODENAME     VARCHAR2(50),
  CODEVALUE    VARCHAR2(50),
  CODELEVEL    NUMBER(4),
  PARENTID     VARCHAR2(16),
  ISLEAF       NUMBER(4),
  PY           VARCHAR2(50),
  DISPLAYORDER NUMBER(4)
)
;

prompt
prompt Creating table UMS_CT_DSHH
prompt ==========================
prompt
create table UMS_CT_DSHH
(
  SEQKEY       VARCHAR2(16) not null,
  CODENAME     VARCHAR2(50),
  CODEVALUE    VARCHAR2(50),
  CODELEVEL    NUMBER(4),
  PARENTID     VARCHAR2(16),
  ISLEAF       NUMBER(4),
  PY           VARCHAR2(50),
  DISPLAYORDER NUMBER(4)
)
;

prompt
prompt Creating table UMS_CT_DSMM
prompt ==========================
prompt
create table UMS_CT_DSMM
(
  SEQKEY       VARCHAR2(16) not null,
  CODENAME     VARCHAR2(50),
  CODEVALUE    VARCHAR2(50),
  CODELEVEL    NUMBER(4),
  PARENTID     VARCHAR2(16),
  ISLEAF       NUMBER(4),
  PY           VARCHAR2(50),
  DISPLAYORDER NUMBER(4)
)
;

prompt
prompt Creating table UMS_CT_FFFSFX
prompt ============================
prompt
create table UMS_CT_FFFSFX
(
  SEQKEY       VARCHAR2(16) not null,
  CODENAME     VARCHAR2(50),
  CODEVALUE    VARCHAR2(50),
  CODELEVEL    NUMBER(4),
  PARENTID     VARCHAR2(16),
  ISLEAF       NUMBER(4),
  PY           VARCHAR2(50),
  DISPLAYORDER NUMBER(4)
)
;

prompt
prompt Creating table UMS_CT_FFLX
prompt ==========================
prompt
create table UMS_CT_FFLX
(
  SEQKEY       VARCHAR2(16) not null,
  CODENAME     VARCHAR2(50),
  CODEVALUE    VARCHAR2(50),
  CODELEVEL    NUMBER(4),
  PARENTID     VARCHAR2(16),
  ISLEAF       NUMBER(4),
  PY           VARCHAR2(50),
  DISPLAYORDER NUMBER(4)
)
;

prompt
prompt Creating table UMS_CT_FFZT
prompt ==========================
prompt
create table UMS_CT_FFZT
(
  SEQKEY       VARCHAR2(16) not null,
  CODENAME     VARCHAR2(50),
  CODEVALUE    VARCHAR2(50),
  CODELEVEL    NUMBER(4),
  PARENTID     VARCHAR2(16),
  ISLEAF       NUMBER(4),
  PY           VARCHAR2(50),
  DISPLAYORDER NUMBER(4)
)
;

prompt
prompt Creating table UMS_CT_FILTER
prompt ============================
prompt
create table UMS_CT_FILTER
(
  SEQKEY       VARCHAR2(16) not null,
  CODENAME     VARCHAR2(50),
  CODEVALUE    NUMBER(4),
  CODELEVEL    NUMBER(4),
  PARENTID     VARCHAR2(16),
  ISLEAF       NUMBER(4),
  PY           VARCHAR2(50),
  DISPLAYORDER NUMBER(4)
)
;

prompt
prompt Creating table UMS_CT_FYLX
prompt ==========================
prompt
create table UMS_CT_FYLX
(
  SEQKEY       VARCHAR2(16) not null,
  CODENAME     VARCHAR2(50),
  CODEVALUE    VARCHAR2(50),
  CODELEVEL    NUMBER(4),
  PARENTID     VARCHAR2(16),
  ISLEAF       NUMBER(4),
  PY           VARCHAR2(50),
  DISPLAYORDER NUMBER(4)
)
;

prompt
prompt Creating table UMS_CT_JMS
prompt =========================
prompt
create table UMS_CT_JMS
(
  SEQKEY       VARCHAR2(16) not null,
  CODENAME     VARCHAR2(50),
  CODEVALUE    VARCHAR2(50),
  CODELEVEL    NUMBER(4),
  PARENTID     VARCHAR2(16),
  ISLEAF       NUMBER(4),
  PY           VARCHAR2(50),
  DISPLAYORDER NUMBER(4)
)
;

prompt
prompt Creating table UMS_CT_MEDIASTYLE
prompt ================================
prompt
create table UMS_CT_MEDIASTYLE
(
  SEQKEY       VARCHAR2(16) not null,
  CODENAME     VARCHAR2(50),
  CODEVALUE    VARCHAR2(50),
  CODELEVEL    NUMBER(4),
  PARENTID     VARCHAR2(16),
  ISLEAF       NUMBER(4),
  PY           VARCHAR2(50),
  DISPLAYORDER NUMBER(4)
)
;

prompt
prompt Creating table UMS_CT_OUT_READY_ACK
prompt ===================================
prompt
create table UMS_CT_OUT_READY_ACK
(
  SEQKEY       VARCHAR2(16) not null,
  CODENAME     VARCHAR2(50),
  CODEVALUE    VARCHAR2(50),
  CODELEVEL    NUMBER(4),
  PARENTID     VARCHAR2(16),
  ISLEAF       NUMBER(4),
  PY           VARCHAR2(50),
  DISPLAYORDER NUMBER(4)
)
;

prompt
prompt Creating table UMS_CT_OUT_READY_NEEDREPLY
prompt =========================================
prompt
create table UMS_CT_OUT_READY_NEEDREPLY
(
  SEQKEY       VARCHAR2(16) not null,
  CODENAME     VARCHAR2(50),
  CODEVALUE    VARCHAR2(50),
  CODELEVEL    NUMBER(4),
  PARENTID     VARCHAR2(16),
  ISLEAF       NUMBER(4),
  PY           VARCHAR2(50),
  DISPLAYORDER NUMBER(4)
)
;

prompt
prompt Creating table UMS_CT_OUT_READY_PRIORITY
prompt ========================================
prompt
create table UMS_CT_OUT_READY_PRIORITY
(
  SEQKEY       VARCHAR2(16) not null,
  CODENAME     VARCHAR2(50),
  CODEVALUE    VARCHAR2(50),
  CODELEVEL    NUMBER(4),
  PARENTID     VARCHAR2(16),
  ISLEAF       NUMBER(4),
  PY           VARCHAR2(50),
  DISPLAYORDER NUMBER(4)
)
;

prompt
prompt Creating table UMS_CT_OUT_READY_SENDDIRECTLY
prompt ============================================
prompt
create table UMS_CT_OUT_READY_SENDDIRECTLY
(
  SEQKEY       VARCHAR2(16) not null,
  CODENAME     VARCHAR2(50),
  CODEVALUE    VARCHAR2(50),
  CODELEVEL    NUMBER(4),
  PARENTID     VARCHAR2(16),
  ISLEAF       NUMBER(4),
  PY           VARCHAR2(50),
  DISPLAYORDER NUMBER(4)
)
;

prompt
prompt Creating table UMS_CT_OUT_READY_TIMESETFLAG
prompt ===========================================
prompt
create table UMS_CT_OUT_READY_TIMESETFLAG
(
  SEQKEY       VARCHAR2(16) not null,
  CODENAME     VARCHAR2(50),
  CODEVALUE    VARCHAR2(50),
  CODELEVEL    NUMBER(4),
  PARENTID     VARCHAR2(16),
  ISLEAF       NUMBER(4),
  PY           VARCHAR2(50),
  DISPLAYORDER NUMBER(4)
)
;

prompt
prompt Creating table UMS_CT_OUT_READY_UMSFLAG
prompt =======================================
prompt
create table UMS_CT_OUT_READY_UMSFLAG
(
  SEQKEY       VARCHAR2(16) not null,
  CODENAME     VARCHAR2(50),
  CODEVALUE    VARCHAR2(50),
  CODELEVEL    NUMBER(4),
  PARENTID     VARCHAR2(16),
  ISLEAF       NUMBER(4),
  PY           VARCHAR2(50),
  DISPLAYORDER NUMBER(4)
)
;

prompt
prompt Creating table UMS_CT_OUT_READY_V3_BATCHMODE
prompt ============================================
prompt
create table UMS_CT_OUT_READY_V3_BATCHMODE
(
  SEQKEY       VARCHAR2(16) not null,
  CODENAME     VARCHAR2(50),
  CODEVALUE    VARCHAR2(50),
  CODELEVEL    NUMBER(4),
  PARENTID     VARCHAR2(16),
  ISLEAF       NUMBER(4),
  PY           VARCHAR2(50),
  DISPLAYORDER NUMBER(4)
)
;

prompt
prompt Creating table UMS_CT_OUT_READY_V3_STATUSFLAG
prompt =============================================
prompt
create table UMS_CT_OUT_READY_V3_STATUSFLAG
(
  SEQKEY       VARCHAR2(16) not null,
  CODENAME     VARCHAR2(50),
  CODEVALUE    VARCHAR2(50),
  CODELEVEL    NUMBER(4),
  PARENTID     VARCHAR2(16),
  ISLEAF       NUMBER(4),
  PY           VARCHAR2(50),
  DISPLAYORDER NUMBER(4)
)
;

prompt
prompt Creating table UMS_CT_QDYXJ
prompt ===========================
prompt
create table UMS_CT_QDYXJ
(
  SEQKEY       VARCHAR2(16) not null,
  CODENAME     VARCHAR2(50),
  CODEVALUE    VARCHAR2(50),
  CODELEVEL    NUMBER(4),
  PARENTID     VARCHAR2(16),
  ISLEAF       NUMBER(4),
  PY           VARCHAR2(50),
  DISPLAYORDER NUMBER(4)
)
;

prompt
prompt Creating table UMS_CT_SFZJFS
prompt ============================
prompt
create table UMS_CT_SFZJFS
(
  SEQKEY       VARCHAR2(16) not null,
  CODENAME     VARCHAR2(50),
  CODEVALUE    VARCHAR2(50),
  CODELEVEL    NUMBER(4),
  PARENTID     VARCHAR2(16),
  ISLEAF       NUMBER(4),
  PY           VARCHAR2(50),
  DISPLAYORDER NUMBER(4)
)
;

prompt
prompt Creating table UMS_CT_SJJG
prompt ==========================
prompt
create table UMS_CT_SJJG
(
  SEQKEY       VARCHAR2(16) not null,
  CODENAME     VARCHAR2(50),
  CODEVALUE    VARCHAR2(50),
  CODELEVEL    NUMBER(4),
  PARENTID     VARCHAR2(16),
  ISLEAF       NUMBER(4),
  PY           VARCHAR2(50),
  DISPLAYORDER NUMBER(4)
)
;

prompt
prompt Creating table UMS_CT_SYZT
prompt ==========================
prompt
create table UMS_CT_SYZT
(
  SEQKEY       VARCHAR2(16) not null,
  CODENAME     VARCHAR2(50),
  CODEVALUE    VARCHAR2(50),
  CODELEVEL    NUMBER(4),
  PARENTID     VARCHAR2(16),
  ISLEAF       NUMBER(4),
  PY           VARCHAR2(50),
  DISPLAYORDER NUMBER(4)
)
;

prompt
prompt Creating table UMS_CT_YYZT
prompt ==========================
prompt
create table UMS_CT_YYZT
(
  SEQKEY       VARCHAR2(16) not null,
  CODENAME     VARCHAR2(50),
  CODEVALUE    VARCHAR2(50),
  CODELEVEL    NUMBER(4),
  PARENTID     VARCHAR2(16),
  ISLEAF       NUMBER(4),
  PY           VARCHAR2(50),
  DISPLAYORDER NUMBER(4)
)
;
alter table UMS_CT_YYZT
  add constraint UMS_CT_YXZT_SEQKEY primary key (SEQKEY);

prompt
prompt Creating table UMS_EMAIL
prompt ========================
prompt
create table UMS_EMAIL
(
  CONTENT       VARCHAR2(4000),
  SUBJECT       VARCHAR2(200),
  EMAILPASSWORD VARCHAR2(32),
  EMAIL         VARCHAR2(32),
  SEQKEY        VARCHAR2(32) not null
)
;

prompt
prompt Creating table USERRECEIVEPOLICY
prompt ================================
prompt
create table USERRECEIVEPOLICY
(
  UMSUSER   VARCHAR2(100) not null,
  APPID     VARCHAR2(50),
  SERVICEID VARCHAR2(50),
  MEDIA_ID  VARCHAR2(50),
  PRIORITY  NUMBER(1),
  PROP1     VARCHAR2(50),
  PROP2     VARCHAR2(50),
  PROP3     VARCHAR2(50),
  PROP4     VARCHAR2(50),
  PROP5     VARCHAR2(50),
  SEQKEY    VARCHAR2(50)
)
;

prompt
prompt Creating table USERSUBSCRIBE
prompt ============================
prompt
create table USERSUBSCRIBE
(
  SERVICEID VARCHAR2(20) not null,
  UMSUSER   VARCHAR2(100) not null,
  SUBSTIME  VARCHAR2(50),
  PROP1     VARCHAR2(50),
  PROP2     VARCHAR2(50),
  PROP3     VARCHAR2(50),
  PROP4     VARCHAR2(50),
  PROP5     VARCHAR2(50),
  SEQKEY    VARCHAR2(50)
)
;

prompt
prompt Creating table USERUNSUBSCRIBE
prompt ==============================
prompt
create table USERUNSUBSCRIBE
(
  SERVICEID  VARCHAR2(20) not null,
  UMSUSER    VARCHAR2(100) not null,
  UNSUBSTIME VARCHAR2(50),
  PROP1      VARCHAR2(50),
  PROP2      VARCHAR2(50),
  PROP3      VARCHAR2(50),
  PROP4      VARCHAR2(50),
  PROP5      VARCHAR2(50),
  SEQKEY     VARCHAR2(50)
)
;

prompt
prompt Creating table UUM_APPLICATION
prompt ==============================
prompt
create table UUM_APPLICATION
(
  APP_ID         NUMBER not null,
  APP_SHORT_NAME VARCHAR2(20) not null,
  APP_NAME       VARCHAR2(100),
  APP_ORDER      NUMBER,
  APP_HOME_URL   VARCHAR2(255),
  APP_LOGIN_URL  VARCHAR2(255),
  APP_AUTH_URL   VARCHAR2(255),
  APP_AUTH_CODE  VARCHAR2(255),
  APP_SAME_ID    NUMBER,
  APP_NOTE       VARCHAR2(255)
)
;

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
;

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
;

prompt
prompt Creating table UUM_LOG
prompt ======================
prompt
create table UUM_LOG
(
  LOG_ID      NUMBER not null,
  LOG_TYPE    NUMBER,
  LOG_USER_ID VARCHAR2(64),
  LOG_TIME    DATE,
  LOG_NOTE    VARCHAR2(200)
)
;

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
;

prompt
prompt Creating table UUM_MENU
prompt =======================
prompt
create table UUM_MENU
(
  MENU_ID          NUMBER not null,
  MENU_APP_ID      NUMBER,
  MENU_RGT_ID      NUMBER,
  MENU_NAME        VARCHAR2(50) not null,
  MENU_URL         VARCHAR2(255),
  MENU_PARENT_ID   NUMBER not null,
  MENU_IS_LEAF     NUMBER not null,
  MENU_LAYER       VARCHAR2(255),
  MENU_ORDER       NUMBER,
  MENU_STATUS      NUMBER,
  MENU_NOTE        VARCHAR2(200),
  MENU_CREATE_ID   VARCHAR2(64),
  MENU_CREATE_DATE DATE,
  MENU_UPDATE_ID   VARCHAR2(64),
  MENU_UPDATE_DATE DATE
)
;

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
  ORG_IS_LEAF       NUMBER,
  ORG_RELATION      NUMBER,
  ORG_MANAGER       VARCHAR2(50),
  ORG_MANAGER_PHONE VARCHAR2(50),
  ORG_ADDRESS       VARCHAR2(255),
  ORG_BILL_ADDRESS  VARCHAR2(255),
  ORG_POST_CODE     VARCHAR2(10),
  ORG_TELEPHONE     VARCHAR2(50),
  ORG_STATUS        NUMBER,
  ORG_NOTE          VARCHAR2(200),
  ORG_UNITE         VARCHAR2(255),
  ORG_REPORT_ID     VARCHAR2(64),
  ORG_ADMIN_ID      VARCHAR2(64),
  ORG_ORDER         NUMBER,
  ORG_CREATE_ID     VARCHAR2(64),
  ORG_CREATE_DATE   DATE,
  ORG_UPDATE_ID     VARCHAR2(64),
  ORG_UPDATE_DATE   DATE,
  ORG_UNUSED1       VARCHAR2(200),
  ORG_UNUSED2       VARCHAR2(200),
  ORG_UNUSED3       VARCHAR2(64),
  ORG_UNUSED4       VARCHAR2(64)
)
;

prompt
prompt Creating table UUM_RIGHT
prompt ========================
prompt
create table UUM_RIGHT
(
  RGT_ID              NUMBER not null,
  RGT_CODE            VARCHAR2(255),
  RGT_APP_ID          NUMBER,
  RGT_PARENT_ID       NUMBER,
  RGT_LAYER           VARCHAR2(255),
  RGT_IS_LEAF         NUMBER,
  RGT_NAME            VARCHAR2(50) not null,
  RGT_TYPE            VARCHAR2(50),
  RGT_URL             VARCHAR2(255),
  RGT_ORDER           NUMBER,
  RGT_STATUS          NUMBER,
  RGT_NOTE            VARCHAR2(200),
  RGT_FUNCTION_NAME   VARCHAR2(100),
  RGT_FUNCTION_ACTION VARCHAR2(100),
  RGT_UNUSED1         VARCHAR2(100),
  RGT_UNUSED2         VARCHAR2(200)
)
;

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
;

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
;

prompt
prompt Creating table UUM_R_ROLE_APP
prompt =============================
prompt
create table UUM_R_ROLE_APP
(
  ROLE_APP_ID NUMBER(6),
  RA_APP_ID   NUMBER not null,
  RA_ROLE_ID  NUMBER not null,
  RA_IS_FUNC  NUMBER
)
;

prompt
prompt Creating table UUM_R_ROLE_RIGHT
prompt ===============================
prompt
create table UUM_R_ROLE_RIGHT
(
  ROLE_RIGHT_ID NUMBER(6) not null,
  RR_ROLE_ID    NUMBER not null,
  RR_RGT_ID     NUMBER not null,
  RR_IS_FUNC    NUMBER
)
;

prompt
prompt Creating table UUM_R_ROLE_SUB_RIGHT
prompt ===================================
prompt
create table UUM_R_ROLE_SUB_RIGHT
(
  RSR_ID      NUMBER not null,
  RSR_ROLE_ID NUMBER not null,
  RSR_SR_ID   NUMBER not null,
  RSR_IS_FUNC NUMBER
)
;

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
;

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
;

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
  USER_STATUS             NUMBER not null,
  USER_OUT                NUMBER,
  USER_AREA               CHAR(6),
  USER_OUT_DATE           DATE,
  USER_LOGIN_DATE         DATE,
  USER_NOTE               VARCHAR2(4000),
  USER_PASSWD_CHANGE_DATE DATE,
  USER_UNLOCK_DATE        DATE,
  USER_TYPE               NUMBER,
  USER_LDAP_URL           VARCHAR2(255),
  USER_ORDER              NUMBER,
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
;
comment on column UUM_USER.USER_SYSTEM_CODE
  is '���ڵ�λ���ŵ�ϵͳ���룫��ǰ�û�˳���롣';
comment on column UUM_USER.USER_ORG_ID
  is '������ϵͳ����Ա�⣬�����û�������ϵͳ����Ա����ͨ������ָ����ֵ��';
comment on column UUM_USER.USER_LOGIN_NAME
  is '����Ψһ��Լ��';
comment on column UUM_USER.USER_LOGIN_PASSWD
  is '����md5����';
comment on column UUM_USER.USER_SEX
  is '1:Ů��
2:����';
comment on column UUM_USER.USER_STATUS
  is '1:���� �ڲ����Ƿּ������Լ��������£�����״̬���û�������ҵ������ɼ����û�����ʱ��״̬Ĭ��Ϊ����״̬��
2:���� �����û�����ʱ�ģ�����ҵ��Ӷ����ʱ��������״̬���û�ֻ��һ��ʱ������Ч�����Ҳ��ܵ�¼ϵͳ��
3:ͣ�� ������Ҫʹ�ø��û�����ʷ��Ϣ���������ݵ��û�����ͳ��ʱ���õ����û�����Ϣ��ͣ��״̬���û����ܵ�¼ϵͳ��
���ã����ã�ͣ��״̬���û����໥ת����';
comment on column UUM_USER.USER_TYPE
  is '1:��ͨ�û� ���߱������û�������������Ȩ��
2:ϵͳ����Ա �߱��ּ����������������û���Ȩ��
9:����ϵͳ����Ա �ܹ������е��û������е���������';

prompt
prompt Creating sequence S_APPCHANNELPOLICY_V3
prompt =======================================
prompt
create sequence S_APPCHANNELPOLICY_V3
minvalue 1
maxvalue 999999999999999999999999999
start with 101
increment by 1
cache 20;

prompt
prompt Creating sequence S_APPLICATION
prompt ===============================
prompt
create sequence S_APPLICATION
minvalue 1
maxvalue 999999999999999999999999999
start with 610
increment by 1
cache 20;

prompt
prompt Creating sequence S_COMPANY
prompt ===========================
prompt
create sequence S_COMPANY
minvalue 1
maxvalue 999999999999999999999999999
start with 590
increment by 1
cache 20;

prompt
prompt Creating sequence S_FILTER
prompt ==========================
prompt
create sequence S_FILTER
minvalue 1
maxvalue 999999999999999999999999999
start with 670
increment by 1
cache 20;

prompt
prompt Creating sequence S_FORWARD_SERVICELIST
prompt =======================================
prompt
create sequence S_FORWARD_SERVICELIST
minvalue 1
maxvalue 999999999999999999999999999
start with 5070
increment by 1
cache 20;

prompt
prompt Creating sequence S_HR_CTRT_ARGUE
prompt =================================
prompt
create sequence S_HR_CTRT_ARGUE
minvalue 1
maxvalue 999999999999999999999999999
start with 21
increment by 1
cache 20;

prompt
prompt Creating sequence S_HR_CT_TR_PXKCTXKC
prompt =====================================
prompt
create sequence S_HR_CT_TR_PXKCTXKC
minvalue 1
maxvalue 9999999999999999999999999
start with 81
increment by 1
cache 20;

prompt
prompt Creating sequence S_HR_HI_ABILITY
prompt =================================
prompt
create sequence S_HR_HI_ABILITY
minvalue 1
maxvalue 999999999999999999999999999
start with 21
increment by 1
cache 20;

prompt
prompt Creating sequence S_HR_HI_ABILITYDETAIL
prompt =======================================
prompt
create sequence S_HR_HI_ABILITYDETAIL
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
start with 21
increment by 1
cache 20;

prompt
prompt Creating sequence S_HR_HI_CTRT
prompt ==============================
prompt
create sequence S_HR_HI_CTRT
minvalue 1
maxvalue 999999999999999999999999999
start with 41
increment by 1
cache 20;

prompt
prompt Creating sequence S_HR_HI_CT_PERSONSCOPE
prompt ========================================
prompt
create sequence S_HR_HI_CT_PERSONSCOPE
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
start with 161
increment by 1
cache 20;

prompt
prompt Creating sequence S_HR_HI_EDU
prompt =============================
prompt
create sequence S_HR_HI_EDU
minvalue 1
maxvalue 999999999999999999999999999
start with 294801
increment by 1
cache 20;

prompt
prompt Creating sequence S_HR_HI_ERROR
prompt ===============================
prompt
create sequence S_HR_HI_ERROR
minvalue 1
maxvalue 999999999999999999999999999
start with 41
increment by 1
cache 20;

prompt
prompt Creating sequence S_HR_HI_ERRORPERSON
prompt =====================================
prompt
create sequence S_HR_HI_ERRORPERSON
minvalue 1
maxvalue 999999999999999999999999999
start with 441
increment by 1
cache 20;

prompt
prompt Creating sequence S_HR_HI_EXPERT
prompt ================================
prompt
create sequence S_HR_HI_EXPERT
minvalue 0
maxvalue 99999999999999999999999
start with 21361
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
prompt Creating sequence S_HR_HI_LANGSKILL
prompt ===================================
prompt
create sequence S_HR_HI_LANGSKILL
minvalue 1
maxvalue 999999999999999999999999999
start with 21
increment by 1
cache 20;

prompt
prompt Creating sequence S_HR_HI_PERSON
prompt ================================
prompt
create sequence S_HR_HI_PERSON
minvalue 1
maxvalue 999999999999999999999999999
start with 26556
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
start with 21511
increment by 1
cache 20;

prompt
prompt Creating sequence S_HR_HI_PROBATION
prompt ===================================
prompt
create sequence S_HR_HI_PROBATION
minvalue 1
maxvalue 999999999999999999999999999
start with 101
increment by 1
cache 20;

prompt
prompt Creating sequence S_HR_HI_RETIRE
prompt ================================
prompt
create sequence S_HR_HI_RETIRE
minvalue 1
maxvalue 999999999999999999999999999
start with 41
increment by 1
cache 20;

prompt
prompt Creating sequence S_HR_HI_TECHTITLE
prompt ===================================
prompt
create sequence S_HR_HI_TECHTITLE
minvalue 1
maxvalue 999999999999999999999999999
start with 85201
increment by 1
cache 20;

prompt
prompt Creating sequence S_HR_HI_TECHWORK
prompt ==================================
prompt
create sequence S_HR_HI_TECHWORK
minvalue 1
maxvalue 999999999999999999999999999
start with 41
increment by 1
cache 20;

prompt
prompt Creating sequence S_HR_HI_WORKQUALIFY
prompt =====================================
prompt
create sequence S_HR_HI_WORKQUALIFY
minvalue 1
maxvalue 999999999999999999999999999
start with 76781
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
start with 981
increment by 1
cache 20;

prompt
prompt Creating sequence S_HR_OM_POS
prompt =============================
prompt
create sequence S_HR_OM_POS
minvalue 1
maxvalue 999999999999999999999999999
start with 24115
increment by 1
cache 20;

prompt
prompt Creating sequence S_HR_OM_POSABILITY
prompt ====================================
prompt
create sequence S_HR_OM_POSABILITY
minvalue 1
maxvalue 999999999999999999999999999
start with 41
increment by 1
cache 20;

prompt
prompt Creating sequence S_HR_OM_POSCTRL
prompt =================================
prompt
create sequence S_HR_OM_POSCTRL
minvalue 1
maxvalue 999999999999999999999999999
start with 21
increment by 1
cache 20;

prompt
prompt Creating sequence S_HR_OM_POSDESC
prompt =================================
prompt
create sequence S_HR_OM_POSDESC
minvalue 1
maxvalue 999999999999999999999999999
start with 21
increment by 1
cache 20;

prompt
prompt Creating sequence S_HR_OM_POSINDEX
prompt ==================================
prompt
create sequence S_HR_OM_POSINDEX
minvalue 1
maxvalue 999999999999999999999999999
start with 192
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
prompt Creating sequence S_HR_OM_POSSERIAL
prompt ===================================
prompt
create sequence S_HR_OM_POSSERIAL
minvalue 1
maxvalue 999999999999999999999999999
start with 341
increment by 1
cache 20;

prompt
prompt Creating sequence S_HR_OM_POSSUCCESSION
prompt =======================================
prompt
create sequence S_HR_OM_POSSUCCESSION
minvalue 1
maxvalue 999999999999999999999999999
start with 81
increment by 1
cache 20;

prompt
prompt Creating sequence S_HR_OM_POSSUPERV
prompt ===================================
prompt
create sequence S_HR_OM_POSSUPERV
minvalue 1
maxvalue 999999999999999999999999999
start with 106161
increment by 1
cache 20;

prompt
prompt Creating sequence S_HR_OM_POSTRAINING
prompt =====================================
prompt
create sequence S_HR_OM_POSTRAINING
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
start with 21
increment by 1
cache 20;

prompt
prompt Creating sequence S_HR_ORG_B07
prompt ==============================
prompt
create sequence S_HR_ORG_B07
minvalue 1
maxvalue 999999999999999999999999999
start with 61
increment by 1
cache 20;

prompt
prompt Creating sequence S_HR_ORG_B104
prompt ===============================
prompt
create sequence S_HR_ORG_B104
minvalue 1
maxvalue 999999999999999999999999999
start with 41
increment by 1
cache 20;

prompt
prompt Creating sequence S_HR_ORG_B13
prompt ==============================
prompt
create sequence S_HR_ORG_B13
minvalue 1
maxvalue 999999999999999999999999999
start with 21
increment by 1
cache 20;

prompt
prompt Creating sequence S_HR_ORG_B130
prompt ===============================
prompt
create sequence S_HR_ORG_B130
minvalue 1
maxvalue 999999999999999999999999999
start with 481
increment by 1
cache 20;

prompt
prompt Creating sequence S_HR_ORG_B132
prompt ===============================
prompt
create sequence S_HR_ORG_B132
minvalue 0
maxvalue 999999999999999999999999
start with 181
increment by 1
cache 20;

prompt
prompt Creating sequence S_HR_ORG_B16
prompt ==============================
prompt
create sequence S_HR_ORG_B16
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
start with 11777
increment by 1
cache 20;

prompt
prompt Creating sequence S_HR_ORG_EXTENSION
prompt ====================================
prompt
create sequence S_HR_ORG_EXTENSION
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
cache 20;

prompt
prompt Creating sequence S_HR_ORG_INDEX
prompt ================================
prompt
create sequence S_HR_ORG_INDEX
minvalue 1
maxvalue 999999999999999999999999999
start with 21
increment by 1
cache 20;

prompt
prompt Creating sequence S_HR_ORG_SALARYCONTROL
prompt ========================================
prompt
create sequence S_HR_ORG_SALARYCONTROL
minvalue 1
maxvalue 999999999999999999999999999
start with 23
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
start with 725
increment by 1
cache 20;

prompt
prompt Creating sequence S_HR_SYSTEM_CODEPOOL
prompt ======================================
prompt
create sequence S_HR_SYSTEM_CODEPOOL
minvalue 1
maxvalue 999999999999999999999999999
start with 81
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
start with 241
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
start with 101
increment by 1
cache 20;

prompt
prompt Creating sequence S_HR_SYSTEM_TOPIC
prompt ===================================
prompt
create sequence S_HR_SYSTEM_TOPIC
minvalue 1
maxvalue 999999999999999999999999999
start with 121
increment by 1
cache 20;

prompt
prompt Creating sequence S_HR_TM_ERRAND
prompt ================================
prompt
create sequence S_HR_TM_ERRAND
minvalue 1
maxvalue 999999999999999999999999999
start with 21
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
start with 41
increment by 1
cache 20;

prompt
prompt Creating sequence S_HR_TM_MONTHRESULT
prompt =====================================
prompt
create sequence S_HR_TM_MONTHRESULT
minvalue 1
maxvalue 999999999999999999999999999
start with 141
increment by 1
cache 20;

prompt
prompt Creating sequence S_HR_TM_OVERTIME
prompt ==================================
prompt
create sequence S_HR_TM_OVERTIME
minvalue 1
maxvalue 999999999999999999999999999
start with 21
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
start with 41
increment by 1
cache 20;

prompt
prompt Creating sequence S_HR_TM_VAC
prompt =============================
prompt
create sequence S_HR_TM_VAC
minvalue 1
maxvalue 999999999999999999999999999
start with 61
increment by 1
cache 20;

prompt
prompt Creating sequence S_HR_TM_VACRATE
prompt =================================
prompt
create sequence S_HR_TM_VACRATE
minvalue 1
maxvalue 999999999999999999999999999
start with 41
increment by 1
cache 20;

prompt
prompt Creating sequence S_HR_TM_VACRULE
prompt =================================
prompt
create sequence S_HR_TM_VACRULE
minvalue 1
maxvalue 999999999999999999999999999
start with 21
increment by 1
cache 20;

prompt
prompt Creating sequence S_HR_TM_VACTYPE
prompt =================================
prompt
create sequence S_HR_TM_VACTYPE
minvalue 1
maxvalue 999999999999999999999999999
start with 41
increment by 1
cache 20;

prompt
prompt Creating sequence S_IN_OK_V3
prompt ============================
prompt
create sequence S_IN_OK_V3
minvalue 1
maxvalue 999999999999999999999999999
start with 9770
increment by 1
cache 20;

prompt
prompt Creating sequence S_IN_READY_V3
prompt ===============================
prompt
create sequence S_IN_READY_V3
minvalue 1
maxvalue 999999999999999999999999999
start with 10390
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
start with 21
increment by 1
cache 20;

prompt
prompt Creating sequence S_JFW_BASE_CUSTOMCONFIG
prompt =========================================
prompt
create sequence S_JFW_BASE_CUSTOMCONFIG
minvalue 1
maxvalue 999999999999999999999999999
start with 126
increment by 1
cache 20;

prompt
prompt Creating sequence S_JFW_BASE_CUSTOMFIELD
prompt ========================================
prompt
create sequence S_JFW_BASE_CUSTOMFIELD
minvalue 1
maxvalue 999999999999999999999999999
start with 332
increment by 1
cache 20;

prompt
prompt Creating sequence S_JFW_BASE_NOTION
prompt ===================================
prompt
create sequence S_JFW_BASE_NOTION
minvalue 1
maxvalue 999999999999999999999999999
start with 321
increment by 1
cache 20;

prompt
prompt Creating sequence S_JFW_BASE_QSEARCH
prompt ====================================
prompt
create sequence S_JFW_BASE_QSEARCH
minvalue 1
maxvalue 999999999999999999999999999
start with 65
increment by 1
cache 20;

prompt
prompt Creating sequence S_JFW_BASE_SPECIALDAY
prompt =======================================
prompt
create sequence S_JFW_BASE_SPECIALDAY
minvalue 1
maxvalue 999999999999999999999999999
start with 21
increment by 1
cache 20;

prompt
prompt Creating sequence S_JFW_SECURITY_DATAMODEL
prompt ==========================================
prompt
create sequence S_JFW_SECURITY_DATAMODEL
minvalue 1
maxvalue 999999999999999999999999999
start with 540
increment by 1
cache 20;

prompt
prompt Creating sequence S_JFW_SECURITY_POLICY
prompt =======================================
prompt
create sequence S_JFW_SECURITY_POLICY
minvalue 1
maxvalue 999999999999999999999999999
start with 204
increment by 1
cache 20;

prompt
prompt Creating sequence S_JFW_SECURITY_UNITPOLICY
prompt ===========================================
prompt
create sequence S_JFW_SECURITY_UNITPOLICY
minvalue 1
maxvalue 999999999999999999999999999
start with 21
increment by 1
cache 20;

prompt
prompt Creating sequence S_JMS_V3
prompt ==========================
prompt
create sequence S_JMS_V3
minvalue 1
maxvalue 999999999999999999999999999
start with 570
increment by 1
cache 20;

prompt
prompt Creating sequence S_MEDIA
prompt =========================
prompt
create sequence S_MEDIA
minvalue 1
maxvalue 999999999999999999999999999
start with 630
increment by 1
cache 20;

prompt
prompt Creating sequence S_OUT_ERROR_V3
prompt ================================
prompt
create sequence S_OUT_ERROR_V3
minvalue 1
maxvalue 999999999999999999999999999
start with 530
increment by 1
cache 20;

prompt
prompt Creating sequence S_OUT_OK_V3
prompt =============================
prompt
create sequence S_OUT_OK_V3
minvalue 1
maxvalue 999999999999999999999999999
start with 20410
increment by 1
cache 20;

prompt
prompt Creating sequence S_OUT_READY_V3
prompt ================================
prompt
create sequence S_OUT_READY_V3
minvalue 1
maxvalue 999999999999999999999999999
start with 20610
increment by 1
cache 20;

prompt
prompt Creating sequence S_OUT_REPLY_V3
prompt ================================
prompt
create sequence S_OUT_REPLY_V3
minvalue 1
maxvalue 999999999999999999999999999
start with 530
increment by 1
cache 20;

prompt
prompt Creating sequence S_PDM_BASE
prompt ============================
prompt
create sequence S_PDM_BASE
minvalue 1
maxvalue 999999999999999999999999999
start with 21
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
start with 4821
increment by 1
cache 20;

prompt
prompt Creating sequence S_PDM_RIGHT
prompt =============================
prompt
create sequence S_PDM_RIGHT
minvalue 1
maxvalue 999999999999999999999999999
start with 221
increment by 1
cache 20;

prompt
prompt Creating sequence S_PDM_VIRTUALROOT
prompt ===================================
prompt
create sequence S_PDM_VIRTUALROOT
minvalue 1
maxvalue 999999999999999999999999999
start with 181
increment by 1
cache 20;

prompt
prompt Creating sequence S_QFXX
prompt ========================
prompt
create sequence S_QFXX
minvalue 1
maxvalue 999999999999999999999999999
start with 1050
increment by 1
cache 20;

prompt
prompt Creating sequence S_QFXX_EAMIL
prompt ==============================
prompt
create sequence S_QFXX_EAMIL
minvalue 1
maxvalue 999999999999999999999999999
start with 221
increment by 1
cache 20;

prompt
prompt Creating sequence S_QFXX_MSGATTACHMENT
prompt ======================================
prompt
create sequence S_QFXX_MSGATTACHMENT
minvalue 1
maxvalue 999999999999999999999999999
start with 550
increment by 1
cache 20;

prompt
prompt Creating sequence S_QFXX_RECEIVERS
prompt ==================================
prompt
create sequence S_QFXX_RECEIVERS
minvalue 1
maxvalue 999999999999999999999999999
start with 1130
increment by 1
cache 20;

prompt
prompt Creating sequence S_QFXX_SENDER
prompt ===============================
prompt
create sequence S_QFXX_SENDER
minvalue 1
maxvalue 999999999999999999999999999
start with 630
increment by 1
cache 20;

prompt
prompt Creating sequence S_SERVICE_V3
prompt ==============================
prompt
create sequence S_SERVICE_V3
minvalue 1
maxvalue 999999999999999999999999999
start with 590
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
prompt Creating sequence S_TS_BUGS_BUGRECORD
prompt =====================================
prompt
create sequence S_TS_BUGS_BUGRECORD
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
start with 41
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
start with 61
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
prompt Creating sequence S_T_JFW_CODE_COMPLEX
prompt ======================================
prompt
create sequence S_T_JFW_CODE_COMPLEX
minvalue 1
maxvalue 999999999999999999999999999
start with 33
increment by 1
cache 20;

prompt
prompt Creating sequence S_T_JFW_CODE_DATA
prompt ===================================
prompt
create sequence S_T_JFW_CODE_DATA
minvalue 1
maxvalue 999999999999999999999999999
start with 785
increment by 1
cache 20;

prompt
prompt Creating sequence S_T_JFW_FUNCTION
prompt ==================================
prompt
create sequence S_T_JFW_FUNCTION
minvalue 1
maxvalue 999999999999999999999999999
start with 2663
increment by 1
cache 20;

prompt
prompt Creating sequence S_T_JFW_FUNCTIONVERSION
prompt =========================================
prompt
create sequence S_T_JFW_FUNCTIONVERSION
minvalue 1
maxvalue 999999999999999999999999999
start with 41
increment by 1
cache 20;

prompt
prompt Creating sequence S_T_JFW_RULE_PARAM
prompt ====================================
prompt
create sequence S_T_JFW_RULE_PARAM
minvalue 1
maxvalue 999999999999999999999999999
start with 2841
increment by 1
cache 20;

prompt
prompt Creating sequence S_UMS_EMAIL
prompt =============================
prompt
create sequence S_UMS_EMAIL
minvalue 1
maxvalue 999999999999999999999999999
start with 61
increment by 1
cache 20;

prompt
prompt Creating sequence S_USERRECEIVEPOLICY
prompt =====================================
prompt
create sequence S_USERRECEIVEPOLICY
minvalue 1
maxvalue 999999999999999999999999999
start with 530
increment by 1
cache 20;

prompt
prompt Creating sequence S_USERSUBSCRIBE
prompt =================================
prompt
create sequence S_USERSUBSCRIBE
minvalue 1
maxvalue 999999999999999999999999999
start with 550
increment by 1
cache 20;

prompt
prompt Creating sequence S_USERUNSUBSCRIBE
prompt ===================================
prompt
create sequence S_USERUNSUBSCRIBE
minvalue 1
maxvalue 999999999999999999999999999
start with 530
increment by 1
cache 20;

prompt
prompt Creating sequence S_UUM_APPLICATION
prompt ===================================
prompt
create sequence S_UUM_APPLICATION
minvalue 1
maxvalue 999999999999999999999999999
start with 21
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
prompt Creating sequence S_UUM_MENU
prompt ============================
prompt
create sequence S_UUM_MENU
minvalue 1
maxvalue 999999999999999999999999999
start with 1000
increment by 1
cache 20;

prompt
prompt Creating sequence S_UUM_ORG
prompt ===========================
prompt
create sequence S_UUM_ORG
minvalue 1
maxvalue 999999999999999999999999999
start with 21
increment by 1
cache 20;

prompt
prompt Creating sequence S_UUM_RIGHT
prompt =============================
prompt
create sequence S_UUM_RIGHT
minvalue 1
maxvalue 999999999999999999999999999
start with 2006
increment by 1
cache 20;

prompt
prompt Creating sequence S_UUM_ROLE
prompt ============================
prompt
create sequence S_UUM_ROLE
minvalue 1
maxvalue 999999999999999999999999999
start with 21
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
start with 21
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
start with 21
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
start with 21
increment by 1
cache 20;

prompt
prompt Creating view IN_OK_V3_VIEW
prompt ===========================
prompt
create or replace view in_ok_v3_view as
select 
to_char(to_date(batchno,'yyyy-mm-dd hh24:mi:ss'),'yyyy-mm-dd hh24:mi:ss') as charformat,
in_ok_v3.*,'����' as xc ,
to_date(batchno,'yyyy-mm-dd hh24:mi:ss') as dateformat
from in_ok_v3 order by charformat desc
/

prompt
prompt Creating view IN_READY_V3_VIEW
prompt ==============================
prompt
create or replace view in_ready_v3_view as
select 
to_char(to_date(batchno,'yyyy-mm-dd hh24:mi:ss'),'yyyy-mm-dd hh24:mi:ss') as charformat,
in_ready_v3.*,'����' as xc,
to_date(batchno,'yyyy-mm-dd hh24:mi:ss') as dateformat
from in_ready_v3
order by charformat desc
/

prompt
prompt Creating view MEDIA_BC
prompt ======================
prompt
create or replace view media_bc as
select "MEDIA_ID","MEDIANAME","CLASS","TYPE","STATUSFLAG","IP","PORT","TIMEOUT","REPEATTIMES","STARTWORKTIME","ENDWORKTIME","LOGINNAME","LOGINPASSWORD","SLEEPTIME","PROP1","PROP2","PROP3","PROP4","PROP5","SEQKEY","MEDIASTYLE" from MEDIA where  type =1
/

prompt
prompt Creating view MEDIA_EMAIL_USED
prompt ==============================
prompt
create or replace view media_email_used as
select "MEDIA_ID","MEDIANAME","CLASS","TYPE","STATUSFLAG","IP","PORT","TIMEOUT","REPEATTIMES","STARTWORKTIME","ENDWORKTIME","LOGINNAME","LOGINPASSWORD","SLEEPTIME","PROP1","PROP2","PROP3","PROP4","PROP5","SEQKEY","MEDIASTYLE" from media where media.statusflag=0 and media.mediastyle=2 and media.type=1
/

prompt
prompt Creating view MEDIA_USEED
prompt =========================
prompt
create or replace view media_useed as
select "MEDIA_ID","MEDIANAME","CLASS","TYPE","STATUSFLAG","IP","PORT","TIMEOUT","REPEATTIMES","STARTWORKTIME","ENDWORKTIME","LOGINNAME","LOGINPASSWORD","SLEEPTIME","PROP1","PROP2","PROP3","PROP4","PROP5","SEQKEY","MEDIASTYLE" from MEDIA where MEDIA.STATUSFLAG=0 and type =1
/

prompt
prompt Creating view MEDIA_USEED_BR
prompt ============================
prompt
create or replace view media_useed_br as
select "MEDIA_ID","MEDIANAME","CLASS","TYPE","STATUSFLAG","IP","PORT","TIMEOUT","REPEATTIMES","STARTWORKTIME","ENDWORKTIME","LOGINNAME","LOGINPASSWORD","SLEEPTIME","PROP1","PROP2","PROP3","PROP4","PROP5","SEQKEY","MEDIASTYLE" from MEDIA where  type =0
 union all
 select 
 '013',
'NCI����Ϣ����',
null,
0,
null,
null,
null,
null,
null,
null,
null,
null,
null,
null,
null,
null,
null,
null,
null,
null,
null
from dual
union all
 select 
 '015',
'NCIV3����Ϣ����',
null,
0,
null,
null,
null,
null,
null,
null,
null,
null,
null,
null,
null,
null,
null,
null,
null,
null,
null
from dual
/

prompt
prompt Creating view MEDIA_USEED_DX
prompt ============================
prompt
create or replace view media_useed_dx as
select "MEDIA_ID","MEDIANAME","CLASS","TYPE","STATUSFLAG","IP","PORT","TIMEOUT","REPEATTIMES","STARTWORKTIME","ENDWORKTIME","LOGINNAME","LOGINPASSWORD","SLEEPTIME","PROP1","PROP2","PROP3","PROP4","PROP5","SEQKEY","MEDIASTYLE" from MEDIA
 where MEDIA.STATUSFLAG=0 and type =1 and MEDIASTYLE=1
/

prompt
prompt Creating view MEDIA_USEED_YJ
prompt ============================
prompt
create or replace view media_useed_yj as
select "MEDIA_ID",
"MEDIANAME",
"CLASS",
"TYPE",
"STATUSFLAG",
"IP",
"PORT",
"TIMEOUT",
"REPEATTIMES",
"STARTWORKTIME",
"ENDWORKTIME",
"LOGINNAME",
"LOGINPASSWORD",
"SLEEPTIME",
"PROP1",
"PROP2",
"PROP3",
"PROP4",
"PROP5",
"SEQKEY",
"MEDIASTYLE" from MEDIA
 where MEDIA.STATUSFLAG=0 and type =1 and MEDIASTYLE=2
/

prompt
prompt Creating view OUT_ERROR_V3_VIEW
prompt ===============================
prompt
create or replace view out_error_v3_view as
select 
to_char(to_date(batchno,'yyyy-mm-dd hh24:mi:ss'),'yyyy-mm-dd hh24:mi:ss') as charformat,
out_error_v3.*,'����' as xc,
to_date(batchno,'yyyy-mm-dd hh24:mi:ss') as dateformat
from out_error_v3 order by charformat desc
/

prompt
prompt Creating view OUT_OK_V3_ERROR_VIEW
prompt ==================================
prompt
create or replace view out_ok_v3_error_view as
select 
to_char(to_date(batchno,'yyyy-mm-dd hh24:mi:ss'),'yyyy-mm-dd hh24:mi:ss') as charformat,
out_ok_v3.*,'����' as xc,
to_date(batchno,'yyyy-mm-dd hh24:mi:ss') as dateformat
from out_ok_v3 where STATUSFLAG=1 order by charformat desc
/

prompt
prompt Creating view OUT_OK_V3_STATIC
prompt ==============================
prompt
create or replace view out_ok_v3_static as
select 
out_ok_v3.seqkey,out_ok_v3.serviceid,out_ok_v3.mediaid,out_ok_v3.feetype,out_ok_v3.fee from out_ok_v3
union all
select 
null,out_ok.appid as serviceid ,out_ok.mediaid,out_ok.feetype,out_ok.fee  from out_ok
/

prompt
prompt Creating function TIMEFORMAT
prompt ============================
prompt
create or replace function timeformat(finishtime   in  varchar2) return varchar2 is
  Result varchar2(50);
begin
  Result:= substr(finishtime,0,2) ;
  return(Result);
end timeformat;
/

prompt
prompt Creating view OUT_OK_V3_TIME
prompt ============================
prompt
create or replace view out_ok_v3_time as
select out_ok_v3.batchno,timeformat(out_ok_v3.finishtime) as hour from out_ok_v3
union all
select out_ok.batchno,timeformat(out_ok.finishtime) as hour from out_ok
/

prompt
prompt Creating view OUT_OK_V3_VIEW
prompt ============================
prompt
create or replace view out_ok_v3_view as
select 
to_char(to_date(batchno,'yyyy-mm-dd hh24:mi:ss'),'yyyy-mm-dd hh24:mi:ss') as charformat,
to_date(batchno,'yyyy-mm-dd hh24:mi:ss') as dateformat,
out_ok_v3.*,'����' as xc,
decode(fee,-1,0,fee) as realfee
from out_ok_v3 where STATUSFLAG=0 order by charformat desc
/

prompt
prompt Creating view OUT_READY_V3_VIEW
prompt ===============================
prompt
create or replace view out_ready_v3_view as
select 
to_char(to_date(batchno,'yyyy-mm-dd hh24:mi:ss'),'yyyy-mm-dd hh24:mi:ss') as charformat,
to_date(batchno,'yyyy-mm-dd hh24:mi:ss') as dateformat,
out_ready_v3.*,'����' as xc,
decode(fee,-1,0,fee) as realfee
from out_ready_v3 order by charformat desc
/

prompt
prompt Creating view OUT_REPLY_V3_VIEW
prompt ===============================
prompt
create or replace view out_reply_v3_view as
select 
to_char(to_date(batchno,'yyyy-mm-dd hh24:mi:ss'),'yyyy-mm-dd hh24:mi:ss') as charformat,
out_reply_v3.*,'����' as xc,
to_date(batchno,'yyyy-mm-dd hh24:mi:ss') as dateformat
from out_reply_v3 order by charformat desc
/


spool off
