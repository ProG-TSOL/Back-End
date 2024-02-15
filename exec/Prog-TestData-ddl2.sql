drop database prog;

create database prog DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;

use prog;

create table if not exists code (
    code_id int auto_increment comment '코드 아이디' primary key,
    name varchar(30) null comment '코드명',
    description varchar(300) null comment '코드 설명',
    is_use bit default 1 null comment '코드 사용 여부',
    created_at datetime(6) null comment '생성일시',
    modified_at datetime(6) null comment '수정일시',
    constraint UK_3uwskoakbmqrrlda81xtkofpk unique (name)
);

create table if not exists code_detail (
    detail_id int auto_increment comment '상세코드 아이디' primary key,
    code_id int null comment '코드 아이디',
    detail_name varchar(30) null comment '상세코드명',
    detail_description varchar(300) null comment '상세코드 설명',
    img_url varchar(255) null comment '이미지 주소',
    is_use bit default 1 null comment '상세코드 사용 여부',
    created_at datetime(6) null comment '생성일시',
    modified_at datetime(6) null comment '수정일시',
    constraint FK9upiwk749iml6p0cfxwgvql2i foreign key (code_id) references code (code_id)
);

create table if not exists member (
    member_id int auto_increment comment '회원ID' primary key,
    email varchar(100) null comment '이메일',
    password varchar(64) null comment '비밀번호',
    name varchar(30) null comment '이름',
    nickname varchar(30) null comment '닉네임',
    provider enum ('GOOGLE', 'GITHUB') null comment '소셜로그인 사이트',
    description varchar(150) null comment '한줄소개',
	git_username varchar(20) null comment '깃허브 ID',
    origin_img varchar(255) null comment '저장된 파일명',
    img_url varchar(255) null comment '이미지주소',
    is_deleted bit default 0 null comment '삭제여부',
    created_at datetime(6) null comment '생성일시',
    modified_at datetime(6) null comment '수정일시'
);

create table if not exists comment (
    comment_id bigint auto_increment comment '댓글ID' primary key,
    content_code int null comment '컨텐츠코드',
    member_id int null comment '멤버ID',
    parent_id bigint null comment '댓글부모ID',
    content_id bigint null comment '컨텐츠ID',
    content varchar(300) null comment '내용',
    is_deleted bit default 0 null comment '삭제여부',
    created_at datetime(6) null comment '생성일시',
    modified_at datetime(6) null comment '수정일시',
    constraint FKmrrrpi513ssu63i2783jyiv9m foreign key (member_id) references member (member_id),
    constraint FKpis5waicjlxhkqnxa1gc26t7w foreign key (content_code) references code_detail (detail_id)
);

create table if not exists member_roles (
    member_member_id int not null,
    roles enum ('USER', 'ADIM') null,
    constraint FKruptm2dtwl95mfks4bnhv828k foreign key (member_member_id) references member (member_id)
);

create table if not exists member_tech (
    member_id int not null comment '멤버ID',
    tech_code int not null comment '기술스택코드',
    created_at datetime(6) null comment '생성일시',
    modified_at datetime(6) null comment '수정일시',
    primary key (member_id, tech_code),
    constraint FK89s3mp7rcur1jmyrulvg4rkly foreign key (member_id) references member (member_id),
    constraint FKj5xl4tminsqlnhkl8s1ohxpy7 foreign key (tech_code) references code_detail (detail_id)
);

create table if not exists project (
    project_id bigint auto_increment comment '프로젝트 아이디' primary key,
    status_code int null comment '프로젝트 상태 코드',
    title varchar(150) null comment '제목',
    content text null comment '내용',
    start_day date null comment '프로젝트 시작일',
    end_day date null comment '프로젝트 종료일',
    view_cnt int default 0 null comment '조회수',
    like_cnt int default 0 null comment '좋아요 수',
    period int null comment '프로젝트 기간',
    project_img_url varchar(255) null comment '프로젝트 이미지 주소',
    is_deleted bit default 0 null comment '삭제여부',
    created_at datetime(6) null comment '생성일시',
    modified_at datetime(6) null comment '수정일시',
    constraint FKobindmhg37dum8wt1buuux9p0 foreign key (status_code) references code_detail (detail_id)
);

