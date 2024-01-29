import { Link, useLocation } from 'react-router-dom';

interface SideTab {
	label: string;
	path: string;
	onClick?: () => void;
}

const SideBar = () => {
	const location = useLocation();
	const sideTabs: SideTab[] = [
		{ label: '홈', path: '/myproject' },
		{ label: '근태', path: '/commute' },
		{ label: '업무', path: '/task' },
		{ label: '피드', path: '/feed' },
		{ label: '회고', path: '/retrospect' },
	];

	return (
		<div className='flex mt-16 bg-sub-color w-48 justify-center items-center' 
				style={{ height: 'calc(100vh - 64px)' }}>
			<div className='flex flex-col justify-center items-center'>
				{sideTabs.map((tab, index) => (
					<Link
						key={index}
						to={tab.path}
						className={`my-2 p-2 ${location.pathname === tab.path ? 'text-main-color' : 'text-slate-500'}`}
						onClick={tab.onClick}
					>
						{tab.label}
					</Link>
				))}
			</div>
		</div>
	);
};

export default SideBar;
