package org.example.fitpinserver.DAL.elasticsearch;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Document(indexName = "tags", createIndex = false)
@Getter
@Setter
public class TagDocument {

    @Id
    private Long id;

    @Field(type = FieldType.Text)
    private String name;

    public TagDocument() {
    }

    public TagDocument(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
