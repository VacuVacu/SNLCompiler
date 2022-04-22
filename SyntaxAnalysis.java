package syntax;

import com.sun.org.apache.xml.internal.resolver.readers.ExtendedXMLCatalogReader;
import lex.LexItem;
import lex.Token;
import lex.WordAutomat;
import node.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

//文法分析
public class SyntaxAnalysis {
    private int PredictTable[][] = new int[106][45];
    private Stack<Integer> signStack;  //符号栈
    private int tokenIndex;   //当前处理token下标
    private Stack<TreeNode>    treeStack;  //语法树栈
    private WordAutomat wordAutomat;
    private TreeNode root;  //语法树根节点
    private Stack<TreeNode> preF;  //cur前一个指向
    private List<TreeNode> Exps;  //符号栈 用来中缀转后缀
    private TreeNode cur;    //当前栈中最接近栈顶的父节点
    private int expflag = 0;

    void createPredictTable(){
        PredictTable[Symple.Program][-LexItem.PROGRAM] = 1;

        PredictTable[Symple.ProgramHead][-LexItem.PROGRAM] = 2;

        PredictTable[Symple.ProgramName][-LexItem.ID] = 3;

        PredictTable[Symple.DeclarePart][-LexItem.TYPE] = 4;
        PredictTable[Symple.DeclarePart][-LexItem.VAR] = 4;
        PredictTable[Symple.DeclarePart][-LexItem.PROCEDURE] = 4;
        PredictTable[Symple.DeclarePart][-LexItem.BEGIN] = 4;

        PredictTable[Symple.TypeDec][-LexItem.VAR] = 5;
        PredictTable[Symple.TypeDec][-LexItem.PROCEDURE] = 5;
        PredictTable[Symple.TypeDec][-LexItem.BEGIN] = 5;

        PredictTable[Symple.TypeDec][-LexItem.TYPE] = 6;

        PredictTable[Symple.TypeDeclaration][-LexItem.TYPE] = 7;

        PredictTable[Symple.TypeDecList][-LexItem.ID] = 8;

        PredictTable[Symple.TypeDecMore][-LexItem.VAR] = 9;
        PredictTable[Symple.TypeDecMore][-LexItem.PROCEDURE] = 9;
        PredictTable[Symple.TypeDecMore][-LexItem.BEGIN] = 9;

        PredictTable[Symple.TypeDecMore][-LexItem.ID] = 10;

        PredictTable[Symple.TypeId][-LexItem.ID] = 11;

        PredictTable[Symple.TypeName][-LexItem.INTEGER] = 12;
        PredictTable[Symple.TypeName][-LexItem.CHAR] = 12;

        PredictTable[Symple.TypeName][-LexItem.ARRAY] = 13;
        PredictTable[Symple.TypeName][-LexItem.RECORD] = 13;

        PredictTable[Symple.TypeName][-LexItem.ID] = 14;

        PredictTable[Symple.BaseType][-LexItem.INTEGER] = 15;

        PredictTable[Symple.BaseType][-LexItem.CHAR] = 16;

        PredictTable[Symple.StructureType][-LexItem.ARRAY] = 17;

        PredictTable[Symple.StructureType][-LexItem.RECORD] = 18;

        PredictTable[Symple.ArrayType][-LexItem.ARRAY] = 19;

        PredictTable[Symple.Low][-LexItem.INTC] = 20;

        PredictTable[Symple.Top][-LexItem.INTC] = 21;

        PredictTable[Symple.RecType][-LexItem.RECORD] = 22;

        PredictTable[Symple.FieldDecList][-LexItem.INTEGER] = 23;
        PredictTable[Symple.FieldDecList][-LexItem.CHAR] = 23;

        PredictTable[Symple.FieldDecList][-LexItem.ARRAY] = 24;

        PredictTable[Symple.FieldDecMore][-LexItem.END] = 25;

        PredictTable[Symple.FieldDecMore][-LexItem.INTEGER] = 26;
        PredictTable[Symple.FieldDecMore][-LexItem.CHAR] = 26;
        PredictTable[Symple.FieldDecMore][-LexItem.ARRAY] = 26;

        PredictTable[Symple.IdList][-LexItem.ID] = 27;

        PredictTable[Symple.IdMore][-LexItem.SEMI] = 28;

        PredictTable[Symple.IdMore][-LexItem.COMMA] = 29;

        PredictTable[Symple.VarDec][-LexItem.PROCEDURE] = 30;
        PredictTable[Symple.VarDec][-LexItem.BEGIN] = 30;

        PredictTable[Symple.VarDec][-LexItem.VAR] = 31;

        PredictTable[Symple.VarDeclaration][-LexItem.VAR] = 32;

        PredictTable[Symple.VarDecList][-LexItem.INTEGER] = 33;
        PredictTable[Symple.VarDecList][-LexItem.CHAR] = 33;
        PredictTable[Symple.VarDecList][-LexItem.ARRAY] = 33;
        PredictTable[Symple.VarDecList][-LexItem.RECORD] = 33;
        PredictTable[Symple.VarDecList][-LexItem.ID] = 33;

        PredictTable[Symple.VarDecMore][-LexItem.PROCEDURE] = 34;
        PredictTable[Symple.VarDecMore][-LexItem.BEGIN] = 34;

        PredictTable[Symple.VarDecMore][-LexItem.INTEGER] = 35;
        PredictTable[Symple.VarDecMore][-LexItem.CHAR] = 35;
        PredictTable[Symple.VarDecMore][-LexItem.ARRAY] = 35;
        PredictTable[Symple.VarDecMore][-LexItem.RECORD] = 35;
        PredictTable[Symple.VarDecMore][-LexItem.ID] = 35;

        PredictTable[Symple.VarIdList][-LexItem.ID] = 36;

        PredictTable[Symple.VarIdMore][-LexItem.SEMI] = 37;

        PredictTable[Symple.VarIdMore][-LexItem.COMMA] = 38;

        PredictTable[Symple.ProcDec][-LexItem.BEGIN] = 39;

        PredictTable[Symple.ProcDec][-LexItem.PROCEDURE] = 40;

        PredictTable[Symple.ProcDeclaration][-LexItem.PROCEDURE] = 41;

        PredictTable[Symple.ProcDecMore][-LexItem.BEGIN] = 42;

        PredictTable[Symple.ProcDecMore][-LexItem.PROCEDURE] = 43;

        PredictTable[Symple.ProcDecMore][-LexItem.DOT] = 42;

        PredictTable[Symple.ProcName][-LexItem.ID] = 44;

        PredictTable[Symple.ParamList][-LexItem.RPAREN] = 45;

        PredictTable[Symple.ParamList][-LexItem.INTEGER] = 46;
        PredictTable[Symple.ParamList][-LexItem.CHAR] = 46;
        PredictTable[Symple.ParamList][-LexItem.ARRAY] = 46;
        PredictTable[Symple.ParamList][-LexItem.RECORD] = 46;
        PredictTable[Symple.ParamList][-LexItem.ID] = 46;
        PredictTable[Symple.ParamList][-LexItem.VAR] = 46;

        PredictTable[Symple.ParamDecList][-LexItem.INTEGER] = 47;
        PredictTable[Symple.ParamDecList][-LexItem.CHAR] = 47;
        PredictTable[Symple.ParamDecList][-LexItem.ARRAY] = 47;
        PredictTable[Symple.ParamDecList][-LexItem.RECORD] = 47;
        PredictTable[Symple.ParamDecList][-LexItem.ID] = 47;
        PredictTable[Symple.ParamDecList][-LexItem.VAR] = 47;

        PredictTable[Symple.ParamMore][-LexItem.RPAREN] = 48;

        PredictTable[Symple.ParamMore][-LexItem.SEMI] = 49;

        PredictTable[Symple.Param][-LexItem.INTEGER] = 50;
        PredictTable[Symple.Param][-LexItem.CHAR] = 50;
        PredictTable[Symple.Param][-LexItem.ARRAY] = 50;
        PredictTable[Symple.Param][-LexItem.RECORD] = 50;
        PredictTable[Symple.Param][-LexItem.ID] = 50;

        PredictTable[Symple.Param][-LexItem.VAR] = 51;

        PredictTable[Symple.FormList][-LexItem.ID] = 52;

        PredictTable[Symple.FidMore][-LexItem.SEMI] = 53;
        PredictTable[Symple.FidMore][-LexItem.RPAREN] = 53;

        PredictTable[Symple.FidMore][-LexItem.COMMA] = 54;

        PredictTable[Symple.ProcDecPart][-LexItem.TYPE] = 55;
        PredictTable[Symple.ProcDecPart][-LexItem.VAR] = 55;
        PredictTable[Symple.ProcDecPart][-LexItem.PROCEDURE] = 55;
        PredictTable[Symple.ProcDecPart][-LexItem.BEGIN] = 55;

        PredictTable[Symple.ProcBody][-LexItem.BEGIN] = 56;

        PredictTable[Symple.ProgramBody][-LexItem.BEGIN] = 57;

        PredictTable[Symple.StmList][-LexItem.ID] = 58;
        PredictTable[Symple.StmList][-LexItem.IF] = 58;
        PredictTable[Symple.StmList][-LexItem.WHILE] = 58;
        PredictTable[Symple.StmList][-LexItem.RETURN] = 58;
        PredictTable[Symple.StmList][-LexItem.READ] = 58;
        PredictTable[Symple.StmList][-LexItem.WRITE] = 58;

        PredictTable[Symple.StmMore][-LexItem.END] = 59;
        PredictTable[Symple.StmMore][-LexItem.ENDWH] = 59;
        PredictTable[Symple.StmMore][-LexItem.ELSE] = 59;
        PredictTable[Symple.StmMore][-LexItem.FI] = 59;

        PredictTable[Symple.StmMore][-LexItem.SEMI] = 60;

        PredictTable[Symple.Stm][-LexItem.IF] = 61;

        PredictTable[Symple.Stm][-LexItem.WHILE] = 62;

        PredictTable[Symple.Stm][-LexItem.READ] = 63;

        PredictTable[Symple.Stm][-LexItem.WRITE] = 64;

        PredictTable[Symple.Stm][-LexItem.RETURN] = 65;

        PredictTable[Symple.Stm][-LexItem.ID] = 66;

        PredictTable[Symple.AssCall][-LexItem.ASSIGN] = 67;
        PredictTable[Symple.AssCall][-LexItem.LMIDPAREN] = 67;
        PredictTable[Symple.AssCall][-LexItem.DOT] = 67;

        PredictTable[Symple.AssCall][-LexItem.LPAREN] = 68;

        PredictTable[Symple.AssignmentRest][-LexItem.ASSIGN] = 69;
        PredictTable[Symple.AssignmentRest][-LexItem.LMIDPAREN] = 69;
        PredictTable[Symple.AssignmentRest][-LexItem.DOT] = 69;

        PredictTable[Symple.ConditionalStm][-LexItem.IF] = 70;

        PredictTable[Symple.LoopStm][-LexItem.WHILE] = 71;

        PredictTable[Symple.InputStm][-LexItem.READ] = 72;

        PredictTable[Symple.InVar][-LexItem.ID] = 73;

        PredictTable[Symple.OutputStm][-LexItem.WRITE] = 74;

        PredictTable[Symple.ReturnStm][-LexItem.RETURN] = 75;

        PredictTable[Symple.CallStmRest][-LexItem.LPAREN] = 76;

        PredictTable[Symple.ActParamList][-LexItem.RPAREN] = 77;

        PredictTable[Symple.ActParamList][-LexItem.ID] = 78;
        PredictTable[Symple.ActParamList][-LexItem.INTC] = 78;
        PredictTable[Symple.ActParamList][-LexItem.LPAREN] = 78;

        PredictTable[Symple.ActParamMore][-LexItem.RPAREN] = 79;

        PredictTable[Symple.ActParamMore][-LexItem.COMMA] = 80;

        PredictTable[Symple.RelExp][-LexItem.LPAREN] = 81;
        PredictTable[Symple.RelExp][-LexItem.INTC] = 81;
        PredictTable[Symple.RelExp][-LexItem.ID] = 81;

        PredictTable[Symple.OtherRelE][-LexItem.LT] = 82;
        PredictTable[Symple.OtherRelE][-LexItem.EQ] = 82;

        PredictTable[Symple.Exp][-LexItem.LPAREN] = 83;
        PredictTable[Symple.Exp][-LexItem.INTC] = 83;
        PredictTable[Symple.Exp][-LexItem.ID] = 83;
        PredictTable[Symple.Exp][-LexItem.CHARC] = 83;

        PredictTable[Symple.OtherTerm][-LexItem.LT] = 84;
        PredictTable[Symple.OtherTerm][-LexItem.EQ] = 84;
        PredictTable[Symple.OtherTerm][-LexItem.THEN] = 84;
        PredictTable[Symple.OtherTerm][-LexItem.DO] = 84;
        PredictTable[Symple.OtherTerm][-LexItem.RPAREN] = 84;
        PredictTable[Symple.OtherTerm][-LexItem.END] = 84;
        PredictTable[Symple.OtherTerm][-LexItem.SEMI] = 84;
        PredictTable[Symple.OtherTerm][-LexItem.COMMA] = 84;
        PredictTable[Symple.OtherTerm][-LexItem.ENDWH] = 84;
        PredictTable[Symple.OtherTerm][-LexItem.ELSE] = 84;
        PredictTable[Symple.OtherTerm][-LexItem.FI] = 84;
        PredictTable[Symple.OtherTerm][-LexItem.RMIDPAREN] = 84;

        PredictTable[Symple.OtherTerm][-LexItem.PLUS] = 85;
        PredictTable[Symple.OtherTerm][-LexItem.MINUS] = 85;

        PredictTable[Symple.Term][-LexItem.LPAREN] = 86;
        PredictTable[Symple.Term][-LexItem.INTC] = 86;
        PredictTable[Symple.Term][-LexItem.ID] = 86;
        PredictTable[Symple.Term][-LexItem.CHARC] = 86;


        PredictTable[Symple.OtherFactor][-LexItem.PLUS] = 87;
        PredictTable[Symple.OtherFactor][-LexItem.MINUS] = 87;
        PredictTable[Symple.OtherFactor][-LexItem.LT] = 87;
        PredictTable[Symple.OtherFactor][-LexItem.EQ] = 87;
        PredictTable[Symple.OtherFactor][-LexItem.THEN] = 87;
        PredictTable[Symple.OtherFactor][-LexItem.ELSE] = 87;
        PredictTable[Symple.OtherFactor][-LexItem.FI] = 87;
        PredictTable[Symple.OtherFactor][-LexItem.DO] = 87;
        PredictTable[Symple.OtherFactor][-LexItem.ENDWH] = 87;
        PredictTable[Symple.OtherFactor][-LexItem.RPAREN] = 87;
        PredictTable[Symple.OtherFactor][-LexItem.END] = 87;
        PredictTable[Symple.OtherFactor][-LexItem.SEMI] = 87;
        PredictTable[Symple.OtherFactor][-LexItem.COMMA] = 87;
        PredictTable[Symple.OtherFactor][-LexItem.RMIDPAREN] = 87;

        PredictTable[Symple.OtherFactor][-LexItem.TIMES] = 88;
        PredictTable[Symple.OtherFactor][-LexItem.OVER] = 88;

        PredictTable[Symple.Factor][-LexItem.LPAREN] = 89;

        PredictTable[Symple.Factor][-LexItem.INTC] = 90;

        PredictTable[Symple.Factor][-LexItem.ID] = 91;

        PredictTable[Symple.Variable][-LexItem.ID] = 92;

        PredictTable[Symple.VariMore][-LexItem.ASSIGN] = 93;
        PredictTable[Symple.VariMore][-LexItem.TIMES] = 93;
        PredictTable[Symple.VariMore][-LexItem.OVER] = 93;
        PredictTable[Symple.VariMore][-LexItem.PLUS] = 93;
        PredictTable[Symple.VariMore][-LexItem.MINUS] = 93;
        PredictTable[Symple.VariMore][-LexItem.LT] = 93;
        PredictTable[Symple.VariMore][-LexItem.EQ] = 93;
        PredictTable[Symple.VariMore][-LexItem.THEN] = 93;
        PredictTable[Symple.VariMore][-LexItem.ELSE] = 93;
        PredictTable[Symple.VariMore][-LexItem.FI] = 93;
        PredictTable[Symple.VariMore][-LexItem.DO] = 93;
        PredictTable[Symple.VariMore][-LexItem.ENDWH] = 93;
        PredictTable[Symple.VariMore][-LexItem.RPAREN] = 93;
        PredictTable[Symple.VariMore][-LexItem.END] = 93;
        PredictTable[Symple.VariMore][-LexItem.SEMI] = 93;
        PredictTable[Symple.VariMore][-LexItem.COMMA] = 93;
        PredictTable[Symple.VariMore][-LexItem.RMIDPAREN] = 93;

        PredictTable[Symple.VariMore][-LexItem.LMIDPAREN] = 94;

        PredictTable[Symple.VariMore][-LexItem.DOT] = 95;

        PredictTable[Symple.FieldVar][-LexItem.ID] = 96;

        PredictTable[Symple.FieldVarMore][-LexItem.ASSIGN] = 97;
        PredictTable[Symple.FieldVarMore][-LexItem.TIMES] = 97;
        PredictTable[Symple.FieldVarMore][-LexItem.OVER] = 97;
        PredictTable[Symple.FieldVarMore][-LexItem.PLUS] = 97;
        PredictTable[Symple.FieldVarMore][-LexItem.MINUS] = 97;
        PredictTable[Symple.FieldVarMore][-LexItem.LT] = 97;
        PredictTable[Symple.FieldVarMore][-LexItem.EQ] = 97;
        PredictTable[Symple.FieldVarMore][-LexItem.THEN] = 97;
        PredictTable[Symple.FieldVarMore][-LexItem.ELSE] = 97;
        PredictTable[Symple.FieldVarMore][-LexItem.FI] = 97;
        PredictTable[Symple.FieldVarMore][-LexItem.DO] = 97;
        PredictTable[Symple.FieldVarMore][-LexItem.ENDWH] = 97;
        PredictTable[Symple.FieldVarMore][-LexItem.RPAREN] = 97;
        PredictTable[Symple.FieldVarMore][-LexItem.END] = 97;
        PredictTable[Symple.FieldVarMore][-LexItem.SEMI] = 97;
        PredictTable[Symple.FieldVarMore][-LexItem.COMMA] = 97;
        PredictTable[Symple.FieldVarMore][-LexItem.LMIDPAREN] = 98;
        PredictTable[Symple.CmpOp][-LexItem.LT] = 99;
        PredictTable[Symple.CmpOp][-LexItem.EQ] = 100;
        PredictTable[Symple.AddOp][-LexItem.PLUS] = 101;
        PredictTable[Symple.AddOp][-LexItem.MINUS] = 102;
        PredictTable[Symple.MultOp][-LexItem.TIMES] = 103;
        PredictTable[Symple.MultOp][-LexItem.OVER] = 104;

    }
    public SyntaxAnalysis(WordAutomat wordAutomat){
        this.wordAutomat = wordAutomat;
        for (int i = 0; i < 106; i++)
            for (int j = 0; j < 45; j++) {
                PredictTable[i][j] = 0;
            }
        root = new TreeNode();
        cur = root;
        root.nodeKind = NodeIdentity.ProK;
        signStack = new Stack<Integer>();
        treeStack = new Stack<TreeNode>();
        Exps = new ArrayList<>();
        preF = new Stack<>();
        tokenIndex = 0;
        expflag = 0;
        createPredictTable();
        signStack.push(Symple.EOF);
        signStack.push(Symple.Program);
    }