create table if not exists action (
    action_id bigint auto_increment comment '액션ID' primary key,
    project_id bigint null comment '프로젝트ID',
    content text null comment '내용',
    week int null comment '주차',
    created_at datetime(6) null comment '생성일시',
    modified_at datetime(6) null comment '수정일시',
    constraint FKcyp55sqv5hma6ubn8yf80of35 foreign key (project_id) references project (project_id)
);

create table if not exists additional (
    additional_id bigint auto_increment comment '부가정보ID' primary key,
    project_id bigint null comment '프로젝트ID',
    title varchar(150) null comment '제목',
    url varchar(255) null comment '추가정보 URL',
    img_url varchar(255) null comment '이미지 URL',
    is_deleted bit default 0 null comment '삭제여부',
    created_at datetime(6) null comment '생성일시',
    modified_at datetime(6) null comment '수정일시',
    constraint FKo0bvu4bpf9iwv1dvc81iecavn foreign key (project_id) references project (project_id)
);

create table if not exists application_status (
    project_id bigint not null comment '프로젝트ID',
    member_id int not null comment '멤버ID',
    job_code int null comment '직무코드',
    created_at datetime(6) null comment '생성일시',
    modified_at datetime(6) null comment '수정일시',
    primary key (member_id, project_id),
    constraint FK5dvv54iwlodsdhoxnbkppji1b foreign key (project_id) references project (project_id),
    constraint FK6rfnroaaw1o4f2nr1a17f160q foreign key (member_id) references member (member_id),
    constraint FKgifuknoiyabb8uu9cjhr18xd0 foreign key (job_code) references code_detail (detail_id)
);

create table if not exists attendance (
    attendance_id bigint auto_increment comment '근태ID' primary key,
    project_id bigint null comment '프로젝트ID',
    member_id int null comment '멤버ID',
    working_day date null comment '근무일자',
    working_time time(6) null comment '근무시간',
    created_at datetime(6) null comment '생성일시',
    modified_at datetime(6) null comment '수정일시',
    constraint FKslaf4mu3eu0gi72u4t9xcsxjd foreign key (member_id) references member (member_id),
    constraint FKtp4jb0g2mqcargitbefhdk2es foreign key (project_id) references project (project_id)
);

create table if not exists attendance_log (
    attendance_log_id bigint auto_increment comment '근무일지ID' primary key,
    member_id int null comment '멤버ID',
    project_id bigint null comment '프로젝트ID',
    start_at datetime(6) null comment '출근일시',
    end_at datetime(6) null comment '퇴근일시',
    created_at datetime(6) null comment '생성일시',
    modified_at datetime(6) null comment '수정일시',
    constraint FKiea69so7xlxt46wo2s3v3fw48 foreign key (project_id) references project (project_id),
    constraint FKs13ilpqryegjygg05x8st6g26 foreign key (member_id) references member (member_id)
);

create table if not exists board (
    board_id bigint auto_increment comment '게시글ID' primary key,
    project_id bigint null comment '프로젝트ID',
    member_id int null comment '멤버ID',
    title varchar(150) null comment '제목',
    content text null comment '내용',
    view_cnt int null comment '조회수',
    is_notice bit null comment '공지여부',
    is_deleted bit default 0 null comment '삭제여부',
    created_at datetime(6) null comment '생성일시',
    modified_at datetime(6) null comment '수정일시',
    constraint FKglbcvvdfyhb68effa48t894ib foreign key (project_id) references project (project_id),
    constraint FKsds8ox89wwf6aihinar49rmfy foreign key (member_id) references member (member_id)
);

create table if not exists board_image (
    board_img_id bigint auto_increment comment '게시글이미지ID' primary key,
    board_id bigint null comment '게시글ID',
    img_url varchar(255) null comment '이미지주소',
    created_at datetime(6) null comment '생성일시',
    modified_at datetime(6) null comment '수정일시',
    constraint FKp567mlnww479xgirmd98kcqnp foreign key (board_id) references board (board_id)
);

