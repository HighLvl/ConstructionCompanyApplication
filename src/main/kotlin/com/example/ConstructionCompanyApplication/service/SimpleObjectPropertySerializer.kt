package com.example.ConstructionCompanyApplication.service

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.*
import com.fasterxml.jackson.databind.deser.ContextualDeserializer
import javafx.beans.property.SimpleObjectProperty
import java.time.LocalDate
import kotlin.reflect.full.createInstance

class SimpleObjectPropertySerializer : JsonSerializer<SimpleObjectProperty<*>>() {
    override fun serialize(value: SimpleObjectProperty<*>, gen: JsonGenerator, serializers: SerializerProvider) {
        serializers.defaultSerializeValue(value.value, gen)
    }

    override fun handledType(): Class<SimpleObjectProperty<*>> {
        return SimpleObjectProperty::class.java
    }
}
class SimpleObjectPropertyDeserializer : JsonDeserializer<SimpleObjectProperty<*>>(), ContextualDeserializer {
    private lateinit var valueType: JavaType
    override fun createContextual(ctxt: DeserializationContext, property: BeanProperty): JsonDeserializer<*> {
        return SimpleObjectPropertyDeserializer().apply {
            valueType = ctxt.contextualType.containedType(0)
        }
    }

    override fun deserialize(p: JsonParser, ctxt: DeserializationContext): SimpleObjectProperty<*> {
        return SimpleObjectProperty(ctxt.readValue<Any>(p, valueType))
    }

    override fun handledType(): Class<SimpleObjectProperty<*>> {
        return SimpleObjectProperty::class.java
    }

    override fun getNullValue(ctxt: DeserializationContext?): SimpleObjectProperty<*> {
        return SimpleObjectProperty(null)
    }
}