# exit when any command fails
set -e

export SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

openapi-generator batch "$SCRIPT_DIR"/generators/spring-boot.yaml
cd out/spring-boot
mvn com.coveo:fmt-maven-plugin:format source:jar-no-fork javadoc:jar install