/*     */ package demo30;
/*     */ 
/*     */ import com.huawei.insa2.comm.cmpp.message.CMPPCancelMessage;
/*     */ import com.huawei.insa2.comm.cmpp.message.CMPPCancelRepMessage;
/*     */ import com.huawei.insa2.comm.cmpp.message.CMPPMessage;
/*     */ import com.huawei.insa2.comm.cmpp.message.CMPPQueryMessage;
/*     */ import com.huawei.insa2.comm.cmpp.message.CMPPQueryRepMessage;
/*     */ import com.huawei.insa2.comm.cmpp30.message.CMPP30DeliverMessage;
/*     */ import com.huawei.insa2.comm.cmpp30.message.CMPP30SubmitMessage;
/*     */ import com.huawei.insa2.comm.cmpp30.message.CMPP30SubmitRepMessage;
/*     */ import com.huawei.insa2.util.Args;
/*     */ import com.huawei.insa2.util.Cfg;
/*     */ import com.huawei.smproxy.SMProxy30;
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
/*     */ public class Demo30 extends JFrame
/*     */   implements DemoConst
/*     */ {
/*     */   private static Args args;
/*  22 */   private MySMProxy30 smp = null;
/*  23 */   private boolean loginSmProxy = false;
/*     */ 
/*  26 */   int sendMsgSum = 0;
/*  27 */   int sendSuccessMsgSum = 0;
/*  28 */   int recvDeliverMsgSum = 0;
/*     */ 
/*  31 */   int mt_tlmsg = 0;
/*  32 */   int mt_tlusr = 0;
/*  33 */   int mt_scs = 0;
/*  34 */   int mt_wt = 0;
/*  35 */   int mt_fl = 0;
/*  36 */   int mo_scs = 0;
/*  37 */   int mo_wt = 0;
/*  38 */   int mo_fl = 0;
/*     */ 
/*  41 */   int cmppSubmitFrom = 0;
/*  42 */   int cmppSubmitTo = 0;
/*  43 */   int calledIndex = 0;
/*  44 */   String serviceId = null;
/*  45 */   String feeTerminalId = null;
/*  46 */   String msgSrc = null;
/*  47 */   java.util.Date valid_Time = null;
/*  48 */   java.util.Date at_Time = null;
/*  49 */   String srcTerminalId = null;
/*  50 */   String[] destTerminalId = new String[1];
/*  51 */   byte[] msgContent = null;
/*  52 */   String destTerminalPhone = null;
/*     */ 
/*  54 */   SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss.sss ");
/*  55 */   JLabel jLabel2 = new JLabel();
/*  56 */   JTextField service_Id = new JTextField();
/*  57 */   JLabel jLabel3 = new JLabel();
/*  58 */   JTextField fee_Terminal_Id = new JTextField();
/*  59 */   JLabel jLabel4 = new JLabel();
/*  60 */   JTextField msg_src = new JTextField();
/*  61 */   JLabel jLabel5 = new JLabel();
/*  62 */   JTextField src_Terminal_Id = new JTextField();
/*  63 */   JLabel jLabel6 = new JLabel();
/*  64 */   JTextField dest_Terminal_Id = new JTextField();
/*  65 */   JLabel jLabel7 = new JLabel();
/*  66 */   JTextField msg_Content = new JTextField();
/*  67 */   JButton SendButton = new JButton();
/*  68 */   JLabel jLabel8 = new JLabel();
/*  69 */   JTextField ThreadNum = new JTextField();
/*  70 */   JLabel jLabel9 = new JLabel();
/*  71 */   JTextField threadRunInterval = new JTextField();
/*  72 */   JLabel jLabel10 = new JLabel();
/*  73 */   JTextField QueryDate = new JTextField();
/*  74 */   JLabel jLabel11 = new JLabel();
/*  75 */   JTextField QueryType = new JTextField();
/*  76 */   JLabel jLabel12 = new JLabel();
/*  77 */   JTextField QueryCode = new JTextField();
/*  78 */   JToggleButton QueryButton = new JToggleButton();
/*  79 */   JLabel jLabel13 = new JLabel();
/*  80 */   JTextField CancelMsgId = new JTextField();
/*  81 */   JButton CancelButton = new JButton();
/*  82 */   JLabel jLabel14 = new JLabel();
/*  83 */   JLabel jLabel15 = new JLabel();
/*  84 */   JTextField SendMsgSum = new JTextField();
/*  85 */   JLabel jLabel16 = new JLabel();
/*  86 */   JTextField SuccessSendSum = new JTextField();
/*  87 */   JLabel jLabel17 = new JLabel();
/*  88 */   JTextField RecvMsgSum = new JTextField();
/*  89 */   JLabel jLabel21 = new JLabel();
/*  90 */   JTextField threadSleepInterval = new JTextField();
/*  91 */   JLabel jLabel22 = new JLabel();
/*  92 */   JTextField myProxyState = new JTextField();
/*  93 */   JLabel jLabel23 = new JLabel();
/*  94 */   JTextField loginName = new JTextField();
/*  95 */   JLabel jLabel24 = new JLabel();
/*  96 */   JTextField loginPass = new JTextField();
/*  97 */   JButton loginButton = new JButton();
/*  98 */   JScrollPane jScrollPane1 = new JScrollPane();
/*  99 */   JTextArea allRecvContent = new JTextArea();
/* 100 */   JLabel jLabel1 = new JLabel();
/* 101 */   JLabel jLabel18 = new JLabel();
/* 102 */   JTextField calledFrom = new JTextField();
/* 103 */   JTextField calledTo = new JTextField();
/*     */ 
/*     */   public Demo30()
/*     */   {
/*     */     try
/*     */     {
/* 110 */       jbInit();
/* 111 */       super.setSize(new Dimension(600, 450));
/* 112 */       Rectangle r = java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()[0].getDefaultConfiguration().getBounds();
/*     */ 
/* 115 */       super.setLocation((int)(r.getWidth() - 600) / 2, (int)(r.getHeight() - 450) / 2);
/*     */     }
/*     */     catch (Exception e)
/*     */     {
/* 119 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void Task()
/*     */   {
/* 128 */     this.sendMsgSum += 1;
/*     */     try
/*     */     {
/* 133 */       ProcessSubmitRep(this.smp.send(getSubmitMsg(this.calledIndex)));
/*     */     } catch (Exception ex) {
/* 135 */       ex.printStackTrace();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void StartSendThread(int threadNum, int timeLong, int sleepInterval)
/*     */   {
/* 144 */     if (this.smp == null)
/*     */     {
/* 146 */       return;
/*     */     }
/*     */ 
/* 149 */     for (int i = 0; i < threadNum; ++i)
/*     */     {
/* 151 */       new SendReqThread30("test", this, timeLong, sleepInterval).start();
/*     */     }
/*     */   }
/*     */ 
/*     */   public static void main(String[] a)
/*     */     throws Exception
/*     */   {
/* 159 */     new Demo30().show();
/* 160 */     args = new Cfg("app.xml", false).getArgs("CMPPConnect"); }
/*     */ 
/*     */   private void jbInit() throws Exception {
/* 163 */     super.getContentPane().setLayout(null);
/* 164 */     super.getContentPane().setBackground(new Color(204, 230, 210));
/* 165 */     super.setDefaultCloseOperation(3);
/* 166 */     super.setForeground(Color.black);
/* 167 */     super.setResizable(false);
/* 168 */     super.setTitle("短消息网关测试程序");
/* 169 */     this.jLabel2.setText("业务类型");
/* 170 */     this.jLabel2.setBounds(new Rectangle(22, 61, 57, 22));
/* 171 */     this.service_Id.setBorder(BorderFactory.createLineBorder(Color.black));
/* 172 */     this.service_Id.setToolTipText("");
/* 173 */     this.service_Id.setText("good news");
/* 174 */     this.service_Id.setBounds(new Rectangle(99, 61, 131, 22));
/* 175 */     this.jLabel3.setText("记费号码");
/* 176 */     this.jLabel3.setBounds(new Rectangle(242, 61, 57, 22));
/* 177 */     this.fee_Terminal_Id.setBorder(BorderFactory.createLineBorder(Color.black));
/* 178 */     this.fee_Terminal_Id.setText("8989899");
/* 179 */     this.fee_Terminal_Id.setBounds(new Rectangle(305, 61, 131, 22));
/* 180 */     this.jLabel4.setText("消息来源");
/* 181 */     this.jLabel4.setBounds(new Rectangle(450, 61, 58, 22));
/* 182 */     this.msg_src.setBorder(BorderFactory.createLineBorder(Color.black));
/* 183 */     this.msg_src.setText("huawei");
/* 184 */     this.msg_src.setBounds(new Rectangle(506, 61, 75, 22));
/* 185 */     this.jLabel5.setText("主叫地址");
/* 186 */     this.jLabel5.setBounds(new Rectangle(22, 94, 59, 22));
/* 187 */     this.src_Terminal_Id.setBorder(BorderFactory.createLineBorder(Color.black));
/* 188 */     this.src_Terminal_Id.setToolTipText("源终端MSISDN号码, 即此短消息的主叫地址");
/* 189 */     this.src_Terminal_Id.setText("86138");
/* 190 */     this.src_Terminal_Id.setBounds(new Rectangle(99, 94, 131, 22));
/* 191 */     this.jLabel6.setText("被叫地址");
/* 192 */     this.jLabel6.setBounds(new Rectangle(242, 94, 58, 22));
/* 193 */     this.dest_Terminal_Id.setBorder(BorderFactory.createLineBorder(Color.black));
/* 194 */     this.dest_Terminal_Id.setToolTipText("目的用户手机号码");
/* 195 */     this.dest_Terminal_Id.setBounds(new Rectangle(305, 94, 70, 24));
/* 196 */     this.jLabel7.setText("短消息内容");
/* 197 */     this.jLabel7.setBounds(new Rectangle(22, 124, 69, 22));
/* 198 */     this.msg_Content.setBorder(BorderFactory.createLineBorder(Color.black));
/* 199 */     this.msg_Content.setToolTipText("输入发送的短消息内容");
/* 200 */     this.msg_Content.setText("this is a test");
/* 201 */     this.msg_Content.setBounds(new Rectangle(99, 124, 483, 22));
/* 202 */     this.SendButton.setBorder(BorderFactory.createEtchedBorder());
/* 203 */     this.SendButton.setText("发送测试请求");
/* 204 */     this.SendButton.setBounds(new Rectangle(492, 161, 91, 22));
/* 205 */     this.SendButton.addActionListener(new ActionListener() {
/*     */       public void actionPerformed(ActionEvent e) {
/* 207 */         Demo30.this.SendButton_actionPerformed(e);
/*     */       }
/*     */     });
/* 210 */     this.jLabel8.setText("启动的线程数");
/* 211 */     this.jLabel8.setBounds(new Rectangle(22, 161, 75, 22));
/* 212 */     this.ThreadNum.setBorder(BorderFactory.createLineBorder(Color.black));
/* 213 */     this.ThreadNum.setToolTipText("启动多个线程测试SmProxy提供的API");
/* 214 */     this.ThreadNum.setText("0");
/* 215 */     this.ThreadNum.setBounds(new Rectangle(99, 161, 76, 22));
/* 216 */     this.jLabel9.setText("执行时长");
/* 217 */     this.jLabel9.setBounds(new Rectangle(180, 161, 53, 22));
/* 218 */     this.threadRunInterval.setBorder(BorderFactory.createLineBorder(Color.black));
/* 219 */     this.threadRunInterval.setToolTipText("调用线程执行发送请求的时间长度");
/* 220 */     this.threadRunInterval.setText("0");
/* 221 */     this.threadRunInterval.setBounds(new Rectangle(237, 161, 76, 22));
/* 222 */     this.jLabel10.setText("查询日期");
/* 223 */     this.jLabel10.setBounds(new Rectangle(22, 190, 65, 22));
/* 224 */     this.QueryDate.setBorder(BorderFactory.createLineBorder(Color.black));
/* 225 */     this.QueryDate.setToolTipText("查询指定日期的短消息信息");
/* 226 */     this.QueryDate.setText("20011210");
/* 227 */     this.QueryDate.setBounds(new Rectangle(99, 190, 76, 22));
/* 228 */     this.jLabel11.setText("查询类别");
/* 229 */     this.jLabel11.setBounds(new Rectangle(180, 190, 57, 22));
/* 230 */     this.QueryType.setBorder(BorderFactory.createLineBorder(Color.black));
/* 231 */     this.QueryType.setToolTipText("0:查询总数,1:按照业务代码查询");
/* 232 */     this.QueryType.setText("0");
/* 233 */     this.QueryType.setBounds(new Rectangle(238, 190, 76, 22));
/* 234 */     this.jLabel12.setText("业务类型");
/* 235 */     this.jLabel12.setBounds(new Rectangle(320, 190, 54, 22));
/* 236 */     this.QueryCode.setBorder(BorderFactory.createLineBorder(Color.black));
/* 237 */     this.QueryCode.setToolTipText("");
/* 238 */     this.QueryCode.setText("good news");
/* 239 */     this.QueryCode.setBounds(new Rectangle(377, 190, 76, 22));
/* 240 */     this.QueryButton.setBorder(BorderFactory.createEtchedBorder());
/* 241 */     this.QueryButton.setText("发送查询请求");
/* 242 */     this.QueryButton.setBounds(new Rectangle(492, 190, 91, 22));
/* 243 */     this.QueryButton.addActionListener(new ActionListener() {
/*     */       public void actionPerformed(ActionEvent e) {
/* 245 */         Demo30.this.QueryButton_actionPerformed(e);
/*     */       }
/*     */     });
/* 248 */     this.jLabel13.setToolTipText("");
/* 249 */     this.jLabel13.setText("消息标识");
/* 250 */     this.jLabel13.setBounds(new Rectangle(22, 221, 64, 22));
/* 251 */     this.CancelMsgId.setBorder(BorderFactory.createLineBorder(Color.black));
/* 252 */     this.CancelMsgId.setToolTipText("发送短消息请求的时候指定的消息ID");
/* 253 */     this.CancelMsgId.setBounds(new Rectangle(99, 221, 131, 22));
/* 254 */     this.CancelButton.setBorder(BorderFactory.createEtchedBorder());
/* 255 */     this.CancelButton.setToolTipText("");
/* 256 */     this.CancelButton.setText("发送取消请求");
/* 257 */     this.CancelButton.setBounds(new Rectangle(299, 221, 111, 22));
/* 258 */     this.CancelButton.addActionListener(new ActionListener() {
/*     */       public void actionPerformed(ActionEvent e) {
/* 260 */         Demo30.this.CancelButton_actionPerformed(e);
/*     */       }
/*     */     });
/* 263 */     this.jLabel14.setFont(new Font("Dialog", 0, 14));
/* 264 */     this.jLabel14.setBorder(BorderFactory.createEtchedBorder());
/* 265 */     this.jLabel14.setText("统计数据");
/* 266 */     this.jLabel14.setBounds(new Rectangle(63, 262, 61, 22));
/* 267 */     this.jLabel15.setText("发送消息总数");
/* 268 */     this.jLabel15.setBounds(new Rectangle(24, 293, 81, 22));
/* 269 */     this.SendMsgSum.setBorder(BorderFactory.createEtchedBorder());
/* 270 */     this.SendMsgSum.setEditable(false);
/* 271 */     this.SendMsgSum.setBounds(new Rectangle(113, 293, 74, 22));
/* 272 */     this.jLabel16.setToolTipText("");
/* 273 */     this.jLabel16.setText("成功发送消息数");
/* 274 */     this.jLabel16.setBounds(new Rectangle(24, 320, 90, 22));
/* 275 */     this.SuccessSendSum.setBorder(BorderFactory.createEtchedBorder());
/* 276 */     this.SuccessSendSum.setEditable(false);
/* 277 */     this.SuccessSendSum.setBounds(new Rectangle(113, 320, 74, 22));
/* 278 */     this.jLabel17.setText("接收消息总数");
/* 279 */     this.jLabel17.setBounds(new Rectangle(24, 347, 85, 22));
/* 280 */     this.RecvMsgSum.setBorder(BorderFactory.createEtchedBorder());
/* 281 */     this.RecvMsgSum.setToolTipText("Smc下发的消息的总素");
/* 282 */     this.RecvMsgSum.setEditable(false);
/* 283 */     this.RecvMsgSum.setBounds(new Rectangle(113, 348, 74, 22));
/* 284 */     this.jLabel21.setToolTipText("");
/* 285 */     this.jLabel21.setText("睡眠时长");
/* 286 */     this.jLabel21.setBounds(new Rectangle(320, 161, 56, 22));
/* 287 */     this.threadSleepInterval.setBorder(BorderFactory.createLineBorder(Color.black));
/* 288 */     this.threadSleepInterval.setText("0");
/* 289 */     this.threadSleepInterval.setBounds(new Rectangle(377, 161, 76, 22));
/* 290 */     this.jLabel22.setBorder(BorderFactory.createEtchedBorder());
/* 291 */     this.jLabel22.setText("运行状态 ");
/* 292 */     this.jLabel22.setBounds(new Rectangle(26, 381, 58, 22));
/* 293 */     this.myProxyState.setBorder(BorderFactory.createEtchedBorder());
/* 294 */     this.myProxyState.setEditable(false);
/* 295 */     this.myProxyState.setBounds(new Rectangle(101, 382, 484, 22));
/* 296 */     this.jLabel23.setToolTipText("");
/* 297 */     this.jLabel23.setText("登录帐号");
/* 298 */     this.jLabel23.setBounds(new Rectangle(23, 21, 66, 22));
/* 299 */     this.loginName.setBorder(BorderFactory.createLineBorder(Color.black));
/* 300 */     this.loginName.setText("d86138");
/* 301 */     this.loginName.setBounds(new Rectangle(100, 21, 131, 22));
/* 302 */     this.jLabel24.setText("登录密码");
/* 303 */     this.jLabel24.setBounds(new Rectangle(243, 21, 60, 22));
/* 304 */     this.loginPass.setBorder(BorderFactory.createLineBorder(Color.black));
/* 305 */     this.loginPass.setBounds(new Rectangle(306, 21, 131, 22));
/* 306 */     this.loginButton.setBorder(BorderFactory.createEtchedBorder());
/* 307 */     this.loginButton.setText("登录系统");
/* 308 */     this.loginButton.setBounds(new Rectangle(512, 21, 70, 22));
/* 309 */     this.loginButton.addActionListener(new ActionListener() {
/*     */       public void actionPerformed(ActionEvent e) {
/* 311 */         Demo30.this.loginButton_actionPerformed(e);
/*     */       }
/*     */     });
/* 314 */     this.jScrollPane1.setHorizontalScrollBarPolicy(31);
/* 315 */     this.jScrollPane1.setVerticalScrollBarPolicy(22);
/* 316 */     this.jScrollPane1.setBounds(new Rectangle(200, 253, 385, 122));
/* 317 */     this.allRecvContent.setLineWrap(true);
/* 318 */     this.jLabel1.setText("From");
/* 319 */     this.jLabel1.setBounds(new Rectangle(395, 95, 37, 20));
/* 320 */     this.jLabel18.setText("To");
/* 321 */     this.jLabel18.setBounds(new Rectangle(516, 94, 27, 19));
/* 322 */     this.calledFrom.setBorder(BorderFactory.createLineBorder(Color.black));
/* 323 */     this.calledFrom.setText("0");
/* 324 */     this.calledFrom.setBounds(new Rectangle(441, 93, 63, 22));
/* 325 */     this.calledTo.setBorder(BorderFactory.createLineBorder(Color.black));
/* 326 */     this.calledTo.setText("0");
/* 327 */     this.calledTo.setBounds(new Rectangle(547, 92, 62, 25));
/* 328 */     super.getContentPane().add(this.jLabel23, null);
/* 329 */     super.getContentPane().add(this.loginName, null);
/* 330 */     super.getContentPane().add(this.jLabel24, null);
/* 331 */     super.getContentPane().add(this.loginPass, null);
/* 332 */     super.getContentPane().add(this.service_Id, null);
/* 333 */     super.getContentPane().add(this.jLabel2, null);
/* 334 */     super.getContentPane().add(this.jLabel3, null);
/* 335 */     super.getContentPane().add(this.fee_Terminal_Id, null);
/* 336 */     super.getContentPane().add(this.msg_src, null);
/* 337 */     super.getContentPane().add(this.ThreadNum, null);
/* 338 */     super.getContentPane().add(this.jLabel8, null);
/* 339 */     super.getContentPane().add(this.jLabel9, null);
/* 340 */     super.getContentPane().add(this.threadSleepInterval, null);
/* 341 */     super.getContentPane().add(this.threadRunInterval, null);
/* 342 */     super.getContentPane().add(this.jLabel21, null);
/* 343 */     super.getContentPane().add(this.QueryDate, null);
/* 344 */     super.getContentPane().add(this.jLabel10, null);
/* 345 */     super.getContentPane().add(this.jLabel11, null);
/* 346 */     super.getContentPane().add(this.QueryCode, null);
/* 347 */     super.getContentPane().add(this.QueryType, null);
/* 348 */     super.getContentPane().add(this.jLabel12, null);
/* 349 */     super.getContentPane().add(this.CancelMsgId, null);
/* 350 */     super.getContentPane().add(this.CancelButton, null);
/* 351 */     super.getContentPane().add(this.jLabel13, null);
/* 352 */     super.getContentPane().add(this.jLabel14, null);
/* 353 */     super.getContentPane().add(this.jScrollPane1, null);
/* 354 */     this.jScrollPane1.getViewport().add(this.allRecvContent, null);
/* 355 */     super.getContentPane().add(this.RecvMsgSum, null);
/* 356 */     super.getContentPane().add(this.jLabel15, null);
/* 357 */     super.getContentPane().add(this.jLabel16, null);
/* 358 */     super.getContentPane().add(this.SuccessSendSum, null);
/* 359 */     super.getContentPane().add(this.SendMsgSum, null);
/* 360 */     super.getContentPane().add(this.jLabel17, null);
/* 361 */     super.getContentPane().add(this.myProxyState, null);
/* 362 */     super.getContentPane().add(this.jLabel22, null);
/* 363 */     super.getContentPane().add(this.dest_Terminal_Id, null);
/* 364 */     super.getContentPane().add(this.src_Terminal_Id, null);
/* 365 */     super.getContentPane().add(this.jLabel6, null);
/* 366 */     super.getContentPane().add(this.jLabel5, null);
/* 367 */     super.getContentPane().add(this.msg_Content, null);
/* 368 */     super.getContentPane().add(this.jLabel7, null);
/* 369 */     super.getContentPane().add(this.SendButton, null);
/* 370 */     super.getContentPane().add(this.QueryButton, null);
/* 371 */     super.getContentPane().add(this.jLabel4, null);
/* 372 */     super.getContentPane().add(this.loginButton, null);
/* 373 */     super.getContentPane().add(this.jLabel1, null);
/* 374 */     super.getContentPane().add(this.calledFrom, null);
/* 375 */     super.getContentPane().add(this.jLabel18, null);
/* 376 */     super.getContentPane().add(this.calledTo, null);
/*     */   }
/*     */ 
/*     */   void SendButton_actionPerformed(ActionEvent e)
/*     */   {
/* 383 */     int threadNum = 0;
/* 384 */     int runInterval = 0;
/* 385 */     int sleepInterval = 0;
/*     */ 
/* 388 */     if (!(this.loginSmProxy))
/*     */     {
/* 390 */       showStateMsg("系统没有成功登录");
/* 391 */       return;
/*     */     }
/*     */ 
/*     */     try
/*     */     {
/* 396 */       threadNum = Integer.parseInt(this.ThreadNum.getText().trim());
/* 397 */       runInterval = Integer.parseInt(this.threadRunInterval.getText().trim());
/* 398 */       sleepInterval = Integer.parseInt(this.threadSleepInterval.getText().trim());
/* 399 */       this.cmppSubmitFrom = Integer.parseInt(this.calledFrom.getText().trim());
/* 400 */       this.cmppSubmitTo = Integer.parseInt(this.calledTo.getText().trim());
/* 401 */       this.calledIndex = this.cmppSubmitFrom;
/*     */ 
/* 403 */       this.serviceId = this.service_Id.getText().trim();
/* 404 */       this.feeTerminalId = this.fee_Terminal_Id.getText().trim();
/* 405 */       this.msgSrc = this.msg_src.getText().trim();
/* 406 */       this.srcTerminalId = this.src_Terminal_Id.getText().trim();
/* 407 */       this.destTerminalPhone = this.dest_Terminal_Id.getText().trim();
/* 408 */       this.msgContent = this.msg_Content.getText().trim().getBytes();
/*     */     }
/*     */     catch (Exception ex)
/*     */     {
/* 413 */       threadNum = 0;
/* 414 */       runInterval = 0;
/*     */     }
/*     */ 
/* 417 */     if ((threadNum > 0) && (runInterval > 0))
/*     */     {
/* 420 */       StartSendThread(threadNum, runInterval, sleepInterval);
/*     */     }
/*     */     else
/*     */     {
/*     */       try
/*     */       {
/* 426 */         this.sendMsgSum += 1;
/* 427 */         ProcessSubmitRep(this.smp.send(getSubmitMsg(0)));
/*     */       }
/*     */       catch (IllegalArgumentException localIllegalArgumentException)
/*     */       {
/*     */       }
/*     */       catch (Exception localIllegalArgumentException)
/*     */       {
/* 435 */         ex.printStackTrace();
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   void QueryButton_actionPerformed(ActionEvent e)
/*     */   {
/* 447 */     if (!(this.loginSmProxy))
/*     */     {
/* 449 */       showStateMsg("系统没有成功登录");
/* 450 */       return;
/*     */     }
/*     */ 
/* 453 */     if (this.smp == null)
/*     */       return;
/*     */     try
/*     */     {
/* 457 */       ProcessQueryRep(this.smp.send(getQueryMsg()));
/*     */     }
/*     */     catch (IllegalArgumentException ex)
/*     */     {
/* 461 */       ex.printStackTrace();
/* 462 */       showStateMsg("Query短消息请求出现异常,可能是格式错误");
/*     */     }
/*     */     catch (Exception ex)
/*     */     {
/* 466 */       ex.printStackTrace();
/* 467 */       showStateMsg("Query短消息请求出现异常");
/*     */     }
/*     */   }
/*     */ 
/*     */   void CancelButton_actionPerformed(ActionEvent e)
/*     */   {
/* 479 */     if (!(this.loginSmProxy))
/*     */     {
/* 481 */       showStateMsg("系统没有成功登录");
/* 482 */       return;
/*     */     }
/*     */ 
/* 485 */     if (this.smp == null)
/*     */       return;
/*     */     try
/*     */     {
/* 489 */       ProcessCancelRep(this.smp.send(getCancelMsg()));
/*     */     }
/*     */     catch (IllegalArgumentException ex)
/*     */     {
/* 493 */       ex.printStackTrace();
/* 494 */       showStateMsg("取消短消息命令的输入参数不合法");
/*     */     }
/*     */     catch (Exception ex)
/*     */     {
/* 498 */       ex.printStackTrace();
/* 499 */       showStateMsg("取消短消息命令的处理出现异常");
/*     */     }
/*     */   }
/*     */ 
/*     */   void ExitButton_actionPerformed(ActionEvent e)
/*     */   {
/* 510 */     if (!(this.loginSmProxy))
/*     */     {
/* 512 */       showStateMsg("系统没有成功登录");
/* 513 */       return;
/*     */     }
/*     */ 
/* 517 */     if (this.smp == null)
/*     */       return;
/*     */     try
/*     */     {
/* 521 */       this.smp.close();
/*     */     }
/*     */     catch (Exception ex)
/*     */     {
/* 525 */       ex.printStackTrace();
/* 526 */       showStateMsg("与ISMG断连出现异常");
/*     */     }
/*     */   }
/*     */ 
/*     */   private CMPP30SubmitMessage getSubmitMsg(int index)
/*     */   {
/* 549 */     if (this.calledIndex < this.cmppSubmitTo)
/*     */     {
/* 551 */       this.calledIndex += 1;
/*     */     }
/*     */     else
/*     */     {
/* 555 */       this.calledIndex = this.cmppSubmitFrom;
/*     */     }
/* 557 */     this.destTerminalId[0] = String.valueOf(String.valueOf(this.destTerminalPhone)).concat(String.valueOf(String.valueOf(this.calledIndex)));
/*     */     Object localObject;
/*     */     try {
/* 560 */       return new CMPP30SubmitMessage(1, 1, 1, 88, this.serviceId, 0, this.feeTerminalId, 1, 0, 0, 15, this.msgSrc, "01", "999", this.valid_Time, this.at_Time, this.srcTerminalId, this.destTerminalId, 0, this.msgContent, "linkid=0123456789012");
/*     */     }
/*     */     catch (IllegalArgumentException e)
/*     */     {
/* 586 */       showStateMsg("提交短消息请求的输入参数不合法");
/* 587 */       e.printStackTrace();
/* 588 */       return null;
/*     */     }
/*     */     catch (Exception e)
/*     */     {
/* 592 */       showStateMsg("提交短消息请求处理异常");
/* 593 */       e.printStackTrace();
/* 594 */       localObject = null; } return localObject;
/*     */   }
/*     */ 
/*     */   private CMPPQueryMessage getQueryMsg()
/*     */   {
/* 604 */     String strDate = this.QueryDate.getText().trim();
/* 605 */     if (strDate.length() != 8)
/*     */     {
/* 607 */       showStateMsg("查询日期输入参数不合法");
/* 608 */       return null;
/*     */     }
/*     */     java.util.Date queryDate;
/*     */     try
/*     */     {
/* 613 */       SimpleDateFormat sdf = new SimpleDateFormat();
/*     */ 
/* 615 */       queryDate = java.sql.Date.valueOf(String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(strDate.substring(0, 4)))).append("-").append(strDate.substring(4, 6)).append("-").append(strDate.substring(6, 8)))));
/*     */ 
/* 617 */       int queryType = Integer.parseInt(this.QueryType.getText().trim());
/* 618 */       String queryCode = this.QueryCode.getText().trim();
/* 619 */       return new CMPPQueryMessage(queryDate, queryType, queryCode, "");
/*     */     }
/*     */     catch (IllegalArgumentException ex)
/*     */     {
/* 624 */       ex.printStackTrace();
/* 625 */       showStateMsg("查询参数输入参数不合法");
/* 626 */       return null;
/*     */     }
/*     */     catch (Exception e)
/*     */     {
/* 630 */       e.printStackTrace();
/* 631 */       queryDate = null; } return queryDate;
/*     */   }
/*     */ 
/*     */   private CMPPCancelMessage getCancelMsg()
/*     */   {
/*     */     CMPPCancelMessage localCMPPCancelMessage;
/*     */     try
/*     */     {
/* 643 */       byte[] msg_Id = this.CancelMsgId.getText().trim().getBytes();
/* 644 */       return new CMPPCancelMessage(msg_Id);
/*     */     }
/*     */     catch (IllegalArgumentException ex)
/*     */     {
/* 648 */       ex.printStackTrace();
/* 649 */       showStateMsg("取消短消息参数输入参数不合法");
/* 650 */       return null;
/*     */     }
/*     */     catch (Exception e)
/*     */     {
/* 654 */       e.printStackTrace();
/* 655 */       localCMPPCancelMessage = null; } return localCMPPCancelMessage;
/*     */   }
/*     */ 
/*     */   private void ProcessSubmitRep(CMPPMessage msg)
/*     */   {
/* 667 */     CMPP30SubmitRepMessage repMsg = (CMPP30SubmitRepMessage)msg;
/* 668 */     if ((repMsg == null) || (repMsg.getResult() != 0))
/*     */       return;
/* 670 */     this.sendSuccessMsgSum += 1;
/*     */   }
/*     */ 
/*     */   private void ProcessQueryRep(CMPPMessage msg)
/*     */   {
/* 681 */     CMPPQueryRepMessage queryRep = (CMPPQueryRepMessage)msg;
/*     */ 
/* 683 */     this.mt_tlmsg = queryRep.getMtTlmsg();
/* 684 */     this.mt_tlusr = queryRep.getMtTlusr();
/* 685 */     this.mt_scs = queryRep.getMtScs();
/* 686 */     this.mt_wt = queryRep.getMtWt();
/* 687 */     this.mt_fl = queryRep.getMtFl();
/* 688 */     this.mo_scs = queryRep.getMoScs();
/* 689 */     this.mo_wt = queryRep.getMoWt();
/* 690 */     this.mo_fl = queryRep.getMoFl();
/* 691 */     showStateMsg(String.valueOf(String.valueOf(new StringBuffer("mt_tlmsg:").append(this.mt_tlmsg).append(",mt_tlusr:").append(this.mt_tlusr).append(",mt_scs:").append(this.mt_scs).append(",mt_wt").append(this.mt_wt).append(",mt_fl:").append(this.mt_fl).append(",mo_scs:").append(this.mo_scs))));
/*     */   }
/*     */ 
/*     */   private boolean ProcessCancelRep(CMPPMessage msg)
/*     */   {
/* 703 */     CMPPCancelRepMessage cancelRep = (CMPPCancelRepMessage)msg;
/* 704 */     if (cancelRep.getSuccessId() == 0)
/*     */     {
/* 706 */       showStateMsg("取消操作成功");
/* 707 */       return true;
/*     */     }
/*     */ 
/* 711 */     showStateMsg("取消操作失败");
/* 712 */     return false;
/*     */   }
/*     */ 
/*     */   public void ProcessRecvDeliverMsg(CMPPMessage msg)
/*     */   {
/* 722 */     CMPP30DeliverMessage deliverMsg = (CMPP30DeliverMessage)msg;
/*     */ 
/* 724 */     if (deliverMsg.getRegisteredDeliver() == 0)
/*     */     {
/*     */       try
/*     */       {
/* 728 */         if (deliverMsg.getMsgFmt() == 8)
/*     */         {
/* 730 */           showStateMsg(String.valueOf(String.valueOf(new StringBuffer("接收消息: 主叫号码=").append(deliverMsg.getSrcterminalId()).append(";内容=").append(new String(deliverMsg.getMsgContent(), "UTF-16BE"))))); break label470:
/*     */         }
/*     */ 
/* 736 */         showStateMsg(String.valueOf(String.valueOf(new StringBuffer("接收消息: 主叫号码=").append(deliverMsg.getSrcterminalId()).append(";内容=").append(new String(deliverMsg.getMsgContent())).append(";destterm=").append(new String(deliverMsg.getDestnationId())).append(";serviceid=").append(new String(deliverMsg.getServiceId())).append(";tppid=").append(deliverMsg.getTpPid()).append(";tpudhi=").append(deliverMsg.getTpUdhi()).append(";msgfmt").append(deliverMsg.getMsgFmt()).append(";srctermid=").append(new String(deliverMsg.getSrcterminalId())).append(";deliver=").append(deliverMsg.getRegisteredDeliver()).append(";msgcontent=").append(new String(deliverMsg.getMsgContent())).append(";LinkID=").append(new String(deliverMsg.getLinkID())))));
/*     */       }
/*     */       catch (Exception e)
/*     */       {
/* 752 */         e.printStackTrace();
/*     */       }
/*     */ 
/*     */     }
/*     */     else
/*     */     {
/* 758 */       showStateMsg(String.valueOf(String.valueOf(new StringBuffer("收到状态报告消息： stat=").append(new String(deliverMsg.getStat())).append("dest_termID=").append(new String(deliverMsg.getDestTerminalId())).append(";destterm=").append(new String(deliverMsg.getDestnationId())).append(";serviceid=").append(new String(deliverMsg.getServiceId())).append(";tppid=").append(deliverMsg.getTpPid()).append(";tpudhi=").append(deliverMsg.getTpUdhi()).append(";msgfmt").append(deliverMsg.getMsgFmt()).append(";srctermid=").append(new String(deliverMsg.getSrcterminalId())).append(";deliver=").append(deliverMsg.getRegisteredDeliver()).append(";LinkID=").append(new String(deliverMsg.getLinkID())))));
/*     */     }
/*     */ 
/* 770 */     label470: this.recvDeliverMsgSum += 1;
/*     */   }
/*     */ 
/*     */   public void Terminate()
/*     */   {
/* 779 */     showStateMsg("SMC下发终断消息");
/* 780 */     this.loginSmProxy = false;
/* 781 */     this.smp = null;
/*     */   }
/*     */ 
/*     */   private void showStateMsg(String str)
/*     */   {
/* 789 */     if ((str == null) || (str == ""))
/*     */     {
/* 791 */       return;
/*     */     }
/* 793 */     this.allRecvContent.insert(String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(this.sdf.format(new java.util.Date())))).append(str).append("\n"))), 0);
/*     */ 
/* 796 */     if (this.allRecvContent.getText().length() > 2048)
/*     */     {
/* 798 */       this.allRecvContent.setText(this.allRecvContent.getText().substring(0, 1024));
/*     */     }
/* 800 */     this.allRecvContent.setCaretPosition(0);
/*     */   }
/*     */ 
/*     */   private void showStatisticData()
/*     */   {
/* 807 */     this.SendMsgSum.setText(new Integer(this.sendMsgSum).toString());
/* 808 */     this.SuccessSendSum.setText(new Integer(this.sendSuccessMsgSum).toString());
/* 809 */     this.RecvMsgSum.setText(new Integer(this.recvDeliverMsgSum).toString());
/*     */   }
/*     */ 
/*     */   void loginButton_actionPerformed(ActionEvent e)
/*     */   {
/* 850 */     if (this.loginSmProxy)
/*     */     {
/* 852 */       showStateMsg("系统已经初始化");
/* 853 */       return;
/*     */     }
/*     */ 
/*     */     try
/*     */     {
/* 858 */       args.set("source-addr", this.loginName.getText().trim());
/* 859 */       args.set("shared-secret", this.loginPass.getText().trim());
/* 860 */       showStateMsg("系统正在初始化");
/* 861 */       this.smp = new MySMProxy30(this, args);
/* 862 */       showStateMsg("系统初始化成功");
/* 863 */       this.loginSmProxy = true;
/* 864 */       new MoniterThread().start();
/*     */     }
/*     */     catch (Exception ex)
/*     */     {
/* 868 */       showStateMsg("系统初始化失败");
/* 869 */       this.myProxyState.setText(ex.getMessage());
/* 870 */       ex.printStackTrace();
/* 871 */       this.loginSmProxy = false;
/*     */     }
/*     */   }
/*     */ 
/*     */   class MoniterThread extends Thread
/*     */   {
/*     */     public MoniterThread()
/*     */     {
/* 818 */       super.setDaemon(true);
/*     */     }
/*     */ 
/*     */     public void run() {
/* 822 */       String connState = null;
/*     */       while (true)
/*     */       {
/*     */         try
/*     */         {
/* 827 */           connState = Demo30.this.smp.getConnState();
/* 828 */           Demo30.this.showStatisticData();
/* 829 */           if (connState == null) connState = "系统运行正常";
/* 830 */           Demo30.this.myProxyState.setText(connState);
/*     */ 
/* 833 */           Thread.sleep(100L);
/*     */         }
/*     */         catch (Exception e)
/*     */         {
/* 838 */           Demo30.this.showStateMsg("状态监控线程出现异常退出");
/* 839 */           e.printStackTrace();
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\Documents\work\UMS3_develop\program\code\source\UMS3.0\server\lib\smproxy.jar
 * Qualified Name:     demo30.Demo30
 * JD-Core Version:    0.5.3
 */