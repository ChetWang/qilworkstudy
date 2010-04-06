/*     */ package com.huawei.insa2.comm.smgp;
/*     */ 
/*     */ import com.huawei.insa2.util.Resource;
/*     */ 
/*     */ public class SMGPConstant
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
/*     */   public static String FORWARD_INPUT_ERROR;
/*     */   public static String MTROUTEUPDATE_INPUT_ERROR;
/*     */   public static String MOROUTEUPDATE_INPUT_ERROR;
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
/*     */   public static String NEEDREPORT_ERROR;
/*     */   public static String PRIORITY_ERROR;
/*     */   public static String DESTTERMID_ERROR;
/* 111 */   public static final int Login_Request_Id = 1;
/*     */ 
/* 113 */   public static final int Login_Resp_Request_Id = -2147483647;
/*     */ 
/* 115 */   public static final int Submit_Request_Id = 2;
/*     */ 
/* 117 */   public static final int Submit_Resp_Request_Id = -2147483646;
/*     */ 
/* 119 */   public static final int Deliver_Request_Id = 3;
/*     */ 
/* 121 */   public static final int Deliver_Resp_Request_Id = -2147483645;
/*     */ 
/* 123 */   public static final int Active_Test_Request_Id = 4;
/*     */ 
/* 125 */   public static final int Active_Test_Resp_Request_Id = -2147483644;
/*     */ 
/* 127 */   public static final int Forward_Request_Id = 5;
/*     */ 
/* 129 */   public static final int Forward_Resp_Request_Id = -2147483643;
/*     */ 
/* 131 */   public static final int Exit_Request_Id = 6;
/*     */ 
/* 133 */   public static final int Exit_Resp_Request_Id = -2147483642;
/*     */ 
/* 135 */   public static final int Query_Request_Id = 7;
/*     */ 
/* 137 */   public static final int Query_Resp_Request_Id = -2147483641;
/*     */ 
/* 139 */   public static final int MtRoute_Update_Request_Id = 8;
/*     */ 
/* 141 */   public static final int MtRoute_Update_Resp_Request_Id = -2147483640;
/*     */ 
/* 143 */   public static final int MoRoute_Update_Request_Id = 9;
/*     */ 
/* 145 */   public static final int MoRoute_Update_Resp_Request_Id = -2147483639;
/*     */ 
/*     */   public static void initConstant(Resource resource)
/*     */   {
/* 154 */     if (LOGINING == null) {
/* 155 */       LOGINING = resource.get("smproxy/logining");
/* 156 */       LOGIN_ERROR = resource.get("smproxy/login-error");
/* 157 */       SEND_ERROR = resource.get("smproxy/send-error");
/* 158 */       CONNECT_TIMEOUT = resource.get("smproxy/connect-timeout");
/* 159 */       STRUCTURE_ERROR = resource.get("smproxy/structure-error");
/* 160 */       NONLICETSP_ID = resource.get("smproxy/nonlicetsp-id");
/* 161 */       SP_ERROR = resource.get("smproxy/sp-error");
/* 162 */       VERSION_ERROR = resource.get("smproxy/version-error");
/* 163 */       OTHER_ERROR = resource.get("smproxy/other-error");
/* 164 */       HEARTBEAT_ABNORMITY = resource.get("smproxy/heartbeat-abnormity");
/* 165 */       SUBMIT_INPUT_ERROR = resource.get("smproxy/submit-input-error");
/* 166 */       CONNECT_INPUT_ERROR = resource.get("smproxy/connect-input-error");
/* 167 */       CANCEL_INPUT_ERROR = resource.get("smproxy/cancel-input-error");
/* 168 */       QUERY_INPUT_ERROR = resource.get("smproxy/query-input-error");
/* 169 */       DELIVER_REPINPUT_ERROR = resource.get("smproxy/deliver-repinput-error");
/* 170 */       ACTIVE_REPINPUT_ERROR = resource.get("smproxy/active-repinput-error");
/* 171 */       SMC_MESSAGE_ERROR = resource.get("smproxy/smc-message-error");
/* 172 */       INT_SCOPE_ERROR = resource.get("smproxy/int-scope-error");
/* 173 */       STRING_LENGTH_GREAT = resource.get("smproxy/string-length-great");
/* 174 */       STRING_NULL = resource.get("smproxy/string-null");
/* 175 */       VALUE_ERROR = resource.get("smproxy/value-error");
/* 176 */       FEE_USERTYPE_ERROR = resource.get("smproxy/fee-usertype-error");
/* 177 */       REGISTERED_DELIVERY_ERROR = resource.get("smproxy/registered-delivery-erro");
/* 178 */       PK_TOTAL_ERROR = resource.get("smproxy/pk-total-error");
/* 179 */       PK_NUMBER_ERROR = resource.get("smproxy/pk-number-error");
/* 180 */       FORWARD_INPUT_ERROR = resource.get("smproxy/forward_input_error");
/* 181 */       MTROUTEUPDATE_INPUT_ERROR = resource.get("smproxy/mtrouteupdate_input_error");
/* 182 */       MOROUTEUPDATE_INPUT_ERROR = resource.get("smproxy/morouteupdate_input_error");
/* 183 */       NEEDREPORT_ERROR = resource.get("smproxy/needreport_error");
/* 184 */       PRIORITY_ERROR = resource.get("smproxy/priority_error");
/* 185 */       DESTTERMID_ERROR = resource.get("smproxy/desttermid_error");
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\Documents\work\UMS3_develop\program\code\source\UMS3.0\server\lib\smproxy.jar
 * Qualified Name:     com.huawei.insa2.comm.smgp.SMGPConstant
 * JD-Core Version:    0.5.3
 */