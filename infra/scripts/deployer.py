# Copyright 2017 Amazon.com, Inc. or its affiliates. All Rights Reserved.
# Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file except in compliance with
# the License. A copy of the License is located at
#     http://aws.amazon.com/apache2.0/
# or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
# CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and
# limitations under the License.

import boto3
import os
from botocore.client import Config
import zipfile
import json

elb_name = os.environ.get('ELB_NAME')
elb_client = boto3.client('elbv2')
describe_elb_response = None

def handler():
    """ Main handler as an entry point of code. Handler controls the sequence of methods to call.No inputs required. 
    As this runs in AWS CodeBuild, the script gets all the values from the environment variables in codebuild.
        1. Retrieve artifact (build.json) from the previous stage (CodeBuild phase, which builds application container images)
        2. Check if the load balancer exists. Name of the ELB is fed through environment variable by the pipeline.
        3. Get tag key value of the target group, running on port 8080 and 80 with KeyName as "Identifier"
        4. Get Sha of the image id running on target group at port 8080 and 80
        5. Edit the build.json retrieved from step-1 and append the values retrieved in step3 and step4
        6. Save the modified build.json. This file is the output from codebuild project and fed as an input to the CloudFormation
        execution stage.

                    Args: None

                    Raises:
                        Exception: Any exception thrown by handler

        """
    print(elb_name)
    build_id = get_build_artifact_id(get_build_execution_id())
    if check_elb_exists():
        beta_identifier, beta_sha, live_identifier, live_sha = find_beta_targetgroup()
        cf_inputs = { beta_identifier:str(build_id),live_identifier:live_sha }
    else:
        cf_inputs = {"Code1": str(build_id), "Code2": str(build_id)}
    with open('cf_inputs.json', 'w+') as outfile:
        json.dump(cf_inputs, outfile)

def check_elb_exists():
    """Checks if the Load Balancer Exists

                Args: None

                Raises: None, as we dont want to stop script execution. If the code reaches exception block, it means
                that the load balancer does not exists.
                
                Returns:
                    Boolean, True if Load Balancer Exists, False, if Load balancer does not exists

    """
    global describe_elb_response
    try:
        describe_elb_response = elb_client.describe_load_balancers(Names=[elb_name])
        return True

    except:
        print("Load Balancer does not exists")
        return False

def find_beta_targetgroup():
    """ Discovers the green side ( non production side) target group, which is running on port 8080.

                Args: None
                Returns:
                    beta_identifier : tag key value of the target group, running on port 8080 with KeyName as "Identifier"
                    beta_sha: Sha or the image id running on target group at port 8080
                    live_identifier : tag key value of the target group, running on port 80 with KeyName as "Identifier"
                    live_sha: Sha or the image id running on target group at port 80
                    

                Raises:
                    Exception: Any exception thrown by handler

    """

    listners = elb_client.describe_listeners(LoadBalancerArn=describe_elb_response['LoadBalancers'][0]['LoadBalancerArn'])

    for x in listners['Listeners']:
        if (x['Port'] == 80):
            livelistenerarn = x['ListenerArn']
        if (x['Port'] == 8080):
            betalistenerarn = x['ListenerArn']

    beta_tg_response = elb_client.describe_rules(ListenerArn=betalistenerarn)
    live_tg_response = elb_client.describe_rules(ListenerArn=livelistenerarn)

    for x in beta_tg_response['Rules']:
        if x['Priority'] == '1':
            beta_target_group = x['Actions'][0]['TargetGroupArn']
            betarulearn = x['RuleArn']
    for x in live_tg_response['Rules']:
        if x['Priority'] == '1':
            live_target_group = x['Actions'][0]['TargetGroupArn']
            betarulearn = x['RuleArn']

    beta_identifier,beta_sha = find_beta_image_identifier(beta_target_group)
    live_identifier, live_sha = find_beta_image_identifier(live_target_group)

    return beta_identifier,beta_sha,live_identifier,live_sha

def find_beta_image_identifier(targetgrouparn):
    """Queries the tags on TargetGroups

                Args:
                    targetgrouparn - Amazon ARN of the Target group that needs to be queried for the Tags
                Returns:
                    identifier : tag key value of the target group , with KeyName as "Identifier"
                    sha: Sha or the image id running on target group
                    
                Raises:
                    Exception: Any exception thrown by handler

    """
    response = elb_client.describe_tags(ResourceArns=[targetgrouparn])
    identifier = None
    imagesha = None
    for tags in response['TagDescriptions']:
        for tag in tags['Tags']:
            if tag['Key'] == "Identifier":
                print("Image identifier string on " + targetgrouparn + " : " + tag['Value'])
                identifier = tag['Value']
            if tag['Key'] == "Image":
                imagesha = tag['Value']
    return identifier,imagesha

def get_build_artifact_id(build_id):
    """Get artifact (build.json) from the build project . We are making this as an additional call to get the build.json
    which already contains the new built repository ECR path. We could have consolidated this script and executed in the build
    phase, but as codebuild accepts the input from one source only (scripts and application code are in different sources), thats 
    why an additional call to retrieve build.json from a different build project.

                    Args:
                        build_id - Build ID for codebuild (build phase)
                    Returns:
                        build.json

                    Raises:
                        Exception: Any exception thrown by handler

    """
    codebuild_client = boto3.client('codebuild')
    response = codebuild_client.batch_get_builds(
        ids=[
            str(build_id),
        ]
    )
    for build in response['builds']:
        s3_location = build['artifacts']['location']
        bucketkey = s3_location.split(":")[5]
        bucket = bucketkey.split("/")[0]
        key = bucketkey[bucketkey.find("/") + 1:]
        s3_client = boto3.client('s3', config=Config(signature_version='s3v4'))
        s3_client.download_file(bucket, key, 'downloaded_object')
        zip_ref = zipfile.ZipFile('downloaded_object', 'r')
        zip_ref.extractall('downloaded_folder')
        zip_ref.close()
        with open('downloaded_folder/build.json') as data_file:
            objbuild = json.load(data_file)
        print(objbuild['tag'])
        return objbuild['tag']

def get_build_execution_id():

    """Query Environment Variables to reteieve "CODEBUILD_INITIATOR", which gives codebuild id.
    Use this ID to call codepipeline API to retrieve last successful build ID (build phase)

                        Args:
                            None
                        Returns:
                            build id

                        Raises:
                            Exception: Any exception thrown by handler

    """
    codepipeline_client = boto3.client('codepipeline')
    initiator = str(os.environ.get('CODEBUILD_INITIATOR')).split("/")[-1]
    response = codepipeline_client.get_pipeline_state(
        name=initiator
    )

    for stage in response['stageStates']:
        if stage['stageName'] == 'Build':
            for actionstate in stage['actionStates']:
                if actionstate['actionName'] == 'Build':
                    return actionstate['latestExecution']['externalExecutionId']

if __name__ == '__main__':
    handler()