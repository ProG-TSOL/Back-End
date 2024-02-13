//accessToken 상태관리
import { create } from "zustand";

interface AuthState {
  accessToken: string | null;
  setAccessToken: (accessToken: string | null) => void;
}

export const useAuthStore = create<AuthState>((set) => ({
  accessToken: null,
  setAccessToken: (accessToken) => set({ accessToken: accessToken }),
}));
