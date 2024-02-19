package com.backend.prog.global.common.constant;

/**
 * 모든 상수는 대문자로 작성
 * 코드 prefix : CODE_####
 * 상세코드 : 코드_상세코드
 *   - ex) 코드 : Content, 상세코드 : Work
 *            => CONTENT_WORK
 */
public interface CommonCode {
    // ########################### 코드 ###########################
    String CODE_CONTENT = "CONTENT";
    String CODE_PROJECTSTATUS = "ProjectStatus";
    String CODE_WORKSTATUS = "WorkStatus";
    String CODE_WORKTYPE = "WorkType";
    String CODE_WORKPRIOIRTY = "WorkPrioirty";
    String CODE_JOB = "Job";
    String CODE_ROLE = "Role";
    String CODE_KPT = "KPT";


    // ########################### 상세코드 ###########################
    String CONTENT_WORK = "Work";
    String CONTENT_POST = "Post";
    String CONTENT_RETROSPECT = "Retrospect";
}
