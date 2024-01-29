/** @type {import('tailwindcss').Config} */
export default {
	content: ['./index.html', './src/**/*.{js,ts,jsx,tsx}'],
	theme: {
		extend: {
			colors: {
				'main-color': '#4B33E3',
				'sub-color': '#EBE9FC',
			},
			fontFamily: {
				sans: ['Noto Sans KR', 'sans-serif'],
			},
		},
	},
	plugins: [],
};
