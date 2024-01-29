package com.backend.prog.domain.member.domain;

import jakarta.persistence.Embeddable;

import java.io.Serializable;

@Embeddable
public class MemberTechId implements Serializable {
    private Integer memberId;

    private Integer techCode;

    // TODO : MemberTechId hashcode, equals 구현

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}
