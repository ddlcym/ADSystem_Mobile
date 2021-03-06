package com.changhong.adsystem.p2p;

import java.net.InetSocketAddress;
import java.net.SocketAddress;

import com.changhong.adsystem.utils.ServiceConfig;

/** 
 * @author  cym  
 * @date 创建时间：2017年2月13日 上午11:46:17 
 * @version 1.0 
 * @parameter   
 */
public class UDPData {
	private String UDPQueryResult="";
	private static UDPData udpData;
	private SocketAddress serverAddress;//服务端地址
	private int serverPort;//服务器端口
	
	public static UDPData getInstance(){
		if(null==udpData){
			udpData=new UDPData();
			
		}
		return udpData;
	}

	public String getUDPQueryResult() {
		return UDPQueryResult;
	}

	public void setUDPQueryResult(String uDPQueryResult) {
		UDPQueryResult = uDPQueryResult;
	}

	public SocketAddress getServerAddress() {
		return serverAddress;
	}

	public void setServerAddress(SocketAddress serverAddress) {
		this.serverAddress = serverAddress;		
	}

	public int getServerPort() {
		return serverPort;
	}

	public void setServerPort(int serverPort) {
		ServiceConfig.P2P_SERVER_PORT=this.serverPort = serverPort;
	}
	
	
	public String getServerIP() {
		InetSocketAddress socketAddress = (InetSocketAddress) serverAddress;
		if(null != socketAddress && null != socketAddress.getAddress()){
			return socketAddress.getAddress().getHostAddress();
		}
		return null;
	}
	
	
}
