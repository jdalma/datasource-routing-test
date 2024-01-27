package com.example.datasourceroutingtest.repository

import com.example.datasourceroutingtest.entity.User
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface UserRepository: CrudRepository<User, Int>{

    override fun findAll(): List<User>

    override fun findById(id: Int): Optional<User>

    override fun <S : User> save(entity: S): S
}
