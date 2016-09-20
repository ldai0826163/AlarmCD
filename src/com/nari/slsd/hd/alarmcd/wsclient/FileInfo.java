/*   1:    */ package com.nari.slsd.hd.alarmcd.wsclient;
/*   2:    */ 
/*   3:    */ import javax.xml.bind.annotation.XmlAccessType;
/*   4:    */ import javax.xml.bind.annotation.XmlAccessorType;
/*   5:    */ import javax.xml.bind.annotation.XmlType;
/*   6:    */ 
/*   7:    */ @XmlAccessorType(XmlAccessType.FIELD)
/*   8:    */ @XmlType(name="fileInfo", propOrder={"dir", "fileName", "size", "time"})
/*   9:    */ public class FileInfo
/*  10:    */ {
/*  11:    */   protected String dir;
/*  12:    */   protected String fileName;
/*  13:    */   protected long size;
/*  14:    */   protected long time;
/*  15:    */   
/*  16:    */   public String getDir()
/*  17:    */   {
/*  18: 54 */     return this.dir;
/*  19:    */   }
/*  20:    */   
/*  21:    */   public void setDir(String value)
/*  22:    */   {
/*  23: 66 */     this.dir = value;
/*  24:    */   }
/*  25:    */   
/*  26:    */   public String getFileName()
/*  27:    */   {
/*  28: 78 */     return this.fileName;
/*  29:    */   }
/*  30:    */   
/*  31:    */   public void setFileName(String value)
/*  32:    */   {
/*  33: 90 */     this.fileName = value;
/*  34:    */   }
/*  35:    */   
/*  36:    */   public long getSize()
/*  37:    */   {
/*  38: 98 */     return this.size;
/*  39:    */   }
/*  40:    */   
/*  41:    */   public void setSize(long value)
/*  42:    */   {
/*  43:106 */     this.size = value;
/*  44:    */   }
/*  45:    */   
/*  46:    */   public long getTime()
/*  47:    */   {
/*  48:114 */     return this.time;
/*  49:    */   }
/*  50:    */   
/*  51:    */   public void setTime(long value)
/*  52:    */   {
/*  53:122 */     this.time = value;
/*  54:    */   }
/*  55:    */ }


/* Location:           C:\Users\lauren\Desktop\HD.AlarmCD.jar
 * Qualified Name:     com.nari.slsd.hd.alarmcd.wsclient.FileInfo
 * JD-Core Version:    0.7.0.1
 */