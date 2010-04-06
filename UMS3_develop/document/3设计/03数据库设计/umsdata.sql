prompt PL/SQL Developer import file
prompt Created on 2007年12月26日 by wu00000bing
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
values ('1', 'WEB', 'WEB平台管理', 'web', null, '0', '127.0.0.1', 8080, 100, '1', 1, null, null, '1', '1', 1, 1, '550');
commit;
prompt 1 record loaded
prompt Loading MEDIA...
insert into MEDIA (MEDIA_ID, MEDIANAME, CLASS, TYPE, STATUSFLAG, IP, PORT, TIMEOUT, REPEATTIMES, STARTWORKTIME, ENDWORKTIME, LOGINNAME, LOGINPASSWORD, SLEEPTIME, PROP1, PROP2, PROP3, PROP4, PROP5, SEQKEY, MEDIASTYLE)
values ('013', 'NCI移动短消息', 'com.nci.ums.channel.outchannel.NCIOutChannel', 1, 1, null, 0, 0, 200, '0', '240000', '240000', null, 300, null, null, null, null, null, '1', 1);
insert into MEDIA (MEDIA_ID, MEDIANAME, CLASS, TYPE, STATUSFLAG, IP, PORT, TIMEOUT, REPEATTIMES, STARTWORKTIME, ENDWORKTIME, LOGINNAME, LOGINPASSWORD, SLEEPTIME, PROP1, PROP2, PROP3, PROP4, PROP5, SEQKEY, MEDIASTYLE)
values ('015', 'NCIV3移动短消息', 'com.nci.ums.channel.outchannel.NCIOutChannel_V3', 1, 0, null, 0, 0, 200, '0', '240000', '240000', null, 300, null, null, null, null, null, '2', 1);
insert into MEDIA (MEDIA_ID, MEDIANAME, CLASS, TYPE, STATUSFLAG, IP, PORT, TIMEOUT, REPEATTIMES, STARTWORKTIME, ENDWORKTIME, LOGINNAME, LOGINPASSWORD, SLEEPTIME, PROP1, PROP2, PROP3, PROP4, PROP5, SEQKEY, MEDIASTYLE)
values ('076', 'NCI Email外拨', 'com.nci.ums.channel.outchannel.EmailOutChannel_V3', 1, 0, null, 0, 0, 0, '0', '240000', '240000', null, 300, null, null, null, null, null, '3', 2);
insert into MEDIA (MEDIA_ID, MEDIANAME, CLASS, TYPE, STATUSFLAG, IP, PORT, TIMEOUT, REPEATTIMES, STARTWORKTIME, ENDWORKTIME, LOGINNAME, LOGINPASSWORD, SLEEPTIME, PROP1, PROP2, PROP3, PROP4, PROP5, SEQKEY, MEDIASTYLE)
values ('077', 'NCI Email内拨', 'com.nci.ums.channel.inchannel.email.EmailInChannel_V3', 0, 0, null, 0, 0, 0, '0', '240000', '240000', null, 300, null, null, null, null, null, '4', 2);
commit;
prompt 4 records loaded
prompt Loading UMS_CT_BRZT...
insert into UMS_CT_BRZT (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('1', '拨出', '1', null, null, null, null, null);
insert into UMS_CT_BRZT (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('2', '拨入', '0', null, null, null, null, null);
commit;
prompt 2 records loaded
prompt Loading UMS_CT_DSHH...
insert into UMS_CT_DSHH (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('1', '0时', '0', null, null, null, null, null);
insert into UMS_CT_DSHH (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('2', '1时', '1', null, null, null, null, null);
insert into UMS_CT_DSHH (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('3', '2时', '2', null, null, null, null, null);
insert into UMS_CT_DSHH (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('4', '3时', '3', null, null, null, null, null);
insert into UMS_CT_DSHH (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('5', '4时', '4', null, null, null, null, null);
insert into UMS_CT_DSHH (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('6', '5时', '5', null, null, null, null, null);
insert into UMS_CT_DSHH (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('7', '6时', '6', null, null, null, null, null);
insert into UMS_CT_DSHH (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('8', '7时', '7', null, null, null, null, null);
insert into UMS_CT_DSHH (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('9', '8时', '8', null, null, null, null, null);
insert into UMS_CT_DSHH (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('10', '9时', '9', null, null, null, null, null);
insert into UMS_CT_DSHH (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('11', '10时', '10', null, null, null, null, null);
insert into UMS_CT_DSHH (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('12', '11时', '11', null, null, null, null, null);
insert into UMS_CT_DSHH (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('13', '12时', '12', null, null, null, null, null);
insert into UMS_CT_DSHH (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('14', '13时', '13', null, null, null, null, null);
insert into UMS_CT_DSHH (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('15', '14时', '14', null, null, null, null, null);
insert into UMS_CT_DSHH (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('16', '15时', '15', null, null, null, null, null);
insert into UMS_CT_DSHH (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('17', '16时', '16', null, null, null, null, null);
insert into UMS_CT_DSHH (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('18', '17时', '17', null, null, null, null, null);
insert into UMS_CT_DSHH (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('19', '18时', '18', null, null, null, null, null);
insert into UMS_CT_DSHH (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('20', '19时', '19', null, null, null, null, null);
insert into UMS_CT_DSHH (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('21', '20时', '20', null, null, null, null, null);
insert into UMS_CT_DSHH (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('22', '21时', '21', null, null, null, null, null);
insert into UMS_CT_DSHH (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('23', '22时', '22', null, null, null, null, null);
insert into UMS_CT_DSHH (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('24', '23时', '23', null, null, null, null, null);
commit;
prompt 24 records loaded
prompt Loading UMS_CT_DSMM...
insert into UMS_CT_DSMM (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('0', '0分', '0', null, null, null, null, null);
insert into UMS_CT_DSMM (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('1', '1分', '1', null, null, null, null, null);
insert into UMS_CT_DSMM (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('2', '2分', '2', null, null, null, null, null);
insert into UMS_CT_DSMM (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('3', '3分', '3', null, null, null, null, null);
insert into UMS_CT_DSMM (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('4', '4分', '4', null, null, null, null, null);
insert into UMS_CT_DSMM (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('5', '5分', '5', null, null, null, null, null);
insert into UMS_CT_DSMM (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('6', '6分', '6', null, null, null, null, null);
insert into UMS_CT_DSMM (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('7', '7分', '7', null, null, null, null, null);
insert into UMS_CT_DSMM (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('8', '8分', '8', null, null, null, null, null);
insert into UMS_CT_DSMM (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('9', '9分', '9', null, null, null, null, null);
insert into UMS_CT_DSMM (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('10', '10分', '10', null, null, null, null, null);
insert into UMS_CT_DSMM (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('11', '11分', '11', null, null, null, null, null);
insert into UMS_CT_DSMM (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('12', '12分', '12', null, null, null, null, null);
insert into UMS_CT_DSMM (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('13', '13分', '13', null, null, null, null, null);
insert into UMS_CT_DSMM (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('14', '14分', '14', null, null, null, null, null);
insert into UMS_CT_DSMM (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('15', '15分', '15', null, null, null, null, null);
insert into UMS_CT_DSMM (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('16', '16分', '16', null, null, null, null, null);
insert into UMS_CT_DSMM (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('17', '17分', '17', null, null, null, null, null);
insert into UMS_CT_DSMM (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('18', '18分', '18', null, null, null, null, null);
insert into UMS_CT_DSMM (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('19', '19分', '19', null, null, null, null, null);
insert into UMS_CT_DSMM (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('20', '20分', '20', null, null, null, null, null);
insert into UMS_CT_DSMM (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('21', '21分', '21', null, null, null, null, null);
insert into UMS_CT_DSMM (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('22', '22分', '22', null, null, null, null, null);
insert into UMS_CT_DSMM (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('23', '23分', '23', null, null, null, null, null);
insert into UMS_CT_DSMM (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('24', '24分', '24', null, null, null, null, null);
insert into UMS_CT_DSMM (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('25', '25分', '25', null, null, null, null, null);
insert into UMS_CT_DSMM (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('26', '26分', '26', null, null, null, null, null);
insert into UMS_CT_DSMM (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('27', '27分', '27', null, null, null, null, null);
insert into UMS_CT_DSMM (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('28', '28分', '28', null, null, null, null, null);
insert into UMS_CT_DSMM (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('29', '29分', '29', null, null, null, null, null);
insert into UMS_CT_DSMM (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('30', '30分', '30', null, null, null, null, null);
insert into UMS_CT_DSMM (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('31', '31分', '31', null, null, null, null, null);
insert into UMS_CT_DSMM (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('32', '32分', '32', null, null, null, null, null);
insert into UMS_CT_DSMM (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('33', '33分', '33', null, null, null, null, null);
insert into UMS_CT_DSMM (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('34', '34分', '34', null, null, null, null, null);
insert into UMS_CT_DSMM (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('35', '35分', '35', null, null, null, null, null);
insert into UMS_CT_DSMM (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('36', '36分', '36', null, null, null, null, null);
insert into UMS_CT_DSMM (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('37', '37分', '37', null, null, null, null, null);
insert into UMS_CT_DSMM (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('38', '38分', '38', null, null, null, null, null);
insert into UMS_CT_DSMM (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('39', '39分', '39', null, null, null, null, null);
insert into UMS_CT_DSMM (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('40', '40分', '40', null, null, null, null, null);
insert into UMS_CT_DSMM (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('41', '41分', '41', null, null, null, null, null);
insert into UMS_CT_DSMM (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('42', '42分', '42', null, null, null, null, null);
insert into UMS_CT_DSMM (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('43', '43分', '43', null, null, null, null, null);
insert into UMS_CT_DSMM (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('44', '44分', '44', null, null, null, null, null);
insert into UMS_CT_DSMM (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('45', '45分', '45', null, null, null, null, null);
insert into UMS_CT_DSMM (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('46', '46分', '46', null, null, null, null, null);
insert into UMS_CT_DSMM (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('47', '47分', '47', null, null, null, null, null);
insert into UMS_CT_DSMM (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('48', '48分', '48', null, null, null, null, null);
insert into UMS_CT_DSMM (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('49', '49分', '49', null, null, null, null, null);
insert into UMS_CT_DSMM (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('50', '50分', '50', null, null, null, null, null);
insert into UMS_CT_DSMM (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('51', '51分', '51', null, null, null, null, null);
insert into UMS_CT_DSMM (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('52', '52分', '52', null, null, null, null, null);
insert into UMS_CT_DSMM (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('53', '53分', '53', null, null, null, null, null);
insert into UMS_CT_DSMM (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('54', '54分', '54', null, null, null, null, null);
insert into UMS_CT_DSMM (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('55', '55分', '55', null, null, null, null, null);
insert into UMS_CT_DSMM (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('56', '56分', '56', null, null, null, null, null);
insert into UMS_CT_DSMM (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('57', '57分', '57', null, null, null, null, null);
insert into UMS_CT_DSMM (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('58', '58分', '58', null, null, null, null, null);
insert into UMS_CT_DSMM (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('59', '59分', '59', null, null, null, null, null);
commit;
prompt 60 records loaded
prompt Loading UMS_CT_FFFSFX...
insert into UMS_CT_FFFSFX (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('3', 'WEBAPPLICATION', '8', null, null, null, null, null);
insert into UMS_CT_FFFSFX (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('1', '主动发送', '0', null, null, null, null, null);
insert into UMS_CT_FFFSFX (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('2', '被动接收', '1', null, null, null, null, null);
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
values ('1', '正常', '0', null, null, null, null, null);
insert into UMS_CT_FFZT (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('2', '停止', '1', null, null, null, null, null);
commit;
prompt 2 records loaded
prompt Loading UMS_CT_FILTER...
insert into UMS_CT_FILTER (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('1', '使用中', 0, null, null, null, null, null);
insert into UMS_CT_FILTER (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('0', '停止使用', 1, null, null, null, null, null);
commit;
prompt 2 records loaded
prompt Loading UMS_CT_FYLX...
insert into UMS_CT_FYLX (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('3', '对SP计费', '2', null, null, null, null, null);
insert into UMS_CT_FYLX (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('4', '无效', '3', null, null, null, null, null);
insert into UMS_CT_FYLX (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('1', '对目的终端MSISDN计费', '0', null, null, null, null, null);
insert into UMS_CT_FYLX (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('2', '对源终端MSISDN计费', '1', null, null, null, null, null);
commit;
prompt 4 records loaded
prompt Loading UMS_CT_JMS...
insert into UMS_CT_JMS (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('1', '当前系统', '1', null, null, null, null, null);
insert into UMS_CT_JMS (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('2', '上级系统', '2', null, null, null, null, null);
insert into UMS_CT_JMS (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('3', '下级系统', '3', null, null, null, null, null);
insert into UMS_CT_JMS (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('4', '同级非当前系统', '4', null, null, null, null, null);
commit;
prompt 4 records loaded
prompt Loading UMS_CT_MEDIASTYLE...
insert into UMS_CT_MEDIASTYLE (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('1', '手机', '1', null, null, null, null, null);
insert into UMS_CT_MEDIASTYLE (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('2', 'email', '2', null, null, null, null, null);
insert into UMS_CT_MEDIASTYLE (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('3', 'lcs', '3', null, null, null, null, null);
insert into UMS_CT_MEDIASTYLE (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('4', 'web用户', '4', null, null, null, null, null);
commit;
prompt 4 records loaded
prompt Loading UMS_CT_OUT_READY_ACK...
insert into UMS_CT_OUT_READY_ACK (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('0', '不需回执', '0', null, null, null, null, null);
insert into UMS_CT_OUT_READY_ACK (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('1', '需回执', '1', null, null, null, null, null);
commit;
prompt 2 records loaded
prompt Loading UMS_CT_OUT_READY_NEEDREPLY...
insert into UMS_CT_OUT_READY_NEEDREPLY (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('0', '不需回复', '0', null, null, null, null, null);
insert into UMS_CT_OUT_READY_NEEDREPLY (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('1', '需回复', '1', null, null, null, null, null);
commit;
prompt 2 records loaded
prompt Loading UMS_CT_OUT_READY_PRIORITY...
insert into UMS_CT_OUT_READY_PRIORITY (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('0', '普通', '0', null, null, null, null, null);
insert into UMS_CT_OUT_READY_PRIORITY (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('1', '高', '1', null, null, null, null, null);
commit;
prompt 2 records loaded
prompt Loading UMS_CT_OUT_READY_SENDDIRECTLY...
insert into UMS_CT_OUT_READY_SENDDIRECTLY (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('0', '策略发送', '0', null, null, null, null, null);
insert into UMS_CT_OUT_READY_SENDDIRECTLY (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('1', '直接发送', '1', null, null, null, null, null);
commit;
prompt 2 records loaded
prompt Loading UMS_CT_OUT_READY_TIMESETFLAG...
insert into UMS_CT_OUT_READY_TIMESETFLAG (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('0', '非定时', '0', null, null, null, null, null);
insert into UMS_CT_OUT_READY_TIMESETFLAG (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('1', '定时', '1', null, null, null, null, null);
commit;
prompt 2 records loaded
prompt Loading UMS_CT_OUT_READY_UMSFLAG...
insert into UMS_CT_OUT_READY_UMSFLAG (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('1', '自身ums', '1', null, null, null, null, null);
insert into UMS_CT_OUT_READY_UMSFLAG (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('2', '其他ums', '2', null, null, null, null, null);
commit;
prompt 2 records loaded
prompt Loading UMS_CT_OUT_READY_V3_BATCHMODE...
insert into UMS_CT_OUT_READY_V3_BATCHMODE (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('0', '单笔', '0', null, null, null, null, null);
insert into UMS_CT_OUT_READY_V3_BATCHMODE (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('1', '批量', '1', null, null, null, null, null);
commit;
prompt 2 records loaded
prompt Loading UMS_CT_OUT_READY_V3_STATUSFLAG...
insert into UMS_CT_OUT_READY_V3_STATUSFLAG (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('0', '正常', '0', null, null, null, null, null);
insert into UMS_CT_OUT_READY_V3_STATUSFLAG (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('1', '未就绪', '1', null, null, null, null, null);
commit;
prompt 2 records loaded
prompt Loading UMS_CT_QDYXJ...
insert into UMS_CT_QDYXJ (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('5', '优先级别5', '5', null, null, null, null, null);
insert into UMS_CT_QDYXJ (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('6', '优先级别6', '6', null, null, null, null, null);
insert into UMS_CT_QDYXJ (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('1', '优先级别1', '1', null, null, null, null, null);
insert into UMS_CT_QDYXJ (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('2', '优先级别2', '2', null, null, null, null, null);
insert into UMS_CT_QDYXJ (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('3', '优先级别3', '3', null, null, null, null, null);
insert into UMS_CT_QDYXJ (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('4', '优先级别4', '4', null, null, null, null, null);
commit;
prompt 6 records loaded
prompt Loading UMS_CT_SFZJFS...
insert into UMS_CT_SFZJFS (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('1', '直接发送', '1', null, null, null, null, null);
insert into UMS_CT_SFZJFS (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('2', '按策略发送', '0', null, null, null, null, null);
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
values ('1', '使用中', '0', null, null, null, null, null);
insert into UMS_CT_SYZT (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('2', '停止使用', '1', null, null, null, null, null);
commit;
prompt 2 records loaded
prompt Loading UMS_CT_YYZT...
insert into UMS_CT_YYZT (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('1', '正常', '0', null, null, null, null, null);
insert into UMS_CT_YYZT (SEQKEY, CODENAME, CODEVALUE, CODELEVEL, PARENTID, ISLEAF, PY, DISPLAYORDER)
values ('2', '停止', '1', null, null, null, null, null);
commit;
prompt 2 records loaded
prompt Loading UUM_ROLE...
insert into UUM_ROLE (ROLE_ID, ROLE_NAME, ROLE_DESC, ROLE_CHAR, ROLE_ADMIN_ID, ROLE_CREATE_ID, ROLE_CREATE_DATE, ROLE_UPDATE_ID, ROLE_UPDATE_DATE)
values (517, '框架管理员', null, '2', '0000', '0000', to_date('07-09-2007', 'dd-mm-yyyy'), '0000', to_date('16-09-2007', 'dd-mm-yyyy'));
insert into UUM_ROLE (ROLE_ID, ROLE_NAME, ROLE_DESC, ROLE_CHAR, ROLE_ADMIN_ID, ROLE_CREATE_ID, ROLE_CREATE_DATE, ROLE_UPDATE_ID, ROLE_UPDATE_DATE)
values (518, '跨单位数据权限', null, '1', '0000', '0000', to_date('07-09-2007', 'dd-mm-yyyy'), '0000', to_date('11-09-2007', 'dd-mm-yyyy'));
insert into UUM_ROLE (ROLE_ID, ROLE_NAME, ROLE_DESC, ROLE_CHAR, ROLE_ADMIN_ID, ROLE_CREATE_ID, ROLE_CREATE_DATE, ROLE_UPDATE_ID, ROLE_UPDATE_DATE)
values (519, '本单位数据权限', null, '1', '0000', '0000', to_date('07-09-2007', 'dd-mm-yyyy'), '0000', to_date('07-09-2007', 'dd-mm-yyyy'));
insert into UUM_ROLE (ROLE_ID, ROLE_NAME, ROLE_DESC, ROLE_CHAR, ROLE_ADMIN_ID, ROLE_CREATE_ID, ROLE_CREATE_DATE, ROLE_UPDATE_ID, ROLE_UPDATE_DATE)
values (520, '本部门数据权限1', '不含虚拟部门 ', '1', '0000', '0000', to_date('07-09-2007', 'dd-mm-yyyy'), '0000', to_date('07-09-2007', 'dd-mm-yyyy'));
insert into UUM_ROLE (ROLE_ID, ROLE_NAME, ROLE_DESC, ROLE_CHAR, ROLE_ADMIN_ID, ROLE_CREATE_ID, ROLE_CREATE_DATE, ROLE_UPDATE_ID, ROLE_UPDATE_DATE)
values (521, '本部门数据权限2', '含虚拟部门，即员工挂在虚拟部门下', '1', '0000', '0000', to_date('07-09-2007', 'dd-mm-yyyy'), '0000', to_date('07-09-2007', 'dd-mm-yyyy'));
insert into UUM_ROLE (ROLE_ID, ROLE_NAME, ROLE_DESC, ROLE_CHAR, ROLE_ADMIN_ID, ROLE_CREATE_ID, ROLE_CREATE_DATE, ROLE_UPDATE_ID, ROLE_UPDATE_DATE)
values (522, '直属部门数据权限', null, '1', '0000', '0000', to_date('07-09-2007', 'dd-mm-yyyy'), '0000', to_date('11-09-2007', 'dd-mm-yyyy'));
insert into UUM_ROLE (ROLE_ID, ROLE_NAME, ROLE_DESC, ROLE_CHAR, ROLE_ADMIN_ID, ROLE_CREATE_ID, ROLE_CREATE_DATE, ROLE_UPDATE_ID, ROLE_UPDATE_DATE)
values (523, '个人数据角色', null, '1', '0000', '0000', to_date('07-09-2007', 'dd-mm-yyyy'), '0000', to_date('07-09-2007', 'dd-mm-yyyy'));
insert into UUM_ROLE (ROLE_ID, ROLE_NAME, ROLE_DESC, ROLE_CHAR, ROLE_ADMIN_ID, ROLE_CREATE_ID, ROLE_CREATE_DATE, ROLE_UPDATE_ID, ROLE_UPDATE_DATE)
values (505, 'HR培训超级管理员', '拥有所有菜单', '2', '234', '234', to_date('31-05-2007', 'dd-mm-yyyy'), '0000', to_date('11-09-2007', 'dd-mm-yyyy'));
insert into UUM_ROLE (ROLE_ID, ROLE_NAME, ROLE_DESC, ROLE_CHAR, ROLE_ADMIN_ID, ROLE_CREATE_ID, ROLE_CREATE_DATE, ROLE_UPDATE_ID, ROLE_UPDATE_DATE)
values (515, 'HR培训普通员工', null, '2', '0000', '0000', to_date('05-09-2007', 'dd-mm-yyyy'), '0000', to_date('11-09-2007', 'dd-mm-yyyy'));
insert into UUM_ROLE (ROLE_ID, ROLE_NAME, ROLE_DESC, ROLE_CHAR, ROLE_ADMIN_ID, ROLE_CREATE_ID, ROLE_CREATE_DATE, ROLE_UPDATE_ID, ROLE_UPDATE_DATE)
values (506, 'HR培训机构管理员', '负责培训中心、教培中心培训工作的日常管理', '2', '234', '234', to_date('31-05-2007', 'dd-mm-yyyy'), '0000', to_date('07-09-2007', 'dd-mm-yyyy'));
insert into UUM_ROLE (ROLE_ID, ROLE_NAME, ROLE_DESC, ROLE_CHAR, ROLE_ADMIN_ID, ROLE_CREATE_ID, ROLE_CREATE_DATE, ROLE_UPDATE_ID, ROLE_UPDATE_DATE)
values (516, 'HR培训主管领导', null, '2', '0000', '0000', to_date('05-09-2007', 'dd-mm-yyyy'), '0000', to_date('11-09-2007', 'dd-mm-yyyy'));
insert into UUM_ROLE (ROLE_ID, ROLE_NAME, ROLE_DESC, ROLE_CHAR, ROLE_ADMIN_ID, ROLE_CREATE_ID, ROLE_CREATE_DATE, ROLE_UPDATE_ID, ROLE_UPDATE_DATE)
values (510, 'HR培训省级管理员', null, '2', '0000', '0000', to_date('04-09-2007', 'dd-mm-yyyy'), '0000', to_date('07-09-2007', 'dd-mm-yyyy'));
insert into UUM_ROLE (ROLE_ID, ROLE_NAME, ROLE_DESC, ROLE_CHAR, ROLE_ADMIN_ID, ROLE_CREATE_ID, ROLE_CREATE_DATE, ROLE_UPDATE_ID, ROLE_UPDATE_DATE)
values (511, 'HR培训本单位管理员', null, '2', '0000', '0000', to_date('05-09-2007', 'dd-mm-yyyy'), '0000', to_date('05-09-2007', 'dd-mm-yyyy'));
insert into UUM_ROLE (ROLE_ID, ROLE_NAME, ROLE_DESC, ROLE_CHAR, ROLE_ADMIN_ID, ROLE_CREATE_ID, ROLE_CREATE_DATE, ROLE_UPDATE_ID, ROLE_UPDATE_DATE)
values (512, 'HR培训数据管理员', null, '2', '0000', '0000', to_date('05-09-2007', 'dd-mm-yyyy'), '0000', to_date('05-09-2007', 'dd-mm-yyyy'));
insert into UUM_ROLE (ROLE_ID, ROLE_NAME, ROLE_DESC, ROLE_CHAR, ROLE_ADMIN_ID, ROLE_CREATE_ID, ROLE_CREATE_DATE, ROLE_UPDATE_ID, ROLE_UPDATE_DATE)
values (513, 'HR培训计划管理员', null, '2', '0000', '0000', to_date('05-09-2007', 'dd-mm-yyyy'), '0000', to_date('05-09-2007', 'dd-mm-yyyy'));
insert into UUM_ROLE (ROLE_ID, ROLE_NAME, ROLE_DESC, ROLE_CHAR, ROLE_ADMIN_ID, ROLE_CREATE_ID, ROLE_CREATE_DATE, ROLE_UPDATE_ID, ROLE_UPDATE_DATE)
values (514, 'HR培训学历管理员', null, '2', '0000', '0000', to_date('05-09-2007', 'dd-mm-yyyy'), '0000', to_date('05-09-2007', 'dd-mm-yyyy'));
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
