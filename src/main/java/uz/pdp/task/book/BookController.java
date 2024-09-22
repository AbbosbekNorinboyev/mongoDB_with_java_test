package uz.pdp.task.book;

import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController {
    private final BookRepository bookRepository;

    @GetMapping
    public ResponseEntity<List<Book>> getAllBook() {
        return ResponseEntity.ok(bookRepository.findBookAll());
    }

    @GetMapping("/paged")
    public ResponseEntity<Page<Book>> getAllPaged(
            @RequestParam(defaultValue = "0", required = false) int page,
            @RequestParam(defaultValue = "10", required = false) int size,
            @RequestParam(defaultValue = "ASC", required = false) Sort.Direction direction,
            @RequestParam String filed
    ) {
        Sort sort = Sort.by(direction, filed);
        Pageable pageable = PageRequest.of(page, size, sort);
        return ResponseEntity.ok(bookRepository.findBookAllPaged(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable String id) {
        Book post = bookRepository.findBookById(id)
                .orElseThrow(() -> new RuntimeException("Book Not Found : " + id));
        return ResponseEntity.ok(post);
    }

    @PostMapping
    public ResponseEntity<Book> createBook(@RequestBody Book book) {
        return ResponseEntity.status(HttpStatus.CREATED).body(bookRepository.saveBook(book));
    }

    @PutMapping
    public ResponseEntity<Boolean> updateBook(@RequestBody Book updatingData) {
        return ResponseEntity.ok(bookRepository.updateBook(updatingData));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable String id) {
        bookRepository.deleteBook(new ObjectId(id));
        return ResponseEntity.noContent().build();
    }
}
