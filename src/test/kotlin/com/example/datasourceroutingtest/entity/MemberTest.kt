package com.example.datasourceroutingtest.entity

import com.example.datasourceroutingtest.repository.MemberRepository
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest

@DataJpaTest
class MemberTest (
    @Autowired
    private val memberRepository: MemberRepository
) {

    @Test
    fun name() {
        val member = Member(1, "읽기전용1")
        memberRepository.save(member)

        val findMember1 = memberRepository.findById(1).get()
        Assertions.assertThat(findMember1.name).isEqualTo("읽기전용1")
    }
}
