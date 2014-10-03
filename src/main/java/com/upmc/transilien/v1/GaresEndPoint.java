package com.upmc.transilien.v1;

import java.util.Collection;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.Named;
import com.upmc.transilien.v1.model.Gare;
import com.upmc.transilien.v1.repository.Gares;

@Api(name = "gares", version = "v1")
public class GaresEndPoint {

	@ApiMethod(name = "getGares", httpMethod = ApiMethod.HttpMethod.GET, path = "list")
	public Collection<Gare> getGares() {
		return Gares.getInstance().findGares();
	}

	@ApiMethod(name = "getGareByCode", httpMethod = ApiMethod.HttpMethod.GET, path = "getGareByCode")
	public Gare getGareByCode(@Named("codeUIC") String codeUIC) {
		return Gares.getInstance().findGareByCode(codeUIC);
	}

	@ApiMethod(name = "getGareByName", httpMethod = ApiMethod.HttpMethod.GET, path = "getGareByName")
	public Gare getGareByName(@Named("name") String name) {
		return Gares.getInstance().findGareByName(name);
	}

	@ApiMethod(name = "create", httpMethod = ApiMethod.HttpMethod.POST, path = "create")
	public Gare create(Gare gare) {
		return Gares.getInstance().create(gare);
	}

	// TODO a voir si on modifie les gares
	// @ApiMethod(name = "update", httpMethod = ApiMethod.HttpMethod.PUT, path =
	// "update")
	// public Gare update(Gare editedGare) {
	// return Gares.getInstance().update(editedGare);
	// }

	@ApiMethod(name = "remove", httpMethod = ApiMethod.HttpMethod.DELETE, path = "remove")
	public void remove(@Named("id") Long id) {
		Gares.getInstance().remove(id);
	}
}