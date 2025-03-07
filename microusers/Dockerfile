# Usamos una imagen base de Java
FROM openjdk:17.0.2 AS builder

WORKDIR /app
COPY ./.mvn ./.mvn
COPY ./mvnw ./mvnw
COPY ./pom.xml ./pom.xml

#DESCARGAR DEPENDENCIAS EN CACHE
RUN ./mvnw dependency:go-offline

#COPIAR EL CODIO FUENTE Y COMPILAR
COPY ./src ./src

RUN ./mvnw clean package -DskipTests

#ETAPA 2 IMAGEN DE PRODUCCION
FROM openjdk:17.0.2
WORKDIR /app
#COPIAR EL AR DESDE LA ETAPA DE COMPITLACION
COPY --from=builder /app/target/inventario-0.0.1-SNAPSHOT.jar .
# Exponemos el puerto donde corre la app
EXPOSE 8001

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "./inventario-0.0.1-SNAPSHOT.jar"]
