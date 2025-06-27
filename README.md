# 

## Model
www.msaez.io/#/105867850/storming/03ace7e054b1a8b7618fd07c466976a7

## Before Running Services
### Make sure there is a Kafka server running
```
cd kafka
docker-compose up
```
- Check the Kafka messages:
```
cd infra
docker-compose exec -it kafka /bin/bash
cd /bin
./kafka-console-consumer --bootstrap-server localhost:9092 --topic
```

## Run the backend micro-services
See the README.md files inside the each microservices directory:

- writerregistration
- subscriptionandpoint
- bookpublication
- aibookautomation


## Run API Gateway (Spring Gateway)
```
cd gateway
mvn spring-boot:run
```

## Test by API
- writerregistration
```
 http :8088/writes userId="user_id"name="name"email="email"bio="bio"status="status"createdAt="createdAt"
```
- subscriptionandpoint
```
 http :8088/users userId="user_id"name="name"email="email"passwordHash="passwordHash"isKtCustomer="isKtCustomer"subscription="subscription"
 http :8088/points pointId="pointId"userId="userId"balance="balance"
```
- bookpublication
```
 http :8088/books bookId="bookId"userId="user_id"title="title"content="content"summary="summary"coverImageUrl="coverImageUrl"category="category"price="price"status="status"viewCount="viewCount"createdAt="createdAt"
```
- aibookautomation
```
 http :8088/aiBookProcessors processorId="processorId"bookId="bookId"summary="summary"coverImageUrl="coverImageUrl"category="category"price="price "processStatus="ProcessStatus"createdAt="createdAt"
```


## Run the frontend
```
cd frontend
npm i
npm run serve
```

## Test by UI
Open a browser to localhost:8088

## Required Utilities

- httpie (alternative for curl / POSTMAN) and network utils
```
sudo apt-get update
sudo apt-get install net-tools
sudo apt install iputils-ping
pip install httpie
```

- kubernetes utilities (kubectl)
```
curl -LO "https://dl.k8s.io/release/$(curl -L -s https://dl.k8s.io/release/stable.txt)/bin/linux/amd64/kubectl"
sudo install -o root -g root -m 0755 kubectl /usr/local/bin/kubectl
```

- aws cli (aws)
```
curl "https://awscli.amazonaws.com/awscli-exe-linux-x86_64.zip" -o "awscliv2.zip"
unzip awscliv2.zip
sudo ./aws/install
```

- eksctl 
```
curl --silent --location "https://github.com/weaveworks/eksctl/releases/latest/download/eksctl_$(uname -s)_amd64.tar.gz" | tar xz -C /tmp
sudo mv /tmp/eksctl /usr/local/bin
```
