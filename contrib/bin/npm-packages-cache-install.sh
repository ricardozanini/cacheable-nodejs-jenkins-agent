#!/bin/bash

echo "Installing cache dependencies from $HOME/package.json"
echo "NPM version: $(npm -v)"

cd $HOME
npm install $HOME