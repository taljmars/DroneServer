package com.db.persistence.objectStore;

import com.db.persistence.scheme.BaseObject;
import com.db.persistence.scheme.Sessionable;
import com.db.persistence.scheme.TargetType;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
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
    private Map<Class, ClzHolder> map;
    private List<Class> sortedManagedClasses;

    public ManagedClassTopologicalSorter() {
        unsortedManagedClasses = new ArrayList<>();
        sortedManagedClasses = new ArrayList<>();
        map = new HashMap<>();
    }

    public void mapClasses(EntityManager entityManager) {
        Metamodel mm = entityManager.getMetamodel();
        unsortedManagedClasses = new ArrayList<>();
        for (final ManagedType<?> managedType : mm.getManagedTypes()) {
            Class clz = managedType.getJavaType();
            LOGGER.error("Found managed class '" + clz.getCanonicalName() + "'");
            if (Modifier.isAbstract(clz.getModifiers()))
                continue;

            if (!BaseObject.class.isAssignableFrom(clz) || clz == BaseObject.class)
                continue;

            if (!clz.isAnnotationPresent(Sessionable.class))
                continue;

            unsortedManagedClasses.add(clz);
        }
    }

    private void buildGraph() {
        for (Class clz : unsortedManagedClasses) {
            ClzHolder clzHolder = map.get(clz);
            map.put(clz, new ClzHolder(clz));
        }

        for (Class clz : unsortedManagedClasses) {
            ClzHolder clzHolder = map.get(clz);
            FC fc = new FC(clzHolder);
            ReflectionUtils.doWithFields(clz, fc);
        }

        for (ClzHolder clzHolder : map.values()) {
            System.out.println(clzHolder);
        }
    }

    private void sort() {
        Set<ClzHolder> S = new HashSet<>();
        for(ClzHolder n : map.values()){
            if(n.in.size() == 0){
                S.add(n);
            }
        }

        //while S is non-empty do
        while(!S.isEmpty()){
            //remove a node n from S
            ClzHolder n = S.iterator().next();
            S.remove(n);

            //insert n into L
            sortedManagedClasses.add(n.node);

            //for each node m with an edge e from n to m do
            for(Iterator<ClzHolder> it = n.out.iterator();it.hasNext();){
                //remove edge e from the graph
                ClzHolder e = it.next();
                e.removeIn(n);
                it.remove();//Remove edge from n

                //if m has no other incoming edges then insert m into S
                if(e.in.isEmpty()){
                    S.add(e);
                }

            }
        }
        //Check to see if all edges are removed
        boolean cycle = false;
        for(ClzHolder n : map.values()){
            if(!n.in.isEmpty() || !n.out.isEmpty()){
                cycle = true;
                break;
            }
        }
        if (cycle) {
            System.out.println("Cycle present, topological sort not possible");
        }
        else {
            System.out.println("Topological Sort: "+Arrays.toString(sortedManagedClasses.toArray()));
        }

        Collections.reverse(sortedManagedClasses);
    }

    private class FC implements ReflectionUtils.FieldCallback {

        ClzHolder clzHolder;

        public FC(ClzHolder clzHolder) {
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

            ClzHolder clzHolder = map.get(clz);
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

    private class ClzHolder {
        Class node;
        Set<ClzHolder> in;
        Set<ClzHolder> out;

        public ClzHolder(Class node) {
            this.node = node;
            in = new HashSet<>();
            out = new HashSet<>();
        }

        public void addIn(ClzHolder holder) {
            in.add(holder);
        }

        public void addOut(ClzHolder holder) {
            out.add(holder);
        }

        public void removeIn(ClzHolder holder) {
            in.remove(holder);
        }

        public void removeOut(ClzHolder holder) {
            out.remove(holder);
        }

        public String toString() {
            return node.getSimpleName() + " IN:" + in.size() + " OUT:" + out.size();
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
