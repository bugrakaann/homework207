package com.mycompany.nameextractor;

import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.util.Span;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class App
{
    public static void main( String[] args )
    {
        String url = args[0];
        Connection connection = Jsoup.connect(url);
        String url_body;
        try {
            url_body = connection.get().body().text();

            InputStream modelToken = App.class.getResourceAsStream("/opennlp-en-ud-ewt-tokens-1.0-1.9.3.bin");
            InputStream modelPerson = App.class.getResourceAsStream("/en-ner-person.bin");
            TokenNameFinderModel modelT = new TokenNameFinderModel(modelPerson);
            TokenizerModel tokenizerModel = new TokenizerModel(modelToken);


            NameFinderME nameFinderME = new NameFinderME(modelT);
            Tokenizer tokenizer = new TokenizerME(tokenizerModel);

            String[] tokens= tokenizer.tokenize(url_body);

            Span[] names = nameFinderME.find(tokens);

            String[] namesA = new String[names.length];

            for (int i = 0; i < names.length; i++){
                namesA[i] = "";
                for (int k = names[i].getStart(); k < names[i].getEnd(); k++){
                    namesA[i] = namesA[i] + tokens[k];
                }
            }


            for(String name : namesA){
                System.out.println(name);
            }
        }
        catch (FileNotFoundException e){
            System.out.println("Model file not found");
            System.out.println(e.getMessage());
            return;
        }
        catch (IOException e){
            System.out.println(e.getMessage());
        }
    }
}
