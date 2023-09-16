TEST_FOLDER="/test"
OUT_FOLDER="./out"
fileName=


compileFlag=
mainFlag=
testFlag=
runFlag=
bflag=
while getopts c:mtr: name
do
    case $name in
    c)    compileFlag=1
          fileName="$OPTARG";;
    m)    mainFlag=1;;
    t)    testFlag=1;;
    r)    runFlag=1
          fileName="$OPTARG";;
    esac
done
if [ ! -z "$compileFlag" ]; then
    printf "Compiling $fileName.java file ...\n"
fi
if [ ! -z "$runFlag" ]; then
    printf "Running $fileName.class file ...\n"
fi
if [ ! -z "$testFlag" ]; then
    printf "Option -testFlag specified: $runFlag\n"
fi

echo "---------------------"
echo "---------------------"

if [ "$compileFlag" ] && [ "$mainFlag" ]; then
    javac -d "$OUT_FOLDER/" "./src/main/$fileName.java"
fi

if [ "$compileFlag" ] && [ "$testFlag" ]; then
    javac -d "$OUT_FOLDER/" "./src/test/$fileName.java"
fi

if [ "$runFlag" ] && [ "$mainFlag" ]; then
    java -cp "$OUT_FOLDER/" "main.$fileName"
fi

if [ "$runFlag" ] && [ "$testFlag" ]; then
    java -cp "$OUT_FOLDER/" "test.$fileName"
fi