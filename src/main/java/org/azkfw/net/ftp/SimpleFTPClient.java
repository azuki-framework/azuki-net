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
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.azkfw.util.StringUtility;


/**
 * 
 * @since 1.0.0
 * @version 1.0.0 2015/02/10
 * @author kawakicchi
 */
public class SimpleFTPClient {

	/** ホスト名 */
	private String host;
	/** ポート番号 */
	private int port;

	/** FTPクライアント */
	private FTPClient client;

	/**
	 * コンストラクタ
	 */
	public SimpleFTPClient() {

	}

	/**
	 * コンストラクタ
	 * 
	 * @param host ホスト名
	 */
	public SimpleFTPClient(final String host) {
		this(host, 21);
	}

	/**
	 * コンストラクタ
	 * 
	 * @param host ホスト名
	 * @param port ポート番号
	 */
	public SimpleFTPClient(final String host, final int port) {
		this.host = host;
		this.port = port;
	}

	/**
	 * ホストに接続する。
	 * 
	 * @return 結果
	 */
	public boolean connect() {
		return connect(host, port);
	}

	/**
	 * ホストに接続する。
	 * 
	 * @param host ホスト名
	 * @return 結果
	 */
	public boolean connect(final String host) {
		return connect(host, 21);
	}

	/**
	 * ホストに接続する。
	 * 
	 * @param host ホスト名
	 * @param port ポート番号
	 * @return 結果
	 */
	public boolean connect(final String host, final int port) {
		boolean result = false;

		this.host = host;
		this.port = port;

		if (null == client) {
			client = new FTPClient();
		}

		if (client.isConnected()) {
			try {
				client.disconnect();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}

		try {
			client.connect(host, port);
			int reply = client.getReplyCode();
			if (!FTPReply.isPositiveCompletion(reply)) {
				throw new IOException(String.format("Can't connect to : %s", host));
			}

			result = true;

		} catch (IOException ex) {
			ex.printStackTrace();
		}

		return result;
	}

	/**
	 * ホストを切断する。
	 * 
	 * @return 結果
	 */
	public boolean disconnect() {
		boolean result = true;

		if (null != client) {
			if (client.isConnected()) {
				try {
					client.disconnect();
				} catch (IOException ex) {
					result = false;
					ex.printStackTrace();
				}
			}
		}

		return result;
	}

	/**
	 * ログインする。
	 * <p>
	 * 匿名アクセス
	 * </p>
	 * 
	 * @return 結果
	 */
	public boolean login() {
		return login("anonymous", "azuki@");
	}

	/**
	 * ログインする
	 * 
	 * @param user ユーザ名
	 * @param password パスワード
	 * @return 結果
	 */
	public boolean login(final String user, final String password) {
		boolean result = false;
		try {
			if (!client.login(user, password)) {
				throw new IOException(String.format("Invalid user/passowrd"));
			}

			//client.pasv();
			client.enterLocalPassiveMode();

			if (!client.setFileType(FTP.BINARY_FILE_TYPE)) {
				throw new IOException(String.format("Can't change file type"));
			}

			result = true;
		} catch (IOException ex) {
			showServerReply(client);

			ex.printStackTrace();
		} finally {

		}

		return result;
	}

	/**
	 * ログアウトする。
	 * 
	 * @return 結果
	 */
	public boolean logout() {
		boolean result = false;
		try {
			result = client.logout();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return result;
	}

	public boolean download(final String remotePath, final String localPath) {
		boolean result = false;

		System.out.println(String.format("download : %s", remotePath));

		try {
			String[] paths = remotePath.split("/");
			if (remotePath.startsWith("/")) {
				if (!client.changeWorkingDirectory("/")) {
					throw new IOException(String.format("Can't change directory"));
				}
			}

			List<String> dirNames = new ArrayList<String>();
			for (int i = 0; i < paths.length - 1; i++) {
				String dirName = paths[i];
				if (StringUtility.isNotEmpty(dirName)) {
					dirNames.add(dirName);
				}
			}
			String fileName = paths[paths.length - 1];

			for (String dirName : dirNames) {
				if (!client.changeWorkingDirectory(dirName)) {
					throw new IOException(String.format("Can't change directory"));
				}
			}

			FileOutputStream fos = new FileOutputStream(localPath);
			client.retrieveFile(fileName, fos);
			showServerReply(client);
			fos.close();

		} catch (IOException ex) {
			showServerReply(client);

			ex.printStackTrace();
		}

		return result;
	}

	private void showServerReply(FTPClient ftpClient) {
		String[] replies = ftpClient.getReplyStrings();
		if (replies != null && replies.length > 0) {
			for (String aReply : replies) {
				System.out.println(" => " + aReply);
			}
		}
	}
}
