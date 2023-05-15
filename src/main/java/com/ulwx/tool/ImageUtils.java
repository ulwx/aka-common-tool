package com.ulwx.tool;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;

import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageOutputStream;
import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.awt.image.CropImageFilter;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

//import com.sun.image.codec.jpeg.JPEGCodec;
//import com.sun.image.codec.jpeg.JPEGEncodeParam;
//import com.sun.image.codec.jpeg.JPEGImageEncoder;

public class ImageUtils {

	private static final Logger logger = org.slf4j.LoggerFactory.getLogger(ImageUtils.class);

	/**
	 * 几种常见的图片格式
	 */
	public static String IMAGE_TYPE_GIF = "gif";// 图形交换格式
	public static String IMAGE_TYPE_JPG = "jpg";// 联合照片专家组
	public static String IMAGE_TYPE_JPEG = "jpeg";// 联合照片专家组
	public static String IMAGE_TYPE_BMP = "bmp";// 英文Bitmap（位图）的简写，它是Windows操作系统中的标准图像文件格式
	public static String IMAGE_TYPE_PNG = "png";// 可移植网络图形
	public static String IMAGE_TYPE_PSD = "psd";// Photoshop的专用格式Photoshop

	/**
	 * 图像切割
	 *
	 * @param srcImageFile
	 *            源图像地址
	 * @param destImageFile
	 *            新图像地址
	 * @param x
	 *            目标切片起点x坐标
	 * @param y
	 *            目标切片起点y坐标
	 * @param destWidth
	 *            目标切片宽度
	 * @param destHeight
	 *            目标切片高度
	 *
	 * @return 图片是否处理成功
	 */
	public static boolean cut(File srcImageFile, File destImageFile, int x, int y, int destWidth, int destHeight, String formatName) {
		try {
			if(StringUtils.isEmpty(formatName)){
				formatName="JPEG";
			}
			Image img;
			ImageFilter cropFilter;
			// 读取源图像
			BufferedImage bi = ImageIO.read(srcImageFile);
			int srcWidth = bi.getWidth(); // 源图宽度
			int srcHeight = bi.getHeight(); // 源图高度
			if (destWidth == 0) {
				destWidth = srcWidth;
			}
			if (destHeight == 0) {
				destHeight = srcHeight;
			}
			if (srcWidth >= destWidth && srcHeight >= destHeight) {
				Image image = bi.getScaledInstance(srcWidth, srcHeight, Image.SCALE_SMOOTH);
				// 改进的想法:是否可用多线程加快切割速度
				// 四个参数分别为图像起点坐标和宽高
				// 即: CropImageFilter(int x,int y,int width,int height)
				cropFilter = new CropImageFilter(x, y, destWidth, destHeight);
				img = Toolkit.getDefaultToolkit().createImage(new FilteredImageSource(image.getSource(), cropFilter));
				BufferedImage tag = new BufferedImage(destWidth, destHeight, BufferedImage.TYPE_INT_RGB);
				Graphics g = tag.getGraphics();
				g.drawImage(img, 0, 0, null); // 绘制缩小后的图
				g.dispose();
				// 输出为文件
				ImageIO.write(tag, formatName, destImageFile);
			} else {
				logger.error("", "目标切片尺寸大于了源图片切片尺寸!");
				return false;
			}
		} catch (Exception e) {
			logger.error("", e);
			return false;
		}
		return true;
	}

