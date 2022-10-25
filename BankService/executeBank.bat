DOCKER_BUILDKIT=1
docker build . -t bank

docker run -p 10013:4000 --name bank bank