package com.matze.therprodkmp.utils

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

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
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("LocalDateTime", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: LocalDateTime) {
        encoder.encodeString(formatLocalDateTime(value))
    }

    override fun deserialize(decoder: Decoder): LocalDateTime {
        return LocalDateTime.parse(decoder.decodeString())
    }
}

fun formatLocalDateTime(dateTime: LocalDateTime): String {
    val year = dateTime.year.toString().padStart(4, '0')
    val month = dateTime.monthNumber.toString().padStart(2, '0')
    val day = dateTime.dayOfMonth.toString().padStart(2, '0')
    val hour = dateTime.hour.toString().padStart(2, '0')
    val minute = dateTime.minute.toString().padStart(2, '0')
    val second = dateTime.second.toString().padStart(2, '0')

    return "$year-$month-${day}T$hour:$minute:$second"
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