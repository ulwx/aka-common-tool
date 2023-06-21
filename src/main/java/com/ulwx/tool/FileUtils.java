package com.ulwx.tool;

import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URL;
import java.util.*;
import java.util.zip.Checksum;

public class FileUtils {

	private static Logger log = LoggerFactory.getLogger(FileUtils.class);

	/**
	 * Finds files within a given directory (and optionally its subdirectories)
	 * which match an array of extensions.
	 * 
	 * @param directory
	 *            - the directory to search in
	 * @param extensions
	 *            - an array of extensions, ex. {"java","xml"}. If this
	 *            parameter is null, all files are returned.
	 * @param recursive
	 *            - if true all subdirectories are searched as well
	 * @return
	 */
	public static Collection<File> listFiles(File directory, String[] extensions,
			boolean recursive) {
		return org.apache.commons.io.FileUtils.listFiles(directory, extensions,
				recursive);
	}

	/**
	 * 检测 relativePath是否包含..或..等字符，防止攻击
	 * @param relativePath
	 */
	public static void checkRelativePath(String relativePath)
	{
		File file = new File(relativePath);

		if (file.isAbsolute())
		{
			throw new RuntimeException("Directory traversal attempt - absolute path not allowed");
		}

		String pathUsingCanonical;
		String pathUsingAbsolute;
		try
		{
			pathUsingCanonical = file.getCanonicalPath();
			pathUsingAbsolute = file.getAbsolutePath();
		}
		catch (IOException e)
		{
			throw new RuntimeException("Directory traversal attempt?", e);
		}


		// Require the absolute path and canonicalized path match.
		// This is done to avoid directory traversal
		// attacks, e.g. "1/../2/"
		if (! pathUsingCanonical.equals(pathUsingAbsolute))
		{
			throw new RuntimeException("Directory traversal attempt?");
		}
	}
	public static Collection<File> listFilesAndDirs(File directory, 
			boolean recursive) {
		

        
		return org.apache.commons.io.FileUtils.listFilesAndDirs(directory, TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE);
				
	}

	/**
	 * Allows iteration over the files in a given directory (and optionally its
	 * subdirectories) which match an array of extensions. This method is based
	 * on listFiles(File, String[], boolean).
	 * 
	 * @param directory
	 *            - the directory to search in
	 * @param extensions
	 *            - an array of extensions, ex. {"java","xml"}. If this
	 *            parameter is null, all files are returned.
	 * @param recursive
	 * @return
	 */
	public static Iterator iterateFiles(File directory, String[] extensions,
			boolean recursive) {
		return org.apache.commons.io.FileUtils.iterateFiles(directory,
				extensions, recursive);
	}

	/**
	 * Copies a filtered directory to a new location.
	 * 
	 * This method copies the contents of the specified source directory to
	 * within the specified destination directory.
	 * 
	 * The destination directory is created if it does not exist. If the
	 * destination directory did exist, then this method merges the source with
	 * the destination, with the source taking precedence.
	 * 
	 * Example: Copy directories only // only copy the directory structure
	 * FileUtils.copyDirectory(srcDir, destDir, DirectoryFileFilter.DIRECTORY,
	 * false); Example: Copy directories and txt files // Create a filter for
	 * ".txt" files IOFileFilter txtSuffixFilter =
	 * FileFilterUtils.suffixFileFilter(".txt"); IOFileFilter txtFiles =
	 * FileFilterUtils.andFileFilter(FileFileFilter.FILE, txtSuffixFilter);
	 * 
	 * // Create a filter for either directories or ".txt" files FileFilter
	 * filter = FileFilterUtils.orFileFilter(DirectoryFileFilter.DIRECTORY,
	 * txtFiles);
	 * 
	 * // Copy using the filter FileUtils.copyDirectory(srcDir, destDir, filter,
	 * false);
	 * 
	 * @param srcDir
	 * @param destDir
	 * @param filter
	 * @param preserveFileDate
	 * @throws IOException
	 */
	public static void copyDirectory(File srcDir, File destDir,
			FileFilter filter, boolean preserveFileDate) throws IOException {
		org.apache.commons.io.FileUtils.copyDirectory(srcDir, destDir, filter,
				preserveFileDate);
	}

	/**
	 * Copies a filtered directory to a new location preserving the file dates.
	 * 
	 * This method copies the contents of the specified source directory to
	 * within the specified destination directory.
	 * 
	 * The destination directory is created if it does not exist. If the
	 * destination directory did exist, then this method merges the source with
	 * the destination, with the source taking precedence
	 * 
	 * Example: Copy directories only // only copy the directory structure
	 * FileUtils.copyDirectory(srcDir, destDir, DirectoryFileFilter.DIRECTORY);
	 * Example: Copy directories and txt files // Create a filter for ".txt"
	 * files IOFileFilter txtSuffixFilter =
	 * FileFilterUtils.suffixFileFilter(".txt"); IOFileFilter txtFiles =
	 * FileFilterUtils.andFileFilter(FileFileFilter.FILE, txtSuffixFilter);
	 * 
	 * // Create a filter for either directories or ".txt" files FileFilter
	 * filter = FileFilterUtils.orFileFilter(DirectoryFileFilter.DIRECTORY,
	 * txtFiles);
	 * 
	 * // Copy using the filter FileUtils.copyDirectory(srcDir, destDir,
	 * filter);
	 * 
	 * @param srcDir
	 * @param destDir
	 * @param filter
	 * @throws IOException
	 */
	public static void copyDirectory(File srcDir, File destDir,
			FileFilter filter) throws IOException {

		org.apache.commons.io.FileUtils.copyDirectory(srcDir, destDir, filter);
	}

