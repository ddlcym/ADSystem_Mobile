package com.changhong.adsystem.p2p;




public class DataUtil {


	public byte[] int8ToByte(int i) {
		byte[] b = new byte[1];
		b[0] = (byte) i;
		return b;
	}


	public byte[] int16ToByte(int i) {
		byte[] b = new byte[2];
//		b[0] = (byte) ((i >> 8) & 0xff);
//		b[1] = (byte) (i & 0xff);
		b[1] = (byte) ((i >> 8) & 0xff);
		b[0] = (byte) (i & 0xff);
		return b;
	}


	public byte[] int24ToByte(int i) {
		byte[] b = new byte[3];
		b[0] = (byte) ((i >> 16) & 0xff);
		b[1] = (byte) ((i >> 8) & 0xff);
		b[2] = (byte) (i & 0xff);

		return b;
	}


	public byte[] int32ToByte(int i) {
		byte[] b = new byte[4];
		b[0] = (byte) ((i >>> 24) & 0xff);
		b[1] = (byte) ((i >>> 16) & 0xff);
		b[2] = (byte) ((i >>> 8) & 0xff);
		b[3] = (byte) (i & 0xff);
//		b[3] = (byte) ((i >>> 24) & 0xff);
//		b[2] = (byte) ((i >>> 16) & 0xff);
//		b[1] = (byte) ((i >>> 8) & 0xff);
//		b[0] = (byte) (i & 0xff);
		return b;
	}


	public byte[] stringToByte(String s) // throws Exception
	{
		byte[] b = null;
		try {
			byte[] bs = s.getBytes();
			int length = bs.length;
			b = new byte[length + 1];
			b[0] = (byte) length;
			for (int i = 0; i < length; i++) {
				b[i + 1] = bs[i];
			}
		} catch (Exception e) {
			 System.out.println("error in stringToByte");
		}
		return b;
	}

	

	public byte[] addBytes(byte[] initBytes, byte[] addBytes)// throws
	// Exception
	{
		

		byte[] b = new byte[initBytes.length + addBytes.length];
		try {
			System.arraycopy(initBytes, 0, b, 0, initBytes.length);
			for (int i = 0; i < addBytes.length; i++) {
				b[i + initBytes.length] = addBytes[i];
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return b;
	}


	
	
	
	public static char[] ByteToChar(byte[] data,int off,int len) {
		char[] A = new char[len];
		for (int i = 0; i < len; i++)
			A[i] = (char) data[off+i];
		return A;
	}
	

	public int byteToInt8(byte[] b, int begin) {	
		int i =0;		
		if(begin <b.length)
			i = b[begin];
		return i;
	}

	public int byteToInt16(byte[] b, int begin) {
		int i =0;		
		if(begin <b.length)			
		 i = ((b[begin+1] & 0xff) << 8) + (b[begin] & 0xff);
//		 i = ((b[begin] & 0xff) << 8) + (b[begin + 1] & 0xff);

		return i;
	}


	public int byteToInt24(byte[] b, int begin) {
		int i =0;		
		if(begin <b.length)
		i = (((b[begin] & 0xff) << 16)) + ((b[begin + 1] & 0xff) << 8)
				+ (b[begin + 2] & 0xff);
		return i;
	}


	public int byteToInt32(byte[] b, int begin) {
		int i =0;		
		if(begin <b.length)
		 i = (((b[begin] & 0xff) << 24)) + (((b[begin + 1] & 0xff) << 16))
				+ ((b[begin + 2] & 0xff) << 8) + (b[begin + 3] & 0xff);
//		if(begin <b.length)
//			 i = (((b[begin+3] & 0xff) << 24)) + (((b[begin + 2] & 0xff) << 16))
//					+ ((b[begin + 1] & 0xff) << 8) + (b[begin] & 0xff);
		return i;
	}

	
	public byte[] createNewByte(byte[] b,int off,int len)
	{
		
	
		int bytesLen=b.length;
		if((off+len) > bytesLen ||  len<0 || off<0 ){
			return null;
		}
		
		byte[] newb = new byte[len];
		for(int loop=0;loop<len;loop++)
		{
			newb[loop] = b[loop+off];		
		}
		return newb;
		
	}
	
	
	//前面长度信息为1个字节的字符串解析
	public String byteToString(byte[]b,int off,boolean bDecrypt,int lenSize){
		String s="";
//		if(lenSize == 1)	
//			len = byteToInt8(b, off);
//		else if(lenSize == 2)
//			len = byteToInt16(b, off);
//		else if(lenSize == 3)
//			len = byteToInt24(b, off);
//		else if(lenSize == 4)
//			len = byteToInt32(b, off);		
//		
//	    int filelen=b.length;
//		if((off+len) <= filelen){
//			
//			byte[]  newb = createNewByte(b,off+lenSize,len);
//			if(bDecrypt)
//				newb = AES.decrypt(newb, AES.key, 16);
//		    
//			if(null != newb){
////				UTF8String utf8s = new UTF8String(newb,newb.length);
////				s = utf8s.toString();
//			}
//		}
		int newLength=0;
		boolean endFlag=false;
		for (int i = 0; i < lenSize; i++) {
			if( 0 == b[off+i]){
				if(endFlag){
					newLength--;
					break;
				}
				endFlag=true;
			}
			newLength++;
		}
		
		int filelen=b.length;
		if((off+newLength) <= filelen){
			
			byte[]  newb = createNewByte(b,off,newLength);
//			if(bDecrypt)
//				newb = AES.decrypt(newb, AES.key, 16);
		    
			if(null != newb){
				s= new String(newb);
			}
		}			
		return s;

		
	}
	

	
	

}