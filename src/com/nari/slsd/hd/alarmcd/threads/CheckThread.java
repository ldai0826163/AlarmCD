/*   1:    */ package com.nari.slsd.hd.alarmcd.threads;
/*   2:    */ 
/*   3:    */ import com.nari.slsd.hd.alarmcd.Config;
/*   4:    */ import com.nari.slsd.hd.alarmcd.Main;
/*   5:    */ import com.nari.slsd.hd.alarmcd.UserLog;
/*   6:    */ import com.nari.slsd.hd.alarmcd.policies.BianFuPolicy;
/*   7:    */ import com.nari.slsd.hd.alarmcd.policies.LongTimeQueShuPolicy;
/*   8:    */ import com.nari.slsd.hd.alarmcd.policies.LongTimeUnchangePolicy;
/*   9:    */ import com.nari.slsd.hd.alarmcd.policies.Policy;
/*  10:    */ import com.nari.slsd.hd.alarmcd.policies.QueShuPolicy;
/*  11:    */ import com.nari.slsd.hd.alarmcd.policies.ScriptPolicy;
/*  12:    */ import com.nari.slsd.hd.alarmcd.policies.SqlPolicy;
/*  13:    */ import com.nari.slsd.hd.alarmcd.policies.YuLiangPolicy;
/*  14:    */ import com.nari.slsd.hd.alarmcd.policies.YueJiePolicy;
/*  15:    */ import com.nari.slsd.hd.alarmcd.type.AlarmRecordManager;
/*  16:    */ import com.nari.slsd.hd.alarmcd.type.MonitorQueueConfig;
/*  17:    */ import com.nari.slsd.hd.model.PubDefAlarm;
/*  18:    */ import com.nari.slsd.hd.model.WdsHydroElements;
/*  19:    */ import com.nari.slsd.hd.service.IPubDataService;
/*  20:    */ import java.util.ArrayList;
/*  21:    */ import java.util.Calendar;
/*  22:    */ import java.util.HashMap;
/*  23:    */ import java.util.List;
/*  24:    */ 
/*  25:    */ public class CheckThread
/*  26:    */   extends Thread
/*  27:    */ {
/*  28: 28 */   static UserLog log = new UserLog();
/*  29:    */   IPubDataService pubService;
/*  30:    */   static Policy[] policies;
/*  31:    */   Policy[] policyClass;
/*  32:    */   int[] policyIndex;
/*  33: 33 */   boolean threadDone = false;
/*  34:    */   YueJiePolicy[] yjpolicies;
/*  35:    */   
/*  36:    */   public void done()
/*  37:    */   {
/*  38: 35 */     this.threadDone = true;
/*  39:    */   }
/*  40:    */   
/*  41:    */   public CheckThread(IPubDataService pds)
/*  42:    */   {
/*  43: 38 */     this.pubService = pds;
/*  44: 39 */     initPoliciesFromConfig();
/*  45: 40 */     getPolicyDefineFromDB();
/*  46: 41 */     AlarmRecordManager.init(policies);
/*  47:    */   }
/*  48:    */   
/*  49:    */   private void initPoliciesFromConfig()
/*  50:    */   {
/*  51: 45 */     int c = 0;
/*  52: 46 */     for (int i = 0; i < Config.mqconfigs.length; i++) {
/*  53: 48 */       c += Config.mqconfigs[i].policies.length;
/*  54:    */     }
/*  55: 50 */     if (c == 0)
/*  56:    */     {
/*  57: 52 */       log.info("no policy define");
/*  58: 53 */       System.exit(0);
/*  59:    */     }
/*  60: 55 */     this.policyClass = new Policy[c];
/*  61: 56 */     this.policyIndex = new int[c];
/*  62: 57 */     c = 0;
/*  63: 58 */     for (int i = 0; i < Config.mqconfigs.length; i++)
/*  64:    */     {
/*  65: 60 */       MonitorQueueConfig one = Config.mqconfigs[i];
/*  66: 61 */       for (int j = 0; j < one.policies.length; j++) {
/*  67:    */         try
/*  68:    */         {
/*  69: 64 */           Policy p = (Policy)Class.forName("com.nari.slsd.hd.alarmcd.policies." + one.policies[j] + "Policy").newInstance();
/*  70: 65 */           this.policyClass[c] = p;
/*  71: 66 */           this.policyIndex[c] = i;
/*  72: 67 */           c++;
/*  73:    */         }
/*  74:    */         catch (Exception e)
/*  75:    */         {
/*  76: 70 */           e.printStackTrace();
/*  77: 71 */           log.info("Policy name " + one.policies + " define error,System exit");
/*  78: 72 */           System.exit(0);
/*  79:    */         }
/*  80:    */       }
/*  81:    */     }
/*  82:    */   }
/*  83:    */   
/*  84:    */   private void getPolicyDefineFromDB()
/*  85:    */   {
/*  86:    */     try
/*  87:    */     {
/*  88: 80 */       List<PubDefAlarm> alarms = this.pubService.getAllList(PubDefAlarm.class);
/*  89: 81 */       ArrayList<Policy> pList = new ArrayList();
/*  90: 82 */       for (int i = 0; i < alarms.size(); i++) {
/*  91:    */         try
/*  92:    */         {
/*  93: 85 */           PubDefAlarm dbone = (PubDefAlarm)alarms.get(i);
/*  94: 87 */           if (dbone.getnOnOff() == 0) {
/*  95: 89 */             if ((dbone.getType().intValue() >= 201) || (dbone.getType().intValue() <= 100) || (dbone.getType().intValue() == 120))
/*  96:    */             {
/*  97:    */               Policy pone=null;
/* 105: 91 */               switch (dbone.getType().intValue())
/* 106:    */               {
/* 107:    */               case 12: 
/* 108: 93 */                 pone = new BianFuPolicy();
/* 109: 94 */                 break;
/* 110:    */               case 16: 
/* 111: 96 */                 pone = new LongTimeQueShuPolicy();
/* 112: 97 */                 break;
/* 113:    */               case 17: 
/* 114: 99 */                 pone = new LongTimeUnchangePolicy();
/* 115:100 */                 break;
/* 116:    */               case 11: 
/* 117:102 */                 pone = new QueShuPolicy();
/* 118:103 */                 break;
/* 119:    */               case 1: 
/* 120:105 */                 pone = new ScriptPolicy();
/* 121:106 */                 break;
/* 122:    */               case 2: 
/* 123:    */               case 120: 
/* 124:109 */                 pone = new SqlPolicy();
/* 125:110 */                 break;
/* 126:    */               case 13: 
/* 127:112 */                 pone = new YueJiePolicy();
/* 128:113 */                 break;
/* 129:    */               case 14: 
/* 130:115 */                 pone = new YuLiangPolicy();
/* 131:116 */                 break;
/* 132:    */               default: 
/* 133:118 */                 log.info("undefine policy type:" + dbone.getType());
/* 134:119 */                 break;
/* 135:    */               }
/* 136:    */               
/* 137:121 */               pone.setId(dbone.getAlarmid());
/* 138:    */               
/* 139:123 */               pone.setSendid(dbone.getSendid());
/* 140:124 */               if (pone.getConfig(dbone.getPolicy()))
/* 141:    */               {
/* 142:126 */                 pone.setName(dbone.getName());
/* 143:127 */                 pList.add(pone);
/* 144:    */               }
/* 145:    */             }
/* 146:    */           }
/* 147:    */         }
/* 148:    */         catch (Exception e)
/* 149:    */         {
/* 150:129 */           String s = "";
/* 151:130 */           StackTraceElement[] st = e.getStackTrace();
/* 152:131 */           for (StackTraceElement stackTraceElement : st)
/* 153:    */           {
/* 154:132 */             String exclass = stackTraceElement.getClassName();
/* 155:133 */             String method = stackTraceElement.getMethodName();
/* 156:134 */             s = s + "[类:" + exclass + "]调用" + method + "时在第" + 
/* 157:135 */               stackTraceElement.getLineNumber() + "行代码处发生异常!异常类型:" + 
/* 158:136 */               e.getClass().getName();
/* 159:    */           }
/* 160:138 */           log.error("read policy define error " + s);
/* 161:139 */           Main.dealException(e);
/* 162:    */         }
/* 163:    */       }
/* 164:143 */       if (pList.size() == 0)
/* 165:    */       {
/* 166:144 */         log.info("no alarm policy,system exit!");
/* 167:145 */         System.exit(0);
/* 168:    */       }
/* 169:147 */       policies = new Policy[pList.size()];
/* 170:148 */       for (int i = 0; i < policies.length; i++) {
/* 171:149 */         policies[i] = ((Policy)pList.get(i));
/* 172:    */       }
/* 173:150 */       HashMap<String, Object> inmap1 = new HashMap();
/* 174:151 */       inmap1.put("App_Type", "APP_WDS");
/* 175:152 */       List<WdsHydroElements> lists = this.pubService.getHydroDataList(
/* 176:153 */         WdsHydroElements.class, inmap1);
/* 177:154 */       for (int i = 0; i < policies.length; i++) {
/* 178:155 */         policies[i].setName(lists);
/* 179:    */       }
/* 180:    */     }
/* 181:    */     catch (Exception e)
/* 182:    */     {
/* 183:    */       StackTraceElement[] st;
/* 184:    */       StackTraceElement stackTraceElement;
/* 185:    */       int i;
/* 186:158 */       String s = "";
/* 187:159 */        st = e.getStackTrace();
/* 188:160 */       for (StackTraceElement stackTraceElement1 : st)
/* 189:    */       {
/* 190:161 */         String exclass = stackTraceElement1.getClassName();
/* 191:162 */         String method = stackTraceElement1.getMethodName();
/* 192:163 */         s = s + "[类:" + exclass + "]调用" + method + "时在第" + 
/* 193:164 */           stackTraceElement1.getLineNumber() + "行代码处发生异常!异常类型:" + 
/* 194:165 */           e.getClass().getName();
/* 195:    */       }
/* 196:167 */       log.error("read policy define error " + s);
/* 197:168 */       System.exit(0);
/* 198:    */     }
/* 199:170 */     int c = 0;
/* 200:171 */     YueJiePolicy one = new YueJiePolicy();
/* 201:172 */     for (int i = 0; i < policies.length; i++) {
/* 202:174 */       if (policies[i].getType() == one.getType()) {
/* 203:175 */         c++;
/* 204:    */       }
/* 205:    */     }
/* 206:177 */     if (c == 0) {
/* 207:178 */       return;
/* 208:    */     }
/* 209:179 */     this.yjpolicies = new YueJiePolicy[c];
/* 210:180 */     c = 0;
/* 211:181 */     for (int i = 0; i < policies.length; i++) {
/* 212:183 */       if (policies[i].getType() == one.getType())
/* 213:    */       {
/* 214:185 */         this.yjpolicies[c] = ((YueJiePolicy)policies[i]);
/* 215:186 */         c++;
/* 216:    */       }
/* 217:    */     }
/* 218:    */   }
/* 219:    */   
/* 220:    */   public void run()
/* 221:    */   {
/* 222:    */     try
/* 223:    */     {
/* 224:203 */       sleep(3000L);
/* 225:    */     }
/* 226:    */     catch (InterruptedException e1)
/* 227:    */     {
/* 228:205 */       e1.printStackTrace();
/* 229:    */     }
/* 230:207 */     if (this.yjpolicies != null) {
/* 231:208 */       new RecvRtDataBrdThr(this.yjpolicies).start();
/* 232:    */     }
/* 233:210 */     while (!this.threadDone)
/* 234:    */     {
/* 235:    */       try
/* 236:    */       {
/* 237:214 */         Calendar cal = Calendar.getInstance();
/* 238:215 */         for (int i = 0; i < this.policyClass.length; i++)
/* 239:    */         {
/* 240:217 */           Policy p = this.policyClass[i];
/* 241:218 */           ArrayList<Policy> rp = p.needCheck(cal, policies);
/* 242:219 */           if (rp != null) {
/* 243:220 */             com.nari.slsd.hd.alarmcd.type.SharedData.monitors[this.policyIndex[i]].insertMonitor(cal, rp, p.getType());
/* 244:    */           }
/* 245:    */         }
/* 246:    */       }
/* 247:    */       catch (Exception e)
/* 248:    */       {
/* 249:245 */         e.printStackTrace();
/* 250:246 */         log.info(e.toString());
/* 251:247 */         System.exit(0);
/* 252:    */       }
/* 253:    */       try
/* 254:    */       {
/* 255:252 */         sleep(20000L);
/* 256:    */       }
/* 257:    */       catch (InterruptedException e)
/* 258:    */       {
/* 259:255 */         e.printStackTrace();
/* 260:    */       }
/* 261:    */     }
/* 262:    */   }
/* 263:    */ }


/* Location:           C:\Users\lauren\Desktop\HD.AlarmCD.jar
 * Qualified Name:     com.nari.slsd.hd.alarmcd.threads.CheckThread
 * JD-Core Version:    0.7.0.1
 */