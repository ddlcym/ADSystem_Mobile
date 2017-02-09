package com.changhong.adsystem.p2p;

/**
 * socket通讯 用于手机与盒子端通讯
 * 
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;
import org.apache.http.conn.util.InetAddressUtils;
import android.util.Log;
import com.changhong.adsystem.model.JsonObject;
import com.changhong.adsystem.model.JsonSendModel;
import com.changhong.adsystem.utils.Configure;

public class TCPClient {

	String TAG = "SocketClient";

	private static TCPClient mTCPClient = null;
	public boolean mClientFlag = false;
	private String hostName;
	private int portNum;
	JsonObject resultObject;
	byte[] theData = null;
	boolean isReqMedia = false;
	PrintWriter mOutput = null;
	BufferedReader mInput = null;

	public static synchronized TCPClient instance() {
		if (mTCPClient == null) {
			mTCPClient = new TCPClient();
		}
		return mTCPClient;
	}

	public Socket getSocket() {
		Socket socket = null;
		try {
			socket = new Socket(hostName, portNum);
		} catch (UnknownHostException e) {
			System.out.println("-->未知的主机名:" + hostName + " 异常");
		} catch (IOException e) {
			System.out.println("-hostName=" + hostName + " portNum=" + portNum
					+ "---->IO异常错误" + e.getMessage());
		}
		return socket;
	}

	/**
	 * 发送数据
	 * 
	 * @param SendModel
	 * @return
	 */
	public String sendMessage(String strMessage) {
		String str = "";
		Socket socket = null;
		PrintWriter out = null;
		Log.i(TAG, "---->sendMsg is " + strMessage);

		try {
			socket = getSocket();
			// socket.setKeepAlive(true);
			if (socket == null) { // 未能得到指定的Socket对象,Socket通讯为空
				return "0001";
			}
			//向机顶盒发送信息
			out = new PrintWriter(socket.getOutputStream());
			out.println(strMessage);
			out.flush();
			//从机顶盒获取返回消息
			str = receiveRespond(socket);
		} catch (UnknownHostException e) {
			Log.e(TAG, "---->出现未知主机错误! 主机信息=" + this.hostName + " 端口号="
					+ this.portNum + " 出错信息=" + e.getMessage());
			str = "2191";
			// System.exit(1);
		} catch (IOException e) {
			Log.e(TAG, "---->出现IO异常! 主机信息=" + this.hostName + " 端口号="
					+ this.portNum + " 出错信息=" + e.getMessage());
			e.printStackTrace();
			str = "2191";
		} catch (Exception e) {
			str = "2177";
			Log.e(TAG, "---->出现未知异常" + e.getMessage());
		} finally {
			try {
				if (null != out) {
					out.close();
					out = null;
				}
				if (null != socket) {
					socket.close();
					socket = null;
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		Log.i(TAG, "--->返回的socket通讯字符串=" + str);
		return str.trim();
	}

	private String receiveRespond(Socket socket) {
		String revStr = "";
		int delayTime = 30, len;
		char buffer[] = new char[1024];
		BufferedReader in = null;
		try {
			in = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));
			while (delayTime-- > 0) {
				if (socket.getInputStream().available() > 0) {
					if ((len = in.read(buffer)) != -1) {
						String temp = new String(buffer, 0, len);
						revStr += temp;
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (null != in) {
					in.close();
					in = null;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return revStr;
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