create table if not exists feed (
    feed_id bigint auto_increment comment '피드ID' primary key,
    project_id bigint null comment '프로젝트ID',
    content_code int null comment '컨텐츠코드',
    content_id bigint null comment '컨텐츠ID',
    member_id int null comment '멤버ID',
    content varchar(255) null comment '피드내용',
    created_at datetime(6) null comment '생성일시',
    modified_at datetime(6) null comment '수정일시',
    constraint FK2dx5mk4xv8k86xxmknxa9vuql foreign key (project_id) references project (project_id),
    constraint FKhotfbnq6up2liyy3npkmh17ro foreign key (content_code) references code_detail (detail_id)
);

create table if not exists likes (
    member_id int not null comment '멤버ID',
    project_id bigint not null comment '프로젝트ID',
    created_at datetime(6) null comment '생성일시',
    modified_at datetime(6) null comment '수정일시',
    primary key (member_id, project_id),
    constraint FK6gupou17or1xfkb1mkasgwqys foreign key (project_id) references project (project_id),
    constraint FKa4vkf1skcfu5r6o5gfb5jf295 foreign key (member_id) references member (member_id)
);

create table if not exists project_member (
    project_id bigint not null comment '프로젝트ID',
    member_id int not null comment '멤버ID',
    job_code int null comment '직무 코드',
    role_code int null comment '역할 코드',
    is_deleted bit default 0 null comment '삭제여부',
    created_at datetime(6) null comment '생성일시',
    modified_at datetime(6) null comment '수정일시',
    primary key (member_id, project_id),
    constraint FK103dwxad12nbaxtmnwus4eft2 foreign key (project_id) references project (project_id),
    constraint FKb8ys2r3lnckmjcu03b6gifvjn foreign key (role_code) references code_detail (detail_id),
    constraint FKilusx84svugdln73un4w2euar foreign key (member_id) references member (member_id),
    constraint FKkec93hbu7gws2uhuv5m6rben0 foreign key (job_code) references code_detail (detail_id)
);

create table if not exists project_tech (
    code_id int not null comment '코드ID',
    project_id bigint not null comment '프로젝트ID',
    created_at datetime(6) null comment '생성일시',
    modified_at datetime(6) null comment '수정일시',
    primary key (code_id, project_id),
    constraint FKe5aepvkna7nr2glrk765ur9p7 foreign key (code_id) references code_detail (detail_id),
    constraint FKg1e83277kl2y382o3olm6yxnw foreign key (project_id) references project (project_id)
);

create table if not exists project_total (
    project_id bigint not null comment '프로젝트ID',
    code_id int not null comment '코드ID',
    current int default 0 null comment '현재 인원',
    total int null comment '총 인원',
    created_at datetime(6) null comment '생성일시',
    modified_at datetime(6) null comment '수정일시',
    primary key (code_id, project_id),
    constraint FKcqv7pah0mfjmkxunnrct4iaex foreign key (code_id) references code_detail (detail_id),
    constraint FKpr2w1l3fixbhply5dbdu3ipbj foreign key (project_id) references project (project_id)
);

create table if not exists retrospect (
    retrospect_id bigint auto_increment comment '회고ID' primary key,
    project_id bigint null comment '프로젝트ID',
    member_id int null comment '멤버ID',
    kpt_code int null comment 'KPT 코드',
    week int null comment '주차',
    content text null comment '내용',
    created_at datetime(6) null comment '생성일시',
    modified_at datetime(6) null comment '수정일시',
    constraint FK1twuj2ryhed83wxh6lffg21ta foreign key (project_id) references project (project_id),
    constraint FK3biwkjjjkugtuuawowmn6ly0w foreign key (member_id) references member (member_id),
    constraint FKshiri1xe28gb0h6qfex8c95v4 foreign key (kpt_code) references code_detail (detail_id)
);

