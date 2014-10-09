package com.upmc.transilien.parse;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.jdom2.DataConversionException;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;

import com.upmc.transilien.v1.model.Train;
import com.upmc.transilien.v1.repository.TrainRepository;

/**
 * Parse du XML en Objet du modèle
 * 
 * @author Kevin Coquart && Mag-Stellon Nadarajah
 *
 */
public class XMLToObject {
	/**
	 * Parse le flux de réponse de l'API transilien en une liste de Train
	 * 
	 * @param reponse
	 *            le flux depuis l'API transilien
	 * @return la liste des trains issu du flux
	 */
	public static List<Train> parseTrain(InputStream reponse) {
		Document document;
		Element racine;

		// On crée une instance de SAXBuilder
		SAXBuilder sxb = new SAXBuilder();
		List<Train> trains = new ArrayList<Train>();
		try {
			document = sxb.build(reponse);
			racine = document.getRootElement();

			int depart = racine.getAttribute("gare").getIntValue();
			List<Element> listTrains = racine.getChildren("train");

			for (Element elt : listTrains) {
				Train t = new Train(depart, Integer.parseInt(elt.getChildText("term")), Integer.parseInt(elt.getChildText("num")), elt.getChildText("miss"),
						elt.getChildText("date"), elt.getChildText("etat"));
				TrainRepository.getInstance().create(t);
				trains.add(t);
			}

		} catch (DataConversionException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return trains;
	}
}
