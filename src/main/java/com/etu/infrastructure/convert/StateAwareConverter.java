package com.etu.infrastructure.convert;

import java.util.List;

import static java.util.stream.Collectors.toList;

// TODO: 23.05.2020 get rid of
public interface StateAwareConverter<SOURCE, TARGET, STATE> {
    TARGET convert(SOURCE source, STATE state);

    default List<TARGET> convertAll(List<SOURCE> sources, STATE state) {
        return sources.stream()
                .map(source -> convert(source, state))
                .collect(toList());
    }
}