	/**
	 * Copies a whole directory to a new location.
	 * 
	 * This method copies the contents of the specified source directory to
	 * within the specified destination directory.
	 * 
	 * The destination directory is created if it does not exist. If the
	 * destination directory did exist, then this method merges the source with
	 * the destination, with the source taking precedence.
	 * 
	 * @param srcDir
	 *            - an existing directory to copy, must not be null
	 * @param destDir
	 *            - the new directory, must not be null
	 * @param preserveFileDate
	 *            - true if the file date of the copy should be the same as the
	 *            original
	 * @throws IOException
	 */
	public static void copyDirectory(File srcDir, File destDir,
			boolean preserveFileDate) throws IOException {
		org.apache.commons.io.FileUtils.copyDirectory(srcDir, destDir,
				preserveFileDate);
	}

	/**
	 * Copies a whole directory to a new location preserving the file dates.
	 * 
	 * This method copies the specified directory and all its child directories
	 * and files to the specified destination. The destination is the new
	 * location and name of the directory.
	 * 
	 * The destination directory is created if it does not exist. If the
	 * destination directory did exist, then this method merges the source with
	 * the destination, with the source taking precedence.
	 * 
	 * @param srcDir
	 * @param destDir
	 * @throws IOException
	 */
	public static void copyDirectory(File srcDir, File destDir)
			throws IOException {
		org.apache.commons.io.FileUtils.copyDirectory(srcDir, destDir);
	}

	/**
	 * Copies a directory to within another directory preserving the file dates.
	 * 
	 * This method copies the source directory and all its contents to a
	 * directory of the same name in the specified destination directory.
	 * 
	 * The destination directory is created if it does not exist. If the
	 * destination directory did exist, then this method merges the source with
	 * the destination, with the source taking precedence.
	 * 
	 * @param srcDir
	 *            - an existing directory to copy, must not be null
	 * @param destDir
	 *            - the directory to place the copy in, must not be null
	 * @throws IOException
	 */
	public static void copyDirectoryToDirectory(File srcDir, File destDir)
			throws IOException {
		org.apache.commons.io.FileUtils.copyDirectoryToDirectory(srcDir,
				destDir);
	}

	/**
	 * Copies a file to a new location.
	 * 
	 * This method copies the contents of the specified source file to the
	 * specified destination file. The directory holding the destination file is
	 * created if it does not exist. If the destination file exists, then this
	 * method will overwrite it.
	 * 
	 * @param srcFile
	 *            - an existing file to copy, must not be null
	 * @param destFile
	 *            - the new file, must not be null
	 * @param preserveFileDate
	 *            - true if the file date of the copy should be the same as the
	 *            original
	 * @throws IOException
	 */
	public static void copyFile(File srcFile, File destFile,
			boolean preserveFileDate) throws IOException {
		org.apache.commons.io.FileUtils.copyFile(srcFile, destFile,
				preserveFileDate);
	}

	/**
	 * Copies a file to a new location preserving the file date.
	 * 
	 * This method copies the contents of the specified source file to the
	 * specified destination file. The directory holding the destination file is
	 * created if it does not exist. If the destination file exists, then this
	 * method will overwrite it.
	 * 
	 * @param srcFile
	 *            - an existing file to copy, must not be null
	 * @param destFile
	 *            - the new file, must not be null
	 * @throws IOException
	 */
	public static void copyFile(File srcFile, File destFile) throws IOException {
		org.apache.commons.io.FileUtils.copyFile(srcFile, destFile);
	}
	
	public static void copyInputStreamToFile(InputStream source, File destination)throws IOException {
		org.apache.commons.io.FileUtils.copyInputStreamToFile(source, destination);
	}

	/**
	 * Copies a file to a directory optionally preserving the file date.
	 * 
	 * This method copies the contents of the specified source file to a file of
	 * the same name in the specified destination directory. The destination
	 * directory is created if it does not exist. If the destination file
	 * exists, then this method will overwrite it.
	 * 
	 * @param srcFile
	 *            - an existing file to copy, must not be null
	 * @param destDir
	 *            - the directory to place the copy in, must not be null
	 * @param preserveFileDate
	 *            - true if the file date of the copy should be the same as the
	 *            original
	 * @throws IOException
	 */
	public static void copyFileToDirectory(File srcFile, File destDir,
			boolean preserveFileDate) throws IOException {
		org.apache.commons.io.FileUtils.copyFileToDirectory(srcFile, destDir,
				preserveFileDate);
	}

	/**
	 * Copies a file to a directory preserving the file date.
	 * 
	 * This method copies the contents of the specified source file to a file of
	 * the same name in the specified destination directory. The destination
	 * directory is created if it does not exist. If the destination file
	 * exists, then this method will overwrite it.
	 * 
	 * @param srcFile
	 *            - an existing file to copy, must not be null
	 * @param destDir
	 *            - the directory to place the copy in, must not be null
	 * @throws IOException
	 */
	public static void copyFileToDirectory(File srcFile, File destDir)
			throws IOException {
		org.apache.commons.io.FileUtils.copyFileToDirectory(srcFile, destDir);
	}

	/**
	 * Converts each of an array of File to a URL.
	 * 
	 * @param files
	 *            - the files to convert
	 * @return an array of URLs matching the input
	 * @throws IOException
	 */
	public static URL[] toURLs(File[] files) throws IOException {
		return org.apache.commons.io.FileUtils.toURLs(files);
	}

	/**
	 * Compares the contents of two files to determine if they are equal or not.
	 * This method checks to see if the two files are different lengths or if
	 * they point to the same file, before resorting to byte-by-byte comparison
	 * of the contents. Code origin: Avalon
	 * 
	 * @param file1
	 * @param file2
	 * @return true if the content of the files are equal or they both don't
	 *         exist, false otherwise
	 * @throws IOException
	 */
	public static boolean contentEquals(File file1, File file2)
			throws IOException {
		return org.apache.commons.io.FileUtils.contentEquals(file1, file2);
	}

