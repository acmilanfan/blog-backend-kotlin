package com.gentooway.blog.repository

import com.gentooway.blog.model.Comment
import org.springframework.data.jpa.repository.JpaRepository

interface CommentRepository : JpaRepository<Comment, Long>
