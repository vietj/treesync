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

package org.exoplatform.treesync;

import org.exoplatform.treesync.lcs.ChangeIterator;
import org.exoplatform.treesync.lcs.LCS;

import java.util.Iterator;
import java.util.List;
import java.util.RandomAccess;

/**
 * @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a>
 * @version $Revision$
 */
public class Diff<N1, N2> {

  /** . */
  private final NodeContext<N1> context1;

  /** . */
  private final NodeContext<N2> context2;

  public Diff(NodeModel<N1> model1, N1 root1, NodeModel<N2> model2, N2 root2) {
    this.context1 = new NodeContext<N1>(model1, root1);
    this.context2 = new NodeContext<N2>(model2, root2);
  }

  public void perform(DiffHandler<N1, N2> handler) {
    perform(context1.root, context2.root, handler);
  }

  private void perform(N1 node1, N2 node2, DiffHandler<N1, N2> handler) {

    handler.enter(node1);

    String id1 = context1.model.getId(node1);
    String id2 = context2.model.getId(node2);

    // First check equality
    if (!id1.equals(id2)) {
      //
    }

    //

    //
    List<N1> children1 = context1.model.getChildren(node1);
    String[] childrenIds1 = ids(context1.model.getChildren(node1), context1.model);
    Iterator<N1> it1 = children1.iterator();
    List<N2> children2 = context2.model.getChildren(node2);
    String[] childrenIds2 = ids(context2.model.getChildren(node2), context2.model);
    Iterator<N2> it2 = children2.iterator();

    //
    ChangeIterator<String> it = new LCS<String>().diff(childrenIds1, childrenIds2);
    while (it.hasNext()) {
      switch (it.next()) {
        case KEEP:
          perform(it1.next(), it2.next(), handler);
          break;
        case ADD:
          it2.next();
          N1 a = context1.findById(it.getElement());
          N2 added = children2.get(it.getIndex2() - 1);
          if (a != null) {
            handler.movedIn(a);
            perform(a, added, handler);
          } else {
            handler.added(added);
          }
          break;
        case REMOVE:
          it1.next();
          N1 removed = children1.get(it.getIndex1() - 1);
          N2 b = context2.findById(it.getElement());
          if (b != null) {
            handler.movedOut(removed);
          } else {
            handler.removed(removed);
          }
          break;
      }
    }

    handler.leave(node1);

  }

  private <N> String[] ids(List<N> nodes, NodeModel<N> model) {
    int size = nodes.size();
    String[] ids = new String[size];
    if (nodes instanceof RandomAccess) {
      for (int i = 0;i < size;i++) {
        ids[i] = model.getId(nodes.get(i));
      }
    } else {
      int i = 0;
      for (N node : nodes) {
        ids[i++] = model.getId(node);
      }
    }
    return ids;
  }
}
