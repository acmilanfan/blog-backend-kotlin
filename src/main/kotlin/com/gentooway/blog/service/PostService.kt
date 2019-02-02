package com.gentooway.blog.service

import com.gentooway.blog.model.Post
import com.gentooway.blog.repository.PostRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class PostService(private val postRepository: PostRepository) {

    fun getAllPosts(): List<Post> {
        return postRepository.findAll()
    }

    fun createOrUpdatePost(post: Post) {
        postRepository.save(post)
    }

    fun deletePost(id: Long) {
        postRepository.deleteById(id)
    }

    fun getByAuthor(author: String): List<Post> {
        return postRepository.getAllByAuthor(author)
    }

    fun changeDisplayed(id: Long) {
        val post = retrievePost(id)

        val updated = post.copy(displayed = !post.displayed)

        postRepository.save(updated)
    }

    fun getDisplayedPosts(): List<Post> {
        return postRepository.getAllByDisplayedTrue()
    }

    fun like(id: Long) {
        val post = retrievePost(id)

        val updated = post.copy(rating = post.rating.inc())

        postRepository.save(updated)
    }

    private fun retrievePost(id: Long) = postRepository.findById(id)
            .orElseThrow { IllegalArgumentException("Post with the given id not found") }
}
