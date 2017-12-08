package com.db.persistence.objectStore;

import com.db.persistence.scheme.BaseObject;
import com.db.persistence.scheme.Sessionable;
import com.db.persistence.scheme.TargetType;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;

import javax.persistence.EntityManager;
import javax.persistence.metamodel.ManagedType;
import javax.persistence.metamodel.Metamodel;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

@Component
public class ManagedClassTopologicalSorter {

    private final static Logger LOGGER = Logger.getLogger(ManagedClassTopologicalSorter.class);

    private List<Class> unsortedManagedClasses;
    private Map<Class, ClzTreeNode> map;
    private List<Class> sortedManagedClasses;

    public ManagedClassTopologicalSorter() {
        unsortedManagedClasses = new ArrayList<>();
        sortedManagedClasses = new ArrayList<>();
        map = new HashMap<>();
    }

    /**
     * The following function map the managed class in the JPA using one
     * of the entity manager received as a parameter
     *
     * @param entityManager to extract the JPA entities
     */
    private void mapClasses(EntityManager entityManager) {
        Metamodel mm = entityManager.getMetamodel();
        unsortedManagedClasses = new ArrayList<>();
        for (final ManagedType<?> managedType : mm.getManagedTypes()) {
            Class clz = managedType.getJavaType();
            if (Modifier.isAbstract(clz.getModifiers()))
                continue;

            if (!BaseObject.class.isAssignableFrom(clz) || clz == BaseObject.class)
                continue;

            if (!clz.isAnnotationPresent(Sessionable.class))
                continue;

            LOGGER.debug("Found managed class '" + clz.getCanonicalName() + "'");
            unsortedManagedClasses.add(clz);
        }
    }

    /**
     * The following function build a class tree. each node-child it placed
     * has a parant if the parent class have one (or more) field(s) of the type.
     */
    private void buildGraph() {
        for (Class clz : unsortedManagedClasses) {
            ClzTreeNode clzHolder = map.get(clz);
            map.put(clz, new ClzTreeNode(clz));
        }

        for (Class clz : unsortedManagedClasses) {
            ClzTreeNode clzHolder = map.get(clz);
            ClzNodeTreeBuilder clzBuilder = new ClzNodeTreeBuilder(clzHolder);
            ReflectionUtils.doWithFields(clz, clzBuilder);
        }

        LOGGER.debug("Unsorted Classes:");
        map.values().stream().forEach(e -> LOGGER.debug(e));
    }

    /**
     * Topological sorted of the class
     */
    private void sort() {
        Set<ClzTreeNode> S = new HashSet<>();
        for (ClzTreeNode clzHolder : map.values()) {
            // Setting roots
            if(clzHolder.in.size() == 0)
                S.add(clzHolder);
        }

        // Starting to map from the root
        while (!S.isEmpty()) {

            // Pop the first root
            ClzTreeNode root = S.iterator().next();
            S.remove(root);

            // Push to sorted
            sortedManagedClasses.add(root.clz);

            // For each root with an pointer to e
            for (Iterator<ClzTreeNode> it = root.out.iterator(); it.hasNext();) {
                // Remove pointer
                ClzTreeNode e = it.next();
                e.removeIn(root);
                it.remove(); // Remove edge

                // the pointed clz don't have anyone pointing to him than it is a root
                if(e.in.isEmpty())
                    S.add(e);
            }
        }

        // Check for cycles
        boolean foundCycle = false;
        for (ClzTreeNode clzHolder : map.values()){
            if (!clzHolder.in.isEmpty() || !clzHolder.out.isEmpty()) {
                foundCycle = true;
                break;
            }
        }

        Assert.isTrue(!foundCycle, "Cycle present, topological sort not possible");
        LOGGER.debug("Publish order was set");
        sortedManagedClasses.stream().forEach(e -> LOGGER.debug(e));
        Collections.reverse(sortedManagedClasses);
    }

    private class ClzNodeTreeBuilder implements ReflectionUtils.FieldCallback {

        private ClzTreeNode clzHolder;

        public ClzNodeTreeBuilder(ClzTreeNode clzHolder) {
            this.clzHolder = clzHolder;
        }

        @Override
        public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException {
            LOGGER.debug("Check field " + field + " of class " + this.clzHolder);
            Annotation annotation = field.getAnnotation(TargetType.class);
            if (annotation == null)
                return;

            Class clz = ((TargetType) annotation).clz();
            if (clz == null)
                return;

            ClzTreeNode clzHolder = map.get(clz);
            if (clzHolder == null) {
                for (Class clazz : map.keySet()) {
                    if (clz.isAssignableFrom(clazz) && clz != clazz) {
                        clzHolder = map.get(clazz);
                        this.clzHolder.addOut(clzHolder);
                        clzHolder.addIn(this.clzHolder);
                    }
                }
            }
            else {
                this.clzHolder.addOut(clzHolder);
                clzHolder.addIn(this.clzHolder);
            }
        }
    }

    private class ClzTreeNode {

        private Class clz;
        private Set<ClzTreeNode> in;
        private Set<ClzTreeNode> out;

        public ClzTreeNode(Class node) {
            this.clz = node;
            in = new HashSet<>();
            out = new HashSet<>();
        }

        public void addIn(ClzTreeNode holder) {
            in.add(holder);
        }

        public void addOut(ClzTreeNode holder) {
            out.add(holder);
        }

        public void removeIn(ClzTreeNode holder) {
            in.remove(holder);
        }

        public void removeOut(ClzTreeNode holder) {
            out.remove(holder);
        }

        public String toString() {
            return clz.getSimpleName() + " IN:" + in.size() + " OUT:" + out.size();
        }
    }

    public List<Class> getManagedClasses(EntityManager entityManager) {
        if (sortedManagedClasses == null || sortedManagedClasses.isEmpty()) {
            LOGGER.debug("Build topological publish graph");
            mapClasses(entityManager);
            buildGraph();
            sort();
        }
        return this.sortedManagedClasses;
    }
}
