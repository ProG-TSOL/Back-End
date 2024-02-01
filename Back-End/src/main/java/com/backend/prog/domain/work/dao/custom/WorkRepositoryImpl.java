package com.backend.prog.domain.work.dao.custom;

import com.backend.prog.domain.project.domain.Project;
import com.backend.prog.domain.work.domain.Work;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.backend.prog.domain.work.domain.QWork.work;

@RequiredArgsConstructor
public class WorkRepositoryImpl implements WorkRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Work> findAllByProject(Project project) {
        return queryFactory.selectFrom(work)
                .where(work.project.eq(project))
                .fetch();
    }
}
