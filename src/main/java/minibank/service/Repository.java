package minibank.service;
import minibank.model.annotation.Id;
import java.lang.reflect.Field;
import java.util.*;
public class Repository<T> {
    private final Map<String,T> items=new HashMap<>();
    public T save(T entity){
        Objects.requireNonNull(entity,"Entity is required.");
        try{
            for(Class<?> type=entity.getClass(); type!=null; type=type.getSuperclass()) {
                for(Field f:type.getDeclaredFields()) if(f.isAnnotationPresent(Id.class)){
                    f.setAccessible(true); Object id=f.get(entity);
                    if(id==null||id.toString().isBlank()) throw new IllegalArgumentException("@Id field is empty.");
                    items.put(id.toString(),entity); return entity;
                }
            }
            throw new IllegalArgumentException("No @Id field found on "+entity.getClass().getSimpleName());
        }catch(IllegalAccessException e){throw new IllegalStateException("Cannot read @Id field.",e);}
    }
    @SafeVarargs public final void saveAll(T... entities){for(T entity:entities)save(entity);}
    public T findById(String id){return items.get(id);}
    public ArrayList<T> findAll(){return new ArrayList<>(items.values());}
    public boolean delete(String id){return items.remove(id)!=null;}
    public int size(){return items.size();}
    public static <N extends Number> double average(N[] values){if(values==null||values.length==0)return 0;double sum=0;for(N n:values)sum+=n.doubleValue();return sum/values.length;}
}
