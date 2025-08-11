package com.ulwx.tool;

import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xerial.snappy.Snappy;

import java.io.*;
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

	/**
	 * 解压zip文件到指定的目录 File Unzip
	 * 
	 * @param sToPath
	 *            Directory path to be unzipted to
	 * @param sZipFile
	 *            zip File Name to be ziped
	 */
	public static void unZip(String sToPath, String sZipFile,
			String fileNameEncoding) throws Exception {

		if (null == sToPath || ("").equals(sToPath.trim())) {
			File objZipFile = new File(sZipFile);
			sToPath = objZipFile.getParent();
			// System.out.println(sToPath);
		}
		ZipFile zfile = new ZipFile(sZipFile, fileNameEncoding);

		Enumeration zList = zfile.getEntries();
		ZipArchiveEntry ze = null;
		byte[] buf = new byte[1024];
		while (zList.hasMoreElements()) {

			ze = (ZipArchiveEntry)zList.nextElement();

			if (ze.isDirectory()) {

				String dir = FileUtils.getDirectoryPath(sToPath + "/"
						+ ze.getName());

				FileUtils.makeDirectory(dir);


			} else {
				FileUtils.makeDirectory(sToPath);
				String filePath = FileUtils.getDirectoryPath(sToPath + "/"
						+ ze.getName());
				FileUtils.makeDirectory(FileUtils.getParentPath(filePath));
				OutputStream os = new BufferedOutputStream(
						new FileOutputStream(filePath));
				InputStream is = new BufferedInputStream(
						zfile.getInputStream(ze));
				int readLen = 0;
				while ((readLen = is.read(buf, 0, 1024)) != -1) {
					os.write(buf, 0, readLen);
				}
				is.close();
				os.close();
			}
		}
		zfile.close();
	}

	public static void unZip(String sToPath, File srcZipFile,
			String fileNameEncoding) throws Exception {

		if (null == sToPath || ("").equals(sToPath.trim())) {
			File objZipFile = srcZipFile;
			sToPath = objZipFile.getParent();
			// System.out.println(sToPath);
		}
		ZipFile zfile = new ZipFile(srcZipFile, fileNameEncoding);

		Enumeration zList = zfile.getEntries();
		ZipArchiveEntry ze = null;
		byte[] buf = new byte[1024];
		while (zList.hasMoreElements()) {

			ze = (ZipArchiveEntry)zList.nextElement();

			if (ze.isDirectory()) {
			
				String dir = FileUtils.getDirectoryPath(sToPath + "/"
						+ ze.getName());
				
				FileUtils.makeDirectory(dir);
				

			} else {
				FileUtils.makeDirectory(sToPath);
				String filePath = FileUtils.getDirectoryPath(sToPath + "/"
						+ ze.getName());

				FileUtils.makeDirectory(FileUtils.getParentPath(filePath));
				OutputStream os = new BufferedOutputStream(
						new FileOutputStream(filePath));
				InputStream is = new BufferedInputStream(
						zfile.getInputStream(ze));
				int readLen = 0;
				while ((readLen = is.read(buf, 0, 1024)) != -1) {
					os.write(buf, 0, readLen);
				}
				is.close();
				os.close();
			}
		}
		zfile.close();
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
