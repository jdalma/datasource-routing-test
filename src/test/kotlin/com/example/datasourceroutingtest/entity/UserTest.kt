package com.example.datasourceroutingtest.entity

import com.example.datasourceroutingtest.repository.UserRepository
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest

@DataJpaTest
class UserTest (
    @Autowired
    private val userRepository: UserRepository
) {

    @Test
    fun name() {
        val saveUser = userRepository.save(User("testA"))
        val findUser = userRepository.findById(saveUser.id!!).get()

        Assertions.assertThat(saveUser.name).isEqualTo(findUser.name)
    }
}
