CREATE SCHEMA IF NOT EXISTS symphony;

CREATE TABLE IF NOT EXISTS message
(
    message_pk INT NOT NULL,
    message_id VARCHAR NOT NULL,
    message_content VARCHAR NOT NULL,
    message_ts INT NOT NULL,
    PRIMARY KEY (message_pk)
)
;
