package com.ulwx.tool;

import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xerial.snappy.Snappy;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Enumeration;
import java.util.zip.*;

public class ZipUtils {
	private static final Logger log = LoggerFactory.getLogger(ZipUtils.class);
	/**
	 * 压缩多个文件到一个zip文件
	 * @param files   要压缩的文件路径数组
	 * @param zipFile 输出的zip文件路径
	 * @throws IOException
	 */
	public static void zipFiles(String[] files, String zipFile) throws IOException {
		try (FileOutputStream fos = new FileOutputStream(zipFile);
			 ZipOutputStream zos = new ZipOutputStream(fos)) {

			byte[] buffer = new byte[1024];

			for (String filePath : files) {
				File file = new File(filePath);
				if (!file.exists() || file.isDirectory()) {
					System.out.println("跳过不存在或是目录的文件：" + filePath);
					continue;
				}

				try (FileInputStream fis = new FileInputStream(file)) {
					// 添加 Zip 条目
					zos.putNextEntry(new ZipEntry(file.getName()));

					int len;
					while ((len = fis.read(buffer)) > 0) {
						zos.write(buffer, 0, len);
					}

					zos.closeEntry();
				}
			}
		}
	}

	public static void zipFiles(File[] files, File zipFile) throws IOException {
		try (FileOutputStream fos = new FileOutputStream(zipFile);
			 ZipOutputStream zos = new ZipOutputStream(fos)) {

			byte[] buffer = new byte[1024];

			for (File fileElm : files) {
				File file = fileElm;
				if (!file.exists() || file.isDirectory()) {
					continue;
				}

				try (FileInputStream fis = new FileInputStream(file)) {
					// 添加 Zip 条目
					zos.putNextEntry(new ZipEntry(file.getName()));

					int len;
					while ((len = fis.read(buffer)) > 0) {
						zos.write(buffer, 0, len);
					}

					zos.closeEntry();
				}
			}
		}
	}
	public static byte[] gzip(byte[] bs) throws Exception {
		ByteArrayOutputStream bout = new ByteArrayOutputStream(1000);
		GZIPOutputStream gzout = null;
		try {
			gzout = new GZIPOutputStream(bout);
			gzout.write(bs);
			gzout.flush();
		} catch (Exception e) {
			throw e;

		} finally {
			if (gzout != null) {
				try {
					gzout.close();
				} catch (Exception ex) {
				}
			}
		}

		return bout.toByteArray();

	}

	public static byte[] ungzip(byte[] bs) throws Exception {
		GZIPInputStream gzin = null;
		try {
			ByteArrayInputStream bin = new ByteArrayInputStream(bs);
			gzin = new GZIPInputStream(bin);
			return FileUtils.readBytes(gzin);
		} catch (Exception e) {
			throw e;

		} finally {
		}

	}

	public static byte[] zip(byte[] bs) throws Exception {

		ByteArrayOutputStream o = null;
		try {
			o = new ByteArrayOutputStream();
			Deflater compresser = new Deflater();
			compresser.setInput(bs);
			compresser.finish();
			byte[] output = new byte[1024];
			while (!compresser.finished()) {
				int got = compresser.deflate(output);
				o.write(output, 0, got);
			}
			o.flush();
			return o.toByteArray();
		} catch (Exception ex) {
			throw ex;

		} finally {
			if (o != null) {
				try {
					o.close();
				} catch (IOException e) {
					throw e;
				}
			}
		}

	}

	public static byte[] unzip(byte[] bs) throws Exception {

		ByteArrayOutputStream o = null;
		try {
			o = new ByteArrayOutputStream();
			Inflater decompresser = new Inflater();
			decompresser.setInput(bs);
			byte[] result = new byte[1024];
			while (!decompresser.finished()) {
				int resultLength = decompresser.inflate(result);
				o.write(result, 0, resultLength);
			}
			decompresser.end();
			o.flush();
			return o.toByteArray();
		} catch (Exception ex) {
			throw ex;

		} finally {
			if (o != null) {
				try {
					o.close();
				} catch (IOException e) {
					throw e;
				}
			}
		}

	}

	/**
	 * 
	 * @param srcPathName
	 *            源文件或源目录
	 * @param destZipFile
	 *            目标压缩文件
	 * @param fileEncoding
	 *            压缩文件里的文件名的编码
	 * @throws Exception
	 */

	public static void zip(String srcPathName, String destZipFile,
			String fileEncoding) throws Exception {
		try {
			final OutputStream out = new FileOutputStream(destZipFile);
			ZipArchiveOutputStream zos = new ZipArchiveOutputStream(out);
			zos.setEncoding(fileEncoding);

			RecursiveZip(new File(srcPathName), "", zos);
			zos.close();
		} catch (Exception e) {
			log.error("", e);
		}

	}

