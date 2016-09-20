/*  1:   */ package com.nari.slsd.hd.alarmcd;
/*  2:   */ 
/*  3:   */ import java.awt.BorderLayout;
/*  4:   */ import java.util.LinkedList;
/*  5:   */ import java.util.concurrent.locks.ReentrantLock;
/*  6:   */ import javax.swing.JPanel;
/*  7:   */ import javax.swing.JScrollPane;
/*  8:   */ import javax.swing.JTextArea;
/*  9:   */ import javax.swing.SwingWorker;
/* 10:   */ 
/* 11:   */ public class LogPane
/* 12:   */   extends JPanel
/* 13:   */ {
/* 14:   */   private static final long serialVersionUID = 1L;
/* 15:   */   private JTextArea textArea;
/* 16:   */   private JScrollPane scrPnl_log;
/* 17:   */   
/* 18:   */   public LogPane()
/* 19:   */   {
/* 20:46 */     this.textArea = new JTextArea();
/* 21:47 */     this.scrPnl_log = new JScrollPane();
/* 22:48 */     this.scrPnl_log.setViewportView(this.textArea);
/* 23:   */     
/* 24:50 */     setLayout(new BorderLayout());
/* 25:51 */     add("Center", this.scrPnl_log);
/* 26:52 */     init();
/* 27:   */   }
/* 28:   */   
/* 29:   */   public void init()
/* 30:   */   {
/* 31:81 */     new SwingWorker()
/* 32:   */     {
/* 33:   */       protected String doInBackground()
/* 34:   */         throws Exception
/* 35:   */       {
/* 36:   */         for (;;)
/* 37:   */         {
/* 38:62 */           showToTextField();
/* 39:63 */           Thread.sleep(5000L);
/* 40:   */         }
/* 41:   */       }
/* 42:   */       
/* 43:   */       private void showToTextField()
/* 44:   */       {
/* 45:72 */         StringBuilder sb = new StringBuilder();
/* 46:73 */         UserLog.ilock.lock();
/* 47:74 */         for (int i = 0; i < UserLog.strs.size(); i++) {
/* 48:75 */           sb.append((String)UserLog.strs.get(i) + "\n");
/* 49:   */         }
/* 50:77 */         UserLog.ilock.unlock();
/* 51:78 */         if (!sb.toString().equals(LogPane.this.textArea.getText())) {
/* 52:79 */           LogPane.this.textArea.setText(sb.toString());
/* 53:   */         }
/* 54:   */       }
/* 55:   */     }.execute();
/* 56:   */   }
/* 57:   */ }


/* Location:           C:\Users\lauren\Desktop\HD.AlarmCD.jar
 * Qualified Name:     com.nari.slsd.hd.alarmcd.LogPane
 * JD-Core Version:    0.7.0.1
 */