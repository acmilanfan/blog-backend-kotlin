package com.gentooway.blog.web

import com.gentooway.blog.model.Post
import com.gentooway.blog.service.PostService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/post")
class PostController(private val postService: PostService) {

    @GetMapping
    fun list(): List<Post> {
        return postService.getAllPosts()
    }

    @PostMapping
    fun create(@RequestBody post: Post) {
        postService.createPost(post)
    }
}