package com.firma.twitter4jorge.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;

/**
 * Clase Hashtag
 * 
 * Hashtag JPA Entity
 * 
 * @version 1.0
 * @author Jorge López Capdevila
 * 
 *         09/05/2021
 */
@Entity
public class Hashtag {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	@Lob
	private String text;
	private long repeticiones;

	public Hashtag(String text, long repeticiones) {
		this.text = text;
		this.repeticiones = repeticiones;
	}

	public Hashtag() {
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public long getRepeticiones() {
		return repeticiones;
	}

	public void setRepeticiones(long repeticiones) {
		this.repeticiones = repeticiones;
	}

}
