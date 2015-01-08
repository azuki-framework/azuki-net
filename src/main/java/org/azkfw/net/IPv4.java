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

import java.util.regex.Pattern;

import org.azkfw.util.StringUtility;

/**
 * このクラスは、IPv4情報を保持するクラスです。
 * 
 * @since 1.0.0
 * @version 1.0.0 2015/01/08
 * @author kawakicchi
 */
public final class IPv4 {

	private static final Pattern PATTERN = Pattern.compile("^[0-9]+\\.[0-9]+.[0-9]+.[0-9]+$");

	/** ip value */
	private Long ip;

	/**
	 * コンストラクタ
	 * 
	 * @param address IPv4(xxx.xxx.xxx.xxx)
	 */
	public IPv4(final String address) {
		ip = parseLong(address);
	}

	/**
	 * コンストラクタ
	 * 
	 * @param address IPv4(0 - 4294967295)
	 */
	public IPv4(final Long address) {
		ip = parseLong(address);
	}

	public static Long parseLong(final Long address) {
		if (null != address) {
			if (0 <= address && 4294967295L >= address) {
				return address;
			}
		}
		return null;
	}

	public static Long parseLong(final String address) {
		if (StringUtility.isNotEmpty(address)) {
			if (PATTERN.matcher(address).matches()) {
				String[] split = address.split("\\.");
				String str1 = split[0];
				String str2 = split[1];
				String str3 = split[2];
				String str4 = split[3];
				int ip1 = Integer.parseInt(str1);
				int ip2 = Integer.parseInt(str2);
				int ip3 = Integer.parseInt(str3);
				int ip4 = Integer.parseInt(str4);

				if (ip1 < 256 && ip2 < 256 && ip3 < 256 && ip4 < 256) {
					long buf = ((long) ip1 * (256L * 256L * 256L)) + ((long) ip2 * (256L * 256L)) + ((long) ip3 * (256L)) + ((long) ip4);
					return buf;
				}
			}
		}
		return null;
	}

	public String getString() {
		String address = null;
		if (null != ip) {
			long buf = ip;
			int ip1 = 0;
			int ip2 = 0;
			int ip3 = 0;
			int ip4 = 0;
			if (buf >= 256 * 256 * 256) {
				ip1 = (int) (buf / (256 * 256 * 256));
				buf = (int) (buf % (256 * 256 * 256));
			}
			if (buf >= 256 * 256) {
				ip2 = (int) (buf / (256 * 256));
				buf = (int) (buf % (256 * 256));
			}
			if (buf >= 256) {
				ip3 = (int) (buf / (256));
				buf = (int) (buf % (256));
			}
			ip4 = (int) buf;
			address = String.format("%d.%d.%d.%d", ip1, ip2, ip3, ip4);
		}
		return address;
	}

	public Long getValue() {
		return ip;
	}

	public String toString() {
		String s = "";
		if (null != ip) {
			s = getString();
		}
		return s;
	}
}
