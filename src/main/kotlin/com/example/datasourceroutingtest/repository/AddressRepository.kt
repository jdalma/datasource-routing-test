package com.example.datasourceroutingtest.repository

import com.example.datasourceroutingtest.entity.Address
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface AddressRepository: CrudRepository<Address, Int>{

    override fun findAll(): List<Address>

    override fun findById(id: Int): Optional<Address>

    override fun <S : Address> save(entity: S): S
}
