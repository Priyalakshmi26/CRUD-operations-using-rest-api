# CRUD-operations-using-rest-api

# To get the details api(including subresource):
http://localhost:8080/Application1/api/v1/customers/

# To create a new customer:
curl -X POST http://localhost:8080/Application1/api/v1/customers/ -H "Content-Type: application/json" -d '{
        "name": "John Doe",
        "email": "john@example.com",
        "phone_number": "123-456-7890"
        }'

# To create a new customer with contact person
curl -X POST http://localhost:8080/Application1/api/v1/customers/customerid/contact_persons -H "Content-Type: application/json" -d '{
        "name": "John Doe",
        "email": "john@example.com",
        "phone_number": "123-456-7890","person_name":"priya"
        }'
        
# To update a existing customer
 curl -X PUT http://localhost:8080/Application1/api/v1/customers/customerid -H "Content-Type: application/json" -d '{
           "customer_id":8,
         "first_name": "New Name",
         "email":"new@gmail.com",
         "phone_number": "7384792374"
         }'
# To delete a customer
curl -X DELETE http://localhost:8080/Application1/api/v1/customers/customerid

# To delete a contact person 
curl -X DELETE http://localhost:8080/Application1/api/v1/customers/customerid/contact_persons/contactpersonid
