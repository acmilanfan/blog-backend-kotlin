package com.gentooway.blog.service

import com.gentooway.blog.model.Comment
import com.gentooway.blog.repository.CommentRepository
import com.gentooway.blog.repository.PostRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class CommentService(private val commentRepository: CommentRepository,
                     private val postRepository: PostRepository) {

    fun create(postId: Long, comment: Comment) {
        val post = postRepository.findById(postId)
                .orElseThrow { IllegalArgumentException("Post with the given id not found") }

        post.comments.add(comment)
    }

    fun delete(commentId: Long) {
        commentRepository.deleteById(commentId)
    }

    fun changeDisplayed(commentId: Long) {
        val comment = commentRepository.findById(commentId)
                .orElseThrow { IllegalArgumentException("Comment with the given id not found") }

        val updated = comment.copy(displayed = !comment.displayed)

        commentRepository.save(updated)
    }

}
