DOCKER_BUILDKIT=1
docker build . -t bank

docker run -p 10000:8000 --name bank bank