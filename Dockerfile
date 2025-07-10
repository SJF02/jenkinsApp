# JDK 21 기반의 경량 Alpine 이미지 사용
FROM eclipse-temurin:21-jre-alpine

# 비루트 사용자 생성 (보안 강화)
RUN addgroup -S spring && adduser -S spring -G spring

# JAR 파일 복사
COPY build/libs/*.jar /opt/app.jar

# 작업 디렉토리 설정
WORKDIR /opt

# 비루트 사용자로 실행
USER spring:spring

# 애플리케이션 실행
ENTRYPOINT ["java", "-jar", "app.jar"]

# 외부에 노출할 포트
EXPOSE 9999