	/**
	 * Computes the checksum of a file using the CRC32 checksum routine. The
	 * value of the checksum is returned.
	 * 
	 * @param file
	 *            - the file to checksum, must not be null
	 * @return the checksum value
	 * @throws IOException
	 */
	public static long checksumCRC32(File file) throws IOException {
		return org.apache.commons.io.FileUtils.checksumCRC32(file);
	}

	/**
	 * Computes the checksum of a file using the specified checksum object.
	 * Multiple files may be checked using one Checksum instance if desired
	 * simply by reusing the same checksum object. For example:
	 * 
	 * long csum = FileUtils.checksum(file, new CRC32()).getValue();
	 * 
	 * @param file
	 *            - the file to checksum, must not be null
	 * @param checksum
	 *            - the checksum object to be used, must not be null
	 * @return the checksum specified, updated with the content of the file
	 * @throws IOException
	 */
	public static Checksum checksum(File file, Checksum checksum)
			throws IOException {

		return org.apache.commons.io.FileUtils.checksum(file, checksum);
	}

	/**
	 * Copies bytes from the URL source to a file destination. The directories
	 * up to destination will be created if they don't already exist.
	 * destination will be overwritten if it already exists.
	 * 
	 * @param source
	 * @param destination
	 * @throws IOException
	 */
	public static void copyURLToFile(URL source, File destination)
			throws IOException {
		org.apache.commons.io.FileUtils.copyURLToFile(source, destination);
	}

	
	/**
	 * Deletes a directory recursively.
	 * 
	 * @param directory
	 *            - directory to delete
	 * @throws IOException
	 */

	public static void deleteDirectory(File directory) throws IOException {
		org.apache.commons.io.FileUtils.deleteDirectory(directory);
	}

	/**
	 * Deletes a file. If file is a directory, delete it and all
	 * sub-directories. The difference between File.delete() and this method
	 * are:
	 * 
	 * •A directory to be deleted does not have to be empty. •You get exceptions
	 * when a file or directory cannot be deleted. (java.io.File methods returns
	 * a boolean)
	 * 
	 * @param file
	 *            - file or directory to delete, must not be null
	 * @throws IOException
	 */
	public static void delete(File file) throws IOException {
		org.apache.commons.io.FileUtils.forceDelete(file);
	}

	/**
	 * Deletes a file, never throwing an exception. If file is a directory,
	 * delete it and all sub-directories.
	 * 
	 * The difference between File.delete() and this method are: •A directory to
	 * be deleted does not have to be empty. •No exceptions are thrown when a
	 * file or directory cannot be deleted.
	 * 
	 * @param file
	 *            - file or directory to delete, can be null
	 * @return true if the file or directory was deleted, otherwise false
	 */
	public static boolean deleteQuietly(File file) {
		return org.apache.commons.io.FileUtils.deleteQuietly(file);
	}

	/**
	 * Moves a file or directory to the destination directory.
	 * 
	 * When the destination is on another file system, do a "copy and delete".
	 * 
	 * @param src
	 *            - the file or directory to be moved
	 * @param destDir
	 *            - the destination directory
	 * @param createDestDir
	 *            - If true create the destination directory, otherwise if false
	 *            throw an IOException
	 * @throws IOException
	 */
	public static void moveToDirectory(File src, File destDir,
			boolean createDestDir) throws IOException {
		org.apache.commons.io.FileUtils.moveToDirectory(src, destDir,
				createDestDir);
	}

	/**
	 * Moves a file to a directory.
	 * 
	 * @param srcFile
	 *            - the file to be moved
	 * @param destDir
	 *            - the destination file
	 * @param createDestDir
	 *            - If true create the destination directory, otherwise if false
	 *            throw an IOException
	 * @throws IOException
	 */
	public static void moveFileToDirectory(File srcFile, File destDir,
			boolean createDestDir) throws IOException {
		org.apache.commons.io.FileUtils.moveFileToDirectory(srcFile, destDir,
				createDestDir);
	}

	/**
	 * Moves a file.
	 * 
	 * When the destination file is on another file system, do a
	 * "copy and delete".
	 * 
	 * @param srcFile
	 * @param destFile
	 * @throws IOException
	 */
	public static void moveFile(File srcFile, File destFile) throws IOException {
		org.apache.commons.io.FileUtils.moveFile(srcFile, destFile);
	}

	/**
	 * Moves a directory to another directory.
	 * 
	 * @param src
	 * @param destDir
	 * @param createDestDir
	 *            - If true create the destination directory, otherwise if false
	 *            throw an IOException
	 * @throws IOException
	 */
	public static void moveDirectoryToDirectory(File src, File destDir,
			boolean createDestDir) throws IOException {
		org.apache.commons.io.FileUtils.moveDirectoryToDirectory(src, destDir,
				createDestDir);
	}

	/**
	 * Moves a directory.
	 * 
	 * When the destination directory is on another file system, do a
	 * "copy and delete".
	 * 
	 * @param srcDir
	 * @param desDir
	 * @throws Exception
	 */
	public static void moveDirectory(File srcDir, File desDir) throws Exception {
		org.apache.commons.io.FileUtils.moveDirectory(srcDir, desDir);
	}

	/**
	 * Converts each of an array of URL to a File.
	 * 
	 * @param urls
	 * @return
	 */
	public static File[] toFiles(URL[] urls) {
		return org.apache.commons.io.FileUtils.toFiles(urls);
	}

	/**
	 * Convert from a URL to a File
	 */
	public static File toFiles(URL url) {
		return org.apache.commons.io.FileUtils.toFile(url);
	}

	/**
	 * Counts the size of a directory recursively (sum of the length of all
	 * files).
	 * 
	 * @param directory
	 * @return
	 */
	public static long sizeOfDirectory(File directory) {
		return org.apache.commons.io.FileUtils.sizeOfDirectory(directory);
	}

