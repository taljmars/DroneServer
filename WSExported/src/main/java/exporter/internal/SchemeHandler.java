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
    protected boolean shouldRemoveClass(Class clz) {
        if (clz.getAnnotation(Component.class) != null)
            return true;

        return false;
    }

    protected boolean shouldRemoveAnnotation(Annotation annotation) {
        if (annotation.getTypeName().equals(Override.class.getName()))
            return false;

        return true;
    }

    public boolean shouldRemoveConstructor(CtConstructor ctConstructor) throws Exception {
        System.err.println("Checking " + ctConstructor.getName());

        if (ctConstructor.getParameterTypes().length == 0)
            return false;

        return true;
    }

    public boolean shouldRemoveMethod(CtMethod ctMethod) throws Exception {
        System.err.println("Checking " + ctMethod.getName());

        if (ctMethod.getAnnotation(XmlTransient.class) != null)
            return true;

        if (ctMethod.getAnnotation(Transient.class) != null)
            return true;

        String mName = ctMethod.getName();
        if (!(mName.startsWith("set") && mName.length() > "set".length()) &&
                !(mName.startsWith("get") && mName.length() > "get".length()) &&
                !(mName.startsWith("is") && mName.length() > "is".length()))
            return true;

        return false;
    }

}
