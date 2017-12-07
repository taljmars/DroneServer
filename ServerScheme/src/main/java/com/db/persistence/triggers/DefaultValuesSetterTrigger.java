package com.db.persistence.triggers;

import com.db.persistence.scheme.BaseObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;

import java.beans.PropertyEditor;
import java.beans.PropertyEditorManager;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class DefaultValuesSetterTrigger extends UpdateObjectTriggerImpl {

    private final static Logger logger = Logger.getLogger(DefaultValuesSetterTrigger.class);

    @Override
    @SuppressWarnings("unchecked")
    public <T extends BaseObject> void handleUpdateObject(T oldInst, T newInst, UpdateTrigger.PHASE phase) throws Exception {
        if (!phase.equals(UpdateTrigger.PHASE.PRE_PERSIST))
            return;

        logger.debug("Setting default values for: " + newInst.getClass().getSimpleName());

        Class<T> clz = (Class<T>) newInst.getClass();

        Method[] methods = clz.getMethods();
        for (Method method : methods) {
            if (!method.getName().startsWith("set") && !method.getName().startsWith("get"))
                continue;

            Value value = method.getAnnotation(Value.class);
            if (value == null || value.value() == null || value.value().isEmpty())
                continue;

            if (method.getName().startsWith("set")) {
                method.invoke(newInst, convert(method.getParameterTypes()[0] ,value.value()));
                logger.debug("Updating field by method named '" + method.getName() + "'");
                continue;
            }

            if (method.getName().startsWith("get")) {
                String paramName = method.getName().substring("get".length());
                paramName = paramName.substring(0,1).toLowerCase() + paramName.substring(1);
                Field field = clz.getDeclaredField(paramName);
                field.setAccessible(true);
                field.set(newInst, convert(field.getType(), value.value()));
                logger.debug("Updating field named '" + paramName + "'");
                continue;
            }
        }

        for (Field field : clz.getDeclaredFields()) {
            Value value = field.getAnnotation(Value.class);
            if (value == null || value.value() == null || value.value().isEmpty())
                continue;

            logger.debug("Updating field named '" + field.getName() + "'");
            field.setAccessible(true);
            field.set(newInst, value.value());
        }
    }

    // TODO: Learn more about this one
    private Object convert(Class<?> targetType, String text) {
        /* Getting the property editor of a type, than set it value via String */
        PropertyEditor editor = PropertyEditorManager.findEditor(targetType);
        editor.setAsText(text);
        return editor.getValue();
    }

}
