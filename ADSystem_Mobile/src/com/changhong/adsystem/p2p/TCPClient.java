package com.changhong.adsystem.p2p;

/**
 * TCPClient通讯 用于手机与盒子端通讯
 * 
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.http.conn.util.InetAddressUtils;

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
	PrintWriter mOutput = null;
	BufferedReader mInput = null;
	Socket mSocket = new Socket();
	Map<String, Handler> mActionList = new HashMap<String, Handler>();
	SocketAddress oldSocketAddress=null;
	public static synchronized TCPClient instance() {
		if (mTCPClient == null) {
			mTCPClient = new TCPClient();
		}
		return mTCPClient;
	}

	public boolean tcpConnect() {
		boolean isOk = false;
		try {
			SocketAddress socketAddress = UDPData.getInstance().getServerAddress();
			if(null != oldSocketAddress && !oldSocketAddress.equals(socketAddress)){
				socketRelease();
				mSocket.connect(socketAddress, 10000);
				if (mSocket.isConnected()) {
					mOutput = new PrintWriter(mSocket.getOutputStream());
					mInput = new BufferedReader(new InputStreamReader(
							mSocket.getInputStream()));
					oldSocketAddress=socketAddress;
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
	public void sendMessage(Handler handler, String action, String sendMsg) {
		if (null != mOutput) {
			mOutput.println(sendMsg);
			mOutput.flush();
			mActionList.put(action, handler);
		}
	}

	public void socketRelease() {

		try {
			if (null != mOutput) {
				mOutput.close();
				mOutput = null;
			}
			if (null != mInput) {
				mInput.close();
				mInput = null;
			}
			if (null != mSocket) {
				mSocket.close();
				mSocket = null;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String receiveRespond() {

		String revStr = "";
		int len;
		char buffer[] = new char[1024];

		while (true) {
			try {
				while ((len = mInput.read(buffer)) != -1) {
					String temp = new String(buffer, 0, len);
					revStr += temp;
				}
				String action = getTcpAction(revStr);
				Handler mParentHandler = matchHandler(action);
				if (null != mParentHandler) {
					Message respondMsg = mParentHandler.obtainMessage();
					respondMsg.what = ServiceConfig.SHOW_ACTION_RESULT;
					respondMsg.obj = JsonResolve.getTcpReponse(revStr);
					mParentHandler.sendMessage(respondMsg);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
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

	/**
	 * 获取本地网络IP地址。
	 * 
	 * @return
	 */
	private String getLocalHostIp() {
		String ipaddress = "";
		try {
			Enumeration<NetworkInterface> en = NetworkInterface
					.getNetworkInterfaces();
			// 遍历所用的网络接口
			while (en.hasMoreElements()) {
				NetworkInterface nif = en.nextElement();// 得到每一个网络接口绑定的所有ip
				Enumeration<InetAddress> inet = nif.getInetAddresses();
				// 遍历每一个接口绑定的所有ip
				while (inet.hasMoreElements()) {
					InetAddress ip = inet.nextElement();
					if (!ip.isLoopbackAddress()
							&& InetAddressUtils.isIPv4Address(ip
									.getHostAddress())) {
						return ipaddress = ip.getHostAddress();
					}
				}
			}
		} catch (SocketException e) {
			// Log.e(TAG, "获取本地ip地址失败");
			e.printStackTrace();
		}
		return ipaddress;
	}

}
