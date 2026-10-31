package lab07;
import java.lang.annotation.*;import java.lang.reflect.Field;import java.util.*;
public class Lab07Demo {
    @Retention(RetentionPolicy.RUNTIME) @Target(ElementType.FIELD) @interface Positive {String message() default "must be > 0";}
    static class Account { @Positive private final long balance; Account(long b){balance=b;} }
    static String[] validate(Object o){List<String> errors=new ArrayList<>();for(Field f:o.getClass().getDeclaredFields())if(f.isAnnotationPresent(Positive.class)){try{f.setAccessible(true);if(((Number)f.get(o)).longValue()<=0)errors.add(f.getName()+": "+f.getAnnotation(Positive.class).message());}catch(ReflectiveOperationException e){errors.add("Validation failed");}}return errors.toArray(String[]::new);}
    public static void main(String[] args){System.out.println(Arrays.toString(validate(new Account(-100))));}
}
