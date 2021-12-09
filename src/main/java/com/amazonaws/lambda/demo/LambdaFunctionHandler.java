package com.amazonaws.lambda.demo;

import java.util.Dictionary;
import java.util.Hashtable;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class LambdaFunctionHandler implements RequestHandler<Object, String> {

    @Override
    public String handleRequest(Object input, Context context) {
    	context.getLogger().log("Input: " + input);
    	String phoneNumberStr = input.toString();
        String vanityNumber = "";
        String[] p = phoneNumberStr.split("=");
        phoneNumberStr = p[1];
        phoneNumberStr = phoneNumberStr.substring(0, phoneNumberStr.length() - 1);
        
        // make sure the number is long enough
        if (phoneNumberStr.length() > 6) {
	        // this is the logic we would use if we weren't using pre-loaded words
	        // we could check every digit of the phone number for possible letters
	//        for (int i = 0; i < phoneNumberStr.length(); i++) {
	//        	if (phoneNumberStr.charAt(i) == '1' || phoneNumberStr.charAt(i) == '0') {
	//            	context.getLogger().log("char at " + i + " is not usable");
	//            	character = phoneNumberStr.charAt(i);
	//        	} else if (phoneNumberStr.charAt(i) == '2') {
	//            	context.getLogger().log("char at " + i + " is A B C");
	//        	} else if (phoneNumberStr.charAt(i) == '3') {
	//            	context.getLogger().log("char at " + i + " is D E F");
	//        	} else if (phoneNumberStr.charAt(i) == '4') {
	//            	context.getLogger().log("char at " + i + " is G H I");
	//        	} else if (phoneNumberStr.charAt(i) == '5') {
	//            	context.getLogger().log("char at " + i + " is J K L");
	//        	} else if (phoneNumberStr.charAt(i) == '6') {
	//            	context.getLogger().log("char at " + i + " is M N O");
	//        	} else if (phoneNumberStr.charAt(i) == '7') {
	//            	context.getLogger().log("char at " + i + " is P Q R S");
	//        	} else if (phoneNumberStr.charAt(i) == '8') {
	//            	context.getLogger().log("char at " + i + " is T U V");
	//        	} else if (phoneNumberStr.charAt(i) == '9') {
	//            	context.getLogger().log("char at " + i + " is W X Y Z");
	//        	}
	//        	vanityNumber = vanityNumber + character;
	//        }
	        
	        // common word library
	        // I made a small library of common words to save on space
	        // the top for four five one grow love eat food tick pets pet grip
	        // 843 867 367 3687 3483 663 4769 5683 328 3663 8425 7387 738 4747
	
	        // try lean look stay deal cost no low wait time fix leak save care earn
	        // 879 5326 5665 7829 3325 2678 66 569 9248 8463 349 5325 7283 2273 3276
	   
	        Dictionary dictionary = new Hashtable();
	        dictionary.put(3687, "four");
	        dictionary.put(3483, "five");
	        dictionary.put(4769, "grow");
	        dictionary.put(5683, "love");
	        dictionary.put(3663, "food");
	        dictionary.put(8425, "tick");
	        dictionary.put(7387, "pets");
	        dictionary.put(4357, "help");
	        dictionary.put(5326, "lean");
	        dictionary.put(5665, "look");
	        dictionary.put(7829, "stay");
	        dictionary.put(3325, "deal");
	        dictionary.put(2678, "cost");
	        dictionary.put(9248, "wait");
	        dictionary.put(8463, "time");
	        dictionary.put(5325, "leak");
	        dictionary.put(7283, "save");
	        dictionary.put(2273, "care");
	        dictionary.put(3276, "earn");
	        
	        // check the last 4 digits for any matches to the dictionary terms above
	        // we are trying to return something like 1-800-555-PETS
	        int lastFour = Integer.parseInt(phoneNumberStr.substring(phoneNumberStr.length()-4, phoneNumberStr.length()));
	    	context.getLogger().log("lastFour: " + lastFour);

	        if(dictionary.get(lastFour) != null) {
	        	vanityNumber = phoneNumberStr;
	        	vanityNumber = vanityNumber.substring(0, vanityNumber.length()-4);
	        	vanityNumber = vanityNumber + String.valueOf(dictionary.get(lastFour));
		    	context.getLogger().log("vanityNumber: " + vanityNumber);
	        }
	        
	    	// if we couldn't make a vanity number, leave the vanity_number null in the table
	    	context.getLogger().log("Phone Number: " + phoneNumberStr);
	    	context.getLogger().log("Vanity Number: " + vanityNumber);
	    	
	    	// if there was a larger dictionary of words, we could go through each possibility and save 
	    	// each on as its own vanityNumber2, vanityNumber3, vanityNumber4, etc.
	    	SaveToDb saveToDb = new SaveToDb();
	    	saveToDb.handleRequest(phoneNumberStr, vanityNumber, context);
        } else {
	    	context.getLogger().log("Phone Number is too short! " + phoneNumberStr);
        }
        return vanityNumber;
    }

}
