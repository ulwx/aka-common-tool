package com.ulwx.tool.secure;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

import org.apache.commons.codec.binary.Base64;

import com.ulwx.tool.StringUtils;

public class SignatureUtilsTest {

//商户验存管系统同理

	public static void createTestKeys(String privateK, String publicK) throws Exception {

		// 1.生成密钥对
		KeyPair keyPair = SignatureUtils.generateRsaKeyPair(1024);
		// 2.生成公私钥匙base64
		String privateStr = privateK;
		String publicStr = publicK;
		if (StringUtils.isEmpty(privateStr)) {
			privateStr = Base64.encodeBase64String(keyPair.getPrivate().getEncoded());// 商户留着签名
			publicStr = Base64.encodeBase64String(keyPair.getPublic().getEncoded());// 发给存管系统
		}
		System.out.println("privateStr=" + privateStr);
		System.out.println("publicStr=" + publicStr);
		// 3.公私钥匙生成
		PrivateKey privateKey = SignatureUtils.getRsaPkcs8PrivateKey(Base64.decodeBase64(privateStr));
		PublicKey publicKey = SignatureUtils.getRsaX509PublicKey(Base64.decodeBase64(publicStr));

		// 签名
		String content = "{\"platformUserNo\":\"123\",\"requestNo\":\"34234234\",\"realName\":\"陈少聪\",\"idCardType\":\"G2_IDCARD\",\"userRole\":\"20150312135520\",\"idCardNo\":\"220102199203120812\",\"mobile\":\"18611936074\",\"bankcardNo\":\"6228480402564890018\",\"callbackUrl\":\"http://requestb.in/1jaym0m1\",\"notifyUrl\":\"http://requestb.in/ojkzvdoj\",\"timestamp\":\"20151112134411\"}";
		byte[] sign = SignatureUtils.sign(SignatureAlgorithm.SHA1WithRSA, privateKey, content);
		String sign64 = Base64.encodeBase64String(sign);// 报文中的sign
		System.out.println(sign64);

		// 存管系统根据商户发来的公钥验签
		boolean b = SignatureUtils.verify(SignatureAlgorithm.SHA1WithRSA, publicKey, content, sign);
		System.out.println(b);
	}

	public static void main(String[] args) throws Exception {
		/*
		 * 公钥:
		 * MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAIT7MUOPbwiXJQuEosofGq2IKh9MfEQvJgtiea8kV8KmyGqf4b3UeDULZTk0Df3ep8iEmuywdb2QDZfPNkzHJ2KE53tNcIRVNoJwOB9wMoaFOFpBcRIqzrnYD9sJN5JwUu
		 * +8zcVRRzIg4gaKjQzrtxF2Suo7e2/PxHZNYM/n87aRAgMBAAECgYBqY8z2EDr01Hd+
		 * z46txlTBoL6Sa86/fjkhwpc32AueX3DYXTFtfh7t0UAak9rd0NTLR8vqsQKBtk4ptk3q/yoyLGy/
		 * YDhUjgodxCOf2EATBnJuSpLEXIwNPcrNWUEO/
		 * WUx0FZtEiHSPFNOPM9pKntdLFNNhoYX5yXSL6oWy5oqQQJBANnw82YhePWawzIco8T9JJE/
		 * syEjrREbwelZSRWNcR6A0WyqzcGUWk8qw5hShQSa70e8PFVNgOJpQVlcIDrhaAsCQQCcNBwwyEOCxNvX11fA7ST8xbH
		 * /4JQh0lM0+2rzNCDbwruKw8BJ3VGyINQX0OIVVb+2e/XgxX6Gj6h3W0o/
		 * itFTAkA2DICAepqh5mC0Q9AFoXX5I6AL/uoDzSt48HCkjLBaDT5iwbVHZcBD0+
		 * owE37zlmzzEGayDQeCnvHa9HL1x0LjAkBjb0yuxR0sxpJIIT4sgwmzm0nHYDgMG656IL4hWYK9QoZOPHRQ0QFrijhZfNAe0ro6uUh5MF9t209bw5JNRtofAkEAsNu5dqBS05lj9fM6K0KHLaPq0C35xkvZT6q
		 * +yvqEQkBGITJTVcXACtf5PAuzWhuu31dijmCdx8Pvxzl4fEVi6A==
		 * 
		 * 私钥: MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCE+
		 * zFDj28IlyULhKLKHxqtiCofTHxELyYLYnmvJFfCpshqn+
		 * G91Hg1C2U5NA393qfIhJrssHW9kA2XzzZMxydihOd7TXCEVTaCcDgfcDKGhThaQXESKs652A/
		 * bCTeScFLvvM3FUUcyIOIGio0M67cRdkrqO3tvz8R2TWDP5/O2kQIDAQAB
		 */
		String pub = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDDGo5cPAYRNmm89lBpz62B8dsTrJszonFcHNDEzm00jkKg12lixTczvosu2Q8IN5/x+IsY5TVIKdtmbNJuU0dCAHAspIWdRUfQiLFDliu+UTrdQaJFWm+YzNU0aD7rWLoZ/ZiVf60omBlCXubSS2uCQ6sZXlAeO2xcLoM8qLfykwIDAQAB";
		String pri = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAMMajlw8BhE2abz2UGnPrYHx2xOsmzOicVwc0MTObTSOQqDXaWLFNzO+iy7ZDwg3n/H4ixjlNUgp22Zs0m5TR0IAcCykhZ1FR9CIsUOWK75ROt1BokVab5jM1TRoPutYuhn9mJV/rSiYGUJe5tJLa4JDqxleUB47bFwugzyot/KTAgMBAAECgYBkf2NhQ1Hkv+CLg108g8teKhSs97YiTMSTrAwltCcwcS8E5rDUPb4Hm1jaUtiZZP2wiFQuCLL/bwAirF9gkOkcwV823XRVDywO3Wb7ghFn1aB0K4DlqUOOaMndv8gJXcWWcKjVzIlAZyo3Ntr7o0lPMOVkxFH2JvP4nO/cNVEwAQJBAPIXiVQ5XmN4TqoW1RT+7mvhtvIXdAFPwgas6sBCbhGhf22KW/k1eF8qNISTy+J2V8ptwN0MvlKweCn2evcc9pMCQQDOT/XzzUAVgXGRwR19qiLKJus4pcKimSFQ7mhAEK7MdyrAWiGFYfpDcI9x3oZ56C313E+uFzSV+6uXKdhrepQBAkA0uV2GQ0xEO5JeRsS3YuIICstuJB92nakzDPu/TXhtTI/VCnoHZ1bE2ws/CHUx5/YstwR23+yfU6GH+g9DvEITAkBrB6Jx84YOYhPX6JZzDeN0ehJHVf8OOWDGSpg8vCrimePM3DVNrBGYEPnpueaLsb6+MKgJibJXKLPQ/P1wbKwBAkA7+PZxO9IFvuqq+mB7kC3oIGvu7fJpBKaVx7+l768VTlPDk+mJqvYTzwfP5Nm05FBSWFGqe95oKsARgWldKbiV";
		createTestKeys(pri, pub);
	}

}
