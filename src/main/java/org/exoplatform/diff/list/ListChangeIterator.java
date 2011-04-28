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

package org.exoplatform.diff.list;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Iterates over a list of {@link ListChangeType} computed from two list of  objects. The implementation
 * is optimized to use the LCS algorithm only when needed, for trivial list no LCS computation should be
 * required.
 *
 * @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a>
 */
public class ListChangeIterator<L1, L2, E> implements Iterator<ListChangeType> {

   /** . */
   private static final int TRIVIAL_MODE = 0;

   /** . */
   private static final int LCS_MODE = 1;

   /** . */
   ListDiff<L1, L2, E> diff;

   /** . */
   private final L1 elements1;

   /** . */
   private final L2 elements2;

   /** . */
   private final Iterator<E> it1;

   /** . */
   private final Iterator<E> it2;

   /** . */
   private int index1;

   /** . */
   private int index2;

   /** . */
   private E next1;

   /** . */
   private E next2;

   /** . */
   private E element;

   /** . */
   private ListChangeType type;

   /** . */
   private int mode;

   /** . */
   private boolean buffered;

   ListChangeIterator(ListDiff<L1, L2, E> diff, L1 elements1, L2 elements2) {
      this.diff = diff;
      this.elements1 = elements1;
      this.elements2 = elements2;
      this.it1 = diff.adapter1.iterator(elements1, false);
      this.it2 = diff.adapter2.iterator(elements2, false);
      this.mode = TRIVIAL_MODE;

      //
      this.index1 = 0;
      this.index2 = 0;
      this.buffered = false;
      this.next1 = null;
      this.next2 = null;
      this.type = null;
      this.element = null;

      //
      if (it1.hasNext()) {
         next1 = it1.next();
      }
      if (it2.hasNext()) {
         next2 = it2.next();
      }
   }

   private void next1() {
      index1++;
      if (it1.hasNext()) {
         next1 = it1.next();
      } else {
         next1 = null;
      }
   }

   private void next2() {
      index2++;
      if (it2.hasNext()) {
         next2 = it2.next();
      } else {
         next2 = null;
      }
   }

   public boolean hasNext() {

      while (!buffered) {
         if (mode == TRIVIAL_MODE) {
            if (next1 != null) {
               if (next2 != null) {
                  if (diff.equals(next1, next2)) {
                     type = ListChangeType.SAME;
                     element = next1;
                     buffered = true;
                     next1();
                     next2();
                  } else {
                     diff.lcs(index1, elements1, elements2);
                     mode = LCS_MODE;
                  }
               } else {
                  type = ListChangeType.REMOVE;
                  element = next1;
                  buffered = true;
                  next1();
               }
            } else {
               if (next2 != null) {
                  type = ListChangeType.ADD;
                  element = next2;
                  buffered = true;
                  next2();
               } else {
                  // Force a break with buffered to false
                  break;
               }
            }
         } else if (mode == LCS_MODE) {
            E elt1 = null;
            E elt2 = null;
            int i = diff.adapter1.size(elements1) - index1;
            int j = diff.adapter2.size(elements2) - index2;
            if (i > 0 && j > 0 && diff.equals(elt1 = next1, elt2 = next2)) {
               type = ListChangeType.SAME;
               element = elt1;
               next1();
               next2();
               buffered = true;
            } else {
               int index1 = i + (j - 1) * diff.m;
               int index2 = i - 1 + j * diff.m;
               if (j > 0 && (i == 0 || diff.matrix[index1] >= diff.matrix[index2])) {
                  type = ListChangeType.ADD;
                  element = elt2 == null ? next2 : elt2;
                  next2();
                  buffered = true;
               } else if (i > 0 && (j == 0 || diff.matrix[index1] < diff.matrix[index2])) {
                  type = ListChangeType.REMOVE;
                  element = elt1 == null ? next1 : elt1;
                  next1();
                  buffered = true;
               } else {
                  // Force a break with buffered to false
                  break;
               }
            }
         } else {
            throw new AssertionError();
         }
      }

      //
      return buffered;
   }

   public ListChangeType next() {
      if (!hasNext()) {
         throw new NoSuchElementException();
      } else {
         buffered = false;
         return type;
      }
   }

   public void remove() {
      throw new UnsupportedOperationException();
   }

   public E getElement() {
      return element;
   }

   public int getIndex1() {
      return index1;
   }

   public int getIndex2() {
      return index2;
   }
}


