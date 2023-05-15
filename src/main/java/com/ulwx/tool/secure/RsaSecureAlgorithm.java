package com.ulwx.tool.secure;

public enum RsaSecureAlgorithm {

	SHA1WithRSA("SHA1WithRSA"),
	
	MD5WithRSA("MD5WithRSA");
	
	private String signAlgorithm;

	private RsaSecureAlgorithm(String signAlgorithm) {
		this.signAlgorithm = signAlgorithm;
	}

	public String getSignAlgorithm() {
		return signAlgorithm;
	}
}
