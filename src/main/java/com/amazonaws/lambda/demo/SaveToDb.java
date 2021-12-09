package com.amazonaws.lambda.demo;

import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.PutItemOutcome;
import com.amazonaws.services.dynamodbv2.document.spec.PutItemSpec;
import com.amazonaws.services.dynamodbv2.model.ConditionalCheckFailedException;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class SaveToDb 
	implements RequestHandler<String, String> {
	  
	 // open an instance of our table to save
	  private DynamoDB dynamoDb;
	  private String DYNAMODB_TABLE_NAME = "vanity_number_gen";
	  private Regions REGION = Regions.US_EAST_1;
	
	  public String handleRequest(
	    String phoneNumber, String vanityNumber, Context context) {
	      this.initDynamoDbClient();
	      persistData(phoneNumber, vanityNumber);
	      return phoneNumber;
	  }
	
	  private PutItemOutcome persistData(String phoneNumber, String vanityNumber) throws ConditionalCheckFailedException {

		  // convert the string to a number so we can save it as a number in the DB
		  // we don't have enough possibilities to generate 5 different vanity numbers
		  // but we can go ahead and save these fields to the DB
		  Long phoneNumberLong = Long.valueOf(phoneNumber);
		  return this.dynamoDb.getTable(DYNAMODB_TABLE_NAME)
	        .putItem(
	          new PutItemSpec().withItem(new Item()
	            .withLong("phone_number", phoneNumberLong)
	            .withString("vanity_number_1", vanityNumber)
	            .withString("vanity_number_2", vanityNumber)
	            .withString("vanity_number_3", vanityNumber)
	            .withString("vanity_number_4", vanityNumber)
	            .withString("vanity_number_5", vanityNumber)
	          )
          );
	  }
	
	  private void initDynamoDbClient() {
	      AmazonDynamoDBClient client = new AmazonDynamoDBClient();
	      client.setRegion(Region.getRegion(REGION));
	      this.dynamoDb = new DynamoDB(client);
	  }

	@Override
	public String handleRequest(String input, Context context) {
		// TODO Auto-generated method stub
		return null;
	}
	}
