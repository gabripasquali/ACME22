DOCKER_BUILDKIT=1
docker build . -t bank

docker run -p 10001:8000 --name bank bank
