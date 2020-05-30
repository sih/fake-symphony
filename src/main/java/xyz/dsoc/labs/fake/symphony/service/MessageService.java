package xyz.dsoc.labs.fake.symphony.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.dsoc.labs.fake.symphony.domain.Message;
import xyz.dsoc.labs.fake.symphony.domain.Stream;
import xyz.dsoc.labs.fake.symphony.repo.StreamRepo;

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
     * @throws MessageServiceException When the stream cannot be created
     */
    public Stream createStream(Stream stream) throws MessageServiceException {
        if (null == stream) {
            throw new MessageServiceException("You need to supply a valid, non-null stream");
        }

        return repo.save(stream);
    }


    /**
     * @param streamId The id of the message stream
     * @param message The message to add to the stream
     */
    public Stream addMessageToStream(String streamId, Message message) throws MessageServiceException {

        if (null == message) {
            throw new MessageServiceException("You need to supply a valid, non-null message");

        }

        Optional<Stream> oStream = repo.findOneByStreamId(streamId);

        if (!oStream.isPresent()) {
            throw new MessageServiceException("There is no stream with id "+streamId);

        }

        Stream stream = oStream.get();
        stream.addMessage(message);

        return repo.save(stream);

    }

}
