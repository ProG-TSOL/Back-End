import { useEffect, useState } from 'react';
import { FaPlus } from 'react-icons/fa6';
import ModalEditor from './ModalEditor';
import { useUserStore } from '../../stores/useUserStore';
import { useParams } from 'react-router-dom';
import { axiosInstance } from '../../apis/lib/axios';
import useFeedStore from '../../stores/useFeedStore.ts';
import FreeFeedSimple from './FreeFeedSimple.tsx';

const FreeFeed = () => {
	const [rotateIcon, setRotateIcon] = useState(false);
	const [isModalOpen, setIsModalOpen] = useState(false);
	const [editorContent, setEditorContent] = useState('');

	const { profile } = useUserStore();
	const { projectId } = useParams();

	const handleIconClick = () => {
		setRotateIcon(!rotateIcon);
		setIsModalOpen(true);
	};

	const closeModal = () => {
		setIsModalOpen(false);
		setRotateIcon(false); // rotateIcon 상태를 직접 false로 설정
	};

	const editorChange = (content: string) => {
		setEditorContent(content);
	};

	const handleModalSubmit = async (title: string, content: string) => {
		const form = new FormData();

		const boardData = {
			projectId: projectId,
			memberId: profile?.id.toString(),
			title: title,
			content: content,
			isNotice: false,
		};

		const jsonData = JSON.stringify(boardData);

		form.set('board', new Blob([jsonData], { type: 'application/json' }));
		// POST 요청 로직

		try {
			const respons = await axiosInstance.post('/boards', form, {});

			console.log(respons);

			getFreeFeeds();

			setEditorContent('');
		} catch (e) {
			console.error(e);
		}
	};
	const updateGetFeeds = useFeedStore((state) => state.updateGetFeeds);

	const getFreeFeeds = async () => {
		try {
			console.log(projectId);

			const response = await axiosInstance.get(`/boards/${projectId}`, {});

			const data = response.data.data.boards;
			updateGetFeeds(data);
			console.log('프리피드', data);
		} catch (e) {
			console.error(e);
		}
	};

	useEffect(() => {
		getFreeFeeds();
	}, []);

	const { getFeeds } = useFeedStore();

	const deleteBoard = async (boardId: number, index: number) => {
		console.log('상위 삭제 함수 들어옴');
		try {
			const response = await axiosInstance.patch(`/boards/${boardId}`);

			console.log('deleted ' + response);

			popFeeds(index);
		} catch (e) {
			console.error(e);
		}
	};

	const [feeds, setFeeds] = useState(getFeeds);

	useEffect(() => {
		setFeeds(getFeeds);
	}, [getFeeds]);

	const popFeeds = (index: number) => {
		const newFeeds = [...feeds];
		newFeeds.splice(index, 1);
		setFeeds(newFeeds);
		console.log('삭제요청옴');
		console.log(newFeeds);
	};

	return (
		<div className='flex flex-col h-full '>
			<div className='p-8 w-full mt-10flex-grow overflow-y-scroll feed-contents-border'>
				{feeds.map((freeFeed, index) => (
					<div key={freeFeed.boardId}>
						<FreeFeedSimple
							memberId={freeFeed.memberId}
							nickname={freeFeed.nickname}
							imgUrl={freeFeed.imgUrl}
							position={freeFeed.position}
							boardId={freeFeed.boardId}
							createdAt={freeFeed.createdAt}
							isDeleted={freeFeed.isDeleted}
							title={freeFeed.title}
							viewCnt={freeFeed.viewCnt}
							isNotice={freeFeed.isNotice}
							popFeeds={() => deleteBoard(freeFeed.boardId, index)}
							index={index}
							getFreeFeeds={() => getFreeFeeds()}
						/>
					</div>
				))}
			</div>
			<button
				onClick={handleIconClick}
				className={`fixed bottom-20 right-1/4 transform transition-transform duration-500 ${
					rotateIcon ? 'rotate-45' : 'rotate-0'
				}`}
			>
				<FaPlus className='size-10' />
			</button>
			<ModalEditor
				isOpen={isModalOpen}
				onClose={closeModal}
				value={editorContent}
				onChange={editorChange}
				onSubmit={handleModalSubmit}
			/>
		</div>
	);
};

export default FreeFeed;
