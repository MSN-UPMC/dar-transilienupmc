package com.upmc.transilien.parse;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.upmc.transilien.v1.model.Gare;
import com.upmc.transilien.v1.model.Ligne;
import com.upmc.transilien.v1.repository.GareRepository;
import com.upmc.transilien.v1.repository.LigneRepository;

/**
 * Classe statique qui convertit les fichiers JSON en Objet du modèle
 * 
 * @author Kevin Coquart &amp; Mag-Stellon Nadarajah
 *
 */
public class JsonToObject {
	/**
	 * Charge les gares à partir d'un fichier JSON<br>
	 * <br>
	 * La fonction parse le fichier puis le parcours pour créer chaque gare à la suite.
	 * 
	 * @param filename
	 *            le nom du fichier (sncf-gares-et-arrets-transilien-ile-de-france.json)
	 * @throws Exception
	 */
	public static void loadGare(String filename) throws Exception {
		Map<String, Gare> gares = new HashMap<String, Gare>();

		JSONParser parser = new JSONParser();
		JSONArray array = (JSONArray) parser.parse(new FileReader(filename));
		for (int i = 0; i < array.size(); i++) {
			JSONObject jsonGare = (JSONObject) ((JSONObject) array.get(i)).get("fields");
			int codeUIC = Integer.parseInt((String) jsonGare.get("code_uic"));

			String nom = (String) jsonGare.get("libelle");
			JSONArray coordGPS = (JSONArray) jsonGare.get("coord_gps_wgs84");
			Double latitude = (Double) coordGPS.get(0), longitude = (Double) coordGPS.get(1);

			if (gares.containsKey(nom)) {
				gares.get(nom).ajouteUIC(codeUIC, false);
			} else
				gares.put(nom, new Gare(nom, codeUIC, longitude, latitude));

		}

		for (Gare g : gares.values())
			GareRepository.getInstance().create(g);
	}

	/**
	 * Charge les lignes à partir d'un fichier JSON<br>
	 * <br>
	 * La fonction parse le fichier puis parcours les gares. A chaque gare, on l'ajoute à la ligne correspondante. La fonction se termine en sauvegardant les
	 * lignes.
	 * 
	 * @param filename
	 *            le nom du fichier (sncf-lignes-par-gares-idf.json)
	 * @throws ParseException
	 * @throws IOException
	 * @throws FileNotFoundException
	 * @throws Exception
	 */
	public static void loadLigne(String filename) throws IOException, ParseException {

		String[] lTrain = { "h", "j", "k", "l", "n", "p", "r", "u" }, lRER = { "a", "b", "c", "d", "e" };
		Map<String, Ligne> lignes = new HashMap<String, Ligne>();

		JSONParser parser = new JSONParser();
		JSONArray array = (JSONArray) parser.parse(new FileReader(filename));

		for (int i = 0; i < array.size(); i++) {
			JSONObject jsonLigne = (JSONObject) ((JSONObject) array.get(i)).get("fields");

			if (jsonLigne.get("train") != null)
				auxLoadLigne(lTrain, jsonLigne, lignes);
			if (jsonLigne.get("rer") != null)
				auxLoadLigne(lRER, jsonLigne, lignes);
		}

		for (Ligne ligne : lignes.values())
			LigneRepository.getInstance().create(ligne);
	}

	/**
	 * Fonction auxiliaire au chargement des lignes.<br>
	 * Simplifie la recherche des lignes de chaque gare.
	 * 
	 * @param noms
	 *            un tableau de nom de ligne
	 * @param jsonLigne
	 *            le json
	 * @param lignes
	 *            la map des lignes
	 */
	private static void auxLoadLigne(String[] noms, JSONObject jsonLigne, Map<String, Ligne> lignes) {
		for (String s : noms)
			if (jsonLigne.get(s) != null) {
				if (!lignes.containsKey(s))
					lignes.put(s, new Ligne(s));
				int codeUIC = Integer.parseInt((String) jsonLigne.get("code_uic"));
				lignes.get(s).addGares(codeUIC);
			}
	}
}
