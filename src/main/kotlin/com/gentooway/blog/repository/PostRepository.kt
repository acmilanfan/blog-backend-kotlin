package com.gentooway.blog.repository

import com.gentooway.blog.model.Post
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

/**
 * JPA repository for post entity
 */
interface PostRepository : JpaRepository<Post, Long> {

    /**
     * Returns all posts by author
     * @param author author string to filter
     * @return list of posts
     */
    fun getAllByAuthor(author: String): List<Post>

    /**
     * Returns sorted posts page
     * @param pageable page request with page, size and sort information
     * @return sorted posts page
     */
    fun getAllByDisplayedTrue(pageable: Pageable): Page<Post>

    /**
     * Returns posts page of the most popular by comments count
     * @param pageable page request with page, size and sort information
     * @return the most popular posts page
     */
    @Query(
            value = "select p from Post p join p.comments comments group by p order by count(comments) desc",
            countQuery = "select count(p) from Post p"
    )
    fun getAllByOrderByComments(pageable: Pageable): Page<Post>

    /**
     * Returns posts by a content string like
     */
    fun findAllByContentContainingIgnoreCase(content: String): List<Post>
}
