package obruening.timer.service.primary;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import obruening.timer.model.primary.Post;
import obruening.timer.repository.primary.PostRepository;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    @Transactional("primaryTransactionManager")
    public Post save(Post post) {
        return postRepository.save(post);
    }
    
    @Transactional("primaryTransactionManager")
    public List<Post> findAll() {
        return postRepository.findAll();
    }
}
