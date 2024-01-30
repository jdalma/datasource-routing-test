package com.example.datasourceroutingtest.service

import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class MemberServiceTest (
    @Autowired
    private val memberService: MemberService
) {

    @Test
    fun findById() {
        val id = 1
        assertThat(memberService.findByIdReadOnly(id).get().name).isEqualTo("secondary1")
        assertThat(memberService.findById(id).get().name).isEqualTo("primary1")
    }
}
