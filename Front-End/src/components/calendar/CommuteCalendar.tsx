import {
  Calendar as BigCalendar,
  CalendarProps,
  momentLocalizer,
} from "react-big-calendar";

import moment from "moment";

const localizer = momentLocalizer(moment);

const CommuteCalendar = (props: Omit<CalendarProps, "localizer">) => {
  return <BigCalendar {...props} localizer={localizer} />;
};

export default CommuteCalendar;
