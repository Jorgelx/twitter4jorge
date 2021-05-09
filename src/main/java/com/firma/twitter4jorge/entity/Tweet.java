package com.firma.twitter4jorge.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;

/**
 * Clase Tweet
 * 
 * Tweet JPA Entity
 * 
 * @version 1.0
 * @author Jorge López Capdevila
 * 
 *         09/05/2021
 */
@Entity
public class Tweet {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private String usuario;

	@Lob
	private String texto;
	// En localización vamos a usar el idioma de momento, ya que devuelve siempre
	// NULL los metodos getGeoLocation() y getPlace()
	private String localizacion;
	private boolean isValidated = false;

	public Tweet() {
	}

	public Tweet(String usuario, String texto, String localizacion, boolean isValidated) {
		this.usuario = usuario;
		this.texto = texto;
		this.localizacion = localizacion;
		this.isValidated = isValidated;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getTexto() {
		return texto;
	}

	public void setTexto(String texto) {
		this.texto = texto;
	}

	public String getLocalizacion() {
		return localizacion;
	}

	public void setLocalizacion(String localizacion) {
		this.localizacion = localizacion;
	}

	public boolean isValidated() {
		return isValidated;
	}

	public void setValidated(boolean isValidated) {
		this.isValidated = isValidated;
	}

	@Override
	public String toString() {
		return "Tweet [id=" + id + ", usuario=" + usuario + ", texto=" + texto + ", localizacion=" + localizacion
				+ ", isValidated=" + isValidated + "]";
	}

}