	/**
	 * Opens a FileInputStream for the specified file, providing better error
	 * messages than simply calling new FileInputStream(file). At the end of the
	 * method either the stream will be successfully opened, or an exception
	 * will have been thrown.
	 * 
	 * An exception is thrown if the file does not exist. An exception is thrown
	 * if the file object exists but is a directory. An exception is thrown if
	 * the file exists but cannot be read.
	 * 
	 * @param file
	 *            - the file to open for input, must not be null
	 * @return a new FileInputStream for the specified file
	 * @throws IOException
	 */
	public static FileInputStream openInputStream(File file) throws IOException {
		return org.apache.commons.io.FileUtils.openInputStream(file);
	}

	/**
	 * Opens a FileOutputStream for the specified file, checking and creating
	 * the parent directory if it does not exist. At the end of the method
	 * either the stream will be successfully opened, or an exception will have
	 * been thrown. The parent directory will be created if it does not exist.
	 * The file will be created if it does not exist. An exception is thrown if
	 * the file object exists but is a directory. An exception is thrown if the
	 * file exists but cannot be written to. An exception is thrown if the
	 * parent directory cannot be created.
	 * 
	 * @param file
	 *            - the file to open for output, must not be null
	 * @return
	 * @throws IOException
	 */
	public static FileOutputStream openOutputStream(File file)
			throws IOException {
		return org.apache.commons.io.FileUtils.openOutputStream(file);
	}

	/**
	 * Reads the contents of a file into a String.
	 * 
	 * @param file
	 * @param encoding
	 * @return
	 * @throws IOException
	 */
	public static List<String> readLines(File file, String encoding)
			throws IOException {
		return org.apache.commons.io.FileUtils.readLines(file, encoding);
	}

	public static List<String> listSubDirNames(String path) {
		if (!StringUtils.hasText(path)) {
			return null;
		}
		File fpath = new File(path);
		List<String> dirs = new ArrayList<String>();
		if (fpath.isDirectory()) {
			File[] ff = fpath.listFiles();
			for (File file : ff) {
				if (file.isDirectory()) {
					dirs.add(file.getName());
				}
			}
		}
		return dirs;
	}

	public static File[] listOnlySubDirs(String path) {
		File fpath = new File(path);
		Assert.isTrue(fpath.isDirectory(), "[" + fpath.getAbsolutePath()
				+ "]must be a dir!");
		FileFilter ff = new FileFilter() {
			public boolean accept(File file) {
				return file.isDirectory();
			}

		};
		return FileUtils.listFiles(fpath, ff);
	}

	/**
	 * File exist check
	 * 
	 * @param sFileName
	 *            File Name
	 * @return boolean true - exist<br>
	 *         false - not exist
	 */
	public static boolean checkExist(String sFileName) {

		boolean result = false;

		try {
			File f = new File(sFileName);

			// if (f.exists() && f.isFile() && f.canRead()) {
			if (f.exists() && f.isFile()) {
				result = true;
			} else {
				result = false;
			}
		} catch (Exception e) {
			log.error("", e);
			result = false;
		}

		/* return */
		return result;
	}

	/**
	 * Get File Size
	 * 
	 * @param f  File
	 * @return long File's size(byte) when File not exist return -1
	 */
	private static long getDirectorySize(File f) {
		long size = 0;
		try {
			// File f = new File(sFileName);
			File[] flist = f.listFiles();
			for (int i = 0; flist != null && i < flist.length; i++) {
				if (flist[i].isDirectory()) {
					size = size + getDirectorySize(flist[i]);
				} else {
					size = size + flist[i].length();
				}
			}
		} catch (Exception e) {
			// log.error("读目录大小出错！", e);
			size = -1;
		}

		return size;
	}

	public static long getLastUpdateTime(String sFileName) {
		File f = new File(sFileName);
		if (FileUtils.checkExist(sFileName)) {
			return f.lastModified();
		}
		return 0;

	}

	public static long getDirectorySize(String dirName) {
		File f = new File(dirName);
		return FileUtils.getDirectorySize(f);
	}

	public static long getFileSize(String sFileName) {

		long lSize = -1;

		try {
			File f = new File(sFileName);
			// exist
			if (f.exists()) {
				if (f.isFile() && f.canRead()) {
					lSize = f.length();
				} else if (f.isDirectory() && f.canRead()) {
					lSize = -1;
				} else {
					lSize = -1;
				}
				// not exist
			} else {
				lSize = -1;
			}
		} catch (Exception e) {
			// e.printStackTrace();
			log.error("", e);
			lSize = -1;
		}

		/* return */
		return lSize;
	}

	/**
	 * 根据路径文件名删除文件，如果sFileName标示的是一个目录，则此目录必须为空才能删除
	 * 
	 * @param sFileName
	 *            File Nmae
	 * @return boolean true - Delete Success<br>
	 *         false - Delete Fail
	 */
	public static boolean deleteFromName(String sFileName) {

		boolean bReturn = true;

		try {
			File oFile = new File(sFileName);

			// exist
			if (oFile.exists()) {
				// Delete File
				boolean bResult = oFile.delete();
				// Delete Fail
				if (bResult == false) {
					bReturn = false;
				}

				// not exist
			} else {

			}

		} catch (Exception e) {
			log.error("", e);
			bReturn = false;
		}

		// return
		return bReturn;
	}

	/**
	 * getRealFileName
	 * 
	 * @param baseDir
	 *            Root Directory
	 * @param absFileName
	 *            absolute Directory File Name
	 * @return java.io.File Return file
	 */
	public static File getRealFileName(String baseDir, String absFileName)
			throws Exception {

		File ret = null;

		List dirs = new ArrayList();
		StringTokenizer st = new StringTokenizer(absFileName,
				System.getProperty("file.separator"));
		while (st.hasMoreTokens()) {
			dirs.add(st.nextToken());
		}

		ret = new File(baseDir);
		// if (dirs.size() > 1) {
		// for (int i = 0; i < dirs.size() - 1; i++) {
		// ret = new File(ret, (String) dirs.get(i));
		// }
		// }
		if (!ret.exists()) {
			ret.mkdirs();
		}
		ret = new File(ret, (String)dirs.get(dirs.size() - 1));
		return ret;
	}

