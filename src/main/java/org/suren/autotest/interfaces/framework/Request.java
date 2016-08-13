/**
 * http://surenpi.com
 */
package org.suren.autotest.interfaces.framework;

import java.util.List;

/**
 * @author suren
 * @date Aug 13, 2016 7:25:05 PM
 */
public class Request
{
	private String url;
	private String type;
	private List<Param> paramList;
	public String getUrl()
	{
		return url;
	}
	public void setUrl(String url)
	{
		this.url = url;
	}
	public String getType()
	{
		return type;
	}
	public void setType(String type)
	{
		this.type = type;
	}
	public List<Param> getParamList()
	{
		return paramList;
	}
	public void setParamList(List<Param> paramList)
	{
		this.paramList = paramList;
	}
}
