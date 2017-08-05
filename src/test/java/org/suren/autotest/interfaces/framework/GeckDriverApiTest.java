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
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.ParseException;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import net.sf.json.JSONArray;
import net.sf.json.JSONNull;
import net.sf.json.JSONObject;

/**
 * @author suren
 * @date 2017年8月1日 下午3:29:55
 */
//@Ignore
public class GeckDriverApiTest
{
    private static SimpleHttpClient client;
    private static String sessionId;
    
    @BeforeClass
    public static void newSession() throws ParseException, IOException
    {
        client = new SimpleHttpClient();
        client.setHost("http://localhost:2828");
    }
    
    @Test
    public void test() throws ParseException, IOException
    {
        JSONObject param = new JSONObject();
        param.put("sessionId", JSONNull.getInstance());
        param.put("capabilities", JSONNull.getInstance());
        
        JSONArray array = new JSONArray();
        array.add(0);
        array.add(1);
        array.add("newSession");
        array.add(param.toString());

        System.out.println(param.toString());
        System.out.println(array.toString());
        String arrayStr = array.toString();
        
        String payload = String.format("%d:%s", arrayStr.length(), arrayStr);
//        String res = client.executeGet("/" + payload);

//        System.out.println(res);
        SocketAddress addr = new InetSocketAddress("127.0.0.1", 2828);
        Socket socket = new Socket();
        socket.connect(addr);
        
        OutputStream out = socket.getOutputStream();
        InputStream in = socket.getInputStream();
        
        out.write(payload.getBytes());
        
        
        int len = -1;
        byte[] buf = new byte[1024];
        
        in.read(buf, 0, 10);
        String head = new String(buf, 0, 10);
        int index = head.indexOf(":");
        
        while((len = in.read(buf)) != -1)
        {
            System.out.print(new String(buf, 0, len));
        }
        
        open(out, in);
    }
    
    public void open(OutputStream out, InputStream in) throws IOException
    {
        JSONObject param = new JSONObject();
        param.put("url", "http://surenpi.com");
        
        JSONArray array = new JSONArray();
        array.add(0);
        array.add(2);
        array.add("get");
        array.add(param.toString());
        
        out.write(array.toString().getBytes());
        
        int len = -1;
        byte[] buf = new byte[1024];
        while((len = in.read(buf)) != -1)
        {
            System.out.print(new String(buf, 0, len));
        }
    }
    
    @AfterClass
    public static void close() throws ParseException, IOException
    {
    }
}
