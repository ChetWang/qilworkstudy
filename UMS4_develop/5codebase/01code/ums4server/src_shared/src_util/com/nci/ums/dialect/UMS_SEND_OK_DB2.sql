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