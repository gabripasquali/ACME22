DOCKER_BUILDKIT=1
docker build . -t rider

docker run -td -p 10002:3000 --name Redir rider
docker run -td -p 10003:3000 --name Riderin rider
docker run -td -p 10004:3000 --name Astro rider
docker run -td -p 10005:3000 --name B4L rider