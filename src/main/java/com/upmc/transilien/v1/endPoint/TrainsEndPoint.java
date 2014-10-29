package com.upmc.transilien.v1.endPoint;

import java.util.Collection;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.upmc.transilien.v1.model.Train;
import com.upmc.transilien.v1.repository.ItineraireRepository;
import com.upmc.transilien.v1.repository.TrainRepository;

/**
 * EndPoint des Trains
 * 
 * @author Kevin Coquart &amp; Mag-Stellon Nadarajah
 *
 */
@Api(name = "trains", version = "v1")
public class TrainsEndPoint {
	/**
	 * @return la liste des trains existants
	 */
	@ApiMethod(name = "list", httpMethod = ApiMethod.HttpMethod.GET, path = "list")
	public Collection<Train> getTrains() {
		return TrainRepository.getInstance().findTrains();
	}

	/**
	 * Calcule le taux de service des trains.<br>
	 * Le dataStore limite le nombre d'appel, nous privilégions ici le calcule CPU dans le cas d'une vrai base de donnée ou d'une utilisation payante du
	 * dataStore, le tout serait en appel à la BDD.
	 * 
	 * @return un tableau de pourcentage
	 */
	@ApiMethod(name = "serveLevel", httpMethod = ApiMethod.HttpMethod.GET, path = "serveLevel")
	public double[] serveLevel() {
		double[] etatDeService = new double[3];

		Collection<Train> trains = TrainRepository.getInstance().findTrains();
		double nbRetard = 0, nbAnnule = 0;
		for (Train t : trains)
			switch (t.getEtat()) {
			case 1:
				nbRetard++;
				break;
			case 2:
				nbAnnule++;
				break;

			default:
				break;
			}

		int nbTrains = trains.size();
		etatDeService[0] = 100 - nbRetard - nbAnnule;
		etatDeService[1] = nbRetard / nbTrains * 100;
		etatDeService[2] = nbAnnule / nbTrains * 100;
		return etatDeService;
	}

	/**
	 * Ajoute des trains dans la base<br>
	 * URL faite pour le cron.
	 * 
	 * @param password
	 *            un mot de passe pour limiter l'accès
	 * @throws Exception
	 */
	@ApiMethod(name = "addTrainInDB", httpMethod = ApiMethod.HttpMethod.GET, path = "addTrainInDB")
	public void addTrainInDB() throws Exception {
		// Chatelet
		ItineraireRepository.getInstance().prochainDepart(87758607);
		// Gare de lyon
		ItineraireRepository.getInstance().prochainDepart(87686030);
		// La défense
		ItineraireRepository.getInstance().prochainDepart(87382218);
	}
}