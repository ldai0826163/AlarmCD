/*   1:    */ package com.nari.slsd.hd.alarmcd.send;
/*   2:    */ 
/*   3:    */ import com.nari.slsd.hd.alarmcd.Config;
/*   4:    */ import com.nari.slsd.hd.alarmcd.UserLog;
/*   5:    */ import com.nari.slsd.hd.data.pub.AlarmLogType;
/*   6:    */ import com.nari.slsd.hd.data.pub.AlarmNotice;
/*   7:    */ import com.nari.slsd.hd.data.pub.MailSenderInfo;
/*   8:    */ import com.nari.slsd.hd.data.pub.RushToDealPara;
/*   9:    */ import com.nari.slsd.hd.data.pub.SMMsg;
/*  10:    */ import com.nari.slsd.hd.model.PubDataAlarmlog;
/*  11:    */ import com.nari.slsd.hd.model.PubDefSendpolicy;
/*  12:    */ import com.nari.slsd.hd.rights.PurvTeamdef;
/*  13:    */ import com.nari.slsd.hd.rights.PurvUserdef;
/*  14:    */ import com.nari.slsd.hd.service.IPubDataService;
/*  15:    */ import com.nari.slsd.hd.util.CommonTool;
/*  16:    */ import com.nari.slsd.hd.util.MailSender;
/*  17:    */ import com.nari.slsd.hd.util.XmlTool;
/*  18:    */ import java.io.UnsupportedEncodingException;
/*  19:    */ import java.net.DatagramPacket;
/*  20:    */ import java.net.DatagramSocket;
/*  21:    */ import java.net.Inet4Address;
/*  22:    */ import java.net.InetAddress;
/*  23:    */ import java.net.InetSocketAddress;
/*  24:    */ import java.net.MulticastSocket;
/*  25:    */ import java.net.NetworkInterface;
/*  26:    */ import java.net.SocketAddress;
/*  27:    */ import java.sql.Timestamp;
/*  28:    */ import java.text.SimpleDateFormat;
/*  29:    */ import java.util.ArrayList;
/*  30:    */ import java.util.Arrays;
/*  31:    */ import java.util.Collection;
/*  32:    */ import java.util.Date;
/*  33:    */ import java.util.HashMap;
/*  34:    */ import java.util.Iterator;
/*  35:    */ import java.util.List;
/*  36:    */ import java.util.Map;
/*  37:    */ import java.util.Set;
/*  38:    */ import java.util.UUID;
/*  39:    */ 
/*  40:    */ public class SendAlarmInfo
/*  41:    */ {
/*  42:    */   private static IPubDataService iPub;
/*  43:    */   private static Map<String, RushToDealPara> policies;
/*  44:    */   private static Map<String, PubDefSendpolicy> objs;
/*  45:    */   private static Map<String, PurvUserdef> users;
/*  46:    */   private static Map<String, List<String>> team_users;
/*  47: 46 */   private static long smTaskID = 0L;
/*  48: 47 */   private static UserLog log = new UserLog();
/*  49:    */   
/*  50:    */   public static void init(IPubDataService pubservice)
/*  51:    */   {
/*  52: 54 */     iPub = pubservice;
/*  53: 55 */     List<PubDefSendpolicy> policy = iPub.getAllList(PubDefSendpolicy.class);
/*  54: 56 */     if ((policy != null) && (policy.size() > 0))
/*  55:    */     {
/*  56: 57 */       objs = new HashMap(policy.size() * 4 / 3);
/*  57: 58 */       policies = new HashMap(policy.size() * 4 / 3);
/*  58:    */       label389:
/*  59: 59 */       for (PubDefSendpolicy p : policy) {
/*  60:    */         try
/*  61:    */         {
/*  62: 61 */           if (p.getContent() != null)
/*  63:    */           {
/*  64: 63 */             objs.put(p.getSendid(), p);
/*  65: 64 */             policies.put(p.getSendid(), (RushToDealPara)XmlTool.unmarshal(new String(p.getContent(), "UTF-8"), new Class[] { RushToDealPara.class }));
/*  66: 65 */             List<PurvUserdef> us = iPub.getAllPurvUserdefs();
/*  67: 66 */             if (us != null)
/*  68:    */             {
/*  69: 67 */               users = new HashMap();
/*  70: 68 */               for (PurvUserdef u : us) {
/*  71: 69 */                 users.put(u.getUserid(), u);
/*  72:    */               }
/*  73:    */             }
/*  74: 72 */             List<PurvTeamdef> ts = iPub.getPurvTeamList();
/*  75: 73 */             if (ts != null)
/*  76:    */             {
/*  77: 74 */               team_users = new HashMap();
/*  78:    */               Iterator localIterator4;
/*  79: 75 */               for (Iterator localIterator3 = ts.iterator(); localIterator3.hasNext(); localIterator4.hasNext())
/*  80:    */               {
/*  81: 75 */                 PurvTeamdef t = (PurvTeamdef)localIterator3.next();
/*  82: 76 */                 team_users.put(t.getId(), new ArrayList());
/*  83: 77 */                 List<PurvUserdef> tempu = iPub.getUserByRelation(t.getId());
/*  84: 78 */                 if ((tempu == null) || (tempu.size() <= 0)) {
/*  85:    */                   break label389;
/*  86:    */                 }
/*  87: 79 */                 localIterator4 = tempu.iterator(); 
                               PurvUserdef u = (PurvUserdef)localIterator4.next();
/*  88: 80 */                 ((List)team_users.get(t.getId())).add(u.getUserid());
/*  89:    */               }
/*  90:    */             }
/*  91:    */           }
/*  92:    */         }
/*  93:    */         catch (Exception e)
/*  94:    */         {
/*  95: 86 */           e.printStackTrace();
/*  96:    */         }
/*  97:    */       }
/*  98:    */     }
/*  99:    */     else
/* 100:    */     {
/* 101: 90 */       policies = null;
/* 102: 91 */       objs = null;
/* 103:    */     }
/* 104:    */   }
/* 105:    */   
/* 106:    */   public static byte sendAlarmInfo(String sendid, String alalrmID, String content, Date time)
/* 107:    */   {
/* 108:103 */     if ((iPub != null) && (policies != null) && (policies.size() > 0) && (policies.containsKey(sendid)))
/* 109:    */     {
/* 110:104 */       RushToDealPara para = (RushToDealPara)policies.get(sendid);
/* 111:106 */       if ((Config.localIP != null) && (Config.IMCIP != null))
/* 112:    */       {
/* 113:107 */         String[] infos = getPersonInfo(para.WarnPeople_Screen, 5);
/* 114:108 */         if ((infos != null) && (infos.length > 0))
/* 115:    */         {
/* 116:109 */           AlarmNotice notice = new AlarmNotice();
/* 117:110 */           notice.alarmId = alalrmID;
/* 118:111 */           notice.content = content;
/* 119:112 */           notice.date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(time);
/* 120:113 */           notice.userIds = infos;
/* 121:    */           try
/* 122:    */           {
/* 123:115 */             sendToIMC(XmlTool.marshal(notice, "UTF-8").getBytes("UTF-8"), Config.localIP.getPort(), Config.IMCIP);
/* 124:    */           }
/* 125:    */           catch (Exception e)
/* 126:    */           {
/* 127:117 */             log.error("sendid:" + sendid + " alarmid:" + alalrmID + " send screen fail  " + e.toString());
/* 128:118 */             e.printStackTrace();
/* 129:    */           }
/* 130:    */         }
/* 131:    */       }
/* 132:    */       String[] arrayOfString2;
/* 133:    */       int i;
/* 134:    */       
/* 135:122 */       if ((para.WarnPeople_SM != null) && (Config.localIP != null) && (Config.SMIP != null))
/* 136:    */       {
/* 137:123 */         String[] infos = getPersonInfo(para.WarnPeople_SM, 0);
/* 138:124 */         if ((infos != null) && (infos.length > 0))
/* 139:    */         {
/* 140:125 */           SMMsg msg = new SMMsg();
/* 141:126 */           msg.address = infos;
/* 142:127 */           msg.needConfirm = 0;
/* 143:128 */           msg.taskID = String.valueOf(smTaskID++);
/* 144:129 */           msg.content = content;
/* 145:    */           try
/* 146:    */           {
/* 147:131 */             if (sendUDP(XmlTool.marshal(msg, "UTF-8").replace("smMsg", "SMMsg").replace("needConfirm", "NeedConfirm").replace("content", "Content").replace("taskID", "TaskID").replace("address", "Address").replace(" standalone=\"yes\"", "").getBytes("UTF-8"), Config.localIP, Config.SMIP))
/* 148:    */             {
/* 149:132 */               StringBuilder sbd = new StringBuilder();
/* 150:133 */               i = (arrayOfString2 = msg.address).length;
/* 151:133 */               for (int str1 = 0; str1 < i; str1++)
/* 152:    */               {
/* 153:133 */                 String s = arrayOfString2[str1];
/* 154:134 */                 sbd.append(s + "，");
/* 155:    */               }
/* 156:136 */               sbd.setLength(sbd.length() - 1);
/* 157:137 */               writeLog(alalrmID, time, content + " --- " + sbd.toString(), AlarmLogType.SMNotice);
/* 158:    */             }
/* 159:    */           }
/* 160:    */           catch (Exception e)
/* 161:    */           {
/* 162:141 */             log.error("sendid:" + sendid + " alarmid:" + alalrmID + " send sms fail  " + e.toString());
/* 163:142 */             e.printStackTrace();
/* 164:    */           }
/* 165:    */         }
/* 166:    */       }
/* 167:    */       
/* 168:146 */       if ((para.WarnPeople_APP != null) && (Config.localIP != null) && (Config.appIP != null))
/* 169:    */       {
/* 170:147 */         String[] infos = getPersonInfo(para.WarnPeople_APP, 1);
/* 171:148 */         if ((infos != null) && (infos.length > 0))
/* 172:    */         {
/* 173:149 */           log.info("send sms");
/* 174:150 */           SMMsg msg = new SMMsg();
/* 175:151 */           msg.address = infos;
/* 176:152 */           msg.needConfirm = 0;
/* 177:153 */           msg.taskID = String.valueOf(smTaskID++);
/* 178:154 */           msg.content = content;
/* 179:    */           try
/* 180:    */           {
/* 181:156 */             if (sendUDP(XmlTool.marshal(msg, "UTF-8").replace("smMsg", "SMMsg").replace("needConfirm", "NeedConfirm").replace("content", "Content").replace("taskID", "TaskID").replace("address", "Address").replace(" standalone=\"yes\"", "").getBytes("UTF-8"), Config.localIP, Config.appIP))
/* 182:    */             {
/* 183:157 */               StringBuilder sbd = new StringBuilder();
/* 184:158 */               i = (arrayOfString2 = msg.address).length;
/* 185:158 */               for (int str1 = 0; str1 < i; str1++)
/* 186:    */               {
/* 187:158 */                 int s = Integer.parseInt(arrayOfString2[str1]);
/* 188:159 */                 sbd.append(s + "，");
/* 189:    */               }
/* 190:161 */               sbd.setLength(sbd.length() - 1);
/* 191:162 */               writeLog(alalrmID, time, content + " --- " + sbd.toString(), AlarmLogType.AppNotice);
/* 192:    */             }
/* 193:    */           }
/* 194:    */           catch (Exception e)
/* 195:    */           {
/* 196:166 */             log.error("sendid:" + sendid + " alarmid:" + alalrmID + " send app fail  " + e.toString());
/* 197:167 */             e.printStackTrace();
/* 198:    */           }
/* 199:    */         }
/* 200:    */       }
/* 201:171 */       if ((para.WarnPeople_Mail != null) && (Config.mailInfo != null))
/* 202:    */       {
/* 203:172 */         String[] infos = getPersonInfo(para.WarnPeople_Voice_MTel, 2);
/* 204:173 */         if ((infos != null) && (sendMails(infos, content, "[OnCall]")))
/* 205:    */         {
/* 206:174 */           StringBuilder sbd = new StringBuilder();
/* 207:    */           String[] arrayOfString1;
/* 208:175 */           int str1 = (arrayOfString1 = infos).length;
/* 209:175 */           for (int s = 0; s < str1; s++)
/* 210:    */           {
/* 211:175 */              s = Integer.parseInt(arrayOfString1[s]);
/* 212:176 */             sbd.append(s + "，");
/* 213:    */           }
/* 214:178 */           writeLog(alalrmID, time, content + " --- " + sbd.toString(), AlarmLogType.MailNotice);
/* 215:    */         }
/* 216:    */       }
/* 217:181 */       if (((para.WarnPeople_Voice_MTel != null) || (para.WarnPeople_Voice_Tel != null)) && (Config.localIP != null) && (Config.oncallIP != null))
/* 218:    */       {
/* 219:182 */         String[] infos = getPersonInfo(para.WarnPeople_Voice_MTel, 3);
/* 220:183 */         String[] infos1 = getPersonInfo(para.WarnPeople_Voice_Tel, 4);
/* 221:184 */         if (infos == null)
/* 222:    */         {
/* 223:185 */           infos = infos1;
/* 224:    */         }
/* 225:187 */         else if (infos1 != null)
/* 226:    */         {
/* 227:188 */           infos = (String[])Arrays.copyOf(infos, infos.length + infos1.length);
/* 228:189 */           System.arraycopy(infos1, 0, infos, infos.length - infos1.length, infos1.length);
/* 229:    */         }
/* 230:191 */         if (infos != null)
/* 231:    */         {
/* 232:192 */           StringBuilder sb = new StringBuilder();
/* 233:193 */           sb.append("CodeId=" + smTaskID++ + ";");
/* 234:194 */           sb.append("Content=" + content + ";");
/* 235:195 */           sb.append("Confirm=false;");
/* 236:196 */           StringBuilder sbd = new StringBuilder();
/* 237:197 */           for (String s : infos) {
/* 238:198 */             sbd.append(s + ",");
/* 239:    */           }
/* 240:200 */           sbd.setLength(sbd.length() - 1);
/* 241:201 */           sb.append("PhoneNum=" + sbd.toString() + ";");
/* 242:    */           try
/* 243:    */           {
/* 244:203 */             if (sendUDP(sb.toString().getBytes("UTF-8"), Config.localIP, Config.oncallIP)) {
/* 245:204 */               writeLog(alalrmID, time, content + " --- " + sbd.toString(), AlarmLogType.TelNotice);
/* 246:    */             }
/* 247:    */           }
/* 248:    */           catch (Exception e)
/* 249:    */           {
/* 250:207 */             log.error("sendid:" + sendid + " alarmid:" + alalrmID + " send mobile fail  " + e.toString());
/* 251:208 */             e.printStackTrace();
/* 252:    */           }
/* 253:    */         }
/* 254:    */       }
/* 255:211 */       return 1;
/* 256:    */     }
/* 257:213 */     log.info("sendid:" + sendid + " alarmid:" + alalrmID + "未定义");
/* 258:214 */     return 0;
/* 259:    */   }
/* 260:    */   
/* 261:    */   public static String[] getPersonInfo(String[] ids, int attribute)
/* 262:    */   {
/* 263:220 */     if ((ids == null) || (ids.length < 1)) {
/* 264:221 */       return null;
/* 265:    */     }
/* 266:222 */     HashMap<String, String> res = new HashMap();
/* 267:    */     
/* 268:    */ 
/* 269:    */ 
/* 270:226 */     String[] arrayOfString = ids;int j = ids.length;
/* 271:226 */     for (int i = 0; i < j; i++)
/* 272:    */     {
/* 273:226 */       String id = arrayOfString[i];
/* 274:227 */       if ((!res.containsKey(id)) && (users.get(id) != null))
/* 275:    */       {
/* 276:228 */         PurvUserdef user = (PurvUserdef)users.get(id);
/* 277:229 */         if ((attribute == 0) || (attribute == 1))
/* 278:    */         {
/* 279:230 */           String temp = user.getMobile();
/* 280:231 */           if ((temp != null) && (temp.trim().length() >= 1))
/* 281:    */           {
/* 282:234 */             temp = temp.trim();
/* 283:    */             
/* 284:236 */             int idx = temp.indexOf('-');
/* 285:237 */             res.put(id, (idx >= 0) && (idx < temp.length() - 1) ? temp.substring(idx + 1) : temp);
/* 286:    */           }
/* 287:    */         }
/* 288:238 */         else if (attribute == 2)
/* 289:    */         {
/* 290:239 */           if ((user.getEmail() != null) && (user.getEmail().trim().length() > 0)) {
/* 291:240 */             res.put(id, user.getEmail().trim());
/* 292:    */           }
/* 293:    */         }
/* 294:241 */         else if ((attribute == 3) || (attribute == 4))
/* 295:    */         {
/* 296:242 */           String temp = attribute == 3 ? user.getMobile() : user.getPhone();
/* 297:243 */           if ((temp != null) && (temp.trim().length() >= 1))
/* 298:    */           {
/* 299:246 */             temp = temp.trim();
/* 300:    */             
/* 301:248 */             res.put(id, temp.replace("-", ""));
/* 302:    */           }
/* 303:    */         }
/* 304:249 */         else if (attribute == 5)
/* 305:    */         {
/* 306:250 */           res.put(id, id);
/* 307:    */         }
/* 308:    */       }
/* 309:252 */       else if ((team_users.get(id) != null) && (((List)team_users.get(id)).size() > 0))
/* 310:    */       {
/* 311:253 */         for (String pid : team_users.get(id)) {
/* 312:254 */           if ((users.get(pid) != null) && (!res.containsKey(id)))
/* 313:    */           {
/* 314:255 */             PurvUserdef user = (PurvUserdef)users.get(pid);
/* 315:256 */             if ((attribute == 0) || (attribute == 1))
/* 316:    */             {
/* 317:257 */               String temp = user.getMobile();
/* 318:258 */               if ((temp != null) && (temp.trim().length() >= 1))
/* 319:    */               {
/* 320:261 */                 temp = temp.trim();
/* 321:    */                 
/* 322:263 */                 int idx = temp.indexOf('-');
/* 323:264 */                 res.put(pid, (idx >= 0) && (idx < temp.length() - 1) ? temp.substring(idx + 1) : temp);
/* 324:    */               }
/* 325:    */             }
/* 326:265 */             else if (attribute == 2)
/* 327:    */             {
/* 328:266 */               if ((user.getEmail() != null) && (user.getEmail().trim().length() > 0)) {
/* 329:267 */                 res.put(pid, user.getEmail().trim());
/* 330:    */               }
/* 331:    */             }
/* 332:268 */             else if ((attribute == 3) || (attribute == 4))
/* 333:    */             {
/* 334:269 */               String temp = attribute == 3 ? user.getMobile() : user.getPhone();
/* 335:270 */               if ((temp != null) && (temp.trim().length() >= 1))
/* 336:    */               {
/* 337:273 */                 temp = temp.trim();
/* 338:    */                 
/* 339:275 */                 res.put(pid, temp.replace("-", ""));
/* 340:    */               }
/* 341:    */             }
/* 342:276 */             else if (attribute == 5)
/* 343:    */             {
/* 344:277 */               res.put(pid, pid);
/* 345:    */             }
/* 346:    */           }
/* 347:    */         }
/* 348:    */       }
/* 349:    */     }
/* 350:284 */     if (res.size() > 0) {
/* 351:285 */       return (String[])res.values().toArray(new String[0]);
/* 352:    */     }
/* 353:287 */     return null;
/* 354:    */   }
/* 355:    */   
/* 356:    */   public static boolean sendToIMC(byte[] content, int localport, SocketAddress dest)
/* 357:    */   {
/* 358:291 */     Map<NetworkInterface, List<InetAddress>> res = CommonTool.getAllIP();
/* 359:292 */     if ((res != null) && (res.size() > 0))
/* 360:    */     {
/* 361:293 */       MulticastSocket local = null;
/* 362:294 */       Set<NetworkInterface> nis = res.keySet();
/* 363:296 */       for (NetworkInterface ni : nis)
/* 364:    */       {
/* 365:297 */         List<InetAddress> ips = (List)res.get(ni);
/* 366:298 */         if ((ips != null) && (ips.size() > 0)) {
/* 367:    */           try
/* 368:    */           {
/* 369:300 */             for (InetAddress ip : ips) {
/* 370:301 */               if (((ip instanceof Inet4Address)) && (!ip.getHostAddress().equals("127.0.0.1")))
/* 371:    */               {
/* 372:303 */                 local = new MulticastSocket(new InetSocketAddress(ip, localport));
/* 373:    */                 
/* 374:305 */                 local.send(new DatagramPacket(content, 0, content.length, dest));
/* 375:306 */                 local.close();
/* 376:307 */                 break;
/* 377:    */               }
/* 378:    */             }
/* 379:    */           }
/* 380:    */           catch (Exception e)
/* 381:    */           {
/* 382:310 */             log.error("sendToImc fail" + e.toString());
/* 383:311 */             e.printStackTrace();
/* 384:312 */             if (local != null) {
/* 385:    */               try
/* 386:    */               {
/* 387:313 */                 local.close();
/* 388:    */               }
/* 389:    */               catch (Exception e1)
/* 390:    */               {
/* 391:314 */                 log.error(e1.toString());
/* 392:315 */                 e1.printStackTrace();
/* 393:    */               }
/* 394:    */             }
/* 395:    */           }
/* 396:    */         }
/* 397:    */       }
/* 398:321 */       return true;
/* 399:    */     }
/* 400:323 */     return false;
/* 401:    */   }
/* 402:    */   
/* 403:    */   public static boolean sendUDP(byte[] content, SocketAddress local, SocketAddress dest)
/* 404:    */   {
/* 405:327 */     DatagramSocket socket = null;
/* 406:    */     try
/* 407:    */     {
/* 408:329 */       socket = new DatagramSocket(local);
/* 409:330 */       DatagramPacket p = new DatagramPacket(content, 0, content.length, dest);
/* 410:331 */       socket.send(p);
/* 411:332 */       socket.close();
/* 412:333 */       return true;
/* 413:    */     }
/* 414:    */     catch (Exception e)
/* 415:    */     {
/* 416:335 */       log.error("sendUDP error " + e.toString());
/* 417:336 */       if (socket != null) {
/* 418:    */         try
/* 419:    */         {
/* 420:338 */           socket.close();
/* 421:    */         }
/* 422:    */         catch (Exception e1)
/* 423:    */         {
/* 424:340 */           e.printStackTrace();
/* 425:    */         }
/* 426:    */       }
/* 427:343 */       e.printStackTrace();
/* 428:    */     }
/* 429:345 */     return false;
/* 430:    */   }
/* 431:    */   
/* 432:    */   public static boolean sendMails(String[] mails, String content, String subject)
/* 433:    */   {
/* 434:349 */     if ((mails != null) && (mails.length > 0) && (Config.mailInfo != null) && (content != null)) {
/* 435:    */       try
/* 436:    */       {
/* 437:351 */         MailSenderInfo info = Config.mailInfo.clone();
/* 438:352 */         info.setSubject(subject);
/* 439:353 */         info.setContent(content.trim());
/* 440:354 */         String[] arrayOfString = mails;int j = mails.length;
/* 441:354 */         for (int i = 0; i < j; i++)
/* 442:    */         {
/* 443:354 */           String mail = arrayOfString[i];
/* 444:355 */           info.setToAddress(mail);
/* 445:356 */           MailSender.sendTextMail(info);
/* 446:    */         }
/* 447:358 */         return true;
/* 448:    */       }
/* 449:    */       catch (Exception e)
/* 450:    */       {
/* 451:360 */         log.error("sendmail fail " + mails + e.toString());
/* 452:361 */         e.printStackTrace();
/* 453:    */       }
/* 454:    */     }
/* 455:363 */     return false;
/* 456:    */   }
/* 457:    */   
/* 458:    */   public static boolean writeLog(String alalrmid, Date time, String content, AlarmLogType type)
/* 459:    */   {
/* 460:367 */     PubDataAlarmlog log = new PubDataAlarmlog();
/* 461:    */     try
/* 462:    */     {
/* 463:369 */       log.setId(UUID.randomUUID().toString().replace("-", ""));
/* 464:370 */       log.setDescr(content.getBytes("UTF-8"));
/* 465:371 */       log.setDt(new Timestamp(time.getTime()));
/* 466:372 */       log.setType(type.getValue());
/* 467:373 */       log.setPara("alalrmid=" + alalrmid);
/* 468:374 */       iPub.save(log);
/* 469:    */     }
/* 470:    */     catch (UnsupportedEncodingException e)
/* 471:    */     {
/* 472:376 */       e.printStackTrace();
/* 473:    */     }
/* 474:378 */     return false;
/* 475:    */   }
/* 476:    */ }


/* Location:           C:\Users\lauren\Desktop\HD.AlarmCD.jar
 * Qualified Name:     com.nari.slsd.hd.alarmcd.send.SendAlarmInfo
 * JD-Core Version:    0.7.0.1
 */