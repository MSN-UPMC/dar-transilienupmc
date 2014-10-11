package com.upmc.transilien.v1.repository;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.Collection;
import java.util.List;

import com.googlecode.objectify.ObjectifyService;
import com.upmc.transilien.v1.model.Gare;
import com.upmc.transilien.v1.model.Train;

/**
 * Répertorie les trains<br>
 * <b>Singleton</b>
 * 
 * @author Kevin Coquart && Mag-Stellon Nadarajah
 *
 */
public class TrainRepository {
	private static TrainRepository trains = null;

	static {
		ObjectifyService.register(Train.class);
		ObjectifyService.register(Gare.class);
	}

	private TrainRepository() {
	}

	/**
	 * @return le singleton
	 */
	public static synchronized TrainRepository getInstance() {
		if (null == trains) {
			trains = new TrainRepository();
		}
		return trains;
	}

	/**
	 * @return la liste des trains existant
	 */
	public Collection<Train> findTrains() {
		List<Train> trains = ofy().load().type(Train.class).list();
		return trains;
	}

	// TODO sachant que le train bouge, les 2 fonctions suivantes n'ont pas trop d'interet
	// Par contre réussir à associer le train à une ligne serait plus intéressant pour les stats.
	/**
	 * Recherche un train par gare de départ
	 * 
	 * @param depart
	 *            le codeUIC de la gare de départ
	 * @return les trains partant de la gare
	 */
	public Collection<Train> findTrainsByDepart(int depart) {
		List<Train> trains = ofy().load().type(Train.class).filter("depart = ", depart).list();
		return trains;
	}

	// TODO voir au dessus
	/**
	 * Recherche un train par son terminus
	 * 
	 * @param terminus
	 *            codeUIC du terminus
	 * @return
	 */
	public Collection<Train> findTrainsByTerminus(int terminus) {
		List<Train> trains = ofy().load().type(Train.class).filter("terminus = ", terminus).list();
		return trains;
	}

	/**
	 * Recherche un train par son numéro
	 * 
	 * @param numero
	 *            son numéro
	 * @return le train correspondant
	 */
	public Train findTrainsByNumero(int numero) {
		List<Train> trains = ofy().load().type(Train.class).filter("numero = ", numero).list();
		return (trains.isEmpty() ? null : trains.get(0));
	}

	/**
	 * Recherche des trains par leur code Mission
	 * 
	 * @param codeMission
	 *            le code Mission à chercher
	 * @return les trains qui corresponde
	 */
	public Collection<Train> findTrainsByMission(String codeMission) {
		List<Train> trains = ofy().load().type(Train.class).filter("codeMission = ", codeMission).list();
		return trains;
	}

	/**
	 * Recherche les trains par leurs états
	 * 
	 * @param etat
	 *            l'état à rechercher
	 * @return la liste des trains correspondant
	 */
	public Collection<Train> findTrainsByEtat(String etat) {
		List<Train> trains = ofy().load().type(Train.class).filter("etat = ", etat).list();
		return trains;
	}

	/**
	 * Créer un train dans le système de persistance.
	 * 
	 * @param train
	 *            le train à sauvegarder
	 * @return le train
	 */
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

	// TODO a voir, veut-on supprimer des trains ? je ne pense pas
	// public void remove(Long id) {
	// if (id == null) {
	// return;
	// }
	// ofy().delete().type(Train.class).id(id).now();
	// }
}
