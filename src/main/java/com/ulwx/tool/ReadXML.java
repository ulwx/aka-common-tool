package com.ulwx.tool;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;



public class ReadXML {
	private static Logger log = LoggerFactory.getLogger(ReadXML.class);
	private Document doc;

	public Document getDocumnet() {
		return doc;
	}

	public ReadXML(String filename) throws Exception {

		this(filename, false);
	}

	public ReadXML(String filename, boolean validate) throws Exception {

		try {
			
			SAXReader reader = new SAXReader();  
			reader.setValidation(validate);
	        //读取文件 转换成Document  
	        Document doc = reader.read(new File(filename)); 
	        this.doc=doc;
	        

		} catch (Exception e) {
			throw e;
			// e.printStackTrace();
		}
	}

	public ReadXML(StringBuilder xml, boolean validate) throws Exception {
		this(new ByteArrayInputStream(xml.toString().getBytes("utf-8")),
				"utf-8", validate);

	}

	public ReadXML(InputStream inputStream, boolean validate) throws Exception {
		try {
			SAXReader reader = new SAXReader();  
			reader.setValidation(validate);
	        //读取文件 转换成Document  
	        Document doc = reader.read(inputStream); 
	        this.doc=doc;
		} catch (Exception e) {
			throw e;
		}
	}

	public ReadXML(InputStream inputStream, String charset, boolean validate)
			throws Exception {
		try {
			SAXReader reader = new SAXReader();  
			reader.setValidation(validate);
			reader.setEncoding(charset);
	
	        //读取文件 转换成Document  
	        Document doc = reader.read(inputStream); 
	        this.doc=doc;
		} catch (Exception e) {
			throw e;
		}
	}

	public ReadXML(InputStream in) throws Exception {
		this(in, false);
	}

	public void Close() {

		doc = null;
	}

	public String readValueByTagName(String nodename) throws Exception {
		String result = null;
		String node = nodename;
		try {
			Element root = doc.getRootElement(); 
			   @SuppressWarnings("unchecked")
			Iterator<Element> iterator=root.elementIterator(nodename);
			while(iterator.hasNext()){
				 Element el = iterator.next();
				 return el.getText();
			}
		} catch (Exception e) {
			// e.printStackTrace();
			throw e;
		}
		return result;
	}

	public String readValueByTagName(Element findElement, String nodename)
			throws Exception {

		try { 
			 @SuppressWarnings("unchecked")
			Iterator<Element> iterator=findElement.elementIterator(nodename);
			while(iterator.hasNext()){
				 Element el = iterator.next();
				 return el.getText();
			}
				
	
		} catch (Exception e) {
			// e.printStackTrace();
			throw e;
		}
		return null;

	}

	public List<Element> getNodeListByTagName(String nodename) throws Exception {
		try {
			
			Element root = doc.getRootElement(); 
			List<Element> list=root.elements(nodename);
			   
			return list;
		} catch (Exception e) {
			// e.printStackTrace();
			throw e;
			// return null;
		}
	}

	public  List<Element> getNodeListByTagName(Element findElement, String nodename)
			throws Exception {
		try {
			List<Element> list=findElement.elements(nodename);
			return list;
		} catch (Exception e) {
			// e.printStackTrace();
			throw e;
			// return null;
		}
	}

	


}
