FROM jolielang/jolie

EXPOSE 8000
COPY jdbc-postgresql.jar /usr/lib/jolie/lib/ 

COPY src/ src/
WORKDIR src
CMD jolie server.ol