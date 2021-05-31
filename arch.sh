#!/bin/bash
#You should set environment var ARCH_PATH
nme=beige-springboot$(date +'%d%m%g%H%M')
pth=$ARCH_PATH/$nme.tar.xz
tar --exclude=target/* --exclude=.mvn/* --exclude=.git/* -cJf $pth .
