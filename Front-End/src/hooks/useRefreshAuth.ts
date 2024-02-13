import { useAuthStore } from "../stores/useAuthStore";
import { useEffect, useState } from "react";
import { logout } from "../utils/logout";
import { useNavigate } from "react-router-dom";
import { reissueToken } from "../utils/authUtils";

export const useRefreshAuth = () => {
  const [isLoading, setIsLoading] = useState(true);
  const [isAuthenticated, setIsAuthenticated] = useState(false);
  const accessToken = useAuthStore((state) => state.accessToken);
  const navigate = useNavigate();

  useEffect(() => {
    const tryRefreshToken = async () => {
      //refreshToken을 사용해서 accessToken 재발급
      try {
        const newAccessToken = await reissueToken();
        if (newAccessToken) {
          useAuthStore.getState().setAccessToken(newAccessToken);
          setIsAuthenticated(true); // 재발급 성공 시 인증 상태 업데이트
        } else {
          // 재발급 실패 시 로그아웃 처리
          logout(navigate);
          setIsAuthenticated(false); // 인증 실패 상태 업데이트
        }
      } catch (error) {
        console.error("Failed to refresh token", error);
        logout(navigate);
        setIsAuthenticated(false); // 예외 발생 시 인증 실패 상태 업데이트
      } finally {
        setIsLoading(false); // 로딩 상태 업데이트
      }
    };

    if (!accessToken) {
      tryRefreshToken();
    } else {
      setIsAuthenticated(true); // 이미 accessToken이 있으면 인증된 상태로 간주
      setIsLoading(false); // 로딩 상태 업데이트
    }
  }, [accessToken, navigate]);

  return { isLoading, isAuthenticated };
};
