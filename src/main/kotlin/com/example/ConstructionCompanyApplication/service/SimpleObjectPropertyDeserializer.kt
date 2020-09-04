package com.example.ConstructionCompanyApplication.service

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.BeanProperty
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JavaType
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.deser.ContextualDeserializer
import javafx.beans.property.SimpleObjectProperty

/*Десериализует поля в javafx SimpleObjectProperty, используемые в gui сущностях*/
class SimpleObjectPropertyDeserializer : JsonDeserializer<SimpleObjectProperty<*>>(),
    ContextualDeserializer {
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