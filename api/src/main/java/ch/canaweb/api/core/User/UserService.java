package ch.canaweb.api.core.User;

import org.springframework.web.bind.annotation.*;

public interface UserService {

    @GetMapping(
            value = "/user/{userId}",
            produces = "application/json")
    User getUser(@PathVariable int userId);

}
