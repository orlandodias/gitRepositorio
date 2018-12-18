package br.com.orlandodias.model;

public class Planeta {
	private String id;
	private String nome;
	private String terreno;
	private String clima;
	private String idSwapi;
	private Integer aparicoes;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getTerreno() {
		return terreno;
	}

	public void setTerreno(String terreno) {
		this.terreno = terreno;
	}

	public String getClima() {
		return clima;
	}

	public void setClima(String clima) {
		this.clima = clima;
	}

	public String getIdSwapi() {
		return idSwapi;
	}

	public void setIdSwapi(String idSwapi) {
		this.idSwapi = idSwapi;
	}

	public Integer getAparicoes() {
		return aparicoes;
	}

	public void setAparicoes(Integer aparicoes) {
		this.aparicoes = aparicoes;
	}

	@Override
	public String toString() {
		return "Planeta [id=" + id + ", nome=" + nome + ", terreno=" + terreno + ", clima=" + clima + ", idSwapi="
				+ idSwapi + ", aparicoes=" + aparicoes + "]";
	}

}