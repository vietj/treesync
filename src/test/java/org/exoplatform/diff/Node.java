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

package org.exoplatform.diff;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a>
 * @version $Revision$
 */
public class Node {

  /** . */
  private static final AtomicInteger generator = new AtomicInteger();

  /** . */
  private Node parent;

  /** . */
  private final String id;

  /** . */
  private final List<Node> children;

  private Node(Node that) {

    ArrayList<Node> children = new ArrayList<Node>(that.children.size());
    for (Node thatChild : that.children) {
      Node child = new Node(thatChild);
      child.parent = this;
      children.add(child);
    }

    //
    this.id = that.id;
    this.children = children;
    this.parent = null;
  }

  public Node()
  {
    this.id = "" + generator.incrementAndGet();
    this.children = new ArrayList<Node>();
    this.parent = null;
  }

  public String getId() {
    return id;
  }

   public Node getParent() {
      return parent;
   }

   public Node addChild() {
    Node child = new Node();
    children.add(child);
    child.parent = this;
    return child;
  }

  public Node getRoot() {
    return parent == null ? this : parent.getRoot();
  }

  public void addChild(Node child) {
    if (child.parent == null) {
      throw new AssertionError();
    }
    if (getRoot() != child.getRoot()) {
      throw new AssertionError();
    }
    for (Iterator<Node> i = child.parent.children.iterator();i.hasNext();) {
      Node sibling = i.next();
      if (sibling == child) {
        i.remove();
        child.parent = null;
        break;
      }
    }
    child.parent = this;
    children.add(child);
  }

  public Node getChild(String id) {
    for (Node child : children) {
      if (child.id.equals(id)) {
        return child;
      }
    }
    return null;
  }

   public Node getDescendant(String id) {
     Node descendant = getChild(id);
     if (descendant == null) {
       for (Node child : children) {
         descendant = child.getDescendant(id);
         if (descendant != null) {
           break;
         }
       }
     }
     return descendant;
   }

  public void destroy() {
    if (parent != null) {
      for (Iterator<Node> i = parent.children.iterator();i.hasNext();) {
        Node sibling = i.next();
        if (sibling == this) {
          i.remove();
          parent = null;
        }
      }
    }
    for (Node child : children) {
      child.parent = null;
    }
  }

  private final List<String> childrenIds = new AbstractList<String>() {
     @Override
     public String get(int index) {
       return children.get(index).id;
     }
     @Override
     public int size() {
        return children.size();
     }
  };

  public List<String> getChildrenIds() {
    return childrenIds;
  }

  public List<Node> getChildren() {
    return new ArrayList<Node>(children);
  }

  public Node clone() {
    return new Node(this);
  }

  @Override
  public String toString() {
    return "SimpleNode[id=" + id + ",@=" + System.identityHashCode(this) + "]";
  }
}
