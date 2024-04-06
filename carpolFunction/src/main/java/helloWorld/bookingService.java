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

public class bookingService {
    private static final AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().build();
    private static final DynamoDBMapper dynamoDBMapper = new DynamoDBMapper(client);

    public static String getBookings() {
        List<Booking> bookings = dynamoDBMapper.scan(Booking.class, new DynamoDBScanExpression());
        return new Gson().toJson(bookings);
    }

    public static String getBookingByBookingId(String bookingId) {
        Booking booking = dynamoDBMapper.load(Booking.class, bookingId);
        if (booking != null) {
            return new Gson().toJson(booking);
        } else {
            return "Booking not found";
        }
    }

    public static String createBooking(String requestBody) {
        Booking booking = new Gson().fromJson(requestBody, Booking.class);
        dynamoDBMapper.save(booking);
        return "Booking created: " + new Gson().toJson(booking);
    }

    public static String updateBooking(String requestBody) {
        Booking updatedBooking = new Gson().fromJson(requestBody, Booking.class);
        Booking existingBooking = dynamoDBMapper.load(Booking.class, updatedBooking.getBookingId());
        if (existingBooking != null) {
            existingBooking.setFare(updatedBooking.getFare());
            existingBooking.setDateTime(updatedBooking.getDateTime());
            existingBooking.setUserId(updatedBooking.getUserId());
            existingBooking.setProviderId(updatedBooking.getProviderId());
            existingBooking.setTimestamp(updatedBooking.getTimestamp());
            dynamoDBMapper.save(existingBooking);
            return "Booking updated: " + existingBooking.getBookingId();
        } else {
            return "Booking not found";
        }
    }

    public static String deleteBooking(String bookingId) {
        Booking existingBooking = dynamoDBMapper.load(Booking.class, bookingId);
        if (existingBooking != null) {
            dynamoDBMapper.delete(existingBooking);
            return "Booking deleted: " + bookingId;
        } else {
            return "Booking not found";
        }
    }

}