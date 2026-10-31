package lab13;
import java.lang.annotation.*;import java.lang.reflect.*;import java.util.*;
public class Lab13Demo {
    @Retention(RetentionPolicy.RUNTIME) @Target(ElementType.FIELD) @interface Id {}
    static class Account { @Id private final String accountNumber; Account(String n){accountNumber=n;} public String toString(){return accountNumber;} }
    static class Repository<T> {private final Map<String,T> items=new HashMap<>();T save(T x){try{for(Field f:x.getClass().getDeclaredFields())if(f.isAnnotationPresent(Id.class)){f.setAccessible(true);items.put(String.valueOf(f.get(x)),x);return x;}throw new IllegalArgumentException("No @Id");}catch(IllegalAccessException e){throw new IllegalStateException(e);}}T findById(String id){return items.get(id);}ArrayList<T> findAll(){return new ArrayList<>(items.values());}boolean delete(String id){return items.remove(id)!=null;}@SafeVarargs final void saveAll(T... xs){for(T x:xs)save(x);}static <N extends Number> double average(N[] xs){return Arrays.stream(xs).mapToDouble(Number::doubleValue).average().orElse(0);}}
    public static void main(String[] args){Repository<Account> r=new Repository<>();Account a=new Account("AC0001");r.save(a);System.out.println("Repository ready; CRUD methods are demonstrated by source.");}
}
