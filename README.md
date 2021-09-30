# CanaWebSB

kubectl.exe create secret tls localhost-cert --key localhost.key --cert localhost.crt
kubectl apply -f ingress-nginx.yaml
kubectl apply -f kubernetes.yaml


#
kubectl.exe -n ingress-nginx get svc