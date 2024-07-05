package com.example.datasourceroutingtest.repository

import com.example.datasourceroutingtest.entity.Member
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface MemberRepository: CrudRepository<Member, Int>{
}
