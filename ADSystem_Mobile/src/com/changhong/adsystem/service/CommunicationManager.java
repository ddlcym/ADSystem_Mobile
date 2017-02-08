package com.changhong.adsystem.service;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.xmlpull.v1.XmlPullParserException;

import com.changhong.adsystem.model.JsonObject;
import com.changhong.adsystem.p2p.SocketUtil;
import com.changhong.adsystem.utils.Configure;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

public class CommunicationManager{

	//是否有效
	String LOG_TAG = "YDInfor";
	private int communicationType = 0;
	String mMethodName, Service_URL,NAMESPACE ;
	Map<String, Object> mParams;
	private Handler mMsgHandler;
	private List<Handler> reCallHandlers=new ArrayList<Handler>();
	private static CommunicationManager instance=null;
	private Thread mMsgThread;
	private static final  int MAX_BOSS_METHOD=5; 
	private static final  int MAX_RC_METHOD=3; 

	private CommunicationManager(){
		init();
	}
	public static CommunicationManager getInstance(){
		if(null == instance){
			instance= new CommunicationManager();			
		}		
		return instance;
	}
		
	public void init() {
		//启动通讯线程		
		CommunicationThread commThread=new CommunicationThread();
		mMsgThread=new Thread(commThread);
		mMsgThread.start();	
		System.out.println("mMsgThread is "+mMsgThread.getName());
		try {
				Thread.sleep(1000);
			} catch (Exception e) {
				e.printStackTrace();
			}
	}
	
	public void postMessage(Handler parentHandler, int type,  Map<String, Object> params){
		
		System.out.println("postMessage: Thread is "+Thread.currentThread().getName());

		if(null != mMsgHandler){//发送消息给子线程			
			
			Message sendMsg=mMsgHandler.obtainMessage();
			sendMsg.what=type;
			sendMsg.obj=params;
			reCallHandlers.add(parentHandler);
			mMsgHandler.sendMessage(sendMsg);
			
		}		
	}
	
	
	
	
	
	class CommunicationThread implements Runnable{

		
		public void run() {
			
			//创建handler之前先初始化Looper
			Looper.prepare();
			
			mMsgHandler= new Handler() {	
				
				public void handleMessage(Message msg) {
					
//					System.out.println("CommunicationThread: Thread is "+Thread.currentThread().getName());
                   if(reCallHandlers.isEmpty())return;
					communicationType = msg.what;
					mParams = (Map<String, Object>) msg.obj;
					
					Handler calltHandler=reCallHandlers.remove(reCallHandlers.size()-1);
					Message commMsg=calltHandler.obtainMessage();
 					commMsg.what=Configure.MSG_FAILURE;

					//接收来之通讯线程的消息
					switch (communicationType) {
	     			
	     			case Configure.MSG_HTTP_GET:
							
 						commMsg.arg1 = Configure.MSG_HTTP_GET;
 						calltHandler.sendMessage(commMsg);
 						
//	     				try {
	     					
// 					        Object webServiceResult = WSUtil.getObjectByCallingWS(
// 							NAMESPACE,mMethodName, mParams,
// 							Service_URL);
// 					        
//	     					if (null != webServiceResult) {// 请求成功
//	     						//冰箱接收 						
//	     						List<Map<String, Object>> rowMapList = WSObjectMapUtil.getRowMapList(webServiceResult);					
//	 							// 通过Message对象传递结果
//	     						commMsg.what=Configure.MSG_SUCCESS;
//	     						commMsg.arg1 = Configure.MSG_WEBSERVICE;
//	     						commMsg.obj = rowMapList; 						
////	     					}
//
//	     				} catch (IOException e2) {
//	     					e2.printStackTrace();
//	     				} catch (XmlPullParserException e2) {
//	     					e2.printStackTrace();
//	     				} finally {
//
//	     					mMainHandler.sendMessage(commMsg);
//	     					try {
//	     						Thread.sleep(1000);
//	     					} catch (Exception e) {
//	     						e.printStackTrace();
//	     					}
//	     				}
 						
	     				break;	
	     			case Configure.MSG_SOCKET://本地通讯
	     				
	     				try {     					
	     					JsonObject socketResult =SocketUtil.getObjectByCallingBox(mParams);
	     					if (null != socketResult) {// 请求成功	     						
	     						// 通过Message对象传递结果
	     						commMsg.what=Configure.MSG_SUCCESS;
	     						commMsg.arg1 = Configure.MSG_SOCKET;
	     						commMsg.obj = socketResult;
	     					}
	     				} catch (IOException e1) {
	     					e1.printStackTrace();
	     				} catch (XmlPullParserException e1) {
	     					e1.printStackTrace();
	     				} finally {
     						calltHandler.sendMessage(commMsg);
	     				}
	     				break;
                
	     			default:
	     				break;
	     		}		
				}				
			};
			Looper.loop();
		}		
	}
	
	
	private Handler getFirstHandler(){
		Handler firstHandler=null;
		  if(reCallHandlers.size()>0){
			  firstHandler=reCallHandlers.remove(reCallHandlers.size()-1);
		  }else{
			  firstHandler=new Handler();
		  }
		  return firstHandler;
	}
	
	
		
	//释放资源
	public void onDestroy() {		
		SocketUtil.free();
	} 
	

}

