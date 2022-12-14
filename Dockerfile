FROM openjdk:16.0.2
ARG PRIVATE_KEY=qwertyazerty1234
ARG TOKEN_LIFE=86400
ARG PG_DB_USER=postgres
ARG PG_DB_URL=db.wnppqypousbcqipwutah.supabase.co/postgres
ARG PG_DB_PASSWORD=Mabri2019#007
ENV PG_DB_USER=${PG_DB_USER}
ENV PG_DB_PASSWORD=${PG_DB_PASSWORD}
ENV TOKEN_LIFE=${TOKEN_LIFE}
ENV PRIVATE_KEY=${PRIVATE_KEY}
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","-Dspring.profiles.active=staging","/app.jar"]