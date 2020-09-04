package com.example.ConstructionCompanyApplication.service

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import javafx.beans.property.SimpleObjectProperty

/*Сериализует javafx SimpleObjectProperty, используемые в gui сущностях*/
class SimpleObjectPropertySerializer : JsonSerializer<SimpleObjectProperty<*>>() {
    override fun serialize(value: SimpleObjectProperty<*>, gen: JsonGenerator, serializers: SerializerProvider) {
        serializers.defaultSerializeValue(value.value, gen)
    }

    override fun handledType(): Class<SimpleObjectProperty<*>> {
        return SimpleObjectProperty::class.java
    }
}

