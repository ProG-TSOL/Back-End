/* eslint-disable @typescript-eslint/no-explicit-any */
import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import { axiosInstance } from '../../../apis/lib/axios';
import { useRequireAuth } from '../../../hooks/useRequireAuth';
import { useUserStore } from '../../../stores/useUserStore';

interface Position {
  posName: string;
  posCode: number;
  posNowNumber: number;
  posNumber: number;
  members: Member[];
}
interface Member {
  nickname: string;
  id: number;
}

interface MemberData {
  jobCode: {
    id: number;
    detailDescription: string;
  };
  member: {
    nickname: string;
    id: number;
  };
}
interface ActionableModalData {
  memberId: number;
  posCode: number;
  visible: boolean;
  x: number;
  y: number;
  nickname: string;
}



const MemberSettingPage = () => {
  useRequireAuth();
  const [actionableModal, setActionableModal] = useState<ActionableModalData | null>(null);
  const [selectedPosCode, setSelectedPosCode] = useState<number | null>(null);

  const { profile } = useUserStore();
  const memberId = profile?.id;
  const [positions, setPositions] = useState<Position[]>([]);
  const [applyList, setApplyList] = useState<MemberData[]>([]);
  const { projectId } = useParams<{ projectId: string }>(); // Specify the type of useParams

  const getData = async () => {
    try {
      const positionsResponse = await axiosInstance.get(`/projects/${projectId}/${memberId}`);
      const positionsData = positionsResponse.data.data;
      console.log(positionsData);
      const updatedPositions: Position[] = positionsData.projectTotals.map((item: any) => ({
        posName: item.jobCode.detailDescription,
        posCode: item.jobCode.id,
        posNowNumber: item.current,
        posNumber: item.total,
        members: [],
      }));

      const membersResponse = await axiosInstance.get(`/projects/${projectId}/members`);
      const membersData: MemberData[] = membersResponse.data.data;

      membersData.forEach((memberData) => {
        const position = updatedPositions.find(pos => pos.posCode === memberData.jobCode.id);
        if (position) {
          position.members.push({
            nickname: memberData.member.nickname,
            id: memberData.member.id
          });
        }
      });

      setPositions(updatedPositions);
    } catch (error) {
      console.error("Loading project positions and members failed:", error);
    }
  };

  const getApplyList = async () => {
    try {
      const response = await axiosInstance.get(`/projects/${projectId}/application/${memberId}`);
      if (response.data.status === 'OK') {
        setApplyList(response.data.data);
      }
    } catch (error) {
      console.error("Loading applications failed:", error);
    }
  };

  const accept = async (acceptMemberId: number) => {
    try {
      const response = await axiosInstance.post(`/projects/${projectId}/acceptMember/${memberId}?acceptMemberId=${acceptMemberId}`);
      console.log(response);
      getData();
      getApplyList();
    } catch (error) {
      console.error("Accepting application failed:", error);
    }
  };

  const refuse = async (refuseMemberId: number) => {
    try {
      const response = await axiosInstance.delete(`/projects/${projectId}/refuseMember/${memberId}?refuseMemberId=${refuseMemberId}`);
      console.log(response);
      getApplyList();
    } catch (error) {
      console.error("Refusing application failed:", error);
    }
  };
  const toggleActionableModal = (memberId: number, posCode: number, x: number, y: number, nickname: string) => {
    setSelectedPosCode(null);
    if (actionableModal && actionableModal.memberId === memberId) {
      setActionableModal(null);
    } else {
      setActionableModal({
        memberId,
        posCode,
        visible: true,
        x,
        y,
        nickname
      });
    }
  };
  const handlePositionChange = (e: React.ChangeEvent<HTMLSelectElement>) => {
    setSelectedPosCode(parseInt(e.target.value));
    console.log(e.target.value);
  };
  const applyPositionChange = async () => {
    if (actionableModal && selectedPosCode !== null) {
      await movePositions(actionableModal.memberId, selectedPosCode);
      setActionableModal(null); // 포지션 변경 후 모달 닫기
    }
  };

  const exile = async (exiledId: number) => {
    try {
      const response = await axiosInstance.delete(`/projects/${projectId}/exiled/${memberId}?exileId=${exiledId}`);
      console.log(response);
      getApplyList();
    } catch (error) {
      console.error("Exiling application failed:", error);
    }
  }

  const movePositions = async (moveMember: number, moveJob: number) => {
    try {
      const response = await axiosInstance.patch(`/projects/${projectId}/${memberId}/members?moveMember=${moveMember}&moveJob=${moveJob}`);
      console.log(response);
      getData();
      getApplyList();
    } catch (error) {
      console.error("Moving application failed:", error);
    }
  }



  useEffect(() => {
    if (projectId && memberId) {
      getData();
      getApplyList();
    }
  }, [projectId, memberId]); // Add memberId as a dependency to useEffect

  return (
    <div className='bg-gray-300 w-auto h-auto p-16 m-5 border-black border-2'>
      <div className='font-bold text-lg'>
        신청 현황
      </div>
      {applyList.map((list, index) => (
        <div key={index} className="grid grid-cols-4 gap-4 items-center">
          <div>{list.jobCode.detailDescription}</div>
          <div>{list.member.nickname}</div>
          <div className="flex justify-start gap-2">
            <button className='px-3 bg-green-400 text-white' onClick={() => accept(list.member.id)}>수락</button>
            <button className='px-3 bg-red-400 text-white' onClick={() => refuse(list.member.id)}>거절</button>
          </div>
        </div>
      ))}


      <div className='font-bold text-lg'>
        멤버 설정
      </div>
      {positions.map((position, index) => (
        <div key={index}>
          <div className="font-bold my-2">{position.posName}</div>
          {/* 멤버들을 담을 flex 컨테이너 추가 */}
          <div className="flex flex-wrap -mx-1">
            {position.members.map((member, memberIndex) => (
              // my-2 대신 mx-1 사용하여 좌우 마진 적용, 상하 마진은 컨테이너에 적용
              <div key={memberIndex} className="relative mx-1">
                <span
                  className="bg-gray-200 p-2 m-1 rounded-full cursor-pointer"
                  onClick={(e) => toggleActionableModal(member.id, position.posCode, e.clientX + 20, e.clientY, member.nickname)}
                >
                  {member.nickname}
                </span>
              </div>
            ))}
          </div>
        </div>
      ))}

      {actionableModal && (
        <div
          className="fixed bg-white shadow-md p-2 rounded z-50"
          style={{
            left: `${actionableModal.x}px`,
            top: `${actionableModal.y}px`,
            transform: 'translate(0, 0)' // Adjust as necessary
          }}
        >
          <div className="font-bold">{actionableModal.nickname}</div>
          <div className="block mt-2 cursor-pointer text-red-600" onClick={() => exile(actionableModal.memberId)}>추방</div>
          <div className="inline-block mt-2 relative">
            <select
              className="appearance-none w-full bg-white border px-4 py-2 pr-8 rounded leading-tight"
              onChange={handlePositionChange}
              value={selectedPosCode || actionableModal.posCode} // 선택된 포지션 코드가 없으면 현재 모달의 포지션 코드를 기본값으로 사용
            >
              {positions.map((position) => (
                <option key={position.posCode} value={position.posCode}>
                  {position.posName}
                </option>
              ))}
            </select>
            <button onClick={applyPositionChange} className="mt-2">변경</button>
          </div>
        </div>
      )}
    </div>
  );
};

export default MemberSettingPage;
