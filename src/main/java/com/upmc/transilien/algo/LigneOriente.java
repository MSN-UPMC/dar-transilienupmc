package com.upmc.transilien.algo;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import com.upmc.transilien.v1.model.Gare;
import com.upmc.transilien.v1.model.Ligne;
import com.upmc.transilien.v1.repository.GareRepository;

public class LigneOriente {
	class GareOriente extends Gare {
		private GareOriente voisin1, voisin2;

		public GareOriente(Gare g) {
			super(g.getNom(), g.getCodeUIC(), g.getLongitude(), g.getLatitude());
			voisin1 = voisin2 = null;
		}
	}

	class Couple {
		int codeUIC;
		double distance;

		Couple(int codeUIC, double distance) {
			this.codeUIC = codeUIC;
			this.distance = distance;
		}
	}

	public void execute(Ligne ligne) throws Exception {
		Map<Integer, GareOriente> gos = new HashMap<Integer, GareOriente>();

		List<Integer> aTraiter = new ArrayList<Integer>(ligne.getGares());
		for (Integer ite : aTraiter)
			gos.put(ite, new GareOriente(GareRepository.getInstance().findGareByCode(ite)));

		while (!aTraiter.isEmpty()) {
			Integer elt = aTraiter.remove(0);
			GareOriente gareElt = gos.get(elt);
			if (gareElt == null)
				throw new Exception("La gare " + elt + " n'existe pas ou plus 'voir les doublons.");

			PriorityQueue<Couple> distances = new PriorityQueue<Couple>(5, new Comparator<Couple>() {
				public int compare(Couple c1, Couple c2) {
					return Double.compare(c1.distance, c2.distance);
				}
			});

			for (Integer ite : aTraiter)
				distances.add(new Couple(ite, gareElt.calcule(gos.get(ite))));

			while (!distances.isEmpty() && (gareElt.voisin1 == null || gareElt.voisin2 == null)) {
				Couple c = distances.poll();
				if (gareElt.voisin1 == null)
					gareElt.voisin1 = gos.get(c.codeUIC);
				else {
					// calcule de vecteur
				}
			}
		}
	}
}
