package SyntaxAnalysis;

import entity.ConstValues;
import entity.TokenElement;

import java.util.*;

public class Analyszer {
    Stack<Symple> analyseStack; //分析栈
    Deque<TokenElement> elementQueue;  //token序列
    Syntax LL1Syntax;  //文法
    public Analyszer(){
        this.analyseStack = new Stack<>();
        this.elementQueue = new ArrayDeque<>();
        this.analyseStack.add(new Symple(ConstValues.EMPTY,"#"));
        this.analyseStack.add(new Symple(ConstValues.N_SYMPLE,"Program"));
    }

    public Analyszer(Syntax syntax){
        this.LL1Syntax = syntax;
        this.analyseStack = new Stack<>();
        this.elementQueue = new ArrayDeque<>();
        this.analyseStack.add(new Symple(ConstValues.EMPTY,"#"));
        this.analyseStack.add(new Symple(ConstValues.N_SYMPLE,"Program"));
    }

    //设置LL1文法
    public void setLL1Syntax(Syntax LL1Syntax) {
        this.LL1Syntax = LL1Syntax;
    }

    //通过分析表分析
    public void parserByPredictTable(List<TokenElement> tokenElements){
        this.elementQueue.addAll(tokenElements);
        while (this.elementQueue.size()>0){
            Symple symple = this.analyseStack.peek();
            if(symple.getIdentity()==ConstValues.EMPTY)
                this.analyseStack.pop();
            else if(symple.getIdentity()==ConstValues.N_SYMPLE){
                processN(symple);
            }
            else{
                processT(symple);
            }
        }
    }

    private void processN(Symple symple){
        TokenElement tokenElement = this.elementQueue.getFirst();
        Symple cur = convertTokentoSymple(tokenElement);
        if(cur.getContent().equals("#")){
            this.elementQueue.pop();
            return ;
        }
        List<Symple> useExp = LL1Syntax.finduseExp(symple,cur);
        if(useExp==null){
            System.out.println("输入未知符号");
            return ;
        }
        else{
//            if(!useExp.contains(symple)) {
//                System.out.println(tokenElement.getSemanticsInfo()+"不应该输入"+"应输入"+symple.getContent());
//                return ;
//            }
            this.analyseStack.pop();
            for(int i = useExp.size()-1;i>=0;i--)
                this.analyseStack.push(useExp.get(i));

        }

    }

    private void processT(Symple symple){
        TokenElement token = this.elementQueue.peek();
        Symple cur = convertTokentoSymple(token);
        if(cur.getContent().equals("#")){
            this.elementQueue.pop();
            return ;
        }
        if(Objects.equals(cur.getContent(), symple.getContent())){
            System.out.println("match\t"+cur.content);
            this.elementQueue.pop();
            this.analyseStack.pop();
        }
        else {
            System.out.println("应该输入"+symple.getContent()+"但是输入"+token.getSemanticsInfo());
            return ;
        }
    }

    //将token换成对应的Symple
    private Symple convertTokentoSymple(TokenElement token){
        if(token.getItemIdentifier()==ConstValues.IDENTIFER_ITEM)
            return new Symple(ConstValues.T_SYMPLE,"id");
        else if(token.getItemIdentifier()==ConstValues.CONSTANT_ITEM)
            return new Symple(ConstValues.T_SYMPLE,"intc");
        else if(token.getItemIdentifier()==ConstValues.CHAR_ITEM)
            return new Symple(ConstValues.T_SYMPLE,"char");
        return new Symple(ConstValues.T_SYMPLE,token.getSemanticsInfo());
    }

    //递归下降法
    public void parserByRecursion(List<TokenElement> tokenList){

    }



}
