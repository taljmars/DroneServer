package exporter.internal;

import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtMethod;
import javassist.NotFoundException;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.ClassFile;
import javassist.bytecode.annotation.Annotation;

public abstract class PluginHandler implements Filter {

    protected String targetDirectory;

    public PluginHandler(String target) {
        targetDirectory = target;
    }

    @Override
    public boolean filter(Class c) {
        try {
            CtClass ctClass = Utils.getPool().getCtClass(c.getCanonicalName());
            if (ctClass.isFrozen())
                ctClass.defrost();
            ClassFile cf = ctClass.getClassFile();

            // Clean class
            if (shouldRemoveClass(c))
                return false;

            handleAnnotations((AnnotationsAttribute) cf.getAttribute(AnnotationsAttribute.visibleTag));

            // Clean Constructors
            CtConstructor[] ctConstructors = ctClass.getConstructors();
            for (CtConstructor ctConstructor : ctConstructors) {
                if (shouldRemoveConstructor(ctConstructor)) {
                    ctClass.defrost();
                    ctClass.removeConstructor(ctConstructor);
                }
                else {
                    handleAnnotations((AnnotationsAttribute) ctConstructor.getMethodInfo().getAttribute(AnnotationsAttribute.visibleTag));
                }
            }

            // Clean methods
            CtMethod[] ctMethods = ctClass.getDeclaredMethods();
            for (CtMethod ctMethod : ctMethods) {
                if (shouldRemoveMethod(ctMethod)) {
                    ctClass.defrost();
                    ctClass.removeMethod(ctMethod);
                }
                else {
                    // Clean methods
                    handleAnnotations((AnnotationsAttribute) ctMethod.getMethodInfo().getAttribute(AnnotationsAttribute.visibleTag));
                }
            }

            ctClass.writeFile(targetDirectory);
            return true;
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    protected abstract boolean shouldRemoveClass(Class clz);

    protected abstract boolean shouldRemoveConstructor(CtConstructor ctConstructor) throws NotFoundException, Exception;

    protected abstract boolean shouldRemoveMethod(CtMethod ctMethod) throws Exception;

    protected abstract boolean shouldRemoveAnnotation(Annotation annotation);

    private void handleAnnotations(AnnotationsAttribute annotationsAttribute) {
        if (annotationsAttribute != null) {
            Annotation[] annotations = annotationsAttribute.getAnnotations();
            for (Annotation annotation : annotations) {
                if (shouldRemoveAnnotation(annotation))
                    annotationsAttribute.removeAnnotation(annotation.getTypeName());
            }
        }
    }
}
