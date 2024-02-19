package com.backend.prog.domain.manager.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CodeResponse {
    private Integer id;
    private String name;
    private String description;
    private Boolean isUse;

}
