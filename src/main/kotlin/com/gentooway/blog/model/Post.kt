package com.gentooway.blog.model

import java.io.Serializable
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

        @Column(name = "CREATION_DATE")
        val creationDate: LocalDateTime = LocalDateTime.now(),

        val displayed: Boolean = false,

        @OneToMany(mappedBy = "post", cascade = [CascadeType.ALL], fetch = FetchType.LAZY, orphanRemoval = true)
        val comments: MutableList<Comment> = mutableListOf()
) : Serializable
