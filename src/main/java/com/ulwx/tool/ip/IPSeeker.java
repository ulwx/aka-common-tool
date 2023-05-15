package com.ulwx.tool.ip;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.Inet4Address;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;


public class IPSeeker {
	final ByteBuffer buffer;
	final Helper h;
	final int offsetBegin, offsetEnd;

	public IPSeeker(byte[] data) throws IOException {
		if (data!=null && data.length>0) {
			buffer = ByteBuffer.wrap(data);
			buffer.order(ByteOrder.LITTLE_ENDIAN);
			offsetBegin = buffer.getInt(0);
			offsetEnd   = buffer.getInt(4);
			if (offsetBegin == -1 || offsetEnd == -1) {
				throw new IllegalArgumentException("File Format Error");
			}
			h = new Helper(this);
		} else {
			throw new FileNotFoundException();
		}

	}
	
	public IPLocation getLocation(final byte ip1, final byte ip2, final byte ip3, final byte ip4) {
		return getLocation(new byte[] { ip1, ip2, ip3, ip4 });
	}
	
	protected final IPLocation getLocation(final byte[] ip) {
		return h.getLocation(h.locateOffset(ip));
	}
	
	public IPLocation getLocation(final Inet4Address address) {
		return getLocation(address.getAddress());
	}
}