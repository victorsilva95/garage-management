package com.estapar.garage.garage_management.aplication.web.config

import com.estapar.garage.garage_management.common.DATE_TIME_FORMAT_PATTERN
import com.estapar.garage.garage_management.common.TIME_ZONE_OFFSET
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer
import com.fasterxml.jackson.datatype.jsr310.ser.ZonedDateTimeSerializer
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder
import java.time.temporal.ChronoField

private const val MIN_NANO_OF_SECOND = 0
private const val MAX_NANO_OF_SECOND = 9

@Configuration
class MapperConfig {

    @Bean
    @Primary
    fun objectMapper(): ObjectMapper = configJsonMapper()
        .setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE)
        .setSerializationInclusion(JsonInclude.Include.ALWAYS)
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

    fun configJsonMapper(): ObjectMapper = jacksonObjectMapper().also {
        it.registerModule(getDateTimeModule())
    }.apply {
        this.registerKotlinModule()
    }

    private fun getDateTimeModule(): JavaTimeModule {
        val formatter = DateTimeFormatterBuilder()
            .appendOptional(DateTimeFormatter.ofPattern(DATE_TIME_FORMAT_PATTERN))
            .appendFraction(ChronoField.NANO_OF_SECOND, MIN_NANO_OF_SECOND, MAX_NANO_OF_SECOND, true)
            .appendOptional(DateTimeFormatter.ofPattern(TIME_ZONE_OFFSET))
            .toFormatter()

        val timeFormatter = DateTimeFormatterBuilder()
            .appendOptional(DateTimeFormatter.ISO_LOCAL_TIME)
            .toFormatter()

        val dateTimeModule = JavaTimeModule()
        dateTimeModule.addSerializer(LocalDateTime::class.java, LocalDateTimeSerializer(formatter))
        dateTimeModule.addDeserializer(LocalDateTime::class.java, LocalDateTimeDeserializer(formatter))
        dateTimeModule.addSerializer(ZonedDateTime::class.java, ZonedDateTimeSerializer(formatter))
        dateTimeModule.addSerializer(LocalDate::class.java, LocalDateSerializer(DateTimeFormatter.ISO_LOCAL_DATE))
        dateTimeModule.addDeserializer(LocalDate::class.java, LocalDateDeserializer(DateTimeFormatter.ISO_LOCAL_DATE))
        dateTimeModule.addSerializer(LocalTime::class.java, LocalTimeSerializer(timeFormatter))
        dateTimeModule.addDeserializer(LocalTime::class.java, LocalTimeDeserializer(timeFormatter))

        return dateTimeModule
    }
}
