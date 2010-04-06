/*    */ package demo;
/*    */ 
/*    */ import com.huawei.insa2.util.Cfg;
/*    */ import com.huawei.insa2.util.Resource;
/*    */ import java.io.IOException;
/*    */ 
/*    */ public class Env
/*    */ {
/*    */   static Cfg config;
/*    */   static Resource resource;
/*    */ 
/*    */   public static Cfg getConfig()
/*    */   {
/* 27 */     if (config == null) {
/*    */       try {
/* 29 */         config = new Cfg("app.xml");
/*    */       }
/*    */       catch (Exception ex) {
/* 32 */         ex.printStackTrace();
/*    */       }
/*    */     }
/* 35 */     return config;
/*    */   }
/*    */ 
/*    */   public static Resource getResource()
/*    */   {
/* 42 */     if (resource == null) {
/*    */       try {
/* 44 */         resource = new Resource("resource");
/*    */       } catch (IOException ex) {
/* 46 */         ex.printStackTrace();
/*    */       }
/*    */     }
/* 49 */     return resource;
/*    */   }
/*    */ }

/* Location:           D:\Documents\work\UMS3_develop\program\code\source\UMS3.0\server\lib\smproxy.jar
 * Qualified Name:     demo.Env
 * JD-Core Version:    0.5.3
 */