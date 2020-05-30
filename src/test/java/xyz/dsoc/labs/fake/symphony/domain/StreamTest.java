package xyz.dsoc.labs.fake.symphony.domain;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author sih
 */
@RunWith(SpringRunner.class)
public class StreamTest {

    private Message message;

    @Before
    public void setUp() {
        message = new Message();
        message.setMessageId("123");
    }

    @Test
    public void addMessageShouldCreateListWhenNullAndAddMessage() {
        Stream s = new Stream();
        assertNull(s.getMessages());

        s.addMessage(message);

        List<Message> sMessages = s.getMessages();
        assertNotNull(sMessages);
        assertEquals(1, sMessages.size());
        assertEquals(message, sMessages.get(0));

    }


    @Test
    public void addMessageShouldAddMessageWhenListNotNull() {
        Stream s = new Stream();
        s.setMessages(new ArrayList<>());
        assertNotNull(s.getMessages());

        s.addMessage(message);

        List<Message> sMessages = s.getMessages();
        assertNotNull(sMessages);
        assertEquals(1, sMessages.size());
        assertEquals(message, sMessages.get(0));

    }


}