package com.upmc.transilien.v1.repository;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.Collection;
import java.util.List;

import com.googlecode.objectify.ObjectifyService;
import com.upmc.transilien.v1.model.Gare;

public class Gares {
	private static Gares gares = null;

	static {
		ObjectifyService.register(Gare.class);
	}

	private Gares() {
	}

	public static synchronized Gares getInstance() {
		if (null == gares) {
			gares = new Gares();
		}
		return gares;
	}

	public Collection<Gare> findGares() {
		List<Gare> gares = ofy().load().type(Gare.class).list();
		return gares;
	}

	public Gare findGareByCode(String codeUIC) {
		List<Gare> gares = ofy().load().type(Gare.class).filter("codeUIC =", codeUIC).list();
		return (gares.isEmpty() ? null : gares.get(0));
	}

	public Gare findGareByName(String name) {
		List<Gare> gares = ofy().load().type(Gare.class).filter("nom =", name).list();
		return (gares.isEmpty() ? null : gares.get(0));
	}

	public Gare create(Gare gare) {
		if (ofy().load().type(Gare.class).filter("codeUIC =", gare.getCodeUIC()).list().isEmpty())
			ofy().save().entity(gare).now();
		else
			throw new Error("Une gare possède déjà ce numéro UIC.");
		return gare;
	}

	// TODO voir si on peut modifier une gare et si oui quelles propriétés
	// public Gare update(Gare editedGare) {
	// if (editedGare.getId() == null) {
	// return null;
	// }
	//
	// Gare gare = ofy().load()
	// .key(Key.create(Gare.class, editedGare.getId())).now();
	// gare.setCompleted(editedGare.isCompleted());
	// gare.setTitle(editedGare.getTitle());
	// ofy().save().entity(gare).now();
	//
	// return gare;
	// }

	public void remove(Long id) {
		if (id == null) {
			return;
		}
		ofy().delete().type(Gare.class).id(id).now();
	}
}
