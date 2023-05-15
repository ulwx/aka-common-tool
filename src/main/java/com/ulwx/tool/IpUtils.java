package com.ulwx.tool;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.HashSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ulwx.tool.ip.FileUtil;
import com.ulwx.tool.ip.IPLocation;
import com.ulwx.tool.ip.IPSeeker;
import com.ulwx.type.TString;

public class IpUtils {
	private static Logger log = LoggerFactory.getLogger(IpUtils.class);

	public static class SingletonHolder {
		public static IPSeeker seeker = null;
		static {
			try {

				seeker = new IPSeeker(FileUtil.getJarResourceToBytes("/qqwry.dat"));///
			} catch (Exception e) {
				log.error("", e);
			}
		}
	}

	static {
		try {
			SingletonHolder.class.newInstance();/////
		} catch (Exception e) {
			log.error("", e);
		}
	}

	public static HashSet<String> countrySet = new HashSet<String>();
	static {
		countrySet.add("阿尔巴尼亚");
		countrySet.add("阿尔及利亚");
		countrySet.add("安道尔共和国");
		countrySet.add("安哥拉");
		countrySet.add("安圭拉岛");
		countrySet.add("安提瓜和巴布达");
		countrySet.add("亚美尼亚");
		countrySet.add("阿森松");
		countrySet.add("澳大利亚");
		countrySet.add("奥地利");
		countrySet.add("阿塞拜疆");
		countrySet.add("巴哈马");
		countrySet.add("巴林");
		countrySet.add("孟加拉国");
		countrySet.add("巴巴多斯");
		countrySet.add("白俄罗斯");
		countrySet.add("比利时");
		countrySet.add("伯利兹");
		countrySet.add("贝宁");
		countrySet.add("百慕大群岛");
		countrySet.add("玻利维亚");
		countrySet.add("博茨瓦纳");
		countrySet.add("巴西");
		countrySet.add("文莱");
		countrySet.add("保加利亚");
		countrySet.add("布基纳法索");
		countrySet.add("缅甸");
		countrySet.add("布隆迪");
		countrySet.add("喀麦隆");
		countrySet.add("加拿大");
		countrySet.add("开曼群岛");
		countrySet.add("中非共和国");
		countrySet.add("乍得");
		countrySet.add("智利");
		countrySet.add("中国");
		countrySet.add("哥伦比亚");
		countrySet.add("刚果");
		countrySet.add("库克群岛");
		countrySet.add("哥斯达黎加");
		countrySet.add("古巴");
		countrySet.add("塞浦路斯");
		countrySet.add("捷克");
		countrySet.add("丹麦");
		countrySet.add("吉布提");
		countrySet.add("多米尼加共和国");
		countrySet.add("厄瓜多尔");
		countrySet.add("埃及");
		countrySet.add("萨尔瓦多");
		countrySet.add("爱沙尼亚");
		countrySet.add("埃塞俄比亚");
		countrySet.add("斐济");
		countrySet.add("芬兰");
		countrySet.add("法国");
		countrySet.add("法属圭亚那");
		countrySet.add("法属玻利尼西亚");
		countrySet.add("加蓬");
		countrySet.add("冈比亚");
		countrySet.add("格鲁吉亚");
		countrySet.add("德国");
		countrySet.add("加纳");
		countrySet.add("直布罗陀");
		countrySet.add("希腊");
		countrySet.add("格林纳达");
		countrySet.add("关岛");
		countrySet.add("危地马拉");
		countrySet.add("几内亚");
		countrySet.add("圭亚那");
		countrySet.add("海地");
		countrySet.add("洪都拉斯");
		countrySet.add("匈牙利");
		countrySet.add("冰岛");
		countrySet.add("印度");
		countrySet.add("印度尼西亚");
		countrySet.add("伊朗");
		countrySet.add("伊拉克");
		countrySet.add("爱尔兰");
		countrySet.add("以色列");
		countrySet.add("意大利");
		countrySet.add("科特迪瓦");
		countrySet.add("牙买加");
		countrySet.add("日本");
		countrySet.add("约旦");
		countrySet.add("柬埔寨");
		countrySet.add("哈萨克斯坦");
		countrySet.add("肯尼亚");
		countrySet.add("韩国");
		countrySet.add("科威特");
		countrySet.add("吉尔吉斯坦");
		countrySet.add("老挝");
		countrySet.add("拉脱维亚");
		countrySet.add("黎巴嫩");
		countrySet.add("莱索托");
		countrySet.add("利比里亚");
		countrySet.add("利比亚");
		countrySet.add("列支敦士登");
		countrySet.add("立陶宛");
		countrySet.add("卢森堡");
		countrySet.add("马达加斯加");
		countrySet.add("马拉维");
		countrySet.add("马来西亚");
		countrySet.add("马尔代夫");
		countrySet.add("马里");
		countrySet.add("马耳他");
		countrySet.add("马里亚那群岛");
		countrySet.add("马提尼克");
		countrySet.add("毛里求斯");
		countrySet.add("墨西哥");
		countrySet.add("摩尔多瓦");
		countrySet.add("摩纳哥");
		countrySet.add("蒙古");
		countrySet.add("蒙特塞拉特岛");
		countrySet.add("摩洛哥");
		countrySet.add("莫桑比克");
		countrySet.add("纳米比亚");
		countrySet.add("瑙鲁");
		countrySet.add("尼泊尔");
		countrySet.add("荷属安的列斯");
		countrySet.add("荷兰");
		countrySet.add("新西兰");
		countrySet.add("尼加拉瓜");
		countrySet.add("尼日尔");
		countrySet.add("尼日利亚");
		countrySet.add("朝鲜");
		countrySet.add("挪威");
		countrySet.add("阿曼");
		countrySet.add("巴基斯坦");
		countrySet.add("巴拿马");
		countrySet.add("巴布亚新几内亚");
		countrySet.add("巴拉圭");
		countrySet.add("秘鲁");
		countrySet.add("菲律宾");
		countrySet.add("波兰");
		countrySet.add("葡萄牙");
		countrySet.add("波多黎各");
		countrySet.add("卡塔尔");
		countrySet.add("留尼旺");
		countrySet.add("罗马尼亚");
		countrySet.add("俄罗斯");
		countrySet.add("圣卢西亚");
		countrySet.add("圣文森特岛");
		countrySet.add("东萨摩亚(美)");
		countrySet.add("西萨摩亚");
		countrySet.add("圣马力诺");
		countrySet.add("圣多美和普林西比");
		countrySet.add("沙特阿拉伯");
		countrySet.add("塞内加尔");
		countrySet.add("塞舌尔");
		countrySet.add("塞拉利昂");
		countrySet.add("新加坡");
		countrySet.add("斯洛伐克");
		countrySet.add("斯洛文尼亚");
		countrySet.add("所罗门群岛");
		countrySet.add("索马里");
		countrySet.add("南非");
		countrySet.add("西班牙");
		countrySet.add("斯里兰卡");
		countrySet.add("圣卢西亚");
		countrySet.add("圣文森特");
		countrySet.add("苏丹");
		countrySet.add("苏里南");
		countrySet.add("斯威士兰");
		countrySet.add("瑞典");
		countrySet.add("瑞士");
		countrySet.add("叙利亚");
		countrySet.add("塔吉克斯坦");
		countrySet.add("坦桑尼亚");
		countrySet.add("泰国");
		countrySet.add("多哥");
		countrySet.add("汤加");
		countrySet.add("特立尼达和多巴哥");
		countrySet.add("突尼斯");
		countrySet.add("土耳其");
		countrySet.add("土库曼斯坦");
		countrySet.add("乌干达");
		countrySet.add("乌克兰");
		countrySet.add("阿拉伯联合酋长国");
		countrySet.add("英国");
		countrySet.add("美国");
		countrySet.add("乌拉圭");
		countrySet.add("乌兹别克斯坦");
		countrySet.add("委内瑞拉");
		countrySet.add("越南");
		countrySet.add("也门");
		countrySet.add("南斯拉夫");
		countrySet.add("扎伊尔");
		countrySet.add("赞比亚");
		countrySet.add("津巴布韦");
	}

