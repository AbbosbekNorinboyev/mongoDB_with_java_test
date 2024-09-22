package uz.pdp.task;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import uz.pdp.task.post.Post;
import uz.pdp.task.post.PostRepository;

import java.net.URL;
import java.util.List;
import java.util.Optional;

@SpringBootApplication
@EnableMongoAuditing
public class TaskApplication {

    public static void main(String[] args) {
        SpringApplication.run(TaskApplication.class, args);
    }

    @Bean
    public AuditorAware<Long> auditorAware() {
        return () -> Optional.of(1L);
    }

//    @Bean
    public ApplicationRunner applicationRunner(
            ObjectMapper objectMapper, PostRepository postRepository
    ) {
        return args -> {
            URL url = new URL("https://jsonplaceholder.typicode.com/posts");
            List<Post> posts = objectMapper.readValue(url, new TypeReference<>() {
            });
            postRepository.saveAll(posts);
        };
    }

}
