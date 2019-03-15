package com.gentooway.blog.web

import com.gentooway.blog.web.ResponseConstants.Companion.OK
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.hamcrest.core.Is
import org.junit.jupiter.api.Test
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

internal class HealthControllerTest : WebControllerTest() {

    @Test
    internal fun `should return ok`() {
        // when
        val mvcResult = mvc.perform(get("/health"))
                .andExpect(status().isOk)
                .andReturn()

        // then
        val content = mvcResult.response.contentAsString
        MatcherAssert.assertThat(content, Is(CoreMatchers.equalTo(OK)))
    }
}