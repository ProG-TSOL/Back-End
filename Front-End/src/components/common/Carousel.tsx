import React, { useState } from 'react';

const CustomCarousel = () => {
  const [activeIndex, setActiveIndex] = useState(0);
  const images = [
    "https://flowbite.com/docs/images/carousel/carousel-1.svg",
    "https://flowbite.com/docs/images/carousel/carousel-2.svg",
    "https://flowbite.com/docs/images/carousel/carousel-3.svg",
    "https://flowbite.com/docs/images/carousel/carousel-4.svg",
    "https://flowbite.com/docs/images/carousel/carousel-5.svg",
  ];

  const goToPrevSlide = () => {
    setActiveIndex(activeIndex === 0 ? images.length - 1 : activeIndex - 1);
  };

  const goToNextSlide = () => {
    setActiveIndex(activeIndex === images.length - 1 ? 0 : activeIndex + 1);
  };

  return (
    <div className="relative flex items-center justify-center w-96 overflow-hidden">
      <button 
        onClick={goToPrevSlide} 
        className="absolute left-0 z-10 ml-4 p-1.5 text-white bg-black bg-opacity-50 rounded-full hover:bg-opacity-80 focus:outline-none"
      >
        &#10094;
      </button>
      <div className="flex justify-center items-center w-full">
        <img src={images[activeIndex]} alt="carousel" className="w-full h-auto max-h-96 object-cover transition-all duration-500 ease-in-out"/>
      <button 
        onClick={goToNextSlide} 
        className="absolute right-0 z-10 mr-4 p-1.5 text-white bg-black bg-opacity-50 rounded-full hover:bg-opacity-80 focus:outline-none"
      >
        &#10095;
      </button>
      </div>
    </div>
  );
};

export default CustomCarousel;