    protected boolean match(Token expected, int lexItem) {
        if (expected.getLexType() == lexItem)
            return true;
        else {
            //错误处理
//            errorList.addError(new Error("Syntax Error>>>Unexpected token "
//                    + expected.getSem(), expected.getLineNum()));
            return false;
        }
    }

    int getNodeLexItem(TreeNode node){
        ExpAttr attr = (ExpAttr) node.attr;
        if(attr==null){
            System.out.println("出错");
            return -1001;
        }
       return attr.LexItem;
    }


    //检查是否是分界
    boolean isEnd(TreeNode node){
        if(node.nodeKind==0)
            return true;
        return false;
    }

    //中缀转后缀
    Stack<TreeNode> transform(){
        Stack<TreeNode> backExp = new Stack<>();
        Stack<TreeNode> opStack = new Stack<>();
        while(!Exps.isEmpty()&&!isEnd(Exps.get(0))){
            TreeNode node = Exps.remove(0);
//            if(isEnd(node)||Exps.size()==0) break;
            if(getNodeLexItem(node)==LexItem.PLUS||getNodeLexItem(node)==LexItem.MINUS)
                operationOpStack(opStack,backExp,node,1);
            else if(getNodeLexItem(node)==LexItem.TIMES||getNodeLexItem(node)==LexItem.OVER)
                operationOpStack(opStack,backExp,node,2);
            else if(getNodeLexItem(node)==LexItem.LPAREN)
                backExp.push(node);
            else if(getNodeLexItem(node)==LexItem.RPAREN){
                operationParen(opStack,backExp);
                expflag--;
            }
            else
                backExp.push(node);
        }

        return backExp;
    }

