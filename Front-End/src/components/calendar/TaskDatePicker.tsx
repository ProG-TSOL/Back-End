import { useState } from "react";
import DatePicker from "react-datepicker";
import "react-datepicker/dist/react-datepicker.css";

const TaskDatePicker = () => {
  const [startDate, setStartDate] = useState(new Date());
  return (
    <div className="flex">
      <label
        htmlFor="datepicker"
        className="w-24 text-sm mt-1 font-medium text-gray-700"
      >
        업무 마감일
      </label>
      
        <DatePicker
          id="datepicker"
          selected={startDate}
          onChange={(date) => setStartDate(date)}
          className="mb-2 block w-full pl-3 pr-10 py-2 text-base border-gray-300 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm rounded-md"
        />
      </div>
  );
};

export default TaskDatePicker;
