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

package org.suren.autotest.interfaces.framework.data;

/**
 * @author suren
 * @date 2017年7月4日 下午4:23:46
 */
public abstract class AbstractDynamicValue implements DynamicValue
{

	@Override
	public String getValue(String type)
	{
		if(typeMatch(Integer.class, type))
		{
			return String.valueOf(getInt());
		}
		else if(typeMatch(String.class, type))
		{
			return getStr();
		}
		else if(typeMatch(Boolean.class, type))
		{
			return String.valueOf(getBoolean());
		}
		else if(typeMatch(Number.class, type))
		{
			return String.valueOf(getInt());
		}
		else if(typeMatch(Double.class, type))
		{
			return String.valueOf(getDouble());
		}
		else if(typeMatch(Float.class, type))
		{
			return String.valueOf(getFloat());
		}
		else if(typeMatch(Long.class, type))
		{
			return String.valueOf(getLong());
		}
		else
		{
			return getStr();
		}
	}
	
	protected boolean typeMatch(Class<?> clazz, String type)
	{
		return clazz.getSimpleName().toLowerCase().equals(type);
	}
	
	protected abstract int getInt();

	protected abstract String getStr();
	
	protected abstract boolean getBoolean();
	
	protected abstract double getDouble();
	
	protected abstract float getFloat();
	
	protected abstract long getLong();
}
