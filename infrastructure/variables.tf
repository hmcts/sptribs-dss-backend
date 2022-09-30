variable "product" {}

variable "component" {}

variable "raw_product" {
  default = "sptribs"
}

variable "location" {
  default = "UK South"
}

variable "env" {}

variable "subscription" {}

variable "deployment_namespace" {}

variable "common_tags" {
  type = "map"
}
