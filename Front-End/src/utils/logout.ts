// import { axiosInstance } from '../apis/lib/axios';
import { proxyAxiosInstance } from '../apis/lib/proxyAxios';
import { NavigateFunction } from 'react-router-dom';
import { useAuthStore } from '../stores/useAuthStore';
import { useUserStore } from '../stores/useUserStore';

export const logout = async (navigate: NavigateFunction) => {
	try {
		await proxyAxiosInstance.post('/members/logout');
	} catch (error) {
		console.error('Failed to logout', error);
	}

	//클라이언트에서 accessToken 초기화
	useAuthStore.getState().setAccessToken(null);

	//localStorage에서 사용자 프로필 삭제
	useUserStore.getState().clearProfile();

	//로그인 페이지로 이동
	navigate('/login');
};
