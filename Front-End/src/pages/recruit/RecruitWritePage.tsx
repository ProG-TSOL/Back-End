import React, { useState, ChangeEvent, useEffect } from 'react';
import TechStack, { techStack } from '../../components/techstack/TechStack';
import Position, { position } from '../../components/position/Position';
import { useNavigate } from 'react-router-dom';
import { axiosInstance } from '../../apis/lib/axios';
import { useRequireAuth } from '../../hooks/useRequireAuth';
import { useUserStore } from '../../stores/useUserStore';

interface State {
	projectTitle: string;
	projectContent: string;
	projectImage: string | null;
	projectPeriodNum: number;
	projectPeriodUnit: string;
}

const RecruitWritePage: React.FC = () => {
	useRequireAuth();
	const { profile } = useUserStore();
	const memberId = profile?.id;
	const navigate = useNavigate();
	const [state, setState] = useState<State>({
		projectTitle: '',
		projectContent: '',
		projectImage: null,
		projectPeriodNum: 0,
		projectPeriodUnit: '주',
	});
	const [isModalVisible, setModalVisible] = useState(false);
	useEffect(() => {
		return () => {
			techStack.mystack = [];
			position.totalList = [];
		};
	}, []);
	const handleInputChange = (e: ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
		const { name, value } = e.target;
		if (name === 'projectPeriodNum') {
			setState((prev) => ({
				...prev,
				projectPeriodNum: parseInt(value, 10) || 0,
			}));
		} else {
			setState((prev) => ({
				...prev,
				[name]: value,
			}));
		}
	};

	const handleImageChange = (e: ChangeEvent<HTMLInputElement>) => {
		const file = e.target.files?.[0];

		if (file) {
			const imageUrl = URL.createObjectURL(file);
			setState((prev) => ({
				...prev,
				projectImage: imageUrl,
			}));
		}
	};

	const handleUnitClick = (unit: string) => {
		setState((prev) => ({
			...prev,
			projectPeriodUnit: unit,
		}));
	};

	const handleSave = async () => {
		const { projectTitle, projectContent } = state;

		if (projectTitle === '') {
			alert('제목을 입력해주세요.');
			return;
		} else if (projectContent === '') {
			alert('본문을 입력해주세요.');
			return;
		} else if (position.totalList.length === 0) {
			alert('한 개 이상의 포지션을 입력해주세요.');
			return;
		} else {
			let periodCal;
			if (state.projectPeriodUnit === '달') {
				periodCal = state.projectPeriodNum * 4;
			} else {
				periodCal = state.projectPeriodNum;
			}

			const projectData = {
				title: projectTitle,
				content: projectContent,
				period: periodCal,
				totechList: [...techStack.mystack], // 객체 복사
				totalList: position.totalList,
			};
			console.log(projectData);

			const projectDataString = JSON.stringify(projectData);
			const form = new FormData();
			form.append('post', new Blob([projectDataString], { type: 'application/json' }));
			if (state.projectImage !== null) {
				form.append('file', state.projectImage);
			}
			console.log(projectDataString);
			console.log('FormData entries:');
			for (const pair of form.entries()) {
				console.log(pair[0] + ': ' + pair[1]);
			}
			console.log(memberId);
			try {
				const response = await axiosInstance.post(`/projects/${memberId}`, form, {
					headers: {
						'Content-Type': undefined,
					},
				});
				console.log('Response:', response);
				setModalVisible(true);
			} catch (error) {
				console.error('Post failed:', error);
			}
		}
	};
	const closeModal = () => {
		// Close the modal
		setModalVisible(false);
		navigate('../recruit/');
		window.scrollTo({ top: 0 });
	};

	return (
		<div className='bg-gray-100 w-screen h-max grid place-items-center'>
			<div className='bg-sub-color w-screen h-20 justify-center flex items-center font-bold text-4xl'>
				새 프로젝트 생성
			</div>
			<div className='bg-gray-300 w-9/12 h-auto p-16 m-5 border-black border-2 '>
				<div>
					<label htmlFor='projectTitle' className='font-bold text-lg my-3'>
						프로젝트 제목
					</label>
					<div>
						<input
							type='text'
							id='projectTitle'
							name='projectTitle'
							className='w-full h-10'
							placeholder='제목을 입력해 주세요'
							onChange={handleInputChange}
						/>
					</div>
				</div>
				<div className='my-3'>
					<label htmlFor='projectContent' className='font-bold text-lg '>
						프로젝트 내용
					</label>
					<div>
						<textarea
							id='projectContent'
							name='projectContent'
							className='w-full h-40'
							placeholder='내용을 입력해 주세요'
							onChange={handleInputChange}
						/>
					</div>

					<TechStack />

					<div className='my-3'>
						<label htmlFor='projectImage' className='font-bold text-lg'>
							프로젝트 이미지 업로드
						</label>
						<div>
							<input
								type='file'
								id='projectImage'
								name='projectImage'
								accept='image/*'
								onChange={handleImageChange}
								className='w-max mt-2'
							/>
							{state.projectImage && <img src={state.projectImage} alt='Uploaded' className='mt-2 max-h-40' />}
						</div>
					</div>

					<div className='my-3'>
						<label htmlFor='projectPeriod' className='font-bold text-lg my-3'>
							프로젝트 기간
						</label>
						<div>
							<span className='p-2'>약</span>
							<input
								type='text'
								id='projectPeriodNum'
								name='projectPeriodNum'
								className='w-20 h-10 p-1 mr-2'
								onChange={handleInputChange}
							/>
							<button
								className={`p-2 ${
									state.projectPeriodUnit === '주' ? 'bg-main-color text-white' : ''
								} border-main-color border-2`}
								onClick={() => handleUnitClick('주')}
							>
								주
							</button>
							<button
								className={`p-2 ${
									state.projectPeriodUnit === '달' ? 'bg-main-color text-white' : ''
								} border-main-color border-2`}
								onClick={() => handleUnitClick('달')}
							>
								달
							</button>
							<div className='ml-8 bg-white p-1 w-max mt-3'>달을 선택하시면 자동으로 주 단위로 계산됩니다.</div>
						</div>
					</div>

					<Position />
				</div>
				<button onClick={handleSave} className='mt-5 bg-main-color text-white p-3'>
					저장
				</button>

				{isModalVisible && (
					<div className='fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center'>
						<div className='bg-white p-8'>
							<p>저장되었습니다!</p>
							<button onClick={closeModal}>닫기</button>
						</div>
					</div>
				)}
			</div>
		</div>
	);
};

export default RecruitWritePage;
