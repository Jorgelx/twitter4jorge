package com.firma.twitter4jorge.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.firma.twitter4jorge.entity.Hashtag;

public interface HashtagRepository extends JpaRepository<Hashtag, Long> {
	Hashtag findByText(String text);
}
