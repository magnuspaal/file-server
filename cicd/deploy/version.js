fs = require('fs');

const compose = './cicd/deploy/docker-compose.yml';
const pom = './pom.xml'
const newVersion = process.argv[2]

bumpVersion(compose, /^(\s+image.*file-server:)(.+)$/m, newVersion);
bumpVersion(pom, /^(\s+<artifactId>file-server<\/artifactId>\n\s+<version>)(.+?(?=<\/version>))/m, newVersion);

function bumpVersion(file, regexp, version) {
  fs.readFile(file, "utf-8", (err, data) => {
    if (err) { console.log(err) }
     const text = data.replace(regexp, "$1" + version);
     fs.writeFileSync(file, text);
     console.log(`Bumping version in ${file} to ${version}`)
  });
}