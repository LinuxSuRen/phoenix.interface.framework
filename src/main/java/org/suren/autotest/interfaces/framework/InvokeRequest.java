/**
 * http://surenpi.com
 */
package org.suren.autotest.interfaces.framework;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.ParseException;
import org.apache.http.conn.ConnectionPoolTimeoutException;
import org.dom4j.DocumentException;
import org.suren.autotest.interfaces.framework.data.DynamicParam;
import org.suren.autotest.interfaces.framework.data.DynamicValue;
import org.suren.autotest.interfaces.framework.data.SimpleDynamicParam;
import org.suren.autotest.interfaces.framework.data.SimpleDynamicValue;
import org.suren.autotest.interfaces.framework.data.SwaggerJsonDynamicValue;

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
			if(!request.isEnable())
			{
				continue;
			}
			
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
			catch(HttpException e)
			{
				System.err.println(request.getUrl() + " - " + request.getType() + " - error code : " + e.getCode());
			}
			catch(ConnectionPoolTimeoutException e)
			{
				System.err.println(request.getUrl() + " - " + request.getType() + " - timeout");
			}
			catch(Exception e)
			{
				System.err.println(request.getUrl() + " - " + request.getType() + " - unknow error");
				e.printStackTrace();
			}
		}
	}
	
	private DynamicParam dynamicParam = new SimpleDynamicParam();
	private DynamicValue dynamicValue = new SimpleDynamicValue();
	private SwaggerJsonDynamicValue swaggerJsonDynamicValue;

	/**
	 * @param request
	 * @throws IOException 
	 * @throws ParseException 
	 */
	private void testRequest(Request request) throws ParseException, IOException
	{
		String type = request.getType().toLowerCase();
		
		if(request.getParamList() != null)
		{
			request.getParamList().forEach((p) -> {
				String value = null;
				if("schema".equals(p.getType()))
				{
					value = swaggerJsonDynamicValue.getJsonValue(p.getRef());
				}
				else
				{
					value = dynamicValue.getValue(p.getType());
				}
				
				dynamicParam.process(request, p.getName(), value, p.getPosition());
			});
		}

		String url = request.getUrl();
		if("get".equals(type))
		{
			simpleHttpClient.executeGet(url);
		}
		else if("post".equals(type))
		{
			if(request.isJsonApp())
			{
				Map<String, String> map = new HashMap<String, String>();
				map.put("Content-Type", "application/json;charset=UTF-8");
				
				simpleHttpClient.executePost(url, request.getBodyParam(), map);
			}
			else
			{
				simpleHttpClient.executePost(url, request.getBodyParam());
			}
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

	/**
	 * @return the swaggerJsonDynamicValue
	 */
	public SwaggerJsonDynamicValue getSwaggerJsonDynamicValue()
	{
		return swaggerJsonDynamicValue;
	}

	/**
	 * @param swaggerJsonDynamicValue the swaggerJsonDynamicValue to set
	 */
	public void setSwaggerJsonDynamicValue(SwaggerJsonDynamicValue swaggerJsonDynamicValue)
	{
		this.swaggerJsonDynamicValue = swaggerJsonDynamicValue;
	}

	/**
	 * @return the dynamicValue
	 */
	public DynamicValue getDynamicValue()
	{
		return dynamicValue;
	}

	/**
	 * @param dynamicValue the dynamicValue to set
	 */
	public void setDynamicValue(DynamicValue dynamicValue)
	{
		this.dynamicValue = dynamicValue;
	}

}
