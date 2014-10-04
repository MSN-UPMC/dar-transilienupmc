package com.upmc.transilien.v1.repository;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.Collection;
import java.util.List;

import com.googlecode.objectify.ObjectifyService;
import com.upmc.transilien.v1.model.EtatTrain;
import com.upmc.transilien.v1.model.Train;

public class TrainRepository {

	// public Trains(int i) {
	// Gare jussieu = new Gare("jussieu", "16544");
	// Gare chatelet = new Gare("chatelet", "67986");
	//
	// Train t1 = new Train(jussieu, chatelet, 16545, "VERI",
	// "26/09/2014 00:20", EtatTrain.RAS);
	// Train t2 = new Train(jussieu, chatelet, 19955, "VERI",
	// "26/09/2014 00:20", EtatTrain.SUP);
	// Train t3 = new Train(jussieu, chatelet, 30648, "VERI",
	// "26/09/2014 00:20", EtatTrain.RETARD);
	//
	// add(t1);
	// add(t2);
	// add(t3);
	// }
	//
	// public String toHTML() {
	// String result = "";
	// for (Train tmp : this) {
	// result += tmp + "<br>";
	// }
	// return result;
	// }

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
}
