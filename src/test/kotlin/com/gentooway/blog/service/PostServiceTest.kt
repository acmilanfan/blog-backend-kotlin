package com.gentooway.blog.service

import com.gentooway.blog.errors.PostNotFoundException
import com.gentooway.blog.model.Post
import com.gentooway.blog.repository.PostRepository
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.*
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.*
import java.util.*

internal class PostServiceTest {

    @Mock
    private lateinit var postRepository: PostRepository

    @InjectMocks
    private lateinit var postService: PostService

    @BeforeEach
    internal fun setUp() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    internal fun `should throw exception when post not found while changing displayed`() {
        // given
        Mockito.`when`(postRepository.findById(any())).thenReturn(Optional.empty())

        // when
        val executable = { postService.changeDisplayed(123L) }

        // then
        assertThrows<PostNotFoundException>(executable)
    }

    @Test
    internal fun `should throw exception when post not found while liking a post`() {
        // given
        Mockito.`when`(postRepository.findById(any())).thenReturn(Optional.empty())

        // when
        val executable = { postService.like(123L) }

        // then
        assertThrows<PostNotFoundException>(executable)
    }

    @Test
    internal fun `should throw exception when post not found while disliking a post`() {
        // given
        Mockito.`when`(postRepository.findById(any())).thenReturn(Optional.empty())

        // when
        val executable = { postService.dislike(123L) }

        // then
        assertThrows<PostNotFoundException>(executable)
    }

    @Test
    internal fun `should update post displayed value and save`() {
        // given
        val post = Post(
                title = "test",
                content = "test123",
                author = "test",
                preview = "123",
                tags = "tag1",
                displayed = true)

        `when`(postRepository.findById(any())).thenReturn(Optional.ofNullable(post))

        // when
        postService.changeDisplayed(123L)

        // then
        val captor: ArgumentCaptor<Post> = ArgumentCaptor.forClass(Post::class.java)

        verify(postRepository).save(captor.capture())

        assertThat(captor.value.displayed, Is(equalTo(false)))
    }

    @Test
    internal fun `should increment post rating`() {
        // given
        val post = Post(
                title = "test",
                content = "test123",
                author = "test",
                preview = "123",
                tags = "tag1",
                rating = 10)

        Mockito.`when`(postRepository.findById(any())).thenReturn(Optional.ofNullable(post))

        // when
        postService.like(123L)

        // then
        val captor: ArgumentCaptor<Post> = ArgumentCaptor.forClass(Post::class.java)

        verify(postRepository).save(captor.capture())

        assertThat(captor.value.rating, Is(equalTo(11)))
    }

    @Test
    internal fun `should decrement post rating`() {
        // given
        val post = Post(
                title = "test",
                content = "test123",
                author = "test",
                preview = "123",
                tags = "tag1",
                rating = 10)

        Mockito.`when`(postRepository.findById(any())).thenReturn(Optional.ofNullable(post))

        // when
        postService.dislike(123L)

        // then
        val captor: ArgumentCaptor<Post> = ArgumentCaptor.forClass(Post::class.java)

        verify(postRepository).save(captor.capture())

        assertThat(captor.value.rating, Is(equalTo(9)))
    }

    @Test
    internal fun `should not decrement if post rating is zero`() {
        // given
        val post = Post(
                title = "test",
                content = "test123",
                author = "test",
                preview = "123",
                tags = "tag1")

        Mockito.`when`(postRepository.findById(any())).thenReturn(Optional.ofNullable(post))

        // when
        postService.dislike(123L)

        // then
        verify(postRepository, never()).save(any())
    }
}