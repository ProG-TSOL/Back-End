package com.backend.prog.domain.board.dto;

public record BoardModifyRequest(String title, String content, Boolean isNotice) {
}
