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

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;

/**
 * @since 1.0.0
 * @version 1.0.0 2015/01/08
 * @author kawakicchi
 */
public class Whois {

	private String crlf = "\n";

	public static void main(final String[] args) {
		Whois whois = new Whois();
		String string = whois.get("202.12.30.0");

		System.out.println(string);
	}

	public Whois() {
		try {
			crlf = System.getProperty("line.separator");
		} catch (SecurityException e) {
		}
	}

	public String get(final String address) {
		String str = null;
		try {
			InetAddress host = InetAddress.getByName("whois.nic.ad.jp");
			int port = 43;

			Socket sock = new Socket(host, port);

			BufferedReader in = new BufferedReader(new InputStreamReader(sock.getInputStream(), "ISO2022JP"));

			OutputStreamWriter osw = new OutputStreamWriter(sock.getOutputStream(), "ISO2022JP");
			osw.write(address + crlf);
			osw.flush();

			StringBuffer sbBuf = new StringBuffer();
			String strMes = null;
			while ((strMes = in.readLine()) != null) {
				sbBuf.append(strMes);
				sbBuf.append("\r\n");
			}

			str = sbBuf.toString();

			in.close();
			sock.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return str;
	}
}
