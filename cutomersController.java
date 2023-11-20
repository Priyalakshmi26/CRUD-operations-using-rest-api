import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.TypeFactory;

@WebServlet("/api/v1/customers/*")
public class cutomersController extends HttpServlet{
    static customersManager instance;
    static List<customers> allCustomers;

    static ObjectMapper objectMapper;
    static TypeFactory typeFactory;
    static CollectionType listType;

    static {
        instance = customersManager.getInstance();
        objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT); // Enable pretty printing
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES); // Ignore unknown properties during deserialization
        typeFactory = objectMapper.getTypeFactory();
        listType = typeFactory.constructCollectionType(List.class, Items.class);
    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        String pathInfo = request.getPathInfo();

        if (pathInfo == null || pathInfo.equals("/")) {
            // Return all items
            out.print(getJSONAllItems());
        } else {
            String[] pathParts = pathInfo.split("/");
            if (pathParts.length == 2) {
                // Get the item ID
                String customerid = pathParts[1];
                getSingleCustomer(out, customerid);
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid URL");
            }
        }
        out.flush();
    }

//    @Override
//    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        // Read parameters from the request
//        PrintWriter out = response.getWriter();
//        response.setContentType("application/json");
//        response.setCharacterEncoding("UTF-8");
//        String pathInfo = request.getPathInfo();
//        if(pathInfo==null || pathInfo.equals("/")) {
//            JsonNode jsonNode = objectMapper.readTree(request.getReader());
//
//            String name = jsonNode.get("name").asText();
//            String email = jsonNode.get("email").asText();
//            String phone_number = jsonNode.get("phone_number").asText();
//
//            customers newCustomers = new customers(name, email, phone_number);
//            instance.addCustomers(name, email, phone_number);
//            response.setStatus(HttpServletResponse.SC_CREATED);
//            response.getWriter().print("Customer added successfully");
//        }
////        String[] pathParts = pathInfo.split("/");
////        if(pathParts.length==3){
////
////        }
//    }
@Override
protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");
    String pathInfo = request.getPathInfo();

    if (pathInfo == null || pathInfo.equals("/")) {
        // Handle adding a customer without contact details
        JsonNode jsonNode = objectMapper.readTree(request.getReader());

        String name = jsonNode.get("name").asText();
        String email = jsonNode.get("email").asText();
        String phone_number = jsonNode.get("phone_number").asText();

        customers newCustomers = new customers(name, email, phone_number);
        instance.addCustomers(name, email, phone_number);
        response.setStatus(HttpServletResponse.SC_CREATED);
        response.getWriter().print("Customer added successfully");
    } else if (pathInfo.matches("/\\d+/contact_persons")) {
        // Handle adding contact persons for an existing customer
        // Extract the customer_id from the path
        System.out.println("Server");
        String[] pathParts = pathInfo.split("/");
        int customer_id = Integer.parseInt(pathParts[1]);

        JsonNode jsonNode = objectMapper.readTree(request.getReader());
        String personName = jsonNode.get("person_name").asText();

        // Add the contact person to the customer with the specified customer_id
        instance.addContactPerson(customer_id, personName);

        response.setStatus(HttpServletResponse.SC_CREATED);
        response.getWriter().print("Contact person added successfully for customer with ID " + customer_id);
    } else {
        response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid URL");
    }
}
//    @Override
//    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        PrintWriter out = response.getWriter();
//        String pathInfo = request.getPathInfo();
//
//        if (pathInfo == null || pathInfo.equals("/")) {
//            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid URL");
//        } else {
//            String[] pathParts = pathInfo.split("/");
//            if (pathParts.length == 2) {
//                String customerid = pathParts[1];
//                instance.removeCustomers(Integer.parseInt(customerid));
//                response.getWriter().print("Customer deleted successfully");
//            } else {
//                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid URL");
//            }
//        }
//        out.flush();
//    }
@Override
protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    String pathInfo = request.getPathInfo();

    if (pathInfo == null || pathInfo.equals("/")) {
        response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid URL");
    } else {
        String[] pathParts = pathInfo.split("/");
        if (pathParts.length == 2) {
            String customerid = pathParts[1];
            boolean customerDeleted = instance.removeCustomers(Integer.parseInt(customerid));
            if (customerDeleted) {
                response.getWriter().print("Customer deleted successfully");
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Customer not found");
            }
        } else if (pathParts.length == 4 && pathParts[2].equals("contact_persons")) {
            // Handle deleting a contact person for a specific customer
            String customerid = pathParts[1];
            String contactPersonId = pathParts[3];
            boolean contactPersonDeleted = instance.removeContactPerson(Integer.parseInt(customerid), Integer.parseInt(contactPersonId));
            if (contactPersonDeleted) {
                response.getWriter().print("Contact person deleted successfully");
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Contact person not found");
            }
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid URL");
        }
    }
    out.flush();
}

