#!/bin/sh
defaults write -g ApplePressAndHoldEnabled -bool false
cd "`dirname "$0"`"
java Pong
