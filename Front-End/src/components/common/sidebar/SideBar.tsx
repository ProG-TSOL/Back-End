import { Link, useLocation, useParams } from 'react-router-dom';
import CommuteCheckBtn from '../../commute/CommuteCheckBtn';
import '../../../styles/component/sidebar.scss';
interface SideTab {
	label: string;
	path: string;
	onClick?: () => void;
}

const SideBar = () => {
	const location = useLocation();
	const params = useParams<{ projectId: string; memberId: string }>();

	const projectId = Number(params.projectId);
	let memberId = 0;

	// 로컬 스토리지에서 userProfile을 가져옴
	const userProfileKey = 'userProfile';
	const userProfileString = localStorage.getItem(userProfileKey);
	if (userProfileString) {
		const userProfile = JSON.parse(userProfileString);
		memberId = userProfile.id;
	}

	const sideTabs: SideTab[] = [
		{ label: '홈', path: `/project/${projectId}` },
		{ label: '근태', path: `/project/${projectId}/commute` },
		{ label: '업무', path: `/project/${projectId}/task` },
		{ label: '피드', path: `/project/${projectId}/feed` },
		{ label: '회고', path: `/project/${projectId}/retrospect` },
	];

	return (
		<div className='flex flex-col'>
			<div
				className='flex flex-col sidebar-main w-52 items-center justify-evenly'
				style={{ height: 'calc(100vh - 64px)' }}
			>
				{/* 출퇴근 */}
				<CommuteCheckBtn projectId={projectId} memberId={memberId} />
				{/* <CommuteCheckBtn projectId={1} memberId={1} /> */}

				{/* 메뉴 */}
				<div className='flex flex-col justify-center items-center'>
					{sideTabs.map((tab, index) => (
						<Link
							key={index}
							to={tab.path}
							className={`my-2 p-2 text-lg ${
								location.pathname === tab.path
									? 'text-main-color underline underline-offset-4 hover:text-main-color hover:underline-offset-4'
									: 'text-slate-500'
							}`}
							onClick={tab.onClick}
						>
							{tab.label}
						</Link>
					))}
				</div>
			</div>
		</div>
	);
};

export default SideBar;
