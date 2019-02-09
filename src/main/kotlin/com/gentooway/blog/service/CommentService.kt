package com.gentooway.blog.service

import com.gentooway.blog.errors.ExceptionDescription
import com.gentooway.blog.errors.ExceptionDescription.Companion.COMMENT_NOT_FOUND
import com.gentooway.blog.json.PageableRequest
import com.gentooway.blog.model.Comment
import com.gentooway.blog.repository.CommentRepository
import com.gentooway.blog.repository.PostRepository
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * Service for the comments entities logic
 */
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
        val comment = retrieveComment(commentId)

        val updated = comment.copy(displayed = !comment.displayed)

        commentRepository.save(updated)
    }

    fun getDisplayed(postId: Long, request: PageableRequest): List<Comment> {
        val pageRequest = PageRequest.of(request.page, request.size, Sort.by(request.direction, request.field))

        return commentRepository.getAllByPostIdAndDisplayedTrue(postId, pageRequest).content
    }

    fun like(commentId: Long) {
        val comment = retrieveComment(commentId)

        val updated = comment.copy(rating = comment.rating.inc())

        commentRepository.save(updated)
    }

    fun dislike(commentId: Long) {
        val comment = retrieveComment(commentId)

        if (comment.rating > 0) {
            val updated = comment.copy(rating = comment.rating.dec())
            commentRepository.save(updated)
        }
    }

    private fun retrieveComment(commentId: Long) =
            commentRepository.findById(commentId)
                    .orElseThrow { IllegalArgumentException(COMMENT_NOT_FOUND) }

}
