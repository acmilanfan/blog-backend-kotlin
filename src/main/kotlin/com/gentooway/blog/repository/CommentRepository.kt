package com.gentooway.blog.repository

import com.gentooway.blog.model.Comment
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository

/**
 * JPA repository for comment entity
 */
interface CommentRepository : JpaRepository<Comment, Long> {

    /**
     * Returns comments page by post id
     * @param postId post id
     * @param pageable page request with page, size and sort information
     * @return sorted comments page
     */
    fun getAllByPostIdAndDisplayedTrue(postId: Long, pageable: Pageable): Page<Comment>
}
