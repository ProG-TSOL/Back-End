import NavBar from '../components/common/navbar/NavBar';
import { Outlet } from 'react-router-dom';

const Layout = () => {
	return (
		<div className=''>
			<div className='fixed top-0 w-full z-50'>
				<NavBar />
			</div>
			<div className='pt-16'>
				<Outlet />
			</div>
		</div>
	);
};

export default Layout;
