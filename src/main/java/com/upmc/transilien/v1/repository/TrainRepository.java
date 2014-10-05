package com.upmc.transilien.v1.repository;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.io.InputStream;
import java.util.Collection;
import java.util.List;

import com.googlecode.objectify.ObjectifyService;
import com.upmc.transilien.parse.XMLToTrain;
import com.upmc.transilien.request.TransilienRequest;
import com.upmc.transilien.v1.model.EtatTrain;
import com.upmc.transilien.v1.model.Train;

public class TrainRepository {
	private static TrainRepository trains = null;

	static {
		ObjectifyService.register(Train.class);
	}

	private TrainRepository() {
	}

	public static synchronized TrainRepository getInstance() {
		if (null == trains) {
			trains = new TrainRepository();
		}
		return trains;
	}

	public Collection<Train> findTrains() {
		List<Train> trains = ofy().load().type(Train.class).list();
		return trains;
	}

	public Collection<Train> findTrainsByDepart(int depart) {
		List<Train> trains = ofy().load().type(Train.class).filter("depart = ", depart).list();
		return trains;
	}

	public Collection<Train> findTrainsByTerminus(int terminus) {
		List<Train> trains = ofy().load().type(Train.class).filter("terminus = ", terminus).list();
		return trains;
	}

	public Collection<Train> findTrainsByNumero(int numero) {
		List<Train> trains = ofy().load().type(Train.class).filter("numero = ", numero).list();
		return trains;
	}

	public Collection<Train> findTrainsByMission(String codeMission) {
		List<Train> trains = ofy().load().type(Train.class).filter("codeMission = ", codeMission).list();
		return trains;
	}

	public Collection<Train> findTrainsByEtat(EtatTrain etat) {
		List<Train> trains = ofy().load().type(Train.class).filter("etat = ", etat).list();
		return trains;
	}

	public Train create(Train train) {
		if (ofy().load().type(Train.class).filter("numero =", train.getNumero()).list().isEmpty())
			ofy().save().entity(train).now();
		else
			throw new Error("Un train possède déjà ce numéro.");
		return train;
	}

	// TODO voir si on peut modifier un train et si oui quelles propriétés
	// public Train update(Todo editedTrain) {
	// if (editedTrain.getId() == null) {
	// return null;
	// }
	//
	// Train train = ofy().load()
	// .key(Key.create(Train.class, editedTrain.getId())).now();
	// train.setCompleted(editedTrain.isCompleted());
	// train.setTitle(editedTrain.getTitle());
	// ofy().save().entity(train).now();
	//
	// return train;
	// }

	public void remove(Long id) {
		if (id == null) {
			return;
		}
		ofy().delete().type(Train.class).id(id).now();
	}

	public Collection<Train> prochainDepart(String codeUIC) throws Exception {
		InputStream reponse = TransilienRequest.prochainDepart(codeUIC);
		List<Train> trains = XMLToTrain.parseTrain(reponse);

		for (Train t : trains) {
			ofy().save().entities(t).now();
		}
		return trains;
	}
}
