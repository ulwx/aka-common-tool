package com.ulwx.tool;

import java.util.Locale;

import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber;
import com.google.i18n.phonenumbers.geocoding.PhoneNumberOfflineGeocoder;

public class PhoneNumberUtils {
	private  static volatile PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
	private  static volatile PhoneNumberOfflineGeocoder phoneNumberOfflineGeocoder = PhoneNumberOfflineGeocoder.getInstance();

	private static String language = "CN";
	

	public PhoneNumberUtils() {
		// TODO Auto-generated constructor stub
	}

	public static String getArea(String phone) {
	
		PhoneNumber referencePhonenumber = null;
		try {
			referencePhonenumber = phoneUtil.parse(phone, language);
			String referenceRegion  = phoneNumberOfflineGeocoder.getDescriptionForNumber(referencePhonenumber, Locale.CHINA);
			return referenceRegion;	
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
	
	public static void main(String[] args) {
		//System.out.println("18565574709");
		System.out.println(getArea("18680214627"));
	}

}
