package com.strawberry.bazaarapi.common.security

import com.strawberry.bazaarapi.user.domain.User
import com.strawberry.bazaarapi.user.dto.UserAdapter
import com.strawberry.bazaarapi.user.repository.UserRepository
import com.strawberry.bazaarapi.user.service.UserJwtTokenService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service
import org.springframework.web.filter.OncePerRequestFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
@Service
class JwtAuthenticationFilter(
    @Autowired private val userJwtTokenService: UserJwtTokenService,
    @Autowired private val userRepository: UserRepository
) : OncePerRequestFilter() {
    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
        val token = extractToken(request)
        if (!token.isNullOrBlank()) {
            val username = userJwtTokenService.extractUsername(token)
            val user: User = userRepository.findByEmail(username)
                    ?: throw UsernameNotFoundException("username doesn't exit with email: $username")

            val userAdapter = UserAdapter(user)
            if (userJwtTokenService.isTokenValid(token, userAdapter)) {
                val authToken = UsernamePasswordAuthenticationToken(userAdapter, null, userAdapter.authorities)
                SecurityContextHolder.getContext().authentication = authToken
            }
        }
        filterChain.doFilter(request, response)
    }



    companion object{
        private const val AUTHORIZATION_HEADER_PREFIX = "Authorization"
        private const val BEARER_TYPE_PREFIX = "Bearer "

        fun extractToken(request: HttpServletRequest): String? {
            val authorization: String? = request.getHeader(AUTHORIZATION_HEADER_PREFIX)
            authorization?.let {
                if(authorization.startsWith(BEARER_TYPE_PREFIX)) {
                    return authorization.substring(BEARER_TYPE_PREFIX.length)
                }
            }
            return null
        }
    }
}