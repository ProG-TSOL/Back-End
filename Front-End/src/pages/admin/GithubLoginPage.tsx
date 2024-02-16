// import { axiosInstance } from '../../apis/lib/axios';
import { proxyAxiosInstance } from '../../apis/lib/proxyAxios';
import { useEffect } from 'react';
import { useAuthStore } from '../../stores/useAuthStore';
import { useUserStore } from '../../stores/useUserStore';
import { fetchUserProfile } from '../../utils/fetchUserProfile';
import { useNavigate } from 'react-router-dom';

const GithubLoginPage = () => {
	const { setAccessToken } = useAuthStore();
	const { setProfile } = useUserStore();
	const navigate = useNavigate();

	useEffect(() => {
		//url에서 code 쿼리 파라미터 추출
		const urlParams = new URLSearchParams(window.location.search);
		const code = urlParams.get('code');

		console.log('urlParams : ', urlParams);
		console.log('code : ', code);

		//서버로 code post 요청
		if (code) {
			postCode(code);
		}
	}, []);

	const postCode = async (code: string) => {
		try {
			const response = await proxyAxiosInstance.post(`/members/login/oauth2/github?code=${code}`);
			console.log('github response : ', response.data);

			const accessToken = response.headers['accesstoken'];

			if (accessToken) {
				setAccessToken(accessToken); // 추출한 토큰을 Zustand 스토어에 저장

				await fetchUserProfile(accessToken, setProfile, navigate); // 사용자 프로필 정보 가져오기
			}
		} catch (error) {
			console.log('github error : ', error);
		}
	};

	return <div></div>;
};

export default GithubLoginPage;
