/*
 * Copyright (C) 2020 Grakn Labs
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package grakn.core.graph.graphdb.util;

import com.google.common.collect.Iterators;
import grakn.core.graph.graphdb.tinkerpop.optimize.HasStepFolder.OrderEntry;
import org.apache.tinkerpop.gremlin.process.traversal.step.util.ElementValueComparator;
import org.apache.tinkerpop.gremlin.structure.Element;
import org.apache.tinkerpop.gremlin.util.function.MultiComparator;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.TreeMap;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class MultiDistinctOrderedIterator<E extends Element> implements Iterator<E> {

    private final Map<Integer, Iterator<E>> iterators = new LinkedHashMap<>();
    private final Map<Integer, E> values = new LinkedHashMap<>();
    private final TreeMap<E, Integer> currentElements;
    private final Set<E> allElements = new HashSet<>();
    private final Integer limit;
    private long count = 0;

    public MultiDistinctOrderedIterator(Integer lowLimit, Integer highLimit, List<Iterator<E>> iterators, List<OrderEntry> orders) {
        this.limit = highLimit;
        Comparator<E> comparator = null;
        if (orders.isEmpty()) {
            final Stream<E> stream = StreamSupport.stream(Spliterators.spliteratorUnknownSize(Iterators.concat(iterators.iterator()), Spliterator.ORDERED), false);
            this.iterators.put(0, stream.iterator());
        } else {
            final List<Comparator<E>> comp = new ArrayList<>();
            orders.forEach(o -> comp.add(new ElementValueComparator(o.key, o.order)));
            comparator = new MultiComparator<>(comp);
            for (int i = 0; i < iterators.size(); i++) {
                this.iterators.put(i, iterators.get(i));
            }
        }
        currentElements = new TreeMap<>(comparator);
        long i = 0;
        while (i < lowLimit && this.hasNext()) {
            this.next();
            i++;
        }
    }

    @Override
    public boolean hasNext() {
        if (limit != null && count >= limit) {
            return false;
        }
        for (int i = 0; i < iterators.size(); i++) {
            if (!values.containsKey(i) && iterators.get(i).hasNext()){
                E element = null;
                do {
                    element = iterators.get(i).next();
                    if (allElements.contains(element)) {
                        element = null;
                    }
                } while (element == null && iterators.get(i).hasNext());
                if (element != null) {
                    values.put(i, element);
                    currentElements.put(element, i);
                    allElements.add(element);
                }
            }
        }
        return !values.isEmpty();
    }

    @Override
    public E next() {
        count++;
        return values.remove(currentElements.remove(currentElements.firstKey()));
    }

}
