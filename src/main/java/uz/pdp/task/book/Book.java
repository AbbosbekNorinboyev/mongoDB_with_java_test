package uz.pdp.task.book;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
@Document("books")
public class Book {
    @Id
    private String id;
    private String name;
    private String author;
}
