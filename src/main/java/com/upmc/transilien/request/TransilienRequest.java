package com.upmc.transilien.request;

import java.io.InputStream;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

public class TransilienRequest {
	private final static String USER_AGENT = "Mozilla/5.0";
	private static String urlBase = "http://tnhtn175:cgP479kW@api.transilien.com/gare/";

	public static InputStream prochainDepart(String codeUIC) throws Exception {
		String url = urlBase + codeUIC + "/depart/";

		HttpClient client = new DefaultHttpClient();
		HttpGet request = new HttpGet(url);

		// add request header
		request.addHeader("User-Agent", USER_AGENT);

		HttpResponse response = client.execute(request);
		return response.getEntity().getContent();
	}

	public static InputStream prochainDepart(String departcodeUIC, String destinatationCodeUIC) throws Exception {
		String url = urlBase + departcodeUIC + "/depart/" + destinatationCodeUIC + "/";

		HttpClient client = new DefaultHttpClient();
		HttpGet request = new HttpGet(url);

		// add request header
		request.addHeader("User-Agent", USER_AGENT);

		HttpResponse response = client.execute(request);
		return response.getEntity().getContent();
	}
}