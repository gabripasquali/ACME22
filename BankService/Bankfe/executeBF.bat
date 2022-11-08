DOCKER_BUILDKIT=1
docker build . -t bank-frontend

docker run -td -p 10015:5000 --name BANK bank-frontend