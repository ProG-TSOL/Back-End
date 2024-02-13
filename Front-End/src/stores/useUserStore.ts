import { create } from 'zustand';

interface UserProfile {
	id: number;
	email: string;
	nickname: string;
	imgUrl: string;
}

interface UserState {
	profile: UserProfile | null;
	setProfile: (profile: UserProfile) => void;
	clearProfile: () => void;
}

const userProfileKey = 'userProfile';

// 사용자 프로필 localstorage에서 가져오기
const getStoredProfile = (): UserProfile | null => {
	const storedProfile = localStorage.getItem(userProfileKey);
	return storedProfile ? JSON.parse(storedProfile) : null;
};

// 사용자 프로필 localstorage에 저장
const storeProfile = (profile: UserProfile) => {
	localStorage.setItem(userProfileKey, JSON.stringify(profile));
};

// logout시 사용자 프로필 localstorage에서 삭제
const clearStoredProfile = () => {
	localStorage.removeItem(userProfileKey);
};

export const useUserStore = create<UserState>((set) => ({
	// 초기상태 : localstorage에서 사용자 프로필 가져오기
	profile: getStoredProfile(),

	// 프로필을 localstorage에 저장하고 상태 업데이트
	setProfile: (profile) => {
		storeProfile(profile);
		set({ profile });
	},

	// 사용자 프로필 localstorage에서 삭제
	clearProfile: () => {
		clearStoredProfile();
		set({ profile: null });
	},
}));
