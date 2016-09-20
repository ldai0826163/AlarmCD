/*  1:   */ package com.nari.slsd.hd.alarmcd.wsclient;
/*  2:   */ 
/*  3:   */ import java.util.ArrayList;
/*  4:   */ import java.util.List;
/*  5:   */ import javax.xml.bind.annotation.XmlAccessType;
/*  6:   */ import javax.xml.bind.annotation.XmlAccessorType;
/*  7:   */ import javax.xml.bind.annotation.XmlElement;
/*  8:   */ import javax.xml.bind.annotation.XmlType;
/*  9:   */ 
/* 10:   */ @XmlAccessorType(XmlAccessType.FIELD)
/* 11:   */ @XmlType(name="anyTypeArray", namespace="http://jaxb.dev.java.net/array", propOrder={"item"})
/* 12:   */ public class AnyTypeArray
/* 13:   */ {
/* 14:   */   @XmlElement(nillable=true)
/* 15:   */   protected List<Object> item;
/* 16:   */   
/* 17:   */   public List<Object> getItem()
/* 18:   */   {
/* 19:63 */     if (this.item == null) {
/* 20:64 */       this.item = new ArrayList();
/* 21:   */     }
/* 22:66 */     return this.item;
/* 23:   */   }
/* 24:   */ }


/* Location:           C:\Users\lauren\Desktop\HD.AlarmCD.jar
 * Qualified Name:     com.nari.slsd.hd.alarmcd.wsclient.AnyTypeArray
 * JD-Core Version:    0.7.0.1
 */