package com.changhong.adsystem.p2p;

/**
 * TCPClient通讯 用于手机与盒子端通讯
 * 
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import android.graphics.Path.FillType;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import com.changhong.adsystem.model.JsonResolve;
import com.changhong.adsystem.utils.ServiceConfig;

public class TCPClient {

	String TAG = "TCPClient";

	private static TCPClient mTCPClient = null;
	public boolean mClientFlag = false;
	byte[] theData = null;
	boolean isReqMedia = false;
	Socket mSocket = null;
	Map<String, Handler> mActionList = new HashMap<String, Handler>();
	String oldSocketAddress = null;
	ReceiveThread mReceiveThread = null;
	String mRunLock = "runFlag";

	private TCPClient() {
		initTCPClient();
	}

	public static synchronized TCPClient instance() {
		if (mTCPClient == null) {
			mTCPClient = new TCPClient();
		}
		return mTCPClient;
	}

	private void initTCPClient() {

	}

	public boolean tcpConnect() {
		boolean isOk = false;
		try {
			String curServerIP = UDPData.getInstance().getServerIP();

			if (null == oldSocketAddress
					|| !oldSocketAddress.equals(curServerIP)) {
				socketRelease();
				Log.i(TAG,"-------------------p2p connect-------------------------");
				mSocket = new Socket(curServerIP, ServiceConfig.P2P_SERVER_PORT);
				if (mSocket.isConnected()) {
					oldSocketAddress = curServerIP;
					// 启动接收线程
					mReceiveThread = new ReceiveThread(mSocket);
					new Thread(mReceiveThread).start();
					isOk = true;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return isOk;
	}

	/**
	 * 发送数据
	 * 
	 * @param String
	 *            发送信息正文
	 * @return
	 */
	public void sendMessage(Handler handler, String action, byte[] sendBuff) {
		OutputStream mOutput=null;
		try {
			if (null != mSocket) {	
				Log.i(TAG, ">>>>>> socket send msg");
				mOutput = mSocket.getOutputStream();
				mOutput.write(sendBuff);			
				mOutput.flush();
				mActionList.put(action, handler);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
//			try {
//				if(null != mOutput){
//					mOutput.close();
//					mOutput=null;
//				}
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
		}
	}

	public void socketRelease() {

		try {
			if (null != mReceiveThread) {
				mReceiveThread.stopThread();
			}
			if (null != mSocket) {
				mSocket.close();
				mSocket = null;
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取通讯的action
	 * 
	 * @param msg
	 * @return
	 */
	private String getTcpAction(String msg) {
		return JsonResolve.getTcpAction(msg);
	}

	private Handler matchHandler(String action) {

		for (Map.Entry<String, Handler> entry : mActionList.entrySet()) {
			String key = entry.getKey().toString();
			if (action.equals(key)) {
				return (Handler) entry.getValue();
			}
		}
		return null;
	}

	/********************************************************************************************/
	class ReceiveThread implements Runnable {

		Socket mSocket = null;
		private boolean _run = true;

		public ReceiveThread(Socket socket) {
			this.mSocket = socket;
		}

		public void stopThread() {
			synchronized (mRunLock) {
				this._run = false;
			}
		}

		public boolean isRunning() {
			boolean isRunning = false;
			synchronized (mRunLock) {
				isRunning = this._run;
			}
			return isRunning;
		}

		@Override
		public void run() {
			String revStr = "";
			char buffer[] = new char[1024];
			BufferedReader mInput=null;
			while (isRunning()) {
				int len = -1;
				try {
					revStr = "";
					Log.i(TAG, ">>>>>> start to read  buffer！！！！");

					mInput = new BufferedReader(new InputStreamReader(mSocket.getInputStream()));
					while ((len = mInput.read(buffer)) != -1) {
						revStr +=  new String(buffer, 0, len);;
					}
					
					if (!revStr.isEmpty()) {
						revStr=JsonResolve.filterJsonMsg(revStr);
						if(!ServiceConfig.TCP_SOCKET_BEATS.equals(revStr)){
						
							String action = getTcpAction(revStr);
							Handler mParentHandler = matchHandler(action);
							if (null != mParentHandler) {
								Message respondMsg = mParentHandler.obtainMessage();
								respondMsg.what = ServiceConfig.SHOW_ACTION_RESULT;
								respondMsg.obj = JsonResolve.getTcpReponse(revStr);
								mParentHandler.sendMessage(respondMsg);
							}
						}
					}
										
				} catch (IOException e1) {
					e1.printStackTrace();
				}finally{
					try {
//						if (null != mInput) {
//							mInput.close();
//							mInput = null;
//						}
						Thread.sleep(1000);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}		
		}

	}

}
