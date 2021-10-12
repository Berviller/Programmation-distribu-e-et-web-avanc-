package com.example.ProfileService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@RestController
public class ProfileController {
    private final AtomicLong counter = new AtomicLong();
    private final Map<Long, Profile> profiles = new HashMap<>();
    private final Set<String> emails = new HashSet<>();

    private Logger logger = LoggerFactory.getLogger(ProfileController.class);

    /*@ResponseBody
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

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationException(
            MethodArgumentNotValidException ex){
        Map<String, String> errors = new HashMap<>();
        for (ObjectError error : ex.getBindingResult().getAllErrors()){
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        }
        return errors;
    }*/

    @GetMapping("/PS/profiles")
    public Collection<Profile> profiles(){
        logger.trace("GET /PS/profiles");
        return profiles.values();
    }
    //GET /PS/profiles/{id}
    //GET /PS/profiles/{id}/name
    //PUT /PS/profiles/{id}/name
    // + email, description, ...
    @PutMapping("/PS/profiles")
    public Profile profiles_put(@RequestBody @Valid Profile profile){
        logger.trace("PUT /PS/profiles");
        if (emails.contains(profile.getEmail()))
            throw new EmailInUseException(profile.getEmail());
        long new_id = counter.incrementAndGet();
        profile.setId(new_id);
        profiles.put(new_id, profile);
        emails.add(profile.getEmail());
        logger.info(String.format("Profile created : [%d] %s", new_id, profile.getEmail()));
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

    @GetMapping("/PS/profiles/{id}/name")
    public String profile_get_name(@PathVariable(value = "id") Long id){
        if (!profiles.containsKey((id)))
            throw new ProfileNotFoundException(id);
        return profiles.get(id).getName();
    }

    @GetMapping("/PS/profiles/{id}/email")
    public String profile_get_email(@PathVariable(value = "id") Long id){
        if (!profiles.containsKey((id)))
            throw new ProfileNotFoundException(id);
        return profiles.get(id).getEmail();
    }
}
