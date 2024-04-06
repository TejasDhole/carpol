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
import helloWorld.userService;

import com.google.gson.Gson;


public class App implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
    private static final AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().build();
    private static final DynamoDBMapper dynamoDBMapper = new DynamoDBMapper(client);

    public APIGatewayProxyResponseEvent handleRequest(final APIGatewayProxyRequestEvent input, final Context context) {
        String httpMethod = input.getHttpMethod();
        String resourcePath = input.getPath();
        String output;
        int statusCode;
//
//        switch (httpMethod) {
//            case "GET":
//                output = userService.getUsers();
//                statusCode = 200;
//                break;
//            case "POST":
//                output = userService.createUser(input.getBody());
//                statusCode = 201;
//                break;
//            case "PUT":
//                output = userService.updateUser(input.getBody());
//                statusCode = 200;
//                break;
//            case "DELETE":
//                output = userService.deleteUser(input.getBody());
//                statusCode = 200;
//                break;
//            default:
//                output = "Invalid HTTP Method";
//                statusCode = 400;
//                break;
//        }
        if (resourcePath.equals("/user") && httpMethod.equals("GET")) {
            output = userService.getUsers();
             statusCode = 200;
             } else if (resourcePath.equals("/user") && httpMethod.equals("POST")) {
                    output = userService.createUser(input.getBody());
                    statusCode = 201;
             } else if (resourcePath.startsWith("/user/") && httpMethod.equals("GET")) {
            String userId = resourcePath.substring("/user/".length());
                  output = userService.getUserByUserId(userId);
                    statusCode = 200;
            // } else if (resourcePath.startsWith("/user/") && httpMethod.equals("PUT")) {
            //      updateUser(event, context);
             } else if (resourcePath.startsWith("/user/") && httpMethod.equals("DELETE")) {
                    String userId = resourcePath.substring("/user/".length());
                    output = userService.deleteUser(userId);
                    statusCode = 200;
        } else {
            output = "Invalid HTTP Method";
            statusCode = 400;
        }

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("X-Custom-Header", "application/json");

        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent()
                .withStatusCode(statusCode)
                .withHeaders(headers)
                .withBody(output);

        return response;
    }

}

// import java.util.HashMap;
// import java.util.Map;

// import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
// import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
// import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
// import com.amazonaws.services.lambda.runtime.Context;
// import com.amazonaws.services.lambda.runtime.RequestHandler;
// import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
// import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;

// public class App1 implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
//     public APIGatewayProxyResponseEvent handleRequest(final APIGatewayProxyRequestEvent input, final Context context) {

//         AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().build();
//         DynamoDBMapper dynamoDBMapper = new DynamoDBMapper(client);

//         Map<String, String> headers = new HashMap<>();
//         headers.put("Content-Type", "application/json");
//         headers.put("X-Custom-Header", "application/json");
//         APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent().withHeaders(headers);
//             String output = String.format("{ \"message\": \"hello world\", \"location\": \"%s\" }", "pageContents");

//             User user = new User();
//             user.setEmpId("EMP001");
//             user.setName("John Doe");
//             user.setEmail("johndoe@example.com");

//             dynamoDBMapper.save(user);
//             context.getLogger().log("Successfully added new Employee");

//             return response.withStatusCode(200).withBody(output);
//     }
// }