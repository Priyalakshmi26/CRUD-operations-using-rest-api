import java.util.*;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
public class customers {
    private int customer_id;
    private String first_name;
    private String email;
    private String phone_number;
    private List<String> contactPersons;

    public customers(String first_name, String email, String phone_number) {
        //this.customer_id=customer_id;
        this.first_name = first_name;
        this.email = email;
        this.phone_number = phone_number;
    }

    public customers(int customer_id, String first_name, String email, String phone_number) {
        this.customer_id = customer_id;
        this.first_name = first_name;
        this.email = email;
        this.phone_number = phone_number;
    }

    public customers(int customer_id, String first_name, String email, String phone_number, List<String> contactPersons) {
        this.customer_id = customer_id;
        this.first_name = first_name;
        this.email = email;
        this.phone_number = phone_number;
        this.contactPersons = contactPersons;
    }



    public customers(int customerId, String firstName, String email, String phoneNumber, ArrayList<Object> objects) {
    }

    public String getFirst_name() {
        return first_name;
    }
    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone_number() {
        return phone_number;
    }
    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }
    public List<String> getContactPersons() {
        return contactPersons;
    }

    public void setContactPersons(List<String> contactPersons) {
        this.contactPersons = contactPersons;
    }


}
