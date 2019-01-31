package com.gentooway.blog.web

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.gentooway.blog.model.Post
import com.gentooway.blog.repository.PostRepository
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.time.LocalDate

@SpringBootTest
@AutoConfigureMockMvc
class PostControllerTest {

    @Autowired
    private lateinit var mvc: MockMvc

    @Autowired
    private lateinit var postRepository: PostRepository

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @BeforeEach
    internal fun setUp() {
        postRepository.deleteAll()
    }

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
        val mvcResult = mvc.perform(get("/post/$testAuthor" ))
                .andExpect(status().isOk)
                .andReturn()

        // then
        val posts = objectMapper.readValue<List<Post>>(mvcResult.response.contentAsString)
        assertThat(posts.size, Is(equalTo(1)))

        val foundPost = posts.get(0)
        assertThat(foundPost.id, Is(equalTo(post.id)))
    }
}
