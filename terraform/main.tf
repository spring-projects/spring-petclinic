module "vpc" {
  source = ".//vpc_module"
}

module "ec2" {
  source           = ".//ec2_module"
  ami              = "ami-0ecb62995f68bb549"
  public_subnet_id = module.vpc.public_subnet_ids
  vpc_id           = module.vpc.vpc_id
}