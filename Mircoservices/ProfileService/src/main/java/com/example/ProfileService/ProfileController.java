package com.example.ProfileService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@RestController
public class ProfileController {
    private final AtomicLong counter = new AtomicLong();
    private final Map<Long, Profile> profiles = new HashMap<>();
    private final Set<String> emails = new HashSet<>();
    private final RestTemplate restTemplate;


    private Logger logger = LoggerFactory.getLogger(ProfileController.class);
    public ProfileController(
            RestTemplateBuilder restTemplateBuilder) {
        restTemplate = restTemplateBuilder.build();
    }

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
        AuthServerUser auth_server_user = new AuthServerUser(new_id);//-------------------------------------------------
        auth_server_user.setId(new_id);
        auth_server_user.setMdp("test");
        restTemplate.put("http://localhost:8081/AS/users", auth_server_user);
        profiles.put(new_id, profile);
        emails.add(profile.getEmail());
        logger.info(String.format("Profile created : [%d] %s", new_id, profile.getEmail()));
        return profile;
    }

    @DeleteMapping("/PS/profiles/{id}")
    public void profile_delete(
            @PathVariable(value = "id") Long id,
            @RequestHeader(value = "X-Token") String token) throws InvalidTokenException {
        logger.trace(String.format("DELETE /PS/profiles/%d", id));
        checkTokenAgainstUser(token, id);
        Profile profile = profiles.get(id);
        emails.remove(profile.getEmail());
        profiles.remove(id);
        logger.info(String.format("Delete profile [%d] %s.", id, profile.getEmail()));
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
    @CrossOrigin
    public String profile_get_email(@PathVariable(value = "id") Long id){
        if (!profiles.containsKey((id)))
            throw new ProfileNotFoundException(id);
        return profiles.get(id).getEmail();
    }

    @PostMapping("/PS/login")
    @CrossOrigin
    public String login(
            @RequestParam(value = "email") String email,
            @RequestBody String password)
    {
        if(!email.contains(email))
            throw new RuntimeException();
        for(Profile p : profiles.values()){
            if(p.getEmail().equals(email)) {
                return restTemplate.postForObject(
                        String.format("%s/AS/users/%d/token", "http://localhost8080", p.getId()), password, String.class);
            }
        }
        throw new UnvalidEmalException(email);
    }


    //Throws an exception if the token is not valid or for a different user
    private void checkTokenAgainstUser(String token, Long user_id) throws InvalidTokenException {
        HttpHeaders header = new HttpHeaders();
        header.add("X-Token", token);
        HttpEntity<String> entity = new HttpEntity("", header);
        try {
            ResponseEntity<Long> response = restTemplate.exchange("http://localhost:8081/AS/token", HttpMethod.GET, entity, Long.class);
            Long token_user = response.getBody();
            if(!Objects.equals(token_user, user_id))
                throw new InvalidTokenException(user_id, token);
        }
        catch(HttpClientErrorException.Unauthorized ex){
            throw new InvalidTokenException(user_id, token);
        }
    }

}
