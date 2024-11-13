FROM gradle:8.8-jdk21 as build
WORKDIR /app

# Установка dos2unix
RUN apt-get update && apt-get install -y dos2unix

# Копируем Gradle Wrapper файлы
COPY homework5/gradlew .
COPY homework5gradle/ gradle/
COPY homework5build.gradle.kts .

RUN chmod +x gradlew

# Преобразуем конец строки в файлах Gradle
RUN dos2unix gradlew homework5/gradle/wrapper/gradle-wrapper.properties build.gradle.kts

# Проверка наличия необходимых файлов
RUN ls -la
RUN ls -la homework5/gradle/wrapper

# Установка переменной окружения для TLS
ENV JAVA_OPTS="-Dhttps.protocols=TLSv1.2,TLSv1.3"

# Установка зависимостей
RUN ./gradlew dependencies --configuration runtimeClasspath

# Копирование исходного кода
COPY homework5/src homework5/src

# Сборка проекта
RUN ./gradlew clean build -x test --info --stacktrace

RUN echo "Содержимое /app после сборки:" && ls -R /app

# Проверка содержимого директории build/libs после сборки
RUN echo "Содержимое build/libs:"
RUN ls -la homework5/build/libs/

# Проверка текущей директории и содержимого
RUN echo "Текущая директория перед извлечением: $(pwd)"
RUN ls -la
RUN ls -la homework5/build/libs/
RUN mkdir -p homework5/build/dependency && (cd homework5/build/dependency; ls -la; jar -xf ../libs/app-0.0.1-SNAPSHOT.jar)

FROM openjdk:21 as runner

ARG DEPENDENCY=/app/homework5/build/dependency

COPY --from=build ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=build ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /app

ENTRYPOINT ["java", "-cp", "/app:/app/lib/*", "org.example.SpringAppApplication"]