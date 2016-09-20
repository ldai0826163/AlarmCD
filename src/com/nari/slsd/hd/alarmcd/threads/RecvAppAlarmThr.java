/*   1:    */ package com.nari.slsd.hd.alarmcd.threads;
/*   2:    */ 
/*   3:    */ import com.nari.slsd.hd.alarmcd.Main;
/*   4:    */ import com.nari.slsd.hd.alarmcd.UserLog;
/*   5:    */ import com.nari.slsd.hd.alarmcd.policies.AppAlarmPolicy;
/*   6:    */ import com.nari.slsd.hd.alarmcd.type.AppAlarmDefine;
/*   7:    */ import com.nari.slsd.hd.model.PubDefAlarm;
/*   8:    */ import com.nari.slsd.hd.service.IPubDataService;
/*   9:    */ import java.net.DatagramPacket;
/*  10:    */ import java.net.DatagramSocket;
/*  11:    */ import java.net.InetSocketAddress;
/*  12:    */ import java.net.SocketException;
/*  13:    */ import java.nio.ByteBuffer;
/*  14:    */ import java.nio.ByteOrder;
/*  15:    */ import java.text.SimpleDateFormat;
/*  16:    */ import java.util.ArrayList;
/*  17:    */ import java.util.Date;
/*  18:    */ import java.util.Iterator;
/*  19:    */ import java.util.LinkedList;
/*  20:    */ import java.util.List;
/*  21:    */ 
/*  22:    */ public class RecvAppAlarmThr
/*  23:    */   extends Thread
/*  24:    */ {
/*  25: 35 */   private static UserLog log = new UserLog();
/*  26:    */   private DatagramSocket socket;
/*  27: 37 */   LinkedList<String> fileNameList = new LinkedList();
/*  28:    */   Iterator<String> it;
/*  29:    */   int port;
/*  30: 40 */   ArrayList<AppAlarmInfo> appAlarmList = new ArrayList();
/*  31:    */   AppAlarmPolicy[] policies;
/*  32:    */   IPubDataService pubService;
/*  33:    */   
/*  34:    */   public RecvAppAlarmThr(int p, IPubDataService pds)
/*  35:    */   {
/*  36: 45 */     this.port = p;
/*  37: 46 */     this.pubService = pds;
/*  38: 47 */     getPolicyDefineFromDB();
/*  39:    */   }
/*  40:    */   
/*  41:    */   private void getPolicyDefineFromDB()
/*  42:    */   {
/*  43:    */     try
/*  44:    */     {
/*  45: 51 */       List<PubDefAlarm> alarms = this.pubService.getAllList(PubDefAlarm.class);
/*  46: 52 */       ArrayList<AppAlarmPolicy> pList = new ArrayList();
/*  47: 53 */       for (int i = 0; i < alarms.size(); i++) {
/*  48:    */         try
/*  49:    */         {
/*  50: 56 */           PubDefAlarm dbone = (PubDefAlarm)alarms.get(i);
/*  51: 58 */           if (dbone.getnOnOff() == 0) {
/*  52: 61 */             if ((dbone.getType().intValue() > 100) && (dbone.getType().intValue() < 201))
/*  53:    */             {
/*  54: 63 */               AppAlarmPolicy pone = new AppAlarmPolicy(dbone.getType());
/*  55: 64 */               pone.setId(dbone.getAlarmid());
/*  56: 65 */               pone.setSendid(dbone.getSendid());
/*  57: 66 */               pone.setName(dbone.getName());
/*  58: 67 */               pList.add(pone);
/*  59:    */             }
/*  60:    */           }
/*  61:    */         }
/*  62:    */         catch (Exception e)
/*  63:    */         {
/*  64: 70 */           Main.dealException(e);
/*  65:    */         }
/*  66:    */       }
/*  67: 75 */       this.policies = new AppAlarmPolicy[pList.size()];
/*  68: 76 */       for (int i = 0; i < this.policies.length; i++) {
/*  69: 77 */         this.policies[i] = ((AppAlarmPolicy)pList.get(i));
/*  70:    */       }
/*  71:    */     }
/*  72:    */     catch (Exception e)
/*  73:    */     {
/*  74: 80 */       Main.dealException(e);
/*  75:    */     }
/*  76:    */   }
/*  77:    */   
/*  78:    */   public void run()
/*  79:    */   {
/*  80:    */     try
/*  81:    */     {
/*  82: 86 */       sleep(3000L);
/*  83:    */     }
/*  84:    */     catch (InterruptedException e1)
/*  85:    */     {
/*  86: 89 */       e1.printStackTrace();
/*  87:    */     }
/*  88:    */     try
/*  89:    */     {
/*  90: 93 */       this.socket = new DatagramSocket(null);
/*  91: 94 */       this.socket.setReuseAddress(true);
/*  92: 95 */       this.socket.bind(new InetSocketAddress(this.port));
/*  93:    */     }
/*  94:    */     catch (Exception e)
/*  95:    */     {
/*  96: 98 */       log.info("Listen at " + this.port + " Failed.");
/*  97: 99 */       return;
/*  98:    */     }
/*  99:102 */     log.info("在端口" + this.port + "接收应用报警信息");
/* 100:103 */     byte[] recvBuf = new byte[2000];
/* 101:104 */     DatagramPacket packet = new DatagramPacket(recvBuf, 2000);
/* 102:105 */     SimpleDateFormat sdf = new SimpleDateFormat(" d日HH时mm分");
/* 103:    */     for (;;)
/* 104:    */     {
/* 105:    */       try
/* 106:    */       {
/* 107:111 */         this.socket.receive(packet);
/* 108:112 */         ByteBuffer bb = ByteBuffer.wrap(recvBuf);
/* 109:113 */         bb.order(ByteOrder.BIG_ENDIAN);
/* 110:114 */         int id = bb.getInt();
/* 111:115 */         int t = bb.getInt();
/* 112:116 */         int type = bb.getInt();
/* 113:117 */         if ((type <= 200) && (type >= 101))
/* 114:    */         {
/* 115:119 */           short len = bb.getShort();
/* 116:120 */           byte[] bs = new byte[len];
/* 117:121 */           bb.get(bs);
/* 118:122 */           String host = new String(bs, "GB2312");
/* 119:123 */           int i = redundance(id, t, host, type);
/* 120:124 */           if (i >= 0)
/* 121:    */           {
/* 122:126 */             len = bb.getShort();
/* 123:127 */             String alarmid = null;
/* 124:128 */             if (len > 0)
/* 125:    */             {
/* 126:130 */               bs = new byte[len];
/* 127:131 */               bb.get(bs);
/* 128:132 */               alarmid = new String(bs, "GB2312");
/* 129:    */             }
/* 130:134 */             len = bb.getShort();
/* 131:135 */             if (len == 0)
/* 132:    */             {
/* 133:137 */               log.error("host:" + host + " appType " + type + "报警 no alarminfo");
/* 134:    */             }
/* 135:    */             else
/* 136:    */             {
/* 137:140 */               bs = new byte[len];
/* 138:141 */               bb.get(bs);
/* 139:142 */               String info = new String(bs, "GB2312");
/* 140:143 */               String detail = "";
/* 141:144 */               len = bb.getShort();
/* 142:145 */               if (len > 0)
/* 143:    */               {
/* 144:147 */                 bs = new byte[len];
/* 145:148 */                 bb.get(bs);
/* 146:149 */                 detail = new String(bs, "GB2312");
/* 147:    */               }
/* 148:151 */               String sendid = null;
/* 149:152 */               boolean find = false;
/* 150:153 */               String alname = null;
/* 151:154 */               if (alarmid != null)
/* 152:    */               {
/* 153:155 */                 int j = 0; 
/* 154:156 */                 AppAlarmPolicy apone = this.policies[j];
/* 155:157 */                 if (apone.getId().equals(alarmid))
/* 156:    */                 {
/* 157:158 */                   sendid = apone.getSendid();
/* 158:159 */                   alname = apone.getName();
/* 159:160 */                   find = true;
/* 160:    */                 }
/* 161:    */                 else
/* 162:    */                 {
/* 163:155 */                   j++;
/* 164:155 */                   if (j < this.policies.length) {
/* 165:    */                     continue;
/* 166:    */                   }
/* 167:    */                 }
/* 168:    */               }
/* 169:    */               else
/* 170:    */               {
/* 171:165 */                 int j = 0; 
/* 172:166 */                 AppAlarmPolicy apone = this.policies[j];
/* 173:167 */                 if (apone.getType() == type)
/* 174:    */                 {
/* 175:168 */                   sendid = apone.getSendid();
/* 176:169 */                   alname = apone.getName();
/* 177:170 */                   alarmid = apone.getId();
/* 178:171 */                   find = true;
/* 179:    */                 }
/* 180:    */                 else
/* 181:    */                 {
/* 182:165 */                   j++;
/* 183:165 */                   if (j < this.policies.length) {
/* 184:    */                     continue;
/* 185:    */                   }
/* 186:    */                 }
/* 187:    */               }
/* 188:177 */               if (find)
/* 189:    */               {
/* 190:179 */                 if (alname == null) {
/* 191:180 */                   alname = alarmid;
/* 192:    */                 }
/* 193:181 */                 WriteAlarmDbThread.insertData(t * 1000L, alarmid, sendid, info, detail);
/* 194:182 */                 log.info("主机:" + host + " 应用 " + AppAlarmDefine.getAppName(type) + sdf.format(new Date(t * 1000L)) + " " + info);
/* 195:    */               }
/* 196:    */               else
/* 197:    */               {
/* 198:186 */                 log.info("主机:" + host + AppAlarmDefine.getAppName(type) + " 报警id:" + alarmid + "未定义");
/* 199:    */               }
/* 200:    */             }
/* 201:    */           }
/* 202:    */         }
/* 203:    */       }
/* 204:    */       catch (Exception e)
/* 205:    */       {
/* 206:192 */         e.printStackTrace();
/* 207:193 */         log.error(e.toString());
/* 208:194 */         this.socket.close();
/* 209:    */         try
/* 210:    */         {
/* 211:196 */           this.socket = new DatagramSocket(null);
/* 212:197 */           this.socket.setReuseAddress(true);
/* 213:198 */           this.socket.bind(new InetSocketAddress(this.port));
/* 214:    */         }
/* 215:    */         catch (SocketException e1)
/* 216:    */         {
/* 217:201 */           e1.printStackTrace();
/* 218:    */         }
/* 219:    */       }
/* 220:    */     }
/* 221:    */   }
/* 222:    */   
/* 223:    */   private int redundance(int id, int t, String host, int appType)
/* 224:    */   {
/* 225:208 */     if ((id > 10000) || (id < 0)) {
/* 226:209 */       return -1;
/* 227:    */     }
/* 228:210 */     int inow = (int)(System.currentTimeMillis() / 1000L);
/* 229:211 */     if ((t < inow - 864000) || (t > inow + 864000)) {
/* 230:213 */       return -1;
/* 231:    */     }
/* 232:215 */     for (int i = 0; i < this.appAlarmList.size(); i++)
/* 233:    */     {
/* 234:216 */       AppAlarmInfo one = (AppAlarmInfo)this.appAlarmList.get(i);
/* 235:217 */       if ((one.host.equals(host)) && (one.appType == appType))
/* 236:    */       {
/* 237:218 */         if ((id != one.id) || (t != one.time))
/* 238:    */         {
/* 239:219 */           one.id = id;
/* 240:220 */           one.time = t;
/* 241:221 */           return i;
/* 242:    */         }
/* 243:223 */         return -1;
/* 244:    */       }
/* 245:    */     }
/* 246:227 */     if (this.appAlarmList.size() > 1000)
/* 247:    */     {
/* 248:228 */       ArrayList<AppAlarmInfo> newAppAlarmList = new ArrayList();
/* 249:229 */       int c = 0;
/* 250:230 */       for (int j = 0; (j < this.appAlarmList.size()) && (c < 1000); j++) {
/* 251:231 */         if (((AppAlarmInfo)this.appAlarmList.get(j)).time > inow - 30)
/* 252:    */         {
/* 253:233 */           newAppAlarmList.add((AppAlarmInfo)this.appAlarmList.get(j));
/* 254:234 */           c++;
/* 255:    */         }
/* 256:    */       }
/* 257:237 */       this.appAlarmList = newAppAlarmList;
/* 258:    */     }
/* 259:239 */     AppAlarmInfo one = new AppAlarmInfo();
/* 260:240 */     one.id = id;
/* 261:241 */     one.time = t;
/* 262:242 */     one.host = host;
/* 263:243 */     one.appType = appType;
/* 264:244 */     this.appAlarmList.add(one);
/* 265:245 */     return this.appAlarmList.size() - 1;
/* 266:    */   }
/* 267:    */ }


/* Location:           C:\Users\lauren\Desktop\HD.AlarmCD.jar
 * Qualified Name:     com.nari.slsd.hd.alarmcd.threads.RecvAppAlarmThr
 * JD-Core Version:    0.7.0.1
 */