package com.ulwx.tool.cache;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

public class CacheUtils {
    private static LoadingCache<String,String> cache  = CacheBuilder.newBuilder().
            refreshAfterWrite(1l,TimeUnit.SECONDS).
            maximumSize(1000L)
            .build(new CacheLoader<String, String>() {
                @Override
                public String load(String key) throws Exception {
                   System.out.println(System.currentTimeMillis());
                   return "";
                }
    });;

    /**
     *
     * @param key 键值、
     * @param loader 函数，有异常一定要封装成RuntimeException抛出，而且不能返回空
     * @return
     * @throws Exception
     */
    public static String get(String key, Function<String,String> loader) throws  Exception{

       return cache.get(key, new Callable<String>() {
            @Override
            public String call() throws Exception {
                return loader.apply(key);
            }
        });

    }

    public static String get(String key)throws Exception{
        return cache.get(key);
    }

    public static void main(String[] args) throws Exception{
        System.out.println(get("2"));
        System.out.println(get("2"));
        Thread.sleep(1200);
        System.out.println(get("2"));
        System.out.println(get("2"));

    }

}
