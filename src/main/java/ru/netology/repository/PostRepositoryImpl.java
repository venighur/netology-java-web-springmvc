package ru.netology.repository;

import org.springframework.stereotype.Repository;
import ru.netology.exception.NotFoundException;
import ru.netology.model.Post;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class PostRepositoryImpl implements PostRepository {
    ConcurrentHashMap<Long, String> posts = new ConcurrentHashMap<>();
    private long counter = 0;

    public List<Post> all() {
        List<Post> posts = new ArrayList<>();

        for (long key: this.posts.keySet()) {
            posts.add(new Post(key, this.posts.get(key)));
        }

        return posts;
    }

    public Optional<Post> getById(long id) {
        if (!posts.containsKey(id)) {
            throw new NotFoundException("Пост не найден");
        }
        return Optional.of(new Post(id, posts.get(id)));
    }

    public Post save(Post post) {
        if (post.getId() == 0) {
            do {
                counter++;
            } while (posts.containsKey(counter));

            posts.put(counter, post.getContent());
        } else {
            if (posts.containsKey(post.getId())) {
                posts.replace(post.getId(), post.getContent());
            } else {
                posts.put(post.getId(), post.getContent());
            }
        }

        return post;
    }

    public void removeById(long id) {
        posts.remove(id);
    }
}