	/**
	 * 缩放后并且剪切
	 *
	 * @param srcImageFile
	 * @param destImageFile
	 * @param x
	 * @param y
	 * @param destWidth
	 * @param destHeight
	 *
	 * @return 图片是否处理成功
	 */
	public static boolean scaleAndCut(File srcImageFile, File destImageFile, int x, int y, int destWidth,
									  int destHeight, int scaleWidth, int scalHeight) {
		try {
			Image img;
			ImageFilter cropFilter;
			// 读取源图像
			BufferedImage bi = ImageIO.read(srcImageFile);

			if (destWidth == 0) {
				destWidth = scaleWidth;
			}
			if (destHeight == 0) {
				destHeight = scalHeight;
			}

			Image image = bi.getScaledInstance(scaleWidth, scalHeight, Image.SCALE_SMOOTH);
			// 改进的想法:是否可用多线程加快切割速度
			// 四个参数分别为图像起点坐标和宽高
			// 即: CropImageFilter(int x,int y,int width,int height)
			cropFilter = new CropImageFilter(x, y, destWidth, destHeight);
			img = Toolkit.getDefaultToolkit().createImage(new FilteredImageSource(image.getSource(), cropFilter));
			BufferedImage tag = new BufferedImage(destWidth, destHeight, BufferedImage.TYPE_INT_RGB);
			Graphics g = tag.getGraphics();
			g.drawImage(img, 0, 0, null); // 绘制缩小后的图
			g.dispose();

			ImageIO.write(tag, "JPEG", destImageFile);
		} catch (Exception e) {
			logger.error("", e);
			return false;
		}
		return true;
	}
	/**
	 * 图片缩放，并且保证不失真，缩放到w和h指定的矩形要能容纳下整个图片
	 */
	public static boolean scaleInRect(File srcImageFile, String result, int w, int h, String imageType) {
		try {
			// 读取源图像
			if(StringUtils.isEmpty(imageType)){
				imageType="JPEG";
			}
			BufferedImage bi = ImageIO.read(srcImageFile);
			double sw = bi.getWidth(); // 源图宽度
			double sh = bi.getHeight(); // 源图高度

			if (w == 0) {
				w = bi.getWidth();
			}
			if (h == 0) {
				h = bi.getHeight();
			}
			double needRadio = w / (float) h;
			double radio = sw / sh;
			double scaleWidth = w;
			double scaleHeight = h;
			//double x1 = 0, y1 = 0;

			if (needRadio < radio) {// 说明原始图片的宽度长了
				scaleWidth = w;
				scaleHeight = scaleWidth / radio;
				//y1 = (scaleHeight - h) / 2;

			} else {// 说明原始图片的高度长了，要按照宽度定
				scaleHeight = h;
				scaleWidth = scaleHeight * radio;
				//x1 = (scaleWidth - w) / 2;

			}
			// float scaleWidth=
			ImageUtils.scale(srcImageFile.getCanonicalPath(), result, (int)scaleWidth, (int)scaleHeight,imageType);
		} catch (Exception e) {
			logger.error("", e);
			return false;
		}
		return true;
	}
	/**
	 * 缩放图像
	 *
	 * @param srcImageFile
	 *            源图像文件地址
	 * @param result
	 *            缩放后的图像地址
	 * @param scale
	 *            缩放比例
	 * @param flag
	 *            缩放选择:true 放大; false 缩小;
	 */
	public static boolean scale(String srcImageFile, String result, int scale, boolean flag, String imageType) {
		try {
			if(StringUtils.isEmpty(imageType)){
				imageType="JPEG";
			}
			BufferedImage src = ImageIO.read(new File(srcImageFile)); // 读入文件
			int width = src.getWidth(); // 得到源图宽
			int height = src.getHeight(); // 得到源图长
			if (flag) {
				// 放大
				width = width * scale;
				height = height * scale;
			} else {
				// 缩小
				width = width / scale;
				height = height / scale;
			}
			Image image = src.getScaledInstance(width, height, Image.SCALE_SMOOTH);
			BufferedImage tag = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			Graphics g = tag.getGraphics();
			g.drawImage(image, 0, 0, null); // 绘制缩小后的图
			g.dispose();
			ImageIO.write(tag, imageType, new File(result));// 输出到文件流
		} catch (IOException e) {
			logger.error("", e);
			return false;
		}
		return true;
	}

