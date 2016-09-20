/*   1:    */ package com.nari.slsd.hd.alarmcd.policies;
/*   2:    */ 
/*   3:    */ import com.nari.slsd.hd.alarmcd.UserLog;
/*   4:    */ import com.nari.slsd.hd.alarmcd.type.AlarmRecord;
/*   5:    */ import com.nari.slsd.hd.alarmcd.type.AlarmRecordManager;
/*   6:    */ import com.nari.slsd.hd.alarmcd.type.SingleDataBase;
/*   7:    */ import com.nari.slsd.hd.model.WdsHydroElements;
/*   8:    */ import com.nari.slsd.hd.service.IWdsHisDataService;
/*   9:    */ import java.io.ByteArrayInputStream;
/*  10:    */ import java.text.SimpleDateFormat;
/*  11:    */ import java.util.Calendar;
/*  12:    */ import java.util.Iterator;
/*  13:    */ import java.util.List;
/*  14:    */ import org.dom4j.Document;
/*  15:    */ import org.dom4j.Element;
/*  16:    */ import org.dom4j.io.SAXReader;
/*  17:    */ 
/*  18:    */ public class YueJiePolicy
/*  19:    */   extends Policy
/*  20:    */ {
/*  21:    */   long senid;
/*  22:    */   
/*  23:    */   public long getSenid()
/*  24:    */   {
/*  25: 25 */     return this.senid;
/*  26:    */   }
/*  27:    */   
/*  28:    */   public void setSenid(long senid)
/*  29:    */   {
/*  30: 29 */     this.senid = senid;
/*  31:    */   }
/*  32:    */   
/*  33: 31 */   double upLimit1 = -100000.0D;
/*  34:    */   
/*  35:    */   public double getUpLimit1()
/*  36:    */   {
/*  37: 33 */     return this.upLimit1;
/*  38:    */   }
/*  39:    */   
/*  40:    */   public void setUpLimit1(double upLimit1)
/*  41:    */   {
/*  42: 37 */     this.upLimit1 = upLimit1;
/*  43:    */   }
/*  44:    */   
/*  45:    */   public double getDownLimit1()
/*  46:    */   {
/*  47: 41 */     return this.downLimit1;
/*  48:    */   }
/*  49:    */   
/*  50:    */   public void setDownLimit1(double downLimit1)
/*  51:    */   {
/*  52: 45 */     this.downLimit1 = downLimit1;
/*  53:    */   }
/*  54:    */   
/*  55:    */   public double getUpLimit2()
/*  56:    */   {
/*  57: 49 */     return this.upLimit2;
/*  58:    */   }
/*  59:    */   
/*  60:    */   public void setUpLimit2(double upLimit2)
/*  61:    */   {
/*  62: 53 */     this.upLimit2 = upLimit2;
/*  63:    */   }
/*  64:    */   
/*  65:    */   public double getDownLimit2()
/*  66:    */   {
/*  67: 57 */     return this.downLimit2;
/*  68:    */   }
/*  69:    */   
/*  70:    */   public void setDownLimit2(double downLimit2)
/*  71:    */   {
/*  72: 61 */     this.downLimit2 = downLimit2;
/*  73:    */   }
/*  74:    */   
/*  75: 64 */   double downLimit1 = -100000.0D;
/*  76: 65 */   double upLimit2 = -100000.0D;
/*  77: 66 */   double downLimit2 = -100000.0D;
/*  78: 67 */   double downLimit3 = -100000.0D;
/*  79:    */   
/*  80:    */   public double getDownLimit3()
/*  81:    */   {
/*  82: 69 */     return this.downLimit3;
/*  83:    */   }
/*  84:    */   
/*  85:    */   public void setDownLimit3(double downLimit3)
/*  86:    */   {
/*  87: 73 */     this.downLimit3 = downLimit3;
/*  88:    */   }
/*  89:    */   
/*  90:    */   public double getUpLimit3()
/*  91:    */   {
/*  92: 77 */     return this.upLimit3;
/*  93:    */   }
/*  94:    */   
/*  95:    */   public void setUpLimit3(double upLimit3)
/*  96:    */   {
/*  97: 81 */     this.upLimit3 = upLimit3;
/*  98:    */   }
/*  99:    */   
/* 100: 83 */   double upLimit3 = -100000.0D;
/* 101: 84 */   double recoverLimit = -1.0D;
/* 102:    */   
/* 103:    */   public double getRecoverLimit()
/* 104:    */   {
/* 105: 95 */     return this.recoverLimit;
/* 106:    */   }
/* 107:    */   
/* 108:    */   public void setRecoverLimit(double recoverLimit)
/* 109:    */   {
/* 110: 99 */     this.recoverLimit = recoverLimit;
/* 111:    */   }
/* 112:    */   
/* 113:102 */   String table = "rtev";
/* 114:    */   
/* 115:    */   public YueJiePolicy()
/* 116:    */   {
/* 117:105 */     this.type = 13;
/* 118:    */   }
/* 119:    */   
/* 120:    */   public void check(Calendar cal, SingleDataBase db) {}
/* 121:    */   
/* 122:    */   public boolean getConfig(byte[] bs)
/* 123:    */   {
/* 124:119 */     Document doc = null;
/* 125:120 */     SAXReader saxReader = new SAXReader();
/* 126:    */     try
/* 127:    */     {
/* 128:122 */       doc = saxReader.read(new ByteArrayInputStream(bs));
/* 129:123 */       Element root = doc.getRootElement();
/* 130:124 */       this.senid = Long.parseLong(root.element("senid").getStringValue());
/* 131:125 */       if (this.senid < 0L)
/* 132:    */       {
/* 133:127 */         this.log.info(this.id + "非法数据点号");
/* 134:128 */         return false;
/* 135:    */       }
/* 136:130 */       if (root.element("up2") != null) {
/* 137:131 */         this.upLimit2 = Double.parseDouble(root.element("up2").getStringValue());
/* 138:    */       }
/* 139:132 */       if (root.element("up1") != null) {
/* 140:133 */         this.upLimit1 = Double.parseDouble(root.element("up1").getStringValue());
/* 141:    */       }
/* 142:135 */       if (root.element("down2") != null) {
/* 143:136 */         this.downLimit2 = Double.parseDouble(
/* 144:137 */           root.element("down2").getStringValue());
/* 145:    */       }
/* 146:138 */       if (root.element("down1") != null) {
/* 147:139 */         this.downLimit1 = Double.parseDouble(
/* 148:140 */           root.element("down1").getStringValue());
/* 149:    */       }
/* 150:141 */       if (root.element("up3") != null) {
/* 151:142 */         this.upLimit3 = Double.parseDouble(
/* 152:143 */           root.element("up3").getStringValue());
/* 153:    */       }
/* 154:144 */       if (root.element("down3") != null) {
/* 155:145 */         this.downLimit3 = Double.parseDouble(
/* 156:146 */           root.element("down3").getStringValue());
/* 157:    */       }
/* 158:148 */       this.recoverLimit = Double.parseDouble(
/* 159:149 */         root.element("range").getStringValue());
/* 160:150 */       return true;
/* 161:    */     }
/* 162:    */     catch (Exception e)
/* 163:    */     {
/* 164:152 */       this.log.info(e.toString());
/* 165:153 */       e.printStackTrace();
/* 166:    */     }
/* 167:154 */     return false;
/* 168:    */   }
/* 169:    */   
/* 170:    */   public void setName(List lists)
/* 171:    */   {
/* 172:161 */     for (Iterator it = lists.iterator(); it.hasNext();)
/* 173:    */     {
/* 174:163 */       Object one = it.next();
/* 175:164 */       if (one.getClass().getName().equals("com.nari.slsd.hd.model.WdsHydroElements"))
/* 176:    */       {
/* 177:166 */         WdsHydroElements mea = (WdsHydroElements)one;
/* 178:167 */         if (mea.getId().longValue() == this.senid)
/* 179:    */         {
/* 180:169 */           this.name = mea.getName();
/* 181:170 */           switch (mea.getRtdb().intValue())
/* 182:    */           {
/* 183:    */           case 1: 
/* 184:173 */             this.table = "rtdb";
/* 185:174 */             break;
/* 186:    */           case 2: 
/* 187:176 */             this.table = "rtsq";
/* 188:177 */             break;
/* 189:    */           case 3: 
/* 190:179 */             this.table = "rtems";
/* 191:180 */             break;
/* 192:    */           case 4: 
/* 193:182 */             this.table = "rtcalc";
/* 194:    */           }
/* 195:185 */           return;
/* 196:    */         }
/* 197:    */       }
/* 198:    */     }
/* 199:190 */     this.name = ("测站" + String.valueOf(this.senid));
/* 200:    */   }
/* 201:    */   
/* 202:    */   private void alarm(long t, double v, AlarmRecord a, int level, boolean abnormal)
/* 203:    */   {
/* 204:194 */     SimpleDateFormat sdf = new SimpleDateFormat("d日 HH时mm分ss秒");
/* 205:195 */     a.abnormal = abnormal;
/* 206:196 */     a.checkTime = System.currentTimeMillis();
/* 207:197 */     a.recordTime = t;
/* 208:198 */     a.setLevel(level);
/* 209:199 */     a.setV(v);
/* 210:200 */     String str = null;
/* 211:201 */     if (!abnormal) {
/* 212:202 */       str = "恢复正常";
/* 213:    */     } else {
/* 214:204 */       switch (level)
/* 215:    */       {
/* 216:    */       case 1: 
/* 217:207 */         str = "越上限报警";
/* 218:208 */         break;
/* 219:    */       case 2: 
/* 220:210 */         str = "越上上限报警";
/* 221:211 */         break;
/* 222:    */       case 3: 
/* 223:213 */         str = "越三级上限报警";
/* 224:214 */         break;
/* 225:    */       case -1: 
/* 226:216 */         str = "越下限报警";
/* 227:217 */         break;
/* 228:    */       case -2: 
/* 229:219 */         str = "越下下限报警";
/* 230:220 */         break;
/* 231:    */       case -3: 
/* 232:222 */         str = "越三级下限报警";
/* 233:    */       }
/* 234:    */     }
/* 235:227 */     String msg = this.name + sdf.format(Long.valueOf(t)) + "数值：" + v + str;
/* 236:228 */     this.log.info(msg);
/* 237:    */     
/* 238:230 */     sendToDB(a.checkTime, msg, null);
/* 239:    */   }
/* 240:    */   
/* 241:    */   public void check(Calendar cal, double v)
/* 242:    */   {
/* 243:235 */     AlarmRecord a = AlarmRecordManager.getRecord(this.id);
/* 244:238 */     if ((this.upLimit3 > -100000.0D) && (v >= this.upLimit3))
/* 245:    */     {
/* 246:240 */       if ((!a.abnormal) || (a.getLevel() != 3)) {
/* 247:241 */         alarm(cal.getTimeInMillis(), v, a, 3, true);
/* 248:    */       }
/* 249:242 */       return;
/* 250:    */     }
/* 251:246 */     if ((this.upLimit2 > -100000.0D) && (v >= this.upLimit2))
/* 252:    */     {
/* 253:248 */       if ((!a.abnormal) || (
/* 254:249 */         (a.getLevel() != 2) && ((a.getLevel() != 3) || 
/* 255:    */         
/* 256:251 */         (Math.round((a.getV() - v) * 10000.0D) > Math.round(this.recoverLimit * 10000.0D))))) {
/* 257:256 */         alarm(cal.getTimeInMillis(), v, a, 2, true);
/* 258:    */       }
/* 259:258 */       return;
/* 260:    */     }
/* 261:260 */     if ((this.upLimit1 > -100000.0D) && (v >= this.upLimit1))
/* 262:    */     {
/* 263:262 */       if ((!a.abnormal) || (
/* 264:263 */         (a.getLevel() != 1) && ((a.getLevel() != 2) || 
/* 265:    */         
/* 266:265 */         (Math.round((a.getV() - v) * 10000.0D) > Math.round(this.recoverLimit * 10000.0D))))) {
/* 267:270 */         alarm(cal.getTimeInMillis(), v, a, 1, true);
/* 268:    */       }
/* 269:272 */       return;
/* 270:    */     }
/* 271:274 */     if ((this.downLimit3 > -100000.0D) && (v <= this.downLimit3))
/* 272:    */     {
/* 273:276 */       if ((!a.abnormal) || (a.getLevel() != -3)) {
/* 274:277 */         alarm(cal.getTimeInMillis(), v, a, -3, true);
/* 275:    */       }
/* 276:278 */       return;
/* 277:    */     }
/* 278:280 */     if ((this.downLimit2 > -100000.0D) && (v <= this.downLimit2))
/* 279:    */     {
/* 280:282 */       if ((!a.abnormal) || (
/* 281:283 */         (a.getLevel() != -2) && ((a.getLevel() != -3) || 
/* 282:    */         
/* 283:285 */         (Math.round((a.getV() - v) * 10000.0D) > Math.round(this.recoverLimit * 10000.0D))))) {
/* 284:290 */         alarm(cal.getTimeInMillis(), v, a, -2, true);
/* 285:    */       }
/* 286:292 */       return;
/* 287:    */     }
/* 288:294 */     if ((this.downLimit1 > -100000.0D) && (v <= this.downLimit1))
/* 289:    */     {
/* 290:296 */       if ((!a.abnormal) || (
/* 291:297 */         (a.getLevel() != -1) && ((a.getLevel() != -2) || 
/* 292:    */         
/* 293:299 */         (Math.round((v - a.getV()) * 10000.0D) > Math.round(this.recoverLimit * 10000.0D))))) {
/* 294:302 */         alarm(cal.getTimeInMillis(), v, a, -1, true);
/* 295:    */       }
/* 296:303 */       return;
/* 297:    */     }
/* 298:305 */     if ((a.abnormal) && (((a.getLevel() != -1) && (a.getLevel() != 1)) || 
/* 299:    */     
/* 300:307 */       (Math.round(Math.abs(v - a.getV()) * 10000.0D) > Math.round(this.recoverLimit * 10000.0D)))) {
/* 301:309 */       alarm(cal.getTimeInMillis(), v, a, 0, false);
/* 302:    */     }
/* 303:    */   }
/* 304:    */   
/* 305:    */   public void check(Calendar c, IWdsHisDataService wdsService) {}
/* 306:    */ }


/* Location:           C:\Users\lauren\Desktop\HD.AlarmCD.jar
 * Qualified Name:     com.nari.slsd.hd.alarmcd.policies.YueJiePolicy
 * JD-Core Version:    0.7.0.1
 */