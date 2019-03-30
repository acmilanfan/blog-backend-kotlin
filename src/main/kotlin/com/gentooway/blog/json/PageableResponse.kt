package com.gentooway.blog.json

import com.gentooway.blog.model.Post

/**
 * Class for a pageable response.
 */
data class PageableResponse(
        val content: List<Post>,
        val page: Int,
        val totalElements: Long,
        val totalPages: Int
)