    public void operationOpStack(Stack<TreeNode> opStack,Stack<TreeNode> backExp,TreeNode opThis, int prec1) {//运算符栈操作
        while (!opStack.isEmpty()) {
            TreeNode opTop = opStack.pop();
            if (getNodeLexItem(opTop)==LexItem.LPAREN) {
                opStack.push(opTop);
            }
            else {
                int prec2;
                if (getNodeLexItem(opTop)==LexItem.PLUS||getNodeLexItem(opTop)==LexItem.MINUS)
                    prec2 = 1;
                else
                    prec2 = 2;
                if (prec2 < prec1) {
                    opStack.push(opTop);
                    break;
                }
                else
                    backExp.push(opTop);
            }
        }
        opStack.push(opThis);
    }

    public void operationParen(Stack<TreeNode> opStack,Stack<TreeNode> backExp) {
        while (!opStack.isEmpty()) {
            TreeNode c = opStack.pop();
            if (getNodeLexItem(c)==LexItem.LPAREN)
                break;
            else
                backExp.push(c);
        }
    }

    public void parserByPredictTable(){
        treeStack.push(root);
        while (!signStack.empty()){
            Token token = wordAutomat.getToken(tokenIndex);
            if(token.getLexType()==LexItem.COMMENT){
                tokenIndex++;
                continue;
            }
            if(token!=null){
                int topSign = signStack.peek();
                if(topSign>0){
                    int key = PredictTable[topSign][-token.getLexType()];
                    switch (key){
                        case 0: break;//出错
                        case 1: process1(); break;
                        case 2: process2(token);break;
                        case 3: process3(token);break;
                        case 4: process4();break;
                        case 5: process5();break;
                        case 6: process6();break;
                        case 7: process7(token);break;
                        case 8: process8();break;
                        case 9: process9();break;
                        case 10: process10();break;
                        case 11: process11(token);break;
                        case 12:process12(token);break;
                        case 13:process13(token);break;
                        case 14:process14(token);break;
                        case 15:process15(token);break;
                        case 16:process16(token);break;
                        case 17:process17();break;
                        case 18:process18();break;
                        case 19:process19(token);break;
                        case 20:process20(token);break;
                        case 21:process21(token);break;
                        case 22:process22(token);break;
                        case 23:process23();break;
                        case 24:process24();break;
                        case 25:process25();break;
                        case 26:process26();break;
                        case 27:process27();break;
                        case 28:process28();break;
                        case 29:process29();break;
                        case 30:process30();break;
                        case 31:process31();break;
                        case 32:process32(token);break;
                        case 33:process33(token);break;
                        case 34:process34();break;
                        case 35:process35();break;
                        case 36:process36(token);break;
                        case 37:process37();break;
                        case 38:process38(token);break;
                        case 39:process39();break;
                        case 40:process40();break;
                        case 41:process41(token);break;
                        case 42:process42();break;
                        case 43:process43();break;
                        case 44:process44(token);break;
                        case 45:process45();break;
                        case 46:process46();break;
                        case 47:process47();break;
                        case 48:process48();break;
                        case 49:process49(token);break;
                        case 50:process50();break;
                        case 51:process51(token);break;
                        case 52:process52(token);break;
                        case 53:process53();break;
                        case 54:process54(token);break;
                        case 55:process55();break;
                        case 56:process56();break;
                        case 57:process57(token);break;
                        case 58:process58();break;
                        case 59:process59();break;
                        case 60:process60(token);break;
                        case 61:process61();break;
                        case 62:process62();break;
                        case 63:process63();break;
                        case 64:process64();break;
                        case 65:process65();break;
                        case 66:process66(token);break;
                        case 67:process67();break;
                        case 68:process68();break;
                        case 69:process69();break;
                        case 70:process70(token);break;
                        case 71:process71(token);break;
                        case 72:process72(token);break;
                        case 73:process73(token);break;
                        case 74:process74(token);break;
                        case 75:process75(token);break;
                        case 76:process76(token);break;
                        case 77:process77();break;
                        case 78:process78();break;
                        case 79:process79();break;
                        case 80:process80(token);break;
                        case 81:process81();break;
                        case 82:process82();break;
                        case 83:process83();break;
                        case 84:process84(token);break;
                        case 85:process85();break;
                        case 86:process86();break;
                        case 87:process87(token);break;
                        case 88:process88(token);break;
                        case 89:process89(token);break;
                        case 90:process90(token);break;
                        case 91:process91();break;
                        case 92:process92(token);break;
                        case 93:process93(token);break;
                        case 94:process94(token);break;
                        case 95:process95(token);break;
                        case 96:process96();break;
                        case 97:process97();break;
                        case 98:process98();break;
                        case 99:process99();break;
                        case 100:process100();break;
                        case 101:process101(token);break;
                        case 102:process102(token);break;
                        case 103:process103(token);break;
                        case 104:process104(token);break;

                    }
                }
                else{
                    if(match(token,LexItem.EQ)){
                        signStack.pop();
                        tokenIndex++;
                    }
                    else if(match(token,LexItem.SEMI)){
                        signStack.pop();
                        tokenIndex++;
                    }
                    else if(match(token, LexItem.LMIDPAREN)){
                        signStack.pop();
                        tokenIndex++;
                    }
                    else if(match(token,LexItem.RMIDPAREN)){
                        signStack.pop();
                        tokenIndex++;
                    }
                    else if(match(token,LexItem.LPAREN)){
                        signStack.pop();
                        tokenIndex++;
                    }
                    else if(match(token,LexItem.RPAREN)){
                        signStack.pop();
                        tokenIndex++;
                    }
                }

            }
        }

    }


