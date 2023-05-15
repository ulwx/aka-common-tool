package com.ulwx.tool.ip;


import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;

public class IPSeekerSimpleCache extends IPSeeker {
	private final Map<byte[], IPLocation> cache = new Hashtable<byte[], IPLocation>();
	
	public IPSeekerSimpleCache(byte[] data) throws IOException {
		super(data);
	}
	
	@Override
	public synchronized IPLocation getLocation(byte ip1, byte ip2, byte ip3, byte ip4) {
		final byte[] ip = { ip1, ip2, ip3, ip4 };
		if (cache.containsKey(ip)) {
			return cache.get(ip);
		} else {
			return cache.put(ip, super.getLocation(ip));
		}
	}
}
