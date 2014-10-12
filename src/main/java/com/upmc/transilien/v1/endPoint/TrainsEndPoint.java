package com.upmc.transilien.v1.endPoint;

import java.util.Collection;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.upmc.transilien.v1.model.Train;
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
}