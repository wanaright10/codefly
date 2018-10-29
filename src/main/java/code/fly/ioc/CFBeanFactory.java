package code.fly.ioc;

import code.fly.ioc.annotations.CFBean;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CFBeanFactory {
    private CFBeanFactory() {
    }

    private static Map<String, Object> beanPool = new ConcurrentHashMap<>();
    private static final Class<CFBean> annotation = CFBean.class;

    @SuppressWarnings("unchecked")
    public static <T> T getBean(String beanName, Class<T> clazz) {
        T bean = ((T) beanPool.get(beanName));
        if (bean == null) {
            if (clazz.isAnnotationPresent(annotation)) {
                CFBean classAnnotation = clazz.getDeclaredAnnotation(annotation);
                String declaredClassName = classAnnotation.value();
                if (declaredClassName.equals(CFBean.DEFAULT)) {
                    declaredClassName = clazz.getSimpleName();
                }

                if (declaredClassName.equals(beanName)) {
                    try {
                        bean = clazz.newInstance();

                        Field[] fields = clazz.getDeclaredFields();
                        for (Field field : fields) {
                            if (field.isAnnotationPresent(annotation)) {
                                field.setAccessible(true);
                                CFBean fieldAnnotation = field.getAnnotation(annotation);
                                String declaredTypeName = fieldAnnotation.value();
                                if (declaredTypeName.equals(CFBean.DEFAULT)) {
                                    declaredTypeName = field.getType().getSimpleName();
                                }

                                field.set(bean, getBean(declaredTypeName, field.getType()));
                            }
                        }

                        beanPool.put(beanName, bean);
                    } catch (InstantiationException | IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
        return bean;
    }

    public static <T> T getBean(Class<T> clazz) {
        return getBean(clazz.getSimpleName(), clazz);
    }

    public static void addBean(String beanName, Object bean) {
        beanPool.put(beanName, bean);
    }
}
