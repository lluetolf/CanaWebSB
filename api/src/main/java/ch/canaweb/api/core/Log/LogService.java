package ch.canaweb.api.core.Log;

import ch.canaweb.api.core.Log.Log;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

public interface LogService {

    /**
     * Sample usage: curl $HOST:$PORT/log/1
     *
     * @param logId
     * @return the log, if found, else null
     */
    @GetMapping(
            value    = "/log/{logId}",
            produces = "application/json")
    Log getLog(@PathVariable int logId);

    /**
     * Sample usage: curl $HOST:$PORT/log
     *
     * @return all logs
     */
    @GetMapping(
            value    = "/log",
            produces = "application/json")
    Log getAllLogs();
}
