set -ex

export GIT_HASH=latest

helm upgrade --install urlshortener ./helm/url-shortener --set image.tag=latest -n letterboxd
