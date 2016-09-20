/*   1:    */ package com.nari.slsd.hd.alarmcd.policies;
/*   2:    */ 
/*   3:    */ import com.nari.slsd.hd.alarmcd.UserLog;
/*   4:    */ import com.nari.slsd.hd.alarmcd.type.AlarmRecord;
/*   5:    */ import com.nari.slsd.hd.alarmcd.type.AlarmRecordManager;
/*   6:    */ import com.nari.slsd.hd.alarmcd.type.SingleDataBase;
/*   7:    */ import com.nari.slsd.hd.alarmcd.type.UserTime;
/*   8:    */ import com.nari.slsd.hd.service.IWdsHisDataService;
/*   9:    */ import java.io.ByteArrayInputStream;
/*  10:    */ import java.sql.Connection;
/*  11:    */ import java.sql.PreparedStatement;
/*  12:    */ import java.sql.ResultSet;
/*  13:    */ import java.sql.SQLException;
/*  14:    */ import java.sql.Timestamp;
/*  15:    */ import java.util.ArrayList;
/*  16:    */ import java.util.Calendar;
/*  17:    */ import java.util.Date;
/*  18:    */ import java.util.List;
/*  19:    */ import java.util.regex.Matcher;
/*  20:    */ import java.util.regex.Pattern;
/*  21:    */ import javax.script.ScriptEngine;
/*  22:    */ import javax.script.ScriptEngineManager;
/*  23:    */ import javax.script.ScriptException;
/*  24:    */ import org.dom4j.Document;
/*  25:    */ import org.dom4j.Element;
/*  26:    */ import org.dom4j.io.SAXReader;
/*  27:    */ 
/*  28:    */ public class ScriptPolicy
/*  29:    */   extends Policy
/*  30:    */ {
/*  31:    */   TimeParamType[] timeBegins;
/*  32:    */   TimeParamType[] timeEnds;
/*  33:    */   String[] strs;
/*  34:    */   String[] sqls;
/*  35:    */   static ScriptEngine engine;
/*  36:    */   String script;
/*  37:    */   
/*  38:    */   static
/*  39:    */   {
/*  40: 44 */     ScriptEngineManager manager = new ScriptEngineManager();
/*  41: 45 */     engine = manager.getEngineByName("javascript");
/*  42:    */   }
/*  43:    */   
/*  44:    */   public ScriptPolicy()
/*  45:    */   {
/*  46: 48 */     this.type = 1;
/*  47:    */   }
/*  48:    */   
/*  49:    */   public void check(Calendar c, IWdsHisDataService wdsService) {}
/*  50:    */   
/*  51:    */   public void check(Calendar cal, SingleDataBase db)
/*  52:    */   {
/*  53: 58 */     this.log.debug("SqlPolicy check " + this.script + " begin!");
/*  54: 59 */     AlarmRecord ar = AlarmRecordManager.getRecord(this.id);
/*  55: 60 */     if (ar.recordTime > cal.getTimeInMillis()) {
/*  56: 61 */       return;
/*  57:    */     }
/*  58: 62 */     PreparedStatement pstmt = null;
/*  59: 63 */     ResultSet rs = null;
/*  60:    */     try
/*  61:    */     {
/*  62: 65 */       StringBuffer script = new StringBuffer(this.strs[0]);
/*  63: 66 */       String[] result = new String[this.sqls.length];
/*  64: 67 */       for (int i = 0; i < this.sqls.length; i++)
/*  65:    */       {
/*  66: 69 */         pstmt = db.conn.prepareStatement(this.sqls[i]);
/*  67: 70 */         if (this.timeBegins[i] != null) {
/*  68: 71 */           setTimeParam(pstmt, cal, this.timeBegins[i], 1);
/*  69:    */         }
/*  70: 72 */         if (this.timeEnds[i] != null) {
/*  71: 73 */           setTimeParam(pstmt, cal, this.timeEnds[i], 2);
/*  72:    */         }
/*  73: 74 */         rs = pstmt.executeQuery();
/*  74: 75 */         if (rs.next())
/*  75:    */         {
/*  76: 76 */           result[i] = rs.getObject(1).toString();
/*  77: 77 */           script.append(result[i]);
/*  78: 78 */           script.append(this.strs[(i + 1)]);
/*  79:    */         }
/*  80:    */         else
/*  81:    */         {
/*  82: 80 */           this.log.info(this.sqls[i] + " no result!");
/*  83:    */           try
/*  84:    */           {
/*  85: 83 */             rs.close();
/*  86:    */           }
/*  87:    */           catch (Exception e1)
/*  88:    */           {
/*  89: 87 */             e1.printStackTrace();
/*  90:    */           }
/*  91:    */           try
/*  92:    */           {
/*  93: 91 */             pstmt.close();
/*  94:    */           }
/*  95:    */           catch (Exception e1)
/*  96:    */           {
/*  97: 94 */             e1.printStackTrace();
/*  98:    */           }
/*  99: 96 */           return;
/* 100:    */         }
/* 101:    */       }
/* 102:    */       try
/* 103:    */       {
/* 104:102 */         rs.close();
/* 105:103 */         rs = null;
/* 106:    */       }
/* 107:    */       catch (Exception e1)
/* 108:    */       {
/* 109:107 */         e1.printStackTrace();
/* 110:    */       }
/* 111:    */       try
/* 112:    */       {
/* 113:111 */         pstmt.close();
/* 114:    */       }
/* 115:    */       catch (Exception e1)
/* 116:    */       {
/* 117:114 */         e1.printStackTrace();
/* 118:    */       }
/* 119:116 */       boolean b = ((Boolean)engine.eval(script.toString())).booleanValue();
/* 120:117 */       if (!b)
/* 121:    */       {
/* 122:119 */         String msg = this.name + " 错误 ";
/* 123:120 */         this.log.info("报警id(" + this.id + ")" + this.name + "错误 ");
/* 124:121 */         String detail = this.type + "&";
/* 125:122 */         for (int i = 0; i < this.sqls.length; i++) {
/* 126:124 */           detail = detail + "'" + this.sqls[i] + "'=" + result[i] + "&";
/* 127:    */         }
/* 128:127 */         sendToDB(cal.getTimeInMillis(), msg, detail);
/* 129:128 */         ar.recordTime = cal.getTimeInMillis();
/* 130:    */       }
/* 131:130 */       ar.checkTime = cal.getTimeInMillis();
/* 132:    */     }
/* 133:    */     catch (ScriptException e)
/* 134:    */     {
/* 135:132 */       e.printStackTrace();
/* 136:133 */       this.log.info(e.toString() + this.script.toString());
/* 137:    */     }
/* 138:    */     catch (SQLException e)
/* 139:    */     {
/* 140:135 */       if (rs != null) {
/* 141:    */         try
/* 142:    */         {
/* 143:137 */           rs.close();
/* 144:    */         }
/* 145:    */         catch (Exception e1)
/* 146:    */         {
/* 147:139 */           e1.printStackTrace();
/* 148:    */         }
/* 149:    */       }
/* 150:141 */       if (pstmt != null) {
/* 151:    */         try
/* 152:    */         {
/* 153:143 */           pstmt.close();
/* 154:    */         }
/* 155:    */         catch (Exception e1)
/* 156:    */         {
/* 157:145 */           e1.printStackTrace();
/* 158:    */         }
/* 159:    */       }
/* 160:147 */       e.printStackTrace();
/* 161:148 */       dealDBException(e, db);
/* 162:    */     }
/* 163:    */   }
/* 164:    */   
/* 165:    */   private void setTimeParam(PreparedStatement pstmt, Calendar cal, TimeParamType tparam, int index)
/* 166:    */     throws SQLException
/* 167:    */   {
/* 168:154 */     switch (tparam.type)
/* 169:    */     {
/* 170:    */     case 0: 
/* 171:157 */       pstmt.setTimestamp(index, new Timestamp(UserTime.getHour(cal.getTime()).getTime() + tparam.adjustment * 3600000L));
/* 172:158 */       break;
/* 173:    */     case 1: 
/* 174:160 */       pstmt.setTimestamp(index, new Timestamp(UserTime.getDay(cal.getTime()).getTimeInMillis() + tparam.adjustment * 3600000L));
/* 175:161 */       break;
/* 176:    */     case 2: 
/* 177:163 */       pstmt.setTimestamp(index, new Timestamp(UserTime.getDay10(cal.getTime()).getTimeInMillis() + tparam.adjustment * 3600000L));
/* 178:164 */       break;
/* 179:    */     case 3: 
/* 180:166 */       pstmt.setTimestamp(index, new Timestamp(UserTime.getMonth(cal.getTime()).getTimeInMillis() + tparam.adjustment * 3600000L));
/* 181:    */     }
/* 182:    */   }
/* 183:    */   
/* 184:    */   public boolean getConfig(byte[] bs)
/* 185:    */   {
/* 186:173 */     Document doc = null;
/* 187:174 */     SAXReader saxReader = new SAXReader();
/* 188:    */     try
/* 189:    */     {
/* 190:176 */       doc = saxReader.read(new ByteArrayInputStream(bs));
/* 191:177 */       Element root = doc.getRootElement();
/* 192:178 */       this.name = root.attributeValue("name");
/* 193:179 */       this.interval = Integer.parseInt(
/* 194:180 */         root.element("interval").getStringValue());
/* 195:181 */       this.script = root.element("script").getStringValue();
/* 196:182 */       Pattern p = Pattern.compile("@[a-z,A-Z]*");
/* 197:183 */       this.strs = p.split(" " + this.script + " ");
/* 198:184 */       Matcher m = p.matcher(this.script);
/* 199:185 */       ArrayList<String> names = new ArrayList();
/* 200:186 */       while (m.find()) {
/* 201:188 */         names.add(m.group(0).substring(1));
/* 202:    */       }
/* 203:190 */       int c = names.size();
/* 204:    */       
/* 205:192 */       this.sqls = new String[c];
/* 206:193 */       this.timeBegins = new TimeParamType[c];
/* 207:194 */       this.timeEnds = new TimeParamType[c];
/* 208:195 */       Element ph = root.element("variables");
/* 209:196 */       for (int i = 0; i < c; i++)
/* 210:    */       {
/* 211:198 */         this.sqls[i] = ph.elementText((String)names.get(i));
/* 212:199 */         this.timeBegins[i] = SetTimeParamFromXml(ph.element((String)names.get(i)).attributeValue("tb"));
/* 213:200 */         this.timeEnds[i] = SetTimeParamFromXml(ph.element((String)names.get(i)).attributeValue("te"));
/* 214:    */       }
/* 215:202 */       return true;
/* 216:    */     }
/* 217:    */     catch (Exception e)
/* 218:    */     {
/* 219:204 */       this.log.info("Policy" + this.id + " define " + e.toString());
/* 220:205 */       e.printStackTrace();
/* 221:    */     }
/* 222:206 */     return false;
/* 223:    */   }
/* 224:    */   
/* 225:    */   private TimeParamType SetTimeParamFromXml(String str)
/* 226:    */     throws Exception
/* 227:    */   {
/* 228:212 */     if (str == null) {
/* 229:213 */       return null;
/* 230:    */     }
/* 231:214 */     TimeParamType tpt = new TimeParamType();
/* 232:215 */     int i = str.indexOf("+");
/* 233:216 */     if (i < 0) {
/* 234:217 */       i = str.indexOf("-");
/* 235:    */     }
/* 236:    */     String tstr;
/* 237:219 */     if (i < 0)
/* 238:    */     {
/* 239:221 */       tstr = str;
/* 240:222 */       tpt.adjustment = 0;
/* 241:    */     }
/* 242:    */     else
/* 243:    */     {
/* 244:226 */       tstr = str.substring(0, i);
/* 245:227 */       tpt.adjustment = Integer.parseInt(str.substring(i + 1));
/* 246:228 */       if (tstr.charAt(i - 1) == '-') {
/* 247:229 */         tpt.adjustment = (-tpt.adjustment);
/* 248:    */       }
/* 249:    */     }
/* 250:231 */     if (tstr.equals("hour")) {
/* 251:232 */       tpt.type = 0;
/* 252:233 */     } else if (tstr.equals("day")) {
/* 253:234 */       tpt.type = 1;
/* 254:235 */     } else if (tstr.equals("day10")) {
/* 255:236 */       tpt.type = 2;
/* 256:237 */     } else if (tstr.equals("month")) {
/* 257:238 */       tpt.type = 3;
/* 258:    */     } else {
/* 259:240 */       throw new Exception("undefined time type " + str);
/* 260:    */     }
/* 261:242 */     return tpt;
/* 262:    */   }
/* 263:    */   
/* 264:    */   public void setName(List lists) {}
/* 265:    */ }


/* Location:           C:\Users\lauren\Desktop\HD.AlarmCD.jar
 * Qualified Name:     com.nari.slsd.hd.alarmcd.policies.ScriptPolicy
 * JD-Core Version:    0.7.0.1
 */