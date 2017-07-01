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
import java.util.HashMap;
import java.util.Map;

import org.junit.Rule;
import org.junit.Test;
import org.suren.autotest.interfaces.framework.junit.ReportRule;
import org.suren.autotest.interfaces.framework.junit.Skip;

import junit.framework.Assert;

/**
 * @author suren
 * @date 2017年6月30日 下午5:13:14
 */
public class SwaggerParseTest
{
	@Rule
	public ReportRule reportRule = new ReportRule();
	
	@Test
	@Skip
	public void justForTest()
	{
		System.out.println("just for test");
	}
	
	@Test
	@Skip
	public void buffer() throws UnsupportedEncodingException, IOException
	{
		String swaggerText;
		StringBuffer buffer = new StringBuffer();
		
		SimpleHttpClient client = new SimpleHttpClient();
		
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("username", "demo");
		paramMap.put("password", "demo");
		client.executePost("http://127.0.0.1/phoenix/user_info/login_process.su", paramMap);
		
		swaggerText = client.executeGet("http://127.0.0.1/phoenix/swagger-ui.html");
		
		new SwaggerParse().transfer(swaggerText, buffer);
		
		Assert.assertTrue(buffer.length() > 0);
		System.out.println(buffer);
	}
}
