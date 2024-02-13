import { useQuery } from '@tanstack/react-query';
import { axiosInstance } from './lib/axios';
import { queryKeys } from '../constants/queryKeys';

interface ActionDatas {
	actionId: number;
	content: string[];
	week: number;
}

interface ActionData {
	responseTime: string;
	status: string;
	cnt: number;
	data: ActionDatas[];
}

const getAction = async (projectId: number): Promise<ActionData> => {
	const { data } = await axiosInstance.get<ActionData>(`/actions/${projectId}`);
	return data;
};

export const useActionQuery = (projectId: number) => {
	return useQuery<ActionData>({
		queryKey: [queryKeys.getAction],
		queryFn: () => getAction(projectId),
	});
};
