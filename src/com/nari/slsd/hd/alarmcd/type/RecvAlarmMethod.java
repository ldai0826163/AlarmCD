/*  1:   */ package com.nari.slsd.hd.alarmcd.type;
/*  2:   */ 
/*  3:   */ import com.nari.slsd.hd.model.PubDataAlarmdb;
/*  4:   */ import com.nari.slsd.hd.model.PubDataAlarmdbId;
/*  5:   */ import java.net.DatagramPacket;
/*  6:   */ import java.net.DatagramSocket;
/*  7:   */ import java.net.InetSocketAddress;
/*  8:   */ import java.nio.ByteBuffer;
/*  9:   */ import java.nio.ByteOrder;
/* 10:   */ import java.sql.Timestamp;
/* 11:   */ 
/* 12:   */ public class RecvAlarmMethod
/* 13:   */ {
/* 14:   */   DatagramSocket socket;
/* 15:   */   DatagramPacket packet;
/* 16:   */   int id;
/* 17:   */   byte[] recvBuf;
/* 18:   */   
/* 19:   */   public int getId()
/* 20:   */   {
/* 21:20 */     return this.id;
/* 22:   */   }
/* 23:   */   
/* 24:   */   public void setId(int id)
/* 25:   */   {
/* 26:23 */     this.id = id;
/* 27:   */   }
/* 28:   */   
/* 29:   */   public RecvAlarmMethod(int p)
/* 30:   */     throws Exception
/* 31:   */   {
/* 32:28 */     this.id = -1;
/* 33:29 */     this.socket = new DatagramSocket(null);
/* 34:30 */     this.socket.setReuseAddress(true);
/* 35:31 */     this.socket.bind(new InetSocketAddress(p));
/* 36:32 */     this.recvBuf = new byte[2000];
/* 37:33 */     this.packet = new DatagramPacket(this.recvBuf, 2000);
/* 38:   */   }
/* 39:   */   
/* 40:   */   public PubDataAlarmdb recvAlarm()
/* 41:   */     throws Exception
/* 42:   */   {
/* 43:37 */     this.socket.receive(this.packet);
/* 44:38 */     ByteBuffer bb = ByteBuffer.wrap(this.recvBuf);
/* 45:39 */     bb.order(ByteOrder.BIG_ENDIAN);
/* 46:40 */     int newid = bb.getInt();
/* 47:41 */     if (this.id == newid) {
/* 48:42 */       return null;
/* 49:   */     }
/* 50:43 */     this.id = newid;
/* 51:44 */     int t = bb.getInt();
/* 52:45 */     short len = bb.getShort();
/* 53:46 */     byte[] buff = new byte[len];
/* 54:47 */     bb.get(buff);
/* 55:48 */     String alarmid = new String(buff, "GB2312");
/* 56:49 */     PubDataAlarmdb alarmdb = new PubDataAlarmdb();
/* 57:50 */     PubDataAlarmdbId dbid = new PubDataAlarmdbId(alarmid, new Timestamp(t * 1000L));
/* 58:51 */     alarmdb.setId(dbid);
/* 59:52 */     len = bb.getShort();
/* 60:53 */     buff = new byte[len];
/* 61:54 */     bb.get(buff);
/* 62:55 */     alarmdb.setContent(new String(buff, "GB2312"));
/* 63:56 */     len = bb.getShort();
/* 64:57 */     buff = new byte[len];
/* 65:58 */     bb.get(buff);
/* 66:59 */     alarmdb.setKeys(buff);
/* 67:60 */     return alarmdb;
/* 68:   */   }
/* 69:   */ }


/* Location:           C:\Users\lauren\Desktop\HD.AlarmCD.jar
 * Qualified Name:     com.nari.slsd.hd.alarmcd.type.RecvAlarmMethod
 * JD-Core Version:    0.7.0.1
 */