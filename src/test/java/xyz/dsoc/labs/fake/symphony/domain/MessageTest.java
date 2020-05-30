package xyz.dsoc.labs.fake.symphony.domain;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * @author sih
 */
@RunWith(SpringRunner.class)
public class MessageTest {

    private static final ObjectMapper mapper = new ObjectMapper();

    @Test
    public void toJsonShouldOnlyIncludeNecessaryAttrs() {
        Message m = new Message();
        m.setMessagePk(123);
        m.setContent("Hello");
        m.setMessageId("123");
        m.setTimestamp(123L);
        m.setUserHandle("foobert");

        JsonNode messageNode = mapper.valueToTree(m);
        assertNotNull(messageNode);
        // should expose attrs with their symphony names
        assertEquals("123", messageNode.get("messageId").asText());
        assertEquals("Hello", messageNode.get("message").asText());
        assertEquals(123L, messageNode.get("timestamp").asLong());
        assertEquals("foobert", messageNode.get("user").asText());

        // should hide some attributes
        assertNull(messageNode.get("content"));
        assertNull(messageNode.get("messagePk"));
    }

}