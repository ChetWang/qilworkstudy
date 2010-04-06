/*     */ package sgipdemo;
/*     */ 
/*     */ import com.huawei.insa2.comm.sgip.message.SGIPDeliverMessage;
/*     */ import com.huawei.insa2.comm.sgip.message.SGIPMessage;
/*     */ import com.huawei.insa2.comm.sgip.message.SGIPSubmitMessage;
/*     */ import com.huawei.insa2.comm.sgip.message.SGIPSubmitRepMessage;
/*     */ import com.huawei.insa2.util.Args;
/*     */ import com.huawei.insa2.util.Cfg;
/*     */ import com.huawei.smproxy.SGIPSMProxy;
/*     */ import java.awt.Color;
/*     */ import java.awt.Component;
/*     */ import java.awt.Container;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Font;
/*     */ import java.awt.Frame;
/*     */ import java.awt.GraphicsConfiguration;
/*     */ import java.awt.GraphicsDevice;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.Window;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ActionListener;
/*     */ import java.text.DateFormat;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.Date;
/*     */ import javax.swing.AbstractButton;
/*     */ import javax.swing.BorderFactory;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JFrame;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.JScrollPane;
/*     */ import javax.swing.JTextArea;
/*     */ import javax.swing.JTextField;
/*     */ import javax.swing.JToggleButton;
/*     */ import javax.swing.text.JTextComponent;
/*     */ 
/*     */ public class SGIPDemo extends JFrame
/*     */ {
/*     */   private static Args args;
/*  20 */   private MySGIPSMProxy smp = null;
/*  21 */   private boolean loginSmProxy = false;
/*     */ 
/*  24 */   int sendMsgSum = 0;
/*  25 */   int sendSuccessMsgSum = 0;
/*  26 */   int recvDeliverMsgSum = 0;
/*     */ 
/*  29 */   int mt_tlmsg = 0;
/*  30 */   int mt_tlusr = 0;
/*  31 */   int mt_scs = 0;
/*  32 */   int mt_wt = 0;
/*  33 */   int mt_fl = 0;
/*  34 */   int mo_scs = 0;
/*  35 */   int mo_wt = 0;
/*  36 */   int mo_fl = 0;
/*     */ 
/*  39 */   int cmppSubmitFrom = 0;
/*  40 */   int cmppSubmitTo = 0;
/*  41 */   int calledIndex = 0;
/*  42 */   String serviceId = null;
/*  43 */   String feeTerminalId = null;
/*  44 */   String msgSrc = null;
/*  45 */   Date valid_Time = null;
/*  46 */   Date at_Time = null;
/*  47 */   String srcTerminalId = null;
/*  48 */   String[] UserNumber = new String[1];
/*  49 */   byte[] msgContent = null;
/*  50 */   String destTerminalPhone = null;
/*     */ 
/*  52 */   SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss.sss ");
/*  53 */   JLabel jLabel2 = new JLabel();
/*  54 */   JTextField service_Id = new JTextField();
/*  55 */   JLabel jLabel3 = new JLabel();
/*  56 */   JTextField fee_Terminal_Id = new JTextField();
/*  57 */   JLabel jLabel4 = new JLabel();
/*  58 */   JTextField msg_src = new JTextField();
/*  59 */   JLabel jLabel5 = new JLabel();
/*  60 */   JTextField src_Terminal_Id = new JTextField();
/*  61 */   JLabel jLabel6 = new JLabel();
/*  62 */   JTextField dest_Terminal_Id = new JTextField();
/*  63 */   JLabel jLabel7 = new JLabel();
/*  64 */   JTextField msg_Content = new JTextField();
/*  65 */   JButton SendButton = new JButton();
/*  66 */   JLabel jLabel8 = new JLabel();
/*  67 */   JTextField ThreadNum = new JTextField();
/*  68 */   JLabel jLabel9 = new JLabel();
/*  69 */   JTextField threadRunInterval = new JTextField();
/*  70 */   JLabel jLabel10 = new JLabel();
/*  71 */   JTextField QueryDate = new JTextField();
/*  72 */   JLabel jLabel11 = new JLabel();
/*  73 */   JTextField QueryType = new JTextField();
/*  74 */   JLabel jLabel12 = new JLabel();
/*  75 */   JTextField QueryCode = new JTextField();
/*  76 */   JToggleButton loginbutton = new JToggleButton();
/*  77 */   JLabel jLabel13 = new JLabel();
/*  78 */   JTextField CancelMsgId = new JTextField();
/*  79 */   JButton logoutbutton = new JButton();
/*  80 */   JLabel jLabel14 = new JLabel();
/*  81 */   JLabel jLabel15 = new JLabel();
/*  82 */   JTextField SendMsgSum = new JTextField();
/*  83 */   JLabel jLabel16 = new JLabel();
/*  84 */   JTextField SuccessSendSum = new JTextField();
/*  85 */   JLabel jLabel17 = new JLabel();
/*  86 */   JTextField RecvMsgSum = new JTextField();
/*  87 */   JLabel jLabel21 = new JLabel();
/*  88 */   JTextField threadSleepInterval = new JTextField();
/*  89 */   JLabel jLabel22 = new JLabel();
/*  90 */   JTextField myProxyState = new JTextField();
/*  91 */   JLabel jLabel23 = new JLabel();
/*  92 */   JTextField loginName = new JTextField();
/*  93 */   JLabel jLabel24 = new JLabel();
/*  94 */   JTextField loginPass = new JTextField();
/*  95 */   JButton initbutton = new JButton();
/*  96 */   JButton stopbutton = new JButton();
/*  97 */   JScrollPane jScrollPane1 = new JScrollPane();
/*  98 */   JTextArea allRecvContent = new JTextArea();
/*  99 */   JLabel jLabel1 = new JLabel();
/* 100 */   JLabel jLabel18 = new JLabel();
/* 101 */   JTextField calledFrom = new JTextField();
/* 102 */   JTextField calledTo = new JTextField();
/*     */ 
/*     */   public SGIPDemo()
/*     */   {
/*     */     try
/*     */     {
/* 109 */       jbInit();
/* 110 */       super.setSize(new Dimension(620, 450));
/* 111 */       Rectangle r = java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()[0].getDefaultConfiguration().getBounds();
/*     */ 
/* 114 */       super.setLocation((int)(r.getWidth() - 600) / 2, (int)(r.getHeight() - 450) / 2);
/*     */     }
/*     */     catch (Exception e)
/*     */     {
/* 118 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void Task()
/*     */   {
/* 127 */     this.sendMsgSum += 1;
/*     */   }
/*     */ 
/*     */   public void StartSendThread(int threadNum, int timeLong, int sleepInterval)
/*     */   {
/* 143 */     if (this.smp != null)
/*     */       return;
/* 145 */     return;
/*     */   }
/*     */ 
/*     */   public static void main(String[] a)
/*     */     throws Exception
/*     */   {
/* 158 */     new SGIPDemo().show();
/* 159 */     args = new Cfg("app.xml", false).getArgs("SGIPConnect"); }
/*     */ 
/*     */   private void jbInit() throws Exception {
/* 162 */     super.getContentPane().setLayout(null);
/* 163 */     super.getContentPane().setBackground(new Color(204, 230, 210));
/* 164 */     super.setDefaultCloseOperation(3);
/* 165 */     super.setForeground(Color.black);
/* 166 */     super.setResizable(false);
/* 167 */     super.setTitle("短消息网关测试程序");
/* 168 */     this.jLabel2.setText("业务类型");
/* 169 */     this.jLabel2.setBounds(new Rectangle(22, 61, 57, 22));
/* 170 */     this.service_Id.setBorder(BorderFactory.createLineBorder(Color.black));
/* 171 */     this.service_Id.setToolTipText("");
/* 172 */     this.service_Id.setText("good news");
/* 173 */     this.service_Id.setBounds(new Rectangle(99, 61, 131, 22));
/* 174 */     this.jLabel3.setText("记费号码");
/* 175 */     this.jLabel3.setBounds(new Rectangle(242, 61, 57, 22));
/* 176 */     this.fee_Terminal_Id.setBorder(BorderFactory.createLineBorder(Color.black));
/* 177 */     this.fee_Terminal_Id.setText("8989899");
/* 178 */     this.fee_Terminal_Id.setBounds(new Rectangle(305, 61, 131, 22));
/* 179 */     this.jLabel4.setText("消息来源");
/* 180 */     this.jLabel4.setBounds(new Rectangle(450, 61, 58, 22));
/* 181 */     this.msg_src.setBorder(BorderFactory.createLineBorder(Color.black));
/* 182 */     this.msg_src.setText("huawei");
/* 183 */     this.msg_src.setBounds(new Rectangle(506, 61, 75, 22));
/* 184 */     this.jLabel5.setText("主叫地址");
/* 185 */     this.jLabel5.setBounds(new Rectangle(22, 94, 59, 22));
/* 186 */     this.src_Terminal_Id.setBorder(BorderFactory.createLineBorder(Color.black));
/* 187 */     this.src_Terminal_Id.setToolTipText("源终端MSISDN号码, 即此短消息的主叫地址");
/* 188 */     this.src_Terminal_Id.setText("86138");
/* 189 */     this.src_Terminal_Id.setBounds(new Rectangle(99, 94, 131, 22));
/* 190 */     this.jLabel6.setText("被叫地址");
/* 191 */     this.jLabel6.setBounds(new Rectangle(242, 94, 58, 22));
/* 192 */     this.dest_Terminal_Id.setBorder(BorderFactory.createLineBorder(Color.black));
/* 193 */     this.dest_Terminal_Id.setToolTipText("目的用户手机号码");
/* 194 */     this.dest_Terminal_Id.setBounds(new Rectangle(305, 94, 70, 24));
/* 195 */     this.jLabel7.setText("短消息内容");
/* 196 */     this.jLabel7.setBounds(new Rectangle(22, 124, 69, 22));
/* 197 */     this.msg_Content.setBorder(BorderFactory.createLineBorder(Color.black));
/* 198 */     this.msg_Content.setToolTipText("输入发送的短消息内容");
/* 199 */     this.msg_Content.setText("this is a test");
/* 200 */     this.msg_Content.setBounds(new Rectangle(99, 124, 483, 22));
/* 201 */     this.SendButton.setBorder(BorderFactory.createEtchedBorder());
/* 202 */     this.SendButton.setText("发送测试请求");
/* 203 */     this.SendButton.setBounds(new Rectangle(492, 161, 91, 22));
/* 204 */     this.SendButton.addActionListener(new ActionListener() {
/*     */       public void actionPerformed(ActionEvent e) {
/* 206 */         SGIPDemo.this.SendButton_actionPerformed(e);
/*     */       }
/*     */     });
/* 209 */     this.jLabel8.setText("启动的线程数");
/* 210 */     this.jLabel8.setBounds(new Rectangle(22, 161, 75, 22));
/* 211 */     this.ThreadNum.setBorder(BorderFactory.createLineBorder(Color.black));
/* 212 */     this.ThreadNum.setToolTipText("启动多个线程测试SmProxy提供的API");
/* 213 */     this.ThreadNum.setText("0");
/* 214 */     this.ThreadNum.setBounds(new Rectangle(99, 161, 76, 22));
/* 215 */     this.jLabel9.setText("执行时长");
/* 216 */     this.jLabel9.setBounds(new Rectangle(180, 161, 53, 22));
/* 217 */     this.threadRunInterval.setBorder(BorderFactory.createLineBorder(Color.black));
/* 218 */     this.threadRunInterval.setToolTipText("调用线程执行发送请求的时间长度");
/* 219 */     this.threadRunInterval.setText("0");
/* 220 */     this.threadRunInterval.setBounds(new Rectangle(237, 161, 76, 22));
/* 221 */     this.jLabel10.setText("查询日期");
/* 222 */     this.jLabel10.setBounds(new Rectangle(22, 190, 65, 22));
/* 223 */     this.QueryDate.setBorder(BorderFactory.createLineBorder(Color.black));
/* 224 */     this.QueryDate.setToolTipText("查询指定日期的短消息信息");
/* 225 */     this.QueryDate.setText("20011210");
/* 226 */     this.QueryDate.setBounds(new Rectangle(99, 190, 76, 22));
/* 227 */     this.jLabel11.setText("查询类别");
/* 228 */     this.jLabel11.setBounds(new Rectangle(180, 190, 57, 22));
/* 229 */     this.QueryType.setBorder(BorderFactory.createLineBorder(Color.black));
/* 230 */     this.QueryType.setToolTipText("0:查询总数,1:按照业务代码查询");
/* 231 */     this.QueryType.setText("0");
/* 232 */     this.QueryType.setBounds(new Rectangle(238, 190, 76, 22));
/* 233 */     this.jLabel12.setText("业务类型");
/* 234 */     this.jLabel12.setBounds(new Rectangle(320, 190, 54, 22));
/* 235 */     this.QueryCode.setBorder(BorderFactory.createLineBorder(Color.black));
/* 236 */     this.QueryCode.setToolTipText("");
/* 237 */     this.QueryCode.setText("good news");
/* 238 */     this.QueryCode.setBounds(new Rectangle(377, 190, 76, 22));
/* 239 */     this.loginbutton.setBorder(BorderFactory.createEtchedBorder());
/* 240 */     this.loginbutton.setText("登陆系统");
/* 241 */     this.loginbutton.setBounds(new Rectangle(492, 190, 91, 22));
/* 242 */     this.loginbutton.addActionListener(new ActionListener() {
/*     */       public void actionPerformed(ActionEvent e) {
/* 244 */         SGIPDemo.this.loginbutton_actionPerformed(e);
/*     */       }
/*     */     });
/* 247 */     this.jLabel13.setToolTipText("");
/* 248 */     this.jLabel13.setText("消息标识");
/* 249 */     this.jLabel13.setBounds(new Rectangle(22, 221, 64, 22));
/* 250 */     this.CancelMsgId.setBorder(BorderFactory.createLineBorder(Color.black));
/* 251 */     this.CancelMsgId.setToolTipText("发送短消息请求的时候指定的消息ID");
/* 252 */     this.CancelMsgId.setBounds(new Rectangle(99, 221, 131, 22));
/* 253 */     this.logoutbutton.setBorder(BorderFactory.createEtchedBorder());
/* 254 */     this.logoutbutton.setToolTipText("");
/* 255 */     this.logoutbutton.setText("退出登陆");
/* 256 */     this.logoutbutton.setBounds(new Rectangle(492, 221, 91, 22));
/* 257 */     this.logoutbutton.addActionListener(new ActionListener() {
/*     */       public void actionPerformed(ActionEvent e) {
/* 259 */         SGIPDemo.this.logoutbutton_actionPerformed(e);
/*     */       }
/*     */     });
/* 262 */     this.jLabel14.setFont(new Font("Dialog", 0, 14));
/* 263 */     this.jLabel14.setBorder(BorderFactory.createEtchedBorder());
/* 264 */     this.jLabel14.setText("统计数据");
/* 265 */     this.jLabel14.setBounds(new Rectangle(63, 262, 61, 22));
/* 266 */     this.jLabel15.setText("发送消息总数");
/* 267 */     this.jLabel15.setBounds(new Rectangle(24, 293, 81, 22));
/* 268 */     this.SendMsgSum.setBorder(BorderFactory.createEtchedBorder());
/* 269 */     this.SendMsgSum.setEditable(false);
/* 270 */     this.SendMsgSum.setBounds(new Rectangle(113, 293, 74, 22));
/* 271 */     this.jLabel16.setToolTipText("");
/* 272 */     this.jLabel16.setText("成功发送消息数");
/* 273 */     this.jLabel16.setBounds(new Rectangle(24, 320, 90, 22));
/* 274 */     this.SuccessSendSum.setBorder(BorderFactory.createEtchedBorder());
/* 275 */     this.SuccessSendSum.setEditable(false);
/* 276 */     this.SuccessSendSum.setBounds(new Rectangle(113, 320, 74, 22));
/* 277 */     this.jLabel17.setText("接收消息总数");
/* 278 */     this.jLabel17.setBounds(new Rectangle(24, 347, 85, 22));
/* 279 */     this.RecvMsgSum.setBorder(BorderFactory.createEtchedBorder());
/* 280 */     this.RecvMsgSum.setToolTipText("Smc下发的消息的总素");
/* 281 */     this.RecvMsgSum.setEditable(false);
/* 282 */     this.RecvMsgSum.setBounds(new Rectangle(113, 348, 74, 22));
/* 283 */     this.jLabel21.setToolTipText("");
/* 284 */     this.jLabel21.setText("睡眠时长");
/* 285 */     this.jLabel21.setBounds(new Rectangle(320, 161, 56, 22));
/* 286 */     this.threadSleepInterval.setBorder(BorderFactory.createLineBorder(Color.black));
/* 287 */     this.threadSleepInterval.setText("0");
/* 288 */     this.threadSleepInterval.setBounds(new Rectangle(377, 161, 76, 22));
/* 289 */     this.jLabel22.setBorder(BorderFactory.createEtchedBorder());
/* 290 */     this.jLabel22.setText("运行状态 ");
/* 291 */     this.jLabel22.setBounds(new Rectangle(26, 381, 58, 22));
/* 292 */     this.myProxyState.setBorder(BorderFactory.createEtchedBorder());
/* 293 */     this.myProxyState.setEditable(false);
/* 294 */     this.myProxyState.setBounds(new Rectangle(101, 382, 484, 22));
/* 295 */     this.jLabel23.setToolTipText("");
/* 296 */     this.jLabel23.setText("登录帐号");
/* 297 */     this.jLabel23.setBounds(new Rectangle(23, 21, 66, 22));
/* 298 */     this.loginName.setBorder(BorderFactory.createLineBorder(Color.black));
/* 299 */     this.loginName.setBounds(new Rectangle(100, 21, 131, 22));
/* 300 */     this.jLabel24.setText("登录密码");
/* 301 */     this.jLabel24.setBounds(new Rectangle(243, 21, 60, 22));
/* 302 */     this.loginPass.setBorder(BorderFactory.createLineBorder(Color.black));
/* 303 */     this.loginPass.setBounds(new Rectangle(306, 21, 131, 22));
/* 304 */     this.initbutton.setBorder(BorderFactory.createEtchedBorder());
/* 305 */     this.initbutton.setText("初始化");
/* 306 */     this.initbutton.setBounds(new Rectangle(452, 21, 70, 22));
/* 307 */     this.initbutton.addActionListener(new ActionListener() {
/*     */       public void actionPerformed(ActionEvent e) {
/* 309 */         SGIPDemo.this.initbutton_actionPerformed(e);
/*     */       }
/*     */     });
/* 312 */     this.stopbutton.setBorder(BorderFactory.createEtchedBorder());
/* 313 */     this.stopbutton.setText("关闭监听");
/* 314 */     this.stopbutton.setBounds(new Rectangle(532, 21, 70, 22));
/* 315 */     this.stopbutton.addActionListener(new ActionListener() {
/*     */       public void actionPerformed(ActionEvent e) {
/* 317 */         SGIPDemo.this.stopbutton_actionPerformed(e);
/*     */       }
/*     */     });
/* 320 */     this.jScrollPane1.setHorizontalScrollBarPolicy(31);
/* 321 */     this.jScrollPane1.setVerticalScrollBarPolicy(22);
/* 322 */     this.jScrollPane1.setBounds(new Rectangle(200, 253, 385, 122));
/* 323 */     this.allRecvContent.setLineWrap(true);
/* 324 */     this.jLabel1.setText("From");
/* 325 */     this.jLabel1.setBounds(new Rectangle(395, 95, 37, 20));
/* 326 */     this.jLabel18.setText("To");
/* 327 */     this.jLabel18.setBounds(new Rectangle(516, 94, 27, 19));
/* 328 */     this.calledFrom.setBorder(BorderFactory.createLineBorder(Color.black));
/* 329 */     this.calledFrom.setText("0");
/* 330 */     this.calledFrom.setBounds(new Rectangle(441, 93, 63, 22));
/* 331 */     this.calledTo.setBorder(BorderFactory.createLineBorder(Color.black));
/* 332 */     this.calledTo.setText("0");
/* 333 */     this.calledTo.setBounds(new Rectangle(547, 92, 62, 25));
/* 334 */     super.getContentPane().add(this.jLabel23, null);
/* 335 */     super.getContentPane().add(this.loginName, null);
/* 336 */     super.getContentPane().add(this.jLabel24, null);
/* 337 */     super.getContentPane().add(this.loginPass, null);
/* 338 */     super.getContentPane().add(this.service_Id, null);
/* 339 */     super.getContentPane().add(this.jLabel2, null);
/* 340 */     super.getContentPane().add(this.jLabel3, null);
/* 341 */     super.getContentPane().add(this.fee_Terminal_Id, null);
/* 342 */     super.getContentPane().add(this.msg_src, null);
/* 343 */     super.getContentPane().add(this.ThreadNum, null);
/* 344 */     super.getContentPane().add(this.jLabel8, null);
/* 345 */     super.getContentPane().add(this.jLabel9, null);
/* 346 */     super.getContentPane().add(this.threadSleepInterval, null);
/* 347 */     super.getContentPane().add(this.threadRunInterval, null);
/* 348 */     super.getContentPane().add(this.jLabel21, null);
/* 349 */     super.getContentPane().add(this.QueryDate, null);
/* 350 */     super.getContentPane().add(this.jLabel10, null);
/* 351 */     super.getContentPane().add(this.jLabel11, null);
/* 352 */     super.getContentPane().add(this.QueryCode, null);
/* 353 */     super.getContentPane().add(this.QueryType, null);
/* 354 */     super.getContentPane().add(this.jLabel12, null);
/* 355 */     super.getContentPane().add(this.CancelMsgId, null);
/* 356 */     super.getContentPane().add(this.logoutbutton, null);
/* 357 */     super.getContentPane().add(this.jLabel13, null);
/* 358 */     super.getContentPane().add(this.jLabel14, null);
/* 359 */     super.getContentPane().add(this.jScrollPane1, null);
/* 360 */     this.jScrollPane1.getViewport().add(this.allRecvContent, null);
/* 361 */     super.getContentPane().add(this.RecvMsgSum, null);
/* 362 */     super.getContentPane().add(this.jLabel15, null);
/* 363 */     super.getContentPane().add(this.jLabel16, null);
/* 364 */     super.getContentPane().add(this.SuccessSendSum, null);
/* 365 */     super.getContentPane().add(this.SendMsgSum, null);
/* 366 */     super.getContentPane().add(this.jLabel17, null);
/* 367 */     super.getContentPane().add(this.myProxyState, null);
/* 368 */     super.getContentPane().add(this.jLabel22, null);
/* 369 */     super.getContentPane().add(this.dest_Terminal_Id, null);
/* 370 */     super.getContentPane().add(this.src_Terminal_Id, null);
/* 371 */     super.getContentPane().add(this.jLabel6, null);
/* 372 */     super.getContentPane().add(this.jLabel5, null);
/* 373 */     super.getContentPane().add(this.msg_Content, null);
/* 374 */     super.getContentPane().add(this.jLabel7, null);
/* 375 */     super.getContentPane().add(this.SendButton, null);
/* 376 */     super.getContentPane().add(this.loginbutton, null);
/* 377 */     super.getContentPane().add(this.jLabel4, null);
/* 378 */     super.getContentPane().add(this.initbutton, null);
/* 379 */     super.getContentPane().add(this.stopbutton, null);
/* 380 */     super.getContentPane().add(this.jLabel1, null);
/* 381 */     super.getContentPane().add(this.calledFrom, null);
/* 382 */     super.getContentPane().add(this.jLabel18, null);
/* 383 */     super.getContentPane().add(this.calledTo, null);
/*     */   }
/*     */ 
/*     */   void SendButton_actionPerformed(ActionEvent e)
/*     */   {
/* 390 */     int threadNum = 0;
/* 391 */     int runInterval = 0;
/* 392 */     int sleepInterval = 0;
/*     */ 
/* 395 */     if (!(this.loginSmProxy))
/*     */     {
/* 397 */       return;
/*     */     }
/*     */ 
/*     */     try
/*     */     {
/* 402 */       threadNum = Integer.parseInt(this.ThreadNum.getText().trim());
/* 403 */       runInterval = Integer.parseInt(this.threadRunInterval.getText().trim());
/* 404 */       sleepInterval = Integer.parseInt(this.threadSleepInterval.getText().trim());
/* 405 */       this.cmppSubmitFrom = Integer.parseInt(this.calledFrom.getText().trim());
/* 406 */       this.cmppSubmitTo = Integer.parseInt(this.calledTo.getText().trim());
/* 407 */       this.calledIndex = this.cmppSubmitFrom;
/*     */ 
/* 409 */       this.msgContent = this.msg_Content.getText().trim().getBytes();
/*     */     }
/*     */     catch (Exception ex)
/*     */     {
/* 414 */       threadNum = 0;
/* 415 */       runInterval = 0;
/*     */     }
/*     */ 
/* 418 */     if ((threadNum > 0) && (runInterval > 0))
/*     */     {
/* 421 */       StartSendThread(threadNum, runInterval, sleepInterval);
/*     */     }
/*     */     else
/*     */     {
/*     */       try
/*     */       {
/* 427 */         this.sendMsgSum += 1;
/* 428 */         ProcessSubmitRep(this.smp.send(getSubmitMsg(0)));
/*     */       }
/*     */       catch (IllegalArgumentException localIllegalArgumentException)
/*     */       {
/*     */       }
/*     */       catch (Exception localIllegalArgumentException)
/*     */       {
/* 436 */         ex.printStackTrace();
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   void loginbutton_actionPerformed(ActionEvent e)
/*     */   {
/* 461 */     boolean result = this.smp.connect(this.loginName.getText(), this.loginPass.getText());
/*     */ 
/* 463 */     if (result) {
/* 464 */       showStateMsg("登陆成功");
/*     */     }
/*     */     else
/* 467 */       showStateMsg("登陆失败");
/*     */   }
/*     */ 
/*     */   void logoutbutton_actionPerformed(ActionEvent e)
/*     */   {
/* 476 */     if (this.smp == null)
/*     */       return;
/* 478 */     this.smp.close();
/*     */   }
/*     */ 
/*     */   void ExitButton_actionPerformed(ActionEvent e)
/*     */   {
/* 488 */     if (!(this.loginSmProxy))
/*     */     {
/* 490 */       showStateMsg("系统没有成功登录");
/* 491 */       return;
/*     */     }
/*     */ 
/* 495 */     if (this.smp == null)
/*     */       return;
/*     */     try
/*     */     {
/* 499 */       this.smp.close();
/*     */     }
/*     */     catch (Exception ex)
/*     */     {
/* 503 */       ex.printStackTrace();
/* 504 */       showStateMsg("与ISMG断连出现异常");
/*     */     }
/*     */   }
/*     */ 
/*     */   private SGIPSubmitMessage getSubmitMsg(int index)
/*     */   {
/* 527 */     if (this.calledIndex < this.cmppSubmitTo)
/*     */     {
/* 529 */       this.calledIndex += 1;
/*     */     }
/*     */     else
/*     */     {
/* 533 */       this.calledIndex = this.cmppSubmitFrom;
/*     */     }
/* 535 */     this.UserNumber[0] = "130123456";
/*     */     Object localObject;
/*     */     try {
/* 538 */       return new SGIPSubmitMessage("302", "000000000000000000000", this.UserNumber, "22222", "test", 1, "100", "100", 0, 0, 0, null, null, 3, 1, 1, 0, 0, this.msgContent.length, this.msgContent, "");
/*     */     }
/*     */     catch (IllegalArgumentException e)
/*     */     {
/* 564 */       showStateMsg("提交短消息请求的输入参数不合法");
/* 565 */       e.printStackTrace();
/* 566 */       return null;
/*     */     }
/*     */     catch (Exception e)
/*     */     {
/* 570 */       showStateMsg("提交短消息请求处理异常");
/* 571 */       e.printStackTrace();
/* 572 */       localObject = null; } return localObject;
/*     */   }
/*     */ 
/*     */   private void ProcessSubmitRep(SGIPMessage msg)
/*     */   {
/* 584 */     SGIPSubmitRepMessage repMsg = (SGIPSubmitRepMessage)msg;
/* 585 */     if ((repMsg == null) || (repMsg.getResult() != 0))
/*     */       return;
/* 587 */     this.sendSuccessMsgSum += 1;
/*     */   }
/*     */ 
/*     */   public void ProcessRecvDeliverMsg(SGIPMessage msg)
/*     */   {
/* 596 */     SGIPDeliverMessage deliverMsg = (SGIPDeliverMessage)msg;
/* 597 */     showStateMsg(deliverMsg.toString());
/*     */ 
/* 599 */     this.recvDeliverMsgSum += 1;
/*     */   }
/*     */ 
/*     */   public void Terminate()
/*     */   {
/* 608 */     showStateMsg("SMC下发终断消息");
/* 609 */     this.loginSmProxy = false;
/* 610 */     this.smp = null;
/*     */   }
/*     */ 
/*     */   private void showStateMsg(String str)
/*     */   {
/* 618 */     if ((str == null) || (str == ""))
/*     */     {
/* 620 */       return;
/*     */     }
/* 622 */     this.allRecvContent.insert(String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(this.sdf.format(new Date())))).append(str).append("\n"))), 0);
/*     */ 
/* 625 */     if (this.allRecvContent.getText().length() > 2048)
/*     */     {
/* 627 */       this.allRecvContent.setText(this.allRecvContent.getText().substring(0, 1024));
/*     */     }
/* 629 */     this.allRecvContent.setCaretPosition(0);
/*     */   }
/*     */ 
/*     */   private void showStatisticData()
/*     */   {
/* 636 */     this.SendMsgSum.setText(new Integer(this.sendMsgSum).toString());
/* 637 */     this.SuccessSendSum.setText(new Integer(this.sendSuccessMsgSum).toString());
/* 638 */     this.RecvMsgSum.setText(new Integer(this.recvDeliverMsgSum).toString());
/*     */   }
/*     */ 
/*     */   void initbutton_actionPerformed(ActionEvent e)
/*     */   {
/* 679 */     if (this.loginSmProxy)
/*     */     {
/* 681 */       showStateMsg("系统已经初始化");
/* 682 */       return;
/*     */     }
/*     */ 
/*     */     try
/*     */     {
/* 687 */       args.set("login-name", this.loginName.getText().trim());
/* 688 */       args.set("login-pass", this.loginPass.getText().trim());
/* 689 */       showStateMsg("系统正在初始化");
/* 690 */       this.smp = new MySGIPSMProxy(this, args);
/* 691 */       showStateMsg("系统初始化成功");
/* 692 */       this.loginSmProxy = true;
/* 693 */       new MoniterThread().start();
/*     */     }
/*     */     catch (Exception ex)
/*     */     {
/* 697 */       showStateMsg("系统初始化失败");
/* 698 */       this.myProxyState.setText(ex.getMessage());
/* 699 */       ex.printStackTrace();
/* 700 */       this.loginSmProxy = false;
/*     */     }
/*     */   }
/*     */ 
/*     */   void stopbutton_actionPerformed(ActionEvent e)
/*     */   {
/*     */     try
/*     */     {
/* 711 */       this.smp.stopService();
/*     */     }
/*     */     catch (Exception localException)
/*     */     {
/*     */     }
/*     */   }
/*     */ 
/*     */   class MoniterThread extends Thread
/*     */   {
/*     */     public MoniterThread()
/*     */     {
/* 647 */       super.setDaemon(true);
/*     */     }
/*     */ 
/*     */     public void run() {
/* 651 */       String connState = null;
/*     */       while (true)
/*     */         try {
/* 654 */           if (SGIPDemo.this.smp == null)
/*     */             return;
/* 656 */           connState = SGIPDemo.this.smp.getConnState();
/* 657 */           SGIPDemo.this.showStatisticData();
/* 658 */           if (connState == null) connState = "系统运行正常";
/* 659 */           SGIPDemo.this.myProxyState.setText(connState);
/*     */ 
/* 662 */           Thread.sleep(100L);
/*     */         }
/*     */         catch (Exception e)
/*     */         {
/* 667 */           SGIPDemo.this.showStateMsg("状态监控线程出现异常退出");
/* 668 */           e.printStackTrace();
/*     */         }
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\Documents\work\UMS3_develop\program\code\source\UMS3.0\server\lib\smproxy.jar
 * Qualified Name:     sgipdemo.SGIPDemo
 * JD-Core Version:    0.5.3
 */