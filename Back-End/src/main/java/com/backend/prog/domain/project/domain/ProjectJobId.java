package com.backend.prog.domain.project.domain;

import jakarta.persistence.Embeddable;

import java.io.Serializable;

@Embeddable
public class ProjectJobId implements Serializable {
    private Long projectId;
    private Integer jobCode;

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}
