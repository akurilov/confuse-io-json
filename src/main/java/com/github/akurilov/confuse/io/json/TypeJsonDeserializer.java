package com.github.akurilov.confuse.io.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;

public class TypeJsonDeserializer<T extends Class>
extends StdDeserializer<T> {

	public TypeJsonDeserializer(final Class<?> cls) {
		super(cls);
	}

	@Override @SuppressWarnings("unchecked")
	public T deserialize(final JsonParser p, final DeserializationContext ctx)
	throws IOException, JsonProcessingException {
		final String typeName = p.getValueAsString();
		try {
			return (T) Class.forName(typeName);
		} catch(final ClassNotFoundException e) {
			return (T) TypeNames.MAP.get(typeName);
		}
	}
}
