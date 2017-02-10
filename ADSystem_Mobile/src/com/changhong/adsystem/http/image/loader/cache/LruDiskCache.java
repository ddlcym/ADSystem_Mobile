package com.changhong.adsystem.http.image.loader.cache;

import android.util.Log;
import java.io.File;
import java.util.Arrays;
import java.util.Comparator;

import com.changhong.adsystem.activity.MyApp;

public class LruDiskCache implements DiskCache {

    public final static int MAX_DISKCACHE_ITEM_SIZE = 100;

    public final static int DELETE_ITEM_SIZE = 2;


    /**
     * this method is used for check is local disk cache download imageURL
     * @param imageUri image which get from http
     * @return if this file exist, return file else return null
     */
    public File getDiskCachedFile(String imageUri) {
        String cacheFilename = DiskCacheFilenameGenerator.generateDiskCacheFilename(imageUri);
        File cachedFile = new File(cacheFilename);
        if (cachedFile != null && cachedFile.exists()) {
            return cachedFile;
        }
        return null;
    }

    /**
     * this method is used for check if cached files number is exceed max limitation
     * if exceed, delete oldest two
     * else igronal it
     */
    public synchronized static void checkMaxFileItemExceedAndProcess() {
        String[] list = MyApp.imageDownloadRootPath.list();
        if (list != null && list.length > MAX_DISKCACHE_ITEM_SIZE) {
            Log.e("FILE_DELETE", "now small picture number is  " + list.length);

            int alreadyDeleteNumber = 0;
            File[] files = MyApp.imageDownloadRootPath.listFiles();
            Arrays.sort(files, new FileComparator());

            for (File file : files) {
                try {
                    if (!file.isDirectory()) {
                        file.delete();
                        alreadyDeleteNumber++;
                        if (alreadyDeleteNumber >= DELETE_ITEM_SIZE) {
                            break;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    continue;
                }
            }

        }
    }

    static class FileComparator implements Comparator<File> {
        @Override
        public int compare(File f1, File f2) {
            Long last1 = f1.lastModified();
            Long last2 = f2.lastModified();
            return last1.compareTo(last2);
        }
    }

}
