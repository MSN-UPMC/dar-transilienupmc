package com.upmc.transilien.parse;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.jdom2.DataConversionException;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;

import com.upmc.transilien.v1.model.Train;

public class XMLToTrain {
	static Document document;
	static Element racine;

	public static List<Train> parseTrain(InputStream reponse) {
		// On crée une instance de SAXBuilder
		SAXBuilder sxb = new SAXBuilder();
		List<Train> trains = new ArrayList<Train>();
		try {
			document = sxb.build(reponse);
			racine = document.getRootElement();

			// Long depart = racine.getAttribute("gare").getLongValue();
			List<Element> listTrains = racine.getChildren("train");

			for (Element elt : listTrains) {
				Train t = new Train(/* depart, elt.getChildText("term"), */elt.getChildText("num"), elt.getChildText("miss"), elt.getChildText("date"),
						elt.getChildText("etat"));
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
