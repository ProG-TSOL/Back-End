import axios from "axios";
import { useAuthStore } from "../../stores/authStore";

export const axiosInstance = axios.create({
  baseURL: "여기에 baseURL",
});

axiosInstance.interceptors.request.use((config) => {
  const accessToken = useAuthStore.getState().accessToken;
  if (accessToken && config.headers) {
    config.headers["Authorization"] = `Bearer ${accessToken}`;
  }
  return config;
});
