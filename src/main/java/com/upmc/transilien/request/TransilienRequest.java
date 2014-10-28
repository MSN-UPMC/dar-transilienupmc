package com.upmc.transilien.request;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import com.upmc.transilien.parse.XMLToObject;
import com.upmc.transilien.v1.model.Train;

/**
 * Requête sur l'API transilien prochainDepart
 * 
 * @author Kevin Coquart &amp; Mag-Stellon Nadarajah
 *
 */
public class TransilienRequest {
	private static String urlBase = "http://api.transilien.com/gare/";
	private static String userpass = "tnhtn175:cgP479kW";

	/**
	 * Récupère le retour d'une demande à l'API prochain départ
	 * 
	 * @param codeUIC
	 *            le codeUIC de la gare de départ
	 * @return le flux renvoyé par l'API transilien (du xml)
	 * @throws IOException
	 * @throws Exception
	 */
	public static List<Train> prochainDepart(int codeUIC) throws IOException {
		URL url = new URL(urlBase + codeUIC + "/depart/");

		URLConnection uc = url.openConnection();
		String basicAuth = "Basic " + javax.xml.bind.DatatypeConverter.printBase64Binary(userpass.getBytes());
		uc.setRequestProperty("Authorization", basicAuth);

		return XMLToObject.parseTrain(uc.getInputStream());
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
	public static List<Train> prochainDepart(int departcodeUIC, int destinatationCodeUIC) throws Exception {
		URL url = new URL(urlBase + departcodeUIC + "/depart/" + destinatationCodeUIC + "/");

		URLConnection uc = url.openConnection();
		String basicAuth = "Basic " + javax.xml.bind.DatatypeConverter.printBase64Binary(userpass.getBytes());
		uc.setRequestProperty("Authorization", basicAuth);

		return XMLToObject.parseTrain(uc.getInputStream());
	}
}