package com.strawberry.bazaarapi.admin

import com.strawberry.bazaarapi.BaseBazaarApiIntegrationTest
import com.strawberry.bazaarapi.admin.service.AdminService
import com.strawberry.bazaarapi.user.dto.UpdateUserRoleDto
import com.strawberry.bazaarapi.user.enums.Role
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.restdocs.headers.HeaderDocumentation
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

class AdminController : BaseBazaarApiIntegrationTest() {

    @MockBean
    private lateinit var adminService: AdminService

    @Test
    fun updateUserRoleTest() {
        val updateUserRoleDto = UpdateUserRoleDto(authenticateAdminUser().username, Role.MANAGER)

        Mockito.`when`(adminService.updateUserRole(updateUserRoleDto))
            .thenReturn(updateUserRoleDto.toResponse(updateUserRoleDto.email, updateUserRoleDto.userRole))

        mockMvc.perform(
            MockMvcRequestBuilders.patch("/api/v1/admin/update-role")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(updateUserRoleDto)))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andDo(
                MockMvcRestDocumentation.document("update-role",
                    HeaderDocumentation.requestHeaders(
                        HeaderDocumentation.headerWithName(HttpHeaders.ACCEPT)
                            .description("accept"),
                        HeaderDocumentation.headerWithName(HttpHeaders.CONTENT_TYPE)
                            .description("content type")
                    ),
                    PayloadDocumentation.requestFields(
                        PayloadDocumentation.fieldWithPath("email").type(JsonFieldType.STRING)
                            .description("user email address"),
                        PayloadDocumentation.fieldWithPath("userRole").type(JsonFieldType.STRING)
                            .description("user role to be updated")
                    ),
                    PayloadDocumentation.responseFields(
                        PayloadDocumentation.fieldWithPath("email").type(JsonFieldType.STRING)
                            .description("user email address"),
                        PayloadDocumentation.fieldWithPath("userRole").type(JsonFieldType.STRING)
                            .description("updated user role")
                    )
                )
            )
    }


}