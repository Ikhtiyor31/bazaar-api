package com.strawberry.bazaarapi



import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.ComponentScan
import org.springframework.restdocs.RestDocumentationContextProvider
import org.springframework.restdocs.RestDocumentationExtension
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import org.springframework.web.filter.CharacterEncodingFilter

@ExtendWith(RestDocumentationExtension::class, SpringExtension::class)
@SpringBootTest(classes = [BaseBazaarApiIntegrationTest::class])
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@ComponentScan(basePackages = ["com.strawberry.bazaarapi"])
@EnableAsync
class BaseBazaarApiIntegrationTest {

     @Autowired
     protected lateinit var mockMvc: MockMvc

     protected  val objectMapper: ObjectMapper = ObjectMapper()

     @BeforeEach
     fun setUp(webApplicationContext: WebApplicationContext, restDocumentationContextProvider: RestDocumentationContextProvider) {
          mockMvc = MockMvcBuilders
               .webAppContextSetup(webApplicationContext)
               .apply<DefaultMockMvcBuilder?>(MockMvcRestDocumentation.documentationConfiguration(restDocumentationContextProvider))
               .addFilter<DefaultMockMvcBuilder>(CharacterEncodingFilter("UTF-8", true))
               .alwaysDo<DefaultMockMvcBuilder>(MockMvcResultHandlers.print())
               .build()
     }
}
