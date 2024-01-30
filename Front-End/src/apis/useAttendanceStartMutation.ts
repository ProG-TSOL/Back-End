import axios from 'axios';
import { useMutation, useQueryClient } from '@tanstack/react-query';
import { queryKeys } from '../constants/queryKeys';

//api 호출 함수 정의
const startAttendance = (projectId: string) => {
	return axios.post(`{{ip}}/attendance_logs/${projectId}/start`);
};

//react query hook 사용해서 api 호출 및 캐시 관리
export const useAttendanceStartMutation = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: startAttendance,
    onSuccess: () => {
      //요청 성공시 캐시 무효화
      //쿼리키는 보통 상수로 constants파일에 관리
      //여기에 get 요청을 queryKey로 설정해줘야 함
      queryClient.invalidateQueries({ queryKey: [queryKeys.attendance] });
    },
  });
};
