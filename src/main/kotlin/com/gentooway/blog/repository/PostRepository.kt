package com.gentooway.blog.repository

import com.gentooway.blog.model.Post
import org.springframework.data.jpa.repository.JpaRepository
import javax.transaction.Transactional
import javax.transaction.Transactional.TxType.MANDATORY

@Transactional(MANDATORY)
internal interface PostRepository : JpaRepository<Post, Long> {
    fun getAllByAuthor(author: String): Post?
}
