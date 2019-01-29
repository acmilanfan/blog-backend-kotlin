package com.gentooway.blog.web

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.gentooway.blog.model.Post
import com.gentooway.blog.repository.PostRepository
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest
@AutoConfigureMockMvc
class PostControllerTest {

    @Autowired
    private lateinit var mvc: MockMvc

    @Autowired
    private lateinit var postRepository: PostRepository

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Test
    internal fun `should return all posts`() {
        // given
        val post = Post(content = "test123", author = "test", preview = "123")
        postRepository.save(post)

        val secondPost = Post(content = "test321", author = "test1", preview = "321")
        postRepository.save(secondPost)

        // when
        val mvcResult = mvc.perform(get("/post"))
                .andExpect(status().isOk)
                .andReturn()

        // then
        val posts = objectMapper.readValue<List<Post>>(mvcResult.response.contentAsString)
        assertThat(posts.size, Is(equalTo(2)))
    }
}