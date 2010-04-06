create table UMS_TRANSMIT_ERR  (
   SEQKEY               VARCHAR2(36)                    not null,
   TRANS_SOURCE         VARCHAR2(36),
   TRANS_TARGET         VARCHAR2(36),
   TRANS_RECEIVE_TIME   VARCHAR2(19),
   TRANS_SENT_TIME      VARCHAR2(19),
   TRANS_CONTENT        BLOB,
   TRANS_ERR_TYPE       VARCHAR2(16),
   TRANS_LENGTH         NUMBER,
   constraint PK_UMS_TRANSMIT_ERR primary key (SEQKEY)
           using index
);