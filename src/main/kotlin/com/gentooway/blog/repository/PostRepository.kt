package com.gentooway.blog.repository

import com.gentooway.blog.model.Post
import org.springframework.data.jpa.repository.JpaRepository

interface PostRepository : JpaRepository<Post, Long> {

    fun getAllByAuthor(author: String): List<Post>
}
