import { useState } from 'react';
import { FaMapPin, FaEllipsisVertical } from 'react-icons/fa6';
import FeedDetail from './FeedDetail';
import ModiDetail from './ModiDetail';

const TaskFeed = () => {
	const [showDetail, setShowDetail] = useState(false);
	const [showModiDelete, setShowModiDelete] = useState(false);
	const [showModiDetail, setShowModiDetail] = useState(false);

	const handleEllipsisClick = (event: React.MouseEvent) => {
		event.stopPropagation(); // 상위 버전으로 이벤트 전파 중단
		setShowModiDelete((prev) => !prev);
	};

	const handleModifyClick = (event: React.MouseEvent) => {
		event.stopPropagation();
		setShowModiDetail(true);
	};

	// ModiDetail 저장 버튼 핸들러
	const handleSaveModiDetail = (newContent: string) => {
		console.log('새로운 내용:', newContent);
		// 여기에 API 요청 로직 또는 다른 상태 업데이트 로직을 추가할 수 있습니다.
		setShowModiDetail(false); // 모달 닫기
	};

	const handleCloseModiDetail = () => {
		setShowModiDetail(false);
	};

	const handleDelete = (event: React.MouseEvent) => {
		// 삭제하기 버튼 눌렀을 때 요청.
		event.stopPropagation();
	};

	return (
		<div className='flex flex-col h-screen'>
			<div className='p-4 w-auto mt-10 m-60 border-2 border-gray-200 shadow-lg rounded-lg flex-grow'>
				<div className='mt-2 gap-4 border-2 border-main-color rounded-lg p-4' onClick={() => setShowDetail(true)}>
					<div className='flex justify-between items-center'>
						<div className='flex gap-4'>
							<p>프사</p>
							<p>닉네임</p>
							<p>날짜</p>
						</div>
						<div className='flex gap-2 relative'>
							<FaMapPin className='text-xl' />
							<FaEllipsisVertical className='text-xl' color='#4B33E3' onClick={handleEllipsisClick} />
							{showModiDelete && (
								<div className='absolute w-20 right-0 top-full bg-white shadow-md rounded-md mt-1'>
									<button onClick={handleModifyClick}>수정하기</button>
									<button onClick={handleDelete}>삭제하기</button>
								</div>
							)}
							{showModiDetail && (
								<ModiDetail initialContent='초기 내용' onSave={handleSaveModiDetail} onClose={handleCloseModiDetail} />
							)}{' '}
						</div>
					</div>
					<p className='mt-2'>여기에 제목이 들어갑니다. map.filter로</p>
				</div>
				{showDetail && <FeedDetail onClose={() => setShowDetail(false)} />} {/* 조건부 렌더링 및 onClose 핸들러 전달 */}
			</div>
		</div>
	);
};

export default TaskFeed;
