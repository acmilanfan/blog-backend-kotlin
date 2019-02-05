package com.gentooway.blog.service

import com.gentooway.blog.model.Comment
import com.gentooway.blog.model.Post
import com.gentooway.blog.repository.CommentRepository
import com.gentooway.blog.repository.PostRepository
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.*
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import java.util.*

internal class CommentServiceTest {

    @Mock
    private lateinit var postRepository: PostRepository

    @Mock
    private lateinit var commentRepository: CommentRepository

    @InjectMocks
    private lateinit var commentService: CommentService

    @BeforeEach
    internal fun setUp() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    internal fun `should throw exception while creating comment if post not found`() {
        // given
        Mockito.`when`(postRepository.findById(ArgumentMatchers.any())).thenReturn(Optional.empty())

        val post = Post(
                content = "test123",
                author = "test",
                preview = "123",
                tags = "tag1")

        val comment = Comment(
                content = "test comment",
                author = "test132",
                post = post)

        // when
        val executable = { commentService.create(123L, comment) }

        // then
        assertThrows<IllegalArgumentException>(executable)
    }

    @Test
    internal fun `should throw exception if comment not found`() {
        // given
        Mockito.`when`(commentRepository.findById(ArgumentMatchers.any())).thenReturn(Optional.empty())

        // when
        val executable = { commentService.changeDisplayed(123L) }

        // then
        assertThrows<IllegalArgumentException>(executable)
    }

    @Test
    internal fun `should update post displayed value and save`() {
        // given
        val post = Post(
                content = "test123",
                author = "test",
                preview = "123",
                tags = "tag1")

        val comment = Comment(
                content = "test comment",
                author = "test132",
                post = post,
                displayed = true)

        `when`(commentRepository.findById(ArgumentMatchers.any())).thenReturn(Optional.ofNullable(comment))

        // when
        commentService.changeDisplayed(123L)

        // then
        val captor: ArgumentCaptor<Comment> = ArgumentCaptor.forClass(Comment::class.java)

        verify(commentRepository).save(captor.capture())

        assertThat(captor.value.displayed, Is(equalTo(false)))
    }
}