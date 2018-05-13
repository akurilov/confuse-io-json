package com.github.akurilov.confuse.io.json;

import java.util.HashMap;
import java.util.Map;

public interface PrimitiveTypeNames {

	Map<String, Class> MAP = new HashMap<String, Class>() {{
		put("boolean", boolean.class);
		put("char", char.class);
		put("byte", byte.class);
		put("short", short.class);
		put("int", int.class);
		put("long", long.class);
		put("float", float.class);
		put("double", double.class);
		put("void", void.class);
	}};
}
