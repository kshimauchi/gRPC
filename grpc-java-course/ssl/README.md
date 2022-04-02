#!/bin/bash

# SSL certificates information

##(Linux setup)
### Dependent on environments setup
### Inspired from https://github.com/grpc/grpc-java/tree/master/examples#generating-self-signed-certficates-for-use-with-grpc-java

### Output files
#### ca.key: Certficate Authority private key file (this shouldn't be shared in real-life)
#### ca.crt: Certificate Authority trust certificate (this should be shared with users in real-life)
#### server.key: Server private key, password protected (this shouldn't be shared)
#### server.csr: Server certificate signing request (this should be shared with the CA owner)
#### server.crt: Server certificate signed by the CA (this would be sent back by the CA owner) - keep on server
#### server.pem: Conversion of server.key into a format gRPC likes (this shouldn't be shared)

#Summary
#### Private files: ca.key, server.key, server.pem, server.crt
#### "Share" files: ca.crt (needed by the client), server.csr (needed by the CA)

### SERVER_CN=localhost

##### Setup for linux mac pass:1111 can be anything you like
openssl genrsa -passout pass:1111 -de3 -out ca.key 4096
openssl req -passin pass:1111 -new -x509 -days 365 -key ca.key -out ca.crt -subj "/CN=${SERVER_ON}"

#### Step 2: generate the Server Private key (server.key)
openssl genrsa -passout pass:1111 -des3 -out server.key 4096

#### Step 3: Get a signed certificate from the CA (server.csr)
openssl req -passin pass:1111 -new -key server.key -out server.csr -subj "/CN=${localhost}"

#### Step 4: Sign the certificate with the CA we created (It's called self signing) -server.crt
openssl x509 -req -passin pass:1111 -days 365 -in server.csr -CA ca.crt -CAkey ca.key -set_serial 01 -out server.crt

#### Step 5: Convert the server certificate to .pem formate (server.pem) -usable by grpc
openssl pkcs8 -topk8 -nocrypt -passin pass:1111 -in server.key -out server.pem


##( Windows setup) modifications for windows coming soon 
### Dependent on environment setups

#### Step 1)

#### Step 2)

#### Step 3)

#### Step 4)

#### Step 5)
