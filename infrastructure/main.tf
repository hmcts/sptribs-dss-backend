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

resource "azurerm_key_vault_secret" "s2s-secret-sptribs-dss-backend" {
  name         = "s2s-secret-sptribs-dss-backend"
  value        = data.azurerm_key_vault_secret.microservicekey_fis_cos_api.value
  key_vault_id = data.azurerm_key_vault.sptribs_key_vault.id
}
