/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

public class Permutation {
    public static void main(String[] args) {
        int k = Integer.parseInt(args[0]);
        String[] strings = new String[args.length - 1];

        if (k > strings.length) throw new IllegalArgumentException("incorrect arguments");

        for (int i = 1; i < args.length; i++) {
            strings[i-1] = args[i];
        }


        // Load to queue
        RandomizedQueue<String> queue = new RandomizedQueue<>();
        for (String string: strings) {
            queue.enqueue(string);
        }

        // Output
        for (int i = 0; i < k; i++) {
            System.out.println(queue.dequeue());
        }

    }
}
