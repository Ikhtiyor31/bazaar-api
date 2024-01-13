package com.strawberry.bazaarapi.user.controller

import com.strawberry.bazaarapi.BaseBazaarApiIntegrationTest
import com.strawberry.bazaarapi.user.dto.UserDeviceDto
import com.strawberry.bazaarapi.user.dto.UserDeviceExistResponse
import com.strawberry.bazaarapi.user.enums.PlatformType
import com.strawberry.bazaarapi.user.service.UserDeviceService
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.given
import org.mockito.Mockito.`when`
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.HttpHeaders.*
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.restdocs.headers.HeaderDocumentation.headerWithName
import org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation.*
import org.springframework.restdocs.request.RequestDocumentation.parameterWithName
import org.springframework.restdocs.request.RequestDocumentation.requestParameters
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.lang.Boolean.TRUE


class UserDeviceControllerTest : BaseBazaarApiIntegrationTest() {

    @MockBean
    private lateinit var userDeviceService: UserDeviceService

    @Test
    fun createUserDeviceInfo() {
        val userDeviceDto = UserDeviceDto(
            deviceKey = "UiasdfaSD234e23asdfasdfASDFasfsdfs",
            deviceFcmToken = "eklasdjhfeKLfaklhjkldafsdasdfsd12l3kjLKJsdflk232",
            deviceIpAddress = "212.43.45.12",
            platformType = PlatformType.IOS,
            deviceOsVersion = "12",
            appVersion = "1.0.2"
        )

        `when`(
            userDeviceService.createUserDevice(
                authenticateUser().userInfo(),
                userDeviceDto
            )
        ).thenReturn(
            userDeviceDto.toEntity(
                getUser()
            ).toResponse()
        )

        mockMvc.perform(
            post("/api/v1/user-device")
                .header(CONTENT_TYPE, APPLICATION_JSON)
                .header(ACCEPT, APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(userDeviceDto))
        )
            .andExpect(status().isOk)
            .andDo(MockMvcResultHandlers.print())
            .andDo(
                document(
                    "createUserDeviceInfo",
                    requestHeaders(
                        headerWithName(ACCEPT).description("accept"),
                        headerWithName(CONTENT_TYPE).description("content type")
                    ),
                    requestFields(
                        fieldWithPath("deviceKey").type(JsonFieldType.STRING).description("device uuid key"),
                        fieldWithPath("deviceFcmToken").type(JsonFieldType.STRING).description("device fcm tokens"),
                        fieldWithPath("deviceIpAddress").type(JsonFieldType.STRING)
                            .description("device public IP address"),
                        fieldWithPath("platformType").type(JsonFieldType.STRING).description(
                            "platform type: "
                                    + PlatformType.IOS + ",\n "
                                    + PlatformType.ANDROID + ", \n "
                                    + PlatformType.WEB + "\n"
                        ),
                        fieldWithPath("deviceOsVersion").type(JsonFieldType.STRING).description("device OS version"),
                        fieldWithPath("appVersion").type(JsonFieldType.STRING)
                            .description("currently installed app version"),
                    ),
                    responseFields(
                        fieldWithPath("deviceKey").type(JsonFieldType.STRING).description("saved device key"),
                        fieldWithPath("deviceFcmToken").type(JsonFieldType.STRING).description("saved deviceFcmToken"),
                        fieldWithPath("deviceIpAddress").type(JsonFieldType.STRING)
                            .description("saved user public ip address"),
                        fieldWithPath("platformType").type(JsonFieldType.STRING).description("saved platform type"),
                        fieldWithPath("deviceOsVersion").type(JsonFieldType.STRING)
                            .description("saved device OS version"),
                        fieldWithPath("appVersion").type(JsonFieldType.STRING)
                            .description("saved currently installed app version"),
                    )
                )
            )
    }

    @Test
    fun deleteUserDeviceTest() {

        mockMvc.perform(
            delete("/api/v1/user-device")
                .header(CONTENT_TYPE, APPLICATION_JSON)
                .header(ACCEPT, APPLICATION_JSON_VALUE)
                .header(AUTHORIZATION, authenticateUser())
        )
            .andExpect(status().isOk)
            .andDo(MockMvcResultHandlers.print())
            .andDo(
                document(
                    "deleteUserDevice",
                    requestHeaders(
                        headerWithName(ACCEPT).description("accept"),
                        headerWithName(CONTENT_TYPE).description("content type")
                    )
                )
            )
    }

    @Test
    fun isUserDeviceExist() {
        val deviceKey = "afdadsfadfasdfasdfasdfasdf"
        given(userDeviceService.findUserDevice(authenticateUser().userInfo(), deviceKey)).willReturn(UserDeviceExistResponse(TRUE))


        mockMvc.perform(
            get("/api/v1/user-device")
                .header(CONTENT_TYPE, APPLICATION_JSON)
                .header(ACCEPT, APPLICATION_JSON_VALUE)
                .param("deviceKey", deviceKey)
        )
            .andExpect(status().isOk)
            .andDo(MockMvcResultHandlers.print())
            .andDo(
                document(
                    "isUserDeviceExist",
                    requestHeaders(
                        headerWithName(ACCEPT).description("accept"),
                        headerWithName(CONTENT_TYPE).description("content type")
                    ),
                    requestParameters(
                        parameterWithName("deviceKey").description("device uuid key"),
                    ),
                    responseFields(
                        fieldWithPath("isUserDeviceExist").type(JsonFieldType.BOOLEAN).description("true or false whether user device exists or not"),
                    )
                )
            )
    }
}