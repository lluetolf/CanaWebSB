# Deploy CanaWebSB
kubectl.exe delete secret localhost-cert                                              <br />
kubectl.exe create secret tls localhost-cert --key localhost.key --cert localhost.crt <br />
kubectl apply -f ingress-nginx.yaml                                                   <br />
kubectl apply -f kubernetes.yaml                                                      <br />

# Check
kubectl.exe -n ingress-nginx get svc                <br />
https: https://localhost:32222/ui/                  <br />
http: http://localhost:31111/ui                     <br />