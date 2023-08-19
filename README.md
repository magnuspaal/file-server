### Environment variables

```
PORT=8082
API_KEY=
MEDIA_MAPPING=
ALLOWED_ORIGINS=http://localhost:3000
JWT_SECRET=
```

#### Release
* Bump version
    * Change version in **pom.xml**
    * Create commit with message `docs: bump to <version>`
    * Create git tag with version
* Run Maven `package` lifecycle action.
* Build Docker image `docker build -t <registry/file-server:version> --build-arg="APP_VERSION=<version>" .`
* Push Docker image `docker push <registry/file-server:version>`


