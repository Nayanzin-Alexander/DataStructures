package avlTree;
import java.util.*;

/**
 * Created by nayanzin on 10.06.17.
 */
public class AVLTree <Key extends Comparable<Key>, Value>{
    private Node root;
    /* Inner node class */
    private class Node{
        private final Key key;
        private Value value;
        private int height;
        private int size;
        private Node left;
        private Node right;

        public Node(Key key, Value value, int height, int size){
            this.key = key;
            this.value = value;
            this.size = size;
            this.height = height;
            left = null;
            right = null;
        }

    }


    /* Counstructor */
    public AVLTree(){

    }


    /* Check if the tree is empty */
    public boolean isEmpty(){
        return root == null;
    }


    /* Retruns the number of nodes in tree */
    public int size(){
        return size(root);
    }


    /* Retruns the number of nodes in the subtree */
    private int size(Node node){
        return (node == null) ? 0 : node.size;
    }


    /** Return the height of the interal AVL tree.
     * It is assumed ths the height of an empty tree is -1
     * and the height of a tree with just one node is 0
     *
     * @return the geight of the internal AVL tree
     */
    public int height(){
        return height(root);
    }


    /**
     * retruns the height of the subtree
     */
    private int height(Node node){
        if (node == null)
            return -1;
        else
            return node.height;
    }


    /* Return the value associated with the given key */
    public Value get(Key key){
        if(key == null)
            throw new IllegalArgumentException("Argument to get is null");
        Node node = get(root, key);
        if(node == null)
            return null;
        else
            return node.value;
    }


    /* Recurcively search and returns value associated whith the given key */
    private Node get(Node node, Key key){
        if(node == null)
            return null;

        int compare = key.compareTo(key);

        if(compare < 0 )
            return get(node.left, key);

        if(compare > 0 )
            return get(node.right, key);

        return node;
    }


    /* Checks if the AVL tree contains the given key */
    public boolean contains(Key key){
        return get(key) !=  null;
    }


    /* Inserts specified key-value pair into the AVL tree */
    public void put(Key key, Value value) {
        if (key == null)
            throw new IllegalArgumentException("Argument key to put(Key, Value) is null");
        if (value == null){
            delete(key);
            return;
        }

        root = put(root, key, value);
        assert check();
    }


    /** recursively search place to insert key value pair.
     * Than rebalancing the nodes form insertion place to the root node if need
     */
    private Node put(Node node, Key key, Value value){
        if(node == null)
            return new Node(key, value, 0, 1);

        int compare = key.compareTo(node.key);
        if(compare < 0)
            node.left = put(node.left, key, value);
        else if (compare > 0)
            node.right = put(node.right, key, value);
        else{
            node.value = value;
            return node;
        }

        node.size = 1 + size(node.left) + size(node.right);
        node.height = 1 + Math.max(height(node.left), height(node.right));
        return balance(node);
    }


    /* Restores AVL property of the subtree */
    private Node balance(Node node){
        if(balanceFactor(node) < -1){
            if(balanceFactor(node.right) > 0){
                node.right = rotateRight(node.right);
            }
            node = rotateLeft(node);
        }else if(balanceFactor(node) > 1){
            if(balanceFactor(node.left) < 0){
                node.left = rotateLeft(node.left);
            }
            node = rotateRight(node);
        }
        return node;
    }


    /**
     * Returns the balance factor of the subtree. The balance factor
     * is defined as the difference in height of the left subtree
     * and right subtree, in this order. Therefore, a subtree with a
     * balance factor of -1, 0, or 1 has the AVL property since
     * the heights of the two child subtrees differ by a most one.
     * @param node the subtree
     * @return the balance factor of the subtree
     */
    private int balanceFactor(Node node){
        return height(node.left) - height(node.right);
    }


    /* Rotates the given subtree to the right */
    private Node rotateRight(Node node){
        Node y = node.left;
        node.left = y.right;
        y.right = node;
        y.size = node.size;
        node.size = 1 + size(node.left) + size(node.right);

        node.height = 1 + Math.max(height(node.left), height(node.right));
        y.height = 1 + Math.max(height(y.left), height(y.right));

        return y;
    }


    /* Rotates the given subtree to the right */
    private Node rotateLeft(Node node){
        Node y = node.right;
        node.right = y.left;
        y.left = node;

        y.size = node.size;
        node.size = 1 + size(node.left) + size(node.right);

        node.height = 1 + Math.max(height(node.left), height(node.right));
        y.height = 1 + Math.max(height(y.left), height(y.right));

        return y;
    }


    /* Removes the specified key and its value from the tree */
    public void delete(Key key){
        if(key == null)
            throw new IllegalArgumentException("Argument key to delete(Key) is null");
        if(!contains(key)) return;
        root = delete(root, key);
        assert check();
    }


