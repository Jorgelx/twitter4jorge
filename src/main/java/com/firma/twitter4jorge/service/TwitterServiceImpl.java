package com.firma.twitter4jorge.service;

import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.firma.twitter4jorge.Twitter4jorgeApplication;
import com.firma.twitter4jorge.entity.Hashtag;
import com.firma.twitter4jorge.entity.Tweet;
import com.firma.twitter4jorge.repository.HashtagRepository;
import com.firma.twitter4jorge.repository.TweetRepository;

import twitter4j.FilterQuery;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.TwitterException;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.conf.ConfigurationBuilder;

/**
 * Clase TwitterServiceImpl
 * 
 * Servicio de Twitter
 * 
 * @version 1.0
 * @author Jorge López Capdevila
 * 
 *         09/05/2021
 */

@Service
@Transactional
public class TwitterServiceImpl implements TwitterService {

	@Autowired
	TweetRepository tweetRepository;
	@Autowired
	HashtagRepository hashtagRepository;
	TwitterStream twitterStream;
	@PersistenceContext
	private EntityManager entityManager;

	ConfigurationBuilder cb;
	private static final Logger logger = LoggerFactory.getLogger(Twitter4jorgeApplication.class);

	public void start(String buscar) throws IllegalStateException, TwitterException {
		logger.info("Iniciando configuración de Twitter4J");
		cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true).setOAuthConsumerKey("Nh2yH3NKUv9hlsqwrLE9FDimq")
				.setOAuthConsumerSecret("q3yx2Q9E77edcXKWAUla3d2ZgetjIgTgQiKNdcLxSDVIDE7ToE")
				.setOAuthAccessToken("1304277912422678531-1L9aW5Rdlvs6s84igzgjvfxakzRDsy")
				.setOAuthAccessTokenSecret("heDj5wMG6CnNfif6TU3pGTSH2Huokg62kEqdSlxkU6G69");
		twitterStream = new TwitterStreamFactory(cb.build()).getInstance();

		logger.info("Twitter4J arrancado: ID: " + twitterStream.getId());

		FilterQuery filterQuery = new FilterQuery();

		String buscarArray[] = { buscar };
		String lenguages[] = { "es", "fr", "it" };

		filterQuery.track(buscarArray);
		filterQuery.language(lenguages);

		twitterStream.addListener(listener);

		if (buscarArray.length == 0) {
			twitterStream.sample();
		} else {
			twitterStream.filter(filterQuery);
		}

	}

	StatusListener listener = new StatusListener() {

		public void onStatus(Status status) {
			StringBuilder str = new StringBuilder();

			/*
			 * Log para localizar los tweets recibidos y probar twitter4j
			 * 
			 * En localización vamos a usar el idioma de momento, ya que devuelve siempre
			 * NULL los metodos getGeoLocation() y getPlace()
			 */
			str.append("\n");
			str.append("------------------------------------------------------");
			str.append("\n");
			str.append("Usuario: " + status.getUser().getScreenName());
			str.append("\n");
			str.append("\n");
			str.append("Texto: " + status.getText());
			str.append("\n");
			str.append("\n");
			str.append("Seguidores: " + status.getUser().getFollowersCount());
			str.append("\n");
			str.append("\n");
			str.append("Idioma: " + status.getLang());
			str.append("\n");
			str.append("\n");
			str.append("------------------------------------------------------");
			str.append("\n");

			Tweet tweet = new Tweet(status.getUser().getScreenName(), status.getText(), status.getLang(), false);

			logger.info(str.toString());

			if (status.getUser().getFollowersCount() > 1500) {
				save(tweet);
				logger.info("Tweet guardado");
			} else {
				logger.info("Este tweet no cumple los requisitos para ser guardado");
			}

			/*
			 * No tengo una cuenta con mas de 1500 seguidores, para guardar los Hastags cuyo
			 * twittero tenga mas de 1500 seguidores,simplemente añadirlo al if de arriba y
			 * comentarlo.
			 */
			String contenidoTweet = status.getText();
			contarHashtag(contenidoTweet);

		}

		@Override
		public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
			logger.warn("Track Limitation Notices: " + numberOfLimitedStatuses);
		}

		@Override
		public void onStallWarning(StallWarning warning) {
			logger.warn("Iniciando StallWarning" + warning);
		}

		@Override
		public void onException(Exception ex) {
			ex.printStackTrace();
		}

		@Override
		public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
			logger.warn("onDeletionNotice: " + statusDeletionNotice);

		}

		@Override
		public void onScrubGeo(long userId, long upToStatusId) {
			logger.warn("DonScrubGeo: userId=" + userId + " upToStatusId=" + upToStatusId);

		}
	};

	public void save(Tweet tweet) {
		tweetRepository.saveAndFlush(tweet);
	}

	public List<Tweet> list() {
		return tweetRepository.findAll();
	}

	public Optional<Tweet> getById(long id) {
		return tweetRepository.findById(id);
	}

	public void validar(Tweet tweet) {
		tweetRepository.save(tweet);
	}

	public Optional<List<Tweet>> validados() {
		return tweetRepository.findByIsValidated(true);
	}

	public void stop() {
		twitterStream.removeListener(listener);
		twitterStream.cleanUp();
		twitterStream.shutdown();
		logger.info("Se ha finalizado la busqueda en Twitter");
	}

	public void contarHashtag(String twit) {
		if (twit.contains("#")) {

			Pattern pattern = Pattern.compile("(#[A-Za-z0-9-_]+)");
			// "#(.*?) "
			Matcher matcher = pattern.matcher(twit);

			while (matcher.find()) {

				logger.info("Hashtag encontrado: " + matcher.group(1));

				if (hashtagRepository.findByText(matcher.group(1)) != null) {
					Hashtag hashtag = hashtagRepository.findByText(matcher.group(1));
					long repeticiones = hashtag.getRepeticiones() + 1;
					hashtag.setRepeticiones(repeticiones);
					hashtagRepository.save(hashtag);
				} else {
					Hashtag hashtag = new Hashtag(matcher.group(1), 1);
					hashtagRepository.save(hashtag);
				}

			}
		}
	}

	/*
	 * Descomentar para implementar buscar Hashtags pageable public Page<Hashtag>
	 * findHashtag(Pageable pageable) { return hashtagRepository.findAll(pageable);
	 * }
	 */

	public List<Hashtag> getHashtag(Integer limit) {
		@SuppressWarnings("unchecked")
		List<Hashtag> listHashtag = entityManager
				.createNativeQuery(
						"Select h.text , h.repeticiones from Hashtag h order by h.repeticiones desc limit 0 ," + limit)
				.getResultList();
		return listHashtag;

	}

}