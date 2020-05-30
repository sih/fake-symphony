CREATE SCHEMA IF NOT EXISTS symphony;

CREATE TABLE IF NOT EXISTS message
(
    message_pk INT NOT NULL,
    message_id VARCHAR NOT NULL,
    message_content VARCHAR NOT NULL,
    message_ts INT NOT NULL,
    stream_pk INT NOT NULL,
    user_handle VARCHAR NOT NULL,
    PRIMARY KEY (message_pk)
)
;

CREATE TABLE IF NOT EXISTS stream
(
    stream_pk INT NOT NULL,
    stream_id VARCHAR NOT NULL,
    stream_title VARCHAR NOT NULL,
    PRIMARY KEY (stream_pk)
)
;