create table if not exists work (
    work_id bigint auto_increment comment '업무ID' primary key,
    project_id bigint null comment '프로젝트ID',
    member_id int null comment '업무신청자ID',
    status_code int null comment '업무상태코드',
    type_code int null comment '업무구분코드',
    priority_code int null comment '우선순위코드',
    consumer_id int null comment '업무담당자ID',
    title varchar(150) null comment '제목',
    content text null comment '내용',
    start_day date null comment '시작일',
    end_day date null comment '종료일',
    created_at datetime(6) null comment '생성일시',
    modified_at datetime(6) null comment '수정일시',
    constraint FKf4jf1abybj7fc5w1nt686svxt foreign key (project_id) references project (project_id),
    constraint FKhu5thhymmgpkhluanli7ku43l foreign key (status_code) references code_detail (detail_id),
    constraint FKie859u45lqga4un590cjmx1er foreign key (priority_code) references code_detail (detail_id),
    constraint FKlgvd59a9me13vdur5bintqf1g foreign key (member_id) references member (member_id),
    constraint FKlyk8o6x1s2xk1lp8xtip4skj0 foreign key (consumer_id) references member (member_id)
);

create table if not exists work_checklist (
    checklist_id bigint auto_increment comment '체크리스트ID' primary key,
    work_id bigint null comment '업무ID',
    title varchar(255) null comment '제목',
    is_finish bit default 0 null comment '완료 여부',
    modified_at datetime(6) null comment '수정일시',
    started_at datetime(6) null comment '시작일시',
    created_at datetime(6) null comment '생성일시',
    finished_at datetime(6) null comment '종료일시',
    constraint FK1tiawlhsvejo0emgi7d4pqt83 foreign key (work_id) references work (work_id)
);


-- Code 기초데이터삽입
INSERT INTO code (code_id, is_use, created_at, name, description, modified_at)
VALUES (1, 1, now(), 'WorkStatus', '업무상태를 표현하기 위한 코드', now());
INSERT INTO code (code_id, is_use, created_at,  name, description, modified_at)
VALUES (2, 1, now(), 'WorkType', '업무를 구분하기 위한 코드', now());
INSERT INTO code (code_id, is_use, created_at,  name, description, modified_at)
VALUES (3, 1, now(), 'WorkPriority', '우선순위 코드', now());
INSERT INTO code (code_id, is_use, created_at,  name, description, modified_at)
VALUES (4, 1, now(), 'Tech', '기술스택', now());
INSERT INTO code (code_id, is_use, created_at,  name, description, modified_at)
VALUES (5, 1, now(), 'Job', '직무코드', now());
INSERT INTO code (code_id, is_use, created_at, name, description, modified_at)
VALUES (6, 1, now(), 'ProjectRole', '프로젝트 역할 코드', now());
INSERT INTO code (code_id, is_use, created_at, name, description, modified_at)
VALUES (7, 1, now(), 'ProjectStatus', '프로젝트 상태', now());
INSERT INTO code (code_id, is_use, created_at, name, description, modified_at)
VALUES (8, 1, now(), 'Content', '게시글, 프로젝트, 회고를 구분하기 위한 코드', now());
INSERT INTO code (code_id, is_use, created_at, name, description, modified_at)
VALUES (9, 1, now(), 'KPT', '회고 분류를 하기위한 코드', now());

-- code detail -  업무 상태코드
-- 업무 상태(WorkStatus)
INSERT INTO code_detail (code_id, detail_id, is_use, detail_name, detail_description, img_url, created_at, modified_at)
VALUES (1, 1, 1, 'ToDo', '시작 전', null, now(), now());
INSERT INTO code_detail (code_id, detail_id, is_use, detail_name, detail_description, img_url, created_at, modified_at)
VALUES (1, 2, 1, 'InProgress', '진행 중', null, now(), now());
INSERT INTO code_detail (code_id, detail_id, is_use, detail_name, detail_description, img_url, created_at,  modified_at)
VALUES (1, 3, 1, 'Done', '완료', null, now(), now());

