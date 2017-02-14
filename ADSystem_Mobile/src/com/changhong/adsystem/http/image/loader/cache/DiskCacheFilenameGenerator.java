package com.changhong.adsystem.http.image.loader.cache;


import java.io.File;
import com.changhong.adsystem.utils.Configure;
import com.changhong.adsystem.utils.FileUtil;

public class DiskCacheFilenameGenerator {

    public static File getDiskCacheFile(String imageUri) {
    	    
    	return creatSDFile(Configure.adResFilePath+File.separator+imageUri);
    }

    public static String generateDiskCacheFilename(String imageUri) {
        return String.valueOf(imageUri.hashCode());
    }

    public static File creatSDFile(String filePath) {
    	String dirPath="";
    	String[] tempPath=filePath.split(File.separator);
    	int length=tempPath.length;
    	if(length <=1){
    		return new File(filePath);
    	}
      	
    	for (int i = 0; i < length-1; i++) {
		    dirPath += File.separator+tempPath[i];				
			if (null != dirPath && !dirPath.isEmpty()) {
				File dir = new File(dirPath);
				if (!dir.exists()) {
					dir.mkdir();
				}
			}			
    	}
		File file = new File(filePath);
		return file;
	}
}
