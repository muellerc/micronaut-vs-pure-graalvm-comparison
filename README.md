## 1. Build, package and deploy the Micronaut based native application

Change the directory and go to the **micronaut-app** sub-directory. Run the **package.sh** command to compile your Java sources, create your jar file and build the native image. We run these commands in a Docker container because when we create the native image, we have to use the same OS as the target environment (AWS Lambda in our case):

```bash
cd micronaut-app
./package.sh
```

Deploy the packaged AWS Lambda function using [AWS SAM](https://aws.amazon.com/serverless/sam/) and the [AWS CLI](https://aws.amazon.com/cli/):

```bash
sam deploy --guided
```

For **Stack Name**, enter `micronaut-app`. For **Region**, select your prefered region. Accept the **default** for all other options by hitting **ENTER**. After a successful deployment of your Lambda function, you should see in your terminal:  

*Successfully created/updated stack - micronaut-app in <region>*

Let's tese the deployed function:  

```bash
export FUNCTION_NAME=$(aws cloudformation describe-stacks \
    --stack-name micronaut-app \
    --query 'Stacks[].Outputs[?OutputKey==`MicronautAppName`].OutputValue' \
    --output text)

aws lambda invoke \
    --function-name $FUNCTION_NAME \
    --cli-binary-format raw-in-base64-out \
    --payload '{"body": "{\"name\": \"Serverless\"}", "resource": "/", "path": "/", "httpMethod": "POST", "isBase64Encoded": false, "queryStringParameters": {}, "multiValueQueryStringParameters": {}, "pathParameters": {}, "stageVariables": {}, "headers": {}}' \
    response.json
```

Open the **response.json** file and check, that the received **statusCode** is **200**.

```bash
more response.json 
```

Now, let's run 10 test, where we force a cold-start by updating the environment variable between each execution:  

```bash
for i in {1..10}; do aws lambda update-function-configuration --function-name $FUNCTION_NAME --environment "Variables={ForceReload=Value$i}" --output text; aws lambda invoke --function-name $FUNCTION_NAME --cli-binary-format raw-in-base64-out --payload '{"body": "{\"name\": \"Serverless\"}", "resource": "/", "path": "/", "httpMethod": "POST", "isBase64Encoded": false, "queryStringParameters": {}, "multiValueQueryStringParameters": {}, "pathParameters": {}, "stageVariables": {}, "headers": {}}' response.json; done
```

After a successful execution, open your [AWS X-Ray Console](https://console.aws.amazon.com/xray/home) and find your trace with the lowest cold-start (Initialization) time for this AWS Lambda function. For me, it was 424 ms as you can see below:  

![X-Ray Trace](/image/micronaut-app/x-ray.png)

Let's also have a look at other interesting data:  

- Deployment size 17.8 MB  
- Max Memory Used: 152 MB (lowest amount for all 10 executions)  
- Billed Duration: 808 ms (fasted overall execution for a cold-start)  

![CloudWatch Logs](/image/micronaut-app/cloud-watch-logs.png)

It has to be faster...


## 2. Build, package and deploy the pure GraalVM based native application

Change the directory and go to the **pure-graalvm-app** sub-directory. Run the **package.sh** command to compile your Java sources, create your jar file and build the native image. We run these commands in a Docker container because when we create the native image, we have to use the same OS as the target environment (AWS Lambda in our case):

```bash
cd pure-graalvm-app
./package.sh
```

Deploy the packaged AWS Lambda function using [AWS SAM](https://aws.amazon.com/serverless/sam/) and the [AWS CLI](https://aws.amazon.com/cli/):

```bash
sam deploy --guided
```

For **Stack Name**, enter `pure-graalvm-app`. For **Region**, select your prefered region. Accept the **default** for all other options by hitting **ENTER**. After a successful deployment of your Lambda function, you should see in your terminal:  

*Successfully created/updated stack - pure-graalvm-app in <region>*

Let's tese the deployed function:  

```bash
export FUNCTION_NAME=$(aws cloudformation describe-stacks \
    --stack-name pure-graalvm-app \
    --query 'Stacks[].Outputs[?OutputKey==`PureGraalVMAppName`].OutputValue' \
    --output text)

aws lambda invoke \
    --function-name $FUNCTION_NAME \
    --cli-binary-format raw-in-base64-out \
    --payload '{"body": "{\"name\": \"Serverless\"}", "resource": "/", "path": "/", "httpMethod": "POST", "isBase64Encoded": false, "queryStringParameters": {}, "multiValueQueryStringParameters": {}, "pathParameters": {}, "stageVariables": {}, "headers": {}}' \
    response.json
```

Open the **response.json** file and check, that the received **statusCode** is **200**.

```bash
more response.json 
```

Now, let's run 10 test, where we force a cold-start by updating the environment variable between each execution:  

```bash
for i in {1..10}; do aws lambda update-function-configuration --function-name $FUNCTION_NAME --environment "Variables={ForceReload=Value$i}" --output text; aws lambda invoke --function-name $FUNCTION_NAME --cli-binary-format raw-in-base64-out --payload '{"body": "{\"name\": \"Serverless\"}", "resource": "/", "path": "/", "httpMethod": "POST", "isBase64Encoded": false, "queryStringParameters": {}, "multiValueQueryStringParameters": {}, "pathParameters": {}, "stageVariables": {}, "headers": {}}' response.json; done
```

After a successful execution, open your [AWS X-Ray Console](https://console.aws.amazon.com/xray/home) and find your trace with the lowest cold-start (Initialization) time for this AWS Lambda function. For me, it was 153 ms as you can see below:  

![X-Ray Trace](/image/pure-graalvm-app/x-ray.png)

Let's also have a look at other interesting data:  

- Deployment size 5.5 MB  
- Max Memory Used: 50 MB (lowest amount for all 10 executions)  
- Billed Duration: 254 ms (fasted overall execution for a cold-start)  

![CloudWatch Logs](/image/pure-graalvm-app/cloud-watch-logs.png)

Cool! But let's make it even better!


## 3. Build, package and deploy the pure GraalVM based native application with eager initialisation

Change the directory and go to the **pure-graalvm-app-eager-init** sub-directory. Run the **package.sh** command to compile your Java sources, create your jar file and build the native image. We run these commands in a Docker container because when we create the native image, we have to use the same OS as the target environment (AWS Lambda in our case):

```bash
cd pure-graalvm-app-eager-init
./package.sh
```

Deploy the packaged AWS Lambda function using [AWS SAM](https://aws.amazon.com/serverless/sam/) and the [AWS CLI](https://aws.amazon.com/cli/):

```bash
sam deploy --guided
```

For **Stack Name**, enter `pure-graalvm-app-eager-init`. For **Region**, select your prefered region. Accept the **default** for all other options by hitting **ENTER**. After a successful deployment of your Lambda function, you should see in your terminal:  

*Successfully created/updated stack - pure-graalvm-app-eager-init in <region>*

Let's tese the deployed function:  

```bash
export FUNCTION_NAME=$(aws cloudformation describe-stacks \
    --stack-name pure-graalvm-app-eager-init \
    --query 'Stacks[].Outputs[?OutputKey==`PureGraalVMAppEagerInitName`].OutputValue' \
    --output text)

aws lambda invoke \
    --function-name $FUNCTION_NAME \
    --cli-binary-format raw-in-base64-out \
    --payload '{"body": "{\"name\": \"Serverless\"}", "resource": "/", "path": "/", "httpMethod": "POST", "isBase64Encoded": false, "queryStringParameters": {}, "multiValueQueryStringParameters": {}, "pathParameters": {}, "stageVariables": {}, "headers": {}}' \
    response.json
```

Open the **response.json** file and check, that the received **statusCode** is **200**.

```bash
more response.json 
```

Now, let's run 10 test, where we force a cold-start by updating the environment variable between each execution:  

```bash
for i in {1..10}; do aws lambda update-function-configuration --function-name $FUNCTION_NAME --environment "Variables={ForceReload=Value$i}" --output text; aws lambda invoke --function-name $FUNCTION_NAME --cli-binary-format raw-in-base64-out --payload '{"body": "{\"name\": \"Serverless\"}", "resource": "/", "path": "/", "httpMethod": "POST", "isBase64Encoded": false, "queryStringParameters": {}, "multiValueQueryStringParameters": {}, "pathParameters": {}, "stageVariables": {}, "headers": {}}' response.json; done
```

After a successful execution, open your [AWS X-Ray Console](https://console.aws.amazon.com/xray/home) and find your trace with the lowest cold-start (Initialization) time for this AWS Lambda function. For me, it was 144 ms as you can see below:  

![X-Ray Trace](/image/pure-graalvm-app-eager-init/x-ray.png)

Let's also have a look at other interesting data:  

- Deployment size 5.5 MB  
- Max Memory Used: 50 MB (lowest amount for all 10 executions)  
- Billed Duration: 147 ms (fasted overall execution for a cold-start)  

![CloudWatch Logs](/image/pure-graalvm-app-eager-init/cloud-watch-logs.png)

Yes, we could lower even further the perceive latency of our users by initializing the Jackson library during the function initialisation! Why? During initialization, Lambda can take advantage of burst CPU credits, no matter the memory settings for the function.   