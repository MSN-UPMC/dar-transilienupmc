package com.upmc.transilien.v1.endPoint;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;

/**
 * EndPoint d'Initialisation
 * 
 * @author Kevin Coquart &amp; Mag-Stellon Nadarajah
 *
 */
@Api(name = "initialisation", version = "v1")
public class InitialisationEndPoint {
	/**
	 * Initialise le systèmes
	 */
	@ApiMethod(name = "init", httpMethod = ApiMethod.HttpMethod.GET, path = "init")
	public void init() {
		// TODO integré le comportement directement une fois uniformisé et fini
		GaresEndPoint gep = new GaresEndPoint();
		LignesEndPoint lep = new LignesEndPoint();

		gep.loadGare();
		lep.loadLigne();
	}
}