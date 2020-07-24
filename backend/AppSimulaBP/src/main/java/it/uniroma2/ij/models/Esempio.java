package it.uniroma2.ij.models;

public class Esempio {
	
	String nome;
	String descrizione;
	String pathModello;
	int id;
	
	public Esempio(String nome, String descrizione, String pathModello, int id) {
		this.nome = nome;
		this.descrizione = descrizione;
		this.pathModello = pathModello;
		this.id = id;
	}
	
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getDescrizione() {
		return descrizione;
	}
	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}
	public String getPathModello() {
		return pathModello;
	}
	public void setPathModello(String pathModello) {
		this.pathModello = pathModello;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}

}
