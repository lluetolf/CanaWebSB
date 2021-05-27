package ch.canaweb.api.core.Document;

import ch.canaweb.api.core.Document.Document;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

public interface DocumentService {

    /**
     * Sample usage: curl $HOST:$PORT/document/1
     *
     * @param documentId
     * @return the document, if found, else null
     */
    @GetMapping(
            value    = "/document/{documentId}",
            produces = "application/json")
    Document getDocument(@PathVariable int documentId);

    /**
     * Sample usage: curl $HOST:$PORT/document
     *
     * @return all documents
     */
    @GetMapping(
            value    = "/document",
            produces = "application/json")
    Document getAllDocuments();
}
