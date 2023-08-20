#!/bin/bash

node cicd/deploy/version.js $1
git add .
git commit -m "docs: bump to $1"
git tag -a $1 -m ""