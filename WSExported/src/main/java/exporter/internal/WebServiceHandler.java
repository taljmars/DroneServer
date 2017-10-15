package exporter.internal;

import javassist.CtConstructor;
import javassist.CtMethod;
import javassist.NotFoundException;
import javassist.bytecode.annotation.Annotation;

import javax.jws.WebMethod;
import javax.jws.WebService;

public class WebServiceHandler extends PluginHandler {

    public WebServiceHandler(String target) {
        super(target);
    }

    protected boolean shouldRemoveAnnotation(Annotation annotation) {
        if (annotation.getTypeName().equals(WebMethod.class.getName()) ||
                annotation.getTypeName().equals(WebService.class.getName()))
            return false;

        return true;
    }

    protected boolean shouldRemoveClass(Class clz) {
        if (clz.getAnnotation(WebService.class) == null)
            return true;

        if (clz.isInterface())
            return false;

        return true;
    }

    protected boolean shouldRemoveConstructor(CtConstructor ctConstructor) throws NotFoundException {
        System.err.println("Checking " + ctConstructor.getName());
        return true;
    }

    protected boolean shouldRemoveMethod(CtMethod ctMethod) throws ClassNotFoundException, NotFoundException {
        System.err.println("Checking " + ctMethod.getName());

        if (ctMethod.getAnnotation(WebMethod.class) != null ||
            ctMethod.getAnnotation(WebService.class) != null)
            return false;

        return true;
    }

}
