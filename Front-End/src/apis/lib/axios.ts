import axios from 'axios';
import { useAuthStore } from '../../stores/useAuthStore';
import { ERROR_CODES } from '../../constants/errorCodes';
import { reissueToken } from '../../utils/authUtils';

export const axiosInstance = axios.create({
	baseURL: 'http://i10a210.p.ssafy.io:8080',
	// baseURL: 'https://ssafy-prog.shop/api',
	withCredentials: true, //쿠키 포함
});

//accessToken 자동으로 헤더에 추가
axiosInstance.interceptors.request.use((config) => {
	const accessToken = useAuthStore.getState().accessToken;
	if (accessToken && config.headers) {
		config.headers['Authorization'] = `Bearer ${accessToken}`;
		console.log(`Setting Authorization header: ${config.headers['Authorization']}`); // accessToken 로그 출력
	}
	return config;
});

//axios response에 따른 에러처리
axiosInstance.interceptors.response.use(
	(response) => response,
	async (error) => {
		if (!error.response) {
			return Promise.reject(error);
		}

		const errorCode = error.response.data.exceptionDto.errorCode;
		// const errorMsg = error.response.data.exceptionDto.errorMessage;

		const originalRequest = error.config;

		//originalRequest._retry는 axios 요청 객체에 임의로 추가한 속성
		//특정 요청이 이미 재시도되었는지 여부를 추적하는 데 사용됨
		//accessToken이 만료되었을 때 토큰을 자동으로 갱신하고 원래 요청을 재시도하는 로직을 구현할 때 사용
		if (errorCode === ERROR_CODES.TOKEN.EXPIRED_ACCESS_TOKEN && !originalRequest._retry) {
			originalRequest._retry = true; //재시도한 요청 -> 무한 루프 빠지지 않게 재시도 요청x
			try {
				const newAccessToken = await reissueToken();
				if (newAccessToken) {
					originalRequest.headers['Authorization'] = `Bearer ${newAccessToken}`;
					return axiosInstance(originalRequest);
				}
			} catch (refreshError) {
				return Promise.reject(refreshError);
			}
		} else {
			useAuthStore.getState().setAccessToken(null);
		}
		return Promise.reject(error);
	},
);
