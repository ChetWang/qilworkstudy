/*     */ package com.huawei.insa2.comm.sgip;
/*     */ 
/*     */ import com.huawei.insa2.util.Resource;
/*     */ 
/*     */ public class SGIPConstant
/*     */ {
/*     */   public static boolean debug;
/*     */   public static String LOGINING;
/*     */   public static String LOGIN_ERROR;
/*     */   public static String SEND_ERROR;
/*     */   public static String CONNECT_TIMEOUT;
/*     */   public static String STRUCTURE_ERROR;
/*     */   public static String NONLICETSP_LOGNAME;
/*     */   public static String NONLICETSP_ID;
/*     */   public static String SP_ERROR;
/*     */   public static String VERSION_ERROR;
/*     */   public static String OTHER_ERROR;
/*     */   public static String HEARTBEAT_ABNORMITY;
/*     */   public static String SUBMIT_INPUT_ERROR;
/*     */   public static String CONNECT_INPUT_ERROR;
/*     */   public static String CANCEL_INPUT_ERROR;
/*     */   public static String QUERY_INPUT_ERROR;
/*     */   public static String DELIVER_REPINPUT_ERROR;
/*     */   public static String REPORT_REPINPUT_ERROR;
/*     */   public static String USERREPORT_REPINPUT_ERROR;
/*     */   public static String ACTIVE_REPINPUT_ERROR;
/*     */   public static String SMC_MESSAGE_ERROR;
/*     */   public static String INT_SCOPE_ERROR;
/*     */   public static String STRING_LENGTH_GREAT;
/*     */   public static String STRING_NULL;
/*     */   public static String VALUE_ERROR;
/*     */   public static String FEE_USERTYPE_ERROR;
/*     */   public static String REGISTERED_DELIVERY_ERROR;
/*  95 */   public static final int Bind_Command_Id = 1;
/*     */ 
/*  97 */   public static final int Bind_Rep_Command_Id = -2147483647;
/*     */ 
/*  99 */   public static final int Unbind_Command_Id = 2;
/*     */ 
/* 101 */   public static final int Unbind_Rep_Command_Id = -2147483646;
/*     */ 
/* 103 */   public static final int Submit_Command_Id = 3;
/*     */ 
/* 105 */   public static final int Submit_Rep_Command_Id = -2147483645;
/*     */ 
/* 107 */   public static final int Deliver_Command_Id = 4;
/*     */ 
/* 109 */   public static final int Deliver_Rep_Command_Id = -2147483644;
/*     */ 
/* 111 */   public static final int Report_Command_Id = 5;
/*     */ 
/* 113 */   public static final int Report_Rep_Command_Id = -2147483643;
/*     */ 
/* 115 */   public static final int UserReport_Command_Id = 17;
/*     */ 
/* 117 */   public static final int UserReport_Rep_Command_Id = -2147483631;
/*     */ 
/*     */   public static void initConstant(Resource resource)
/*     */   {
/* 126 */     if (LOGINING == null) {
/* 127 */       LOGINING = resource.get("smproxy/logining");
/* 128 */       LOGIN_ERROR = resource.get("smproxy/login-error");
/* 129 */       SEND_ERROR = resource.get("smproxy/send-error");
/* 130 */       CONNECT_TIMEOUT = resource.get("smproxy/connect-timeout");
/* 131 */       STRUCTURE_ERROR = resource.get("smproxy/structure-error");
/* 132 */       NONLICETSP_LOGNAME = resource.get("smproxy/nonlicetsp-logname");
/* 133 */       NONLICETSP_ID = resource.get("smproxy/nonlicetsp-id");
/* 134 */       SP_ERROR = resource.get("smproxy/sp-error");
/* 135 */       VERSION_ERROR = resource.get("smproxy/version-error");
/* 136 */       OTHER_ERROR = resource.get("smproxy/other-error");
/* 137 */       HEARTBEAT_ABNORMITY = resource.get("smproxy/heartbeat-abnormity");
/* 138 */       SUBMIT_INPUT_ERROR = resource.get("smproxy/submit-input-error");
/* 139 */       CONNECT_INPUT_ERROR = resource.get("smproxy/connect-input-error");
/* 140 */       CANCEL_INPUT_ERROR = resource.get("smproxy/cancel-input-error");
/* 141 */       QUERY_INPUT_ERROR = resource.get("smproxy/query-input-error");
/* 142 */       DELIVER_REPINPUT_ERROR = resource.get("smproxy/deliver-repinput-error");
/* 143 */       REPORT_REPINPUT_ERROR = resource.get("smproxy/report-repinput-error");
/* 144 */       USERREPORT_REPINPUT_ERROR = resource.get("smproxy/userreport-repinput-error");
/* 145 */       ACTIVE_REPINPUT_ERROR = resource.get("smproxy/active-repinput-error");
/* 146 */       SMC_MESSAGE_ERROR = resource.get("smproxy/smc-message-error");
/* 147 */       INT_SCOPE_ERROR = resource.get("smproxy/int-scope-error");
/* 148 */       STRING_LENGTH_GREAT = resource.get("smproxy/string-length-great");
/* 149 */       STRING_NULL = resource.get("smproxy/string-null");
/* 150 */       VALUE_ERROR = resource.get("smproxy/value-error");
/* 151 */       FEE_USERTYPE_ERROR = resource.get("smproxy/fee-usertype-error");
/* 152 */       REGISTERED_DELIVERY_ERROR = resource.get("smproxy/registered-delivery-erro");
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\Documents\work\UMS3_develop\program\code\source\UMS3.0\server\lib\smproxy.jar
 * Qualified Name:     com.huawei.insa2.comm.sgip.SGIPConstant
 * JD-Core Version:    0.5.3
 */