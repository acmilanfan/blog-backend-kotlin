package com.gentooway.blog.service

import com.gentooway.blog.model.Post
import com.gentooway.blog.repository.PostRepository
import org.springframework.stereotype.Service

@Service
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
}
