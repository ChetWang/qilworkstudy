CREATE TABLE UMS_TRANSMIT_MES  (
   SEQKEY               VARCHAR2(36)                    NOT null,
   TRANS_SOURCE         VARCHAR2(36),
   TRANS_TARGET         VARCHAR2(36),
   TRANS_RECEIVE_TIME   VARCHAR2(19),
   TRANS_SENT_TIME      VARCHAR2(19),
   TRANS_CONTENT        BLOB,
   TRANS_LENGTH         NUMBER,
   CONSTRAINT PK_UMS_TRANSMIT_MES PRIMARY KEY (SEQKEY)
           USING index
);