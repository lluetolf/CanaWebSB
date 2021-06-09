package ch.canaweb.api.core.Document;

import org.springframework.web.bind.annotation.*;

public interface DocumentService {

    @GetMapping(
            value    = "/document",
            produces = "application/json")
    Document getAllDocuments();

    @GetMapping(
            value    = "/document/{documentId}",
            produces = "application/json")
    Document getDocument(@PathVariable int documentId);

}
