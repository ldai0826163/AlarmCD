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
/*  24:    */ public class LongTimeQueShuPolicy
/*  25:    */   extends Policy
/*  26:    */ {
/*  27:    */   long senid;
/*  28:    */   int startMin;
/*  29:    */   int timeSpan;
/*  30:    */   long checkSpan;
/*  31:    */   
/*  32:    */   public long getSenid()
/*  33:    */   {
/*  34: 31 */     return this.senid;
/*  35:    */   }
/*  36:    */   
/*  37:    */   public void setSenid(long senid)
/*  38:    */   {
/*  39: 35 */     this.senid = senid;
/*  40:    */   }
/*  41:    */   
/*  42:    */   public int getTimeSpan()
/*  43:    */   {
/*  44: 39 */     return this.timeSpan;
/*  45:    */   }
/*  46:    */   
/*  47:    */   public void setTimeSpan(int timeSpan)
/*  48:    */   {
/*  49: 43 */     this.timeSpan = timeSpan;
/*  50:    */   }
/*  51:    */   
/*  52:    */   public int getStartMin()
/*  53:    */   {
/*  54: 46 */     return this.startMin;
/*  55:    */   }
/*  56:    */   
/*  57:    */   public void setStartMin(int startMin)
/*  58:    */   {
/*  59: 50 */     this.startMin = startMin;
/*  60:    */   }
/*  61:    */   
/*  62:    */   public long getCheckSpan()
/*  63:    */   {
/*  64: 54 */     return this.checkSpan;
/*  65:    */   }
/*  66:    */   
/*  67:    */   public void setCheckSpan(long checkSpan)
/*  68:    */   {
/*  69: 58 */     this.checkSpan = checkSpan;
/*  70:    */   }
/*  71:    */   
/*  72: 64 */   String table = "rtev";
/*  73:    */   
/*  74:    */   public ArrayList<Policy> needCheck(Calendar c, Policy[] ps)
/*  75:    */   {
/*  76: 66 */     ArrayList<Policy> rps = new ArrayList();
/*  77: 67 */     for (int i = 0; i < ps.length; i++)
/*  78:    */     {
/*  79: 68 */       Policy one = ps[i];
/*  80: 69 */       if (one.type == this.type)
/*  81:    */       {
/*  82: 71 */         LongTimeQueShuPolicy lp = (LongTimeQueShuPolicy)one;
/*  83:    */         
/*  84: 73 */         AlarmRecord ar = AlarmRecordManager.getRecord(one.id);
/*  85: 75 */         if (c.get(11) * 60 + c.get(12) - lp.startMin >= 0) {
/*  86: 77 */           if (c.getTimeInMillis() - ar.checkTime >= 60000 * lp.interval) {
/*  87: 78 */             rps.add(one);
/*  88:    */           }
/*  89:    */         }
/*  90:    */       }
/*  91:    */     }
/*  92: 82 */     if (rps.size() == 0) {
/*  93: 83 */       return null;
/*  94:    */     }
/*  95: 84 */     return rps;
/*  96:    */   }
/*  97:    */   
/*  98:    */   public LongTimeQueShuPolicy()
/*  99:    */   {
/* 100: 88 */     this.type = 16;
/* 101:    */   }
/* 102:    */   
/* 103:    */   public void check(Calendar cal, SingleDataBase db)
/* 104:    */   {
/* 105: 92 */     this.log.debug("LongTime queshu check " + this.senid + " begin!");
/* 106: 93 */     AlarmRecord ar = AlarmRecordManager.getRecord(this.id);
/* 107: 95 */     if (ar.checkTime > cal.getTimeInMillis()) {
/* 108: 96 */       return;
/* 109:    */     }
/* 110: 97 */     Connection conn = db.conn;
/* 111: 98 */     Calendar c = Calendar.getInstance();
/* 112: 99 */     c.setTimeInMillis(cal.getTimeInMillis());
/* 113:100 */     c.set(13, 0);
/* 114:101 */     c.set(14, 0);
/* 115:102 */     c.setTimeInMillis(c.getTimeInMillis() - (c.get(11) * 60 + c.get(12) - this.startMin) % this.interval * 60000L);
/* 116:    */     
/* 117:104 */     Date endTime = new Date(c.getTimeInMillis());
/* 118:105 */     Date startTime = new Date(c.getTimeInMillis() - this.checkSpan * 60000L);
/* 119:106 */     String sql = "SELECT time from " + this.table + " WHERE SENID=? AND TIME >= ? AND TIME <=? order by time";
/* 120:107 */     SimpleDateFormat sdf = new SimpleDateFormat("d日HH时mm分ss秒");
/* 121:    */     
/* 122:109 */     PreparedStatement pstmt = null;
/* 123:110 */     ResultSet rs = null;
/* 124:    */     try
/* 125:    */     {
/* 126:112 */       pstmt = conn.prepareStatement(sql);
/* 127:113 */       pstmt.setLong(1, this.senid);
/* 128:114 */       pstmt.setTimestamp(2, new Timestamp(startTime.getTime()));
/* 129:115 */       pstmt.setTimestamp(3, new Timestamp(endTime.getTime()));
/* 130:116 */       rs = pstmt.executeQuery();
/* 131:117 */       ArrayList<Long> datas = new ArrayList();
/* 132:118 */       while (rs.next()) {
/* 133:119 */         datas.add(Long.valueOf(rs.getTimestamp(1).getTime()));
/* 134:    */       }
/* 135:122 */       long one = c.getTimeInMillis() - this.checkSpan * 60000L;
/* 136:123 */       boolean find = false;
/* 137:124 */       String msg = "";String detail = "";
/* 138:125 */       for (int i = 0; i < datas.size(); i++)
/* 139:    */       {
/* 140:127 */         long two = ((Long)datas.get(i)).longValue();
/* 141:128 */         if (two - one > this.timeSpan * 60000)
/* 142:    */         {
/* 143:130 */           find = true;
/* 144:131 */           msg = msg + this.name + "(" + this.senid + ")从" + sdf.format(new Date(one)) + "到" + sdf.format(new Date(two)) + "没有实时数据,";
/* 145:132 */           this.log.info(msg);
/* 146:133 */           if (detail.equals("")) {
/* 147:134 */             detail = detail + this.type + "&" + this.senid + "&" + two + "&" + 1 + "&";
/* 148:    */           }
/* 149:135 */           sendToDB(two, msg, detail);
/* 150:    */         }
/* 151:137 */         one = two;
/* 152:    */       }
/* 153:139 */       long two = c.getTimeInMillis();
/* 154:140 */       if (two - one > this.timeSpan * 60000)
/* 155:    */       {
/* 156:142 */         find = true;
/* 157:143 */         msg = msg + this.name + "从" + sdf.format(new Date(one)) + "到" + sdf.format(new Date(two)) + "没有实时数据,";
/* 158:145 */         if (detail.equals("")) {
/* 159:146 */           detail = detail + this.type + "&" + this.senid + "&" + two + "&" + 1 + "&";
/* 160:    */         }
/* 161:    */       }
/* 162:149 */       if (find)
/* 163:    */       {
/* 164:151 */         ar.recordTime = c.getTimeInMillis();
/* 165:152 */         this.log.info(msg);
/* 166:153 */         sendToDB(cal.getTimeInMillis(), msg, detail);
/* 167:    */       }
/* 168:155 */       ar.checkTime = c.getTimeInMillis();
/* 169:    */       try
/* 170:    */       {
/* 171:158 */         rs.close();
/* 172:159 */         rs = null;
/* 173:    */       }
/* 174:    */       catch (Exception e1)
/* 175:    */       {
/* 176:163 */         e1.printStackTrace();
/* 177:    */       }
/* 178:    */       try
/* 179:    */       {
/* 180:167 */         pstmt.close();
/* 181:    */       }
/* 182:    */       catch (Exception e1)
/* 183:    */       {
/* 184:170 */         e1.printStackTrace();
/* 185:    */       }
/* 186:172 */       this.log.debug("LongTime queshu check " + this.senid + " end!");
/* 187:    */     }
/* 188:    */     catch (Exception e)
/* 189:    */     {
/* 190:175 */       if (rs != null) {
/* 191:    */         try
/* 192:    */         {
/* 193:177 */           rs.close();
/* 194:    */         }
/* 195:    */         catch (Exception e1)
/* 196:    */         {
/* 197:179 */           e1.printStackTrace();
/* 198:    */         }
/* 199:    */       }
/* 200:181 */       if (pstmt != null) {
/* 201:    */         try
/* 202:    */         {
/* 203:183 */           pstmt.close();
/* 204:    */         }
/* 205:    */         catch (Exception e1)
/* 206:    */         {
/* 207:185 */           e1.printStackTrace();
/* 208:    */         }
/* 209:    */       }
/* 210:187 */       dealDBException(e, db);
/* 211:    */     }
/* 212:    */   }
/* 213:    */   
/* 214:    */   public boolean getConfig(byte[] bs)
/* 215:    */   {
/* 216:195 */     Document doc = null;
/* 217:196 */     SAXReader saxReader = new SAXReader();
/* 218:    */     try
/* 219:    */     {
/* 220:198 */       doc = saxReader.read(new ByteArrayInputStream(bs));
/* 221:199 */       Element root = doc.getRootElement();
/* 222:200 */       this.senid = Integer.parseInt(root.element("senid").getStringValue());
/* 223:200 */       if (this.senid < 0L)
/* 224:    */       {
/* 225:202 */         this.log.info(this.id + "非法数据点号");
/* 226:203 */         return false;
/* 227:    */       }
/* 228:205 */       this.interval = Integer.parseInt(
/* 229:206 */         root.element("interval").getStringValue());
/* 230:207 */       this.timeSpan = Integer.parseInt(
/* 231:208 */         root.element("timespan").getStringValue());
/* 232:209 */       this.startMin = Integer.parseInt(
/* 233:210 */         root.element("startmin").getStringValue());
/* 234:211 */       this.checkSpan = Integer.parseInt(
/* 235:212 */         root.element("checkspan").getStringValue());
/* 236:213 */       return true;
/* 237:    */     }
/* 238:    */     catch (Exception e)
/* 239:    */     {
/* 240:215 */       this.log.info(e.toString());
/* 241:216 */       e.printStackTrace();
/* 242:    */     }
/* 243:217 */     return false;
/* 244:    */   }
/* 245:    */   
/* 246:    */   public void setName(List lists)
/* 247:    */   {
/* 248:224 */     for (Iterator it = lists.iterator(); it.hasNext();)
/* 249:    */     {
/* 250:226 */       Object one = it.next();
/* 251:227 */       if (one.getClass().getName().equals("com.nari.slsd.hd.model.WdsHydroElements"))
/* 252:    */       {
/* 253:229 */         WdsHydroElements mea = (WdsHydroElements)one;
/* 254:230 */         if (mea.getId().longValue() == this.senid)
/* 255:    */         {
/* 256:232 */           this.name = mea.getName();
/* 257:233 */           switch (mea.getRtdb().intValue())
/* 258:    */           {
/* 259:    */           case 1: 
/* 260:236 */             this.table = "rtdb";
/* 261:237 */             break;
/* 262:    */           case 2: 
/* 263:239 */             this.table = "rtsq";
/* 264:240 */             break;
/* 265:    */           case 3: 
/* 266:242 */             this.table = "rtems";
/* 267:243 */             break;
/* 268:    */           case 4: 
/* 269:245 */             this.table = "rtcalc";
/* 270:    */           }
/* 271:248 */           return;
/* 272:    */         }
/* 273:    */       }
/* 274:    */     }
/* 275:253 */     this.name = ("测站" + String.valueOf(this.senid));
/* 276:    */   }
/* 277:    */   
/* 278:    */   public void check(Calendar c, IWdsHisDataService wdsService) {}
/* 279:    */ }


/* Location:           C:\Users\lauren\Desktop\HD.AlarmCD.jar
 * Qualified Name:     com.nari.slsd.hd.alarmcd.policies.LongTimeQueShuPolicy
 * JD-Core Version:    0.7.0.1
 */