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
        return !annotation.getTypeName().equals(Override.class.getName());
    }

    @SuppressWarnings("unchecked")
    protected boolean shouldRemoveClass(Class clz) {
        return clz.isAssignableFrom(Exception.class);
    }

    protected boolean shouldRemoveConstructor(CtConstructor ctConstructor) {
//        System.err.println("Checking " + ctConstructor.getName());
        return false;
    }

    protected boolean shouldRemoveMethod(CtMethod ctMethod) {
//        System.err.println("Checking " + ctMethod.getName());
        return false;
    }

}
