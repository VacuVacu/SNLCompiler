package SyntaxAnalysis;

import entity.ConstValues;
import utils.FileReader;
import utils.SympleUtils;


import java.io.IOException;
import java.util.*;

import static java.sql.Types.NULL;

public class Syntax {

    private Map<Symple,Derivation> formulaList;  //生成式列表
    private Map<Symple,List<FormulaItem>> predictSet;  //predict集
    private Map<Symple,Set<Symple>> firstSet;   //first集
    private Map<Symple,Set<Symple>> followSet;  //follow集
    private Set<Symple> N_Set;   //非终极符
    private Set<Symple> T_Set;   //终极符
    private Symple start; //开始符
    private Map<SelectItem,List<Symple>> predictTable;   //分析表
    private Map<FormulaHead,Integer> formulaNum;


    public List<String> getFormula(String filepath) throws IOException {
        FileReader fileReader = new FileReader();
        String content = fileReader.loadContentOfFile(filepath);
        return Arrays.asList(content.split("\n"));
    }

    public Syntax(){}

    public Syntax(String path) throws IOException {
        List<String> fl = getFormula(path);
        formulaList = new TreeMap<>();
        firstSet = new TreeMap<>();
        followSet = new TreeMap<>();
        predictSet = new TreeMap<>();
        N_Set = new TreeSet<>();
        T_Set = new TreeSet<>();
        start = new Symple(ConstValues.N_SYMPLE,"Program");
        predictTable = new HashMap<>();
        formulaNum = new HashMap<>();
        int num = 1;
        for(String i:fl){
            Derivation d = new Derivation(parserFormula(i));
            formulaList.put(d.left,d);
            for(List<Symple> rights:d.getRight()){
                FormulaHead fhead = new FormulaHead(d.getLeft(),rights.get(0));
                formulaNum.put(fhead,num++);
                System.out.println(fhead.Left.content+"\t"+fhead.RightFirst.content+"\t"+(num-1));
            }
        }
        System.out.println();
    }

    //返回分析表
    public Map<SelectItem,List<Symple>> getPredictTable(){
        return predictTable;
    }

    //输出生成式
    public void displayFormula(List<String> fl){
        for(String i:fl){
            Derivation d = new Derivation(parserFormula(i));
            System.out.print(d.left.getContent()+" : ");
            int in=0;
            for(List<Symple> symples:d.right){
                for(Symple s:symples){
                    System.out.print(s.getContent()+" ");
                }
                if(in!=d.right.size()-1) System.out.print("\n"+d.left.getContent()+" : ");
                in++;
            }
            System.out.println();
        }
    }


    public void displayFirst(){
        System.out.println("FOLLOW SET");
        for (Symple symple : N_Set) {
            System.out.print(symple+"\t");
            Set<Symple> s = followSet.get(symple);
            for (Symple cur : s) {
                System.out.println(cur);
            }
        }
    }

    //处理从文件中的读取的文法
    public ReturnValue parserFormula(String line) {
        int cursor = 0;
        int state = 0;
        String token = "";
        List<Symple> symples = new ArrayList<>();
        List<Integer> index = new ArrayList<>();
        boolean head = false;
        while(cursor < line.length()){
            if(line.charAt(cursor) == ':' || (state==0&&line.charAt(cursor) == ' ')) {
                cursor++;
                continue;
            }
            if(state == 0 && line.charAt(cursor) == '<' && line.charAt(cursor+1) != ' ') state = 1;
            else if(state == 0 && line.charAt(cursor) != '<' && line.charAt(cursor) != '|') {
                state = 2;
                token+=line.charAt(cursor);
            }
            else if(state == 0 && line.charAt(cursor) == '<' && (line.charAt(cursor+1) == ' '||line.charAt(cursor+1)=='|')){
                state = 2;
                token += line.charAt(cursor);
            }
            else if(state == 1 && line.charAt(cursor) != '>') token += line.charAt(cursor);
            else if(state == 1 && line.charAt(cursor) == '>') {
                Symple symple = new Symple(ConstValues.N_SYMPLE,token);
                symples.add(symple);
//                System.out.println("非终结符: "+token);
                N_Set.add(symple);
                state = 0;
                token = "";
            }
            else if(state == 2 && line.charAt(cursor) != ' ' && line.charAt(cursor) != '|' && line.charAt(cursor) != '>'){
                token += line.charAt(cursor);
            }
            else if(state==2 &&!(line.charAt(cursor) != ' ' && line.charAt(cursor) != '|' && line.charAt(cursor) != '>')) {
                token=token.toLowerCase(Locale.ROOT);
                Symple symple;
                if(token.equals("$")) {
                    symple = new Symple(ConstValues.EMPTY, token);
                    symple.empty = true;
                }
                else  {
                    symple = new Symple(ConstValues.T_SYMPLE, token);
                }
                symples.add(symple);
                T_Set.add(symple);
//                System.out.println("终结符: "+token);

                state = 0;
                token = "";
                if(line.charAt(cursor)=='|'||line.charAt(cursor)=='>') cursor--;
            }
            else if(line.charAt(cursor)=='|'&& state==0) index.add(symples.size());
            cursor++;
        }
        if (token.length()>0&&line.charAt(cursor-1)!='>'){
            token=token.toLowerCase(Locale.ROOT);
            Symple symple;
            if(token.equals("$")){
                symple = new Symple(ConstValues.EMPTY, token);
                symple.empty = true;
            }
            else{
                symple = new Symple(ConstValues.T_SYMPLE, token);
            }
            symples.add(symple);
            T_Set.add(symple);
            //            System.out.println("终结符: "+token);
        }
        else if(token.length()>0){
            Symple symple = new Symple(ConstValues.N_SYMPLE,token);
            symples.add(symple);
//            System.out.println("非终结符: "+token);
            N_Set.add(symple);
        }
        return new ReturnValue(symples,index);
    }

