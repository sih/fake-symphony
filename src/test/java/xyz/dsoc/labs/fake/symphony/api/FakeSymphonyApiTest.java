package xyz.dsoc.labs.fake.symphony.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import xyz.dsoc.labs.fake.symphony.domain.Message;
import xyz.dsoc.labs.fake.symphony.domain.Stream;
import xyz.dsoc.labs.fake.symphony.service.MessageService;
import xyz.dsoc.labs.fake.symphony.service.NoSuchStreamException;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author sih
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@EnableWebMvc
@AutoConfigureMockMvc
public class FakeSymphonyApiTest {

    private List<Message> messages;
    private String mJson;
    private Message m;

    private static final ObjectMapper mapper = new ObjectMapper();

    @Before
    public void setUp() throws Exception {

        messages = new ArrayList<>();

        m = new Message();
        m.setTimestamp(1234L);
        m.setMessageId("1234");
        m.setContent("Hello");
        m.setUserHandle("foobert");

        messages.add(m);

        mJson  = mapper.writeValueAsString(m);
    }

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MessageService messageService;


    @Test
    public void whenNoStreamForIdGetMessagesShouldReturnNotFound() throws Exception {

        when(messageService.getMessages("123")).thenThrow(new NoSuchStreamException("no stream"));
        mockMvc
                .perform(get("/streams/123/messages"))
                .andExpect(status().isNotFound())
        ;

    }

    @Test
    public void whenMessagesInStreamGetMessagesShouldReturnOk() throws Exception {

        when(messageService.getMessages("123")).thenReturn(messages);
        mockMvc
                .perform(get("/streams/123/messages"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].message", is("Hello")))
        ;

    }

    @Test
    public void whenNoStreamForIdAddMessageShouldReturnNotFound() throws Exception {
        when(messageService.addMessageToStream("123", m)).thenThrow(new NoSuchStreamException("no stream"));
        mockMvc
                .perform(post("/streams/123/messages")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mJson)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNotFound())
        ;
    }

    @Test
    public void whenValidMessageProvidedToAddMessageShouldReturnOk() throws Exception {

        Stream s = new Stream();
        s.addMessage(m);

        when(messageService.addMessageToStream("123", m)).thenReturn(s);


        mockMvc
                .perform(post("/streams/123/messages")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mJson)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
        ;
    }

}