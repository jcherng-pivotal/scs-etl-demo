package io.pivotal.scs.demo.model.etl;

public class PayloadWrapper<T> {
	private final T payload;

	public T getPayload() {
		return payload;
	}

	public boolean hasPayload() {
		return payload != null;
	}

	public PayloadWrapper(T payload) {
		super();
		this.payload = payload;
	}
}
