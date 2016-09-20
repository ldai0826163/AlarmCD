/*  1:   */ package com.nari.slsd.hd.alarmcd;
/*  2:   */ 
/*  3:   */ import javax.swing.table.DefaultTableModel;
/*  4:   */ 
/*  5:   */ public class DBConfigModel
/*  6:   */   extends DefaultTableModel
/*  7:   */ {
/*  8:   */   private static final long serialVersionUID = 1L;
/*  9:   */   
/* 10:   */   public DBConfigModel()
/* 11:   */   {
/* 12:17 */     super(new String[] { "数据来源", "驱动", "地址", "用户名", "密码", "应用", "队列长度", "等待分钟" }, 0);
/* 13:   */   }
/* 14:   */   
/* 15:35 */   int editRow = -1;
/* 16:   */   
/* 17:   */   public boolean isCellEditable(int row, int col)
/* 18:   */   {
/* 19:38 */     return true;
/* 20:   */   }
/* 21:   */   
/* 22:   */   public Class<?> getColumnClass(int arg0)
/* 23:   */   {
/* 24:45 */     return String.class;
/* 25:   */   }
/* 26:   */ }


/* Location:           C:\Users\lauren\Desktop\HD.AlarmCD.jar
 * Qualified Name:     com.nari.slsd.hd.alarmcd.DBConfigModel
 * JD-Core Version:    0.7.0.1
 */