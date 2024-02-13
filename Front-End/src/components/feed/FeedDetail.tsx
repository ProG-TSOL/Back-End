import { FC, useRef, useEffect, useCallback } from "react";
import { FaMapPin, FaEllipsisVertical } from "react-icons/fa6";

type FeedDetailProps = {
  onClose: () => void;
};

const FeedDetail: FC<FeedDetailProps> = ({ onClose }) => {
  const modalRef = useRef<HTMLDivElement>(null);

  // useCallBack, React의 훅 중 하나, 함수의 메모이제이션에 사용 / 반복된 계산, 메모리에 저장.
  const handleClickOutside = useCallback(
    (event: MouseEvent) => {
      if (
        modalRef.current &&
        event.target instanceof Node && // 그래서 event.target을 instanceof 을 사용하여 Node인지 확인
        !modalRef.current.contains(event.target) // event.target은 EventTarget 타입이지만, contains는 Node 타입을 필요로 함.
      ) {
        onClose();
      }
    },
    [onClose]
  );

  // 컴포넌트가 마운트될 때 이벤트 리스너 추가
  useEffect(() => {
    document.addEventListener("mousedown", handleClickOutside);
    return () => {
      // 컴포넌트가 언마운트될 때 이벤트 리스너 제거
      document.removeEventListener("mousedown", handleClickOutside);
    };
  }, [handleClickOutside]);

  return (
    <div className="fixed inset-0 bg-gray-200 bg-opacity-50 flex justify-center items-center z-10">
      <div
        ref={modalRef}
        id="indi"
        className="bg-white mt-2 gap-4 border-2 border-main-color rounded-lg p-4 shadow-lg"
      >
        {/* flex를 사용하여 주요 컨텐츠를 양쪽으로 정렬 */}
        <div className="flex justify-between items-center">
          <div className="flex gap-4">
            <p>프사</p>
            <p>닉네임</p>
            <p>날짜</p>
          </div>
          <div className="flex gap-2">
            <FaMapPin className="text-xl" />
            <FaEllipsisVertical className="text-xl" />
          </div>
        </div>
        <p className="mt-2">여기에 제목이 들어갑니다. map.filter로</p>
        <hr />
        <div>
          <p>여기에 사진이 들어갑니다.</p>
        </div>
        <div>
          <p>여기에 내용이 들어갑니다.</p>
        </div>
        <hr />
        <div>
          <p>여기에 댓글이 들어갑니다.</p>
        </div>
        <div className="flex justify-end">
          <button
            onClick={onClose}
            className="bg-red-500 text-white p-2 rounded hover:bg-red-700 transition duration-300 mt-4"
          >
            닫기
          </button>
        </div>
      </div>
    </div>
  );
};

export default FeedDetail;