-- 업무 카테고리(WorkType)
INSERT INTO code_detail (code_id, detail_id, is_use, detail_name, detail_description, img_url, created_at, modified_at)
VALUES (2, 4, 1, 'Analysis', '분석', null, now(), now());
INSERT INTO code_detail (code_id, detail_id, is_use, detail_name, detail_description, img_url, created_at, modified_at)
VALUES (2, 5, 1, 'Design', '설계', null, now(), now());
INSERT INTO code_detail (code_id, detail_id, is_use, detail_name, detail_description, img_url, created_at, modified_at)
VALUES (2, 6, 1, 'Develop', '개발', null, now(), now());
INSERT INTO code_detail (code_id, detail_id, is_use, detail_name, detail_description, img_url, created_at,  modified_at)
VALUES (2, 7, 1, 'Test', '테스트', null, now(), now());
INSERT INTO code_detail (code_id, detail_id, is_use, detail_name, detail_description, img_url, created_at,  modified_at)
VALUES (2, 8, 1, 'WorkETC', '기타', null, now(), now());

-- 업무 우선순위(WorkPriority)
INSERT INTO code_detail (code_id, detail_id, is_use, detail_name, detail_description, img_url, created_at,  modified_at)
VALUES (3, 9, 1, 'Top', '상', null, now(), now());
INSERT INTO code_detail (code_id, detail_id, is_use, detail_name, detail_description, img_url, created_at, modified_at)
VALUES (3, 10, 1, 'Middle', '중', null, now(), now());
INSERT INTO code_detail (code_id, detail_id, is_use, detail_name, detail_description, img_url, created_at, modified_at)
VALUES (3, 11, 1, 'Bottom', '하', null, now(), now());

