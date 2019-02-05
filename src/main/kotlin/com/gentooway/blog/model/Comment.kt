package com.gentooway.blog.model

import java.io.Serializable
import java.time.LocalDateTime
import javax.persistence.*

@Entity
data class Comment(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long = 0,

        val content: String,

        val author: String,

        val rating: Int = 0,

        @Column(name = "CREATION_DATE")
        val creationDate: LocalDateTime = LocalDateTime.now(),

        val displayed: Boolean = false,

        @ManyToOne
        @JoinColumn(name = "POST_ID")
        val post: Post
) : Serializable