package com.strawberry.bazaarapi.user.controller

import com.strawberry.bazaarapi.BaseBazaarApiIntegrationTest
import com.strawberry.bazaarapi.user.dto.*
import com.strawberry.bazaarapi.user.enums.Role
import com.strawberry.bazaarapi.user.repository.UserRepository
import com.strawberry.bazaarapi.user.service.UserService
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.given
import org.mockito.Mockito.*
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.HttpHeaders.*
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.restdocs.headers.HeaderDocumentation.headerWithName
import org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation.*
import org.springframework.restdocs.request.RequestDocumentation.parameterWithName
import org.springframework.restdocs.request.RequestDocumentation.requestParameters
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status


class UserControllerTest: BaseBazaarApiIntegrationTest() {

    @MockBean
    private lateinit var userService: UserService

    @MockBean
    private lateinit var userRepository: UserRepository


    @Test
    fun singUpApiTest() {
        val userSignupRequest = UserSignupRequest("abdul","john.doe@example.com", "zfgghbh1995")
        val userSignupResponse = UserSignupResponse(
            1,
            "Please verify your address with a verification code that we have sent to your email!",
            2345)


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
                    fieldWithPath("accessToken").type(JsonFieldType.STRING).description("user access token"),
                    fieldWithPath("refreshToken").type(JsonFieldType.STRING).description("user refresh token")
                )
            ))


        verify(userService, times(1)).signInUser(userLoginRequest)
    }

    @Test
    fun verifyEmailApiTest() {
        val accountVerificationRequest = AccountVerificationRequest("abdul@gmail.com", 345672)
        val accountVerificationResponse = AccountVerificationResponse(true, "your email is successfully confirmed!")


        `when`(userService.verifyEmail(accountVerificationRequest)).thenReturn(accountVerificationResponse)

        mockMvc.perform(post("/api/v1/users/verify-email")
                .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .header(ACCEPT, APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(accountVerificationRequest))
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(status().isOk)
            .andDo(MockMvcRestDocumentation.document("verify-email",
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

    @Test
    fun refreshToken() {
        val refreshTokenRequest = RefreshTokenRequest(
            "eyasdflkjasfLKJlajsfl;kj23lkjasflkj",
            authenticateUser().username
        )

        `when`(userJwtTokenService.generateUserRefreshToken(refreshTokenRequest))
            .thenReturn(UserLoginResponse("eyJ0eXBlIjoiSldUIiwiYWxnIjoiSFM1MTIifQ.eyJzdWIiOiJhYmR1YXppem92MTk5NTMyQGdtYWlsLmNvbSIsImV4cCI6MTY5NjMyMzU1",
                "eyJ0eXBlIjoiSldUIiwiYWxnIjoiSFM1MTIifQ.eyJzdWIiOiJhYmR1YXppem92MTk5NTMyQGdtYWlsLmNvbSIsImV4cCI6MTY5NjMyN"))

        mockMvc.perform(
            post("/api/v1/users/token/refresh")
                .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .header(ACCEPT, APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(refreshTokenRequest)))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(status().isOk)
            .andDo(MockMvcRestDocumentation.document("refreshToken",
                requestHeaders(
                    headerWithName(ACCEPT).description("accept"),
                    headerWithName(CONTENT_TYPE).description("content type")
                ),
                requestFields(
                    fieldWithPath("username").type(JsonFieldType.STRING).description("user email address"),
                    fieldWithPath("refreshToken").type(JsonFieldType.STRING).description("refresh token")
                ),
                responseFields(
                    fieldWithPath("accessToken").type(JsonFieldType.STRING).description("user access token"),
                    fieldWithPath("refreshToken").type(JsonFieldType.STRING).description("user refresh token")
                ))
            )
    }

    @Test
    fun forgotPassword() {
        val email = "abdul@gmail.com"
        `when`(userService.forgotPassword(email)).thenReturn(
            ForgotPasswordResponse(
                "Please enter a verification code that we have sent to your email",
                4567
            )
        )

        mockMvc.perform(
            post("/api/v1/users/forgot-password")
                .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .header(ACCEPT, APPLICATION_JSON_VALUE)
                .param("email", email))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(status().isOk)
            .andDo(MockMvcRestDocumentation.document("forgot-password",
                requestHeaders(
                    headerWithName(ACCEPT).description("accept"),
                    headerWithName(CONTENT_TYPE).description("content type")
                ),
                requestParameters(
                    parameterWithName("email").description("user email address"),
                ),
                responseFields(
                    fieldWithPath("message").type(JsonFieldType.STRING).description("success verification message"),
                    fieldWithPath("verificationCode").type(JsonFieldType.NUMBER).description("verification code to verify your email")
                ))
            )
    }

    @Test
    fun resetPassword() {
        val email = "abdul@gmail.com"
        val passwordResetRequest = PasswordResetRequest(email, "abcd1234", "abcd1234")
        val passwordResetResponse = PasswordResetResponse(HttpStatus.OK, "your password is rest")

        given(userService.resetPassword(passwordResetRequest)).willReturn(passwordResetResponse)

        mockMvc.perform(
            patch("/api/v1/users/reset-password")
                .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .header(ACCEPT, APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(passwordResetRequest)))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(status().isOk)
            .andDo(MockMvcRestDocumentation.document("reset-password",
                requestHeaders(
                    headerWithName(ACCEPT).description("accept"),
                    headerWithName(CONTENT_TYPE).description("content type")
                ),
                requestFields(
                    fieldWithPath("email").type(JsonFieldType.STRING).description("user email address"),
                    fieldWithPath("newPassword").type(JsonFieldType.STRING).description("your new password "),
                    fieldWithPath("confirmNewPassword").type(JsonFieldType.STRING).description("confirm new passowrd")
                ),
                responseFields(
                    fieldWithPath("statusCode").type(JsonFieldType.STRING).description("http status code"),
                    fieldWithPath("message").type(JsonFieldType.STRING).description("confirmation of your changed password")
                ))
            )
    }

    @Test
    fun deleteUserAccount() {
        val email = authenticateUser().email
        given(userService.deleteUserAccount(email)).willReturn("OK")

        mockMvc.perform(
            delete("/api/v1/users/delete-account")
                .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .header(ACCEPT, APPLICATION_JSON_VALUE))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(status().isOk)
            .andDo(MockMvcRestDocumentation.document("delete-account",
                requestHeaders(
                    headerWithName(ACCEPT).description("accept"),
                    headerWithName(CONTENT_TYPE).description("content type")
                )
            ))
    }

}