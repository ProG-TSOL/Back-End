import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import { queryKeys } from '../constants/queryKeys';
import { axiosInstance } from './lib/axios';
// import { proxyAxiosInstance } from './lib/proxyAxios';

//출근 - post
//api 호출 함수 정의
const startAttendance = ({ projectId, memberId }: { projectId: number; memberId: number }) => {
	return axiosInstance.post(`/attendances/logs/${projectId}/${memberId}/start`);
};

//react query hook 사용해서 api 호출 및 캐시 관리
export const useAttendanceStartMutation = () => {
	const queryClient = useQueryClient();

	return useMutation({
		mutationFn: startAttendance,
		onSuccess: (data, variables, context) => {
			//variables에서 projectId와 memberId 추출
			const { projectId, memberId } = variables;
			//요청 성공시 캐시 무효화
			//쿼리키는 보통 상수로 constants파일에 관리
			//여기에 get 요청을 queryKey로 설정해줘야 함
			queryClient.invalidateQueries({ queryKey: [queryKeys.attendance, projectId, memberId] });
		},
	});
};

interface AttendanceResponse {
	responseTime: string;
	status: string;
	cnt: number;
	data: {
		isWorking: boolean;
		id: number;
		startAt: string;
	};
}

//출근시간 조회 - get
const getAttendance = async ({ projectId, memberId }: { projectId: number; memberId: number }) => {
	const { data } = await axiosInstance.get(`/attendances/logs/${projectId}/${memberId}/startTime`);
	return data;
};

export const useAttendanceStartQuery = (projectId: number, memberId: number) => {
	return useQuery<AttendanceResponse>({
		queryKey: [queryKeys.attendance, projectId, memberId], //각 projectId, memberId에 맞는 쿼리만
		queryFn: () => getAttendance({ projectId, memberId }),
		staleTime: Infinity, //조회된 데이터를 post로 invalidate하기 전까지 유지
	});
};

//근무 종료 - patch
const endAttendance = ({ projectId, memberId }: { projectId: number; memberId: number }) => {
	return axiosInstance.patch(`/attendances/logs/${projectId}/${memberId}/end`);
};

export const useAttendanceEndMutation = () => {
	const queryClient = useQueryClient();

	return useMutation({
		mutationFn: endAttendance,
		onSuccess(data, variables, context) {
			const { projectId, memberId } = variables;
			queryClient.invalidateQueries({ queryKey: [queryKeys.attendance, projectId, memberId] });
		},
	});
};