    void process1(){
        signStack.pop();
        signStack.push(Symple.ProgramBody);
        signStack.push(Symple.DeclarePart);
        signStack.push(Symple.ProgramHead);
    }

    void process2(Token token){
        if(match(token,LexItem.PROGRAM)){
            signStack.pop();
            tokenIndex++;
            signStack.push(Symple.ProgramName);
            TreeNode node = new TreeNode();
            node.nodeKind = NodeIdentity.PheadK;
            TreeNode top = treeStack.peek();
            top.children[0] = node;
            treeStack.push(node);
        }else{
            System.out.println("出错");
        }
        //否则进入错误处理
    }

    void process3(Token token){
        signStack.pop();
        if(match(token,LexItem.ID)){
            TreeNode top = treeStack.pop();
            top.line = token.getLine();
            top.name.add(token.getSeminfo());
            tokenIndex++;
        }
        else{
            System.out.println("出错");
        }
    }

    void process4(){
        signStack.pop();
        signStack.push(Symple.ProcDec);
        signStack.push(Symple.VarDec);
        signStack.push(Symple.TypeDec);
    }

    void process5(){
        signStack.pop();
    }

    void process6(){
        signStack.pop();
        signStack.push(Symple.TypeDeclaration);
    }

    void process7(Token token){
        if(match(token,LexItem.TYPE)){
            signStack.pop();
            signStack.push(Symple.TypeDecList);
            tokenIndex++;
//            signStack.push(LexItem.TYPE);
            TreeNode node = new TreeNode();
            node.nodeKind = NodeIdentity.TypeK;
            TreeNode top = treeStack.peek();
            top.children[1] = node;
            treeStack.push(node);
        }else {
            System.out.println("出错");
        }
    }

