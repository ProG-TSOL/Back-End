/* eslint-disable @typescript-eslint/no-explicit-any */
import React, { useEffect, useState } from 'react';
import { Link, useParams } from 'react-router-dom';
import Comment from '../../components/comment/Comment';
import { axiosInstance } from '../../apis/lib/axios';
import { useRequireAuth } from '../../hooks/useRequireAuth';
import { useUserStore } from '../../stores/useUserStore';
import { FaEye } from 'react-icons/fa6';
import { FaHeart } from 'react-icons/fa6';
import { FaArrowLeftLong } from 'react-icons/fa6';

const RecruitProjectPage = () => {
	useRequireAuth();
	const { profile } = useUserStore();
	const memberId = profile?.id;
	const [title, setTitle] = useState('');
	const [description, setDescription] = useState('');
	const [img, setImg] = useState('');
	const [mystack, setMyStack] = useState<string[]>([]);
	const [period, setPeriod] = useState('');
	const [number, setNumber] = useState<number>(0);
	const [view, setView] = useState('');
	const [like, setLike] = useState('');
	const [isLike, setIsLike] = useState<number>(0);
	const [positions, setPositions] = useState({
		posName: [],
		posCode: [],
		posNowNumber: [],
		posNumber: [],
	});
	const [MyProject, setMyProject] = useState(true);
	const [getApply, setGetApply] = useState('');
	const { projectId } = useParams();

	const apply = async (posName: never, posCode: never) => {
		console.log(`Applying for position: ${posName} with Code: ${posCode} in ${projectId} by ${memberId}`);
		try {
			const response = await axiosInstance.post(`/projects/${projectId}/application/${memberId}?jobCode=${posCode}`);
			console.log(response);
			getData();
		} catch (error) {
			console.error('Apply failed:', error);
		}
	};
	const cancelApply = async () => {
		try {
			const response = await axiosInstance.delete(
				`/projects/${projectId}/refuseMember/${memberId}?refuseMemberId=${memberId}`,
			);
			console.log(response);
		} catch (error) {
			console.error('Apply failed:', error);
		}
	};

	const addLike = async () => {
		try {
			const response = await axiosInstance.post(`/projects/${projectId}/like/${memberId}/add`);
			console.log(response);
			getData();
		} catch (error) {
			console.error('Apply failed:', error);
		}
	};
	const deleteLike = async () => {
		try {
			const response = await axiosInstance.delete(`/projects/${projectId}/like/${memberId}/delete`);
			console.log(response);
			getData();
		} catch (error) {
			console.error('Apply failed:', error);
		}
	};
	const toggleLike = async () => {
		try {
			if (isLike === 0) {
				await addLike();
			} else if (isLike === 1) {
				await deleteLike();
			}
		} catch (error) {
			console.error('Toggle like failed:', error);
		}
	};

	const getData = async () => {
		try {
			const response = await axiosInstance.get(`/projects/${projectId}/${memberId}`, {});
			const data = response.data.data;

			console.log(data);
			setTitle(data.title);
			setDescription(data.content);
			setImg(data.projectImgUrl);
			const mystack = data.techCodes.map((tech: { detailName: any }) => tech.detailName);
			setMyStack(mystack);
			setPeriod(data.period);
			setLike(data.likeCnt);
			setView(data.viewCnt);
			setIsLike(data.isLike);
			let memberNum = 0; // memberNum을 먼저 초기화

			for (let i = 0; i < data.projectTotals.length; i++) {
				memberNum += data.projectTotals[i].total;
			}
			setNumber(memberNum);
			if (data.applicationStatus) {
				setGetApply(data.applicationStatus.id);
			} else {
				setGetApply('');
			}
			console.log(getApply);

			const currentArray = data.projectTotals.map((item: { current: any }) => item.current);
			const totalArray = data.projectTotals.map((item: { total: any }) => item.total);
			const jobIdArray = data.projectTotals.map((item: { jobCode: { id: any } }) => item.jobCode.id);
			const jobDescriptionArray = data.projectTotals.map(
				(item: { jobCode: { detailDescription: any } }) => item.jobCode.detailDescription,
			);
			setPositions({
				posName: jobDescriptionArray,
				posCode: jobIdArray,
				posNowNumber: currentArray,
				posNumber: totalArray,
			});

			setMyProject(data.isMember);
		} catch (error) {
			console.error('Loading failed:', error);
		}
	};
	useEffect(() => {
		getData();
		console.log(projectId);
		// eslint-disable-next-line react-hooks/exhaustive-deps
	}, []);

	return (
		<div>
			<div className='flex items-center justify-center '>
				<div className='w-10/12 h-screen flex p-10 flex-col '>
					<div className='h-auto border-2 border-black rounded-3xl p-5'>
						<div className='align-middle text-center text-5xl font-bold mt-5'> {title}</div>
						<hr className='m-3 border-main-color border-1' />
						<div className='flex justify-between m-5'>
							<div className='w-9/12 mr-3'>
								<img src={img} className='w-full h-36' alt='Project' />
							</div>
							<div className='flex-col w-3/12 h-52'>
								<div className='border-2 border-black h-24 mb-4 rounded-lg p-2'>
									예상기간
									<div className='text-center text-3xl mt-3'>{period} 주</div>
								</div>
								<div className='border-2 border-black h-24 rounded-lg p-2'>
									{' '}
									총 인원
									<div className='text-center text-3xl mt-3'>{number} 명</div>
								</div>
							</div>
						</div>

						<div className='m-5'>
							<div className='flex flex-wrap'>
								{mystack.map((tech, index) => (
									<div key={index} className='bg-gray-200 p-1 m-1 rounded-full'>
										{tech}
									</div>
								))}
							</div>
						</div>
						<hr className='m-3 border-main-color border-1' />
						<div className=' m-5'>
							<div>
								{description.split('\n').map((line, index) => (
									<React.Fragment key={index}>
										{line}
										<br />
									</React.Fragment>
								))}
							</div>
						</div>
						<hr className='m-3 border-main-color border-1' />
						<div className=' m-5'>
							<div className=' h-40 p-2 flex-col '>
								{MyProject ? (
									<div className='flex flex-col'>
										<div className='text-2xl h-8'>프로젝트 상세</div>
										<div className='flex items-center justify-center'>
											<Link
												to={`/project/${projectId}`}
												className=' bg-main-color text-white p-2 rounded font-bold text-3xl m-5 w-max text-center'
											>
												프로젝트로 이동하기
											</Link>
										</div>
									</div>
								) : (
									<>
										<div className='text-2xl h-8'>구성인원</div>
										<div className='overflow-y-scroll max-h-28 flex flex-col items-center justify-start'>
											{positions.posName.map((name, index) => (
												<div key={index} className='h-8 m-1'>
													{`${name}: ${positions.posNowNumber[index]} / ${positions.posNumber[index]}명`}
													{positions.posNowNumber[index] < positions.posNumber[index] ? (
														<>
															{getApply === '' ? (
																<button
																	className='ml-3 bg-main-color text-white p-1 rounded '
																	onClick={() => apply(name, positions.posCode[index])}
																>
																	지원하기
																</button>
															) : getApply === positions.posCode[index] ? (
																<button
																	className='ml-3 bg-red-300 text-white p-1 rounded'
																	onClick={() => cancelApply()}
																>
																	취소하기
																</button>
															) : (
																<button className='ml-3 bg-white text-white p-1 rounded' disabled>
																	공백
																</button>
															)}
														</>
													) : (
														<button className='ml-3 bg-orange-500 text-black p-1 rounded' disabled>
															모집완료
														</button>
													)}
												</div>
											))}
										</div>
									</>
								)}
							</div>
						</div>
						<hr className='m-3 border-main-color border-1' />
						<div className=' m-5 flex justify-center'>
							<div className='border-black border-2 m-5 w-32 h-28 flex-col rounded-3xl p-2'>
								<div className='flex justify-center'>
									<FaEye className='w-14 h-12' />
								</div>
								<div className='flex justify-center'>조회수</div>
								<div className='flex justify-center'>{view}</div>
							</div>
							<div
								className={`border-black border-2 m-5 w-32 h-28 rounded-3xl p-2 cursor-pointer ${
									isLike === 1 ? 'bg-sub-color' : ''
								}`}
								onClick={toggleLike}
							>
								<div className='flex justify-center'>
									<FaHeart className='w-14 h-12' />
								</div>
								<div className='flex justify-center'>좋아요</div>
								<div className='flex justify-center'>{like}</div>
							</div>

							<div className='border-black border-2 m-5 w-32 h-28 rounded-3xl p-2'>
								<Link to='../recruit' onClick={() => window.scrollTo({ top: 0 })}>
									<div className='flex justify-center'>
										<FaArrowLeftLong className='w-14 h-12' />
									</div>
									<div className='flex justify-center'>목록으로</div>
									<div className='flex text-white'>0</div>
								</Link>
							</div>
						</div>
						<hr className='m-3 border-main-color border-1' />
						<div className='border-2 border-black m-5'>
							<Comment />
						</div>
					</div>
				</div>
			</div>
		</div>
	);
};

export default RecruitProjectPage;
