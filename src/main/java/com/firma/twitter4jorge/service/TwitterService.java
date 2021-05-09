package com.firma.twitter4jorge.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.firma.twitter4jorge.entity.Hashtag;
import com.firma.twitter4jorge.entity.Tweet;

import twitter4j.TwitterException;

@Service
public interface TwitterService {
	public void start(String buscar) throws IllegalStateException, TwitterException;

	public List<Tweet> list();

	public Optional<Tweet> getById(long id);

	public void validar(Tweet tweet);

	public Optional<List<Tweet>> validados();

	public void stop();

	public List<Hashtag> getHashtag(Integer limit);
	
	// public Page<Hashtag> findHashtag(Pageable pageable)
}
