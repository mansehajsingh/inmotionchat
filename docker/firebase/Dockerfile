FROM node:20-alpine3.17

RUN apk update
RUN npm config set registry https://registry.npmjs.org/
RUN npm install -g firebase-tools # install firebase tools

RUN mkdir -p /var/firebase
RUN mkdir -p /tmp/firebase
WORKDIR /var/firebase

RUN touch firebase.json

EXPOSE 9099
EXPOSE 4000

COPY entrypoint.sh /var/firebase/

CMD ["sh", "/var/firebase/entrypoint.sh"]