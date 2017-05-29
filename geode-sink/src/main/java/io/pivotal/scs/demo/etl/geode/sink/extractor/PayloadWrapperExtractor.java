package io.pivotal.scs.demo.etl.geode.sink.extractor;

import io.pivotal.scs.demo.model.etl.PayloadWrapper;

public interface PayloadWrapperExtractor<P, T> {
	T extractData(PayloadWrapper<P> payloadWrapper);
}
