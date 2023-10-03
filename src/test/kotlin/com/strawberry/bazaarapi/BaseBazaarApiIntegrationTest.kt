package com.strawberry.bazaarapi



import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.PropertyAccessor
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.strawberry.bazaarapi.user.domain.User
import com.strawberry.bazaarapi.user.domain.UserToken
import com.strawberry.bazaarapi.user.enums.Role
import com.strawberry.bazaarapi.user.service.UserJwtTokenService
import com.strawberry.bazaarapi.util.UserUtil
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.ComponentScan
import org.springframework.restdocs.RestDocumentationContextProvider
import org.springframework.restdocs.RestDocumentationExtension
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation
import org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.context.WebApplicationContext
import org.springframework.web.filter.CharacterEncodingFilter
import java.time.LocalDateTime
import java.util.*


@ExtendWith(RestDocumentationExtension::class, SpringExtension::class)
@SpringBootTest
@Transactional
@ActiveProfiles("test")
@AutoConfigureRestDocs
@ComponentScan("com.strawberry.bazaarapi")
class BaseBazaarApiIntegrationTest {

     @Autowired
     private lateinit var webApplicationContext: WebApplicationContext

     protected lateinit var mockMvc: MockMvc

     protected val objectMapper: ObjectMapper = ObjectMapper()

     @MockBean
     lateinit var userJwtTokenService: UserJwtTokenService

     @BeforeEach
     internal fun setUp(restDocumentationContextProvider: RestDocumentationContextProvider) {
          objectMapper.registerModule(JavaTimeModule())
          mockMvc = MockMvcBuilders
               .webAppContextSetup(webApplicationContext)
               .apply<DefaultMockMvcBuilder?>(
                    MockMvcRestDocumentation.documentationConfiguration(restDocumentationContextProvider)
                         .operationPreprocessors()
                         .withRequestDefaults(prettyPrint())
                         .withResponseDefaults(prettyPrint())
               )
               .addFilter<DefaultMockMvcBuilder>(CharacterEncodingFilter("UTF-8", true))
               .alwaysDo<DefaultMockMvcBuilder>(MockMvcResultHandlers.print())
               .build()
          objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.NONE)
          objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY)
     }

     fun authenticateUser(): User {
          val authToken = UsernamePasswordAuthenticationToken(getUser(), null, getUser().authorities)
          SecurityContextHolder.getContext().authentication = authToken

          Mockito.`when`(userJwtTokenService.getUserAccessToken(getUser().username))
               .thenReturn(getUserToken(getUser().username))
          SecurityContextHolder.getContext().authentication.principal as User

          return SecurityContextHolder.getContext().authentication.principal as User
     }

     fun authenticateAdminUser(): User {
          val authToken = UsernamePasswordAuthenticationToken(getAdminUser(), null, getAdminUser().authorities)
          SecurityContextHolder.getContext().authentication = authToken

          Mockito.`when`(userJwtTokenService.getUserAccessToken(getUser().username))
               .thenReturn(getUserToken(getAdminUser().username))
          SecurityContextHolder.getContext().authentication.principal as User

          return SecurityContextHolder.getContext().authentication.principal as User
     }

     companion object {
          fun getUser(): User {
               return User().apply {
                    this.id = 1L
                    this.name = "abdul"
                    this.email = "test@gmail.com"
                    this.passwordHashed = "123asfsfh"
                    this.role = Role.USER
                    this.joinedAt = LocalDateTime.of(2023, 10, 2, 23, 52, 14, 500_000_000)
                    this.lastLoggedAt = LocalDateTime.of(2023, 10, 2, 23, 52, 14, 500_000_000)
               }
          }

          fun getAdminUser(): User {
               return User().apply {
                    this.id = 1L
                    this.name = "abdul"
                    this.email = "test@gmail.com"
                    this.passwordHashed = "123asfsfh"
                    this.role = Role.ADMIN
                    this.joinedAt = LocalDateTime.of(2023, 10, 2, 23, 52, 14, 500_000_000)
                    this.lastLoggedAt = LocalDateTime.of(2023, 10, 2, 23, 52, 14, 500_000_000)
               }
          }

          private fun getUserToken(username: String): UserToken {
               return UserToken(
                    username = username,
                    accessToken = "eyasfafdaffasdfasdfasdfLKJalkshdfakj",
                    lifetimeHour = UserUtil.ACCESS_TOKEN_LIFETIME_HOUR,
                    expiryAt = Date(System.currentTimeMillis() + (1000 * 60 * 5)),
                    refreshToken = "eyasdflklaskjflkjahsdfLKJKhasdflkjhLKJHasdf"
               )
          }
     }
}
