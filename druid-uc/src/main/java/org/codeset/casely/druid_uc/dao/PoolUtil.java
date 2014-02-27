package org.codeset.casely.druid_uc.dao;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Properties;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.alibaba.druid.pool.DruidPooledConnection;

public class PoolUtil {
	private static PoolUtil poolUtil = null;
	private static DruidDataSource dds = null;
	private static String confile = "druid.properties";

	static {
		Properties properties = loadPropertyFile(confile);
		try {
			dds = (DruidDataSource) DruidDataSourceFactory
					.createDataSource(properties);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private PoolUtil() {}
	
	public static synchronized PoolUtil getInstance() {
		if (null == poolUtil) {
			poolUtil = new PoolUtil();
		}
		return poolUtil;
	}
	
	public DruidPooledConnection getConnection() throws SQLException {
		return dds.getConnection();
	}
	
	public static Properties loadPropertyFile(String fullFile) {
		String webRootPath = null;
		if (null == fullFile || fullFile.equals(""))
			throw new IllegalArgumentException(
					"Properties file path can not be null : " + fullFile);
		webRootPath = PoolUtil.class.getClassLoader().getResource("")
				.getPath();
		webRootPath = new File(webRootPath).getParent();
		InputStream inputStream = null;
		Properties p = null;
		System.out.println(webRootPath
		+ File.separator + "classes" + File.separator + fullFile);
		try {
			inputStream = new FileInputStream(new File(webRootPath
					+ File.separator + "classes" + File.separator + fullFile));
			p = new Properties();
			p.load(inputStream);
		} catch (FileNotFoundException e) {
			throw new IllegalArgumentException("Properties file not found: "
					+ fullFile);
		} catch (IOException e) {
			throw new IllegalArgumentException(
					"Properties file can not be loading: " + fullFile);
		} finally {
			try {
				if (inputStream != null)
					inputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return p;
	}
}
