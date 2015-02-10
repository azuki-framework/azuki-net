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
package org.azkfw.net.ftp;

import junit.framework.TestCase;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * このクラスは、{@link SimpleFTPClient}クラスの評価を行うクラスです。
 * 
 * @since 1.0.0
 * @version 1.0.0 2015/02/10
 * @author kawakicchi
 */
@RunWith(JUnit4.class)
public class SimpleFTPClientTest extends TestCase {

	@Test
	public void download() {
		SimpleFTPClient client = new SimpleFTPClient();

		// ARIN アメリカ
		// ftp://ftp.arin.net/pub/stats/arin/delegated-arin-extended-latest
		client.connect("ftp.arin.net");
		client.login();
		client.download("/pub/stats/arin/delegated-arin-extended-latest", "delegated-arin-extended-latest.csv");
		client.logout();
		client.disconnect();

		// RIPE ヨーロッパ
		// ftp://ftp.ripe.net/pub/stats/ripencc/delegated-ripencc-extended-latest
		client.connect("ftp.ripe.net");
		client.login();
		client.download("/pub/stats/ripencc/delegated-ripencc-extended-latest", "delegated-ripencc-extended-latest.csv");
		client.logout();
		client.disconnect();

		// AP アジア
		// ftp://ftp.apnic.net/pub/stats/apnic/delegated-apnic-extended-latest
		client.connect("ftp.apnic.net");
		client.login();
		client.download("/pub/stats/apnic/delegated-apnic-extended-latest", "delegated-apnic-extended-latest.csv");
		client.logout();
		client.disconnect();

		// LAC ラテンアメリカ
		// ftp://ftp.lacnic.net/pub/stats/lacnic/delegated-lacnic-extended-latest
		client.connect("ftp.lacnic.net");
		client.login();
		client.download("/pub/stats/lacnic/delegated-lacnic-extended-latest", "delegated-lacnic-extended-latest.csv");
		client.logout();
		client.disconnect();

		// AFRI アフリカ
		// ftp://ftp.afrinic.net/pub/stats/afrinic/delegated-afrinic-extended-latest
		client.connect("ftp.afrinic.net");
		client.login();
		client.download("/pub/stats/afrinic/delegated-afrinic-extended-latest", "delegated-afrinic-extended-latest.csv");
		client.logout();
		client.disconnect();
	}
}
