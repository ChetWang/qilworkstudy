prompt PL/SQL Developer import file
prompt Created on 2007��12��26�� by wu00000bing
set feedback off
set define off
prompt Disabling triggers for APPLICATION...
alter table APPLICATION disable all triggers;
prompt Disabling triggers for MEDIA...
alter table MEDIA disable all triggers;
prompt Disabling triggers for UMS_CT_BRZT...
alter table UMS_CT_BRZT disable all triggers;
prompt Disabling triggers for UMS_CT_DSHH...
alter table UMS_CT_DSHH disable all triggers;
prompt Disabling triggers for UMS_CT_DSMM...
alter table UMS_CT_DSMM disable all triggers;
prompt Disabling triggers for UMS_CT_FFFSFX...
alter table UMS_CT_FFFSFX disable all triggers;
prompt Disabling triggers for UMS_CT_FFLX...
alter table UMS_CT_FFLX disable all triggers;
prompt Disabling triggers for UMS_CT_FFZT...
alter table UMS_CT_FFZT disable all triggers;
prompt Disabling triggers for UMS_CT_FILTER...
alter table UMS_CT_FILTER disable all triggers;
prompt Disabling triggers for UMS_CT_FYLX...
alter table UMS_CT_FYLX disable all triggers;
prompt Disabling triggers for UMS_CT_JMS...
alter table UMS_CT_JMS disable all triggers;
prompt Disabling triggers for UMS_CT_MEDIASTYLE...
alter table UMS_CT_MEDIASTYLE disable all triggers;
prompt Disabling triggers for UMS_CT_OUT_READY_ACK...
alter table UMS_CT_OUT_READY_ACK disable all triggers;
prompt Disabling triggers for UMS_CT_OUT_READY_NEEDREPLY...
alter table UMS_CT_OUT_READY_NEEDREPLY disable all triggers;
prompt Disabling triggers for UMS_CT_OUT_READY_PRIORITY...
alter table UMS_CT_OUT_READY_PRIORITY disable all triggers;
prompt Disabling triggers for UMS_CT_OUT_READY_SENDDIRECTLY...
alter table UMS_CT_OUT_READY_SENDDIRECTLY disable all triggers;
prompt Disabling triggers for UMS_CT_OUT_READY_TIMESETFLAG...
alter table UMS_CT_OUT_READY_TIMESETFLAG disable all triggers;
prompt Disabling triggers for UMS_CT_OUT_READY_UMSFLAG...
alter table UMS_CT_OUT_READY_UMSFLAG disable all triggers;
prompt Disabling triggers for UMS_CT_OUT_READY_V3_BATCHMODE...
alter table UMS_CT_OUT_READY_V3_BATCHMODE disable all triggers;
prompt Disabling triggers for UMS_CT_OUT_READY_V3_STATUSFLAG...
alter table UMS_CT_OUT_READY_V3_STATUSFLAG disable all triggers;
prompt Disabling triggers for UMS_CT_QDYXJ...
alter table UMS_CT_QDYXJ disable all triggers;
prompt Disabling triggers for UMS_CT_SFZJFS...
alter table UMS_CT_SFZJFS disable all triggers;
prompt Disabling triggers for UMS_CT_SJJG...
alter table UMS_CT_SJJG disable all triggers;
prompt Disabling triggers for UMS_CT_SYZT...
alter table UMS_CT_SYZT disable all triggers;
prompt Disabling triggers for UMS_CT_YYZT...
alter table UMS_CT_YYZT disable all triggers;
prompt Disabling triggers for UUM_ROLE...
alter table UUM_ROLE disable all triggers;
prompt Loading APPLICATION...
insert into APPLICATION (COMPANYID, APPID, APPNAME, PASSWORD, MD5PWD, STATUS, IP, PORT, TIMEOUT, SPNO, CHANNELTYPE, EMAIL, OBJECT, LOGINNAME, LOGINPWD, FEE, FEETYPE, SEQKEY)
values ('1', 'WEB', 'WEBƽ̨����', 'web', null, '0', '127.0.0.1', 8080, 100, '1', 1, null, null, '1', '1', 1, 1, '550');
commit;
prompt 1 record loaded
prompt Loading MEDIA...
insert into MEDIA (MEDIA_ID, MEDIANAME, CLASS, TYPE, STATUSFLAG, IP, PORT, TIMEOUT, REPEATTIMES, STARTWORKTIME, ENDWORKTIME, LOGINNAME, LOGINPASSWORD, SLEEPTIME, PROP1, PROP2, PROP3, PROP4, PROP5, SEQKEY, MEDIASTYLE)
values ('013', 'NCI�ƶ�����Ϣ', 'com.nci.ums.channel.outchannel.NCIOutChannel', 1, 1, null, 0, 0, 200, '0', '240000', '240000', null, 300, null, null, null, null, null, '1', 1);
insert into MEDIA (MEDIA_ID, MEDIANAME, CLASS, TYPE, STATUSFLAG, IP, PORT, TIMEOUT, REPEATTIMES, STARTWORKTIME, ENDWORKTIME, LOGINNAME, LOGINPASSWORD, SLEEPTIME, PROP1, PROP2, PROP3, PROP4, PROP5, SEQKEY, MEDIASTYLE)
values ('015', 'NCIV3�ƶ�����Ϣ', 'com.nci.ums.channel.outchannel.NCIOutChannel_V3', 1, 0, null, 0, 0, 200, '0', '240000', '240000', null, 300, null, null, null, null, null, '2', 1);
insert into MEDIA (MEDIA_ID, MEDIANAME, CLASS, TYPE, STATUSFLAG, IP, PORT, TIMEOUT, REPEATTIMES, STARTWORKTIME, ENDWORKTIME, LOGINNAME, LOGINPASSWORD, SLEEPTIME, PROP1, PROP2, PROP3, PROP4, PROP5, SEQKEY, MEDIASTYLE)
values ('076', 'NCI Email�Ⲧ', 'com.nci.ums.channel.outchannel.EmailOutChannel_V3', 1, 0, null, 0, 0, 0, '0', '240000', '240000', null, 300, null, null, null, null, null, '3', 2);
insert into MEDIA (MEDIA_ID, MEDIANAME, CLASS, TYPE, STATUSFLAG, IP, PORT, TIMEOUT, REPEATTIMES, STARTWORKTIME, ENDWORKTIME, LOGINNAME, LOGINPASSWORD, SLEEPTIME, PROP1, PROP2, PROP3, PROP4, PROP5, SEQKEY, MEDIASTYLE)
values ('077', 'NCI Email�ڲ�', 'com.nci.ums.channel.inchannel.email.EmailInChannel_V3', 0, 0, null, 0, 0, 0, '0', '240000', '240000', null, 300, null, null, null, null, null, '4', 2);
commit;
prompt 4 records loaded
prompt Loading UMS_CT_BRZT...
insert into UMS_CT_BRZT (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('1', '����', '1', null, null, null, null, null);
insert into UMS_CT_BRZT (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('2', '����', '0', null, null, null, null, null);
commit;
prompt 2 records loaded
prompt Loading UMS_CT_DSHH...
insert into UMS_CT_DSHH (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('1', '0ʱ', '0', null, null, null, null, null);
insert into UMS_CT_DSHH (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('2', '1ʱ', '1', null, null, null, null, null);
insert into UMS_CT_DSHH (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('3', '2ʱ', '2', null, null, null, null, null);
insert into UMS_CT_DSHH (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('4', '3ʱ', '3', null, null, null, null, null);
insert into UMS_CT_DSHH (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('5', '4ʱ', '4', null, null, null, null, null);
insert into UMS_CT_DSHH (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('6', '5ʱ', '5', null, null, null, null, null);
insert into UMS_CT_DSHH (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('7', '6ʱ', '6', null, null, null, null, null);
insert into UMS_CT_DSHH (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('8', '7ʱ', '7', null, null, null, null, null);
insert into UMS_CT_DSHH (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('9', '8ʱ', '8', null, null, null, null, null);
insert into UMS_CT_DSHH (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('10', '9ʱ', '9', null, null, null, null, null);
insert into UMS_CT_DSHH (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('11', '10ʱ', '10', null, null, null, null, null);
insert into UMS_CT_DSHH (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('12', '11ʱ', '11', null, null, null, null, null);
insert into UMS_CT_DSHH (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('13', '12ʱ', '12', null, null, null, null, null);
insert into UMS_CT_DSHH (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('14', '13ʱ', '13', null, null, null, null, null);
insert into UMS_CT_DSHH (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('15', '14ʱ', '14', null, null, null, null, null);
insert into UMS_CT_DSHH (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('16', '15ʱ', '15', null, null, null, null, null);
insert into UMS_CT_DSHH (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('17', '16ʱ', '16', null, null, null, null, null);
insert into UMS_CT_DSHH (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('18', '17ʱ', '17', null, null, null, null, null);
insert into UMS_CT_DSHH (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('19', '18ʱ', '18', null, null, null, null, null);
insert into UMS_CT_DSHH (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('20', '19ʱ', '19', null, null, null, null, null);
insert into UMS_CT_DSHH (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('21', '20ʱ', '20', null, null, null, null, null);
insert into UMS_CT_DSHH (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('22', '21ʱ', '21', null, null, null, null, null);
insert into UMS_CT_DSHH (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('23', '22ʱ', '22', null, null, null, null, null);
insert into UMS_CT_DSHH (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('24', '23ʱ', '23', null, null, null, null, null);
commit;
prompt 24 records loaded
prompt Loading UMS_CT_DSMM...
insert into UMS_CT_DSMM (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('0', '0��', '0', null, null, null, null, null);
insert into UMS_CT_DSMM (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('1', '1��', '1', null, null, null, null, null);
insert into UMS_CT_DSMM (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('2', '2��', '2', null, null, null, null, null);
insert into UMS_CT_DSMM (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('3', '3��', '3', null, null, null, null, null);
insert into UMS_CT_DSMM (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('4', '4��', '4', null, null, null, null, null);
insert into UMS_CT_DSMM (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('5', '5��', '5', null, null, null, null, null);
insert into UMS_CT_DSMM (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('6', '6��', '6', null, null, null, null, null);
insert into UMS_CT_DSMM (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('7', '7��', '7', null, null, null, null, null);
insert into UMS_CT_DSMM (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('8', '8��', '8', null, null, null, null, null);
insert into UMS_CT_DSMM (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('9', '9��', '9', null, null, null, null, null);
insert into UMS_CT_DSMM (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('10', '10��', '10', null, null, null, null, null);
insert into UMS_CT_DSMM (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('11', '11��', '11', null, null, null, null, null);
insert into UMS_CT_DSMM (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('12', '12��', '12', null, null, null, null, null);
insert into UMS_CT_DSMM (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('13', '13��', '13', null, null, null, null, null);
insert into UMS_CT_DSMM (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('14', '14��', '14', null, null, null, null, null);
insert into UMS_CT_DSMM (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('15', '15��', '15', null, null, null, null, null);
insert into UMS_CT_DSMM (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('16', '16��', '16', null, null, null, null, null);
insert into UMS_CT_DSMM (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('17', '17��', '17', null, null, null, null, null);
insert into UMS_CT_DSMM (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('18', '18��', '18', null, null, null, null, null);
insert into UMS_CT_DSMM (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('19', '19��', '19', null, null, null, null, null);
insert into UMS_CT_DSMM (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('20', '20��', '20', null, null, null, null, null);
insert into UMS_CT_DSMM (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('21', '21��', '21', null, null, null, null, null);
insert into UMS_CT_DSMM (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('22', '22��', '22', null, null, null, null, null);
insert into UMS_CT_DSMM (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('23', '23��', '23', null, null, null, null, null);
insert into UMS_CT_DSMM (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('24', '24��', '24', null, null, null, null, null);
insert into UMS_CT_DSMM (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('25', '25��', '25', null, null, null, null, null);
insert into UMS_CT_DSMM (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('26', '26��', '26', null, null, null, null, null);
insert into UMS_CT_DSMM (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('27', '27��', '27', null, null, null, null, null);
insert into UMS_CT_DSMM (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('28', '28��', '28', null, null, null, null, null);
insert into UMS_CT_DSMM (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('29', '29��', '29', null, null, null, null, null);
insert into UMS_CT_DSMM (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('30', '30��', '30', null, null, null, null, null);
insert into UMS_CT_DSMM (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('31', '31��', '31', null, null, null, null, null);
insert into UMS_CT_DSMM (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('32', '32��', '32', null, null, null, null, null);
insert into UMS_CT_DSMM (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('33', '33��', '33', null, null, null, null, null);
insert into UMS_CT_DSMM (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('34', '34��', '34', null, null, null, null, null);
insert into UMS_CT_DSMM (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('35', '35��', '35', null, null, null, null, null);
insert into UMS_CT_DSMM (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('36', '36��', '36', null, null, null, null, null);
insert into UMS_CT_DSMM (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('37', '37��', '37', null, null, null, null, null);
insert into UMS_CT_DSMM (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('38', '38��', '38', null, null, null, null, null);
insert into UMS_CT_DSMM (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('39', '39��', '39', null, null, null, null, null);
insert into UMS_CT_DSMM (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('40', '40��', '40', null, null, null, null, null);
insert into UMS_CT_DSMM (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('41', '41��', '41', null, null, null, null, null);
insert into UMS_CT_DSMM (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('42', '42��', '42', null, null, null, null, null);
insert into UMS_CT_DSMM (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('43', '43��', '43', null, null, null, null, null);
insert into UMS_CT_DSMM (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('44', '44��', '44', null, null, null, null, null);
insert into UMS_CT_DSMM (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('45', '45��', '45', null, null, null, null, null);
insert into UMS_CT_DSMM (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('46', '46��', '46', null, null, null, null, null);
insert into UMS_CT_DSMM (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('47', '47��', '47', null, null, null, null, null);
insert into UMS_CT_DSMM (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('48', '48��', '48', null, null, null, null, null);
insert into UMS_CT_DSMM (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('49', '49��', '49', null, null, null, null, null);
insert into UMS_CT_DSMM (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('50', '50��', '50', null, null, null, null, null);
insert into UMS_CT_DSMM (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('51', '51��', '51', null, null, null, null, null);
insert into UMS_CT_DSMM (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('52', '52��', '52', null, null, null, null, null);
insert into UMS_CT_DSMM (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('53', '53��', '53', null, null, null, null, null);
insert into UMS_CT_DSMM (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('54', '54��', '54', null, null, null, null, null);
insert into UMS_CT_DSMM (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('55', '55��', '55', null, null, null, null, null);
insert into UMS_CT_DSMM (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('56', '56��', '56', null, null, null, null, null);
insert into UMS_CT_DSMM (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('57', '57��', '57', null, null, null, null, null);
insert into UMS_CT_DSMM (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('58', '58��', '58', null, null, null, null, null);
insert into UMS_CT_DSMM (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('59', '59��', '59', null, null, null, null, null);
commit;
prompt 60 records loaded
prompt Loading UMS_CT_FFFSFX...
insert into UMS_CT_FFFSFX (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('3', 'WEBAPPLICATION', '8', null, null, null, null, null);
insert into UMS_CT_FFFSFX (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('1', '��������', '0', null, null, null, null, null);
insert into UMS_CT_FFFSFX (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('2', '��������', '1', null, null, null, null, null);
commit;
prompt 3 records loaded
prompt Loading UMS_CT_FFLX...
insert into UMS_CT_FFLX (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('1', 'socket', '0', null, null, null, null, null);
insert into UMS_CT_FFLX (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('2', 'jms', '1', null, null, null, null, null);
insert into UMS_CT_FFLX (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('3', 'webservice', '2', null, null, null, null, null);
commit;
prompt 3 records loaded
prompt Loading UMS_CT_FFZT...
insert into UMS_CT_FFZT (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('1', '����', '0', null, null, null, null, null);
insert into UMS_CT_FFZT (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('2', 'ֹͣ', '1', null, null, null, null, null);
commit;
prompt 2 records loaded
prompt Loading UMS_CT_FILTER...
insert into UMS_CT_FILTER (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('1', 'ʹ����', 0, null, null, null, null, null);
insert into UMS_CT_FILTER (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('0', 'ֹͣʹ��', 1, null, null, null, null, null);
commit;
prompt 2 records loaded
prompt Loading UMS_CT_FYLX...
insert into UMS_CT_FYLX (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('3', '��SP�Ʒ�', '2', null, null, null, null, null);
insert into UMS_CT_FYLX (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('4', '��Ч', '3', null, null, null, null, null);
insert into UMS_CT_FYLX (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('1', '��Ŀ���ն�MSISDN�Ʒ�', '0', null, null, null, null, null);
insert into UMS_CT_FYLX (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('2', '��Դ�ն�MSISDN�Ʒ�', '1', null, null, null, null, null);
commit;
prompt 4 records loaded
prompt Loading UMS_CT_JMS...
insert into UMS_CT_JMS (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('1', '��ǰϵͳ', '1', null, null, null, null, null);
insert into UMS_CT_JMS (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('2', '�ϼ�ϵͳ', '2', null, null, null, null, null);
insert into UMS_CT_JMS (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('3', '�¼�ϵͳ', '3', null, null, null, null, null);
insert into UMS_CT_JMS (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('4', 'ͬ���ǵ�ǰϵͳ', '4', null, null, null, null, null);
commit;
prompt 4 records loaded
prompt Loading UMS_CT_MEDIASTYLE...
insert into UMS_CT_MEDIASTYLE (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('1', '�ֻ�', '1', null, null, null, null, null);
insert into UMS_CT_MEDIASTYLE (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('2', 'email', '2', null, null, null, null, null);
insert into UMS_CT_MEDIASTYLE (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('3', 'lcs', '3', null, null, null, null, null);
insert into UMS_CT_MEDIASTYLE (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('4', 'web�û�', '4', null, null, null, null, null);
commit;
prompt 4 records loaded
prompt Loading UMS_CT_OUT_READY_ACK...
insert into UMS_CT_OUT_READY_ACK (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('0', '�����ִ', '0', null, null, null, null, null);
insert into UMS_CT_OUT_READY_ACK (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('1', '���ִ', '1', null, null, null, null, null);
commit;
prompt 2 records loaded
prompt Loading UMS_CT_OUT_READY_NEEDREPLY...
insert into UMS_CT_OUT_READY_NEEDREPLY (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('0', '����ظ�', '0', null, null, null, null, null);
insert into UMS_CT_OUT_READY_NEEDREPLY (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('1', '��ظ�', '1', null, null, null, null, null);
commit;
prompt 2 records loaded
prompt Loading UMS_CT_OUT_READY_PRIORITY...
insert into UMS_CT_OUT_READY_PRIORITY (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('0', '��ͨ', '0', null, null, null, null, null);
insert into UMS_CT_OUT_READY_PRIORITY (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('1', '��', '1', null, null, null, null, null);
commit;
prompt 2 records loaded
prompt Loading UMS_CT_OUT_READY_SENDDIRECTLY...
insert into UMS_CT_OUT_READY_SENDDIRECTLY (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('0', '���Է���', '0', null, null, null, null, null);
insert into UMS_CT_OUT_READY_SENDDIRECTLY (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('1', 'ֱ�ӷ���', '1', null, null, null, null, null);
commit;
prompt 2 records loaded
prompt Loading UMS_CT_OUT_READY_TIMESETFLAG...
insert into UMS_CT_OUT_READY_TIMESETFLAG (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('0', '�Ƕ�ʱ', '0', null, null, null, null, null);
insert into UMS_CT_OUT_READY_TIMESETFLAG (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('1', '��ʱ', '1', null, null, null, null, null);
commit;
prompt 2 records loaded
prompt Loading UMS_CT_OUT_READY_UMSFLAG...
insert into UMS_CT_OUT_READY_UMSFLAG (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('1', '����ums', '1', null, null, null, null, null);
insert into UMS_CT_OUT_READY_UMSFLAG (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('2', '����ums', '2', null, null, null, null, null);
commit;
prompt 2 records loaded
prompt Loading UMS_CT_OUT_READY_V3_BATCHMODE...
insert into UMS_CT_OUT_READY_V3_BATCHMODE (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('0', '����', '0', null, null, null, null, null);
insert into UMS_CT_OUT_READY_V3_BATCHMODE (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('1', '����', '1', null, null, null, null, null);
commit;
prompt 2 records loaded
prompt Loading UMS_CT_OUT_READY_V3_STATUSFLAG...
insert into UMS_CT_OUT_READY_V3_STATUSFLAG (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('0', '����', '0', null, null, null, null, null);
insert into UMS_CT_OUT_READY_V3_STATUSFLAG (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('1', 'δ����', '1', null, null, null, null, null);
commit;
prompt 2 records loaded
prompt Loading UMS_CT_QDYXJ...
insert into UMS_CT_QDYXJ (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('5', '���ȼ���5', '5', null, null, null, null, null);
insert into UMS_CT_QDYXJ (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('6', '���ȼ���6', '6', null, null, null, null, null);
insert into UMS_CT_QDYXJ (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('1', '���ȼ���1', '1', null, null, null, null, null);
insert into UMS_CT_QDYXJ (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('2', '���ȼ���2', '2', null, null, null, null, null);
insert into UMS_CT_QDYXJ (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('3', '���ȼ���3', '3', null, null, null, null, null);
insert into UMS_CT_QDYXJ (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('4', '���ȼ���4', '4', null, null, null, null, null);
commit;
prompt 6 records loaded
prompt Loading UMS_CT_SFZJFS...
insert into UMS_CT_SFZJFS (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('1', 'ֱ�ӷ���', '1', null, null, null, null, null);
insert into UMS_CT_SFZJFS (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('2', '�����Է���', '0', null, null, null, null, null);
commit;
prompt 2 records loaded
prompt Loading UMS_CT_SJJG...
insert into UMS_CT_SJJG (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('1', '00:00-01:00', '00', null, null, null, null, null);
insert into UMS_CT_SJJG (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('2', '01:00-02:00', '01', null, null, null, null, null);
insert into UMS_CT_SJJG (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('3', '02:00-03:00', '02', null, null, null, null, null);
insert into UMS_CT_SJJG (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('4', '03:00-04:00', '03', null, null, null, null, null);
insert into UMS_CT_SJJG (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('5', '04:00-05:00', '04', null, null, null, null, null);
insert into UMS_CT_SJJG (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('6', '05:00-06:00', '05', null, null, null, null, null);
insert into UMS_CT_SJJG (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('7', '06:00-07:00', '06', null, null, null, null, null);
insert into UMS_CT_SJJG (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('8', '07:00-08:00', '07', null, null, null, null, null);
insert into UMS_CT_SJJG (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('9', '08:00-09:00', '08', null, null, null, null, null);
insert into UMS_CT_SJJG (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('10', '09:00-10:00', '09', null, null, null, null, null);
insert into UMS_CT_SJJG (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('11', '10:00-11:00', '10', null, null, null, null, null);
insert into UMS_CT_SJJG (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('12', '11:00-12:00', '11', null, null, null, null, null);
insert into UMS_CT_SJJG (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('13', '12:00-13:00', '12', null, null, null, null, null);
insert into UMS_CT_SJJG (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('14', '13:00-14:00', '13', null, null, null, null, null);
insert into UMS_CT_SJJG (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('15', '14:00-15:00', '14', null, null, null, null, null);
insert into UMS_CT_SJJG (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('16', '15:00-16:00', '15', null, null, null, null, null);
insert into UMS_CT_SJJG (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('17', '16:00-17:00', '16', null, null, null, null, null);
insert into UMS_CT_SJJG (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('18', '17:00-18:00', '17', null, null, null, null, null);
insert into UMS_CT_SJJG (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('19', '18:00-19:00', '18', null, null, null, null, null);
insert into UMS_CT_SJJG (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('20', '19:00-20:00', '19', null, null, null, null, null);
insert into UMS_CT_SJJG (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('21', '20:00-21:00', '20', null, null, null, null, null);
insert into UMS_CT_SJJG (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('22', '21:00-22:00', '21', null, null, null, null, null);
insert into UMS_CT_SJJG (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('23', '22:00-23:00', '22', null, null, null, null, null);
insert into UMS_CT_SJJG (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('24', '23:00-24:00', '23', null, null, null, null, null);
commit;
prompt 24 records loaded
prompt Loading UMS_CT_SYZT...
insert into UMS_CT_SYZT (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('1', 'ʹ����', '0', null, null, null, null, null);
insert into UMS_CT_SYZT (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('2', 'ֹͣʹ��', '1', null, null, null, null, null);
commit;
prompt 2 records loaded
prompt Loading UMS_CT_YYZT...
insert into UMS_CT_YYZT (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('1', '����', '0', null, null, null, null, null);
insert into UMS_CT_YYZT (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('2', 'ֹͣ', '1', null, null, null, null, null);
commit;
prompt 2 records loaded
prompt Loading UUM_ROLE...
insert into UUM_ROLE (ROLE_ID, ROLE_NAME, ROLE_DESC, ROLE_CHAR, ROLE_ADMIN_ID, ROLE_CREATE_ID, ROLE_CREATE_DATE, ROLE_UPDATE_ID, ROLE_UPDATE_DATE)
values (517, '��ܹ���Ա', null, '2', '0000', '0000', to_date('07-09-2007', 'dd-mm-yyyy'), '0000', to_date('16-09-2007', 'dd-mm-yyyy'));
insert into UUM_ROLE (ROLE_ID, ROLE_NAME, ROLE_DESC, ROLE_CHAR, ROLE_ADMIN_ID, ROLE_CREATE_ID, ROLE_CREATE_DATE, ROLE_UPDATE_ID, ROLE_UPDATE_DATE)
values (518, '�絥λ����Ȩ��', null, '1', '0000', '0000', to_date('07-09-2007', 'dd-mm-yyyy'), '0000', to_date('11-09-2007', 'dd-mm-yyyy'));
insert into UUM_ROLE (ROLE_ID, ROLE_NAME, ROLE_DESC, ROLE_CHAR, ROLE_ADMIN_ID, ROLE_CREATE_ID, ROLE_CREATE_DATE, ROLE_UPDATE_ID, ROLE_UPDATE_DATE)
values (519, '����λ����Ȩ��', null, '1', '0000', '0000', to_date('07-09-2007', 'dd-mm-yyyy'), '0000', to_date('07-09-2007', 'dd-mm-yyyy'));
insert into UUM_ROLE (ROLE_ID, ROLE_NAME, ROLE_DESC, ROLE_CHAR, ROLE_ADMIN_ID, ROLE_CREATE_ID, ROLE_CREATE_DATE, ROLE_UPDATE_ID, ROLE_UPDATE_DATE)
values (520, '����������Ȩ��1', '�������ⲿ�� ', '1', '0000', '0000', to_date('07-09-2007', 'dd-mm-yyyy'), '0000', to_date('07-09-2007', 'dd-mm-yyyy'));
insert into UUM_ROLE (ROLE_ID, ROLE_NAME, ROLE_DESC, ROLE_CHAR, ROLE_ADMIN_ID, ROLE_CREATE_ID, ROLE_CREATE_DATE, ROLE_UPDATE_ID, ROLE_UPDATE_DATE)
values (521, '����������Ȩ��2', '�����ⲿ�ţ���Ա���������ⲿ����', '1', '0000', '0000', to_date('07-09-2007', 'dd-mm-yyyy'), '0000', to_date('07-09-2007', 'dd-mm-yyyy'));
insert into UUM_ROLE (ROLE_ID, ROLE_NAME, ROLE_DESC, ROLE_CHAR, ROLE_ADMIN_ID, ROLE_CREATE_ID, ROLE_CREATE_DATE, ROLE_UPDATE_ID, ROLE_UPDATE_DATE)
values (522, 'ֱ����������Ȩ��', null, '1', '0000', '0000', to_date('07-09-2007', 'dd-mm-yyyy'), '0000', to_date('11-09-2007', 'dd-mm-yyyy'));
insert into UUM_ROLE (ROLE_ID, ROLE_NAME, ROLE_DESC, ROLE_CHAR, ROLE_ADMIN_ID, ROLE_CREATE_ID, ROLE_CREATE_DATE, ROLE_UPDATE_ID, ROLE_UPDATE_DATE)
values (523, '�������ݽ�ɫ', null, '1', '0000', '0000', to_date('07-09-2007', 'dd-mm-yyyy'), '0000', to_date('07-09-2007', 'dd-mm-yyyy'));
insert into UUM_ROLE (ROLE_ID, ROLE_NAME, ROLE_DESC, ROLE_CHAR, ROLE_ADMIN_ID, ROLE_CREATE_ID, ROLE_CREATE_DATE, ROLE_UPDATE_ID, ROLE_UPDATE_DATE)
values (505, 'HR��ѵ��������Ա', 'ӵ�����в˵�', '2', '234', '234', to_date('31-05-2007', 'dd-mm-yyyy'), '0000', to_date('11-09-2007', 'dd-mm-yyyy'));
insert into UUM_ROLE (ROLE_ID, ROLE_NAME, ROLE_DESC, ROLE_CHAR, ROLE_ADMIN_ID, ROLE_CREATE_ID, ROLE_CREATE_DATE, ROLE_UPDATE_ID, ROLE_UPDATE_DATE)
values (515, 'HR��ѵ��ͨԱ��', null, '2', '0000', '0000', to_date('05-09-2007', 'dd-mm-yyyy'), '0000', to_date('11-09-2007', 'dd-mm-yyyy'));
insert into UUM_ROLE (ROLE_ID, ROLE_NAME, ROLE_DESC, ROLE_CHAR, ROLE_ADMIN_ID, ROLE_CREATE_ID, ROLE_CREATE_DATE, ROLE_UPDATE_ID, ROLE_UPDATE_DATE)
values (506, 'HR��ѵ��������Ա', '������ѵ���ġ�����������ѵ�������ճ�����', '2', '234', '234', to_date('31-05-2007', 'dd-mm-yyyy'), '0000', to_date('07-09-2007', 'dd-mm-yyyy'));
insert into UUM_ROLE (ROLE_ID, ROLE_NAME, ROLE_DESC, ROLE_CHAR, ROLE_ADMIN_ID, ROLE_CREATE_ID, ROLE_CREATE_DATE, ROLE_UPDATE_ID, ROLE_UPDATE_DATE)
values (516, 'HR��ѵ�����쵼', null, '2', '0000', '0000', to_date('05-09-2007', 'dd-mm-yyyy'), '0000', to_date('11-09-2007', 'dd-mm-yyyy'));
insert into UUM_ROLE (ROLE_ID, ROLE_NAME, ROLE_DESC, ROLE_CHAR, ROLE_ADMIN_ID, ROLE_CREATE_ID, ROLE_CREATE_DATE, ROLE_UPDATE_ID, ROLE_UPDATE_DATE)
values (510, 'HR��ѵʡ������Ա', null, '2', '0000', '0000', to_date('04-09-2007', 'dd-mm-yyyy'), '0000', to_date('07-09-2007', 'dd-mm-yyyy'));
insert into UUM_ROLE (ROLE_ID, ROLE_NAME, ROLE_DESC, ROLE_CHAR, ROLE_ADMIN_ID, ROLE_CREATE_ID, ROLE_CREATE_DATE, ROLE_UPDATE_ID, ROLE_UPDATE_DATE)
values (511, 'HR��ѵ����λ����Ա', null, '2', '0000', '0000', to_date('05-09-2007', 'dd-mm-yyyy'), '0000', to_date('05-09-2007', 'dd-mm-yyyy'));
insert into UUM_ROLE (ROLE_ID, ROLE_NAME, ROLE_DESC, ROLE_CHAR, ROLE_ADMIN_ID, ROLE_CREATE_ID, ROLE_CREATE_DATE, ROLE_UPDATE_ID, ROLE_UPDATE_DATE)
values (512, 'HR��ѵ���ݹ���Ա', null, '2', '0000', '0000', to_date('05-09-2007', 'dd-mm-yyyy'), '0000', to_date('05-09-2007', 'dd-mm-yyyy'));
insert into UUM_ROLE (ROLE_ID, ROLE_NAME, ROLE_DESC, ROLE_CHAR, ROLE_ADMIN_ID, ROLE_CREATE_ID, ROLE_CREATE_DATE, ROLE_UPDATE_ID, ROLE_UPDATE_DATE)
values (513, 'HR��ѵ�ƻ�����Ա', null, '2', '0000', '0000', to_date('05-09-2007', 'dd-mm-yyyy'), '0000', to_date('05-09-2007', 'dd-mm-yyyy'));
insert into UUM_ROLE (ROLE_ID, ROLE_NAME, ROLE_DESC, ROLE_CHAR, ROLE_ADMIN_ID, ROLE_CREATE_ID, ROLE_CREATE_DATE, ROLE_UPDATE_ID, ROLE_UPDATE_DATE)
values (514, 'HR��ѵѧ������Ա', null, '2', '0000', '0000', to_date('05-09-2007', 'dd-mm-yyyy'), '0000', to_date('05-09-2007', 'dd-mm-yyyy'));
commit;
prompt 16 records loaded
prompt Enabling triggers for APPLICATION...
alter table APPLICATION enable all triggers;
prompt Enabling triggers for MEDIA...
alter table MEDIA enable all triggers;
prompt Enabling triggers for UMS_CT_BRZT...
alter table UMS_CT_BRZT enable all triggers;
prompt Enabling triggers for UMS_CT_DSHH...
alter table UMS_CT_DSHH enable all triggers;
prompt Enabling triggers for UMS_CT_DSMM...
alter table UMS_CT_DSMM enable all triggers;
prompt Enabling triggers for UMS_CT_FFFSFX...
alter table UMS_CT_FFFSFX enable all triggers;
prompt Enabling triggers for UMS_CT_FFLX...
alter table UMS_CT_FFLX enable all triggers;
prompt Enabling triggers for UMS_CT_FFZT...
alter table UMS_CT_FFZT enable all triggers;
prompt Enabling triggers for UMS_CT_FILTER...
alter table UMS_CT_FILTER enable all triggers;
prompt Enabling triggers for UMS_CT_FYLX...
alter table UMS_CT_FYLX enable all triggers;
prompt Enabling triggers for UMS_CT_JMS...
alter table UMS_CT_JMS enable all triggers;
prompt Enabling triggers for UMS_CT_MEDIASTYLE...
alter table UMS_CT_MEDIASTYLE enable all triggers;
prompt Enabling triggers for UMS_CT_OUT_READY_ACK...
alter table UMS_CT_OUT_READY_ACK enable all triggers;
prompt Enabling triggers for UMS_CT_OUT_READY_NEEDREPLY...
alter table UMS_CT_OUT_READY_NEEDREPLY enable all triggers;
prompt Enabling triggers for UMS_CT_OUT_READY_PRIORITY...
alter table UMS_CT_OUT_READY_PRIORITY enable all triggers;
prompt Enabling triggers for UMS_CT_OUT_READY_SENDDIRECTLY...
alter table UMS_CT_OUT_READY_SENDDIRECTLY enable all triggers;
prompt Enabling triggers for UMS_CT_OUT_READY_TIMESETFLAG...
alter table UMS_CT_OUT_READY_TIMESETFLAG enable all triggers;
prompt Enabling triggers for UMS_CT_OUT_READY_UMSFLAG...
alter table UMS_CT_OUT_READY_UMSFLAG enable all triggers;
prompt Enabling triggers for UMS_CT_OUT_READY_V3_BATCHMODE...
alter table UMS_CT_OUT_READY_V3_BATCHMODE enable all triggers;
prompt Enabling triggers for UMS_CT_OUT_READY_V3_STATUSFLAG...
alter table UMS_CT_OUT_READY_V3_STATUSFLAG enable all triggers;
prompt Enabling triggers for UMS_CT_QDYXJ...
alter table UMS_CT_QDYXJ enable all triggers;
prompt Enabling triggers for UMS_CT_SFZJFS...
alter table UMS_CT_SFZJFS enable all triggers;
prompt Enabling triggers for UMS_CT_SJJG...
alter table UMS_CT_SJJG enable all triggers;
prompt Enabling triggers for UMS_CT_SYZT...
alter table UMS_CT_SYZT enable all triggers;
prompt Enabling triggers for UMS_CT_YYZT...
alter table UMS_CT_YYZT enable all triggers;
prompt Enabling triggers for UUM_ROLE...
alter table UUM_ROLE enable all triggers;
set feedback on
set define on
prompt Done.
