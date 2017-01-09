/**
 * http://surenpi.com
 */
package org.suren.autotest.interfaces.framework;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.util.EntityUtils;
import org.suren.autotest.interfaces.framework.param.AtCookie;

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
		String jsonText = getResult(url, null, cookieList);

		JSONObject obj = JSONObject.fromObject(jsonText);
		
		return obj.getString(name);
	}
	
	public static String getResult(final String url, final Map<String, String> paramMap,
			final List<AtCookie> cookieList)
	{
		CloseableHttpClient client = HttpClients.createDefault();
		HttpClientContext httpClientContext = HttpClientContext.create();
		HttpPost post = new HttpPost(url);
		
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
			
			httpClientContext.setCookieStore(cookieStore);
		}
		
		try
		{
			CloseableHttpResponse response = client.execute(post, httpClientContext);
			int statusCode = response.getStatusLine().getStatusCode();
			if(statusCode == 200)
			{
				HttpEntity responseEntity = response.getEntity();
				
				String jsonRes = EntityUtils.toString(responseEntity).trim();
				
				return jsonRes;
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
