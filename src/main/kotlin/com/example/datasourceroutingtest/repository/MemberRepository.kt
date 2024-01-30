package com.example.datasourceroutingtest.repository

import com.example.datasourceroutingtest.entity.Member
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface MemberRepository: CrudRepository<Member, Int>{

    override fun findAll(): List<Member>

    override fun findById(id: Int): Optional<Member>

    override fun <S : Member> save(entity: S): S
}
