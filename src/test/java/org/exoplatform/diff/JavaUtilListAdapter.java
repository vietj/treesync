/*
 * Copyright (C) 2011 eXo Platform SAS.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.exoplatform.diff;

import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a>
 */
public class JavaUtilListAdapter<E> implements ListAdapter<List<E>, E> {

   public int size(List<E> list) {
      return list.size();
   }

   public Iterator<E> iterator(List<E> list, boolean reverse) {
      if (reverse) {
         final ListIterator<E> it = list.listIterator(list.size());
         return new Iterator<E>() {
            public boolean hasNext() {
               return it.hasPrevious();
            }
            public E next() {
               return it.previous();
            }
            public void remove() {
               throw new UnsupportedOperationException();
            }
         };
      } else {
         return list.iterator();
      }
   }
}
