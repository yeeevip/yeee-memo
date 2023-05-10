package vip.yeee.memo.integrate.netty.heartcheck.server.components;

import io.netty.channel.Channel;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *  工具类把客户端id作为key放入map中 把通道放入对应value，相当于做一个绑定
*/
@Component(value = "channelRepository")
public class ChannelRepository {
	private final static Map<String, Channel> channelCache = new ConcurrentHashMap<String, Channel>();

	public void put(String key, Channel value) {
		channelCache.put(key, value);
	}

	public Channel get(String key) {
		return channelCache.get(key);
	}

	public void remove(String key) { 
		channelCache.remove(key);
	}

	public int size() {
		return channelCache.size();
	}
}