    //获取first集
    public void getFirst(){
        for (Symple symple : N_Set) {
            Derivation d = this.formulaList.get(symple);
            List<List<Symple>> list = d.getRight();
            for (List<Symple> lt : list) {
                boolean flag = false;
                for (Symple s : lt) {
                    Set<Symple> set = firstSet.get(s);
                    if (set == null) set = new HashSet<>();
                    flag = deduceFirst(set, symple, s);
                    if (flag) {
                        for (Symple item : set) {
                            System.out.println(symple + "\t" + item);
                        }
                        break;
                    }
                }
            }
        }
    }

    //推理first集
    private boolean deduceFirst(Set<Symple> set,Symple cur,Symple process) {
        if(process.getIdentity()==ConstValues.T_SYMPLE|| process.empty){
            set.add(process);
            firstSet.put(cur,set);
            return  true;
        }
        else if(process.getIdentity()==ConstValues.N_SYMPLE){
            List<List<Symple>> list = formulaList.get(process).getRight();
            for(List<Symple> lt: list){
                Symple tmp = lt.get(0);
                deduceFirst(set,cur,tmp);
            }
        }
        return true;
    }

    //获得follow集
    public void getFollow(){
        for(Symple symple : N_Set){
            TreeSet<Symple> tmp = new TreeSet<>();
            followSet.put(symple,tmp);
        }
        for (Symple cur : N_Set) {
            Set<Symple> keySet = formulaList.keySet();
            for (Symple keySymple : keySet) {
                List<List<Symple>> formula = formulaList.get(keySymple).getRight();
                for (List<Symple> itemlst : formula) {
                    Set<Symple> itemSet = followSet.get(cur);
                    deduceFollow(cur,cur, keySymple,itemlst,itemSet);
                }
            }
        }
    }

    //推理follow集
    private void deduceFollow(Symple cur,Symple item,Symple keyItem,List<Symple> itemFormList,Set<Symple> itemRes){
        if(item.equals(start)){
            itemRes.add(new Symple(ConstValues.T_SYMPLE,"#"));
            followSet.put(cur,itemRes);
        }
        if(SympleUtils.containsAb(T_Set,itemFormList,item)){
            Symple aLastSymple = SympleUtils.getAlastSymple(itemFormList,item);
            itemRes.add(aLastSymple);
            followSet.put(cur,itemRes);
        }
        if(SympleUtils.containsAB(N_Set,itemFormList,item)){
            Symple aLastSymple = SympleUtils.getAlastSymple(itemFormList,item);
            Set<Symple> set = firstSet.get(aLastSymple);
            itemRes.addAll(set);
            if(set.contains(new Symple(ConstValues.EMPTY,"$")))
                itemRes.add(new Symple(ConstValues.T_SYMPLE,"#"));
            itemRes.remove(new Symple(ConstValues.EMPTY,"$"));
            followSet.put(cur,itemRes);
            if(SympleUtils.containsbAbIsNULL(N_Set,itemFormList,item,formulaList)){
                Symple tempSymple = SympleUtils.getAlastSymple(itemFormList,item);
                if(!keyItem.equals(item)){
                    Set<Symple> keySet = formulaList.keySet();
                    for(Symple keySymple : keySet){
                        List<List<Symple>> symples = formulaList.get(keySymple).getRight();
                        for(List<Symple> lst : symples){
                            deduceFollow(cur,keySymple,keyItem,lst,itemRes);
                        }
                    }
                }
            }
        }

        if(SympleUtils.containsbA(N_Set,itemFormList,item)){
            if(!keyItem.equals(item)){
                Set<Symple> keySet = formulaList.keySet();
                for(Symple keySymple : keySet){
                    List<List<Symple>> symples = formulaList.get(keySymple).getRight();
                    for(List<Symple> lst : symples){
                        deduceFollow(cur,keySymple,keyItem,lst,itemRes);
                    }
                }
            }
        }
    }

