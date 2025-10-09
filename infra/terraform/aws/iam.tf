data "aws_iam_policy_document" "worker_policy_document" {
  statement {
    sid = "1"

    actions = [
      "s3:*",
      "elasticmapreduce:*",
      "glue:*",
      "cloudwatch:*",
      "ecr:*",
      "iam:PassRole",
    ]

    resources = [
      "*",
    ]
  }

}

resource "aws_iam_policy" "worker_policy" {
  name        = "${var.name_prefix}_feast_worker_policy"
  path        = "/"
  description = "Worker IAM policy"

  policy = data.aws_iam_policy_document.worker_policy_document.json
}