import { FC, useCallback, useEffect, useRef, useState } from 'react';
import { FaEllipsisVertical } from 'react-icons/fa6';
import { axiosInstance } from '../../apis/lib/axios.ts';
import ModiDetail from './ModiDetail.tsx';
import Comment from '../comment/Comment.tsx';
import { useRequireAuth } from '../../hooks/useRequireAuth.ts';

type FeedDetailProps = {
	boardId: number;
	onClose?: () => void;
	popFeeds?: (boardId: number, index: number) => void;
	index: number;
	getFreeFeeds: () => void;
};

interface Feed {
	memberId: number;
	nickname: string;
	imgUrl: string;
	boardId: number;
	createdAt: string;
	isDeleted: boolean;
	title: string;
	content: string;
	viewCnt: number;
	isNotice: boolean;
}

const FeedDetail: FC<FeedDetailProps> = ({ boardId, onClose = () => {}, popFeeds, index, getFreeFeeds }) => {
	useRequireAuth();
	const modalRef = useRef<HTMLDivElement>(null);
	// useCallBack, React의 훅 중 하나, 함수의 메모이제이션에 사용 / 반복된 계산, 메모리에 저장.
	const handleClickOutside = useCallback(
		(event: MouseEvent) => {
			if (
				modalRef.current &&
				event.target instanceof Node && // 그래서 event.target을 instanceof 을 사용하여 Node인지 확인
				!modalRef.current.contains(event.target) // event.target은 EventTarget 타입이지만, contains는 Node 타입을 필요로 함.
			) {
				onClose();
			}
		},
		[onClose],
	);

	// 컴포넌트가 마운트될 때 이벤트 리스너 추가
	useEffect(() => {
		document.addEventListener('mousedown', handleClickOutside);
		return () => {
			// 컴포넌트가 언마운트될 때 이벤트 리스너 제거
			document.removeEventListener('mousedown', handleClickOutside);
		};
	}, [handleClickOutside]);

	const [feed, setFeed] = useState<Feed>({
		memberId: 0,
		nickname: '',
		imgUrl: '',
		boardId: 0,
		createdAt: '',
		isDeleted: false,
		title: '',
		content: '',
		viewCnt: 0,
		isNotice: false,
	});

	const getFreeFeed = async () => {
		try {
			const response = await axiosInstance.get(`/boards/detail/${boardId}`, {});

			const data = response.data.data;
			setFeed(() => ({
				memberId: data.memberId,
				nickname: data.nickname,
				imgUrl: data.imgUrl,
				boardId: data.boardId,
				createdAt: data.createdAt,
				isDeleted: data.isDeleted,
				title: data.title,
				content: data.content,
				viewCnt: data.viewCnt,
				isNotice: data.isNotice,
			}));
			console.log('피드 디테일', data);
			getFreeFeeds();
		} catch (e) {
			console.error(e);
		}
	};

	useEffect(() => {
		getFreeFeed();
	}, []);

	const [showModiDelete, setShowModiDelete] = useState(false);
	//수정 모달 상태
	const [showModiDetail, setShowModiDetail] = useState(false);

	const handleEllipsisClick = (event: React.MouseEvent) => {
		event.stopPropagation(); // 상위 버전으로 이벤트 전파 중단
		setShowModiDelete((prev) => !prev);
	};

	//수정하기 버튼 클릭
	const handleModifyClick = (event: React.MouseEvent) => {
		event.stopPropagation();
		setShowModiDetail(true);
		// onClose();
	};

	//삭제하기 버튼 클릭
	const handleDelete = (event: React.MouseEvent) => {
		// 삭제하기 버튼 눌렀을 때 요청.
		event.stopPropagation();
		console.log('버튼 클릭');
		if (popFeeds) {
			console.log('삭제 함수 실행');
			popFeeds(boardId, index);
		}
	};

	const formatDate = (dateString: string): string => {
		const serverDateTime = new Date(dateString);

		const koreaDateTime = new Date(serverDateTime.getTime() + 9 * 60 * 60 * 1000);

		const formattedDateTime = `${koreaDateTime.getFullYear()}.${String(koreaDateTime.getMonth() + 1).padStart(
			2,
			'0',
		)}.${String(koreaDateTime.getDate()).padStart(2, '0')} ${String(koreaDateTime.getHours()).padStart(
			2,
			'0',
		)}:${String(koreaDateTime.getMinutes()).padStart(2, '0')}:${String(koreaDateTime.getSeconds()).padStart(2, '0')}`;

		return formattedDateTime;
	};

	return (
		<div className='fixed inset-0 bg-gray-200 bg-opacity-50 flex justify-center items-center z-10 '>
			<div
				ref={modalRef}
				id='indi'
				className='bg-white mt-2 gap-4 border-2 border-main-color rounded-lg p-4 shadow-lg w-1/2 h-4/5 overflow-y-auto'
			>
				<div className='flex pt-4 pr-2 pl-2 pb-4 justify-between items-center'>
					<div className='flex justify-between gap-4'>
						<div className='row'>
							<p>
								<img className='ml-2 w-12 h-12 rounded-full' src={feed.imgUrl} alt='Profile' />
							</p>
							<p className='ml-3 pt-2'>{feed.nickname}</p>
						</div>
					</div>
					<div className='flex gap-2'>
						<p>{formatDate(feed.createdAt)}</p>
						<FaEllipsisVertical className='text-xl' color='#4B33E3' onClick={handleEllipsisClick} />
						{showModiDelete && (
							<div className='grid grid-rows-1 absolute 40 right-1/4 top-36 bg-white shadow-xl border-solid border-2 border-indigo-600 rounded-lg mt-1 z-40 h-30'>
								<button className='w-40 m-2 hover:text-violet-600' onClick={handleModifyClick}>
									수정하기
								</button>
								<button className='w-40 m-2 hover:text-violet-600' onClick={handleDelete}>
									삭제하기
								</button>
							</div>
						)}
						{showModiDetail && (
							<div className='z-40'>
								<ModiDetail
									boardId={boardId}
									onClose={() => setShowModiDetail(false)}
									getFreeFeedDetail={() => getFreeFeed()}
								/>
							</div>
						)}{' '}
					</div>
				</div>
				<div>
					<p className='mt-2 mb-4 text-3xl'>{feed.title}</p>
					<div>
						<div className='ml-2 mt-5 mb-4 h-auto' dangerouslySetInnerHTML={{ __html: feed.content }}></div>
					</div>
					<hr />
					<div className='bg-scroll pt-4'>
						<Comment contentCode='게시물' contentId={boardId.toString()} />
					</div>
				</div>
			</div>
		</div>
	);
};

export default FeedDetail;
