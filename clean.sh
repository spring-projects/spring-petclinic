#!/bin/bash

kubectl delete pipeline spring-petclinic
kubectl delete imagestream spring-petclinic
kubectl delete secret spring-petclinic-generic-webhook-secret
kubectl delete secret spring-petclinic-github-webhook-secret
kubectl delete deploymentconfig spring-petclinic
kubectl delete buildconfig spring-petclinic
kubectl delete service spring-petclinic
kubectl delete route spring-petclinic


