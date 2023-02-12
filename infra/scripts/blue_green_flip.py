# Copyright 2017 Amazon.com, Inc. or its affiliates. All Rights Reserved.
# Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file except in compliance with
# the License. A copy of the License is located at
#     http://aws.amazon.com/apache2.0/
# or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
# CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and
# limitations under the License.

from __future__ import print_function

import json
import boto3
import sys
import traceback

print('Loading function')

code_pipeline = boto3.client('codepipeline')
elbclient = boto3.client('elbv2')


def put_job_success(job, message):
    """Notify CodePipeline of a successful job

    Args:
        job: The CodePipeline job ID
        message: A message to be logged relating to the job status

    Raises:
        Exception: Any exception thrown by .put_job_success_result()

    """
    print('Putting job success')
    print(message)
    code_pipeline.put_job_success_result(jobId=job)


def put_job_failure(job, message):
    """Notify CodePipeline of a failed job

    Args:
        job: The CodePipeline job ID
        message: A message to be logged relating to the job status

    Raises:
        Exception: Any exception thrown by .put_job_failure_result()

    """
    print('Putting job failure')
    print(message)
    code_pipeline.put_job_failure_result(jobId=job, failureDetails={'message': message, 'type': 'JobFailed'})


def continue_job_later(job, message):
    """Notify CodePipeline of a continuing job

    This will cause CodePipeline to invoke the function again with the
    supplied continuation token.

    Args:
        job: The JobID
        message: A message to be logged relating to the job status
        continuation_token: The continuation token

    Raises:
        Exception: Any exception thrown by .put_job_success_result()

    """

    # Use the continuation token to keep track of any job execution state
    # This data will be available when a new job is scheduled to continue the current execution
    continuation_token = json.dumps({'previous_job_id': job})

    print('Putting job continuation')
    print(message)
    code_pipeline.put_job_success_result(jobId=job, continuationToken=continuation_token)

def get_user_params(job_id,job_data):
    """Gets user parameter object sent from CodePipeline

        Args:
            job_id: The CodePipeline job ID
            job_data: json job data sent from codepipeline

        Raises:
            Exception: Any exception caught in decoding the params

    """
    try:
        user_parameters = job_data['actionConfiguration']['configuration']['UserParameters']
        decoded_parameters = json.loads(user_parameters)
        print(decoded_parameters)
    except Exception as e:
        put_job_failure(job_id,e)
        raise Exception('UserParameters could not be decoded as JSON')

    return decoded_parameters


def swaptargetgroups(elbname):
    """Discovers the live target group and non-production target group and swaps

            Args:
                elbname : name of the load balancer, which has the target groups to swap

            Raises:
                Exception: Any exception thrown by handler

    """
    elbresponse = elbclient.describe_load_balancers(Names=[elbname])

    listners = elbclient.describe_listeners(LoadBalancerArn=elbresponse['LoadBalancers'][0]['LoadBalancerArn'])
    for x in listners['Listeners']:
        if (x['Port'] == 443):
            livelistenerarn = x['ListenerArn']
        if (x['Port'] == 80):
            livelistenerarn = x['ListenerArn']
        if (x['Port'] == 8443):
            betalistenerarn = x['ListenerArn']
        if (x['Port'] == 8080):
            betalistenerarn = x['ListenerArn']

    livetgresponse = elbclient.describe_rules(ListenerArn=livelistenerarn)

    for x in livetgresponse['Rules']:
        if x['Priority'] == '1':
            livetargetgroup = x['Actions'][0]['TargetGroupArn']
            liverulearn = x['RuleArn']

    betatgresponse = elbclient.describe_rules(ListenerArn=betalistenerarn)

    for x in betatgresponse['Rules']:
        if x['Priority'] == '1':
            betatargetgroup = x['Actions'][0]['TargetGroupArn']
            betarulearn = x['RuleArn']

    print("Live=" + livetargetgroup)
    print("Beta=" + betatargetgroup)

    modifyOnBeta = elbclient.modify_rule(
        RuleArn=betarulearn,
        Actions=[
            {
                'Type': 'forward',
                'TargetGroupArn': livetargetgroup
            }
        ]
    )

    print(modifyOnBeta)

    modifyOnLive = elbclient.modify_rule(
        RuleArn=liverulearn,
        Actions=[
            {
                'Type': 'forward',
                'TargetGroupArn': betatargetgroup
            }
        ]
    )

    print(modifyOnLive)
    modify_tags(livetargetgroup,"IsProduction","False")
    modify_tags(betatargetgroup, "IsProduction", "True")

def modify_tags(arn,tagkey,tagvalue):
    """Modifies the tags on the target groups as an identifier, after swap has been performed to indicate, 
        which target group is live and which target group is non-production

                Args:
                    arn : AWS ARN of the Target Group
                    tagkey: Key of the Tag
                    tagvalue: Value of the Tag

                Raises:
                    Exception: Any exception thrown by handler

    """

    elbclient.add_tags(
        ResourceArns=[arn],
        Tags=[
            {
                'Key': tagkey,
                'Value': tagvalue
            },
        ]
    )
def handler(event, context):
    """ Main haldler as an entry point of the AWS Lambda function. Handler controls the sequence of methods to call
    1. Read Job Data from input json
    2. Read Job ID from input json
    3. Get parameters from input json
    4. Get Load balancer name from parameters
    5. Perform the swap on target group
    6. Send success or failure to codepipeline

                Args:
                    event : http://docs.aws.amazon.com/codepipeline/latest/userguide/actions-invoke-lambda-function.html
                    #actions-invoke-lambda-function-json-event-example
                    context: not used but required for Lambda function

                Raises:
                    Exception: Any exception thrown by handler

    """

    try:
        print(event)
        job_id = event['CodePipeline.job']['id']
        job_data = event['CodePipeline.job']['data']
        params = get_user_params(job_id,job_data)
        elb_name = params['ElbName']
        print("ELBNAME="+elb_name)
        swaptargetgroups(elb_name)
        put_job_success(job_id,"Target Group Swapped.")

    except Exception as e:
        print('Function failed due to exception.')
        print(e)
        traceback.print_exc()
        put_job_failure(job_id, 'Function exception: ' + str(e))

    print('Function complete.')
    return "Complete."

if __name__ == "__main__":
    handler(sys.argv[0], None)