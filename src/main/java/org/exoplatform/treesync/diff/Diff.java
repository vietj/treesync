/*
 * Copyright (C) 2010 eXo Platform SAS.
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

package org.exoplatform.treesync.diff;

import org.exoplatform.treesync.SyncContext;
import org.exoplatform.treesync.SyncModel;

import java.util.Comparator;

/**
 * @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a>
 * @version $Revision$
 */
public class Diff<N1, N2, H> {

   /** . */
   final SyncModel<N1, H> model1;

   /** . */
   final SyncModel<N2, H> model2;

   /** . */
   final Comparator<H> comparator;

   public Diff(SyncModel<N1, H> model1, SyncModel<N2, H> model2, Comparator<H> comparator) {
      this.model1 = model1;
      this.model2 = model2;
      this.comparator = comparator;
   }

   public DiffChangeIterator<N1, N2, H> perform(N1 node1, N2 node2) {
      return new DiffChangeIterator<N1, N2, H>(this, new SyncContext<N1, H>(model1, node1), new SyncContext<N2, H>(model2, node2));
   }
}