-- 기술스택(Tech)
INSERT INTO code_detail (code_id, detail_id, is_use, detail_name, detail_description, img_url, created_at, modified_at)
VALUES (4, 12, 1, 'Java', 'Java', null, now(), now());
INSERT INTO code_detail (code_id, detail_id, is_use, detail_name, detail_description, img_url, created_at, modified_at)
VALUES (4, 13, 1, 'Python', 'Python', null, now(), now());
INSERT INTO code_detail (code_id, detail_id, is_use, detail_name, detail_description, img_url, created_at, modified_at)
VALUES (4, 14, 1, 'Zeplin', 'Zeplin', null, now(), now());
INSERT INTO code_detail (code_id, detail_id, is_use, detail_name, detail_description, img_url, created_at, modified_at)
VALUES (4, 15, 1, 'Figma', 'Figma', null, now(), now());
INSERT INTO code_detail (code_id, detail_id, is_use, detail_name, detail_description, img_url, created_at, modified_at)
VALUES (4, 16, 1, 'Git', 'Git', null, now(), now());
INSERT INTO code_detail (code_id, detail_id, is_use, detail_name, detail_description, img_url, created_at, modified_at)
VALUES (4, 17, 1, 'Docker', 'Docker', null, now(), now());
INSERT INTO code_detail (code_id, detail_id, is_use, detail_name, detail_description, img_url, created_at, modified_at)
VALUES (4, 18, 1, 'Kubernetes', 'Kubernetes', null, now(), now());
INSERT INTO code_detail (code_id, detail_id, is_use, detail_name, detail_description, img_url, created_at, modified_at)
VALUES (4, 19, 1, 'AWS', 'AWS', null, now(), now());
INSERT INTO code_detail (code_id, detail_id, is_use, detail_name, detail_description, img_url, created_at, modified_at)
VALUES (4, 20, 1, 'Flutter', 'Flutter', null, now(), now());
INSERT INTO code_detail (code_id, detail_id, is_use, detail_name, detail_description, img_url, created_at, modified_at)
VALUES (4, 21, 1, 'Unity', 'Unity', null, now(), now());
INSERT INTO code_detail (code_id, detail_id, is_use, detail_name, detail_description, img_url, created_at, modified_at)
VALUES (4, 22, 1, 'ReactNative', 'ReactNative', null, now(), now());
INSERT INTO code_detail (code_id, detail_id, is_use, detail_name, detail_description, img_url, created_at, modified_at)
VALUES (4, 23, 1, 'Firebase', 'Firebase', null, now(), now());
INSERT INTO code_detail (code_id, detail_id, is_use, detail_name, detail_description, img_url, created_at, modified_at)
VALUES (4, 24, 1, 'GraphQL', 'GraphQL', null, now(), now());
INSERT INTO code_detail (code_id, detail_id, is_use, detail_name, detail_description, img_url, created_at, modified_at)
VALUES (4, 25, 1, 'php', 'php', null, now(), now());
INSERT INTO code_detail (code_id, detail_id, is_use, detail_name, detail_description, img_url, created_at, modified_at)
VALUES (4, 26, 1, 'MongoDB', 'MongoDB', null, now(), now());
INSERT INTO code_detail (code_id, detail_id, is_use, detail_name, detail_description, img_url, created_at, modified_at)
VALUES (4, 27, 1, 'MySQL', 'MySQL', null, now(), now());
INSERT INTO code_detail (code_id, detail_id, is_use, detail_name, detail_description, img_url, created_at, modified_at)
VALUES (4, 28, 1, 'Kotlin', 'Kotlin', null, now(), now());
INSERT INTO code_detail (code_id, detail_id, is_use, detail_name, detail_description, img_url, created_at, modified_at)
VALUES (4, 29, 1, 'Swift', 'Swift', null, now(), now());
INSERT INTO code_detail (code_id, detail_id, is_use, detail_name, detail_description, img_url, created_at, modified_at)
VALUES (4, 30, 1, 'C++', 'C++', null, now(), now());
INSERT INTO code_detail (code_id, detail_id, is_use, detail_name, detail_description, img_url, created_at, modified_at)
VALUES (4, 31, 1, 'C#', 'C#', null, now(), now());
INSERT INTO code_detail (code_id, detail_id, is_use, detail_name, detail_description, img_url, created_at, modified_at)
VALUES (4, 32, 1, 'Go', 'Go', null, now(), now());
INSERT INTO code_detail (code_id, detail_id, is_use, detail_name, detail_description, img_url, created_at, modified_at)
VALUES (4, 33, 1, 'Express', 'Express', null, now(), now());
INSERT INTO code_detail (code_id, detail_id, is_use, detail_name, detail_description, img_url, created_at, modified_at)
VALUES (4, 34, 1, 'Nestjs', 'Nestjs', null, now(), now());
INSERT INTO code_detail (code_id, detail_id, is_use, detail_name, detail_description, img_url, created_at, modified_at)
VALUES (4, 35, 1, 'Nextjs', 'Nextjs', null, now(), now());
INSERT INTO code_detail (code_id, detail_id, is_use, detail_name, detail_description, img_url, created_at, modified_at)
VALUES (4, 36, 1, 'Nodejs', 'Nodejs', null, now(), now());
INSERT INTO code_detail (code_id, detail_id, is_use, detail_name, detail_description, img_url, created_at, modified_at)
VALUES (4, 37, 1, 'Spring', 'Spring', null, now(), now());
INSERT INTO code_detail (code_id, detail_id, is_use, detail_name, detail_description, img_url, created_at, modified_at)
VALUES (4, 38, 1, 'SpringBoot', 'SpringBoot', null, now(), now());
INSERT INTO code_detail (code_id, detail_id, is_use, detail_name, detail_description, img_url, created_at, modified_at)
VALUES (4, 39, 1, 'Vue', 'Vue', null, now(), now());
INSERT INTO code_detail (code_id, detail_id, is_use, detail_name, detail_description, img_url, created_at, modified_at)
VALUES (4, 40, 1, 'React', 'React', null, now(), now());
INSERT INTO code_detail (code_id, detail_id, is_use, detail_name, detail_description, img_url, created_at, modified_at)
VALUES (4, 41, 1, 'TypeScript', 'TypeScript', null, now(), now());
INSERT INTO code_detail (code_id, detail_id, is_use, detail_name, detail_description, img_url, created_at, modified_at)
VALUES (4, 42, 1, 'JavaScript', 'JavaScript', null, now(), now());

