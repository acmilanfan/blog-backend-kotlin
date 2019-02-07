package com.gentooway.blog.repository

import com.gentooway.blog.model.Post
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface PostRepository : JpaRepository<Post, Long> {

    fun getAllByAuthor(author: String): List<Post>

    fun getAllByDisplayedTrue(pageable: Pageable): Page<Post>

    @Query(
            value = "select p from Post p join p.comments comments group by p order by count(comments) desc",
            countQuery = "select count(p) from Post p"
    )
    fun getAllByOrderByComments(pageable: Pageable): Page<Post>
}
