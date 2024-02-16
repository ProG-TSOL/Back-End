//react-modules
import { Link, useLocation, useNavigate } from 'react-router-dom';

//react-icons
import { useUserStore } from '../../../stores/useUserStore';
import { useEffect, useState } from 'react';
import { logout } from '../../../utils/logout';
import logo from '../../../assets/logo.png';
import ImageWithFallback from '../../../utils/DefaultImgage.tsx';

interface Tab {
	label: string;
	path: string;
	logo?: string;
	onClick?: () => void;
}

const Nav = () => {
	// const [activeTab, setActiveTab] = useState("");
	const location = useLocation();
	const navigate = useNavigate();
	const profile = useUserStore((state) => state.profile); // 프로필 정보 가져오기
	const [isAuthenticated, setIsAuthenticated] = useState(false);

	useEffect(() => {
		if (profile) {
			console.log('현재 프로필', profile);
			setIsAuthenticated(true);
		} else {
			setIsAuthenticated(false);
		}
	}, [profile]);

	const handleLogout = () => {
		logout(navigate);
		navigate('/');
	};

	const leftTabs: Tab[] = [
		{ label: '홈', path: '/', logo: logo },
		{ label: '프로젝트 모집', path: '/recruit' },
		{ label: '내 프로젝트', path: '/myproject' },
	];

	const rightTabs: Tab[] = isAuthenticated
		? [
				{ label: '마이페이지', path: '/mypage' },
				{ label: '로그아웃', path: '/', onClick: handleLogout },
		  ]
		: [
				{ label: '로그인', path: '/login' },
				{ label: '회원가입', path: '/signup' },
		  ];

	return (
		<div className='fixed flex justify-between items-center bg-white w-full h-16 px-4 text-lg font-semibold border border-bottom'>
			<div className='flex items-center space-x-4'>
				{leftTabs.map((tab, index) => (
					<Link
						key={index}
						to={tab.path}
						onClick={tab.onClick}
						className={location.pathname.startsWith(tab.path) ? 'text-main-color' : ''}
					>
						{tab.logo && typeof tab.logo === 'string' ? (
							<img src={tab.logo} alt='Logo' className='w-12 h-12' />
						) : (
							<div>{tab.label}</div>
						)}
					</Link>
				))}
			</div>
			<div className='flex space-x-4 items-center'>
				{isAuthenticated && profile && (
					<>
						<ImageWithFallback
							src={profile.imgUrl}
							alt='Profile'
							type='member'
							style='w-8 h-8 rounded-full object-cover'
						/>
						<div>{profile.nickname}</div>
					</>
				)}
				{rightTabs.map((tab, index) => (
					<Link
						key={index}
						to={tab.path}
						onClick={tab.onClick}
						className={location.pathname.startsWith(tab.path) && tab.label !== '로그아웃' ? 'text-main-color' : ''}
					>
						{tab.logo && <tab.logo />}
						{tab.label}
					</Link>
				))}
			</div>
		</div>
	);
};

export default Nav;
