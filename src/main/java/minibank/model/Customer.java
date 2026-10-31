package minibank.model;
import minibank.model.annotation.MaxLength;
import java.io.Serializable;
import java.util.Objects;

public class Customer implements Serializable, Cloneable {
    private static final long serialVersionUID = 1L;
    private static long customerCounter = 100;
    private final String customerId;
    @MaxLength(80) private final String name;
    @MaxLength(120) private final String email;
    private final String mobile;
    private final Address address;

    public Customer(String name, String email, String mobile, Address address) {
        this.customerId = generateCustomerId();
        this.name = Objects.requireNonNull(name);
        this.email = Objects.requireNonNull(email);
        this.mobile = Objects.requireNonNull(mobile);
        this.address = address;
    }
    private static synchronized String generateCustomerId() { return "CUST" + (++customerCounter); }
    public String getCustomerId(){ return customerId; }
    public String getName(){ return name; }
    public String getEmail(){ return email; }
    public String getMobile(){ return mobile; }
    public Address getAddress(){ return address; }
    @Override public Customer clone() {
        return new Customer(name, email, mobile, address == null ? null : new Address(address.line, address.city, address.pincode));
    }
    public static class Address implements Serializable {
        private static final long serialVersionUID = 1L;
        private final String line, city, pincode;
        public Address(String line, String city, String pincode) { this.line=line; this.city=city; this.pincode=pincode; }
        public String getLine(){return line;} public String getCity(){return city;} public String getPincode(){return pincode;}
        @Override public String toString(){ return line + ", " + city + " - " + pincode; }
    }
    @Override public String toString(){ return customerId+" | "+name+" | "+email+" | "+mobile+(address==null?"":" | "+address); }
}
