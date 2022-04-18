package entity;

import java.util.HashMap;

public class ConstValues {
    //词法分析
    public static final int EOF = -1;
    public static final int ERROR = 0;
    public static final int IDENTIFER_ITEM = 1; //标识符
    public static final int RESERVED_ITEM = 2;  //保留字
    public static final int CONSTANT_ITEM = 3;  //常量
    public static final int SINGLE_DELIMITER_ITEM = 4;  //单分界符
    public static final int ASSIGN_ITEM = 5;     //赋值符号
    public static final int ARRAY_RANGE = 6;     //数组下标
    public static final int COMMENT_ITEM = 7;    //注释
    public static final int CHAR_ITEM = 8;       //字符
    public static final int NONE = 9;
    //语法分析
    public static final int T_SYMPLE = 1001;     //终极符
    public static final int N_SYMPLE = 1002;     //非终极符
    public static final int EMPTY = 1003;        //空生成式
    //语义分析
    public static final int GENRE_TYPE = 2001;   //类型名
    public static final int VAR_TYPE = 2002;     //变量名
    public static final int FUNC_TYPE = 2003;    //函数名
    //用于生成token序列
    public static HashMap<Integer,String> wordType = new HashMap<Integer,String>(){
        {
            put(-1,"EOF");put(0,"ERROR");put(1,"IDENTITY");put(2,"RESERVED");put(3,"CONSTANT");
            put(4,"SINGLE_DELIMITER");put(5,"ASSISG");put(6,"ARRAY_RANGE");put(7,"COMMENT");put(8,"CHAR");

        }
    };

}

