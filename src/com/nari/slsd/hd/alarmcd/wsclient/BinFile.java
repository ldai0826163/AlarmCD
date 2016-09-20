/*  1:   */ package com.nari.slsd.hd.alarmcd.wsclient;
/*  2:   */ 
/*  3:   */ import javax.activation.DataHandler;
/*  4:   */ import javax.xml.bind.annotation.XmlAccessType;
/*  5:   */ import javax.xml.bind.annotation.XmlAccessorType;
/*  6:   */ import javax.xml.bind.annotation.XmlMimeType;
/*  7:   */ import javax.xml.bind.annotation.XmlType;
/*  8:   */ 
/*  9:   */ @XmlAccessorType(XmlAccessType.FIELD)
/* 10:   */ @XmlType(name="binFile", propOrder={"data", "title"})
/* 11:   */ public class BinFile
/* 12:   */ {
/* 13:   */   @XmlMimeType("application/octet-stream")
/* 14:   */   protected DataHandler data;
/* 15:   */   protected String title;
/* 16:   */   
/* 17:   */   public DataHandler getData()
/* 18:   */   {
/* 19:51 */     return this.data;
/* 20:   */   }
/* 21:   */   
/* 22:   */   public void setData(DataHandler value)
/* 23:   */   {
/* 24:63 */     this.data = value;
/* 25:   */   }
/* 26:   */   
/* 27:   */   public String getTitle()
/* 28:   */   {
/* 29:75 */     return this.title;
/* 30:   */   }
/* 31:   */   
/* 32:   */   public void setTitle(String value)
/* 33:   */   {
/* 34:87 */     this.title = value;
/* 35:   */   }
/* 36:   */ }


/* Location:           C:\Users\lauren\Desktop\HD.AlarmCD.jar
 * Qualified Name:     com.nari.slsd.hd.alarmcd.wsclient.BinFile
 * JD-Core Version:    0.7.0.1
 */