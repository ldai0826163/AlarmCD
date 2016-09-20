/*   1:    */ package com.nari.slsd.hd.alarmcd.policies;
/*   2:    */ 
/*   3:    */ import com.nari.slsd.hd.alarmcd.UserLog;
/*   4:    */ import com.nari.slsd.hd.alarmcd.type.AlarmRecord;
/*   5:    */ import com.nari.slsd.hd.alarmcd.type.AlarmRecordManager;
/*   6:    */ import com.nari.slsd.hd.alarmcd.type.SingleDataBase;
/*   7:    */ import com.nari.slsd.hd.alarmcd.type.UserTime;
/*   8:    */ import com.nari.slsd.hd.model.WdsHydroElements;
/*   9:    */ import com.nari.slsd.hd.service.IWdsHisDataService;
/*  10:    */ import java.io.ByteArrayInputStream;
/*  11:    */ import java.math.BigDecimal;
/*  12:    */ import java.text.SimpleDateFormat;
/*  13:    */ import java.util.Calendar;
/*  14:    */ import java.util.HashMap;
/*  15:    */ import java.util.Iterator;
/*  16:    */ import java.util.List;
/*  17:    */ import org.dom4j.Document;
/*  18:    */ import org.dom4j.Element;
/*  19:    */ import org.dom4j.io.SAXReader;
/*  20:    */ 
/*  21:    */ public class YuLiangPolicy
/*  22:    */   extends Policy
/*  23:    */ {
/*  24:    */   long senid;
/*  25:    */   int timeSpan;
/*  26:    */   
/*  27:    */   public long getSenid()
/*  28:    */   {
/*  29: 30 */     return this.senid;
/*  30:    */   }
/*  31:    */   
/*  32:    */   public void setSenid(long senid)
/*  33:    */   {
/*  34: 34 */     this.senid = senid;
/*  35:    */   }
/*  36:    */   
/*  37:    */   public int getTimeSpan()
/*  38:    */   {
/*  39: 40 */     return this.timeSpan;
/*  40:    */   }
/*  41:    */   
/*  42:    */   public void setTimeSpan(int timeSpan)
/*  43:    */   {
/*  44: 44 */     this.timeSpan = timeSpan;
/*  45:    */   }
/*  46:    */   
/*  47: 48 */   double limit = 0.0D;
/*  48:    */   
/*  49:    */   public double getLimit()
/*  50:    */   {
/*  51: 51 */     return this.limit;
/*  52:    */   }
/*  53:    */   
/*  54:    */   public void setLimit(double limit)
/*  55:    */   {
/*  56: 55 */     this.limit = limit;
/*  57:    */   }
/*  58:    */   
/*  59: 57 */   String table = "rtev";
/*  60:    */   
/*  61:    */   public YuLiangPolicy()
/*  62:    */   {
/*  63: 61 */     this.type = 14;
/*  64:    */   }
/*  65:    */   
/*  66:    */   public void check(Calendar cal, SingleDataBase db) {}
/*  67:    */   
/*  68:    */   public boolean getConfig(byte[] bs)
/*  69:    */   {
/*  70: 69 */     Document doc = null;
/*  71: 70 */     SAXReader saxReader = new SAXReader();
/*  72:    */     try
/*  73:    */     {
/*  74: 72 */       doc = saxReader.read(new ByteArrayInputStream(bs));
/*  75: 73 */       Element root = doc.getRootElement();
/*  76: 74 */       this.senid = Long.parseLong(root.element("senid").getStringValue());
/*  77: 75 */       if (this.senid < 0L)
/*  78:    */       {
/*  79: 77 */         this.log.info(this.id + "非法数据点号");
/*  80: 78 */         return false;
/*  81:    */       }
/*  82: 80 */       this.interval = Integer.parseInt(
/*  83: 81 */         root.element("interval").getStringValue());
/*  84: 82 */       this.timeSpan = Integer.parseInt(
/*  85: 83 */         root.element("timespan").getStringValue());
/*  86: 84 */       this.limit = Double.parseDouble(root.element("range").getStringValue());
/*  87: 85 */       return true;
/*  88:    */     }
/*  89:    */     catch (Exception e)
/*  90:    */     {
/*  91: 87 */       this.log.info(e.toString());
/*  92: 88 */       e.printStackTrace();
/*  93:    */     }
/*  94: 89 */     return false;
/*  95:    */   }
/*  96:    */   
/*  97:    */   public void setName(List lists)
/*  98:    */   {
/*  99: 95 */     for (Iterator it = lists.iterator(); it.hasNext();)
/* 100:    */     {
/* 101: 97 */       Object one = it.next();
/* 102: 98 */       if (one.getClass().getName().equals("com.nari.slsd.hd.model.WdsHydroElements"))
/* 103:    */       {
/* 104:100 */         WdsHydroElements mea = (WdsHydroElements)one;
/* 105:101 */         if (mea.getId().longValue() == this.senid)
/* 106:    */         {
/* 107:103 */           this.name = mea.getName();
/* 108:104 */           switch (mea.getRtdb().intValue())
/* 109:    */           {
/* 110:    */           case 1: 
/* 111:107 */             this.table = "rtdb";
/* 112:108 */             break;
/* 113:    */           case 2: 
/* 114:110 */             this.table = "rtsq";
/* 115:111 */             break;
/* 116:    */           case 3: 
/* 117:113 */             this.table = "rtems";
/* 118:114 */             break;
/* 119:    */           case 4: 
/* 120:116 */             this.table = "rtcalc";
/* 121:    */           }
/* 122:119 */           return;
/* 123:    */         }
/* 124:    */       }
/* 125:    */     }
/* 126:124 */     this.name = ("测站" + String.valueOf(this.senid));
/* 127:    */   }
/* 128:    */   
/* 129:    */   public void check(Calendar cal, IWdsHisDataService wdsService)
/* 130:    */   {
/* 131:129 */     this.log.debug("yuliang " + this.senid + " begin!");
/* 132:130 */     AlarmRecord ar = AlarmRecordManager.getRecord(this.id);
/* 133:131 */     if (ar.checkTime > cal.getTimeInMillis()) {
/* 134:132 */       return;
/* 135:    */     }
/* 136:133 */     Calendar endTime = cal;
/* 137:134 */     Calendar startTime = (Calendar)endTime.clone();
/* 138:135 */     switch (this.timeSpan)
/* 139:    */     {
/* 140:    */     case 0: 
/* 141:138 */       startTime = UserTime.setHour(startTime, 0);
/* 142:139 */       break;
/* 143:    */     case -8: 
/* 144:142 */       startTime = UserTime.setHour(startTime, 8);
/* 145:143 */       if (endTime.get(11) < 8) {
/* 146:145 */         startTime.add(5, -1);
/* 147:    */       }
/* 148:147 */       break;
/* 149:    */     default: 
/* 150:149 */       if ((this.timeSpan > 8) || (this.timeSpan < 0)) {
/* 151:151 */         return;
/* 152:    */       }
/* 153:153 */       startTime.add(11, -1 * this.timeSpan);
/* 154:    */     }
/* 155:156 */     HashMap<String, Object> inmap = new HashMap();
/* 156:    */     
/* 157:158 */     inmap.put("App_Type", "APP_WDS");
/* 158:159 */     inmap.put("LONG_IDARRAY", new Long[] { Long.valueOf(this.senid) });
/* 159:160 */     inmap.put("GE_BTIME", startTime);
/* 160:161 */     inmap.put("LE_ETIME", endTime);
/* 161:162 */     HashMap<Long, Double> ha = null;
/* 162:    */     try
/* 163:    */     {
/* 164:164 */       ha = wdsService.getRealCount(inmap);
/* 165:    */     }
/* 166:    */     catch (Exception e)
/* 167:    */     {
/* 168:168 */       dealWdsServiceException(e);
/* 169:    */     }
/* 170:170 */     if (ha == null)
/* 171:    */     {
/* 172:172 */       ar.checkTime = cal.getTimeInMillis();
/* 173:173 */       return;
/* 174:    */     }
/* 175:175 */     Double v = (Double)ha.get(Long.valueOf(this.senid));
/* 176:176 */     if (v == null)
/* 177:    */     {
/* 178:178 */       ar.checkTime = cal.getTimeInMillis();
/* 179:179 */       return;
/* 180:    */     }
/* 181:181 */     this.log.debug("雨量" + v);
/* 182:    */     
/* 183:183 */     BigDecimal bd = new BigDecimal(v.doubleValue());
/* 184:184 */     BigDecimal bd1 = bd.setScale(3, 4);
/* 185:185 */     v = Double.valueOf(bd1.doubleValue());
/* 186:186 */     long ll = Double.doubleToLongBits(v.doubleValue());
/* 187:187 */     this.log.debug("取整雨量" + v);
/* 188:188 */     if (v.doubleValue() > this.limit)
/* 189:    */     {
/* 190:190 */       if ((!ar.abnormal) || (ar.recordTime != startTime.getTimeInMillis()))
/* 191:    */       {
/* 192:192 */         SimpleDateFormat sdf = new SimpleDateFormat("d日HH时mm分");
/* 193:193 */         ar.abnormal = true;
/* 194:194 */         ar.recordTime = startTime.getTimeInMillis();
/* 195:195 */         String msg = this.name + "从" + sdf.format(startTime.getTime()) + "到" + sdf.format(endTime.getTime()) + "雨量累计为" + v + "超过限值" + this.limit;
/* 196:196 */         this.log.info(msg);
/* 197:197 */         String detail = this.type + "&" + this.senid + "&" + cal.getTimeInMillis() + "&" + msg;
/* 198:198 */         sendToDB(endTime.getTimeInMillis(), msg, detail);
/* 199:    */       }
/* 200:    */     }
/* 201:204 */     else if (ar.abnormal)
/* 202:    */     {
/* 203:206 */       SimpleDateFormat sdf = new SimpleDateFormat("d日 HH时mm分");
/* 204:207 */       ar.abnormal = false;
/* 205:208 */       String msg = this.name + "(" + this.senid + ")从" + sdf.format(startTime.getTime()) + "到" + sdf.format(endTime.getTime()) + "雨量累计恢复正常";
/* 206:209 */       this.log.info(msg);
/* 207:210 */       String detail = this.type + "&" + this.senid + "&" + cal.getTimeInMillis() + "&" + msg;
/* 208:211 */       sendToDB(endTime.getTimeInMillis(), msg, detail);
/* 209:    */     }
/* 210:214 */     ar.checkTime = cal.getTimeInMillis();
/* 211:    */   }
/* 212:    */ }


/* Location:           C:\Users\lauren\Desktop\HD.AlarmCD.jar
 * Qualified Name:     com.nari.slsd.hd.alarmcd.policies.YuLiangPolicy
 * JD-Core Version:    0.7.0.1
 */