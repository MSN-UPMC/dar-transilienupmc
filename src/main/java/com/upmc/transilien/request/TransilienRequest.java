package com.upmc.transilien.request;

import java.io.InputStream;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.upmc.transilien.v1.repository.Gares;

public class TransilienRequest {
	private final static String USER_AGENT = "Mozilla/5.0";

	public static InputStream prochainDepart(String codeUIC) throws Exception {
		Gares gares = Gares.getInstance();
		if (gares.findGareByCode(codeUIC) == null) {
			throw new Exception("Le numero UIC de la gare n'est pas valide.");
		}

		String url = "http://api.transilien.com/gare/" + codeUIC + "/depart/";

		HttpClient client = new DefaultHttpClient();
		HttpGet request = new HttpGet(url);

		// add request header
		request.addHeader("User-Agent", USER_AGENT);

		HttpResponse response = client.execute(request);
		return response.getEntity().getContent();
	}

	public static InputStream prochainDepart(String departcodeUIC, String destinatationCodeUIC) throws Exception {
		Gares gares = Gares.getInstance();
		if (gares.findGareByCode(departcodeUIC) == null) {
			throw new Exception("Le numero UIC de la gare de départ n'est pas valide.");
		}
		if (gares.findGareByCode(destinatationCodeUIC) == null) {
			throw new Exception("Le numero UIC de la gare de destination n'est pas valide.");
		}

		String url = "http://api.transilien.com/gare/" + departcodeUIC + "/depart/" + destinatationCodeUIC + "/";

		HttpClient client = new DefaultHttpClient();
		HttpGet request = new HttpGet(url);

		// add request header
		request.addHeader("User-Agent", USER_AGENT);

		HttpResponse response = client.execute(request);
		return response.getEntity().getContent();
	}
}