    void process8(){
        signStack.pop();
        signStack.push(Symple.TypeDecMore);
        signStack.push(LexItem.SEMI);
        signStack.push(Symple.TypeName);
        signStack.push(LexItem.EQ);
        signStack.push(Symple.TypeId);
        this.preF.push(this.cur);
        this.cur = treeStack.peek();
        TreeNode node = new TreeNode();
        node.nodeKind = NodeIdentity.DecK;
        cur.children[0]=node;
        treeStack.push(node);
    }

    void process9(){
        signStack.pop();
        TreeNode node = treeStack.get(treeStack.size()-2);
        node.sibling = null;
        while(treeStack.peek()!=this.cur) treeStack.pop();
//        while(treeStack.peek().sibling!=null) treeStack.pop();
        treeStack.pop();
        this.cur = this.preF.pop();
    }

    void process10(){
        signStack.pop();
        signStack.push(Symple.TypeDecList);
    }

    void process11(Token token){
        signStack.pop();
        signStack.push(LexItem.ID);
        if(match(token,LexItem.ID)){
            TreeNode top = treeStack.peek();
            top.name.add(token.getSeminfo());
            top.line = token.getLine();
            signStack.pop();
            tokenIndex++;
        }
        else {
            System.out.println("出错");
        }
    }

    void process12(Token token){
        signStack.pop();
        signStack.push(Symple.BaseType);
    }

    void process13(Token token){
        signStack.pop();
        signStack.push(Symple.StructureType);
    }

    void process14(Token token){
        signStack.pop();
        if(match(token,LexItem.ID)){
            TreeNode top = treeStack.peek();
            if(this.cur.nodeKind==NodeIdentity.TypeK){
                top.kind = SubIdentity.IdK;
                top.name.add(token.getSeminfo());
                TreeNode node = new TreeNode();
                node.nodeKind = NodeIdentity.DecK;
                top.sibling = node;
                treeStack.push(node);
            }else if(this.cur.nodeKind==NodeIdentity.VarK){
                top.type_name = token.getSeminfo();
            }else if(top.kind==SubIdentity.ArrayK){
                ArrayAttr attr = (ArrayAttr)top.attr;
                attr.typename = token.getSeminfo();
            }else if(this.cur.nodeKind==NodeIdentity.ProcDecK){
                ProcAttr attr = (ProcAttr) top.attr;
                attr.type.add(token.getSeminfo());
            }
            tokenIndex++;
        }
    }

    void process15(Token token){
        signStack.pop();
        if(match(token,LexItem.INTEGER)){
            TreeNode top = treeStack.peek();
            if(this.cur.nodeKind==NodeIdentity.TypeK){
                top.kind = SubIdentity.IntegerK;
                TreeNode node = new TreeNode();
                node.nodeKind = NodeIdentity.DecK;
                top.sibling = node;
                treeStack.push(node);
            }
            else if(this.cur.nodeKind==NodeIdentity.VarK){
                top.type_name = "Integer";
            }
            else if(top.kind==SubIdentity.ArrayK){
                ArrayAttr attr = (ArrayAttr)top.attr;
                attr.typename = "Integer";
            }else if(this.cur.nodeKind==NodeIdentity.ProcDecK){
                ProcAttr attr = (ProcAttr) top.attr;
                attr.type.add("Integer");
            }
            tokenIndex++;
        }
        else{
            System.out.println("出错");
        }
    }

    void process16(Token token){
        signStack.pop();
        if(match(token,LexItem.CHAR)){
            TreeNode top = treeStack.peek();
            if(this.cur.nodeKind==NodeIdentity.TypeK){
                top.kind = SubIdentity.CharK;
                TreeNode node = new TreeNode();
                node.nodeKind = NodeIdentity.DecK;
                top.sibling = node;
                treeStack.push(node);
            }
            else if(this.cur.nodeKind==NodeIdentity.VarK){
                top.type_name = "Char";
            }
            else if(top.kind==SubIdentity.ArrayK){
                ArrayAttr attr = (ArrayAttr)top.attr;
                attr.typename = "Char";
            }else if(this.cur.nodeKind==NodeIdentity.ProcDecK){
                ProcAttr attr = (ProcAttr) top.attr;
                attr.type.add("Char");
            }
            tokenIndex++;
        }
        else{
            System.out.println("出错");
        }
    }

    void process17(){
        signStack.pop();
        signStack.push(Symple.ArrayType);
    }

    void process18(){
        signStack.pop();
        signStack.push(Symple.ArrayType);
    }

    void process19(Token token){
        signStack.pop();
        if(match(token,LexItem.ARRAY)){
            TreeNode top = treeStack.peek();
            top.kind = SubIdentity.ArrayK;
            tokenIndex++;
            TreeNode node = new TreeNode();
            node.nodeKind = NodeIdentity.DecK;
            node.attr = new ArrayAttr();
            node.line = token.getLine();
            top.sibling = node;
            treeStack.push(node);
            signStack.push(Symple.BaseType);
            signStack.push(LexItem.OF);
            signStack.push(LexItem.RMIDPAREN);
            signStack.push(Symple.Top);
            signStack.push(LexItem.UNDERANGE);
            signStack.push(Symple.Low);
            signStack.push(LexItem.LMIDPAREN);
        }
        else{
            System.out.println("出错");
        }
    }

