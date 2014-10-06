package com.upmc.transilien.parse;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.upmc.transilien.v1.model.Gare;
import com.upmc.transilien.v1.repository.GareRepository;

public class JsonToGare {
	public static void loadGare(String filename) throws FileNotFoundException, IOException, ParseException {
		JSONParser parser = new JSONParser();
		JSONArray array = (JSONArray) parser.parse(new FileReader(filename));

		GareRepository gareRepo = GareRepository.getInstance();
		for (int i = 0; i < array.size(); i++) {
			JSONObject gare = (JSONObject) ((JSONObject) array.get(i)).get("fields");
			String codeUIC = (String) gare.get("code_uic");

			if (gareRepo.findGareByCode(codeUIC) != null)
				continue;

			String nom = (String) gare.get("libelle");
			JSONArray coordGPS = (JSONArray) gare.get("coord_gps_wgs84");
			Double longitude = (Double) coordGPS.get(1), lattitude = (Double) coordGPS.get(0);

			gareRepo.create(new Gare(nom, codeUIC, longitude, lattitude));
		}
	}

	// TODO effacer le test
	// public static void main(String[] args) throws FileNotFoundException, IOException, ParseException {
	// JSONParser parser = new JSONParser();
	// JSONArray array = (JSONArray) parser.parse(new FileReader("ressourcesACOPIERDANSTARGET/sncf-gares-et-arrets-transilien-ile-de-france.json"));
	//
	// String s = "";
	// for (int i = 0; i < array.size(); i++) {
	// JSONObject gare = (JSONObject) ((JSONObject) array.get(i)).get("fields");
	// String codeUIC = (String) gare.get("code_uic");
	//
	// String nom = (String) gare.get("libelle");
	// JSONArray coordGPS = (JSONArray) gare.get("coord_gps_wgs84");
	// Double longitude = (Double) coordGPS.get(1), lattitude = (Double) coordGPS.get(0);
	//
	// s += new Gare(nom, codeUIC, longitude, lattitude).toString();
	// }
	// System.out.println(s);
	// }
}
