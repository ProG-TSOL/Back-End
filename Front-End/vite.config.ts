import { defineConfig } from 'vite';
import react from '@vitejs/plugin-react';
import path from 'path';

export default defineConfig({
	base: './',
	server: {
		port: 3000,
		host: '0.0.0.0',
		proxy: {
			'/members/reissue-token': {
				target: 'http://i10a210.p.ssafy.io:8080/',
				changeOrigin: true,
			},
			'/members/logout': {
				target: 'http://i10a210.p.ssafy.io:8080/',
				changeOrigin: true,
			},
			'/members/login': {
				target: 'http://i10a210.p.ssafy.io:8080/',
				changeOrigin: true,
			},
			'/members/login/oauth2/github': {
				target: 'http://i10a210.p.ssafy.io:8080/',
				changeOrigin: true,
			},
		},
	},
	plugins: [react()],
	resolve: {
		alias: [{ find: '@', replacement: path.resolve(__dirname, 'src') }],
	},
	assetsInclude: ['**/*.jpg'],
});
