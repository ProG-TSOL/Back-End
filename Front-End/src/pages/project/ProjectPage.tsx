import { Outlet } from 'react-router-dom';
import RightBar from '../../components/common/rightbar/RightBar';
import SideBar from '../../components/common/sidebar/SideBar';
import { useRequireAuth } from '../../hooks/useRequireAuth';

const ProjectPage = () => {
	useRequireAuth();

	return (
		<div className='flex h-full'>
			<div className='flex-none'>
				<SideBar />
			</div>
			<div className='flex-grow'>
				<Outlet />
			</div>
			<div className='flex-none'>
				<RightBar />
			</div>
		</div>
	);
};

export default ProjectPage;
