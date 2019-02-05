package com.gentooway.blog.web

import com.gentooway.blog.model.Comment
import com.gentooway.blog.service.CommentService
import org.springframework.web.bind.annotation.*

@RestController
class CommentController(private val commentService: CommentService) {

    @PostMapping("/post/{postId}/comment")
    fun create(@PathVariable postId: Long, @RequestBody comment: Comment) {
        commentService.create(postId, comment)
    }

    @DeleteMapping("/comment/{commentId}")
    fun delete(@PathVariable commentId: Long) {
        commentService.delete(commentId)
    }
}
