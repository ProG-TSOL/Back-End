import { useQuery } from "@tanstack/react-query";
import { axiosInstance } from "../apis/lib/axios";
import { useAuthStore } from "../stores/useAuthStore";

const fetchDetailCodes = async (codeType: string) => {
  const response = await axiosInstance.get(`/codes/details/${codeType}`);
  console.log(response.data.data)
  return response.data.data;
};

export const useDetailCodes = (codeType: string) => {
  const accessToken = useAuthStore((state) => state.accessToken);
  // 설정 객체를 단일 인자로 전달
  return useQuery({
    queryKey: ['detailCodes', codeType],
    queryFn: () => fetchDetailCodes(codeType),
    enabled: !!accessToken, // accessToken이 있을 때만 쿼리를 실행합니다.
  });
}
// 사용 예시 구문
// const { data: detailCodesData } = useDetailCodes("KPT");