/*
 * Copyright 2002-2007 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.suren.autotest.interfaces.framework;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

/**
 * @author suren
 * @date 2017年6月27日 下午3:10:48
 */
public class SimpleHttpClient
{
	CloseableHttpClient client;
	HttpClientContext httpClientContext;

	public SimpleHttpClient()
	{
		client = HttpClients.createDefault();
		httpClientContext = HttpClientContext.create();
	}
	
	public String executeGet(String url) throws ParseException, IOException
	{
		HttpGet httpGet = new HttpGet(url);
		return fetchReponseText(httpGet);
	}
	
	public String executePost(String url, Map<String, String> requestBody) throws ParseException, IOException
	{
		HttpPost post = new HttpPost(url);
		
		List<NameValuePair> pairList = new ArrayList<NameValuePair>();
		requestBody.forEach((key, value) -> {
			pairList.add(new BasicNameValuePair(key, value));
		});
		
        try
		{
        	post.setEntity(new UrlEncodedFormEntity(pairList));
		}
		catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
		}
        
		return fetchReponseText(post);
	}

	/**
	 * @param post
	 * @return
	 * @throws IOException 
	 * @throws ParseException 
	 */
	private String fetchReponseText(HttpUriRequest request) throws ParseException, IOException
	{
		CloseableHttpResponse response = client.execute(request, httpClientContext);
		int statusCode = response.getStatusLine().getStatusCode();
		if(statusCode == 200)
		{
			HttpEntity responseEntity = response.getEntity();
			
			return EntityUtils.toString(responseEntity).trim();
		}
		else if(statusCode == 302)
		{
			Header[] Location = response.getHeaders("Location");
			if(Location.length > 0)
			{
				String redirectUrl = Location[0].getValue();
				
//				System.out.println(toLoc);
//				
//				String setCookie = response.getFirstHeader("Set-Cookie").getValue();
//				String JSESSIONID = setCookie.substring("JSESSIONID=".length(),
//				        setCookie.indexOf(";"));
//				    System.out.println("JSESSIONID:" + JSESSIONID);
//				    // 新建一个Cookie
//				    BasicClientCookie cookie = new BasicClientCookie("JSESSIONID",
//				        JSESSIONID);
//				    cookie.setVersion(0);
//				    cookie.setDomain("127.0.0.1");
//				    cookie.setPath("/uua");
//				    // cookie.setAttribute(ClientCookie.VERSION_ATTR, "0");
//				    // cookie.setAttribute(ClientCookie.DOMAIN_ATTR, "127.0.0.1");
//				    // cookie.setAttribute(ClientCookie.PORT_ATTR, "8080");
//				    // cookie.setAttribute(ClientCookie.PATH_ATTR, "/CwlProWeb");
//				    httpClientContext.getCookieStore().addCookie(cookie);
				if(redirectUrl.startsWith("/"))
				{
					redirectUrl = "http://" + request.getURI().getHost() + redirectUrl;
				}
				
				return executeGet(redirectUrl);
			}
		}
		
		return null;
	}
}
