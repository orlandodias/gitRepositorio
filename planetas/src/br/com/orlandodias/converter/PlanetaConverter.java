package br.com.orlandodias.converter;

import org.bson.types.ObjectId;

import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DBObject;

import br.com.orlandodias.model.Planeta;

public class PlanetaConverter {

	public static DBObject toDBObject(Planeta p) {
		BasicDBObjectBuilder builder = BasicDBObjectBuilder.start()
				.append("nome", p.getNome())
				.append("terreno", p.getTerreno())
				.append("clima", p.getClima())
				.append("aparicoes", p.getAparicoes())
				.append("idSwapi", p.getIdSwapi());
		if (p.getId() != null)
			builder = builder.append("_id", new ObjectId(p.getId()));
		return builder.get();
	}

	public static Planeta toPlaneta(DBObject doc) {
		Planeta p = new Planeta();
		// System.out.println("PlanetaConverter.toPlaneta. entrei");
		// System.out.println("PlanetaConverter.toPlaneta. nome:" + (String)
		// doc.get("nome"));
		p.setNome((String) doc.get("nome"));
		p.setTerreno((String) doc.get("terreno"));
		p.setClima((String) doc.get("clima"));
		p.setIdSwapi((String) doc.get("idSwapi"));
		p.setAparicoes((Integer) doc.get("aparicoes"));
		ObjectId id = (ObjectId) doc.get("_id");
		p.setId(id.toString());
		// System.out.println("PlanetaConverter.toPlaneta. p:" + p.toString());
		return p;
	}

}
