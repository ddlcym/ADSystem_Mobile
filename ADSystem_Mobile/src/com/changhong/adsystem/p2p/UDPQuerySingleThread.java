package com.changhong.adsystem.p2p;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.Arrays;
import java.util.LinkedList;

import com.changhong.adsystem.utils.Configure;

import android.content.Intent;
import android.os.Handler;
import android.util.Log;


public class UDPQuerySingleThread extends Thread {

	private String serverIP="239.239.239.99";

	private int UDPport = 20149;

	private String queryDevices="_searchService";
	private SocketAddress serverAddress;//服务端地址
//	private byte[] cacheBuffer;
//	private int TTL=5;
//	private InetAddress group;
	
	
	private int localPort;
	private byte[] netBuffer = new byte[4];
	private byte[] IPBuffer = new byte[4];
	private byte[] macBuffer = new byte[6];
	private SocketAddress remoteAddress = null;
	private DatagramChannel channel = null;
	public boolean flag = false;
	private final boolean blockingMode = false;
	private final int bufferSize = 1024;
	byte[] sendCache;
	private int sleepTime = 100;
	private int reTryTimes=15;

	private State state = State.FREE;

	private enum State {
		FREE, QUERY, END
	}

	private int count;
	
	private Handler parentHandler;

	private LinkedList<byte[]> dataList;
	private LinkedList<SocketAddress> socketList;

	
	
	
	
	public UDPQuerySingleThread(Handler handler) {
		parentHandler=handler;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			DatagramSocket ds = new DatagramSocket();
			localPort = ds.getLocalPort();
			ds.close();
			channel = DatagramChannel.open();
			channel.socket().bind(new InetSocketAddress(localPort));
			channel.configureBlocking(blockingMode);
			dataList = new LinkedList<byte[]>();// 发送的数据队列
			socketList = new LinkedList<SocketAddress>();// 发送的目的地址的队列
			remoteAddress = new InetSocketAddress(
					InetAddress.getByName(serverIP), UDPport);
			
//			mSocket=new MulticastSocket();
//			group=InetAddress.getByName(serverIP);
//			mSocket.joinGroup(group);
//			mSocket.setTimeToLive(TTL);
			sendCache=queryDevices.getBytes("utf-8");
			
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

		android.os.Process
				.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
		
		byte[] cache;
		
		ByteBuffer receiveBuffer = ByteBuffer.allocate(bufferSize);
		state = State.QUERY;
		count = 0;
		while (flag) {
			receiveBuffer.clear();
			try {
				serverAddress=channel.receive(receiveBuffer);
				if (serverAddress!= null) {
					receiveBuffer.flip();
					cache = new byte[receiveBuffer.limit()];
					receiveBuffer.get(cache);
					receiveBuffer.clear();
//					Log.i("mmmm","receive-udp");
					// 增加cache的处理方法
					try {
						String result=getHexString(cache);
						if(result.contains("_serviceRespond")){
							UDPData.getInstance().setServerAddress(serverAddress);
							dealCache(result);
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			stateMachine();

			try {
				if (!dataList.isEmpty()) {
					byte[] buffer = dataList.removeFirst();
					receiveBuffer.put(buffer);
					receiveBuffer.flip();
					channel.send(receiveBuffer, socketList.removeFirst());
					Log.i("mmmm",
							"sendUDPPacket---" + count + "----"
									+ Arrays.toString(buffer));
				}
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
		try {
			if (channel.isOpen())
				channel.close();
			if (!dataList.isEmpty()) {
				dataList.clear();
				socketList.clear();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		flag = false;
	}

	private void stateMachine() {
		switch (state) {
		case FREE:

			break;
		case QUERY:
			if (count > reTryTimes) {
				state = State.END;
				count = 0;
			} else if (0 == count % 5) {
				dataList.add(sendCache);
				socketList.add(remoteAddress);
			}
			count++;
			break;
		case END:
			flag = false;
			break;
		}
	}

	public static String getHexString(byte[] b) throws Exception {
		String result = "";
		for (int i = 0; i < b.length; i++) {
			result += Integer.toString((b[i] & 0xff) + 0x100, 16).substring(1);
			result += ",";
		}
		return result;
	}
	
	public String intToIp(int ipInt) {
		return new StringBuilder().append(((ipInt >> 24) & 0xff)).append('.')
				.append((ipInt >> 16) & 0xff).append('.')
				.append((ipInt >> 8) & 0xff).append('.').append((ipInt & 0xff))
				.toString();
	}

	private void dealCache(String result) {
		UDPData.getInstance().setUDPQueryResult(result);
		parentHandler.sendEmptyMessage(Configure.UDPQueryResult);
		state = State.END;
	}
	

	
	public void stopQuery(){
		flag=false;
	}
}
