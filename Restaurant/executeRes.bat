DOCKER_BUILDKIT=1
docker build . -t restaurant

docker run -td -p 10007:5000 --name Vegetale restaurant
docker run -td -p 10008:5000 --name DaCarlo restaurant
docker run -td -p 10009:5000 --name Paradiso restaurant
docker run -td -p 10010:5000 --name Sushino restaurant
docker run -td -p 10011:5000 --name Tramonto restaurant
docker run -td -p 10012:5000 --name YinDyan restaurant