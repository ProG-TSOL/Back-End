package com.backend.prog.domain.project.domain;

import jakarta.persistence.Embeddable;

import java.io.Serializable;

@Embeddable
public class ProjectMemberId implements Serializable {
    private Long projectId;
    private Integer memberId;

    // TODO: 신청현황 id hashcode, equals 구현
    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}
