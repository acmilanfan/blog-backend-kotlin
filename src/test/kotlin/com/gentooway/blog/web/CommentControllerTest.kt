package com.gentooway.blog.web

import com.fasterxml.jackson.module.kotlin.readValue
import com.gentooway.blog.model.Comment
import com.gentooway.blog.model.Post
import com.gentooway.blog.repository.CommentRepository
import com.gentooway.blog.repository.PostRepository
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

internal class CommentControllerTest : WebControllerTest() {

    @Autowired
    private lateinit var postRepository: PostRepository

    @Autowired
    private lateinit var commentRepository: CommentRepository

    @Test
    internal fun `should save new comment`() {
        // given
        val post = Post(
                content = "test123",
                author = "test",
                preview = "123",
                tags = "tag1")
        postRepository.save(post)

        val comment = Comment(
                content = "test comment",
                author = "test132",
                post = post)

        // when
        mvc.perform(post("/post/${post.id}/comment")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsString(comment)))
                .andExpect(MockMvcResultMatchers.status().isOk)

        // then
        val postFromDb = postRepository.findById(post.id).orElse(post)
        assertThat(postFromDb.comments.size, Is(equalTo(1)))

        val commentsCount = commentRepository.count()
        assertThat(commentsCount, Is(equalTo(1L)))
    }

    @Test
    internal fun `should delete comment`() {
        // given
        val post = Post(
                content = "test123",
                author = "test",
                preview = "123",
                tags = "tag1")
        postRepository.save(post)

        val comment = Comment(
                content = "test comment",
                author = "test132",
                post = post)
        commentRepository.save(comment)

        // when
        mvc.perform(delete("/comment/${comment.id}"))
                .andExpect(MockMvcResultMatchers.status().isOk)

        // then
        val postFromDb = postRepository.findById(post.id).orElse(post)
        assertThat(postFromDb.comments.size, Is(equalTo(0)))

        val commentsCount = commentRepository.count()
        assertThat(commentsCount, Is(equalTo(0L)))
    }

    @Test
    internal fun `should switch comment displayed value`() {
        // given
        val post = Post(
                content = "test123",
                author = "test",
                preview = "123",
                tags = "tag1")
        postRepository.save(post)

        val comment = Comment(
                content = "test comment",
                author = "test132",
                post = post,
                displayed = true)
        commentRepository.save(comment)

        // when
        mvc.perform(put("/comment/${comment.id}/displayed"))
                .andExpect(MockMvcResultMatchers.status().isOk)

        // then
        val comments = commentRepository.findAll()
        assertThat(comments.size, Is(equalTo(1)))

        val savedComment = comments.get(0)
        assertThat(savedComment.displayed, Is(equalTo(false)))
    }

    @Test
    internal fun `should return only displayed comments by post id`() {
        // given
        val post = Post(
                content = "test123",
                author = "test",
                preview = "123",
                tags = "tag1")
        postRepository.save(post)

        val comment = Comment(
                content = "test comment",
                author = "test132",
                post = post,
                displayed = true)
        commentRepository.save(comment)

        val norDisplayedComment = Comment(
                content = "test comment 2",
                author = "test321",
                post = post,
                displayed = false)
        commentRepository.save(norDisplayedComment)

        // when
        val mvcResult = mvc.perform(get("/post/${post.id}/comment/displayed"))
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andReturn()

        // then
        val comments = objectMapper.readValue<List<Comment>>(mvcResult.response.contentAsString)
        assertThat(comments.size, Is(equalTo(1)))

        val foundComment = comments.get(0)
        assertThat(foundComment.id, Is(equalTo(comment.id)))
        assertThat(foundComment.displayed, Is(equalTo(true)))
    }
}
