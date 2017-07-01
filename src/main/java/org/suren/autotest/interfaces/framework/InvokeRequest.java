/**
 * http://surenpi.com
 */
package org.suren.autotest.interfaces.framework;

import java.io.IOException;
import java.util.List;

import org.apache.http.ParseException;
import org.dom4j.DocumentException;
import org.suren.autotest.interfaces.framework.data.DynamicParam;
import org.suren.autotest.interfaces.framework.data.DynamicValue;
import org.suren.autotest.interfaces.framework.data.SimpleDynamicParam;
import org.suren.autotest.interfaces.framework.data.SimpleDynamicValue;

/**
 * @author suren
 * @date Aug 13, 2016 12:57:20 PM
 */
public class InvokeRequest
{
	private SimpleHttpClient simpleHttpClient;
	
	public InvokeRequest(SimpleHttpClient simpleHttpClient)
	{
		this.simpleHttpClient = simpleHttpClient;
	}

	/**
	 * @param args
	 * @throws IOException 
	 * @throws ParseException 
	 * @throws DocumentException 
	 */
	public void invoke(List<Request> requestList) throws ParseException, IOException
	{
		for(Request request : requestList)
		{
			int loop = request.getLoop();
			if(loop > 0)
			{
				for(int i = 0; i < loop; i++)
				{
					testRequest(request);
				}
				
				continue;
			}
			
			try
			{
				testRequest(request);
				System.out.println(request.getUrl() + " - passed");
			}
			catch(Exception e)
			{
				System.err.println(request.getUrl() + " - error");
			}
		}
	}
	
	private DynamicParam dynamicParam = new SimpleDynamicParam();
	private DynamicValue dynamicValue = new SimpleDynamicValue();

	/**
	 * @param request
	 * @throws IOException 
	 * @throws ParseException 
	 */
	private void testRequest(Request request) throws ParseException, IOException
	{
		String type = request.getType().toLowerCase();
		String url = request.getUrl();
		
		if(request.getParamList() != null)
		{
			request.getParamList().forEach((p) -> {
				String value = dynamicValue.getValue(request.getType());
				
				dynamicParam.process(request, p.getName(), value, p.getPosition());
			});
		}
		
		if("get".equals(type))
		{
			simpleHttpClient.executeGet(url);
		}
		else if("post".equals(type))
		{
			simpleHttpClient.executePost(url, request.getBodyParam());
		}
		else if("delete".equals(type))
		{
			simpleHttpClient.executeDel(url);
		}
		else if("put".equals(type))
		{
			simpleHttpClient.executePut(url);
		}
		else
		{
			System.err.println("not support method : " + request.getType());
		}
	}

}
