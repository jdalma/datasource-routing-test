package com.example.datasourceroutingtest.service

import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class MemberServiceTest (
    @Autowired
    private val memberService: MemberService
) {

    @Test
    fun findAll() {
        assertThat(memberService.findAllPrimary()).hasSize(4)
        assertThat(memberService.findAllSecondary()).hasSize(2)
    }

    @Test
    fun findById() {
        val id = 1
        assertThat(memberService.findByIdSecondary(id).name).isEqualTo("읽기전용1")
        assertThat(memberService.findByIdPrimary(id).name).isEqualTo("읽기/쓰기1")
        assertThat(memberService.findByIdDefault(id).name).isEqualTo("읽기/쓰기1")
    }
}
