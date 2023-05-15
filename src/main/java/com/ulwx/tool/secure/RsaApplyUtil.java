package com.ulwx.tool.secure;

import java.security.PrivateKey;
import java.security.Security;
import java.security.interfaces.RSAPrivateKey;

import javax.crypto.Cipher;

import org.apache.commons.codec.binary.Base64;


public class RsaApplyUtil {

	/**
	 * 解密RSA   content 解密加密内容
	 * @param content
	 * @return
	 * @throws Exception
	 */
	public static String  decryptRsa(String content,String privateKeyStr) throws Exception {

		PrivateKey privateKey= RsaUtil.getRsaPkcs8PrivateKey(Base64.decodeBase64(privateKeyStr));
		return decryptBase64(content,privateKey);
	
	}
	public static String decryptBase64(String content,PrivateKey privateKey) {
	        return new String(decrypt(Base64.decodeBase64(content),privateKey));
	    }
	private static byte[] decrypt(byte[] string,PrivateKey privateKey) {
        try {
            Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
            Cipher cipher = Cipher.getInstance("RSA/None/PKCS1Padding", "BC");
            RSAPrivateKey pbk = (RSAPrivateKey)privateKey;
            cipher.init(Cipher.DECRYPT_MODE, pbk);
            byte[] plainText = cipher.doFinal(string);
            return plainText;
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
	public static void main(String[] args) throws Exception{
		//createTestKeys();
	}

}
