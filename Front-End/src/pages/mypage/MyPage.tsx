import MyProfile from '../../components/mypage/MyProfile';
import MyProject from '../../components/mypage/MyProject';
import MyGit from '../../components/mypage/MyGit';
import { useRequireAuth } from '../../hooks/useRequireAuth';

const MyPage = () => {
	useRequireAuth();

	return (
		<div className='p-4'>
			<MyProfile />
			<hr className='border-main-color border-1 mt-4 mb-5' />
			<MyProject />
			<hr className='border-main-color border-1 mt-4 mb-5' />
			<MyGit />
		</div>
	);
};

export default MyPage;
