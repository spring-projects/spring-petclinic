package helloworld;

public class HelloWorld extends Object {

    public static void main(String[] args) {

    int i = 0;

    System.out.println("Hello World !");

    int k = i / 5;
    k++;
    int j;
    for (j = 0; j > 10; j++) {
       System.out.println("New buggy loop for release LL branch");
    }

    // int k = 5 / i;

    if ( i = 0 ) {
       int a = 5;
       a++;
       a++;
       a++;
       a++;
       a++;
       a++;
       a++;
       a++;
       a++;
       a++;
    }

    // ISSUE-MASTER

    int x = 0; // LINE-DELETE-MASTER
    int y = 0; // LINE-DELETE-MASTER
    int y1 = 0; // LINE-DELETE-MASTER
    int y2 = 0; // LINE-DELETE-MASTER
    int y3 = 0; // LINE-DELETE-MASTER
    // LINE-ADD-MASTER int z = 0;

   // LINE-ADD-FEATURE if ( i = 0 ) {
   // LINE-ADD-FEATURE    int a = 5;
   // LINE-ADD-FEATURE    a++;
   // LINE-ADD-FEATURE    a++;
   // LINE-ADD-FEATURE    a++;
   // LINE-ADD-FEATURE    a++;
   // LINE-ADD-FEATURE    a++;
   // LINE-ADD-FEATURE    a++;
   // LINE-ADD-FEATURE    a++;
   // LINE-ADD-FEATURE    a++;
   // LINE-ADD-FEATURE    a++;
   // LINE-ADD-FEATURE    a++;
   // LINE-ADD-FEATURE    i++;
   // LINE-ADD-FEATURE }


   }

}
// FIXME issue introduced in hotfix

