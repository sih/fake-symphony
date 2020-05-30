package xyz.dsoc.labs.fake.symphony.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.*;

/**
 * This is a subset of a data item returned by the Symphony /messages API
 *
 * <pre>
 *     {
 *       "messageId":   "ryoQ1abc"
 *       "timestamp":   1534891105293
 *       "message":     "<div data-format="PresentationML" data-version="2.0">Hello World</div>"
 *     }
 * </pre>
 *
 * @author sih
 *
 */
@Data
@Entity
@Table(name="message", schema="symphony")
public class Message {

    @Id
    @GeneratedValue
    @JsonIgnore
    @Column(name = "messagePk")
    private Integer messagePk;

    @Column(name = "message_id")
    private String messageId;

    @Column(name = "message_ts")
    private Long timestamp;

    @Column(name = "message_content")
    @JsonProperty("message")
    private String content;
}

