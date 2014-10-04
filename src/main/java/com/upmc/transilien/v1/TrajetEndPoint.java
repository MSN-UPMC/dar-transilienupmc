package com.upmc.transilien.v1;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.Named;
import com.google.appengine.api.datastore.Text;
import com.upmc.transilien.parse.XMLToTrain;
import com.upmc.transilien.request.TransilienRequest;
import com.upmc.transilien.v1.model.Train;

@Api(name = "trajet", version = "v1")
public class TrajetEndPoint {

	@ApiMethod(name = "prochainDepart", httpMethod = ApiMethod.HttpMethod.GET, path = "prochainDepart")
	public Text getGareByCode(@Named("codeUIC") String codeUIC) throws Exception {
		InputStream reponse = TransilienRequest.prochainDepart(codeUIC);
		List<Train> train = XMLToTrain.parseTrain(reponse);
		String s = "";
		InputStreamReader fr = new InputStreamReader(reponse);
		int c = fr.read();
		// en UTF-8 le premier octet indique le codage
		c = fr.read();
		while (c != -1) {
			s += (char) c;
			c = fr.read();
		}
		
		Text a = new Text(s);
		return a;
	}
}
