package com.story.controller;

import com.story.exception.ResourceNotFoundException;
import com.story.model.Story;
import com.story.repository.StoryRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;


import javax.validation.Valid;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
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



//    final private StoryRepository storyRepository;
//
//    public StoryController(StoryRepository storyRepository) {
//        this.storyRepository = storyRepository;
//    }
//
//    @GetMapping("/story")
//    public List<Story> index(){
//        return storyRepository.findAll();
//    }
//
//    @GetMapping("/story/{id}")
//    public Story show(@PathVariable String id){
//        int storyId = Integer.parseInt(id);
//        return storyRepository.findById(storyId).orElse(new Story());
//    }
//
//    @PostMapping("/story/search")
//    public List<Story> search(@RequestBody Map<String, String> body){
//        String searchTerm = body.get("text");
//        return storyRepository.findByTitleOrBody(searchTerm, searchTerm);
//    }
//
//    @PostMapping("/story")
//    public Story create(@RequestBody Map<String, String> story){
//        String title = story.get("title");
//        String body = story.get("body");
//        String publishDate = story.get("publishDate");
//        return storyRepository.save(new Story(title, body, publishDate));
//    }
//
//    @PutMapping("/story/{id}")
//    public Story update(@PathVariable String id, @RequestBody Map<String, String> story){
//        int storyId = Integer.parseInt(id);
//        Story localStory = storyRepository.findById(storyId).orElse(new Story());
//        localStory.setTitle(story.get("title"));
//        localStory.setBody(story.get("body"));
//        return storyRepository.save(localStory);
//    }
//
//    @DeleteMapping("story/{id}")
//    public boolean delete(@PathVariable String id){
//        int storyId = Integer.parseInt(id);
//        storyRepository.deleteById(storyId);
//        return true;
//    }

}