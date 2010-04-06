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