package com.gentooway.blog.web

import com.gentooway.blog.json.PageableRequest
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

    @GetMapping("/{id}/info")
    fun getById(@PathVariable id: Long): Post {
        return postService.getById(id)
    }

    @PostMapping
    fun create(@RequestBody post: Post) {
        postService.createOrUpdatePost(post)
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long) {
        postService.deletePost(id)
    }

    @PutMapping
    fun update(@RequestBody post: Post) {
        postService.createOrUpdatePost(post)
    }

    @GetMapping("/{author}")
    fun getByAuthor(@PathVariable author: String): List<Post> {
        return postService.getByAuthor(author)
    }

    @PutMapping("/{id}/displayed")
    fun changeDisplayed(@PathVariable id: Long) {
        postService.changeDisplayed(id)
    }

    @PostMapping("/displayed")
    fun getDisplayedPosts(@RequestBody pageableRequest: PageableRequest): List<Post> {
        return postService.getDisplayedPosts(pageableRequest)
    }

    @PutMapping("/{id}/like")
    fun like(@PathVariable id: Long) {
        postService.like(id)
    }

    @PutMapping("/{id}/dislike")
    fun dislike(@PathVariable id: Long) {
        postService.dislike(id)
    }
}