#!/bin/bash

for i in {1..11}
do
  java -cp "./out/" "main.Main" < "./src/test/in/t$i.in" > "./out/test/t$i.result"
  diff "./src/test/out/t$i.out" "./out/test/t$i.result"
done