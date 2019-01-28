package com.gentooway.blog.model

import java.time.LocalDateTime
import javax.persistence.*

@Entity
data class Post(
       @Id
       @GeneratedValue(strategy = GenerationType.IDENTITY)
       val id: Long,

       val content: String,

       val author: String,

       val rating: Int,

       val preview: String,

       @Column
       val creationDate: LocalDateTime
)
