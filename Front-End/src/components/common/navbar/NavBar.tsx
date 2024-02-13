//react-modules
import { Link, useLocation, useNavigate, useParams } from 'react-router-dom';

//react-icons
import { FaBell } from 'react-icons/fa6';
import { useUserStore } from '../../../stores/useUserStore';
import { useEffect } from 'react';
import { logout } from '../../../utils/logout';

interface Tab {
	label: string;
	path: string;
	logo?: React.ElementType;
	onClick?: () => void;
}

const Nav = () => {
	// const [activeTab, setActiveTab] = useState("");
	const { projectId } = useParams();
	const location = useLocation();
	const navigate = useNavigate();

	const profile = useUserStore((state) => state.profile); // 프로필 정보 가져오기

	useEffect(() => {
		if (profile) {
			console.log('현재 프로필', profile);
		}
	}, [profile]);

	const tabs: Tab[] = [
		{ label: '홈', path: '/', logo: FaBell },
		{ label: '프로젝트 모집', path: '/recruit' },
		{ label: '내 프로젝트', path: '/myproject' },
		{ label: '마이페이지', path: '/mypage', logo: FaBell },
		//로그인 상태에 따라 탭 변경
		// profile ? { label: '로그아웃', path: '/logout', onClick: logout(navigate) } : { label: '로그인', path: '/login' },
	];

	return (
		<div className='fixed flex bg-slate-200 w-full h-16'>
			<div className='flex space-x-2 items-center'>
				{tabs.map((tab, index) => (
					<Link
						key={index}
						to={tab.path}
						//path를 포함하고 있다면 text 색깔을 main color로 바꿔줌
						className={location.pathname.startsWith(tab.path) ? 'text-main-color' : 'text-slate-500'}
					>
						{tab.logo && <tab.logo />}
						{tab.label}
					</Link>
				))}
				{profile && (
					<>
						<img src={profile.imgUrl} alt='Profile' className='w-8 h-8 rounded-full' />
						<div>{profile.nickname}</div>
					</>
				)}
			</div>
		</div>
	);
};

export default Nav;
