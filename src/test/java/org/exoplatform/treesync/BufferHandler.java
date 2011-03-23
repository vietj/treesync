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

import junit.framework.Assert;

import java.util.Collections;
import java.util.LinkedList;

/**
 * @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a>
 * @version $Revision$
 */
public class BufferHandler implements DiffHandler<SimpleNode, SimpleNode> {

  public static class Change {
  }

  public static class Enter extends Change {
    private final SimpleNode node;
    public Enter(SimpleNode node) {
      this.node = node;
    }
  }

  public static class Leave extends Change {
    private final SimpleNode node;
    public Leave(SimpleNode node) {
      this.node = node;
    }
  }

  public static class Added extends Change {
    private final SimpleNode node;
    public Added(SimpleNode node) {
      this.node = node;
    }
  }

  public static class Removed extends Change {
    private final SimpleNode node;
    public Removed(SimpleNode node) {
      this.node = node;
    }
  }

  public static class MovedOut extends Change {
    private final SimpleNode node;
    public MovedOut(SimpleNode node) {
      this.node = node;
    }
  }

  public static class MovedIn extends Change {
    private final SimpleNode node;
    public MovedIn(SimpleNode node) {
      this.node = node;
    }
  }

  /** . */
  private final LinkedList<Change> changes = new LinkedList<Change>();

  public void enter(SimpleNode node1) {
    changes.add(new Enter(node1));
  }

  public void added(SimpleNode node2) {
    changes.add(new Added(node2));
  }

  public void removed(SimpleNode node1) {
    changes.add(new Removed(node1));
  }

  public void movedOut(SimpleNode node1) {
    changes.add(new MovedOut(node1));
  }

  public void movedIn(SimpleNode node1) {
    changes.add(new MovedIn(node1));
  }

  public void leave(SimpleNode node1) {
    changes.add(new Leave(node1));
  }

  public void assertEnter(SimpleNode node) {
    Assert.assertTrue(changes.size() > 0);
    Enter change = (Enter)changes.removeFirst();
    Assert.assertSame(node, change.node);
  }

  public void assertLeave(SimpleNode node) {
    Assert.assertTrue(changes.size() > 0);
    Leave change = (Leave)changes.removeFirst();
    Assert.assertSame(node, change.node);
  }

  public void assertRemoved(SimpleNode node) {
    Assert.assertTrue(changes.size() > 0);
    Removed change = (Removed)changes.removeFirst();
    Assert.assertSame(node, change.node);
  }

  public void assertAdded(SimpleNode node) {
    Assert.assertTrue(changes.size() > 0);
    Added change = (Added)changes.removeFirst();
    Assert.assertSame(node, change.node);
  }

  public void assertMovedOut(SimpleNode node) {
    Assert.assertTrue(changes.size() > 0);
    MovedOut change = (MovedOut)changes.removeFirst();
    Assert.assertSame(node, change.node);
  }

  public void assertMovedIn(SimpleNode node) {
    Assert.assertTrue(changes.size() > 0);
    MovedIn change = (MovedIn)changes.removeFirst();
    Assert.assertSame(node, change.node);
  }

  public void assertEmpty() {
    Assert.assertEquals(Collections.<Change>emptyList(), changes);
  }
}
