/**
 * http://surenpi.com
 */
package org.suren.autotest.interfaces.framework;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 * @author suren
 * @date Aug 13, 2016 12:59:51 PM
 */
public class InterfacesParser
{
	public static List<Request> parseFromText(CharSequence buffer) throws DocumentException
	{
		ByteArrayInputStream arrayInput = new ByteArrayInputStream(buffer.toString().getBytes());
		
		return parseFromStream(arrayInput);
	}
	
	public static List<Request> parseFromStream(InputStream input) throws DocumentException
	{
		List<Request> requestList = new ArrayList<Request>();
		Document doc = new SAXReader().read(input);
		
		Element root = doc.getRootElement();
		
		List<Element> interEleList = root.elements("interface");
		
		parse(interEleList, requestList);
		
		List<Element> interGroupEleList = root.elements("interfaceGroup");
		interGroupParse(interGroupEleList, requestList);
		
		return requestList;
	}
	
	public static List<Request> parseFromClassPath(String interFile) throws DocumentException
	{
		InputStream stream = InterfacesParser.class.getClassLoader().getResourceAsStream(interFile);
		
		return parseFromStream(stream);
	}

	/**
	 * @param interEleList
	 * @param requestList
	 */
	private static void parse(List<Element> interEleList,
			List<Request> requestList)
	{
		for(Element interEle : interEleList)
		{
			parseInterface(interEle, requestList);
		}
	}
	
	private static void parseInterface(Element interEle, List<Request> requestList)
	{
		Request request = new Request();
		requestList.add(request);
		
		String url = interEle.attributeValue("url");
		String loop = interEle.attributeValue("loop");
		String type = interEle.attributeValue("method");
		
		request.setUrl(url);
		request.setType(type);
		
		try
		{
			request.setLoop(Integer.parseInt(loop));
		} catch (NumberFormatException e){}
		
		Element paramEle = interEle.element("params");
		
		List<Element> paramEleList = paramEle.elements("param");
		
		parseParam(request, paramEleList);
	}

	/**
	 * @param request
	 * @param paramEleList
	 */
	private static void parseParam(Request request, List<Element> paramEleList)
	{
		List<Param> paramList = new ArrayList<Param>();
		request.setParamList(paramList);
		
		for(Element paramEle : paramEleList)
		{
			Param param = new Param();
			paramList.add(param);
			
			String name = paramEle.attributeValue("name");
			String value = paramEle.attributeValue("value");
			String type = paramEle.attributeValue("type");
			String in = paramEle.attributeValue("in");
			
			if("plaintext_md5".equals(type))
			{
				value = DigestUtils.md5Hex(value);
			}
			
			param.setName(name);
			param.setValue(value);
			param.setPosition(in);
			param.setType(type);
		}
	}

	/**
	 * @param interGroupEleList
	 * @param requestList
	 */
	private static void interGroupParse(List<Element> interGroupEleList,
			List<Request> requestList)
	{
		for(Element interGroupEle : interGroupEleList)
		{
			String url = interGroupEle.attributeValue("url");
			
			List<Element> interEleList = interGroupEle.elements("interface");
			for(Element interEle : interEleList)
			{
				String uri = interEle.attributeValue("url", "");
				if(!"".equals(uri) && !uri.startsWith("/"))
				{
					uri = url + "/" + uri;
				}
				else
				{
					uri = url;
				}
				
				interEle.addAttribute("url", uri);
				parseInterface(interEle, requestList);
			}
		}
	}
}
