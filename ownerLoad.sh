THREAD_COUNT=100

callOwners() {
 responseOwners=$(curl "http://localhost:9753/owners")
}

for ((i=0; i<$THREAD_COUNT; i++)); do
   callOwners &
done

wait
