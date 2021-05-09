package com.firma.twitter4jorge.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import com.firma.twitter4jorge.entity.Hashtag;
import com.firma.twitter4jorge.entity.Tweet;
import com.firma.twitter4jorge.service.TwitterService;

import twitter4j.TwitterException;

/**
 * Clase TwitterController
 * 
 * Controlador REST
 * 
 * @version 1.0
 * @author Jorge López Capdevila
 * 
 *         09/05/2021
 */

@RestController
@RequestMapping("/twitter")
@CrossOrigin(origins = "*")
public class TwitterController {

	@Autowired
	TwitterService twitterService;

	@GetMapping({ "/start/{buscar}", "/start" })
	public ResponseEntity<String> start(@PathVariable(required = false) String buscar)
			throws IllegalStateException, TwitterException {

		if (buscar == null) {
			return new ResponseEntity<String>("Debes buscar una palabra", HttpStatus.NOT_FOUND);
		} else {
			twitterService.start(buscar);

			return new ResponseEntity<String>(
					"Se ha iniciado el buscador de Twitter, se van a buscar y guardar los tweets que contengan la palabra: "
							+ buscar,
					HttpStatus.OK);
		}

	}

	@GetMapping("/stop")
	public ResponseEntity<String> stop() {
		twitterService.stop();
		return new ResponseEntity<String>("Se ha parado el buscador", HttpStatus.OK);
	}

	@GetMapping("/list")
	public ResponseEntity<List<Tweet>> list() {
		return new ResponseEntity<List<Tweet>>(twitterService.list(), HttpStatus.OK);
	}

	@GetMapping({ "/validar/{id}", "/validar" })
	public ResponseEntity<String> validar(@PathVariable(required = false) Long id) {
		if (id != null) {
			Tweet tweet = twitterService.getById(id).get();
			tweet.setValidated(true);
			twitterService.validar(tweet);
			return new ResponseEntity<String>("Tweet con id: " + id + " validado", HttpStatus.OK);
		} else {
			return new ResponseEntity<String>("Debe introducir el id del twit a validar, use /list para ver todos los tweets", HttpStatus.OK);
		}
	}

	@GetMapping("/validados")
	public ResponseEntity<List<Tweet>> listValidated() {
		return new ResponseEntity<List<Tweet>>(twitterService.validados().get(), HttpStatus.OK);
	}

	/*
	 * El tipo Page funciona muy bien para adaptarlo al front, pero no lo voy a usar
	 * (si lo descomentas funciona[descomentar tambien en el service findHashtag]),
	 * 
	 * @GetMapping("/getHastag") public Page<Hashtag> getHashtag(
	 * 
	 * @RequestParam(defaultValue = "0") int page,
	 * 
	 * @RequestParam(defaultValue = "10") int size,
	 * 
	 * @RequestParam(defaultValue = "repeticiones") String order,
	 * 
	 * @RequestParam(defaultValue = "false") boolean asc){ Page<Hashtag> hashtags =
	 * twitterService.findHashtag(PageRequest.of(page, size, Sort.by(order)));
	 * return hashtags; }
	 */

	@GetMapping(value = { "/Hastags/{limit}", "/Hastags" })
	public ResponseEntity<List<Hashtag>> getHashtag(@PathVariable(required = false) Integer limit) {
		if (limit == null || limit == 0)
			limit = 10;
		return new ResponseEntity<List<Hashtag>>(twitterService.getHashtag(limit), HttpStatus.OK);
	}
}
