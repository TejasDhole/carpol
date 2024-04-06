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

public class userService {
    private static final AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().build();
    private static final DynamoDBMapper dynamoDBMapper = new DynamoDBMapper(client);

    public static String getUsers() {
        List<User> users = dynamoDBMapper.scan(User.class, new DynamoDBScanExpression());
        return new Gson().toJson(users);
    }
    public static String getUserByUserId(String userId) {
        User user = dynamoDBMapper.load(User.class, userId);
        if (user != null) {
            return new Gson().toJson(user);
        } else {
            return "User not found";
        }
    }

    public static String createUser(String requestBody) {
        User user = new Gson().fromJson(requestBody, User.class);
        dynamoDBMapper.save(user);
        return "User created: " + new Gson().toJson(user);
    }
    public static String updateUser(String requestBody) {
        User updatedUser = new Gson().fromJson(requestBody, User.class);
        User existingUser = dynamoDBMapper.load(User.class, updatedUser.getUserId());
        if (existingUser != null) {
            existingUser.setName(updatedUser.getName());
            existingUser.setEmail(updatedUser.getEmail());
            existingUser.setPhone(updatedUser.getPhone());
            existingUser.setGender(updatedUser.getGender());
            existingUser.setDob(updatedUser.getDob());
            existingUser.setAbout(updatedUser.getAbout());
            dynamoDBMapper.save(existingUser);
            return "User updated: " + existingUser.getUserId();
        } else {
            return "User not found";
        }
    }


    public static String deleteUser(String userId) {
        User existingUser = dynamoDBMapper.load(User.class, userId);
        if (existingUser != null) {
            dynamoDBMapper.delete(existingUser);
            return "User deleted: " + userId;
        } else {
            return "User not found";
        }
    }

}