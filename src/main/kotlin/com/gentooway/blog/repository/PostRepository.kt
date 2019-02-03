package com.gentooway.blog.repository

import com.gentooway.blog.model.Post
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository

interface PostRepository : JpaRepository<Post, Long> {

    fun getAllByAuthor(author: String): List<Post>

    fun getAllByDisplayedTrue(pageable: Pageable): Page<Post>
}
