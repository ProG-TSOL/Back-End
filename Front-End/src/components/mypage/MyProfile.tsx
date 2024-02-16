import { useState } from 'react';
import MyProfileDetail from './MyProfileDetail.tsx';

type ComponentType = 'MyProfileDetail' | 'MyGit' | 'MyProject';

const MyProfile = () => {
	const [selectedComponentType, setSelectedComponentType] = useState('MyProfileDetail');

	const handleProjectTypeChange = (type: ComponentType) => {
		setSelectedComponentType(type);
	};

	return (
		<div>
			<div className='flex justify-center'>
				<div
					className={`mx-16 text-2xl cursor-pointer ${
						selectedComponentType === 'MyProfileDetail' ? 'font-semibold text-main-color' : ''
					}`}
					onClick={() => handleProjectTypeChange('MyProfileDetail')}
				>
					상세정보
				</div>
			</div>

			{selectedComponentType === 'MyProfileDetail' && <MyProfileDetail />}
		</div>
	);
};

export default MyProfile;
