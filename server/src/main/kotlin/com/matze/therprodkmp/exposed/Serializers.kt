package com.matze.therprodkmp.exposed

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.time.Instant
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object InstantSerializer : KSerializer<Instant> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("Instant", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: Instant) {
        encoder.encodeString(value.toString())
    }

    override fun deserialize(decoder: Decoder): Instant {
        return Instant.parse(decoder.decodeString())
    }
}

object LocalDateTimeSerializer : KSerializer<LocalDateTime> {
    private val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME

    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("LocalDateTime", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: LocalDateTime) {
        encoder.encodeString(formatter.format(value))
    }

    override fun deserialize(decoder: Decoder): LocalDateTime {
        return LocalDateTime.parse(decoder.decodeString(), formatter)
    }
}


object InstantSerializerNullable : KSerializer<Instant?> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("Instant", PrimitiveKind.STRING)

    @OptIn(ExperimentalSerializationApi::class)
    override fun serialize(encoder: Encoder, value: Instant?) {
        if (value != null) {
            encoder.encodeString(value.toString())
        } else {
            encoder.encodeNull() // Handle null case
        }
    }

    override fun deserialize(decoder: Decoder): Instant? {
        val stringValue = decoder.decodeString()
        return if (stringValue.isNotEmpty()) {
            Instant.parse(stringValue)
        } else {
            null // Handle null case
        }
    }
}