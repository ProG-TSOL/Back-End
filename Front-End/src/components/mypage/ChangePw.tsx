import React, { useState } from "react";
import ChangePwInfo from "../alarm/ChangePwInfo";
import { axiosInstance } from "../../apis/lib/axios";

interface ChangePwModalProps {
  onClose: () => void;
}

interface ChangePasswordProps {
  message: string;
}

const ChangePasswordModal: React.FC<ChangePwModalProps> = ({ onClose }) => {
  const [currentPassword, setCurrentPassword] = useState("");
  const [newPassword, setNewPassword] = useState("");
  const [confirmNewPassword, setConfirmNewPassword] = useState("");

  const handleOutsideClick = (e: React.MouseEvent<HTMLDivElement>) => {
    if (e.target instanceof HTMLDivElement && e.target.id === "modalBackdrop") {
      onClose();
    }
  };

  const validatePassword = (password: string): boolean => {
    const re = /^(?=.*[!@#$%^&*])[A-Za-z\d!@#$%^&*]{8,16}$/;
    return re.test(password);
  };

  const isNewPasswordValid = validatePassword(newPassword);
  const doNewPasswordsMatch = newPassword === confirmNewPassword;

  const changePw = async () => {
    const pwData = {
      originPassword: currentPassword,
      updatePassword: newPassword,
      updatePasswordCheck: confirmNewPassword,
    };
    try {
      await axiosInstance.patch<ChangePasswordProps>(
        "/members/change-password",
        pwData
      );

      onClose();
    } catch (error) {
      console.log(error);
    }
  };

  return (
    <div
      id="modalBackdrop"
      className="fixed inset-0 bg-gray-600 bg-opacity-50 flex justify-center items-center"
      onClick={handleOutsideClick}
    >
      <div
        className="bg-white p-6 rounded-lg shadow-xl w-1/3"
        onClick={(e) => e.stopPropagation()}
      >
        <div className="flex">
          <h2 className="text-xl font-bold mb-4">비밀번호 변경</h2>
          <ChangePwInfo />
        </div>
        <div className="mb-4">
          <label
            htmlFor="currentPassword"
            className="block mb-2 text-sm font-bold text-gray-700"
          >
            현재 비밀번호
          </label>
          <input
            type="password"
            id="currentPassword"
            className="w-full p-2 border border-gray-300 rounded"
            value={currentPassword}
            onChange={(e) => setCurrentPassword(e.target.value)}
          />
        </div>

        <div className="mb-4">
          <label
            htmlFor="newPassword"
            className="block mb-2 text-sm font-bold text-gray-700"
          >
            변경할 비밀번호
          </label>
          <input
            type="password"
            id="newPassword"
            className={`w-full p-2 border ${
              isNewPasswordValid ? "border-green-500" : "border-red-500"
            } rounded`}
            value={newPassword}
            onChange={(e) => setNewPassword(e.target.value)}
          />
        </div>

        <div className="mb-4">
          <label
            htmlFor="confirmNewPassword"
            className="block mb-2 text-sm font-bold text-gray-700"
          >
            변경 비밀번호 확인
          </label>
          <input
            type="password"
            id="confirmNewPassword"
            className={`w-full p-2 border ${
              doNewPasswordsMatch && isNewPasswordValid
                ? "border-green-500"
                : "border-red-500"
            } rounded`}
            value={confirmNewPassword}
            onChange={(e) => setConfirmNewPassword(e.target.value)}
          />
        </div>

        <div className="flex justify-end">
          <button
            className="bg-red-500 hover:bg-red-700 text-white font-bold py-2 px-4 rounded mr-2"
            onClick={onClose}
          >
            닫기
          </button>
          <button 
          onClick={changePw}
          className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded">
            변경하기
          </button>
        </div>
      </div>
    </div>
  );
};

export default ChangePasswordModal;
