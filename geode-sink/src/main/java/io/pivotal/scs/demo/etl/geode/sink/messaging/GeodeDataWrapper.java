package io.pivotal.scs.demo.etl.geode.sink.messaging;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class GeodeDataWrapper {
	private Map<Object, Object> map = new HashMap<>();
	private Set<Object> set = new HashSet<>();

	public Map<Object, Object> getMap() {
		return map;
	}

	public Set<Object> getSet() {
		return set;
	}
}
