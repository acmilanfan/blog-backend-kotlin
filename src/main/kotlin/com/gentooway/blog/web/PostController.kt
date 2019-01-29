package com.gentooway.blog.web

import com.gentooway.blog.model.Post
import com.gentooway.blog.service.PostService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/post")
class PostController(private val postService: PostService) {

    @GetMapping
    fun list(): List<Post> {
        return postService.getAllPosts()
    }
}