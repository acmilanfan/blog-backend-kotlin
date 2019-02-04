package com.gentooway.blog.web

import com.fasterxml.jackson.module.kotlin.readValue
import com.gentooway.blog.json.PageableRequest
import com.gentooway.blog.model.Post
import com.gentooway.blog.repository.PostRepository
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Sort
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.time.LocalDate


internal class PostControllerTest : WebControllerTest() {

    @Autowired
    private lateinit var postRepository: PostRepository

    @Test
    internal fun `should return all posts`() {
        // given
        val post = Post(
                content = "test123",
                author = "test",
                preview = "123",
                tags = "tag1")
        postRepository.save(post)

        val secondPost = Post(
                content = "test321",
                author = "test1",
                preview = "321",
                tags = "tag2")
        postRepository.save(secondPost)

        // when
        val mvcResult = mvc.perform(get("/post"))
                .andExpect(status().isOk)
                .andReturn()

        // then
        val posts = objectMapper.readValue<List<Post>>(mvcResult.response.contentAsString)
        assertThat(posts.size, Is(equalTo(2)))
    }

    @Test
    internal fun `should save new post`() {
        // given
        val post = Post(
                content = "Test content",
                author = "AShumailov",
                preview = "321",
                tags = "tag1;tag2")

        // when
        mvc.perform(post("/post")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsString(post)))
                .andExpect(status().isOk)

        // then
        val posts = postRepository.findAll()
        assertThat(posts.size, Is(equalTo(1)))

