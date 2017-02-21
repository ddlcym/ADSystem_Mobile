package com.changhong.adsystem.p2p;

/**
 * TCPClient通讯 用于手机与盒子端通讯
 * 
 */
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import com.changhong.adsystem.model.JsonResolve;
import com.changhong.adsystem.utils.ServiceConfig;

public class TCPClient {

	String TAG = "TCPClient";

	private static TCPClient mTCPClient = null;
	public boolean mClientFlag = false;
	byte[] theSendData = null;
	boolean isReqMedia = false;
	Socket mSocket = null;
	Map<String, Handler> mActionList = new HashMap<String, Handler>();
	String oldSocketAddress = null;
	ReceiveThread mReceiveThread = null;
	String mRunLock = "runFlag";
	String mSendMsg = "sendMsg";

	private DataOutputStream dataOutputStream = null;
	private DataInputStream dataInputStream = null;
	private DataUtil mDataUtil=null;
	private Handler deafultHandler=null;
	
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
		mDataUtil=new DataUtil();
	}

	public boolean tcpConnect(Handler handler) {
		boolean isOk = false;
		
		try {
			String curServerIP = UDPData.getInstance().getServerIP();

			if (null != curServerIP) {
				tcpSocketClose();
				Log.i(TAG,
						"-------------------p2p connect-------------------------");
				mSocket = new Socket(curServerIP, ServiceConfig.P2P_SERVER_PORT);
				if (mSocket.isConnected()) {
					dataOutputStream = new DataOutputStream(mSocket.getOutputStream());
					dataInputStream = new DataInputStream(mSocket.getInputStream());

					if (null == oldSocketAddress || !oldSocketAddress.equals(curServerIP)) {
						if (null != mReceiveThread)mReceiveThread.stopThread();
						oldSocketAddress = curServerIP;
						// 启动接收线程
						mReceiveThread = new ReceiveThread(dataInputStream);
						new Thread(mReceiveThread).start();
					}
					isOk = true;
					deafultHandler=handler;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();

			try {
				if (null != mSocket) {
					mSocket.close();
					mSocket = null;
				}
			} catch (IOException e1) {
				e1.printStackTrace();
			}

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
		try {
			if (null != dataOutputStream) {
				Log.i(TAG, ">>>>>> socket send msg");
				dataOutputStream.write(sendBuff);
				dataOutputStream.flush();
				mActionList.put(action, handler);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void reTcpConnect(){
		 if(null != deafultHandler){
			 deafultHandler.sendEmptyMessage(ServiceConfig.TCP_SOCKET_TYPE_CREATECONNECT);	
		 }
	}

	public void tcpSocketClose() {

		try {

			if (null != dataInputStream) {
				dataInputStream.close();
				dataInputStream = null;
			}

			if (null != dataOutputStream) {
				dataOutputStream.close();
				dataOutputStream = null;
			}

			if (null != mSocket) {
				mSocket.close();
				mSocket = null;
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void stopReceiveTask() {
		if(null != mReceiveThread){
			mReceiveThread.stopThread();
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

		private boolean _run = true;
		DataInputStream _Input = null;

		public ReceiveThread(DataInputStream input) {
			this._Input = input;
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
			try {
				if (null != _Input) {
					int revalue=handleInputStream(_Input);
					if(-1 == revalue){
						reTcpConnect();
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
				reTcpConnect();	
			}
           
		}

		private int handleInputStream(DataInputStream dataInputStream)
				throws IOException {
			final int cacheSize = 512;
			final int MaxByteHeapSize = 1024 * 1024;
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			byte[] data = new byte[cacheSize];
			byte[] dataLengthArr = new byte[4];
			byte[] sessionIndexArr = new byte[4];
			byte[] sessionArg2Arr = new byte[4];
			int readSize = 0;
			int dataLength = 0;
			int sessionIndex = 0;
			int sessionArg2 = 0;
			while (isRunning()) {
				readSize = dataInputStream.read(dataLengthArr);
				if (readSize > 0) {

					dataLength = mDataUtil.byteToInt32(dataLengthArr,0);

					if (dataLength > 0) {
						Log.i(TAG, "begin read data,dataLength = " + dataLength);

						// sessionIndex
						readSize = dataInputStream.read(sessionIndexArr);
						if (readSize < 0 && !isRunning()) {
							return -1;
						}
						sessionIndex = mDataUtil.byteToInt32(sessionIndexArr,0);

						// sessionArg2
						readSize = dataInputStream.read(sessionArg2Arr);
						// logi("sessionArg2Arr>>"+DataFrame.getDataString(sessionArg2Arr));
						if (readSize < 0 && !isRunning()) {
							return -1;
						}
						sessionArg2 = mDataUtil.byteToInt32(sessionArg2Arr,0);
						byteArrayOutputStream.reset();
						if (dataLength > MaxByteHeapSize) {
							Log.i(TAG, "dataLength large than "
									+ MaxByteHeapSize + ", new " + cacheSize
									+ " bytes array.");
							data = new byte[cacheSize];
						} else {
							data = new byte[dataLength];
						}
						do {

							readSize = dataInputStream.read(data);
							if (readSize != -1) {
								byteArrayOutputStream.write(data, 0, readSize);
								dataLength -= readSize;
							}
						} while (dataLength > 0 && readSize != -1);
						String revStr = new String(byteArrayOutputStream.toByteArray());
						String action = getTcpAction(revStr);
						Handler mParentHandler = matchHandler(action);
						if (null != mParentHandler) {
							Message respondMsg = mParentHandler.obtainMessage();
							respondMsg.what = ServiceConfig.SHOW_ACTION_RESULT;
							respondMsg.obj = JsonResolve.getTcpReponse(revStr);
							respondMsg.sendToTarget();
						}else{
							Message respondMsg = deafultHandler.obtainMessage();
							respondMsg.what = ServiceConfig.TCP_SOCKET_TYPE_RESPOND;
							respondMsg.obj = revStr;
							respondMsg.sendToTarget();
						}
					}
				}
			}
			byteArrayOutputStream.close();
			return 0;
		}
	}

}
