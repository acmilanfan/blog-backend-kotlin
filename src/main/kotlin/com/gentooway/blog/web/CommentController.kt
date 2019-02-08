package com.gentooway.blog.web

import com.gentooway.blog.json.PageableRequest
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

    @PutMapping("/comment/{commentId}/displayed")
    fun changeDisplayed(@PathVariable commentId: Long) {
        commentService.changeDisplayed(commentId)
    }

    @PostMapping("/post/{postId}/comment/displayed")
    fun getDisplayed(@PathVariable postId: Long, @RequestBody pageableRequest: PageableRequest): List<Comment> {
        return commentService.getDisplayed(postId, pageableRequest)
    }

    @PutMapping("/comment/{commentId}/like")
    fun like(@PathVariable commentId: Long) {
        commentService.like(commentId)
    }

    @PutMapping("/comment/{commentId}/dislike")
    fun dislike(@PathVariable commentId: Long) {
        commentService.dislike(commentId)
    }
}
