package com.project.counter.model;

import lombok.Builder;

@Builder
public record Word( String word,
                    long count,
                    double frequency) {
}
