package com.strawberry.bazaarapi.mock

import com.strawberry.bazaarapi.user.domain.User
import com.strawberry.bazaarapi.user.enums.Role
import com.strawberry.bazaarapi.user.repository.UserRepository
import org.springframework.boot.test.context.TestComponent
import org.springframework.context.annotation.Bean


@TestComponent
class MockUserFactory(
    private val userRepository: UserRepository
) {

    @Bean(name = ["mockUser"])
    fun createMockUser(): User {
        val user = User().apply {
            this.name = "abdul"
            this.email = "test@gmail.com"
            this.passwordHashed = "123asfsfh"
            this.role = Role.USER
        }
        return userRepository.save(user)
    }
}