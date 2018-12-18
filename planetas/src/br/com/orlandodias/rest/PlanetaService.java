package br.com.orlandodias.rest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.mongodb.MongoClient;

import br.com.orlandodias.DAO.PlanetaDAO;
import br.com.orlandodias.exception.ObjectNotFoundException;
import br.com.orlandodias.model.Planeta;

@Path("/planetas")
public class PlanetaService {
	public static final String CHARSET_UTF8 = ";charset=utf-8";
	private PlanetaDAO planetaDAO;

	@PostConstruct
	private void init() {
		MongoClient mongo = new MongoClient("localhost", 27017);
		planetaDAO = new PlanetaDAO(mongo);
	}

	@GET
	@Path("/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Planeta getPlaneta(@PathParam("id") String idSwapi) {
		Planeta planeta = new Planeta();
		planeta.setIdSwapi(idSwapi);
		@SuppressWarnings("unused")
		String msg = "";
		try {
			planeta = planetaDAO.readPlaneta(idSwapi);
			try {
				planeta.setAparicoes(this.getAparicoesFromSwapi(idSwapi));
			} catch (Exception exc) {
				planeta.setAparicoes(0);
			}
		} catch (ObjectNotFoundException e) {
			return null;
		}
		return planeta;
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON + CHARSET_UTF8)
	@Produces(MediaType.TEXT_PLAIN)
	public String InsertPlaneta(Planeta planeta) {
		String msg = "";
		try {
			planeta.setAparicoes(this.getAparicoesFromSwapi(planeta.getIdSwapi()));
			planeta = planetaDAO.createPlaneta(planeta);
			return String.valueOf(planeta.getId());
		} catch (Exception e) {
			msg = e.getMessage();
		}
		return msg;
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Planeta> listPlanetas() {
		// System.out.println("---> PlanetasService.listPlanetas");
		List<Planeta> listPlanetas = null;
		try {
			listPlanetas = planetaDAO.readAllPlaneta();
		} catch (Exception e) {
		}
		return listPlanetas;
	}

	@PUT
	@Path("/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	public String updatePlaneta(Planeta planeta, @PathParam("id") String idSwapi) {
		String msg = "";
		try {
			Planeta myP = planetaDAO.readPlaneta(idSwapi);
			planeta.setId(myP.getId());
			planeta.setAparicoes(this.getAparicoesFromSwapi(idSwapi));
			//System.out.println("planeta::::" + planeta.toString());
			planetaDAO.updatePlaneta(planeta);
			msg = "Atualização bem Sucedida";
		} catch (ObjectNotFoundException e) {
			msg = "Erro:" + e.getMessage();
		}
		return msg;
	}

	@DELETE
	@Path("/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	public String deletePlaneta(@PathParam("id") String idSwapi) {
		//System.out.println("---> PlanetasService.deletePlaneta: " + idSwapi);
		Planeta planeta = new Planeta();
		String msg = "";
		try {
			Planeta myP = planetaDAO.readPlaneta(idSwapi);
			planeta.setId(myP.getId());
			planeta.setIdSwapi(idSwapi);
			planetaDAO.deletePlaneta(planeta);
			//System.out.println("---> PlanetasService.deletePlaneta: apagado");
		} catch (ObjectNotFoundException e1) {
			msg = "Não localizado";
			//System.out.println("---> PlanetasService.deletePlaneta: msg:" + msg);
		}
		return msg;
	}

	public Integer getAparicoesFromSwapi(String idSwapi) {
		// para simplificar, caso apresente erro, devolve zero filmes
		Integer qtFilmes = 0;
		try {
			URL url = new URL("https://swapi.co/api/planets/" + idSwapi);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestProperty("User-Agent",
					"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");

			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/json");

			if (conn.getResponseCode() != 200) {
				qtFilmes = 0;
			} else {
				BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

				String output;
				StringBuffer sbJson = new StringBuffer();
				while ((output = br.readLine()) != null) {
					sbJson.append(output);
				}
				try {
					JSONObject obj = new JSONObject(sbJson.toString());
					JSONArray filmes = obj.getJSONArray("films");
					qtFilmes = filmes.length();
				} catch (JSONException e) {
					qtFilmes = 0;
				}
			}
			conn.disconnect();
			return qtFilmes;
		} catch (MalformedURLException e) {
			return 0;
		} catch (IOException e) {
			return 0;
		}
	}
}