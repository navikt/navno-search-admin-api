<<<<<<< Updated upstream
FROM europe-north1-docker.pkg.dev/cgr-nav/pull-through/nav.no/jre:openjdk-21
=======
FROM europe-north1-docker.pkg.dev/cgr-nav/pull-through/nav.no/node:24-slim

WORKDIR /app

>>>>>>> Stashed changes
COPY app/build/libs/app.jar /app.jar
COPY node_modules /app/node_modules/
COPY .env /app/.env

ENV TZ="Europe/Oslo"

EXPOSE 8080
<<<<<<< Updated upstream
CMD ["-jar", "/app.jar"]
=======
ENTRYPOINT ["node"]
CMD ["app.jar"]
>>>>>>> Stashed changes