    /* Removes the key and repairs AVL value */
    private Node delete(Node node, Key key){
        int cmp = key.compareTo(node.key);
        if(cmp < 0){
            node.left = delete(node.left, key);
        } else if(cmp >0){
            node.right = delete(node.right, key);
        } else{
            if(node.left == null) {
                return node.right;
            }
            else if(node.right == null){
                return node.left;
            }
            else {
                Node y = node;
                node = min(y.right);
                node.right = deleteMin(y.right);
                node.left = y.left;
            }
        }
        node.size = 1 + size(node.left) + size(node.right);
        node.height = 1 + Math.max(height(node.left), height(node.right));
        return balance(node);
    }


    /* Removes the smallest key and associated value from the tree */
    public void deleteMin(){
        if(isEmpty()) {
            System.out.println("deleteMin() - Tree is empty");
            return;
        }
        root = deleteMin(root);
        assert check();
    }


    /* Removes the smallest key and repairs AVL property form the given subtree */
    private Node deleteMin(Node node){
        if(node.left == null)
            return node.right;
        node.left = deleteMin(node.left);

        node.size = 1 + size(node.left) + size(node.right);
        node.height = 1 + Math.max(height(node.left), height(node.right));
        return balance(node);
    }


    /* Removes the largest key and associated value from the tree */
    public void deleteMax(){
        if(isEmpty()){
            System.out.println("deleteMax() - Tree is empty");
            return;
        }
        root = deleteMax(root);
        assert check();
    }


    /* Removes max key and restores AVL property to the given subtree */
    private Node deleteMax(Node node){
        if(node == null)
            return null;
        if(node.right == null){
            return node.left;
        } else
            node.right = deleteMax(node.right);

        node.size = 1 + size(node.left) + size(node.right);
        node.height = 1 + Math.max(height(node.left), height(node.right));
        return balance(node);
    }


    /* Returns the smallest key form the tree */
    public Key min(){
        if(isEmpty()){
            System.out.println("min() - Tree is empty");
            return null;
        }
        return min(root).key;
    }


    /* Return node with minimal key from the given subtree */
    private Node min(Node node){
        if(node == null)
            throw new IllegalArgumentException("Argument node in min(Node node) is null");
        if(node.left == null)
            return node;
        else
            return min(node.left);
    }


    /* Returns the larges key form the tree */
    public Key max(){
        if(isEmpty()){
            System.out.println("max() - Tree is empty");
            return null;
        }
        return max(root).key;
    }


    /* Return node with max key from the given subtree */
    private Node max(Node node){
        if(node == null)
            throw new IllegalArgumentException("Argument node in min(Node node) is null");
        if(node.right == null)
            return node;
        else
            return max(node.right);
    }


    /* Returns the largest key in the tree
    /* less than or equal to given key */
    public Key floor(Key key){
        if(isEmpty()){
            System.out.println("floor("+key+") - Tree is empty");
            return null;
        }
        Node node = floor(root, key);
        return (node == null) ? null : node.key;
    }


    /* Recursively search floor node to the given key */
    private Node floor(Node node, Key key){
        if (node == null)
            return null;
        int cmp = key.compareTo(node.key);

        if (cmp == 0)
            return node;
        if (cmp < 0)
            return floor(node.left, key);

        Node y = floor(node.right, key);

        if (y != null)
            return y;
        else
            return node;
    }


    /* My floor realization */
     public Key myFloor(Key key){
        if(isEmpty()){
            System.out.println("floor("+key+") - Tree is empty");
            return null;
        }
        Node node = myFloor(root, key);
        return (node == null) ? null : node.key;
    }

    /* My floor */
    private Node myFloor(Node node, Key key){
        if(node == null)
            return null;

        int cmp = key.compareTo(node.key);


        if(cmp < 0)
            node = myFloor(node.left, key);

        if(cmp == 0)
            node = node;

        if(cmp > 0){
            Node searchAnotherFloor = myFloor(node.right, key);
            if(searchAnotherFloor != null)
                node = searchAnotherFloor;
        }

        return node;
    }


    /* Return the smallest key in the tree
    /* grater than or equal to the given key */
    public Key ceiling(Key key){
        if(isEmpty()){
            System.out.println("ceiling("+key+") - Tree is empty");
            return null;
        }
        Node node = ceiling(root, key);
        return (node == null) ? null : node.key;
    }


    /* Recursively search ceiling node to the given key */
    private Node ceiling(Node node, Key key){
        if (node == null)
            return null;
        int cmp = key.compareTo(node.key);

        if (cmp == 0)
            return node;

        if (cmp > 0)
            return ceiling(node.right, key);

        Node y = ceiling(node.left, key);
        if (y != null)
            return y;
        else
            return node;
    }


