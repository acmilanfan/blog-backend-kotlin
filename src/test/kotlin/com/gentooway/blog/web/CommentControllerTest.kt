package com.gentooway.blog.web

import com.fasterxml.jackson.module.kotlin.readValue
import com.gentooway.blog.errors.CommentNotFoundException
import com.gentooway.blog.errors.ExceptionDescription.Companion.COMMENT_NOT_FOUND
import com.gentooway.blog.json.PageableRequest
import com.gentooway.blog.model.Comment
import com.gentooway.blog.model.Post
import com.gentooway.blog.repository.CommentRepository
import com.gentooway.blog.repository.PostRepository
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Sort
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

internal class CommentControllerTest : WebControllerTest() {

    @Autowired
    private lateinit var postRepository: PostRepository

    @Autowired
    private lateinit var commentRepository: CommentRepository

    @Test
    internal fun `should save new comment`() {
        // given
        val post = Post(
                title = "test",
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
                .andExpect(status().isOk)

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
                title = "test",
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
                .andExpect(status().isOk)

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
                title = "test",
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
                .andExpect(status().isOk)

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
                title = "test",
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

        val pageableRequest = PageableRequest(
                page = 0,
                size = 2)

        // when
        val mvcResult = mvc.perform(post("/post/${post.id}/comment/displayed")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsString(pageableRequest)))
                .andExpect(status().isOk)
                .andReturn()

        // then
        val comments = objectMapper.readValue<List<Comment>>(mvcResult.response.contentAsString)
        assertThat(comments.size, Is(equalTo(1)))

        val foundComment = comments.get(0)
        assertThat(foundComment.id, Is(equalTo(comment.id)))
        assertThat(foundComment.displayed, Is(equalTo(true)))
    }

    @Test
    internal fun `should return a sorted page with comments`() {
        // given
        val post = Post(
                title = "test",
                content = "test123",
                author = "test",
                preview = "123",
                tags = "tag1")
        postRepository.save(post)

        val comment = Comment(
                content = "test comment",
                author = "3",
                post = post,
                displayed = true)
        commentRepository.save(comment)

        val secondComment = Comment(
                content = "test comment 2",
                author = "2",
                post = post,
                displayed = true)
        commentRepository.save(secondComment)

        val thirdComment = Comment(
                content = "test comment 3",
                author = "1",
                post = post,
                displayed = false)
        commentRepository.save(thirdComment)


        val pageableRequest = PageableRequest(
                page = 0,
                size = 2,
                field = "author",
                direction = Sort.Direction.ASC)

        // when
        val mvcResult = mvc.perform(post("/post/${post.id}/comment/displayed")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsString(pageableRequest)))
                .andExpect(status().isOk)
                .andReturn()

        // then
        val comments = objectMapper.readValue<List<Comment>>(mvcResult.response.contentAsString)
        assertThat(comments.size, Is(equalTo(2)))

        val foundComment = comments.get(0)
        assertThat(foundComment.id, Is(equalTo(secondComment.id)))

        val foundSecondComment = comments.get(1)
        assertThat(foundSecondComment.id, Is(equalTo(comment.id)))
    }

    @Test
    internal fun `should like a comment`() {
        // given
        val post = Post(
                title = "test",
                content = "test123",
                author = "test",
                preview = "123",
                tags = "tag1")
        postRepository.save(post)

        val comment = Comment(
                content = "test comment",
                author = "123",
                post = post,
                displayed = true,
                rating = 1)
        commentRepository.save(comment)

        // when
        mvc.perform(put("/comment/${comment.id}/like"))
                .andExpect(status().isOk)

        // then
        val comments = commentRepository.findAll()
        assertThat(comments.size, Is(equalTo(1)))

        val savedComment = comments.get(0)
        assertThat(savedComment.rating, Is(equalTo(2)))
    }

    @Test
    internal fun `should dislike a comment`() {
        // given
        val post = Post(
                title = "test",
                content = "test123",
                author = "test",
                preview = "123",
                tags = "tag1")
        postRepository.save(post)

        val comment = Comment(
                content = "test comment",
                author = "123",
                post = post,
                displayed = true,
                rating = 5)
        commentRepository.save(comment)

        // when
        mvc.perform(put("/comment/${comment.id}/dislike"))
                .andExpect(status().isOk)

        // then
        val comments = commentRepository.findAll()
        assertThat(comments.size, Is(equalTo(1)))

        val savedComment = comments.get(0)
        assertThat(savedComment.rating, Is(equalTo(4)))
    }

    @Test
    internal fun `should receive an error message when comment not found`() {
        // when
        val mvcResult = mvc.perform(put("/comment/123/like"))
                .andExpect(status().isBadRequest)
                .andReturn()

        // then
        val resolvedException = mvcResult.resolvedException
        assertThat(resolvedException, Is(instanceOf(CommentNotFoundException::class.java)))

        val message = resolvedException?.message
        assertThat(message, Is(equalTo(COMMENT_NOT_FOUND)))
    }
}
