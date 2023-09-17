#!/bin/bash

for i in {1..11}
do
  echo "Check test $i:"
  java -cp "./out/" "main.Main" < "./src/test/in/t$i.in" > "./out/test/t$i.result"
  diff "./src/test/out/t$i.out" "./out/test/t$i.result" | tr -d "\\" | tr -d " No newline at end of file"
  echo "---------------------------------"
done