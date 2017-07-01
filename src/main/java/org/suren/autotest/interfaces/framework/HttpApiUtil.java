/**
 * http://surenpi.com
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
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.suren.autotest.interfaces.framework.param.AtCookie;

import net.sf.json.JSONObject;

/**
 * http的api接口工具类
 * @author suren
 * @date 2017年1月6日 下午3:18:36
 */
public class HttpApiUtil
{
	/**
	 * @see #getJsonValue(String, List, String)
	 * @param url
	 * @param name
	 * @return
	 */
	public static String getJsonValue(final String url, final String name)
	{
		return getJsonValue(url, null, name);
	}
	
	/**
	 * 从请求的json类型返回值中获取指定的值
	 * @param url
	 * @param cookieList
	 * @param name json对象的key
	 * @return
	 */
	public static String getJsonValue(final String url, final List<AtCookie> cookieList, final String name)
	{
		String jsonText = getResult(null, url, null, cookieList, null);

		JSONObject obj = JSONObject.fromObject(jsonText);
		
		return obj.getString(name);
	}
	
	static HttpClientContext httpClientContext = HttpClientContext.create();
	public static String getResult(CloseableHttpClient client, final String url, final Map<String, String> paramMap,
			final List<AtCookie> cookieList, final String referUrl)
	{
		if(client == null)
		{
			client = HttpClients.createDefault();
		}
		HttpUriRequest post = null;
		
		CookieStore cookieStore = new BasicCookieStore();
		if(cookieList != null)
		{
			for(AtCookie atCookie : cookieList)
			{
				String name = atCookie.getName();
				String value = atCookie.getValue();
				
				BasicClientCookie cookie = new BasicClientCookie(name, value);
				cookie.setDomain(atCookie.getDomain());
				cookie.setPath(atCookie.getPath());
				
				cookieStore.addCookie(cookie);
			}
			
		}
		
		if(httpClientContext.getCookieStore() == null)
		{
			httpClientContext.setCookieStore(cookieStore);
		}
		
		if(paramMap != null)
		{
			post = new HttpPost(url);
			
			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			paramMap.forEach((key, value) -> {
				nvps.add(new BasicNameValuePair(key, value));
			});
	        try
			{
				((HttpPost) post).setEntity(new UrlEncodedFormEntity(nvps));
			}
			catch (UnsupportedEncodingException e1)
			{
				e1.printStackTrace();
			}
		}
		else
		{
			post = new HttpGet(url);
		}
		
		try
		{
//			if(referUrl != null)
//			{
//				post.setHeader("Referer", referUrl);
//			}
			CloseableHttpResponse response = client.execute(post, httpClientContext);
			int statusCode = response.getStatusLine().getStatusCode();
			if(statusCode == 200)
			{
				HttpEntity responseEntity = response.getEntity();
				
				String jsonRes = EntityUtils.toString(responseEntity).trim();
				
				return jsonRes;
			}
			else if(statusCode == 302)
			{
				Header[] Location = response.getHeaders("Location");
				if(Location.length > 0)
				{
					String toLoc = Location[0].getValue();
					
					System.out.println(toLoc);
					
					String setCookie = response.getFirstHeader("Set-Cookie").getValue();
					String JSESSIONID = setCookie.substring("JSESSIONID=".length(),
					        setCookie.indexOf(";"));
					    System.out.println("JSESSIONID:" + JSESSIONID);
					    // 新建一个Cookie
					    BasicClientCookie cookie = new BasicClientCookie("JSESSIONID",
					        JSESSIONID);
					    cookie.setVersion(0);
					    cookie.setDomain("127.0.0.1");
					    cookie.setPath("/uua");
					    // cookie.setAttribute(ClientCookie.VERSION_ATTR, "0");
					    // cookie.setAttribute(ClientCookie.DOMAIN_ATTR, "127.0.0.1");
					    // cookie.setAttribute(ClientCookie.PORT_ATTR, "8080");
					    // cookie.setAttribute(ClientCookie.PATH_ATTR, "/CwlProWeb");
					    httpClientContext.getCookieStore().addCookie(cookie);
					
					return getResult(client, toLoc, null, null, url);
				}
			}
		}
		catch (ClientProtocolException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		return null;
	}
}
