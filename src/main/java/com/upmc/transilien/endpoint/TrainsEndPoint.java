package com.upmc.transilien.endpoint;

import java.util.Collection;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.Named;
import com.upmc.transilien.model.train.Train;
import com.upmc.transilien.model.train.Trains;

@Api(name = "trains", version = "v1")
public class TrainsEndPoint {

	@ApiMethod(name = "list", httpMethod = ApiMethod.HttpMethod.GET, path = "list")
	public Collection<Train> getTrains() {
		return Trains.getInstance().findTrains();
	}

	@ApiMethod(name = "create", httpMethod = ApiMethod.HttpMethod.POST, path = "create")
	public Train create(Train train) {
		return Trains.getInstance().create(train);
	}

	// TODO a voir si on modifie les trains
	// @ApiMethod(name = "update", httpMethod = ApiMethod.HttpMethod.PUT, path =
	// "update")
	// public Train update(Train editedTrain) {
	// return Trains.getInstance().update(editedTrain);
	// }

	@ApiMethod(name = "remove", httpMethod = ApiMethod.HttpMethod.DELETE, path = "remove")
	public void remove(@Named("id") Long id) {
		Trains.getInstance().remove(id);
	}
}