    void process20(Token token){
        signStack.pop();
        if(match(token,LexItem.INTC)){
            TreeNode top = treeStack.peek();
            ArrayAttr attr = (ArrayAttr)top.attr;
            attr.low = token.getSeminfo();
            tokenIndex++;
        }

    }

    void process21(Token token){
        signStack.pop();
        if(match(token,LexItem.INTC)){
            TreeNode top = treeStack.peek();
            ArrayAttr attr = (ArrayAttr)top.attr;
            attr.high = token.getSeminfo();
            tokenIndex++;
        }
    }

    //处理记录类型
    void process22(Token token){

    }
    //处理记录类型
    void process23(){

    }
    //处理记录类型
    void process24(){

    }
    //处理记录类型
    void process25(){

    }
    //处理记录类型
    void process26(){

    }

    void process27(){

    }

    void process28(){

    }

    void process29(){

    }

    //处理Var
    void process30(){
        signStack.pop();
    }

    void process31(){
        signStack.pop();
        signStack.push(Symple.VarDeclaration);
    }

    void process32(Token token){
        signStack.pop();
        if(match(token,LexItem.VAR)){
            signStack.push(Symple.VarDecList);
            TreeNode top = treeStack.peek();
            TreeNode node = new TreeNode();
            top.children[2]=node;
            node.nodeKind = NodeIdentity.VarK;
            treeStack.push(node);
            tokenIndex++;
        }
        else {
            System.out.println("出错");
        }
    }

    void process33(Token token){
        signStack.pop();
        signStack.push(Symple.VarDecMore);
        signStack.push(LexItem.SEMI);
        signStack.push(Symple.VarIdList);
        signStack.push(Symple.TypeName);
        TreeNode top = treeStack.peek();
        if(top.nodeKind!=0) {
            this.cur = top;
            this.preF.push(this.cur);
            TreeNode node = new TreeNode();
            node.nodeKind = NodeIdentity.DecK;
            top.children[0] = node;
            treeStack.push(node);
        }
        else {
            top.nodeKind = NodeIdentity.DecK;
        }
    }

    void process34(){
        signStack.pop();
        TreeNode node = treeStack.get(treeStack.size()-2);
        node.sibling = null;
        while(treeStack.peek()!=this.cur) treeStack.pop();
//        while(treeStack.peek().sibling!=null) treeStack.pop();
        treeStack.pop();
//        this.cur = this.preF;
        this.cur = this.preF.pop();
    }


    void process35(){
        signStack.pop();
        signStack.push(Symple.VarDecList);
    }

    void process36(Token token){
        signStack.pop();
        if(match(token,LexItem.ID)){
            signStack.push(Symple.VarIdMore);
            TreeNode top = treeStack.peek();
            if(top.name.size()==0)
                top.line = token.getLine();
            top.name.add(token.getSeminfo());
            tokenIndex++;
        }
        else{
            System.out.println("出错");
        }
    }

    void process37(){
        signStack.pop();
        TreeNode node = new TreeNode();
        TreeNode top = treeStack.peek();
        top.sibling = node;
        treeStack.push(node);
    }

    void process38(Token token){
        signStack.pop();
        if(match(token,LexItem.COMMA)){
            signStack.push(Symple.VarIdList);
            tokenIndex++;
        }
    }

    void process39(){
        signStack.pop();
    }

    void process40(){
        signStack.pop();
        signStack.push(Symple.ProcDeclaration);
    }

    void process41(Token token){
        signStack.pop();
        if(match(token,LexItem.PROCEDURE)){
            tokenIndex++;
            signStack.push(Symple.ProcDecMore);
            signStack.push(Symple.ProcBody);
            signStack.push(Symple.ProcDecPart);
            signStack.push(LexItem.SEMI);
            signStack.push(LexItem.RPAREN);
            signStack.push(Symple.ParamList);
            signStack.push(LexItem.LPAREN);
            signStack.push(Symple.ProcName);
            TreeNode node = new TreeNode();
            node.nodeKind = NodeIdentity.ProcDecK;
            TreeNode top = treeStack.peek();
            if(top.nodeKind==NodeIdentity.ProcDecK) //一个主程序或过程声明多个过程
                top.sibling = node;
            else
                top.children[3]=node;
            treeStack.push(node);
        }else{
            System.out.println("出错");
        }
    }

    void process42(){
        signStack.pop();
        TreeNode node = treeStack.get(treeStack.size()-2);
        node.sibling = null;
        while(treeStack.peek()!=this.cur) treeStack.pop();
//        while(treeStack.peek().sibling!=null) treeStack.pop();
        treeStack.pop();
//        this.cur = this.preF;
        this.cur = this.preF.pop();
        signStack.pop();
    }

    void process43(){
        signStack.pop();
        signStack.push(Symple.ProcDeclaration);
    }

    void process44(Token token){
        signStack.pop();
        if(match(token,LexItem.ID)){
            TreeNode top = treeStack.peek();
            top.name.add(token.getSeminfo());
            top.line = token.getLine();
            this.cur = top;
            this.preF.push(this.cur);
            tokenIndex++;
        }
        else{
            System.out.println("出错");
        }
    }

    void process45(){
        signStack.pop();
    }

    void process46(){
        signStack.pop();
        signStack.push(Symple.ParamDecList);
        TreeNode top = treeStack.peek();
        top.attr = new ProcAttr();
    }

    void process47(){
        signStack.pop();
        signStack.push(Symple.ParamMore);
        signStack.push(Symple.Param);
    }

    void process48(){
        signStack.pop();
    }

    void process49(Token token){
        signStack.pop();
        if(match(token,LexItem.SEMI)){
            signStack.push(Symple.ParamDecList);
            tokenIndex++;
        }
        else{
            System.out.println("出错");
        }
    }

    void process50(){
        signStack.pop();
        signStack.push(Symple.FormList);
        signStack.push(Symple.TypeName);
        TreeNode top = treeStack.peek();
        ProcAttr attr = (ProcAttr) top.attr;
        attr.isVariable.add(false);
    }

    void process51(Token token){
        signStack.pop();
        if(match(token,LexItem.VAR)){
            signStack.push(Symple.FormList);
            signStack.push(Symple.TypeName);
            tokenIndex++;
            TreeNode top = treeStack.peek();
            ProcAttr attr = (ProcAttr) top.attr;
            attr.isVariable.add(true);
        }
    }

    void process52(Token token){
        signStack.pop();
        if(match(token,LexItem.ID)){
            tokenIndex++;
            signStack.push(Symple.FidMore);
            TreeNode top = treeStack.peek();
            ProcAttr attr = (ProcAttr) top.attr;
            attr.name.add(token.getSeminfo());
            attr.index++;
            if(attr.name.size()>attr.type.size()){
                attr.type.add(attr.type.get(attr.index-2));
                attr.isVariable.add(attr.isVariable.get(attr.index-2));
            }
        }
    }

    void process53(){
        signStack.pop();
    }

