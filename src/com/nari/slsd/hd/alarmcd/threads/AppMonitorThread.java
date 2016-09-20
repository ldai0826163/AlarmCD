/*   1:    */ package com.nari.slsd.hd.alarmcd.threads;
/*   2:    */ 
/*   3:    */ import com.nari.slsd.hd.alarmcd.UserLog;
/*   4:    */ import com.nari.slsd.hd.alarmcd.policies.AgentPolicyDefine;
/*   5:    */ import com.nari.slsd.hd.alarmcd.policies.PolicyArg;
/*   6:    */ import com.nari.slsd.hd.alarmcd.policies.SqlPolicyDefine;
/*   7:    */ import com.nari.slsd.hd.alarmcd.type.SharedData;
/*   8:    */ import com.nari.slsd.hd.alarmcd.wsclient.AnyTypeArray;
/*   9:    */ import com.nari.slsd.hd.alarmcd.wsclient.BinFile;
/*  10:    */ import com.nari.slsd.hd.alarmcd.wsclient.FileInfo;
/*  11:    */ import com.nari.slsd.hd.alarmcd.wsclient.MonitorInfo;
/*  12:    */ import com.nari.slsd.hd.alarmcd.wsclient.MonitorInfoService;
/*  13:    */ import com.nari.slsd.hd.service.IPubDataService;
///*  14:    */ import com.sun.xml.internal.ws.developer.StreamingAttachmentFeature;
/*  15:    */ import java.io.File;
/*  16:    */ import java.io.FileOutputStream;
/*  17:    */ import java.io.InputStream;
/*  18:    */ import java.util.ArrayList;
/*  19:    */ import java.util.Calendar;
/*  20:    */ import java.util.HashMap;
/*  21:    */ import java.util.List;
/*  22:    */ import java.util.Map;
/*  23:    */ import java.util.concurrent.locks.ReentrantLock;
/*  24:    */ import javax.activation.DataHandler;
/*  25:    */ import javax.activation.FileDataSource;
/*  26:    */ import javax.xml.namespace.QName;
/*  27:    */ import javax.xml.ws.BindingProvider;
/*  28:    */ import javax.xml.ws.WebServiceFeature;
/*  29:    */ import javax.xml.ws.soap.MTOMFeature;
/*  30:    */ 
/*  31:    */ public class AppMonitorThread
/*  32:    */   extends Thread
/*  33:    */ {
/*  34: 37 */   static UserLog log = new UserLog();
/*  35:    */   IPubDataService pubService;
/*  36: 39 */   public static ReentrantLock sDatalock = new ReentrantLock();
/*  37: 40 */   public static ReentrantLock cDatalock = new ReentrantLock();
/*  38: 42 */   boolean threadDone = false;
/*  39:    */   
/*  40:    */   public void done()
/*  41:    */   {
/*  42: 44 */     this.threadDone = true;
/*  43:    */   }
/*  44:    */   
/*  45:    */   public AppMonitorThread(IPubDataService pds)
/*  46:    */   {
/*  47: 47 */     this.pubService = pds;
/*  48:    */   }
/*  49:    */   
///*  50:    */   private void uploadFile(ArrayList<PolicyArg> args)
///*  51:    */     throws Exception
///*  52:    */   {
///*  53: 51 */     String RemoteFile = PolicyArg.getValue("RemoteFile", args);
///*  54: 52 */     String LocalName = PolicyArg.getValue("localFile", args);
///*  55: 53 */     String url = PolicyArg.getValue("url", args);
///*  56: 54 */     MonitorInfoService wsService = new MonitorInfoService(
///*  57: 55 */       MonitorInfoService.getUrl(url), new QName("http://monitor", 
///*  58: 56 */       "MonitorInfoService"));
///*  59: 57 */     MonitorInfo mPort = wsService.getMonitorInfoPort(new WebServiceFeature[] { new MTOMFeature(true, 
///*  60: 58 */       2048), new StreamingAttachmentFeature(null,true,40000000l) });
///*  61: 59 */     Map<String, Object> ctxt = ((BindingProvider)mPort)
///*  62: 60 */       .getRequestContext();
///*  63: 61 */     ctxt.put("com.sun.xml.internal.ws.transport.http.client.streaming.chunk.size", Integer.valueOf(8192));
///*  64: 62 */     BinFile bf = new BinFile();
///*  65: 63 */     bf.setTitle(RemoteFile);
///*  66: 64 */     DataHandler value = new DataHandler(new FileDataSource(new File(
///*  67: 65 */       LocalName)));
///*  68: 66 */     bf.setData(value);
///*  69: 67 */     mPort.upLoadFile(bf);
///*  70:    */   }
///*  71:    */   
///*  72:    */   private void downloadFile(ArrayList<PolicyArg> args)
///*  73:    */     throws Exception
///*  74:    */   {
///*  75: 72 */     String RemoteFile = PolicyArg.getValue("RemoteFile", args);
///*  76: 73 */     String LocalName = PolicyArg.getValue("localFile", args);
///*  77: 74 */     String url = PolicyArg.getValue("url", args);
///*  78: 75 */     MonitorInfoService wsService = new MonitorInfoService(
///*  79: 76 */       MonitorInfoService.getUrl(url), new QName("http://monitor", 
///*  80: 77 */       "MonitorInfoService"));
///*  81:    */     try
///*  82:    */     {
///*  83: 80 */       MonitorInfo mPort = wsService.getMonitorInfoPort(new WebServiceFeature[] { new MTOMFeature(
///*  84: 81 */         true, 2048), new StreamingAttachmentFeature(null, true, 
///*  85: 82 */         4000000L) });
///*  86: 83 */       Map<String, Object> ctxt = ((BindingProvider)mPort)
///*  87: 84 */         .getRequestContext();
///*  88: 85 */       ctxt.put("com.sun.xml.internal.ws.transport.http.client.streaming.chunk.size", Integer.valueOf(8192));
///*  89:    */       
///*  90: 87 */       BinFile bf = mPort.downFile(RemoteFile);
///*  91: 88 */       InputStream is = bf.getData().getInputStream();
///*  92:    */       
///*  93: 90 */       FileOutputStream fs = new FileOutputStream(new File(LocalName));
///*  94: 91 */       byte[] buff = new byte[2000];
///*  95:    */       int count;
///*  96: 92 */       while ((count = is.read(buff)) >= 0)
///*  97:    */       {
///*  98:    */         
///*  99: 93 */         fs.write(buff, 0, count);
///* 100:    */       }
///* 101: 95 */       fs.close();
///* 102:    */     }
///* 103:    */     catch (Exception e)
///* 104:    */     {
///* 105: 98 */       e.printStackTrace();
///* 106:    */     }
///* 107:    */   }
/* 108:    */   
///* 109:    */   public void run()
///* 110:    */   {
///* 111:103 */     while (!this.threadDone)
///* 112:    */     {
///* 113:104 */       for (int i = 0; i < SharedData.policysArrayList.size(); i++)
///* 114:    */       {
///* 115:107 */         SqlPolicyDefine sone = (SqlPolicyDefine)SharedData.policysArrayList.get(i);
///* 116:108 */         dealSqlPolicy(sone);
///* 117:    */         
///* 118:    */ 
///* 119:    */ 
///* 120:112 */         AgentPolicyDefine apone = (AgentPolicyDefine)SharedData.policysArrayList.get(i);
///* 121:113 */         if (apone.method.equalsIgnoreCase("UploadFile")) {
///* 122:    */           try
///* 123:    */           {
///* 124:115 */             uploadFile(apone.args);
///* 125:    */           }
///* 126:    */           catch (Exception e)
///* 127:    */           {
///* 128:118 */             e.printStackTrace();
///* 129:    */           }
///* 130:121 */         } else if (apone.method.equalsIgnoreCase("DownloadFile")) {
///* 131:    */           try
///* 132:    */           {
///* 133:123 */             downloadFile(apone.args);
///* 134:    */           }
///* 135:    */           catch (Exception e)
///* 136:    */           {
///* 137:126 */             e.printStackTrace();
///* 138:    */           }
///* 139:128 */         } else if (apone.method.equalsIgnoreCase("DirectoryInfo")) {
///* 140:    */           try
///* 141:    */           {
///* 142:130 */             getDirectoryInfo(apone.args);
///* 143:    */           }
///* 144:    */           catch (Exception e)
///* 145:    */           {
///* 146:133 */             e.printStackTrace();
///* 147:    */           }
///* 148:    */         }
///* 149:    */       }
///* 150:    */       try
///* 151:    */       {
///* 152:140 */         sleep(300000L);
///* 153:    */       }
///* 154:    */       catch (InterruptedException e)
///* 155:    */       {
///* 156:143 */         e.printStackTrace();
///* 157:    */       }
///* 158:    */     }
///* 159:    */   }
/* 160:    */   
/* 161:    */   private void dealSqlPolicy(SqlPolicyDefine sone)
/* 162:    */   {
/* 163:149 */     switch (sone.timeType)
/* 164:    */     {
/* 165:    */     }
/* 166:153 */     Calendar tB = null;
/* 167:154 */     HashMap<String, Object> inmapLocal = new HashMap();
/* 168:155 */     inmapLocal.put("EQ_BTIME", tB);
/* 169:156 */     inmapLocal.put("EX_SQL", sone.sql);
/* 170:    */   }
/* 171:    */   
/* 172:    */   private void getDirectoryInfo(ArrayList<PolicyArg> args)
/* 173:    */     throws Exception
/* 174:    */   {
/* 175:160 */     String url = PolicyArg.getValue("url", args);
/* 176:161 */     String key = PolicyArg.getValue("key", args);
/* 177:162 */     int type = 0;
/* 178:163 */     long value = Long.parseLong(PolicyArg.getValue("value", args));
/* 179:164 */     if (PolicyArg.getValue("type", args).equals("num"))
/* 180:    */     {
/* 181:165 */       type = 0;
/* 182:    */     }
/* 183:166 */     else if (PolicyArg.getValue("type", args).equals("size"))
/* 184:    */     {
/* 185:167 */       type = 1;
/* 186:    */     }
/* 187:    */     else
/* 188:    */     {
/* 189:170 */       type = 2;
/* 190:171 */       value = System.currentTimeMillis() - value * 60000L;
/* 191:    */     }
/* 192:174 */     MonitorInfoService wsService = new MonitorInfoService(
/* 193:175 */       MonitorInfoService.getUrl(url), new QName("http://monitor", 
/* 194:176 */       "MonitorInfoService"));
/* 195:177 */     AnyTypeArray x = wsService.getMonitorInfoPort().getDirectoryInfo(key, type, value);
/* 196:179 */     if (type != 0) {
/* 197:    */       try
/* 198:    */       {
/* 199:182 */         int y = Integer.parseInt((String)x.getItem().get(0));
/* 200:183 */         for (int i = 0; i < y; i++) {
/* 201:185 */           FileInfo localFileInfo = (FileInfo)x.getItem().get(i + 1);
/* 202:    */         }
/* 203:    */       }
/* 204:    */       catch (Exception e)
/* 205:    */       {
/* 206:189 */         e.printStackTrace();
/* 207:    */       }
/* 208:    */     }
/* 209:    */   }
/* 210:    */ }


/* Location:           C:\Users\lauren\Desktop\HD.AlarmCD.jar
 * Qualified Name:     com.nari.slsd.hd.alarmcd.threads.AppMonitorThread
 * JD-Core Version:    0.7.0.1
 */