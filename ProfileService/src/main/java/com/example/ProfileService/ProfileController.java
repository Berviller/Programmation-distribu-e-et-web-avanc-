package com.example.ProfileService;

import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public class ProfileController {
    private final AtomicLong counter = new AtomicLong();
    private final Map<Long, Profile> profiles = new HashMap<>();

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
        //String email = profile.getEmail();
        long new_id = counter.incrementAndGet();
        profile.setId(new_id);
        profiles.put(new_id, profile);
        return profile;
    }

    @DeleteMapping("/PS/profiles/{id}")
    public void profile_delete(
            @PathVariable(value = "id") Long id) {
        Profile profile = profiles.get(id);
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
