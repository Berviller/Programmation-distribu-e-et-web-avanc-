package com.microservice2.demo;

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
public class Controler {
    private Logger logger = LoggerFactory.getLogger(Controler.class);
    private final Map<Long, User> users = new HashMap<>();
    private final Map<Long, Token> tokens = new HashMap<>();

    @GetMapping("/AS/users")
    public Collection<User> users() {
        logger.trace("GET /AS/users");
        return users.values();
    }

    @PutMapping("/AS/users")
    public User profiles_put(@RequestBody @Valid User user){
        logger.trace("PUT /AS/users");
        if (users.containsKey(user.getId())){
            throw new UserExistException(user.getId());
        }
        users.put(user.getId(), user);
        logger.info(String.format("Profile created : [%d]", user.getId()));
        return user;
    }

    /*@DeleteMapping("/AS/users/{id}"){
        public void profile_delete(
                @PathVariable(value = "id") Long id) {
            logger.trace(String.format("DELETE /PS/profiles/%d", id));
            User user = users.get(User.getId());
            users.remove(user.getId());
            logger.info(String.format("Delete profile [%d].", user.getId()));
    }*/

    @PutMapping("/AS/users/{id}/token")
            public Token token_put(@RequestBody @Valid String mdp, @PathVariable(value = "id") Long id){
            logger.trace("PUT /AS/users/{id}/token");
            User user = users.get(id);
            if (!user.getMdp().equals(mdp)){
                throw new PasswordExistException(mdp);
            }
            if (!users.containsKey(id)){
                throw new MissingIdException(id);
            }
            Token tk = new Token();
            tokens.put(user.getId(), tk);
            logger.info(String.format("Token created : [%s]", tk.getToken()));
            return tk;
        }

    @GetMapping("/AS/token")
        public long checktoken(@RequestHeader(value = "X-Token") String XToken){
        for (Map.Entry<Long, Token> e : tokens.entrySet()) {
            if (XToken.equals(e.getValue().getToken())){
                return e.getKey();
            }
        }
        throw new MissingTokenException(XToken);
    }
    @DeleteMapping("/AS/users/{id}/token")
    public void token_delete(
            @PathVariable(value = "id") Long id) {
        
    }


}
