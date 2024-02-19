package com.backend.prog.domain.member.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Provider {
    GOOGLE("google"), GITHUB("github");

    private final String provider;
}
