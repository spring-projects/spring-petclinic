import boto3
from tqdm import tqdm


def main() -> None:
    db = boto3.resource("dynamodb")

    num_to_delete = 0
    all_tables = db.tables.all()
    for table in all_tables:
        if "integration_test" in table.name:
            num_to_delete += 1
    with tqdm(total=num_to_delete) as progress:
        for table in all_tables:
            if "integration_test" in table.name:
                table.delete()
                progress.update()
    print(f"Deleted {num_to_delete} CI DynamoDB tables")


if __name__ == "__main__":
    main()
