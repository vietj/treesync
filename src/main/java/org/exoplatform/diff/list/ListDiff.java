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

import java.util.Comparator;
import java.util.Iterator;

/**
 * @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a>
 */
public class ListDiff<L1, L2, E> {

   /** . */
   private static final int[] EMPTY = new int[0];

   /** . */
   final Comparator<E> comparator;

   /** . */
   final ListAdapter<L1, E> adapter1;

   /** . */
   final ListAdapter<L2, E> adapter2;

   /** . */
   int[] matrix;

   /** . */
   int m;

   /** . */
   int n;

   public ListDiff(ListAdapter<L1, E> adapter1, ListAdapter<L2, E> adapter2, Comparator<E> comparator) {
      this.adapter1 = adapter1;
      this.adapter2 = adapter2;
      this.comparator = comparator;
      this.matrix = EMPTY;
   }

   public ListDiff(ListAdapter<L1, E> adapter1, ListAdapter<L2, E> adapter2) {
      this(adapter1, adapter2, null);
   }

   boolean equals(E e1, E e2) {
      if (comparator == null) {
         return e1.equals(e2);
      } else {
         return comparator.compare(e1, e2) == 0;
      }
   }

   /**
    * Compute the LCS matrix from the specified offset. It updates the state of this object
    * with the relevant state. The LCS matrix is computed using the LCS algorithm
    * (see http://en.wikipedia.org/wiki/Longest_common_subsequence_problem).
    *
    * @param offset the offset
    * @param elements1 the elements 1
    * @param elements2 the elements 2
    */
   void lcs(int offset, L1 elements1, L2 elements2) {
      m = 1 + adapter1.size(elements1) - offset;
      n = 1 + adapter2.size(elements2) - offset;

      // Clean / adapt matrix
      int s = m * n;
      if (matrix.length < s) {
         matrix = new int[s];
      } else {
         for (int i = 0; i < m; i++) {
            matrix[i] = 0;
         }
         for (int j = 1; j < n; j++) {
            matrix[j * m] = 0;
         }
      }

      // Compute the lcs matrix
      Iterator<E> itI = adapter1.iterator(elements1, true);
      for (int i = 1; i < m; i++) {
         E abc = itI.next();
         Iterator<E> itJ = adapter2.iterator(elements2, true);
         for (int j = 1; j < n; j++) {
            int index = i + j * m;
            int v;
            E def = itJ.next();
            if (equals(abc, def)) {
               v = matrix[index - m - 1] + 1;
            } else {
               int v1 = matrix[index - 1];
               int v2 = matrix[index - m];
               v = v1 < v2 ? v2 : v1;
            }
            matrix[index] = v;
         }
      }
   }

   public final ListChangeIterator<L1, L2, E> iterator(L1 elements1, L2 elements2) {
      return new ListChangeIterator<L1, L2, E>(this, elements1, elements2);
   }

   @Override
   public String toString() {
      StringBuilder sb = new StringBuilder();
      for (int i = 0; i < m; i++) {
         sb.append('[');
         for (int j = 0; j < n; j++) {
            if (j > 0) {
               sb.append(',');
            }
            sb.append(matrix[i + j * m]);
         }
         sb.append("]\n");
      }
      return sb.toString();
   }
}
