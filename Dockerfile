# Usa l'immagine di base di OpenJDK
FROM openjdk:17-jdk-slim

# Imposta la directory di lavoro
WORKDIR /app

# Copia il file JAR del microservizio nel container
COPY target/microservice-demo-1.0-SNAPSHOT.jar /app/microservice-demo.jar

# Esponi la porta su cui il microservizio ascolta
EXPOSE 8080

# Installazione di Docker e altri strumenti necessari (sudo, wget, ecc.)
USER root
RUN apt-get update && \
    apt-get install -y sudo git wget ca-certificates curl && \
    wget http://get.docker.com/builds/Linux/x86_64/docker-latest.tgz && \
    tar -xvzf docker-latest.tgz && \
    mv docker/* /usr/bin/ && \
    rm -rf docker-latest.tgz

# Crea l'utente 'jenkins' e aggiungilo al gruppo sudo
RUN useradd -m jenkins && \
    echo "jenkins ALL=NOPASSWD: ALL" >> /etc/sudoers

# Crea una directory per il Docker socket e configura i permessi
RUN mkdir -p /var/run/docker.sock && \
    chown -R jenkins:jenkins /var/run/docker.sock

# Riporta l'utente su Jenkins per evitare problemi di permessi
USER jenkins

# Comando per avviare il microservizio
ENTRYPOINT ["java", "-jar", "microservice-demo.jar"]
