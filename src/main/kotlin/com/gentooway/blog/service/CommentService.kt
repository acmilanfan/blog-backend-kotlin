package com.gentooway.blog.service

import com.gentooway.blog.errors.ExceptionDescription
import com.gentooway.blog.errors.ExceptionDescription.Companion.COMMENT_NOT_FOUND
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
                .orElseThrow { IllegalArgumentException(ExceptionDescription.POST_NOT_FOUND) }

        post.comments.add(comment)
    }

    fun delete(commentId: Long) {
        commentRepository.deleteById(commentId)
    }

    fun changeDisplayed(commentId: Long) {
        val comment = commentRepository.findById(commentId)
                .orElseThrow { IllegalArgumentException(COMMENT_NOT_FOUND) }

        val updated = comment.copy(displayed = !comment.displayed)

        commentRepository.save(updated)
    }

    fun getDisplayed(postId: Long): List<Comment> {
        return commentRepository.getAllByPostIdAndDisplayedTrue(postId)
    }

}
