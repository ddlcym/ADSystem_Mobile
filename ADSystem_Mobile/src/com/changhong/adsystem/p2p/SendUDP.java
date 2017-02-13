package com.changhong.adsystem.p2p;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * @author cym
 * @date 创建时间：2017年2月9日 下午5:10:07
 * @version 1.0
 * @parameter
 */
public class SendUDP extends Thread {
	private String serverIP = "239.239.239.99";
	private int STBPort = 20149;
	private int phonePort=20249;
	private String queryDevices = "_searchService";
	private byte[] sendMSG;
	private int TTL = 5;
	private InetAddress group;
	private MulticastSocket mSocket;

	private boolean flag = true;
	private int times = 0;
	private int maxTimes = 3;
	private int sleepTime = 500;

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			mSocket = new MulticastSocket(phonePort);
			group = InetAddress.getByName(serverIP);
			mSocket.joinGroup(group);
			mSocket.setTimeToLive(TTL);
			sendMSG = queryDevices.getBytes("utf-8");
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		DatagramPacket dp = new DatagramPacket(sendMSG, sendMSG.length, group,
				STBPort);
		while (flag && times < maxTimes) {
			try {
				mSocket.send(dp);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				sleep(sleepTime);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		mSocket.close();
	}

	
	public void setFlag(boolean flag){
		this.flag=flag;
	}
}