	/**
	 * 重新生成按指定宽度和高度的图像
	 *
	 * @param srcImageFile
	 *            源图像文件地址
	 * @param result
	 *            新的图像地址
	 * @param _width
	 *            设置新的图像宽度
	 * @param _height
	 *            设置新的图像高度
	 * @param  imageType  图片格式，JPEG ,PNG等
	 */
	public static boolean scale(String srcImageFile, String result, int _width, int _height, String imageType) {
		return scale(srcImageFile, result, _width, _height, 0, 0,imageType);
	}

	public static boolean scale(String srcImageFile, String result, int _width, int _height, int x, int y, String imageType) {
		try {
			if(StringUtils.isEmpty(imageType)){
				imageType="JPEG";
			}
			BufferedImage src = ImageIO.read(new File(srcImageFile)); // 读入文件

			int width = src.getWidth(); // 得到源图宽
			int height = src.getHeight(); // 得到源图长

			if (width > _width) {
				width = _width;
			}
			if (height > _height) {
				height = _height;
			}

			Image image = src.getScaledInstance(width, height, Image.SCALE_SMOOTH);
			//Image image = src.getScaledInstance(width, height, Image.SCALE_FAST);
			BufferedImage tag = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			Graphics g = tag.getGraphics();
			g.drawImage(image, x, y, null); // 绘制缩小后的图
			g.dispose();
			ImageIO.write(tag, imageType, new File(result));// 输出到文件流
		} catch (IOException e) {
			logger.error("", e);
			return false;
		}
		return true;
	}

	/**
	 * 图片缩放，并且保证不失真，合适的时候需要剪切如果原始图片和需要剪切的图片比例不一样
	 */
	public static boolean scaleWithHigh(File srcImageFile, File dirImageFile, int w, int h) {
		try {
			// 读取源图像
			BufferedImage bi = ImageIO.read(srcImageFile);
			double sw = bi.getWidth(); // 源图宽度
			double sh = bi.getHeight(); // 源图高度

			if (w == 0) {
				w = bi.getWidth();
			}
			if (h == 0) {
				h = bi.getHeight();
			}
			double needRadio = w / (float) h;
			double radio = sw / sh;
			double scaleWidth = w;
			double scaleHeight = h;
			double x1 = 0, y1 = 0;
			double cutW = w;
			double cutH = h;

			if (needRadio < radio) {// 说明高度长了，要按照高度来定宽度
				if (h > sh) {
					cutH = sh;
				}
				scaleHeight = h;
				scaleWidth = scaleHeight * radio;
				x1 = (scaleWidth - w) / 2;
			} else {// 说明宽度长了，要按照宽度定高度
				if (w > sw) {
					cutW = sw;
				}
				scaleWidth = w;
				scaleHeight = scaleWidth / radio;
				y1 = (scaleHeight - h) / 2;
			}
			// float scaleWidth=
			ImageUtils.scaleAndCut(srcImageFile, dirImageFile, (int) x1, (int) y1, (int) cutW, (int) cutH,
					(int) scaleWidth, (int) scaleHeight);
		} catch (Exception e) {
			logger.error("", e);
			return false;
		}
		return true;
	}

	/**
	 * 图像类型转换 GIF->JPG GIF->PNG PNG->JPG PNG->GIF(X)
	 */
	public static boolean convert(String source, String result) {
		try {
			File f = new File(source);
			f.canRead();
			f.canWrite();
			BufferedImage src = ImageIO.read(f);
			ImageIO.write(src, "JPG", new File(result));
		} catch (Exception e) {
			logger.error("", e);
			return false;
		}
		return true;
	}

	public static int[] getPicWidthAndHeight(String path) {

		File file = new File(path);
		FileInputStream fis=null;
		try {
			fis = new FileInputStream(file);
			BufferedImage bufferedImg = ImageIO.read(fis);
			int imgWidth = bufferedImg.getWidth();
			int imgHeight = bufferedImg.getHeight();

			return new int[] { imgWidth, imgHeight };
		} catch (Exception e) {
			return null;
		}finally{
			if(fis!=null){
				try {
					fis.close();
				} catch (IOException e) {
				}
			}
		}
	}



