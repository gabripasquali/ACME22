DOCKER_BUILDKIT=1
docker build . -t gis

docker run -p 10000:3001 --name gis gis