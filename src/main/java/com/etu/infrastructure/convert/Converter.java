package com.etu.infrastructure.convert;

import java.util.List;

import static java.util.stream.Collectors.toList;

// TODO: 23.05.2020 get rid of
public interface Converter<SOURCE, TARGET> {
    TARGET convert(SOURCE source);

    default List<TARGET> convertAll(List<SOURCE> sources) {
        return sources.stream()
                .map(this::convert)
                .collect(toList());
    }
}
