import { useMutation, useQueryClient } from '@tanstack/react-query';
import { queryKeys } from '../constants/queryKeys';
import { axiosInstance } from './lib/axios';
import { useRequireAuth } from '../hooks/useRequireAuth';

//출근 - post
//api 호출 함수 정의
const startAttendance = async ({
	projectId,
	memberId,
}: {
	projectId: number;
	memberId: number;
}): Promise<AttendanceResponse> => {
	const response = await axiosInstance.post<AttendanceResponse>(`/attendances/logs/${projectId}/${memberId}/start`);
	return response.data;
};

//react query hook 사용해서 api 호출 및 캐시 관리
export const useAttendanceStartMutation = () => {
	const queryClient = useQueryClient();
	useRequireAuth();

	return useMutation<AttendanceResponse, unknown, { projectId: number; memberId: number }>({
		mutationFn: startAttendance,
		onSuccess: (data) => {
			console.log(data);
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
// const getAttendance = async ({ projectId, memberId }: { projectId: number; memberId: number }) => {
// 	const { data } = await axiosInstance.get(`/attendances/logs/${projectId}/${memberId}/startTime`);
// 	return data;
// };

// export const useAttendanceStartQuery = (projectId: number, memberId: number, enabled: boolean) => {
// 	useRequireAuth();
// 	return useQuery<AttendanceResponse>({
// 		queryKey: [queryKeys.attendance, projectId, memberId], //각 projectId, memberId에 맞는 쿼리만
// 		queryFn: () => getAttendance({ projectId, memberId }),
// 		staleTime: Infinity, //조회된 데이터를 post로 invalidate하기 전까지 유지
// 		enabled,
// 	});
// };

//근무 종료 - patch
const endAttendance = ({ projectId, memberId }: { projectId: number; memberId: number }) => {
	return axiosInstance.patch(`/attendances/logs/${projectId}/${memberId}/end`);
};

export const useAttendanceEndMutation = () => {
	const queryClient = useQueryClient();
	useRequireAuth();

	return useMutation({
		mutationFn: endAttendance,
		onSuccess(data, variables, context) {
			const { projectId, memberId } = variables;
			queryClient.invalidateQueries({ queryKey: [queryKeys.attendance, projectId, memberId] });
		},
	});
};
