package com.story.controller;

import com.story.model.Story;
import com.story.repository.StoryRepository;

import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
public class StoryController {

    final private StoryRepository storyRepository;

    public StoryController(StoryRepository storyRepository) {
        this.storyRepository = storyRepository;
    }

    @GetMapping("/story")
    public List<Story> index(){
        return storyRepository.findAll();
    }

    @GetMapping("/story/{id}")
    public Story show(@PathVariable String id){
        int storyId = Integer.parseInt(id);
        return storyRepository.findById(storyId).orElse(new Story());
    }

    @PostMapping("/story/search")
    public List<Story> search(@RequestBody Map<String, String> body){
        String searchTerm = body.get("text");
        return storyRepository.findByTitleOrBody(searchTerm, searchTerm);
    }

    @PostMapping("/story")
    public Story create(@RequestBody Map<String, String> story){
        String title = story.get("title");
        String body = story.get("body");
        String publishDate = story.get("publishDate");
        return storyRepository.save(new Story(title, body, publishDate));
    }

    @PutMapping("/story/{id}")
    public Story update(@PathVariable String id, @RequestBody Map<String, String> story){
        int storyId = Integer.parseInt(id);
        Story localStory = storyRepository.findById(storyId).orElse(new Story());
        localStory.setTitle(story.get("title"));
        localStory.setBody(story.get("body"));
        return storyRepository.save(localStory);
    }

    @DeleteMapping("story/{id}")
    public boolean delete(@PathVariable String id){
        int storyId = Integer.parseInt(id);
        storyRepository.deleteById(storyId);
        return true;
    }

}