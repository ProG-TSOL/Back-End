//accessToken 상태관리
import { createStore } from "zustand";

interface AuthState {
  accessToken: string | null;
  setAccessToken: (accessToken: string | null) => void;
}

export const useAuthStore = createStore<AuthState>((set) => ({
  accessToken: null,
  setAccessToken: (accessToken) => set({ accessToken: accessToken }),
}));
