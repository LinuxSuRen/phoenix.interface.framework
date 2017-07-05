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

import java.util.Random;

/**
 * @author suren
 * @date 2017年7月4日 上午8:55:46
 */
public class RandomDynamicValue extends SimpleDynamicValue
{
	private Random random = new Random();

	@Override
	protected int getInt()
	{
		return random.nextInt();
	}

	@Override
	protected String getStr()
	{
		return String.valueOf(getInt());
	}

	@Override
	protected boolean getBoolean()
	{
		return random.nextBoolean();
	}

	@Override
	protected double getDouble()
	{
		return random.nextDouble();
	}

	@Override
	protected float getFloat()
	{
		return random.nextFloat();
	}

	@Override
	protected long getLong()
	{
		return random.nextLong();
	}

}
