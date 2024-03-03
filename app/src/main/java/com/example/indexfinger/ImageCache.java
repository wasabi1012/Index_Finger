package com.example.indexfinger;

import android.graphics.Bitmap;

import androidx.collection.LruCache;

public class ImageCache {

    private LruCache<String, Bitmap> mCache;

    public ImageCache() {
        long maxMemory =Runtime.getRuntime().maxMemory() / 1024;
        int cacheSize = (int)(maxMemory / 8);

        mCache = new LruCache<String, Bitmap>(cacheSize) {
            protected int sizeOf(String key, Bitmap value) {
                return value.getAllocationByteCount() / 1024;
            }
        };
    }

    public void put(String key, Bitmap image) {
        mCache.put(key, image);
    }

    public Bitmap get(String key) {
        return mCache.get(key);
    }

    public void clear() {
        mCache.evictAll();
    }

}
