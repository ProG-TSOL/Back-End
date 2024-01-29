package com.backend.prog.domain.board.dto;

public record BoardSaveRequest(Long projectId, Integer memberId, String title, String content, Boolean isNotice) {
}
