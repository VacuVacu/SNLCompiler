package WordAnalysis;

import entity.ConstValues;
import entity.TokenElement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class WordAutomat {

    //自动机状态
    static HashMap<Integer, String> states = new HashMap<Integer, String>() {
        {
            put(0, "START");
            put(1, "ID");
            put(2, "NUM");
            put(3, "DONE");
            put(4, "ASSIGN");
            put(5, "RANGE");
            put(6, "COMMENT");
            put(7, "CHAR");
        }
    };

    //单分界符
   static List<Character> singleDelimiter = new ArrayList<Character>() {
        {
            add('.'); add(','); add(';'); add('+'); add('-'); add('*'); add('/'); add('('); add(')');
            add('['); add(']'); add('='); add('<');
        }
    };

    //读取代码文件生成tokenList
    public List<TokenElement> getTokenList(String fileContent) {
        List<TokenElement> tokenList = new ArrayList<>();
        AssistNode assistNode = new AssistNode(0,0);
        while(assistNode.getIndex()<fileContent.length()){
            TokenElement token = analyzeCode(fileContent,assistNode);
            if(token.getItemIdentifier()!=ConstValues.NONE)
                tokenList.add(token);
        }
        tokenList.add(new TokenElement(-1,ConstValues.EOF,"#"));
        return tokenList;
    }

    //采用直接转向法实现自动机
    public TokenElement analyzeCode(String code, AssistNode node) {
        int curState = 0;
        TokenElement tokenElement = new TokenElement(0,ConstValues.NONE,"");
        boolean flag = true;
        while(flag && node.getIndex()<code.length()) {
            switch (curState) {
                case 0: {
                    if ((code.charAt(node.getIndex()) >= 'a' && code.charAt(node.getIndex()) <= 'z') ||
                            (code.charAt(node.getIndex()) >= 'A' && code.charAt(node.getIndex()) <= 'Z')) curState = 1;
                    else if (code.charAt(node.getIndex()) >= '0' && code.charAt(node.getIndex()) <= '9') curState = 2;
                    else if (singleDelimiter.contains(code.charAt(node.getIndex()))) curState = 3;
                    else if (code.charAt(node.getIndex()) == ':') curState = 4;
                    else if (code.charAt(node.getIndex()) == '.') curState = 5;
                    else if (code.charAt(node.getIndex()) == '{') curState = 6;
                    else if (code.charAt(node.getIndex()) == '\'') curState = 7;
                    else if (code.charAt(node.getIndex()) == ' ' || code.charAt(node.getIndex()) == '\t') node.setIndex(node.getIndex()+1);
                    else if (code.charAt(node.getIndex()) == '\n') {
                        node.setLineNum(node.getLineNum()+1);
                        node.setIndex(node.getIndex()+1);
                    }
                    else {
                        tokenElement = new TokenElement(node.getLineNum(),ConstValues.ERROR,Character.toString(code.charAt(node.getIndex())));
                        node.setIndex(node.getIndex()+1);
                        flag = false;
                    }
                    break;
                }
                case 1: {
                    StringBuilder content = new StringBuilder();
                    while((code.charAt(node.getIndex()) >= 'a' && code.charAt(node.getIndex()) <= 'z') || (code.charAt(node.getIndex()) >= 'A' &&code.charAt(node.getIndex()) <= 'Z')
                    || (code.charAt(node.getIndex()) >= '0' && code.charAt(node.getIndex()) <= '9')){
                        content.append(code.charAt(node.getIndex()));
                        node.setIndex(node.getIndex()+1);
                    }
                    if(Reserved.isReservedWord(content.toString()))
                        tokenElement = new TokenElement(node.getLineNum(), ConstValues.RESERVED_ITEM,content.toString());
                    else
                        tokenElement = new TokenElement(node.getLineNum(), ConstValues.IDENTIFER_ITEM, content.toString());
                    flag = false;
                    break;
                }
                case 2: {
                    StringBuilder content = new StringBuilder();
                    while (code.charAt(node.getIndex()) >= '0' && code.charAt(node.getIndex()) <= '9') {
                        content.append(code.charAt(node.getIndex()));
                        node.setIndex(node.getIndex()+1);
                    }
                    tokenElement = new TokenElement(node.getLineNum(),ConstValues.CONSTANT_ITEM,content.toString());
                    flag = false;
                    break;
                }
                case 3: {
                    tokenElement = new TokenElement(node.getLineNum(),ConstValues.SINGLE_DELIMITER_ITEM,
                            Character.toString(code.charAt(node.getIndex())));
                    node.setIndex(node.getIndex()+1);
                    flag = false;
                    break;
                }
                case 4: {
                    if(code.charAt(node.getIndex()+1)=='=') {
                        tokenElement = new TokenElement(node.getLineNum(),ConstValues.ASSIGN_ITEM,":=");
                        node.setIndex(node.getIndex()+1);
                    }
                    else{
                        tokenElement = new TokenElement(node.getLineNum(),ConstValues.ERROR,Character.toString(code.charAt(node.getIndex())));
                    }
                    node.setIndex(node.getIndex()+1);
                    flag = false;
                    break;
                }
                case 5: {
                    if(code.charAt(node.getIndex()+1)=='.') {
                        tokenElement = new TokenElement(node.getLineNum(),ConstValues.ARRAY_RANGE,"..");
                        node.setIndex(node.getIndex()+1);
                    }
                    else {
                        tokenElement = new TokenElement(node.getLineNum(),ConstValues.EOF,".");
                    }
                    node.setIndex(node.getIndex()+1);
                    flag = false;
                    break;
                }
                case 6: {
                    StringBuilder content = new StringBuilder();
                    node.setIndex(node.getIndex()+1);
                    while(node.getIndex() < code.length() && code.charAt(node.getIndex())!='}'){
                        content.append(code.charAt(node.getIndex()));
                        node.setIndex(node.getIndex()+1);
                    }
                    tokenElement = new TokenElement(node.getLineNum(),ConstValues.COMMENT_ITEM,content.toString());
                    node.setIndex(node.getIndex()+1);
                    flag = false;
                    break;
                }
                case 7: {
                    int index = code.indexOf('\'',node.getIndex()+1);
                    if(index==-1){
                        tokenElement = new TokenElement(node.getLineNum(),ConstValues.ERROR,"\'");
                    }
                    if((index-node.getIndex()) == 2) {
                        tokenElement = new TokenElement(node.getLineNum(),ConstValues.CHAR_ITEM,Character.toString(code.charAt(node.getIndex()+1)));
                    }
                    if((index-node.getIndex()) > 2) {
                        tokenElement = new TokenElement(node.getLineNum(),ConstValues.ERROR,code.substring(node.getIndex(),index+1));
                    }
                    node.setIndex(index == -1 ? node.getIndex()+1 : index+1);
                    flag = false;
                    break;
                }

            }
        }
        return tokenElement;
    }

}


//辅助类用于传参
class AssistNode {
    private int lineNum = 0; //行号
    private int index = 0;   //当前字符串处理位置

    public AssistNode(int lineNum, int index) {
        this.lineNum = lineNum;
        this.index = index;
    }

    public int getLineNum() {
        return lineNum;
    }

    public void setLineNum(int lineNum) {
        this.lineNum = lineNum;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}

class Reserved {
    //保留字表
    private static ArrayList<String> reservedWords = new ArrayList<String>() {
        {
            add("program");
            add("type");
            add("var");
            add("procedure");
            add("begin");
            add("end");
            add("array");
            add("of");
            add("record");
            add("if");
            add("then");
            add("else");
            add("fi");
            add("while");
            add("do");
            add("endwh");
            add("read");
            add("write");
            add("return");
            add("integer");
            add("char");
        }
    };

    public static boolean isReservedWord(String word) {
        if (reservedWords.contains(word)) {
            return true;
        }
        return false;
    }

}


