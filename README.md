# Cashfree Cashgram Integration Kit for Java

Below is an integration flow on how to use Cashfree's Cashgram.
Please go through the payout docs [here](https://dev.cashfree.com/payouts)
<br/>
This kit is linked to the Cashgram flow. Go [here](https://dev.cashfree.com/payouts/integrations/cashgram) to get a better understanding.
<br/>

## Functionalities

The following kit contains the following functionalities:
    <ol>
    <li> [getToken](https://dev.cashfree.com/api-reference/payouts-api#authorise): to get the auth token to be used in all following calls.
    <li> [createCashgram](https://dev.cashfree.com/api-reference/payouts-api#create-cashgram-request): to create a cashgram.
    <li> [cashgramGetStatus](https://dev.cashfree.com/api-reference/payouts-api#create-cashgram-request): get the status of the created cashgram.
    </ol>

## Build Steps

follow the following build steps to compile the Integration kit:
  1. Download the code and cd into the directory containing the code.
  2. run the following command from your terminal to compile the code into an executable file:
      ```
      javac -cp .:json-simple-1.1.1.jar Main.java
      ```
      The code has a dependency on json simple for json parsing. Include this in your classpath while building(Hence -cp .:json-simple-1.1.1.jar 
      to include the jar in the classpath).
## Set Up

### Pre Requisites:
The following kit uses information stored in a config file. Before running the code for the first time open the config.json file and add the relevant details:
  1. ClientId: This is a unique identifier that identifies the merchant. For more information please go [here](https://dev.cashfree.com/payouts).
  2. ClientSecret: Corresponding secret key for the given ClientId that helps Cashfree identify the merchant. For more information please go [here](https://dev.cashfree.com/payouts).
  3. Environment: Environment to be hit. The following values are accepted prod: for production, test: for the test environment.

### IP Whitelisting:

Your IP has to be whitelisted to hit Cashfree's server. For more information please go [here](https://dev.cashfree.com/development/api/ip-whitelisting).

### Cashgram

The following kit needs Cashgram details to create a cashgram. To know more information on how to create cashgrams please go [here](https://dev.cashfree.com/api-reference/payouts-api#create-cashgram-request).

The kit picks up the cashgram details from the config file cashgramDetails section.
Required fields are:
  1. cashgramId: unique Id of the created cashgram.
  2. amount: amount to be transferred.
  3. name: name of the contact.
  4. phone: phone number of the contact.

## Usage

Once the config file is setup you can run the executable, to run the entire flow. Authorize, create a cashgram 
and check the status of the created cashgram.

You can change the necessary values in the config file as per your requirements and re-run the script whenever needed.

## Doubts

Reach out to techsupport@cashfree.com in case of doubts.
 


