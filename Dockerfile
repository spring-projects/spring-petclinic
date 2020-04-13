from openjdk
COPY /target/spring*.jar pet-animal/
EXPOSE 8080
CMD java -jar -Dspring.profiles.active=mysql pet-animal/*.jar
