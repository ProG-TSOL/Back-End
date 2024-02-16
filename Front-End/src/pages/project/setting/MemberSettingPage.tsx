/* eslint-disable @typescript-eslint/no-explicit-any */
import React, {useCallback, useEffect, useState} from 'react';
import {axiosInstance} from '../../../apis/lib/axios';
import {useRequireAuth} from '../../../hooks/useRequireAuth';
import {useUserStore} from '../../../stores/useUserStore';
import ImageWithFallback from "../../../utils/DefaultImgage.tsx";
import ProjectApplicationStatus, {MemberData} from "../../../components/project_home/ProjectApplicationStatus.tsx";
import '../../../styles/component/project-partication-member.scss'
import ProjectMemberContext from "../../../components/project_home/ProjectMemberContext.tsx";

interface MemberSettingPageProps {
	projectId: string | undefined;
}

interface Member {
	memberId: number;
	imgUrl: string;
	nickname: string;
	gitUsername: string;
	jobCode: {
		id: number;
		detailName: string;
		detailDescription: string;
		imgUrl: string;
	};
	roleCode: {
		id: number;
		detailName: string;
		detailDescription: string;
		imgUrl: string;
	};
}

interface Member {
	nickname: string;
	id: number;
}

interface ApplicationStatus {
	member: {
		id: number;
		email: string;
		nickname: string;
		imgUrl: string;
	};
	jobCode: {
		id: number;
		detailName: string;
		detailDescription: string;
		imgUrl: string;
	};
}

const useProjectMemberList = (projectId: string) => {
	const [memberList, setMemberList] = useState<Member[]>([]);

	const getMemberList = useCallback(async () => {
		await axiosInstance.get(`/projects/home/project-member/${projectId}`)
			.then((response) => {
				const data = response.data.data;
				setMemberList(data);
			})
			.catch((error) => {
				console.error("Loading project members failed:", error);
			});
	},[projectId]);

	useEffect(() => {
		(async () => {
			await getMemberList();
		})();

	}, [getMemberList]);

	return {memberList, getMemberList};
}

// const MemberSettingPage = () => {
const MemberSettingPage: React.FC<MemberSettingPageProps> = ({projectId}) => {
	useRequireAuth();
	const {profile} = useUserStore();
	const memberId = profile?.id;
	const [applyList, setApplyList] = useState<ApplicationStatus[]>([]);
	const [modalOpen, setModalOpen] = useState(false);

	const getApplyList = useCallback(async () => {
		try {
			const response = await axiosInstance.get(`/projects/${projectId}/application/${memberId}`);
			if (response.data.status === 'OK') {
				setApplyList(response.data.data);
			}
		} catch (error) {
			console.error("Loading applications failed:", error);
		}
	},[projectId, memberId]);

	// 신청현황 모달
	const onHandleModal = (e: React.MouseEvent<HTMLButtonElement>) => {
		e.preventDefault();
		e.stopPropagation();
		setModalOpen(true);
	}

	const makeMemberData = (memberList: ApplicationStatus[]): MemberData[] => {
		return memberList.map((data) => {
			return {
				member: {
					id: data.member.id,
					email: data.member.email,
					nickname: data.member.nickname,
					imgUrl: data.member.imgUrl
				},
				jobCode: {
					id: data.jobCode.id,
					detailName: data.jobCode.detailName,
					detailDescription: data.jobCode.detailDescription,
					imgUrl: data.jobCode.imgUrl
				}
			};
		})
	}

	const {memberList, getMemberList} = useProjectMemberList(projectId || '');

	const [acceptStatus, setAcceptStatus] = useState(false);

	useEffect(() => {
		if (projectId && memberId) {
			(async () => {
				await getApplyList();
			})();
		}
		if (acceptStatus) {
			(async () => {
				await getMemberList();
			})();

			(async () => {
				await getApplyList();
			})();
			makeMemberData(applyList);
			setAcceptStatus(false);
		}
	}, [projectId, memberId, acceptStatus]); // Add memberId as a dependency to useEffect

	// const memberList = useProjectMemberList(projectId || '');
	const handleUpate = (statusYN: boolean) => {
		//     조회 api 실행후 다시 뿌리기
		setAcceptStatus(statusYN);
	}

	const makeMemberProps = (data: Member) => {
		let leader: number | undefined;
		let role: string | undefined;
		memberList.forEach((member) => {
			if (member.memberId === data.memberId) {
				leader = member.memberId;
				role = member.roleCode.detailName;
				return;
			}
		})

		return {
			leaderId: leader,
			roleCodeName: role,
			projectId: projectId,
			memberId: data.memberId,
			gitProfile: data.gitUsername
		};
	}

	return (
		<div className={'flex flex-col'}>
			<div className={'under-line flex justify-between'}>
				<h2>참여멤버</h2>
				<button id={'app-btn'} className={'project-home-btn'}
						onClick={(e) => onHandleModal(e)}>
					신청멤버 {applyList?.length || 0}명
				</button>
			</div>
			<ProjectApplicationStatus modalOpen={modalOpen} setModalOpen={setModalOpen}
									  memberDataList={makeMemberData(applyList)} onUpdate={handleUpate}/>
			<div className={'pt-4'}>
				{/*<ul className={'part-member-box grid grid-cols-4 gap-y-4'}>*/}
				<ul className={'part-member-box grid'}>
					{memberList.map((member, index) => (
						<li key={index} className={'flex justify-center'}>
							<ProjectMemberContext
								// projectId={projectId} memberId={member.memberId} gitProfile={member.gitUsername}
								memberInfo={makeMemberProps(member)}
								onUpdate={handleUpate}>
								<div className={'member-card p-5 text-xl'}>

									<div className={'member-img-box'}>
										<div>
											<ImageWithFallback src={`${member.imgUrl}`} alt={''} type={'member'}
															   style={''}/>
										</div>
										<div className={'flex flex-col justify-center overflow-hidden'}>
											{
												member.roleCode.detailName === 'TeamLeader' ?
													<p className={'team-leader'}>
														{member.roleCode.detailDescription}
													</p>
													:
													<p className={'team-member'}>{member.roleCode.detailDescription}</p>
											}
											<p className={'truncate text-center'}>{member.nickname}</p>
											{/*TeamLeader*/}
											{/*TeamMembers*/}

										</div>
									</div>
									<div className={'mt-3 flex gap-x-4 items-center justify-center'}>
										<p className={'tag'}>직무</p>
										<p>{member.jobCode.detailDescription}</p>
										{/*/!*<a href={''}>git</a>*!/ // git 주소 있으면 클릭했을때 git 이동*/}
									</div>
								</div>
							</ProjectMemberContext>
						</li>
					))}
				</ul>
			</div>
		</div>
	);
};

export default MemberSettingPage;
