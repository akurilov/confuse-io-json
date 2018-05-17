package com.github.akurilov.confuse.io.json;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface TypeNames {

	Map<String, Class> MAP = new HashMap<String, Class>() {{
		put("any", Object.class);
		put("boolean", boolean.class);
		put("byte", byte.class);
		put("int16", short.class);
		put("char", char.class);
		put("int32", int.class);
		put("int64", long.class);
		put("float32", float.class);
		put("float64", double.class);
		put("void", void.class);
		put("string", String.class);
		put("list", List.class);
		put("map", Map.class);
	}};
}
