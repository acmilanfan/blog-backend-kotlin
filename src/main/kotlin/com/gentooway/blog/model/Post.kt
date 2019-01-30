package com.gentooway.blog.model

import java.time.LocalDateTime
import javax.persistence.*

@Entity
data class Post(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long = 0,

        val content: String,

        val author: String,

        val rating: Int = 0,

        val preview: String,

        val tags: String,

        @Column
        val creationDate: LocalDateTime = LocalDateTime.now()
)
