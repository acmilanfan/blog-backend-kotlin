package com.gentooway.blog.repository

import com.gentooway.blog.model.Comment
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository

interface CommentRepository : JpaRepository<Comment, Long> {

    fun getAllByPostIdAndDisplayedTrue(postId: Long, pageable: Pageable): Page<Comment>
}
