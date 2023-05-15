package com.ulwx.tool;



import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;


public class Ec3des {
	

	public static final String encode = "utf-8"; 
	
    /**
     * 加密
     * @param data
     * @return
     * @throws Exception
     */
	public static final String tDesEncrypt(String data,String key) throws Exception {
		// 生成密钥
		SecretKey deskey = new SecretKeySpec(key.getBytes(encode), "DESede");
		// 密码器
		Cipher cipher = Cipher.getInstance("DESede");
		// 初始化密码器执行模式和密钥
		cipher.init(Cipher.ENCRYPT_MODE, deskey);
		// 执行算法
		byte[] encryptedByteArray = cipher.doFinal(data.getBytes(encode));
		//return Base64.encode(encryptedByteArray);
		return EncryptUtil.encryptBASE64(encryptedByteArray);
	}

	/**
	 * 解密
	 * @param data
	 * @return
	 */
	public static final String tDesDecrypt(String data,String key) throws NoSuchAlgorithmException,
			NoSuchPaddingException, InvalidKeyException,
			IllegalBlockSizeException, BadPaddingException, IOException {
		// 生成密钥
		SecretKey deskey = new SecretKeySpec(key.getBytes(encode), "DESede");
		// 密码器
		Cipher cipher = Cipher.getInstance("DESede");
		// 初始化密码器执行模式和密钥
		cipher.init(Cipher.DECRYPT_MODE, deskey);
		byte[] encryptedByteArray =EncryptUtil.decryptBASE64(data) ;//Base64.decode(data);
		return new String(cipher.doFinal(encryptedByteArray), encode);
	}
}
