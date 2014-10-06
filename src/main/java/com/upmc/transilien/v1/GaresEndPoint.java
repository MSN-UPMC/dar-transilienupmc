package com.upmc.transilien.v1;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import org.json.simple.parser.ParseException;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.Named;
import com.google.appengine.api.datastore.Text;
import com.upmc.transilien.parse.JsonToGare;
import com.upmc.transilien.v1.model.Gare;
import com.upmc.transilien.v1.repository.GareRepository;

@Api(name = "gares", version = "v1")
public class GaresEndPoint {

	@ApiMethod(name = "getGares", httpMethod = ApiMethod.HttpMethod.GET, path = "list")
	public Collection<Gare> getGares() {
		return GareRepository.getInstance().findGares();
	}

	@ApiMethod(name = "getGareByCode", httpMethod = ApiMethod.HttpMethod.GET, path = "getGareByCode")
	public Gare getGareByCode(@Named("codeUIC") String codeUIC) {
		return GareRepository.getInstance().findGareByCode(codeUIC);
	}

	@ApiMethod(name = "getGareByName", httpMethod = ApiMethod.HttpMethod.GET, path = "getGareByName")
	public Gare getGareByName(@Named("name") String name) {
		return GareRepository.getInstance().findGareByName(name);
	}

	@ApiMethod(name = "getGareName", httpMethod = ApiMethod.HttpMethod.GET, path = "getGareName")
	public List<String> getGareName() {
		return GareRepository.getInstance().findGareName();
	}

	@ApiMethod(name = "create", httpMethod = ApiMethod.HttpMethod.POST, path = "create")
	public Gare create(Gare gare) {
		return GareRepository.getInstance().create(gare);
	}

	@ApiMethod(name = "loadGare", httpMethod = ApiMethod.HttpMethod.POST, path = "loadGare")
	public Text loadGare() {
		try {
			JsonToGare.loadGare("ressources/sncf-gares-et-arrets-transilien-ile-de-france.json");
			return new Text("OK");
		} catch (IOException | ParseException e) {
			return new Text(System.getProperties().get("user.dir") + "\n" + e.getMessage());
		}
	}

	// TODO a voir si on modifie les gares
	// @ApiMethod(name = "update", httpMethod = ApiMethod.HttpMethod.PUT, path =
	// "update")
	// public Gare update(Gare editedGare) {
	// return Gares.getInstance().update(editedGare);
	// }

	@ApiMethod(name = "remove", httpMethod = ApiMethod.HttpMethod.DELETE, path = "remove")
	public void remove(@Named("id") Long id) {
		GareRepository.getInstance().remove(id);
	}
}