	/**
	 * copyFile
	 * 
	 * @param srcFile
	 *            Source File
	 * @param targetFile
	 *            Target file
	 */
	static public void copyFile(String srcFile, String targetFile)
			throws Exception {

		FileInputStream reader = new FileInputStream(srcFile);
		FileOutputStream writer = new FileOutputStream(targetFile);

		byte[] buffer = new byte[4096];
		int len;

		try {
			reader = new FileInputStream(srcFile);
			writer = new FileOutputStream(targetFile);

			while ((len = reader.read(buffer)) > 0) {
				writer.write(buffer, 0, len);
			}
		} catch (IOException e) {
			log.error("", e);
			throw e;
		} finally {
			if (writer != null)
				writer.close();
			if (reader != null)
				reader.close();
		}
	}

	/**
	 * renameFile
	 * 
	 * @param srcFile
	 *            Source File
	 * @param targetFile
	 *            Target file
	 */
	static public void renameFile(String srcFile, String targetFile)
			throws Exception {
		try {
			copyFile(srcFile, targetFile);
			deleteFromName(srcFile);
		} catch (IOException e) {
			throw e;
		}
	}

	public static void write(String pathName, String content)
			throws IOException {

		write(pathName, content, false);

	}

	public static void write(String pathName, byte[] bs) throws Exception {
		FileUtils.makeDirectory(FileUtils.getFileParentPath(pathName));

		try {
			File file = new File(pathName);
			org.apache.commons.io.FileUtils.writeByteArrayToFile(file, bs);
		} catch (Exception e) {
			log.error("", e);
			throw e;
		} finally {

		}
	}

	public static void write(String pathName, String content, boolean append)
			throws IOException {
		PrintWriter out = null;
		try {
			// makeDirFromPathName(pathName);
			FileUtils.makeDirectory(FileUtils.getFileParentPath(pathName));
			FileOutputStream fOut = new FileOutputStream(pathName, append);

			out = new PrintWriter(fOut);
			out.print(content);
		} catch (IOException e) {
			log.error("", e);
			// throw the exception
			throw e;
		} finally {
			if (out != null) {
				out.close();
			}
		}

	}

	public static void write(String pathName, String content, String charset)
			throws IOException {

		write(pathName, content, false, charset);

	}

