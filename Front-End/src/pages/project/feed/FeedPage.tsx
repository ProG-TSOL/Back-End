import { useState } from 'react';
import FreeFeed from '../../../components/feed/FreeFeed';
import TaskFeed from '../../../components/feed/TaskFeed';
import { FaPlus } from 'react-icons/fa6';

const FeedPage = () => {
	const [page, setPage] = useState<string>('업무');
	const [rotateIcon, setRotateIcon] = useState(false);

	const handleSectionClick = (section: string) => {
		setPage(section);
	};

	const handleIconClick = () => {
		setRotateIcon(!rotateIcon);
	};

	const selectedButtonClass = 'text-main-color border-b-4 border-main-color';

	return (
		<div className='relative'>
			<div className='flex justify-center items-center'>
				<button
					className={`font-sans text-2xl mt-20 mr-40 ${page === '업무' ? selectedButtonClass : ''}`}
					onClick={() => handleSectionClick('업무')}
				>
					업무
				</button>
				<button
					className={`font-sans text-2xl mt-20 ml-40 ${page === '자유' ? selectedButtonClass : ''}`}
					onClick={() => handleSectionClick('자유')}
				>
					자유
				</button>
				{/* 아이콘 버튼 및 조건부 렌더링 */}
				<button
					onClick={handleIconClick}
					className={`fixed bottom-20 right-96 transform transition-transform duration-500 ${
						rotateIcon ? 'rotate-45' : 'rotate-0'
					}`}
				>
					<FaPlus className='size-10' />
				</button>
			</div>

			{/* 조건부 렌더링 */}
			{page === '업무' && <TaskFeed />}
			{page === '자유' && <FreeFeed />}
		</div>
	);
};

export default FeedPage;
