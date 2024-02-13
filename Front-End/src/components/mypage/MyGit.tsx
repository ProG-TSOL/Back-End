const MyGit = () => {
  return (
    <div className="flex">
      {/* My Git 타이틀 부분 */}
      <div className="flex flex-col items-start mr-10">
        <div className="flex items-center mb-4">
          <p className="font-mainFont font-semibold mr-2">My Git</p>
        </div>
      </div>

      {/* 나의 잔디 부분 */}
      <div className="flex-1 flex flex-col items-start">
        <div className="flex items-center mb-4">
          <p className="font-mainFont ml-14 mr-2">나의 잔디</p>
        </div>
        {/* 후에 사용자 정보를 받아오기 또한 여기 상태관리 가능할 듯, 생각하기 */}
        <img
          className="ml-14"
          src="https://ghchart.rshah.org/4B33E3/Gardener-soul"
        />
      </div>
      <div className="flex-1 items-center mb-4">
        {/*컴포넌트 구성에 대해서 이야기 나눠보기 (아래에 둘 건지, 한 칸 아래?), 또한 리드미는 백엔드와의 작업 필요 */}
        <p className="font-mainFont ml-14 mr-2">나의 리드미</p>
      </div>
    </div>
  );
};

export default MyGit;
