package cn.wxjia.gfmonitor.tool;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class StreamTool {
	/**
	 * 将InputStream 转化为 byte[]
	 * 
	 * @throws IOException
	 */

	public static byte[] getByteFromInputStream(InputStream inputStream)
			throws IOException {
		byte[] bytes = new byte[1024];
		ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
		int len = 0;
		while ((len = inputStream.read(bytes)) != -1) {
			arrayOutputStream.write(bytes, 0, len);
		}
		inputStream.close();
		return arrayOutputStream.toByteArray();

	}

}