    /* My ceiling */
    public Key myCeiling(Key key){
        if(isEmpty()){
            System.out.println("ceiling("+key+") - Tree is empty");
            return null;
        }
        Node node = myCeiling(root, key);
        return (node == null) ? null : node.key;
    }


    /* My ceiling */
    private Node myCeiling(Node node, Key key){
        if (node == null)
            return null;
        int cmp = key.compareTo(node.key);

        if(cmp > 0)
            node = myCeiling(node.right, key);

        if(cmp < 0){
            Node possibleCeilng = myCeiling(node.left, key);
            if(possibleCeilng != null)
                node = possibleCeilng;
        }

        return node;
    }


    /* My select */
    public Key mySelect(int k){
        if (k<0 || k >=size()){
            System.out.println("Argument k in select(k) is out of tree range");
            return null;
        }
        Node node = mySelect(root, k);
        return node.key;
    }


    /* My select */
    private Node mySelect(Node node, int k){
        if(node == null)
            return null;

        if(k > size(node.left))
            return mySelect(node.right, k-size(node.left)-1);

        if(k < size(node.left))
            return mySelect(node.left, k);
        if(k == 1)
            return node;
        return node;
    }


    /* Retrurns the k-th smallest key in the tree
    *  k index starts from 0 */
    public Key select(int k){
        if (k<0 || k >=size()){
            System.out.println("Argument k in select(k) is out of tree range");
            return null;
        }
        Node node = select(root, k);
        return node.key;
    }


    /* Recursicevly search and returns node
    /* whith the k-th smallest key in a subtree */
    private Node select(Node node, int k){
        if(node == null)
            return null;

        int nodeSize = size(node.left);

        if( nodeSize > k)
            return select(node.left, k);
        else if(nodeSize < k)
            return select(node.right, k - nodeSize - 1);
        else //(nodeSize == k)
            return node;
    }


    /* Returns the number of keys on the tree
    /* strictly less than key */
    public int rank(Key key){
        if (key == null){
            throw new IllegalArgumentException("argument key to rank(key) is null");
        }
        return rank(root, key);
    }


    /* Returns the number of keys on the subtree less than key */
    private int rank(Node node, Key key){
        if(node == null)
            return 0;
        int cmp = key.compareTo(node.key);

        if(cmp < 0)
            return rank(node.left, key);
        else if(cmp > 0)
            return 1 + size(node.left) + rank(node.right, key);
        else
            return size(node.left);
    }


    /* My rank */
    public int myRank(Key key){
        if (key == null){
            throw new IllegalArgumentException("argument key to rank(key) is null");
        }
        return myRank(root, key);
    }


    /* My rank */
    private int myRank(Node node, Key key){
        // the number of keys less than key
        if(node == null)
            return 0;
        int cmp = key.compareTo(node.key);
        if(cmp > 0)
            return 1+size(node.left)+myRank(node.right, key);
        if(cmp < 0)
            return myRank(node.left, key);
        //if(cmp == 0)
            return size(node.left);// + myRank(node.right, key);
    }


    /* Returns all keys on the tree */
    public Iterable<Key> keys(){
        return keysInOrder();
    }


    /* Returns all keys on the tree in-order traversal*/
    public Iterable<Key> keysInOrder(){
        List<Key> list = new ArrayList<>();
        keysInOrder(root, list);
        return list;
    }


    /* Adds the keys in the subtree to queue following an in-order traversal. */
    private void keysInOrder(Node node, List<Key> list){
        if(node == null)
            return;
        keysInOrder(node.left, list);
        list.add(node.key);
        keysInOrder(node.right, list);
    }


    /* Returns all keys in the tree folowing a level-order traversal */
    public Iterable<Key> keysLevelOrder(){
        List<Key> list = new ArrayList<>();
        if(isEmpty()){
            System.out.println("Tree is empty");
            return list;
        }
        List<Node> list2 = new ArrayList<>();
        list2.add(root);

        while(!list2.isEmpty()){
            Node x = list2.remove(0);
            list.add(x.key);
            if(x.left != null)
                list2.add(x.left);

            if(x.right != null)
                list2.add(x.right);
        }
        return list;
    }



    /* Returns all keys in the tree folowing a level-order traversal */
    public Iterable<Key> keysLevelOrderSeparated(){
        List<Key> list = new ArrayList<>();
        if(isEmpty()){
            System.out.println("Tree is empty");
            return list;
        }
        List<Node> list2 = new ArrayList<>();
        List<Node> list3 = new ArrayList<>();
        list2.add(root);

        while(!list2.isEmpty()){
            Node x = list2.remove(0);
            list.add(x.key);
            if(x.left != null)
                list3.add(x.left);

            if(x.right != null)
                list3.add(x.right);

            if(list2.isEmpty()){
                list.add(null);
                list2.addAll(list3);
                list3.clear();
            }
        }
        return list;
    }


