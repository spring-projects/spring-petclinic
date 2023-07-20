shell_files=("hibernateLoad.sh" "sqlLoad.sh" "dbcpLoad.sh" "jdbcLoad.sh")

for file in "${shell_files[@]}"; do
    echo "Running $file"
    bash "$file" &
    
    # Store the process ID of the background job
    pid=$!
    
    # Wait for the process to finish
    wait "$pid"
    
    echo "$file completed"
done

echo "All shell files completed"
