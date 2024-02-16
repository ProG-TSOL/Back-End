import CommuteCalendar from '../../../components/calendar/CommuteCalendar';
import { useParams } from 'react-router-dom';
import { useRequireAuth } from '../../../hooks/useRequireAuth';
import ActionBoard from '../../../components/commute/ActionBoard';

const CommutePage = () => {
	//로그인이 필요한 페이지에 useRequireAuth 호출
	useRequireAuth();

	const params = useParams<{ projectId: string }>();
	const projectId = Number(params.projectId);
	let memberId = 0;

	// 로컬 스토리지에서 userProfile을 가져옴
	const userProfileKey = 'userProfile';
	const userProfileString = localStorage.getItem(userProfileKey);
	if (userProfileString) {
		const userProfile = JSON.parse(userProfileString);
		memberId = userProfile.id;
	}

	return (
		<div className='flex flex-col justify-center'>
			{/* action, ranking div */}
			<div className='flex justify-center my-3 w-auto'>
				<ActionBoard />
			</div>

			<div className='flex justify-center'>
				<CommuteCalendar projectId={projectId} memberId={memberId} w-auto />
			</div>
		</div>
	);
};

export default CommutePage;
