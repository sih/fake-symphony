package xyz.dsoc.labs.fake.symphony.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author sih
 */
@Data
@Entity
@Table(name="stream")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Stream {

    @Id
    @GeneratedValue
    @JsonIgnore
    @Column(name = "message_pk")
    private Integer streamPk;

    @Column(name = "stream_id")
    @EqualsAndHashCode.Include
    private String streamId;

    @Column(name = "stream_title")
    private String title;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Message> messages;

    public void addMessage(Message message) {
        if (null == messages) {
            messages = new ArrayList<>();
        }
        messages.add(message);
    }

}
