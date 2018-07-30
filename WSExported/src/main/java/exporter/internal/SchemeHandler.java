package exporter.internal;

import javassist.CtConstructor;
import javassist.CtMethod;
import javassist.bytecode.annotation.Annotation;
import org.springframework.stereotype.Component;

import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlTransient;

public class SchemeHandler extends PluginHandler {

    public SchemeHandler(String target) {
        super(target);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected boolean shouldRemoveClass(Class clz) {
        return clz.getAnnotation(Component.class) != null;

    }

    protected boolean shouldRemoveAnnotation(Annotation annotation) {
        return !annotation.getTypeName().equals(Override.class.getName());
    }

    public boolean shouldRemoveConstructor(CtConstructor ctConstructor) throws Exception {
//        System.err.println("Checking " + ctConstructor.getName());

        return ctConstructor.getParameterTypes().length != 0;
    }

    public boolean shouldRemoveMethod(CtMethod ctMethod) throws Exception {
//        System.err.println("Checking " + ctMethod.getName());

        if (ctMethod.getAnnotation(XmlTransient.class) != null)
            return true;

        if (ctMethod.getAnnotation(Transient.class) != null)
            return true;

        String mName = ctMethod.getName();
        return !(mName.startsWith("set") && mName.length() > "set".length()) &&
                !(mName.startsWith("get") && mName.length() > "get".length()) &&
                !(mName.startsWith("is") && mName.length() > "is".length());

    }

}
