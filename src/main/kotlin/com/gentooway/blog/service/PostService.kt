package com.gentooway.blog.service

import com.gentooway.blog.errors.ExceptionDescription.Companion.POST_NOT_FOUND
import com.gentooway.blog.errors.PostNotFoundException
import com.gentooway.blog.json.PageableRequest
import com.gentooway.blog.json.PageableResponse
import com.gentooway.blog.json.SearchRequest
import com.gentooway.blog.model.Post
import com.gentooway.blog.repository.PostRepository
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * Service for the posts entities logic
 */
@Service
@Transactional
class PostService(private val postRepository: PostRepository) {

    fun getAllPosts(): List<Post> {
        return postRepository.findAll()
    }

    fun getById(id: Long): Post {
        return retrievePost(id)
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

    fun getDisplayedPosts(request: PageableRequest): PageableResponse {
        val pageRequest = PageRequest.of(request.page - 1, request.size, Sort.by(request.direction, request.field))

        val page = postRepository.getAllByDisplayedTrue(pageRequest)

        return PageableResponse(
                content = page.content,
                totalElements = page.totalElements,
                totalPages = page.totalPages)
    }

    fun like(id: Long) {
        val post = retrievePost(id)

        val updated = post.copy(rating = post.rating.inc())

        postRepository.save(updated)
    }

    fun dislike(id: Long) {
        val post = retrievePost(id)

        if (post.rating > 0) {
            val updated = post.copy(rating = post.rating.dec())
            postRepository.save(updated)
        }
    }

    fun getMostPopular(request: PageableRequest): List<Post> {
        val pageRequest = PageRequest.of(request.page - 1, request.size)

        return postRepository.getAllByOrderByComments(pageRequest).content
    }

    fun searchByContent(searchRequest: SearchRequest): List<Post> {
        return postRepository.findAllByContentContainingIgnoreCase(searchRequest.content)
    }

    private fun retrievePost(id: Long) =
            postRepository.findById(id)
                    .orElseThrow { PostNotFoundException(POST_NOT_FOUND) }
}
