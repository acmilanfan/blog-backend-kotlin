package com.gentooway.blog.errors

import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(BAD_REQUEST)
class PostNotFoundException(message: String?) : RuntimeException(message)