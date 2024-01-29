import Chat from "../../chat/Chat"
import Site from "../../site/Site"

const RightBar = () => {
  return (
    <div className="flex flex-col w-52 justify-center items-center bg-sub-color"
    style={{ height: 'calc(100vh - 64px)' }}>
      {/* 팀 공통 사이트 컴포넌트 */}
      <div className="flex">
        <Site />
      </div>
      {/* 채팅 컴포넌트 */}
      <div className="flex">
        <Chat />
      </div>
    </div> 
  )
} 

export default RightBar 