package com.ulwx.tool;

import java.io.StringWriter;
import java.util.Map;

import org.yaml.snakeyaml.Yaml;

public class YamlUtils {

	@SuppressWarnings("unchecked")
	public static Map<String, Object> loadFromResource(String yamlFileName) {
		Yaml yaml = new Yaml();
		Map<String, Object> ret = (Map<String, Object>) yaml
				.load(YamlUtils.class.getResourceAsStream(yamlFileName));
		return ret;

	}

	public static <T> T loadFromResource(String yamlFileName, Class<T> clazz) {
		Yaml yaml = new Yaml();
		T ret = yaml.loadAs(YamlUtils.class.getResourceAsStream(yamlFileName), clazz);
		return ret;
	}

	public static String toString(Object obj) {

		Yaml yaml = new Yaml();
		StringWriter sw = new StringWriter();
	
		yaml.dump(obj, sw);
		return sw.toString();

	}
	public static void main(String[] args) {
		Map<String, String> pu = PropertyUtil.loadAsMap(Path.getResource("/application.property"));
		System.out.println(toString(pu));
		

	}

}
