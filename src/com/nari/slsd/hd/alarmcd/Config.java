 package com.nari.slsd.hd.alarmcd;
/*   2:    */ 
/*   3:    */ import com.nari.slsd.hd.alarmcd.type.MonitorQueueConfig;
/*   4:    */ import com.nari.slsd.hd.data.pub.MailSenderInfo;
/*   5:    */ import com.nari.slsd.hd.util.ParamString;
/*   6:    */ import java.io.ByteArrayInputStream;
/*   7:    */ import java.io.ByteArrayOutputStream;
/*   8:    */ import java.io.File;
/*   9:    */ import java.io.FileNotFoundException;
/*  10:    */ import java.io.FileOutputStream;
/*  11:    */ import java.io.IOException;
/*  12:    */ import java.io.UnsupportedEncodingException;
/*  13:    */ import java.net.InetAddress;
/*  14:    */ import java.net.InetSocketAddress;
/*  15:    */ import java.util.List;
/*  16:    */ import org.dom4j.Document;
/*  17:    */ import org.dom4j.DocumentException;
/*  18:    */ import org.dom4j.DocumentHelper;
/*  19:    */ import org.dom4j.Element;
/*  20:    */ import org.dom4j.io.OutputFormat;
/*  21:    */ import org.dom4j.io.SAXReader;
/*  22:    */ import org.dom4j.io.XMLWriter;
/*  23:    */ 
/*  24:    */ public class Config
/*  25:    */ {
/*  26: 37 */   public static int sendPort = -1;
/*  27: 38 */   public static int appAlarmInfoPort = 10000;
/*  28: 39 */   public static int rtDataPort = 9000;
/*  29: 40 */   public static int yuejieTime = 60;
/*  30:    */   public static MonitorQueueConfig[] mqconfigs;
/*  31:    */   public static InetSocketAddress localIP;
/*  32:    */   public static InetSocketAddress SMIP;
/*  33:    */   public static InetSocketAddress oncallIP;
/*  34:    */   public static InetSocketAddress appIP;
/*  35:    */   public static MailSenderInfo mailInfo;
/*  36:    */   public static InetSocketAddress IMCIP;
/*  37: 49 */   public static int rebootTime = 0;
/*  38: 50 */   public static String[] addrStrs = new String[6];
/*  39:    */   
/*  40:    */   public static int getQueue(String policy)
/*  41:    */     throws Exception
/*  42:    */   {
/*  43: 53 */     if (mqconfigs == null) {
/*  44: 54 */       throw new Exception("undefine MonitorQueue");
/*  45:    */     }
/*  46: 55 */     for (int i = 0; i < mqconfigs.length; i++)
/*  47:    */     {
/*  48: 57 */       MonitorQueueConfig one = mqconfigs[i];
/*  49: 58 */       for (int j = 0; j < one.policies.length; j++) {
/*  50: 60 */         if (one.policies[j].equals(policy)) {
/*  51: 61 */           return i;
/*  52:    */         }
/*  53:    */       }
/*  54:    */     }
/*  55: 65 */     throw new Exception("undefine policy " + policy + " in MonitorQueue");
/*  56:    */   }
/*  57:    */   
/*  58:    */   public static void readXmlFile(byte[] bs)
/*  59:    */   {
/*  60: 70 */     UserLog log = new UserLog();
/*  61: 71 */     Document doc = null;
/*  62: 72 */     SAXReader saxReader = new SAXReader();
/*  63:    */     try
/*  64:    */     {
/*  65: 74 */       if (bs == null) {
/*  66: 75 */         doc = saxReader.read("alarmconfig.xml");
/*  67:    */       } else {
/*  68: 77 */         doc = saxReader.read(new ByteArrayInputStream(bs));
/*  69:    */       }
/*  70:    */     }
/*  71:    */     catch (DocumentException e1)
/*  72:    */     {
	                e1.printStackTrace();
/*  73: 79 */       log.info("AlarmServer config file can't parse,use default config");
/*  74: 80 */       return;
/*  75:    */     }
/*  76: 83 */     Element root = doc.getRootElement();
/*  77:    */     try
/*  78:    */     {
/*  79: 85 */       Element eport = root.element("SendPort");
/*  80: 86 */       if (eport != null) {
/*  81: 87 */         sendPort = Integer.parseInt(eport.getText());
/*  82:    */       }
/*  83:    */     }
/*  84:    */     catch (Exception e)
/*  85:    */     {
/*  86: 89 */       e.printStackTrace();
/*  87: 90 */       log.error(e.toString());
/*  88:    */     }
/*  89:    */     try
/*  90:    */     {
/*  91: 93 */       Element eport = root.element("AlarmInfoPort");
/*  92: 94 */       appAlarmInfoPort = Integer.parseInt(eport.getText());
/*  93:    */     }
/*  94:    */     catch (Exception e)
/*  95:    */     {
/*  96: 96 */       e.printStackTrace();
/*  97: 97 */       log.error(e.toString());
/*  98:    */     }
/*  99:    */     try
/* 100:    */     {
/* 101:100 */       Element eport = root.element("RtDataPort");
/* 102:101 */       rtDataPort = Integer.parseInt(eport.getText());
/* 103:    */     }
/* 104:    */     catch (Exception e)
/* 105:    */     {
/* 106:103 */       e.printStackTrace();
/* 107:104 */       log.error(e.toString());
/* 108:    */     }
/* 109:    */     try
/* 110:    */     {
/* 111:107 */       Element eport = root.element("YueJieTime");
/* 112:108 */       yuejieTime = Integer.parseInt(eport.getText());
/* 113:109 */       log.debug("yuejieTime" + yuejieTime);
/* 114:    */     }
/* 115:    */     catch (Exception e)
/* 116:    */     {
/* 117:112 */       e.printStackTrace();
/* 118:    */     }
/* 119:    */     try
/* 120:    */     {
/* 121:115 */       Element eport = root.element("RebootMin");
/* 122:116 */       rebootTime = Integer.parseInt(eport.getText());
/* 123:117 */       log.debug("rebootTime" + rebootTime);
/* 124:    */     }
/* 125:    */     catch (Exception e)
/* 126:    */     {
/* 127:120 */       e.printStackTrace();
/* 128:    */     }
/* 129:    */     try
/* 130:    */     {
/* 131:124 */       String text = root.element("LocalAddr").getText();
/* 132:125 */       addrStrs[0] = new String(text);
/* 133:126 */       localIP = new InetSocketAddress(InetAddress.getByName(ParamString.GetParaValue(text, "ip")), Integer.valueOf(ParamString.GetParaValue(text, "port")).intValue());
/* 134:    */     }
/* 135:    */     catch (Exception e)
/* 136:    */     {
/* 137:128 */       e.printStackTrace();
/* 138:129 */       log.error(e.toString());
/* 139:    */     }
/* 140:    */     try
/* 141:    */     {
/* 142:132 */       String text = root.element("SMAddr").getText();
/* 143:133 */       addrStrs[1] = new String(text);
/* 144:134 */       SMIP = new InetSocketAddress(InetAddress.getByName(ParamString.GetParaValue(text, "ip")), Integer.valueOf(ParamString.GetParaValue(text, "port")).intValue());
/* 145:    */     }
/* 146:    */     catch (Exception e)
/* 147:    */     {
/* 148:136 */       e.printStackTrace();
/* 149:137 */       log.error(e.toString());
/* 150:    */     }
/* 151:    */     try
/* 152:    */     {
/* 153:140 */       String text = root.element("OncallAddr").getText();
/* 154:141 */       addrStrs[2] = new String(text);
/* 155:142 */       oncallIP = new InetSocketAddress(InetAddress.getByName(ParamString.GetParaValue(text, "ip")), Integer.valueOf(ParamString.GetParaValue(text, "port")).intValue());
/* 156:    */     }
/* 157:    */     catch (Exception e)
/* 158:    */     {
/* 159:144 */       e.printStackTrace();
/* 160:145 */       log.error(e.toString());
/* 161:    */     }
/* 162:    */     try
/* 163:    */     {
/* 164:148 */       String text = root.element("APPAddr").getText();
/* 165:149 */       addrStrs[3] = new String(text);
/* 166:150 */       appIP = new InetSocketAddress(InetAddress.getByName(ParamString.GetParaValue(text, "ip")), Integer.valueOf(ParamString.GetParaValue(text, "port")).intValue());
/* 167:    */     }
/* 168:    */     catch (Exception e)
/* 169:    */     {
/* 170:152 */       e.printStackTrace();
/* 171:153 */       log.error(e.toString());
/* 172:    */     }
/* 173:    */     try
/* 174:    */     {
/* 175:156 */       String text = root.element("MailAddr").getText();
/* 176:157 */       addrStrs[4] = new String(text);
/* 177:158 */       mailInfo = new MailSenderInfo();
/* 178:159 */       mailInfo.setMailServerHost(ParamString.GetParaValue(text, "smtp"));
/* 179:160 */       mailInfo.setMailServerPort(ParamString.GetParaValue(text, "port"));
/* 180:161 */       mailInfo.setValidate(true);
/* 181:162 */       mailInfo.setUserName(ParamString.GetParaValue(text, "name"));
/* 182:163 */       mailInfo.setPassword(ParamString.GetParaValue(text, "password"));
/* 183:164 */       mailInfo.setFromAddress(ParamString.GetParaValue(text, "mail"));
/* 184:    */     }
/* 185:    */     catch (Exception e)
/* 186:    */     {
/* 187:166 */       e.printStackTrace();
/* 188:167 */       log.error(e.toString());
/* 189:    */     }
/* 190:    */     try
/* 191:    */     {
/* 192:170 */       String text = root.element("IMCAddr").getText();
/* 193:171 */       addrStrs[5] = new String(text);
/* 194:172 */       IMCIP = new InetSocketAddress(InetAddress.getByName(ParamString.GetParaValue(text, "ip")), Integer.valueOf(ParamString.GetParaValue(text, "port")).intValue());
/* 195:    */     }
/* 196:    */     catch (Exception e)
/* 197:    */     {
/* 198:174 */       e.printStackTrace();
/* 199:175 */       log.error(e.toString());
/* 200:    */     }
/* 201:178 */     Element ele = root.element("MonitorQueue");
/* 202:179 */     if (ele != null) {
/* 203:    */       try
/* 204:    */       {
/* 205:183 */         mqconfigs = new MonitorQueueConfig[ele.elements().size()];
/* 206:184 */         for (int i = 0; i < ele.elements().size(); i++)
/* 207:    */         {
/* 208:186 */           Element e = (Element)ele.elements().get(i);
/* 209:187 */           String type = ((Element)ele.elements().get(i)).attributeValue("type");
/* 210:188 */           if ((type != null) && ((!type.equals("JDBC")) || (!type.equals("WDS"))))
/* 211:    */           {
/* 212:190 */             String[] ps = new String[e.elements().size()];
/* 213:191 */             for (int j = 0; j < e.elements().size(); j++) {
/* 214:193 */               ps[j] = ((Element)e.elements().get(j)).getText();
/* 215:    */             }
/* 216:195 */             MonitorQueueConfig one = null;
/* 217:196 */             if (type.equals("JDBC")) {
/* 218:197 */               one = new MonitorQueueConfig(0, e.attributeValue("driver"), e.attributeValue("url"), 
/* 219:198 */                 e.attributeValue("user"), e.attributeValue("password"), 
/* 220:199 */                 Integer.parseInt(e.attributeValue("len")), 
/* 221:200 */                 Integer.parseInt(e.attributeValue("WaitMin")), ps);
/* 222:202 */             } else if (type.equals("WDS")) {
/* 223:203 */               one = new MonitorQueueConfig(1, 
/* 224:204 */                 Integer.parseInt(e.attributeValue("len")), 
/* 225:205 */                 Integer.parseInt(e.attributeValue("WaitMin")), ps);
/* 226:    */             }
/* 227:209 */             mqconfigs[i] = one;
/* 228:    */           }
/* 229:    */         }
/* 230:    */       }
/* 231:    */       catch (Exception e)
/* 232:    */       {
/* 233:214 */         e.printStackTrace();
/* 234:215 */         log.error("config invalid MonitorQueue,right format is \n<MonitorQueue> <Queue1  type=\"JDBC\" driver=\"oracle.jdbc.driver.OracleDriver\" url=\"jdbc:oracle:thin:@10.144.118.204:1521:wdscd\" user=\"wds\" password=\"narisq\" len=\"5\" WaitMin=\"10\">\t\n<policy>QueShu</policy>\t\n</Queue1>\n</MonitorQueue> ");
/* 235:    */         
/* 236:    */ 
/* 237:    */ 
/* 238:    */ 
/* 239:    */ 
/* 240:221 */         System.exit(0);
/* 241:    */       }
/* 242:    */     }
/* 243:    */   }
/* 244:    */   
/* 245:    */   public static byte[] writeXmlFile(MonitorQueueConfig[] mqcs)
/* 246:    */   {
/* 247:228 */     if (mqcs == null) {
/* 248:229 */       mqcs = mqconfigs;
/* 249:    */     }
/* 250:230 */     Document doc = DocumentHelper.createDocument();
/* 251:    */     
/* 252:232 */     Element root = doc.addElement("project");
/* 253:233 */     Element eport = root.addElement("AlarmInfoPort");
/* 254:234 */     eport.setText(String.valueOf(appAlarmInfoPort));
/* 255:235 */     eport = root.addElement("SendPort");
/* 256:236 */     eport.setText(String.valueOf(sendPort));
/* 257:237 */     Element rport = root.addElement("RtDataPort");
/* 258:238 */     rport.setText(String.valueOf(rtDataPort));
/* 259:239 */     Element yjTime = root.addElement("YueJieMin");
/* 260:240 */     yjTime.setText(String.valueOf(yuejieTime));
/* 261:241 */     yjTime = root.addElement("RebootMin");
/* 262:242 */     yjTime.setText(String.valueOf(rebootTime));
/* 263:243 */     Element one = root.addElement("LocalAddr");
/* 264:244 */     one.setText(addrStrs[0]);
/* 265:245 */     one = root.addElement("SMAddr");
/* 266:246 */     one.setText(addrStrs[1]);
/* 267:247 */     one = root.addElement("OncallAddr");
/* 268:248 */     one.setText(addrStrs[2]);
/* 269:249 */     one = root.addElement("APPAddr");
/* 270:250 */     one.setText(addrStrs[3]);
/* 271:251 */     one = root.addElement("MailAddr");
/* 272:252 */     one.setText(addrStrs[4]);
/* 273:253 */     one = root.addElement("IMCAddr");
/* 274:254 */     one.setText(addrStrs[5]);
/* 275:255 */     Element mqNode = root.addElement("MonitorQueue");
/* 276:256 */     for (int i = 0; i < mqcs.length; i++)
/* 277:    */     {
/* 278:258 */       MonitorQueueConfig mqc = mqcs[i];
/* 279:259 */       one = mqNode.addElement("Queue" + (i + 1));
/* 280:260 */       if (mqc.type == 0)
/* 281:    */       {
/* 282:262 */         one.addAttribute("type", "JDBC");
/* 283:263 */         one.addAttribute("driver", mqc.driver);
/* 284:264 */         one.addAttribute("url", mqc.url);
/* 285:265 */         one.addAttribute("user", mqc.user);
/* 286:266 */         one.addAttribute("password", mqc.pass);
/* 287:    */       }
/* 288:270 */       else if (mqc.type == 1)
/* 289:    */       {
/* 290:271 */         one.addAttribute("type", "WDS");
/* 291:    */       }
/* 292:273 */       one.addAttribute("len", String.valueOf(mqc.queueLen));
/* 293:274 */       one.addAttribute("WaitMin", String.valueOf(mqc.waitTime));
/* 294:275 */       for (int j = 0; j < mqc.policies.length; j++)
/* 295:    */       {
/* 296:277 */         Element e = one.addElement("policy" + (j + 1));
/* 297:278 */         e.setText(mqc.policies[j]);
/* 298:    */       }
/* 299:    */     }
/* 300:281 */     OutputFormat outFmt = OutputFormat.createPrettyPrint();
/* 301:282 */     XMLWriter output = null;
/* 302:    */     
/* 303:284 */     UserLog log = new UserLog();
/* 304:    */     try
/* 305:    */     {
/* 306:286 */       output = new XMLWriter(new FileOutputStream(new File("alarmconfig.xml")), 
/* 307:287 */         outFmt);
/* 308:288 */       output.write(doc);
/* 309:    */     }
/* 310:    */     catch (UnsupportedEncodingException e1)
/* 311:    */     {
/* 312:291 */       e1.printStackTrace();
/* 313:292 */       log.info(e1.toString());
/* 314:    */     }
/* 315:    */     catch (FileNotFoundException e1)
/* 316:    */     {
/* 317:295 */       e1.printStackTrace();
/* 318:296 */       log.info(e1.toString());
/* 319:    */     }
/* 320:    */     catch (IOException e)
/* 321:    */     {
/* 322:299 */       e.printStackTrace();
/* 323:300 */       log.info(e.toString());
/* 324:    */     }
/* 325:    */     try
/* 326:    */     {
/* 327:303 */       output.close();
/* 328:    */     }
/* 329:    */     catch (IOException e)
/* 330:    */     {
/* 331:305 */       e.printStackTrace();
/* 332:306 */       log.info(e.toString());
/* 333:    */     }
/* 334:309 */     boolean succ = false;
/* 335:310 */     ByteArrayOutputStream bas = new ByteArrayOutputStream();
/* 336:    */     try
/* 337:    */     {
/* 338:312 */       output = new XMLWriter(bas, outFmt);
/* 339:313 */       output.write(doc);
/* 340:314 */       succ = true;
/* 341:    */     }
/* 342:    */     catch (Exception e)
/* 343:    */     {
/* 344:316 */       log.info(e.toString());
/* 345:    */     }
/* 346:    */     try
/* 347:    */     {
/* 348:319 */       output.close();
/* 349:    */     }
/* 350:    */     catch (IOException e)
/* 351:    */     {
/* 352:321 */       e.printStackTrace();
/* 353:322 */       log.info(e.toString());
/* 354:    */     }
/* 355:324 */     if (succ) {
/* 356:325 */       return bas.toByteArray();
/* 357:    */     }
/* 358:327 */     return null;
/* 359:    */   }
/* 360:    */ }


/* Location:           C:\Users\lauren\Desktop\HD.AlarmCD.jar
 * Qualified Name:     com.nari.slsd.hd.alarmcd.Config
 * JD-Core Version:    0.7.0.1
 */