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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.XMLWriter;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * @author suren
 * @date 2017年6月30日 下午4:57:08
 */
public class SwaggerParse
{
    public void transfer(String swaggerText, OutputStream output) throws UnsupportedEncodingException, IOException
    {
        Document doc = DocumentHelper.createDocument();
        Element root = doc.addElement("autotest");
        
        JsonParser jsonParser = new JsonParser();
        JsonObject jsonObject = (JsonObject) jsonParser.parse(swaggerText);
        JsonObject paths = (JsonObject) jsonObject.get("paths");
        paths.entrySet().forEach((path) -> {
        	String pathUri = path.getKey();
            Element interfaceGroupEle = root.addElement("interfaceGroup");
            interfaceGroupEle.addAttribute("url", pathUri);
        	
        	JsonObject pathObj = (JsonObject) path.getValue();
        	
        	pathObj.entrySet().forEach((entry) -> {
        		String method = entry.getKey();
        		JsonObject requestObj = (JsonObject) entry.getValue();
        		Element interfaceEle = interfaceGroupEle.addElement("interface");
        		interfaceEle.addAttribute("method", method);
        		
        		Element paramsEle = interfaceEle.addElement("params");

        		JsonArray params = (JsonArray) requestObj.get("parameters");
        		if(params != null)
        		{
            		params.forEach((param) -> {
            			JsonObject paramObj = param.getAsJsonObject();
            			String paramName = paramObj.get("name").getAsString();
            			String in = paramObj.get("in").getAsString();
                        
                        Element paramEle = paramsEle.addElement("param");
                        
                        paramEle.addAttribute("name", paramName);
                        paramEle.addAttribute("in", in);
                        if(paramObj.get("type") != null)
                        {
                			String type = paramObj.get("type").getAsString();
                        	
                			paramEle.addAttribute("type", type);
                        }
            		});
        		}
        	});
        });
        
    	XMLWriter writer = new XMLWriter(output);
		writer.write(doc);
    }

    public void transfer(String swaggerText, StringBuffer buffer) throws UnsupportedEncodingException, IOException
    {
    	ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();
    	transfer(swaggerText, byteArrayOut);
    	buffer.append(byteArrayOut.toString());
    }
    
    public void transfer(String swaggerText, File targetFile) throws FileNotFoundException, IOException
    {
        try(OutputStream out = new FileOutputStream(targetFile))
		{
        	transfer(swaggerText, out);
		}
    }
}
