//author：SX1916085 贺星宇
//github: https://github.com/andy172008/NUAA-Logic-for-applications

import java.util.Vector;

public class DeMorgan {
    public static void main(String[] argc) {
        String rs = deMorganLaw("!(a*b*(c+e))");
        System.out.println(rs);
    }


    //设定输入的表达式均为合法表达式
    //包含5类字符：小写字母、小括号、加号、乘号、叹号
    //小写字母代表表达式，加号代表or，乘号代表and，叹号代表not
    //德摩根律有两条，这个程序中只对其中一条进行考虑，会将输入的式子往析取范式（DNF）方向靠拢
    //!(a*b) = !a+!b
    public static String deMorganLaw(String s) {
        s = removeParentheses(s);
        if (!isLegal(s)) {
            //return "Error:This expression cannot be processed with DeMorgan's Law!!!\n";
            return s;
        }
        Vector<String> vector = splitByAnd(s.substring(1, s.length()));
        if (vector.size() == 1) {
            return s;
        }
        String rs = "";
        for (int i = 0; i < vector.size(); i++) {
            rs += deMorganLaw("!" + vector.elementAt(i));
            if (i != vector.size() - 1) {
                rs += "+";
            }
        }
        return rs;
    }

    //这个函数的作用是，判断是否能用德摩根律处理
    public static boolean isLegal(String s) {
        if (s.charAt(0) != '!') {
            return false;
        }
        String temp = removeParentheses(s.substring(1, s.length()));
        int flag = 0;
        for (int i = 0; i < temp.length(); i++) {
            if (temp.charAt(i) == '(') {
                flag--;
            } else if (temp.charAt(i) == ')') {
                flag++;
            }
            if (flag == 0 && temp.charAt(i) == '+') {
                return false;
            }
        }
        return true;
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
}
