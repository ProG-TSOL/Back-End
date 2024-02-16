# 노드 이미지를 최신 버전으로 가져옴
FROM node:latest

RUN mkdir /app

WORKDIR /app

COPY package.json ./
COPY yarn.lock ./

RUN yarn

# dependency 설치
COPY . .
RUN yarn install
RUN yarn add vite
RUN yarn add @types/node --dev
RUN yarn build
# 실제 환경 배포시 yarn build를 사용한다. 오늘 작성중인 Dockerfile은 개발용으로 작성되었다. (로컬 파일을 컨테이너화 한것과 동일함)

# 앱 실행
CMD ["yarn", "preview" ]