package com.cprt.demo.utils.configuration;

import java.lang.reflect.Method;
import java.util.Properties;
import java.util.Random;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

public final class ApplicationProperties {
	// private static final String resourceName =
	// "/resources/sysconfig.properties";
	private static String resourceName = "/config/global_config.properties";
	private static Properties property = new Properties();
	private static final Logger logger = LoggerFactory.getLogger(ApplicationProperties.class);
	static {
		ApplicationProperties.init();
	}
	private PropertyPlaceholderConfigurer configPropertyConfigurer;

	/**
	 * spring init-method 初始化
	 */
	public void afterPropertiesSet() {
		logger.info("读取系统配置 begin ----------------");
		Method mergeProperties;
		try {
			mergeProperties = org.springframework.util.ReflectionUtils.findMethod(configPropertyConfigurer.getClass(),
					"mergeProperties");
			mergeProperties.setAccessible(true);
			Properties props = (Properties) mergeProperties.invoke(configPropertyConfigurer, new Object[0]);
			if (props != null) {
				logger.info("系统配置 ： " + props);
				ApplicationProperties.setProperty(props);
			}
		} catch (Exception e) {
			e.printStackTrace();
			ApplicationProperties.setProperty(PropertiesLoaderUtils.loadUrlProperties(resourceName));
		}
	}

	/**
	 * 初始化
	 */
	public static void init() {
		// ApplicationProperties.setProperty(PropertiesLoaderUtils.loadUrlProperties(resourceName));
	}

	/**
	 * @return Properties
	 */
	public static Properties getProperty() {
		// System.out.println(property.isEmpty());
		if (property == null || property.isEmpty() || property.size() == 0) {
			ApplicationProperties.init();
		}
		return property;
	}

	public static void setProperty(Properties property) {
		ApplicationProperties.property = property;
	}

	/**
	 * 获取属性值
	 * 
	 * @param key
	 * @return String
	 */
	public static String getPropertyValue(String key) throws Exception {
		if (StringUtils.isEmpty(key)) {
			return "";
		}
		return (String) ApplicationProperties.getProperty().get(key);
	}

	/**
	 * 取tcp 协议服务地址
	 *
	 * @return
	 */
	public static String[] tcpHost() {
		String ss = get("TCP_HOSTS");
		String[] servers = ss.split(",");
		Random random = new Random();
		int index = random.nextInt(servers.length);
		String host = servers[index];
		return host.split(":");
	}

	/**
	 * 取udp协议服务地址
	 *
	 * @return
	 */
	public static String[] udpHost() {
		String ss = get("UDP_HOSTS");
		String[] servers = ss.split(",");
		Random random = new Random();
		int index = random.nextInt(servers.length);
		String host = servers[index];
		return host.split(":");
	}

	/**
	 * 取udp协议服务地址
	 *
	 * @return
	 */
	public static String[] socketHost() {
		String ss = get("SOCKET_HOSTS");
		String[] servers = ss.split(",");
		return servers;
	}

	/**
	 * 判断当前的环境, 是否是开发环境，还是生产环境
	 * 
	 * @return
	 */
	public static boolean isDev() {

		String runMode = get("runMode");
		if (!"".equalsIgnoreCase(runMode) && "formal".equalsIgnoreCase(runMode)) {
			return false;
		}
		return true;

	}

	public static String get(String key) {
		String value = "";
		try {
			value = getPropertyValue(key);
		} catch (Exception e) {
		}
		return value;
	}

	/**
	 * 取分享地址
	 *
	 * @return
	 */
	public static String shareUrl() {
		return get("SHARE_URL");
	}

	/**
	 * 取分享地址，测试环境
	 *
	 * @return
	 */
	public static String shareUrlTest() {
		return get("SHARE_URL_TEST");
	}

	/**
	 * 取分享图标地址
	 *
	 * @return
	 */
	public static String shareIcon() {
		return get("SHARE_ICON");
	}

	/**
	 * 取分享图标地址
	 *
	 * @return
	 */
	public static String shareTitle() {
		return get("SHARE_TITLE");
	}

	/**
	 * 取分享图标地址
	 *
	 * @return
	 */
	public static String shareDesc() {
		return get("SHARE_DESC");
	}

	/**
	 * 注册频率验证开关 true/false
	 *
	 * @return
	 */
	public static String registerRateVerify() {
		return StringUtils.isEmpty(get("REGISTER_RATE_VERIFY")) ? "false" : get("REGISTER_RATE_VERIFY");
	}

	/**
	 * 单个手机号发送频率限制次数
	 *
	 * @return
	 */
	public static String registerPhoneNumVerify() {
		return StringUtils.isEmpty(get("REGISTER_RATE_VERIFY_PHONE")) ? "5" : get("REGISTER_RATE_VERIFY_PHONE");
	}

	/**
	 * IP限制发送次数
	 *
	 * @return
	 */
	public static String registerIPMSGVerify() {
		return StringUtils.isEmpty(get("REGISTER_RATE_VERIFY_IP_MSG")) ? "100" : get("REGISTER_RATE_VERIFY_IP_MSG");
	}

	/**
	 * 注册频率验证 ip
	 *
	 * @return
	 */
	public static String registerRateVerifyIp() {
		return StringUtils.isEmpty(get("REGISTER_RATE_VERIFY_IP")) ? "500" : get("REGISTER_RATE_VERIFY_IP");
	}

	/**
	 * 注册频率验证 pushId
	 *
	 * @return
	 */
	public static String registerRateVerifyPushId() {
		return StringUtils.isEmpty(get("REGISTER_RATE_VERIFY_PUSHID")) ? "5" : get("REGISTER_RATE_VERIFY_PUSHID");
	}

	/**
	 * 注册频率验证 imei
	 *
	 * @return
	 */
	public static String registerRateVerifyImei() {
		return StringUtils.isEmpty(get("REGISTER_RATE_VERIFY_IMEI")) ? "5" : get("REGISTER_RATE_VERIFY_IMEI");
	}

	public static void main(String[] args) {
	}

	public PropertyPlaceholderConfigurer getConfigPropertyConfigurer() {
		return configPropertyConfigurer;
	}

	public void setConfigPropertyConfigurer(PropertyPlaceholderConfigurer configPropertyConfigurer) {
		this.configPropertyConfigurer = configPropertyConfigurer;
	}
}