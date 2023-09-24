package com.strawberry.bazaarapi.common.config

import com.strawberry.bazaarapi.util.TimeUtil.getUZTZoneId
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.auditing.DateTimeProvider
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import java.time.LocalDateTime
import java.util.Optional

@Configuration
@EnableJpaAuditing(dateTimeProviderRef = "dateTimeProvider")
class JpaAuditingConfig {

    @Bean
    fun dateTimeProvider(): DateTimeProvider {
        return DateTimeProvider { Optional.of(LocalDateTime.now(getUZTZoneId())) }
    }
}