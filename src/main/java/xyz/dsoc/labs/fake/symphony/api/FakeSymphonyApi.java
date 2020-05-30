package xyz.dsoc.labs.fake.symphony.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import xyz.dsoc.labs.fake.symphony.domain.Message;
import xyz.dsoc.labs.fake.symphony.domain.Stream;
import xyz.dsoc.labs.fake.symphony.service.BadMessageException;
import xyz.dsoc.labs.fake.symphony.service.MessageService;
import xyz.dsoc.labs.fake.symphony.service.NoSuchStreamException;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * This is meant to be a mock service for https://developers.symphony.com/restapi/reference#messages-v4
 *
 * @author sih
 */
@RestController
@RequestMapping(value = "/streams")
public class FakeSymphonyApi {

    @Autowired
    private MessageService messageService;

    @GetMapping(path="/{streamId}/messages", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Message>> getMessages(@PathVariable String streamId) throws NoSuchStreamException {

        List<Message> messages = messageService.getMessages(streamId);

        return new ResponseEntity<>(messages, HttpStatus.OK);
    }

    @PostMapping(path="/{streamId}/messages", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Message>> addMessage
            (@PathVariable String streamId,
             @RequestBody Message message)
            throws BadMessageException, NoSuchStreamException {

        Stream stream = messageService.addMessageToStream(streamId, message);

        List<Message> messages = stream.getMessages();

        return new ResponseEntity<>(messages, HttpStatus.CREATED);
    }


    @PostMapping(path="/", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Stream> createStream
            (@RequestBody Stream stream)
            throws NoSuchStreamException {

        Stream newStream = messageService.createStream(stream);

        return new ResponseEntity<>(newStream, HttpStatus.CREATED);
    }


    @ExceptionHandler(NoSuchStreamException.class)
    public void noStreamException(HttpServletResponse response) {
        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
    }

    @ExceptionHandler(BadMessageException.class)
    public void badMessageException(HttpServletResponse response) {
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }

}
