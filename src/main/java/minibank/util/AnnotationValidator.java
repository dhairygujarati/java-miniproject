package minibank.util;
import minibank.model.annotation.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
public final class AnnotationValidator {
    private AnnotationValidator(){}
    public static String[] validate(Object obj){
        if(obj==null) return new String[]{"Object cannot be null"};
        List<String> errors=new ArrayList<>();
        for(Class<?> type=obj.getClass(); type!=null; type=type.getSuperclass()) for(Field f:type.getDeclaredFields()){
            try{
                f.setAccessible(true); Object value=f.get(obj);
                if(f.isAnnotationPresent(Positive.class) && value instanceof Number n && n.longValue()<=0)
                    errors.add(f.getName()+": "+f.getAnnotation(Positive.class).message());
                if(f.isAnnotationPresent(MaxLength.class) && value instanceof String s && s.length()>f.getAnnotation(MaxLength.class).value())
                    errors.add(f.getName()+": maximum length is "+f.getAnnotation(MaxLength.class).value());
            }catch(ReflectiveOperationException|RuntimeException e){errors.add("Could not validate "+f.getName());}
        }
        return errors.toArray(String[]::new);
    }
}