	public static int[] getPicWidthAndHeight(File file) {

		FileInputStream fis=null;
		try {
			fis = new FileInputStream(file);
			BufferedImage bufferedImg = ImageIO.read(fis);
			int imgWidth = bufferedImg.getWidth();
			int imgHeight = bufferedImg.getHeight();

			return new int[] { imgWidth, imgHeight };
		} catch (Exception e) {
			return null;
		}finally{
			if(fis!=null){
				try {
					fis.close();
				} catch (IOException e) {
				}
			}
		}
	}
	/**
	 * 彩色转为黑白
	 *
	 * @param source
	 * @param result
	 */
	public static boolean gray(String source, String result, String imageType) {
		try {
			if(StringUtils.isEmpty(imageType)){
				imageType="JPEG";
			}
			BufferedImage src = ImageIO.read(new File(source));
			ColorSpace cs = ColorSpace.getInstance(ColorSpace.CS_GRAY);
			ColorConvertOp op = new ColorConvertOp(cs, null);
			src = op.filter(src, null);
			ImageIO.write(src, imageType, new File(result));
		} catch (IOException e) {
			logger.error("", e);
			return false;
		}
		return true;
	}

	/**
	 * 对图片进行缩放
	 *
	 * @param imagebyte
	 *            原图的字节数组
	 * @param width
	 *            需要缩放的宽度
	 * @param height
	 *            需要缩放的高度
	 * @param algor
	 *            缩放使用的算法，见Image的jdk文档，如，Image.SCALE_SMOOTH缩放的质量比较好，但效率比较低
	 * @return 返回缩放后的图片字节数组
	 */
	public static byte[] scaleImage(byte[] imagebyte, int width, int height, int algor, String imageType) {
		if (imagebyte == null) {
			return null;
		}

		if(StringUtils.isEmpty(imageType)){
			imageType="JPEG";
		}

		FileImageOutputStream fios = null;// 保存原图的图片
		Image image = null;// 读取原图
		ByteArrayOutputStream out = null;
		BufferedImage newImage = null;
		// JPEGImageEncoder encoder=null;
		try {
			image = ImageIO.read(new ByteArrayInputStream(imagebyte));// 读取原图
			// 根据传过去得参数 ---变成 3个字符串数组
			newImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			// 绘制缩小后的图
			newImage.getGraphics().drawImage(image.getScaledInstance(width, height, algor), 0, 0, null);
			out = new ByteArrayOutputStream();
			// encoder=JPEGCodec.createJPEGEncoder(out);
			// encoder.encode(newImage);
			ImageIO.write(newImage, imageType, out);

		} catch (Exception e) {
			logger.error("", e);
		} finally {
			try {
				if (fios != null)
					fios.close();

				if (image != null)
					image = null;

				if (out != null) {
					out.flush();
					out.close();
				}
				// if(encoder!=null) encoder=null;

			} catch (Exception e) {
				logger.error("", e);
			}
		}
		return out.toByteArray();

	}

//	public static void main(String[] args) {
//		File src = new File("D:\\aaa\\2.png");
//
//		File dest=new File("D:\\aaa\\2_02.png");
//
//		File scaleImage=new File("D:\\aaa\\2_02_scale.png");
//		// ImageUtils.scaleWithHigh(src, dest, 600, 800);
//		// ImageUtils.cut(src, dest, 0, 0, 794, 1075,"PNG");
//		//ImageUtils.cut(src, dest, 0, 0, 794, 1075,"JPG");
//		//ImageUtils.scaleInRect(src, "d://66.jpg", 500,500);
//		ImageUtils.scale("D:\\aaa\\2_02.png","D:\\aaa\\2_02_scale.png",2,false,"PNG");
//
//	}

}