	public static void getInfoByIP(String ipStr, TString areaInfo, TString carrierAddrInfo) throws Exception {
		try {
			areaInfo.setValue("");
			carrierAddrInfo.setValue("");
			if (StringUtils.isEmpty(ipStr))
				return;
			if (Inet4Address.getByName(ipStr) instanceof Inet4Address) {
				if (SingletonHolder.seeker == null) {
					SingletonHolder.seeker = new IPSeeker(FileUtil.getJarResourceToBytes("/qqwry.dat"));
				}
				IPLocation location = SingletonHolder.seeker
						.getLocation((Inet4Address) (Inet4Address.getByName(ipStr)));
				String countryProvinceCity = location.getCountry();
				log.debug(countryProvinceCity);
				areaInfo.setValue(countryProvinceCity);
				carrierAddrInfo.setValue(location.getArea());
			} else {
				areaInfo.setValue("");
				carrierAddrInfo.setValue("");
				throw new Exception("不是ipv4格式！");
			}

		} catch (Exception e) {
			log.error("", e);
			throw e;
		} finally {

		}
	}

	public static void main(String[] args) {
		System.out.println(ObjectUtils.toString(getCountryProvinceCityCarrier("183.60.222.63")));
	}

	/**
	 * 
	 * @param ip
	 * @return 返回4个元素的字符串数组，第一个为国家，第二个为省份，第三个为城市，第四个为运营商
	 */
	public static String[] getCountryProvinceCityCarrier(String ip) {
		String[] retstrs = new String[] { "中国", "", "", "" };
		if (StringUtils.hasText(ip)) {
			TString areaInfo = new TString();
			TString carrierAddrInfo = new TString();

			try {
				IpUtils.getInfoByIP(ip, areaInfo, carrierAddrInfo);
			} catch (Exception e) {
				log.error("", e);
			}

			String area = StringUtils.trim(areaInfo.getValue());
			String cainfo = StringUtils.trim(carrierAddrInfo.getValue());
			// System.out.println("==="+area);
			// System.out.println("==="+cainfo);
			if (countrySet.contains(area)) {
				retstrs[0] = area;
			} else {
				for (String country : countrySet) {
					if (area.contains(country) && !country.equals("蒙古")) {
						retstrs[0] = area;
						break;
					}
				}
			}
			String[][] strs = StringUtils.searchSubStrByReg(area, "^((内蒙古|香港|台湾|广西|西藏|宁夏|新疆)|((.*?)省))?((.*?)市)?",
					new int[] { 1, 6 });
			if (strs != null && strs.length >= 1 && strs[0].length >= 1) {
				String province = StringUtils.trim(strs[0][0]).replace("省", "");
				String city = "";
				if (strs[0].length == 2) {
					city = StringUtils.trim(strs[0][1]);
					// 北京 上海 天津 重庆
					if (city.contains("北京") || city.contains("上海") || city.contains("天津") || city.contains("重庆")) {
						province = city;
					}
				}
				retstrs[1] = province;
				retstrs[2] = city;

			}

			if (cainfo.contains("联通")) {
				retstrs[3] = "联通";
			} else if (cainfo.contains("移动")) {
				retstrs[3] = "移动";
			} else if (cainfo.contains("电信")) {
				retstrs[3] = "电信";
			} else if (cainfo.contains("网通")) {
				retstrs[3] = "网通";
			} else if (cainfo.contains("长城宽带")) {
				retstrs[3] = "长城宽带";
			} else {
				retstrs[3] = cainfo;
			}
		}

		// System.out.println(ObjectUtils.toString(strs));
		return retstrs;
	}

	private volatile static String localIp = "";

	public static String getLocalIp() {
		try {
			if (StringUtils.isEmpty(localIp)) {
				synchronized (IpUtils.class) {
					if (StringUtils.isEmpty(localIp)) {
						Enumeration allNetInterfaces = NetworkInterface.getNetworkInterfaces();
						InetAddress ip = null;
						while (allNetInterfaces.hasMoreElements()) {
							NetworkInterface netInterface = (NetworkInterface) allNetInterfaces.nextElement();
							if (netInterface.isLoopback()) {
								continue;
							}

							Enumeration addresses = netInterface.getInetAddresses();

							while (addresses.hasMoreElements()) {
								ip = (InetAddress) addresses.nextElement();
								if (ip != null && ip instanceof Inet4Address) {
									localIp = ip.getHostAddress();
									return localIp;
								}
							}
						}
					}
				}
			}
			
			return localIp;
		} catch (Exception e) {
			log.error("", e);
		}

		return "";

	}

}
