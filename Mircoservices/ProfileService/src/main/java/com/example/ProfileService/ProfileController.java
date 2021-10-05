package com.example.ProfileService;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@RestControllerAdvice
public class ProfileController {
    private final AtomicLong counter = new AtomicLong();
    private final Map<Long, Profile> profiles = new HashMap<>();
    private final Set<String> emails = new HashSet<>();

    @ResponseBody
    @ExceptionHandler(ProfileNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String profileNotFoundHandler(ProfileNotFoundException ex){
        return ex.getMessage();
    }

    @ResponseBody
    @ExceptionHandler(EmailInUseException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String profileNotFoundHandler(EmailInUseException ex){
        return ex.getMessage();
    }


    @GetMapping("/PS/profiles")
    public Collection<Profile> profiles(){
        return profiles.values();
    }
    //GET /PS/profiles/{id}
    //GET /PS/profiles/{id}/name
    //PUT /PS/profiles/{id}/name
    // + email, description, ...
    @PutMapping("/PS/profiles")
    public Profile profiles_put(@RequestBody @Valid Profile profile){
        if (emails.contains(profile.getEmail()))
            throw new EmailInUseException(profile.getEmail());
        long new_id = counter.incrementAndGet();
        profile.setId(new_id);
        profiles.put(new_id, profile);
        emails.add(profile.getEmail());
        return profile;
    }

    @DeleteMapping("/PS/profiles/{id}")
    public void profile_delete(
            @PathVariable(value = "id") Long id) {
        Profile profile = profiles.get(id);
        emails.remove(profile.getEmail());
        profiles.remove(id);
    }

    @PutMapping("/PS/profiles/{id}/name")
    public void update_name(
            @PathVariable(value = "id") Long id,
            @RequestBody String name)
    {
        Profile profile = profiles.get(id);
        profile.setName(name);
    }
}
