# Fake Symphony API

## Overview
This is meant to be a very basic version of a chat server with the GET API based roughly on the output from the [Symphony API](https://developers.symphony.com/restapi/reference#messages-v4).

The API has two concepts:

* Streams (or channels), which have:
  * An id, e.g. "abc123"
  * A title, e.g. "Shopping"
  * A list of messages ordered by creation time ascending
  
* Messages, which have:
  * Content, e.g. "Pot noodles"
  * An author (user), e.g. "foobert"
  * A (creation) timestamp in epoch ms, e.g. 1590917152

## What you can do

### Create a new stream
````
curl --location --request POST 'localhost:8080/streams' \
--header 'Content-Type: text/plain' \
--data-raw '{
	"streamId": "123",
	"title": "Foo",
	"messages": []
}'
````

### Add a message to a stream
````
curl --location --request POST 'localhost:8080/streams/123/messages' \
--header 'Content-Type: text/plain' \
--data-raw '{
	"messageId": "1",
	"message": "Hello, World!",
	"user": "foobert",
	"timestamp": 1590839801
}'
````


### List the messages in a stream
````
curl --location --request GET 'localhost:8080/streams/123/messages'
````

## How to run
1. Clone the project: ````git clone https://github.com/sih/fake-symphony.git````
2. Go to your project dir: ````cd ./fake-sympohony````
3. Build the project: ````mvn package spring-boot:repackage````
4. Run: ````java -jar ./target/fake-symphony-1.0.jar```` 
