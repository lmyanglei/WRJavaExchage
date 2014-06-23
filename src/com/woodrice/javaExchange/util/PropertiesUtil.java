package com.woodrice.javaExchange.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;

public class PropertiesUtil {

	public static Properties getProperties(String propertiesName){
		Properties prop = new Properties();
    	
    	String currpath = new File("").getAbsolutePath()+"\\"+propertiesName;
		try {
			InputStream in = new FileInputStream(new File(currpath));
			prop.load(in);
		} catch (FileNotFoundException e) {
			e.printStackTrace(System.err);
		} catch (IOException e) {
			e.printStackTrace(System.err);
		}
    	
    	return prop;
	}
	
	public static void main(String[] args) {
		// test
		Properties properties = PropertiesUtil.getProperties("test.properties");
		Set set = properties.keySet();
		Iterator iterator = set.iterator();
		while(iterator.hasNext()){
			String key = (String)iterator.next();
			String value = properties.getProperty(key);
			System.out.println(key+"="+value);
		}
	}
}
