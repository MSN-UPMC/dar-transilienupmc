package com.upmc.transilien.v1.endPoint;

import java.util.Collection;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.Named;
import com.upmc.transilien.v1.model.Train;
import com.upmc.transilien.v1.repository.ItineraireRepository;

/**
 * EndPoint des Itinéraires
 * 
 * @author Kevin Coquart && Mag-Stellon Nadarajah
 *
 */
@Api(name = "itineraire", version = "v1")
public class ItineraireEndPoint {
	/**
	 * Recherche les trains au prochain départ de la gare
	 * 
	 * @param codeUIC
	 *            le codeUIC de la gare
	 * @return les trains qui passent prochainement par la gare
	 * @throws Exception
	 */
	@ApiMethod(name = "prochainDepart", httpMethod = ApiMethod.HttpMethod.GET, path = "prochainDepart")
	public Collection<Train> prochainDepart(@Named("codeUIC") int codeUIC) throws Exception {
		return ItineraireRepository.getInstance().prochainDepart(codeUIC);
	}

	/**
	 * Recherche les trains au prochain départ de la gare et qui passe par la destination.<br>
	 * Pour recherche un itinéraire qui utilisent différentes lignes, il va nous falloir le réaliser.
	 * 
	 * @param departUIC
	 *            le codeUIC de la gare de départ
	 * @param destinationUIC
	 *            le codeUIC de la gare de destination
	 * @return la liste des trains au départ de gare donné et à la destination voulue
	 * @throws Exception
	 */
	@ApiMethod(name = "itineraire", httpMethod = ApiMethod.HttpMethod.GET, path = "itineraire")
	public Collection<Train> itineraire(@Named("departCodeUIC") int departUIC, @Named("destinationCodeUIC") int destinationUIC) throws Exception {
		return ItineraireRepository.getInstance().prochainDepart(departUIC, destinationUIC);
	}
}
