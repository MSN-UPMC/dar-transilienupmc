package com.upmc.transilien.algo;

import java.util.ArrayList;
import java.util.List;

import com.upmc.transilien.v1.model.Gare;
import com.upmc.transilien.v1.model.Ligne;
import com.upmc.transilien.v1.repository.GareRepository;
import com.upmc.transilien.v1.repository.LigneRepository;

/**
 * Algo nécessaire pour initialisé l'application
 * 
 * @author Kevin Coquart &amp; Mag-Stellon Nadarajah
 *
 */
public class Initialisation {

	/**
	 * Retire les gares qui sont en dehors de notre réseau.<br>
	 * Le fichier json du transilien contient des gares TER ainsi que celle du T4 or leur API ne les gerent pas.
	 */
	public static void filtreGareNonTransilien() {
		List<Gare> gares = GareRepository.getInstance().findGares();
		List<Ligne> lignes = LigneRepository.getInstance().findLigne();

		List<Integer> existInLine = new ArrayList<Integer>();
		for (Ligne ligne : lignes)
			existInLine.addAll(ligne.getGares());

		for (Gare gare : gares)
			if (!existInLine.contains(gare.getCodeUIC()))
				GareRepository.getInstance().remove(gare.getId());
	}

}
