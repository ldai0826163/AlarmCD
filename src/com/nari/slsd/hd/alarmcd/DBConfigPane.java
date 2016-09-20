/*   1:    */ package com.nari.slsd.hd.alarmcd;
/*   2:    */ 
/*   3:    */ import com.nari.slsd.hd.alarmcd.type.MonitorQueueConfig;
/*   4:    */ import java.awt.BorderLayout;
/*   5:    */ import java.awt.FlowLayout;
/*   6:    */ import java.awt.event.ActionEvent;
/*   7:    */ import java.awt.event.ActionListener;
/*   8:    */ import java.util.Vector;
/*   9:    */ import javax.swing.DefaultCellEditor;
/*  10:    */ import javax.swing.DefaultListCellRenderer;
/*  11:    */ import javax.swing.ImageIcon;
/*  12:    */ import javax.swing.JButton;
/*  13:    */ import javax.swing.JComboBox;
/*  14:    */ import javax.swing.JPanel;
/*  15:    */ import javax.swing.JScrollPane;
/*  16:    */ import javax.swing.JTable;
/*  17:    */ import javax.swing.JToolBar;
/*  18:    */ import javax.swing.table.DefaultTableCellRenderer;
/*  19:    */ import javax.swing.table.TableColumn;
/*  20:    */ import javax.swing.table.TableColumnModel;
/*  21:    */ 
/*  22:    */ public class DBConfigPane
/*  23:    */   extends JPanel
/*  24:    */ {
/*  25: 36 */   static UserLog log = new UserLog();
/*  26:    */   private static DBConfigModel dbcs;
/*  27:    */   private JTable dbTable;
/*  28:    */   private JButton addButton;
/*  29:    */   private JButton delButton;
/*  30:    */   private JToolBar commonToolbar;
/*  31:    */   
/*  32:    */   public DBConfigPane()
/*  33:    */   {
/*  34: 44 */     init();
/*  35:    */   }
/*  36:    */   
/*  37:    */   public void getData()
/*  38:    */   {
/*  39: 48 */     if (dbcs != null)
/*  40:    */     {
/*  41: 50 */       dbcs.getDataVector().clear();
/*  42: 52 */       for (MonitorQueueConfig one : Config.mqconfigs)
/*  43:    */       {
/*  44: 54 */         String ps = "";
/*  45: 55 */         for (String s : one.policies) {
/*  46: 56 */           ps = ps + "," + s;
/*  47:    */         }
/*  48: 57 */         ps = ps.substring(1, ps.length());
/*  49: 58 */         if (one.type == 0) {
/*  50: 59 */           dbcs.addRow(new Object[] { "数据库", one.driver, one.url, one.user, one.pass, ps, String.valueOf(one.queueLen), String.valueOf(one.waitTime) });
/*  51:    */         } else {
/*  52: 61 */           dbcs.addRow(new Object[] { "数据服务", one.driver, one.url, one.user, one.pass, ps, String.valueOf(one.queueLen), String.valueOf(one.waitTime) });
/*  53:    */         }
/*  54:    */       }
/*  55: 65 */       dbcs.editRow = -1;
/*  56:    */     }
/*  57: 68 */     dbcs.fireTableDataChanged();
/*  58:    */   }
/*  59:    */   
/*  60:    */   public void init()
/*  61:    */   {
/*  62: 71 */     this.addButton = new JButton(new ImageIcon(ClassLoader.getSystemResource("resource/table_row_insert.png")));
/*  63: 72 */     this.addButton.setToolTipText("增加");
/*  64: 73 */     this.addButton.addActionListener(new ActionListener()
/*  65:    */     {
/*  66:    */       public void actionPerformed(ActionEvent e)
/*  67:    */       {
/*  68: 77 */         DBConfigPane.dbcs.addRow(new Vector());
/*  69: 78 */         JComboBox comboBox = new JComboBox();
/*  70: 79 */         comboBox.addItem("数据库");
/*  71: 80 */         comboBox.addItem("数据服务");
/*  72: 81 */         DefaultListCellRenderer r = new DefaultListCellRenderer();
/*  73: 82 */         r.setHorizontalAlignment(0);
/*  74: 83 */         comboBox.setRenderer(r);
/*  75: 84 */         DBConfigPane.this.dbTable.getColumnModel().getColumn(0).setCellEditor(new DefaultCellEditor(comboBox));
/*  76: 85 */         DBConfigPane.dbcs.editRow = (DBConfigPane.dbcs.getRowCount() - 1);
/*  77:    */       }
/*  78: 87 */     });
/*  79: 88 */     this.delButton = new JButton(new ImageIcon(ClassLoader.getSystemResource("resource/table_row_delete.png")));
/*  80:    */     
/*  81: 90 */     this.delButton.setToolTipText("删除");
/*  82: 91 */     this.delButton.addActionListener(new ActionListener()
/*  83:    */     {
/*  84:    */       public void actionPerformed(ActionEvent e)
/*  85:    */       {
/*  86: 95 */         if ((DBConfigPane.this.dbTable.getSelectedRow() >= 0) && (DBConfigPane.this.dbTable.getSelectedRow() < DBConfigPane.this.dbTable.getRowCount())) {
/*  87: 96 */           DBConfigPane.dbcs.removeRow(DBConfigPane.this.dbTable.getSelectedRow());
/*  88:    */         }
/*  89:    */       }
/*  90:111 */     });
/*  91:112 */     dbcs = new DBConfigModel();
/*  92:113 */     this.dbTable = new JTable(dbcs);
/*  93:114 */     DefaultTableCellRenderer render = new DefaultTableCellRenderer();
/*  94:115 */     render.setHorizontalAlignment(0);
/*  95:116 */     this.dbTable.setDefaultRenderer(Object.class, render);
/*  96:117 */     this.dbTable.setEnabled(true);
/*  97:118 */     this.dbTable.setSelectionMode(0);
/*  98:119 */     JComboBox comboBox = new JComboBox();
/*  99:120 */     comboBox.addItem("数据库");
/* 100:121 */     comboBox.addItem("数据服务");
/* 101:122 */     DefaultListCellRenderer r = new DefaultListCellRenderer();
/* 102:123 */     r.setHorizontalAlignment(0);
/* 103:124 */     comboBox.setRenderer(r);
/* 104:125 */     this.dbTable.getColumnModel().getColumn(0).setCellEditor(new DefaultCellEditor(comboBox));
/* 105:128 */     for (int i = 0; i < 7; i++)
/* 106:    */     {
/* 107:130 */       TableColumn column = this.dbTable.getColumnModel().getColumn(i);
/* 108:131 */       if (i == 0) {
/* 109:132 */         column.setPreferredWidth(120);
/* 110:134 */       } else if (i == 1) {
/* 111:135 */         column.setPreferredWidth(120);
/* 112:136 */       } else if (i == 2) {
/* 113:137 */         column.setPreferredWidth(220);
/* 114:138 */       } else if (i == 3) {
/* 115:139 */         column.setPreferredWidth(30);
/* 116:140 */       } else if (i == 4) {
/* 117:141 */         column.setPreferredWidth(30);
/* 118:142 */       } else if (i == 5) {
/* 119:143 */         column.setPreferredWidth(150);
/* 120:144 */       } else if (i == 6) {
/* 121:145 */         column.setPreferredWidth(30);
/* 122:146 */       } else if (i == 7) {
/* 123:147 */         column.setPreferredWidth(30);
/* 124:    */       }
/* 125:    */     }
/* 126:154 */     this.commonToolbar = new JToolBar();
/* 127:155 */     this.commonToolbar.add(this.addButton);
/* 128:156 */     this.commonToolbar.add(this.delButton);
/* 129:    */     
/* 130:158 */     setLayout(new BorderLayout());
/* 131:159 */     add(this.commonToolbar, "North");
/* 132:160 */     add(new JScrollPane(this.dbTable), "Center");
/* 133:161 */     add(getBtnPnl(), "South");
/* 134:162 */     getData();
/* 135:    */   }
/* 136:    */   
/* 137:    */   private JPanel getBtnPnl()
/* 138:    */   {
/* 139:168 */     JPanel panel = new JPanel();
/* 140:169 */     JButton okBtn = new JButton("确定");
/* 141:170 */     panel.setLayout(new FlowLayout(1));
/* 142:171 */     panel.add(okBtn);
/* 143:172 */     JButton cancelBtn = new JButton("取消");
/* 144:173 */     panel.add(cancelBtn);
/* 145:174 */     okBtn.addActionListener(new ActionListener()
/* 146:    */     {
/* 147:    */       public void actionPerformed(ActionEvent e)
/* 148:    */       {
/* 149:178 */         MonitorQueueConfig[] mqcs = new MonitorQueueConfig[DBConfigPane.dbcs.getRowCount()];
/* 150:179 */         for (int i = 0; i < DBConfigPane.dbcs.getRowCount(); i++)
/* 151:    */         {
/* 152:181 */           String type = (String)DBConfigPane.dbcs.getValueAt(i, 0);
/* 153:    */           
/* 154:183 */           String driver = null;String url = null;String user = null;String pass = null;
/* 155:184 */           if (type.equals("数据库"))
/* 156:    */           {
/* 157:186 */             driver = (String)DBConfigPane.dbcs.getValueAt(i, 1);
/* 158:187 */             url = (String)DBConfigPane.dbcs.getValueAt(i, 2);
/* 159:188 */             user = (String)DBConfigPane.dbcs.getValueAt(i, 3);
/* 160:189 */             pass = (String)DBConfigPane.dbcs.getValueAt(i, 4);
/* 161:    */           }
/* 162:191 */           String[] policies = ((String)DBConfigPane.dbcs.getValueAt(i, 5)).split(",");
/* 163:192 */           int len = Integer.parseInt((String)DBConfigPane.dbcs.getValueAt(i, 6));
/* 164:193 */           int wt = Integer.parseInt((String)DBConfigPane.dbcs.getValueAt(i, 7));
/* 165:194 */           MonitorQueueConfig one = null;
/* 166:195 */           if (type.equals("数据库")) {
/* 167:196 */             one = new MonitorQueueConfig(0, driver, url, user, pass, len, wt, policies);
/* 168:    */           } else {
/* 169:199 */             one = new MonitorQueueConfig(1, len, wt, policies);
/* 170:    */           }
/* 171:202 */           mqcs[i] = one;
/* 172:    */         }
/* 173:204 */         Config.writeXmlFile(mqcs);
/* 174:205 */         DBConfigPane.dbcs.editRow = -1;
/* 175:    */       }
/* 176:209 */     });
/* 177:210 */     cancelBtn.addActionListener(new ActionListener()
/* 178:    */     {
/* 179:    */       public void actionPerformed(ActionEvent e)
/* 180:    */       {
/* 181:214 */         DBConfigPane.this.getData();
/* 182:215 */         MonitorQueueConfig[] mqcs = new MonitorQueueConfig[DBConfigPane.dbcs.getRowCount()];
/* 183:216 */         for (int i = 0; i < DBConfigPane.dbcs.getRowCount(); i++)
/* 184:    */         {
/* 185:218 */           String type = (String)DBConfigPane.dbcs.getValueAt(i, 0);
/* 186:    */           
/* 187:220 */           String driver = null;String url = null;String user = null;String pass = null;
/* 188:221 */           if (type.equals("数据库"))
/* 189:    */           {
/* 190:223 */             driver = (String)DBConfigPane.dbcs.getValueAt(i, 1);
/* 191:224 */             url = (String)DBConfigPane.dbcs.getValueAt(i, 2);
/* 192:225 */             user = (String)DBConfigPane.dbcs.getValueAt(i, 3);
/* 193:226 */             pass = (String)DBConfigPane.dbcs.getValueAt(i, 4);
/* 194:    */           }
/* 195:228 */           String[] policies = ((String)DBConfigPane.dbcs.getValueAt(i, 5)).split(",");
/* 196:229 */           int len = Integer.parseInt((String)DBConfigPane.dbcs.getValueAt(i, 6));
/* 197:230 */           int wt = Integer.parseInt((String)DBConfigPane.dbcs.getValueAt(i, 7));
/* 198:231 */           MonitorQueueConfig one = null;
/* 199:232 */           if (type.equals("数据库")) {
/* 200:233 */             one = new MonitorQueueConfig(0, driver, url, user, pass, len, wt, policies);
/* 201:    */           } else {
/* 202:236 */             one = new MonitorQueueConfig(1, len, wt, policies);
/* 203:    */           }
/* 204:239 */           mqcs[i] = one;
/* 205:    */         }
/* 206:241 */         DBConfigPane.dbcs.editRow = -1;
/* 207:    */       }
/* 208:242 */     });
/* 209:243 */     return panel;
/* 210:    */   }
/* 211:    */ }


/* Location:           C:\Users\lauren\Desktop\HD.AlarmCD.jar
 * Qualified Name:     com.nari.slsd.hd.alarmcd.DBConfigPane
 * JD-Core Version:    0.7.0.1
 */