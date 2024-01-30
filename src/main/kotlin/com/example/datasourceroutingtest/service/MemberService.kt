package com.example.datasourceroutingtest.service

import com.example.datasourceroutingtest.repository.MemberRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class MemberService (
    private val memberRepository: MemberRepository
) {

    @Transactional(readOnly = true)
    fun findByIdReadOnly(id: Int) = memberRepository.findById(id)

    @Transactional(readOnly = false)
    fun findById(id: Int) = memberRepository.findById(id)
}
