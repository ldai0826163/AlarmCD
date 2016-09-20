/*   1:    */ package com.nari.slsd.hd.alarmcd.policies;
/*   2:    */ 
/*   3:    */ import com.nari.slsd.hd.alarmcd.UserLog;
/*   4:    */ import com.nari.slsd.hd.alarmcd.type.AlarmRecord;
/*   5:    */ import com.nari.slsd.hd.alarmcd.type.AlarmRecordManager;
/*   6:    */ import com.nari.slsd.hd.alarmcd.type.SingleDataBase;
/*   7:    */ import com.nari.slsd.hd.service.IWdsHisDataService;
/*   8:    */ import java.io.ByteArrayInputStream;
/*   9:    */ import java.sql.Connection;
/*  10:    */ import java.sql.PreparedStatement;
/*  11:    */ import java.sql.ResultSet;
/*  12:    */ import java.util.ArrayList;
/*  13:    */ import java.util.Calendar;
/*  14:    */ import java.util.List;
/*  15:    */ import org.dom4j.Document;
/*  16:    */ import org.dom4j.Element;
/*  17:    */ import org.dom4j.io.SAXReader;
/*  18:    */ 
/*  19:    */ public class SqlPolicy
/*  20:    */   extends Policy
/*  21:    */ {
/*  22:    */   String sqlStr;
/*  23:    */   boolean[] min;
/*  24:    */   boolean[] hour;
/*  25:    */   boolean[] day;
/*  26:    */   boolean[] month;
/*  27:    */   boolean[] week;
/*  28:    */   
/*  29:    */   public SqlPolicy()
/*  30:    */   {
/*  31: 41 */     this.type = 2;
/*  32:    */   }
/*  33:    */   
/*  34:    */   public void check(Calendar c, IWdsHisDataService wdsService) {}
/*  35:    */   
/*  36:    */   public void check(Calendar cal, SingleDataBase db)
/*  37:    */   {
/*  38: 52 */     this.log.debug("SqlPolicy check " + this.name + " begin!");
/*  39: 53 */     AlarmRecord ar = AlarmRecordManager.getRecord(this.id);
/*  40: 54 */     if (ar.recordTime > cal.getTimeInMillis()) {
/*  41: 55 */       return;
/*  42:    */     }
/*  43: 56 */     PreparedStatement pstmt = null;
/*  44: 57 */     ResultSet rs = null;
/*  45:    */     try
/*  46:    */     {
/*  47: 60 */       pstmt = db.conn.prepareStatement(this.sqlStr);
/*  48: 61 */       rs = pstmt.executeQuery();
/*  49: 62 */       if ((rs.next()) && (rs.getString(1) != null))
/*  50:    */       {
/*  51: 64 */         this.log.info("数据库运算" + this.name + " 结果" + rs.getString(1));
/*  52: 65 */         sendToDB(cal.getTimeInMillis(), rs.getString(1), this.name);
/*  53: 66 */         ar.recordTime = System.currentTimeMillis();
/*  54:    */       }
/*  55:    */       else
/*  56:    */       {
/*  57: 69 */         this.log.debug(this.name + " normal!");
/*  58:    */         try
/*  59:    */         {
/*  60: 71 */           rs.close();
/*  61:    */         }
/*  62:    */         catch (Exception e1)
/*  63:    */         {
/*  64: 73 */           e1.printStackTrace();
/*  65:    */         }
/*  66:    */         try
/*  67:    */         {
/*  68: 76 */           pstmt.close();
/*  69:    */         }
/*  70:    */         catch (Exception e1)
/*  71:    */         {
/*  72: 78 */           e1.printStackTrace();
/*  73:    */         }
/*  74: 80 */         ar.checkTime = cal.getTimeInMillis();
/*  75: 81 */         return;
/*  76:    */       }
/*  77:    */     }
/*  78:    */     catch (Exception e)
/*  79:    */     {
/*  80:    */       try
/*  81:    */       {
/*  82: 86 */         rs.close();
/*  83: 87 */         rs = null;
/*  84:    */       }
/*  85:    */       catch (Exception e1)
/*  86:    */       {
/*  87: 89 */         e1.printStackTrace();
/*  88:    */       }
/*  89:    */       try
/*  90:    */       {
/*  91: 92 */         pstmt.close();
/*  92:    */       }
/*  93:    */       catch (Exception e1)
/*  94:    */       {
/*  95: 94 */         e1.printStackTrace();
/*  96:    */       }
/*  97: 96 */       dealDBException(e, db);
/*  98:    */     }
/*  99:    */   }
/* 100:    */   
/* 101:    */   private boolean[] setCronElements(String str, int len)
/* 102:    */     throws Exception
/* 103:    */   {
/* 104:    */     try
/* 105:    */     {
/* 106:103 */       boolean[] bl = new boolean[len];
/* 107:104 */       if (str.equals("*"))
/* 108:    */       {
/* 109:106 */         for (int i = 0; i < len; i++) {
/* 110:107 */           bl[i] = true;
/* 111:    */         }
/* 112:108 */         return bl;
/* 113:    */       }
/* 114:111 */       for (int i = 0; i < len; i++) {
/* 115:112 */         bl[i] = false;
/* 116:    */       }
/* 117:113 */       String[] mins = str.split(",");
/* 118:114 */       for (int i = 0; i < mins.length; i++) {
/* 119:115 */         for (int j = 0; j < mins.length; j++)
/* 120:    */         {
/* 121:116 */           String[] xs = mins[j].split("-");
/* 122:117 */           int start = Integer.parseInt(xs[0]);
/* 123:118 */           bl[start] = true;
/* 124:119 */           if (xs.length > 1)
/* 125:    */           {
/* 126:120 */             int end = Integer.parseInt(xs[1]);
/* 127:121 */             for (int k = start + 1; k <= end; k++) {
/* 128:122 */               bl[k] = true;
/* 129:    */             }
/* 130:    */           }
/* 131:    */         }
/* 132:    */       }
/* 133:126 */       return bl;
/* 134:    */     }
/* 135:    */     catch (Exception e)
/* 136:    */     {
/* 137:128 */       e.printStackTrace();
/* 138:129 */       throw new Exception(e.toString());
/* 139:    */     }
/* 140:    */   }
/* 141:    */   
/* 142:    */   private boolean setCron(String str)
/* 143:    */   {
/* 144:134 */     while (str.indexOf("  ") > 0) {
/* 145:135 */       str = str.replaceAll("  ", " ");
/* 146:    */     }
/* 147:136 */     String[] strs = str.split(" ");
/* 148:137 */     if (strs.length != 5) {
/* 149:138 */       return false;
/* 150:    */     }
/* 151:    */     try
/* 152:    */     {
/* 153:140 */       this.min = setCronElements(strs[0], 60);
/* 154:141 */       this.hour = setCronElements(strs[1], 24);
/* 155:142 */       this.day = setCronElements(strs[2], 32);
/* 156:143 */       this.month = setCronElements(strs[3], 13);
/* 157:144 */       this.week = setCronElements(strs[4], 7);
/* 158:145 */       return true;
/* 159:    */     }
/* 160:    */     catch (Exception e) {}
/* 161:148 */     return false;
/* 162:    */   }
/* 163:    */   
/* 164:    */   public boolean getConfig(byte[] bs)
/* 165:    */   {
/* 166:155 */     Document doc = null;
/* 167:156 */     SAXReader saxReader = new SAXReader();
/* 168:    */     try
/* 169:    */     {
/* 170:158 */       doc = saxReader.read(new ByteArrayInputStream(bs));
/* 171:159 */       Element root = doc.getRootElement();
/* 172:160 */       this.sqlStr = root.element("sql").getStringValue();
/* 173:161 */       if (!setCron(root.element("cron").getStringValue()))
/* 174:    */       {
/* 175:163 */         this.log.error("invalid cron " + root.element("cron").getStringValue());
/* 176:164 */         return false;
/* 177:    */       }
/* 178:166 */       return true;
/* 179:    */     }
/* 180:    */     catch (Exception e)
/* 181:    */     {
/* 182:168 */       this.log.info("Policy" + this.id + " define " + e.toString());
/* 183:169 */       e.printStackTrace();
/* 184:    */     }
/* 185:170 */     return false;
/* 186:    */   }
/* 187:    */   
/* 188:    */   public void setName(List lists) {}
/* 189:    */   
/* 190:    */   public ArrayList<Policy> needCheck(Calendar c, Policy[] ps)
/* 191:    */   {
/* 192:181 */     ArrayList<Policy> rps = new ArrayList();
/* 193:182 */     for (int i = 0; i < ps.length; i++)
/* 194:    */     {
/* 195:183 */       Policy one = ps[i];
/* 196:184 */       if (one.type == this.type)
/* 197:    */       {
/* 198:185 */         SqlPolicy sp = (SqlPolicy)one;
/* 199:186 */         AlarmRecord ar = AlarmRecordManager.getRecord(one.id);
/* 200:187 */         int m = c.get(12);
/* 201:188 */         int h = c.get(11);
/* 202:189 */         int d = c.get(5);
/* 203:190 */         int mon = c.get(2);
/* 204:191 */         int w = c.get(7);
/* 205:192 */         if ((sp.min[m] != false) && (sp.hour[h]!= false) && (sp.day[d] != false) && (sp.month[(mon + 1)] != false) && 
/* 206:193 */           (sp.week[(w - 1)] != false))
/* 207:    */         {
/* 208:195 */           Calendar ac = Calendar.getInstance();
/* 209:196 */           ac.setTimeInMillis(ar.recordTime);
/* 210:197 */           if ((m != ac.get(12)) || 
/* 211:198 */             (h != ac.get(11)) || 
/* 212:199 */             (d != ac.get(5)) || 
/* 213:200 */             (mon != ac.get(2)) || 
/* 214:201 */             (w != ac.get(7))) {
/* 215:203 */             rps.add(one);
/* 216:    */           }
/* 217:    */         }
/* 218:    */       }
/* 219:    */     }
/* 220:207 */     if (rps.size() == 0) {
/* 221:208 */       return null;
/* 222:    */     }
/* 223:209 */     return rps;
/* 224:    */   }
/* 225:    */ }


/* Location:           C:\Users\lauren\Desktop\HD.AlarmCD.jar
 * Qualified Name:     com.nari.slsd.hd.alarmcd.policies.SqlPolicy
 * JD-Core Version:    0.7.0.1
 */