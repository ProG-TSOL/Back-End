package com.backend.prog.domain.retrospect.dao.custom;

import com.backend.prog.domain.project.domain.Project;
import com.backend.prog.domain.retrospect.domain.QRetrospect;
import com.backend.prog.domain.retrospect.domain.Retrospect;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.backend.prog.domain.retrospect.domain.QRetrospect.retrospect;

@RequiredArgsConstructor
public class RetrospectRepositoryImpl implements RetrospectRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Retrospect> getLatestRetrospects(Project project) {
        QRetrospect innerRetrospect = new QRetrospect("innerRetrospect");
        return jpaQueryFactory.selectFrom(retrospect)
                .where(retrospect.week.eq(
                                JPAExpressions.select(innerRetrospect.week.max())
                                        .from(innerRetrospect)
                                        .where(innerRetrospect.project.eq(project))
                        ), eqProject(project)
                ).fetch();
    }

    private static BooleanExpression eqProject(Project project) {
        return project != null ? retrospect.project.eq(project) : null;
    }
}
