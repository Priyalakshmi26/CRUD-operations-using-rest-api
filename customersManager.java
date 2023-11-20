import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.sql.*;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;

import static java.lang.Class.forName;

public class customersManager {

    private static customersManager customerInstance;
    private final List<customers> allCustomers;
    //private int count;

    private customersManager() {
        this.allCustomers = new ArrayList<>();
        //count = 0;
    }

    public static customersManager getInstance() {
        if (customerInstance == null) {
            customerInstance = new customersManager();
        }
        return customerInstance;
    }

    public static boolean addCustomers(String first_name, String email, String phone_number) {
        try {
            forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/application", "root", "");
            PreparedStatement st = con
                    .prepareStatement("insert into customers (first_name,email,phone_number) values(?, ?, ?)");
            //st.setInt(1,id);
            st.setString(1, first_name);
            st.setString(2, email);
            st.setString(3, phone_number);
            st.executeUpdate();
            st.close();
            con.close();
            //++count;
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    public static boolean addContactPerson(int customer_id, String person_name) {
        try {
            forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/application", "root", "");
            PreparedStatement st = con
                    .prepareStatement("insert into contact_persons (customer_id,person_name) values(?, ?)");
            //st.setInt(1,id);
            st.setInt(1, customer_id);
            st.setString(2, person_name);
           // st.setString(3, phone_number);
            st.executeUpdate();
            st.close();
            con.close();
            //++count;
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    public boolean removeCustomers(int customer_id) {
        if (customer_id < 0) return false;
        //allItems.remove(id - 1);
        try {
            forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/application", "root", "");
            Statement stmt = con.createStatement();
            String q1 = "DELETE from customers WHERE customer_id = " + customer_id;
            int x = stmt.executeUpdate(q1);
            //--count;
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }
    public boolean removeContactPerson(int customer_id,int contactPersonId) {
        if (customer_id < 0) return false;
        //allItems.remove(id - 1);
        try {
            forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/application", "root", "");
            Statement stmt = con.createStatement();
            String q1 = "DELETE from contact_persons WHERE contact_person_id = " + contactPersonId;
            int x = stmt.executeUpdate(q1);
            //--count;
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

//    public customers getCustomer(int customer_id) {
//        customers customer = null;
//
//        try {
//            forName("com.mysql.jdbc.Driver");
//            Connection con = DriverManager.getConnection(
//                    "jdbc:mysql://localhost:3306/application", "root", "");
//            String query = "SELECT * FROM customers WHERE customer_id = ?";
//            PreparedStatement preparedStatement = con.prepareStatement(query);
//            preparedStatement.setInt(1, customer_id);
//
//            ResultSet rs = preparedStatement.executeQuery();
//            if (rs.next()) {
//                // Retrieve customer data from the result set
//                String firstName = rs.getString("first_name");
//                String email = rs.getString("email");
//                String phone_number = rs.getString("phone_number");
//
//                // Create a Customer object
//                customer = new customers(customer_id,firstName, email, phone_number);
//            }
//            ObjectMapper objectMapper = new ObjectMapper();
//            String json = objectMapper.writeValueAsString(customer);
//            System.out.println(json);
//            con.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return customer;
//    }


//    public List<customers> getAllCustomers() {
//        try {
//            forName("com.mysql.jdbc.Driver");
//            Connection con = DriverManager.getConnection(
//                    "jdbc:mysql://localhost:3306/application", "root", "");
//            Statement stmt = con.createStatement();
//            String q1 = "select * from customers";
//            ResultSet rs = stmt.executeQuery(q1);
//            while (rs.next()) {
//                allCustomers.add(new customers(rs.getString(2), rs.getString(3), rs.getString(4)));
//            }
//            ObjectMapper objectMapper = new ObjectMapper();
//            String json = objectMapper.writeValueAsString(allCustomers);
//            System.out.println(json);
//            con.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return allCustomers;
//    }

    public customers updateCustomers(int customer_id, String first_name, String email, String phone_number) {
        customers updatedCustomer = null;
        try {
            // 1. Establish a database connection
            forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/application", "root", "");

            // 2. Create the SQL UPDATE statement
            String updateSQL = "UPDATE customers SET first_name = ?, email = ?, phone_number = ? WHERE customer_id = ?";

            // 3. Create a PreparedStatement with the SQL statement
            PreparedStatement preparedStatement = conn.prepareStatement(updateSQL);

            // 4. Set the parameters
            preparedStatement.setString(1, first_name);
            preparedStatement.setString(2, email);
            preparedStatement.setString(3, phone_number);
            preparedStatement.setInt(4, customer_id);

            // 5. Execute the update
            int rowsUpdated = preparedStatement.executeUpdate();

            if (rowsUpdated > 0) {
                // The update was successful; retrieve the updated customer information
                updatedCustomer = getCustomer(customer_id);
            }

            preparedStatement.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return updatedCustomer;
    }
    public customers updateCustomerWithContactPerson(int customer_id, String first_name, String email, String phone_number,int contact_person_id) {
        customers updatedCustomer = null;
        try {
            // 1. Establish a database connection
            forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/application", "root", "");

            // 2. Create the SQL UPDATE statement
            String updateSQL = "UPDATE customers SET first_name = ?, email = ?, phone_number = ? WHERE customer_id = ?";

            // 3. Create a PreparedStatement with the SQL statement
            PreparedStatement preparedStatement = conn.prepareStatement(updateSQL);

            // 4. Set the parameters
            preparedStatement.setString(1, first_name);
            preparedStatement.setString(2, email);
            preparedStatement.setString(3, phone_number);
            preparedStatement.setInt(4, customer_id);

            // 5. Execute the update
            int rowsUpdated = preparedStatement.executeUpdate();

            if (rowsUpdated > 0) {
                // The update was successful; retrieve the updated customer information
                updatedCustomer = getCustomer(customer_id);
            }

            preparedStatement.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return updatedCustomer;
    }

//    public List<String> getContactPersons(int customer_id) {
//        List<String> contactPersons = new ArrayList<>();
//
//        try {
//            forName("com.mysql.jdbc.Driver");
//            // Create a connection to your database
//            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/application", "root", "");
//
//            // Create a PreparedStatement with the SQL query to retrieve contact persons
//            String sql = "SELECT person_name FROM contact_persons WHERE customer_id = ?";
//            PreparedStatement preparedStatement = connection.prepareStatement(sql);
//            preparedStatement.setInt(1, customer_id);
//
//            // Execute the query and get the result set
//            ResultSet resultSet = preparedStatement.executeQuery();
//
//            // Process the result and add contact person names to the list
//            while (resultSet.next()) {
//                String personName = resultSet.getString("person_name");
//                contactPersons.add(personName);
//            }
//
//            // Close the database connections
//            resultSet.close();
//            preparedStatement.close();
//            connection.close();
//        } catch (SQLException | ClassNotFoundException e) {
//            e.printStackTrace();
//        }
//
//        return contactPersons;
//    }


    public customers getCustomer(int customer_id) {
        customers customer = null;

        try {
            // Load the JDBC driver (assuming the driver class is available in your classpath)
            Class.forName("com.mysql.jdbc.Driver");

            // Create a connection to the database
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/application", "root", "");

            // Prepare a SQL query to retrieve the customer's details
            String customerQuery = "SELECT * FROM customers WHERE customer_id = ?";
            PreparedStatement customerStatement = con.prepareStatement(customerQuery);
            customerStatement.setInt(1, customer_id);

            // Execute the query to retrieve customer details
            ResultSet customerResultSet = customerStatement.executeQuery();

            if (customerResultSet.next()) {
                // Retrieve customer data from the result set
                String firstName = customerResultSet.getString("first_name");
                String email = customerResultSet.getString("email");
                String phone_number = customerResultSet.getString("phone_number");

                // Create a Customer object
                customer = new customers(customer_id, firstName, email, phone_number);

                // Prepare a SQL query to retrieve contact persons for the customer
                String contactPersonsQuery = "SELECT * FROM contact_persons WHERE customer_id = ?";
                PreparedStatement contactPersonsStatement = con.prepareStatement(contactPersonsQuery);
                contactPersonsStatement.setInt(1, customer_id);

                // Execute the query to retrieve contact persons
                ResultSet contactPersonsResultSet = contactPersonsStatement.executeQuery();

                List<String> contactPersons = new ArrayList<>();
                while (contactPersonsResultSet.next()) {
                    String personName = contactPersonsResultSet.getString("person_name");
                    contactPersons.add(personName);
                }

                // Set the contactPersons list in the customer object
                customer.setContactPersons(contactPersons);
            }

            // Serialize the customer object to JSON
            ObjectMapper objectMapper = new ObjectMapper();
            String json = objectMapper.writeValueAsString(customer);
            System.out.println(json);

            // Close the database connections
            customerResultSet.close();
            customerStatement.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return customer;
    }
    public List<customers> getAllCustomers() {
        List<customers> customersList = new ArrayList<>();

        try {
            // Load the JDBC driver (assuming the driver class is available in your classpath)
            Class.forName("com.mysql.jdbc.Driver");

            // Create a connection to the database
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/application", "root", "");

            // Prepare a SQL query to retrieve all customers
            String customerQuery = "SELECT * FROM customers";
            PreparedStatement customerStatement = con.prepareStatement(customerQuery);

            // Execute the query to retrieve all customers
            ResultSet customerResultSet = customerStatement.executeQuery();

            while (customerResultSet.next()) {
                int customer_id = customerResultSet.getInt("customer_id");
                String firstName = customerResultSet.getString("first_name");
                String email = customerResultSet.getString("email");
                String phone_number = customerResultSet.getString("phone_number");

                // Create a Customer object
                customers customer = new customers(customer_id, firstName, email, phone_number);

                // Prepare a SQL query to retrieve contact persons for the customer
                String contactPersonsQuery = "SELECT * FROM contact_persons where customer_id= ?";
                PreparedStatement contactPersonsStatement = con.prepareStatement(contactPersonsQuery);
                contactPersonsStatement.setInt(1, customer_id);

                // Execute the query to retrieve contact persons
                ResultSet contactPersonsResultSet = contactPersonsStatement.executeQuery();

                List<String> contactPersons = new ArrayList<>();
                while (contactPersonsResultSet.next()) {
                    String personName = contactPersonsResultSet.getString("person_name");
                    contactPersons.add(personName);
                }

                // Set the contactPersons list in the customer object
                customer.setContactPersons(contactPersons);

                // Add the customer to the list
                customersList.add(customer);
            }

            // Serialize the list of customers to JSON
            ObjectMapper objectMapper = new ObjectMapper();
            String json = objectMapper.writeValueAsString(customersList);
            System.out.println(json);

            // Close the database connections
            customerResultSet.close();
            customerStatement.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return customersList;
    }


}
