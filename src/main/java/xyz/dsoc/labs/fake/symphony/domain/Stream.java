package xyz.dsoc.labs.fake.symphony.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

/**
 * @author sih
 */
@Data
@Entity
@Table(name="stream")
public class Stream {

    @Id
    @GeneratedValue
    @JsonIgnore
    @Column(name = "message_pk")
    private Integer streamPk;

    @Column(name = "stream_id")
    private String streamId;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Message> messages;
}
