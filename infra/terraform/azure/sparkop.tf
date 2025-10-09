resource "kubernetes_role" "sparkop-user" {
  metadata {
    name = "use-spark-operator"
    namespace = var.aks_namespace
  }
  rule {
    api_groups = ["sparkoperator.k8s.io"]
    resources = ["sparkapplications"]
    verbs = ["create", "delete", "deletecollection", "get", "list", "update", "watch", "patch"]
  }
}

resource "kubernetes_role_binding" "sparkop-user" {
  metadata {
    name = "use-spark-operator"
    namespace = var.aks_namespace
  }
  role_ref {
    api_group = "rbac.authorization.k8s.io"
    kind = "Role"
    name = kubernetes_role.sparkop-user.metadata[0].name
  }
  subject {
    kind = "ServiceAccount"
    name = "default"
  }
}
