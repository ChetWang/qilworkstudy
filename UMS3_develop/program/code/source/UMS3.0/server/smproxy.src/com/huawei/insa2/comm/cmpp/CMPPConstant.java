/*     */ package com.huawei.insa2.comm.cmpp;
/*     */ 
/*     */ import com.huawei.insa2.util.Resource;
/*     */ 
/*     */ public class CMPPConstant
/*     */ {
/*     */   public static boolean debug;
/*     */   public static String LOGINING;
/*     */   public static String LOGIN_ERROR;
/*     */   public static String SEND_ERROR;
/*     */   public static String CONNECT_TIMEOUT;
/*     */   public static String STRUCTURE_ERROR;
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
/*     */   public static String ACTIVE_REPINPUT_ERROR;
/*     */   public static String SMC_MESSAGE_ERROR;
/*     */   public static String INT_SCOPE_ERROR;
/*     */   public static String STRING_LENGTH_GREAT;
/*     */   public static String STRING_NULL;
/*     */   public static String VALUE_ERROR;
/*     */   public static String FEE_USERTYPE_ERROR;
/*     */   public static String REGISTERED_DELIVERY_ERROR;
/*     */   public static String PK_TOTAL_ERROR;
/*     */   public static String PK_NUMBER_ERROR;
/*  93 */   public static final int Connect_Command_Id = 1;
/*     */ 
/*  95 */   public static final int Connect_Rep_Command_Id = -2147483647;
/*     */ 
/*  97 */   public static final int Terminate_Command_Id = 2;
/*     */ 
/*  99 */   public static final int Terminate_Rep_Command_Id = -2147483646;
/*     */ 
/* 101 */   public static final int Submit_Command_Id = 4;
/*     */ 
/* 103 */   public static final int Submit_Rep_Command_Id = -2147483644;
/*     */ 
/* 105 */   public static final int Deliver_Command_Id = 5;
/*     */ 
/* 107 */   public static final int Deliver_Rep_Command_Id = -2147483643;
/*     */ 
/* 109 */   public static final int Query_Command_Id = 6;
/*     */ 
/* 111 */   public static final int Query_Rep_Command_Id = -2147483642;
/*     */ 
/* 113 */   public static final int Cancel_Command_Id = 7;
/*     */ 
/* 115 */   public static final int Cancel_Rep_Command_Id = -2147483641;
/*     */ 
/* 117 */   public static final int Active_Test_Command_Id = 8;
/*     */ 
/* 119 */   public static final int Active_Test_Rep_Command_Id = -2147483640;
/*     */ 
/*     */   public static void initConstant(Resource resource)
/*     */   {
/* 128 */     if (LOGINING == null) {
/* 129 */       LOGINING = resource.get("smproxy/logining");
/* 130 */       LOGIN_ERROR = resource.get("smproxy/login-error");
/* 131 */       SEND_ERROR = resource.get("smproxy/send-error");
/* 132 */       CONNECT_TIMEOUT = resource.get("smproxy/connect-timeout");
/* 133 */       STRUCTURE_ERROR = resource.get("smproxy/structure-error");
/* 134 */       NONLICETSP_ID = resource.get("smproxy/nonlicetsp-id");
/* 135 */       SP_ERROR = resource.get("smproxy/sp-error");
/* 136 */       VERSION_ERROR = resource.get("smproxy/version-error");
/* 137 */       OTHER_ERROR = resource.get("smproxy/other-error");
/* 138 */       HEARTBEAT_ABNORMITY = resource.get("smproxy/heartbeat-abnormity");
/* 139 */       SUBMIT_INPUT_ERROR = resource.get("smproxy/submit-input-error");
/* 140 */       CONNECT_INPUT_ERROR = resource.get("smproxy/connect-input-error");
/* 141 */       CANCEL_INPUT_ERROR = resource.get("smproxy/cancel-input-error");
/* 142 */       QUERY_INPUT_ERROR = resource.get("smproxy/query-input-error");
/* 143 */       DELIVER_REPINPUT_ERROR = resource.get("smproxy/deliver-repinput-error");
/* 144 */       ACTIVE_REPINPUT_ERROR = resource.get("smproxy/active-repinput-error");
/* 145 */       SMC_MESSAGE_ERROR = resource.get("smproxy/smc-message-error");
/* 146 */       INT_SCOPE_ERROR = resource.get("smproxy/int-scope-error");
/* 147 */       STRING_LENGTH_GREAT = resource.get("smproxy/string-length-great");
/* 148 */       STRING_NULL = resource.get("smproxy/string-null");
/* 149 */       VALUE_ERROR = resource.get("smproxy/value-error");
/* 150 */       FEE_USERTYPE_ERROR = resource.get("smproxy/fee-usertype-error");
/* 151 */       REGISTERED_DELIVERY_ERROR = resource.get("smproxy/registered-delivery-erro");
/* 152 */       PK_TOTAL_ERROR = resource.get("smproxy/pk-total-error");
/* 153 */       PK_NUMBER_ERROR = resource.get("smproxy/pk-number-error");
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\Documents\work\UMS3_develop\program\code\source\UMS3.0\server\lib\smproxy.jar
 * Qualified Name:     com.huawei.insa2.comm.cmpp.CMPPConstant
 * JD-Core Version:    0.5.3
 */