package com.gentooway.blog.web

import com.gentooway.blog.model.Comment
import com.gentooway.blog.model.Post
import com.gentooway.blog.repository.CommentRepository
import com.gentooway.blog.repository.PostRepository
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@SpringBootTest
@AutoConfigureMockMvc
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
        mvc.perform(MockMvcRequestBuilders.post("/post/${post.id}/comment")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsString(comment)))
                .andExpect(MockMvcResultMatchers.status().isOk)

        // then
        val postFromDb = postRepository.findById(post.id).orElse(post)
        assertThat(postFromDb.comments.size, Is(equalTo(1)))

        val commentsCount = commentRepository.count()
        assertThat(commentsCount, Is(equalTo(1L)))
    }
}
