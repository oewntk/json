package org.oewntk.json.out

import kotlinx.serialization.ExperimentalSerializationApi

@OptIn(ExperimentalSerializationApi::class)
@kotlinx.serialization.Serializable
@kotlinx.serialization.json.JsonClassDiscriminator("#type")
sealed class Value {
    @kotlinx.serialization.Serializable
    @kotlinx.serialization.SerialName("null")
    object NullValue : Value()

    @kotlinx.serialization.Serializable
    @kotlinx.serialization.SerialName("bool")
    data class BoolValue(@kotlinx.serialization.SerialName("#val") val v: Boolean) : Value()

    @kotlinx.serialization.Serializable
    @kotlinx.serialization.SerialName("int")
    data class IntValue(@kotlinx.serialization.SerialName("#val") val v: Int) : Value()

    @kotlinx.serialization.Serializable
    @kotlinx.serialization.SerialName("long")
    data class LongValue(@kotlinx.serialization.SerialName("#val") val v: Long) : Value()

    @kotlinx.serialization.Serializable
    @kotlinx.serialization.SerialName("float")
    data class FloatValue(@kotlinx.serialization.SerialName("#val") val v: Float) : Value()

    @kotlinx.serialization.Serializable
    @kotlinx.serialization.SerialName("double")
    data class DoubleValue(@kotlinx.serialization.SerialName("#val") val v: Double) : Value()

    @kotlinx.serialization.Serializable
    @kotlinx.serialization.SerialName("char")
    data class CharValue(@kotlinx.serialization.SerialName("#val") val v: Char) : Value()

    @kotlinx.serialization.Serializable
    @kotlinx.serialization.SerialName("string")
    data class StringValue(@kotlinx.serialization.SerialName("#val") val v: String) : Value()

    @kotlinx.serialization.Serializable
    @kotlinx.serialization.SerialName("list")
    data class ListValue(@kotlinx.serialization.SerialName("#val") val v: List<Value>) : Value()

    @kotlinx.serialization.Serializable
    @kotlinx.serialization.SerialName("set")
    data class SetValue(@kotlinx.serialization.SerialName("#val") val v: Set<Value>) : Value()

    @kotlinx.serialization.Serializable
    @kotlinx.serialization.SerialName("map")
    data class MapValue(@kotlinx.serialization.SerialName("#val") val v: Map<String, Value>) : Value()
}

fun Any?.toValue(): Value = when (this) {
    null -> Value.NullValue
    is Boolean -> Value.BoolValue(this)
    is Int -> Value.IntValue(this)
    is Long -> Value.LongValue(this)
    is Float -> Value.FloatValue(this)
    is Double -> Value.DoubleValue(this)
    is Char -> Value.CharValue(this)
    is String -> Value.StringValue(this)
    is Array<*> -> Value.ListValue(this.map { it.toValue() })
    is List<*> -> Value.ListValue(this.map { it.toValue() })
    is Set<*> -> Value.SetValue(this.map { it.toValue() }.toSet())
    is Map<*, *> -> Value.MapValue(this.entries.associate { (k, v) -> (k as String) to v.toValue() })
    else -> error("Unsupported type: ${this.let { it::class }}")
}

fun Value?.fromValue(): Any = when (this) {
    Value.NullValue -> "null"
    is Value.BoolValue -> v
    is Value.IntValue -> v
    is Value.LongValue -> v
    is Value.FloatValue -> v
    is Value.DoubleValue -> v
    is Value.CharValue -> v
    is Value.StringValue -> v
    is Value.ListValue -> v.map { it.fromValue() }
    is Value.SetValue -> v.map { it.fromValue() }.toSet()
    is Value.MapValue -> v.mapValues { (_, v) -> v.fromValue() }
    else -> error("Unsupported type: ${this?.let { it::class }}")
}