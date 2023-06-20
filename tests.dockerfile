FROM maven:latest
WORKDIR /app
COPY . /app
RUN mvn clean package -DskipTests
EXPOSE 8081
CMD ["mvn", "test"]
