package uz.pdp.task.book;

import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class BookRepository {
    private final MongoTemplate mongoTemplate;

    public Book saveBook(@NonNull Book book) {
        return mongoTemplate.save(book);
    }

    public List<Book> findBookAll() {
        return mongoTemplate.findAll(Book.class);
    }

    public Page<Book> findBookAllPaged(Pageable pageable) {
        Query query = new Query().with(pageable);
        List<Book> books = mongoTemplate.find(query, Book.class);
        return PageableExecutionUtils.getPage(books, pageable, () -> mongoTemplate.count(new Query(), Book.class));
    }

    public Optional<Book> findBookById(String id) {
        Criteria criteria = Criteria.where("_id").is(new ObjectId(id));
        Query query = new Query(criteria);
        return Optional.ofNullable(mongoTemplate.findOne(query, Book.class));
    }

    public boolean deleteBook(ObjectId id) {
        Criteria criteria = Criteria.where("_id").is(id);
        Query query = new Query(criteria);
        DeleteResult deleteResult = mongoTemplate.remove(query, Book.class);
        return deleteResult.wasAcknowledged();
    }

    public boolean updateBook(Book book) {
        Criteria criteria = Criteria.where("_id").is(book.getId());
        Query query = new Query(criteria);
        Update update = new Update();
        if (book.getName() != null) {
            update.set("name", book.getName());
        }
        if (book.getAuthor() != null) {
            update.set("author", book.getAuthor());
        }
        UpdateResult updateResult = mongoTemplate.updateFirst(query, update, Book.class);
        return updateResult.wasAcknowledged();
    }
}
