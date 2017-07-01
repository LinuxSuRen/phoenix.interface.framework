/**
 * http://surenpi.com
 */
package org.suren.autotest.interfaces.framework;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.dom4j.DocumentException;

import net.sf.json.JSONObject;

/**
 * @author suren
 * @date Aug 13, 2016 12:57:20 PM
 */
public class Testa
{

	/**
	 * @param args
	 * @throws DocumentException 
	 */
	public static void main(String[] args) throws DocumentException
	{
		CloseableHttpClient client = HttpClients.createDefault();
		
		try
		{
			List<Request> requestList = Parser.parseFromClassPath("NewFile.xml");
			for(Request request : requestList)
			{
				int loop = request.getLoop();
				if(loop > 0)
				{
					for(int i = 0; i < loop; i++)
					{
						testRequest(request, client);
					}
					
					continue;
				}
				
				testRequest(request, client);
			}
		}
		finally
		{
			try
			{
				client.close();
			}
			catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * @param request
	 */
	private static void testRequest(Request request, CloseableHttpClient client)
	{
		HttpPost post = new HttpPost(request.getUrl());
		
		List<NameValuePair> pairList = new ArrayList<NameValuePair>();
		
		for(Param param : request.getParamList())
		{
			String name = param.getName();
			String value = param.getValue();
			
			pairList.add(new BasicNameValuePair(name, value));
		}
		
		try
		{
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(pairList, "utf-8");
			post.setEntity(entity);
		}
		catch (UnsupportedEncodingException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try
		{
			CloseableHttpResponse response = client.execute(post);
			int statusCode = response.getStatusLine().getStatusCode();
			if(statusCode == 200)
			{
				HttpEntity responseEntity = response.getEntity();
				
				String jsonRes = EntityUtils.toString(responseEntity).trim();
				
				JSONObject obj = JSONObject.fromObject(jsonRes);
				System.out.println(obj.toString(4));
			}
			else 
			{
				System.err.println("code : " + statusCode);
			}
		}
		catch (ClientProtocolException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
