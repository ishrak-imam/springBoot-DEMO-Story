package com.story.repository;

import com.story.model.Story;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StoryRepository extends JpaRepository<Story, Integer> {

    List<Story> findByTitleOrBody(String text, String textAgain);
}