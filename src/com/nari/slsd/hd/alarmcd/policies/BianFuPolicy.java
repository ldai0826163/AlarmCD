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
/*  15:    */ import java.util.ArrayList;
/*  16:    */ import java.util.Calendar;
/*  17:    */ import java.util.Date;
/*  18:    */ import java.util.Iterator;
/*  19:    */ import java.util.List;
/*  20:    */ import org.dom4j.Document;
/*  21:    */ import org.dom4j.Element;
/*  22:    */ import org.dom4j.io.SAXReader;
/*  23:    */ 
/*  24:    */ public class BianFuPolicy
/*  25:    */   extends Policy
/*  26:    */ {
/*  27:    */   long senid;
/*  28:    */   int dataType;
/*  29:    */   
/*  30:    */   public long getSenid()
/*  31:    */   {
/*  32: 34 */     return this.senid;
/*  33:    */   }
/*  34:    */   
/*  35:    */   public void setSenid(long senid)
/*  36:    */   {
/*  37: 38 */     this.senid = senid;
/*  38:    */   }
/*  39:    */   
/*  40:    */   public int getDataType()
/*  41:    */   {
/*  42: 42 */     return this.dataType;
/*  43:    */   }
/*  44:    */   
/*  45:    */   public void setDataType(int dataType)
/*  46:    */   {
/*  47: 46 */     this.dataType = dataType;
/*  48:    */   }
/*  49:    */   
/*  50:    */   public int getTimeSpan()
/*  51:    */   {
/*  52: 50 */     return this.timeSpan;
/*  53:    */   }
/*  54:    */   
/*  55:    */   public void setTimeSpan(int timeSpan)
/*  56:    */   {
/*  57: 54 */     this.timeSpan = timeSpan;
/*  58:    */   }
/*  59:    */   
/*  60: 58 */   int timeSpan = 10;
/*  61: 60 */   double rangeLimit = 0.0D;
/*  62:    */   
/*  63:    */   public double getRangeLimit()
/*  64:    */   {
/*  65: 63 */     return this.rangeLimit;
/*  66:    */   }
/*  67:    */   
/*  68:    */   public void setRangeLimit(double rangeLimit)
/*  69:    */   {
/*  70: 67 */     this.rangeLimit = rangeLimit;
/*  71:    */   }
/*  72:    */   
/*  73: 71 */   String table = "rtev";
/*  74:    */   
/*  75:    */   public BianFuPolicy()
/*  76:    */   {
/*  77: 74 */     this.type = 12;
/*  78:    */   }
/*  79:    */   
/*  80:    */   public void check(Calendar cal, SingleDataBase db)
/*  81:    */   {
/*  82: 78 */     this.log.debug("bianfu check " + this.dataType + " " + this.senid + " begin!");
/*  83: 79 */     AlarmRecord ar = AlarmRecordManager.getRecord(this.id);
/*  84: 80 */     if (ar.recordTime > cal.getTimeInMillis()) {
/*  85: 81 */       return;
/*  86:    */     }
/*  87: 82 */     Connection conn = db.conn;
/*  88: 83 */     Date endTime = new Date(cal.getTimeInMillis());
/*  89: 84 */     Date startTime = null;
/*  90:    */     String sql;
/*  92: 86 */     if (this.dataType == 12) {
/*  93: 88 */       sql = "SELECT v,time from hourdb WHERE SENID=? AND TIME >= ? AND TIME <? order by time";
/*  94:    */     } else {
/*  95: 92 */       sql = "SELECT factv,time from " + this.table + " WHERE SENID=? AND TIME >= ? AND TIME <=? order by time";
/*  96:    */     }
/*  97: 94 */     if (this.dataType == 1)
/*  98:    */     {
/*  99: 97 */       startTime = new Date(cal.getTimeInMillis() - this.timeSpan * 60 * 1000);
/* 100:    */     }
/* 101: 98 */     else if ((this.dataType == 102) || (this.dataType == 12))
/* 102:    */     {
/* 103:101 */       startTime = new Date(cal.getTimeInMillis() - this.timeSpan * 60 * 1000);
/* 104:    */     }
/* 105:102 */     else if (this.dataType == 288)
/* 106:    */     {
/* 107:104 */       Calendar day = Calendar.getInstance();
/* 108:105 */       day.setTimeInMillis(cal.getTimeInMillis());
/* 109:106 */       day.set(11, 0);
/* 110:107 */       day.set(12, 0);
/* 111:108 */       day.set(13, 0);
/* 112:109 */       day.set(14, 0);
/* 113:110 */       startTime = new Date(day.getTimeInMillis());
/* 114:    */     }
/* 115:    */     else
/* 116:    */     {
/* 117:112 */       this.log.error("变幅报警" + this.id + "Unknow DataType");
/* 118:113 */       return;
/* 119:    */     }
/* 120:115 */     PreparedStatement pstmt = null;
/* 121:116 */     ResultSet rs = null;
/* 122:    */     try
/* 123:    */     {
/* 124:118 */       pstmt = conn.prepareStatement(sql);
/* 125:119 */       pstmt.setLong(1, this.senid);
/* 126:120 */       pstmt.setTimestamp(2, new Timestamp(startTime.getTime()));
/* 127:121 */       pstmt.setTimestamp(3, new Timestamp(endTime.getTime()));
/* 128:122 */       rs = pstmt.executeQuery();
/* 129:123 */       ArrayList<RTEV> datas = new ArrayList();
/* 130:124 */       while (rs.next()) {
/* 131:125 */         datas.add(new RTEV(this.senid, rs.getTimestamp(2).getTime(), rs.getDouble(1)));
/* 132:    */       }
/* 133:127 */       if (datas.size() < 2)
/* 134:    */       {
/* 135:129 */         ar.checkTime = cal.getTimeInMillis();
/* 136:    */         try
/* 137:    */         {
/* 138:132 */           rs.close();
/* 139:    */         }
/* 140:    */         catch (Exception e1)
/* 141:    */         {
/* 142:136 */           e1.printStackTrace();
/* 143:    */         }
/* 144:    */         try
/* 145:    */         {
/* 146:140 */           pstmt.close();
/* 147:    */         }
/* 148:    */         catch (Exception e1)
/* 149:    */         {
/* 150:143 */           e1.printStackTrace();
/* 151:    */         }
/* 152:145 */         this.log.debug("bianfu check " + this.dataType + " " + this.senid + " no data end!");
/* 153:146 */         return;
/* 154:    */       }
/* 155:148 */       switch (this.dataType)
/* 156:    */       {
/* 157:    */       case 1: 
/* 158:151 */         checkReal(datas, ar);
/* 159:152 */         break;
/* 160:    */       case 102: 
/* 161:154 */         checkSpan(datas, ar, cal);
/* 162:155 */         break;
/* 163:    */       case 12: 
/* 164:157 */         checkSpan(datas, ar, cal);
/* 165:158 */         break;
/* 166:    */       case 288: 
/* 167:160 */         checkSpan(datas, ar, cal);
/* 168:    */       }
/* 169:162 */       ar.checkTime = cal.getTimeInMillis();
/* 170:    */       try
/* 171:    */       {
/* 172:165 */         rs.close();
/* 173:166 */         rs = null;
/* 174:    */       }
/* 175:    */       catch (Exception e1)
/* 176:    */       {
/* 177:170 */         e1.printStackTrace();
/* 178:    */       }
/* 179:    */       try
/* 180:    */       {
/* 181:174 */         pstmt.close();
/* 182:    */       }
/* 183:    */       catch (Exception e1)
/* 184:    */       {
/* 185:177 */         e1.printStackTrace();
/* 186:    */       }
/* 187:179 */       this.log.debug("bianfu check " + this.dataType + " " + this.senid + " end!");
/* 188:    */     }
/* 189:    */     catch (Exception e)
/* 190:    */     {
/* 191:182 */       if (rs != null) {
/* 192:    */         try
/* 193:    */         {
/* 194:184 */           rs.close();
/* 195:    */         }
/* 196:    */         catch (Exception e1)
/* 197:    */         {
/* 198:186 */           e1.printStackTrace();
/* 199:    */         }
/* 200:    */       }
/* 201:188 */       if (pstmt != null) {
/* 202:    */         try
/* 203:    */         {
/* 204:190 */           pstmt.close();
/* 205:    */         }
/* 206:    */         catch (Exception e1)
/* 207:    */         {
/* 208:192 */           e1.printStackTrace();
/* 209:    */         }
/* 210:    */       }
/* 211:194 */       dealDBException(e, db);
/* 212:    */     }
/* 213:    */   }
/* 214:    */   
/* 215:    */   private void checkSpan(ArrayList<RTEV> datas, AlarmRecord ar, Calendar cal)
/* 216:    */   {
/* 217:200 */     SimpleDateFormat sdf = null;
/* 218:    */     
/* 219:202 */     String typeName = null;
/* 220:203 */     RTEV rmax = new RTEV(this.senid, ((RTEV)datas.get(0)).t, ((RTEV)datas.get(0)).factv.doubleValue());
/* 221:204 */     RTEV rmin = new RTEV(this.senid, ((RTEV)datas.get(0)).t, ((RTEV)datas.get(0)).factv.doubleValue());
/* 222:205 */     switch (this.dataType)
/* 223:    */     {
/* 224:    */     case 12: 
/* 225:208 */       sdf = new SimpleDateFormat("d日HH时");
/* 226:209 */       typeName = this.timeSpan / 60 - 1 + "小时";
/* 227:210 */       break;
/* 228:    */     case 102: 
/* 229:212 */       sdf = new SimpleDateFormat("d日HH时mm分");
/* 230:213 */       typeName = this.timeSpan + "分钟";
/* 231:214 */       break;
/* 232:    */     case 288: 
/* 233:216 */       sdf = new SimpleDateFormat("d日HH时mm分");
/* 234:217 */       typeName = "日";
/* 235:    */     }
/* 236:220 */     for (int i = 0; i < datas.size(); i++)
/* 237:    */     {
/* 238:222 */       RTEV one = (RTEV)datas.get(i);
/* 239:223 */       if (one.factv.doubleValue() > rmax.factv.doubleValue())
/* 240:    */       {
/* 241:225 */         rmax.factv = one.factv;
/* 242:226 */         rmax.t = one.t;
/* 243:    */       }
/* 244:228 */       if (one.factv.doubleValue() < rmin.factv.doubleValue())
/* 245:    */       {
/* 246:230 */         rmin.factv = one.factv;
/* 247:231 */         rmin.t = one.t;
/* 248:    */       }
/* 249:    */     }
/* 250:234 */     if (Math.round((rmax.factv.doubleValue() - rmin.factv.doubleValue()) * 10000.0D) >= Math.round(this.rangeLimit * 10000.0D))
/* 251:    */     {
/* 252:236 */       if ((!ar.abnormal) || (cal.getTimeInMillis() - ar.recordTime >= 3600000L))
/* 253:    */       {
/* 254:239 */         String msg = this.name + sdf.format(Long.valueOf(rmax.t)) + "最大值为" + rmax.factv + "," + sdf.format(Long.valueOf(rmin.t)) + "最小值为" + rmin.factv + "," + typeName + "数值变幅超过" + this.rangeLimit + "报警";
/* 255:240 */         this.log.info(msg);
/* 256:241 */         String detail = this.type + "&" + this.senid + "&" + cal.getTimeInMillis() + "&" + 1 + "&" + msg;
/* 257:242 */         sendToDB(cal.getTimeInMillis(), msg, detail);
/* 258:243 */         ar.abnormal = true;
/* 259:244 */         ar.recordTime = cal.getTimeInMillis();
/* 260:    */       }
/* 261:    */     }
/* 262:249 */     else if (ar.abnormal)
/* 263:    */     {
/* 264:251 */       String msg = sdf.format(cal.getTime()) + this.name + typeName + "数值变幅恢复正常";
/* 265:252 */       this.log.info(msg);
/* 266:253 */       String detail = this.type + "&" + this.senid + "&" + cal.getTimeInMillis() + "&" + 0 + "&" + msg;
/* 267:254 */       sendToDB(cal.getTimeInMillis(), msg, detail);
/* 268:255 */       ar.abnormal = false;
/* 269:256 */       ar.recordTime = cal.getTimeInMillis();
/* 270:    */     }
/* 271:    */   }
/* 272:    */   
/* 273:    */   private void checkReal(ArrayList<RTEV> datas, AlarmRecord ar)
/* 274:    */   {
/* 275:264 */     SimpleDateFormat sdf = new SimpleDateFormat("M月d日HH时mm分");
/* 276:266 */     for (int i = 0; i < datas.size() - 1; i++)
/* 277:    */     {
/* 278:268 */       RTEV one = (RTEV)datas.get(i);
/* 279:269 */       RTEV two = (RTEV)datas.get(i + 1);
/* 280:270 */       if (Math.abs(Math.round((one.factv.doubleValue() - two.factv.doubleValue()) * 10000.0D)) >= Math.round(this.rangeLimit * 10000.0D))
/* 281:    */       {
/* 282:272 */         ar.abnormal = true;
/* 283:273 */         if (ar.recordTime < ((RTEV)datas.get(i + 1)).t)
/* 284:    */         {
/* 285:275 */           ar.recordTime = ((RTEV)datas.get(i + 1)).t;
/* 286:276 */           String msg = this.name + sdf.format(Long.valueOf(one.t)) + "值为" + one.factv + "," + sdf.format(Long.valueOf(two.t)) + "值为" + two.factv + " " + "连续两个数值变幅超过" + this.rangeLimit + "报警";
/* 287:277 */           this.log.info(msg);
/* 288:278 */           String detail = this.type + "&" + this.senid + "&" + two.t + "&" + 1 + "&" + msg;
/* 289:279 */           sendToDB(two.t, msg, detail);
/* 290:    */         }
/* 291:    */       }
/* 292:    */       else
/* 293:    */       {
/* 294:285 */         ar.abnormal = false;
/* 295:    */       }
/* 296:    */     }
/* 297:    */   }
/* 298:    */   
/* 299:    */   public boolean getConfig(byte[] bs)
/* 300:    */   {
/* 301:292 */     Document doc = null;
/* 302:293 */     SAXReader saxReader = new SAXReader();
/* 303:    */     try
/* 304:    */     {
/* 305:295 */       doc = saxReader.read(new ByteArrayInputStream(bs));
/* 306:296 */       Element root = doc.getRootElement();
/* 307:297 */       this.senid = Long.parseLong(root.element("senid").getStringValue());
/* 308:298 */       if (this.senid < 0L)
/* 309:    */       {
/* 310:300 */         this.log.info(this.id + "非法数据点号");
/* 311:301 */         return false;
/* 312:    */       }
/* 313:303 */       this.interval = Integer.parseInt(
/* 314:304 */         root.element("interval").getStringValue());
/* 315:305 */       this.timeSpan = Integer.parseInt(
/* 316:306 */         root.element("timespan").getStringValue());
/* 317:307 */       this.dataType = Integer.parseInt(
/* 318:308 */         root.element("datatype").getStringValue());
/* 319:309 */       this.rangeLimit = Double.parseDouble(
/* 320:310 */         root.element("range").getStringValue());
/* 321:311 */       return true;
/* 322:    */     }
/* 323:    */     catch (Exception e)
/* 324:    */     {
/* 325:313 */       this.log.info(e.toString());
/* 326:314 */       e.printStackTrace();
/* 327:    */     }
/* 328:315 */     return false;
/* 329:    */   }
/* 330:    */   
/* 331:    */   public void setName(List lists)
/* 332:    */   {
/* 333:322 */     for (Iterator it = lists.iterator(); it.hasNext();)
/* 334:    */     {
/* 335:324 */       Object one = it.next();
/* 336:325 */       if (one.getClass().getName().equals("com.nari.slsd.hd.model.WdsHydroElements"))
/* 337:    */       {
/* 338:327 */         WdsHydroElements mea = (WdsHydroElements)one;
/* 339:328 */         if (mea.getId().longValue() == this.senid)
/* 340:    */         {
/* 341:330 */           this.name = mea.getName();
/* 342:331 */           switch (mea.getRtdb().intValue())
/* 343:    */           {
/* 344:    */           case 1: 
/* 345:334 */             this.table = "rtdb";
/* 346:335 */             break;
/* 347:    */           case 2: 
/* 348:337 */             this.table = "rtsq";
/* 349:338 */             break;
/* 350:    */           case 3: 
/* 351:340 */             this.table = "rtems";
/* 352:341 */             break;
/* 353:    */           case 4: 
/* 354:343 */             this.table = "rtcalc";
/* 355:    */           }
/* 356:346 */           return;
/* 357:    */         }
/* 358:    */       }
/* 359:    */     }
/* 360:351 */     this.name = ("测站" + String.valueOf(this.senid));
/* 361:    */   }
/* 362:    */   
/* 363:    */   public void check(Calendar c, IWdsHisDataService wdsService) {}
/* 364:    */ }


/* Location:           C:\Users\lauren\Desktop\HD.AlarmCD.jar
 * Qualified Name:     com.nari.slsd.hd.alarmcd.policies.BianFuPolicy
 * JD-Core Version:    0.7.0.1
 */