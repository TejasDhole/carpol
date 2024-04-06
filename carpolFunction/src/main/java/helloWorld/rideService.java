package helloWorld;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.google.gson.Gson;

public class rideService {
    private static final AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().build();
    private static final DynamoDBMapper dynamoDBMapper = new DynamoDBMapper(client);

    public static String getRiders() {
        List<Ride> riders = dynamoDBMapper.scan(Ride.class, new DynamoDBScanExpression());
        return new Gson().toJson(riders);
    }

    public static String createRide(String requestBody) {
        Ride ride = new Gson().fromJson(requestBody, Ride.class);
        dynamoDBMapper.save(ride);
        return "Ride created: " + ride.getRideId();
    }

    public static String updateRide(String requestBody) {
        Ride updatedRide = new Gson().fromJson(requestBody, Ride.class);
        Ride existingRide = dynamoDBMapper.load(Ride.class, updatedRide.getRideId());
        if (existingRide != null) {

            existingRide.setOriginAddress(updatedRide.getOriginAddress());
            existingRide.setDestinationAddress(updatedRide.getDestinationAddress());
            existingRide.setRideDate(updatedRide.getRideDate());
            existingRide.setRideTime(updatedRide.getRideTime());
            existingRide.setFare(updatedRide.getFare());
            existingRide.setCarType(updatedRide.getCarType());
            existingRide.setAvailableSeats(updatedRide.getAvailableSeats());
            existingRide.setMessage(updatedRide.getMessage());
            existingRide.setInstantBooking(updatedRide.isInstantBooking());
            existingRide.setPassengers(updatedRide.getPassengers());

            dynamoDBMapper.save(existingRide);
            return "Ride updated: " + existingRide.getRideId();

        } else {
            return "Ride not found";
        }
    }


    public static String deleteRide(String requestBody) {
        Ride rideToDelete = new Gson().fromJson(requestBody, Ride.class);
        Ride existingRide = dynamoDBMapper.load(Ride.class, rideToDelete.getRideId());
        if (existingRide != null) {
            dynamoDBMapper.delete(existingRide);
            return "Ride deleted: " + existingRide.getRideId();
        } else {
            return "Ride not found";
        }
    }

}