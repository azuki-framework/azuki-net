package org.azkfw.net.ftp;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.azkfw.util.StringUtility;

public class SimpleFTPClient {

	private String host;
	private int port;

	private FTPClient client;

	public static void main(final String[] args) {
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

	public SimpleFTPClient() {

	}

	public SimpleFTPClient(final String host) {
		this(host, 21);
	}

	public SimpleFTPClient(final String host, final int port) {
		this.host = host;
		this.port = port;
	}

	public boolean connect() {
		return connect(host, port);
	}

	public boolean connect(final String host) {
		return connect(host, 21);
	}

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

	public boolean login() {
		return login("anonymous", "anonymous");
	}

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

	public boolean logout() {
		boolean result = false;
		try {
			result = client.logout();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return result;
	}

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

	private void showServerReply(FTPClient ftpClient) {
		String[] replies = ftpClient.getReplyStrings();
		if (replies != null && replies.length > 0) {
			for (String aReply : replies) {
				System.out.println(" => " + aReply);
			}
		}
	}
}
