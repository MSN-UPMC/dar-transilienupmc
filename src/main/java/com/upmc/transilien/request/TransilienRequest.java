package com.upmc.transilien.request;

import java.io.InputStream;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

/**
 * Requête sur l'API transilien prochainDepart
 * 
 * @author Kevin Coquart && Mag-Stellon Nadarajah
 *
 */
public class TransilienRequest {
	private final static String USER_AGENT = "Mozilla/5.0";
	private static String urlBase = "http://tnhtn175:cgP479kW@api.transilien.com/gare/";

	/**
	 * Récupère le retour d'une demande à l'API prochain départ
	 * 
	 * @param codeUIC
	 *            le codeUIC de la gare de départ
	 * @return le flux renvoyé par l'API transilien (du xml)
	 * @throws Exception
	 */
	public static InputStream prochainDepart(int codeUIC) throws Exception {
		String url = urlBase + codeUIC + "/depart/";

		HttpClient client = new DefaultHttpClient();
		HttpGet request = new HttpGet(url);

		// add request header
		request.addHeader("User-Agent", USER_AGENT);

		HttpResponse response = client.execute(request);
		return response.getEntity().getContent();
	}

	/**
	 * Récupère le retour d'une demande à l'API prochain départ avec une destination
	 * 
	 * @param departcodeUIC
	 *            le codeUIC de la gare de départ
	 * @param destinatationCodeUIC
	 *            le codeUIC de la destination
	 * @return le flux renvoyé par l'API transilien (du xml)
	 * @throws Exception
	 */
	public static InputStream prochainDepart(int departcodeUIC, int destinatationCodeUIC) throws Exception {
		String url = urlBase + departcodeUIC + "/depart/" + destinatationCodeUIC + "/";

		HttpClient client = new DefaultHttpClient();
		HttpGet request = new HttpGet(url);

		// add request header
		request.addHeader("User-Agent", USER_AGENT);

		HttpResponse response = client.execute(request);
		return response.getEntity().getContent();
	}
}