package xyz.dsoc.labs.fake.symphony.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import xyz.dsoc.labs.fake.symphony.domain.Message;
import xyz.dsoc.labs.fake.symphony.domain.Stream;
import xyz.dsoc.labs.fake.symphony.repo.StreamRepo;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author sih
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD) // we're storing stuff to H2
public class MessageServiceTest {

    private Stream foo;
    private Message m;

    @Autowired
    private StreamRepo repo;

    @Before
    public void setUp() {
        foo = new Stream();
        foo.setStreamId("123");
        foo.setTitle("foo");


        m = new Message();
        m.setTimestamp(1234L);
        m.setMessageId("1234");
        m.setContent("Hello");
    }


    @Autowired
    private MessageService messageService;

    @Test(expected = MessageServiceException.class)
    public void createStreamShouldThrowExceptionWhenStreamIsNull() throws MessageServiceException {
        messageService.createStream(null);
    }

    @Test
    public void createStreamShouldReturnStreamWithPkSet() throws MessageServiceException {



        Stream dbFoo = messageService.createStream(foo);
        assertNotNull(dbFoo.getStreamPk());
        assertNotEquals(Long.valueOf(0L), dbFoo.getStreamPk());
        assertEquals("123", dbFoo.getStreamId());
        assertEquals("foo", dbFoo.getTitle());
        assertNull(dbFoo.getMessages());
    }

    @Test
    public void createStreamShouldCascadeAndCreateMessages() throws MessageServiceException {


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

    @Test(expected = MessageServiceException.class)
    public void addMessageShouldThrowExceptionWhenMessageNull() throws MessageServiceException {

        repo.save(foo);

        messageService.addMessageToStream("123", null);
    }

    @Test(expected = MessageServiceException.class)
    public void addMessageShouldThrowExceptionWhenNoStreamForId() throws MessageServiceException {
        messageService.addMessageToStream("123", m);
    }

    @Test
    public void addMessageShouldAddANewMessageToTheStream() throws MessageServiceException {

        repo.save(foo);

        assertNull(foo.getMessages());

        Stream dbFoo = messageService.addMessageToStream("123", m);
        List<Message> dbFooMessages = dbFoo.getMessages();
        assertNotNull(dbFooMessages);
        assertEquals(1, dbFooMessages.size());
        Message dbFooMessage = dbFooMessages.get(0);
        assertEquals(m, dbFooMessage);  // message ids should be equal
        // difference is we should have a PK as we've persisted this
        assertNotEquals(dbFooMessage.getMessagePk(), m.getMessagePk());

    }

}