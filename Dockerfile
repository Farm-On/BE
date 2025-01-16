# 기본 이미지 설정
FROM openjdk:17-jdk-slim

### JAR_FILE 경로에 해당하는 파일을 Docker 이미지 내부로 복사
COPY build/libs/farmon-1.0.0.jar farmon-backendr-dev.jar

EXPOSE 8080

# 컨테이너 실행 명령 설정
ENTRYPOINT ["java", "-jar", "/farmon-backendr-dev.jar"]