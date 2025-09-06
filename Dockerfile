# 1단계: 빌더 이미지 - 애플리케이션 빌드
FROM openjdk:17-jdk-slim AS builder
WORKDIR /app
COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .
COPY src src

# Gradle Wrapper 실행 권한 부여
RUN chmod +x ./gradlew

# 애플리케이션 빌드
RUN ./gradlew bootJar

# 2단계: 최종 이미지 - 빌드된 JAR 파일 복사 및 실행
FROM openjdk:17-jdk-slim
WORKDIR /app

# 1단계에서 빌드된 JAR 파일을 복사합니다.
COPY --from=builder /app/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]