        val addedPost = posts.get(0)
        assertThat(addedPost.content, Is(equalTo(post.content)))
        assertThat(addedPost.author, Is(equalTo(post.author)))
        assertThat(addedPost.preview, Is(equalTo(post.preview)))
        assertThat(addedPost.tags, Is(equalTo(post.tags)))
        assertThat(addedPost.rating, Is(equalTo(0)))
        assertThat(addedPost.creationDate.toLocalDate(), Is(equalTo(LocalDate.now())))
        assertThat(addedPost.id, Is(notNullValue()))
    }

    @Test
    internal fun `should delete post`() {
        // given
        val post = Post(
                content = "test123",
                author = "test",
                preview = "123",
                tags = "tag1")
        postRepository.save(post)

        // when
        mvc.perform(delete("/post/${post.id}"))
                .andExpect(status().isOk)

        // then
        val posts = postRepository.findAll()
        assertThat(posts.size, Is(equalTo(0)))
    }

    @Test
    internal fun `should update post`() {
        // given
        val post = Post(
                content = "test123",
                author = "test",
                preview = "123",
                tags = "tag1")
        postRepository.save(post)

        val updatedPost = Post(
                id = post.id,
                content = "test12355555",
                author = "test",
                preview = "123555555",
                tags = "tag1;tag3")

        // when
        mvc.perform(put("/post")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsString(updatedPost)))
                .andExpect(status().isOk)

        // then
        val posts = postRepository.findAll()
        assertThat(posts.size, Is(equalTo(1)))

        val savedPost = posts.get(0)
        assertThat(savedPost.content, Is(equalTo(updatedPost.content)))
        assertThat(savedPost.preview, Is(equalTo(updatedPost.preview)))
        assertThat(savedPost.tags, Is(equalTo(updatedPost.tags)))
    }

    @Test
    internal fun `should return author posts`() {
        // given
        val testAuthor = "test"

        val post = Post(
                content = "test123",
                author = testAuthor,
                preview = "123",
                tags = "tag1")
        postRepository.save(post)

        val secondPost = Post(
                content = "test12355555",
                author = "test1",
                preview = "123555555",
                tags = "tag1;tag3")
        postRepository.save(secondPost)

        // when
        val mvcResult = mvc.perform(get("/post/$testAuthor"))
                .andExpect(status().isOk)
                .andReturn()

        // then
        val posts = objectMapper.readValue<List<Post>>(mvcResult.response.contentAsString)
        assertThat(posts.size, Is(equalTo(1)))

        val foundPost = posts.get(0)
        assertThat(foundPost.id, Is(equalTo(post.id)))
    }

    @Test
    internal fun `should switch displayed value`() {
        // given
        val post = Post(
                content = "test123",
                author = "test",
                preview = "123",
                tags = "tag1",
                displayed = true)
        postRepository.save(post)

        // when
        mvc.perform(put("/post/${post.id}/displayed"))
                .andExpect(status().isOk)

        // then
        val posts = postRepository.findAll()
        assertThat(posts.size, Is(equalTo(1)))

        val savedPost = posts.get(0)
        assertThat(savedPost.displayed, Is(equalTo(false)))
    }

    @Test
    internal fun `should return only displayed posts`() {
        // given
        val post = Post(
                content = "test123",
                author = "test",
                preview = "123",
                tags = "tag1",
                displayed = true)
        postRepository.save(post)

        val notDisplayedPost = Post(
                content = "testewq",
                author = "test",
                preview = "321",
                tags = "tagw",
                displayed = false)
        postRepository.save(notDisplayedPost)

        val pageableRequest = PageableRequest(page = 0, size = 1)

        // when
        val mvcResult = mvc.perform(post("/post/displayed")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsString(pageableRequest)))
                .andExpect(status().isOk)
                .andReturn()

        // then
        val posts = objectMapper.readValue<List<Post>>(mvcResult.response.contentAsString)
        assertThat(posts.size, Is(equalTo(1)))

        val foundPost = posts.get(0)
        assertThat(foundPost.id, Is(equalTo(post.id)))
        assertThat(foundPost.displayed, Is(equalTo(true)))
    }

    @Test
    internal fun `should like a post`() {
        // given
        val post = Post(
                content = "test123",
                author = "test",
                preview = "123",
                tags = "tag1",
                rating = 1)
        postRepository.save(post)

        // when
        mvc.perform(put("/post/${post.id}/like"))
                .andExpect(status().isOk)

        // then
        val posts = postRepository.findAll()
        assertThat(posts.size, Is(equalTo(1)))

        val savedPost = posts.get(0)
        assertThat(savedPost.rating, Is(equalTo(2)))
    }

    @Test
    internal fun `should dislike a post`() {
        // given
        val post = Post(
                content = "test123",
                author = "test",
                preview = "123",
                tags = "tag1",
                rating = 5)
        postRepository.save(post)

        // when
        mvc.perform(put("/post/${post.id}/dislike"))
                .andExpect(status().isOk)

        // then
        val posts = postRepository.findAll()
        assertThat(posts.size, Is(equalTo(1)))

        val savedPost = posts.get(0)
        assertThat(savedPost.rating, Is(equalTo(4)))
    }

    @Test
    internal fun `should return a sorted page with posts`() {
        // given
        val post = Post(
                content = "test123",
                author = "1",
                preview = "123",
                tags = "tag1",
                displayed = true)
        postRepository.save(post)

        val secondPost = Post(
                content = "testewq",
                author = "3",
                preview = "321",
                tags = "tagw",
                displayed = true)
        postRepository.save(secondPost)

        val thirdPost = Post(
                content = "test123",
                author = "2",
                preview = "123",
                tags = "tag1",
                displayed = true)
        postRepository.save(thirdPost)

        val pageableRequest = PageableRequest(
                page = 0,
                size = 2,
                field = "author",
                direction = Sort.Direction.ASC)

        // when
        val mvcResult = mvc.perform(post("/post/displayed")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsString(pageableRequest)))
                .andExpect(status().isOk)
                .andReturn()

        // then
        val posts = objectMapper.readValue<List<Post>>(mvcResult.response.contentAsString)
        assertThat(posts.size, Is(equalTo(2)))

        val foundPost = posts.get(0)
        assertThat(foundPost.id, Is(equalTo(post.id)))

        val foundSecondPost = posts.get(1)
        assertThat(foundSecondPost.id, Is(equalTo(thirdPost.id)))
    }

    @Test
    internal fun `should return full post information`() {
        // given
        val post = Post(
                content = "test123",
                author = "1",
                preview = "123",
                tags = "tag1",
                displayed = true)
        postRepository.save(post)

        // when
        val mvcResult = mvc.perform(get("/post/${post.id}/info"))
                .andExpect(status().isOk)
                .andReturn()

        // then
        val receivedPost = objectMapper.readValue<Post>(mvcResult.response.contentAsString)

        assertThat(receivedPost.id, Is(equalTo(post.id)))
        assertThat(receivedPost.rating, Is(equalTo(post.rating)))
        assertThat(receivedPost.displayed, Is(equalTo(post.displayed)))
        assertThat(receivedPost.creationDate, Is(equalTo(post.creationDate)))
        assertThat(receivedPost.tags, Is(equalTo(post.tags)))
        assertThat(receivedPost.preview, Is(equalTo(post.preview)))
        assertThat(receivedPost.author, Is(equalTo(post.author)))
        assertThat(receivedPost.content, Is(equalTo(post.content)))
    }
}
