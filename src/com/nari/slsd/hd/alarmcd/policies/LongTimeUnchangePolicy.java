/*   1:    */ package com.nari.slsd.hd.alarmcd.policies;
/*   2:    */ 
/*   3:    */ import com.nari.slsd.hd.alarmcd.UserLog;
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
/*  24:    */ public class LongTimeUnchangePolicy
/*  25:    */   extends Policy
/*  26:    */ {
/*  27:    */   long senid;
/*  28:    */   int startMin;
/*  29:    */   int timeSpan;
/*  30:    */   long checkSpan;
/*  31:    */   
/*  32:    */   public long getSenid()
/*  33:    */   {
/*  34: 42 */     return this.senid;
/*  35:    */   }
/*  36:    */   
/*  37:    */   public void setSenid(long senid)
/*  38:    */   {
/*  39: 46 */     this.senid = senid;
/*  40:    */   }
/*  41:    */   
/*  42:    */   public int getTimeSpan()
/*  43:    */   {
/*  44: 50 */     return this.timeSpan;
/*  45:    */   }
/*  46:    */   
/*  47:    */   public void setTimeSpan(int timeSpan)
/*  48:    */   {
/*  49: 54 */     this.timeSpan = timeSpan;
/*  50:    */   }
/*  51:    */   
/*  52:    */   public int getStartMin()
/*  53:    */   {
/*  54: 57 */     return this.startMin;
/*  55:    */   }
/*  56:    */   
/*  57:    */   public void setStartMin(int startMin)
/*  58:    */   {
/*  59: 61 */     this.startMin = startMin;
/*  60:    */   }
/*  61:    */   
/*  62:    */   public long getCheckSpan()
/*  63:    */   {
/*  64: 65 */     return this.checkSpan;
/*  65:    */   }
/*  66:    */   
/*  67:    */   public void setCheckSpan(long checkSpan)
/*  68:    */   {
/*  69: 69 */     this.checkSpan = checkSpan;
/*  70:    */   }
/*  71:    */   
/*  72: 75 */   double range = 0.0D;
/*  73:    */   
/*  74:    */   public double getRange()
/*  75:    */   {
/*  76: 77 */     return this.range;
/*  77:    */   }
/*  78:    */   
/*  79:    */   public void setRange(double range)
/*  80:    */   {
/*  81: 81 */     this.range = range;
/*  82:    */   }
/*  83:    */   
/*  84: 83 */   String table = "rtev";
/*  85:    */   
/*  86:    */   public ArrayList<Policy> needCheck(Calendar c, Policy[] ps)
/*  87:    */   {
/*  88: 86 */     ArrayList<Policy> rps = new ArrayList();
/*  89: 87 */     for (int i = 0; i < ps.length; i++)
/*  90:    */     {
/*  91: 88 */       Policy one = ps[i];
/*  92: 89 */       if (one.type == this.type)
/*  93:    */       {
/*  94: 91 */         LongTimeUnchangePolicy lp = (LongTimeUnchangePolicy)one;
/*  95:    */         
/*  96: 93 */         AlarmRecord ar = AlarmRecordManager.getRecord(one.id);
/*  97: 95 */         if (c.get(11) * 60 + c.get(12) - lp.startMin >= 0) {
/*  98: 97 */           if (c.getTimeInMillis() - ar.checkTime >= 60000 * lp.interval) {
/*  99: 98 */             rps.add(one);
/* 100:    */           }
/* 101:    */         }
/* 102:    */       }
/* 103:    */     }
/* 104:102 */     if (rps.size() == 0) {
/* 105:103 */       return null;
/* 106:    */     }
/* 107:104 */     return rps;
/* 108:    */   }
/* 109:    */   
/* 110:    */   public LongTimeUnchangePolicy()
/* 111:    */   {
/* 112:108 */     this.type = 17;
/* 113:    */   }
/* 114:    */   
/* 115:    */   public void check(Calendar cal, SingleDataBase db)
/* 116:    */   {
/* 117:112 */     this.log.debug("LongTime unchange check " + this.senid + " begin!");
/* 118:113 */     AlarmRecord ar = AlarmRecordManager.getRecord(this.id);
/* 119:114 */     if (ar.checkTime > cal.getTimeInMillis()) {
/* 120:115 */       return;
/* 121:    */     }
/* 122:116 */     Connection conn = db.conn;
/* 123:117 */     Calendar c = Calendar.getInstance();
/* 124:118 */     c.setTimeInMillis(cal.getTimeInMillis());
/* 125:119 */     c.set(13, 0);
/* 126:120 */     c.set(14, 0);
/* 127:121 */     c.setTimeInMillis(c.getTimeInMillis() - (c.get(11) * 60 + c.get(12) - this.startMin) % this.interval * 60000L);
/* 128:    */     
/* 129:123 */     Date endTime = new Date(c.getTimeInMillis());
/* 130:124 */     Date startTime = new Date(c.getTimeInMillis() - this.checkSpan * 60000L);
/* 131:125 */     String sql = "SELECT factv,time from " + this.table + " WHERE senid=? AND TIME >= ? AND TIME <=? order by time";
/* 132:126 */     SimpleDateFormat sdf = new SimpleDateFormat("d日HH时mm分ss秒");
/* 133:127 */     PreparedStatement pstmt = null;
/* 134:128 */     ResultSet rs = null;
/* 135:    */     try
/* 136:    */     {
/* 137:130 */       pstmt = conn.prepareStatement(sql);
/* 138:131 */       pstmt.setLong(1, this.senid);
/* 139:132 */       pstmt.setTimestamp(2, new Timestamp(startTime.getTime()));
/* 140:133 */       pstmt.setTimestamp(3, new Timestamp(endTime.getTime()));
/* 141:134 */       rs = pstmt.executeQuery();
/* 142:135 */       ArrayList<RTEV> datas = new ArrayList();
/* 143:136 */       while (rs.next()) {
/* 144:137 */         datas.add(new RTEV(this.senid, rs.getTimestamp(2).getTime(), 
/* 145:138 */           rs.getDouble(1)));
/* 146:    */       }
/* 147:139 */       int i = 0;
/* 148:140 */       boolean find = false;
/* 149:141 */       String msg = "";
/* 150:142 */       String detail = null;
/* 151:143 */       while (i < datas.size() - 1)
/* 152:    */       {
/* 153:144 */         RTEV two = null;RTEV one = (RTEV)datas.get(i);
/* 154:145 */         int j = i + 1;
/* 155:146 */         while (j < datas.size())
/* 156:    */         {
/* 157:147 */           two = (RTEV)datas.get(j);
/* 158:149 */           if (Math.round(Math.abs(two.factv.doubleValue() - one.factv.doubleValue()) * 10000.0D) > Math.round(this.range * 10000.0D))
/* 159:    */           {
/* 160:152 */             if (two.t - one.t > this.timeSpan * 60000)
/* 161:    */             {
/* 162:154 */               msg = 
/* 163:    */               
/* 164:156 */                 msg + this.name + "从" + sdf.format(new Date(one.t)) + "到" + sdf.format(new Date(two.t)) + "之间数据没有变化,";
/* 165:158 */               if (detail == null) {
/* 166:159 */                 detail = 
/* 167:    */                 
/* 168:    */ 
/* 169:162 */                   this.type + "&" + this.senid + "&" + two.t + "&" + 1 + "&";
/* 170:    */               }
/* 171:164 */               i = j;
/* 172:165 */               find = true; break;
/* 173:    */             }
/* 174:167 */             i++;
/* 175:    */             
/* 176:169 */             break;
/* 177:    */           }
/* 178:171 */           j++;
/* 179:    */         }
/* 180:174 */         if ((j >= datas.size()) && (two != null))
/* 181:    */         {
/* 182:175 */           if (two.t - one.t <= this.timeSpan * 60000) {
/* 183:    */             break;
/* 184:    */           }
/* 185:176 */           msg = 
/* 186:    */           
/* 187:178 */             msg + this.name + "从" + sdf.format(new Date(one.t)) + "到" + sdf.format(new Date(two.t)) + "之间数据没有变化,";
/* 188:180 */           if (detail == null) {
/* 189:181 */             detail = 
/* 190:    */             
/* 191:    */ 
/* 192:184 */               this.type + "&" + this.senid + "&" + two.t + "&" + 1 + "&";
/* 193:    */           }
/* 194:185 */           find = true;
/* 195:    */           
/* 196:    */ 
/* 197:    */ 
/* 198:189 */           break;
/* 199:    */         }
/* 200:    */       }
/* 201:192 */       if (find)
/* 202:    */       {
/* 203:193 */         ar.recordTime = c.getTimeInMillis();
/* 204:194 */         this.log.info(msg);
/* 205:195 */         sendToDB(cal.getTimeInMillis(), msg, detail);
/* 206:    */       }
/* 207:197 */       ar.checkTime = c.getTimeInMillis();
/* 208:    */       try
/* 209:    */       {
/* 210:199 */         rs.close();
/* 211:200 */         rs = null;
/* 212:    */       }
/* 213:    */       catch (Exception e1)
/* 214:    */       {
/* 215:202 */         e1.printStackTrace();
/* 216:    */       }
/* 217:    */       try
/* 218:    */       {
/* 219:205 */         pstmt.close();
/* 220:    */       }
/* 221:    */       catch (Exception e1)
/* 222:    */       {
/* 223:207 */         e1.printStackTrace();
/* 224:    */       }
/* 225:209 */       this.log.debug("LongTime unchange check " + this.senid + " end!");
/* 226:    */     }
/* 227:    */     catch (Exception e)
/* 228:    */     {
/* 229:211 */       if (rs != null) {
/* 230:    */         try
/* 231:    */         {
/* 232:213 */           rs.close();
/* 233:    */         }
/* 234:    */         catch (Exception e1)
/* 235:    */         {
/* 236:215 */           e1.printStackTrace();
/* 237:    */         }
/* 238:    */       }
/* 239:217 */       if (pstmt != null) {
/* 240:    */         try
/* 241:    */         {
/* 242:219 */           pstmt.close();
/* 243:    */         }
/* 244:    */         catch (Exception e1)
/* 245:    */         {
/* 246:221 */           e1.printStackTrace();
/* 247:    */         }
/* 248:    */       }
/* 249:223 */       dealDBException(e, db);
/* 250:    */     }
/* 251:    */   }
/* 252:    */   
/* 253:    */   public boolean getConfig(byte[] bs)
/* 254:    */   {
/* 255:228 */     Document doc = null;
/* 256:229 */     SAXReader saxReader = new SAXReader();
/* 257:    */     try
/* 258:    */     {
/* 259:231 */       doc = saxReader.read(new ByteArrayInputStream(bs));
/* 260:232 */       Element root = doc.getRootElement();
/* 261:233 */       this.senid = Integer.parseInt(root.element("senid").getStringValue());
/* 262:234 */       if (this.senid < 0L)
/* 263:    */       {
/* 264:236 */         this.log.info(this.id + "非法数据点号");
/* 265:237 */         return false;
/* 266:    */       }
/* 267:239 */       this.interval = Integer.parseInt(
/* 268:240 */         root.element("interval").getStringValue());
/* 269:241 */       this.timeSpan = Integer.parseInt(
/* 270:242 */         root.element("timespan").getStringValue());
/* 271:243 */       this.startMin = Integer.parseInt(
/* 272:244 */         root.element("startmin").getStringValue());
/* 273:245 */       this.checkSpan = Integer.parseInt(
/* 274:246 */         root.element("checkspan").getStringValue());
/* 275:247 */       this.range = Double.parseDouble(
/* 276:248 */         root.element("range").getStringValue());
/* 277:249 */       return true;
/* 278:    */     }
/* 279:    */     catch (Exception e)
/* 280:    */     {
/* 281:251 */       this.log.info(e.toString());
/* 282:252 */       e.printStackTrace();
/* 283:    */     }
/* 284:253 */     return false;
/* 285:    */   }
/* 286:    */   
/* 287:    */   public void setName(List lists)
/* 288:    */   {
/* 289:260 */     for (Iterator it = lists.iterator(); it.hasNext();)
/* 290:    */     {
/* 291:262 */       Object one = it.next();
/* 292:263 */       if (one.getClass().getName().equals("com.nari.slsd.hd.model.WdsHydroElements"))
/* 293:    */       {
/* 294:265 */         WdsHydroElements mea = (WdsHydroElements)one;
/* 295:266 */         if (mea.getId().longValue() == this.senid)
/* 296:    */         {
/* 297:268 */           this.name = mea.getName();
/* 298:269 */           switch (mea.getRtdb().intValue())
/* 299:    */           {
/* 300:    */           case 1: 
/* 301:272 */             this.table = "rtdb";
/* 302:273 */             break;
/* 303:    */           case 2: 
/* 304:275 */             this.table = "rtsq";
/* 305:276 */             break;
/* 306:    */           case 3: 
/* 307:278 */             this.table = "rtems";
/* 308:279 */             break;
/* 309:    */           case 4: 
/* 310:281 */             this.table = "rtcalc";
/* 311:    */           }
/* 312:284 */           return;
/* 313:    */         }
/* 314:    */       }
/* 315:    */     }
/* 316:289 */     this.name = ("测站" + String.valueOf(this.senid));
/* 317:    */   }
/* 318:    */   
/* 319:    */   public void check(Calendar c, IWdsHisDataService wdsService) {}
/* 320:    */ }


/* Location:           C:\Users\lauren\Desktop\HD.AlarmCD.jar
 * Qualified Name:     com.nari.slsd.hd.alarmcd.policies.LongTimeUnchangePolicy
 * JD-Core Version:    0.7.0.1
 */