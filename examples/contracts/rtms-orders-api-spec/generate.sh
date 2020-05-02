set -e

export SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

rm -rf "$SCRIPT_DIR"/out

openapi-generator batch "$SCRIPT_DIR"/generators/spring-boot.yaml
cd out/spring-boot
mvn com.coveo:fmt-maven-plugin:format source:jar-no-fork install

cd "$SCRIPT_DIR"

openapi-generator batch "$SCRIPT_DIR"/generators/http-client.yaml
cd out/http-client
mvn com.coveo:fmt-maven-plugin:format source:jar-no-fork install

rm -rf "$SCRIPT_DIR"/out