	// 递归压缩函数
	// fDir　要压缩的目录或者文件
	// pName　父压缩记录名称，第一次调用应该被设置为一个空字符串""
	// zos　压缩输出流
	private static void RecursiveZip(File fDir, String pName,
			ArchiveOutputStream zos) throws Exception {
		if (fDir.isDirectory()) {
			// 如果为目录，ZipEntry名称的尾部应该以反斜杠"/"结尾

			zos.putArchiveEntry(new ZipArchiveEntry(pName + fDir.getName()
					+ "/"));
			File[] files = fDir.listFiles();
			if (files != null) {
				for (int i = 0; i < files.length; i++) {
					// 进行递归，同时传递父文件ZipEntry的名称，还有压缩输出流
					RecursiveZip(files[i], pName + fDir.getName() + "/", zos);
				}
			}
		}
		if (fDir.isFile()) {
			byte[] bt = new byte[2048];
			ZipArchiveEntry ze = new ZipArchiveEntry(pName + fDir.getName());
			// 设置压缩前的文件大小
			ze.setSize(fDir.length());
			zos.putArchiveEntry(ze);
			FileInputStream fis = new FileInputStream(fDir);

			try {
				IOUtils.copy(fis, zos, false);
			} finally {
				fis.close();
			}
			zos.closeArchiveEntry();
		}
	}
	private static final Charset[] CHARSETS = {
			Charset.forName("GBK"),
			Charset.forName("UTF-8"),
			Charset.forName("GB18030"),
			Charset.forName("CP437"),
			Charset.forName("ISO-8859-1")
	};
	/**
	 * 解压zip文件到指定的目录 File Unzip
	 *
	 * @param sToPath
	 *            Directory path to be unzipted to
	 * @param sZipFile
	 *            zip File Name to be ziped
	 */
	public static void unZip(String sToPath, String sZipFile, String fileNameEncoding) throws Exception {
		if (null == sToPath || ("").equals(sToPath.trim())) {
			File objZipFile = new File(sZipFile);
			sToPath = objZipFile.getParent();
		}

		// 先使用ISO-8859-1读取，然后手动转换文件名
		try (InputStream is = new FileInputStream(sZipFile);
			 ZipArchiveInputStream zis = new ZipArchiveInputStream(is, "ISO-8859-1", false)) {

			byte[] buf = new byte[1024];
			int entryCounter = 0;

			while (true) {
				ZipArchiveEntry ze = null;
				try {
					ze = zis.getNextZipEntry();
					if (ze == null) break; // 没有更多条目

					entryCounter++;
				} catch (Exception e) {
					// 如果读取条目时发生编码错误，尝试跳过这个条目
					log.error("读取ZIP条目时发生错误，尝试跳过: {}", e.getMessage());

					// 尝试跳过损坏的条目
					try {
						// 跳过当前条目的内容
						long skipped = zis.skip(Long.MAX_VALUE);
						log.debug("跳过了 {} 字节", skipped);
						continue;
					} catch (Exception skipEx) {
						log.error("无法跳过损坏的ZIP条目", skipEx);
						break;
					}
				}

				try {
					// 处理文件名编码问题
					String originalName = ze.getName();
					String decodedName = decodeZipEntryName(originalName);
					String safeName = makeSafeFileName(decodedName);

					if (ze.isDirectory()) {
						String dir = FileUtils.getDirectoryPath(sToPath + "/" + safeName);
						FileUtils.makeDirectory(dir);
					} else {
						String filePath = FileUtils.getDirectoryPath(sToPath + "/" + safeName);
						FileUtils.makeDirectory(FileUtils.getParentPath(filePath));

						try (OutputStream os = new BufferedOutputStream(new FileOutputStream(filePath))) {
							int readLen = 0;
							while ((readLen = zis.read(buf, 0, 1024)) != -1) {
								os.write(buf, 0, readLen);
							}
						}
					}
				} catch (Exception e) {
					log.error("解压文件失败！条目: {}, 错误: {}", ze != null ? ze.getName() : "unknown", e.getMessage());
					// 继续处理下一个文件
				}
			}
		}
	}

	/**
	 * 解码ZIP条目名称
	 */
	private static String decodeZipEntryName(String name) {
		if (name == null) return "unknown_" + System.currentTimeMillis();

		// 尝试多种编码解码
		String[] encodings = {"GBK", "UTF-8", "GB2312", "CP437"};

		for (String encoding : encodings) {
			try {
				// 假设名称是以ISO-8859-1存储的，尝试转换到目标编码
				byte[] bytes = name.getBytes("ISO-8859-1");
				String decoded = new String(bytes, encoding);

				// 检查解码后的字符串是否合理（不包含太多乱码字符）
				if (isValidFileName(decoded)) {
					return decoded;
				}
			} catch (Exception e) {
				log.error("当前编码 {} 失败: {}，尝试下一个编码", encoding, e.getMessage());
			}
		}

		// 如果所有编码都失败，返回一个安全的默认名称
		return "decoded_file_" + System.currentTimeMillis();
	}

