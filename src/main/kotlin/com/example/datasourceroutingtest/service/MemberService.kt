package com.example.datasourceroutingtest.service

import com.example.datasourceroutingtest.entity.Member
import com.example.datasourceroutingtest.repository.MemberRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class MemberService (
    private val memberRepository: MemberRepository
) {

    @Transactional(readOnly = false)
    fun findByIdPrimary(id: Int) = memberRepository.findById(id)

    @Transactional(readOnly = true)
    fun findByIdSecondary(id: Int) = memberRepository.findById(id)

    fun findByIdDefault(id: Int) = memberRepository.findById(id)

    @Transactional(readOnly = false)
    fun findAllPrimary(): Iterable<Member> = memberRepository.findAll()

    @Transactional(readOnly = true)
    fun findAllSecondary(): Iterable<Member> = memberRepository.findAll()
}
