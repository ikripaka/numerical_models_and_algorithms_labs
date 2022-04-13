public class Main {

    public static void main(String[] args) throws Exception {
       int[] array = new int[]{14, 18, 20, 15, 30};
       int a,b;
       for(int i = 1; i < 33; i++){
           for(int j = 0; j < 10; j++){
               a = i;
               b = j;
               System.out.println("A: " + a + "B: " + b);
               for(int ints : array){
                   System.out.println(ints + " -- " + (ints*a+b)%33 );
               }
               a = i;
               b = -j;
               System.out.println("A:\n" + a + "\nB:\n" + b);
               for(int ints : array){
                   System.out.println(ints + " -- " + (ints*a+b)%33 );
               }
           }
       }
    }
}
