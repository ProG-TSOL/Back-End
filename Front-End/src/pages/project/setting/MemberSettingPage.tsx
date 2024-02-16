/* eslint-disable @typescript-eslint/no-explicit-any */
import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import { axiosInstance } from '../../../apis/lib/axios';
import { useRequireAuth } from '../../../hooks/useRequireAuth';
import { useUserStore } from '../../../stores/useUserStore';
import ImageWithFallback from '../../../utils/DefaultImgage.tsx';
import ProjectApplicationStatus, { MemberData } from '../../../components/project_home/ProjectApplicationStatus.tsx';
import '../../../styles/component/project-partication-member.scss';

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

interface Position {
	posName: string;
	posCode: number;
	posNowNumber: number;
	posNumber: number;
	members: Member[];
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

interface ActionableModalData {
	memberId: number;
	posCode: number;
	visible: boolean;
	x: number;
	y: number;
	nickname: string;
}

const useProjectMemberList = (projectId: string) => {
	const [memberList, setMemberList] = useState<Member[]>([]);
	const getMemberList = async () => {
		try {
			const response = await axiosInstance.get(`/projects/home/project-member/${projectId}`);
			if (response.data.status === 'OK') {
				const data = response.data.data;
				console.log(`받은 멤버 데이터 : ${JSON.stringify(data)}`);
				setMemberList(data);
			}
		} catch (error) {
			console.error('Loading project members failed:', error);
		}
	};

	useEffect(() => {
		getMemberList();
	}, [projectId]);

	return { memberList, getMemberList };
};

// const MemberSettingPage = () => {
const MemberSettingPage: React.FC<MemberSettingPageProps> = ({ projectId }) => {
	console.log(`받은 프로젝트 아이디 : ${projectId}`);
	useRequireAuth();
	const [actionableModal, setActionableModal] = useState<ActionableModalData | null>(null);
	const [selectedPosCode, setSelectedPosCode] = useState<number | null>(null);

	const { profile } = useUserStore();
	const memberId = profile?.id;
	const [positions, setPositions] = useState<Position[]>([]);
	const [applyList, setApplyList] = useState<ApplicationStatus[]>([]);
	// const { projectId } = useParams<{ projectId: string }>(); // Specify the type of useParams
	const [modalOpen, setModalOpen] = useState(false);

	const getApplyList = async () => {
		try {
			const response = await axiosInstance.get(`/projects/${projectId}/application/${memberId}`);
			if (response.data.status === 'OK') {
				setApplyList(response.data.data);
			}
		} catch (error) {
			console.error('Loading applications failed:', error);
		}
	};

	const toggleActionableModal = (memberId: number, posCode: number, x: number, y: number, nickname: string) => {
		setSelectedPosCode(null);
		if (actionableModal && actionableModal.memberId === memberId) {
			setActionableModal(null);
		} else {
			setActionableModal({
				memberId,
				posCode,
				visible: true,
				x,
				y,
				nickname,
			});
		}
	};
	const handlePositionChange = (e: React.ChangeEvent<HTMLSelectElement>) => {
		setSelectedPosCode(parseInt(e.target.value));
		console.log(e.target.value);
	};
	const applyPositionChange = async () => {
		if (actionableModal && selectedPosCode !== null) {
			await movePositions(actionableModal.memberId, selectedPosCode);
			setActionableModal(null); // 포지션 변경 후 모달 닫기
		}
	};

	const exile = async (exiledId: number) => {
		try {
			const response = await axiosInstance.delete(`/projects/${projectId}/exiled/${memberId}?exileId=${exiledId}`);
			console.log(response);
			getApplyList();
		} catch (error) {
			console.error('Exiling application failed:', error);
		}
	};

	const movePositions = async (moveMember: number, moveJob: number) => {
		try {
			const response = await axiosInstance.patch(
				`/projects/${projectId}/${memberId}/members?moveMember=${moveMember}&moveJob=${moveJob}`,
			);
			console.log(response);
			getData();
			getApplyList();
		} catch (error) {
			console.error('Moving application failed:', error);
		}
	};

	// 신청현황 모달
	const onHandleModal = (e: React.MouseEvent<HTMLButtonElement>) => {
		console.log(`onHandleModal 신청현황`);
		e.preventDefault();
		e.stopPropagation();

		// console.log(member);
		setModalOpen(true);
	};

	const makeMemberData = (memberList: ApplicationStatus[]): MemberData[] => {
		return memberList.map((data) => {
			return {
				member: {
					id: data.member.id,
					email: data.member.email,
					nickname: data.member.nickname,
					imgUrl: data.member.imgUrl,
				},
				jobCode: {
					id: data.jobCode.id,
					detailName: data.jobCode.detailName,
					detailDescription: data.jobCode.detailDescription,
					imgUrl: data.jobCode.imgUrl,
				},
			};
		});
	};

	const { memberList, getMemberList } = useProjectMemberList(projectId || '');

	const [acceptStatus, setAcceptStatus] = useState(false);

	useEffect(() => {
		console.log(`useEf?`);
		if (projectId && memberId) {
			// getData();
			getApplyList();
		}
		if (acceptStatus) {
			console.log(`여기타나?`);
			getMemberList();
			getApplyList();
			makeMemberData(applyList);
			setAcceptStatus(false);
		}
	}, [projectId, memberId, acceptStatus]); // Add memberId as a dependency to useEffect

	// const memberList = useProjectMemberList(projectId || '');
	const handleUpate = (statusYN: boolean) => {
		//     조회 api 실행후 다시 뿌리기
		setAcceptStatus(statusYN);
	};

	return (
		<div className={'flex flex-col'}>
			<div className={'under-line flex justify-between'}>
				<h2>참여멤버</h2>
				<button id={'app-btn'} className={'project-home-btn'} onClick={(e) => onHandleModal(e)}>
					신청멤버 {applyList?.length || 0}명
				</button>
			</div>
			<ProjectApplicationStatus
				modalOpen={modalOpen}
				setModalOpen={setModalOpen}
				memberDataList={makeMemberData(applyList)}
				onUpdate={handleUpate}
			/>
			<div className={'pt-4'}>
				<ul className={'grid grid-cols-4 gap-y-4'}>
					{memberList.map((member, index) => (
						<li key={index} className={'flex justify-center'}>
							<div className={'member-card p-5 text-xl'}>
								<div className={'member-img-box'}>
									<div>
										<ImageWithFallback src={`${member.imgUrl}`} alt={''} type={'member'} />
									</div>
									<div className={'flex flex-col justify-center overflow-hidden'}>
										{member.roleCode.detailName === 'TeamLeader' ? (
											<p className={'team-leader'}>{member.roleCode.detailDescription}</p>
										) : (
											<p className={'team-member'}>{member.roleCode.detailDescription}</p>
										)}
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
						</li>
					))}
				</ul>
			</div>
		</div>
	);
};

export default MemberSettingPage;
