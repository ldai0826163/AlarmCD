/*  1:   */ package com.nari.slsd.hd.alarmcd;
/*  2:   */ 
/*  3:   */ import java.awt.BorderLayout;
/*  4:   */ import java.awt.Dimension;
/*  5:   */ import javax.swing.JFrame;
/*  6:   */ import javax.swing.JPanel;
/*  7:   */ import javax.swing.JTabbedPane;
/*  8:   */ 
/*  9:   */ public class MainPane
/* 10:   */   extends JPanel
/* 11:   */ {
/* 12:   */   private static final long serialVersionUID = 1L;
/* 13:17 */   private LogPane logPane = new LogPane();
/* 14:18 */   private DBConfigPane dbcPane = new DBConfigPane();
/* 15:19 */   private JTabbedPane tabPanel = new JTabbedPane();
/* 16:   */   
/* 17:   */   public MainPane()
/* 18:   */   {
/* 19:22 */     init();
/* 20:   */   }
/* 21:   */   
/* 22:   */   public void init()
/* 23:   */   {
/* 24:25 */     this.tabPanel.addTab("系统日志", this.logPane);
/* 25:26 */     this.tabPanel.addTab("数据库配置", this.dbcPane);
/* 26:   */     
/* 27:28 */     setLayout(new BorderLayout());
/* 28:29 */     add(this.tabPanel, "Center");
/* 29:30 */     setMinimumSize(new Dimension(700, 600));
/* 30:   */   }
/* 31:   */   
/* 32:   */   public static void main(String[] args)
/* 33:   */   {
/* 34:34 */     JFrame frame = new JFrame("测试");
/* 35:35 */     frame.setLayout(new BorderLayout());
/* 36:36 */     MainPane panel = new MainPane();
/* 37:37 */     frame.add(panel, "Center");
/* 38:38 */     frame.setDefaultCloseOperation(3);
/* 39:   */     
/* 40:40 */     frame.setSize(new Dimension(800, 600));
/* 41:41 */     frame.setVisible(true);
/* 42:   */   }
/* 43:   */ }


/* Location:           C:\Users\lauren\Desktop\HD.AlarmCD.jar
 * Qualified Name:     com.nari.slsd.hd.alarmcd.MainPane
 * JD-Core Version:    0.7.0.1
 */