package com.nari.slsd.hd.alarmcd.wsclient;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;
import javax.xml.bind.annotation.XmlSeeAlso;

@WebService(name="MonitorInfo", targetNamespace="http://monitor")
@SOAPBinding(style=SOAPBinding.Style.RPC)
@XmlSeeAlso({ObjectFactory.class})
public abstract interface MonitorInfo
{
  @WebMethod
  @WebResult(partName="return")
  public abstract AnyTypeArray getDirectoryInfo(@WebParam(name="arg0", partName="arg0") String paramString, @WebParam(name="arg1", partName="arg1") int paramInt, @WebParam(name="arg2", partName="arg2") long paramLong);
  
  @WebMethod
  public abstract void upLoadFile(@WebParam(name="arg0", partName="arg0") BinFile paramBinFile);
  
  @WebMethod
  @WebResult(partName="return")
  public abstract BinFile downFile(@WebParam(name="arg0", partName="arg0") String paramString);
}


/* Location:           C:\Users\lauren\Desktop\HD.AlarmCD.jar
 * Qualified Name:     com.nari.slsd.hd.alarmcd.wsclient.MonitorInfo
 * JD-Core Version:    0.7.0.1
 */