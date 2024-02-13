import React, { useEffect, useState } from 'react';

interface TimerProps {
  initialSeconds: number;
  isActive: boolean;
  onComplete: () => void;
}

const Timer: React.FC<TimerProps> = ({ initialSeconds, isActive, onComplete }) => {
  const [seconds, setSeconds] = useState<number>(initialSeconds);

  useEffect(() => {
    let interval: number | null = null;

    if (isActive && seconds > 0) {
      interval = setInterval(() => {
        setSeconds((prevSeconds) => prevSeconds - 1);
      }, 1000) as unknown as number;
    } else if (seconds === 0) {
      onComplete();
    }

    return () => {
      if (interval) clearInterval(interval);
    };
  }, [isActive, seconds, onComplete]);

  useEffect(() => {
    if (isActive) {
      setSeconds(initialSeconds);
    }
  }, [isActive, initialSeconds]);

  const formatTime = () => {
    const minutes = Math.floor(seconds / 60);
    const secondsLeft = seconds % 60;
    return `${minutes}:${secondsLeft < 10 ? '0' : ''}${secondsLeft}`;
  };

  return <span className="text-gray-500 font-bold ml-80">{formatTime()} 남음</span>;
};

export default Timer;
