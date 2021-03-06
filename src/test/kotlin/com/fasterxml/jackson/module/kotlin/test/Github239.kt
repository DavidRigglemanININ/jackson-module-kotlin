package com.fasterxml.jackson.module.kotlin.test

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.annotation.JsonTypeName

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue

import org.junit.Test
import kotlin.test.assertEquals

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME)
sealed class Github239Either {

    @JsonTypeName("a")
    data class A(var field: String = "") : Github239Either()

    @JsonTypeName("b")
    data class B(var otherField: String = "") : Github239Either()

}

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME)
@JsonSubTypes(
        JsonSubTypes.Type(Github239EitherCustomized.A::class, name = "a"),
        JsonSubTypes.Type(Github239EitherCustomized.B::class, name = "b")
)
sealed class Github239EitherCustomized {

    data class A(var field: String = "") : Github239EitherCustomized()

    data class B(var otherField: String = "") : Github239EitherCustomized()

}

class TestGithub239 {
    val json = """[
        {
            "@type": "a",
            "field": "value"
        },
        {
            "@type": "b",
            "otherField": "1234"
        }
    ]"""

    val mapper = jacksonObjectMapper()

    @Test
    fun test_implicit_subclasses() {

        val array = mapper.readValue<Array<Github239Either>>(json)

        assertEquals(2, array.size)
        assertEquals(Github239Either.A("value"), array[0])
        assertEquals(Github239Either.B("1234"), array[1])

    }

    @Test
    fun test_explicit_subclasses() {

        val array = mapper.readValue<Array<Github239EitherCustomized>>(json)

        assertEquals(2, array.size)
        assertEquals(Github239EitherCustomized.A("value"), array[0])
        assertEquals(Github239EitherCustomized.B("1234"), array[1])

    }

}