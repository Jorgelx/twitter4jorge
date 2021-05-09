package com.firma.twitter4jorge.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.firma.twitter4jorge.entity.Tweet;

public interface TweetRepository extends JpaRepository<Tweet, Long> {
	Optional<List<Tweet>> findByIsValidated(boolean validated);

}
