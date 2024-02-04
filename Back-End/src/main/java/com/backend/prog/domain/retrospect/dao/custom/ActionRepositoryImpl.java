package com.backend.prog.domain.retrospect.dao.custom;

import com.backend.prog.domain.project.domain.Project;
import com.backend.prog.domain.retrospect.domain.Action;
import com.backend.prog.domain.retrospect.domain.QAction;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.backend.prog.domain.retrospect.domain.QAction.action;

@RequiredArgsConstructor
public class ActionRepositoryImpl implements ActionRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Action> findLatestAction(Project project) {
        QAction condAction = new QAction("condAction");

        return jpaQueryFactory.selectFrom(action)
                .where(action.week.eq(
                        JPAExpressions.select(condAction.week.max())
                                .from(condAction)
                                .where(condAction.project.eq(project))
                ))
                .fetch();
    }
}
