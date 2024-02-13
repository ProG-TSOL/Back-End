// import { axiosInstance } from '../apis/lib/axios';
import { proxyAxiosInstance } from '../apis/lib/proxyAxios';
import { useAuthStore } from '../stores/useAuthStore';

//accessToken 재발급 함수
export const reissueToken = async () => {
	try {
		const response = await proxyAxiosInstance.get('/members/reissue-token');
		const newAccessToken = response.headers['accesstoken'];
		if (newAccessToken) {
			useAuthStore.getState().setAccessToken(newAccessToken);
			return newAccessToken;
		}
	} catch (error) {
		console.error('Failed to refresh token', error);
		useAuthStore.getState().setAccessToken(null); //accessToken 초기화
	}
	return null;
};
