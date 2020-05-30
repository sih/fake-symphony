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
        m.setUserHandle("foobert");
    }


    @Autowired
    private MessageService messageService;

    @Test(expected = NoSuchStreamException.class)
    public void createStreamShouldThrowExceptionWhenStreamIsNull() throws NoSuchStreamException {
        messageService.createStream(null);
    }

    @Test
    public void createStreamShouldReturnStreamWithPkSet() throws NoSuchStreamException {
        Stream dbFoo = messageService.createStream(foo);
        assertNotNull(dbFoo.getStreamPk());
        assertNotEquals(Long.valueOf(0L), dbFoo.getStreamPk());
        assertEquals("123", dbFoo.getStreamId());
        assertEquals("foo", dbFoo.getTitle());
        assertNull(dbFoo.getMessages());
    }

    @Test
    public void createStreamShouldCascadeAndCreateMessages() throws NoSuchStreamException {


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

    @Test(expected = BadMessageException.class)
    public void addMessageShouldThrowExceptionWhenMessageNull() throws NoSuchStreamException, BadMessageException {

        repo.save(foo);

        messageService.addMessageToStream("123", null);
    }

    @Test(expected = NoSuchStreamException.class)
    public void addMessageShouldThrowExceptionWhenNoStreamForId() throws NoSuchStreamException, BadMessageException {
        messageService.addMessageToStream("123", m);
    }

    @Test
    public void addMessageShouldAddANewMessageToTheStream() throws NoSuchStreamException, BadMessageException {

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

    @Test(expected = NoSuchStreamException.class)
    public void getMessagesShouldThrowExceptionWhenNoStreamForId() throws NoSuchStreamException {
        messageService.getMessages("123");
    }

    @Test
    public void getMessagesShouldReturnEmptyArrayWhenNullMessagesInStream() throws NoSuchStreamException {

        assertNull(foo.getMessages());
        repo.save(foo);

        List<Message> messages = messageService.getMessages(foo.getStreamId());
        assertNotNull(messages);
        assertTrue(messages.isEmpty());
    }

    @Test
    public void getMessagesShouldReturnAppropriateMessagesWhenTheyExist() throws NoSuchStreamException {

        foo.addMessage(m);
        assertEquals(1, foo.getMessages().size());
        repo.save(foo);

        List<Message> messages = messageService.getMessages(foo.getStreamId());
        assertNotNull(messages);
        assertEquals(1, messages.size());

        assertEquals(m ,messages.get(0));
    }


}