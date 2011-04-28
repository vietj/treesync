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

import org.exoplatform.diff.list.ListAdapter;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a>
 */
public class ArrayAdapter<E> implements ListAdapter<E[], E> {

   public int size(E[] list) {
      return list.length;
   }

   public Iterator<E> iterator(final E[] list, boolean reverse) {
      if (reverse) {
         return new Iterator<E>() {
            int count = list.length;
            public boolean hasNext() {
               return count > 0;
            }
            public E next() {
               if (!hasNext()) {
                  throw new NoSuchElementException();
               } else {
                  return list[--count];
               }
            }
            public void remove() {
               throw new UnsupportedOperationException();
            }
         };
      } else {
         return new Iterator<E>() {
            int count = 0;
            public boolean hasNext() {
               return count < list.length;
            }
            public E next() {
               if (!hasNext()) {
                  throw new NoSuchElementException();
               } else {
                  return list[count++];
               }
            }
            public void remove() {
               throw new UnsupportedOperationException();
            }
         };
      }
   }
}
