package com.story.controller;

import com.story.exception.ResourceNotFoundException;
import com.story.model.Story;
import com.story.repository.StoryRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class StoryController {

    @Autowired
    private StoryRepository storyRepository;


    @RequestMapping(value = "/story", method = RequestMethod.GET)
    public List<Story> getAllStories() {
        return storyRepository.findAll();
    }


    @RequestMapping(value = "/story/{id}", method = RequestMethod.GET)
    public ResponseEntity<Story> getStoryById(@PathVariable(value = "id") Integer storyId) throws ResourceNotFoundException {
        Story story = storyRepository.findById(storyId)
                .orElseThrow(() -> new ResourceNotFoundException("Story not found with id " + storyId));
        return ResponseEntity.ok().body(story);
    }


    @RequestMapping(value = "/story", method = RequestMethod.POST)
    public ResponseEntity<Story> createStory(@Valid @RequestBody Story story) {

        Story newStory = storyRepository.save(story);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(newStory.getId()).toUri();
        return ResponseEntity.created(location).body(newStory);
    }


    @RequestMapping(value = "/story/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Story> updateStory(@PathVariable(value = "id") Integer storyId,
                                             @Valid @RequestBody Story storyDetails) throws ResourceNotFoundException {
        Story story = storyRepository.findById(storyId)
                .orElseThrow(() -> new ResourceNotFoundException("Story not found with id " + storyId));

        story.setTitle(storyDetails.getTitle());
        story.setBody(storyDetails.getBody());
        story.setPublishDate(storyDetails.getPublishDate());
        final Story updatedStory = storyRepository.save(story);
        return ResponseEntity.ok(updatedStory);
    }


    @RequestMapping(value = "/story/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Map> deleteStory(@PathVariable(value = "id") Integer storyId) throws ResourceNotFoundException {

        Story story = storyRepository.findById(storyId)
                .orElseThrow(() -> new ResourceNotFoundException("Story not found with id " + storyId));
        storyRepository.delete(story);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return ResponseEntity.ok(response);
    }

}