	public static void write(String pathName, String content, boolean append,
			String charset) throws IOException {
		OutputStreamWriter out = null;
		try {
			// makeDirFromPathName(pathName);
			FileUtils.makeDirectory(FileUtils.getFileParentPath(pathName));
			FileOutputStream fOut = new FileOutputStream(pathName, append);

			out = new OutputStreamWriter(fOut, charset);
			out.write(content);
		} catch (IOException e) {
			log.error("", e);
			throw e;
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (Exception e) {
					log.error("", e);
				}
			}
		}

	}

	/**
	 * 修改文件的最后访问时间。 如果文件不存在则创建该文件。
	 * <b>目前这个方法的行为方式还不稳定，主要是方法有些信息输出，这些信息输出是否保留还在考虑中。</b>
	 * 
	 * @param file
	 *            需要修改最后访问时间的文件。
	 * @since 0.1
	 */
	public static void touch(File file) {
		long currentTime = System.currentTimeMillis();
		if (!file.exists()) {
			// System.err.println("file not found:" + file.getName());
			// System.err.println("Create a new file:" + file.getName());
			try {
				if (file.createNewFile()) {
					// System.out.println("Succeeded!");
				} else {
					// System.err.println("Create file failed!");
				}
			} catch (IOException e) {
				// System.err.println("Create file failed!");
				e.printStackTrace();
			}
		}
		boolean result = file.setLastModified(currentTime);
		if (!result) {
			// System.err.println("touch failed: " + file.getName());
		}
	}

	/**
	 * 修改文件的最后访问时间。 如果文件不存在则创建该文件。
	 * <b>目前这个方法的行为方式还不稳定，主要是方法有些信息输出，这些信息输出是否保留还在考虑中。</b>
	 * 
	 * @param fileName
	 *            需要修改最后访问时间的文件的文件名。
	 * @since 0.1
	 */
	public static void touch(String fileName) {
		File file = new File(fileName);
		touch(file);
	}

	/**
	 * 修改文件的最后访问时间。 如果文件不存在则创建该文件。
	 * <b>目前这个方法的行为方式还不稳定，主要是方法有些信息输出，这些信息输出是否保留还在考虑中。</b>
	 * 
	 * @param files
	 *            需要修改最后访问时间的文件数组。
	 * @since 0.1
	 */
	public static void touch(File[] files) {
		for (int i = 0; i < files.length; i++) {
			touch(files[i]);
		}
	}

	/**
	 * 修改文件的最后访问时间。 如果文件不存在则创建该文件。
	 * <b>目前这个方法的行为方式还不稳定，主要是方法有些信息输出，这些信息输出是否保留还在考虑中。</b>
	 * 
	 * @param fileNames
	 *            需要修改最后访问时间的文件名数组。
	 * @since 0.1
	 */
	public static void touch(String[] fileNames) {
		File[] files = new File[fileNames.length];
		for (int i = 0; i < fileNames.length; i++) {
			files[i] = new File(fileNames[i]);
		}
		touch(files);
	}

	/**
	 * 判断指定的文件是否存在。
	 * 
	 * @param fileName
	 *            要判断的文件的文件名
	 * @return 存在时返回true，否则返回false。
	 * @since 0.1
	 */
	public static boolean isFileExist(String fileName) {
		File file = new File(fileName);
		if (file.exists() && file.isFile()) {
			return true;
		}
		return false;
	}

	public static boolean isDirExist(String fileName) {
		File file = new File(fileName);
		if (file.exists() && file.isDirectory()) {
			return true;
		}
		return false;
	}

	/**
	 * 创建指定的目录。 如果指定的目录的父目录不存在则创建其目录书上所有需要的父目录。 <b>注意：可能会在返回false的时候创建部分父目录。</b>
	 * 
	 * @param file
	 *            要创建的目录
	 * @return 完全创建成功时返回true，否则返回false。
	 * @since 0.1
	 */
	public static boolean makeDirectory(File file) {
		/*
		 * File parent = file.getParentFile(); if (parent != null) { boolean r=
		 * parent.mkdirs(); System.out.println(r); }
		 */

		try {

			boolean r = file.mkdirs();
			// file.
			return r;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			// log.error("", e);
			log.error("", e);
		}
		return false;

	}

	/**
	 * 创建指定的目录。 如果指定的目录的父目录不存在则创建其目录书上所有需要的父目录。 <b>注意：可能会在返回false的时候创建部分父目录。</b>
	 * 
	 * @param fileName
	 *            要创建的目录的目录名
	 * @return 完全创建成功时返回true，否则返回false。
	 * @since 0.1
	 */
	public static boolean makeDirectory(String fileName) {
		// System.out.println("");
		File file = new File(fileName);

		return makeDirectory(file);
	}

	public static void cleanDirectory(File directory) throws IOException {
		Assert.isTrue(directory.isDirectory(),
				"[" + directory.getAbsolutePath() + "]must be a directory");
		org.apache.commons.io.FileUtils.cleanDirectory(directory);
	}

	/**
	 * 清空指定目录中的文件。 这个方法将尽可能删除所有的文件，但是只要有一个文件没有被删除都会返回false。
	 * 另外这个方法不会迭代删除，即不会删除具有内容的子目录。
	 * 
	 * @param directory
	 *            要清空的目录
	 * @return 目录下的所有文件都被成功删除时返回true，否则返回false.
	 * @since 0.1
	 */
	public static boolean emptyDirectory(File directory) {
		// if(!directory.isDirectory()) return false;
		Assert.isTrue(directory.isDirectory(),
				"[" + directory.getAbsolutePath() + "]must be a dir");

		boolean result = false;
		File[] entries = directory.listFiles();
		for (int i = 0; i < entries.length; i++) {
			if (!entries[i].delete()) {
				result = false;
			}
		}
		return true;
	}

	/**
	 * 清空指定目录中的文件。 这个方法将尽可能删除所有的文件，但是只要有一个文件没有被删除都会返回false。
	 * 另外这个方法不会迭代删除，即不会删除具有内容的子目录。
	 * 
	 * @param directoryName
	 *            要清空的目录的目录名
	 * @return 目录下的所有文件都被成功删除时返回true，否则返回false。
	 * @since 0.1
	 */
	public static boolean emptyDirectory(String directoryName) {
		File dir = new File(directoryName);
		return emptyDirectory(dir);
	}

	/**
	 * 删除指定目录及其中的所有内容。
	 * 
	 * @param dirName
	 *            要删除的目录的目录名
	 * @return 删除成功时返回true，否则返回false。
	 * @since 0.1
	 */
	public static void deleteDirectory(String dirName) throws Exception {
		deleteDirectory(new File(dirName));
	}

	/**
	 * 列出目录中的所有内容，包括其子目录中的内容。
	 * 
	 * @param directory
	 *            要列出的目录
	 * @param filter
	 *            过滤器
	 * @return 目录内容的文件数组。
	 * @since 0.1
	 */

	public static File[] listFiles(File directory, FileFilter filter) {
		Assert.isTrue(directory.isDirectory(),
				"[" + directory.getAbsolutePath() + "]must be a dir!");
		return directory.listFiles(filter);
	}

	/**
	 * 在目录里通过通配符来找文件 The wildcard matcher uses the characters '?' and '*' to
	 * represent a single or multiple wildcard characters 例如："*test*.java~*~"
	 * 
	 * @return
	 */
	public static File[] listFilesByWildcard(File directory, String wildcard) {
		Assert.isTrue(directory.isDirectory(),
				"[" + directory.getAbsolutePath() + "]must be a dir!");
		FileFilter fileFilter = new WildcardFileFilter(wildcard);
		return directory.listFiles(fileFilter);

	}

	/**
	 * 返回文件夹里的文件名
	 * 
	 * @param filePathName
	 * @return
	 */
	public static String[] list(String filePathName) {
		File f = new File(filePathName);
		String[] result = f.list();
		if (result == null) {
			return new String[0];
		} else {
			return result;
		}
	}

	public static String[] getTopLevelDirName(String dirpath) {
		File file = new File(dirpath);
		if (file == null)
			return null;
		ArrayList list = new ArrayList();
		if (file.isDirectory()) {
			File[] files = file.listFiles();
			for (int i = 0; i < files.length; i++) {
				if (files[i].isDirectory()) {
					list.add(files[i].getName());
				}
			}
		} else {
			return null;
		}
		return StringUtils.toStringArray(list);
	}

	/**
	 * 从文件路径得到文件名。
	 * 
	 * @param filePath
	 *            文件的路径，可以是相对路径也可以是绝对路径
	 * @return 对应的文件名
	 * @since 0.4
	 */
	public static String getFileName(String filePath) {

		int i = getPathLastIndex(filePath);
		int len = filePath.length();
		// System.out.println(i+"  "+len);
		if (i == len - 1) {
			return "";
		}
		File file = new File(filePath);
		return file.getName();
	}

	/**
	 * 从文件名得到文件绝对路径。
	 * 
	 * @param fileName
	 *            文件名
	 * @return 对应的文件路径
	 * @since 0.4
	 */
	public static String getFilePath(String fileName) {
		File file = new File(fileName);
		return file.getAbsolutePath();
	}

	/**
	 * 将DOS/Windows格式的路径转换为UNIX/Linux格式的路径。
	 * 其实就是将路径中的"\"全部换为"/"，因为在某些情况下我们转换为这种方式比较方便，
	 * 某中程度上说"/"比"\"更适合作为路径分隔符，而且DOS/Windows也将它当作路径分隔符。
	 * 
	 * @param filePath
	 *            转换前的路径
	 * @return 转换后的路径
	 * @since 0.4
	 */
	public static String toUNIXpath(String filePath) {
		return filePath.replace('\\', '/');
	}

	/**
	 * 从文件名得到UNIX风格的文件绝对路径。
	 * 
	 * @param fileName
	 *            文件名
	 * @return 对应的UNIX风格的文件路径
	 * @since 0.4
	 * @see #toUNIXpath(String filePath) toUNIXpath
	 */
	public static String getUNIXfilePath(String fileName) {
		File file = new File(fileName);
		return toUNIXpath(file.getAbsolutePath());
	}

	/**
	 * 得到文件的类型。 实际上就是得到文件名中最后一个“.”后面的部分。
	 * 
	 * @param fileName
	 *            文件名
	 * @return 文件名中的类型部分
	 * @since 0.5
	 */
	public static String getTypePart(String fileName) {
		int point = fileName.lastIndexOf('.');
		int length = fileName.length();
		if (point == -1 || point == length - 1) {
			return "";
		} else {
			return fileName.substring(point + 1, length);
		}
	}
    public static File getTempDirectory() {
        return new File(org.apache.commons.io.FileUtils.getTempDirectoryPath());
    }
	/**
	 * 得到文件的类型。 实际上就是得到文件名中最后一个“.”后面的部分。
	 * 
	 * @param file
	 *            文件
	 * @return 文件名中的类型部分
	 * @since 0.5
	 */
	public static String getFileType(File file) {
		return getTypePart(file.getName());
	}

	/**
	 * 得到文件的名字部分。 实际上就是路径中的最后一个路径分隔符后的部分。
	 * 
	 * @param fileName
	 *            文件名
	 * @return 文件名中的名字部分
	 * @since 0.5
	 */
	public static String getFileNameFromPath(String fileName) {
		int point = getPathLastIndex(fileName);
		int length = fileName.length();
		if (point == -1) {
			return fileName;
		} else if (point == length - 1) {
			int secondPoint = getPathLastIndex(fileName, point - 1);
			if (secondPoint == -1) {
				if (length == 1) {
					return fileName;
				} else {
					return fileName.substring(0, point);
				}
			} else {
				return fileName.substring(secondPoint + 1, point);
			}
		} else {
			return fileName.substring(point + 1);
		}
	}

	public static String getParentPath(String pathname) {
		return getFileParentPath(pathname);
	}

	/**
	 * 得到文件名中的父路径部分。 对两种路径分隔符都有效。 不存在时返回""。
	 * 如果文件名是以路径分隔符结尾的则不考虑该分隔符，例如"/path/"返回""。
	 * 
	 * @param fileName
	 *            文件名
	 * @return 父路径，不存在或者已经是父目录时返回""
	 * @since 0.5
	 */
	public static String getFileParentPath(String fileName) {
		int point = getPathLastIndex(fileName);
		int length = fileName.length();
		if (point == -1) {
			return "";
		} else if (point == length - 1) {
			int secondPoint = getPathLastIndex(fileName, point - 1);
			if (secondPoint == -1) {
				return "";
			} else {
				return fileName.substring(0, secondPoint);
			}
		} else {
			// System.out.println("point="+point);
			return fileName.substring(0, point);
		}
	}

	/**
	 * 如 c:/sun/ 返回 c:/sun
	 * 
	 * @param fileName
	 * @return
	 */
	public static String getDirectoryPath(String fileName) {
		if (fileName == null)
			return "";
		fileName = fileName.trim();
		int point = getPathLastIndex(fileName);
		if (point != -1) {
			if (point == (fileName.length() - 1))
				return fileName.substring(0, point);

		}

		return fileName;
	}

	/**
	 * 得到路径分隔符在文件路径中首次出现的位置。 对于DOS或者UNIX风格的分隔符都可以。
	 * 
	 * @param fileName
	 *            文件路径
	 * @return 路径分隔符在路径中首次出现的位置，没有出现时返回-1。
	 * @since 0.5
	 */
	public static int getPathIndex(String fileName) {
		int point = fileName.indexOf('/');
		if (point == -1) {
			point = fileName.indexOf('\\');
		}
		return point;
	}

	/**
	 * 得到路径分隔符在文件路径中指定位置后首次出现的位置。 对于DOS或者UNIX风格的分隔符都可以。
	 * 
	 * @param fileName
	 *            文件路径
	 * @param fromIndex
	 *            开始查找的位置
	 * @return 路径分隔符在路径中指定位置后首次出现的位置，没有出现时返回-1。
	 * @since 0.5
	 */
	public static int getPathIndex(String fileName, int fromIndex) {
		int point = fileName.indexOf('/', fromIndex);
		if (point == -1) {
			point = fileName.indexOf('\\', fromIndex);
		}
		return point;
	}

	public static String getFileNameBeforeType(String filename) {
		int pos = filename.lastIndexOf(".");
		if (pos == -1)
			return "";
		return filename.substring(0, pos);
	}

	/**
	 * 得到路径分隔符在文件路径中最后出现的位置。 对于DOS或者UNIX风格的分隔符都可以。
	 * 
	 * @param fileName
	 *            文件路径
	 * @return 路径分隔符在路径中最后出现的位置，没有出现时返回-1。
	 * @since 0.5
	 */
	public static int getPathLastIndex(String fileName) {
		int point1 = fileName.lastIndexOf('/');
		int point2 = fileName.lastIndexOf('\\');
		int point = point1 > point2 ? point1 : point2;

		return point;
	}

	/**
	 * 得到路径分隔符在文件路径中指定位置前最后出现的位置。 对于DOS或者UNIX风格的分隔符都可以。
	 * 
	 * @param fileName
	 *            文件路径
	 * @param fromIndex
	 *            开始查找的位置
	 * @return 路径分隔符在路径中指定位置前最后出现的位置，没有出现时返回-1。
	 * @since 0.5
	 */
	public static int getPathLastIndex(String fileName, int fromIndex) {
		int point = fileName.lastIndexOf('/', fromIndex);
		if (point == -1) {
			point = fileName.lastIndexOf('\\', fromIndex);
		}
		return point;
	}

	/**
	 * 将文件名中的类型部分去掉。
	 * 
	 * @param filename
	 *            文件名
	 * @return 去掉类型部分的结果
	 * @since 0.5
	 */
	public static String trimType(String filename) {
		int index = filename.lastIndexOf(".");
		if (index != -1) {
			return filename.substring(0, index);
		} else {
			return filename;
		}
	}

	/**
	 * 得到相对路径。 文件名不是目录名的子节点时返回文件名。
	 * 
	 * @param pathName
	 *            目录名
	 * @param fileName
	 *            文件名
	 * @return 得到文件名相对于目录名的相对路径，目录下不存在该文件时返回文件名
	 * @since 0.5
	 */
	public static String getSubpath(String pathName, String fileName) {
		int index = fileName.indexOf(pathName);
		if (index != -1) {
			return fileName.substring(index + pathName.length() + 1);
		} else {
			return fileName;
		}
	}

	public static byte[] readBytes(String pathName) throws Exception {

		File fs = new File(pathName);

		return org.apache.commons.io.FileUtils.readFileToByteArray(fs);
		// return IOUtils.toByteArray(fs, true);

	}

	public static byte[] readBytes(File file) throws Exception {

		return org.apache.commons.io.FileUtils.readFileToByteArray(file);

	}

	public static byte[] readBytes(InputStream in) throws Exception {

		// byte[] bs = FileCopyUtils.copyToByteArray(in);
		//
		// return bs;
		return IOUtils.toByteArray(in, true);

	}

	public static byte[] readBytes(Reader in, String charset) throws Exception {

		// byte[] bs = FileCopyUtils.copyToByteArray(in);
		//
		// return bs;
		return IOUtils.toByteArray(in, charset, true);

	}

	/**
	 * 已经关闭流的连接
	 * 
	 * @param in
	 * @return
	 * @throws IOException
	 */
	public static String readTxt(Reader in) throws Exception {
		// return FileCopyUtils.copyToString(in);
		return IOUtils.toString(in, true);

	}

	/**
	 * 已经关闭流的连接
	 * 
	 * @param in
	 * @param charset
	 * @return
	 * @throws IOException
	 */
	public static String readTxt(InputStream in, String charset)
			throws Exception {

		// InputStreamReader inputReader=new InputStreamReader(in,charset);;
		// return FileUtils.readTxt(inputReader);
		return IOUtils.toString(in, charset, true);

	}

	/**
	 * 已经关闭流的连接
	 * 
	 * @param file
	 * @param charset
	 * @return
	 * @throws IOException
	 */
	public static String readTxt(File file, String charset) throws IOException {
		return org.apache.commons.io.FileUtils.readFileToString(file, charset);
	}

	/**
	 * 读取文本文件内容
	 * 
	 * @param filePathAndName
	 *            带有完整绝对路径的文件名
	 * @param charset
	 *            文本文件打开的编码方式
	 * @return 返回文本文件的内容
	 */
	public static String readTxt(String filePathAndName, String charset)
			throws IOException {
		// return st;
		File file = new File(filePathAndName);
		return FileUtils.readTxt(file, charset);
	}

	// public static String[] gets(){
	// String[] ss=new String[]{"34","66"};
	// return ss;
	// }
	public static void main(String[] args) {

		File dir = new File("d:/ss");

		// 为了谦让优化以前的方案，因为存在gbk和utf-8的文件格式的文本重叠的情况
		String[] fileNames = dir.list();
		Arrays.sort(fileNames);
		List list = new ArrayList();
		for (int i = 0; i < fileNames.length; i++) {
			String name = fileNames[i];

			String noSuffixName = "";
			int pos2 = name.indexOf(".");
			noSuffixName = name.substring(0, pos2);
			// System.out.println(name+":"+noSuffixName);
			String gbkName = noSuffixName + ".txt";
			String utfName = noSuffixName + ".utf8.txt";
			if (!list.contains(gbkName) && !list.contains(utfName)) {
				// System.out.println(name);
				list.add(name);
			}
		}
		int maxPage = 0;
		int curPage = 4;
		maxPage = list.size();
		if (list.contains("0.txt") || list.contains("0.utf8.txt")) {
			maxPage--;
		}
		// logger.debug("maxPage=" + maxPage);
		try {
			String pageName = "";
			String charset = "gbk";
			int index = list.indexOf(curPage + ".utf8.txt");
			if (index == -1) {
				index = list.indexOf(curPage + ".txt");
				if (index >= 0) {
					pageName = curPage + ".txt";
					charset = "gbk";
				}
			} else {
				pageName = curPage + ".utf8.txt";
				charset = "utf-8";
			}
			String newFilePageFolder = "d:/ss";

			String datastring = FileUtils.readTxt(newFilePageFolder + "/"
					+ pageName, charset);
			System.out.println("charset=" + charset + ":result=:" + datastring);
		} catch (IOException e) {
			// logger.error("", e);
		}

	}
}
