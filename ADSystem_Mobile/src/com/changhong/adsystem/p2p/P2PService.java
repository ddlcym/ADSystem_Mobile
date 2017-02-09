package com.changhong.adsystem.p2p;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.changhong.adsystem.utils.ServiceConfig;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

public class P2PService {

	private static final String Tag = "P2PService::";
	private static P2PService intance;
	private ServerSocket mServerSocket;
	private Handler mParentHandler;
	private Handler mMsgHandler;

	// socketServer接收服务器：
	private Thread mSocketCommunication = null;

	private P2PService() {
		initP2PService();
	}

	public static P2PService creatFileEditServer() {
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
	 * 接收端返回的信息
	 * 
	 * @return
	 */
	public void communicationWithServer(Handler handler, int communicationType,
			String param) {

		mParentHandler = handler;
		// 发送消息给子线程
		if (null != mMsgHandler) {
			Message sendMsg = mMsgHandler.obtainMessage();
			sendMsg.what = communicationType;
			sendMsg.obj = param;
			mMsgHandler.sendMessage(sendMsg);
		}
	}

	public void close() {
		try {
			if (null != mServerSocket) {
				mServerSocket.close();
				mServerSocket = null;
			}
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	}
	
	

	/********************************************************** clientCommunicationThread *******************************************************************/

	private class clientCommunicationThread implements Runnable {

		int communicationType;

		@Override
		public void run() {

			// 创建handler之前先初始化Looper
			Looper.prepare();

			mMsgHandler = new Handler() {

				public void handleMessage(Message msg) {
					String sendMsg = null;
					communicationType = msg.what;
					// 接收来之通讯线程的消息
					switch (communicationType) {

					
					case ServiceConfig.ACTION_P2P_UDP:
						sendMsg = (String) msg.obj;
						if (ServiceConfig.P2P_SERVER_IP != null && sendMsg != null) {
							DatagramSocket dgSocket = null;
							try {
								dgSocket = new DatagramSocket();
								byte b[] = sendMsg.getBytes();

								DatagramPacket dgPacket = new DatagramPacket(b,
										b.length,
										InetAddress
												.getByName(ServiceConfig.P2P_SERVER_IP),
												ServiceConfig.P2P_SERVER_PORT);
								dgSocket.send(dgPacket);
							} catch (Exception e) {
								e.printStackTrace();
							} finally {
								try {
									if (dgSocket != null) {
										dgSocket.close();
									}
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						} else {
							Log.e(Tag, "未获取到服务器IP");
						}
						break;
					case ServiceConfig.ACTION_P2P_TCPSOCKET:
						sendMsg = (String) msg.obj;
						TCPClient tcpClient=TCPClient.instance();		
						String result=tcpClient.sendMessage(sendMsg);
						Message respondMsg = mParentHandler.obtainMessage();
						respondMsg.what = ServiceConfig.SHOW_ACTION_RESULT;
						respondMsg.obj=result;
						mParentHandler.sendMessage(respondMsg);
						break;

					}
				}
			};

			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
			Looper.loop();
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

}
