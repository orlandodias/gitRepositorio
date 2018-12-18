package br.com.orlandodias.DAO;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

import br.com.orlandodias.converter.PlanetaConverter;
import br.com.orlandodias.exception.ObjectNotFoundException;
import br.com.orlandodias.model.Planeta;

public class PlanetaDAO {

	private DBCollection col;

	@SuppressWarnings("deprecation")
	public PlanetaDAO(MongoClient mongo) {
		this.col = mongo.getDB("starwars").getCollection("planets");
	}

	public Planeta createPlaneta(Planeta p) {
		//System.out.println("PlanetaDAO.createPlaneta. p:" + p.toString());
		p.setId(new ObjectId().toString());
		DBObject doc = PlanetaConverter.toDBObject(p);
		this.col.insert(doc);
		ObjectId id = (ObjectId) doc.get("_id");
		p.setId(id.toString());
		return p;
	}

	public void updatePlaneta(Planeta p) {
		//System.out.println("PlanetaDAO.updatePlaneta. p:" + p.toString());
		BasicDBObject whereQuery = new BasicDBObject();
		whereQuery.put("idSwapi", p.getIdSwapi());
		this.col.update(whereQuery, PlanetaConverter.toDBObject(p));
	}

	public List<Planeta> readAllPlaneta() {
		//System.out.println("PlanetaDAO.readAllPlaneta");
		List<Planeta> data = new ArrayList<Planeta>();
		DBCursor cursor = col.find();
		while (cursor.hasNext()) {
			DBObject doc = cursor.next();
			Planeta p = PlanetaConverter.toPlaneta(doc);
			data.add(p);
		}
		return data;
	}

	public void deletePlaneta(Planeta p) {
		//System.out.println("PlanetaDAO.deletePlaneta. p:" + p.toString());
		BasicDBObject whereQuery = new BasicDBObject();
		whereQuery.put("idSwapi", p.getIdSwapi());
		this.col.remove(whereQuery);
	}

	public Planeta readPlaneta(String idSwapi) throws ObjectNotFoundException {
		//System.out.println("PlanetaDAO.readPlaneta. idSwapi:" + idSwapi);
		BasicDBObject whereQuery = new BasicDBObject();
		whereQuery.put("idSwapi", idSwapi);

		//System.out.println("PlanetaDAO.vou executar query");
		DBObject data = this.col.findOne(whereQuery);
		if (data == null) {
			throw new ObjectNotFoundException();
		}
		//System.out.println("PlanetaDAO.readPlaneta.executei.");
		// System.out.println("PlanetaDAO.readPlaneta.data: " + data);
		return PlanetaConverter.toPlaneta(data);
	}
}