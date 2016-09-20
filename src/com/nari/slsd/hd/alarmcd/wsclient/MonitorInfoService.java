/*  1:   */ package com.nari.slsd.hd.alarmcd.wsclient;
/*  2:   */ 
/*  3:   */ import java.net.MalformedURLException;
/*  4:   */ import java.net.URL;
/*  5:   */ import java.util.logging.Logger;
/*  6:   */ import javax.xml.namespace.QName;
/*  7:   */ import javax.xml.ws.Service;
/*  8:   */ import javax.xml.ws.WebEndpoint;
/*  9:   */ import javax.xml.ws.WebServiceClient;
/* 10:   */ import javax.xml.ws.WebServiceFeature;
/* 11:   */ 
/* 12:   */ @WebServiceClient(name="MonitorInfoService", targetNamespace="http://monitor", wsdlLocation="http://10.144.118.84:9900/MonitorService?wsdl")
/* 13:   */ public class MonitorInfoService
/* 14:   */   extends Service
/* 15:   */ {
/* 16:   */   private static final URL MONITORINFOSERVICE_WSDL_LOCATION;
/* 17:26 */   private static final Logger logger = Logger.getLogger(MonitorInfoService.class.getName());
/* 18:   */   
/* 19:   */   static
/* 20:   */   {
/* 21:29 */     URL url = null;
/* 22:   */     try
/* 23:   */     {
/* 24:32 */       URL baseUrl = MonitorInfoService.class.getResource(".");
/* 25:33 */       url = new URL(baseUrl, "http://10.144.118.84:9900/MonitorService?wsdl");
/* 26:   */     }
/* 27:   */     catch (MalformedURLException e)
/* 28:   */     {
/* 29:35 */       logger.warning("Failed to create URL for the wsdl Location: 'http://10.144.118.84:9900/MonitorService?wsdl', retrying as a local file");
/* 30:36 */       logger.warning(e.getMessage());
/* 31:   */     }
/* 32:38 */     MONITORINFOSERVICE_WSDL_LOCATION = url;
/* 33:   */   }
/* 34:   */   
/* 35:   */   public static URL getUrl(String s)
/* 36:   */   {
/* 37:42 */     URL url = null;
/* 38:   */     try
/* 39:   */     {
/* 40:45 */       URL baseUrl = MonitorInfoService.class.getResource(".");
/* 41:46 */       url = new URL(baseUrl, s);
/* 42:   */     }
/* 43:   */     catch (MalformedURLException e)
/* 44:   */     {
/* 45:48 */       logger.warning("Failed to create URL for the wsdl Location: 's', retrying as a local file");
/* 46:49 */       logger.warning(e.getMessage());
/* 47:   */     }
/* 48:51 */     return url;
/* 49:   */   }
/* 50:   */   
/* 51:   */   public MonitorInfoService(URL wsdlLocation, QName serviceName)
/* 52:   */   {
/* 53:54 */     super(wsdlLocation, serviceName);
/* 54:   */   }
/* 55:   */   
/* 56:   */   public MonitorInfoService()
/* 57:   */   {
/* 58:58 */     super(MONITORINFOSERVICE_WSDL_LOCATION, new QName("http://monitor", "MonitorInfoService"));
/* 59:   */   }
/* 60:   */   
/* 61:   */   @WebEndpoint(name="MonitorInfoPort")
/* 62:   */   public MonitorInfo getMonitorInfoPort()
/* 63:   */   {
/* 64:68 */     return (MonitorInfo)super.getPort(new QName("http://monitor", "MonitorInfoPort"), MonitorInfo.class);
/* 65:   */   }
/* 66:   */   
/* 67:   */   @WebEndpoint(name="MonitorInfoPort")
/* 68:   */   public MonitorInfo getMonitorInfoPort(WebServiceFeature... features)
/* 69:   */   {
/* 70:80 */     return (MonitorInfo)super.getPort(new QName("http://monitor", "MonitorInfoPort"), MonitorInfo.class, features);
/* 71:   */   }
/* 72:   */ }


/* Location:           C:\Users\lauren\Desktop\HD.AlarmCD.jar
 * Qualified Name:     com.nari.slsd.hd.alarmcd.wsclient.MonitorInfoService
 * JD-Core Version:    0.7.0.1
 */