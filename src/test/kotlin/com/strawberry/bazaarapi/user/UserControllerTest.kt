package com.strawberry.bazaarapi.user

import com.strawberry.bazaarapi.BaseBazaarApiIntegrationTest
import com.strawberry.bazaarapi.user.dto.*
import com.strawberry.bazaarapi.user.repository.UserRepository
import com.strawberry.bazaarapi.user.service.UserJwtTokenService
import com.strawberry.bazaarapi.user.service.UserService
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.HttpHeaders.ACCEPT
import org.springframework.http.HttpHeaders.CONTENT_TYPE
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.restdocs.headers.HeaderDocumentation.headerWithName
import org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation.*
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status


class UserControllerTest: BaseBazaarApiIntegrationTest() {

    @MockBean
    private lateinit var userService: UserService

    @MockBean
    private lateinit var userJwtTokenService: UserJwtTokenService

    @MockBean
    private lateinit var userRepository: UserRepository


    @Test
    fun singUpApiTest() {
        val userSignupRequest = UserSignupRequest("abdul","john.doe@example.com", "zfgghbh1995")
        val userSignupResponse = UserSignupResponse(
            1,
            "Please verify your address with a verification code that we have sent to your email!",
            234567)


        `when`(userService.createUser(userSignupRequest)).thenReturn(userSignupResponse)

        mockMvc.perform(post("/api/v1/users/signup")
                .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .header(ACCEPT, APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(userSignupRequest)))
            .andExpect(status().isOk)
            .andDo(MockMvcRestDocumentation.document("signup",
                requestHeaders(
                    headerWithName(ACCEPT).description("accept"),
                    headerWithName(CONTENT_TYPE).description("content type")
                ),
                requestFields(
                    fieldWithPath("name").type(JsonFieldType.STRING).description("user name"),
                    fieldWithPath("email").type(JsonFieldType.STRING).description("user email address"),
                    fieldWithPath("password").type(JsonFieldType.STRING).description("password must be at 6 characters"),
                ),
                responseFields(
                    fieldWithPath("userId").type(JsonFieldType.NUMBER).description("a new created user ID"),
                    fieldWithPath("message").type(JsonFieldType.STRING).description("message to show user"),
                    fieldWithPath("verificationCode").type(JsonFieldType.NUMBER).description("verification code")
                )
            ))

        verify(userService, times(1)).createUser(userSignupRequest)
    }

    @Test
    fun signApiTest() {
        val userLoginRequest = UserLoginRequest("tourist@example.com", "password")
        val userLoginResponse = UserLoginResponse("accessToken", "refresh-token")

        `when`(userService.signInUser(userLoginRequest)).thenReturn(userLoginResponse)

        mockMvc.perform(
            post("/api/v1/users/signin")
                .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .header(ACCEPT, APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(userLoginRequest)))
            .andExpect(status().isOk)
            .andDo(MockMvcRestDocumentation.document("signin",
                requestHeaders(
                    headerWithName(ACCEPT).description("accept"),
                    headerWithName(CONTENT_TYPE).description("content type")
                ),
                requestFields(
                    fieldWithPath("email").type(JsonFieldType.STRING).description("user email address"),
                    fieldWithPath("password").type(JsonFieldType.STRING).description("user password")
                ),
                responseFields(
                    fieldWithPath("userAccessToken").type(JsonFieldType.STRING).description("Email confirmation status"),
                    fieldWithPath("userRefreshToken").type(JsonFieldType.STRING).description("Confirmation message")
                )
            ))


        verify(userService, times(1)).signInUser(userLoginRequest)
    }

    @Test
    fun verifyEmailApiTest() {
        val accountVerificationRequest = AccountVerificationRequest("abdul@gmail.com", 345672)
        val accountVerificationResponse = AccountVerificationResponse(true, "your email is successfully confirmed!")


        `when`(userService.verifyEmail(accountVerificationRequest)).thenReturn(accountVerificationResponse)

        mockMvc.perform(post("/api/v1/users/confirm-email")
                .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .header(ACCEPT, APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(accountVerificationRequest))
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(status().isOk)
            .andDo(MockMvcRestDocumentation.document("confirm-email",
                requestHeaders(
                    headerWithName(ACCEPT).description("accept"),
                    headerWithName(CONTENT_TYPE).description("content type")
                ),
                requestFields(
                    fieldWithPath("email").type(JsonFieldType.STRING).description("user email address"),
                    fieldWithPath("confirmationCode").type(JsonFieldType.NUMBER).description("confirmation code")
                ),
                responseFields(
                    fieldWithPath("isConfirmed").type(JsonFieldType.BOOLEAN).description("email confirmation status"),
                    fieldWithPath("message").type(JsonFieldType.STRING).description("successful or failed confirmation message")
                ))
            )

        verify(userService, times(1)).verifyEmail(accountVerificationRequest)
    }

}