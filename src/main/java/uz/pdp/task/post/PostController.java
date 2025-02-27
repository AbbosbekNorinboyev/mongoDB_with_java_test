package uz.pdp.task.post;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostRepository postRepository;

    @GetMapping
    public ResponseEntity<List<Post>> getAllPost() {
        return ResponseEntity.ok(postRepository.findAll());
    }
    @GetMapping("/paged")
    public ResponseEntity<Page<Post>> getAllPaged(
            @RequestParam(defaultValue = "0", required = false) int page,
            @RequestParam(defaultValue = "10", required = false) int size,
            @RequestParam(defaultValue = "ASC", required = false) Sort.Direction direction,
            @RequestParam String field
    ) {
        Sort sort = Sort.by(direction, field);
        Pageable pageable = PageRequest.of(page, size, sort);
        return ResponseEntity.ok(postRepository.findAll(pageable));
    }

    @GetMapping("/query")
    public ResponseEntity<List<Post>> getAllByParams(
            @RequestParam(defaultValue = "", required = false) String title,
            @RequestParam(defaultValue = "0", required = false) int userId
    ) {
//        return ResponseEntity.ok(postRepository.findAllByTitleCustom(title, userId));
        String regex = "^" + title + ".*$";
        return ResponseEntity.ok(postRepository.findAllByTitleRegexAndUserIdGreaterThan(regex, userId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Post> getPostById(@PathVariable String id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post Not Found : " + id));
        return ResponseEntity.ok(post);
    }

    @PostMapping
    public ResponseEntity<Post> createPost(@RequestBody Post post) {
        return ResponseEntity.status(HttpStatus.CREATED).body(postRepository.save(post));
    }

    @PutMapping
    public ResponseEntity<Void> updatePost(@RequestBody Post updatingData) {
        Post post = postRepository.findById(updatingData.getPostId()).orElseThrow(
                () -> new RuntimeException("Post Not Found : " + updatingData.getPostId()));
        if (updatingData.getTitle() != null) {
            post.setTitle(updatingData.getTitle());
        }
        if (updatingData.getBody() != null) {
            post.setBody(updatingData.getBody());
        }
        if (updatingData.getId() != null) {
            post.setId(updatingData.getId());
        }
        if (updatingData.getUserId() != null) {
            post.setUserId(updatingData.getUserId());
        }
        postRepository.save(post);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable String id) {
        postRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
