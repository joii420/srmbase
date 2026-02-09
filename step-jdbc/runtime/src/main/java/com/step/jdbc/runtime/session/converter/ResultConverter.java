package com.step.jdbc.runtime.session.converter;

public interface ResultConverter {

    Object convert(String type, Object object);
}
