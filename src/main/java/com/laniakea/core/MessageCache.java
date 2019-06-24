package com.laniakea.core;

import io.netty.channel.Channel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author wb-lgc489196
 * @version HandleCache.java, v 0.1 2019年06月20日 10:19 wb-lgc489196 Exp
 */
public class MessageCache {


    private  Map<String, MessageCallBack> mapCallBack = new ConcurrentHashMap<>();

    private  Map<String, Channel> channelMap = new ConcurrentHashMap<>();

    private Map<String, Object> handlerMap = new ConcurrentHashMap<>();

    private Map<String, Object> getHandlerMap() {
        return handlerMap;
    }


    private static class CacheHolder {
        public static MessageCache cache = new MessageCache();
    }

    public void putHandler(String key,Object obj) {
         handlerMap.put(key,obj);
    }

    public Object getHandler(String key) {
        return handlerMap.get(key);
    }

    public static MessageCache getCache() {
        return CacheHolder.cache;
    }

    public void removeCallBack(String key){
        channelMap.remove(key);
    }

    public  void putMassgeChannel(String key, Channel ch){
        channelMap.putIfAbsent(key,ch);
    }

    public  Channel getMassgeChannel(String key){
        return channelMap.get(key);
    }


    public  void putMassgeCallBack(String key, MessageCallBack callBack){
        mapCallBack.putIfAbsent(key,callBack);
    }

    public  MessageCallBack getMassgeCallBack(String key){
        return mapCallBack.get(key);
    }

}
