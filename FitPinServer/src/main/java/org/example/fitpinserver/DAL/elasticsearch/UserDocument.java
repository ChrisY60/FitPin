package org.example.fitpinserver.DAL.elasticsearch;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Document(indexName = "users", createIndex = false)
@Getter
@Setter
public class UserDocument {

    @Id
    private Long id;

    @Field(type = FieldType.Text)
    private String username;

    @Field(type = FieldType.Keyword, index = false)
    private String profilePictureUrl;

    public UserDocument() {
    }

    public UserDocument(Long id, String username, String profilePictureUrl) {
        this.id = id;
        this.username = username;
        this.profilePictureUrl = profilePictureUrl;
    }
}
