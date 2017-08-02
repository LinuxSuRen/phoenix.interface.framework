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
import org.junit.AfterClass;
import org.junit.BeforeClass;
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
    private static SimpleHttpClient client;
    private static String sessionId;
    
    @BeforeClass
    public static void newSession() throws ParseException, IOException
    {
        client = new SimpleHttpClient();
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
        
        sessionId = jsonObject.get("sessionId").getAsString();
        System.out.println(sessionId);
        
        openUrl("http://surenpi.com");
    }
    
    private static void openUrl(String url) throws ParseException, IOException
    {
        Map<String, String> param = new HashMap<String, String>();
        param.clear();
        param.put("url", url);
        String res = client.executePost("/session/" + sessionId + "/url", param, true);
        System.out.println(res);
    }
    
    @Test
    public void getTitle() throws ParseException, IOException
    {
        String res = client.executeGet("/session/" + sessionId + "/title");
        System.out.println("get title : " + res);
    }
    
    @Test
    public void getUrl() throws ParseException, IOException
    {
        String res = client.executeGet("/session/" + sessionId + "/url");
        System.out.println("get current url : " + res);
    }
    
    @Test
    public void findElement() throws ParseException, IOException
    {
//        res = client.executePost("/session/" + sessionId + "/timeouts", null);
//        System.out.println("set timeouts" + res);
        
//        res = client.executeGet("/session/" + sessionId + "/timeouts");
//        System.out.println("timeouts : " + res);
        Map<String, String> param = new HashMap<String, String>();
        param.put("using", "id");
        param.put("value", "content-sidebar");
        
        String res = client.executePost("/session/" + sessionId + "/element", param, true);
        System.out.println("find element : " + res);
        
        JsonParser jsonParser = new JsonParser();
        JsonObject jsonObject = (JsonObject) jsonParser.parse(res);
        
        String elementId = jsonObject.get("value").getAsJsonObject().get("ELEMENT").getAsString();
        
        getAttribute(elementId);
        getCss(elementId);
        isSelected(elementId);
        elementEnabled(elementId);
    }
    
    private void getAttribute(String elementId) throws ParseException, IOException
    {
        String res = client.executeGet("/session/" + sessionId + "/element/" + elementId + "/attribute/class");
        System.out.println("getAttribute: " + res);
    }
    
    private void getCss(String elementId) throws ParseException, IOException
    {
        String res = client.executeGet("/session/" + sessionId + "/element/" + elementId + "/css/width");
        System.out.println("getCss: " + res);
    }
    
    private void isSelected(String elementId) throws ParseException, IOException
    {
        String res = client.executeGet("/session/" + sessionId + "/element/" + elementId + "/selected");
        System.out.println("isSelected: " + res);
    }
    
    private void elementEnabled(String elementId) throws ParseException, IOException
    {
        String res = client.executeGet("/session/" + sessionId + "/element/" + elementId + "/enabled");
        System.out.println("elementEnabled: " + res);
    }
    
    @Test
    public void getAllCookie() throws ParseException, IOException
    {
        String res = client.executeGet("/session/" + sessionId + "/cookie");
        System.out.println("getAllCookie: " + res);
    }
    
    @Test
    public void status() throws ParseException, IOException
    {
        String res = client.executeGet("/status");
        System.out.println("status: " + res);
    }
    
    @AfterClass
    public static void close() throws ParseException, IOException
    {
        String res = client.executeDel("/session/" + sessionId);
        System.out.println(res);
    }
}
