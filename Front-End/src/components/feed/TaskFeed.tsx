/* eslint-disable react-hooks/exhaustive-deps */
import { useEffect, useState } from 'react';
import TaskFeedSimple from './TaskFeedSimple.tsx';
import { axiosInstance } from '../../apis/lib/axios.ts';
import { useUserStore } from '../../stores/useUserStore.ts';
import { useParams } from 'react-router-dom';

interface Feed {
	feedId: number;
	contentsCode: number;
	contentsId: number;
	memberImgUrl: string;
	feedContent: string;
	// Add other properties as needed
}

const TaskFeed = () => {
	const { profile } = useUserStore();
	const memberId = profile?.id;
	const { projectId } = useParams<{ projectId: string }>();
	const [feeds, setFeeds] = useState<Feed[]>([]);

	const getTaskFeed = async () => {
		console.log(projectId);
		try {
			const response = await axiosInstance.get('/feeds', {
				params: {
					memberId: memberId,
					projectId: projectId,
				},
			});

			setFeeds(response.data.data);
		} catch (e) {
			console.error(e);
		}
	};

	useEffect(() => {
		getTaskFeed();
	}, []);

	return (
		<div className='flex flex-col h-full'>
			<div className='p-8 w-full mt-10 flex-grow overflow-y-auto feed-contents-border'>
				{feeds.map((feed) => (
					<div className='flex flex-col' key={feed.feedId}>
						<TaskFeedSimple
							feedId={feed.feedId}
							contentsCode={feed.contentsCode}
							contentsId={feed.contentsId}
							memberImgUrl={feed.memberImgUrl}
							feedContent={feed.feedContent}
						/>
					</div>
				))}
			</div>
		</div>
	);
};

export default TaskFeed;
