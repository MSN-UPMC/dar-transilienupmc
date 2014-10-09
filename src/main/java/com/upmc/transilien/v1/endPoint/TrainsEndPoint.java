package com.upmc.transilien.v1.endPoint;

import java.util.Collection;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.upmc.transilien.v1.model.Train;
import com.upmc.transilien.v1.repository.TrainRepository;

/**
 * EndPoint des Trains
 * 
 * @author Kevin Coquart && Mag-Stellon Nadarajah
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

	// @ApiMethod(name = "create", httpMethod = ApiMethod.HttpMethod.POST, path = "create")
	// public Train create(Train train) {
	// return TrainRepository.getInstance().create(train);
	// }

	// TODO a voir si on modifie les trains
	// @ApiMethod(name = "update", httpMethod = ApiMethod.HttpMethod.PUT, path =
	// "update")
	// public Train update(Train editedTrain) {
	// return Trains.getInstance().update(editedTrain);
	// }

	// @ApiMethod(name = "remove", httpMethod = ApiMethod.HttpMethod.DELETE, path = "remove")
	// public void remove(@Named("id") Long id) {
	// TrainRepository.getInstance().remove(id);
	// }
}