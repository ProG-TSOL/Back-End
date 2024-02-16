import MyProfile from '../../components/mypage/MyProfile';
import { useRequireAuth } from '../../hooks/useRequireAuth';

const MyPage = () => {
	useRequireAuth();

	return (
		<div className='p-4'>
			<MyProfile />
		</div>
	);
};

export default MyPage;