    void process54(Token token){
        signStack.pop();
        if(match(token,LexItem.COMMA)){
            tokenIndex++;
            signStack.push(Symple.FormList);
        }
    }

    void process55(){
        signStack.pop();
        signStack.push(Symple.DeclarePart);
    }

    void process56(){
        signStack.pop();
        signStack.push(Symple.ProcBody);
    }

    void process57(Token token){
        signStack.pop();
        if(match(token,LexItem.BEGIN)){
            signStack.push(LexItem.END);
            signStack.push(Symple.StmList);
            tokenIndex++;
            TreeNode node = new TreeNode();
            node.nodeKind = NodeIdentity.StmLK;
            TreeNode top = treeStack.peek();
            top.children[4]=node;
            treeStack.push(node);
            this.preF.push(this.cur);
            this.cur = top;
        }
    }

    void process58(){
        signStack.pop();
        signStack.push(Symple.StmMore);
        signStack.push(Symple.Stm);
        TreeNode node = new TreeNode();
        node.nodeKind = NodeIdentity.StmtK;
        TreeNode top = treeStack.peek();
        if(top.nodeKind==NodeIdentity.StmtK)
            top.sibling = node;
        else
            top.children[0] = node;
        treeStack.push(node);
        this.preF.push(this.cur);
        this.cur = top;
    }

    void process59(){
        signStack.pop();
        TreeNode node = treeStack.get(treeStack.size()-2);
        node.sibling = null;
        while(treeStack.peek()!=this.cur) treeStack.pop();
        treeStack.pop();
        this.cur = this.preF.pop();
    }

    void process60(Token token){
        signStack.pop();
        if(match(token,LexItem.SEMI)){
            tokenIndex++;
            signStack.push(Symple.StmList);
        }
        else{
            System.out.println("出错");
        }
    }

    void process61(){
        signStack.pop();
        signStack.push(Symple.ConditionalStm);
    }

    void process62(){
        signStack.pop();
        signStack.push(Symple.LoopStm);
    }

    void process63(){
        signStack.pop();
        signStack.push(Symple.InputStm);
    }

    void process64(){
        signStack.pop();
        signStack.push(Symple.OutputStm);
    }

    void process65(){
        signStack.pop();
        signStack.push(Symple.ReturnStm);
    }

    void process66(Token token){
        signStack.pop();
        if(match(token,LexItem.ID)){
            tokenIndex++;
            signStack.push(Symple.AssCall);
            TreeNode node = new TreeNode();
            node.nodeKind = NodeIdentity.ExpK;
            node.line = token.getLine();
            ExpAttr attr = new ExpAttr();
            node.attr = attr;
            attr.LexItem = LexItem.ID;
            attr.val = token.getSeminfo();
            TreeNode top = treeStack.peek();
            top.children[0]=node;
            treeStack.push(node);
            this.preF.push(this.cur);
            this.cur = top;
        }
        else{
            System.out.println("出错");
        }
    }

    void process67(){
        signStack.pop();
        signStack.push(Symple.AssignmentRest);
        this.cur.kind = SubIdentity.AssignK;

    }

    void process68(){
        signStack.pop();
        signStack.push(Symple.CallStmRest);
        this.cur.kind = SubIdentity.CallK;
    }

    void process69(){
        signStack.pop();
        signStack.push(Symple.Exp);
        signStack.push(LexItem.ASSIGN);
        signStack.push(Symple.VariMore);
    }

    void process70(Token token){
        signStack.pop();
        if(match(token,LexItem.IF)){
            tokenIndex++;
            signStack.push(LexItem.FI);
            signStack.push(Symple.StmList);
            signStack.push(LexItem.ELSE);
            signStack.push(Symple.StmList);
            signStack.push(LexItem.THEN);
            signStack.push(Symple.RelExp);
            TreeNode node = new TreeNode();
            node.nodeKind = NodeIdentity.StmtK;
            TreeNode top = treeStack.peek();
            top.children[0] = node;
            treeStack.push(node);
            this.preF.push(this.cur);
            this.cur = node;
        }
    }

    void process71(Token token){
        signStack.pop();
        if(match(token,LexItem.WHILE)){
            signStack.push(LexItem.ENDWH);
            signStack.push(Symple.StmList);
            signStack.push(LexItem.DO);
            signStack.push(Symple.RelExp);
            TreeNode node = new TreeNode();
            node.nodeKind = NodeIdentity.StmtK;
            TreeNode top = treeStack.peek();
            top.children[0] = node;
            treeStack.push(node);
            this.preF.push(this.cur);
            this.cur = node;
        }else{
            System.out.println("出错");
        }
    }

    void process72(Token token){
        signStack.pop();
        if(match(token,LexItem.READ)){
            tokenIndex++;
            token = wordAutomat.getToken(tokenIndex);
            if(match(token,LexItem.LPAREN)){
                signStack.push(Symple.InVar);
                TreeNode node = new TreeNode();
                node.nodeKind = NodeIdentity.StmtK;
                TreeNode top = treeStack.peek();
                top.children[0] = node;
                treeStack.push(node);
            }else{
                System.out.println("出错");
            }
        }else{
            System.out.println("出错");
        }
    }

    void process73(Token token){
        signStack.pop();
        if(match(token,LexItem.ID)){
            TreeNode top = treeStack.peek();
            top.kind = SubIdentity.ReadK;
            top.line = token.getLine();
            top.name.add(token.getSeminfo());
            tokenIndex++;
            token = wordAutomat.getToken(tokenIndex);
            if(match(token,LexItem.RPAREN)){
                tokenIndex++;
            }else{
                System.out.println("出错");
            }
        }else{
            System.out.println("出错");
        }
    }

    void process74(Token token){
        signStack.pop();
        if(match(token,LexItem.WRITE)){
            tokenIndex++;
            token = wordAutomat.getToken(tokenIndex);
            if(match(token,LexItem.LPAREN)){
                signStack.push(Symple.Exp);
                TreeNode node = new TreeNode();
                node.nodeKind = NodeIdentity.StmtK;
                TreeNode top = treeStack.peek();
                top.kind = SubIdentity.WriteK;
                top.children[0] = node;
                treeStack.push(node);
            }else{
                System.out.println("出错");
            }
        }else{
            System.out.println("出错");
        }
    }

    void process75(Token token){
        signStack.pop();
        if(match(token,LexItem.RETURN)){
            tokenIndex++;
        }
    }

    void process76(Token token){
        signStack.pop();
        if(match(token,LexItem.LPAREN)){
            tokenIndex++;
            signStack.push(Symple.ActParamList);

        }
    }

    void process77() {
        signStack.pop();
    }

    void process78(){
        signStack.pop();
        signStack.push(Symple.ActParamMore);
        signStack.push(Symple.Exp);
    }

    void process79(){
        signStack.pop();
    }

