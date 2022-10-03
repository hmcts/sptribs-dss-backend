provider "azurerm" {
  features {}
}

locals {
  vaultName = "${var.product}-${var.env}"
}

data "azurerm_key_vault" "sptribs_key_vault" {
  name = "sptribs-aat"
  resource_group_name = "${var.raw_product}-${var.env}"
}

data "azurerm_key_vault" "s2s_vault" {
  name                = "s2s-${var.env}"
  resource_group_name = "rpe-service-auth-provider-${var.env}"
}

data "azurerm_key_vault_secret" "microservicekey_fis_cos_api" {
  name         = "microservicekey-fis-cos-api"
  key_vault_id = data.azurerm_key_vault.s2s_vault.id
}
