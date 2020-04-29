# exit when any command fails
set -e

export SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

cd "$SCRIPT_DIR"

rm -rf out

openapi-generator batch "$SCRIPT_DIR"/generators/spring-boot.yaml
cd out/spring-boot
mvn com.coveo:fmt-maven-plugin:format source:jar-no-fork install

cd "$SCRIPT_DIR"

openapi-generator batch "$SCRIPT_DIR"/generators/http-client.yaml
cd out/http-client
mvn com.coveo:fmt-maven-plugin:format source:jar-no-fork install

cd "$SCRIPT_DIR"

rm -rf out