DOCKER_BUILDKIT=1
docker build . -t bank

docker run -p 10000:3002 --name bank bank