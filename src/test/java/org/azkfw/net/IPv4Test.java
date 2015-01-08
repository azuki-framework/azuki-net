/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.azkfw.net;

import junit.framework.TestCase;

/**
 * このクラスは、{@link IPv4}クラスの評価を行うクラスです。
 * 
 * @since 1.0.0
 * @version 1.0.0 2015/01/08
 * @author kawakicchi
 */
public class IPv4Test extends TestCase {

	public void test() {
		IPv4 ip = null;

		ip = new IPv4(0L);
		assertEquals(Long.valueOf(0), ip.getValue());
		assertEquals("0.0.0.0", ip.getString());
		assertEquals("0.0.0.0", ip.toString());
		ip = new IPv4("0.0.0.0");
		assertEquals(Long.valueOf(0), ip.getValue());
		assertEquals("0.0.0.0", ip.getString());
		assertEquals("0.0.0.0", ip.toString());

		ip = new IPv4(4294967295L);
		assertEquals(Long.valueOf(4294967295L), ip.getValue());
		assertEquals("255.255.255.255", ip.getString());
		assertEquals("255.255.255.255", ip.toString());
		ip = new IPv4("255.255.255.255");
		assertEquals(Long.valueOf(4294967295L), ip.getValue());
		assertEquals("255.255.255.255", ip.getString());
		assertEquals("255.255.255.255", ip.toString());
	}

	public void testLimit() {
		IPv4 ip = null;

		ip = new IPv4(-1L);
		assertNull(ip.getValue());
		assertNull(ip.getString());
		assertEquals("", ip.toString());

		ip = new IPv4(4294967296L);
		assertNull(ip.getValue());
		assertNull(ip.getString());
		assertEquals("", ip.toString());
	}
}
