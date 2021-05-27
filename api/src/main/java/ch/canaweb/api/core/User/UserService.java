package ch.canaweb.api.core.User;

import ch.canaweb.api.core.User.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

public interface UserService {

    /**
     * Sample usage: curl $HOST:$PORT/user/1
     *
     * @param userId
     * @return the user, if found, else null
     */
    @GetMapping(
            value = "/user/{userId}",
            produces = "application/json")
    User getUser(@PathVariable int userId);
}
