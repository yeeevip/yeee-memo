package vip.yeee.memo.integrate.ai.chatgpt.cache;

import cn.hutool.cache.Cache;
import cn.hutool.cache.CacheUtil;
import cn.hutool.core.lang.Validator;
import vip.yeee.memo.integrate.ai.chatgpt.params.chat.ChatMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * 携带信息
 */
public class ChatCache {

    // 本地缓存的信息 -- 修改 capacity 设置最大容量
    public static final Cache<String, List<ChatMessage>> chats = CacheUtil.newFIFOCache(100);

    public static void put(String key, List<ChatMessage> value){
        int listSize = value.size();
        List<ChatMessage> newMessages = value;
        if (listSize > 10){
            newMessages = value.subList(listSize-10,listSize);
        }
        chats.put(key,newMessages);
    }

    public static List<ChatMessage> get(String key){
        List<ChatMessage> messages = chats.get(key);
        if (Validator.isEmpty(messages)){
            return new ArrayList<>();
        }else {
            return messages;
        }
    }
}
