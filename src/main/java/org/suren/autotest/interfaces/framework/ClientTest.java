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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.suren.autotest.interfaces.framework.param.AtCookie;

/**
 * @author suren
 * @date 2017年6月27日 上午10:31:44
 */
public class ClientTest
{

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		Map<String, String> paramMap = new HashMap<String, String>();
		List<AtCookie> cookieList = null;
		
		paramMap.put("username", "zhaoxj@glodon.com");
		paramMap.put("password", "aaa111");
		
		CloseableHttpClient client = HttpClients.createDefault();
		String result = HttpApiUtil.getResult(client, "http://localhost:8080/uaa/login", paramMap, cookieList, null);
		
		System.out.println(result);
		
		result = HttpApiUtil.getResult(client, "http://192.168.94.61:8080/cost/swagger-ui.html", null, cookieList, null);
		
		System.out.println(result);

		paramMap.put("username", "demo");
		paramMap.put("password", "demo");
		result = HttpApiUtil.getResult(client, "http://phoenix.surenpi.com/phoenix/user_info/login_process.su", paramMap, cookieList, null);
		
		System.out.println(result);
		
		result = HttpApiUtil.getResult(client, "http://phoenix.surenpi.com/phoenix/project/list.json", paramMap, cookieList, null);
		
		System.out.println(result);
		
		result = HttpApiUtil.getResult(client, "https://bugzilla.mozilla.org/rest/login?login=zxjlwt@126.com&password=123456", paramMap, cookieList, null);
		
		System.out.println(result);
	}

}
