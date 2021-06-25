package ch.canaweb.util.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class DupliateException extends RuntimeException {
    public DupliateException(String msg){
        super(msg);
    }
}