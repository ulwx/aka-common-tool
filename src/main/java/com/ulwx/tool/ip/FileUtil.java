package com.ulwx.tool.ip;

import com.ulwx.tool.IOUtils;
import org.slf4j.Logger;

import java.io.InputStream;

public  class FileUtil {
	private static final Logger log = org.slf4j.LoggerFactory.getLogger(FileUtil.class);


	public static byte[] getJarResourceToBytes(String name)throws Exception{
		 InputStream stream=FileUtil.class.getResourceAsStream(name);
		 return IOUtils.toByteArray(stream, true);
	}
	public static void main(String[] args) throws Exception{
		//Path pp=Paths.get(new URI("file:/e:/1.txt"));
		//String s=com.ulwx.tool.Path.getClassPath("1.txt");
		//URL url=new URL(s);
		//System.out.println(pp);
		//Path pp=classpath("/qqwry.dat");
		int i=0;
	}
	private FileUtil() {
	}
}