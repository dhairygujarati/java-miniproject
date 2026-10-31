package lab03;
import java.util.Objects;
public class Lab03Demo {
    static class Account {
        private final String number, owner; private final long balance;
        Account(String number,String owner,long balance){this.number=number;this.owner=owner;this.balance=balance;}
        @Override public String toString(){return number+" | "+owner+" | "+balance;}
        @Override public boolean equals(Object o){return o instanceof Account a && number.equals(a.number);}
        @Override public int hashCode(){return Objects.hash(number);}
    }
    static class Customer implements Cloneable {
        private final String name; private final Address address;
        Customer(String name,Address address){this.name=name;this.address=address;}
        static class Address { final String line,city,pincode; Address(String l,String c,String p){line=l;city=c;pincode=p;} }
        @Override public Customer clone(){return new Customer(name,new Address(address.line,address.city,address.pincode));}
    }
    public static void main(String[] args){Account a=new Account("AC0001","User",500);System.out.println(a);System.out.println("Account instance: "+(a instanceof Account));}
}
