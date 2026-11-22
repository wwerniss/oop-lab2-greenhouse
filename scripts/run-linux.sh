set -euo pipefail
if [[ ! -f pom.xml ]]; then echo 'Run from project root.' >&2; exit 2; fi
mkdir -p target/classes
find src/main/java -name '*.java' -print0 | xargs -0 javac --release 17 -encoding UTF-8 -d target/classes
cp src/main/resources/logging.properties target/classes/
java -cp target/classes ua.edu.oop.greenhouse.Main
