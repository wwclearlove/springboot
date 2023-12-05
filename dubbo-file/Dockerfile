FROM openjdk:8-jdk-oracle
ADD ./target/kg-file.jar //

ENV TZ=Asia/Shanghai
ENV FORMAT_MESSAGES_PATTERN_DISABLE_LOOKUPS=true

ENTRYPOINT ["java","-jar","-Dlog4j2.formatMsgNoLookups=true", "kg-file.jar"]