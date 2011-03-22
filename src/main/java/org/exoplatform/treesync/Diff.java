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

import javax.xml.soap.Node;
import java.util.HashMap;
import java.util.ListIterator;
import java.util.Map;

/**
 * @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a>
 * @version $Revision$
 */
public class Diff<N1, N2> {

  /** . */
  private final Context<N1> context1;

  /** . */
  private final Context<N2> context2;

  public Diff(NodeModel<N1> model1, N1 root1, NodeModel<N2> model2, N2 root2) {
    this.context1 = new Context<N1>(model1, root1);
    this.context2 = new Context<N2>(model2, root2);
  }

  private static class Context<N> {

    /** . */
    private final NodeModel<N> model;

    /** . */
    private final N root;

    private Context(NodeModel<N> model, N root) {
      this.model = model;
      this.root = root;
    }

    private N findById(String id) {
      return findById(root, id);
    }

    private N findById(N node, String id) {
      N found;
      if (model.getId(node).equals(id)) {
        found = node;
      } else {
        found = null;
        for (N child : model.getChildren(node)) {
          found = findById(child, id);
          if (found != null) {
            break;
          }
        }
      }
      return found;
    }
  }

  public void perform(DiffHandler<N1, N2> handler) {

  }

  public void perform(N1 node1, N2 node2, DiffHandler<N1, N2> handler) {

    String id1 = context1.model.getId(node1);
    String id2 = context2.model.getId(node2);

    // First check equality
    if (!id1.equals(id2)) {
      //
    }

    //

    //
    ListIterator<N1> children1 = context1.model.getChildren(node1).listIterator();
    ListIterator<N2> children2 = context2.model.getChildren(node2).listIterator();

    //
  }

}
