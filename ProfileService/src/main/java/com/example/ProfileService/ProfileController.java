package com.example.ProfileService;

import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@RestController
public class ProfileController {
    private final AtomicLong counter = new AtomicLong();
    private final Map<Long, Profile> profiles = new HashMap<>();
    private final Set<String> emails = new HashSet<>();

    @GetMapping("/PS/profiles")
    public Collection<Profile> profiles(){
        return profiles.values();
    }
    //GET /PS/profiles/{id}
    //GET /PS/profiles/{id}/name
    //PUT /PS/profiles/{id}/name
    // + email, description, ...
    @PutMapping("/PS/profiles")
    public Profile profiles_put(@RequestBody Profile profile){
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

    @GetMapping("/PS/profiles/{id}/name")
    public void update_name(
            @PathVariable(value = "id") Long id,
            @RequestBody String name)
    {
        Profile profile = profiles.get(id);
        profile.setName(name);
    }
}
