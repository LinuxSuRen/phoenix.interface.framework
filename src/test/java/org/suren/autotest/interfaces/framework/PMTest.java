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
import java.util.List;
import java.util.Map;

import org.dom4j.DocumentException;
import org.junit.Rule;
import org.junit.Test;
import org.suren.autotest.interfaces.framework.data.SwaggerJsonDynamicValue;
import org.suren.autotest.interfaces.framework.data.SwaggerJsonValue;
import org.suren.autotest.interfaces.framework.junit.ReportRule;
import org.suren.autotest.interfaces.framework.junit.Skip;

import junit.framework.Assert;

/**
 * @author suren
 * @date 2017年6月30日 下午5:13:14
 */
public class PMTest
{
	@Rule
	public ReportRule reportRule = new ReportRule();
	
	@Test
//	@Skip
	public void buffer() throws UnsupportedEncodingException, IOException, DocumentException
	{
		String swaggerText;
		StringBuffer buffer = new StringBuffer();
		
		SimpleHttpClient client = new SimpleHttpClient();
		client.setHost("http://192.168.94.61:8080");
		
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("username", "zhaoxj@glodon.com");
		paramMap.put("password", "aaa111");
		client.executePost("/uaa/login", paramMap);
		
		swaggerText = client.executeGet("/cost/v2/api-docs");
		
		new SwaggerParse().transfer(swaggerText, buffer);
		
		System.out.println(buffer);
		
		Assert.assertTrue(buffer.length() > 0);
		
		List<Request> requestList = InterfacesParser.parseFromText(buffer);
		
		requestList.forEach((req) -> {
//			if(!req.getUrl().contains("quantityprices"))
//			{
//				req.setEnable(false);
//			}
			
			req.setJsonApp(true);
		});

		client.setHost("http://192.168.94.61:8080/cost");
		
		SwaggerJsonValue swaggerJsonValue = new SwaggerJsonValue();
		swaggerJsonValue.setDefinition(swaggerText);
		
		InvokeRequest invokeRequest = new InvokeRequest(client);
		invokeRequest.setSwaggerJsonDynamicValue(swaggerJsonValue);
		invokeRequest.invoke(requestList);
	}
}
