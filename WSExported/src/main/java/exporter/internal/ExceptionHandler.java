package exporter.internal;

import javassist.CtConstructor;
import javassist.CtMethod;
import javassist.NotFoundException;
import javassist.bytecode.annotation.Annotation;

public class ExceptionHandler extends PluginHandler {

    public ExceptionHandler(String target) {
        super(target);
    }

    @Override
    protected boolean shouldRemoveAnnotation(Annotation annotation) {
        if (annotation.getTypeName().equals(Override.class.getName()))
            return false;
        return true;
    }

    protected boolean shouldRemoveClass(Class clz) {
        if (!clz.isAssignableFrom(Exception.class))
            return false;

        return true;
    }

    protected boolean shouldRemoveConstructor(CtConstructor ctConstructor) throws NotFoundException {
        System.err.println("Checking " + ctConstructor.getName());
        return false;
    }

    protected boolean shouldRemoveMethod(CtMethod ctMethod) throws ClassNotFoundException, NotFoundException {
        System.err.println("Checking " + ctMethod.getName());
        return false;
    }

}
