package ch.canaweb.api.core.Log;

import org.springframework.web.bind.annotation.*;

public interface LogService {

    @GetMapping(
            value    = "/log/{logId}",
            produces = "application/json")
    Log getLog(@PathVariable int logId);

    @GetMapping(
            value    = "/log",
            produces = "application/json")
    Log getAllLogs();

}
