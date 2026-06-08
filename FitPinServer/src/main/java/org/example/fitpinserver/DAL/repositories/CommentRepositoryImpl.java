package org.example.fitpinserver.DAL.repositories;

import org.example.fitpinserver.DAL.entities.CommentEntity;
import org.example.fitpinserver.DAL.entities.PostEntity;
import org.example.fitpinserver.DAL.entities.UserEntity;
import org.example.fitpinserver.DAL.mappers.CommentPersistenceMapper;
import org.example.fitpinserver.business.repositories.CommentRepository;
import org.example.fitpinserver.domain.models.Comment;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class CommentRepositoryImpl implements CommentRepository {

    private final CommentJPARepository commentJPARepository;
    private final CommentPersistenceMapper commentPersistenceMapper;
    private final UserJPARepository userJPARepository;
    private final PostJPARepository postJPARepository;

    public CommentRepositoryImpl(CommentJPARepository commentJPARepository, CommentPersistenceMapper commentPersistenceMapper,
                                 UserJPARepository userJPARepository, PostJPARepository postJPARepository) {
        this.commentJPARepository = commentJPARepository;
        this.commentPersistenceMapper = commentPersistenceMapper;
        this.userJPARepository = userJPARepository;
        this.postJPARepository = postJPARepository;
    }

    @Override
    public List<Comment> findByPostId(Long postId) {
        return commentJPARepository.findByPost_IdOrderByTimestampAsc(postId).stream()
                .map(commentPersistenceMapper::toDomain)
                .toList();
    }

    @Override
    public Comment save(Comment comment) {
        UserEntity authorEntity = userJPARepository.findById(comment.getAuthor().getId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        PostEntity postEntity = postJPARepository.findById(comment.getPost().getId())
                .orElseThrow(() -> new RuntimeException("Post not found"));

        CommentEntity entity = commentPersistenceMapper.toEntity(comment, authorEntity, postEntity);
        return commentPersistenceMapper.toDomain(commentJPARepository.save(entity));
    }

    @Override
    public Optional<Comment> findById(Long id) {
        return commentJPARepository.findById(id).map(commentPersistenceMapper::toDomain);
    }

    @Override
    public void deleteById(Long id) {
        commentJPARepository.deleteById(id);
    }
}
