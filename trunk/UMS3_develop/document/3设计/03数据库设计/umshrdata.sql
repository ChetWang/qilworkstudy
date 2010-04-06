prompt PL/SQL Developer import file
prompt Created on 2007年12月26日 by wu00000bing
set feedback off
set define off
prompt Disabling triggers for HR_HI_PERSON...
alter table HR_HI_PERSON disable all triggers;
prompt Disabling triggers for HR_ORG_BASE...
alter table HR_ORG_BASE disable all triggers;
prompt Loading HR_HI_PERSON...
insert into HR_HI_PERSON (SEQKEY, ORGCODE, PSNNUM, NAME, PERSONTYPE, SEX, BIRTHDATE, NATIVEPLACE, NATIONAL, HEALTHSTATUS, MARRISTATUS, IDNO, PERRESIDENCE, ALIANAME, JOINWORKDATE, ENTERDATE, REGULARDATE, BUSINESSSORT, WORKMAJOR, EMIGFLAG, SSNO, PERSTATUS, CHECKINFLAG, SYNWAGEFLAG, ENTERUNITDATE, PSNPROPERTY, PSNBANKCODE, PSNACCOUNT, INDUTYSTATUS, DUTYCLASS, LOGINNAME, PERSONSCOPE, LASTDATE, WORKAGEDIFF, AGE, WORKAGE, ENTERUNITAGE, PHOTOFILE, POSCODE, JOBID, JOBTITLEID, JOBTITLELEVEL, EDUCATION, DEGREE, LEAVEDATE, WORKTYPE, JOBLEVELID, QUALIFYNAME, QUALIFYLEV, CREATEID, CREATEDATE, UPDATEID, UPDATEDATE, ORGID, COMPANYID)
values ('0000', '00100000', '0000', '超级管理员', 'UMS', null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
commit;
prompt 1 record loaded
prompt Enabling triggers for HR_HI_PERSON...
alter table HR_HI_PERSON enable all triggers;
prompt Enabling triggers for HR_ORG_BASE...
alter table HR_ORG_BASE enable all triggers;
set feedback on
set define on
prompt Done.
