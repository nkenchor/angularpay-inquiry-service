package io.angularpay.inquiry.domain.commands;

public interface ResourceReferenceCommand<T, R> {

    R map(T referenceResponse);
}