	/**
	 * 检查文件名是否有效（不包含太多乱码）
	 */
	private static boolean isValidFileName(String fileName) {
		if (fileName == null || fileName.isEmpty()) return false;
		return true;
	}

	/**
	 * 创建安全的文件名（过滤Windows文件系统不允许的字符）
	 */
	private static String makeSafeFileName(String fileName) {
		if (fileName == null) return "unknown";

		// Windows文件系统不允许的字符
		String illegalChars = "[\\\\/:*?\"<>|]";

		// 替换非法字符为下划线
		String safeName = fileName.replaceAll(illegalChars, "_");

		// 处理文件名过长的情况
		if (safeName.length() > 255) {
			String extension = "";
			int lastDot = safeName.lastIndexOf(".");
			if (lastDot > 0) {
				extension = safeName.substring(lastDot);
				safeName = safeName.substring(0, lastDot);
			}

			// 保留前250个字符（留5个字符给编号和扩展名）
			if (safeName.length() > 250) {
				safeName = safeName.substring(0, 250) + "_truncated";
			}

			safeName = safeName + extension;
		}

		// 处理文件名以空格或点结尾的情况（Windows不允许）
		safeName = safeName.replaceAll("[. ]+$", "_");

		// 如果文件名变为空，使用默认名
		if (safeName.trim().isEmpty()) {
			safeName = "unnamed_file_" + System.currentTimeMillis();
		}

		return safeName;
	}
	public static void unZip(String sToPath, File srcZipFile,
			String fileNameEncoding) throws Exception {

		if (null == sToPath || ("").equals(sToPath.trim())) {
			File objZipFile = srcZipFile;
			sToPath = objZipFile.getParent();
		}

		// 先使用ISO-8859-1读取，然后手动转换文件名
		try (InputStream is = new FileInputStream(srcZipFile);
			 ZipArchiveInputStream zis = new ZipArchiveInputStream(is, "ISO-8859-1", false)) {

			byte[] buf = new byte[1024];
			int entryCounter = 0;

			while (true) {
				ZipArchiveEntry ze = null;
				try {
					ze = zis.getNextZipEntry();
					if (ze == null) break; // 没有更多条目

					entryCounter++;
				} catch (Exception e) {
					// 如果读取条目时发生编码错误，尝试跳过这个条目
					log.error("读取ZIP条目时发生错误，尝试跳过: {}", e.getMessage());

					// 尝试跳过损坏的条目
					try {
						// 跳过当前条目的内容
						long skipped = zis.skip(Long.MAX_VALUE);
						log.debug("跳过了 {} 字节", skipped);
						continue;
					} catch (Exception skipEx) {
						log.error("无法跳过损坏的ZIP条目", skipEx);
						break;
					}
				}

				try {
					// 处理文件名编码问题
					String originalName = ze.getName();
					String decodedName = decodeZipEntryName(originalName);
					String safeName = makeSafeFileName(decodedName);

					if (ze.isDirectory()) {
						String dir = FileUtils.getDirectoryPath(sToPath + "/" + safeName);
						FileUtils.makeDirectory(dir);
					} else {
						String filePath = FileUtils.getDirectoryPath(sToPath + "/" + safeName);
						FileUtils.makeDirectory(FileUtils.getParentPath(filePath));

						try (OutputStream os = new BufferedOutputStream(new FileOutputStream(filePath))) {
							int readLen = 0;
							while ((readLen = zis.read(buf, 0, 1024)) != -1) {
								os.write(buf, 0, readLen);
							}
						}
					}
				} catch (Exception e) {
					log.error("解压文件失败！条目: {}, 错误: {}", ze != null ? ze.getName() : "unknown", e.getMessage());
					// 继续处理下一个文件
				}
			}
		}
	}

	public byte[] snappyZip(String str)throws Exception{
		byte[] compressed = Snappy.compress(str.getBytes("UTF-8"));
		return compressed;
	}

	public String snappyUnZip(byte[] compressed)throws Exception{
		byte[] uncompressed = Snappy.uncompress(compressed);

		String result = new String(uncompressed, "UTF-8");
		return result;
	}
	public static void main(String[] args) throws Exception {
		
		ZipUtils.unZip("d:/sunchaojin", "d:/坤平.zip", "gbk");
	

	}
}