    //推理predict集
    public void deducePredict(){
        Set<Symple> keySet = formulaList.keySet();
        for(Symple symple : keySet){
            List<List<Symple>> formulas = formulaList.get(symple).getRight();
            List<FormulaItem> formulaItemList = new ArrayList<>();
            for(List<Symple> formula:formulas){
                Set<Symple> preSet = new TreeSet<>();
                if(SympleUtils.isEmptyStart(formula)){
                    preSet = followSet.get(symple);
                    preSet.remove(new Symple(ConstValues.EMPTY,"$"));
                    formulaItemList.add(new FormulaItem(formula,preSet));
                }
                if(SympleUtils.isTStart(formula)){
                    preSet.add(formula.get(0));
                    preSet.remove(new Symple(ConstValues.EMPTY,"$"));
                    formulaItemList.add(new FormulaItem(formula,preSet));
                }
                if(SympleUtils.isNStart(formula)){
                    preSet = firstSet.get(symple);
                    preSet.remove(new Symple(ConstValues.EMPTY,"$"));
                    formulaItemList.add(new FormulaItem(formula,preSet));
                }
                predictSet.put(symple,formulaItemList);
            }

        }
    }

    //输出predict集
    public void displayPredict(){
        System.out.println("*****************Predict****************");
        for(Symple symple:N_Set){
            List<FormulaItem> fl = predictSet.get(symple);
            for(FormulaItem f:fl){
                System.out.print(symple.content+"->");
                for(Symple s:f.formula){
                    System.out.print(s.getContent()+" ");
                }
                System.out.print("\t\tPredict-Set:{");
                for(Symple s:f.select){
                    System.out.print(" "+s.content);
                }
                System.out.println('}');
            }

        }
    }

    //找到需要的生成式 peek当前处理的非终极符 cur为当前需要匹配的终极符
    public List<Symple> finduseExp(Symple peek,Symple cur){
        try{
            List<FormulaItem> formulaItems = predictSet.get(peek);
            List<Symple> matchEmpty=null;
            for(FormulaItem f:formulaItems){
                for(Symple s:f.select){
                    if(s.content.equals(cur.content)&&s.getIdentity()==cur.getIdentity()) return f.formula;
                    if(s.content.equals("#")) matchEmpty=f.formula;
                }
            }
            if(matchEmpty!=null) return matchEmpty;
        }catch (Exception e){
            return null;
        }
        return null;
    }

    //生成predict集
    public void formPredictTable(){
        System.out.println("*****************************PredictTable");
        for(Symple T_symple:T_Set){
            for(Symple N_symple:N_Set){
                List<Symple> symples=finduseExp(N_symple,T_symple);
                if(symples!=null) {
                    predictTable.put(new SelectItem(T_symple, N_symple), symples);
                    System.out.print(T_symple.content + "," + N_symple.content + "\tformula\t");
                    for (Symple s : symples) {
                        System.out.print(s.content + " ");
                    }
                    System.out.println();
                }
            }
        }
    }

    //根据formulaNum表返回编号
    public int getFormulaNum(Symple left,Symple rightFirst){
        FormulaHead formulaHead = new FormulaHead(left,rightFirst);
        if(formulaNum.containsKey(formulaHead))
            return formulaNum.get(formulaHead);
        return -1;
    }

}

//辅助类 用于生成Derivation类对象
class ReturnValue {
    List<Symple> symples;
    List<Integer> index;

    public ReturnValue(List<Symple> symples, List<Integer> index) {
        this.symples = symples;
        this.index = index;
    }
}

//辅助类 用于生成predict集
class FormulaItem {
    List<Symple> formula;
    Set<Symple> select;
    public FormulaItem(List<Symple> formula,Set<Symple> select){
        this.formula=formula;
        this.select=select;
    }
    public FormulaItem(){}

}

//辅助类 用于分析表
class SelectItem {
    Symple T_symple; //终结符
    Symple N_symple; //非终结符
    public SelectItem(){}
    public SelectItem(Symple t_symple, Symple n_symple) {
        T_symple = t_symple;
        N_symple = n_symple;
    }
}

//辅助类 用于找到生成式编号
class FormulaHead {
    Symple Left;
    Symple RightFirst;
    public FormulaHead(Symple left, Symple right) {
        Left = left;
        RightFirst = right;
    }
}
