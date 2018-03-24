package avlTree;


import java.util.List;

/**
 * Created by nayanzin on 10.06.17.
 */
public class Test {
    public static void main(String[] args){
        System.out.println("Hello");
        AVLTree<Integer, Integer> tree = new AVLTree<>();

        tree.put(2, 2);
        tree.put(6, 6);
        tree.put(3, 3);
        tree.put(1, 1);
        tree.put(0, 0);
        tree.put(9, 9);
        tree.put(8, 8);
        tree.put(5, 5);
        tree.put(4, 4);
        tree.put(7, 7);


        tree.printTree();
        tree.check();

        //System.out.println("Deleting min " + tree.min());
        //tree.deleteMin();
        //tree.printTree();

       // System.out.println("Deleting max " + tree.max());
        //tree.deleteMax();
        //tree.printTree();

        //System.out.println("Start");
        for(int i=0; i < tree.size(); i++) {
            if(tree.floor(i) != tree.myFloor(i)) {
                System.out.println("Floor("+i+") is " + tree.floor(i));
                System.out.println("My floor ("+i+") is " + tree.myFloor(i));
            }

            if(tree.ceiling(i) != tree.myCeiling(i)) {
                System.out.println("Ceiling("+i+") is " + tree.ceiling(i));
                System.out.println("My ceiling ("+i+") is " + tree.myCeiling(i));
            }

            if(tree.select(i) != tree.mySelect(i)) {
                System.out.println("Select("+i+") is " + tree.select(i));
                System.out.println("My select ("+i+") is " + tree.mySelect(i));
            }

            if(tree.rank(i) != tree.myRank(i)) {
                System.out.println("Rank("+i+") is " + tree.rank(i));
                System.out.println("My rank ("+i+") is " + tree.myRank(i));
            }
        }
       // System.out.println("Finish");

        //tree.keysInOrder().forEach(System.out::println);
        //tree.keysLevelOrder().forEach(System.out::println);
        /*
        tree.keysLevelOrderSeparated().forEach(key->{
            if(key == null)
                System.out.println("\n");
            else
                System.out.print(key+"  ");
        });
        */
        //tree.keysInRange(4, 8).forEach(System.out::println);

    }
}
