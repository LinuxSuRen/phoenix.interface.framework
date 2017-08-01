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
import org.junit.Ignore;
import org.junit.Test;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import net.sf.json.JSONObject;

/**
 * @author suren
 * @date 2017年8月1日 下午3:29:55
 */
//@Ignore
public class WebDriverApiTest
{
    @Test
    public void newSession() throws ParseException, IOException
    {
        SimpleHttpClient client = new SimpleHttpClient();
        client.setHost("http://localhost:9515");

        JSONObject jsonObj = new JSONObject();
        
        JSONObject chromeOptionsJson = new JSONObject();
        chromeOptionsJson.put("args", new String[]{});
        chromeOptionsJson.put("extensions", new String[]{});
        
        JSONObject desiredCapabilitiesJson = new JSONObject();
        desiredCapabilitiesJson.put("browserName", "chrome");
        desiredCapabilitiesJson.put("chromeOptions", chromeOptionsJson);
        desiredCapabilitiesJson.put("version", "");
        desiredCapabilitiesJson.put("platform", "ANY");
        jsonObj.put("desiredCapabilities", desiredCapabilitiesJson);
        
        String res = client.executeJsonPost("/session", jsonObj);

        JsonParser jsonParser = new JsonParser();
        JsonObject jsonObject = (JsonObject) jsonParser.parse(res);
        
        String sessionId = jsonObject.get("sessionId").getAsString();
        System.out.println(sessionId);

        Map<String, String> param = new HashMap<String, String>();
        param.clear();
        param.put("url", "http://surenpi.com");
        res = client.executePost("/session/" + sessionId + "/url", param, true);
        System.out.println(res);
        
        res = client.executePost("/session/" + sessionId + "/timeouts", null);
        System.out.println(res);
        
        res = client.executeGet("/session/" + sessionId + "/timeouts");
        System.out.println(res);
        
        res = client.executeGet("/status");
        System.out.println(res);
        
        res = client.executeDel("/session/" + sessionId);
        System.out.println(res);
    }
}
