FROM node:ubuntu
WORKDIR /usr/src/app
COPY package*.jar ./
RUN npm install
COPY . .
EXPOSE 8080
CMD [ "npm", "start" ]
