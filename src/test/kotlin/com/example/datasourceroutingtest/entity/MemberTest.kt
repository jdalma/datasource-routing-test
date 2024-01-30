package com.example.datasourceroutingtest.entity

import com.example.datasourceroutingtest.repository.MemberRepository
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class MemberTest (
    @Autowired
    private val memberRepository: MemberRepository
) {

    @Test
    fun name() {
        val findMember1 = memberRepository.findById(1).get()
        Assertions.assertThat(findMember1.name).isEqualTo("primary1")

        memberRepository.save(Member(1, "primary1 update"))
        val findMember2 = memberRepository.findById(1).get()
        Assertions.assertThat(findMember2.name).isEqualTo("primary1 update")
    }
}
