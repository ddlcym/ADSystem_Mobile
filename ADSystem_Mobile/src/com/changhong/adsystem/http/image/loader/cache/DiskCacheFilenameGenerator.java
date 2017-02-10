package com.changhong.adsystem.http.image.loader.cache;


import java.io.File;
import com.changhong.adsystem.utils.Configure;

public class DiskCacheFilenameGenerator {

    public static File getDiskCacheFile(String imageUri) {
        return new File(Configure.adResFilePath + File.separator + generateDiskCacheFilename(imageUri));
    }

    public static String generateDiskCacheFilename(String imageUri) {
        return String.valueOf(imageUri.hashCode());
    }

}
