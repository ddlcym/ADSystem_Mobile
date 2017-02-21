package com.changhong.adsystem.p2p;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.Toast;

import com.changhong.adsystem.activity.MyApp;
import com.changhong.adsystem.model.JsonPackage;
import com.changhong.adsystem.utils.ServiceConfig;
import com.changhong.adsystem_mobile.R;

public class P2PService {

	private static final String Tag = "P2PService::";
	private static P2PService intance;
	private Handler mParentHandler;
	private Handler mMsgHandler;
	private TCPClient mTCPClient=null;
	// socketServer接收服务器：
	private Thread mSocketCommunication = null;
	private boolean isConnected=false;
	
	private P2PService() {
		initP2PService();
	}

	public static P2PService creatP2PService() {
		if (null == intance) {
			intance = new P2PService();
		}
		return intance;
	}

	/**
	 * 初始化全局变量，启动那个服务器
	 */
	private void initP2PService() {
		
		// 启动接收线程:
		clientCommunicationThread commThread = new clientCommunicationThread();
		mSocketCommunication = new Thread(commThread);
		mSocketCommunication.start();		

	}
	
	/**
	 * 创建TCp连接
	 */
	public void creatTcpConnect(){
		mTCPClient=TCPClient.instance();
		if(null != mMsgHandler){
		   mMsgHandler.sendEmptyMessage(ServiceConfig.TCP_SOCKET_TYPE_CREATECONNECT);
		}
	}
	

	/**
	 * 接收端返回的信息
	 * 
	 * @return
	 */
	public void communicationWithBox(Handler handler, int action, String msg) {

		mParentHandler = handler;
		// 发送消息给子线程
		if (null != mMsgHandler) {
			mMsgHandler.removeMessages(ServiceConfig.TCP_SOCKET_TYPE_SEND_BEATS);
			Message sendMsg = mMsgHandler.obtainMessage();
			sendMsg.what=ServiceConfig.TCP_SOCKET_TYPE_REQUESR;
			sendMsg.arg1 = action;
			sendMsg.obj = msg;
			mMsgHandler.sendMessage(sendMsg);
		}
	}

	public void close() {		
		if (null != mTCPClient) {
			mTCPClient.stopReceiveTask();
			mTCPClient.tcpSocketClose();			
			mTCPClient = null;
		}
	}

	int beatsCount=ServiceConfig.SOCKET_MAX_WATING_TIME;
	/********************************************************** clientCommunicationThread *******************************************************************/

	private class clientCommunicationThread implements Runnable {

		String mAction = "";

		@Override
		public void run() {

			// 创建handler之前先初始化Looper
			Looper.prepare();

			mMsgHandler = new Handler() {
				public void handleMessage(Message msg) {
					boolean isSendBeats=true;
					switch(msg.what){
					case ServiceConfig.TCP_SOCKET_TYPE_REQUESR:
					
						String sendMsg = null;
						mAction = matchAction(msg.arg1);
						sendMsg = (String) msg.obj;
						if (null != mAction && !mAction.isEmpty() && null != sendMsg && !sendMsg.isEmpty()) {
							sendMsg = JsonPackage.formateTcpSendMsg(mAction,sendMsg);
							sendMsg=sendMsg.replace("\\","").replace(" ", "");
							sendTcpMsg(mParentHandler,mAction,JsonPackage.sendMsgToByte(sendMsg));						
						}
						break;
					case ServiceConfig.TCP_SOCKET_TYPE_SEND_BEATS:						
						sendTcpMsg(this,ServiceConfig.TCP_SOCKET_BEATS,JsonPackage.sendMsgToByte(ServiceConfig.TCP_SOCKET_BEATS));						
						break;
                   case ServiceConfig.TCP_SOCKET_TYPE_RESPOND:	
                	    String action=(String) msg.obj;
                	    if(action.equals(ServiceConfig.TCPS_SERVER_FILEDOWNLOAD_FINISHED)){
                	    	Toast.makeText(MyApp.getContext(), R.string.ad_res_download_finished, Toast.LENGTH_SHORT).show();
                	    }else if(action.contains(ServiceConfig.TCP_SOCKET_BEATS)){
                	    	beatsCount=ServiceConfig.SOCKET_MAX_WATING_TIME;
                	    }
						break;		
                   case ServiceConfig.TCP_SOCKET_TYPE_CREATECONNECT:
              		    if(null != mTCPClient && !mTCPClient.tcpConnect(this)){
              		    	isSendBeats=false;
   						   sendEmptyMessageDelayed(ServiceConfig.TCP_SOCKET_TYPE_CREATECONNECT,5000);
              		    }
						break;	
					}
					if(isSendBeats){
						sendEmptyMessageDelayed(ServiceConfig.TCP_SOCKET_TYPE_SEND_BEATS,10*1000);
					}
					if(beatsCount--<0){
						  sendEmptyMessage(ServiceConfig.TCP_SOCKET_TYPE_CREATECONNECT);	
					}
				}
			};	
			Looper.loop();
		}
		
		
		private void sendTcpMsg(Handler handler, String action, byte[] sendBuff){
			if(null == mTCPClient || null == sendBuff)return;
		    mTCPClient.sendMessage(handler,ServiceConfig.TCP_SOCKET_BEATS,JsonPackage.sendMsgToByte(ServiceConfig.TCP_SOCKET_BEATS));						

		}

