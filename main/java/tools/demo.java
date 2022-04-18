package tools;

import SyntaxAnalysis.Analyszer;
import SyntaxAnalysis.Syntax;
import entity.ConstValues;
import entity.TokenElement;
import utils.FileReader;
import WordAnalysis.WordAutomat;

import java.io.IOException;
import java.util.List;

public class demo {
    public static void main(String[] args) throws IOException {
        FileReader fileReader = new FileReader();
        String code = fileReader.loadContentOfFile("src/main/resources/other/testcode.txt");
        WordAutomat automat = new WordAutomat();
        List<TokenElement> tokenList = automat.getTokenList(code);
        tokenList.forEach((e)->{
            System.out.println(e.getLineNum()+"\t"+ConstValues.wordType.get(e.getItemIdentifier())+"\t"+e.getSemanticsInfo());
        });
        Syntax syntax = new Syntax("src/main/resources/other/formula.txt");
        syntax.getFirst();
        syntax.getFollow();
        syntax.deducePredict();
        syntax.displayFirst();
        syntax.displayPredict();
        syntax.formPredictTable();
        Analyszer analyszer = new Analyszer(syntax);

//        List<String> formulaList = syntax.getFormula("src/main/resources/other/formula.txt");
//        syntax.displayFormula(formulaList);

    }
}
