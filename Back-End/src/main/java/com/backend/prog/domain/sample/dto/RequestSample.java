package com.backend.prog.domain.sample.dto;

import lombok.Data;

@Data
public class RequestSample {
    private Long id;
    private String name;

    public RequestSample(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public RequestSample() {
    }

}
