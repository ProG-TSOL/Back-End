import { useState, useRef, useEffect, CSSProperties } from 'react';
import FreeFeed from '../../../components/feed/FreeFeed';
import TaskFeed from '../../../components/feed/TaskFeed';
import { useRequireAuth } from '../../../hooks/useRequireAuth';

import '../../../styles/page/project-home/feed.scss';

const FeedPage = () => {
	useRequireAuth();

	const [page, setPage] = useState<string>('업무');
	const taskButtonRef = useRef<HTMLButtonElement>(null);
	const freeButtonRef = useRef<HTMLButtonElement>(null);
	const [highlightStyle, setHighlightStyle] = useState<React.CSSProperties>({});

	useEffect(() => {
		const taskButton = taskButtonRef.current;
		const freeButton = freeButtonRef.current;

		if (taskButton && freeButton) {
			const newStyle: CSSProperties = {
				left: page === '업무' ? taskButton.offsetLeft : freeButton.offsetLeft,
				width: page === '업무' ? taskButton.offsetWidth : freeButton.offsetWidth,
			};
			setHighlightStyle(newStyle);
		}
	}, [page]);

	const handleSectionClick = (section: string) => {
		setPage(section);
	};

	const selectedButtonClass = 'text-main-color';

	return (
		<div className='relative h-5/6 feed-main'>
			<div className='flex justify-center items-center relative mt-20 '>
				<button
					ref={taskButtonRef}
					className={`font-sans mb-2 text-2xl mr-40 ${page === '업무' ? selectedButtonClass : ''}`}
					onClick={() => handleSectionClick('업무')}
				>
					업무
				</button>
				<button
					ref={freeButtonRef}
					className={`font-sans mb-2 text-2xl ml-40 ${page === '자유' ? selectedButtonClass : ''}`}
					onClick={() => handleSectionClick('자유')}
				>
					자유
				</button>
				<div
					className='absolute bottom-0 h-2 bg-main-color transition-all duration-1000'
					style={{ left: `${highlightStyle.left}px`, width: `${highlightStyle.width}px` }}
				></div>
			</div>
			{/* 조건부 렌더링 */}
			{page === '업무' && <TaskFeed />}
			{page === '자유' && <FreeFeed />}
		</div>
	);
};

export default FeedPage;
