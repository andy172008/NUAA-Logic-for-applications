//author：SX1916085 贺星宇
import java.util.Vector;

public class Distributive {
    public static void main(String[] args) {

        String s = distributiveLaw("a*(b*c+d*e)");
        System.out.println(s);


    }

    //设定输入的表达式均为合法表达式
    //包含4类字符：小写字母、小括号、加号、乘号
    //小写字母代表表达式，加号代表or，乘号代表and
    //分配律有两条，这个程序中只对其中一条进行考虑，会将输入的式子往析取范式（DNF）方向靠拢
    public static String distributiveLaw(String s) {

        s = removeParentheses(s);
        //判断当前是否在括号里
        int flag = 0;
        Vector<Integer> vector = new Vector<>();
        vector.add(-1);
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == '(') {
                flag++;
            } else if (s.charAt(i) == ')') {
                flag--;
            }
            if (flag == 0 && s.charAt(i) == '+') {
                vector.add(i);
            }
        }
        vector.add(s.length());
        String rs = "";

        if (vector.size() > 2) {
            for (int i = 0; i < vector.size() - 1; i++) {
                rs += distributiveLaw(s.substring(vector.elementAt(i) + 1, vector.elementAt(i + 1)));
                if (i != vector.size() - 2) {
                    rs += "+";
                }
            }
            return rs;
        }

        //如果程序执行到这里，说明输入的式子是若干个式子的乘积，该对其进行分析
        Vector<String> expression = splitByAnd(s);
        //因为不知道能不能使用交换律，所以这里假定不能使用交换律
        //如果第一个式子能够拆分
        if (isSplitByOr(expression.elementAt(0))) {
            Vector<String> exp1 = splitByOr(expression.elementAt(0));
            String exp2 = "";
            for (int i = 1; i < expression.size(); i++) {
                exp2 += expression.elementAt(i);
                if (i != expression.size() - 1) {
                    exp2 += "*";
                }
            }
            String tempExp = "";
            for (int i = 0; i < exp1.size(); i++) {
                tempExp += exp1.elementAt(i) + "*" + exp2;
                if (i != exp1.size() - 1) {
                    tempExp += "+";
                }
            }
            return distributiveLaw(tempExp);
        }
        //如果第一个式子不能拆分，我们继续分析后面的式子
        else {
            for (int i = 1; i < expression.size() ; i++) {
                if (isSplitByOr(expression.elementAt(i))) {
                    Vector<String> exp1 = splitByOr(expression.elementAt(i));
                    String exp2 = "";
                    for (int j = 0; j < i; j++) {
                        exp2 += expression.elementAt(j);
                        if (j != i - 1) {
                            exp2 += "*";
                        }
                    }
                    String tempExp = "";
                    for (int j = 0; j < exp1.size(); j++) {
                        tempExp += exp2 + "*" + exp1.elementAt(j);
                        if (j != exp1.size() - 1) {
                            tempExp += "+";
                        }
                    }
                    String tail = "";
                    for (int j = i + 1; j < expression.size() ; j++) {
                        tail += expression.elementAt(j);
                        if (j != expression.size() - 2) {
                            tail += "*";
                        }
                    }
                    if(tail!="") {
                        return "(" + distributiveLaw(tempExp) + ")*" + tail;
                    }else{
                        return distributiveLaw(tempExp);
                    }
                }
            }
        }
        //如果所有的式子都不能拆分
        return s;
    }

    //这个函数的作用是，若输入的字符串整体被小括号所包裹，则脱去小括号
    public static String removeParentheses(String s) {

        if (s.charAt(0) == '(' && s.charAt(s.length() - 1) == ')') {
            int flag = 0;
            for (int i = 1; i < s.length() - 1; i++) {
                if (s.charAt(i) == '(') {
                    flag++;
                } else if (s.charAt(i) == ')') {
                    flag--;
                }
                if (flag < 0) return s;
            }
            return removeParentheses(s.substring(1, s.length() - 1));
        } else {
            return s;
        }
    }

    //这个函数的作用是，判断一个简单表达式是否能被拆分成若干个表达式的累加
    public static boolean isSplitByOr(String s) {
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == '+') {
                return true;
            }
        }
        return false;
    }

    //这个函数的作用是，将若干个乘在一起的表达式分开
    public static Vector<String> splitByAnd(String s) {
        s = removeParentheses(s);
        Vector<String> rs = new Vector<>();

        int flag = 0;
        int pos = 0;


        for (int i = 0; i < s.length(); i++) {
            if (i == s.length() - 1) {
                rs.add(s.substring(pos, i + 1));
                break;
            }
            if (s.charAt(i) == '(') {
                flag++;
            } else if (s.charAt(i) == ')') {
                flag--;
            }

            if (flag == 0 && s.charAt(i) == '*') {
                rs.add(s.substring(pos, i));
                pos = i + 1;
            }
        }

        return rs;
    }

    //这个函数的作用是，将若干个加在一起的表达式分开
    public static Vector<String> splitByOr(String s) {
        s = removeParentheses(s);
        Vector<String> rs = new Vector<>();

        int flag = 0;
        int pos = 0;

        for (int i = 0; i < s.length(); i++) {
            if (i == s.length() - 1) {
                rs.add(s.substring(pos, i + 1));
                break;
            }
            if (s.charAt(i) == '(') {
                flag++;
            } else if (s.charAt(i) == ')') {
                flag--;
            }

            if (flag == 0 && s.charAt(i) == '+') {
                rs.add(s.substring(pos, i));
                pos = i + 1;
            }
        }


        return rs;
    }

}
