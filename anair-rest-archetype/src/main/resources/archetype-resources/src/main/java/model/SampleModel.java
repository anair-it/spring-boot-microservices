#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.model;

import java.io.Serializable;

public class SampleModel implements Serializable {

    private String input;

    public String getInput(){
        return input;
    }

    public void setInput(String input){
        this.input = input;
    }
}