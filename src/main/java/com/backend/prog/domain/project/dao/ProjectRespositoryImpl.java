package com.backend.prog.domain.project.dao;

import com.backend.prog.domain.project.domain.Project;
import com.backend.prog.domain.project.domain.QProjectMember;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

import static com.backend.prog.domain.project.domain.QApplicationStatus.applicationStatus;
import static com.backend.prog.domain.project.domain.QProject.project;
import static com.backend.prog.domain.project.domain.QProjectTechCode.projectTechCode;
import static org.springframework.util.StringUtils.hasText;


public class ProjectRespositoryImpl implements ProjectRespositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    public ProjectRespositoryImpl(EntityManager entityManager) {
        this.jpaQueryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public Page<Project> getProject(String keyword, Integer techCodes, Integer statusCode, Pageable pageable) {
        List<Project> projects = jpaQueryFactory
                .select(project)
                .from(project)
                .where(project.isDeleted.eq(false), keywordEq(keyword), statusCodeEq(statusCode), techCodesEq(techCodes))
                .groupBy(project.id)
                .orderBy(project.createdAt.desc())
                .fetch();

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), projects.size());

        return new PageImpl<>(projects.subList(start, end), pageable, projects.size());
    }

    @Override
    public List<Project> findHotProject() {
        return jpaQueryFactory
                .select(project)
                .from(project)
                .where(createAtAfterOneWeekAgo(), project.isDeleted.eq(false))
                .orderBy(project.viewCnt.desc())
                .limit(16)
                .fetch();
    }

    @Override
    public List<Project> findMyProjectByStatus(Integer memberId, Integer statusCode) {
        QProjectMember projectMember = new QProjectMember("projectMember");

        return jpaQueryFactory.selectFrom(project)
                .where(project.in(
                                JPAExpressions.select(projectMember.project)
                                        .from(projectMember)
                                        .where(projectMember.member.id.eq(memberId))
                        ), statusCodeEq(statusCode)
                ).fetch();
    }

    @Override
    public List<Project> findAllMySignedProject(Integer memberId) {
        return jpaQueryFactory.selectFrom(project)
                .where(project.id.in(
                        JPAExpressions.select(applicationStatus.project.id)
                                .from(applicationStatus)
                                .where(applicationStatus.member.id.eq(memberId))
                )).fetch();
    }

    private BooleanExpression createAtAfterOneWeekAgo() {
        LocalDateTime oneWeekAgo = LocalDateTime.now().minusWeeks(1);
        return project.createdAt.after(oneWeekAgo);
    }

    private BooleanExpression keywordEq(String keyword) {
        return hasText(keyword) ? project.title.contains(keyword) : null;
    }

    private BooleanExpression statusCodeEq(Integer statusCode) {
        return statusCode != null ? project.statusCode.id.eq(statusCode) : null;
    }

    private BooleanExpression techCodesEq(Integer techCodes) {
        return techCodes != null ? project.id.in(
                JPAExpressions.select(projectTechCode.project.id)
                        .from(projectTechCode)
                        .where(projectTechCode
                                .techCode.id.eq(techCodes)
                        )
        ) : Expressions.asBoolean(true).isTrue();
    }
}
