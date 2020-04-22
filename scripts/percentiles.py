import sys

output_filename = sys.argv[1]
input_files = sys.argv[2:]


def get_percentile(percentile, values):
    count = len(values)
    return values[int(count * percentile)]


latencies = []
percentiles = []
with open(output_filename, "w") as output_file:
    headers = [input_file.replace('results_', '').replace('.csv', '') for input_file in input_files]
    output_file.write(','.join(headers) + '\n')
    for input_filename in input_files:
        with open(input_filename) as input_file:
            for line in input_file:
                cols = line.split(',')
                if cols[0] == "starttransfer": # header
                    continue
                latencies.append(cols[0])
        latencies.sort()
        percentile_dist = [get_percentile(0.1, latencies),
                           get_percentile(0.2, latencies),
                           get_percentile(0.3, latencies),
                           get_percentile(0.4, latencies),
                           get_percentile(0.5, latencies),
                           get_percentile(0.6, latencies),
                           get_percentile(0.7, latencies),
                           get_percentile(0.8, latencies),
                           get_percentile(0.9, latencies),
                           get_percentile(0.95, latencies),
                           get_percentile(0.99, latencies)]
        percentiles.append(percentile_dist)
    for percentile_no in range(11):  # number of percentiles
        line_values = []
        for input_no in range(len(percentiles)):
             line_values.append(percentiles[input_no][percentile_no])
        line = ','.join(line_values) + '\n'
        output_file.write(line)
