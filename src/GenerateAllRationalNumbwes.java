//author：SX1916085 贺星宇
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class GenerateAllRationalNumbwes {
    public static void main(String[] args) {
        Scanner cin = new Scanner(System.in);
        int num = cin.nextInt();
        getRationalNumbwes(num);
        cin.close();
    }

    //这个程序的主要思想是来源于Calkin-Wilf tree，这里对其内容不做具体介绍，有兴趣可去维基百科查阅
    //这个二叉树上有所有的正有理数，我稍加修改后，即可输出全部有理数
    public static void getRationalNumbwes(int num) {
        //对num的取值范围做一个检验
        if (num <= 0) {
            return;
        }

        if (num == 1) {
            System.out.println(0);
            return;
        } else {
            System.out.println(0);
            num--;
        }

        //利用队列，构造一个二叉树的层次遍历
        Queue<Integer> queue = new LinkedList<>();
        int numerator = 0;
        int denominator = 0;
        queue.add(1);
        queue.add(1);

        while (num > 0) {
            numerator = queue.poll();
            denominator = queue.poll();

            //将一个节点a/b移除后，将a/a+b和a+b/b添加进队列中
            queue.add(numerator);
            queue.add(numerator+denominator);

            queue.add(numerator+denominator);
            queue.add(denominator);

            System.out.println(numerator+"/"+denominator);
            num--;
            //如果条件允许，将这个正有理数的相反数也输出
            if(num > 0){
                System.out.println("-"+numerator+"/"+denominator);
                num--;
            }
        }
    }

}
