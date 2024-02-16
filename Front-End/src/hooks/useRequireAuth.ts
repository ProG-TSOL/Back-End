import { useAuthStore } from '../stores/useAuthStore';
import { useEffect } from 'react';
import { logout } from '../utils/logout';
import { useNavigate } from 'react-router-dom';
import { reissueToken } from '../utils/authUtils';

export const useRequireAuth = (additionalDeps?: boolean) => {
	const accessToken = useAuthStore((state) => state.accessToken);
	const navigate = useNavigate();

	useEffect(() => {
		const tryRefreshToken = async () => {
			//refreshToken을 사용해서 accessToken 재발급
			try {
				const newAccessToken = await reissueToken();
				if (newAccessToken) {
					useAuthStore.getState().setAccessToken(newAccessToken);
				} else {
					//재발급 실패 시 로그아웃 처리
					logout(navigate);
				}
			} catch (error) {
				console.error('Failed to refresh token', error);
				logout(navigate);
			}
		};

		//로그인 상태가 아니면 로그인 페이지로 이동
		if (!accessToken && additionalDeps) {
			tryRefreshToken();
		}
	}, [accessToken, navigate, additionalDeps]);

	return accessToken;
};