    void process80(Token token){
        signStack.pop();
        if(match(token,LexItem.COMMA)){
            tokenIndex++;
            signStack.push(Symple.ActParamList);

        }
    }

    void process81(){
        signStack.pop();
        signStack.push(Symple.OtherRelE);
        signStack.push(Symple.Exp);
    }

    void process82(){
        signStack.pop();
        signStack.push(Symple.Exp);
        signStack.push(Symple.CmpOp);
    }

    void process83(){
        signStack.pop();
        signStack.push(Symple.OtherTerm);
        signStack.push(Symple.Term);
    }

    void process84(Token token){
        Stack<TreeNode> nodes = transform();
        TreeNode top = treeStack.peek();
        if(nodes.size()==1) top.sibling = nodes.pop();
        while(treeStack.peek().sibling!=null) treeStack.pop();
//        treeStack.pop();
        this.cur = this.preF.pop();
        signStack.pop();
    }

    void process85(){
        signStack.pop();
        signStack.push(Symple.Exp);
        signStack.push(Symple.AddOp);
        TreeNode node = new TreeNode();
        node.nodeKind = NodeIdentity.ExpK;
        node.kind = SubIdentity.OpK;
        ExpAttr attr = new ExpAttr();
//        attr.LexItem = LexItem.PLUS;
        node.attr = attr;
        Exps.add(node);
    }

    void process86(){
        signStack.pop();
        signStack.push(Symple.OtherFactor);
        signStack.push(Symple.Factor);
    }

    void process87(Token token){
        signStack.pop();
//        TreeNode node = treeStack.get(treeStack.size()-2);
//        node.sibling = null;
//        Stack<TreeNode> nodes = transform();
//        TreeNode top = treeStack.peek();
//        if(nodes.size()==1) top.sibling = nodes.pop();
//        while(treeStack.peek()!=this.cur) treeStack.pop();
//        while(treeStack.peek().sibling!=null) treeStack.pop();
//        treeStack.pop();
//        this.cur = this.preF;
//        this.cur = this.preF.pop();
//        signStack.pop();
    }

    void process88(Token token){

    }

    void process89(Token token){
        signStack.pop();
        if(match(token,LexItem.LPAREN)){
            tokenIndex++;
            signStack.push(Symple.Exp);
            TreeNode node = Exps.get(Exps.size()-1);
//            node.nodeKind = NodeIdentity.ExpK;
//            TreeNode top = treeStack.peek();
//            top.children[1] = node;
//            this.preF.push(this.cur);
//            this.cur = top;
            node.nodeKind = NodeIdentity.ExpK;
            node.kind = SubIdentity.OpK;
            ExpAttr attr = new ExpAttr();
            attr.LexItem = LexItem.LPAREN;
            node.attr = attr;
//            Exps.add(node);
            expflag++;

            TreeNode Nid = new TreeNode();
            Nid.nodeKind = NodeIdentity.ExpK;
            ExpAttr eattr = new ExpAttr();
            Nid.attr = eattr;
            Exps.add(Nid);
        }else{
            System.out.println("出错");
        }
    }

    void process90(Token token){
        signStack.pop();
        if(match(token,LexItem.INTC)){
            tokenIndex++;
            TreeNode top = Exps.get(Exps.size()-1);
//            TreeNode top = treeStack.peek();
            ExpAttr attr = (ExpAttr) top.attr;
            if(attr.varKind == 2||attr.varKind==0){
                attr.val = token.getSeminfo();
                attr.LexItem = LexItem.INTC;
                top.kind = SubIdentity.ConstK;
                top.line = token.getLine();
            }
        }
    }

    void process91(){
        signStack.pop();
        signStack.push(Symple.Variable);
    }

    void process92(Token token){
        signStack.pop();
        if(match(token,LexItem.ID)){
            tokenIndex++;
            signStack.push(Symple.VariMore);
            TreeNode last = Exps.get(Exps.size()-1);
            ExpAttr attr = (ExpAttr) last.attr;
            attr.LexItem = LexItem.ID;
            attr.val = token.getSeminfo();
        }
    }

    void process93(Token token){
        if(match(token,LexItem.ASSIGN)){
            TreeNode top = treeStack.peek();
            top.kind = SubIdentity.IdK;
            ExpAttr attr = (ExpAttr) top.attr;
            attr.varKind = 1;
            TreeNode node = new TreeNode();
            node.nodeKind = NodeIdentity.ExpK;
            ExpAttr eattr = new ExpAttr();
            node.attr = eattr;
//            top.sibling = node;
            this.Exps.add(node);
//            treeStack.push(node);
            tokenIndex++;
            signStack.pop();
            signStack.pop();
        }
        else if(match(token,LexItem.PLUS)){
            signStack.pop();
        }
        else if(match(token,LexItem.RPAREN)){
            signStack.pop();
        }
        else if(match(token,LexItem.SEMI)){
            signStack.pop();
        }
//        tokenIndex++;
    }

    void process94(Token token){
        signStack.pop();
        if(match(token,LexItem.LMIDPAREN)){
            tokenIndex++;
            signStack.push(LexItem.RMIDPAREN);
            signStack.push(Symple.Exp);
            TreeNode top = treeStack.peek();
            ExpAttr attr = (ExpAttr) top.attr;
            attr.varKind = 2;

            TreeNode node = new TreeNode();
            node.nodeKind = NodeIdentity.ExpK;
            top.children[0] = node;
            treeStack.push(node);
            this.preF.push(this.cur);
            this.cur = top;
        }else{
            System.out.println("出错");
        }
    }

    void process95(Token token){
        signStack.pop();
        if(match(token,LexItem.DOT)){
            tokenIndex++;
            signStack.push(Symple.FieldVar);
            TreeNode top = treeStack.peek();
            ExpAttr attr = (ExpAttr) top.attr;
            attr.varKind = 3;

            TreeNode node = new TreeNode();
            node.nodeKind = NodeIdentity.ExpK;
            top.children[0] = node;
            treeStack.push(node);
            this.preF.push(this.cur);
            this.cur = top;
        }else{
            System.out.println("出错");
        }
    }

    void process96(){

    }

    void process97(){

    }

    void process98(){

    }

    void process99(){

    }

    void process100(){

    }

    void process101(Token token){
        signStack.pop();
        if(match(token,LexItem.PLUS)){
            TreeNode node = Exps.get(Exps.size()-1);
            ExpAttr attr = (ExpAttr) node.attr;
            attr.LexItem = LexItem.PLUS;
            tokenIndex++;
        }
        else {
            System.out.println("出错");
        }
    }

    void process102(Token token){
        if(match(token,LexItem.MINUS)){
            TreeNode node = Exps.get(Exps.size()-1);
            ExpAttr attr = (ExpAttr) node.attr;
            attr.LexItem = LexItem.MINUS;
        }
        else {
            System.out.println("出错");
        }
    }

    void process103(Token token){

    }

    void process104(Token token){

    }


}