		/**
		 * 封装发送数据为json 格式
		 * 
		 * @param context
		 * @param editType
		 * @param content
		 * @return
		 */
		private String getRespondMsg(String jsonStr) {

			String msgRespond = "";
			try {

				JSONTokener jsonParser = new JSONTokener(jsonStr);

				// 此时还未读取任何json文本，直接读取就是一个JSONObject对象。
				// 如果此时的读取位置在"name" : 了，那么nextValue就是"返回对象了"（String）

				JSONObject msgObject = (JSONObject) jsonParser.nextValue();
				// 获取消息类型
				String msgAction = msgObject.getString("msgAction");
				JSONArray array = msgObject.getJSONArray("msgRespond");

				if (msgAction.equals("requestMusicList")) {
					msgRespond = array.toString();
				} else {

					// 文件操作结果返回
					if (array.length() > 0) {
						msgObject = array.getJSONObject(0);
						msgRespond = msgObject.getString("doResult");
					}
				}

			} catch (JSONException ex) {
				// 异常处理代码
				ex.printStackTrace();
			}
			return msgRespond;
		}

	}

	private String matchAction(int actionInt) {
		String action = "";
		if (ServiceConfig.TCPS_ACTION_STBINFOR_CODE == actionInt)
			action = ServiceConfig.TCPS_ACTION_STBINFOR;
		else if (ServiceConfig.TCPS_ACTION_DOWNLOADCONF_CODE == actionInt)
			action = ServiceConfig.TCPS_ACTION_DOWNLOADCONF;
		else if (ServiceConfig.TCPS_ACTION_STBINFOR_CODE == actionInt)
			action = ServiceConfig.TCPS_ACTION_STBINFOR;
		else 
			action =ServiceConfig.TCP_SOCKET_BEATS;
		return action;
	}

	/**
	 * 特殊字符还原
	 * 
	 * @param url
	 *            url字符串
	 * @return
	 */
	public String convertHttpURLToFileUrl(String url) {
		if (null != url && url.length() > 0) {
			return url.replace("%25", "%").replace("%20", " ")
					.replace("%2B", "+").replace("%23", "#")
					.replace("%26", "&").replace("%3D", "=")
					.replace("%3F", "?").replace("%5E", "^");
		}
		return url;
	}

	public static String getLocalIpAddress() {

		try {
			for (Enumeration<NetworkInterface> en = NetworkInterface
					.getNetworkInterfaces(); en.hasMoreElements();) {

				NetworkInterface intf = en.nextElement();

				for (Enumeration<InetAddress> enumIpAddr = intf
						.getInetAddresses(); enumIpAddr.hasMoreElements();) {
					InetAddress inetAddress = enumIpAddr.nextElement();
					if (!inetAddress.isLoopbackAddress()) {
						return inetAddress.getHostAddress().toString();
					}
				}
			}
		} catch (SocketException ex) {
		}
		return "127.0.0.1";
	}
	
	
	public static String getIp(){
	     WifiManager wm=(WifiManager)MyApp.getContext().getSystemService(Context.WIFI_SERVICE);
	     //检查Wifi状态  
	     if(!wm.isWifiEnabled())wm.setWifiEnabled(true);
	     WifiInfo wi=wm.getConnectionInfo();
	     //获取32位整型IP地址  
	     int ipAdd=wi.getIpAddress();
	     //把整型地址转换成“*.*.*.*”地址  
	     String ip=intToIp(ipAdd);
	     return ip;
	 }
	 private static String intToIp(int i) {
	     return (i & 0xFF ) + "." +
	     ((i >> 8 ) & 0xFF) + "." +
	     ((i >> 16 ) & 0xFF) + "." +
	     ( i >> 24 & 0xFF) ;
	 } 

}
