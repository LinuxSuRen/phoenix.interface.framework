/**
 * http://surenpi.com
 */
package org.suren.autotest.interfaces.framework.param;

import java.io.Serializable;

/**
 * 自动化测试框架中封装的Cookie
 * @author suren
 * @date 2017年1月6日 下午9:15:38
 */
public class AtCookie implements Serializable
{
	/**  */
	private static final long	serialVersionUID	= 1L;
	private String name;
	private String value;
	private String domain;
	private String path;
	/**
	 * @return the name
	 */
	public String getName()
	{
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name)
	{
		this.name = name;
	}
	/**
	 * @return the value
	 */
	public String getValue()
	{
		return value;
	}
	/**
	 * @param value the value to set
	 */
	public void setValue(String value)
	{
		this.value = value;
	}
	/**
	 * @return the domain
	 */
	public String getDomain()
	{
		return domain;
	}
	/**
	 * @param domain the domain to set
	 */
	public void setDomain(String domain)
	{
		this.domain = domain;
	}
	/**
	 * @return the path
	 */
	public String getPath()
	{
		return path;
	}
	/**
	 * @param path the path to set
	 */
	public void setPath(String path)
	{
		this.path = path;
	}
}
