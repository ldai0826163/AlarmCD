/*  1:   */ package com.nari.slsd.hd.alarmcd.send;
/*  2:   */ 
/*  3:   */ import java.io.Serializable;
/*  4:   */ import javax.xml.bind.annotation.XmlAttribute;
/*  5:   */ import javax.xml.bind.annotation.XmlElement;
/*  6:   */ import javax.xml.bind.annotation.XmlRootElement;
/*  7:   */ 
/*  8:   */ @XmlRootElement
/*  9:   */ public class RushToDealPara
/* 10:   */   implements Serializable
/* 11:   */ {
/* 12:   */   private static final long serialVersionUID = 1L;
/* 13:   */   @XmlAttribute
/* 14:13 */   public String Name = "";
/* 15:   */   @XmlElement
/* 16:   */   public String[] WarnPeople_Voice_MTel;
/* 17:   */   @XmlElement
/* 18:   */   public String[] WarnPeople_Voice_Tel;
/* 19:   */   @XmlElement
/* 20:   */   public String[] WarnPeople_Mail;
/* 21:   */   @XmlElement
/* 22:   */   public String[] WarnPeople_SM;
/* 23:   */   @XmlAttribute
/* 24:   */   public String AddInfo;
/* 25:   */   @XmlAttribute
/* 26:25 */   public int RedialTimeSpan = 30;
/* 27:   */ }


/* Location:           C:\Users\lauren\Desktop\HD.AlarmCD.jar
 * Qualified Name:     com.nari.slsd.hd.alarmcd.send.RushToDealPara
 * JD-Core Version:    0.7.0.1
 */