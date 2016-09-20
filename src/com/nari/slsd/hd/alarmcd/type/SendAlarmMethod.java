/*   1:    */ package com.nari.slsd.hd.alarmcd.type;
/*   2:    */ 
/*   3:    */ import java.io.PrintStream;
/*   4:    */ import java.io.UnsupportedEncodingException;
/*   5:    */ import java.net.DatagramPacket;
/*   6:    */ import java.net.DatagramSocket;
/*   7:    */ import java.net.InetAddress;
/*   8:    */ import java.net.SocketException;
/*   9:    */ import java.nio.ByteBuffer;
/*  10:    */ import java.nio.ByteOrder;
/*  11:    */ 
/*  12:    */ public class SendAlarmMethod
/*  13:    */ {
/*  14:    */   String serverIp;
/*  15:    */   int port;
/*  16:    */   DatagramSocket ds;
/*  17: 25 */   InetAddress ia = null;
/*  18: 26 */   int id = 0;
/*  19:    */   
/*  20:    */   public SendAlarmMethod(String sip, int p)
/*  21:    */     throws Exception
/*  22:    */   {
/*  23: 35 */     this.serverIp = sip;
/*  24: 36 */     this.port = p;
/*  25:    */     try
/*  26:    */     {
/*  27: 38 */       this.ia = InetAddress.getByName("255.255.255.255");
/*  28: 39 */       this.serverIp = sip;
/*  29:    */       
/*  30: 41 */       this.ds = new DatagramSocket(null);
/*  31: 42 */       this.ds.setBroadcast(true);
/*  32:    */     }
/*  33:    */     catch (SocketException e)
/*  34:    */     {
/*  35: 45 */       e.printStackTrace();
/*  36: 46 */       throw e;
/*  37:    */     }
/*  38:    */   }
/*  39:    */   
/*  40:    */   public synchronized boolean appSendAlarm(int t, String machine, int appType, String alarmid, String info, String detail)
/*  41:    */     throws SocketException
/*  42:    */   {
/*  43: 59 */     byte[] b1 = (byte[])null;byte[] b3 = (byte[])null;byte[] b4 = (byte[])null;byte[] b2 = (byte[])null;
/*  44:    */     try
/*  45:    */     {
/*  46: 61 */       b1 = machine.getBytes("GB2312");
/*  47: 62 */       if (alarmid != null) {
/*  48: 63 */         b2 = alarmid.getBytes("GB2312");
/*  49:    */       } else {
/*  50: 65 */         b2 = new byte[0];
/*  51:    */       }
/*  52: 66 */       b3 = info.getBytes("GB2312");
/*  53: 67 */       if (detail != null) {
/*  54: 68 */         b4 = detail.getBytes("GB2312");
/*  55:    */       } else {
/*  56: 70 */         b4 = new byte[0];
/*  57:    */       }
/*  58:    */     }
/*  59:    */     catch (UnsupportedEncodingException e2)
/*  60:    */     {
/*  61: 73 */       e2.printStackTrace();
/*  62: 74 */       return false;
/*  63:    */     }
/*  64: 77 */     if (b1.length + b2.length + b3.length + b4.length + 20 > 2000)
/*  65:    */     {
/*  66: 79 */       System.out.println("报警信息和主机名长度之和超出960个字节，被截断");
/*  67: 80 */       b4 = new byte[0];
/*  68: 81 */       if (b1.length + b2.length + b3.length + 20 > 2000)
/*  69:    */       {
/*  70: 83 */         byte[] bs = new byte[1980 - b1.length - b2.length];
/*  71: 84 */         for (int i = 0; i < bs.length; i++) {
/*  72: 85 */           bs[i] = b3[i];
/*  73:    */         }
/*  74: 86 */         b3 = bs;
/*  75:    */       }
/*  76:    */     }
/*  77: 89 */     ByteBuffer bb = ByteBuffer.allocate(b1.length + b2.length + b3.length + 
/*  78: 90 */       b4.length + 20);
/*  79: 91 */     bb.order(ByteOrder.BIG_ENDIAN);
/*  80: 92 */     bb.putInt(this.id);
/*  81: 93 */     bb.putInt(t);
/*  82: 94 */     bb.putInt(appType);
/*  83: 95 */     bb.putShort((short)b1.length);
/*  84: 96 */     bb.put(b1);
/*  85: 97 */     bb.putShort((short)b2.length);
/*  86: 98 */     if (b2.length > 0) {
/*  87: 99 */       bb.put(b2);
/*  88:    */     }
/*  89:100 */     bb.putShort((short)b3.length);
/*  90:101 */     bb.put(b3);
/*  91:102 */     bb.putShort((short)b4.length);
/*  92:103 */     if (b4.length > 0) {
/*  93:104 */       bb.put(b4);
/*  94:    */     }
/*  95:105 */     byte[] b = bb.array();
/*  96:    */     
/*  97:107 */     DatagramPacket dp = new DatagramPacket(b, 0, b.length, this.ia, this.port);
/*  98:    */     try
/*  99:    */     {
/* 100:109 */       this.ds.send(dp);
/* 101:110 */       this.id += 1;
/* 102:111 */       if (this.id >= 10000) {
/* 103:112 */         this.id = 0;
/* 104:    */       }
/* 105:113 */       return true;
/* 106:    */     }
/* 107:    */     catch (Exception e)
/* 108:    */     {
/* 109:115 */       e.printStackTrace();
/* 110:116 */       System.out.println("send alarm error");
/* 111:117 */       this.ds.close();
/* 112:    */       try
/* 113:    */       {
/* 114:119 */         this.ds = new DatagramSocket(null);
/* 115:120 */         this.ds.setBroadcast(true);
/* 116:    */       }
/* 117:    */       catch (SocketException e1)
/* 118:    */       {
/* 119:122 */         e1.printStackTrace();
/* 120:123 */         throw e1;
/* 121:    */       }
/* 122:    */     }
/* 123:125 */     return false;
/* 124:    */   }
/* 125:    */   
/* 126:    */   public synchronized boolean serverSendAlarm(int t, String alarmId, String info, String detail)
/* 127:    */     throws SocketException
/* 128:    */   {
/* 129:132 */     byte[] b1 = (byte[])null;byte[] b3 = (byte[])null;byte[] b4 = (byte[])null;
/* 130:    */     try
/* 131:    */     {
/* 132:134 */       b1 = alarmId.getBytes("UTF-8");
/* 133:135 */       b3 = info.getBytes("UTF-8");
/* 134:136 */       b4 = detail.getBytes("UTF-8");
/* 135:    */     }
/* 136:    */     catch (UnsupportedEncodingException e2)
/* 137:    */     {
/* 138:139 */       e2.printStackTrace();
/* 139:140 */       return false;
/* 140:    */     }
/* 141:143 */     if (b1.length + b3.length + b4.length + 14 > 2000)
/* 142:    */     {
/* 143:145 */       System.out.println("报警信息超出1000个字节，被截断");
/* 144:146 */       b4 = new byte[0];
/* 145:147 */       if (b1.length + b3.length + 14 > 2000)
/* 146:    */       {
/* 147:149 */         byte[] bs = new byte[1986 - b1.length];
/* 148:150 */         for (int i = 0; i < bs.length; i++) {
/* 149:151 */           bs[i] = b3[i];
/* 150:    */         }
/* 151:152 */         b3 = bs;
/* 152:    */       }
/* 153:    */     }
/* 154:155 */     ByteBuffer bb = ByteBuffer.allocate(b1.length + b3.length + 
/* 155:156 */       b4.length + 14);
/* 156:157 */     bb.order(ByteOrder.BIG_ENDIAN);
/* 157:158 */     bb.putInt(this.id);
/* 158:159 */     bb.putInt(t);
/* 159:160 */     bb.putShort((short)b1.length);
/* 160:161 */     bb.put(b1);
/* 161:162 */     bb.putShort((short)b3.length);
/* 162:163 */     bb.put(b3);
/* 163:164 */     bb.putShort((short)b4.length);
/* 164:165 */     if (b4.length > 0) {
/* 165:166 */       bb.put(b4);
/* 166:    */     }
/* 167:167 */     byte[] b = bb.array();
/* 168:    */     
/* 169:169 */     DatagramPacket dp = new DatagramPacket(b, 0, b.length, this.ia, this.port);
/* 170:    */     try
/* 171:    */     {
/* 172:171 */       this.ds.send(dp);
/* 173:172 */       this.id += 1;
/* 174:173 */       if (this.id == 10000) {
/* 175:174 */         this.id = 0;
/* 176:    */       }
/* 177:175 */       return true;
/* 178:    */     }
/* 179:    */     catch (Exception e)
/* 180:    */     {
/* 181:177 */       e.printStackTrace();
/* 182:178 */       System.out.println("send alarm error");
/* 183:179 */       this.ds.close();
/* 184:    */       try
/* 185:    */       {
/* 186:181 */         this.ds = new DatagramSocket(null);
/* 187:182 */         this.ds.setBroadcast(true);
/* 188:    */       }
/* 189:    */       catch (SocketException e1)
/* 190:    */       {
/* 191:184 */         e1.printStackTrace();
/* 192:185 */         throw e1;
/* 193:    */       }
/* 194:    */     }
/* 195:187 */     return false;
/* 196:    */   }
/* 197:    */ }


/* Location:           C:\Users\lauren\Desktop\HD.AlarmCD.jar
 * Qualified Name:     com.nari.slsd.hd.alarmcd.type.SendAlarmMethod
 * JD-Core Version:    0.7.0.1
 */