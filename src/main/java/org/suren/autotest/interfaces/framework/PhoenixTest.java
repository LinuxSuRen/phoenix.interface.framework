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
import java.util.HashMap;
import java.util.Map;

import org.apache.http.ParseException;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * @author suren
 * @date 2017年6月27日 下午3:20:28
 */
public class PhoenixTest
{

	/**
	 * @param args
	 * @throws IOException 
	 * @throws ParseException 
	 */
	public static void main(String[] args) throws ParseException, IOException
	{
		SimpleHttpClient client = new SimpleHttpClient();
		
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("username", "demo");
		paramMap.put("password", "demo");
		String result = client.executePost("http://127.0.0.1/phoenix/user_info/login_process.su", paramMap);
		
		result = client.executeGet("http://127.0.0.1/phoenix/api/projects");
		System.out.println(result);
		
		System.out.println(JSONArray.fromObject(result));
	}

}
