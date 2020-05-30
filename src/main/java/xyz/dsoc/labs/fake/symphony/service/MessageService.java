package xyz.dsoc.labs.fake.symphony.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.dsoc.labs.fake.symphony.domain.Message;
import xyz.dsoc.labs.fake.symphony.domain.Stream;
import xyz.dsoc.labs.fake.symphony.repo.StreamRepo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author sih
 */
@Service
public class MessageService {

    private StreamRepo repo;

    @Autowired
    public MessageService(StreamRepo repo) {
        this.repo = repo;
    }


    /**
     * @return The new stream to create
     * @throws NoSuchStreamException When the stream cannot be created
     */
    public Stream createStream(Stream stream) throws NoSuchStreamException {
        if (null == stream) {
            throw new NoSuchStreamException("You need to supply a valid, non-null stream");
        }

        return repo.save(stream);
    }


    /**
     * @param streamId The id of the message stream
     * @param message The message to add to the stream
     * @throws NoSuchStreamException when there is no match to the stream id
     * @throws BadMessageException when the message supplied is null
     */
    public Stream addMessageToStream(String streamId, Message message) throws NoSuchStreamException, BadMessageException {

        if (null == message) {
            throw new BadMessageException("You need to supply a valid, non-null message");

        }

        Optional<Stream> oStream = repo.findOneByStreamId(streamId);

        if (!oStream.isPresent()) {
            throw new NoSuchStreamException("There is no stream with id "+streamId);

        }

        Stream stream = oStream.get();
        stream.addMessage(message);

        return repo.save(stream);

    }


    /**
     * @param streamId The stream identifier
     * @return The messages in this stream or an empty list if no messages exist
     * @throws NoSuchStreamException When the stream id cannot be found
     */

    public List<Message> getMessages(String streamId) throws NoSuchStreamException {

        Optional<Stream> oStream = repo.findOneByStreamId(streamId);

        if (!oStream.isPresent()) {
            throw new NoSuchStreamException("There is no stream with id "+streamId);

        }

        Stream stream = oStream.get();
        List<Message> messages = (null == stream.getMessages()) ? new ArrayList<>() : stream.getMessages();

        return messages;

    }

}
