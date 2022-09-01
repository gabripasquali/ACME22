DOCKER_BUILDKIT=1
docker build . -t gis

docker run -p 10000:8080 --name gis gis