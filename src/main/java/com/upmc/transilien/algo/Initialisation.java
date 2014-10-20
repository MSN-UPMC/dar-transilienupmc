package com.upmc.transilien.algo;

import java.util.ArrayList;
import java.util.List;

import com.upmc.transilien.v1.model.Gare;
import com.upmc.transilien.v1.model.Ligne;
import com.upmc.transilien.v1.repository.GareRepository;
import com.upmc.transilien.v1.repository.LigneRepository;

public class Initialisation {

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
