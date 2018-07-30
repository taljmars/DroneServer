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
        return !annotation.getTypeName().equals(WebMethod.class.getName()) &&
                !annotation.getTypeName().equals(WebService.class.getName());
    }

    @SuppressWarnings("unchecked")
    protected boolean shouldRemoveClass(Class clz) {
        if (clz.getAnnotation(WebService.class) == null)
            return true;

        return !clz.isInterface();
    }

    protected boolean shouldRemoveConstructor(CtConstructor ctConstructor) {
//        System.err.println("Checking " + ctConstructor.getName());
        return true;
    }

    protected boolean shouldRemoveMethod(CtMethod ctMethod) throws ClassNotFoundException {
//        System.err.println("Checking " + ctMethod.getName());

        return ctMethod.getAnnotation(WebMethod.class) == null &&
                ctMethod.getAnnotation(WebService.class) == null;
    }

}
