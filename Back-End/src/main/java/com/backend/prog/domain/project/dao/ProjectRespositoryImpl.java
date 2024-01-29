package com.backend.prog.domain.project.dao;

import com.backend.prog.domain.manager.domain.CodeDetail;
import com.backend.prog.domain.project.domain.Project;
import com.backend.prog.domain.project.domain.ProjectTechCode;
import com.backend.prog.domain.project.domain.QProjectTechCode;
import com.backend.prog.domain.project.dto.ProjectDto;
import com.querydsl.core.QueryResults;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.util.StringUtils;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static com.backend.prog.domain.manager.domain.QCodeDetail.codeDetail;
import static com.backend.prog.domain.project.domain.QProject.project;
import static com.backend.prog.domain.project.domain.QProjectTechCode.*;
import static com.backend.prog.domain.project.domain.QProjectTotal.projectTotal;
import static org.springframework.util.StringUtils.hasText;


public class ProjectRespositoryImpl implements ProjectRespositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    public ProjectRespositoryImpl(EntityManager entityManager){
        this.jpaQueryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public Page<ProjectDto.SimpleResponse> getProjectDetails(Pageable pageable) {
//        QueryResults<ProjectDto.SimpleResponse> results = jpaQueryFactory
//                .select(Projections.constructor(ProjectDto.SimpleResponse.class,
//                        project.title,
//                        project.projectImgUrl,
//                        project.statusCode,
//                        project.techCodes,
//                        JPAExpressions.select(projectTotal.total.sum()).from(projectTotal).where(projectTotal.project.eq(project)),
//                        JPAExpressions.select(projectTotal.current.sum()).from(projectTotal).where(projectTotal.project.eq(project))))
//                .from(project)
//                .leftJoin(project.statusCode, codeDetail)
//                .on(codeDetail.id.eq(project.statusCode.id))
//                .leftJoin(
//                        JPAExpressions
//                                .select(project)
//                                .from(project)
//
//                )
//                .on()
//                .leftJoin(project.projectTotals, projectTotal)
//                .groupBy(project.id)
//                .offset(pageable.getOffset())
//                .limit(pageable.getPageSize())
//                .fetchResults();
//
//        return new PageImpl<>(results.getResults(), pageable, results.getTotal());
        return null;
    }

    @Override
    public Page<Project> getProject(String keyword, CodeDetail techCodes, Integer statusCode, Pageable pageable) {
        List<Project> projects =  jpaQueryFactory
                .select(project)
                .from(project)
//                .join(project.statusCode, codeDetail)
//                .on(statusCodeEq(statusCode))
                .join(project.techCodes, projectTechCode)
                .on(techCodesEq(techCodes))
                .where(project.isDeleted.eq(false), keywordEq(keyword), statusCodeEq(statusCode))
                .fetch();

        return new PageImpl<>(projects, pageable, projects.size());
    }

    private BooleanExpression keywordEq(String keyword){
        return hasText(keyword) ? project.title.contains(keyword) : null;
    }

    private BooleanExpression statusCodeEq(Integer statusCode){
        return statusCode != null ? project.statusCode.id.eq(statusCode) : null;
    }

    private BooleanExpression techCodesEq(CodeDetail techCodes){
        return techCodes != null ? projectTechCode.techCode.eq(techCodes) : null;
    }
}
