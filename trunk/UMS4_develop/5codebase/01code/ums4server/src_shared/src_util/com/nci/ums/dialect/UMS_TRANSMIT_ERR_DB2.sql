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