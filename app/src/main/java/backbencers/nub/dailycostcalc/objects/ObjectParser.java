package backbencers.nub.dailycostcalc.objects;

import java.text.SimpleDateFormat;
import java.util.Date;

import backbencers.nub.dailycostcalc.model.Debit;

/**
 * Created by dutchman on 4/26/17.
 */

public class ObjectParser {

    private String text;
    private String category;

    public ObjectParser(String category, String text){

        this.setText(text.trim());
        this.category = category;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Debit parse(){

        Debit debit = null;

        //"Description        Amount\nBiriani         125.00\nBurhani         30.00\nTotal        155.00"

        String[] tokens = this.text.toUpperCase().split("\n");

        String description = "";
        String total = "0.00";

        for(String token : tokens){

            if(token.contains("TOTAL")){

                String[] tTokens = token.trim().split(" ");
                total = tTokens[tTokens.length-1];


            } else{

                description += token + "\n";
            }

        }
        String date = new SimpleDateFormat("MMM dd, yyyy").format(new Date());

        debit = new Debit(date,category, description,Double.parseDouble(total));


        return debit;
    }
}
