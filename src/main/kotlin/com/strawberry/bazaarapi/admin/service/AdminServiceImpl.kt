package com.strawberry.bazaarapi.admin.service

import com.strawberry.bazaarapi.common.exception.ExceptionMessage
import com.strawberry.bazaarapi.common.exception.ApiAuthenticationException
import com.strawberry.bazaarapi.user.dto.UpdateUserRoleDto
import com.strawberry.bazaarapi.user.repository.UserRepository
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
@Transactional
class AdminServiceImpl(
    private val userRepository: UserRepository
): AdminService {

    override fun updateUserRole(updateUserRoleDto: UpdateUserRoleDto): UpdateUserRoleDto {
        val user = userRepository.findByEmail(updateUserRoleDto.email)
            ?: throw ApiAuthenticationException(ExceptionMessage.USER_NOT_EXIST)

        user.updateUserRole(updateUserRoleDto.userRole)

        return UpdateUserRoleDto(user.email, user.role)
    }

}