//    @Override
//    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        PrintWriter out = response.getWriter();
//        String pathInfo = request.getPathInfo();
//
//        if (pathInfo == null || pathInfo.equals("/")) {
//            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid URL");
//        } else {
//            String[] pathParts = pathInfo.split("/");
//            if (pathParts.length == 2) {
//                String customer_id = pathParts[1]; // Get the customer ID from the URL
//                JsonNode jsonNode = objectMapper.readTree(request.getReader()); // Read JSON data from the request
//
//                // Extract the updated properties from the JSON request
//                String first_name = jsonNode.get("first_name").asText();
//                String email = jsonNode.get("email").asText();
//                String phone_number = jsonNode.get("phone_number").asText();
//
//                // Update the customer with the provided ID
//                customers updatedCustomer = instance.updateCustomers(Integer.parseInt(customer_id), first_name, email, phone_number);
//                if (updatedCustomer != null) {
//                    String updatedCustomerJson = objectMapper.writeValueAsString(updatedCustomer);
//                    response.setStatus(HttpServletResponse.SC_OK);
//                    response.setContentType("application/json");
//                    response.setCharacterEncoding("UTF-8");
//                    out.print("Things you updated in the id "+customer_id);
//                    out.print(updatedCustomerJson);
//                } else {
//                    response.sendError(HttpServletResponse.SC_NOT_FOUND, "Customer not found");
//                }
//            }
//            else {
//                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid URL");
//            }
//        }
//        out.flush();
//    }
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        String pathInfo = request.getPathInfo();

        if (pathInfo == null || pathInfo.equals("/")) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid URL");
        } else {
            String[] pathParts = pathInfo.split("/");
            if (pathParts.length == 2) {
                String customer_id = pathParts[1]; // Get the customer ID from the URL
                JsonNode jsonNode = objectMapper.readTree(request.getReader()); // Read JSON data from the request

                // Extract the updated properties from the JSON request
                String first_name = jsonNode.get("first_name").asText();
                String email = jsonNode.get("email").asText();
                String phone_number = jsonNode.get("phone_number").asText();
                int contact_person_id = jsonNode.get("contact_person_id").asInt();

                if (String.valueOf(contact_person_id).length() == 4) {
                    // Update the customer with the provided ID and related contact person
                    customers updatedCustomer = instance.updateCustomerWithContactPerson(
                            Integer.parseInt(customer_id), first_name, email, phone_number, contact_person_id);

                    if (updatedCustomer != null) {
                        String updatedCustomerJson = objectMapper.writeValueAsString(updatedCustomer);
                        response.setStatus(HttpServletResponse.SC_OK);
                        response.setContentType("application/json");
                        response.setCharacterEncoding("UTF-8");
                        out.print("Customer updated successfully");
                        out.print(updatedCustomerJson);
                    } else {
                        response.sendError(HttpServletResponse.SC_NOT_FOUND, "Customer not found");
                    }
                } else {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Contact person ID must have a length of 4.");
                }
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid URL");
            }
        }
        out.flush();
    }




    private String getJSONAllItems() throws JsonProcessingException {
        allCustomers= instance.getAllCustomers();
        return objectMapper.writeValueAsString(allCustomers);
    }

    private void getSingleCustomer(PrintWriter out, String id) throws JsonProcessingException {
        customers singleCustomer = instance.getCustomer(Integer.parseInt(id));

        if (singleCustomer!= null) {
            String json = objectMapper.writeValueAsString(singleCustomer);
            out.print(json);
        } else {
            out.print("{\"error\": \"Customer not found\"}");
        }
    }
}

//curl -X POST http://localhost:8080/Application1/api/v1/customers -H "Content-Type: application/json" -d '{
//        "name": "John Doe",
//        "email": "john@example.com",
//        "phone_number": "123-456-7890"
//        }'

// curl -X PUT http://localhost:8080/Application1/api/v1/customers/11 -H "Content-Type: application/json" -d '{
//           "customer_id":8,
//         "first_name": "New Name",
//         "email":"new@gmail.com",
//         "phone_number": "7384792374"
//         }'

//curl -X DELETE http://localhost:8080/Application1/api/v1/customers/11