-- 프로젝트 포지션(Job)
INSERT INTO code_detail (code_id, detail_id, is_use, detail_name, detail_description, img_url, created_at, modified_at)
VALUES (5, 43, 1, 'BE', '백엔드', null, now(), now());
INSERT INTO code_detail (code_id, detail_id, is_use, detail_name, detail_description, img_url, created_at, modified_at)
VALUES (5, 44, 1, 'FE', '프론트엔드', null, now(), now());
INSERT INTO code_detail (code_id, detail_id, is_use, detail_name, detail_description, img_url, created_at, modified_at)
VALUES (5, 45, 1, 'ProductDesigner', '기획', null, now(), now());
INSERT INTO code_detail (code_id, detail_id, is_use, detail_name, detail_description, img_url, created_at, modified_at)
VALUES (5, 46, 1, 'Infra', '인프라', null, now(), now());
INSERT INTO code_detail (code_id, detail_id, is_use, detail_name, detail_description, img_url, created_at, modified_at)
VALUES (5, 47, 1, 'Publisher', 'UI/UX', null, now(), now());

-- 프로젝트 역할(ProjectRole)
INSERT INTO code_detail (code_id, detail_id, is_use, detail_name, detail_description, img_url, created_at, modified_at)
VALUES (6, 48, 1, 'TeamLeader', '팀장', null, now(), now());
INSERT INTO code_detail (code_id, detail_id, is_use, detail_name, detail_description, img_url, created_at, modified_at)
VALUES (6, 49, 1, 'TeamMembers', '팀원', null, now(), now());

-- 프로젝트 상태(ProjectStatus)
INSERT INTO code_detail (code_id, detail_id, is_use, detail_name, detail_description, img_url, created_at,  modified_at)
VALUES (7, 50, 1, 'Recruiting', '모집중', null, now(), now());
INSERT INTO code_detail (code_id, detail_id, is_use, detail_name, detail_description, img_url, created_at, modified_at)
VALUES (7, 51, 1, 'Proceeding', '진행중', null, now(), now());
INSERT INTO code_detail (code_id, detail_id, is_use, detail_name, detail_description, img_url, created_at, modified_at)
VALUES (7, 52, 1, 'Complete', '완료', null, now(), now());

-- 게시물 타입(Content)
INSERT INTO code_detail (code_id, detail_id, is_use, detail_name, detail_description, img_url, created_at, modified_at)
VALUES (8, 53, 1, 'Post', '게시물', null, now(), now());
INSERT INTO code_detail (code_id, detail_id, is_use, detail_name, detail_description, img_url, created_at, modified_at)
VALUES (8, 54, 1, 'Retrospect', '회고', null, now(), now());
INSERT INTO code_detail (code_id, detail_id, is_use, detail_name, detail_description, img_url, created_at, modified_at)
VALUES (8, 55, 1, 'Project', '프로젝트', null, now(), now());
INSERT INTO code_detail (code_id, detail_id, is_use, detail_name, detail_description, img_url, created_at, modified_at)
VALUES (8, 56, 1, 'Work', '업무', null, now(), now());

-- 회고 타입(KPT)
INSERT INTO code_detail (code_id, detail_id, is_use, detail_name, detail_description, img_url, created_at, modified_at)
VALUES (9, 57, 1, 'Keep', 'Keep', null, now(), now());
INSERT INTO code_detail (code_id, detail_id, is_use, detail_name, detail_description, img_url, created_at, modified_at)
VALUES (9, 58, 1, 'Problem', 'Problem', null, now(), now());
INSERT INTO code_detail (code_id, detail_id, is_use, detail_name, detail_description, img_url, created_at, modified_at)
VALUES (9, 59, 1, 'Try', 'Try', null, now(), now());
INSERT INTO code_detail (code_id, detail_id, is_use, detail_name, detail_description, img_url, created_at, modified_at)
VALUES (9, 60, 1, 'Action', 'Action', null, now(), now());