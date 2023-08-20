### Environment variables

```
PORT=8082
API_KEY=
MEDIA_MAPPING=
ALLOWED_ORIGINS=http://localhost:3000
JWT_SECRET=
```

#### Release
* Run `./cicd/deploy/bump <version>`
* Push new commit and tag. GitHub Actions will deploy the container.


