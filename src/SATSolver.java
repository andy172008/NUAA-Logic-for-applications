//author：SX1916085 贺星宇
//github: https://github.com/andy172008/NUAA-Logic-for-applications

import java.util.HashMap;
import java.util.Map;


//输入的字符串应该为合法表达式，字符串内只有小写字母和逻辑符号
//逻辑符号包含：*（代表and，与），+（代表or，或），！（代表not，非），>（代表imply，蕴涵）
public class SATSolver {
    static Map<Character, Character> tfTable = new HashMap<>();

    public static void main(String[] strings) {
        String s = "((!p*q)>(!q*p))";
        System.out.println("原式为："+s);
        if (tableaux(s, 't')) {
            System.out.println("SAT true");
        } else {
            System.out.println("SAT flase");
        }
    }

    //tableaux算法，用来判断一个逻辑表达式是否可以为真，或是否可以为假
    //s为逻辑表达式，tf为真假，若该字符是't'则验证表达式是否可以为真，若该字符为'f'则验证表达式是否可以为假
    //若返回值为真，代表表达式可满足tf所规定的真假，不代表表达式本身的真假
    public static boolean tableaux(String s, char tf) {
        s = removeParentheses(s);
        int pos = getLogicalSymbolPos(s);
        //该表达式为单个字母或带着！的表达式，无法由二目逻辑符号进行分割
        if (pos == -1) {
            //说明是单个字符
            if (s.length() == 1) {
                //s在之前没被赋值，那么必然可满足要求
                if (!tfTable.containsKey(s.charAt(0))) {
                    System.out.println(s + "的取值应为" + tf);
                    tfTable.put(s.charAt(0), tf);
                    return true;
                } else {
                    if (tf == tfTable.get(s.charAt(0))) {
                        return true;
                    } else {
                        System.out.println(s + "的取值与之前出现冲突，不可满足");
                        return false;
                    }
                }
            }
            //说明是！加上一个表达式
            else {
                if (s.charAt(0) != '!') {
                    System.out.println("出现意外错误，错误代号1");
                }
                s = s.substring(1, s.length());
                s = removeParentheses(s);
                if (tf == 't') {
                    return tableaux(s, 'f');
                } else if (tf == 'f') {
                    return tableaux(s, 't');
                }
            }


        } else {
            String exp1 = s.substring(0, pos);
            String exp2 = s.substring(pos + 1, s.length());
            if (s.charAt(pos) == '*') {
                if (tf == 't') {
                    System.out.println(exp1 + "与" + exp2 + "都应该取真");
                    if (!tableaux(exp1, 't')) {
                        System.out.println(exp1 + "无法为真");
                        return false;
                    }
                    if (!tableaux(exp2, 't')) {
                        System.out.println(exp2 + "无法为真");
                        return false;
                    }
                    System.out.println(exp1 + "与" + exp2 + "可同时为真");
                    return true;
                } else if (tf == 'f') {

                    System.out.println(exp1 + "与" + exp2 + "应该至少有一个能取假");
                    if (tableaux(exp1, 'f')) {
                        System.out.println(exp1 + "可以取假");
                        return true;
                    }
                    if (tableaux(exp2, 'f')) {
                        System.out.println(exp2 + "可以取假");
                        return true;
                    }
                    if (!tableaux(exp1, 'f') && !tableaux(exp2, 'f')) {
                        System.out.println(exp1 + "和" + exp2 + "均不可取假");
                        return false;
                    }
                }

            } else if (s.charAt(pos) == '+') {
                if (tf == 't') {
                    System.out.println(exp1 + "与" + exp2 + "应该至少有一个能取真");
                    if (tableaux(exp1, 't')) {
                        System.out.println(exp1 + "可以取真");
                        return true;
                    }
                    if (tableaux(exp2, 't')) {
                        System.out.println(exp2 + "可以取真");
                        return true;
                    }
                    System.out.println(exp1 + "和" + exp2 + "均不可取真");
                    return false;

                } else if (tf == 'f') {
                    System.out.println(exp1 + "与" + exp2 + "都应该取假");
                    if (!tableaux(exp1, 'f')) {
                        System.out.println(exp1 + "无法为假");
                        return false;
                    }
                    if (!tableaux(exp2, 'f')) {
                        System.out.println(exp2 + "无法为假");
                        return false;
                    }
                    System.out.println(exp1 + "与" + exp2 + "可同时为假");
                    return true;
                }

            } else if (s.charAt(pos) == '>') {
                if (tf == 't') {
                    System.out.println(exp1 + "应该为假，或者" + exp2 + "应该为真");
                    if (tableaux(exp1, 'f')) {
                        System.out.println(exp1 + "可以为假");
                        return true;
                    }
                    if (tableaux(exp2, 't')) {
                        System.out.println(exp2 + "可以为真");
                        return true;
                    }
                    System.out.println(exp1 + "取不了假，并且" + exp2 + "取不了真");
                    return false;
                } else if (tf == 'f') {
                    System.out.println(exp1 + "应该为真，并且" + exp2 + "应该为假");
                    if (!tableaux(exp1, 't')) {
                        System.out.println(exp1 + "取不了真");
                        return false;
                    }
                    if (!tableaux(exp2, 'f')) {
                        System.out.println(exp2 + "取不了假");
                        return false;
                    }
                    System.out.println(exp1 + "可以取真，并且" + exp2 + "可以取假");
                    return true;
                }

            }
        }
        return false;
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

    //这个函数的作用是，返回式子中不在括号内的最后一个逻辑符号的位置,如果出现意外错误，返回-1
    //例如a or b and c,这个式子等价与(a or b) and c,那么这个函数就会返回and所在的位置，即3
    //如果原式为a or b and (c or d),那么这个函数会返回and所在的位置，即3
    //请注意，上述的逻辑符号指双目逻辑符号，不包括！，即not
    public static int getLogicalSymbolPos(String s) {
        removeParentheses(s);
        int flag = 0;
        for (int i = s.length() - 1; i >= 0; i--) {
            if (s.charAt(i) == '(') {
                flag++;
            } else if (s.charAt(i) == ')') {
                flag--;
            }

            if (flag == 0) {
//                if (s.charAt(i) == '*' || s.charAt(i) == '+' || s.charAt(i) == '!' || s.charAt(i) == '>') {
                if (s.charAt(i) == '*' || s.charAt(i) == '+' || s.charAt(i) == '>') {
                    return i;
                }
            }
        }
        return -1;
    }

}
