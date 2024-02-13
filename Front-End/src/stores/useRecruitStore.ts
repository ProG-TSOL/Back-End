import create from 'zustand';
import { axiosInstance } from '../apis/lib/axios'; // Adjust the import path as necessary
import { useRequireAuth } from './../hooks/useRequireAuth';

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
  viewCnt: number;
  likeCnt: number;
}

interface RecruitStore {
  searchResults: SearchResult[];
  currentPage: number;
  selectedTechId: number | null;
  selectedStatusesId: number | null;
  searchInput: string;
  totalPages: number;
  setTotalPages: (pages: number) => void;
  updateSearchResults: (results: SearchResult[]) => void;
  setCurrentPage: (page: number) => void;
  setSelectedTechId: (id: number | null) => void;
  setSelectedStatusesId: (id: number | null) => void;
  setSearchInput: (input: string) => void;
  triggerSearch: () => Promise<void>;
}

const useRecruitStore = create<RecruitStore>((set) => ({
  useRequireAuth,
  searchResults: [],
  currentPage: 1,
  selectedTechId: null,
  selectedStatusesId: null,
  searchInput: '',
  totalPages: 0,
  setTotalPages: (pages) => set({ totalPages: pages }),
  updateSearchResults: (results) => set({ searchResults: results }),
  setCurrentPage: (page) => set({ currentPage: page }),
  setSelectedTechId: (id) => set({ selectedTechId: id }),
  setSelectedStatusesId: (id) => set({ selectedStatusesId: id }),
  setSearchInput: (input) => set({ searchInput: input }),
  triggerSearch: async () => {
    const { currentPage, selectedTechId, selectedStatusesId, searchInput } = useRecruitStore.getState();
    try {
      const response = await axiosInstance.get('/projects', {
        params: {
          keyword: searchInput,
          techCodes: selectedTechId,
          statusesCode: selectedStatusesId,
          page: currentPage - 1,
          size: 10,
        },
      });
      set({ searchResults: response.data.data.content });
    } catch (error) {
      console.error("Search failed:", error);
    }
  },
}));

export default useRecruitStore;
