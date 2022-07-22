DOCKER_BUILDKIT=1
docker build . -t restaurant

docker run -p 10000:5000 --name restaurant restaurant