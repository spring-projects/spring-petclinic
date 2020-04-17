import sys

# INSERT INTO vets VALUES (1, 'James', 'Carter');
# INSERT INTO vets VALUES (2, 'Helen', 'Leary');
# INSERT INTO vets VALUES (3, 'Linda', 'Douglas');
# INSERT INTO vets VALUES (4, 'Rafael', 'Ortega');
# INSERT INTO vets VALUES (5, 'Henry', 'Stevens');
# INSERT INTO vets VALUES (6, 'Sharon', 'Jenkins');

output_filename = sys.argv[1]

counter = 0
idx = 1
with open(output_filename, "w") as output:
    # vets
    for counter in range(3000):
        output.write("INSERT INTO vets VALUES ({:d}, 'James', 'Carter{:04d}');\n".format(idx, counter))
        idx += 1
        output.write("INSERT INTO vets VALUES ({:d}, 'Helen', 'Leary{:04d}');\n".format(idx, counter))
        idx += 1
        output.write("INSERT INTO vets VALUES ({:d}, 'Linda', 'Douglas{:04d}');\n".format(idx, counter))
        idx += 1
        output.write("INSERT INTO vets VALUES ({:d}, 'Rafael', 'Ortega{:04d}');\n".format(idx, counter))
        idx += 1
        output.write("INSERT INTO vets VALUES ({:d}, 'Henry', 'Stevens{:04d}');\n".format(idx, counter))
        idx += 1
        output.write("INSERT INTO vets VALUES ({:d}, 'Sharon', 'Jenkins{:04d}');\n".format(idx, counter))
        idx += 1


