FROM gcr.io/distroless/java21-debian12
COPY app/build/libs/app.jar /app.jar
ENV TZ="Europe/Oslo"
EXPOSE 8080
CMD ["app.jar"]