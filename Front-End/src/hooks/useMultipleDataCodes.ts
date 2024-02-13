import { useQuery } from "@tanstack/react-query";
import { axiosInstance } from "../apis/lib/axios";

const fetchMultipleDetailCodes = async (codeTypes: string[]) => {
  const requests = codeTypes.map((codeType) =>
    axiosInstance.get(`/codes/details/${codeType}`).then(res => res.data.data)
  );
  // 비동기 요청을 병렬로 실행
  return Promise.all(requests);
};

export const useMultipleDetailCodes = (codeTypes: string[]) => {
  return useQuery({
    queryKey: ['detailCodes', ...codeTypes],
    queryFn: () => fetchMultipleDetailCodes(codeTypes),
  });
};

// 사용 예시 구문
// const { data: multipleDetailCodesData} = useMultipleDetailCodes(["KPT", "AnotherType"]);