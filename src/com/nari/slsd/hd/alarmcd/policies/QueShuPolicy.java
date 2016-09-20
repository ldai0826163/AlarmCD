/*   1:    */ package com.nari.slsd.hd.alarmcd.policies;
/*   2:    */ 
/*   4:    */ import com.nari.slsd.hd.alarmcd.type.AlarmRecord;
/*   5:    */ import com.nari.slsd.hd.alarmcd.type.AlarmRecordManager;
/*   6:    */ import com.nari.slsd.hd.alarmcd.type.SingleDataBase;
/*   7:    */ import com.nari.slsd.hd.model.WdsHydroElements;
/*   8:    */ import com.nari.slsd.hd.service.IWdsHisDataService;
/*   9:    */ import java.io.ByteArrayInputStream;
/*  10:    */ import java.sql.Connection;
/*  11:    */ import java.sql.PreparedStatement;
/*  12:    */ import java.sql.ResultSet;
/*  13:    */ import java.sql.Timestamp;
/*  14:    */ import java.text.SimpleDateFormat;
/*  16:    */ import java.util.Calendar;
/*  17:    */ import java.util.Date;
/*  18:    */ import java.util.Iterator;
/*  19:    */ import java.util.List;
/*  20:    */ import org.dom4j.Document;
/*  21:    */ import org.dom4j.Element;
/*  22:    */ import org.dom4j.io.SAXReader;
/*  23:    */ 
/*  24:    */ public class QueShuPolicy
/*  25:    */   extends Policy
/*  26:    */ {
/*  27:    */   IdDefine[] ids;
/*  28:    */   String[] names;
/*  29:    */   int dataType;
/*  30:    */   int timeSpan;
/*  31:    */   
/*  32:    */   public int getDataType()
/*  33:    */   {
/*  34: 33 */     return this.dataType;
/*  35:    */   }
/*  36:    */   
/*  37:    */   public void setDataType(int dataType)
/*  38:    */   {
/*  39: 37 */     this.dataType = dataType;
/*  40:    */   }
/*  41:    */   
/*  42:    */   public int getTimeSpan()
/*  43:    */   {
/*  44: 41 */     return this.timeSpan;
/*  45:    */   }
/*  46:    */   
/*  47:    */   public void setTimeSpan(int timeSpan)
/*  48:    */   {
/*  49: 45 */     this.timeSpan = timeSpan;
/*  50:    */   }
/*  51:    */   
/*  52: 51 */   String table = "rtev";
/*  53:    */   
/*  54:    */   public QueShuPolicy()
/*  55:    */   {
/*  56: 54 */     this.type = 11;
/*  57:    */   }
/*  58:    */   
/*  59:    */   public void check(Calendar cal, SingleDataBase db)
/*  60:    */   {
/*  61: 58 */     this.log.debug("queshu check " + this.dataType + " " + this.name + " begin!");
/*  62: 59 */     AlarmRecord ar = AlarmRecordManager.getRecord(this.id);
/*  63: 61 */     if (ar.recordTime > cal.getTimeInMillis()) {
/*  64: 62 */       return;
/*  65:    */     }
/*  66: 63 */     Connection conn = db.conn;
/*  67: 64 */     Date endTime = new Date(cal.getTimeInMillis() + 60000L);
/*  68: 65 */     Date startTime = null;
/*  69: 66 */     String sql = "";
/*  70: 67 */     String Type = "";
/*  71: 68 */     SimpleDateFormat sdf = new SimpleDateFormat("d日 HH时mm分");
/*  72: 70 */     if (this.dataType == 1)
/*  73:    */     {
/*  74: 72 */       startTime = new Date(cal.getTimeInMillis() - this.timeSpan * 60 * 1000);
/*  75: 73 */       Type = "实时数据";
/*  76:    */     }
/*  77: 74 */     else if (this.dataType == 12)
/*  78:    */     {
/*  79: 76 */       int minute = cal.get(12);
/*  80: 77 */       if (minute < this.timeSpan) {
/*  81: 79 */         return;
/*  82:    */       }
/*  83: 81 */       Calendar hour = Calendar.getInstance();
/*  84: 82 */       hour.setTimeInMillis(cal.getTimeInMillis());
/*  85: 83 */       hour.set(12, 0);
/*  86: 84 */       hour.set(13, 0);
/*  87: 85 */       hour.set(14, 0);
/*  88: 86 */       startTime = new Date(hour.getTimeInMillis());
/*  89: 87 */       sql = "SELECT COUNT(*) from HOURDB WHERE SENID=? AND TIME >= ? AND TIME <?";
/*  90: 88 */       Type = "小时数据";
/*  91:    */     }
/*  92: 89 */     else if (this.dataType == 288)
/*  93:    */     {
/*  94: 91 */       int hourOfDay = cal.get(11);
/*  95: 92 */       int minute = cal.get(12);
/*  96: 93 */       if (hourOfDay * 60 + minute < this.timeSpan) {
/*  97: 95 */         return;
/*  98:    */       }
/*  99: 97 */       Calendar day = Calendar.getInstance();
/* 100: 98 */       day.setTimeInMillis(cal.getTimeInMillis());
/* 101: 99 */       day.set(11, 0);
/* 102:100 */       day.set(12, 0);
/* 103:101 */       day.set(13, 0);
/* 104:102 */       day.set(14, 0);
/* 105:103 */       startTime = new Date(day.getTimeInMillis());
/* 106:104 */       sql = "SELECT COUNT(*) from DAYDB WHERE SENID=? AND TIME >= ? AND TIME <?";
/* 107:105 */       Type = "日数据";
/* 108:    */     }
/* 109:106 */     else if (this.dataType == 8640)
/* 110:    */     {
/* 111:109 */       int dayOfMonth = cal.get(5);
/* 112:110 */       int hourOfDay = cal.get(11);
/* 113:111 */       int minute = cal.get(12);
/* 114:112 */       if (dayOfMonth * 24 * 60 + hourOfDay * 60 + minute < this.timeSpan) {
/* 115:114 */         return;
/* 116:    */       }
/* 117:116 */       Calendar month = Calendar.getInstance();
/* 118:117 */       month.setTimeInMillis(cal.getTimeInMillis());
/* 119:118 */       month.set(5, 1);
/* 120:119 */       month.set(11, 0);
/* 121:120 */       month.set(12, 0);
/* 122:121 */       month.set(13, 0);
/* 123:122 */       month.set(14, 0);
/* 124:123 */       startTime = new Date(month.getTimeInMillis());
/* 125:124 */       sql = "SELECT COUNT(*) from MONTHDB WHERE SENID=? AND TIME >= ? AND TIME <?";
/* 126:125 */       Type = "月数据";
/* 127:    */     }
/* 128:    */     else
/* 129:    */     {
/* 130:127 */       this.log.error("报警" + this.id + "缺数检查Unknow DataType");
/* 131:128 */       return;
/* 132:    */     }
/* 133:130 */     PreparedStatement pstmt = null;
/* 134:131 */     ResultSet rs = null;
/* 135:132 */     StringBuffer errorStr = new StringBuffer(sdf.format(cal.getTime()) + "测站");
/* 136:133 */     boolean findError = false;
/* 137:134 */     StringBuffer recoverStr = new StringBuffer(sdf.format(cal.getTime()) + "测站");
/* 138:135 */     boolean findRecover = false;
/* 139:136 */     boolean alarmAll = false;
/* 140:137 */     if (cal.getTimeInMillis() - ar.recordTime >= 3600000L) {
/* 141:139 */       alarmAll = true;
/* 142:    */     }
/* 143:141 */     for (int i = 0; i < this.ids.length; i++) {
/* 144:    */       try
/* 145:    */       {
/* 146:143 */         if (this.dataType == 1) {
/* 147:144 */           sql = 
/* 148:145 */             "SELECT COUNT(*) from " + this.ids[i].table + " WHERE SENID=? AND TIME >= ? AND TIME <?";
/* 149:    */         }
/* 150:147 */         pstmt = conn.prepareStatement(sql);
/* 151:148 */         pstmt.setLong(1, this.ids[i].senid);
/* 152:149 */         pstmt.setTimestamp(2, new Timestamp(startTime.getTime()));
/* 153:150 */         pstmt.setTimestamp(3, new Timestamp(endTime.getTime()));
/* 154:151 */         rs = pstmt.executeQuery();
/* 155:    */         
/* 156:153 */         int count = 0;
/* 157:154 */         if (rs.next()) {
/* 158:155 */           count = rs.getInt(1);
/* 159:    */         }
/* 160:157 */         if (count == 0)
/* 161:    */         {
/* 162:158 */           this.log.debug(this.name + "(" + this.ids[i].senid + ")" + Type + "无");
/* 163:159 */           boolean find = false;
/* 164:160 */           for (int j = 0; j < ar.abnormalIds.size(); j++) {
/* 165:161 */             if (((Long)ar.abnormalIds.get(j)).longValue() == this.ids[i].senid)
/* 166:    */             {
/* 167:162 */               find = true;
/* 168:163 */               break;
/* 169:    */             }
/* 170:    */           }
/* 171:165 */           if ((!find) || (alarmAll))
/* 172:    */           {
/* 173:167 */             errorStr.append(this.ids[i].name + ",");
/* 174:168 */             findError = true;
/* 175:    */           }
/* 176:170 */           if (!find) {
/* 177:171 */             ar.abnormalIds.add(Long.valueOf(this.ids[i].senid));
/* 178:    */           }
/* 179:    */         }
/* 180:    */         else
/* 181:    */         {
/* 182:174 */           this.log.debug(this.name + "(" + this.ids[i].senid + ")" + Type + "有");
/* 183:175 */           boolean find = false;
                        Iterator<Long> it;
/* 184:177 */           for ( it = ar.abnormalIds.iterator(); it.hasNext();)
/* 185:    */           {
/* 186:178 */             Long one = (Long)it.next();
/* 187:179 */             if (one.longValue() == this.ids[i].senid)
/* 188:    */             {
/* 189:181 */               find = true;
/* 190:182 */               break;
/* 191:    */             }
/* 192:    */           }
/* 193:185 */           if (find)
/* 194:    */           {
/* 195:187 */             it.remove();
/* 196:188 */             recoverStr.append(this.ids[i].name + ",");
/* 197:189 */             findRecover = true;
/* 198:    */           }
/* 199:    */         }
/* 200:193 */         rs.close();
/* 201:194 */         rs = null;
/* 202:195 */         pstmt.close();
/* 203:196 */         pstmt = null;
/* 204:    */       }
/* 205:    */       catch (Exception e)
/* 206:    */       {
/* 207:199 */         if (rs != null) {
/* 208:    */           try
/* 209:    */           {
/* 210:201 */             rs.close();
/* 211:    */           }
/* 212:    */           catch (Exception e1)
/* 213:    */           {
/* 214:203 */             e1.printStackTrace();
/* 215:    */           }
/* 216:    */         }
/* 217:205 */         if (pstmt != null) {
/* 218:    */           try
/* 219:    */           {
/* 220:207 */             pstmt.close();
/* 221:    */           }
/* 222:    */           catch (Exception e1)
/* 223:    */           {
/* 224:209 */             e1.printStackTrace();
/* 225:    */           }
/* 226:    */         }
/* 227:211 */         dealDBException(e, db);
/* 228:    */       }
/* 229:    */     }
/* 230:213 */     ar.checkTime = cal.getTimeInMillis();
/* 231:214 */     if (findError)
/* 232:    */     {
/* 233:216 */       String s = errorStr.toString();
/* 234:217 */       s = s.substring(0, s.lastIndexOf(",")) + "缺数";
/* 235:218 */       sendToDB(cal.getTimeInMillis(), s, null);
/* 236:219 */       this.log.info(s);
/* 237:220 */       ar.recordTime = cal.getTimeInMillis();
/* 238:    */     }
/* 239:222 */     if (findRecover)
/* 240:    */     {
/* 241:224 */       String s = recoverStr.toString();
/* 242:225 */       s = s.substring(0, s.lastIndexOf(",")) + "恢复";
/* 243:226 */       if (findError) {
/* 244:227 */         sendToDB(cal.getTimeInMillis() + 1000L, s, null);
/* 245:    */       } else {
/* 246:229 */         sendToDB(cal.getTimeInMillis(), s, null);
/* 247:    */       }
/* 248:230 */       this.log.info(s);
/* 249:231 */       ar.recordTime = cal.getTimeInMillis();
/* 250:    */     }
/* 251:233 */     this.log.debug("queshu check " + this.name + " end!");
/* 252:    */   }
/* 253:    */   
/* 254:    */   public boolean getConfig(byte[] bs)
/* 255:    */   {
/* 256:238 */     Document doc = null;
/* 257:239 */     SAXReader saxReader = new SAXReader();
/* 258:    */     try
/* 259:    */     {
/* 260:241 */       doc = saxReader.read(new ByteArrayInputStream(bs));
/* 261:    */       
/* 262:243 */       Element root = doc.getRootElement();
/* 263:244 */       String senids = root.element("senid").getStringValue();
/* 264:245 */       String[] idString = senids.split(",");
/* 265:246 */       long[] x = new long[idString.length];
/* 266:247 */       int c = 0;
/* 267:248 */       for (int i = 0; i < idString.length; i++) {
/* 268:    */         try
/* 269:    */         {
/* 270:250 */           boolean find = false;
/* 271:251 */           long ii = Long.parseLong(idString[i]);
/* 272:252 */           if (ii > 0L)
/* 273:    */           {
/* 274:254 */             for (int j = 0; j < c; j++) {
/* 275:256 */               if (x[j] == ii)
/* 276:    */               {
/* 277:257 */                 find = true;
/* 278:258 */                 break;
/* 279:    */               }
/* 280:    */             }
/* 281:261 */             if (!find)
/* 282:    */             {
/* 283:262 */               x[c] = ii;
/* 284:263 */               c++;
/* 285:    */             }
/* 286:    */           }
/* 287:    */         }
/* 288:    */         catch (Exception e)
/* 289:    */         {
/* 290:266 */           this.log.info(e.toString());
/* 291:    */         }
/* 292:    */       }
/* 293:270 */       this.ids = new IdDefine[c];
/* 294:271 */       for (int i = 0; i < c; i++)
/* 295:    */       {
/* 296:273 */         this.ids[i] = new IdDefine();
/* 297:274 */         this.ids[i].senid = x[i];
/* 298:    */       }
/* 299:276 */       this.interval = Integer.parseInt(
/* 300:277 */         root.element("interval").getStringValue());
/* 301:278 */       this.timeSpan = Integer.parseInt(
/* 302:279 */         root.element("timespan").getStringValue());
/* 303:280 */       this.dataType = Integer.parseInt(
/* 304:281 */         root.element("datatype").getStringValue());
/* 305:282 */       return true;
/* 306:    */     }
/* 307:    */     catch (Exception e)
/* 308:    */     {
/* 309:284 */       this.log.info(e.toString());
/* 310:285 */       e.printStackTrace();
/* 311:    */     }
/* 312:286 */     return false;
/* 313:    */   }
/* 314:    */   
/* 315:    */   public void setName(List lists)
/* 316:    */   {
/* 317:293 */     for (int i = 0; i < this.ids.length; i++)
/* 318:    */     {
/* 319:294 */       for (Iterator it = lists.iterator(); it.hasNext();)
/* 320:    */       {
/* 321:295 */         Object one = it.next();
/* 322:296 */         if (one.getClass().getName().equals(
/* 323:297 */           "com.nari.slsd.hd.model.WdsHydroElements"))
/* 324:    */         {
/* 325:298 */           WdsHydroElements mea = (WdsHydroElements)one;
/* 326:300 */           if (mea.getId().longValue() == this.ids[i].senid)
/* 327:    */           {
/* 328:301 */             this.ids[i].name = mea.getName();
/* 329:302 */             switch (mea.getRtdb().intValue())
/* 330:    */             {
/* 331:    */             case 1: 
/* 332:304 */               this.ids[i].table = "rtdb";
/* 333:305 */               break;
/* 334:    */             case 2: 
/* 335:307 */               this.ids[i].table = "rtsq";
/* 336:308 */               break;
/* 337:    */             case 3: 
/* 338:310 */               this.ids[i].table = "rtems";
/* 339:311 */               break;
/* 340:    */             case 4: 
/* 341:313 */               this.ids[i].table = "rtcalc";
/* 342:314 */               break;
/* 343:    */             default: 
/* 344:316 */               this.ids[i].table = "rtsq";
/* 345:    */               
/* 346:318 */               break;
/* 347:    */             }
/* 348:    */           }
/* 349:    */         }
/* 350:    */       }
/* 351:322 */       if (this.ids[i].table == null) {
/* 352:323 */         this.ids[i].table = "rtsq";
/* 353:    */       }
/* 354:324 */       if (this.ids[i].name == null) {
/* 355:325 */         this.ids[i].name = String.valueOf(this.ids[i].senid);
/* 356:    */       }
/* 357:    */     }
/* 358:    */   }
/* 359:    */   
/* 360:    */   public void check(Calendar c, IWdsHisDataService wdsService) {}
/* 361:    */ }