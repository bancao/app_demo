package com.cprt.demo.utils.configuration;


import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author  
 *
 */
public final class PropertiesLoaderUtils {
	private static final Logger log = LoggerFactory.getLogger(PropertiesLoaderUtils.class);
	/**
	 * read properties
	 * 
	 * @param resourceName "/"
	 * @return Properties
	 */
	public static Properties loadUrlProperties(String resourceName) {

		Properties props = new Properties();
		InputStream is = null;
		log.info("PropertiesLoaderUtils 系统读取配置" + resourceName);
		System.out.println(resourceName);
		URL url = PropertiesLoaderUtils.class.getResource(resourceName);
		URLConnection con;
		try {
			con = url.openConnection();
			con.setUseCaches(false);
			is = con.getInputStream();
			props.load(is);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(is != null) {
				try {
					is.close();
					is = null;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return props;
	}

	/**
	 * read properties
	 * @param resourceName "/"
	 * @return Properties
	 */
	public static Properties loadStreamProperties(String resourceName) {

		Properties props = new Properties();
		InputStream is = null;

		try {
			is = PropertiesLoaderUtils.class.getResourceAsStream(resourceName);
			props.load(is);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(is != null) {
				try {
					is.close();
					is = null;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return props;
	}
}
