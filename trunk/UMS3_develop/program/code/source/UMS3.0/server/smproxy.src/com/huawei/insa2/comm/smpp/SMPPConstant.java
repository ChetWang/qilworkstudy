/*     */ package com.huawei.insa2.comm.smpp;
/*     */ 
/*     */ import com.huawei.insa2.util.Resource;
/*     */ 
/*     */ public class SMPPConstant
/*     */ {
/*     */   public static boolean debug;
/*     */   public static String LOGINING;
/*     */   public static String LOGIN_ERROR;
/*     */   public static String SEND_ERROR;
/*     */   public static String CONNECT_TIMEOUT;
/*     */   public static String STRUCTURE_ERROR;
/*     */   public static String VERSION_ERROR;
/*     */   public static String OTHER_ERROR;
/*     */   public static String HEARTBEAT_ABNORMITY;
/*     */   public static String SUBMIT_INPUT_ERROR;
/*     */   public static String CONNECT_INPUT_ERROR;
/*     */   public static String DELIVER_REPINPUT_ERROR;
/*     */   public static String ACTIVE_REPINPUT_ERROR;
/*     */   public static String SMC_MESSAGE_ERROR;
/*     */   public static String STRING_LENGTH_GREAT;
/*     */   public static String STRING_LENGTH_NOTEQUAL;
/*     */   public static String STRING_NULL;
/*  63 */   public static final int Generic_Nack_Command_Id = 0;
/*     */ 
/*  65 */   public static final int Bind_Receiver_Command_Id = 1;
/*     */ 
/*  67 */   public static final int Bind_Receiver_Rep_Command_Id = -2147483647;
/*     */ 
/*  69 */   public static final int Bind_Transmitter_Command_Id = 2;
/*     */ 
/*  71 */   public static final int Bind_Transmitter_Rep_Command_Id = -2147483646;
/*     */ 
/*  73 */   public static final int Submit_Command_Id = 4;
/*     */ 
/*  75 */   public static final int Submit_Rep_Command_Id = -2147483644;
/*     */ 
/*  77 */   public static final int Deliver_Command_Id = 5;
/*     */ 
/*  79 */   public static final int Deliver_Rep_Command_Id = -2147483643;
/*     */ 
/*  81 */   public static final int Unbind_Command_Id = 6;
/*     */ 
/*  83 */   public static final int Unbind_Rep_Command_Id = -2147483642;
/*     */ 
/*  89 */   public static final int Enquire_Link_Command_Id = 21;
/*     */ 
/*  91 */   public static final int Enquire_Link_Rep_Command_Id = -2147483627;
/*     */ 
/*     */   public static void initConstant(Resource resource)
/*     */   {
/* 100 */     if (LOGINING == null) {
/* 101 */       LOGINING = resource.get("smproxy/logining");
/* 102 */       LOGIN_ERROR = resource.get("smproxy/login-error");
/* 103 */       SEND_ERROR = resource.get("smproxy/send-error");
/* 104 */       CONNECT_TIMEOUT = resource.get("smproxy/connect-timeout");
/* 105 */       STRUCTURE_ERROR = resource.get("smproxy/structure-error");
/* 106 */       VERSION_ERROR = resource.get("smproxy/version-error");
/* 107 */       OTHER_ERROR = resource.get("smproxy/other-error");
/* 108 */       HEARTBEAT_ABNORMITY = resource.get("smproxy/heartbeat-abnormity");
/* 109 */       SUBMIT_INPUT_ERROR = resource.get("smproxy/submit-input-error");
/* 110 */       CONNECT_INPUT_ERROR = resource.get("smproxy/connect-input-error");
/* 111 */       DELIVER_REPINPUT_ERROR = resource.get("smproxy/deliver-repinput-error");
/* 112 */       ACTIVE_REPINPUT_ERROR = resource.get("smproxy/active-repinput-error");
/* 113 */       SMC_MESSAGE_ERROR = resource.get("smproxy/smc-message-error");
/* 114 */       STRING_LENGTH_NOTEQUAL = resource.get("smproxy/string-length-notequal");
/* 115 */       STRING_LENGTH_GREAT = resource.get("smproxy/string-length-great");
/* 116 */       STRING_NULL = resource.get("smproxy/string-null");
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\Documents\work\UMS3_develop\program\code\source\UMS3.0\server\lib\smproxy.jar
 * Qualified Name:     com.huawei.insa2.comm.smpp.SMPPConstant
 * JD-Core Version:    0.5.3
 */