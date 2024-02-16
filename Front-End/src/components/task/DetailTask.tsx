import { ChangeEvent, useEffect, useState } from 'react';
import { FaAnglesRight, FaRegCircleUser, FaBoltLightning, FaBookBookmark, FaCalendarCheck } from 'react-icons/fa6';
import ReactQuill from 'react-quill';
import 'react-quill/dist/quill.snow.css'; // ReactQuill 스타일시트 임포트
import { axiosInstance } from '../../apis/lib/axios';
import { useParams } from 'react-router-dom';
import moment from 'moment';
import TaskDatePicker from '../calendar/TaskDatePicker';
import { useMultipleDetailCodes } from '../../hooks/useMultipleDataCodes';
import Chip from '@mui/material/Chip';

interface ProjectMember {
	member: {
		id: number;
		nickname: string;
		imgUrl: string | null;
	};
	jobCode: {
		id: number;
		detailName: string;
		detailDescription: string;
		imgUrl: string | null;
		isUse: boolean;
	};
}

interface DetailCode {
	id: number;
	detailName: string;
	detailDescription: string;
	imgUrl?: string | null;
}

interface DetailTaskProps {
	onClose: () => void; // onClose의 타입 명시
	onTaskUpdate: () => void;
}

const DetailTask: React.FC<DetailTaskProps> = ({ onClose, onTaskUpdate }) => {
	const tomorrow = new Date();
	tomorrow.setDate(tomorrow.getDate() + 1);

	const { projectId } = useParams<{ projectId: string }>();
	const [selectedTypeId, setSelectedTypeId] = useState<number | null>(null);
	const [selectedPriorityId, setSelectedPriorityId] = useState<number | null>(null);
	const [projectMembers, setProjectMembers] = useState<ProjectMember[]>([]);
	const [title, setTitle] = useState<string>('');
	const [showModal, setShowModal] = useState(false);
	const [selectedMemberId, setSelectedMemberId] = useState<number | null>(null);
	const [editorContent, setEditorContent] = useState<string>('');
	const [isOpen] = useState<boolean>(true);
	const [id, setId] = useState<string>('');
	const [nickname, setNickname] = useState<string>('');
	const [endDay, setEndDay] = useState(tomorrow);
	const [taskPostedSuccessfully, setTaskPostedSuccessfully] = useState(false);

	const { data: multipleDetailCodesData } = useMultipleDetailCodes(['WorkType', 'WorkPriority', 'WorkStatus']);
	const workTypes = multipleDetailCodesData?.[0] ?? [];
	const workPriorities = multipleDetailCodesData?.[1] ?? [];

	const getPrjMember = async () => {
		try {
			const response = await axiosInstance.get(`/projects/${projectId}/members`);
			setProjectMembers(response.data.data);
			console.log(response.data.data);
		} catch (error) {
			console.log(error);
		}
	};

	const postTask = async () => {
		// 입력 필드 검증
		if (!title || !selectedMemberId || !selectedTypeId || !selectedPriorityId || !editorContent) {
			// 필요한 모든 필드가 입력되지 않았으므로 모달 표시
			setShowModal(true);
			return; // 함수를 여기서 종료시켜서, API 호출을 방지
		}

		// 모든 필수 입력 필드가 채워져 있으면 업무 등록 API 호출
		const taskInfo = {
			projectId: projectId,
			producerId: id,
			statusCode: 1,
			typeCode: selectedTypeId,
			priorityCode: selectedPriorityId,
			consumerId: selectedMemberId,
			title: title,
			content: editorContent,
			startDay: moment().format('YYYY-MM-DD'),
			endDay: moment(endDay).format('YYYY-MM-DD'),
		};

		try {
			await axiosInstance.post('/works', taskInfo, {
				headers: {
					'Content-Type': 'application/json',
				},
			});
			onTaskUpdate(); // 업무 등록 성공 후 상위 컴포넌트 업데이트
			setTaskPostedSuccessfully(true); // 성공 알림 상태 업데이트
		} catch (error) {
			console.log(error);
			setShowModal(true); // 실패 시 기존 모달 표시
		}
	};

	const handleMemberChange = (event: ChangeEvent<HTMLSelectElement>): void => {
		// 선택된 멤버 ID를 문자열로 상태 업데이트, 빈 문자열이면 null 처리
		setSelectedMemberId(event.target.value ? Number(event.target.value) : null);
	};
	const handleWorkTypeClick = (id: number) => {
		setSelectedTypeId(id);
	};

	const handleWorkPriorityClick = (id: number) => {
		setSelectedPriorityId(id);
	};

	const editorChange = (content: string) => {
		setEditorContent(content);
	};

	const handleTitleChange = (event: ChangeEvent<HTMLInputElement>) => {
		setTitle(event.target.value);
	};

	const closeModal = () => {
		setShowModal(false);
	};

	useEffect(() => {
		// 로컬 스토리지에서 userProfile을 가져옴
		const userProfileString = localStorage.getItem('userProfile');
		if (userProfileString) {
			const userProfile = JSON.parse(userProfileString);
			setId(userProfile.id); // 닉네임 상태 업데이트
			setNickname(userProfile.nickname);
		}

		getPrjMember();
	}, []);

	const chipStyle = (isSelected: boolean) => ({
		margin: 0.5,
		color: isSelected ? '#fff' : '#000',
		backgroundColor: isSelected ? '#4B33E3' : '#ffffff', // 선택 시 배경색 변경 대신, 하얀색 배경 유지
		border: isSelected ? `2px solid #4B33E3` : '1px solid #000000', // 선택된 Chip의 경우 border 색상을 main-color로 설정
		'&:hover': {
			opacity: 0.75,
		},
	});

	if (!isOpen) return null;

	return (
		<>
			<div className={`fixed inset-0 bg-black bg-opacity-50 z-30 ${!isOpen && 'hidden'}`} onClick={onClose}></div>
			<div
				className={`fixed right-0 mt-16 top-0 h-full overflow-y-auto bg-slate-50 p-8 rounded-md shadow-lg z-40 w-full max-w-2xl transition-transform transform ${
					isOpen ? 'translate-x-0' : 'translate-x-full'
				} ease-in-out duration-300`}
			>
				<div className='flex items-center mb-4'>
					<FaAnglesRight onClick={onClose} className='text-lg text-gray-600 mr-2 cursor-pointer' />
					<h1 className='text-xl font-bold'>상세 업무 등록</h1>
				</div>
				<div className='flex items-center mb-4'>
					<FaRegCircleUser className='text-gray-600 mr-2' />
					<p className='text-md'>업무 요청자: {nickname}</p>
					<FaCalendarCheck className='text-md ml-4 mr-2' />
					<p>업무 시작일: {moment().format('YYYY-MM-DD')}</p>
				</div>
				<p className='font-semibold mb-2'>업무 제목</p>
				<input
					type='text'
					value={title}
					onChange={handleTitleChange}
					className='border border-gray-300 rounded p-2 mr-2 w-full'
				/>
				<hr />
				<div className='flex my-4'>
					<FaRegCircleUser className='text-gray-600 mt-3 mr-2' />
					<label htmlFor='member-select' className='mt-2 mr-2'>
						담당자:
					</label>
					<select
						id='member-select'
						value={selectedMemberId ? selectedMemberId.toString() : ''}
						onChange={handleMemberChange}
						className='border border-gray-300 rounded p-2'
					>
						<option value=''>담당자 선택...</option>
						{projectMembers.map((member) => (
							<option key={member.member.id} value={member.member.id.toString()}>
								{member.member.nickname} - {member.jobCode.detailDescription}
							</option>
						))}
					</select>
				</div>
				<div className='flex'>
					<FaBookBookmark className='mt-3' />
					{workTypes.map((workType: DetailCode) => (
						<Chip
							key={workType.id}
							label={workType.detailDescription}
							onClick={() => handleWorkTypeClick(workType.id)}
							variant='outlined'
							sx={chipStyle(selectedTypeId === workType.id)}
						/>
					))}
				</div>

				<div className='flex'>
					<FaBoltLightning className='mt-3' />
					{workPriorities.map((workPriority: DetailCode) => (
						<Chip
							key={workPriority.id}
							label={workPriority.detailDescription}
							onClick={() => handleWorkPriorityClick(workPriority.id)}
							variant='outlined'
							sx={chipStyle(selectedPriorityId === workPriority.id)}
						/>
					))}
				</div>
				<TaskDatePicker selectedDate={endDay} setSelectedDate={setEndDay} />
				<div className='flex flex-col'>
					<div className='mb-4'>
						<ReactQuill className='h-40' theme='snow' value={editorContent} onChange={editorChange} />
					</div>
				</div>
				<div className='flex justify-center'>
					<button onClick={postTask} className='mt-10 bg-blue-500 text-white p-4 rounded'>
						업무 등록
					</button>
				</div>
			</div>
			{taskPostedSuccessfully && (
				<div className='fixed inset-0 bg-black bg-opacity-50 z-50 flex items-center justify-center'>
					<div className='bg-white p-4 rounded-lg shadow-lg max-w-sm mx-auto'>
						<h2 className='text-xl font-bold mb-4'>업무가 등록되었습니다!</h2>
						<p className='mb-4'>프로젝트를 위해 힘내주세요!</p>
						<div className='flex justify-center'>
							<button
								onClick={() => {
									setTaskPostedSuccessfully(false); // 모달 닫기
									onClose(); // DetailTask 모달 닫기
								}}
								className='bg-main-color hover:bg-blue-700 text-white font-bold py-2 px-4 rounded'
							>
								화이팅!
							</button>
						</div>
					</div>
				</div>
			)}

			{showModal && (
				<div className='fixed inset-0 bg-black bg-opacity-50 z-50 flex items-center justify-center'>
					<div className='bg-white p-4 rounded-lg shadow-lg max-w-sm mx-auto'>
						<h2 className='flex justify-center font-bold text-lg mb-4'>알림</h2>
						<p className='mb-4'>부족한 부분을 채워주세요!</p>
						<div className='flex justify-center'>
							<button
								onClick={closeModal}
								className='bg-main-color hover:bg-blue-700 text-white font-bold py-2 px-4 rounded'
							>
								확인
							</button>
						</div>
					</div>
				</div>
			)}
		</>
	);
};

export default DetailTask;
