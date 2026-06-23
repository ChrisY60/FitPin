package org.example.fitpinserver.DAL.elasticsearch;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Document(indexName = "products", createIndex = false)
@Getter
@Setter
public class ProductDocument {

    @Id
    private Long id;

    @Field(type = FieldType.Text)
    private String name;

    @Field(type = FieldType.Long)
    private Long brandId;

    @Field(type = FieldType.Text)
    private String brandName;

    @Field(type = FieldType.Keyword, index = false)
    private String imageUrl;

    public ProductDocument() {
    }

    public ProductDocument(Long id, String name, Long brandId, String brandName, String imageUrl) {
        this.id = id;
        this.name = name;
        this.brandId = brandId;
        this.brandName = brandName;
        this.imageUrl = imageUrl;
    }
}
