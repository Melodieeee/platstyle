# 使用 Maven 3.9.6 + Temurin JDK 17 建置階段
FROM maven:3.9.6-eclipse-temurin-17 AS build

WORKDIR /app
COPY . .

# 建置 JAR 檔，跳過測試
RUN mvn clean package -DskipTests

# 執行階段使用精簡的 JDK 17 映像
FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

# 開放 port 8080
EXPOSE 8080

# 執行 Spring Boot 應用程式
ENTRYPOINT ["java", "-jar", "app.jar"]
