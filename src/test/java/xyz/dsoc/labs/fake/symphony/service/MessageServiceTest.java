package xyz.dsoc.labs.fake.symphony.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import xyz.dsoc.labs.fake.symphony.domain.Message;
import xyz.dsoc.labs.fake.symphony.domain.Stream;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author sih
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class MessageServiceTest {

    @Autowired
    private MessageService messageService;

    @Test(expected = MessageServiceException.class)
    public void createStreamShouldThrowExceptionWhenStreamIsNull() throws MessageServiceException {
        messageService.createStream(null);
    }

    @Test
    public void createStreamShouldReturnStreamWithPkSet() throws MessageServiceException {
        Stream foo = new Stream();
        foo.setStreamId("123");
        foo.setTitle("foo");

        Stream dbFoo = messageService.createStream(foo);
        assertNotNull(dbFoo.getStreamPk());
        assertNotEquals(Long.valueOf(0L), dbFoo.getStreamPk());
        assertEquals("123", dbFoo.getStreamId());
        assertEquals("foo", dbFoo.getTitle());
        assertNull(dbFoo.getMessages());
    }

    @Test
    public void createStreamShouldCascadeAndCreateMessages() throws MessageServiceException {
        Stream foo = new Stream();
        foo.setStreamId("123");
        foo.setTitle("foo");

        Message m = new Message();
        m.setTimestamp(1234L);
        m.setMessageId("1234");
        m.setContent("Hello");

        foo.setMessages(Collections.singletonList(m));

        Stream dbFoo = messageService.createStream(foo);
        assertNotNull(dbFoo.getStreamPk());
        assertNotEquals(Long.valueOf(0L), dbFoo.getStreamPk());
        assertEquals("123", dbFoo.getStreamId());
        assertEquals("foo", dbFoo.getTitle());

        List<Message> dbFooMessages = dbFoo.getMessages();
        assertNotNull(dbFooMessages);
        assertEquals(1, dbFooMessages.size());

        Message dbMessage = dbFooMessages.get(0);
        assertNotNull(dbMessage.getMessagePk());
        assertNotEquals(Long.valueOf(0L), dbMessage.getMessagePk());

    }

}