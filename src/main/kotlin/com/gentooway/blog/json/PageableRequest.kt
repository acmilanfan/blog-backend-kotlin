package com.gentooway.blog.json

import org.springframework.data.domain.Sort

data class PageableRequest(
        val page: Int,
        val size: Int,
        val field: String = "creationDate",
        val direction: Sort.Direction = Sort.Direction.DESC
)
