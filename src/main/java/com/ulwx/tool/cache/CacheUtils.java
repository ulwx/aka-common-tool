package com.ulwx.tool.cache;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Function;

public class CacheUtils {
    static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(4);

    static{
        Thread printingHook = new Thread(() -> scheduler.shutdown() );
        Runtime.getRuntime().addShutdownHook(printingHook);
    }
    public static class CacheObj{
        private String key;
        private Object value;
        private long expireTime;

        public CacheObj(String key, Object value, long ttl) {
            this.key = key;
            this.value = value;
            if(ttl==-1) {
                expireTime = -1;
            }else {

                this.expireTime = System.currentTimeMillis() + ttl * 1000;
                scheduler.schedule(() -> {
                    cache.invalidate(key);
                },  ttl * 1000, TimeUnit.MILLISECONDS);
            }
        }

        public boolean isExpired(){
            if(expireTime==-1) return false;
            if(System.currentTimeMillis()< this.expireTime ){
                return false;
            }else{
                return true;
            }
        }
        public Object getValue() {

            return value;
        }
        public String getKey() {
            return key;
        }


    }
    private static Cache<String, CacheObj> cache  = CacheBuilder.newBuilder().
            maximumSize(1000L).
            removalListener(
                new RemovalListener<String, CacheObj>() {
                    @Override
                    public void onRemoval(RemovalNotification<String, CacheObj> notification) {
                        for(Consumer consumer: consumers){
                            consumer.accept(notification);
                        }
                    }
                }
            )
            .build();
    public static Set<Consumer> consumers= Collections.synchronizedSet(new LinkedHashSet<>());
    public static void registRemoveListener(Consumer<RemovalNotification<String, CacheObj>> consumer){
        consumers.add(consumer);
    }
    /**
     *
     * @param key 键值、
     * @param loader 函数，有异常一定要封装成RuntimeException抛出，而且不能返回空
     * @return
     * @throws Exception
     */
    public static Object get(String key, Function<String,Object> loader) throws  Exception{

        CacheObj obj= cache.get(key, new Callable<CacheObj>() {
            @Override
            public CacheObj call() throws Exception {
                Object ret= loader.apply(key);
                CacheObj cacheObj=new CacheObj(key,ret,-1);
                return cacheObj;
            }
        });
        return obj.getValue();

    }

    public static <T> T get(String key){
        Object obj= cache.getIfPresent(key);
        if(obj==null) return null;
        if(obj instanceof CacheObj){
            CacheObj cacheObj=(CacheObj)obj;
            if(cacheObj.isExpired()){
                cache.invalidate(cacheObj.getKey());
                return null;
            }else{
                return (T)cacheObj.getValue();
            }
        }
        return (T)obj;
    }

    public static void remove(String key){
        cache.invalidate(key);
    }
    /**
     *
     * @param key
     * @param value
     * @param ttl  以秒为单位
     */
    public static void set(String key,Object value,long ttl){
        CacheObj cacheObj=new CacheObj(key,value,ttl);

        cache.put(key,cacheObj);

    }
    public static void set(String key,Object value){
        set(key, value,-1);
    }
    public static void main(String[] args) throws Exception{
//        System.out.println(get("1"));
//        System.out.println(get("2"));
//        System.out.println(get("3"));
//        Thread.sleep(3000);
//        System.out.println(get("2"));
//        System.out.println(get("2"));

        set("11","22",2);

        Thread.sleep(6000);

        get("11");
    }

}
