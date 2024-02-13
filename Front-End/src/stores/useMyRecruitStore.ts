//useRecruitStore.ts
import create from 'zustand';

interface TechCode {
  id: number;
  detailName: string;
}

interface ProjectStatus {
  detailDescription: string;
  id: number;
  detailName: string;
}

interface SearchResult {
  id: number;
  title: string;
  projectImgUrl: string;
  statusCode: ProjectStatus;
  techCodes: TechCode[];
  total: number;
  current: number;
}
interface MyRecruitStore {
  mySearchResults: SearchResult[];
  updateMySearchResults: (results: SearchResult[]) => void;
}

const useMyRecruitStore = create<MyRecruitStore>((set) => ({
  mySearchResults: [],
  updateMySearchResults: (results) => set((state) => ({ ...state, mySearchResults: results })),
}));

export default useMyRecruitStore;
