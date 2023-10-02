package io.angularpay.inquiry.domain.commands;

public interface SensitiveDataCommand<T> {
    T mask(T raw);
}