    /* Return keys in the given range */
    public List<Key> keysInRange(Key low, Key hi){
        if(low == null || hi == null)
            throw new IllegalArgumentException("argument to keysInRange() are null");
        List<Key> keysInRangeList = new ArrayList();
        keysInRange(root, keysInRangeList, low, hi);
        return keysInRangeList;
    }


    /* Return keys in the given range */
    private void keysInRange(Node node, List<Key> list, Key lo, Key hi){
        if(node == null)
            return;
        int cmpLow = node.key.compareTo(lo);
        int cmpHi  = node.key.compareTo(hi);

        //When node.key is smaller than low ->go right
        if(cmpLow < 0) {
            keysInRange(node.right, list, lo, hi);
            return;
        }
        //When node.key is bigger than hi ->go left
        if(cmpHi > 0){
            keysInRange(node.left,list, lo, hi);
            return;
        }

        //if node.key is in range between low and hi
        keysInRange(node.left,list, lo, hi);
        list.add(node.key);
        keysInRange(node.right,list, lo, hi);
    }


    /* Checks if the AVL tree invariants are fine */
    public boolean check(){
        System.out.println("Testing tree");
        boolean isBST = isBST(root, null, null);
        boolean isAVL = isAVL(root);
        boolean isSizeConsistent = isSizeConsistent(root);
        boolean isRankConsistent = isRankConsistent();
        if(!isBST)
            System.out.println("Tree is not BST");
        if(!isAVL)
            System.out.println("Tree is not AVL");
        if(!isSizeConsistent)
            System.out.println("Tree is not isSizeConsistent");
        if(!isRankConsistent)
            System.out.println("Tree is not isRankConsistent");
        return isAVL && isBST && isSizeConsistent & isRankConsistent;
    }


    /* Checks if the symmetric order consistent. */
    private boolean isBST(Node node, Key min, Key max){
        if(node == null)
            return true;
        if(min!= null && node.key.compareTo(min) <= 0) return false;
        if(max!=null && node.key.compareTo(max) >=0) return false;

        return isBST(node.left, min, node.key) && isBST(node.right, node.key, max);
    }


    /* Checks if AVL property is consistent in subtree. */
    private boolean isAVL(Node node){
        if(node == null){
            return true;
        }
        int bf = balanceFactor(node);
        if(bf > 1 || bf < -1 )
            return false;
        return isAVL(node.left) && isAVL(node.right);
    }


    /* Checks if the size of the tree is consistent. */
    private boolean isSizeConsistent(Node node){
        if(node == null)
            return true;
        if(node.size != size(node.left) + size(node.right) + 1)
            return false;
        return isSizeConsistent(node.left) && isSizeConsistent(node.right);
    }


    /* Checks if rank is consistent. */
    private boolean isRankConsistent(){
        // rank is the number of keys on the subtree less than key
        // select returns the k-th smallest key in the tree
        for(int i=0; i<size(); i++){
            if(i != rank(select(i))) return false;
        }
        for(Key key : keysLevelOrder()){
            if(key.compareTo(select(rank(key))) != 0)
                return false;
        }
        return true;
    }



















    public void printTree(){
        if(isEmpty()){
            System.out.println("The tree is empty");
            return;
        }
        int height = height()+1;
        int columnsCount = (((Double) Math.pow(2, height-1)).intValue() * 2) -1; //Why?
        int rowsCount = height;
        for(int i=0; i<height-1; i++)
            rowsCount += (int) Math.pow(2, i);

        System.out.println("");
        String[][] matrix = new String[rowsCount][columnsCount];

        formMatrix(matrix, root, 0, columnsCount/2, 1);

        for(String[] row:matrix){
            System.out.print("       ");
            for(String element:row){
                if(element == null){
                    System.out.format(" ");
                }
                else{
                    System.out.format("%s", element);
                }
            }
            System.out.println("");
        }
    }

    private void formMatrix(String[][] matrix, Node node, int row, int column, int level){
        if(node == null)
            return;

        int height = height()+1;

        int offset = ((Double) Math.pow(2, height - level - 1) ).intValue();
        matrix[row][column] = node.key.toString();
        // matrix[row][column] = String.valueOf(balanceFactor(node));
        //matrix[row][column] = String.valueOf(node.height());
        //matrix[row][column] = String.valueOf(node.size);


        if(node.left!=null){
            for(int i=1; i < offset+1; i++)
                matrix[row+i][column-i] = "/";
            formMatrix(matrix, node.left, row+offset+1, column-offset, level+1);
        }
        if(node.right!=null){
            for(int i=1; i <= offset; i++)
                matrix[row+i][column+i] = "\\";
            formMatrix(matrix, node.right, row+offset+1, column+offset, level+1);
        }
    }
}
