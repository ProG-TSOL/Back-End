import { axiosInstance } from "../apis/lib/axios"

interface ModifyKPTParams {
  retrospectId: number;
  text: string;
}

export const modifyKPT = async ({ retrospectId, text}: ModifyKPTParams) => {
  const data = {
    retrospectId: retrospectId,
    content: text,
  }

  const response = await axiosInstance.patch(`/retrospects/${retrospectId}`,data);
  return response;
}

export const deleteKPT = async (retrospectId: number): Promise<void> => {
  await axiosInstance.delete(`/retrospects/${retrospectId}`);
};