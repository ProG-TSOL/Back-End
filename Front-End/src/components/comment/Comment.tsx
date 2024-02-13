import { useState } from 'react';
// import React, { useState } from 'react';

const Comment = () => {
  const [comment, setComment] = useState('');

  const onSubmit = () => {
    console.log(comment);
  };

  return (
    <div>
      <div className="my-3">
        <div className="flex">
          <textarea 
            id="comment"
            name="comment"
            value={comment}
            onChange={(e) => setComment(e.target.value)}  
            className="w-full h-20 mr-2 p-2"
            placeholder="댓글을 입력하세요"
          />
          <button className="bg-blue-500 text-white p-2 w-32 text-xl" onClick={onSubmit}>
            작성
          </button> 
        </div>
      </div>
    </div>
  );
};

export default Comment;