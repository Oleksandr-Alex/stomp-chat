package com.alexb.chat.document;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class Message {

    private String author;

    private UUID roomId;

    private Instant timestamp;

    private String content;

    public Message(String author, UUID roomId, String content) {
        this.author = author;
        this.roomId = roomId;
        this.timestamp = Instant.now();
        this.content = content;
    }

    @JsonGetter("timestamp")
    public long getTimestampAsLong() {
        return timestamp.toEpochMilli();
    }

    @JsonSetter("timestamp")
    public void setTimestampAsLong(long timestamp) {
        this.timestamp = Instant.ofEpochMilli(timestamp);
    }

    @Override
    public String toString() {
        return "Message{" +
                "author=" + author +
                ", roomId=" + roomId +
                ", timestamp=" + timestamp.toEpochMilli() +
                ", content='" + content + '\'' +
                '}';
    }

}
