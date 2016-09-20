/*  1:   */ package com.nari.slsd.hd.alarmcd.threads;
/*  2:   */ 
/*  3:   */ import com.nari.slsd.hd.alarmcd.Config;
/*  4:   */ import com.nari.slsd.hd.alarmcd.UserLog;
/*  5:   */ import com.nari.slsd.hd.alarmcd.policies.YueJiePolicy;
/*  6:   */ import java.net.DatagramPacket;
/*  7:   */ import java.net.DatagramSocket;
/*  8:   */ import java.net.InetSocketAddress;
/*  9:   */ import java.net.SocketException;
/* 10:   */ import java.nio.ByteBuffer;
/* 11:   */ import java.nio.ByteOrder;
/* 12:   */ import java.util.GregorianCalendar;
/* 13:   */ 
/* 14:   */ public class RecvRtDataBrdThr
/* 15:   */   extends Thread
/* 16:   */ {
/* 17:20 */   private static UserLog log = new UserLog();
/* 18:   */   private DatagramSocket socket;
/* 19:   */   YueJiePolicy[] yjpolicies;
/* 20:   */   
/* 21:   */   public RecvRtDataBrdThr(YueJiePolicy[] policies)
/* 22:   */   {
/* 23:25 */     this.yjpolicies = policies;
/* 24:   */   }
/* 25:   */   
/* 26:   */   public void run()
/* 27:   */   {
/* 28:   */     try
/* 29:   */     {
/* 30:29 */       this.socket = new DatagramSocket(null);
/* 31:30 */       this.socket.setReuseAddress(true);
/* 32:31 */       this.socket.bind(new InetSocketAddress(Config.rtDataPort));
/* 33:   */     }
/* 34:   */     catch (Exception e)
/* 35:   */     {
/* 36:33 */       log.info("Listen at " + Config.rtDataPort + " Failed.");
/* 37:34 */       return;
/* 38:   */     }
/* 39:37 */     log.info("在端口" + Config.rtDataPort + "接收实时数据");
/* 40:   */     
/* 41:39 */     int lastId = -1;
/* 42:40 */     int lastT = -1;
/* 43:   */     for (;;)
/* 44:   */     {
/* 45:   */       try
/* 46:   */       {
/* 47:43 */         byte[] recvBuf = new byte['È'];
/* 48:44 */         DatagramPacket packet = new DatagramPacket(recvBuf, 200);
/* 49:45 */         this.socket.receive(packet);
/* 50:46 */         ByteBuffer bb = ByteBuffer.wrap(recvBuf);
/* 51:47 */         bb.order(ByteOrder.BIG_ENDIAN);
/* 52:   */         
/* 53:   */ 
/* 54:   */ 
/* 55:   */ 
/* 56:   */ 
/* 57:   */ 
/* 58:   */ 
/* 59:55 */         int type = bb.getInt();
/* 60:56 */         if (type == 3)
/* 61:   */         {
/* 62:58 */           int sendId = bb.getInt();
/* 63:59 */           int sendt = bb.getInt();
/* 64:60 */           if ((lastId != sendId) || (lastT != sendt))
/* 65:   */           {
/* 66:62 */             long id = bb.getLong();
/* 67:63 */             int time = bb.getInt();
/* 68:64 */             Double value = Double.valueOf(bb.getDouble());
/* 69:65 */             lastT = sendt;
/* 70:66 */             lastId = sendId;
/* 71:67 */             GregorianCalendar gc = new GregorianCalendar();
/* 72:68 */             gc.setTimeInMillis(1000L * time);
/* 73:69 */             if (System.currentTimeMillis() - Config.yuejieTime * 60000L < 1000L * time)
/* 74:   */             {
/* 75:72 */               int i = 0; 
/* 76:73 */               YueJiePolicy p = this.yjpolicies[i];
/* 77:74 */               if (p.getSenid() == id)
/* 78:   */               {
/* 79:75 */                 p.check(gc, value.doubleValue());
/* 80:   */               }
/* 81:   */               else
/* 82:   */               {
/* 83:72 */                 i++;
/* 84:72 */                 if (i >= this.yjpolicies.length) {}
/* 85:   */               }
/* 86:   */             }
/* 87:   */           }
/* 88:   */         }
/* 89:   */       }
/* 90:   */       catch (Exception e)
/* 91:   */       {
/* 92:81 */         e.printStackTrace();
/* 93:82 */         log.error(e.toString());
/* 94:83 */         this.socket.close();
/* 95:   */         try
/* 96:   */         {
/* 97:85 */           this.socket = new DatagramSocket(null);
/* 98:86 */           this.socket.setReuseAddress(true);
/* 99:87 */           this.socket.bind(new InetSocketAddress(Config.rtDataPort));
/* :0:   */         }
/* :1:   */         catch (SocketException e1)
/* :2:   */         {
/* :3:89 */           e1.printStackTrace();
/* :4:90 */           log.error(e1.toString());
/* :5:   */         }
/* :6:   */       }
/* :7:   */     }
/* :8:   */   }
/* :9:   */ }


/* Location:           C:\Users\lauren\Desktop\HD.AlarmCD.jar
 * Qualified Name:     com.nari.slsd.hd.alarmcd.threads.RecvRtDataBrdThr
 * JD-Core Version:    0.7.0.1
 */