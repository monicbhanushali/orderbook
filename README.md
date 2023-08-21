# Stock Exchange Order Book Implemention
Implementation of Stock Exchange Order Book in Java
For Educational Purposes

The core data structure used here is a binary search tree (red black tree to be specific) were each node itself is a linked list.

TreeMap would allow to get 

Red Black Tree = Java's TreeMap implementation

Why TreeMap/Red Black Tree ?
 - Another option was PriorityQueue (Min & Max Heap) but contains operation take O(n). For TreeMap - contains operation takes O(logN)
 - Min/Max Heap find smallest & largest element take O(1) for treeMap it is O(logN)
 - PriorityQueue allows duplicate elements while tree map doesn't

Why LinkedList ?
 - Natural time based ordering for elements
 - Appending to linked list (adding an order) would take O(1)
 - deleting an order would take O(N)
   - Can we do it in O(1) ? store reference/index somewhere ?