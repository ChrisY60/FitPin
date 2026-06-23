package org.example.fitpinserver.DAL.elasticsearch;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Document(indexName = "brands", createIndex = false)
@Getter
@Setter
public class BrandDocument {

    @Id
    private Long id;

    @Field(type = FieldType.Text)
    private String name;

    public BrandDocument() {
    }

    public BrandDocument(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
